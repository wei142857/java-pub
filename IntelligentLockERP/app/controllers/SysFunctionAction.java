package controllers;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import models.*;
import ServiceDao.SysFunctionService;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Page;

import java.text.SimpleDateFormat;
import java.io.*;

import util.ExcelGenerateHelper;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import play.libs.Json;
import play.mvc.*;
import play.*;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import util.AjaxHellper;
import util.GlobalDBControl;
import util.GlobalSetting;
import util.ImageGenerator;
import util.PagedObject;
import util.StringUtil;
import play.Logger;
import views.html.page.*;
import util.classEntity.*;

@Security.Authenticated(Secured.class)
public class SysFunctionAction extends Controller {
	/**************************************************************	
	 *                增删改查的功能   ： AJAX方式
	 */	
	 private static SimpleDateFormat simp=new SimpleDateFormat("yyyy-MM-dd");
	//删除
	public static Result delete(int idx){
		try{
			SysFunctionService.getInstance().deleteFunction(idx);
		}catch(Exception e){
			Logger.info(e.toString());
			return ok(Json.toJson(e.getMessage()));
		}
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson("操作成功") );
	}
	//获取 单个
	public static Result get(int idx){
		SysFunction sysfunction =SysFunctionService.getInstance().findFunctionById(idx);
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(sysfunction) );
	}
	
	public static Result canDragNode(){
		response().setHeader("Cache-Control", "no-cache");
		List<SysFunction> list = SysFunctionService.getInstance().findCanDragNode();
		return ok(Json.toJson(list));
	}
	
	public static Result dragNode(){
		response().setHeader("Cache-Control", "no-cache");
		String idx = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "idx");
		String parentId = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "parentId");
		try{
			Integer nIdx  = Integer.valueOf(idx);
			Integer nParentId = Integer.valueOf(parentId);
			SysFunctionService.getInstance().dragNode(nIdx, nParentId);
			return ok(Json.toJson("操作成功"));
		}catch(Exception e){
			return ok(Json.toJson("操作错误"));
		}
		
	}
	//新增 / 修改
	public static Result modify(){
		String idx = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "idx");
//		int nidx = StringTool.GetInt(idx, 0);
		String name = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "name");
		String ext = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "ext");
		String isMenu = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "isMenu");
		int nisMenu = StringTool.GetInt(isMenu, 1);
		String parent = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "parent");
		String icon = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "icon");
		String state = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "state");
		int nstate = StringTool.GetInt( state , 0 );
		String url = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "url");
		String fcode = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "fcode");
		String operation = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "operation");
		
		SysFunction sysfunction ;
		
		if(  operation.equals("add") )	// 新增
		{
			sysfunction = new SysFunction ();
			int pid = StringTool.GetInt(parent,-1);
			SysFunction parfunction = Ebean.getServer(GlobalDBControl.getDB())
					.find(SysFunction .class).where().eq("idx", pid).findUnique();
			if( parfunction == null )
				return ok( Json.toJson(null) );
			
			sysfunction.setParent( pid );
			sysfunction.setLevel( parfunction.getLevel() + 1 );
			sysfunction.setAddtime(new Date());
		}
		else			//修改
		{
			sysfunction = Ebean.getServer(GlobalDBControl.getDB())
					.find(SysFunction .class).where().eq("idx", idx).findUnique();
			if(!StringUtil.isNull(parent)){
				int pid = StringTool.GetInt(parent,-1);
				sysfunction.setParent( pid );
			}
		}
		
		if( sysfunction!=null ){
			//赋值,字符串类型可以直接赋值，其他类型需要转换 ！！！！
			sysfunction. setName ( name );
			sysfunction. setExt ( ext );
			sysfunction. setIcon ( icon );
			sysfunction. setAddtime ( new Date() );
			sysfunction. setUrl ( url );
			sysfunction. setState( nstate );
			sysfunction.setIsMenu(nisMenu);
			sysfunction.setFcode(fcode);
			SysFunctionService.getInstance().createFunction(sysfunction);
			response().setHeader("Cache-Control", "no-cache");
			return ok( Json.toJson(sysfunction) );
		}
		
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(null) );
	}
	/*
	 * 树形结构的页面
	 */
	public static Result treeView() {
		return ok( SysFunctionTree.render() );
	}
	
	public static Result treeData() {
		List<SysFunction> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.find(SysFunction .class).where().findList();
		List<TreeNode> po = SysFunctionService.getInstance().convert(ulist);
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(po) );
	}

}