package pay.properties;

import pay.alipay.AliPayApiConfig;
import pay.alipay.AliPayApiConfigKit;
import pay.util.Charsets;
import pay.weixin.api.WxPayApiConfig;
import pay.weixin.api.WxPayApiConfig.PayModel;
import pay.weixin.api.WxPayApiConfigKit;
import play.libs.F;
import play.mvc.Http;
import play.mvc.SimpleResult;

public class AliPayProperties extends play.mvc.Action<SimpleResult> {
    public F.Promise<SimpleResult> call(Http.Context ctx) throws Throwable {
    	String app_id = "2019052065197254";
		String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnXAMgFUsW/UtPelWMXIekLntI1"
				+ "zm3LiJ5zj/KCpBkw5qWLpLcNEOO3QrUBUazAXe2Rfzt0BlD/NqAPHYK0uNUUf3cEq3+2/mBEnBy/WLLDv2Zrd8n"
				+ "yO5iYUtXp6iJcnDUQTGLTrwWyhOcXw6WFb8mg8OOAPOv+xLaLCdZ5KAIRePea+8suSbGk7ox/EhGd/b68IVNNxRp"
				+ "TYjvrWAeMevmDqlMfJbdq3dSKqT27/q8xsj7E1F4dIwZ9jIoBCAJoiOm6euDtDXAnhAUF89rKOS7SUmPxUS5jfOQ"
				+ "8a5eP3hTqVVfNOYY3wZKEXcLNVSbhnCI4k8h/W6ze1MDoxiLuFNXQIDAQAB";
		String private_key = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCGu6CdbAZ3BtHFkUECtlaYN/o64"
				+ "F82MAzPrrw03OZ831UZWt7shvdLO3t3Wdde95TRkFG2KwAZAgRcOa4Yny2Stoogv86KY7dqEVrWuYN74B/B5GPyP"
				+ "urodWpBhwYJrAX735AwvKe4o59tYdsi3/J1OUSw8vRmBXucvA8qQUo6AIKP/jzPB4ptwQSOhGl8WL0cQPCJQedQW"
				+ "/pgC9UboP3Gak9ziZdQGz7BHc8YAV8iKKKLXJ1ZwikBeWi+UUKU4jjIj286j/YRi9GNDeDA+dqTQYkz9m3fqvOor"
				+ "0VpjafPh0Wau4MWznzDMvG6YGMZPwUvHFRM9juw4RijizDlCKWzAgMBAAECggEAFIxK+5c9CSweE/6KJqq+XCq1X"
				+ "ANaIf6EBayFgZ5q/IJnDR3hv7r5vhyNQO+PvCkQJkHXZGv8cNPw7EvTtGeaYLS8LjByhIKet2t8KiFS3zKYRU7au"
				+ "1hDJT66/NFl51W39+jpqbUr8ynpJ5pz/EahSKV6R25WPbT6XDYGLhCVDHFL6me5uK6NQo3aBvw1kIA5jgl/Ic3E1"
				+ "WBi/8duGWZVX1Y93RqP13wt+exLJGRpdr99MjjfWhUBT73tezLlzklszstddefyeJLUTXCtpvlBEhCmYqqIpmhEd"
				+ "KM3vM9aVfcxrvHihez9hyTnlNusscGpChyXh+b4a4Mae4AEczCnKQKBgQDeBlSowa/DugAABmbNizysATTzasfiy"
				+ "uBpzdgBMpoL9dp7d6sQfOpwZr9/BCln0buJF/RBKCGJDs4PTuCEf8IBx5C3F168TBgQHXjgGYJr9LNbVSSQjcwms"
				+ "wPOa21YphphXo+TOD40Gzd1EnxYzir+v3SKhrKIzzuBsOw5MfRZ1QKBgQCbWbLl7QM0RWKySwx1yULyFPMdR/JA4"
				+ "xeC8Ns8d1IfvzTLhdWj8g20Z5Pko8yL49Q+XcNZBN9q1Vy2WvdP5DmezpWmJnxjvMMBOv/bWGBnshro9r6Eza9K2"
				+ "Vmy+idmdhi2E8QDi3z1y5lE03OciAcR/k12AggqlVU3f7wXMq39ZwKBgQDHhsjuH6HlM3Jsk1Qvw8FykmMTnhKkL"
				+ "c4fR22nJeJqfTcrXg/NcXT+LlhJvWO4eTMfsPdA1t/MXBGykCAU8vtWvhG/eLEaZwf484N9YW9jtTS6D3ocrlGZN"
				+ "admCgTqddufRWuh0hmUmiDshQBajmz1IvmIxCQN7q57jpO7krkVQQKBgFk/FMmGefrDicnH16arMuyEII7H0GJ6k"
				+ "/1DhY+dNO7MF/Z8Mpn26uyjPJFzkIUlI7+Yeitf40IqxRJ1+/JHuNqE7+/92CQsQxMn4+kNQoUQCumfJyCAUZ38c"
				+ "82gbJYubk/b1xX6QpWqm6iwmZBpjTZHFBUhYbOlCsnRdisVkGMtAoGBAMajFOfj1rhcl1qPVyDe2PrmYXTb18/U/"
				+ "Wc1gOQPLzvwOEmHRZj1DVG8oKrzZ7oud5X17pggEXnKQ2zLXPTDaQwap7HtB5w/vLaNfnQ7zHUwAQ3q96XgtEmBk"
				+ "9HkMX0RFl9R3R1oh4cpi+3LpAPYD4ZK8rSO7S7DgEWvE8R31J67";
		String sign_type = "RSA2";
		String service_url = "https://openapi.alipay.com/gateway.do";
		AliPayApiConfig aliPayApiConfig = AliPayApiConfig.New()
				.setAppId(app_id)
				.setAlipayPublicKey(alipay_public_key)
				.setCharset(Charsets.UTF_8.name())
				.setPrivateKey(private_key)
				.setServiceUrl(service_url)
				.setSignType(sign_type)
				.build();
		AliPayApiConfigKit.setThreadLocalAliPayApiConfig(aliPayApiConfig);
        return delegate.call(ctx);
    }
}
