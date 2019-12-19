package app.service;

import java.util.Date;

import models.NumPorting;
import models.PhoneNo;
import models.SmartAppUser;
import play.Logger;
import util.GlobalDBControl;
import util.StringUtil;
import util.jedis.RedisUtil;
import app.util.AppUtil;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlRow;

public class UserService {
	static int DBINDEX = 10;
	static int CACHESMSAPPUSER_TOKEN_TIMEOUT = 24*60*60*31;
	static int CACHEHAODUAN_TIMEOUT = 24*60*60*31;
	static String SMART_APP_USER_TOKEN = "smart:app:user:token:";
	static String SMART_APP_USER_PHONE = "smart:app:user:phone:";
	static String SMART_APP_USER_PHONE_IMGCODE = "smart:app:user:phone:imgcode:";
	static String NUMPORTING_PHONE = "numporting:phone:";
	static String HAODUAN_PHONE= "haoduan:phone:";
	
	public static int TYPE_SMART_APP_USER_MOBILE = 0;//手机登录
	
	static int STATUS_SMART_APP_USER_NORMAL = 0;
	
	
	static String DEFAULT_NICKNAME = "智能生活";
	static int STATE_SMART_APP_USER_PUSH_CLOSE = 1;
	static int STATE_SMART_APP_USER_PUSH_OPEN = 0;
	
	
	final static String APPKEY= "SINOBYTE_2Qw0_OIN#V";
	final static String APPSECRET="1020522719800";
	
	static String DEFAULT_URL_USERLOGO = "/public/images/default_logo.png";
	public static SmartAppUser findUserByToken(String token){
		if( token==null )
			return null;
		
		if(token.length()<32)
			return null;
		
		String key = token+token.substring(0, 2);
		String phone = RedisUtil.getInstance().getEntityStr(SMART_APP_USER_TOKEN+key, String.class, DBINDEX);
		
		return findUserByPhone(phone);
	} 
	
	public static void setUserByToken(String token,SmartAppUser user){
		String phone = user.getPhone();
		String key = token+token.substring(0, 2);
		RedisUtil.getInstance().setEntityStr(SMART_APP_USER_TOKEN+key, phone, CACHESMSAPPUSER_TOKEN_TIMEOUT, DBINDEX);
		setUserByPhone(phone, user);
	}
	
	public static SmartAppUser findUserByPhone(String phone){
		SmartAppUser user = RedisUtil.getInstance().getEntityStr(SMART_APP_USER_PHONE+phone, SmartAppUser.class, DBINDEX);
		if(user==null){
			user = Ebean.getServer(GlobalDBControl.getReadDB()).find(SmartAppUser.class).where().eq("phone", phone).findUnique();
			if(user!=null){
				RedisUtil.getInstance().setEntityStr(SMART_APP_USER_PHONE+phone, user, 60*5, DBINDEX);
			}
		}
		return user;
	}
	
	public static SmartAppUser findUserById(Integer userid){
		return Ebean.getServer(GlobalDBControl.getReadDB()).find(SmartAppUser.class).where().eq("idd", userid).findUnique();
	}
	
	public static void clearUserByPhone(String phone){
		RedisUtil.getInstance().deleteEntityStr(SMART_APP_USER_PHONE+phone, DBINDEX);
	}
	
	public static void setUserByPhone(String phone,SmartAppUser user){
		RedisUtil.getInstance().setEntityStr(SMART_APP_USER_PHONE+phone, user, 60*5, DBINDEX);
	}
	
	
	public static SmartAppUser addUser(String phone,int usertype,String ip
			,String appid,String appos,String channel,String appversion,String apptoken,String deviceid,String phonetype,String idfa){
		
		SmartAppUser user = findUserByPhone(phone);
		if(user!=null){
			String sql = "update smart_app_user set appid=:appid,appos=:appos,channel=:channel,appversion=:appversion,"
					+ "apptoken=:apptoken,deviceid=:deviceid,phonetype=:phonetype,idfa=:idfa,lastlogintime=sysdate() where idd=:idd";
			
			Ebean.getServer(GlobalDBControl.getDB()).createSqlUpdate(sql)
			.setParameter("appid", appid)
			.setParameter("appos", appos)
			.setParameter("channel", channel)
			.setParameter("appversion", appversion)
			.setParameter("apptoken", apptoken)
			.setParameter("deviceid", deviceid)
			.setParameter("phonetype", phonetype)
			.setParameter("idfa", idfa)
			.setParameter("idd", user.getIdd()).execute();
			return user;
		}
		if(phone==null || !StringUtil.isMobileNO(phone) )
			return null;
		
		if(user==null){
			user = new SmartAppUser();
			PhoneNo pn =  getHaoduan( phone );
			user.setProv(pn.getProvince());
			user.setCity(pn.getCity());
			user.setYys(pn.getType());
			user.setAddtime(new Date());
			user.setAppid(appid);
			user.setAppos(appos);
			user.setAppversion(appversion);
			user.setApptoken(apptoken);
			user.setDeviceid(deviceid);
			user.setIdfa(idfa);
			user.setIp(ip);
			user.setLastlogintime(new Date());
			user.setNickname(DEFAULT_NICKNAME);
			user.setPhone(phone);
			user.setPhonetype(phonetype);
			user.setPushstate(STATE_SMART_APP_USER_PUSH_CLOSE);
			user.setStatus(STATUS_SMART_APP_USER_NORMAL);
			user.setUsertype(usertype);
			user.setChannel(channel);
			
//			int index = StringUtil.getRandomInt(32);
			user.setLogo(DEFAULT_URL_USERLOGO);
			
			if(TYPE_SMART_APP_USER_MOBILE == usertype)
				user.setLogin(phone);
			Ebean.getServer(GlobalDBControl.getDB()).save(user);
		}
		return user;
	}
	
	public static SmartAppUser addUserGzh(String phone,int usertype,String ip){
		
		SmartAppUser user = findUserByPhone(phone);
		if(user!=null){
			String sql = "update smart_app_user set appid=:appid,lastlogintime=sysdate() where idd=:idd";
			Ebean.getServer(GlobalDBControl.getDB()).createSqlUpdate(sql)
			.setParameter("appid", 103)
			.setParameter("idd", user.getIdd()).execute();
			return user;
		}
		if(phone==null || !StringUtil.isMobileNO(phone) )
			return null;
		
		if(user==null){
			user = new SmartAppUser();
			PhoneNo pn =  getHaoduan( phone );
			user.setAppid("103");
			user.setProv(pn.getProvince());
			user.setCity(pn.getCity());
			user.setYys(pn.getType());
			user.setAddtime(new Date());
			user.setIp(ip);
			user.setLastlogintime(new Date());
			user.setNickname(DEFAULT_NICKNAME);
			user.setPhone(phone);
			user.setPushstate(STATE_SMART_APP_USER_PUSH_CLOSE);
			user.setStatus(STATUS_SMART_APP_USER_NORMAL);
			user.setUsertype(usertype);
			
//			int index = StringUtil.getRandomInt(32);
			user.setLogo(DEFAULT_URL_USERLOGO);
			
			if(TYPE_SMART_APP_USER_MOBILE == usertype)
				user.setLogin(phone);
			Ebean.getServer(GlobalDBControl.getDB()).save(user);
		}
		return user;
	}
	
	public static PhoneNo getHaoduan(String phone)
	{
		if(phone==null||phone.length()<11)
			return null;
		PhoneNo pn = null;
		String str=phone.trim().substring(0,7);
		
		NumPorting nump = RedisUtil.getInstance().getEntityStr(NUMPORTING_PHONE+phone, NumPorting.class,0);
		if(nump==null){
			nump = Ebean.getServer(GlobalDBControl.getReadDB()).find(NumPorting.class).where().eq("phone", phone).findUnique();
		}
		
		if(nump!=null){
			RedisUtil.getInstance().setEntityStr(NUMPORTING_PHONE+phone,nump,60*30,0);
			pn = new PhoneNo();
			pn.setNumber(Integer.parseInt(str));
			pn.setType("联通");
			pn.setProvince(StringUtil.isNull(nump.getProvince())?"未知":nump.getProvince());
			pn.setCity(StringUtil.isNull(nump.getCity())?"未知":nump.getCity());
			return pn;
		}
				
		
		//################手机号到redis取号段#####################
		pn = RedisUtil.getInstance().getEntityStr(HAODUAN_PHONE + str, PhoneNo.class,DBINDEX);//取redis号段
		if(pn != null){
			return pn;
		}
		//################手机号到redis取号段#####################
		
		String str10010Sql = "SELECT number,province,city,type FROM phone_10010 where number = " + StringUtil.getSafesql(str);
		//查找phone_10010表号段
		SqlRow row = Ebean.getServer(GlobalDBControl.getReadDB()).createSqlQuery(str10010Sql).findUnique();
		
		if(row != null){
			pn = new PhoneNo();
			pn.setNumber(Integer.parseInt(str));
			pn.setType(row.getString("type"));
			pn.setProvince(row.getString("province"));
			pn.setCity(row.getString("city"));
			RedisUtil.getInstance().setEntityStr(HAODUAN_PHONE + str, pn, CACHEHAODUAN_TIMEOUT, DBINDEX);
			return pn;
		}
		//查找phone_csv表号段
		pn=Ebean.getServer(GlobalDBControl.getDB()).find(PhoneNo.class).where().eq("number",StringUtil.getSafesql(str)).findUnique();
		if(pn==null){
			pn = new PhoneNo();
			pn.setAreaNumber(0);
			pn.setType(getType(str));
			pn.setProvince("未知");
			pn.setCity("未知");
			Logger.info("从Mysql未取到:phone:"+phone + "***号段：" + pn.getNumber());
			return pn;
		}else{
			RedisUtil.getInstance().setEntityStr(HAODUAN_PHONE + str, pn, CACHEHAODUAN_TIMEOUT, DBINDEX);
			Logger.info("从Mysql取到:phone:"+phone + "***号段：" + pn.getNumber());
		}
		return pn;
	}
	
	static String telcomHead = "130,131,132,155,156,185,186,175,176,170,145,166"; 
	static String mobileHead = "139,138,137,136,135,134,159,150,151,158,157,188,187,152,182,183,147,184,178";
	public static String getType(String phone)
	{
		if( telcomHead.indexOf( phone.substring(0, 3) )!=-1 || RedisUtil.getInstance().sismember("numporting:phones", phone, 0) )
			return "联通";
		if( mobileHead.indexOf( phone.substring(0, 3) )!=-1 )
			return "移动";
		else
			return "电信";
	}
	
	public static String makeToken(String appid,String password,String phone,int idd){
		return "v5" + AppUtil.encryptAppSign(APPKEY, appid,
						password,  phone, APPSECRET, idd);
	}
	
	public static void setImgCode(String phone,String code){
		RedisUtil.getInstance().setEntityStr(SMART_APP_USER_PHONE_IMGCODE+phone, code, 60*2, DBINDEX);
	}
	public static String getImgCode(String phone){
		return RedisUtil.getInstance().getEntityStr(SMART_APP_USER_PHONE_IMGCODE+phone,String.class, DBINDEX);
	}
	public static void clearImgCode(String phone){
		RedisUtil.getInstance().deleteEntityStr(SMART_APP_USER_PHONE_IMGCODE+phone, DBINDEX);
	}
}
