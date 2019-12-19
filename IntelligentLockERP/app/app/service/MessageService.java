package app.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import models.SmartAppMsg;
import models.SmartAppUser;
import util.AliSms;
import util.GlobalDBControl;
import util.LogUtil;
import util.clientPush.SmsPush;
import util.jedis.RedisUtil;
import ServiceDao.WxGzhService;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlRow;
import com.cloopen.rest.demo.SDKTestLandingCall;

public class MessageService {
	
	static String SMART_APP_USER_MSGS_IDS = "smart:app:user:msgs:ids:";
	static String SMART_APP_USER_MSGS_COUNT = "smart:app:user:msgs:count:";
	static String SMART_APP_USER_MSGS = "smart:app:user:msgs:";
	static int DBINDEX = 10;
	
	static int OVERTIME = 60;
	
	public static int TYPE_ILLEGAL_POWER = 1;//电源警告
	public static int TYPE_ILLEGAL_ALARM = 2;//非法开锁警告
	
	public static List<SmartAppMsg> findMessages(TreeMap<String,Object> params,int pageno,int pagecount){
		List<String> ids = findMessageIdsByCondition(params,pageno,pagecount);
		List<SmartAppMsg> list = new ArrayList<SmartAppMsg>();
		if(ids!=null){
			SmartAppMsg message = null;
			for(String id:ids){
				message = findMessage(id);
				if(message!=null)
					list.add(message);
			}
		}
		return list;
	}
	
	public static SmartAppMsg findMessage(String messageId){
		SmartAppMsg message = RedisUtil.getInstance().getEntityStr(SMART_APP_USER_MSGS+messageId, SmartAppMsg.class, DBINDEX);
		if(message==null){
			message = Ebean.getServer(GlobalDBControl.getReadDB()).find(SmartAppMsg.class).where().eq("idd", messageId).findUnique(); 
			if(message!=null)
				RedisUtil.getInstance().setEntityStr(SMART_APP_USER_MSGS+messageId, message,OVERTIME, DBINDEX);
		}		
		return message;
	}
	
	static List<String> findMessageIdsByCondition(TreeMap<String,Object> params,int pageno,int pagecount){
		Integer userid = (Integer)params.get("userid");
		if(userid==null)
			return null;
		StringBuffer paramsStr = combineParams(params);
		long amount = RedisUtil.getInstance().llen(SMART_APP_USER_MSGS_IDS+paramsStr, DBINDEX);
		if(amount<=0 ){
			StringBuffer sb = new StringBuffer("select idd from smart_app_msg where userid=:userid order by idd");
			List<SqlRow> srlist = Ebean.getServer(GlobalDBControl.getReadDB()).createSqlQuery(sb.toString()).setParameter("userid", userid).findList();
			Integer c = 0;//记录总数
			for(SqlRow sr:srlist){
				RedisUtil.getInstance().lpush(SMART_APP_USER_MSGS_IDS+paramsStr, sr.getLong("idd"), DBINDEX, OVERTIME);
				c++;
			}
			RedisUtil.getInstance().setEntityStr(SMART_APP_USER_MSGS_COUNT+paramsStr, c, OVERTIME, DBINDEX);
		}
		return RedisUtil.getInstance().lrange(SMART_APP_USER_MSGS_IDS+paramsStr, DBINDEX, pageno*pagecount*1l, ((pageno+1)*pagecount-1)*1l);
	} 
	
	public static Integer findMessagesCountByCondition(TreeMap<String,Object> params){
		StringBuffer paramsStr = combineParams(params);
		Integer count = RedisUtil.getInstance().getEntityStr(SMART_APP_USER_MSGS_COUNT+paramsStr, Integer.class, DBINDEX);
		if(count==null)
			count = 0;
		return count;
	}
	
	public static StringBuffer combineParams(TreeMap<String, Object> params) {
		StringBuffer paramsStr = new StringBuffer();
		if(params!=null){
			int i = 0;
			int len = params.size();
			for (Map.Entry<String, Object> entry : params.entrySet()) {  
				paramsStr.append(entry.getKey()); 
				paramsStr.append(":");
				paramsStr.append(entry.getValue());
				if(i<len-1)
					paramsStr.append(":");
				
		  }  
		}
		return paramsStr;
	}
	
	public static void pushNormalMsg(String phone,String title,String content){
		SmartAppUser smartAppUser = UserService.findUserByPhone(phone);
		if(smartAppUser!=null){
			SmartAppMsg smartAppMsg = new SmartAppMsg();
			smartAppMsg.setAddtime(new Date());
			smartAppMsg.setMsg(content);
			smartAppMsg.setTitle(title);
			smartAppMsg.setUserid(smartAppUser.getIdd());
			Ebean.getServer(GlobalDBControl.getDB()).save(smartAppMsg);
			SmsPush.push(smartAppUser, title, content, smartAppMsg.getIdd().intValue(),"");
		}
	}
	
	
	public static void pushIllegalMsg(String phone,String deviceDesc,String deviceId,Integer power,int type){
		SmartAppUser smartAppUser = UserService.findUserByPhone(phone);
		if(smartAppUser!=null){
			String title = "低电预警通知";
			if(type==TYPE_ILLEGAL_ALARM){
				title = "非法开锁提醒";
			}
			String content = "您的门锁电量已低于{{power}}%，请及时更换电池！";
			if(type==TYPE_ILLEGAL_ALARM){
				content = "有人正在非法打开您的"+deviceDesc+"门锁，请及时处理.";
			}
			String powerKey = "";
			if(type==TYPE_ILLEGAL_POWER){
				if(power>10){
					powerKey = "20";
				}else if(power>5){
					powerKey = "10";
				}else if(power>0){
					powerKey = "5";
				}
				String key = RedisUtil.getInstance().getEntityStr("power:deviceid:"+powerKey+":"+deviceId, String.class,6);
				if(key!=null){
					LogUtil.writeLog(phone+"\t"+deviceId+"\t"+power+"\t已提醒", "illegalmsg");
					return;
				}
				content = content.replace("{{power}}", powerKey);
			}
			String illegalpushstate = smartAppUser.getIllegalpushstate();
			char[] illegalpushstates = illegalpushstate.toCharArray();
			if(illegalpushstates[0]=='1'){//APP消息通知
				SmartAppMsg smartAppMsg = new SmartAppMsg();
				smartAppMsg.setAddtime(new Date());
				smartAppMsg.setMsg(content);
				smartAppMsg.setTitle(title);
				smartAppMsg.setUserid(smartAppUser.getIdd());
				Ebean.getServer(GlobalDBControl.getDB()).save(smartAppMsg);
				SmsPush.push(smartAppUser, title, content, smartAppMsg.getIdd().intValue(), "");
				util.LogUtil.writeLog(deviceId+"\t"+type+"\tapp\t"+content, "recordLog");
			}
			if(illegalpushstates[1]=='1'){//短信通知
				if(type==TYPE_ILLEGAL_POWER){
					AliSms.sendPowerInfo(phone, powerKey);
				}else{
					AliSms.sendAlarmInfo(phone,deviceDesc);
				}
				util.LogUtil.writeLog(deviceId+"\t"+type+"\tsms\t"+content, "recordLog");
			}
			if(illegalpushstates[2]=='1' && type==TYPE_ILLEGAL_ALARM){//电话通知
				SDKTestLandingCall.sendAlarm(phone);
				util.LogUtil.writeLog(deviceId+"\t"+type+"\tcall\t"+content, "recordLog");
			}
			if(illegalpushstates[3]=='1'){//微信通知
				WxGzhService.sendIllegalMessage(phone, title, deviceDesc, content, "");
				util.LogUtil.writeLog(deviceId+"\t"+type+"\tgzh\t"+content, "recordLog");
			}
		}
	}
	
}
