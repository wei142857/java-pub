package worker.exchange;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlRow;

import models.EShopOrders;
import models.EShopProduct;
import models.SmartBounty;
import models.SmartBountyEshop;
import play.Logger;
import util.EntityConvert;
import util.GlobalDBControl;
import util.StringUtil;

/**
 * 订单超过30分钟以后将订单取消的线程
 * 将订单的状态改为失效
 * 将用户扣除的奖励金返回给用户
 * 将库存恢复
 * @author 陈宏亮
 *
 */
public class CancleOrderThread extends Thread{
	
	private static Integer TIME = 3;
	
	public CancleOrderThread(){
		Logger.info("CancleOrderThread start...");
		this.start();
	}
	
	public void run(){
		while(true){
			Ebean.beginTransaction();
			try{
				//查看订单是否有超时的订单
				List<EShopOrders> ordersList = selectOverdueOrder();
				if(ordersList!=null){
					for(EShopOrders order:ordersList){
						//将订单的状态改为失效(status=-1)
						updateOrderStatus(order);
						//将该笔订单的奖励金退回给用户
						refundBvalueByOrderTimeOut(order);
						//将库存恢复
						recoverEshopProStock(order);
					}
					Ebean.commitTransaction();
				}
			}catch(Exception e){
				Ebean.rollbackTransaction();
				util.LogUtil.writeLog("退回奖励金时出错,事务回滚,错误原因:e:"+e.getMessage(), "CancleOrderThreadLog");
			}finally{
				Ebean.endTransaction();
			}
			StringUtil.sleep(1000);
		}
	}
	
	//将扣除的库存恢复
	public static void recoverEshopProStock(EShopOrders order){
		if(order!=null&&order.getPidd()!=null&&order.getAmount()!=null&&order.getAmount()>0){
			EShopProduct product = selectEshopPro(order.getPidd());
			if(product!=null){
				String sql = "update eshop_product set stock=:stock where 1=1 and idd=:idd";
				Ebean.getServer(GlobalDBControl.getDB())
				.createSqlUpdate(sql)
				.setParameter("idd", order.getPidd())
				.setParameter("stock", order.getAmount()+product.getStock())
				.execute();
				util.LogUtil.writeLog("成功将产品Idd为:"+order.getIdd()+"的库存恢复", "CancleOrderThreadLog");
			}
		}
	}
	
	//根据pid 去eshop_product表中查找出该条产品记录
	public static EShopProduct selectEshopPro(Integer pidd){
		if(pidd!=null){
			EShopProduct product = Ebean.getServer(GlobalDBControl.getDB())
				.find(EShopProduct.class)
				.where().eq("idd",pidd)
				.findUnique();
			if(product!=null){
				return product;
			}
		}
		return null;
	}
	
	//将订单失效后未消耗的奖励金退回给用户,并生成一条奖励金的退回记录
	private static void refundBvalueByOrderTimeOut(EShopOrders order){
		//去smart_bounty_eshop表中查找该笔订单用户消耗的奖励金记录
		List<SmartBountyEshop> bountyEshopList = selectBountyEShopByOrderIdd(order);
		if(bountyEshopList!=null){
			for(SmartBountyEshop bountyEshop:bountyEshopList){
				//去smart_bounty中恢复奖励金记录
				returnBountyToUser(bountyEshop);
			}
			//生成一条奖励金退回记录
			createReturnBountyRecord(order);
		}
	}
	
	//生成一条奖励金退回记录
	public static void createReturnBountyRecord(EShopOrders order){
		SmartBounty bounty = new SmartBounty();
		bounty.setUserid(order.getUserid());
		bounty.setAddtime(new Date());
		bounty.setBvalue(order.getBvalue());
		bounty.setTitile("购买"+order.getTitle()+"的订单超时退回奖励金");
		bounty.setAct(1);
		bounty.setCurrentbvalue(0);
		Ebean.getServer(GlobalDBControl.getDB()).save(bounty);
		util.LogUtil.writeLog("成功生成一条奖励金退回记录", "CancleOrderThreadLog");
	}
	
	//去smart_bounty中恢复奖励金记录
	private static void returnBountyToUser(SmartBountyEshop bountyEshop){
		if(bountyEshop!=null&&bountyEshop.getBvalue()!=null&&bountyEshop.getBid()!=null){
			String sql = "update smart_bounty set currentbvalue=:currentbvalue where 1=1 and idd=:idd";
			Ebean.getServer(GlobalDBControl.getDB())
				.createSqlUpdate(sql)
				.setParameter("currentbvalue", bountyEshop.getBvalue())
				.setParameter("idd", bountyEshop.getBid())
				.execute();
			util.LogUtil.writeLog("成功将smart_bounty表中idd为:"+bountyEshop.getBid()+"的记录的currentbvalue值恢复为:"+bountyEshop.getBvalue(), "CancleOrderThreadLog");
		}
	}
	
	//去smart_bounty_eshop表中查找该笔订单用户消耗的奖励金记录
	private static List<SmartBountyEshop> selectBountyEShopByOrderIdd(EShopOrders order){
		List<SmartBountyEshop> bountyEshopList = Ebean.getServer(GlobalDBControl.getDB())
			.find(SmartBountyEshop.class)
			.where().eq("oid", order.getIdd())
			.findList();
		if(bountyEshopList!=null&&bountyEshopList.size()>0){
			return bountyEshopList;
		}
		return null;
	}
	
	//去eshop_orders表中将订单的状态改为失效状态
	private static void updateOrderStatus(EShopOrders order){
		if(order!=null){
			String sql = "update eshop_orders set status = -1 where idd=:idd and status = 1";
			Ebean.getServer(GlobalDBControl.getDB())
				.createSqlUpdate(sql)
				.setParameter("idd", order.getIdd())
				.execute();
			util.LogUtil.writeLog("将idd:为"+order.getIdd()+"的订单状态改为失效状态", "CancleOrderThreadLog");
		}
	}
	
	
	//去eshop_orders表中查找超时的订单并返回
	private static List<EShopOrders> selectOverdueOrder(){
		Date time = reduceThirtyMin();	//返回30分钟前的时间
		//查找订单超过30分钟并且状态为未支付的
		String sql = "select * from eshop_orders where status=1 and addtime<:time";
		List<SqlRow> rowList = Ebean.getServer(GlobalDBControl.getDB())
			.createSqlQuery(sql)
			.setParameter("time", time)
			.findList();
		if(rowList!=null&&rowList.size()>0){
			List<EShopOrders> ordersList = EntityConvert.convert(rowList,EShopOrders.class);
			util.LogUtil.writeLog("查找到"+ordersList.size()+"笔失效的订单", "CancleOrderThreadLog");
			return ordersList;
		}
		return null;
	}
	
	//返回当前时刻的time分钟前的时间
	private static Date reduceThirtyMin(){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -TIME);
		return cal.getTime();
	}
}