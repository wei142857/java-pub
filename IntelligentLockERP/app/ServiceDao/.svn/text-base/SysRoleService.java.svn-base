package ServiceDao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import models.SysRole;
import models.SysRoleFunc;
import models.SysUser;
import play.Logger;
import util.GlobalSetting;
import util.classEntity.TreeNode;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlRow;

public class SysRoleService {
	static SysRoleService instance = new SysRoleService();

	public static SysRoleService getInstance() {
		if (instance == null)
			instance = new SysRoleService();
		return instance;
	}

	public SysRole createRole(final SysRole role) {
		Ebean.getServer(GlobalSetting.defaultDB).save(role);
		return role;
	}

	public void deleteRole(Integer roleId) throws Exception {
		Ebean.beginTransaction();
		try {
			String sql = "delete from sysuserrole where rid=:rid";
			Ebean.getServer(GlobalSetting.defaultDB).createSqlUpdate(sql)
					.setParameter("rid", roleId).execute();
			sql = "delete from sysrolefunc where rid=:rid";
			Ebean.getServer(GlobalSetting.defaultDB).createSqlUpdate(sql)
					.setParameter("rid", roleId).execute();
			sql = "delete from SysRole where idx=:idx";
			Ebean.getServer(GlobalSetting.defaultDB).createSqlUpdate(sql)
					.setParameter("idx", roleId).execute();
			Ebean.commitTransaction();
		} catch (Exception e) {
			Logger.info(e.toString());
			Ebean.rollbackTransaction();
			throw new Exception("系统错误");
		}finally{
			Ebean.endTransaction();
		}

	}

	public void correlationFunctions(Integer roleId, Integer... fIds) throws Exception {
		if (fIds == null || fIds.length == 0) {
			return;
		}
		Ebean.beginTransaction();
		try{
			uncorrelationFunctions(roleId);
			List<SysRoleFunc> list = new ArrayList<SysRoleFunc>();
			for (Integer fId : fIds) {
				SysRoleFunc srf = new SysRoleFunc();
				srf.setRid(roleId);
				srf.setFid(fId);
				srf.setAddtime(new Date());
				list.add(srf);
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

	public List<String> findRoleIdsByUid(Integer uId) {
		String sql = "select sr.idx from  sysrole sr inner join sysuserrole sur on sr.idx=sur.rid where sur.uid=:uId";
		List<SqlRow> list = Ebean.getServer(GlobalSetting.defaultDB)
				.createSqlQuery(sql).setParameter("uId", uId).findList();
		List<String> rList = new ArrayList<String>();
		for (SqlRow sr : list) {
			rList.add(sr.getString("idx"));
		}
		return rList;
	}

	public void uncorrelationFunctions(Integer roleId) {
		String sql = "delete from sysrolefunc where rid=:roleId";
		Ebean.getServer(GlobalSetting.defaultDB).createSqlUpdate(sql)
				.setParameter("roleId", roleId).execute();
	}

	public List<SqlRow> findSysRolesByUserName(String username) {
		List<SysUser> ulist = Ebean.getServer(GlobalSetting.defaultDB)
				.find(SysUser.class).where().eq("login", username).findList();

		if (ulist.size() > 0) {
			String sql = "select sr.name from sysrole sr INNER JOIN sysuserrole sur on sur.rid = sr.idx where sur.uid=:uid";
			return Ebean.getServer(GlobalSetting.defaultDB).createSqlQuery(sql)
					.setParameter("uid", ulist.get(0).getIdx()).findList();
		}

		return null;
	}

	public List<TreeNode> convert(List<SysRole> roles) {
		List<TreeNode> list = new ArrayList<TreeNode>();
		TreeNode fn = null;
		for (SysRole sr : roles) {
			fn = new TreeNode(sr.getIdx(), String.valueOf(sr.getName()), 0,
					sr.getState(), true);
			list.add(fn);
		}
		return list;
	}
}
