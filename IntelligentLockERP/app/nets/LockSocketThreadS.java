package nets;

import java.io.IOException;

import net.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.List;

import util.GlobalDBControl;
import util.Rc4Util;
import util.StringUtil;
import util.classEntity.StringTool;
import ServiceDao.JdbcOper;

import com.avaje.ebean.Ebean;

import controllers.SysConfigAction;
import models.SmartDevice;
import models.SmartNetMsg;
public class LockSocketThreadS extends Thread{
	private Socket socket;
	private InputStream in;
	private OutputStream out;
	
	static String logfile = "LockSocketThreadS";
	
	public LockSocketThreadS (Socket s)
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
					
					util.LogUtil.writeLog("服务器端收到消息:"+ msg.toString(msg) , logfile);
					
					msg.rc4 = true;
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
						util.LogUtil.writeLog("\r\n!! find and send:"+msg.getIdd()+"\t"+msg.getLockid() , logfile);

						sendMsg( msg );
						lasttime = System.currentTimeMillis();
						Thread.currentThread().sleep(5); //休眠
						
						msg = findMsg( );
					};
				}
				
				if(  System.currentTimeMillis()-lasttime > 1000l*60*15)//如果15分钟没有处理，断开
				{
					util.LogUtil.writeLog("timeout :"+deviceId, logfile);
					break;
				}
				
				Thread.currentThread().sleep(50); //休眠
			}
		}
		catch(Exception e)
		{
			//e.printStackTrace();
			util.LogUtil.writeLog("Socket diconnect 。 ", logfile);
		}
		finally{
			try{
				if(out!=null)out.close();
				if(in!=null)in.close();
				socket.close();
			}catch(Exception ee){
				//util.LogUtil.writeLog("stop exception:"+ee.toString(), logfile);
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
		
		util.LogUtil.writeLog( "!! set deviceid:" + msg.deviceId , logfile);
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
		if( msg == null )
			return;
		
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
				//远程开锁
				//ret = SmartService.remoteOpen( msg, smartdevice);
				return;
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
		if( (msg.getTag()== 0x20      //服务器下发命令
				&& msg.getLen() > 16 && //至少有签名的长度
				( SmartMsgUtil.CMD_SVR_DELPASS== msg.getType()||
				  SmartMsgUtil.CMD_SVR_ADDUSER== msg.getType()||
				  SmartMsgUtil.CMD_SVR_ADD_PASS== msg.getType()||
				  SmartMsgUtil.CMD_SVR_ADD_TEMPPASS== msg.getType()
				) ) ||
			( msg.getTag()== 0x11 && msg.getLen() >= (3 + 8 + 16)) )//回复带MD5
		{
			//服务器下发消息，签名
			msg.setMsg( reMd5(msg.getMsg(),msg) );
		}
		byte[] data = StringTool.getFromHexString( msg.getMsg() );
		util.LogUtil.writeLog("服务器端下发消息:"+ msg.toString(msg) , logfile);
		
		byte[] dataN = encry_RC4( data  );
		
		util.LogUtil.writeLog("server send back:" + StringTool.makeHexString(dataN)+"\r\n" , logfile);
		out.write( dataN );
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

	//重新计算MD5 签名
	private String reMd5( String info , SmartNetMsg msg ) {
		SmartDevice dev = SmartService.findDevice( msg.getLockid() );
		String infoN = "A7" + info.substring( 2 , info.length() - 32 ) ;
		String sign = util.MD5.mkMd5(infoN + dev.getSecret());
		infoN = infoN + sign ; // 签名
		
		util.LogUtil.writeLog("Re-MD5:"
				+ ( "A7" + info.substring( 2 , info.length() - 32 ) ) + dev.getSecret() 
				+ "\t"   + infoN
				, logfile);
		
		msg.descMsg += " 签名：" + sign;
		
		return infoN;
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
		util.LogUtil.writeLog("读到数据长度 -- " + read , logfile);
		
		if( read < 0 )
			throw new IOException();
		
		if( read > 1 ){
			if( data[0] != (byte) 0xA7 ){
				//判断消息头
				//util.LogUtil.writeLog("数据错误: byte 1 got " + data[0] , logfile);
				return null;
			}
			
			util.LogUtil.writeLog("数据:  " + StringTool.makeHexString(data, 2) , logfile);
			
			//根据长度读取剩下的内容
			leng = data[1];
			util.LogUtil.writeLog("后续数据长度  -- " + leng , logfile);
			
			read = in.read( data , 0 , leng  );
			int  off = read;
			while( leng > off ){
				read = in.read( data , off , leng - off );
				off += read ;
			}
			
			byte[] dataN = decry_RC4( data,leng );
			//util.LogUtil.writeLog("Read data: " + StringTool.makeHexString(data) , logfile);
			return SmartMsgUtil.makeFromBytes( dataN , leng );
		}else{
			return null;
		}
	}
	
	//RC4解密
	public static byte[] decry_RC4(byte[] data,int length)
	{
		String key = SysConfigAction.findSysconfig("Rc4", "秘钥");
		byte[] src = new byte[length];
		System.arraycopy(data,0,src,0,length);
		
		byte[] ret = Rc4Util.RC4Base( src , key );
		util.LogUtil.writeLog("数据解密: " + StringTool.makeHexString(src) + "\t=>" + StringTool.makeHexString(ret)
				+"\t"+length , logfile);
		return ret;
	}
	
	private byte[] encry_RC4( byte[] data ) {
		String key = SysConfigAction.findSysconfig("Rc4", "秘钥");
		byte[] src = new byte[data.length-2];
		System.arraycopy( data,2,src,0,data.length-2 );
		
		byte[] des = Rc4Util.RC4Base( src , key );
		util.LogUtil.writeLog("数据加密: " + StringTool.makeHexString(src) + "\t=>" + StringTool.makeHexString(des)
				+"\t"+src.length , logfile);
		
		byte[] ret = new byte[des.length+2];
		ret[0]= (byte)0xA7;
		ret[1]= (byte)des.length;
		System.arraycopy( des,0,ret,2,des.length );
		return ret;
	}
	
}
