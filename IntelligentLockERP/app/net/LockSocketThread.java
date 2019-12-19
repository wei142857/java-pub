package net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.List;

import util.GlobalDBControl;
import util.StringUtil;
import util.classEntity.StringTool;
import ServiceDao.JdbcOper;

import com.avaje.ebean.Ebean;

import models.SmartDevice;
import models.SmartNetMsg;
public class LockSocketThread extends Thread{
	private Socket socket;
	private InputStream in;
	private OutputStream out;
	
	public LockSocketThread (Socket s)
	{
		socket = s ;
		this.start();
	}
	
	//上次网络交互时间
	long lasttime = System.currentTimeMillis();
	String deviceId;
	SmartDevice smartdevice ;
	
	public void run()
	{
		try{
			//设置超时30秒
			socket.setSoTimeout(30*1000);
			
			// 构造该会话中的输入输出流
			in = socket.getInputStream();
			out = socket.getOutputStream();
			
			// work ...
			while( true ){
				SmartNetMsg msg= readMsg();
				if( msg != null ){
					
					//处理输入的消息
					dealMsgInput(msg);
					lasttime = System.currentTimeMillis();
					
				}
				
				//查找锁的其他消息，回送
				if( !StringTool.isNull( deviceId ) ){
					
					delFailedPassword( deviceId );
					
					dealReSend( deviceId );
					msg = findMsg( );
					while( msg != null )
					{
						util.LogUtil.writeLog("\r\n!! find and send:"+msg.getIdd()+"\t"+msg.getLockid() , "LockSocketThread");

						sendMsg( msg );
						lasttime = System.currentTimeMillis();
						Thread.currentThread().sleep(5); //休眠
						
						msg = findMsg( );
					};
				}
				
				if(  System.currentTimeMillis()-lasttime > 1000l*60*15)//如果15分钟没有处理，断开
				{
					util.LogUtil.writeLog("timeout :"+deviceId, "LockSocketThread");
					break;
				}
				
				Thread.currentThread().sleep(50); //休眠
			}
		}
		catch(Exception e)
		{
			//e.printStackTrace();
			util.LogUtil.writeLog("Socket diconnect : "+e.toString(), "LockSocketThread");
		}
		finally{
			try{
				if(out!=null)out.close();
				if(in!=null)in.close();
				socket.close();
			}catch(Exception ee){
				util.LogUtil.writeLog("stop exception:"+ee.toString(), "LockSocketThread");
			}
		}
	}

	//删除一天前的 失败密码
	private void delFailedPassword(String deviceId2) {
		String date = StringUtil.getDateStringDaysAgo(1);
		String sql  = "DELETE FROM smart_device_passport WHERE deviceid='" +deviceId2
				+ "' AND idx IS NULL AND ADDTIME<'" +date
				+ "' AND PASSWORD IN (SELECT ext1 FROM smart_msg WHERE lockid = '"
				+ deviceId2
				+ "' AND tag=32 AND ( replytime IS NOT NULL AND replytime <'" +date
				+ "' ) AND TYPE IN (2,3) )";
		int ret = JdbcOper.extSql(sql);
		if(ret>0)
			util.LogUtil.writeLog( "sql:" + sql + "\t" + ret , "delFail");
	}

	//根据消息中的设备id，找到设备；如果没有就新建一条记录
	void setDevice(SmartNetMsg msg)
	{
		//设置设备在线状态
		SmartService.setDeviceOnline ( msg.deviceId );
		
		if( smartdevice != null || StringTool.isNull( msg.deviceId ) )
			return;
		
		util.LogUtil.writeLog( "!! set deviceid:" + msg.deviceId , "LockSocketThread");
		deviceId = msg.deviceId;
		smartdevice = Ebean.getServer(GlobalDBControl.getDB())
				.find(SmartDevice .class).where().eq("deviceid", deviceId).findUnique();
		if( smartdevice == null ){
			//新建一个 device ，
			smartdevice = new SmartDevice();
			smartdevice.setAddtime(new Date());
			smartdevice.setDeviceid(deviceId);
			smartdevice.setLastconnect(new Date());
			smartdevice.setSecret(StringUtil.mkNumRanmString(10));
			//smartdevice.setVersion(msg.verison);
			//smartdevice.setDevicedesc("临时设备："+msg.getMsg());
			smartdevice.setDevicedesc("SKN智能锁");
			
			smartdevice.setType(2);
			Ebean.getServer(GlobalDBControl.getDB()).save(smartdevice);
		}else{
			smartdevice.setLastconnect(new Date());
			//smartdevice.setVersion(msg.verison);
			Ebean.getServer(GlobalDBControl.getDB()).save(smartdevice);
		}
	}
	
	
	//对输入的消息进行处理，比如锁登录，上报信息等等
	private void dealMsgInput(SmartNetMsg msg) throws IOException {
		String key = "shut"+msg.deviceId;
		if( msg == null )
			return;
		
		/*if( util.jedis.RedisUtil.getInstance().getEntity(key, String.class)!=null){
			util.LogUtil.writeLog( key + " !! \t" + msg.getType() + "\t"  + Thread.currentThread().getId() , "remote");
			return;
		}*/
		
		//消息入库
		if( msg.getType() >= 0 )
			Ebean.getServer(GlobalDBControl.getDB()).save( msg );
		
		//设置当前设备
		setDevice( msg );
				
		//record log
		SmartService.recordLog( msg );
		
		SmartNetMsg ret = null;
		if( msg.getTag()==0x21 ){
			SmartService.dealReply( msg );
		}
		else{
			if(  SmartMsgUtil.CMD_LOCK_CONNECT == msg.getType() )
				ret = SmartService.Connect( msg,smartdevice );
			else if(  SmartMsgUtil.CMD_LOCK_ADDUSER == msg.getType() )
				//锁的密码增
				ret = SmartService.userAdd( msg );
			else if( SmartMsgUtil.CMD_LOCK_DELUSER == msg.getType() )
				//锁的密码删
				ret = SmartService.userDel( msg );
			else if(  SmartMsgUtil.CMD_LOCK_ALARM == msg.getType())
				//锁报警
				ret = SmartService.alarm( msg );
			else if(  SmartMsgUtil.CMD_LOCK_TIP == msg.getType())
				//锁的提醒
				ret = SmartService.beep( msg );
			else if(  SmartMsgUtil.CMD_LOCK_RESET == msg.getType())
				//锁重置
				ret = SmartService.resetDevice( msg );
			else if(  SmartMsgUtil.CMD_LOCK_TIME == msg.getType())
				//锁重置
				ret = SmartService.time( msg );
			else if(  SmartMsgUtil.CMD_LOCK_POWER == msg.getType())
				//锁电量
				ret = SmartService.devicePower( msg );
			else if(  SmartMsgUtil.CMD_LOCK_REMOTE_OPEN == msg.getType()){
				return;
				//远程开锁
				//ret = SmartService.remoteOpen( msg, smartdevice);
			}
			else {
				play.Logger.info("!!! type:"+msg.getType());
				//回复ok
				ret = SmartMsgUtil.makeCommonReply(msg, true);
			}
			
			if(ret == null && SmartMsgUtil.CMD_LOCK_REMOTE_OPEN != msg.getType())
				ret = SmartMsgUtil.makeCommonReply(msg, true);
			
			if(ret!=null)//保存记录
			{
				ret.setLockid( msg.deviceId );
				Ebean.getServer(GlobalDBControl.getDB()).save( ret );
			}
		}
		
		if( ret != null )
			sendMsg( ret );
	}
	
	//发送 给锁缓存的消息
	private void sendMsg(SmartNetMsg msg) throws IOException {
		play.Logger.info("send back: " + msg.getMsg());
		byte[] data = StringTool.getFromHexString( msg.getMsg() );
		out.write( data );
		out.flush();
		if( msg.getTag()==0x20 )//记录下发的消息
			SmartService.recordLog( msg );
		
		try {
			Thread.currentThread().sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//查找 锁缓存的下发消息
	private SmartNetMsg findMsg() {
		if( deviceId == null )
			return null;
		String sql = "find SmartNetMsg where lockid='"+deviceId+"' and dealed=0 and tag=32 and "
				+ "(begintime is null or begintime < now())";
		/*List<SmartNetMsg> ls = Ebean.getServer(GlobalDBControl.getDB()).find(SmartNetMsg.class).where()
				.eq( "lockid", deviceId).eq("dealed", 0)
				.eq( "tag" , 0x20 ) //下发
				.orderBy("idd desc").setMaxRows(1).findList();*/
		List<SmartNetMsg> ls = Ebean.getServer(GlobalDBControl.getDB())
				.createQuery(SmartNetMsg .class, sql)
				.orderBy("idd desc")
				.findList();
		if( ls.size() >0 ){
			//取出后标记 并下发
			SmartNetMsg ret= ls.get(0);
			ret.setDealed(1);
			ret.setDealtime(new Date());
			Ebean.getServer(GlobalDBControl.getDB()).save( ret );
			
			return ret;
		}
		return null;
	}
	
	//重发
	public static void dealReSend(String deviceid)
	{
		String date1 = StringUtil.getDateStringMinutesAgo(300); 
		String date2 = StringUtil.getDateStringMinutesAgo(5); 
		String sql = "update smart_msg set dealed=0,rtime=1 WHERE tag=32 AND dealed=1 AND lockid='" + deviceid
				+ "' AND dealtime<'" + date2
				+ "' AND dealtime>'" + date1
				+ "' AND reply=0 and rtime=0";
		
		int  ret = JdbcOper.extSql(sql);
		util.LogUtil.writeLog("dealReSend:"+ret+"\t"+deviceid+"\t"+sql, "resend");
	}

	//读取锁发过来的消息
	private SmartNetMsg readMsg() throws IOException {
		byte[] data = new byte[256];
		int  read = 0;
		int  leng = 0;
		//读取2个字节
		read = in.read( data , 0 , 2 );
		util.LogUtil.writeLog("读到数据长度 -- " + read , "LockSocketThread");
		
		if( read < 0 )
			throw new IOException();
		
		if( read > 1 ){
			if( data[0] != (byte)0xA9 ){
				//判断消息头
				util.LogUtil.writeLog("数据错误: byte 1 got " + data[0] , "LockSocketThread");
				return null;
			}
			
			//根据长度读取剩下的内容
			leng = data[1];
			util.LogUtil.writeLog("数据长度  -- " + leng , "LockSocketThread");
			
			read = in.read( data , 0 , leng  );
			util.LogUtil.writeLog("读取 -- " + read , "LockSocketThread");
			int  off = read;
			while( leng > off ){
				read = in.read( data , off , leng - off );
				off += read ;
			}
			
			//util.LogUtil.writeLog("Read data: " + StringTool.makeHexString(data) , "LockSocketThread");
			
			return SmartMsgUtil.makeFromBytes( data , leng );
		}else{
			return null;
		}
	}
}
