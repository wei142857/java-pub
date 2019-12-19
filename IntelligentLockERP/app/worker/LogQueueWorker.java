package worker;

import java.util.Date;

import play.Logger;
import util.IniLoader;
import util.classEntity.FileOP;
import util.jedis.RedisUtil;

/*
 * 短信的线程
 */

public class LogQueueWorker extends BaseWorker {
	public LogQueueWorker() {
		util.LogUtil.writeLog("LogQueueWorker start....", "queueLog");
		this.start();
	}
	
	//特殊设定的队列专属 jedis
	public static String QUEUE_Pool = "logQueue"; 
		
	public static void writeLog(String content, String Log)
	{
		LogOne log =new LogOne(content,Log);
		if(IniLoader.getIntegerByKey("JedisSet", "SetQueue", 0, "sys")==1)
			RedisUtil.getInstance().setRedisMQ( queueLOG,log,QUEUE_Pool );
		else
			RedisUtil.getInstance().setRedisMQ( queueLOG,log );
	}
	
	public final static String queueLOG   = "HX_LOG";
	
	public void run() {
		while (running) {
			lastWork = System.currentTimeMillis();
			
			try {
				LogOne one = null;
				//List<LogOne> ones = RedisUtil.getInstance().getRedisListMQ( queueLOG,  LogOne.class);
				if( IniLoader.getIntegerByKey("JedisSet", "SetQueue", 0, "sys") == 1 )
					one = RedisUtil.getInstance().getRedisOneMQ( queueLOG,  LogOne.class,QUEUE_Pool );
				else
					one = RedisUtil.getInstance().getRedisOneMQ( queueLOG,  LogOne.class );
				
				//Logger.info( "queue sms get:"+(System.currentTimeMillis()-lt) );
				if(one != null)
					//for( LogOne one : ones )
					writeLog(one);
				
				Thread.sleep(IniLoader.getIntegerByKey(
						"sms", "QLogInterval", 3 , "sys") );
			} catch (Exception e) {
				Logger.info("SmsWorker running:" + e.toString());
			}
		}
	}

	private void writeLog(LogOne one) {
		FileOP.writeFileAtTailBuffer(util.StringUtil.getDateTimeString(new Date(), 
				"[yyyy-MM-dd HH:mm:ss]\t")
				+ one.content +"\r\n"
				, "/data/logs/"+one.log+".txt" );
		
	}
}
