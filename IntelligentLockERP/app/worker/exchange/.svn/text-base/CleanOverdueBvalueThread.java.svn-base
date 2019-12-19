package worker.exchange;

import java.util.Date;
import java.util.List;

import models.SmartBounty;
import play.Logger;
import util.GlobalDBControl;
import util.StringUtil;
import com.avaje.ebean.Ebean;

/**
 * 清理过期的奖励金的线程
 * @author 陈宏亮
 *
 */
public class CleanOverdueBvalueThread extends Thread{
	
	//public static String CLEAN_OVERDUE_BVALUE = "CLEAN:OVERDUE:BVALUE";
	
	public CleanOverdueBvalueThread(){
		Logger.info("CleanOverdueBvalueThread start ..");
		this.start();
	}
	
	public void run(){
		while(true){
			Ebean.beginTransaction();
			try{
				Date now = new Date();
				List<SmartBounty> bountyList = selectBountyRecord();
				if(bountyList!=null){
					for(SmartBounty bounty:bountyList){
						if(now.getTime()>bounty.getOvertime().getTime()){//已经过期
							//把原来记录的currentbvalue值修改为0
							updateBountyOvertimeByIdd(bounty.getIdd());
							//生成一条新的记录
							createBountyOverdueRecord(bounty.getUserid(),bounty.getIdd());
						}
					}
					Ebean.commitTransaction();
				}
			}catch(Exception e){
				util.LogUtil.writeLog("CleanOverdueBvalueThread线程被打断...Exception:"+ e.getMessage(), "addVipValidityTimeLog");
				Ebean.rollbackTransaction();
			}finally{
				Ebean.endTransaction();
			}
			StringUtil.sleep(1000);
		}
	}
	private List<SmartBounty> selectBountyRecord(){
		StringBuffer buf = new StringBuffer("find SmartBounty where 1=1 and act = 1 and currentbvalue>0 ");
		List<SmartBounty> bountyList = Ebean.getServer(GlobalDBControl.getDB())
				.createQuery(SmartBounty.class,buf.toString())
				.findList();
		if(bountyList!=null&&bountyList.size()>0){
			//util.LogUtil.writeLog("查询到奖励金记录:"+bountyList.size()+"条", "addVipValidityTimeLog");
			return bountyList;
		}
		return null;
	}
	
	private void updateBountyOvertimeByIdd(Integer idd){
		if(idd!=null){
			StringBuffer sql = new StringBuffer();
			sql.append("update smart_bounty set currentbvalue = :currentbvalue where idd = :idd ");
			Ebean.getServer(GlobalDBControl.getDB()).createSqlUpdate(sql.toString())
				.setParameter("currentbvalue", 0)
				.setParameter("idd", idd).execute();
			util.LogUtil.writeLog("成功修改id为:"+idd+"的currentbvalue值为0", "addVipValidityTimeLog");
		}
	}
	
	private void createBountyOverdueRecord(Integer userid,Integer idd) {
		Date now = new Date();
		if(userid != null && idd != null){
			SmartBounty oldBounty = selectSmartBountyByIdd(idd);
			SmartBounty bounty = new SmartBounty();
			bounty.setUserid(userid);
			bounty.setAddtime(now);
			bounty.setBvalue(oldBounty.getCurrentbvalue());
			bounty.setTitile("奖励金过期");
			bounty.setAct(2);
			bounty.setCurrentbvalue(0);
			Ebean.getServer(GlobalDBControl.getDB()).save(bounty);
			util.LogUtil.writeLog("成功生成过期奖励金记录", "addVipValidityTimeLog");
		}
	}
	
	private SmartBounty selectSmartBountyByIdd(Integer idd){
		if(idd != null){
			SmartBounty bounty = Ebean.getServer(GlobalDBControl.getDB())
				.find(SmartBounty.class).where().eq("idd", idd).findUnique();
			if(bounty != null){
				util.LogUtil.writeLog("成功查询到该条过期奖励金记录", "addVipValidityTimeLog");
				return bounty;
			}
		}
		return null;
	}
}
