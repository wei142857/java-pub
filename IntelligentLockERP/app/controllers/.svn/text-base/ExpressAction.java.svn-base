package controllers;

import java.util.HashMap;
import java.util.Map;

import play.mvc.Controller;
import play.mvc.Result;
import util.LogUtil;
import util.http.HttpClientUtil;
import util.http.common.HttpConfig;
import util.http.exception.HttpProcessException;

public class ExpressAction extends Controller {
	public static Result query() {
		String billCode = request().getQueryString("billCode");
		String result = null;
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("billCode", billCode);
		try {
			result = HttpClientUtil.post(HttpConfig.custom().url("https://hdgateway.zto.com/WayBill_GetDetail")
					.encoding("UTF-8").map(params));
		} catch (HttpProcessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		LogUtil.writeLog(billCode + "\t" + result, "expressquery");
		return ok(result);
	}
}
