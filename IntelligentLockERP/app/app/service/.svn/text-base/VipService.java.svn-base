package app.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import org.joda.time.Hours;

import models.SmartAppUserDevice;
import models.SmartBounty;
import models.SmartDevice;
import models.SmartVip;
import play.Logger;
import util.GlobalDBControl;
import util.jedis.RedisUtil;
import app.dto.VipDto;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlRow;

import controllers.SysConfigAction;

public class VipService {
	
	static String SMART_BOUNTY_MSGS_COUNT = "smart:bounty:msgs:count:";
	static String SMART_BOUNTY_MSGS_IDS = "smart:bounty:msgs:ids:";
	static String SMART_BOUNTY_MSGS = "smart:bounty:msgs:";
	static int OVERTIME = 60;
	static int DBINDEX = 10;
	
	static String SMART_DEVICEID = "smart:deviceid:";
	static String FREEZE_DEVICEID = "freeze:device:";
	
	//通过充值 增加会员有效期的方法
	public static void addVipValidityTimeByRecharge(Integer userid){
		if(userid != null){
			//去数据库中查询该userid是否是会员
			if(checkVip(userid) == 2){	//(是会员并且没过期:1 不是会员:2 会员过期:3)
				if(becomeVip(userid,"","2")){
					util.LogUtil.writeLog("userid为:"+ userid +"的用户成功通过充值开通会员,有效期为一年","addVipValidityTimeLog");
				}else{
					util.LogUtil.writeLog("userid为:"+ userid +"的用户(之前不是会员用户)通过充值开通会员失败!", "addVipValidityTimeLog");
				}
			}else if(checkVip(userid) == 1){
				if(addVipTime(userid,"","2")){
					util.LogUtil.writeLog("userid为:"+ userid +"的用户通过充值成功增加一年会员有效期","addVipValidityTimeLog");
				}else{
					util.LogUtil.writeLog("userid为:"+ userid +"的用户通过充值增加一年会员有效期失败","addVipValidityTimeLog");
				}
			}else{
				if(updateVipTime(userid,"","2")){
					util.LogUtil.writeLog("userid为:"+ userid +"的用户(之前的会员已过期)通过充值重新开通会员,有效期为一年","addVipValidityTimeLog");
				}else{
					util.LogUtil.writeLog("userid为:"+ userid +"的用户(之前的会员已过期)通过充值重新开通会员失败","addVipValidityTimeLog");
				}
			}
		}else{
			util.LogUtil.writeLog("增加会员有效期失败,传入userid:"+ userid +"为:null", "addVipValidityTimeLog");
		}
	}
	
	//通过绑定设备 增加会员有效期的方法
	public static void addVipValidityTimeByDevice(Integer userid,String deviceid){
		util.LogUtil.writeLog("通过绑定设备deviceid:"+ deviceid +"给userid:"+ userid+"增加会员","addVipValidityTimeLog");
		if(userid != null){
			//首先去Redis中查找该deviceId是否已经被绑定并且已经增加过会员期限
			Integer value = RedisUtil.getInstance().getEntityStr(SMART_DEVICEID+deviceid,Integer.class,DBINDEX);
			//如果为null说明该设备并未绑定过,为其增加会员期限
			if(value==null){
				//判断该用户是否是会员用户
				if(checkVip(userid)==1){		//是会员并且没有过期
					//将会员日期延续一年
					if(addVipTime(userid,deviceid,"1")){
						RedisUtil.getInstance().setEntityStr(SMART_DEVICEID + deviceid,userid,DBINDEX);
						util.LogUtil.writeLog("通过绑定设备deviceid:"+ deviceid +"成功为userid:"+ userid +"的用户增加一年会员有效期","addVipValidityTimeLog");
					}else{
						util.LogUtil.writeLog("通过绑定设备deviceid:"+ deviceid +"为userid:"+ userid +"的用户(之前是会员用户)增加一年有效期失败!", "addVipValidityTimeLog");
					}
				}else if(checkVip(userid)==2){	//不是会员
					if(becomeVip(userid,deviceid,"1")){
						RedisUtil.getInstance().setEntityStr(SMART_DEVICEID + deviceid,userid,DBINDEX);
						util.LogUtil.writeLog("通过绑定设备deviceid:"+ deviceid +"成功为userid:"+ userid +"的用户开通会员,有效期为一年","addVipValidityTimeLog");
					}else{
						util.LogUtil.writeLog("通过绑定设备deviceid:"+ deviceid +"为userid:"+ userid +"的用户(之前不是会员用户)开通会员失败!", "addVipValidityTimeLog");
					}
				}else{	//会员已过期
					if(updateVipTime(userid,deviceid,"1")){
						RedisUtil.getInstance().setEntityStr(SMART_DEVICEID + deviceid,userid,DBINDEX);
						util.LogUtil.writeLog("通过绑定设备deviceid:"+ deviceid +"成功为userid:"+ userid +"的用户(之前的会员已过期)重新开通会员,有效期为一年","addVipValidityTimeLog");
					}else{
						util.LogUtil.writeLog("通过绑定设备deviceid:"+ deviceid +"为userid:"+ userid +"的用户(之前的会员已过期)修改会员信息失败!", "addVipValidityTimeLog");
					}
				}
			}else{
				util.LogUtil.writeLog("为userid:"+ userid +"的用户增加会员有效期失败,传入deviceid:"+ deviceid +"已经被其他用户绑定过!", "addVipValidityTimeLog");
			}
		}else{
			util.LogUtil.writeLog("增加会员有效期失败,传入userid:"+ userid +"为:null", "addVipValidityTimeLog");
		}
	}
	
	//用户取消订单,将该用户的会员期限减少一年
	public static void reduceVipValidityByCancelOrder(String deviceid){
		if(deviceid != null&&!(deviceid.equals(""))&&!(deviceid.equals("undefined"))){
			//首先去REDIS中查找该deviceId是否存在
			Integer userid = RedisUtil.getInstance().getEntityStr(SMART_DEVICEID+deviceid,Integer.class,DBINDEX);
			if(userid!=null){
				//去smart_vip表中查找该用户
				SmartVip vip = selectSmartVipByUserid(userid);
				if(vip!=null){
					try{
						//将该用户的会员期限减少一年(当前只是将overtime减少一年)
						reduceVipTime(vip);
						//根据deviceid去查询该条记录的idd
						Integer result =selectDeviceIddByDeviceid(deviceid);
						if(result!=null){
							//将smart_app_user_device表中的记录删除
							deleteDeviceRecode(result);
							//将deviceid和userid从缓存中删除
							RedisUtil.getInstance().deleteEntityStr(SMART_DEVICEID+deviceid,DBINDEX);
							//将该设备进行冻结,不让其他的用户进行绑定
							RedisUtil.getInstance().setEntityStr(FREEZE_DEVICEID+deviceid,10000,DBINDEX);
							util.LogUtil.writeLog("userid为:"+ userid +"的用户取消订单,将deviceid:为"+ deviceid +"idd为:"+result+"的设备恢复出厂状态,并成功减少该用户会员期限一年","addVipValidityTimeLog");
							util.LogUtil.writeLog("将deviceid:为"+deviceid+"的设备冻结,其他用户暂时不可以进行绑定","addVipValidityTimeLog");
						}
					}catch(Exception e){
						util.LogUtil.writeLog("userid为:"+ userid +"的用户取消订单,将deviceid:为"+ deviceid +"的设备恢复出厂状态时失败,减少该用户会员一年期限/删除设备失败,e:"+ e.getMessage(),"addVipValidityTimeLog");
						Logger.error("reduceVipValidityTime", e);
					}
				}else{
					util.LogUtil.writeLog("在Redis中没有找到"+ "\t" +"userid为:"+ userid +"和deviceid:为"+ deviceid +"的相关信息","addVipValidityTimeLog");
				}
			}else{
				util.LogUtil.writeLog("传入reduceVipValidityByCancelOrder方法的userid为:"+ userid + "不合法","addVipValidityTimeLog");
			}
		}
	}
	
	public static boolean checkFreezeDevice(String deviceid){
		return RedisUtil.getInstance().getEntityStr(FREEZE_DEVICEID+deviceid,Integer.class,DBINDEX)==null;
	}
	
	//退货的设备成功入库以后调用的方法,将冻结的设备解除冻结,使该设备可以被其他用户绑定
	public static void unfreezeDevice(String deviceid){
		Integer result = RedisUtil.getInstance().getEntityStr(FREEZE_DEVICEID+deviceid,Integer.class,DBINDEX);
		if(result != null&&result==10000){
			RedisUtil.getInstance().deleteEntityStr(FREEZE_DEVICEID+deviceid,DBINDEX);
			util.LogUtil.writeLog("将deviceid:为"+deviceid+"的设备解除冻结状态,其他用户可以进行绑定","addVipValidityTimeLog");
		}
	}
	
	/*
	 * 将某用户 开通为会员用户
	 * 传入参数channel:(1:通过绑定设备成为会员  2:通过充值成为会员)
	 * 传入参数deviceid:("":通过充值开通会员  不为""时则是通过绑定设备开通会员)
	 */
	public static boolean becomeVip(Integer userid,String deviceid,String channel){
		SmartVip vip = new SmartVip();
		Date now = new Date();
		vip.setUserid(userid);	
		vip.setAddtime(now);	//开通时间设置为now
		vip.setBuytime(now);
		vip.setOvertime(addOneYear(now,1));	//将开通时间延后一年
		vip.setFirstbuytime(now);
		vip.setDeviceid(deviceid);
		if(channel.equals("1")){	//会员获取渠道
			vip.setChannel("通过绑定设备成为会员");
		}else{
			vip.setChannel("通过充值成为会员");
		}
		try{
			Ebean.getServer(GlobalDBControl.getDB()).save(vip);
			return true;	//开通成功
		}catch(Exception e){
			Logger.error("createGetBountyRecord", e);
			return false;
		}
	}
	
	/*
	 * 为会员用户的时间增加一年
	 * 传入参数channel:(1:通过绑定设备成为会员  2:通过充值成为会员)
	 * 传入参数deviceid:("":通过充值开通会员  不为""时则是通过绑定设备开通会员)
	 */
	public static boolean addVipTime(Integer userid,String deviceid,String channel){
		Date now = new Date();
		//首先根据UserId去表中查到该条会员记录(用于获取overtime以便进行修改为其增加一年会员期限)
		SmartVip vip = selectSmartVipByUserid(userid);
		try{
			if(channel.equals("1")){
				Ebean.getServer(GlobalDBControl.getDB())
				.createSqlUpdate("UPDATE smart_vip SET buytime = :buytime , overtime = :overtime , lastbuytime = :lastbuytime , deviceid = :deviceid , channel = :channel WHERE userid = :userid")
				.setParameter("buytime", now)	//修改购买时间为当前时间
				.setParameter("overtime", addOneYear(vip.getOvertime(),1))	//到期时间增加一年
				.setParameter("lastbuytime", vip.getBuytime())	//VIP当前的buytime赋值给上次购买时间
				.setParameter("deviceid", deviceid)
				.setParameter("channel", "通过绑定设备成为会员")
				.setParameter("userid", userid)
				.execute();
			}else{
				Ebean.getServer(GlobalDBControl.getDB())
				.createSqlUpdate("UPDATE smart_vip SET buytime = :buytime , overtime = :overtime , lastbuytime = :lastbuytime , deviceid = :deviceid , channel = :channel WHERE userid = :userid")
				.setParameter("buytime", now)	//修改购买时间为当前时间
				.setParameter("overtime", addOneYear(vip.getOvertime(),1))	//到期时间增加一年
				.setParameter("lastbuytime", vip.getBuytime())	//VIP当前的buytime赋值给上次购买时间
				.setParameter("deviceid", deviceid)
				.setParameter("channel", "通过充值成为会员")
				.setParameter("userid", userid)
				.execute();
			}
			return true;	//开通成功
		}catch(Exception e){
			Logger.error("createGetBountyRecord", e);
			return false;
		}
	}
	
	/*
	 * 为会员过期 用户重新开通会员
	 * 传入参数channel:(1:通过绑定设备成为会员  2:通过充值成为会员)
	 * 传入参数deviceid:("":通过充值开通会员  不为""时则是通过绑定设备开通会员)
	 */
	public static boolean updateVipTime(Integer userid,String deviceid,String channel){
		Date now = new Date();
		//首先根据UserId去表中查到该条会员记录(用于获取overtime以便进行修改为其增加一年会员期限)
		SmartVip vip = selectSmartVipByUserid(userid);
		try{
			if(channel.equals("1")){
				Ebean.getServer(GlobalDBControl.getDB())
				.createSqlUpdate("UPDATE smart_vip SET buytime = :buytime , overtime = :overtime , lastbuytime = :lastbuytime , lastovertime=:lastovertime , deviceid = :deviceid , channel = :channel WHERE userid = :userid")
				.setParameter("buytime", now)	//修改购买时间为当前时间
				.setParameter("overtime", addOneYear(now,1))
				.setParameter("lastbuytime", vip.getBuytime())	//上次购买时间修改为VIP当前的buytime
				.setParameter("lastovertime", vip.getOvertime())	//将上次过期时间设置为VIP的过期时间
				.setParameter("deviceid", deviceid)
				.setParameter("channel", "通过绑定设备成为会员")
				.setParameter("userid", userid)
				.execute();
			}else{
				Ebean.getServer(GlobalDBControl.getDB())
				.createSqlUpdate("UPDATE smart_vip SET buytime = :buytime , overtime = :overtime , lastbuytime = :lastbuytime , lastovertime=:lastovertime , deviceid = :deviceid , channel = :channel WHERE userid = :userid")
				.setParameter("buytime", now)	//修改购买时间为当前时间
				.setParameter("overtime", addOneYear(now,1))
				.setParameter("lastbuytime", vip.getBuytime())	//上次购买时间修改为VIP当前的buytime
				.setParameter("lastovertime", vip.getOvertime())	//将上次过期时间设置为VIP的过期时间
				.setParameter("deviceid", deviceid)
				.setParameter("channel", "通过充值成为会员")
				.setParameter("userid", userid)
				.execute();
			}
			return true;
		}catch(Exception e){
			Logger.error("createGetBountyRecord", e);
			return false;
		}
	}
	
	//根据userid 去samrt_vip 将overtime减少一年时间
	public static void reduceVipTime(SmartVip vip){
		Ebean.getServer(GlobalDBControl.getDB())
		.createSqlUpdate("UPDATE smart_vip SET overtime = :overtime WHERE userid = :userid ")
		.setParameter("overtime", reduceOneYear(vip.getOvertime(),1))
		.setParameter("userid", vip.getUserid())
		.execute();
		util.LogUtil.writeLog("成功将userid:为"+vip.getUserid()+"的会员有效期减少一年","addVipValidityTimeLog");
	}
	
	//根据UserId pageno pagesize querydate来查询该用户下的所有奖励金记录
	public static List<SmartBounty> findBountyMessages(TreeMap<String,Object> params,Integer pageno,Integer pagesize,String querydate){
		List<String> ids = findBountyMessageIdsByCondition(params,pageno,pagesize,querydate);
		List<SmartBounty> list = new ArrayList<SmartBounty>();
		if(ids!=null){
			SmartBounty message = null;
			for(String id:ids){
				message = findBountyMessage(id);
				if(message!=null)
					list.add(message);
			}
		}
		return list;
	}
	
	public static Integer findBountyMessagesCountByCondition(TreeMap<String,Object> params,String querydate){
		StringBuffer paramsStr = combineParams(params,querydate);
		Integer count = RedisUtil.getInstance().getEntityStr(SMART_BOUNTY_MSGS_COUNT+paramsStr, Integer.class, DBINDEX);
		if(count==null)
			count = 0;
		return count;
	}
	
	private static SmartBounty findBountyMessage(String messageId){
		SmartBounty message = RedisUtil.getInstance().getEntityStr(SMART_BOUNTY_MSGS+messageId, SmartBounty.class, DBINDEX);
		if(message==null){
			message = Ebean.getServer(GlobalDBControl.getReadDB()).find(SmartBounty.class).where().eq("idd", messageId).findUnique(); 
			if(message!=null)
				RedisUtil.getInstance().setEntityStr(SMART_BOUNTY_MSGS+messageId, message,OVERTIME, DBINDEX);
		}		
		return message;
	}
	
	//根据条件 获取 该条件下的所有奖励金记录的id
	private static List<String> findBountyMessageIdsByCondition(TreeMap<String,Object> params,Integer pageno,Integer pagesize,String querydate){
		//获取用户的UserId
		Integer userid = (Integer)params.get("userid");
		if(userid==null)
			return null;
		StringBuffer paramsStr = combineParams(params,querydate);
		//获取该用户UserId下的记录总条数
		long amount = RedisUtil.getInstance().llen(SMART_BOUNTY_MSGS_IDS+paramsStr, DBINDEX);
		if(amount<=0 ){
			StringBuffer sb = new StringBuffer("select idd from smart_bounty where userid=:userid ");
			sb.append(" and addtime>:querydate1 ");
			sb.append(" and addtime<:querydate2 order by idd");
			List<SqlRow> srlist = Ebean.getServer(GlobalDBControl.getReadDB()).
						createSqlQuery(sb.toString()).setParameter("userid", userid)
						.setParameter("querydate1", querydate)
						.setParameter("querydate2", querydate+"-32")
						.findList();
			Integer c = 0;//记录总数
			for(SqlRow sr:srlist){
				RedisUtil.getInstance().lpush(SMART_BOUNTY_MSGS_IDS+paramsStr, sr.getLong("idd"), DBINDEX, OVERTIME);
				c++;
			}
			RedisUtil.getInstance().setEntityStr(SMART_BOUNTY_MSGS_COUNT+paramsStr, c, OVERTIME, DBINDEX);
		}
		return RedisUtil.getInstance().lrange(SMART_BOUNTY_MSGS_IDS+paramsStr, DBINDEX, pageno*pagesize*1l, ((pageno+1)*pagesize-1)*1l);
	}
	
	public static StringBuffer combineParams(TreeMap<String, Object> params,String querydate) {
		StringBuffer paramsStr = new StringBuffer();
		if(params!=null&&querydate!=null){
			int i = 0;
			int len = params.size();
			for (Map.Entry<String, Object> entry : params.entrySet()) {  
				paramsStr.append(entry.getKey()); 
				paramsStr.append(":");
				paramsStr.append(entry.getValue());
				if(i<len-1)
					paramsStr.append(":");
			}  
			paramsStr.append(querydate);
		}
		return paramsStr;
	}
	
	//根据UserId 去smart_vip表中查找 该用户会员的 对应数据
	public static SmartVip selectSmartVipByUserid(Integer userid){
		if(userid!=null){
			SmartVip vip = Ebean.getServer(GlobalDBControl.getDB())
					.find(SmartVip.class).where()
					.eq("userid", userid).findUnique();
			if(vip!=null){
				return vip;
			}
		}
		return null;
	}
	
	//根据UserId 去smart_bounty表中获取该用户奖励金账户余额(奖励金的currentbvalue)
	public static Integer findBountyByUserid(Integer userid){
		Integer amountBounty = 0;
		List<SmartBounty> bountyList =  ProductService.selectSmartBountyByUserid(userid);
		if(bountyList!=null&&bountyList.size()>0){
			//遍历集合将该UserId下的对应的奖励金额加起来
			for(SmartBounty bounty:bountyList){
				//判断该笔记录是支出还是收入
				if(bounty.getAct()==1){
					amountBounty += bounty.getCurrentbvalue();
				}
			}
			return amountBounty;
		}else{
			//没有找到该用户的奖励金记录,返回奖励金为0
			return 0;
		}
	}
	
	//根据UserId 去smart_bounty表获取该用户 即将过期的奖励金个数
	public static Integer findDisabledBountyByUserid(Integer userid){
		Integer disabledBounty = 0;
		List<SmartBounty> bountyList =  ProductService.selectSmartBountyByUserid_Act(userid);
		//该bountyList不为null
		if(bountyList!=null){
			//遍历集合将该UserId下的对应的即将 失效的奖励金统计出来
			for(SmartBounty bounty:bountyList){
				//首先判断时间是否只剩七天
				if(bounty.getOvertime()!=null){
					if(checkDisableBounty(bounty.getOvertime())){
						//判断该笔记录的奖励金是用户支出还是收入
						if(bounty.getAct()==1){
							disabledBounty += bounty.getCurrentbvalue();
						}
					}
				}
			}
		}else{
			//没有该用户的奖励金记录,即将过期的数量为0
			return 0;
		}
		return disabledBounty;
	}
	
	//根据UserId 去smart_bounty表获取该用户 某月份获得奖励金的个数
	public static Integer findMonthBountyByUserid(Integer userid,Date querydate){
		Integer monthBounty = 0;
		List<SmartBounty> bountyList =  ProductService.selectSmartBountyByUserid_Act(userid);
		if(bountyList!=null){
			for(SmartBounty bounty:bountyList){
				if(bounty.getAddtime()!=null){
					if(checkMonthBounty(bounty.getAddtime(),querydate)){
						monthBounty += bounty.getBvalue();
					}
				}
			}
		}else{
			return 0;
		}
		return monthBounty;
	}
	
	//根据UserId去smart_bounty表中查找 当天该用户是否领取过奖励金
	public static boolean checkGetBounty(Integer userid){
		if(userid != null){
			//根据UserId查询该用户的所有的奖励金记录
			List<SmartBounty> bountyList = ProductService.selectSmartBountyByUserid(userid);
			if(bountyList!=null){
				for(SmartBounty bounty:bountyList){
					//判断 今天是否领取过奖励金
					if(bounty.getAddtime()!=null){
						if(checkTodayBounty(bounty.getAddtime())&&bounty.getTitile().equals("每日登陆领取")){
							return true;	//当日已领取
						}
					}
				}
			}
		}else{
			return true;	//userId为null 提示为 当日已领取
		}
		return false;	//当日未领取
	}
	
	//根据UserId 去smart_bounty表中生成 领取奖励金的记录
	public static Integer createGetBountyRecord(Integer userid){
		SmartBounty bounty = new SmartBounty();
		Date now = new Date();
		if(userid != null){
			bounty.setUserid(userid);
			bounty.setAddtime(now);
			bounty.setOvertime(addOneYear(now, 1));
			bounty.setBvalue(10);
			bounty.setTitile("每日登陆领取");
			bounty.setAct(1);
			bounty.setCurrentbvalue(10);
			try{
				Ebean.getServer(GlobalDBControl.getDB()).save(bounty);
				return 1;	//生成记录成功
			}catch(Exception e){
				Logger.error("createGetBountyRecord", e);
			}
		}
		return 0;	//添加失败
	}
	
	//获取会员首页下方的 图片
	public static List<VipDto.Img> getVipMainImg(Integer amount){
		if(amount>0){
			List<VipDto.Img> imgs = new ArrayList<VipDto.Img>();
			for(int i =1;i<=amount;i++){
				VipDto.Img img = new VipDto.Img();
				img.setImgurl(SysConfigAction.findSysconfig("智能锁", "会员首页宣传图"+i));
				img.setLinkurl(SysConfigAction.findSysconfig("智能锁", "会员首页宣传图"+i+"地址"));
				imgs.add(img);
			}
			return imgs;
		}
		return null;
	}
	
	//判断该用户是否为会员用户/会员是否过期(1:是会员并且没过期 2:不是会员 3:会员过期)
	public static Integer checkVip(Integer userid){
		if(userid != null){
			SmartVip vip = selectSmartVipByUserid(userid);
			//判断是否有该用户(即该用户是否是会员)
			if(vip!=null){
				//判断该用户的会员是否过期
				if(vip.getOvertime()!=null){
					if(!checkVipTime(vip.getOvertime())){
						return 1;	//没过期
					}else{
						return 3;
					}
				}
			}
		}
		return 2;
	}
	
	//根据deviceid 去smart_app_user_device表中将idd找出来
	private static Integer selectDeviceIddByDeviceid(String deviceid){
		SmartDevice smartDevice = Ebean.getServer(GlobalDBControl.getDB())
			.find(SmartDevice.class)
			.where().eq("deviceid", deviceid)
			.findUnique();
		if(smartDevice !=null){
			return smartDevice.getIdd();
		}
		return null;
	}
	
	//根据deviceid 去smart_app_user_device表中将该条记录删除
	private static void deleteDeviceRecode(Integer deviceidd){
		String sql = "DELETE FROM SmartAppUserDevice WHERE deviceid =:deviceid";
		Ebean.getServer(GlobalDBControl.getDB())
				.createUpdate(SmartAppUserDevice.class, sql)
				.setParameter("deviceid", deviceidd)
				.execute();
		util.LogUtil.writeLog("去smart_app_user_device表中将该设备的记录删除成功！","addVipValidityTimeLog");
	}
	
	//传入一个时间 将该时间增加一年 返回一年以后的date
	public static Date addOneYear(Date addtime,int addyear){
		Calendar cal = Calendar.getInstance();
		cal.setTime(addtime);
		cal.add(Calendar.YEAR, addyear);
		return cal.getTime();
	}
	
	//传入一个时间 将该时间减少一年 返回一年之前的date
	private static Date reduceOneYear(Date overtime,int reduceyear){
		Calendar cal = Calendar.getInstance();
		cal.setTime(overtime);
		cal.add(Calendar.YEAR, -reduceyear);
		return cal.getTime();
	}
	
	//用于判断 传入的到期时间的毫秒值 是否大于 今天当天的毫秒值
	public static boolean checkVipTime(Date overtime){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(overtime);
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date today = new Date();
		if(today.getTime()>calendar.getTime().getTime()){
			return true;	//过期
		}
		return false;	//未过期
	}
	
	//用于判断 传入的时间是否为 今天
	private static boolean checkTodayBounty(Date addtime){
		Date today = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		//判断该日期是否为今天当天
		if(sdf.format(today).equals(sdf.format(addtime))){
			return true;	//当日已领取
		}
		return false;	//当日未领取
	}
	
	//用于计算某月奖励金的方法
	//(获取某月并计算出该月份月初到月末的毫秒数,然后传入添加时间看是否在这个范围内来判断是否为本月获取的奖励金)
	private static boolean checkMonthBounty(Date addtime,Date querydate){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		String strquery = sdf.format(querydate);
		Integer query_year = Integer.parseInt(strquery.split("-")[0]);
		Integer query_month = Integer.parseInt(strquery.split("-")[1]);
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		//获取当前月份的第一天
		calendar.set(query_year,query_month-1,1,0,0,0);
		calendar.set(Calendar.MILLISECOND, 0);
		//获取 第一天的毫秒数
		long firstTime = calendar.getTime().getTime();
		
		//获取当月的最后一天
		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DAY_OF_MONTH,0);
		//获取 最后一天的毫秒数
		long lastTime = calendar.getTime().getTime()+24L*60*60*1000;
		
		//判断 传入放入日期是否在 当月的毫秒范围内
		long time = addtime.getTime();
		if(time>=firstTime&&time<=lastTime){
			return true;	//本月领取
		}else{
			return false;	//非本月领取
		}
	}
	
	//用于计算失效奖励金的方法
	private static boolean checkDisableBounty(Date overtime){
		if(overtime==null){
			return false;
		}
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String overtimeStr = sdf.format(overtime);
		String[] list = overtimeStr.split("-");
		Calendar calendar = Calendar.getInstance();
		calendar.set(Integer.parseInt(list[0]), Integer.parseInt(list[1])-1, Integer.parseInt(list[2]), 0, 0, 0);
    	calendar.set(Calendar.MILLISECOND, 0);
		Date overdate = calendar.getTime();
		calendar.set(Integer.parseInt(list[0]), Integer.parseInt(list[1])-1, Integer.parseInt(list[2])-7, 0, 0, 0);
    	calendar.set(Calendar.MILLISECOND, 0);
		Date pastdate = calendar.getTime();
		if(now.getTime()>pastdate.getTime()&&now.getTime()<=overdate.getTime()){
			return true;
		}else{
			return false;
		}
	}
	
}