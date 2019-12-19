package app.action;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import models.SmartAppUser;
import models.SmartAppUserDevice;
import models.SmartDevice;
import models.SmartDeviceLog;
import models.SmartDevicePassport;
import models.SmartDeviceType;
import net.SmartOperService;
import net.SmartService;
import play.Logger;
import play.mvc.Result;
import util.GlobalDBControl;
import util.StringUtil;
import util.classEntity.StringTool;
import util.jedis.RedisUtil;
import util.json.JsonUtil;
import app.dto.LockDeviceDescDto;
import app.dto.LockDeviceFingerListDto;
import app.dto.LockDeviceICCardDto;
import app.dto.LockDeviceListDto;
import app.dto.LockDeviceLogsDto;
import app.dto.LockDevicePswListDto;
import app.dto.ReturnJson;
import app.service.DeviceService;
import app.service.UserService;
import app.service.VipService;
import app.util.AppUtil;

import com.avaje.ebean.Ebean;

import controllers.SysConfigAction;

public class DeviceAction extends BaseAction{
	public static Result getLockDeviceList(){
		if( !checkSign() )
			return makSignFailor();
		ReturnJson reJson = new ReturnJson();
		String token = request().getQueryString("token");
		String appid = request().getQueryString("appid");
		if(StringUtil.isNull(token)){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}	
		
		SmartAppUser user = UserService.findUserByToken(token);
		if(user==null){
			reJson.setTokenTimeOut();
		}else{
			LockDeviceListDto data = new LockDeviceListDto();
			List<LockDeviceListDto.Img> imgs = new ArrayList<LockDeviceListDto.Img>();
			LockDeviceListDto.Img img = new LockDeviceListDto.Img();
			img.setImgurl(SysConfigAction.findSysconfig("智能锁", "首页宣传图"));
			img.setLinkurl(SysConfigAction.findSysconfig("智能锁", "首页宣传图地址"));
			imgs.add(img);
			data.setImgs(imgs);
			//首先判断该用户	是否为会员用户/会员是否过期
			if(VipService.checkVip(user.getIdd())==1){
				//判断该用户今天是否领取过奖励金
				if(VipService.checkGetBounty(user.getIdd())){
					data.setInformgetbounty("1");
				}else{
					data.setInformgetbounty("0");
				}
			}else if(VipService.checkVip(user.getIdd())==2){
				data.setInformgetbounty("0");
			}else{
				data.setInformgetbounty("0");
			}
			List<LockDeviceListDto.Item> items = new ArrayList<LockDeviceListDto.Item>();
			List<SmartAppUserDevice> devices = DeviceService.findUserDevices(user.getIdd());
			for(SmartAppUserDevice userDevice:devices){
				LockDeviceListDto.Item item = new LockDeviceListDto.Item();
				SmartDevice device = DeviceService.findDevice(userDevice.getDeviceid());
				item.setLockname(device.getDevicedesc());
				item.setLockid(device.getIdd());
				item.setLockstatus(SmartService.getDeviceOnline(device.getIdd()+""));
				SmartDeviceType deviceType = DeviceService.findDeviceType(device.getType());
				item.setLocktype(deviceType.getName());
				item.setLockimgurl(deviceType.getLogo());
				item.setLockelectricity(device.getPower());
				SmartDeviceLog deviceLog = DeviceService.findLatelyDeviceLog(device.getDeviceid());
				if(deviceLog!=null){
					item.setOperaterecord(deviceLog.getDescinfo());
					item.setOperationstatus(deviceLog.getWarn());
				}else{
					item.setOperaterecord("无记录");
					item.setOperationstatus(0);
				}
				
				items.add(item);
			}
			data.setItems(items);
			reJson.setSuccess();
			reJson.setData(data);
		}
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);		
	}
	
	//获取设备详情，操作日志
	public static Result getLockDeviceDetail(){
		if( !checkSign() )
			return makSignFailor();
		ReturnJson reJson = new ReturnJson();
		String token = request().getQueryString("token");
		String lockid = request().getQueryString("lockid");
		Integer nlockid = StringTool.GetInt(lockid, 0);
		if(StringUtil.isNull(token) || nlockid<=0){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}	
		
		SmartAppUser user = UserService.findUserByToken(token);
		if(user==null){
			reJson.setTokenTimeOut();
		}else{
			SmartAppUserDevice userDevice = DeviceService.findUserDevice(user.getIdd(),nlockid);
			if(userDevice==null){
				reJson.setCode(1);
				reJson.setMessage("设备不存在");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			SmartDevice device = DeviceService.findDevice(nlockid);
			LockDeviceLogsDto data = new LockDeviceLogsDto();
			List<LockDeviceLogsDto.Item> items = new ArrayList<LockDeviceLogsDto.Item>();
			List<SmartDeviceLog> deviceLogs = DeviceService.findDeviceLogs(device.getDeviceid());
			Map<String,List<LockDeviceLogsDto.Item.Record>> map = new TreeMap<String,List<LockDeviceLogsDto.Item.Record>>();
			for(SmartDeviceLog deviceLog:deviceLogs){
				String operationdate = StringUtil.getDateStr(deviceLog.getAddtime());
				if(!map.containsKey(operationdate)){
					map.put(operationdate, new ArrayList<LockDeviceLogsDto.Item.Record>());
				}
				LockDeviceLogsDto.Item.Record record = new LockDeviceLogsDto.Item.Record();
				record.setOperationname(deviceLog.getDescinfo());
				record.setOperationstatus(deviceLog.getWarn());
				record.setOperationtime(StringUtil.getDateTimeString(deviceLog.getAddtime(), "HH:mm"));
				map.get(operationdate).add(record);
			}
			Set<String> sortSet = new TreeSet<String>(new Comparator<String>() {
	            @Override
	            public int compare(String o1, String o2) {
	                return o2.compareTo(o1);//降序排列
	            }
	        });
			sortSet.addAll(map.keySet());
			for(String key:sortSet){
				LockDeviceLogsDto.Item item = new LockDeviceLogsDto.Item();
				item.setOperationdate(key);
				item.setRecords(map.get(key));
				items.add(item);
			}
			data.setItems(items);
			reJson.setSuccess();
			reJson.setData(data);
		}
		String reContent = JsonUtil.parseObj(reJson);
//		appLogger.debug(reContent);
		return ok(reContent);
	}
	
	public static Result getLockDeviceDesc(){
		if( !checkSign() )
			return makSignFailor();
		ReturnJson reJson = new ReturnJson();
		String token = request().getQueryString("token");
		String lockcode = request().getQueryString("lockcode");
		if(StringUtil.isNull(token) || StringUtil.isNull(lockcode)){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}	
		
		SmartAppUser user = UserService.findUserByToken(token);
		if(user==null){
			reJson.setTokenTimeOut();
		}else{
			LockDeviceDescDto data = new LockDeviceDescDto();
			SmartDevice device = DeviceService.findDevice(lockcode);
			if(device!=null){
				SmartAppUserDevice userDevice = DeviceService.findUserDevice(user.getIdd(),device.getIdd());
				if(userDevice!=null){
					reJson.setCode(2);
					reJson.setMessage("此设备已经有管理员");
					String reContent = JsonUtil.parseObj(reJson);
					appLogger.debug(reContent);
					return ok(reContent);
				}
				SmartDeviceType deviceType = DeviceService.findDeviceType(device.getType());
				data.setLink(deviceType.getImageLink());
				data.setDescinfo(deviceType.getDescinfo());
			}else{
				data.setLink(SysConfigAction.findSysconfig("智能锁", "添加锁描述默认图"));
			}
			reJson.setSuccess();
			reJson.setData(data);
		}
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}
	//添加设备
	public static Result addLockDevice(){
		if( !checkSign() )
			return makSignFailor();
		ReturnJson reJson = new ReturnJson();
		String token = request().getQueryString("token");
		String lockcode = request().getQueryString("lockcode");
		if(StringUtil.isNull(token) || StringUtil.isNull(lockcode)){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}	
		
		SmartAppUser user = UserService.findUserByToken(token);
		if(user==null){
			reJson.setTokenTimeOut();
		}else{
			if(!VipService.checkFreezeDevice(lockcode)){
				reJson.setCode(1);
				reJson.setMessage("此设备正在退货中");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			SmartDevice device = DeviceService.findDevice(lockcode);
			if(device!=null){
				SmartAppUserDevice userDevice = DeviceService.findUserDeviceNoCache(device.getIdd());
				if(userDevice!=null){
					if(userDevice.getDeviceAppUser().intValue()==user.getIdd()){
						reJson.setSuccess();
						String reContent = JsonUtil.parseObj(reJson);
						appLogger.debug(reContent);
						return ok(reContent);
					}
					reJson.setCode(2);
					reJson.setMessage("此设备已经有管理员");
					String reContent = JsonUtil.parseObj(reJson);
					appLogger.debug(reContent);
					return ok(reContent);
				}
			}
			SmartDevice connectDevice = SmartOperService.connectDevice(lockcode, user.getPhone());
			if(connectDevice!=null){
				reJson.setSuccess();
				DeviceService.clearUserDevicesCache(user.getIdd());
				String reContent = JsonUtil.parseObj(reJson);	//设备添加成功
				appLogger.debug(reContent);
				return ok(reContent);
			}else{
				reJson.setCode(1);
				reJson.setMessage("设备连接中");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
		}
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}
	
	//删除设备
	public static Result delLockDevice(){
		if( !checkSign() )
			return makSignFailor();
		ReturnJson reJson = new ReturnJson();
		String token = request().getQueryString("token");
		String lockid = request().getQueryString("lockid");
		String smscode = request().getQueryString("smscode");
		Integer nlockid = StringTool.GetInt(lockid, 0);
		if(StringUtil.isNull(token) || StringUtil.isNull(smscode) || nlockid<=0){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}	
		SmartAppUser user = UserService.findUserByToken(token);
		if(user==null){
			reJson.setTokenTimeOut();
		}else{
			String rCode = AppUtil.getRandomCode(user.getPhone(), AppUtil.TYPE_SMS_DELDEVICE);
			if(!smscode.equals(rCode)){
				reJson.setCode(1);
				reJson.setMessage("短信验证码错误");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			SmartAppUserDevice userDevice = DeviceService.findUserDevice(user.getIdd(),nlockid);
			if(userDevice==null){
				reJson.setCode(1);
				reJson.setMessage("设备不存在");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			Ebean.getServer(GlobalDBControl.getDB())
			.createSqlUpdate("delete from smart_app_user_device where deviceappuser=:deviceappuser and deviceid=:deviceid")
			.setParameter("deviceappuser", user.getIdd())
			.setParameter("deviceid", nlockid).execute();
			DeviceService.clearUserDevicesCache(user.getIdd());
			reJson.setSuccess();
		}
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}
	//删除设备操作记录
	public static Result delLockDeviceLogs(){
		if( !checkSign() )
			return makSignFailor();
		ReturnJson reJson = new ReturnJson();
		String token = request().getQueryString("token");
		String lockid = request().getQueryString("lockid");
		Integer nlockid = StringTool.GetInt(lockid, 0);
		if(StringUtil.isNull(token) || nlockid<=0){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}	
		SmartAppUser user = UserService.findUserByToken(token);
		if(user==null){
			reJson.setTokenTimeOut();
		}else{
			SmartAppUserDevice userDevice = DeviceService.findUserDevice(user.getIdd(),nlockid);
			if(userDevice==null){
				reJson.setCode(1);
				reJson.setMessage("设备不存在");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			SmartDevice device = DeviceService.findDevice(nlockid);
			Ebean.getServer(GlobalDBControl.getDB())
			.createSqlUpdate("update smart_device_log set status=:status where deviceid=:deviceid")
			.setParameter("status", DeviceService.STATU_SMART_APP_DEVICE_LOG_DEL)
			.setParameter("deviceid", device.getDeviceid()).execute();
			DeviceService.clearDeviceLogsCache(device.getDeviceid());
			DeviceService.addDeviceLog(device.getDeviceid(), DeviceService.OP_SMART_APP_DEVICE_LOG_DEL,"删除全部操作记录");
			reJson.setSuccess();
		}
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}
	//修改设备名称
	public static Result setLockDeviceName(){
		if( !checkSign() )
			return makSignFailor();
		ReturnJson reJson = new ReturnJson();
		String token = request().getQueryString("token");
		String lockid = request().getQueryString("lockid");
		String devicename = request().getQueryString("devicename");
		Integer nlockid = StringTool.GetInt(lockid, 0);
		if(StringUtil.isNull(token) || StringUtil.isNull(devicename) || nlockid<=0){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}	
		SmartAppUser user = UserService.findUserByToken(token);
		if(user==null){
			reJson.setTokenTimeOut();
		}else{
			SmartAppUserDevice userDevice = DeviceService.findUserDevice(user.getIdd(),nlockid);
			if(userDevice==null){
				reJson.setCode(1);
				reJson.setMessage("设备不存在");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			Ebean.getServer(GlobalDBControl.getDB()).createSqlUpdate("update smart_device set devicedesc=:devicedesc where idd=:idd")
			.setParameter("devicedesc", devicename).setParameter("idd", nlockid).execute();
			DeviceService.clearDeviceCache(nlockid);
			reJson.setSuccess();
		}
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}
	
	//获取设备密码列表
	public static Result getLockDevicePswList(){
		if( !checkSign() )
			return makSignFailor();
		ReturnJson reJson = new ReturnJson();
		String token = request().getQueryString("token");
		String lockid = request().getQueryString("lockid");
		Integer nlockid = StringTool.GetInt(lockid, 0);
		if(StringUtil.isNull(token) || nlockid<=0){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}	
		SmartAppUser user = UserService.findUserByToken(token);
		if(user==null){
			reJson.setTokenTimeOut();
		}else{
			SmartAppUserDevice userDevice = DeviceService.findUserDevice(user.getIdd(),nlockid);
			if(userDevice==null){
				reJson.setCode(1);
				reJson.setMessage("设备不存在");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			LockDevicePswListDto data = new LockDevicePswListDto();
			List<LockDevicePswListDto.Item> items = new ArrayList<LockDevicePswListDto.Item>();
			SmartDevice device = DeviceService.findDevice(nlockid);
			List<SmartDevicePassport> devicePsws = DeviceService.findDevicePswWithType(device.getDeviceid(),DeviceService.TYPE_SMART_APP_DEVICE_PSW_NUMBER);
			for(SmartDevicePassport devicePsw:devicePsws){
				LockDevicePswListDto.Item item = new LockDevicePswListDto.Item();
				item.setPswcode(devicePsw.getPassword());
				item.setPswid(devicePsw.getIdd());
				item.setPswname(devicePsw.getUserinfo());
				item.setPswvalidity(devicePsw.getValidetime()==null?"-1":StringUtil.getDateTimeString(devicePsw.getValidetime(), "yyyy-MM-dd HH:mm"));
				Date begintime = devicePsw.getBegintime()==null?devicePsw.getAddtime():devicePsw.getBegintime();
				item.setPswbegintime(StringUtil.getDateTimeString(begintime, "yyyy-MM-dd HH:mm"));
				if(devicePsw.getStat()==1 && devicePsw.getValidetime()!=null && new Date().compareTo(devicePsw.getValidetime())>0)
					item.setPswstatu(0);
				else
					item.setPswstatu(devicePsw.getStat());
				items.add(item);
			}
			data.setItems(items);
			reJson.setSuccess();
			reJson.setData(data);
		}
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}
	//添加设备密码
	public static Result addLockDevicePsw(){
		if( !checkSign() )
			return makSignFailor();
		ReturnJson reJson = new ReturnJson();
		String token = request().getQueryString("token");
		String lockid = request().getQueryString("lockid");
		String pswname = request().getQueryString("pswname");//设备密码使用人
		String pswcode = request().getQueryString("pswcode");//设备密码
		String pswvalidity = request().getQueryString("pswvalidity");//有效期
		
		String begintime = request().getQueryString("begintime");    //开始时间
		if(begintime==null)
			begintime = "-1";
		
		Integer nlockid = StringTool.GetInt(lockid, 0);
		if(StringUtil.isNull(token) || StringUtil.isNull(pswvalidity) 
				|| StringUtil.isNull(pswcode) || nlockid<=0){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}	
		SmartAppUser user = UserService.findUserByToken(token);
		if(user==null){
			reJson.setTokenTimeOut();
		}else{
			try{
				
				if(pswvalidity.length()==10){
					pswvalidity = pswvalidity + " 23:59";
				}
				
				if(pswcode.length()!=6){
					reJson.setCode(3);
					reJson.setMessage("密码格式错误");
					String reContent = JsonUtil.parseObj(reJson);
					appLogger.debug(reContent);
					return ok(reContent);
				}
				SmartAppUserDevice userDevice = DeviceService.findUserDevice(user.getIdd(),nlockid);
				if(userDevice==null){
					reJson.setCode(1);
					reJson.setMessage("设备不存在");
					String reContent = JsonUtil.parseObj(reJson);
					appLogger.debug(reContent);
					return ok(reContent);
				}
				SmartDevice device = DeviceService.findDevice(nlockid);
				
				SmartDevicePassport devicePassport = DeviceService.findDevicePswWithPsw(device.getDeviceid(), pswcode);
				if(devicePassport!=null){
					reJson.setCode(2);
					reJson.setMessage("密码不能重复");
					String reContent = JsonUtil.parseObj(reJson);
					appLogger.debug(reContent);
					return ok(reContent);
				}
				
				if(StringUtil.checkSimplePsw(pswcode)){
					reJson.setCode(3);
					reJson.setMessage("密码过于简单");
					String reContent = JsonUtil.parseObj(reJson);
					appLogger.debug(reContent);
					return ok(reContent);
				}
				
				SmartOperService.addPassword(user.getPhone(), device, pswcode,pswname,
						pswvalidity.equals("-1")?null:StringUtil.getDate(pswvalidity, "yyyy-MM-dd HH:mm"),
						begintime.equals("-1")?null:StringUtil.getDate(begintime, "yyyy-MM-dd HH:mm")
								);
				DeviceService.clearDevicePsws(device.getDeviceid());
				reJson.setSuccess();
			}catch(Exception e){
				Logger.error("addLockDevicePsw", e);
			}
		}
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}
	
	public static Result delLockDevicePsw(){
		if( !checkSign() )
			return makSignFailor();
		ReturnJson reJson = new ReturnJson();
		String token = request().getQueryString("token");
		String lockid = request().getQueryString("lockid");
		String pswid = request().getQueryString("pswid");
		Integer nlockid = StringTool.GetInt(lockid, 0);
		Integer npswid = StringTool.GetInt(pswid, 0);
		if(StringUtil.isNull(token) || npswid<=0 || nlockid<=0){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}	
		SmartAppUser user = UserService.findUserByToken(token);
		if(user==null){
			reJson.setTokenTimeOut();
		}else{
			SmartAppUserDevice userDevice = DeviceService.findUserDevice(user.getIdd(),nlockid);
			if(userDevice==null){
				reJson.setCode(1);
				reJson.setMessage("设备不存在");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			SmartDevice device = DeviceService.findDevice(nlockid);
			SmartDevicePassport devicePassport = DeviceService.findDevicePswWithId(device.getDeviceid(), npswid);
			if(devicePassport!=null){
				if(devicePassport.getStat()==DeviceService.STATU_SMART_APP_DEVICE_PSW_ADD
						//永久有效或者还未过期的在添加中状态不能删除
						&& (devicePassport.getValidetime()==null || new Date().compareTo(devicePassport.getValidetime())<0)){
					reJson.setCode(2);
					reJson.setMessage("设备密码添加中不能删除");
					String reContent = JsonUtil.parseObj(reJson);
					appLogger.debug(reContent);
					return ok(reContent);
				}
				SmartOperService.delPassword(user.getPhone(), devicePassport);
				DeviceService.clearDevicePsws(device.getDeviceid());
			}
			reJson.setSuccess();
		}
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}
	
	public static Result setLockDevicePsw(){
		if( !checkSign() )
			return makSignFailor();
		ReturnJson reJson = new ReturnJson();
		String token = request().getQueryString("token");
		String lockid = request().getQueryString("lockid");
		String pswid = request().getQueryString("pswid");
		String pswname = request().getQueryString("pswname");
		Integer nlockid = StringTool.GetInt(lockid, 0);
		Integer npswid = StringTool.GetInt(pswid, 0);
		if(StringUtil.isNull(token) || StringUtil.isNull(pswname) || npswid<=0 || nlockid<=0){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}	
		SmartAppUser user = UserService.findUserByToken(token);
		if(user==null){
			reJson.setTokenTimeOut();
		}else{
			SmartAppUserDevice userDevice = DeviceService.findUserDevice(user.getIdd(),nlockid);
			if(userDevice==null){
				reJson.setCode(1);
				reJson.setMessage("设备不存在");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			SmartDevice device = DeviceService.findDevice(nlockid);
			SmartDevicePassport devicePsw = DeviceService.findDevicePswWithId(device.getDeviceid(), npswid);
			Ebean.getServer(GlobalDBControl.getDB()).createSqlUpdate("update smart_device_passport set userinfo=:userinfo where deviceid=:deviceid and idd=:idd")
			.setParameter("userinfo", pswname).setParameter("deviceid", device.getDeviceid()).setParameter("idd", npswid).execute();
			DeviceService.addDeviceLog(device.getDeviceid(), DeviceService.OP_SMART_APP_DEVICE_LOG_MODIFYPSWNAME,"修改"+devicePsw.getUserinfo()+"名称为"+pswname);
			DeviceService.clearDevicePsws(device.getDeviceid());
			reJson.setSuccess();
		}
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}
	
	public static Result getLockDeviceFingerPrintList(){
		if( !checkSign() )
			return makSignFailor();
		ReturnJson reJson = new ReturnJson();
		String token = request().getQueryString("token");
		String lockid = request().getQueryString("lockid");
		Integer nlockid = StringTool.GetInt(lockid, 0);
		if(StringUtil.isNull(token) || nlockid<=0){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}	
		SmartAppUser user = UserService.findUserByToken(token);
		if(user==null){
			reJson.setTokenTimeOut();
		}else{
			SmartAppUserDevice userDevice = DeviceService.findUserDevice(user.getIdd(),nlockid);
			if(userDevice==null){
				reJson.setCode(1);
				reJson.setMessage("设备不存在");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			LockDeviceFingerListDto data = new LockDeviceFingerListDto();
			List<LockDeviceFingerListDto.Item> items = new ArrayList<LockDeviceFingerListDto.Item>();
			SmartDevice device = DeviceService.findDevice(nlockid);
			List<SmartDevicePassport> devicePsws = DeviceService.findDevicePswWithType(device.getDeviceid(),DeviceService.TYPE_SMART_APP_DEVICE_PSW_FINGER);
			for(SmartDevicePassport devicePsw:devicePsws){
				LockDeviceFingerListDto.Item item = new LockDeviceFingerListDto.Item();
				item.setFingerprintvalidity(devicePsw.getValidetime()==null?"-1":StringUtil.getDateStr(devicePsw.getValidetime()));
				item.setFingerprintid(devicePsw.getIdd());
				item.setFingerprintname(devicePsw.getUserinfo());
				item.setPswstatu(devicePsw.getStat());
				items.add(item);
			}
			data.setItems(items);
			reJson.setSuccess();
			reJson.setData(data);
		}
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}
	//删除设备指纹
	public static Result delLockDeviceFingerPrint(){
		if( !checkSign() )
			return makSignFailor();
		ReturnJson reJson = new ReturnJson();
		String token = request().getQueryString("token");
		String lockid = request().getQueryString("lockid");
		String fingerprintid = request().getQueryString("fingerprintid");
		Integer nlockid = StringTool.GetInt(lockid, 0);
		Integer nfingerprintid = StringTool.GetInt(fingerprintid, 0);
		if(StringUtil.isNull(token) || nfingerprintid<=0 || nlockid<=0){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}	
		SmartAppUser user = UserService.findUserByToken(token);
		if(user==null){
			reJson.setTokenTimeOut();
		}else{
			SmartAppUserDevice userDevice = DeviceService.findUserDevice(user.getIdd(),nlockid);
			if(userDevice==null){
				reJson.setCode(1);
				reJson.setMessage("设备不存在");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			SmartDevice device = DeviceService.findDevice(nlockid);
			SmartDevicePassport devicePassport = DeviceService.findDevicePswWithId(device.getDeviceid(), nfingerprintid);
			if(devicePassport!=null){
				if(devicePassport.getStat()==DeviceService.STATU_SMART_APP_DEVICE_PSW_ADD){
					reJson.setCode(2);
					reJson.setMessage("设备指纹添加中不能删除");
					String reContent = JsonUtil.parseObj(reJson);
					appLogger.debug(reContent);
					return ok(reContent);
				}
				SmartOperService.delPassword(user.getPhone(), devicePassport);
				DeviceService.clearDevicePsws(device.getDeviceid());
			}
			reJson.setSuccess();
		}
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}
	public static Result setLockDeviceFingerPrint(){
		if( !checkSign() )
			return makSignFailor();
		ReturnJson reJson = new ReturnJson();
		String token = request().getQueryString("token");
		String lockid = request().getQueryString("lockid");
		String fingerprintid = request().getQueryString("fingerprintid");
		String fingerprintname = request().getQueryString("fingerprintname");
		Integer nlockid = StringTool.GetInt(lockid, 0);
		Integer nfingerprintid = StringTool.GetInt(fingerprintid, 0);
		if(StringUtil.isNull(token) || StringUtil.isNull(fingerprintname) || nfingerprintid<=0 || nlockid<=0){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}	
		SmartAppUser user = UserService.findUserByToken(token);
		if(user==null){
			reJson.setTokenTimeOut();
		}else{
			SmartAppUserDevice userDevice = DeviceService.findUserDevice(user.getIdd(),nlockid);
			if(userDevice==null){
				reJson.setCode(1);
				reJson.setMessage("设备不存在");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			SmartDevice device = DeviceService.findDevice(nlockid);
			Ebean.getServer(GlobalDBControl.getDB()).createSqlUpdate("update smart_device_passport set userinfo=:userinfo where deviceid=:deviceid and idd=:idd")
			.setParameter("userinfo", fingerprintname).setParameter("deviceid", device.getDeviceid()).setParameter("idd", nfingerprintid).execute();
			DeviceService.clearDevicePsws(device.getDeviceid());
			reJson.setSuccess();
		}
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}
	
	
	public static Result getLockDeviceICCardList(){
		if( !checkSign() )
			return makSignFailor();
		ReturnJson reJson = new ReturnJson();
		String token = request().getQueryString("token");
		String lockid = request().getQueryString("lockid");
		Integer nlockid = StringTool.GetInt(lockid, 0);
		if(StringUtil.isNull(token) || nlockid<=0){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}	
		SmartAppUser user = UserService.findUserByToken(token);
		if(user==null){
			reJson.setTokenTimeOut();
		}else{
			SmartAppUserDevice userDevice = DeviceService.findUserDevice(user.getIdd(),nlockid);
			if(userDevice==null){
				reJson.setCode(1);
				reJson.setMessage("设备不存在");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			LockDeviceICCardDto data = new LockDeviceICCardDto();
			List<LockDeviceICCardDto.Item> items = new ArrayList<LockDeviceICCardDto.Item>();
			SmartDevice device = DeviceService.findDevice(nlockid);
			List<SmartDevicePassport> devicePsws = DeviceService.findDevicePswWithType(device.getDeviceid(),DeviceService.TYPE_SMART_APP_DEVICE_PSW_ICCARD);
			for(SmartDevicePassport devicePsw:devicePsws){
				LockDeviceICCardDto.Item item = new LockDeviceICCardDto.Item();
				item.setIcardvalidity(devicePsw.getValidetime()==null?"-1":StringUtil.getDateStr(devicePsw.getValidetime()));
				item.setIccardid(devicePsw.getIdd());
				item.setIccardname(devicePsw.getUserinfo());
				item.setIccardstatu(devicePsw.getStat());
				items.add(item);
			}
			data.setItems(items);
			reJson.setSuccess();
			reJson.setData(data);
		}
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}
	//删除设备IC卡
	public static Result delLockDeviceICCard(){
		if( !checkSign() )
			return makSignFailor();
		ReturnJson reJson = new ReturnJson();
		String token = request().getQueryString("token");
		String lockid = request().getQueryString("lockid");
		String iccardid = request().getQueryString("iccardid");
		Integer nlockid = StringTool.GetInt(lockid, 0);
		Integer niccardid = StringTool.GetInt(iccardid, 0);
		if(StringUtil.isNull(token) || niccardid<=0 || nlockid<=0){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}	
		SmartAppUser user = UserService.findUserByToken(token);
		if(user==null){
			reJson.setTokenTimeOut();
		}else{
			SmartAppUserDevice userDevice = DeviceService.findUserDevice(user.getIdd(),nlockid);
			if(userDevice==null){
				reJson.setCode(1);
				reJson.setMessage("设备不存在");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			SmartDevice device = DeviceService.findDevice(nlockid);
			SmartDevicePassport devicePassport = DeviceService.findDevicePswWithId(device.getDeviceid(), niccardid);
			if(devicePassport!=null){
				if(devicePassport.getStat()==DeviceService.STATU_SMART_APP_DEVICE_PSW_ADD){
					reJson.setCode(2);
					reJson.setMessage("IC卡添加中不能删除");
					String reContent = JsonUtil.parseObj(reJson);
					appLogger.debug(reContent);
					return ok(reContent);
				}
				SmartOperService.delPassword(user.getPhone(), devicePassport);
				DeviceService.clearDevicePsws(device.getDeviceid());
			}
			reJson.setSuccess();
		}
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}
	public static Result setLockDeviceICCard(){
		if( !checkSign() )
			return makSignFailor();
		ReturnJson reJson = new ReturnJson();
		String token = request().getQueryString("token");
		String lockid = request().getQueryString("lockid");
		String iccardid = request().getQueryString("iccardid");
		String iccardname = request().getQueryString("iccardname");
		Integer nlockid = StringTool.GetInt(lockid, 0);
		Integer niccardid = StringTool.GetInt(iccardid, 0);
		if(StringUtil.isNull(token) || StringUtil.isNull(iccardname) || niccardid<=0 || nlockid<=0){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}	
		SmartAppUser user = UserService.findUserByToken(token);
		if(user==null){
			reJson.setTokenTimeOut();
		}else{
			SmartAppUserDevice userDevice = DeviceService.findUserDevice(user.getIdd(),nlockid);
			if(userDevice==null){
				reJson.setCode(1);
				reJson.setMessage("设备不存在");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			SmartDevice device = DeviceService.findDevice(nlockid);
			Ebean.getServer(GlobalDBControl.getDB()).createSqlUpdate("update smart_device_passport set userinfo=:userinfo where deviceid=:deviceid and idd=:idd")
			.setParameter("userinfo", iccardname).setParameter("deviceid", device.getDeviceid()).setParameter("idd", niccardid).execute();
			DeviceService.clearDevicePsws(device.getDeviceid());
			reJson.setSuccess();
		}
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}
}
