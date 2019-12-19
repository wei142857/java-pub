package controllers;

import models.SmartAppUser;
import models.SubOrders;
import models.SubWxUser;
import pay.weixin.api.oauth.CallBackUrl;
import pay.weixin.api.oauth.SnsAccessTokenApi;
import play.mvc.Controller;
import play.mvc.Result;
import util.GlobalSetting;
import app.service.SubOrdersService;
import app.service.SubWxUserService;
import app.service.UserService;
import app.util.AppUtil;

//SKN智能生活 云浩公众号预约安装授权
public class GzhOAuthAction extends Controller {
	static String APPID = "wx5e9ec41fe9170796";
	static String SECRET = "d224c4eb6c15299c9dfe6d94a1281367";

	public static Result toOAuth() {
		String state = request().getQueryString("state");
		String openid = session(GlobalSetting.GZH_OPENID);
		if(openid!=null){
			if(state.startsWith("1")){
				SubWxUser subWxUser = SubWxUserService.findSubWxUser(openid);
				if(subWxUser!=null){
					if("102".equals(state)){
						SubOrders subOrder = SubOrdersService.findToInstallSubOrder(subWxUser.getPhone());
						if(subOrder!=null){
							return redirect(CallBackUrl.get("102"));
						}
						if(SubOrdersService.findAlreadyInstallSubOrder(subWxUser.getPhone())>0){
							return redirect(CallBackUrl.get("105"));
						}
					}
					
					if("103".equals(state)){
						SubOrders subOrder = SubOrdersService.findToMeasureSubOrder(subWxUser.getPhone());
						if(subOrder!=null){
							return redirect(CallBackUrl.get("103"));
						}
						if(SubOrdersService.findAlreadyMeasureSubOrder(subWxUser.getPhone())>0){
							return redirect(CallBackUrl.get("106"));
						}
					}
					
					//没有订单跳转扫码页面
					return redirect(CallBackUrl.get("104")+"?state="+state);
				}
				//跳转登录页面
				return redirect(CallBackUrl.get("101")+"?state="+state);
			}else if(state.startsWith("2")){//非法开通用户通知
				return redirect(CallBackUrl.get(state));
			}else if(state.startsWith("3")){
				SubWxUser subWxUser = SubWxUserService.findIllegalNotifyWxUser(openid);
				if(subWxUser!=null){//绑定成功跳转后续功能页
					return redirect(CallBackUrl.get(state));
				}
				return redirect(CallBackUrl.get("303")+"?state="+state);//跳转登录页
			}
			
		}
		return redirect(SnsAccessTokenApi
				.getAuthorizeURL(APPID, controllers.routes.GzhOAuthAction
						.oAuth().absoluteURL(request()), state, true));
	}

	public static Result oAuth() {
		String state = request().getQueryString("state");
		String code = request().getQueryString("code");
		if (code != null) {
			String openid = SnsAccessTokenApi.getSnsAccessToken(APPID, SECRET, code)
					.getOpenid();
			session(GlobalSetting.GZH_OPENID,
					openid);
			if(state.startsWith("1")){
				SubWxUser subWxUser = SubWxUserService.findSubWxUser(openid);
				if(subWxUser!=null){
					if("102".equals(state)){
						SubOrders subOrder = SubOrdersService.findToInstallSubOrder(subWxUser.getPhone());
						if(subOrder!=null){
							return redirect(CallBackUrl.get("102"));
						}
						if(SubOrdersService.findAlreadyInstallSubOrder(subWxUser.getPhone())>0){
							return redirect(CallBackUrl.get("105"));
						}
					}
					
					if("103".equals(state)){
						SubOrders subOrder = SubOrdersService.findToMeasureSubOrder(subWxUser.getPhone());
						if(subOrder!=null){
							return redirect(CallBackUrl.get("103"));
						}
						if(SubOrdersService.findAlreadyMeasureSubOrder(subWxUser.getPhone())>0){
							return redirect(CallBackUrl.get("106"));
						}
					}
					//没有订单跳转扫码页面
					return redirect(CallBackUrl.get("104")+"?state="+state);
				}
				//跳转登录页面
				return redirect(CallBackUrl.get("101")+"?state="+state);
			}else if(state.startsWith("2")){//非法开通用户通知
				return redirect(CallBackUrl.get(state));
			}else if(state.startsWith("3")){//公众号锁
				SubWxUser subWxUser = SubWxUserService.findIllegalNotifyWxUser(openid);
				if(subWxUser!=null){//绑定成功跳转后续功能页
					//生成商城所用token保存在session中
					SmartAppUser user = UserService.findUserByPhone(subWxUser.getPhone());
					if(user!=null){
						String token = UserService.makeToken(user.getAppid(), "sys", user.getPhone(), user.getIdd());
						UserService.setUserByToken(token, user);
						session(GlobalSetting.TOKEN,
								token);
					}
					
					return redirect(CallBackUrl.get(state));
				}
				return redirect(CallBackUrl.get("303")+"?state="+state);//跳转登录页
			}
			
		}
		
		return ok("授权失败");

	}
}
