package app.action;

import java.util.Date;

import models.IntelligentLockProduct;
import models.IntelligentLockProductCode;
import models.SubOrders;
import models.SubWxUser;
import play.Logger;
import play.mvc.Result;
import util.GlobalDBControl;
import util.GlobalSetting;
import util.SmsUtil;
import util.StringUtil;
import util.json.JsonUtil;
import app.dto.ReturnJson;
import app.service.SubOrdersService;
import app.service.SubWxUserService;
import app.util.AppUtil;

import com.avaje.ebean.Ebean;

import controllers.SubOrdersAction;
/**
 * doLogin()
 * 返回的状态码:
 * 0:成功
 * 1:输入的电话号码有误
 * 2:短信验证码错误
 * 3:未找到订单
 * 4:已预约安装过
 * 5:已预约测量过
 * 6:openid为null,非法登录
 * @author 陈宏亮
 *
 */
public class AppointLogin extends BaseAction{
	
	//执行登录
	public static final Result doLogin(){
		ReturnJson reJson = new ReturnJson();
		try{
			String phone = request().getQueryString("phone");
			String smscode = request().getQueryString("sms");
			String state = request().getQueryString("state");
			//判断 用户是否点击再次预约安装或者测量
			String again = request().getQueryString("again");
			//检验手机号
			if(StringUtil.isNull(phone)||!StringUtil.isMobileNO(phone)){
				reJson.setCode(1);
				reJson.setMessage("您输入的号码无效，请重新输入。");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			//从Redis中获取短信验证码
			String rCode = AppUtil.getRandomCode(phone, AppUtil.TYPE_SMS_APPOINT_LOGIN);
			if(StringUtil.isNull(smscode)||!smscode.equals(rCode)){
				reJson.setCode(2);
				reJson.setMessage("短信验证码错误");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			//从session中获取 openid
			String openid = session(GlobalSetting.GZH_OPENID);
			
			if(StringUtil.isNull(openid)){
				reJson.setCode(6);
				reJson.setMessage("非法登录,请关注微信：SKN智能生活"+"\t"+"公众号进行登录");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}else{
				//先根据openid查找有没有该openid的记录
				SubWxUser user = checkOpenidInfo(openid);
				if(user == null){	//将phone 和 openid 存到数据库中
					saveUserLoginInfo(phone,openid);
				}else{	//有记录 判断phone是否匹配
					if(!phone.equals(user.getPhone())){	//将原有手机号覆盖
						user.setPhone(phone);
						user.setAddtime(new Date());
						Ebean.getServer(GlobalDBControl.getDB()).save(user);
					}
				}
			}
			if("102".equals(state)){
				SubOrders subOrder = SubOrdersService.findToInstallSubOrder(phone);
				if(subOrder==null){
					if(again==null){
						if(SubOrdersService.findAlreadyInstallSubOrder(phone)>0){
							reJson.setCode(4);
							reJson.setMessage("已经预约安装");
							String reContent = JsonUtil.parseObj(reJson);
							appLogger.debug(reContent);
							return ok(reContent);
						}
					}
					reJson.setCode(3);
					reJson.setMessage("未找到订单");
					String reContent = JsonUtil.parseObj(reJson);
					appLogger.debug(reContent);
					return ok(reContent);
				}
			}
			if("103".equals(state)){
				SubOrders subOrder = SubOrdersService.findToMeasureSubOrder(phone);
				if(subOrder==null){
					if(again==null){
						if(SubOrdersService.findAlreadyMeasureSubOrder(phone)>0){
							reJson.setCode(5);
							reJson.setMessage("已经预约测量");
							String reContent = JsonUtil.parseObj(reJson);
							appLogger.debug(reContent);
							return ok(reContent);
						}
					}
					reJson.setCode(3);
					reJson.setMessage("未找到订单");
					String reContent = JsonUtil.parseObj(reJson);
					appLogger.debug(reContent);
					return ok(reContent);
				}
			}
			reJson.setCode(0);
			reJson.setMessage("成功");
		}catch(Exception e){
			Logger.error("appointLogin", e.getMessage());
		}
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}

	//根据手机号 获取短信验证码
	public static final Result getSMSByPhone(){
		ReturnJson reJson = new ReturnJson();
		try{
			String phone = request().getQueryString("phone");
			//检验手机号
			if(StringUtil.isNull(phone)||!StringUtil.isMobileNO(phone)){
				reJson.setParamsError();
				reJson.setMessage("您输入的号码无效，请重新输入。");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			//获取一个6位短信验证码
			String rdCode = StringUtil.mkNumRanmString(6);
			//将短信验证码放到缓存中(有效期60s)
			AppUtil.setRandomCode(phone, rdCode, AppUtil.TYPE_SMS_APPOINT_LOGIN);
			//发送短信验证码
			SmsUtil.sendRandCode(phone, rdCode, AppUtil.TIMEOUT_CACHESMSCODE_MINUTE+"");
			Logger.info("appointLogin:"+rdCode+"\t"+phone);	//控制台打印验证码
			reJson.setSuccess();
		}catch(Exception e){
			Logger.error("getSMSByPhone",e);
		}
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}
	
	//将phone和openid保存到数据库
	public static void saveUserLoginInfo(String phone,String openid){
		if(phone!=null&&openid!=null){
			SubWxUser user = new SubWxUser();
			user.setOpenid(openid);
			user.setPhone(phone);
			user.setCh(SubWxUserService.CH_SUB);
			user.setAddtime(new Date());
			Ebean.getServer(GlobalDBControl.getDB()).save(user);
		}
	}
	
	//查看openId对应的这条记录
	public static SubWxUser checkOpenidInfo(String openid){
		if(openid!=null){
			SubWxUser user = Ebean.getServer(GlobalDBControl.getDB())
				.createQuery(SubWxUser.class)
				.where().eq("openid", openid).eq("ch", SubWxUserService.CH_SUB)
				.findUnique();
			if(user!=null){
				return user;
			}
		}
		return null;
	}
	
	public static Result offLineOrder(){
		String openid = session(GlobalSetting.GZH_OPENID);
		String deviceid = request().getQueryString("deviceid");
		String state = request().getQueryString("state");
		ReturnJson reJson = new ReturnJson();
		if(openid!=null){
			//先根据phone和openid来查找是否有这条记录
			SubWxUser user = SubWxUserService.findSubWxUser(openid);
			if(user != null){
				IntelligentLockProductCode intelligentLockProductCode = Ebean.getServer(GlobalDBControl.getReadDB()).find(IntelligentLockProductCode.class)
				.where().eq("deviceid", deviceid).findUnique();
				if(intelligentLockProductCode==null){
					reJson.setCode(1);
					reJson.setMessage("订单未找到");
					String reContent = JsonUtil.parseObj(reJson);
					appLogger.debug(reContent);
					return ok(reContent);
				}
				//线下购买没有生成订单，手动生成一笔订单
				SubOrders subOrder = Ebean.getServer(GlobalDBControl.getReadDB()).find(SubOrders.class)
					.where().eq("orderid", deviceid).eq("ch", "offline").findUnique();
				if(subOrder==null){
					subOrder = new SubOrders();
					subOrder.setIsinstall(1);
					subOrder.setIsmeasure(1);
					subOrder.setAddtime(new Date());
					subOrder.setCh("offline");
					subOrder.setPhone(user.getPhone());
					String lockCode = intelligentLockProductCode.getCode();
					if(lockCode.indexOf("A7")!=-1){
						subOrder.setTitle(SubOrdersAction.SKN_A7.get(0));
						subOrder.setSmallimgurl(SubOrdersAction.SKN_A7.get(1));
					}
					if(lockCode.indexOf("N1")!=-1){
						subOrder.setTitle(SubOrdersAction.SKN_N1.get(0));
						IntelligentLockProduct intellogentLockProdct = Ebean.getServer(GlobalDBControl.getReadDB()).find(IntelligentLockProduct.class)
						.where().eq("idd", intelligentLockProductCode.getProductid()).findUnique();
						if(intellogentLockProdct.getTitle().indexOf("金")!=-1){
							subOrder.setSmallimgurl(SubOrdersAction.SKN_N1.get(1));
						}else{
							subOrder.setSmallimgurl(SubOrdersAction.SKN_N1.get(2));
						}
						
					}
					if(lockCode.indexOf("M6")!=-1){
						subOrder.setTitle(SubOrdersAction.SKN_M6.get(0));
						subOrder.setSmallimgurl(SubOrdersAction.SKN_M6.get(1));
					}
					subOrder.setOrderid(deviceid);
					Ebean.getServer(GlobalDBControl.getDB()).save(subOrder);
				}
				if("102".equals(state) && subOrder.getIsinstall()==0){
					reJson.setCode(1);
					reJson.setMessage("该设备已预约安装过啦");
					String reContent = JsonUtil.parseObj(reJson);
					appLogger.debug(reContent);
					return ok(reContent);
				}
				if("103".equals(state) && subOrder.getIsmeasure()==0){
					reJson.setCode(1);
					reJson.setMessage("该设备已预约测量过啦");
					String reContent = JsonUtil.parseObj(reJson);
					appLogger.debug(reContent);
					return ok(reContent);
				}
				reJson.setCode(0);
				reJson.setMessage("成功");
			}
		}
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}
	
}
