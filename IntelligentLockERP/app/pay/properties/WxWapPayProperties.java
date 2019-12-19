package pay.properties;

import pay.weixin.api.WxPayApiConfig;
import pay.weixin.api.WxPayApiConfig.PayModel;
import pay.weixin.api.WxPayApiConfigKit;
import play.libs.F;
import play.mvc.Http;
import play.mvc.SimpleResult;

public class WxWapPayProperties extends play.mvc.Action<SimpleResult> {
    public F.Promise<SimpleResult> call(Http.Context ctx) throws Throwable {
    	String appid = "wx74d793a7a80d86a3";
		String mch_id = "1538086861";
		String partnerKey = "39p6x7pMe8ofrfmDNACZ4Dn3LiGttL6v";

		WxPayApiConfig apiConfig = WxPayApiConfig.New().setAppId(appid)
				.setMchId(mch_id)
				// 商户平台设置的API密钥key
				.setPaternerKey(partnerKey).setPayModel(PayModel.BUSINESSMODEL);
		
		WxPayApiConfigKit.setThreadLocalWxPayApiConfig(apiConfig);
		WxPayApiConfigKit.setThreadLocalAppId(appid);
        return delegate.call(ctx);
    }
}
