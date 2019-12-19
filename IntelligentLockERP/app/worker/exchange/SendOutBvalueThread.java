package worker.exchange;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.avaje.ebean.Ebean;

import controllers.SysConfigAction;
import models.EShopOrders;
import models.SmartBounty;
import play.Logger;
import util.GlobalDBControl;
import util.StringUtil;

/**
 * 派发奖励金的线程
 * 去数据库中查询 订单支付成功 且 支付时间超过 十天 并且没有退货
 * 给推荐人 发放奖励金
 * @author 陈宏亮
 *
 */
public class SendOutBvalueThread extends Thread{
	
	static String ShareEShopProduct_sendOutBounty = SysConfigAction.findSysconfig("智能锁", "分享有礼发放奖励金个数");
	
	public SendOutBvalueThread(){
		Logger.info("SendOutBvalueThread start ..");
		this.start();
	}
	
	public void run(){
		while(true){
			Ebean.beginTransaction();
			try{
				List<EShopOrders> orderList = disposeEShopOrders();
				if(orderList!=null&&orderList.size()>0){ //有要发放奖励金的订单
					for(EShopOrders order:orderList){
						Integer userid = Integer.parseInt(order.getSource());
						if(order.getUserid()!=null&&order.getUserid()!=userid){
							//增加一条该用户的奖励金收入信息
							boolean result = addSmartBounty(userid,ShareEShopProduct_sendOutBounty);
							if(result){
								//将分享奖励金个数set到订单中的shareBvalue
								UpdateShareBvalue(order.getIdd(),ShareEShopProduct_sendOutBounty);
								//将该条订单的award改为1
								UpdateAward_1(order.getIdd());
							}
						}else{
							//将该条订单的award改为1
							UpdateAward_1(order.getIdd());
						}
					}
					Ebean.commitTransaction();
				}
			}catch(Exception e){
				Ebean.rollbackTransaction();
				util.LogUtil.writeLog("退回奖励金时出错,事务回滚,错误原因:e:"+e.getMessage(), "SendOutBvalueThreadLog");
			}finally{
				Ebean.endTransaction();
			}
			StringUtil.sleep(1000);
		}
	}
	
	//生成一条分享有礼的奖励金奖励记录
	public static boolean addSmartBounty(Integer userid,String bounty){
		if(userid!=null&&bounty!=null){
			Integer nbounty = Integer.parseInt(bounty);
			SmartBounty bountyRecord = new SmartBounty();
			bountyRecord.setUserid(userid);
			bountyRecord.setAct(1);
			bountyRecord.setAddtime(new Date());
			bountyRecord.setBvalue(nbounty);
			bountyRecord.setCurrentbvalue(nbounty);
			bountyRecord.setOvertime(addOneYear(bountyRecord.getAddtime(),1));
			bountyRecord.setTitile("分享有礼奖励");
			Ebean.getServer(GlobalDBControl.getDB()).save(bountyRecord);
			util.LogUtil.writeLog("成功生成一条分享有礼的奖励金记录,userid:"+userid, "SendOutBvalueThreadLog");
			return true;
		}else{
			util.LogUtil.writeLog("奖励金配置项配置bounty:"+bounty+"有误或是source中没获取到userid:"+userid, "SendOutBvalueThreadLog");
			return false;
		}
	}
	
	
	//处理 eshop_orders表中的数据,将合理的数据返回
	public static List<EShopOrders> disposeEShopOrders(){
		List<EShopOrders> orderList = selEShopOrdersByStatus_0();
		List<EShopOrders> newList = new ArrayList<EShopOrders>();
		if(orderList!=null){
			for(EShopOrders order:orderList){
				if(disposeUpdateTime(order.getUpdatetime())){
					newList.add(order);
				}
			}
		}
		return newList;
	}
	
	//去eshop_orders表中查看 订单支付成功 已经发货 并且没有发放奖励金 的数据
	public static List<EShopOrders> selEShopOrdersByStatus_0(){
		List<EShopOrders> orderList = Ebean.getServer(GlobalDBControl.getDB())
				.find(EShopOrders.class)
				.where().eq("status", 0)
				.eq("expressstatus", 0)
				.eq("award", 0)
				.isNotNull("source")
				.findList();
		if(orderList!=null&&orderList.size()>0){
			return orderList;
		}
		return null;
	}
	
	//去eshop_orders表中将award的值改为1
	public static void UpdateAward_1(Integer idd){
		if(idd!=null){
			String sql = "update eshop_orders set award=1 where idd=:idd";
			Ebean.getServer(GlobalDBControl.getDB()).createSqlUpdate(sql)
				.setParameter("idd", idd)
				.execute();
			util.LogUtil.writeLog("成功将id为:"+"\t"+idd+"订单award改为1", "SendOutBvalueThreadLog");
		}
	}
	
	//去eshop_orders表中将award的值改为1
	public static void UpdateShareBvalue(Integer idd,String shareBvalue){
		if(idd!=null&&shareBvalue!=null){
			Integer nbounty = Integer.parseInt(shareBvalue);
			String sql = "update eshop_orders set shareBvalue=:shareBvalue where idd=:idd";
			Ebean.getServer(GlobalDBControl.getDB()).createSqlUpdate(sql)
				.setParameter("idd", idd)
				.setParameter("shareBvalue", nbounty)
				.execute();
			util.LogUtil.writeLog("成功将id为:"+"\t"+idd+"订单shareBvalue设置为:"+shareBvalue, "SendOutBvalueThreadLog");
		}
	}
	
	//传入一个时间,将该时间设置为第二天0时 再+10天
	public static boolean disposeUpdateTime(Date updateTime){
		if(updateTime!=null){
			Date now = new Date();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(updateTime);
			calendar.add(Calendar.DAY_OF_YEAR, 11);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			if(now.getTime()>=calendar.getTime().getTime()){
				return true;
			}
		}
		return false;
	}
	
	//传入一个时间 将该时间增加一年 返回一年以后的date
	public static Date addOneYear(Date addtime,int addyear){
		Calendar cal = Calendar.getInstance();
		cal.setTime(addtime);
		cal.add(Calendar.YEAR, addyear);
		return cal.getTime();
	}
	
}
