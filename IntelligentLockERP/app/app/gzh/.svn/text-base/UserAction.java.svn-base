package app.gzh;

import java.util.Date;

import models.IntelligentLockProduct;
import models.PhoneNo;
import models.SmartAppUser;
import models.SubWxUser;
import play.Logger;
import play.mvc.Result;
import util.GlobalDBControl;
import util.GlobalSetting;
import util.SmsUtil;
import util.StringUtil;
import util.classEntity.StringTool;
import util.json.JsonUtil;
import app.dto.ReturnJson;
import app.service.SubWxUserService;
import app.service.UserService;
import app.util.AppUtil;

import com.avaje.ebean.Ebean;

public class UserAction extends BaseAction {
	
	// 登陆
	public static Result login() {
		ReturnJson reJson = new ReturnJson();
		try {
			String phone = request().getQueryString("phone");
			String smscode = request().getQueryString("smscode");

			if (StringUtil.isNull(phone) || StringUtil.isNull(smscode)) {
				reJson.setParamsError();
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}

			String rCode = AppUtil.getRandomCode(phone, AppUtil.TYPE_SMS_LOGIN);
			if (!smscode.equals(rCode)) {
				reJson.setCode(1);
				reJson.setMessage("短信验证码错误");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}

			String openid = session(GlobalSetting.GZH_OPENID);

			if (StringUtil.isNull(openid)) {
				reJson.setCode(2);
				reJson.setMessage("非法登录,请关注微信：SKN智能生活" + "\t" + "公众号进行登录");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}

			UserService.addUserGzh(phone,
					UserService.TYPE_SMART_APP_USER_MOBILE,
					util.AjaxHellper.getIpAddr(request()));

			SubWxUser subWxUser = SubWxUserService
					.findIllegalNotifyWxUser(openid);
			if (subWxUser == null) {
				subWxUser = new SubWxUser();
				subWxUser.setAddtime(new Date());
				subWxUser.setCh(SubWxUserService.CH_ILLEGAL_NOTIFY);
				subWxUser.setOpenid(openid);
				subWxUser.setPhone(phone);
				Ebean.getServer(GlobalDBControl.getDB()).save(subWxUser);
			}
			reJson.setSuccess();
		} catch (Exception e) {
			Logger.error("login", e);
		}

		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}

	// 获取短信验证码
	public static Result getSMSByPhone() {
		ReturnJson reJson = new ReturnJson();
		try {
			String phone = request().getQueryString("phone");
			
			if(!StringUtil.isMobileNO(phone)){
				reJson.setParamsError();
				reJson.setMessage("您输入的号码无效，请重新输入。");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}

			int type = StringTool.GetInt(request().getQueryString("type"), 0);

			String rdCode = StringUtil.mkNumRanmString(6);
			appLogger.debug("rdCode:"+rdCode+"\t"+type);
			if (type == 1) {// 登陆短信
				UserService.clearImgCode(phone);
				AppUtil.setRandomCode(phone, rdCode, AppUtil.TYPE_SMS_LOGIN);
				SmsUtil.sendRandCode(phone, rdCode,
						AppUtil.TIMEOUT_CACHESMSCODE_MINUTE + "");
			}
			if (type == 2) {// 修改登陆手机短信
				AppUtil.setRandomCode(phone, rdCode,
						AppUtil.TYPE_SMS_MODIFYPHONE);
				SmsUtil.sendRandCode(phone, rdCode,
						AppUtil.TIMEOUT_CACHESMSCODE_MINUTE + "");
			}
			if (type == 3) {// 删除设备
				AppUtil.setRandomCode(phone, rdCode, AppUtil.TYPE_SMS_DELDEVICE);
				SmsUtil.sendRandCode(phone, rdCode,
						AppUtil.TIMEOUT_CACHESMSCODE_MINUTE + "");
			}
			if (type == 4) {// 兑换话费
				AppUtil.setRandomCode(phone, rdCode, AppUtil.TYPE_SMS_EXCHANGE);
				SmsUtil.sendRandCode(phone, rdCode,
						AppUtil.TIMEOUT_CACHESMSCODE_MINUTE + "");
			}
			reJson.setSuccess();
		} catch (Exception e) {
			Logger.error("getSMSByPhone", e);
		}

		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}

	// 修改手机号
	public static Result setLoginPhone() {
		ReturnJson reJson = new ReturnJson();

		String openid = session(GlobalSetting.GZH_OPENID);

		if (StringUtil.isNull(openid)) {
			reJson.setCode(2);
			reJson.setMessage("非法登录,请关注微信：SKN智能生活" + "\t" + "公众号进行登录");
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}

		SubWxUser subWxUser = SubWxUserService.findIllegalNotifyWxUser(openid);

		String phone = request().getQueryString("phone");

		String smscode = request().getQueryString("code");
		if (StringUtil.isNull(smscode)) {
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}

		SmartAppUser user = UserService.findUserByPhone(subWxUser.getPhone());

		if (user == null) {
			reJson.setTokenTimeOut();
		} else {
			String rCode = AppUtil.getRandomCode(phone,
					AppUtil.TYPE_SMS_MODIFYPHONE);
			if (!smscode.equals(rCode)) {
				reJson.setCode(1);
				reJson.setMessage("短信验证码错误");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			if (phone == null || !StringUtil.isMobileNO(phone)) {
				reJson.setCode(2);
				reJson.setMessage("手机号格式错误");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}

			PhoneNo pn = UserService.getHaoduan(phone);

			SmartAppUser newUser = UserService.findUserByPhone(phone);

			if (newUser != null) {
				reJson.setCode(3);
				reJson.setMessage("手机号已经注册，请更换手机号");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}

			Ebean.getServer(GlobalDBControl.getDB())
					.createSqlUpdate(
							"update smart_app_user set phone=:phone,login=:login,prov=:prov,city=:city,yys=:yys where idd=:idd")
					.setParameter("phone", phone).setParameter("login", phone)
					.setParameter("prov", pn.getProvince())
					.setParameter("city", pn.getCity())
					.setParameter("yys", pn.getType())
					.setParameter("idd", user.getIdd()).execute();

			Ebean.getServer(GlobalDBControl.getDB())
					.createSqlUpdate(
							"update sub_wx_user set phone=:phone where phone=:odPhone and ch=:ch")
					.setParameter("phone", phone)
					.setParameter("ch", SubWxUserService.CH_ILLEGAL_NOTIFY)
					.setParameter("odPhone", subWxUser.getPhone()).execute();

			UserService.clearUserByPhone(user.getPhone());
			user.setPhone(phone);
			user.setProv(pn.getProvince());
			user.setCity(pn.getCity());
			user.setYys(pn.getType());

			UserService.setUserByPhone(phone, user);

			reJson.setSuccess();
		}
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}
}
