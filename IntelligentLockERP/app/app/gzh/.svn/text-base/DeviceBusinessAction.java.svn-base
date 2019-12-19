package app.gzh;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import models.IntelligentLockProduct;
import models.IntelligentLockProductCode;
import models.LockDevice;
import models.SmartAppUser;
import models.SmartAppUserDevice;
import models.SmartDevice;
import models.SmartDeviceLog;
import models.SmartDevicePassport;
import models.SmartDeviceType;
import models.SmartInsurance;
import models.SmartVip;
import models.SubWxUser;
import net.SmartOperService;
import play.Logger;
import play.mvc.Result;
import util.AjaxHellper;
import util.GlobalDBControl;
import util.GlobalSetting;
import util.LogUtil;
import util.StringUtil;
import util.classEntity.StringTool;
import util.json.JsonUtil;
import app.action.BaseAction;
import app.action.DeviceServiceAction;
import app.dto.LockDeviceICCardDto;
import app.dto.LockDeviceLogsDto;
import app.dto.LockDevicePswListDto;
import app.dto.ReturnJson;
import app.service.DeviceService;
import app.service.SubWxUserService;
import app.service.UserService;
import app.service.VipService;

import com.avaje.ebean.Ebean;

import controllers.SysConfigAction;

//3.设备服务
public class DeviceBusinessAction extends BaseAction{
	static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static final SimpleDateFormat simpleDateFormatT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	static final SimpleDateFormat smallSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	static final SimpleDateFormat timeSimpleDateFormat = new SimpleDateFormat("HH:mm:ss");
	//流量最大使用年限
	static final Integer FLOW_MAX_USE_YEAR = 10;
	
	/**
	 * 3.1返回设备列表 
	 * @return Result
	 */
	public static Result getLockDeviceList() {
		
		ReturnJson reJson = new ReturnJson();
		
		String openid = session(GlobalSetting.GZH_OPENID);
		
		if (StringUtil.isNull(openid)) {
			reJson.setCode(2);
			reJson.setMessage("非法登录,请关注微信：SKN智能生活" + "\t" + "公众号进行登录");
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		
		SubWxUser subWxUser = SubWxUserService.findIllegalNotifyWxUser(openid);
		
		if(subWxUser==null){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		
		
		SmartAppUser user = UserService.findUserByPhone(subWxUser.getPhone());
		if(user==null){
			reJson.setTokenTimeOut();
		}else{
			List<SmartAppUserDevice> devices = DeviceService.findUserDevices(user.getIdd());
			Map<String,Object> data = new HashMap<String,Object>();
			List<LockDevice> items = new ArrayList<LockDevice>();
			for(SmartAppUserDevice userDevice:devices){
				Integer lockid = userDevice.getDeviceid();
				initLockDevice(String.valueOf(lockid),items,user.getIdd());
			}
			//首先判断该用户	是否为会员用户/会员是否过期
			SmartVip vip = VipService.selectSmartVipByUserid(user.getIdd());
			if(VipService.checkVip(user.getIdd())==2){//1:是会员并且没过期 2:不是会员 3:会员过期
				data.put("overtime","0");
			}else {
				data.put("overtime",vip.getOvertime());
			}
			data.put("items", items);
			reJson.setSuccess();
			reJson.setData(data);
		}
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);	
	}
	/**
	 * 3.2获取设备详情
	 * @return Result
	 */
	public static Result getLockDeviceDetail() {
		ReturnJson reJson = new ReturnJson();
		
		String openid = session(GlobalSetting.GZH_OPENID);
		
		if (StringUtil.isNull(openid)) {
			reJson.setCode(2);
			reJson.setMessage("非法登录,请关注微信：SKN智能生活" + "\t" + "公众号进行登录");
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		
		SubWxUser subWxUser = SubWxUserService.findIllegalNotifyWxUser(openid);
		
		if(subWxUser==null){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		String lockid = request().getQueryString("lockid");
		Integer nlockid = StringTool.GetInt(lockid, 0);
		
		if(nlockid<=0){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}	
		
		SmartAppUser user = UserService.findUserByPhone(subWxUser.getPhone());
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
			Map<String,Object> data = new HashMap<String,Object>();
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
			data.put("items",items);
			data.put("lockname", device.getDevicedesc());
			SmartDeviceType smartDeviceType = Ebean.getServer(GlobalDBControl.getDB()).find(SmartDeviceType.class).where().eq("idd", device.getType()).findUnique();

			data.put("locktype", smartDeviceType.getName());
			data.put("lockelectricity", device.getPower());
			data.put("lockdeviceid", device.getDeviceid());
			data.put("lockid", lockid);
			reJson.setSuccess();
			reJson.setData(data);
		}
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}
	/**
	 * 3.3添加设备
	 * @return Result
	 */
	public static Result addLockDevice() {
		ReturnJson reJson = new ReturnJson();
		
		String openid = session(GlobalSetting.GZH_OPENID);
		
		if (StringUtil.isNull(openid)) {
			reJson.setCode(2);
			reJson.setMessage("非法登录,请关注微信：SKN智能生活" + "\t" + "公众号进行登录");
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		
		SubWxUser subWxUser = SubWxUserService.findIllegalNotifyWxUser(openid);
		
		if(subWxUser==null){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		
		String lockcode = request().getQueryString("lockcode");
		
		if(StringUtil.isNull(lockcode)){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}	
		
		SmartAppUser user = UserService.findUserByPhone(subWxUser.getPhone());
		if(user==null){
			reJson.setTokenTimeOut();
		}else{
			if(!VipService.checkFreezeDevice(lockcode)){
				reJson.setCode(3);
				reJson.setMessage("此设备正在退货中");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			SmartDevice device = DeviceService.findDevice(lockcode);
			IntelligentLockProductCode intelligentLockProductCode = Ebean.getServer(GlobalDBControl.getReadDB()).find(IntelligentLockProductCode.class).where().eq("deviceid", lockcode).findUnique();
			if(intelligentLockProductCode==null){
				reJson.setCode(2);
				reJson.setMessage("设备不存在");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}

			if(intelligentLockProductCode.getStatus()==0) {
				reJson.setCode(2);
				reJson.setMessage("未查到此设备");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			
			if(device==null){//有锁但未绑定
				IntelligentLockProduct intelligentLockProduct = Ebean.getServer(GlobalDBControl.getReadDB()).find(IntelligentLockProduct.class).where().eq("idd", intelligentLockProductCode.getProductid()).findUnique();
				String title = intelligentLockProduct.getTitle();
				if(title.indexOf("M6")!=-1){//=M6
					//添加设备
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
				
				device = new SmartDevice();
				device.setAddtime(new Date());
				device.setDeviceid(lockcode);
				device.setLastconnect(new Date());
				device.setSecret(StringUtil.mkNumRanmString(10));
				device.setDevicedesc("SKN智能锁");
				
				//智能锁名称和产品表 里名称需要一样，才能取到 智能锁类型
				SmartDeviceType smartDeviceType = Ebean.getServer(GlobalDBControl.getReadDB()).find(SmartDeviceType.class).where().eq("name", title).findUnique();
				
				device.setType(smartDeviceType.getIdd());
				
				Ebean.getServer(GlobalDBControl.getDB()).save(device);
				
				// 设置设备 属于该用户
				SmartAppUserDevice spd = new SmartAppUserDevice();
				spd.setDeviceid(device.getIdd());
				spd.setDeviceAppUser(user.getIdd());
				spd.setStatus(0);
				spd.setAddtime(new Date());
				spd.setUsertype(1);

				Ebean.getServer(GlobalDBControl.getDB()).save(spd);
				
				//会员有效期增加一年
				VipService.addVipValidityTimeByDevice(user.getIdd(),device.getDeviceid());
				//根据lockid找到SmartDevice表中对应锁的deviceid
				SmartDevice smartDevice = Ebean.getServer(GlobalDBControl.getDB()).createQuery(SmartDevice.class,"find SmartDevice where deviceid = '"+device.getDeviceid()+"'").findUnique();
				if(smartDevice.getBuytime()==null){
					String deviceId = smartDevice.getDeviceid();
					//根据锁deviceid找到主库明细表对应锁的出库时间
					Date outTime = DeviceService.getLockOutDBTimeNoCache(deviceId);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					//根据锁的deviceid将SmartDevice表中对应锁的购买时间与出库时间同步
					DeviceService.updateBuyTimeByDeviceIdNoCache(outTime,deviceId);
				}
				
				reJson.setSuccess();
				DeviceService.clearUserDevicesCache(user.getIdd());
				String reContent = JsonUtil.parseObj(reJson);	//设备添加成功
				appLogger.debug(reContent);
				return ok(reContent);
				
			}
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
			
		}
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}
	/**
	 * 3.4设置设备名称
	 * @return Result
	 */
	public static Result setLockDeviceName() {
		ReturnJson reJson = new ReturnJson();
		
		String openid = session(GlobalSetting.GZH_OPENID);
		
		if (StringUtil.isNull(openid)) {
			reJson.setCode(2);
			reJson.setMessage("非法登录,请关注微信：SKN智能生活" + "\t" + "公众号进行登录");
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		
		SubWxUser subWxUser = SubWxUserService.findIllegalNotifyWxUser(openid);
		
		if(subWxUser==null){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		
		String lockid = request().getQueryString("lockid");//必传
		String devicename = request().getQueryString("devicename");//设备名称 必传
		
		Integer nlockid = StringTool.GetInt(lockid, 0);
		if(StringUtil.isNull(devicename) || nlockid<=0){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}	
		SmartAppUser user = UserService.findUserByPhone(subWxUser.getPhone());
		if(user==null){
			reJson.setTokenTimeOut();
		}else{
			SmartAppUserDevice userDevice = DeviceService.findUserDevice(user.getIdd(),nlockid);//从用户所有锁中找到指定id的锁
			if(userDevice==null){//该锁不属于用户
				reJson.setCode(1);
				reJson.setMessage("设备不存在");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			//更新名称
			Ebean.getServer(GlobalDBControl.getDB()).createSqlUpdate("update smart_device set devicedesc=:devicedesc where idd=:idd")
			.setParameter("devicedesc", devicename).setParameter("idd", nlockid).execute();
			DeviceService.clearDeviceCache(nlockid);
			reJson.setSuccess();
		}
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}
	/**
	 * 3.5获取设备密码列表
	 * @return Result
	 */
	public static Result getLockDevicePswList() {
		ReturnJson reJson = new ReturnJson();
		
		String openid = session(GlobalSetting.GZH_OPENID);
		
		if (StringUtil.isNull(openid)) {
			reJson.setCode(2);
			reJson.setMessage("非法登录,请关注微信：SKN智能生活" + "\t" + "公众号进行登录");
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		
		SubWxUser subWxUser = SubWxUserService.findIllegalNotifyWxUser(openid);
		
		if(subWxUser==null){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		
		String lockid = request().getQueryString("lockid");//必传
		Integer nlockid = StringTool.GetInt(lockid, 0);
		if(nlockid<=0){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}	
		SmartAppUser user = UserService.findUserByPhone(subWxUser.getPhone());
		if(user==null){
			reJson.setTokenTimeOut();
		}else{
			SmartAppUserDevice userDevice = DeviceService.findUserDevice(user.getIdd(),nlockid);//找到用户的锁
			if(userDevice==null){//用户没这把锁
				reJson.setCode(1);
				reJson.setMessage("设备不存在");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			LockDevicePswListDto data = new LockDevicePswListDto();
			List<LockDevicePswListDto.Item> items = new ArrayList<LockDevicePswListDto.Item>();
			SmartDevice device = DeviceService.findDevice(nlockid);
			//获取密码对象集合
			List<SmartDevicePassport> devicePsws = DeviceService.findDevicePswWithType(device.getDeviceid(),DeviceService.TYPE_SMART_APP_DEVICE_PSW_NUMBER);
			for(SmartDevicePassport devicePsw:devicePsws){//封装每一个对象
				LockDevicePswListDto.Item item = new LockDevicePswListDto.Item();
				item.setPswcode(devicePsw.getPassword());
				item.setPswid(devicePsw.getIdd());
				item.setPswname(devicePsw.getUserinfo());
				item.setPswbegintime(StringUtil.getDateTimeStr(devicePsw.getAddtime()));
				item.setPswvalidity(devicePsw.getValidetime()==null?"-1":StringUtil.getDateTimeStr(devicePsw.getValidetime()));
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
	/**
	 * 3.6添加设备密码
	 * @return Result
	 */
	public static Result addLockDevicePsw() {
		ReturnJson reJson = new ReturnJson();
		
		String openid = session(GlobalSetting.GZH_OPENID);
		
		if (StringUtil.isNull(openid)) {
			reJson.setCode(2);
			reJson.setMessage("非法登录,请关注微信：SKN智能生活" + "\t" + "公众号进行登录");
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		
		SubWxUser subWxUser = SubWxUserService.findIllegalNotifyWxUser(openid);
		
		if(subWxUser==null){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		
		String lockid = request().getQueryString("lockid");
		String pswname = request().getQueryString("pswname");//设备密码使用人
		String pswcode = request().getQueryString("pswcode");//设备密码
		String begintime = request().getQueryString("begintime");    //开始时间
		String pswvalidity = request().getQueryString("pswvalidity");//失效时间
		if(begintime==null){
			begintime = "-1";
		}else {
			try {
				long begintimetime = simpleDateFormatT.parse(begintime).getTime()+(1000*60*60);
				long pswvaliditytime = simpleDateFormatT.parse(pswvalidity).getTime();
				if(begintimetime>pswvaliditytime) {
					reJson.setCode(3);
					reJson.setMessage("失效时间必须大于生效时间1小时以上");
					String reContent = JsonUtil.parseObj(reJson);
					appLogger.debug(reContent);
					return ok(reContent);
				}else if(pswvaliditytime<(new Date().getTime()-2000)) {
					reJson.setCode(3);
					reJson.setMessage("开始时间必须 大于 当前时间");
					String reContent = JsonUtil.parseObj(reJson);
					appLogger.debug(reContent);
					return ok(reContent);
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		Integer nlockid = StringTool.GetInt(lockid, 0);
		if(StringUtil.isNull(pswvalidity) 
				|| StringUtil.isNull(pswcode) || nlockid<=0){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}	
		SmartAppUser user = UserService.findUserByPhone(subWxUser.getPhone());
		if(user==null){
			reJson.setTokenTimeOut();
		}else{
			try{
				if(pswcode.length()!=6){
					reJson.setCode(3);
					reJson.setMessage("密码格式错误");
					String reContent = JsonUtil.parseObj(reJson);
					appLogger.debug(reContent);
					return ok(reContent);
				}
				SmartAppUserDevice userDevice = DeviceService.findUserDevice(user.getIdd(),nlockid);//获取用户的锁
				if(userDevice==null){//用户没这把锁
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
						pswvalidity.equals("-1")?null:simpleDateFormatT.parse(pswvalidity),
						begintime.equals("-1")?null:simpleDateFormatT.parse(begintime)
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
	/**
	 * 3.7删除设备密码
	 * @return Result
	 */
	public static Result delLockDevicePsw() {
		ReturnJson reJson = new ReturnJson();
		
		String openid = session(GlobalSetting.GZH_OPENID);
		
		if (StringUtil.isNull(openid)) {
			reJson.setCode(2);
			reJson.setMessage("非法登录,请关注微信：SKN智能生活" + "\t" + "公众号进行登录");
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		
		SubWxUser subWxUser = SubWxUserService.findIllegalNotifyWxUser(openid);
		
		if(subWxUser==null){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		
		String lockid = request().getQueryString("lockid");
		String pswid = request().getQueryString("pswid");
		Integer nlockid = StringTool.GetInt(lockid, 0);
		Integer npswid = StringTool.GetInt(pswid, 0);
		if(npswid<=0 || nlockid<=0){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}	
		SmartAppUser user = UserService.findUserByPhone(subWxUser.getPhone());
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
				if(devicePassport.getStat()==DeviceService.STATU_SMART_APP_DEVICE_PSW_ADD){
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
	/**
	 * 3.8编辑设备密码
	 * @return Result
	 */
	public static Result setLockDevicePsw() {
		ReturnJson reJson = new ReturnJson();
		
		String openid = session(GlobalSetting.GZH_OPENID);
		
		if (StringUtil.isNull(openid)) {
			reJson.setCode(2);
			reJson.setMessage("非法登录,请关注微信：SKN智能生活" + "\t" + "公众号进行登录");
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		
		SubWxUser subWxUser = SubWxUserService.findIllegalNotifyWxUser(openid);
		
		if(subWxUser==null){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}

		String lockid = request().getQueryString("lockid");
		String pswid = request().getQueryString("pswid");
		String pswname = request().getQueryString("pswname");
		Integer nlockid = StringTool.GetInt(lockid, 0);
		Integer npswid = StringTool.GetInt(pswid, 0);
		if(StringUtil.isNull(pswname) || npswid<=0 || nlockid<=0){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}	
		SmartAppUser user = UserService.findUserByPhone(subWxUser.getPhone());
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
	/**
	 * 3.9获取设备指纹列表
	 * @return Result
	 */
	public static Result getLockDeviceFingerPrintList() {
		ReturnJson reJson = new ReturnJson();
		
		String openid = session(GlobalSetting.GZH_OPENID);
		
		if (StringUtil.isNull(openid)) {
			reJson.setCode(2);
			reJson.setMessage("非法登录,请关注微信：SKN智能生活" + "\t" + "公众号进行登录");
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		
		SubWxUser subWxUser = SubWxUserService.findIllegalNotifyWxUser(openid);
		
		if(subWxUser==null){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		
		String lockid = request().getQueryString("lockid");
		Integer nlockid = StringTool.GetInt(lockid, 0);
		if(nlockid<=0){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}	
		SmartAppUser user = UserService.findUserByPhone(subWxUser.getPhone());
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
			Map<String,Object> data = new HashMap<String,Object>();
			Map<String,Object> item = new HashMap<String,Object>();
			List<Map<String,Object>> items = new ArrayList<Map<String,Object>>();
			SmartDevice device = DeviceService.findDevice(nlockid);
			List<SmartDevicePassport> devicePsws = DeviceService.findDevicePswWithType(device.getDeviceid(),DeviceService.TYPE_SMART_APP_DEVICE_PSW_FINGER);
			for(SmartDevicePassport devicePsw:devicePsws){
				item.put("fingerprintvalidity",devicePsw.getValidetime()==null?"-1":StringUtil.getDateStr(devicePsw.getValidetime()));
				item.put("fingerprintid",devicePsw.getIdd());
				item.put("fingerprintname",devicePsw.getUserinfo());
				item.put("fingerprintstatu",devicePsw.getStat());
				items.add(item);
			}
			data.put("items",items);
			reJson.setSuccess();
			reJson.setData(data);
		}
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}
	/**
	 * 3.10删除设备指纹
	 * @return Result
	 */
	public static Result delLockDeviceFingerPrint() {
		ReturnJson reJson = new ReturnJson();
		
		String openid = session(GlobalSetting.GZH_OPENID);
		
		if (StringUtil.isNull(openid)) {
			reJson.setCode(2);
			reJson.setMessage("非法登录,请关注微信：SKN智能生活" + "\t" + "公众号进行登录");
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		
		SubWxUser subWxUser = SubWxUserService.findIllegalNotifyWxUser(openid);
		
		if(subWxUser==null){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		
		String lockid = request().getQueryString("lockid");
		String fingerprintid = request().getQueryString("fingerprintid");
		Integer nlockid = StringTool.GetInt(lockid, 0);
		Integer nfingerprintid = StringTool.GetInt(fingerprintid, 0);
		if(nfingerprintid<=0 || nlockid<=0){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}	
		SmartAppUser user = UserService.findUserByPhone(subWxUser.getPhone());
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
	/**
	 * 3.11编辑设备指纹
	 * @return Result
	 */
	public static Result setLockDeviceFingerPrint() {
		ReturnJson reJson = new ReturnJson();
		
		String openid = session(GlobalSetting.GZH_OPENID);
		
		if (StringUtil.isNull(openid)) {
			reJson.setCode(2);
			reJson.setMessage("非法登录,请关注微信：SKN智能生活" + "\t" + "公众号进行登录");
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		
		SubWxUser subWxUser = SubWxUserService.findIllegalNotifyWxUser(openid);
		
		if(subWxUser==null){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		
		String lockid = request().getQueryString("lockid");
		String fingerprintid = request().getQueryString("fingerprintid");
		String fingerprintname = request().getQueryString("fingerprintname");
		Integer nlockid = StringTool.GetInt(lockid, 0);
		Integer nfingerprintid = StringTool.GetInt(fingerprintid, 0);
		if(StringUtil.isNull(fingerprintname) || nfingerprintid<=0 || nlockid<=0){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}	
		SmartAppUser user = UserService.findUserByPhone(subWxUser.getPhone());
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
	/**
	 * 3.12获取设备IC卡列表
	 * @return Result
	 */
	public static Result getLockDeviceICCardList() {
		ReturnJson reJson = new ReturnJson();
		
		String openid = session(GlobalSetting.GZH_OPENID);
		
		if (StringUtil.isNull(openid)) {
			reJson.setCode(2);
			reJson.setMessage("非法登录,请关注微信：SKN智能生活" + "\t" + "公众号进行登录");
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		
		SubWxUser subWxUser = SubWxUserService.findIllegalNotifyWxUser(openid);
		
		if(subWxUser==null){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		
		String lockid = request().getQueryString("lockid");
		Integer nlockid = StringTool.GetInt(lockid, 0);
		if(nlockid<=0){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}	
		SmartAppUser user = UserService.findUserByPhone(subWxUser.getPhone());
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
	/**
	 * 3.13删除设备IC卡
	 * @return Result
	 */
	public static Result delLockDeviceICCard() {
		ReturnJson reJson = new ReturnJson();
		
		String openid = session(GlobalSetting.GZH_OPENID);
		
		if (StringUtil.isNull(openid)) {
			reJson.setCode(2);
			reJson.setMessage("非法登录,请关注微信：SKN智能生活" + "\t" + "公众号进行登录");
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		
		SubWxUser subWxUser = SubWxUserService.findIllegalNotifyWxUser(openid);
		
		if(subWxUser==null){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		
		String lockid = request().getQueryString("lockid");
		String iccardid = request().getQueryString("iccardid");
		Integer nlockid = StringTool.GetInt(lockid, 0);
		Integer niccardid = StringTool.GetInt(iccardid, 0);
		if(niccardid<=0 || nlockid<=0){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}	
		SmartAppUser user = UserService.findUserByPhone(subWxUser.getPhone());
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
	/**
	 * 3.14编辑设备IC卡
	 * @return Result
	 */
	public static Result setLockDeviceICCard() {
		ReturnJson reJson = new ReturnJson();
		
		String openid = session(GlobalSetting.GZH_OPENID);
		
		if (StringUtil.isNull(openid)) {
			reJson.setCode(2);
			reJson.setMessage("非法登录,请关注微信：SKN智能生活" + "\t" + "公众号进行登录");
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		
		SubWxUser subWxUser = SubWxUserService.findIllegalNotifyWxUser(openid);
		
		if(subWxUser==null){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		
		String lockid = request().getQueryString("lockid");
		String iccardid = request().getQueryString("iccardid");
		String iccardname = request().getQueryString("iccardname");
		Integer nlockid = StringTool.GetInt(lockid, 0);
		Integer niccardid = StringTool.GetInt(iccardid, 0);
		if(StringUtil.isNull(iccardname) || niccardid<=0 || nlockid<=0){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}	
		SmartAppUser user = UserService.findUserByPhone(subWxUser.getPhone());
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
	/**
	 * 校验是否是vip
	 * 
	 * @return 1-是会员 2- 不是会员 3-会员过期 4-签名校验失败
	 */
	public static Result checkVip() {
		ReturnJson reJson = new ReturnJson();
		String token = request().getQueryString("token");
		String openid = session(GlobalSetting.GZH_OPENID);
		SmartAppUser user = null;
		if(StringUtil.isNull(openid)) {//公众号登陆
			if (StringUtil.isNull(token)) {
				reJson.setTokenTimeOut();
				reJson.setMessage("请先登陆"+token);
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			user = UserService.findUserByToken(token);
			if(user==null){
				reJson.setTokenTimeOut();
				reJson.setMessage("请先登陆");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
		}else {//商城登陆
			SubWxUser subWxUser = SubWxUserService.findIllegalNotifyWxUser(openid);
			if(subWxUser==null){
				reJson.setCode(2);
				reJson.setMessage("非法登录,请关注微信：SKN智能生活" + "\t" + "公众号进行登录");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			user = UserService.findUserByPhone(subWxUser.getPhone());
		}
		
		Map<String,Integer> data = new HashMap<String,Integer>();
		if(user==null){
			reJson.setTokenTimeOut();
		}else{
			Integer checkVip = VipService.checkVip(user.getIdd());
			data.put("vipStat", checkVip);
			reJson.setSuccess();
			reJson.setMessage("会员状态："+checkVip);
			reJson.setData(data);
		}
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}
	/**
	 * 获取User的token
	 * POST方式
	 * @param key
	 * @return
	 */
	public static String getHttpParamByPOST(String key){
		String value = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), key);
		if(value ==null || "".equals(value))
			value = session(key);
		else
			session().put(key,value);
		return value;
	}
	/**
	 * 获取User的token
	 * @param key
	 * @return
	 */
	public static String getHttpParam(String key){
		String value = request().getQueryString(key);
		if(value ==null || "".equals(value))
			value = session(key);
		else
			session().put(key,value);
		return value;
	}
	/**
	 *  获取设备购买时间
	 * @return 时间戳
	 */
	public static Result getBuyTime() {
		String deviceid = StringUtil.getHttpParam(request(), "deviceid");
		ReturnJson reJson = new ReturnJson();
		SmartDevice device = Ebean.getServer(GlobalDBControl.getDB()).find(SmartDevice.class).where().eq("deviceid", deviceid).findUnique();
		if(device==null) {
			reJson.setParamsError();
			reJson.setMessage("参数有误");
			String reContent = JsonUtil.parseObj(reJson);
			return ok(reContent);
		}
		Date buytime = device.getBuytime();
		reJson.setSuccess();
		reJson.setData(buytime.getTime());
		String reContent = JsonUtil.parseObj(reJson);
		return ok(reContent);
	}
	/**
	 * 初始化LockDeviceInfo对象
	 * @return LockDeviceInfo
	 */
	private static LockDevice initLockDevice(String lockId, List<LockDevice> items,Integer userIdd) {
		LockDevice item = new LockDevice();
		//将所有时间类型字段单独处理
		initItem(item,lockId,userIdd);
		
		items.add(item);
		
		return item;
	}
	/**
	 * 保险
	 */
	public static void getInsuranceService(String deviceid,LockDevice lockDevice,Integer userid) {
		
		SmartInsurance smartInsurance = DeviceServiceAction.executeSqlRetUnique(SmartInsurance.class, "find SmartInsurance where deviceid='"+deviceid+"' and status='0'");//获取该设备有效的保险信息
		//判断该用户是否是会员用户
		if(smartInsurance==null){//没找到有效的保险信息 待领取
			//根据结果变化
			lockDevice.setInsuranceStat("1");//待领取（领取保险）
			lockDevice.setInsuranceUrl(SysConfigAction.findSysconfig("智能锁", "我的保险领取保险链接")+"?deviceid="+deviceid);
		}else if(System.currentTimeMillis()<smartInsurance.getBegintime().getTime()) {
			lockDevice.setInsuranceStat("2");//待生效（查看保单）
			lockDevice.setInsuranceUrl(SysConfigAction.findSysconfig("智能锁", "我的保险查看保单链接")+"?deviceid="+deviceid);
		}else if(DeviceServiceAction.remainingNumberOfDays(smartInsurance.getOvertime(),smartInsurance.getValidity())>0) {
			lockDevice.setInsuranceStat("3");//已生效（查看保单）
			lockDevice.setInsuranceUrl(SysConfigAction.findSysconfig("智能锁", "我的保险查看保单链接")+"?deviceid="+deviceid);
		}else if(DeviceServiceAction.remainingNumberOfDays(smartInsurance.getOvertime(),smartInsurance.getValidity())<=0) {//已失效
			lockDevice.setInsuranceStat("4");//已失效（查看保单)
			lockDevice.setInsuranceUrl(SysConfigAction.findSysconfig("智能锁", "我的保险已失效链接"));
		}
		
	}
	/**
	 * 初始化LockDevice对象
	 * @param item Item对象
	 * @param lockId 设备ID
	 */
	private static void initItem(LockDevice item, String lockId,Integer userIdd) {
		//获取购买日期
		SmartDevice smartDevice = Ebean.getServer(GlobalDBControl.getDB()).find(SmartDevice.class).where().eq("idd", lockId).findUnique();
		//获取激活日期																										               ↓ 数据库表设计问题 这里的deviceid是设备的id 如：154
		SmartAppUserDevice smartAppUserDevice = Ebean.getServer(GlobalDBControl.getDB()).find(SmartAppUserDevice.class).where().eq("deviceid", lockId).findUnique();
		
		//获取过期日期
		SmartDeviceType smartDeviceType = Ebean.getServer(GlobalDBControl.getDB()).find(SmartDeviceType.class).where().eq("idd", smartDevice.getType()).findUnique();
		
		//设备名称
		item.setLockname(smartDeviceType.getName());
		item.setLockdescname(smartDevice.getDevicedesc());
		//设备型号
		//TODO 和设备名称同名
		item.setLocktype(smartDeviceType.getName());
		
		//设备ID
		item.setLockid(lockId);
		
		//设备DeviceId
		item.setLockdeviceid(smartDevice.getDeviceid());
		
		getInsuranceService(smartDevice.getDeviceid(), item,userIdd);//设置保险信息
		Calendar cal = Calendar.getInstance();
		cal.setTime(smartDevice.getBuytime());
		cal.add(Calendar.YEAR,smartDeviceType.getWarrantyValidity()==null?0:smartDeviceType.getWarrantyValidity());
		item.setWarrantyCardUrl(SysConfigAction.findSysconfig("智能锁", "保修卡查看保修政策链接")+"?b="+getDateFormat(smartDevice.getBuytime())+"&o="+getDateFormat(cal.getTime()));
		//图片地址
		item.setLockimgurl(smartDeviceType.getImage());
		
		//购买日期
		Date buytime = smartDevice.getBuytime();
		
		//过期年数
		Integer warrantyValidity = smartDeviceType.getWarrantyValidity()==null?0:smartDeviceType.getWarrantyValidity();
		
		//购买日期
		item.setBuytime(parseTime(buytime));
		
		//激活日期
		item.setBindtime(parseTime(smartAppUserDevice.getAddtime()));
		
		//根据购买日期 找到过期日期
		item.setOvertime(
				parseTime(getDateByTime(buytime, warrantyValidity))
				);
		
		//写死的流量可使用的年限
		item.setValidflowtime(FLOW_MAX_USE_YEAR);
		
		//已使用天数
		Integer useday = getNumberOfDaysByDate(buytime);
		
		//流量已使用天数
		item.setUseddays(useday);
		
		//流量剩余天数
		item.setSurplusdays(getLastDay(FLOW_MAX_USE_YEAR,useday));
		
	}
	/**
	 * 改变时间表达的格式
	 * @param date 时间对象
	 * @return 格式化后的时间字符串
	 */
	public static String getDateFormat(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
		return dateFormat.format(date);
	}
	/**
	 * 
	 * @param date 时间对象
	 * @return 返回指定格式年月日
	 */
	public static String parseTime(Date date) {
		if(date==null)return null;
		return smallSimpleDateFormat.format(date);
	}
	/**
	 * 获取指定天数后的日期
	 * @param time 时间段
	 * @param year 增加的年数
	 */
	public static Date getDateByTime(Date date,Integer year) {
		
		int day = year*365;
		
		//年数大于一年 就会差一天 需要手动添加
		if(year>1) day++;
		
		Calendar calendar = Calendar.getInstance();
		
		//给日历类设置时间
		calendar.setTime(date);
		
		calendar.set(calendar.DATE, calendar.get(calendar.DATE)+ day);
		
		return calendar.getTime();
	}
	
	/**
	 * 获取指定日期到今天的天数
	 * @param date 指定日期
	 * @return 指定日期到当前日期的天数
	 */
	public static Integer getNumberOfDaysByDate(Date date) {
		long i = (new Date().getTime()) - date.getTime();
		return (int) (i/1000/60/60/24);
	}
	
	/**
	 * 计算剩余天数
	 * @param year 年数
	 * @param day 已过天数
	 * @return 剩余天数(不会小于0)
	 */
	public static Integer getLastDay(Integer year,Integer day) {
		return (year*365)-day>0?(year*365)-day:0;
	}
	//查看openId对应的这条记录
	public static SubWxUser checkOpenidInfo(String openid){
		if(openid!=null){
			SubWxUser user = Ebean.getServer(GlobalDBControl.getDB())
				.createQuery(SubWxUser.class)
				.where().eq("openid", openid).eq("ch", SubWxUserService.CH_SUB)
				.findUnique();
			if(user!=null){
				return user;
			}
		}
		return null;
	}
}
