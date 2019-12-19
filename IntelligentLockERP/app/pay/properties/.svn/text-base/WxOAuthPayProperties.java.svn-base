package pay.properties;

import pay.weixin.api.oauth.ApiConfig;
import pay.weixin.api.oauth.ApiConfigKit;
import play.libs.F;
import play.mvc.Http;
import play.mvc.SimpleResult;

public class WxOAuthPayProperties extends play.mvc.Action<SimpleResult> {
    public F.Promise<SimpleResult> call(Http.Context ctx) throws Throwable {
    	String appid = "wx74d793a7a80d86a3";
		String secret = "b93de68d23ff203da6333f20279f1073";

		ApiConfig apiConfig = new ApiConfig();
		apiConfig.setAppId(appid);
		apiConfig.setAppSecret(secret);

		ApiConfigKit.putApiConfig(apiConfig);
        return delegate.call(ctx);
    }
}
