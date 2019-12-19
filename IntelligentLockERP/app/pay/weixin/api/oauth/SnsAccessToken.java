package pay.weixin.api.oauth;

import java.io.Serializable;
import java.util.Map;

import pay.util.RetryUtils.ResultCheck;
import util.json.JsonUtil;

/**
 * SnsAccessToken
 * 封装 access_token
 */
public class SnsAccessToken implements ResultCheck, Serializable
{

    private static final long serialVersionUID = 6369625123403343963L;

    private String access_token;    // 正确获取到 access_token 时有值
    private Integer expires_in;        // 正确获取到 access_token 时有值
    private String refresh_token;    //
    private String openid;    //
    private String scope;    //
    private String unionid;    //
    private Integer errcode;        // 出错时有值
    private String errmsg;            // 出错时有值

    private Long expiredTime;        // 正确获取到 access_token 时有值，存放过期时间
    private String json;

    public SnsAccessToken(String jsonStr)
    {
        this.json = jsonStr;

        try
        {
            @SuppressWarnings("unchecked")
            Map<String, Object> temp = (Map<String, Object>) JsonUtil.parseJson(jsonStr);
            access_token = (String) temp.get("access_token");
            expires_in = getInt(temp, "expires_in");
            refresh_token = (String) temp.get("refresh_token");
            openid = (String) temp.get("openid");
            unionid = (String) temp.get("unionid");
            scope = (String) temp.get("scope");
            errcode = getInt(temp, "errcode");
            errmsg = (String) temp.get("errmsg");

            if (expires_in != null)
                expiredTime = System.currentTimeMillis() + ((expires_in - 5) * 1000);

        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public String getJson()
    {
        return json;
    }

    private Integer getInt(Map<String, Object> temp, String key) {
    	String number = (String)temp.get(key);
        return number == null ? null : Integer.valueOf(number);
    }

    public boolean isAvailable()
    {
        if (expiredTime == null)
            return false;
        if (errcode != null)
            return false;
        if (expiredTime < System.currentTimeMillis())
            return false;
        return access_token != null;
    }

    public String getAccessToken()
    {
        return access_token;
    }

    public Integer getExpiresIn()
    {
        return expires_in;
    }

    public String getRefresh_token()
    {
        return refresh_token;
    }

    public String getOpenid()
    {
        return openid;
    }

    public String getScope()
    {
        return scope;
    }

    public Integer getErrorCode()
    {
        return errcode;
    }

    public String getErrorMsg()
    {
        if (errcode != null)
        {
            String result = ReturnCode.get(errcode);
            if (result != null)
                return result;
        }
        return errmsg;
    }

    public String getUnionid()
    {
        return unionid;
    }

    @Override
    public boolean matching() {
        return isAvailable();
    }

}