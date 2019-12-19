package controllers;

import java.net.URLEncoder;

import pay.weixin.api.oauth.SnsAccessTokenApi;
import play.mvc.Controller;
import play.mvc.Result;
import util.GlobalSetting;
//商城支付微信浏览器支付授权
public class WxOAuthAction extends Controller {
	static String APPID = "wx74d793a7a80d86a3";
	static String SECRET = "b93de68d23ff203da6333f20279f1073";

	public static Result toOAuth() {
		String state = request().getQueryString("state");
		String openid = session(GlobalSetting.WX_OPENID);
		String ref = request().getQueryString("ref");
		
		if(ref==null){
			ref = request().getHeader("referer");
		}
		
		if(openid!=null){
			return redirect(ref);
		}
		
		try{
			ref = URLEncoder.encode(ref,"UTF-8");
		}catch(Exception e){
			
		}
		
		return redirect(SnsAccessTokenApi
				.getAuthorizeURL(APPID, controllers.routes.WxOAuthAction
						.oAuth().absoluteURL(request())+"?ref="+ref, state, true));
	}

	public static Result oAuth() {
		String ref = request().getQueryString("ref");
		String code = request().getQueryString("code");
		if (code != null) {
			session(GlobalSetting.WX_OPENID,
					SnsAccessTokenApi.getSnsAccessToken(APPID, SECRET, code)
							.getOpenid());
		}
		response().setCookie("oauth", "ok");

		return redirect(ref);

	}
}
