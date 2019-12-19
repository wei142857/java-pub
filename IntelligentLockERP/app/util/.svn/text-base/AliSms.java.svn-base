package util;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
/*
pom.xml
<dependency>
  <groupId>com.aliyun</groupId>
  <artifactId>aliyun-java-sdk-core</artifactId>
  <version>4.0.3</version>
</dependency>
*/
public class AliSms {
    public static void sendSmsCode(String phone,String code) {
        DefaultProfile profile = DefaultProfile.getProfile("default", "LTAIiWoDz7715CIw", "RoeLcLiDjJkINxAgn1SmX75Y4BAvG7");
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        //request.setProtocol(ProtocolType.HTTPS);
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", "聚淘科技");
        request.putQueryParameter("TemplateCode", "SMS_159781576");
        request.putQueryParameter("TemplateParam", "	{\"code\":\""+code
        		+ "\"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
            util.LogUtil.writeLog(phone+"\t"+code+"\t"+response.getData(), "sms");
            //{"Message":"OK","RequestId":"0BC9C232-7281-4C11-95C0-F0E66E424062","BizId":"425113752361932091^0","Code":"OK"}
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
    
    /*
     * 智能锁电量通知
		模版CODE:
			SMS_175240680
		模版内容:
			您的门锁电量已低于${eq}，请及时更换电池！
     */
    public static void sendPowerInfo(String phone,String eq) {
    	String key  = "sendPowerInfoSMS:"+phone;
		if(util.jedis.RedisUtil.getInstance().getEntity(key, String.class)!=null)
			return ;
		
		util.jedis.RedisUtil.getInstance().setEntity(key, "1",60*2 );
    	
        DefaultProfile profile = DefaultProfile.getProfile("default", "LTAIiWoDz7715CIw", "RoeLcLiDjJkINxAgn1SmX75Y4BAvG7");
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        //request.setProtocol(ProtocolType.HTTPS);
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", "聚淘科技");
        request.putQueryParameter("TemplateCode", "SMS_175240680");
        request.putQueryParameter("TemplateParam", "	{\"eq\":\""+eq
        		+ "\"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
            util.LogUtil.writeLog(phone+"\tpower\t"+eq+"\t"+response.getData(), "sms");
            //{"Message":"OK","RequestId":"0BC9C232-7281-4C11-95C0-F0E66E424062","BizId":"425113752361932091^0","Code":"OK"}
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
    
    //报警信息
    public static void sendAlarmInfo(String phone,String lockname) {
    	String key  = "sendAlarmInfoSMS:"+phone;
		if(util.jedis.RedisUtil.getInstance().getEntity(key, String.class)!=null)
			return ;
		
		util.jedis.RedisUtil.getInstance().setEntity(key, "1",60*2 );
    	
        DefaultProfile profile = DefaultProfile.getProfile("default", "LTAIiWoDz7715CIw", "RoeLcLiDjJkINxAgn1SmX75Y4BAvG7");
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        //request.setProtocol(ProtocolType.HTTPS);
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", "聚淘科技");
        request.putQueryParameter("TemplateCode", "SMS_175490409");
        request.putQueryParameter("TemplateParam", "	{\"lockname\":\""+lockname
        		+ "\"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
            util.LogUtil.writeLog(phone+"\t Alarm\t"+"\t"+response.getData(), "sms");
            //{"Message":"OK","RequestId":"0BC9C232-7281-4C11-95C0-F0E66E424062","BizId":"425113752361932091^0","Code":"OK"}
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
}
