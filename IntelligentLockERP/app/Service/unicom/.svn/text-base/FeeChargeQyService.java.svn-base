package Service.unicom;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import util.LogUtil;
import util.http.HttpClientUtil;
import util.http.common.HttpConfig;
import util.http.common.HttpHeader;
import util.http.exception.HttpProcessException;
import util.json.JsonUtil;
import Service.unicom.dto.IFResponse;

public class FeeChargeQyService {
	
	static String FEECHARGE = "/rechargeIntf/singleRecharge";
	static String FEECHARGEQUERY = "/rechargeIntf/queryRechargeState";

	// 充值手机话费
	public static IFResponse feeCharge(String orderid, String phone,
			String money, String url, String appKey, String appSecret) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("appKey", appKey);
		params.put("phone", phone);
		params.put("money", money);
		params.put("busiId", orderid);
		params.put("signature", makeSign(params, appSecret));
		String result = null;
		try {
			result = HttpClientUtil.post(HttpConfig
					.custom()
					.headers(
							HttpHeader
									.custom()
									.contentType(
											"application/json; charset=utf-8")
									.build()).timeout(60 * 1000)
					.url(url + FEECHARGE).encoding("UTF-8").json(JsonUtil.parseObj(params)));
		} catch (HttpProcessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		StringBuffer sb = new StringBuffer();
		int i = 1;
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			if (entry.getValue() != null) {
				sb.append(entry.getKey() + " = " + entry.getValue());
				if (i != params.size())
					sb.append(",");
			}
			i++;
		}
		LogUtil.writeLog("feecharge" + "\t" + sb.toString() + "\t" + result,
				"feechargeqyservice");
		IFResponse resp = new IFResponse();
		if (result != null) {
			resp.returninfo = result;
			Map<?, ?> ret = JsonUtil.parseJson(result);
			if (ret.containsKey("data")) {
				Map<?, ?> data = (Map<?, ?>) ret.get("data");
				if (data.containsKey("id"))
					resp.orderId = (String) ((Map<?, ?>) ret.get("data"))
							.get("id");
				if (data.containsKey("status"))
					resp.status = (String) data.get("status");
			}
			if (ret.containsKey("code")) {
				resp.code = (String) ret.get("code");
			}
			if (ret.containsKey("msg")) {
				resp.msg = (String) ret.get("msg");
			}
		}
		return resp;
	}

	// 查询充值结果
	public static IFResponse queryCharge(String orderid, String url,
			String appKey, String appSecret) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("appKey", appKey);
		params.put("id", orderid);
		params.put("signature", makeSign(params, appSecret));

		String result = null;
		try {
			result = HttpClientUtil.post(HttpConfig
					.custom()
					.headers(
							HttpHeader
									.custom()
									.contentType(
											"application/json; charset=utf-8")
									.build()).timeout(60 * 1000)
					.url(url + FEECHARGEQUERY).json(JsonUtil.parseObj(params)));
		} catch (HttpProcessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		StringBuffer sb = new StringBuffer();
		int i = 1;
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			if (entry.getValue() != null) {
				sb.append(entry.getKey() + " = " + entry.getValue());
				if (i != params.size())
					sb.append(",");
			}
			i++;
		}
		LogUtil.writeLog("querycharge" + "\t" + sb.toString() + "\t" + result,
				"feechargeqyservice");
		IFResponse resp = new IFResponse();
		if (result != null) {
			resp.returninfo = result;
			Map<?, ?> ret = JsonUtil.parseJson(result);
			if (ret.containsKey("data")) {

				Map<?, ?> data = (Map<?, ?>) ret.get("data");
				if (data.containsKey("id"))
					resp.orderId = (String) ((Map<?, ?>) ret.get("data"))
							.get("id");
				if (data.containsKey("status"))
					resp.status = (String) data.get("status");
			}
			if (ret.containsKey("code")) {
				resp.code = (String) ret.get("code");
			}
			if (ret.containsKey("msg")) {
				resp.msg = (String) ret.get("msg");
			}
		}

		return resp;
	}

	// 做Md5加密
	private static String makeSign(Map<String, Object> params, String appsecret) {
		Object keys[] = params.keySet().toArray();
		Arrays.sort(keys);
		StringBuilder sb = new StringBuilder();
		for (Object key1 : keys) {
			String kk = (String) key1;
			String vv = (String) params.get(kk);
			if (kk.equals("signature"))
				continue;
			if (vv != null && !"".equals(vv))
				sb.append(key1).append(vv);
		}
		sb.append("key=").append(appsecret);
		return util.MD5.mkMd5(sb.toString()).toUpperCase();
	}
}
