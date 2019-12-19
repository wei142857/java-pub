package util;

import java.util.Date;

import controllers.SysConfigAction;
import util.classEntity.FileOP;
import util.classEntity.StringTool;
import worker.LogQueueWorker;

public class LogUtil {

	//写日志：logname - 日志文件名称
	public static void writeLog(String con,String logname)
	{
		
		FileOP.writeFileAtTail(util.StringUtil.getDateTimeString(new Date(), 
					"[yyyy-MM-dd HH:mm:ss]\t")
					+ con +"\r\n"
					, 
					//"/data/logs/"+logname+".txt" );
					SysConfigAction.findSysconfig("日志", "文件目录","d:/")+logname+".txt" );
		//LogQueueWorker.writeLog(con, logname);
	}
	
}
