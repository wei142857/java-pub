package net;

import java.util.Date;
import java.util.List;

import util.GlobalDBControl;
import util.LogUtil;
import util.MD5;
import util.classEntity.StringTool;
import util.jedis.RedisUtil;
import ServiceDao.JdbcOper;
import app.service.MessageService;

import com.avaje.ebean.Ebean;

import controllers.SysConfigAction;
import controllers.SysDictAction;
import models.SmartAppMsg;
import models.SmartAppUser;
import models.SmartAppUserDevice;
import models.SmartDevice;
import models.SmartDeviceLog;
import models.SmartDevicePassport;
import models.SmartNetMsg;

public class SmartService {

	static String REDIS_KEY_DEV_CONNECT = "Dev:connect:" ;
	static String REDIS_KEY_APP_CONNECT = "App:connect:" ;
	
	static String REDIS_KEY_DEV_Online = "online:" ;
	static String REDIS_KEY_DEV        = "Dev:" ;
	
	//#################################################################//
	//获取在线状态:0-不在线，1-在线
	public static int getDeviceOnline( String idd )
	{
		String key = REDIS_KEY_DEV_Online+idd;
		String sta = util.jedis.RedisUtil.getInstance().getEntity(key, String.class);
		return StringTool.GetInt(sta, 0);
	}
	
	//设置设备为在线状态
	public static void setDeviceOnline( String devId )
	{
		if( StringTool.isNull(devId))
			return;
		SmartDevice sd = findDevice( devId );
		if( sd!=null ){
			String key = REDIS_KEY_DEV_Online +  sd.getIdd();
			util.jedis.RedisUtil.getInstance().setEntity( key , "1" , 60 );
		}
	}
	
	//#################################################################//
	//获取设备连接中 的状态
	public static int getDeviceConnect( String deviceId )
	{
		String key = REDIS_KEY_DEV_CONNECT + deviceId ;
		String sta = util.jedis.RedisUtil.getInstance().getEntity(key, String.class);
		return StringTool.GetInt(sta, 0);
	}
	
	//设置设备为连接中 的状态
	public static void setDeviceConnect( String devId )
	{
		String key = REDIS_KEY_DEV_CONNECT +  devId;
		util.jedis.RedisUtil.getInstance().setEntity( key , "1" , 60 );
	}
	
	//获取 APP为连接中 的手机号
	public static String getAppConnect( String deviceId  )
	{
		String key = REDIS_KEY_APP_CONNECT + deviceId ;
		String phone = util.jedis.RedisUtil.getInstance().getEntity(key, String.class);
		return phone;
	}
	
	//设置APP 为连接中 的状态
	public static void setAppConnect( String deviceId,String phone )
	{
		String key = REDIS_KEY_APP_CONNECT +  deviceId;
		util.jedis.RedisUtil.getInstance().setEntity( key , phone , 120 );
	}
	
	//#################################################################//
	
	
	//根据设备id 查找设备
	public static SmartDevice findDevice(String devid)
	{
		String key = REDIS_KEY_DEV + devid ;
		SmartDevice smartdevice = util.jedis.RedisUtil.getInstance().getEntity(key, SmartDevice.class);
		if( smartdevice==null ){
			smartdevice = Ebean.getServer(GlobalDBControl.getDB())
				.find(SmartDevice .class).where().eq("deviceid", devid).findUnique();
			if( smartdevice != null )
				util.jedis.RedisUtil.getInstance().setEntity( key ,  smartdevice , 10);
		}
		return smartdevice;
	}
	
	//查找APP用户
	public static SmartAppUser findAppUser(String phone)
	{
		String key = "SmartAppUser:" + phone ;
		SmartAppUser smartU = util.jedis.RedisUtil.getInstance().getEntity(key, SmartAppUser.class);
		if( smartU==null ){
			smartU = Ebean.getServer(GlobalDBControl.getDB())
				.find(SmartAppUser .class).where().eq("phone", phone).findUnique();
			if( smartU != null )
				util.jedis.RedisUtil.getInstance().setEntity( key ,  smartU , 10);
		}
		return smartU;
	}
	
	//查找锁的管理员
	public static String findDeviceUser(int devid)
	{
		List<SmartAppUserDevice>  us = Ebean.getServer(GlobalDBControl.getDB())
				.find(SmartAppUserDevice .class).where().eq("deviceid", devid)
				.orderBy("idd desc")
				.findList();
		if( us.size() > 0 ){
			int uid =  us.get(0).getDeviceAppUser();
			try{
				return Ebean.getServer(GlobalDBControl.getDB())
					.find(SmartAppUser .class).where().eq("idd", uid).findUnique().getPhone();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
			
		return null;
	}
	
	//首次的连接 回复
	public static SmartNetMsg Connect(SmartNetMsg msg,SmartDevice smartdevice ) {
		msg.setDealed( 1 );
		msg.setDealtime( new Date() );
		msg.setLockid(msg.deviceId);
		Ebean.getServer( GlobalDBControl.getDB()).save( msg );
		
		//设置设备连接中状态
		SmartOperService.connectUser(msg.deviceId);
		
		return SmartMsgUtil.makeConnectReply(msg, smartdevice);
	}

	//记录锁端消息 到日志表
	static int MIN_POWER = 20; 
	
	/*
	 * 	1.密码、指纹、IC卡开锁：**开锁（**为密码的备注名，没有备注名时就显示默认名称）
		2.预警通知：警报-门锁发生非法开锁
		3.低电预警通知：警报-门锁电量过低请及时更换电池
		4.新增密码等：新增密码（指纹、IC）-**（**为密码的备注名，没有备注名时就显示默认名称）
		5.删除密码等：删除密码-**
	 */
	public static void recordLog(SmartNetMsg msg) {
		try{
			LogUtil.writeLog(msg.deviceId+"\t"+msg.getType()+"\t"+msg.info+"\t"+msg.getTag(), "recordLog");
			if( msg.getTag()== 0x11 || msg.getTag()== 0x21 )
				return;
			
			SmartDeviceLog log = new SmartDeviceLog();
			if(StringTool.isNull(msg.deviceId))
				log.setDeviceid( msg.getLockid() );
			else
				log.setDeviceid(msg.deviceId);
			
			log.setOp( msg.getType() );
			log.setDescinfo( msg.getExt() );
			
			if( msg.getTag() == 0x10 ){
				//锁端发起
				if( msg.getType()== SmartMsgUtil.CMD_LOCK_POWER  ){
					log.setExtinfo(""+msg.info );
					if( msg.info <= MIN_POWER ){
						log.setDescinfo("警报-门锁电量过低请及时更换电池");
						SmartDevice dev = SmartService.findDevice(msg.deviceId);
						if(dev==null){
							util.LogUtil.writeLog("power dev not found:"+msg.deviceId, "recordLog");
						}else{
							String phone = SmartService.findDeviceUser(dev.getIdd());
							if(!StringTool.isNull(phone)){
								MessageService.pushIllegalMsg(phone, dev.getDevicedesc(),msg.deviceId,msg.info,MessageService.TYPE_ILLEGAL_POWER);
							}
						}
					}
				}
				
				if( msg.getType()== SmartMsgUtil.CMD_LOCK_TIP  ){
					log.setExtinfo( SysDictAction.getDictString( SmartMsgUtil.DICT_TIP_TYPE, ""+msg.info));
				}
				
				if( msg.getType()== SmartMsgUtil.CMD_LOCK_ALARM  ){
					log.setExtinfo( SysDictAction.getDictString( SmartMsgUtil.DICT_ALARM_TYPE, ""+msg.info));
					log.setWarn(1);
					log.setDescinfo("警报-门锁发生非法开锁");
					SmartDevice dev = SmartService.findDevice(msg.deviceId);
					if(dev==null){
						util.LogUtil.writeLog("alarm dev not found:"+msg.deviceId, "recordLog");
					}else{
						String phone = SmartService.findDeviceUser(dev.getIdd());
						if(!StringTool.isNull(phone)){
							MessageService.pushIllegalMsg(phone,dev.getDevicedesc(),msg.deviceId,null,MessageService.TYPE_ILLEGAL_ALARM);
						}
					}
				}
				
				if( msg.getType()>= SmartMsgUtil.CMD_LOCK_OPEN && msg.getType() <= SmartMsgUtil.CMD_LOCK_DELUSER )
					log.setExtinfo(SysDictAction.getDictString( SmartMsgUtil.DICT_USR_TYPE, ""+msg.info) );
				
				/*********文字展示的修改 ******/
				//开锁
				if( SmartMsgUtil.CMD_LOCK_OPEN == msg.getType() ){
					log.setDescinfo( findUserInfoOfPassport(msg,log.getDeviceid()) +"开锁");
				}
				
				//追加用戶
				if( SmartMsgUtil.CMD_LOCK_ADDUSER == msg.getType() ){
					log.setDescinfo( "新增 ："+SysDictAction.getDictString( SmartMsgUtil.DICT_USR_TYPE, ""+msg.info)+
							StringTool.GetIntEx(msg.userCode,0, 16));
				}
				
				//刪除用戶
				if( SmartMsgUtil.CMD_LOCK_DELUSER == msg.getType() ){
					log.setDescinfo( "刪除："+findUserInfoOfPassport(msg,log.getDeviceid()));
				}
			}
			
			if( msg.getTag() == 0x20 ){
				//服务器发起
				if( msg.getType() == SmartMsgUtil.CMD_SVR_ADDUSER || msg.getType() == SmartMsgUtil.CMD_SVR_DELPASS )
					log.setExtinfo(SysDictAction.getDictString( SmartMsgUtil.DICT_USR_TYPE, msg.getExt1() ) );
				log.setOp( log.getOp() + 1000 );//区分上下行消息
			}
			
			//記錄日志
			if( !(msg.getType() == SmartMsgUtil.CMD_LOCK_TIME) //非查询时间
					&& !( msg.info > MIN_POWER && msg.getType()== SmartMsgUtil.CMD_LOCK_POWER )//非正常电量
					&& !( msg.getTag() == 0x20 )
					){
				Ebean.getServer(GlobalDBControl.getDB()).save(log);
			}
		
		}catch(Exception e){
			LogUtil.writeLog(e.toString(), "recordLog");
		}
	}
	
	//记录日志
	static void addDeviceLog(String lockId,int op,String msg)
	{
		SmartDeviceLog log = new SmartDeviceLog();
		log.setDeviceid( lockId );
		log.setOp( op );
		log.setDescinfo( msg );
		Ebean.getServer(GlobalDBControl.getDB()).save(log);
	}

	//獲取鎖的別名
	private static String findUserInfoOfPassport(SmartNetMsg msg,String lockId) {
		List<SmartDevicePassport> ls = Ebean.getServer(GlobalDBControl.getDB()).find(SmartDevicePassport.class)
				.where().eq("type", msg.info).eq("idx", msg.userCode)
				.eq("deviceid", lockId).orderBy("idd desc").findList();
		if(ls.size()>0)
			return SysDictAction.getDictString( SmartMsgUtil.DICT_USR_TYPE, ""+ls.get(0).getType())
					+ ls.get(0).getUserinfo();
		return SysDictAction.getDictString( SmartMsgUtil.DICT_USR_TYPE, ""+msg.info);
	}

	//查找设备的口令
	static SmartDevicePassport findPass(String dev,String pass)
	{
		List<SmartDevicePassport> ls = Ebean.getServer(GlobalDBControl.getDB()).find(SmartDevicePassport.class)
				.where().eq("password", pass).eq("deviceid", dev).orderBy("idd desc").findList();
		if( ls.size() > 0 )
			return ls.get(0);
		return null;
	}
	
	//根据锁的回复消息,记录初始消息 状态
	public static void dealReply(SmartNetMsg msg) {
		SmartNetMsg org = findOrgMsg( msg );
		if( org != null ){
			util.LogUtil.writeLog("find org:"+org.getIdd()+"\t"+msg.getMid(), "LockSocketThread");
			if( org.getType().intValue() == SmartMsgUtil.CMD_SVR_ADD_PASS || org.getType() == SmartMsgUtil.CMD_SVR_ADD_TEMPPASS )
			{
				//如果是对与 设置密码的回复 , 设置返回的编码
				if( msg.getType()==0 ){
					SmartDevicePassport sp = findPass(   msg.deviceId , org.getExt1() );
					
					util.LogUtil.writeLog( "find Passport:"+sp+"\t"+msg.deviceId+"\t"+org.getReplymsg(), "dealReply");
					
					if(sp!=null ){
						if( StringTool.isNull(sp.getIdx()) ){
							util.LogUtil.writeLog( "set idx :"+ sp.getIdd()+"\t"+msg.userCode, "dealReply");
							sp.setIdx ( msg.userCode );
							sp.setStat( 0 );
							Ebean.getServer(GlobalDBControl.getDB()).save(sp);
						}else
							util.LogUtil.writeLog( "set idx fail:"+ sp.getIdd()+",old\t"+sp.getIdx()
									+"\tnew :\t"+msg.userCode, "dealReply");
					}
				}
				else{
					util.LogUtil.writeLog("addpass deal fail:"+org.getIdd()+"\t"+msg.getType(), "LockSocketThread");
					SmartDevicePassport sp = findPass(   msg.deviceId , org.getExt1() );
					if(sp != null && StringTool.isNull( sp.getIdx() ) )
					{
						//失败了删除
						Ebean.getServer(GlobalDBControl.getDB()).delete(sp);
						util.LogUtil.writeLog("addpass fail:"+org.getIdd()+"\t"+msg.getType(), "addPass");
					}
				}
			}
			
			if( org.getType().intValue() == SmartMsgUtil.CMD_SVR_DELPASS )
			{    //删除密码
				if( msg.getType()==0 ){
					String sql = "delete from smart_device_passport where deviceid='" + org.getLockid()
							+ "' and idx='" + org.getExt1() +"' and type='" + org.getExt2()
							+ "'";
					int ext = JdbcOper.extSql( sql );
					util.LogUtil.writeLog("dealpass :"+sql+"\t"+ext, "LockSocketThread");
				}
				else{
					util.LogUtil.writeLog("dealpass fail:"+org.getIdd()+"\t"+msg.getType(), "LockSocketThread");
				}
			}
					
			org.setReply(1);
			org.setReplymsg( ""+msg.getType() );
			org.setReplytime(new Date());
			Ebean.getServer(GlobalDBControl.getDB()).save( org );
		}
	}
	
	//根据消息的 mid 找到原发消息
	static SmartNetMsg findOrgMsg( SmartNetMsg msg)
	{
		List<SmartNetMsg> ls = Ebean.getServer(GlobalDBControl.getDB()).find(SmartNetMsg.class).where()
				.eq("lockid", msg.getLockid()).eq("reply", 0).eq("tag", 32)
				.eq("mid",msg.getMid() ).orderBy("idd desc").setMaxRows(1).findList();
		if( ls.size() >0 )
			return ls.get(0);
		else
			util.LogUtil.writeLog( "findOrgMsg fail:" + msg.getLockid() + "\t" + msg.getMid(), "dealReply");
		return null;
		
	}

	//设置电量
	public static SmartNetMsg devicePower(SmartNetMsg msg) {
		SmartDevice dev = Ebean.getServer(GlobalDBControl.getDB())
				.find(SmartDevice .class).where().eq("deviceid", msg.deviceId).findUnique();
		if( dev != null ){
			dev.setPower( ""+ msg.info );
			Ebean.getServer(GlobalDBControl.getDB()).save(dev);
			return SmartMsgUtil.makeCommonReply(msg, true);
		}else{
			util.LogUtil.writeLog( "devicePower fail:"+msg.deviceId, "dealReply");
		}
		return SmartMsgUtil.makeCommonReply(msg, false);
	}

	//设备重置
	public static SmartNetMsg resetDevice(SmartNetMsg msg) {
		SmartDevice dev = SmartService.findDevice(msg.deviceId);
		if(dev==null){
			util.LogUtil.writeLog("dev not found:"+msg.deviceId, "reset");
			return null;
		}
		String d = "A9"+ StringTool.makeHexString( (byte)msg.getLen().intValue() )
				 + msg.getMsg();
		String d2 = "A7"+ StringTool.makeHexString( (byte)msg.getLen().intValue() )
				 + msg.getMsg();
		
		util.LogUtil.writeLog( d.toUpperCase().substring(0, d.length()-32)+ dev.getSecret() 
				+ "\t"+ MD5.mkMd5( d.toUpperCase().substring(0, d.length()-32) + dev.getSecret() )
				+ "\t"+ MD5.mkMd5( d2.toUpperCase().substring(0, d2.length()-32) + dev.getSecret() )
				+ "\t" + msg.sign , "reset");
		
		if( !msg.sign.equalsIgnoreCase(MD5.mkMd5( d.toUpperCase().substring(0, d.length()-32) + dev.getSecret() ))
				&&
				!msg.sign.equalsIgnoreCase(MD5.mkMd5( d2.toUpperCase().substring(0, d2.length()-32) + dev.getSecret() ))
				)
		{
			util.LogUtil.writeLog( "签名检查失败："+ msg.getLockid() , "reset");
			return null;
		}
		
		String phone = SmartService.findDeviceUser(dev.getIdd());
		if(!StringTool.isNull(phone)){
			MessageService.pushNormalMsg(phone, "智能锁重置",  dev.getDevicedesc()+"重置" );
		}
		
		String sql  ="delete from smart_msg where tag=32 and lockid='"+msg.deviceId
				+ "' and reply=0";
		int ret = JdbcOper.extSql(sql);
		
		sql  ="delete from smart_device_passport where deviceid='"+msg.deviceId
				+ "' ";
		int ret1= JdbcOper.extSql(sql);
		
		if(dev!=null)
			sql  ="delete from smart_app_user_device where deviceid='"+dev.getIdd()
				+ "' ";
		int ret2 = JdbcOper.extSql(sql);
		
		sql  ="delete from smart_device_log where deviceid='"+msg.deviceId
				+ "' ";
		int ret3 = JdbcOper.extSql(sql);
		
		util.LogUtil.writeLog(msg.deviceId+"\t"+ret+"\t"+ret1+"\t"+ret2+"\t"+ret3, "reset");
		
		return null;
	}
	
	//查询时间
	public static SmartNetMsg time(SmartNetMsg msg) {
		return SmartMsgUtil.makeTimeReply(msg, false);
	}

	//通知消息
	public static SmartNetMsg beep(SmartNetMsg msg) {
		SmartDevice dev = SmartService.findDevice(msg.deviceId);
		if(dev==null){
			util.LogUtil.writeLog("dev not found:"+msg.deviceId, "beep");
			return null;
		}
		
		String phone = SmartService.findDeviceUser(dev.getIdd());
		if(!StringTool.isNull(phone)){
			MessageService.pushNormalMsg(phone, "智能锁提醒",  dev.getDevicedesc() + SysDictAction.getDictString( "lock_beep_type" ,  ""+msg.info ) );
		}
		return null;
	}

	//报警
	public static SmartNetMsg alarm(SmartNetMsg msg) {
		SmartDevice dev = SmartService.findDevice(msg.deviceId);
		if(dev==null){
			util.LogUtil.writeLog("dev not found:"+msg.deviceId, "beep");
			return null;
		}
		
		String phone = SmartService.findDeviceUser(dev.getIdd());
		if(!StringTool.isNull(phone)){
				
			//MessageService.pushNormalMsg(phone, "智能锁报警", dev.getDevicedesc() + SysDictAction.getDictString( "lock_alarm_type" ,  ""+msg.info ));
		}
		return null;
	}

	//删除密码
	public static SmartNetMsg userDel(SmartNetMsg msg) {
		String sql = "delete from smart_device_passport where deviceid='"+msg.deviceId+"' and idx='"
				+msg.userCode+"' and type="+msg.info;
		int ret = JdbcOper.extSql( sql );
		util.LogUtil.writeLog( "Del Passport:"+msg.deviceId+"\t"+msg.info+"\t"+msg.userCode+"\t"+ret, "dealReply");
		
		return SmartMsgUtil.makeCommonReply(msg, ret>0);
	}

	//添加密码
	public static SmartNetMsg userAdd(SmartNetMsg msg) {
		SmartDevicePassport sp = Ebean.getServer(GlobalDBControl.getDB()).find(SmartDevicePassport.class)
				.where().eq("idx", msg.userCode).eq("deviceid", msg.deviceId).eq("type", msg.info)
				.findUnique();
		if(sp!=null){
			util.LogUtil.writeLog( "Add fail , exist Passport:"+sp.getIdd()
					+"\t"+msg.deviceId+"\t"+msg.userCode+"\t"+msg.info, "dealReply");
			return SmartMsgUtil.makeCommonReply(msg, false);
		}
		
		sp=new SmartDevicePassport();
		sp.setDeviceid(msg.deviceId);
		sp.setIdx(msg.userCode);
		sp.setType(msg.info);
		//密码名称
		sp.setUserinfo( SysDictAction.getDictString( SmartMsgUtil.DICT_USR_TYPE, ""+msg.info ) + 
				StringTool.GetIntEx(msg.userCode, 0,16) );
		Ebean.getServer(GlobalDBControl.getDB()).save(sp);
		
		util.LogUtil.writeLog( "Add Passport:"+sp.getIdd()+"\t"+msg.deviceId+"\t"+msg.userCode, "dealReply");
		return SmartMsgUtil.makeCommonReply(msg, true);
	}

	//远程开锁
	public static SmartNetMsg remoteOpen( SmartNetMsg msg,SmartDevice device ) {
		//这里直接回复了，以后应该等app回复
		//return SmartMsgUtil.makeRemoteOpenReply(msg, device, true);
		return SmartMsgUtil.makeRemoteOpenReply(msg, device, false);
	}

}
