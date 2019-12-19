package net;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import util.GlobalDBControl;
import util.LogUtil;
import util.classEntity.StringTool;
import ServiceDao.JdbcOper;
import app.service.DeviceService;
import app.service.VipService;

import com.avaje.ebean.Ebean;

import controllers.SysDictAction;
import models.SmartAppUser;
import models.SmartAppUserDevice;
import models.SmartDevice;
import models.SmartDeviceLog;
import models.SmartDevicePassport;
import models.SmartNetMsg;

public class SmartOperService {

	static int TYPE_PASS_Fingerprint = 1;
	static int TYPE_PASS_DIGITAL = 2;

	/*
	 * APP端 添加数字密码
		phone - 用户手机; 
		dev   - 设备; 
		pass  - 密码; 
		validtime - 密码有效期，可选填
	 * 返回：1-成功 ; -1 - 密码重复
	 */
	public static int addPassword(String phone, SmartDevice dev, String pass,String name,
			Date validtime,Date begintime) {
		SmartAppUser su = SmartService.findAppUser(phone);
		if (su == null) {
			util.LogUtil.writeLog("phone user not exist:" + phone, "Password");
			return 0;
		}

		// 密码不能重复
		if (Ebean.getServer(GlobalDBControl.getDB())
				.find(SmartDevicePassport.class).where()
				.eq("deviceid", dev.getDeviceid()).eq("password", pass)
				.findList().size() > 0) {
			util.LogUtil
					.writeLog("pass exist:" + pass + "\t" + dev.getDeviceid(),
							"Password");
			return -1;
		}

		// 保存密码到数据库
		SmartDevicePassport sp = new SmartDevicePassport();
		sp.setAddtime(new Date());
		sp.setDeviceid(dev.getDeviceid());
		sp.setPassword(pass);
		sp.setUserid(su.getIdd());
		if(StringTool.isNull(name))
			name="数字密码";
		sp.setUserinfo(name);
		sp.setValidetime(validtime);
		sp.setType(TYPE_PASS_DIGITAL);
		sp.setStat(1);
		sp.setBegintime(begintime);
		Ebean.getServer(GlobalDBControl.getDB()).save(sp);
		
		SmartService.addDeviceLog(dev.getDeviceid(), 1003, "APP端添加密码："+name);

		// 发送网络信息
		SmartNetMsg msg;
		if (validtime == null)
			msg = SmartMsgUtil.makeAddPassMsg(dev, pass);
		else {
			//long sec = (validtime.getTime() ) / 1000 + 3600*24;
			long sec = (validtime.getTime() ) / 1000 ;
			msg = SmartMsgUtil.makeAddPassMsg2(dev, pass, sec,begintime);
		}
		Ebean.getServer(GlobalDBControl.getDB()).save( msg );

		util.LogUtil.writeLog("pass add ok:" + pass + "\t" + dev.getDeviceid(),
				"Password");
		return 1;
	}

	/*
	 * APP端 删除密码，包括数字密码、指纹密码等
	 *  phone - 用户手机; 
	 *  pass  - 密码; 
	 *  返回：1-成功 ;
	 */
	public static int delPassword( String phone , SmartDevicePassport pass) {
		SmartAppUser su = SmartService.findAppUser(phone);
		if (su == null) {
			util.LogUtil.writeLog("phone user not exist:" + phone, "Password");
			return 0;
		}
		
		if( pass.getStat() == 1 ){
			//密码已经过期并且是添加中直接删除
			if(pass.getValidetime()!=null && new Date().compareTo(pass.getValidetime())>0){
				JdbcOper.extSql("delete from smart_device_passport where idd="+pass.getIdd());
				SmartService.addDeviceLog( pass.getDeviceid(), 1004, "APP端删除" + 
						SysDictAction.getDictString( SmartMsgUtil.DICT_USR_TYPE, ""+ pass.getType() )
						+ "："+pass.getUserinfo());
				util.LogUtil.writeLog("pass del ok:" + pass.getPassword() + "\t"
						+ SmartService.findDevice(pass.getDeviceid()).getDeviceid(),
						"Password");
				return 1;
			}
			util.LogUtil.writeLog("pass stat 1:" + phone +"\t" + pass.getIdd() , "Password");
			return 0;
		}
		
		//pass.setStat(2);
		//Ebean.getServer(GlobalDBControl.getDB()).save(pass);
		
		String sql = "update smart_device_passport set stat=2 where idd="+pass.getIdd();
		
		JdbcOper.extSql(sql);
		
		SmartService.addDeviceLog( pass.getDeviceid(), 1004, "APP端删除" + 
				SysDictAction.getDictString( SmartMsgUtil.DICT_USR_TYPE, ""+ pass.getType() )
				+ "："+pass.getUserinfo());

		// 发送网络信息
		SmartNetMsg msg = SmartMsgUtil.makeDelPass( SmartService.findDevice(pass.getDeviceid()),
				pass.getIdx(), pass.getType() );
		Ebean.getServer(GlobalDBControl.getDB()).save(msg);

		util.LogUtil.writeLog("pass del ok:" + pass.getPassword() + "\t"
				+ SmartService.findDevice(pass.getDeviceid()).getDeviceid(),
				"Password");
		return 1;
	}

	/*
	 * APP端 发起让锁端添加 其他密码，比如指纹密码 
	 * phone - 用户手机; 
	 * dev   - 锁设备
	 * passtype -密码类型：1 - 指纹密码 
	 * 返回：1-成功 ;
	 */
	public static int addUserPass(String phone, SmartDevice dev, int passtype) {
		SmartAppUser su = SmartService.findAppUser(phone);
		if (su == null) {
			util.LogUtil.writeLog("phone user not exist:" + phone, "Password");
			return 0;
		}

		// 发送网络信息
		SmartNetMsg msg =SmartMsgUtil.makeAddUserMsg(dev, passtype);
		Ebean.getServer(GlobalDBControl.getDB()).save(msg);

		util.LogUtil.writeLog(
				"add user ok:" + passtype + "\t" + dev.getDeviceid(),
				"Password");
		return 1;
	}

	/*
	 * APP端 发起连接设备  
	 * devid  - 设备编码，即二维码扫描出的设备字符串； 
	 * phone  - 用户手机号,用户必须已经注册 
	 * 返回：连接上的设备，返回null，即没有连接上锁设备
	 */
	static int DeviceID_Len = 16;
	public static SmartDevice connectDevice(String devid, String phone) {
		if( devid.length()==DeviceID_Len-1 )
			devid += "0";
		
		// 设置用户在 连接锁
		SmartService.setAppConnect( devid , phone );
				
		SmartDevice dev = SmartService.findDevice(devid);
		if (dev == null) {
			util.LogUtil.writeLog("dev not exist:" + devid + "\t" + phone,
					"connect");
			return null;
		}

		SmartAppUser su = SmartService.findAppUser(phone);
		if (su == null) {
			util.LogUtil.writeLog("phone user not exist:" + devid + "\t"
					+ phone, "connect");
			return null;
		}

		// 查找设备的拥有者
		String p = SmartService.findDeviceUser(dev.getIdd());
		if (p != null && p.equals(phone)) {
			util.LogUtil.writeLog("dev phone connect already:" + devid + "\t"
					+ phone, "connect");
			return dev;
		}

		// 有其他用户了，不能再连接
		if (p != null) {
			util.LogUtil.writeLog("dev has phone already:" + devid + "\t" + p,
					"connect");
			return null;
		}

		// 查看设备是否在连接
		if (SmartService.getDeviceConnect(devid) == 1) {
			// 设置设备 属于该用户
			SmartAppUserDevice spd = new SmartAppUserDevice();
			spd.setDeviceid(dev.getIdd());
			spd.setDeviceAppUser(su.getIdd());
			spd.setStatus(0);
			spd.setAddtime(new Date());
			spd.setUsertype(1);

			Ebean.getServer(GlobalDBControl.getDB()).save(spd);
			
			//会员有效期增加一年
			VipService.addVipValidityTimeByDevice(su.getIdd(),dev.getDeviceid());
			//根据lockid找到SmartDevice表中对应锁的deviceid
			SmartDevice smartDevice = Ebean.getServer(GlobalDBControl.getDB()).createQuery(SmartDevice.class,"find SmartDevice where deviceid = '"+dev.getDeviceid()+"'").findUnique();
			if(smartDevice.getBuytime()==null){
				String deviceId = smartDevice.getDeviceid();
				//根据锁deviceid找到主库明细表对应锁的出库时间
				Date outTime = DeviceService.getLockOutDBTimeNoCache(deviceId);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				//根据锁的deviceid将SmartDevice表中对应锁的购买时间与出库时间同步
				DeviceService.updateBuyTimeByDeviceIdNoCache(outTime,deviceId);
			}
			
			
			util.LogUtil.writeLog("dev phone ok:" + devid + "\t" + p, "connect");
			
			return dev;
		}
		else{
			util.LogUtil.writeLog("dev not connect:" + devid + "\t" + p, "connect");
			return null;
		}
	}

	/*
	 * 锁端发起连接用户 
	 * devid ： 设备编码 
	 * 返回：连上的手机号，null即没有用户在连接
	 */
	public static String connectUser(String devid) {
		SmartDevice dev = SmartService.findDevice(devid);
		if (dev == null){
			util.LogUtil.writeLog("dev not exist:" + devid + "\t" ,	"connect");
			return null;
		}

		// 查找设备的拥有者
		String p = SmartService.findDeviceUser(dev.getIdd());
		// 有用户了，不再连接
		if (p != null) {
			util.LogUtil.writeLog("dev admin-phone exist:" + devid + "\t" + p,
					"connect");
			return p;
		}

		// 设置用户在 连接锁
		SmartService.setDeviceConnect(devid);

		String ph = SmartService.getAppConnect(devid);
		if (ph == null) {
			util.LogUtil.writeLog("no phone connecting:" + devid + "\t" + p,
					"connect");
			return null;
		}

		SmartAppUser su = SmartService.findAppUser(ph);
		if (su == null) {
			util.LogUtil.writeLog("phone not exist:" + devid + "\t" + p,
					"connect");
			return null;
		}

		// 设置设备 属于该用户
		SmartAppUserDevice spd = new SmartAppUserDevice();
		spd.setDeviceid(dev.getIdd());
		spd.setDeviceAppUser(su.getIdd());
		spd.setStatus(0);
		spd.setAddtime(new Date());
		spd.setUsertype(1);

		Ebean.getServer(GlobalDBControl.getDB()).save(spd);
		
		//会员有效期增加一年
		VipService.addVipValidityTimeByDevice(su.getIdd(),dev.getDeviceid());
		//根据lockid找到SmartDevice表中对应锁的deviceid
		SmartDevice smartDevice = Ebean.getServer(GlobalDBControl.getDB()).createQuery(SmartDevice.class,"find SmartDevice where deviceid = '"+dev.getDeviceid()+"'").findUnique();
		if(smartDevice.getBuytime()==null){
			String deviceId = smartDevice.getDeviceid();
			//根据锁deviceid找到主库明细表对应锁的出库时间
			Date outTime = DeviceService.getLockOutDBTimeNoCache(deviceId);
			//根据锁的deviceid将SmartDevice表中对应锁的购买时间与出库时间同步
			DeviceService.updateBuyTimeByDeviceIdNoCache(outTime,deviceId);
		}

		util.LogUtil.writeLog("add ok:" + devid + "\t" + p, "connect");
		return ph;
	}

}
