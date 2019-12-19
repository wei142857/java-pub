package app.action;

import java.util.Date;
import java.util.List;

import com.avaje.ebean.Ebean;
import app.dto.ReturnJson;
import app.service.UserService;
import app.service.VipService;
import controllers.SysConfigAction;
import models.EShopConsignee;
import models.EShopOrders;
import models.EShopProduct;
import models.ResponseError;
import models.SmartAppUser;
import play.Logger;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.SimpleResult;
import util.AjaxHellper;
import util.GlobalDBControl;
import util.LogUtil;
import util.json.JsonUtil;

//EShop login
@SuppressWarnings("all")
public class EShopMallIndex extends BaseAction{
	public static SimpleResult index = redirect("/public/app/eshop/EShopProductMallIndex.html");//商城首页
	public static SimpleResult login = redirect("/public/app/eshop/EShopLogin.html");//商城首页
	/**
	 * 获取商城所有商品
	 */
	public static Result index() {
		ReturnJson reJson = new ReturnJson();
		List<EShopProduct> proList = Ebean.getServer(GlobalDBControl.getDB()).createQuery(EShopProduct.class, "find EShopProduct").findList();
		reJson.setSuccess();
		reJson.setData(proList);
		String reContent = JsonUtil.parseObj(reJson);
		return ok(reContent);
	}
	/**
	 * 获取单个商品的信息
	 */
	public static Result findProInfo() {
		ReturnJson reJson = new ReturnJson();
		String token = getHttpParamByPOST("token");
		SmartAppUser user = UserService.findUserByToken(token);
		/*
		 * if(user==null||token==null) { reJson.setTokenTimeOut(); reJson.setData(0);
		 * return ok(JsonUtil.parseObj(reJson)); }
		 */
		String idd = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "idd");
		EShopProduct pro = Ebean.getServer(GlobalDBControl.getDB()).createQuery(EShopProduct.class, "find EShopProduct where idd='"+idd+"'").findUnique();
		reJson.setSuccess();
		reJson.setData(pro);
		return ok(JsonUtil.parseObj(pro));
	}
	/**
	 * 判断用户是否是VIP
	 * 返回1说明是会员
	 * 返回2说明不是会员
	 */
	public static final Result checkVip(){
		ReturnJson reJson = new ReturnJson();
		String token = getHttpParam("token");
		SmartAppUser user = UserService.findUserByToken(token);
		if(user==null||token==null) {
			reJson.setSuccess();
			reJson.setData(2);
			return ok(JsonUtil.parseObj(reJson));
		}
		reJson.setSuccess();
		reJson.setData(VipService.checkVip(user.getIdd()));
		return ok(JsonUtil.parseObj(reJson));
	}
	/**
	 * 获取用户的奖励金数量
	 */
	public static final Result getBountyCount() {
		ReturnJson reJson = new ReturnJson();
		String token = session("token");
		SmartAppUser user = UserService.findUserByToken(token);
		if(user==null||token==null) {
			reJson.setTokenTimeOut();
			reJson.setData(0);
			return ok(JsonUtil.parseObj(reJson));
		}
		reJson.setSuccess();
		reJson.setData(VipService.findBountyByUserid(user.getIdd()));
		return ok(JsonUtil.parseObj(reJson));
	}
	/**
	 * 获取用户的地址信息
	 * @retuen 如果返回null 说明用户没有添加地址信息
	 */
	public static Result findAddr() {
		ReturnJson reJson = new ReturnJson();
		String token = session("token");
		SmartAppUser user = UserService.findUserByToken(token);
		if(user==null||token==null) {
			reJson.setTokenTimeOut();
			reJson.setData(0);
			return ok(JsonUtil.parseObj(reJson));
		}
		reJson.setSuccess();
		List<EShopConsignee> addrList = Ebean.getServer(GlobalDBControl.getDB()).createQuery(EShopConsignee.class, "find EShopConsignee where userid='"+user.getIdd()+"'").findList();
		reJson.setData(addrList);
		return ok(JsonUtil.parseObj(reJson));
	}
	/**
	 * 删除地址
	 * 返回0说明登陆超时
	 * 返回1说明删除成功
	 */
	public static Result deleteAddr() {
		ReturnJson reJson = new ReturnJson();
		String idd = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "idd");
		String token = session("token");
		SmartAppUser user = UserService.findUserByToken(token);
		if(user==null||token==null) {
			reJson.setTokenTimeOut();
			reJson.setData(0);
			return ok(JsonUtil.parseObj(reJson));
		}
		int sucnum = Ebean.getServer(GlobalDBControl.getDB()).createUpdate(EShopConsignee.class, "delete from eshop_consignee where idd='"+idd+"' and userid='"+user.getIdd()+"'").execute();
		reJson.setSuccess();
		reJson.setData(1);
		return ok(JsonUtil.parseObj(reJson));
	}
	/**
	 * 更新地址
	 */
	public static Result updataAddr() {
		String type = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "type");
		String idd = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "idd");
		String name = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "name");
		String phone = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "phone");
		String area = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "area");
		String address = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "address");
		String def = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "def");//0-def,1- not def
		LogUtil.writeLog(type+"-"+idd+"-"+def, "addrinfo");
		
		String token = session("token");
		SmartAppUser user = UserService.findUserByToken(token);
		ReturnJson reJson = new ReturnJson();
		if(user==null) {
			reJson.setTokenTimeOut();
			reJson.setData(0);
			return ok(JsonUtil.parseObj(reJson));
		}
		
		if(type.equals("0")) {//0 -新增
			EShopConsignee ec = new EShopConsignee();
			ec.setUserid(user.getIdd());
			ec.setPhone(phone);
			ec.setName(name);
			if(def.equals("1")) {
				Ebean.getServer(GlobalDBControl.getDB()).createUpdate(EShopConsignee.class, "update eshop_consignee set isdefault=0 where userid='"+user.getIdd()+"'").execute();//将所有的
			}
			ec.setIsdefault(Integer.parseInt(def));
			ec.setArea(area);
			ec.setAddress(address);
			ec.setAddtime(new Date());
			Ebean.getServer(GlobalDBControl.getDB()).save(ec);
			reJson.setSuccess();
			reJson.setData(1);
			return ok(JsonUtil.parseObj(reJson));
		}
		
		if(def.equals("1")) {//设置为默认
			try {
				Ebean.getServer(GlobalDBControl.getDB()).createUpdate(EShopConsignee.class, "update eshop_consignee set isdefault=0 where userid='"+user.getIdd()+"'").execute();//将所有的
				Ebean.getServer(GlobalDBControl.getDB()).createUpdate(EShopConsignee.class, "update eshop_consignee set name='"+name+"',phone='"+phone+"',area='"+area+"',address='"+address+"',isdefault='"+def+"' where userid='"+user.getIdd()+"' and idd='"+idd+"'").execute();//将所有的
			} catch (Exception e) {
				Logger.error("getSMSByPhone",e);
				LogUtil.writeLog("更新地址出现问题"+e.getMessage(), "EShop");
			}
		}else {
			try {
				Ebean.getServer(GlobalDBControl.getDB()).createUpdate(EShopConsignee.class, "update eshop_consignee set name='"+name+"',phone='"+phone+"',area='"+area+"',address='"+address+"',isdefault='"+def+"' where userid='"+user.getIdd()+"' and idd='"+idd+"'").execute();//将所有的
			} catch (Exception e) {
				LogUtil.writeLog("更新地址出现问题"+e.getMessage(), "EShop");
				Logger.error("getSMSByPhone",e);
			}
		}
		
		reJson.setSuccess();
		reJson.setData(1);
		return ok(JsonUtil.parseObj(reJson));
	}
	/**
	 * 获取用户所有的订单
	 */
	public static final Result findUserOrder() {
		String token = getHttpParamByPOST("token");
		SmartAppUser user = UserService.findUserByToken(token);
		ReturnJson reJson = new ReturnJson();
		if(user==null) {//用户需要重新登陆
			reJson.setTokenTimeOut();
			reJson.setData(0);
			return ok(JsonUtil.parseObj(reJson));
		}
		//获取用户订单状态为成功状态（非失败和待付款）的所有订单信息
		List<EShopOrders> orderList = Ebean.getServer(GlobalDBControl.getDB()).createQuery(EShopOrders.class,"find EShopOrders where userid='"+user.getIdd()+"' and status='0'").findList();
		
		return ok(JsonUtil.parseObj(orderList));
	}
	/**
	 * 获取用户支付订单
	 */
	public static final Result findOrder() {
		String orderid = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "orderid");
		String token = session("token");
		SmartAppUser user = UserService.findUserByToken(token);
		ReturnJson reJson = new ReturnJson();
		if(user==null) {//用户需要重新登陆
			reJson.setTokenTimeOut();
			reJson.setData(0);
			return ok(JsonUtil.parseObj(reJson));
		}
		EShopOrders order = Ebean.getServer(GlobalDBControl.getDB()).createQuery(EShopOrders.class, "find EShopOrders where userid='"+user.getIdd()+"' and idd='"+orderid+"'").findUnique();
		if(order==null) {
			reJson.setParamsError();
			reJson.setData(0);
			return ok(JsonUtil.parseObj(reJson));
		}
		return ok(JsonUtil.parseObj(order));
	}
	/**
	 * 获取User的token
	 * @param key
	 * @return
	 */
	public static String getHttpParam(String key){
		String value = request().getQueryString(key);
		if(value ==null || "".equals(value))
			value = session(key);
		else
			session().put(key,value);
		return value;
	}
	/**
	 * 获取User的token
	 * POST方式
	 * @param key
	 * @return
	 */
	public static String getHttpParamByPOST(String key){
		String value = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), key);
		if(value ==null || "".equals(value))
			value = session(key);
		else
			session().put(key,value);
		return value;
	}
	/**
	 * 保存分享source
	 */
	public static Result saveShareSource(){
		String source = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "source");
		ReturnJson reJson = new ReturnJson();
//		String token = session("token");
//		SmartAppUser user = UserService.findUserByToken(token);
//		if(user==null) {//用户需要重新登陆
//			reJson.setTokenTimeOut();
//			reJson.setData(0);
//			return ok(JsonUtil.parseObj(reJson));
//		}
		session("source", source);
		reJson.setSuccess();
		return ok(JsonUtil.parseObj(reJson));
	}
	/**
	 * 判断是否是测试人员
	 */
	public static Result checkTester() {
		String token = getHttpParam("token");
		SmartAppUser appUser = UserService.findUserByToken(token);
		List<EShopProduct> productsList;
	    String ManagerPhone =  SysConfigAction.findSysconfig("智能锁", "苗苗姐测试商城商品手机号");
	    ReturnJson reJson = new ReturnJson();
	    if(appUser.getPhone()!=null&&ManagerPhone!=null&&ManagerPhone.indexOf(appUser.getPhone())!=-1){
	    	reJson.setData(1);//是测试人员
			return ok(JsonUtil.parseObj(reJson));
	    }else {
	    	reJson.setData(0);//不是测试人员
	    	return ok(JsonUtil.parseObj(reJson));
	    }
	}
}
