package app.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.avaje.ebean.Ebean;

import models.IntelligentLockInDetail;
import models.IntelligentLockOutDetailCode;
import models.IntelligentLockProductCode;
import models.SmartAppUserDevice;
import models.SmartDevice;
import models.SmartDeviceLog;
import models.SmartDevicePassport;
import models.SmartDeviceType;
import util.GlobalDBControl;
import util.LogUtil;
import util.jedis.RedisUtil;

public class DeviceService {
	static String SMART_APP_USER_DEVICES = "smart:app:user:devices:";
	static String SMART_APP_USER_DEVICE = "smart:app:user:device:";
	static String SMART_APP_DEVICE = "smart:app:device:";
	static String SMART_APP_DEVICE_TYPE = "smart:app:device:type:";
	static String SMART_APP_DEVICE_LOGS = "smart:app:device:logs:";
	static String SMART_APP_DEVICE_PASSPORTS = "smart:app:device:passports:";
	
	public static int STATU_SMART_APP_DEVICE_LOG_NORMAL = 0;
	public static int STATU_SMART_APP_DEVICE_LOG_DEL = -1;//日志被用户删除
	
	public static int WARN_SMART_APP_DEVICE_LOG_NORMAL = 0;
	
	public static int OP_SMART_APP_DEVICE_LOG_MODIFYPSWNAME = 1000;//修改密码别名
	public static int OP_SMART_APP_DEVICE_LOG_DEL = 1001;//删除操作日志
	
	//设备开锁密码类型
	public static int TYPE_SMART_APP_DEVICE_PSW_NUMBER = 2;//数字密码
	public static int TYPE_SMART_APP_DEVICE_PSW_FINGER = 1;//指纹密码
	public static int TYPE_SMART_APP_DEVICE_PSW_ICCARD = 3;//IC卡密码
	
	public static int STATU_SMART_APP_DEVICE_PSW_NORMAL = 0;//正常
	public static int STATU_SMART_APP_DEVICE_PSW_ADD = 1;//添加中
	public static int STATU_SMART_APP_DEVICE_PSW_DEL = 2;//删除中
	
	public static int STATU_SMART_APP_USER_DEVICE_NORMAL = 0;//正常
	
	static int DBINDEX = 10;
	
	static int OVERTIME = 6;
	
	public static List<SmartAppUserDevice> findUserDevices(Integer userid){
		List<SmartAppUserDevice> list = RedisUtil.getInstance().getEntityStrToList(SMART_APP_USER_DEVICES+userid, SmartAppUserDevice.class, DBINDEX);
		if(list==null){
			list = Ebean.getServer(GlobalDBControl.getReadDB()).find(SmartAppUserDevice.class).where().eq("deviceappuser", userid).eq("status", STATU_SMART_APP_USER_DEVICE_NORMAL).findList();
			RedisUtil.getInstance().setEntityStr(SMART_APP_USER_DEVICES+userid, list, OVERTIME, DBINDEX);
		}
		return list;		
	}
	
	public static SmartAppUserDevice findUserDeviceNoCache(Integer deviceid){
		return Ebean.getServer(GlobalDBControl.getReadDB()).find(SmartAppUserDevice.class).where().eq("deviceid", deviceid).eq("status", STATU_SMART_APP_USER_DEVICE_NORMAL).findUnique();
	}
	
	public static SmartAppUserDevice findUserDevice(Integer userid,int deviceid){
		SmartAppUserDevice userDevice = null;
		List<SmartAppUserDevice> devices = findUserDevices(userid);
		for(SmartAppUserDevice device:devices){
			if(device.getDeviceid()==deviceid){
				userDevice = device;
				break;
			}
		}
		return userDevice;
	}
	
	public static void clearUserDevicesCache(Integer userid){
		RedisUtil.getInstance().deleteEntityStr(SMART_APP_USER_DEVICES+userid, DBINDEX);
	}
	
	public static Date getLockOutDBTimeNoCache(String deviceId){
		IntelligentLockProductCode intelligentLockProductCode = Ebean.getServer(GlobalDBControl.getDB()).createQuery(IntelligentLockProductCode.class, "find IntelligentLockProductCode where deviceid='"+deviceId+"'").findUnique();
		IntelligentLockOutDetailCode intelligentLockOutDetailCode = Ebean.getServer(GlobalDBControl.getDB()).createQuery(IntelligentLockOutDetailCode.class,"find IntelligentLockOutDetailCode where codeid='"+intelligentLockProductCode.getIdd()+"' order by idd desc").findList().get(0);
		return intelligentLockOutDetailCode.getAddtime();
	}
	

	public static void updateBuyTimeByDeviceIdNoCache(Date outTime, String deviceId) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		LogUtil.writeLog("4	-	"+sdf.format(outTime),"abc");
		Ebean.getServer(GlobalDBControl.getDB()).createUpdate(SmartDevice.class, "update SmartDevice set buytime='"+sdf.format(outTime)+"' where deviceid='"+deviceId+"' ").execute();
	}
	
	public static SmartDevice findDevice(Integer idd){
		SmartDevice device = RedisUtil.getInstance().getEntityStr(SMART_APP_DEVICE+idd, SmartDevice.class, DBINDEX);
		if(device==null){
			device = Ebean.getServer(GlobalDBControl.getReadDB()).find(SmartDevice.class).where().eq("idd", idd).findUnique();
			if(device!=null){
				RedisUtil.getInstance().setEntityStr(SMART_APP_DEVICE+idd, device, OVERTIME, DBINDEX);
			}
		}
		return device;
	}
	
	public static SmartDevice findDevice(String deviceid){
		SmartDevice device = RedisUtil.getInstance().getEntityStr(SMART_APP_DEVICE+deviceid, SmartDevice.class, DBINDEX);
		if(device==null){
			device = Ebean.getServer(GlobalDBControl.getReadDB()).find(SmartDevice.class).where().eq("deviceid", deviceid).findUnique();
			if(device!=null){
				RedisUtil.getInstance().setEntityStr(SMART_APP_DEVICE+deviceid, device, OVERTIME, DBINDEX);
			}
		}
		return device;
	}
	
	public static void clearDeviceCache(Integer idd){
		RedisUtil.getInstance().deleteEntityStr(SMART_APP_DEVICE+idd, DBINDEX);
	}
	//查找设备类型
	public static SmartDeviceType findDeviceType(Integer idd){
		SmartDeviceType deviceType = RedisUtil.getInstance().getEntityStr(SMART_APP_DEVICE_TYPE+idd, SmartDeviceType.class, DBINDEX);
		if(deviceType==null){
			deviceType = Ebean.getServer(GlobalDBControl.getReadDB()).find(SmartDeviceType.class).where().eq("idd", idd).findUnique();
			if(deviceType!=null){
				RedisUtil.getInstance().setEntityStr(SMART_APP_DEVICE_TYPE+idd, deviceType, OVERTIME, DBINDEX);
			}
		}		
		return 	deviceType;
	}
	
	public static List<SmartDeviceLog> findDeviceLogs(String deviceid){
		List<SmartDeviceLog> deviceLogs = RedisUtil.getInstance().getEntityStrToList(SMART_APP_DEVICE_LOGS+deviceid, SmartDeviceLog.class, DBINDEX);
		if(deviceLogs==null){
			deviceLogs = Ebean.getServer(GlobalDBControl.getReadDB()).find(SmartDeviceLog.class).where().eq("deviceid", deviceid).eq("status", STATU_SMART_APP_DEVICE_LOG_NORMAL).setMaxRows(50).order("idd desc").findList();
			RedisUtil.getInstance().setEntityStr(SMART_APP_DEVICE_LOGS+deviceid, deviceLogs, OVERTIME, DBINDEX);
		}
		return deviceLogs;	
	}
	
	//查看最新一条操作日志
	public static SmartDeviceLog  findLatelyDeviceLog(String deviceid){
		List<SmartDeviceLog> deviceLogs = findDeviceLogs(deviceid);
		if(deviceLogs.size()>0)
			return deviceLogs.get(0);
		return null;
	}
	
	public static void clearDeviceLogsCache(String deviceid){
		RedisUtil.getInstance().deleteEntityStr(SMART_APP_DEVICE_LOGS+deviceid, DBINDEX);
	}
	
	//获取所有开锁密码
	public static List<SmartDevicePassport> findDevicePsws(String deviceid){
		List<SmartDevicePassport> devicePassports = RedisUtil.getInstance().getEntityStrToList(SMART_APP_DEVICE_PASSPORTS+deviceid, SmartDevicePassport.class, DBINDEX);
		if(devicePassports==null){
			devicePassports = Ebean.getServer(GlobalDBControl.getReadDB()).find(SmartDevicePassport.class).where().eq("deviceid", deviceid).findList();
			RedisUtil.getInstance().setEntityStr(SMART_APP_DEVICE_PASSPORTS+deviceid, devicePassports, OVERTIME, DBINDEX);
		}
		return devicePassports;		
	}
	
	//根据密码类型获取密码
	public static List<SmartDevicePassport> findDevicePswWithType(String deviceid,int type){
		List<SmartDevicePassport> list = new ArrayList<SmartDevicePassport>();
		List<SmartDevicePassport> devicePsws = findDevicePsws(deviceid);
		for(SmartDevicePassport devicePsw:devicePsws){
			if(devicePsw.getType()==type)
				list.add(devicePsw);
		}
		return list;
	}
	
	//根据密码类型获取密码
	public static SmartDevicePassport findDevicePswWithId(String deviceid,int idd){
		SmartDevicePassport devicePassport = null;
		List<SmartDevicePassport> devicePsws = findDevicePsws(deviceid);
		for(SmartDevicePassport devicePsw:devicePsws){
			if(devicePsw.getIdd()==idd)
				devicePassport = devicePsw;
		}
		return devicePassport;
	}
	
	public static SmartDevicePassport findDevicePswWithPsw(String deviceid,String psw){
		SmartDevicePassport devicePassport = null;
		List<SmartDevicePassport> devicePsws = findDevicePsws(deviceid);
		for(SmartDevicePassport devicePsw:devicePsws){
			if(psw.equals(devicePsw.getPassword()))
				devicePassport = devicePsw;
		}
		return devicePassport;
	}
	
	public static void clearDevicePsws(String deviceid){
		RedisUtil.getInstance().deleteEntityStr(SMART_APP_DEVICE_PASSPORTS+deviceid, DBINDEX);
	}
	
	public static void addDeviceLog(String deviceid,int op,String descinfo){
		SmartDeviceLog deviceLog = new SmartDeviceLog();
		deviceLog.setAddtime(new Date());
		deviceLog.setDeviceid(deviceid);
		deviceLog.setOp(op);
		deviceLog.setDescinfo(descinfo);
		
		deviceLog.setStatus(STATU_SMART_APP_DEVICE_LOG_NORMAL);
		deviceLog.setWarn(WARN_SMART_APP_DEVICE_LOG_NORMAL);
		Ebean.getServer(GlobalDBControl.getDB()).save(deviceLog);
	}
	
}
