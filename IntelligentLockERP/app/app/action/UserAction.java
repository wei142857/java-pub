package app.action;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.PhoneNo;
import models.SmartAppUser;
import play.Logger;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import util.AjaxHellper;
import util.ExcelUtils;
import util.GlobalDBControl;
import util.ImageGenerator;
import util.ImageUploadUtil;
import util.SmsUtil;
import util.StringUtil;
import util.classEntity.StringTool;
import util.json.JsonUtil;
import util.picture.FileTypeJudge;
import app.dto.ReturnJson;
import app.dto.UserDto;
import app.service.UserService;
import app.util.AppUtil;

import com.avaje.ebean.Ebean;

import controllers.SysConfigAction;

public class UserAction extends BaseAction{
	//获取短信验证码
	public static Result getSMSByPhone(){
		ReturnJson reJson = new ReturnJson();
		try{
			Map<?, ?> params = decryptAesBody2Map();
			if(params==null)
				return unauthorized();
			if( !checkSign(params) )
				return makSignFailor();
			String appid = (String) params.get("appid");
			String phone = (String) params.get("phone");
			String token = (String) params.get("token");
			String imgcode = (String)params.get("imgcode");
			Integer type = StringTool.GetInt((String) params.get("type"), 0);
			if(StringUtil.isNull(phone) || type==null){
				reJson.setParamsError();
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			if(!StringUtil.isMobileNO(phone)){
				reJson.setParamsError();
				reJson.setMessage("您输入的号码无效，请重新输入。");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			String rdCode = StringUtil.mkNumRanmString(6);
			String phones = SysConfigAction.findSysconfig("智能锁", "测试号码");
			if(!StringUtil.isNull(phones) && phones.indexOf(phone)!=-1){
				rdCode = "147258";
			}
			appLogger.debug("rdCode:"+rdCode+"\t"+type+"\t"+"imgCode:"+imgcode);
//			if(type==0){//注册
//				String code = UserService.getImgCode(phone);
//				if(StringUtil.isNull(imgcode) || !imgcode.equals(code)){
//					reJson.setCode(1);
//					reJson.setMessage("图形验证码错误");
//					String reContent = JsonUtil.parseObj(reJson);
//					appLogger.debug(reContent);
//					return ok(reContent);
//				}
//				UserService.clearImgCode(phone);
//				AppUtil.setRandomCode(phone, rdCode, AppUtil.TYPE_SMS_REGIST);
//				SmsUtil.sendRandCode(phone, rdCode, AppUtil.TIMEOUT_CACHESMSCODE_MINUTE+"");
//			}
			if(type==1){//登陆短信
				String code = UserService.getImgCode(phone);
				if(StringUtil.isNull(imgcode) || !imgcode.equalsIgnoreCase(code)){
					reJson.setCode(1);
					reJson.setMessage("图形验证码错误");
					String reContent = JsonUtil.parseObj(reJson);
					appLogger.debug(reContent);
					return ok(reContent);
				}
				UserService.clearImgCode(phone);
				AppUtil.setRandomCode(phone, rdCode, AppUtil.TYPE_SMS_LOGIN);
				SmsUtil.sendRandCode(phone, rdCode, AppUtil.TIMEOUT_CACHESMSCODE_MINUTE+"");
			}
			if(type==2){//修改登陆手机短信
				AppUtil.setRandomCode(phone, rdCode, AppUtil.TYPE_SMS_MODIFYPHONE);
				SmsUtil.sendRandCode(phone, rdCode, AppUtil.TIMEOUT_CACHESMSCODE_MINUTE+"");
			}
			if(type==3){//删除设备
				AppUtil.setRandomCode(phone, rdCode, AppUtil.TYPE_SMS_DELDEVICE);
				SmsUtil.sendRandCode(phone, rdCode, AppUtil.TIMEOUT_CACHESMSCODE_MINUTE+"");
			}
			if(type==4){//兑换话费
				AppUtil.setRandomCode(phone, rdCode, AppUtil.TYPE_SMS_EXCHANGE);
				SmsUtil.sendRandCode(phone, rdCode, AppUtil.TIMEOUT_CACHESMSCODE_MINUTE+"");
			}
			reJson.setSuccess();
		}catch(Exception e){
			Logger.error("getSMSByPhone",e);
		}
		
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}
	//登陆
	public static Result login(){
		if( !checkSign() )
			return makSignFailor();
		ReturnJson reJson = new ReturnJson();
		try{
			String phone = decryptAes2String("phone");
			String smscode = request().getQueryString("smscode");
			String appid = request().getQueryString("appid");
			String appos = request().getQueryString("appos");//android_5.0.1
			String apptoken = request().getQueryString("apptoken");
			String deviceid = request().getQueryString("deviceid");
			String phonetype = request().getQueryString("phonetype");//机型 iphone6s-Plus
			String idfa = request().getQueryString("idfa");
			if(StringUtil.isNull(phone) || StringUtil.isNull(smscode)){
				reJson.setParamsError();
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			String terver = getHeader(request(), "terver");
			String channel = getHeader(request(), "channel");

			String rCode = AppUtil.getRandomCode(phone, AppUtil.TYPE_SMS_LOGIN);
			if(!smscode.equals(rCode)){
				reJson.setCode(1);
				reJson.setMessage("短信验证码错误");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			
			
			SmartAppUser user = UserService.addUser(phone, UserService.TYPE_SMART_APP_USER_MOBILE, 
					util.AjaxHellper.getIpAddr(request()), appid,appos,channel ,terver, apptoken, deviceid, phonetype, idfa);
			String token = UserService.makeToken(appid, smscode, phone, user.getIdd());
			UserService.setUserByToken(token, user);
			UserDto data = new UserDto();
			data.setToken(token);
			data.setNickname(user.getNickname());
			data.setAvatarurl(user.getLogo());
			data.setPushstatus(user.getPushstate());
			reJson.setSuccess();
			reJson.setData(data);
		}catch(Exception e){
			Logger.error("login", e);
		}
		
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}
	
	//注册
	public static Result regist(){
		if( !checkSign() )
			return makSignFailor();
		ReturnJson reJson = new ReturnJson();
		
		String phone = decryptAes2String("phone");
		String smscode = request().getQueryString("smscode");
		String appid = request().getQueryString("appid");
		String appos = request().getQueryString("appos");//android_5.0.1
		String apptoken = request().getQueryString("apptoken");
		String deviceid = request().getQueryString("deviceid");
		String phonetype = request().getQueryString("phonetype");//机型 iphone6s-Plus
		String idfa = request().getQueryString("idfa");
		if(StringUtil.isNull(phone) || StringUtil.isNull(smscode)){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		String terver = getHeader(request(), "terver");
		String channel = getHeader(request(), "channel");
		
		String rCode = AppUtil.getRandomCode(phone, AppUtil.TYPE_SMS_REGIST);
		if(!smscode.equals(rCode)){
			reJson.setCode(1);
			reJson.setMessage("短信验证码错误");
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		
		SmartAppUser user = UserService.addUser(phone, UserService.TYPE_SMART_APP_USER_MOBILE, 
				util.AjaxHellper.getIpAddr(request()), appid,channel, appos,terver, apptoken, deviceid, phonetype, idfa);
		String token = UserService.makeToken(appid, smscode, phone, user.getIdd());
		UserDto data = new UserDto();
		data.setToken(token);
		data.setNickname(user.getNickname());
		data.setAvatarurl(user.getLogo());
		reJson.setSuccess();
		reJson.setData(data);
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}
	//获取用户信息
	public static Result getUserInfo(){
		if( !checkSign() )
			return makSignFailor();
		ReturnJson reJson = new ReturnJson();
		String token = request().getQueryString("token");
		String appid = request().getQueryString("appid");
		if(StringUtil.isNull(token)){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		SmartAppUser user = UserService.findUserByToken(token);
		if(user==null){
			reJson.setTokenTimeOut();
		}else{
			UserDto data = new UserDto();
			data.setNickname(user.getNickname());
			data.setAvatarurl(user.getLogo());
			data.setPhonenum(user.getPhone());
			reJson.setSuccess();
			reJson.setData(data);
		}
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);		
	}
	//设置用户昵称
	public static Result setUserInfo(){
		if( !checkSign() )
			return makSignFailor();
		ReturnJson reJson = new ReturnJson();
		String token = request().getQueryString("token");
		String nickname = request().getQueryString("nickname");
		if(StringUtil.isNull(token) || StringUtil.isNull(nickname)){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		SmartAppUser user = UserService.findUserByToken(token);
		if(user==null){
			reJson.setTokenTimeOut();
		}else{
			Ebean.getServer(GlobalDBControl.getDB()).createSqlUpdate("update smart_app_user set nickname=:nickname where idd=:idd")
			.setParameter("nickname", nickname)
			.setParameter("idd", user.getIdd()).execute();
			
			user.setNickname(nickname);
			UserService.setUserByPhone(user.getPhone(), user);
			Map<String,String> data = new HashMap<String,String>();
			data.put("nickname", nickname);
			reJson.setSuccess();
			reJson.setData(data);
		}
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}
	//设置用户头像
	public static Result setUserAvatarUrl(){
		appLogger.debug(request().body().toString());
		appLogger.debug(AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "token"));
		appLogger.debug(request().getQueryString("sign"));
		if( !checkSign() )
			return makSignFailor();
		ReturnJson reJson = new ReturnJson();
		String token = request().getQueryString("token");
		String appid = request().getQueryString("appid");
		if(StringUtil.isNull(token)){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		SmartAppUser user = UserService.findUserByToken(token);
		if(user==null){
			reJson.setTokenTimeOut();
		}else{
			try{
				MultipartFormData body = request().body().asMultipartFormData();
				FilePart logo = body.getFile("filename");
				File file = logo.getFile();
				if(ExcelUtils.getSize(file)>2048){
					file.delete();//将临时文件删除掉
					reJson.setCode(1);
					reJson.setMessage("文件最大不能超过2M");
					String reContent = JsonUtil.parseObj(reJson);
					appLogger.debug(reContent);
					return ok(reContent);
				}
				String logoName = logo.getFilename();
				String suffix = logoName.substring(logoName.toLowerCase().lastIndexOf(".")+1).toLowerCase();//文件后缀名
				if(suffix.equals("jpg"))
					suffix = "jpeg";
				String fileType = (FileTypeJudge.getType( file.getAbsolutePath() )+"").toLowerCase();
				if(fileType.equals("jpg"))
					fileType = "jpeg";
				List<String> allowFileTypes = new ArrayList<String>();
				allowFileTypes.add("jpeg");
				allowFileTypes.add("png");
				
				if(!fileType.equals(suffix) || !allowFileTypes.contains(suffix) || !allowFileTypes.contains(fileType)){
					file.delete();//将临时文件删除掉
					reJson.setCode(1);
					reJson.setMessage("文件格式不正确，请上传(.jpg、.jpeg、.png)格式的文件");
					String reContent = JsonUtil.parseObj(reJson);
					appLogger.debug(reContent);
					return ok(reContent);
				}
				
				
				
				if(!StringUtil.isNull(user.getLogo()))//删除旧头像
					new File(user.getLogo()).delete();
				
				String path = ImageUploadUtil.putImg(logo, "userlogo");
				
				Ebean.getServer(GlobalDBControl.getDB()).createSqlUpdate("update smart_app_user set logo=:logo where idd=:idd")
				.setParameter("logo", path)
				.setParameter("idd", user.getIdd()).execute();
				
				
				user.setLogo(path);
				UserService.setUserByPhone(user.getPhone(), user);
				Map<String,String> data = new HashMap<String,String>();
				data.put("avatarurl", path);
				reJson.setSuccess();
				reJson.setData(data);
			}catch(Exception e){
				Logger.error("setUserAvatarUrl",e);
			}
			
		}		
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}
	//修改手机号
	public static Result setLoginPhone(){
		if( !checkSign() )
			return makSignFailor();
		ReturnJson reJson = new ReturnJson();
		String token = request().getQueryString("token");
		String appid = request().getQueryString("appid");
		String phone = decryptAes2String("phone");
		String smscode = request().getQueryString("code");
		if(StringUtil.isNull(token) || StringUtil.isNull(phone) || StringUtil.isNull(smscode)){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		SmartAppUser user = UserService.findUserByToken(token);
		if(user==null){
			reJson.setTokenTimeOut();
		}else{
			String rCode = AppUtil.getRandomCode(phone, AppUtil.TYPE_SMS_MODIFYPHONE);
			if(!smscode.equals(rCode)){
				reJson.setCode(1);
				reJson.setMessage("短信验证码错误");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			if(phone==null || !StringUtil.isMobileNO(phone) ){
				reJson.setCode(2);
				reJson.setMessage("手机号格式错误");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			
			PhoneNo pn =  UserService.getHaoduan( phone );
			
			
			SmartAppUser newUser = UserService.findUserByPhone(phone);
			
			if(newUser!=null){
				reJson.setCode(3);
				reJson.setMessage("手机号已经注册，请更换手机号");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
//			if(newUser!=null){
//				Ebean.getServer(GlobalDBControl.getDB())
//				.createSqlUpdate("update smart_app_user_device set deviceAppUser=:newDeviceAppUser where deviceAppUser=:oldDeviceAppUser")
//				.setParameter("newDeviceAppUser", newUser.getIdd())
//				.setParameter("oldDeviceAppUser", user.getIdd())
//				.execute();
//				
//				Ebean.getServer(GlobalDBControl.getDB()).createSqlUpdate("delete from smart_app_msg where userid=:userid")
//				.setParameter("userid", user.getIdd()).execute();
//				
//				Ebean.getServer(GlobalDBControl.getDB()).createSqlUpdate("delete from smart_app_user where idd=:idd")
//				.setParameter("idd", user.getIdd()).execute();
//				
//				UserService.setUserByToken(token, newUser);
//			}else{
				Ebean.getServer(GlobalDBControl.getDB())
				.createSqlUpdate("update smart_app_user set phone=:phone,login=:login,prov=:prov,city=:city,yys=:yys where idd=:idd")
				.setParameter("phone", phone)
				.setParameter("login", phone)
				.setParameter("prov", pn.getProvince())
				.setParameter("city", pn.getCity())
				.setParameter("yys", pn.getType())
				.setParameter("idd", user.getIdd())
				.execute();
				
				Ebean.getServer(GlobalDBControl.getDB()).createSqlUpdate("update sub_wx_user set phone=:phone where phone=:odPhone")
				.setParameter("phone", phone)
				.setParameter("odPhone", user.getPhone()).execute();
				
				
				UserService.clearUserByPhone(user.getPhone());
				user.setPhone(phone);
				user.setProv(pn.getProvince());
				user.setCity(pn.getCity());
				user.setYys(pn.getType());
				UserService.setUserByToken(token, user);
				
//			}
			
			reJson.setSuccess();
		}
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}
	public static Result senddevicetoken(){
		if( !checkSign() )
			return makSignFailor();
		ReturnJson reJson = new ReturnJson();
		String token = request().getQueryString("token");
		String devicetoken = request().getQueryString("devicetoken");
		String appid = request().getQueryString("appid");
		if(StringUtil.isNull(token) || StringUtil.isNull(devicetoken)){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		SmartAppUser user = UserService.findUserByToken(token);
		if(user==null){
			reJson.setTokenTimeOut();
		}else{
			Ebean.getServer(GlobalDBControl.getDB()).createSqlUpdate("update smart_app_user set apptoken=:apptoken where idd=:idd")
			.setParameter("apptoken", devicetoken).setParameter("idd", user.getIdd()).execute();
			user.setApptoken(devicetoken);
			UserService.setUserByPhone(user.getPhone(), user);
			reJson.setSuccess();
		}
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}
	
	public static Result imageValidate(){
		if( !checkSign() )
			return makSignFailor();
		ReturnJson reJson = new ReturnJson();
		String phone = request().getQueryString("phone");
		String appid = request().getQueryString("appid");
		if(StringUtil.isNull(phone)){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		ImageGenerator igt = ImageGenerator.make();
		String imgCode = igt.imgCode;
		appLogger.debug("imgCode:"+imgCode);
		UserService.setImgCode(phone, imgCode);
		reJson.setSuccess();
		reJson.setData(igt.getImgBytes());
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}
	
	public static Result getIllegalPushState(){
		if( !checkSign() )
			return makSignFailor();
		ReturnJson reJson = new ReturnJson();
		String token = request().getQueryString("token");
		String appid = request().getQueryString("appid");
		String state = request().getQueryString("state");
		int nstate = StringTool.GetInt(state, 1);
		if(StringUtil.isNull(token) || nstate<0){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		SmartAppUser user = UserService.findUserByToken(token);
		if(user==null){
			reJson.setTokenTimeOut();
		}else{
			Map<String,String> data = new HashMap<String,String>();
			data.put("state", user.getIllegalpushstate());
			reJson.setSuccess();
			reJson.setData(data);
		}
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}
	
	public static Result setIllegalPushState(){
		if( !checkSign() )
			return makSignFailor();
		ReturnJson reJson = new ReturnJson();
		String token = request().getQueryString("token");
		String appid = request().getQueryString("appid");
		String state = request().getQueryString("state");
		if(StringUtil.isNull(token) || StringUtil.isNull(state)){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		SmartAppUser user = UserService.findUserByToken(token);
		if(user==null){
			reJson.setTokenTimeOut();
		}else{
			int i = Ebean.getServer(GlobalDBControl.getDB()).createSqlUpdate("update smart_app_user set illegalpushstate=:illegalpushstate where idd=:idd")
			.setParameter("illegalpushstate", state)
			.setParameter("idd", user.getIdd()).execute();
			if(i>0){
				user.setIllegalpushstate(state);
				UserService.setUserByPhone(user.getPhone(), user);
				reJson.setSuccess();
			}
		}
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}
	
	public static Result setPushState(){
		if( !checkSign() )
			return makSignFailor();
		ReturnJson reJson = new ReturnJson();
		String token = request().getQueryString("token");
		String appid = request().getQueryString("appid");
		String state = request().getQueryString("state");
		int nstate = StringTool.GetInt(state, 1);
		if(StringUtil.isNull(token) || nstate<0){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		SmartAppUser user = UserService.findUserByToken(token);
		if(user==null){
			reJson.setTokenTimeOut();
		}else{
			int i = Ebean.getServer(GlobalDBControl.getDB()).createSqlUpdate("update smart_app_user set pushstate=:pushstate where idd=:idd")
			.setParameter("pushstate", state)
			.setParameter("idd", user.getIdd()).execute();
			if(i>0){
				user.setPushstate(nstate);
				UserService.setUserByPhone(user.getPhone(), user);
				reJson.setSuccess();
			}
		}
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}
}
