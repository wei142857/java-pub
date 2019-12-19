package app.util;

import util.MD5;
import util.jedis.RedisUtil;

public class AppUtil {
	static int DBINDEX = 10; 
	public static int TIMEOUT_CACHESMSCODE_MINUTE = 4;
	static int TIMEOUT_CACHESMSCODE = TIMEOUT_CACHESMSCODE_MINUTE*60;
	static String SMART_APP_USER = "smart:app:user:";
	
	public static String TYPE_SMS_REGIST = "regist";
	public static String TYPE_SMS_LOGIN = "login";
	public static String TYPE_SMS_MODIFYPHONE = "modifyphone";
	public static String TYPE_SMS_DELDEVICE = "deldevice";
	public static String TYPE_SMS_EXCHANGE = "exchange";
	public static String TYPE_SMS_ESHOPlOGIN = "eshoplogin";
	public static String TYPE_SMS_ESHOPPAY = "eshoppay";
	public static String TYPE_SMS_APPOINT_LOGIN = "appointlogin";
	public static String TYPE_SMS_ILLEGALNOTIFY_BIND = "illegalnotify";
	
	public static void setRandomCode(String phone,String code,String act){
		RedisUtil.getInstance().setEntityStr(act+":"+phone, code, TIMEOUT_CACHESMSCODE, DBINDEX);
	}
	
	public static String getRandomCode(String phone,String act){
		String rCode = RedisUtil.getInstance().getEntityStr(act+":"+phone, String.class, DBINDEX);
//		RedisUtil.getInstance().deleteEntityStr(act+":"+phone, DBINDEX);//使用过就清除掉
		return rCode;
	}
	
	static long iddd =0;
	public static String encryptAppSign(String APPKEY,String appid,
			String password,String phonenum,String APPSECRET,int idd){
		
		return idd+MD5.mkMd5(idd+"_"+(iddd++)+
				System.currentTimeMillis()+APPKEY+appid
				+phonenum+APPSECRET);
	}
	/**
	 * 获取总页数
	 * @param count
	 * @param pagecount
	 * @return
	 */
	public static Integer getTotalPage(Integer count,Integer pagecount){
		if(count%pagecount!=0)
			return count/pagecount+1;
		else
			return count/pagecount;
	}
}
