
package controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Date;

import models.*;
import ServiceDao.SysFunctionService;
import ServiceDao.SysRoleService;
import com.avaje.ebean.Ebean;
import java.text.SimpleDateFormat;
import play.libs.Json;
import play.mvc.*;
import util.AjaxHellper;
import util.GlobalDBControl;
import util.PagedObject;
import util.StringUtil;
import play.Logger;
import views.html.page.*;
import util.classEntity.*;

@Security.Authenticated(Secured.class)
public class SysRoleAction extends Controller {
  
	//进入页面列表；
	public static Result view() {
		return ok( SysRoleList.render() );
	}
	
	/**************************************************************	
	 *                增删改查的功能   ： AJAX方式
	 */	
	 private static SimpleDateFormat simp=new SimpleDateFormat("yyyy-MM-dd");
	//列表
	public static Result list(){
		//获取前端grid 传递过来的参数：分页，搜索
		String start = StringUtil.getHttpParam(request(), "start");
		String limit = StringUtil.getHttpParam(request(), "limit");
		int nStart = StringTool.GetInt(start, 0);
		int nLimit = StringTool.GetInt(limit, 10);

		StringBuffer sql = new StringBuffer();
		sql.append("find SysRole where 1=1 ");
		
		String idx = StringUtil.getHttpParam(request(), "idx");
		if(idx==null)
			idx = "";	
		if ( !( idx .equals("") ) && !( idx .equals("undefined") ) ) 
			sql.append(" and ( idx= '" + idx + "'  )");
		
		String name = StringUtil.getHttpParam(request(), "name");
		if(name==null)
			name = "";	
		if ( !( name .equals("") ) && !( name .equals("undefined") ) ) 
			sql.append(" and ( name like '%" + name + "%'  )");
		
		String oid = StringUtil.getHttpParam(request(), "oid");
		if(oid==null)
			oid = "";	
		if ( !( oid .equals("") ) && !( oid .equals("undefined") ) ) 
			sql.append(" and ( oid= '" + oid + "'  )");
		
		String state = StringUtil.getHttpParam(request(), "state");
		if(state==null)
			state = "";	
		if ( !( state .equals("") ) && !( state .equals("undefined") ) ) 
			sql.append(" and ( state= '" + state + "'  )");
		
		String addtime = StringUtil.getHttpParam(request(), "addtime");
		if(addtime==null)
			addtime = "";	
		if ( !( addtime .equals("") ) && !( addtime .equals("undefined") ) ) 
			sql.append(" and ( addtime > '" + addtime + "' and addtime < '"+addtime+" 23:59:59'  )");
		
		
		Logger.info(sql.toString());
		//下面简单的分页，需要重构，应该由 Dao-Sevice去实现。
		int rowCount = Ebean.getServer(GlobalDBControl.getDB()).createQuery(SysRole .class, sql.toString()).findRowCount();
		List<SysRole> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.createQuery(SysRole .class, sql.toString())
				.setFirstRow(nStart).setMaxRows(nLimit)
				.orderBy("idx desc")
				.findList();
				
				
		int nPages = rowCount/nLimit;
		if( rowCount%nLimit>0 )
			nPages ++;
		PagedObject<SysRole> po= new PagedObject( ulist,nPages,rowCount );
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(po) );
	}

	/**
	 * 获取所有角色以树形展示
	 * @return
	 */
	public static Result listAll(){
		List<SysRole> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.find(SysRole .class)
				.findList();
		List<TreeNode> rList = SysRoleService.getInstance().convert(ulist);
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(rList) );
	}
	
	//删除
	public static Result delete(int idx){
		response().setHeader("Cache-Control", "no-cache");
		try {
			SysRoleService.getInstance().deleteRole(idx);
		} catch (Exception e) {
			return ok(Json.toJson(e.getMessage()));
		}
		
		return ok( Json.toJson("操作成功") );
	}
	
	//获取 单个
	public static Result get(int idx){
		SysRole sysrole = Ebean.getServer(GlobalDBControl.getDB())
				.find(SysRole .class).where().eq("idx", idx).findUnique();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(sysrole) );
	}
	
	public static Result getFunctions(int roleId){
		List<String> list = SysFunctionService.getInstance().findFunctionIdsByRoleId(roleId);
		return ok(Json.toJson(list));
	}
	
	//新增 / 修改
	public static Result modify(){
		response().setHeader("Cache-Control", "no-cache");
		SysRoleService instance = SysRoleService.getInstance();
		String idx = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "idx");
		
		String idxs = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "idxs");	
		
		String name = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "name");
		
		String state = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "state");
		
		int nstate = StringTool.GetInt(state, 1);
		
		String operation = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "operation");
		
		SysRole sysrole ;
		if(  operation.equals("add") ){	// 新增
			sysrole = new SysRole ();
			sysrole.setAddtime(new Date());
		}
		else{			//修改
			sysrole = Ebean.getServer(GlobalDBControl.getDB())
					.find(SysRole .class).where().eq("idx", idx).findUnique();
			if(!StringUtil.isNull(idxs)&&sysrole!=null){
				if(idxs.equals("DelAll"))
					instance.uncorrelationFunctions(sysrole.getIdx());
				else{
					String fids[] = idxs.split(",");
					List<String> flist = Arrays.asList(fids);
					List<Integer> ilist = new ArrayList<Integer>();
					for(String fid:flist){
						ilist.add(Integer.valueOf(fid));
					}
					try {
						instance.correlationFunctions(sysrole.getIdx(), ilist.toArray(new Integer[ilist.size()]));
					} catch (Exception e) {
						return ok(Json.toJson(e.getMessage()));
					}
				}
			}
		}
		if( sysrole!=null ){
			//赋值,字符串类型可以直接赋值，其他类型需要转换 ！！！！
			sysrole. setName ( name );
			sysrole.setState(nstate);
			instance.createRole(sysrole);
			return ok( Json.toJson(sysrole) );
		}

		return ok( Json.toJson(null) );
	}
	
	
}