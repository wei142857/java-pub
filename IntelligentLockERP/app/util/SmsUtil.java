package util;

import play.Logger;
import util.classEntity.StringTool;
import controllers.SysConfigAction;

public class SmsUtil {
	
	// 发送验证码
	public static void sendRandCode( String phone, String rcode,String min ) {
		if ( checkPhoneFlow(phone) ) {
			return ;
		}
		
		AliSms.sendSmsCode(phone, rcode);
	}
	
	// 检查一个IP的流量，每个小时内 20条，每天100条
	public static boolean checkIpFlow( String ip ) {
		int MAX_HOUR = StringTool.GetInt(
				SysConfigAction.findSysconfig("短信IP限流", "每小时每IP"), 20);
		int MAX_DAY = StringTool.GetInt(
				SysConfigAction.findSysconfig("短信IP限流", "每天每IP"), 100);

		// 小时检查
		if (FlowControl.checkHourAct(ip + "_SmsOverFlow", MAX_HOUR)) {
			Logger.error("!! ip sms in Hour --" + ip);
			util.LogUtil.writeLog("!! ip sms in Hour --" + ip, "smsIpflow");
			return true;
		}

		// 日检查
		if (FlowControl.checkDayAct(ip + "_SmsOverFlow", MAX_DAY)) {
			Logger.error("!! ip sms in day --" + ip);
			util.LogUtil.writeLog("!! ip sms in day --" + ip, "smsIpflow");
			return true;
		}
		return false;
	}

	public static boolean checkPhoneFlow(String ph) {
		int MAX_HOUR = StringTool.GetInt(
				SysConfigAction.findSysconfig("短信限流", "每小时每手机"), 3);
		int MAX_DAY = StringTool.GetInt(
				SysConfigAction.findSysconfig("短信限流", "每天每手机"), 10);

		// 小时检查
		if (FlowControl.checkHourAct(ph + "_SmsOverFlow", MAX_HOUR)) {
			Logger.error("!! ip sms in Hour --" + ph);
			util.LogUtil.writeLog("!! ip sms in Hour --" + ph, "smsflow");
			return true;
		}

		// 日检查
		if (FlowControl.checkDayAct(ph + "_SmsOverFlow", MAX_DAY)) {
			Logger.error("!! ip sms in day --" + ph);
			util.LogUtil.writeLog("!! ip sms in day --" + ph, "smsflow");
			return true;
		}
		return false;
	}

}
