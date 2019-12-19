package ServiceDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.SubWxUser;
import util.classEntity.StringTool;
import util.http.HttpClientUtil;
import util.http.common.HttpConfig;
import util.http.exception.HttpProcessException;
import util.jedis.RedisUtil;
import util.json.JsonUtil;
import app.service.SubWxUserService;

/**
 * 用于公众号开发 获取accessToken 模板消息推送
 * 
 * @author xuml
 *
 */
public class WxGzhService {
	public static String APPID = "wx5e9ec41fe9170796";
	public static String SECRET = "d224c4eb6c15299c9dfe6d94a1281367";
	static String ACCESSTOKENURL = "https://api.weixin.qq.com/cgi-bin/token"
			+ "?grant_type=client_credential&appid=" + APPID + "&secret="
			+ SECRET;
	static String TEMPMESSAGEURL = "https://api.weixin.qq.com/cgi-bin/message/template/send";

	// 获取公众接口凭证
	@SuppressWarnings("unchecked")
	public static String getAccessToken() {
		String accessToken = RedisUtil.getInstance().getEntityStr(APPID,
				String.class);
		if (accessToken == null) {
			try {
				String jsonStr = HttpClientUtil.get(HttpConfig.custom().url(
						ACCESSTOKENURL));
				if (jsonStr != null) {
					Map<String, String> result = (Map<String, String>) JsonUtil
							.parseJson(jsonStr);
					if (result.containsKey("access_token")) {
						accessToken = result.get("access_token");
						RedisUtil.getInstance().setEntityStr(
								APPID,
								accessToken,
								StringTool.GetInt(result.get("expires_in"),
										7200), 6);
					}
				}
			} catch (HttpProcessException e) {
			}
		}
		return accessToken;
	}

	public static void sendMessage(String openid, String template_id,
			Map<String, Map<String, String>> data, String url) {
		String accessToken = getAccessToken();
		if (accessToken != null) {
			try {
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("touser", openid);
				params.put("template_id", template_id);
				params.put("data", data);
				if (url != null) {
					params.put("url", url);
				}

				HttpClientUtil.post(HttpConfig.custom()
						.url(TEMPMESSAGEURL + "?access_token=" + accessToken)
						.json(JsonUtil.parseObj(params)));
			} catch (HttpProcessException e) {

			}
		}
	}

	public static void sendMessage(String openid, String template_id,
			Map<String,Map<String,String>> data) {
		sendMessage(openid, template_id, data, null);
	}

	@SuppressWarnings("serial")
	public static void sendGzhMessage(String phone, String first, String k1,
			String k2, String k3, String remark) {
		String template_id = "DZR42rPvWfusN2kpWwWZxAOvaJzVG1AP8gcgg7cH8p8";
		Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>();
		data.put("first", new HashMap<String, String>() {
			{
				put("value", first);
				put("color", "#173177");
			}
		});
		data.put("keyword1", new HashMap<String, String>() {
			{
				put("value", k1);
				put("color", "#173177");
			}
		});
		data.put("keyword2", new HashMap<String, String>() {
			{
				put("value", k2);
				put("color", "#173177");
			}
		});
		data.put("keyword3", new HashMap<String, String>() {
			{
				put("value", k3);
				put("color", "#173177");
			}
		});
		data.put("remark", new HashMap<String, String>() {
			{
				put("value", remark);
				put("color", "#173177");
			}
		});

		List<SubWxUser> users = SubWxUserService.findSubWxUsers(phone);
		for (SubWxUser user : users) {
			sendMessage(user.getOpenid(), template_id, data);
		}
	}
	
	@SuppressWarnings("serial")
	public static void sendIllegalMessage(String phone, String first, String k1,
			String k2, String remark) {
		String template_id = "cWEDI5m3svwU9TiDOsA8yBwQjSWlKH7HebcUiPI-ZVc";
		Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>();
		data.put("first", new HashMap<String, String>() {
			{
				put("value", first);
				put("color", "#173177");
			}
		});
		data.put("keyword1", new HashMap<String, String>() {
			{
				put("value", k1);
				put("color", "#173177");
			}
		});
		data.put("keyword2", new HashMap<String, String>() {
			{
				put("value", k2);
				put("color", "#173177");
			}
		});
		data.put("remark", new HashMap<String, String>() {
			{
				put("value", remark);
				put("color", "#173177");
			}
		});

		List<SubWxUser> users = SubWxUserService.findIllegalNotifyWxUsers(phone);
		for (SubWxUser user : users) {
			sendMessage(user.getOpenid(), template_id, data);
		}
	}
	@SuppressWarnings("serial")
	public static void sendIllegalBindMessage(String openid, String first, String k1,
			String k2, String remark) {
		String template_id = "WPBiAIdr35-VvyvUvIVPtl2PO7yvdrcuLvffUs5TDFo";
		Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>();
		data.put("first", new HashMap<String, String>() {
			{
				put("value", first);
				put("color", "#173177");
			}
		});
		data.put("keyword1", new HashMap<String, String>() {
			{
				put("value", k1);
				put("color", "#173177");
			}
		});
		data.put("keyword2", new HashMap<String, String>() {
			{
				put("value", k2);
				put("color", "#173177");
			}
		});
		data.put("remark", new HashMap<String, String>() {
			{
				put("value", remark);
				put("color", "#173177");
			}
		});

		sendMessage(openid, template_id, data);
	}
}
