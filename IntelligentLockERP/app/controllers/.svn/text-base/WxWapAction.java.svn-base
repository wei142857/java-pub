package controllers;

import java.util.HashMap;
import java.util.Map;

import ServiceDao.WxGzhService;
import pay.ext.kit.PaymentKit;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import util.SHA1;
import util.classEntity.StringTool;
import util.http.HttpClientUtil;
import util.http.common.HttpConfig;
import util.http.exception.HttpProcessException;
import util.jedis.RedisUtil;
import util.json.JsonUtil;
//获取微信分享的功能
public class WxWapAction extends Controller{
	static String TICKETURL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket"
			+ "?type=jsapi";
	//简单实现获取微信分享签名
	@SuppressWarnings("unchecked")
	public static Result makeSign(){
		String url = request().getQueryString("url");
		String accessToken = WxGzhService.getAccessToken();
		if(accessToken!=null){
			String ticket = RedisUtil.getInstance().getEntityStr(accessToken, String.class, 6);
			try {
				String jsonStr = HttpClientUtil.get(HttpConfig.custom().url(TICKETURL + "&access_token=" + accessToken));
				Map<String, String> result = (Map<String, String>)JsonUtil.parseJson(jsonStr);
				if(StringTool.GetInt(result.get("errcode"), -1)==0){
					ticket = (String)result.get("ticket");
					RedisUtil.getInstance().setEntityStr(accessToken, ticket, StringTool.GetInt(result.get("expires_in"),7200), 6);
				}
			} catch (HttpProcessException e) {
			}
			if(ticket!=null){
				Map<String, String> packageParams = new HashMap<String, String>();
				packageParams.put("appId", WxGzhService.APPID);
				String nonceStr = PaymentKit.generateNonceStr();
				packageParams.put("nostr", nonceStr);
				String timestamp = System.currentTimeMillis() / 1000
						+ "";
				packageParams.put("timestamp", timestamp);
				String sign = "jsapi_ticket="+ticket+"&noncestr="+nonceStr+"&timestamp="+timestamp+"&url="+url;
				packageParams.put("sign", SHA1.Encrypt(sign));
				return ok(Json.toJson(packageParams));
			}
		}
		return ok("");
	}
}
