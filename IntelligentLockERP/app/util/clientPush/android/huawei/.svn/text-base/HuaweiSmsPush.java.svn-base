package util.clientPush.android.huawei;

import java.util.HashMap;
import java.util.Map;

import util.LogUtil;
import util.classEntity.StringTool;
import util.http.HttpClientUtil;
import util.http.common.HttpConfig;
import util.http.common.HttpHeader;
import util.jedis.RedisUtil;
import util.json.JsonUtil;

public class HuaweiSmsPush {
	private static final String APP_ID = "100838909";// 根据自己华为开发者联盟中应用服务->PUSH->产品名称(XXX)->服务信息->APP
														// ID
	private static final String APP_SECRET = "c2d87c75e38dd24614423d312475080ea5f6ec78c1ae33c5ebcfd6861d639250";// 根据自己华为开发者联盟中应用服务->PUSH->产品名称(XXX)->服务信息->APP
																												// SECRET
	private static final String ACCESS_TOKEN_URL = "https://login.cloud.huawei.com/oauth2/v2/token";
	private static final String PUSH_URL = "https://push-api.cloud.huawei.com/v1/"
			+ APP_ID + "/messages:send";
	private static final String ACCESS_TOKEN = "accesstoken:huawei";// 已经获得的accessToken
	private static final int DBINDEX = 6;

	@SuppressWarnings("unchecked")
	public static String getAccessToken() {
		String accessToken = RedisUtil.getInstance().getEntityStr(ACCESS_TOKEN,
				String.class, DBINDEX);
		if (accessToken == null) {
			try {
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("grant_type", "client_credentials");
				params.put("client_secret", APP_SECRET);
				params.put("client_id", APP_ID);
				String jsonStr = HttpClientUtil
						.post(HttpConfig
								.custom()
								.url(ACCESS_TOKEN_URL)
								.headers(
										HttpHeader
												.custom()
												.contentType(
														"application/x-www-form-urlencoded")
												.build()).map(params));
				if (jsonStr != null) {
					Map<String, String> result = (Map<String, String>) JsonUtil
							.parseJson(jsonStr);
					if (result.containsKey("access_token")) {
						accessToken = result.get("access_token");
						RedisUtil.getInstance().setEntityStr(
								ACCESS_TOKEN,
								accessToken,
								StringTool.GetInt(result.get("expires_in"),
										3600), DBINDEX);
					}

				}
			} catch (Exception e) {
				LogUtil.writeLog("getaccesstoken" + e.toString(),
						"huaweismspush");
			}
		}

		return accessToken;
	}

	public static void SmsPushWithUrl(String phone, String deviceToken,
			String title, String content, Integer mid, String url) {
		try {
			String accessToken = getAccessToken();
			String payload = Payload.custom().token(deviceToken).title(title)
					.body(content).notifyId(mid).url(url).build();
			String result = HttpClientUtil
					.post(HttpConfig
							.custom()
							.url(PUSH_URL)
							.headers(
									HttpHeader
											.custom()
											.contentType(
													"Content-Type: application/json;charset=utf-8")
											.authorization(accessToken).build())
							.json(payload));
			LogUtil.writeLog(deviceToken + "\t" + phone + "\t" + content + "\t"
					+ result + "\tPUSH", "huaweismspush");
		} catch (Exception e) {
			LogUtil.writeLog("SmsPush" + e.toString(), "huaweismspush");
		}

	}

	public static void SmsPush(String phone, String deviceToken, String title,
			String content, Integer mid) {
		try {
			String accessToken = getAccessToken();
			String payload = Payload.custom().token(deviceToken).title(title)
					.body(content).notifyId(mid).build();
			String result = HttpClientUtil
					.post(HttpConfig
							.custom()
							.url(PUSH_URL)
							.headers(
									HttpHeader
											.custom()
											.contentType(
													"Content-Type: application/json;charset=utf-8")
											.authorization(accessToken).build())
							.json(payload));
			LogUtil.writeLog(deviceToken + "\t" + phone + "\t" + content + "\t"
					+ result + "\tPUSH", "huaweismspush");
		} catch (Exception e) {
			LogUtil.writeLog("SmsPush" + e.toString(), "huaweismspush");
		}

	}

	public static void main(String args[]) {
		SmsPush("18600212340",
				"AJFLU6yTj5zzZNoEFMiBcP4lghLHZ5J2I4b9KjKATKgXOnhWhVAjogqgbX91rZvzQ7Dqni9FCWCCjXlZ5ZMZJ0RJdXFYHJuHvDr1vHNJfgi5OdgUtrfAz_OmryRxQVfQTg",
				"test", "test push", 1);
	}
}
