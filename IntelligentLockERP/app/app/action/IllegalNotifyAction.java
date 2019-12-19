package app.action;

import java.util.Date;

import models.SubWxUser;
import play.Logger;
import play.mvc.Result;
import util.GlobalDBControl;
import util.GlobalSetting;
import util.SmsUtil;
import util.StringUtil;
import util.json.JsonUtil;
import ServiceDao.WxGzhService;
import app.dto.ReturnJson;
import app.service.SubWxUserService;
import app.util.AppUtil;

import com.avaje.ebean.Ebean;

public class IllegalNotifyAction extends BaseAction{
	public static Result bind(){
		String phone = request().getQueryString("phone");
		String smscode = request().getQueryString("sms");
		ReturnJson reJson = new ReturnJson();
		//检验手机号
		if(StringUtil.isNull(phone)||!StringUtil.isMobileNO(phone)){
			reJson.setCode(1);
			reJson.setMessage("您输入的号码无效，请重新输入。");
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		String rCode = AppUtil.getRandomCode(phone, AppUtil.TYPE_SMS_ILLEGALNOTIFY_BIND);
		if(StringUtil.isNull(smscode)||!smscode.equals(rCode)){
			reJson.setCode(2);
			reJson.setMessage("短信验证码错误");
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		
		String openid = session(GlobalSetting.GZH_OPENID);
		
		if(StringUtil.isNull(openid)){
			reJson.setCode(6);
			reJson.setMessage("非法登录,请关注微信：SKN智能生活"+"\t"+"公众号进行登录");
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		
		SubWxUser subWxUser = SubWxUserService.findIllegalNotifyWxUser(openid);
		if(subWxUser==null){
			subWxUser = new SubWxUser();
			subWxUser.setAddtime(new Date());
			subWxUser.setCh(SubWxUserService.CH_ILLEGAL_NOTIFY);
			subWxUser.setOpenid(openid);
			subWxUser.setPhone(phone);
			Ebean.getServer(GlobalDBControl.getDB()).save(subWxUser);
		}
		
		WxGzhService.sendIllegalBindMessage(openid, "您好，您的账号已成功绑定此微信", phone, StringUtil.getDateTimeStr(new Date()), "帐号绑定微信成功后，可以查看该账号下的智能锁设备信息及通知信息");
		
		reJson.setSuccess();
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
			String rdCode = StringUtil.mkNumRanmString(6);
			
			Logger.info("getSMSByPhone:"+rdCode+"\t"+phone);
			
			AppUtil.setRandomCode(phone, rdCode, AppUtil.TYPE_SMS_ILLEGALNOTIFY_BIND);
			SmsUtil.sendRandCode(phone, rdCode, AppUtil.TIMEOUT_CACHESMSCODE_MINUTE+"");
			reJson.setSuccess();
		}catch(Exception e){
			Logger.error("getSMSByPhone",e);
		}
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}
}
