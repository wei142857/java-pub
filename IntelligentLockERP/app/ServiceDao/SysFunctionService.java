package ServiceDao;

import java.util.ArrayList;
import java.util.List;

import models.SysFunction;
import play.Logger;
import util.GlobalDBControl;
import util.GlobalSetting;
import util.classEntity.TreeNode;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlRow;

public class SysFunctionService {
	static SysFunctionService instance = new SysFunctionService();

	public static SysFunctionService getInstance() {
		if (instance == null)
			instance = new SysFunctionService();
		return instance;
	}
	public SysFunction createFunction(final SysFunction function) {
		Ebean.getServer(GlobalSetting.defaultDB).save(function);
        return function;
    }
	public SysFunction findFunctionById(Integer fid){
		return Ebean.getServer(GlobalDBControl.getDB())
				.find(SysFunction .class).where().eq("idx", fid).findUnique();
	}
	public List<String> findFunctionIdsByRoleId(Integer roleId){
		 String sql = "select sf.idx,sf.parent from  sysfunction sf inner join sysrolefunc srf on sf.idx=srf.fid where srf.rid=:roleId";
		 List<SqlRow> list = Ebean.getServer(GlobalSetting.defaultDB).createSqlQuery(sql).setParameter("roleId", roleId).findList();
		 List<String> idxList = new ArrayList<String>();
		 List<String> parList = new ArrayList<String>();
		 for(SqlRow sr:list){
			 String idx = sr.getString("idx");
			 String parentIdx = sr.getString("parent");
			 idxList.add(idx); 
			 parList.add(parentIdx);
		 }
		 idxList.removeAll(parList);
		 return idxList;
	}
	private boolean hasChildren(String fId){
		Integer num = Ebean.getServer(GlobalSetting.defaultDB).find(SysFunction.class).where().eq("parent", fId).findRowCount();
		if(num==0)
			return false;
		return true;
		
	}
	public void deleteFunction(Integer fId) throws Exception {
		if(hasChildren(String.valueOf(fId)))
			throw new Exception("请先删除子节点");
		
		SysFunction sysfunction = findFunctionById(fId);
		if(sysfunction == null)
			throw new Exception("节点不存在");
		Ebean.beginTransaction();
		try{
			String sql = "delete from SysFunction where fcode like :fcode" ;
			Ebean.getServer(GlobalDBControl.getDB()).createUpdate(SysFunction .class, sql)
				.setParameter("fcode", sysfunction.getFcode()+".%" ).execute() ;
			
	        sql = "delete from sysrolefunc where fid=:fid";
	        Ebean.getServer(GlobalSetting.defaultDB).createSqlUpdate(sql).setParameter("fid", fId).execute();

	        sql = "delete from sysfunction where idx=:idx";
	        Ebean.getServer(GlobalSetting.defaultDB).createSqlUpdate(sql).setParameter("idx", fId).execute();
	        Ebean.commitTransaction();
		}catch(Exception e){
			Logger.info(e.toString());
			Ebean.rollbackTransaction();
			throw new Exception("系统错误");
		}finally{
			Ebean.endTransaction();
		}
		
    }
	
	public SysFunction dragNode(Integer fId,Integer parentId){
		SysFunction sf = findFunctionById(fId);
		sf.setParent(parentId);
		createFunction(sf);
		return sf;
	}
	
	public List<SysFunction> findCanDragNode(){
		
		String sql = "SELECT * from sysfunction where (url not LIKE '%View%' and url not LIKE '%Modify' and url not LIKE '%All' and url not LIKE '%List' and url not LIKE '%Get'  and url not LIKE '%Delete' ) ";
		List<SqlRow> rows = Ebean.getServer(GlobalSetting.defaultDB).createSqlQuery(sql).findList();
		
		List<SysFunction> list = new ArrayList<SysFunction>();
		
		for(SqlRow row : rows){
			SysFunction sf = new SysFunction();
			sf.setExt(row.getString("ext"));
			sf.setFcode(row.getString("fcode"));
			sf.setIcon(row.getString("icon"));
			sf.setIdx(row.getInteger("idx"));
			sf.setIsMenu(row.getInteger("isMenu"));
			sf.setLevel(row.getInteger("level"));
			sf.setName(row.getString("name"));
			sf.setParent(row.getInteger("parent"));
			sf.setState(row.getInteger("state"));
			sf.setUrl(row.getString("url"));
			list.add(sf);
		}
		
		return list;
		
//		return Ebean.getServer(GlobalSetting.defaultDB).find(SysFunction.class).where().lt("level", 4).findList();
	}
	public List<TreeNode> convert(List<SysFunction> funcs){
		List<TreeNode> list = new ArrayList<TreeNode>();
		TreeNode fn = null;
		for(SysFunction sf:funcs){
			fn = new TreeNode(sf.getIdx(),String.valueOf(sf.getName()),sf.getUrl(),sf.getParent(),sf.getIcon(),sf.getState(),sf.getFcode(),sf.getIsMenu(),sf.getLevel()==0);
			list.add(fn);
		}
		return list;
	}
}
