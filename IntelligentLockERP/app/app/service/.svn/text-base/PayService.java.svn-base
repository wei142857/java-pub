package app.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import models.EShopConsignee;
import models.EShopOrders;
import models.EShopProduct;
import models.SmartAppUser;
import models.SmartBounty;
import models.SmartBountyEshop;
import models.SmartVip;
import util.GlobalDBControl;
import Service.unicom.util.AESUtil;
import app.action.EShopShare;

import com.avaje.ebean.Ebean;

public class PayService {
	//根据consigneeId和userid 去eshop_consignee中检查该地址是否存在
	public static EShopConsignee selectAddressByIDD(Integer consigneeId,Integer userid){
		if(consigneeId!=null&&userid!=null&&consigneeId!=0){
			EShopConsignee consignee = Ebean.getServer(GlobalDBControl.getDB())
				.find(EShopConsignee.class)
				.where().eq("idd", consigneeId)
				.eq("userid", userid)
				.findUnique();
			if(consignee!=null){
				return consignee;
			}
		}
		return null;
	}
	//根据userid 去smart_vip表中查看该用户是否是会员(0:会员 1:过期 2:非会员)
	public static Integer checkIsVip(Integer userid){
		if(userid != null){
			SmartVip vip = VipService.selectSmartVipByUserid(userid);
			//判断是否有该用户(即该用户是否是会员)
			if(vip!=null){
				if(!VipService.checkVipTime(vip.getOvertime())){
					return 0;	//会员
				}else{
					return 1;	//过期
				}
			}
			return 2;		//非会员
		}
		return null;
	}
	
	//去eshop_product中将库存扣除对应的amount数量 (stock:为原库存)
	public static void redeceEshopProductStock(Integer pid,Integer stock,Integer amount){
		if(amount!=null&&amount!=0){
			StringBuffer sql = new StringBuffer("update eshop_product set stock=:nstock where 1=1");
			sql.append(" and idd=:pid");
			Ebean.getServer(GlobalDBControl.getDB())
				.createSqlUpdate(sql.toString())
				.setParameter("nstock", stock-amount)
				.setParameter("pid", pid)
				.execute();
			util.LogUtil.writeLog("(redeceEshopProductStock方法)成功将pid为:"+pid+"的产品库存减少amount:"+amount+"个", "EShopLog");
		}
	}
	
	//生成订单	(这里 支付成功时间(updatetime) 和 使用奖励金个数(bvalue)不设置,后面奖励金扣除后再进行设置)
	public static EShopOrders createOrder(SmartAppUser user,EShopProduct product,EShopConsignee consignee,Integer isVip,Integer amount,String outTradeNo,String source){
		if(user!=null&&product!=null&&consignee!=null&&isVip!=null&&amount!=null&&amount>0&&outTradeNo!=null){
			EShopOrders order = new EShopOrders();
			order.setUserid(user.getIdd());
			order.setProvince(user.getProv());
			order.setCity(user.getCity());
			order.setPidd(product.getIdd());
			order.setTitle(product.getTitle());
			order.setSubtitle(product.getSubtitle());
			order.setSmallimgurl(product.getSmallimgurl());
			order.setAmount(amount);
			order.setAddtime(new Date());
			order.setPcsaleprice(product.getPcsaleprice());//成本价
			//暂时设置的价格,稍后会设置为抵扣奖励金后的价格
			if(isVip==0){
				order.setPrice(product.getVipprice());
				order.setMoney(new BigDecimal(product.getVipprice()+"").multiply(new BigDecimal(amount)).doubleValue());//是会员(会员价)
			}else{ 
				order.setPrice(product.getSaleprice());
				order.setMoney(new BigDecimal(product.getSaleprice()+"").multiply(new BigDecimal(amount)).doubleValue());//非会员(原价)
			}
			//设置会员状态
			if(isVip==0)
				order.setVipstatus(0);//会员
			else if(isVip==1)
				order.setVipstatus(1);//会员过期
			else if(isVip==2)
				order.setVipstatus(2);//未开通
			//order.setUpdatetime(); //支付成功的时间
			//order.setBvalue(); //扣除的奖励金个数
			order.setStatus(1);	//状态(0:成功 1:待支付 -1:失败/超时)
			order.setPrepayid(outTradeNo);
			order.setConsigneename(consignee.getName());
			order.setConsigneearea(consignee.getArea());
			order.setConsigneeaddress(consignee.getAddress());
			order.setConsigneephone(consignee.getPhone());
			order.setExpressstatus(1);
			order.setAward(0);	//设置奖励金还未发放
			if(source!=null){
				try {
					source = AESUtil.decrypt(source, EShopShare.shareEShopMainInfo_ASE);
				} catch (Exception e) {
					// TODO Auto-generated catch block
				}
				order.setSource(source);
			}
			
			//将订单对象返回 做后续处理
			return order;
		}
		return null;
	}
	
	//扣除一部分奖励金的方法(参数bvalue:购买商品总共所需的奖励金)
	public static List<SmartBountyEshop> payAPartOfBvalue(Integer userid,Integer bvalue,String productName){
		if(userid!=null&&bvalue!=null&&bvalue>0){
			List<SmartBountyEshop> bountyEshopList = new ArrayList<SmartBountyEshop>();
			List<SmartBounty> bountyList = ProductService.selectSmartBountyByUserid_Act_Idd(userid);
			Integer recordBvalue = bvalue;		//总共需要扣除奖励金的总数
			if(bountyList!=null){
				Ebean.beginTransaction();
				try{
					for(SmartBounty bounty:bountyList){
						SmartBountyEshop bountyEshop = new SmartBountyEshop();
						//当前的currentbvalue值和recordBvalue比较
						if(recordBvalue>0 && recordBvalue>bounty.getCurrentbvalue()){
							bountyEshop.setBid(bounty.getIdd());
							bountyEshop.setBvalue(bounty.getCurrentbvalue());
							bountyEshop.setAddtime(new Date());
							bountyEshopList.add(bountyEshop);
							//将当前记录的currentbvalue的值改为0
							ProductService.updateCurrentBvalueByIdd(bounty.getIdd(),0);
							recordBvalue -= bounty.getCurrentbvalue();
							util.LogUtil.writeLog("userid:"+ userid +"的用户修改smart_bounty表中idd:"+ bounty.getIdd() +"的Currentbvalue值(值为"+ 0 +")成功", "EShopLog");	//修改成功
						}else if(recordBvalue>0 && recordBvalue==bounty.getCurrentbvalue()){
							bountyEshop.setBid(bounty.getIdd());
							bountyEshop.setBvalue(bounty.getCurrentbvalue());
							bountyEshop.setAddtime(new Date());
							bountyEshopList.add(bountyEshop);
							//将当前记录的currentbvalue的值改为0
							ProductService.updateCurrentBvalueByIdd(bounty.getIdd(),0);
							recordBvalue -= bounty.getCurrentbvalue();
							util.LogUtil.writeLog("userid:"+ userid +"的用户修改smart_bounty表中idd:"+ bounty.getIdd() +"的Currentbvalue值(值为"+ 0 +")成功", "EShopLog");	//修改成功
							createBountyPayRecord(userid,bvalue,productName);
							util.LogUtil.writeLog("userid:"+ userid +"的用户购买"+productName+"生成记录成功", "EShopLog");
							Ebean.commitTransaction();
							return bountyEshopList;
						}else{	//bounty.getCurrentbvalue()>recordBvalue
							bountyEshop.setBid(bounty.getIdd());
							bountyEshop.setBvalue(recordBvalue);
							bountyEshop.setAddtime(new Date());
							bountyEshopList.add(bountyEshop);
							ProductService.updateCurrentBvalueByIdd(bounty.getIdd(),bounty.getCurrentbvalue()-recordBvalue);
							recordBvalue -= bounty.getCurrentbvalue();//为负值
							util.LogUtil.writeLog("userid:"+ userid +"的用户修改smart_bounty表中idd:"+ bounty.getIdd() +"的Currentbvalue值(值为"+ (bounty.getCurrentbvalue()-recordBvalue) +")成功", "EShopLog");	//修改成功
							//在smart_bounty中生成一条 话费记录
							createBountyPayRecord(userid,bvalue,productName);
							util.LogUtil.writeLog("userid:"+ userid +"的用户购买"+productName+"生成记录成功", "EShopLog");
							Ebean.commitTransaction();
							return bountyEshopList;
						}
					}
				}catch(Exception e){	//事务回滚
					Ebean.rollbackTransaction();
					util.LogUtil.writeLog("userid:"+ userid +"的用户购买"+productName+"时出错，事务回滚，错误原因:e:"+e.getMessage(), "EShopLog");
					return null;
				}finally{
					Ebean.endTransaction();//事务关闭
				}
			}else{
				util.LogUtil.writeLog("传入的userid:"+ userid +"不合法！", "EShopLog");
				return null;
			}
		}else{
			util.LogUtil.writeLog("传入的userid:"+ userid +",bvalue:"+ bvalue + "productName:" + productName + "的某个值为null！", "EShopLog");
			return null;
		}
		return null;
	}
	
	//扣除全部奖励金的方法(参数bvalue:购买商品总共所需的奖励金)
	public static List<SmartBountyEshop> payAllBvalue(Integer userid,Integer bvalue,String productName){
		if(userid!=null&&bvalue!=null&&bvalue>0){
			try{
				Ebean.beginTransaction();
				List<SmartBountyEshop> bountyEshopList = new ArrayList<SmartBountyEshop>();
				List<SmartBounty> bountyList = ProductService.selectSmartBountyByUserid_Act_Idd(userid);
				if(bountyList!=null){
					for(SmartBounty bounty:bountyList){
						//记录(smart_bounty_eshop)奖励金的消耗
						SmartBountyEshop bountyEshop = new SmartBountyEshop();
						bountyEshop.setBid(bounty.getIdd());
						bountyEshop.setBvalue(bounty.getCurrentbvalue());
						bountyEshop.setAddtime(new Date());
						bountyEshopList.add(bountyEshop);
						//将当前记录的currentbvalue的值改为0
						ProductService.updateCurrentBvalueByIdd(bounty.getIdd(),0);
					}
					//生成(smart_bounty)一条奖励金消耗记录
					createBountyPayRecord(userid,bvalue,productName);
					Ebean.commitTransaction();
					return bountyEshopList;
				}
			}catch(Exception e){
				Ebean.rollbackTransaction();
				util.LogUtil.writeLog("userid:"+ userid +"的用户购买"+productName+"时出错，事务回滚，错误原因:e:"+e.getMessage(), "EShopLog");
				return null;
			}finally{
				Ebean.endTransaction();
			}
		}else{
			util.LogUtil.writeLog("传入的userid:"+ userid +",bvalue:"+ bvalue + "productName:" + productName + "的某个值为null！", "EShopLog");
			return null;
		}
		return null;
	}
	
	//在smart_bounty中生成一条记录
	public static void createBountyPayRecord(Integer userid,Integer bvalue,String productName){
		Date now = new Date();
		SmartBounty bounty = new SmartBounty();
		bounty.setUserid(userid);
		bounty.setAddtime(now);
		bounty.setBvalue(bvalue);	//交易额
		bounty.setTitile("购买"+productName);
		bounty.setAct(2);
		//bounty.setOvertime(null);
		bounty.setCurrentbvalue(0);
		Ebean.getServer(GlobalDBControl.getDB()).save(bounty);
	}
	
	//根据prepayid修改订单状态为已付款
	public static void updateOrderStatus_0(String prepayid){
		if(prepayid!=null){
			String sql = "update eshop_orders set status=0,updatetime=sysdate() where prepayid=:prepayid";
			Ebean.getServer(GlobalDBControl.getDB())
				.createSqlUpdate(sql)
				.setParameter("prepayid", prepayid)
				.execute();
			util.LogUtil.writeLog("用户购买成功,prepayid:为"+prepayid+"的订单生效", "EShopLog");
		}
	}
	
}
