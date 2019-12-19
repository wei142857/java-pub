package app.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import models.SmartBounty;
import models.SmartBountyExchanges;
import models.SmartExchanges;
import models.SmartProduct;
import util.GlobalDBControl;
import worker.exchange.PreProcessThread;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlRow;
public class ProductService {
	
	//根据userId 去smart_bounty表中生成 支出数据
	public static Integer payBountyExchangeProduct(Integer userid,String phone,String channel,Integer bvalue,Integer pid,String ip){
		//根据userId去smart_bounty表中查看该用户当前剩余的奖励金数量
		Integer amountBounty = VipService.findBountyByUserid(userid);
		//根据pId去smart_product表中查看如果想兑换该产品所需要的奖励金数量
		SmartProduct product = selectProductByPid(pid);
		//判断该用户当前的奖励金余额是否>该产品所需奖励金数量
		if(amountBounty>0&&amountBounty>=product.getBvalue()){
			//调用话费充值线程 为用户充值pId对应的话费
			PreProcessThread.add(userid, phone, channel, product.getBvalue(), pid, ip);
			return 1;
		}else{
			util.LogUtil.writeLog("用户:"+ userid +"使用奖励金兑换pid:"+ pid +"产品失败(当前奖励金余额不足)!", "addVipValidityTimeLog");
			return 2;	//当前用户的奖励金余额不足
		}
	}
	
	//兑换话费以后扣除(离过期时间最近的)奖励金的方法
	public static List<SmartBountyExchanges> payBvalueByOvertime(Integer userid,Integer bvalue,Integer price){
		if(userid!=null&&bvalue!=null&&price>0&&bvalue>0){
			List<SmartBounty> bountyList = selectSmartBountyByUserid_Act_Idd(userid);
			List<SmartBountyExchanges> bountyExchangesList = new ArrayList<SmartBountyExchanges>();
			Integer recordBvalue = bvalue;		//总共需要扣除奖励金的总数
			if(bountyList!=null){
				Ebean.beginTransaction();
				try{
					for(SmartBounty bounty:bountyList){
						SmartBountyExchanges bountyExchanges = new SmartBountyExchanges();
						//当前的currentbvalue值和recordBvalue比较
						if(recordBvalue>0 && recordBvalue>bounty.getCurrentbvalue()){
							//将当前记录的currentbvalue的值改为0
							updateCurrentBvalueByIdd(bounty.getIdd(),0);
							bountyExchanges.setBid(bounty.getIdd());
							bountyExchanges.setBvalue(bounty.getCurrentbvalue());
							bountyExchangesList.add(bountyExchanges);
							recordBvalue -= bounty.getCurrentbvalue();
							util.LogUtil.writeLog("userid:"+ userid +"的用户修改smart_bounty表中idd:"+ bounty.getIdd() +"的Currentbvalue值(值为"+ 0 +")成功", "addVipValidityTimeLog");	//修改成功
						}else if(recordBvalue>0 && recordBvalue==bounty.getCurrentbvalue()){
							//将当前记录的currentbvalue的值改为0
							updateCurrentBvalueByIdd(bounty.getIdd(),0);
							bountyExchanges.setBid(bounty.getIdd());
							bountyExchanges.setBvalue(bounty.getCurrentbvalue());
							bountyExchangesList.add(bountyExchanges);
							recordBvalue -= bounty.getCurrentbvalue();
							util.LogUtil.writeLog("userid:"+ userid +"的用户修改smart_bounty表中idd:"+ bounty.getIdd() +"的Currentbvalue值(值为"+ 0 +")成功", "addVipValidityTimeLog");	//修改成功
							//在smart_bounty中生成一条 兑换记录
							createBountyPayRecord(userid,bvalue,price);
							util.LogUtil.writeLog("userid:"+ userid +"的用户兑换话费生成记录成功", "addVipValidityTimeLog");
							Ebean.commitTransaction();
							return bountyExchangesList;
						}else{	//bounty.getCurrentbvalue()>recordBvalue
							updateCurrentBvalueByIdd(bounty.getIdd(),bounty.getCurrentbvalue()-recordBvalue);
							bountyExchanges.setBid(bounty.getIdd());
							bountyExchanges.setBvalue(recordBvalue);
							bountyExchangesList.add(bountyExchanges);
							recordBvalue -= bounty.getCurrentbvalue();//为负值
							util.LogUtil.writeLog("userid:"+ userid +"的用户修改smart_bounty表中idd:"+ bounty.getIdd() +"的Currentbvalue值(值为"+ (bounty.getCurrentbvalue()-recordBvalue) +")成功", "addVipValidityTimeLog");	//修改成功
							//在smart_bounty中生成一条 兑换记录
							createBountyPayRecord(userid,bvalue,price);
							util.LogUtil.writeLog("userid:"+ userid +"的用户兑换话费生成记录成功", "addVipValidityTimeLog");
							Ebean.commitTransaction();
							return bountyExchangesList;
						}
					}
				}catch(Exception e){	//事务回滚
					Ebean.rollbackTransaction();
					util.LogUtil.writeLog("userid:"+ userid +"的用户兑换话费时数据出错,事务回滚", "addVipValidityTimeLog");
					return null;
				}finally{
					Ebean.endTransaction();//事务关闭
				}
			}else{
				util.LogUtil.writeLog("传入的userid:"+ userid +"不合法！", "addVipValidityTimeLog");
				return null;
			}
		}else{
			util.LogUtil.writeLog("传入的userid:"+ userid +",bvalue:"+ bvalue + "price:" + price + "的某个值为null！", "addVipValidityTimeLog");
			return null;
		}
		return null;
	}
	
	//用户兑换话费支付失败,退回奖励金的方法
	public static void refundMoneyByPayFail(SmartExchanges smartExchange){
		if(smartExchange!=null){
			List<SmartBountyExchanges> exchangesList = selectBountyExchangesByEid(smartExchange.getIdd());
			if(exchangesList!=null){
				for(SmartBountyExchanges bountyExchange:exchangesList){
					//根据idd 去smart_bounty中查找该条数据
					SmartBounty bounty = selectSmartBountyByIdd(bountyExchange.getIdd());
					//首先判断该条记录当前的currentbvalue值是否为0
					if(bounty.getCurrentbvalue()==0){
						//去smart_bounty表中根据bid去修改currentbvalue的值
						updateCurrentBvalueByIdd(bountyExchange.getBid(),bountyExchange.getBvalue());
						util.LogUtil.writeLog("兑换话费失败,将奖励金退回用户,将id为:"+bountyExchange.getBid()+"的currentbvalue值改为:"+bountyExchange.getBvalue(), "addVipValidityTimeLog");
					}else{	//不为0
						updateCurrentBvalueByIdd(bountyExchange.getBid(),bounty.getCurrentbvalue()+bountyExchange.getBvalue());
						util.LogUtil.writeLog("兑换话费失败,将奖励金退回用户,将id为:"+bountyExchange.getBid()+"的currentbvalue值改为:"+bounty.getCurrentbvalue()+bountyExchange.getBvalue(), "addVipValidityTimeLog");
					}
				}
				//根据pid去查找该产品的价值(price)
				SmartProduct product = selectProductByPid(smartExchange.getPid());
				if(product!=null){
					//去smart_bounty生成一条退款收入记录
					createBountyRefundRecord(smartExchange.getUserid(),smartExchange.getBvalue(),product.getPrice());
					util.LogUtil.writeLog("兑换话费失败,将奖励金退回用户,生成退还记录成功", "addVipValidityTimeLog");
				}
			}
		}else{
			util.LogUtil.writeLog("调用refundMoneyByPayFail的方法传入的参数smartExchange:"+smartExchange+"不合法", "addVipValidityTimeLog");
		}
	}
	
	//根据idd 去smart_exchanges表中去查找对应的记录
	public static SmartExchanges selectExchangesRecordByIdd(Integer idd){
		if(idd != null){
			SmartExchanges exchange = Ebean.getServer(GlobalDBControl.getDB())
				.find(SmartExchanges.class)
				.where().eq("idd", idd)
				.findUnique();
			if(exchange != null){
				return exchange;
			}
		}
		return null;
	}
	
	//根据phone 去smart_exchanges表中查找该phone下所有的兑换记录
	public static List<SmartExchanges> selectExchangesRecordByPhone(String phone){
		if(checkPhone(phone)){
			List<SmartExchanges> exchangesRecord =  Ebean.getServer(GlobalDBControl.getDB())
					.find(SmartExchanges.class)
					.where().eq("account", phone)
					.findList();
			if(exchangesRecord!=null&&exchangesRecord.size()>0){
				return exchangesRecord;
			}
		}
		return null;
	}
	
	//根据userId 去smart_exchanges表中查找该userId下所有的兑换记录
	public static List<SmartExchanges> selectExchangesRecordByUserid(Integer userid){
		if(userid != null){
			List<SmartExchanges> exchangesRecord =  Ebean.getServer(GlobalDBControl.getDB())
				.createQuery(SmartExchanges.class)
				.where().eq("userid", userid)
				.findList();
			if(exchangesRecord!=null&&exchangesRecord.size()>0){
				return exchangesRecord;
			}
		}
		return null;
	}
	
	//根据userId 去smart_exchanges中去查找满足 分页条件的数据
	public static List<SmartExchanges> selectExchangesRecordByPage(Integer userid,Integer pageno,Integer pagesize){
		if(userid == null)
			return null;
		List<SmartExchanges> exchangesList = new ArrayList<SmartExchanges>();
		StringBuffer sql = new StringBuffer();
		sql.append("select idd from smart_exchanges where 1 = 1 and userid=:userid ORDER BY addtime DESC limit :m,:n ");
		List<SqlRow> list = Ebean.getServer(GlobalDBControl.getDB())	//list：idd的集合
				.createSqlQuery(sql.toString())
				.setParameter("userid", userid)
				.setParameter("m", pagesize*(pageno-1))
				.setParameter("n", pagesize)
				.findList();
		if(list!=null && list.size()>0){
			for(SqlRow row:list){
				//根据idd去smart_exchanges表中查找对应的对象
				SmartExchanges exchange = selectExchangesRecordByIdd(row.getInteger("idd"));
				exchangesList.add(exchange);
			}
			return exchangesList;
		}
		return null;
	}
	
	//根据eid 去smart_bounty_exchanges表中 找到对应的数据
	public static List<SmartBountyExchanges> selectBountyExchangesByEid(Integer eid){
		List<SmartBountyExchanges> exchangesList =  Ebean.getServer(GlobalDBControl.getDB())
				.find(SmartBountyExchanges.class)
				.where().eq("eid", eid)
				.findList();
		if(exchangesList!=null&&exchangesList.size()>0){
			return exchangesList;
		}
		return null;
	}
	
	//去smart_product表中查找所有的产品数据(刨除已下线的)
	public static List<SmartProduct> selectProductList(){
		List<SmartProduct> productList = Ebean.getServer(GlobalDBControl.getDB())
					.find(SmartProduct.class).where().eq("status",0).findList();
		if(productList!=null&&productList.size()>0){
			return productList;
		}
		return null;
	}
	
	//根据pId去 smart_product表中查找该pId所对应的数据
	public static SmartProduct selectProductByPid(Integer pid){
		if(pid != null){
			SmartProduct product = Ebean.getServer(GlobalDBControl.getDB())
					.createQuery(SmartProduct.class)
					.where().eq("idd", pid)
					.findUnique();
			if(product != null){
				return product;
			}
		}
		return null;
	}
	
	//修改smart_bounty表下某idd的currentbvalue的值,修改的值为ncurrentbvalue
	public static void updateCurrentBvalueByIdd(Integer idd,Integer ncurrentbvalue){
		Ebean.getServer(GlobalDBControl.getDB())
			.createSqlUpdate("UPDATE smart_bounty SET currentbvalue=:currentbvalue WHERE idd = :idd")
			.setParameter("currentbvalue", ncurrentbvalue)
			.setParameter("idd", idd)
			.execute();
	}
	
	//修改smart_bounty表 生成一条兑换话费的 支出记录
	public static void createBountyPayRecord(Integer userid,Integer bvalue,Integer price){
		Date now = new Date();
		SmartBounty bounty = new SmartBounty();
		bounty.setUserid(userid);
		bounty.setAddtime(now);
		bounty.setBvalue(bvalue);	//交易额
		bounty.setTitile("兑换"+ price +"元话费");
		bounty.setAct(2);
		//bounty.setOvertime(null);
		bounty.setCurrentbvalue(0);
		Ebean.getServer(GlobalDBControl.getDB()).save(bounty);
	}
	
	//去smart_bounty生成一条退款收入记录
	public static void createBountyRefundRecord(Integer userid,Integer bvalue,Integer price){
		Date now = new Date();
		SmartBounty bounty = new SmartBounty();
		bounty.setUserid(userid);
		bounty.setAddtime(now);
		bounty.setBvalue(bvalue);	//交易额
		bounty.setTitile("退还"+ bvalue +"个奖励金");
		bounty.setAct(1);
		//bounty.setOvertime(null);
		bounty.setCurrentbvalue(0);
		Ebean.getServer(GlobalDBControl.getDB()).save(bounty);
	}
	
	//根据UserId去smart_bounty表中查找 该用户 对应的数据
	public static List<SmartBounty> selectSmartBountyByUserid(Integer userid){
		if(userid!=null){
			List<SmartBounty> bountyList = Ebean.getServer(GlobalDBControl.getDB())
					.find(SmartBounty.class).where()
					.eq("userid", userid).findList();
			if(bountyList!=null&&bountyList.size()>0){
				return bountyList;
			}
		}
		return null;
	}
	
	//根据idd 去smart_bounty表中查找出该idd下的数据
	private static SmartBounty selectSmartBountyByIdd(Integer idd){
		if(idd!=null){
			//返回的是一个SmartBounty
			return Ebean.getServer(GlobalDBControl.getReadDB()).find(SmartBounty.class).where().eq("idd", idd).findUnique(); 
		}
		return null;
	}
	
	//根据UserId 去smart_bounty表中查找 该用户 act为1 对应的数据
	public static List<SmartBounty> selectSmartBountyByUserid_Act(Integer userid){
		if(userid!=null){
			StringBuffer sb = new StringBuffer("find SmartBounty where act=1 and userid=:userid");
			List<SmartBounty> bountyList = Ebean.getServer(GlobalDBControl.getDB())
						.createQuery(SmartBounty.class,sb.toString()).setParameter("userid", userid)
						.findList();
			if(bountyList!=null&&bountyList.size()>0){
				return bountyList;
			}
		}
		return null;
	}
	
	//根据UserId 去smart_bounty表中查找 该用户 act为1 对应的数据(并根据overtime排序)
	public static List<SmartBounty> selectSmartBountyByUserid_Act_Idd(Integer userid){
		if(userid!=null){
			StringBuffer sb = new StringBuffer("find SmartBounty where act=1 and currentbvalue>0 and userid=:userid order by overtime");
			List<SmartBounty> bountyList = Ebean.getServer(GlobalDBControl.getDB())
						.createQuery(SmartBounty.class,sb.toString()).setParameter("userid", userid)
						.findList();
			if(bountyList!=null&&bountyList.size()>0){
				return bountyList;
			}
		}
		return null;
	}
	
	//将smart_product表中的数据根据orders进行排序的方法
	public static List<SmartProduct> sortSmartProductByOrders(List<SmartProduct> list){
		Collections.sort(list,new Comparator<SmartProduct>(){
			
			@Override
			public int compare(SmartProduct o1, SmartProduct o2) {
				//按照smart_product中的orders进行排序
				if(o1.getOrders()>o2.getOrders()){
					return -1;
				}else if(o1.getOrders()<o2.getOrders()){
					return 1;
				}else{
					if(o1.getAddtime().getTime()>o2.getAddtime().getTime()){
						return 1;
					}else{
						return -1;
					}
				}
			}
		});
		return list;
	}
	
	//检验手机号的格式是否正确/是否合法
	private static boolean checkPhone(String phone){
		String regex = "^(13|15|18)\\d{9}$";
		if(phone!=null){
			if(phone.matches(regex)){
				return true;
			}
		}
		return false;
	}
}
