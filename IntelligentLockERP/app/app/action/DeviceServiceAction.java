package app.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.avaje.ebean.Ebean;

import app.dto.ReturnJson;
import app.service.UserService;
import controllers.SysConfigAction;
import models.DeviceService;
import models.DeviceServiceItems;
import models.SmartAppUser;
import models.SmartAppUserDevice;
import models.SmartDevice;
import models.SmartDeviceType;
import models.SmartInsurance;
import play.mvc.Result;
import util.GlobalDBControl;
import util.LogUtil;
import util.StringUtil;
import util.json.JsonUtil;

/**
 * 	设备服务
 */
public class DeviceServiceAction extends BaseAction{
	public static Result getDeviceServiceList() {
		String lockid = StringUtil.getHttpParam(request(), "lockid");
		String token = StringUtil.getHttpParam(request(), "token");
		
//		//sign签名校验
//		if(!checkSign() )
//			return makSignFailor();//校验失败
		
		SmartAppUser user = UserService.findUserByToken(token);//根据token获取用户信息

		ReturnJson reJson = new ReturnJson();
		//用户校验
		if(user==null) {
			reJson.setTokenTimeOut();//如果用户超时
			reJson.setMessage("请重新登陆");
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		
		DeviceService deviceService = readyConfigurationService(lockid,token);
		if(deviceService==null) {
			reJson.setParamsError();
			reJson.setMessage("对象为null 返回失败");
			String reContent = JsonUtil.parseObj(reJson);
			return ok(reContent);
		}
		reJson.setSuccess();
		reJson.setMessage("成功");
		reJson.setData(deviceService);
		String reContent = JsonUtil.parseObj(reJson);
		return ok(reContent);
	}
	
	public static DeviceService readyConfigurationService(String lockid,String token) {
		DeviceService deviceService = new DeviceService();//创建设备服务页对象 此时items已经实例化
		
		SmartDevice smartDevice = executeSqlRetUnique(SmartDevice.class, "find SmartDevice where idd = '"+lockid+"'");
		if(smartDevice==null)return null;//根据lockid为null下面逻辑无法执行 所以return
		
		getIntelligentLockService(deviceService.items,lockid,smartDevice,token);//执行下面的方法 执行链模式
		LogUtil.writeLog(smartDevice.getDeviceid(), "inslog");
		return deviceService;
	}
	/**
	 * 智能锁模块
	 * @param items 用于顺序添加模块
	 * @param lockid 锁id - deviceId
	 */
	public static void getIntelligentLockService(List<DeviceServiceItems> items,String lockid,SmartDevice smartDevice,String token) {
		DeviceServiceItems lockService = new DeviceServiceItems();
		/**
		 * 通过deviceid找到device表中的数据
		 */
		//购买日期
		Date buyTime = smartDevice.getBuytime();
		
		if(buyTime==null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				buyTime = sdf.parse("1970-1-1 00:00:00");
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * 根据device表中的type找到type表中的数据
		 */
		SmartDeviceType smartDeviceType = executeSqlRetUnique(SmartDeviceType.class,"find SmartDeviceType where idd='"+smartDevice.getType()+"' ");
		/**
		 * 获取购买时间和激活时间
		 */
		SmartAppUserDevice smartAppUserDevice = executeSqlRetUnique(SmartAppUserDevice.class, "find SmartAppUserDevice where deviceid='"+lockid+"'");
		//激活日期
		Date addTime = smartAppUserDevice.getAddtime();
		//左边标题d
		lockService.setLefttitle(smartDeviceType.getName());
		//左边图片的链接
		lockService.setLeftimg(smartDeviceType.getImage());
		lockService.setText(
							"购买日期: "	+	getDateFormat(buyTime)	+	"\n"	+
							"激活日期: "	+	getDateFormat(addTime)//addTime - 激活时间
						   );
		//右侧标题链接
		lockService.setRighttitle("查看使用说明");
		//右侧标题链接
		lockService.setRighttitlelink(SysConfigAction.findSysconfig("智能锁", "智能锁使用说明跳转链接"));
		
		items.add(lockService);
		
		getWarrantyCardService(items,smartDeviceType,buyTime,lockid,smartDevice.getDeviceid(),token);//执行下面的方法 执行链模式
	}
	/**
	 * 保修卡模块
	 * @param items 用于顺序添加模块
	 * @param smartDeviceType 获取锁类型对象
	 * @param addTime 锁激活时间
	 * @param lockid 锁id - deviceId
	 */
	public static void getWarrantyCardService(List<DeviceServiceItems> items,SmartDeviceType smartDeviceType,Date buyTime,String lockid,String deviceid,String token) {
		DeviceServiceItems warrantyCardService = new DeviceServiceItems();
		warrantyCardService.setLefttitle("保修卡");
		Integer warrantyValidity = smartDeviceType.getWarrantyValidity()==null?0:smartDeviceType.getWarrantyValidity();
		if(warrantyValidity==null) {
			warrantyValidity = 100;
		}
//		remainingNumberOfDays(addTime,warrantyValidity);
		
		//保修卡左侧图片
		warrantyCardService.setLeftimg("/public/images/weixiu.png");
		
		warrantyCardService.setText(
									smartDeviceType.getName()+"\n"+
									"购买日期: "		+	getDateFormat(buyTime)	+"\n"+
									"保修期: "		+	warrantyValidity	+	"年\n"+
									"距离质保期结束: "	+	remainingNumberOfDays(buyTime,warrantyValidity)	+	"天"
								   );
		warrantyCardService.setRighttitle("查看保修政策");
		//右侧标题链接
		String bxzcLink = SysConfigAction.findSysconfig("智能锁", "保修卡查看保修政策链接");
		Calendar cal = Calendar.getInstance();
		cal.setTime(buyTime);
		cal.add(Calendar.YEAR, warrantyValidity);
		bxzcLink = (bxzcLink+"?b="+getDateFormat(buyTime)+"&o="+getDateFormat(cal.getTime()));
		warrantyCardService.setRighttitlelink(bxzcLink);
		
		warrantyCardService.setButtontext("故障报修");
		warrantyCardService.setButtonlink(SysConfigAction.findSysconfig("智能锁", "保修卡故障报修"));
		
		items.add(warrantyCardService);
		//执行下面的方法 执行链模式
		getInsuranceService(items,lockid,buyTime,deviceid,token);
	}
	/**
	 * 保险模块
	 * @param items 用于顺序添加模块
	 * @param lockid 锁id - deviceId
	 * @param addTime 锁激活时间
	 */
	public static void getInsuranceService(List<DeviceServiceItems> items,String lockid,Date addTime,String deviceid,String token) {
		DeviceServiceItems insuranceService = new DeviceServiceItems();
		//执行泛型方法 参数1:指定model类class对象
		SmartInsurance smartInsurance = executeSqlRetUnique(SmartInsurance.class, "find SmartInsurance where deviceid='"+deviceid+"' and status='0'");//获取该设备有效的保险信息
		insuranceService.setLeftimg("/public/images/baoxian.png");
		insuranceService.setLefttitle("我的保险");
		insuranceService.setText(
								 "智家保保险"	+	"\n"	+
								 "服务期限: "		+	SysConfigAction.findSysconfig("智能锁", "我的保险服务期限日期")	+	"年"
								);
		//待领取
		if(smartInsurance==null){//没找到有效的保险信息
			//右侧图片根据结果变化
			insuranceService.setRightimg("/public/images/dlq@3x.png");//待领取 
			//根据结果变化
			insuranceService.setButtontext("领取保险");
			insuranceService.setButtonlink(SysConfigAction.findSysconfig("智能锁", "我的保险领取保险链接")+"?deviceid="+deviceid+"&token="+token);
		}else if(System.currentTimeMillis()<smartInsurance.getBegintime().getTime()) {
			insuranceService.setRightimg("/public/images/dsx@3x.png");//待生效
			insuranceService.setButtontext("查看保单");
			insuranceService.setButtonlink(SysConfigAction.findSysconfig("智能锁", "我的保险查看保单链接")+"?deviceid="+deviceid+"&token="+token);
		}else if(remainingNumberOfDays(smartInsurance.getOvertime(),smartInsurance.getValidity())>0) {
			insuranceService.setRightimg("/public/images/ysx@3x.png");//已生效
			insuranceService.setButtontext("查看保单");
			insuranceService.setButtonlink(SysConfigAction.findSysconfig("智能锁", "我的保险查看保单链接")+"?deviceid="+deviceid+"&token="+token);
		}else if(remainingNumberOfDays(smartInsurance.getOvertime(),smartInsurance.getValidity())<=0) {//已失效
			insuranceService.setRightimg("/public/images/yshix@3x.png");//已失效
			insuranceService.setButtontext("查看保单");
			insuranceService.setButtonlink(SysConfigAction.findSysconfig("智能锁", "我的保险已失效链接"));
		}
		
		
		items.add(insuranceService);
		
		getFlowService(items,addTime);//执行下面的方法 执行链模式
	}
	/**
	 * 流量模块
	 * @param items 用于顺序添加模块
	 * @param addTime 锁激活时间
	 */
	public static void getFlowService(List<DeviceServiceItems> items,Date addTime) {
		DeviceServiceItems flowService = new DeviceServiceItems();
		long dayoff = (System.currentTimeMillis()-addTime.getTime())/1000/60/60/24;
		flowService.setLeftimg("/public/images/liuliang.png");
		flowService.setLefttitle("我的流量");
		flowService.setText(
							"可使用年限: "	+	10	+"年\n"+
							"已使用: "	+	(dayoff>365?dayoff/365>=10?"10年":dayoff/365+"年"+dayoff%365+"天":dayoff+"天")	+"\n"+
							"剩余期限: "	+	(remainingNumberOfDays(addTime,10)>365?
									
													remainingNumberOfDays(addTime,10)/365+"年"+((remainingNumberOfDays(addTime,10)%365-2)>0?(remainingNumberOfDays(addTime,10)%365-2)+"天":"")
													:
													remainingNumberOfDays(addTime,10)>0?
															
															remainingNumberOfDays(addTime,10)-(remainingNumberOfDays(addTime,10)%365-2)+"天"
															:
															"已过期")
						  );
		items.add(flowService);
	}
	/**
	 * 泛型方法
	 * @param obj 查询的model类的class对象
	 * @param sql 查询语句
	 * @return 返回一个obj的实例对象
	 */
	public static <O> O executeSqlRetUnique(Class<O> obj,String sql) {
		return Ebean.getServer(GlobalDBControl.getDB()).createQuery(obj, sql).findUnique();
	}
	/**
	 * 泛型方法
	 * @param obj 查询的model类的class对象
	 * @param sql 查询语句
	 * @return 返回一个obj类型的集合
	 */
	public <O> List<O> executeSqlRetList(Class<O> obj,String sql) {
		return Ebean.getServer(GlobalDBControl.getDB()).createQuery(obj, sql).findList();
	}
	/**
	 * 泛型方法
	 * @param obj 将obj保存到数据库
	 */
	public <O> void recordInDB(O obj) {
		Ebean.getServer(GlobalDBControl.getDB()).save(obj);
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
	 * 计算剩余天数
	 * @param startTime 购买时间/激活时间
	 * @param totalYear 总有效期
	 * @return 计算购买时间/激活时间到总有效期 的剩余天数 返回-0 表示已经到期
	 */
	public static int remainingNumberOfDays(Date startTime,int totalYear) {
		if(startTime==null)return 0;
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		
		String dateDay = format.format(startTime);
		
		String[] yearInfo = dateDay.split(" ")[0].split("\\.");
		String[] timeInfo = dateDay.split(" ")[1].split(":");
		/**
		 * 获取传入参数startTime中的所有时间单位
		 * 将时间单位解析出来
		 */
		int year = Integer.parseInt(yearInfo[0]);
		int month = Integer.parseInt(yearInfo[1]);
		int day = Integer.parseInt(yearInfo[2]);
		int hour = Integer.parseInt(timeInfo[0]);
		int minute = Integer.parseInt(timeInfo[1]);
		int second = Integer.parseInt(timeInfo[2]);
		
		Calendar calendar = Calendar.getInstance();
		TimeZone tz = TimeZone.getTimeZone("GMT+20:00");  
		calendar.setTimeZone(tz);
		/**
		 * 将解析出来的所有时间单位放到日历对象中
		 */
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month-1);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		calendar.set(Calendar.HOUR, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, second);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.add(Calendar.YEAR, totalYear);
		//获取当前时间戳
		long curTime = System.currentTimeMillis();
		//获取传入进来的startTime（购买时间/激活时间）的时间戳
		long endTime = calendar.getTimeInMillis();
		/**
		 * 计算出相差时间
		 */
		long remainder = endTime-curTime;
		//返回计算得出相差的天数
		//当相差天数为负数 返回0
		return remainder/1000/60/60/24>0?(int) (remainder/1000/60/60/24):0;
	}
	
}
