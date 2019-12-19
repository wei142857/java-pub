package util.clientPush;

import org.apache.axis.utils.StringUtils;

import util.LogUtil;
import util.clientPush.exception.IosSmsPushException;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import com.notnoop.apns.ApnsServiceBuilder;
import com.notnoop.apns.EnhancedApnsNotification;

public class IosSmsPush {
	
	private static String certificatePathAppStore =  "/share/skn/skn.p12"; //在mac系统下导出的证书
	private static String certificatePassword = "Abcd1234"; 
	
	
	public static void SmsPush( String phone, String deviceToken,String content,Integer mid){
		
		if(StringUtils.isEmpty(deviceToken))
			return;
		
		String payload = APNS.newPayload()
                .badge(1)
                .alertBody(content)
                .customField("mid", mid)
                .build();

		int identify = EnhancedApnsNotification.INCREMENT_ID();
		
		EnhancedApnsNotification notification = new EnhancedApnsNotification(identify,
				EnhancedApnsNotification.MAXIMUM_EXPIRY,
				deviceToken,
                payload);
		
		getService().push(notification);
			
		
		LogUtil.writeLog( deviceToken+"\t"+identify+"\t"+phone+"\t"+content+"\tPUSH" ,"iospush");
		
	}
	
	public static void SmsPushWithUrl( String phone, String deviceToken,String content,Integer mid,String url){
		
		if(StringUtils.isEmpty(deviceToken))
			return;
		
		String payload = APNS.newPayload()
                .badge(1)
                .alertBody(content)
                .alertTitle("智能生活")
                .badge(1) 
                .customField("mid", mid)
                .customField("msgtype", 2)
                .customField("url", url)
                .build();
		
		int identify = EnhancedApnsNotification.INCREMENT_ID();
		
		EnhancedApnsNotification notification = new EnhancedApnsNotification(identify,
				EnhancedApnsNotification.MAXIMUM_EXPIRY,
				deviceToken,
                payload);
		
		getService().push(notification);
		
		LogUtil.writeLog( deviceToken+"\t"+identify+"\t"+phone+"\t"+content+"\tPUSH" ,"iospush");
	}
	
	private static ApnsService service;
	
	private static ApnsService getService(){
		if (service == null) {
			service =
					new ApnsServiceBuilder().asBatched()
			            .withCert(certificatePathAppStore, certificatePassword)
			            .withDelegate(new NotificationDelegate())
			            .withProductionDestination()
			            .build();
		}
		return service;
	}
	
	
	public static void main(String[] args) throws IosSmsPushException {
		certificatePathAppStore = "D://comsinobyteflowbank_distribution_push.p12";
		certificatePassword = "chinaunicom";
		String phone = "";
		String deviceToken = "a021790bf16ac5cd5cb0daa7c99d5b27d7fd620588c3a3b8194b42eac7f5dfe1";
		String content = "test push";
		SmsPush(phone, deviceToken, content, 10);
	}

}
