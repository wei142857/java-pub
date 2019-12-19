package ServiceDao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import models.SysFunction;
import models.SysUser;
import models.SysUserRole;
import play.Logger;
import util.GlobalSetting;

import com.avaje.ebean.Ebean;

public class SysUserService {
	static SysUserService instance = new SysUserService();

	public static SysUserService getInstance() {
		if (instance == null)
			instance = new SysUserService();
		return instance;
	}
	
	public List<SysFunction> findSysFunctionsByUser(Integer uId){
		String sql = "find SysFunction WHERE state = 1 and idx IN ( SELECT srf.fid FROM sysrolefunc srf WHERE srf.rid IN (select syr.idx from sysrole syr where syr.state =1  and syr.idx IN ( SELECT sur.rid FROM sysuserrole sur WHERE sur.uid=:uId )) ) ORDER BY fcode";
		return Ebean.getServer(GlobalSetting.defaultDB).createQuery(SysFunction.class,sql).setParameter("uId", uId).findList();
	}
	
	public void deleteUser(Integer uId){
		 String sql = "delete from sysuserrole where uid=:uId";
		 Ebean.getServer(GlobalSetting.defaultDB).createSqlUpdate(sql).setParameter("uId", uId).execute();
		 		sql = "delete from sysuser where idx=:idx";
	     Ebean.getServer(GlobalSetting.defaultDB).createSqlUpdate(sql).setParameter("idx", uId).execute();
	}
	
	public void correlationRoles(Integer userId, Integer... roleIds) throws Exception {
        if(roleIds == null || roleIds.length == 0) {
            return;
        }
        Ebean.beginTransaction();
        try{
        	uncorrelationRoles(userId);
            List<SysUserRole> list =  new ArrayList<SysUserRole>();
            for(Integer roleId : roleIds){
            	SysUserRole sur = new SysUserRole();
            	sur.setRid(roleId);
            	sur.setUid(userId);
            	sur.setAddtime(new Date());
            	list.add(sur);
            }
            Ebean.getServer(GlobalSetting.defaultDB).save(list);
            Ebean.commitTransaction();
        }catch(Exception e){
        	Logger.info(e.toString());
        	Ebean.rollbackTransaction();
        	throw new Exception("系统错误");
        }finally{
        	Ebean.endTransaction();
        }
        
    }
	
	
	public void uncorrelationRoles(Integer userId) {
        String sql = "delete from sysuserrole where uid=:uId";
        Ebean.getServer(GlobalSetting.defaultDB).createSqlUpdate(sql).setParameter("uId", userId).execute();
    }
	
	
	public SysUser  findSysUserById(Integer userId){
		List<SysUser> list = Ebean.getServer(GlobalSetting.defaultDB).find(SysUser.class).where().eq("uid", userId).findList();
		if(list.size()>0)return list.get(0);
		return null;
	}
}
