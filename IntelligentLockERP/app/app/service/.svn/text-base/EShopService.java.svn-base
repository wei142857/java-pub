package app.service;

import java.util.Date;
import java.util.List;
import models.EShopConsignee;
import models.EShopProduct;
import util.EntityConvert;
import util.GlobalDBControl;
import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlRow;

public class EShopService {
	
	//去 eshop_product表 查询出所有上架(status=0)商品相关信息
	public static List<EShopProduct> selectEShopProductByStatus_0(){
		List<EShopProduct> productsList = Ebean.getServer(GlobalDBControl.getDB()).find(EShopProduct.class).where().eq("status", "0").order("orders desc").findList();
		if(productsList!=null&&productsList.size()>0){
			util.LogUtil.writeLog("成功将所有上架的商品信息返回给getEShopMainInfo接口", "EShopLog");
			return productsList;
		}
		return null;
	}
	
	//去 eshop_product表 查询出所有上架(status=0)商品相关信息
	public static List<EShopProduct> selectEShopProductByStatus_1(){
		List<EShopProduct> productsList = Ebean.getServer(GlobalDBControl.getDB()).find(EShopProduct.class).where().ne("status", "-1").order("orders desc").findList();
		if(productsList!=null&&productsList.size()>0){
			util.LogUtil.writeLog("成功将所有上架的商品信息返回给getEShopMainInfo接口", "EShopLog");
			return productsList;
		}
		return null;
	}
	
	//去 eshop_consignee表 查询出所有收货地址相关信息
	public static List<EShopConsignee> selectAllAddress(Integer userid){
		if(userid!=null){
			List<EShopConsignee> consigneeList = Ebean.getServer(GlobalDBControl.getDB()).find(EShopConsignee.class).where().eq("userid", userid).findList();
			if(consigneeList!=null&&consigneeList.size()>0){
				util.LogUtil.writeLog("成功将userid为:"+userid+"的全部收货地址返回给getEShopAddresses接口", "EShopLog");
				return consigneeList;
			}
		}
		return null;
	}
	
	//去 eshop_consignee表 查询出所有收货地址并根据时间排序
	public static List<EShopConsignee> selectAllAddressOrderByAddtime(Integer userid){
		if(userid!=null){
			String sql = "select * FROM eshop_consignee WHERE userid=:userid ORDER BY addtime DESC";
			List<SqlRow> sqlRows = Ebean.getServer(GlobalDBControl.getDB())
						.createSqlQuery(sql)
						.setParameter("userid", userid)
						.findList();
			List<EShopConsignee> consigneeList = EntityConvert.convert(sqlRows, EShopConsignee.class);
			if(consigneeList!=null&&consigneeList.size()>0){
				util.LogUtil.writeLog("成功将userid为:"+userid+"的全部收货地址根据addtime排序并返回给getEShopAddresses接口", "EShopLog");
				return consigneeList;
			}
		}
		return null;
	}
	
	//根据addressId和userid 去 eshop_consignee表 查询出该userid/addressid下的该条地址信息
	public static EShopConsignee selectAddressByAddressIdAndUserid(Integer userid,Integer addressid){
		if(userid!=null&&addressid!=null){
			EShopConsignee consignee = Ebean.getServer(GlobalDBControl.getDB()).find(EShopConsignee.class).where().eq("idd", addressid).eq("userid", userid).findUnique();
			if(consignee!=null){
				util.LogUtil.writeLog("根据userid:"+userid+"和addressid:"+addressid+"成功找到一条收货地址数据并返回给editEShopAddress接口", "EShopLog");
				return consignee;
			}
		}
		return null;
	}
	
	//根据addressId 去 eshop_consignee表 查询出该addressid下的该条地址信息
	public static EShopConsignee selectAddressByAddressId(Integer addressid){
		if(addressid!=null){
			EShopConsignee consignee= Ebean.getServer(GlobalDBControl.getDB()).find(EShopConsignee.class).where().eq("idd", addressid).findUnique();
			if(consignee!=null){
				util.LogUtil.writeLog("根据addressid:"+addressid+"成功找到该条收货地址并返回给editEShopAddress接口", "EShopLog");
				return consignee;
			}
		}
		return null;
	}
	
	//根据userid 去 eshop_consignee表 查询出该userid下是否有地址信息
	public static List<EShopConsignee> selectAddressByUserid(Integer userid){
		if(userid!=null){
			List<EShopConsignee> consigneeList = Ebean.getServer(GlobalDBControl.getDB()).find(EShopConsignee.class).where().eq("userid", userid).findList();
			if(consigneeList!=null&&consigneeList.size()>0){
				return consigneeList;
			}
		}
		return null;
	}
	
	//去 eshop_consignee表 查询当前默认地址的记录
	public static EShopConsignee selectDefaultAddress(Integer userid){
		EShopConsignee consignee = Ebean.getServer(GlobalDBControl.getDB()).find(EShopConsignee.class).where().eq("userid",userid).eq("isdefault", "1").findUnique();
		if(consignee!=null){
			util.LogUtil.writeLog("查找到默认地址其addressid为"+consignee.getIdd(), "EShopLog");
			return consignee;
		}
		return null;
	}
	
	//去eshop_consignee表中新增一条收货地址前 对默认地址进行判断
	public static void addAddress_check(Integer userid,String name,String phone,String area,String address,Integer isdefault){
		try{
			Ebean.beginTransaction();
			//首先判断isdefault的值(0:非默认 1:默认)
			if(isdefault==0){
				//查看该userid是否有收获地址,如果没有,则设置这个收获地址为默认收货地址
				/*List<EShopConsignee> list = selectAddressByUserid(userid);
				if(list!=null&&list.size()>0){	//说明该用户有其他的地址信息
					addAddress(userid,name,phone,area,address,isdefault);
				}else{		//该条地址信息为该用户的第一条地址信息(设置为默认地址)
					addAddress(userid,name,phone,area,address,1);
				}*/
				addAddress(userid,name,phone,area,address,isdefault);//此处不做默认地址处理,用户可以没有默认地址
				Ebean.commitTransaction();
			}else{
				List<EShopConsignee> list = selectAddressByUserid(userid);
				if(list!=null&&list.size()>0){	//说明该用户有其他的地址信息
					cancelDefaultAddress(userid);	//取消其他默认地址,将该条地址设置为默认地址
					addAddress(userid,name,phone,area,address,isdefault);
				}else{	//添加该条默认地址
					addAddress(userid,name,phone,area,address,isdefault);
				}
				Ebean.commitTransaction();
			}
		}catch(Exception e){
			Ebean.rollbackTransaction();
			util.LogUtil.writeLog("回滚时出错信息:"+e.getMessage(), "EShopLog");
		}finally{
			Ebean.endTransaction();
		}
	}
	
	//去eshop_consignee表中修改收货地址前 对默认地址进行判断
	public static boolean updateAddress_check(Integer userid,Integer addressid,String name,String phone,String area,String address,Integer isdefault){
		//首先判断isdefault的值(0:非默认 1:默认)
		if(isdefault==0){
			//根据该addressid查找该条记录的修改前的isdefault的值
			/*EShopConsignee consignee = selectAddressByAddressId(addressid);
			if(consignee!=null){
				Integer oldIsdefault = consignee.getIsdefault();
				if(oldIsdefault==1){	//修改前是默认地址修改后为非默认(不允许这种情况,用户必须保留一条默认地址)
					return false;
				}else{	//执行修改操作
					updateAddressByAddressid(addressid,name,phone,area,address,isdefault);
					return true;
				}
			}*/
			updateAddressByAddressid(addressid,name,phone,area,address,isdefault);//此处不做默认地址处理，用户可以不设置默认地址
			return true;
		}else{	//修改后默认地址
			EShopConsignee consignee = selectAddressByAddressId(addressid);
			if(consignee!=null){
				Integer oldIsdefault = consignee.getIsdefault();
				try{
					Ebean.beginTransaction();
					if(oldIsdefault==1){	//修改前后都是默认地址
						//执行修改操作
						updateAddressByAddressid(addressid,name,phone,area,address,isdefault);
						Ebean.commitTransaction();
						return true;
					}else{		//修改前是非默认地址
						//将之前默认的地址取消
						cancelDefaultAddress(userid);
						updateAddressByAddressid(addressid,name,phone,area,address,isdefault);
						Ebean.commitTransaction();
						return true;
					}
				}catch(Exception e){
					Ebean.rollbackTransaction();
					util.LogUtil.writeLog("回滚时出错信息:"+e.getMessage(), "EShopLog");
				}finally{
					Ebean.endTransaction();
				}
			}
		}
		return false;
	}
	
	//去eshop_consignee表新增一条收货地址
	public static void addAddress(Integer userid,String name,String phone,String area,String address,Integer isdefault){
		EShopConsignee consignee = new EShopConsignee();
		consignee.setUserid(userid);
		consignee.setName(name);
		consignee.setPhone(phone);
		consignee.setArea(area);
		consignee.setAddress(address);
		consignee.setAddtime(new Date());
		consignee.setIsdefault(isdefault);
		try{
			Ebean.getServer(GlobalDBControl.getDB()).save(consignee);
			util.LogUtil.writeLog("成功为userid:"+userid+"增加一条新的收货地址", "EShopLog");
		}catch(Exception e){
			util.LogUtil.writeLog("为userid:"+userid+"增加一条新的收货地址时出错:e:"+e.getMessage(), "EShopLog");
		}
	}
	
	//去 eshop_consignee表 根据传入的参数对地址信息进行修改
	public static void updateAddressByAddressid(Integer addressid,String name,String phone,String area,String address,Integer isdefault){
		String sql = "update eshop_consignee set name=:name,phone=:phone,area=:area,address=:address,isdefault=:isdefault where idd=:addressid";
		Ebean.getServer(GlobalDBControl.getDB())
			.createSqlUpdate(sql)
			.setParameter("name", name)
			.setParameter("phone", phone)
			.setParameter("area", area)
			.setParameter("address", address)
			.setParameter("isdefault", isdefault)
			.setParameter("addressid", addressid)
			.execute();
		util.LogUtil.writeLog("成功将addressid:"+addressid+"的地址信息修改,修改前isdefault为:"+isdefault, "EShopLog");
	}
	
	//去 eshop_consignee表 将1:默认地址改为0:非默认地址
	public static void cancelDefaultAddress(Integer userid){
		EShopConsignee consignee = selectDefaultAddress(userid);
		if(consignee!=null){
			String sql = "update eshop_consignee set isdefault=0 where isdefault=1 ";
			Ebean.getServer(GlobalDBControl.getDB()).createSqlUpdate(sql).execute();
			util.LogUtil.writeLog("成功将addressid:"+consignee.getIdd()+"的地址改为非默认地址", "EShopLog");
		}
	}
	
	//根据addressid 去 eshop_consignee表将该条记录删除前的判断
	public static void deleteAddress_check(Integer userid,Integer addressid){
		//首先根据addressid去查看该条记录是否为默认地址(0:非默认 1:默认)
		EShopConsignee consignee = selectAddressByAddressId(addressid);
		if(consignee!=null){
			//先查看该用户还有没有其他的收货地址
			List<EShopConsignee> consigneeList = selectAddressByUserid(userid);
			if(consigneeList!=null&&consigneeList.size()>1){	//有其它地址
				if(consignee.getIsdefault()==1){
					try{
						Ebean.beginTransaction();
						deleteAddressByAddressid(addressid);
						//按时间将最近添加或修改过的地址设置为默认地址
						setNewDefaultAddress(userid);
						Ebean.commitTransaction();
					}catch(Exception e){
						Ebean.rollbackTransaction();
						util.LogUtil.writeLog("删除收货地址时出错:e:"+e.getMessage(), "EShopLog");
					}finally{
						Ebean.endTransaction();
					}
				}else{	//执行删除操作
					deleteAddressByAddressid(addressid);
				}
			}else{	//该用户除了该条无其他的收货地址
				deleteAddressByAddressid(addressid);
			}
		}
	}
	
	//根据addressid 去eshop_consignee表将该条记录删除
	public static void deleteAddressByAddressid(Integer addressid){
		String sql = "delete from EShopConsignee where idd=:addressid";
		Ebean.getServer(GlobalDBControl.getDB())
			.createUpdate(EShopConsignee.class, sql)
			.setParameter("addressid", addressid).execute();
		util.LogUtil.writeLog("成功将addressid:"+addressid+"的收货地址删除", "EShopLog");
	}
	
	//根据addressid 去eshop_consignee表将该条记录删除
	public static void deleteAddressByAddressidAndUserid(Integer addressid,Integer userid){
		StringBuffer sql = new StringBuffer("delete from EShopConsignee where idd=:addressid");
		sql.append(" and userid=:userid");
		Ebean.getServer(GlobalDBControl.getDB())
			.createUpdate(EShopConsignee.class, sql.toString())
			.setParameter("addressid", addressid)
			.setParameter("userid", userid)
			.execute();
		util.LogUtil.writeLog("成功将addressid:"+addressid+",userid为:"+userid+"的收货地址删除", "EShopLog");
	}
	
	//按时间将最近添加或修改过的地址设置为默认地址
	public static void setNewDefaultAddress(Integer userid){
		//查找到根据addtime排序后的地址集合
		List<EShopConsignee> consigneeList = selectAllAddressOrderByAddtime(userid);
		if(consigneeList!=null){
			Integer addressid = consigneeList.get(0).getIdd();
			if(addressid!=null){
				//将addressid的这条记录的地址改为默认地址
				setDefaultAddressByAddressId(addressid);
			}
		}
	}
	
	//根据addressid去 eshop_consignee表中修改该条地址为默认地址
	public static void setDefaultAddressByAddressId(Integer addressid){
		Ebean.getServer(GlobalDBControl.getDB())
			.createSqlUpdate("update eshop_consignee set isdefault=1 where idd=:addressid")
			.setParameter("addressid", addressid)
			.execute();
		util.LogUtil.writeLog("成功将addressid为:"+addressid+"的地址修改为默认地址", "EShopLog");
	}
}
