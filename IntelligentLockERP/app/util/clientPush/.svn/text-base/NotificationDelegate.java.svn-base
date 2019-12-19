package util.clientPush;

import util.LogUtil;

import com.notnoop.apns.ApnsDelegate;
import com.notnoop.apns.ApnsNotification;
import com.notnoop.apns.DeliveryError;
import com.notnoop.apns.internal.Utilities;

public class NotificationDelegate implements ApnsDelegate {
    @Override
    public void messageSent(ApnsNotification apnsNotification, boolean b) {
		LogUtil.writeLog( Utilities.encodeHex(apnsNotification.getDeviceToken())+"\t"+apnsNotification.getIdentifier()+"\tMESSAGE SUCCESS" ,"iospush");
    }

    @Override
    public void messageSendFailed(ApnsNotification apnsNotification, Throwable throwable) {
        LogUtil.writeLog( Utilities.encodeHex(apnsNotification.getDeviceToken())+"\t"+apnsNotification.getIdentifier()+"\tMESSAGE FAILED" ,"iospush");
    }

    @Override
    public void connectionClosed(DeliveryError deliveryError, int i) {
    	LogUtil.writeLog( "delevery Error :" +deliveryError ,"iospush");
    }

    @Override
    public void cacheLengthExceeded(int i) {

    }

    @Override
    public void notificationsResent(int i) {
    	LogUtil.writeLog( "MESSAGE RESENT\t"+i ,"iospush");
    }
}