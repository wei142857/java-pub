package app.action;

import static play.data.Form.form;
import app.dto.ReturnJson;
import app.service.UserService;
import app.util.AppUtil;
import models.EShopLoginForm;
import models.SmartAppUser;
import play.Logger;
import play.data.Form;
import play.mvc.Result;
import play.mvc.SimpleResult;
import util.AjaxHellper;
import util.GlobalSetting;
import util.SmsUtil;
import util.StringUtil;
import util.json.JsonUtil;

//EShop login
public class EShopLogin extends BaseAction{
	final static Form<EShopLoginForm> eshopLoginForm = form(EShopLoginForm.class);
	public static SimpleResult index = redirect("/public/app/eshop/EShopProductMallIndex.html");//商城首页
	
	//登陆
	public static Result doLogin() {
		Form<EShopLoginForm> form = eshopLoginForm.bindFromRequest();
		EShopLoginForm data = form.get();
		
		//表单验证结果
		if((data.validMsg=checkForm(data))!=null) {
			return ok(data.validMsg);
		}
		
		setSession(data);
		
		return index;
	}
	//把用户信息存session
	public static final void setSession(EShopLoginForm data) {
		SmartAppUser user = UserService.addUser(data.phone, UserService.TYPE_SMART_APP_USER_MOBILE,util.AjaxHellper.getIpAddr(request()), "","","" ,"", "", "", "", "");
		
		String token = UserService.makeToken(user.getAppid(), data.sms, data.phone, user.getIdd());
		
		session("token", token);
		
//		RedisUtil.getInstance().setEntity(token, user,60*2);
		UserService.setUserByToken(token, user);
		
		response().setCookie("token", token);
	}
	
	//获取Post请求中的参数
	public static final String getParameter(String param) {
		return AjaxHellper.getHttpParamOfFormUrlEncoded(request(), param);
	}
	
	//校验表单
	public static final String checkForm(EShopLoginForm data) {
		
		if(data.phone==null||data.phone.equals("")||
				data.validate==null||data.validate.equals("")||
					data.sms==null||data.sms.equals(""))
			
			return "表单有误";
		String sessionCode = session(GlobalSetting.Session_Validate_Image);
		if(sessionCode==null) {
			return "请刷新页面";
		}
		if(!sessionCode.equalsIgnoreCase(data.validate))
			return "请输入正确的验证码";
		
		//校验成功 删除验证码
//		session().remove(GlobalSetting.Session_Validate_Image);
		
		String sms = AppUtil.getRandomCode(data.phone, AppUtil.TYPE_SMS_ESHOPlOGIN);
		if(!data.sms.equals(sms)){
			return "短信验证码错误";
		}
		
		return null;
	}
	//获取短信验证码
	public static final Result getSMSByPhone(){
		ReturnJson reJson = new ReturnJson();
		try{
			String phone = getParameter("phone");
			String validate = getParameter("validate");
		
			if(!StringUtil.isMobileNO(phone)){
				reJson.setParamsError();
				reJson.setMessage("您输入的号码无效，请重新输入。");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			if(!session(GlobalSetting.Session_Validate_Image).equalsIgnoreCase(validate)) {
				reJson.setParamsError();
				reJson.setMessage("请输入正确的图形验证码");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			String rdCode = StringUtil.mkNumRanmString(6);
			AppUtil.setRandomCode(phone, rdCode, AppUtil.TYPE_SMS_ESHOPlOGIN);
			SmsUtil.sendRandCode(phone, rdCode, AppUtil.TIMEOUT_CACHESMSCODE_MINUTE+"");
			appLogger.debug("eshopLogin:"+rdCode+"\t"+phone);
			reJson.setSuccess();
		}catch(Exception e){
			Logger.error("getSMSByPhone",e);
		}
		
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}
	//前端ajax校验验证码
	public static final Result checkImgCode() {
		String imgCode = getParameter("imgCode");
		ReturnJson reJson = new ReturnJson();
		if(!session(GlobalSetting.Session_Validate_Image).equalsIgnoreCase(imgCode)) {
			reJson.setParamsError();
			reJson.setMessage("请输入正确的图形验证码");
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		
		reJson.setSuccess();
		String reContent = JsonUtil.parseObj(reJson);
		return ok(reContent);
	}
	/**
	 * 校验短信验证码
	 * @return
	 */
	public static final Result checkSmsCode() {
		String sms = getParameter("sms");
		String phone = getParameter("phone");
		ReturnJson reJson = new ReturnJson();
		String checkCode = AppUtil.getRandomCode(phone, AppUtil.TYPE_SMS_ESHOPlOGIN);
		if(checkCode==null||!checkCode.equals(sms)) {
			reJson.setParamsError();
			reJson.setMessage("短信验证码错误或者已过期");
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		
		reJson.setSuccess();
		String reContent = JsonUtil.parseObj(reJson);
		return ok(reContent);
	}
	public static final String getParameterByAjax(String param) {
		return StringUtil.getHttpParam(request(), param);
	}
}
