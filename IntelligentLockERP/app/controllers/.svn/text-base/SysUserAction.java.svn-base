package controllers;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Date;

import models.*;
import ServiceDao.SysRoleService;
import ServiceDao.SysUserService;

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
public class SysUserAction extends Controller {
	//进入页面列表；
	public static Result view() {
		return ok( SysUserList.render() );
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
		int pageno = nStart/ nLimit;
		StringBuffer sql = new StringBuffer();
		sql.append("find SysUser where 1=1 ");
		String idx = StringUtil.getHttpParam(request(), "idx");
		if(idx==null)
			idx = "";	
		if ( !( idx .equals("") ) && !( idx .equals("undefined") ) ) 
			sql.append(" and ( idx= '" + idx + "'  )");
		String login = StringUtil.getHttpParam(request(), "login");
		if(login==null)
			login = "";	
		if ( !( login .equals("") ) && !( login .equals("undefined") ) ) 
			sql.append(" and ( login like '%" + login + "%' or name like '%"+login+"%' )");
		String export = StringUtil.getHttpParam(request(), "export");// 是否导出
		if(export!=null && export.equals("1")){
			pageno = 0;
			nLimit = 999999999;
		}
		Logger.info(sql.toString());
		//下面简单的分页，需要重构，应该由 Dao-Sevice去实现。
		Page<SysUser> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.createQuery(SysUser .class, sql.toString())
				//.where().ilike("name", key+"%") 
				.orderBy("idx desc")
				.findPagingList(nLimit).getPage(pageno);
		if(export!=null && export.equals("1")){
			String fileName = "SysUser"+simp.format( new Date())+".xls";
			Logger.info(ulist.getList().size()+"size++++++++++++++++++++++++"+fileName);
			File file=null;
			try{
				file= exportList(ulist.getList(),fileName);
			}catch(Exception e)
			{
				Logger.info("export file:"+e.toString());
			}
			return ok(file);
		}
		for(SysUser su:ulist.getList()){
			su.setPwd("***");
		}
		PagedObject<SysUser> po= new PagedObject( ulist );
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(po) );
	}
	
	//列表
	public static Result listAll(){
		
		List<SysUser> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.find(SysUser .class)
				.orderBy("idx")
				.findList();
		for(SysUser usr:ulist)
			usr.setPwd("***");
		
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(ulist) );
	}
	
	//删除
	public static Result delete(int idx){
		SysUserService.getInstance().deleteUser(idx);
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson("操作成功") );
	}
	//获取 单个
	public static Result get(int idx){
		SysUser sysuser = Ebean.getServer(GlobalDBControl.getDB())
				.find(SysUser .class).where().eq("idx", idx).findUnique();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(sysuser) );
	}
	public static Result getRoles(int uId){
		List<String> list = SysRoleService.getInstance().findRoleIdsByUid(uId);
		return ok(Json.toJson(list));
	}
	
	//新增 / 修改
	public static Result modify(){
		response().setHeader("Cache-Control", "no-cache");
		SysUserService instance = SysUserService.getInstance();
		String idx = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "idx");
		String idxs = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "idxs");
		int nidx = StringTool.GetInt(idx, 0);
		String login = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "login");
		String pwd = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "pwd");
		String name = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "name");
		String phone = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "phone");
		String email = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "email");
		String state = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "state");
		String addtime = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "addtime");
		String domian = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "domian");
		String ext = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "ext");
		String sex = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "sex");
		String birthday = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "birthday");
		String usertype = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "usertype");
		
		String orgid = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "orgid");
		
		String operation = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "operation");
		
		SysUser sysuser ;
		if(  operation.equals("add") )	// 新增
		{
			sysuser = new SysUser ();
			sysuser.setState(0);
			sysuser.setAddtime( new Date() );
		}
		else{			//修改
			sysuser = Ebean.getServer(GlobalDBControl.getDB())
					.find(SysUser .class).where().eq("idx", idx).findUnique();
			if(!StringUtil.isNull(idxs)&&sysuser!=null){
				if(idxs.equals("DelAll"))
					instance.uncorrelationRoles(sysuser.getIdx());
				else{
					String fids[] = idxs.split(",");
					List<String> flist = Arrays.asList(fids);
					List<Integer> ilist = new ArrayList<Integer>();
					for(String fid:flist){
						ilist.add(Integer.valueOf(fid));
					}
					try {
						instance.correlationRoles(sysuser.getIdx(), ilist.toArray(new Integer[ilist.size()]));
					} catch (Exception e) {
						return ok( Json.toJson(e.getMessage()) );
					}
				}
			}
		}
		if( sysuser!=null ){
			//赋值,字符串类型可以直接赋值，其他类型需要转换 ！！！！
					sysuser. setLogin ( login );
					sysuser. setPwd ( "" );
					if( pwd!=null&&pwd.length()>0 )
						sysuser.setPwdmd5(util.MD5.mkMd5(pwd));
					sysuser. setName ( name );
					sysuser. setPhone ( phone );
					sysuser. setEmail ( email );
					sysuser.setUsertype(StringTool.GetInt(usertype, 0));
					sysuser.setOrgid(StringTool.GetInt(orgid, 0));
					sysuser. setExt ( ext );
					sysuser. setBirthday ( new Date() );
					sysuser.setState(StringTool.GetInt(state, 0));
			Ebean.getServer(GlobalDBControl.getDB()).save( sysuser );
			return ok( Json.toJson(sysuser) );
		}
		return ok( Json.toJson(null) );
	}
	static int num=0;
	public static File exportList(List<SysUser> infoList,String fileNameChine) throws UnsupportedEncodingException {
		Workbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet("sheet1");
			sheet.setColumnWidth( 0 , 6000 );
			sheet.setColumnWidth( 1 , 6000 );
			sheet.setColumnWidth( 2 , 6000 );
			sheet.setColumnWidth( 3 , 6000 );
			sheet.setColumnWidth( 4 , 6000 );
			sheet.setColumnWidth( 5 , 6000 );
			sheet.setColumnWidth( 6 , 6000 );
			sheet.setColumnWidth( 7 , 6000 );
			sheet.setColumnWidth( 8 , 6000 );
			sheet.setColumnWidth( 9 , 6000 );
			sheet.setColumnWidth( 10 , 6000 );
			sheet.setColumnWidth( 11 , 6000 );
		ExcelGenerateHelper helper = ExcelGenerateHelper.getInstance(wb);
		// 获取行索引
		int rowIndex = 0;
		int colIndex = 0;
		// 设置表头
		String[] titles = { 
					"idx"
					,"login"
					,"pwd"
					,"name"
					,"phone"
					,"email"
					,"state"
					,"addtime"
					,"domian"
					,"ext"
					,"sex"
					,"birthday"
			};
		// 生成表头
		helper.setRowIndex(rowIndex++);
		helper.generateHeader(sheet, titles, 0, StringUtils.EMPTY,
				StringUtils.EMPTY);
		// 循环生成数据行
		for (int i = 0; i < infoList.size(); i++) {
			colIndex = 0;
			SysUser info = (SysUser) infoList.get(i);
			Row row = sheet.createRow(rowIndex++);
				helper.createStringCell( row , colIndex++, ""+info. getIdx () );
				helper.createStringCell( row , colIndex++, ""+info. getLogin () );
				helper.createStringCell( row , colIndex++, ""+info. getPwd () );
				helper.createStringCell( row , colIndex++, ""+info. getName () );
				helper.createStringCell( row , colIndex++, ""+info. getPhone () );
				helper.createStringCell( row , colIndex++, ""+info. getEmail () );
				helper.createStringCell( row , colIndex++, ""+info. getState () );
				helper.createStringCell( row , colIndex++, ""+info. getAddtime () );
				helper.createStringCell( row , colIndex++, ""+info. getDomian () );
				helper.createStringCell( row , colIndex++, ""+info. getExt () );
				helper.createStringCell( row , colIndex++, ""+info. getSex () );
				helper.createStringCell( row , colIndex++, ""+info. getBirthday () );
		}
		//在application.conf中配置的路径
		String path = Configuration.root().getString("export.path");
		int numStra = num++;//静态值，区分文件名
		File file = new File(path);
		file.mkdir();//判断文件夹是否存在，不存在就创建
		FileOutputStream out = null;
		String fileName = path + "SysUser" + System.currentTimeMillis() + numStra + ".xls";
		try {
			out = new FileOutputStream(fileName);
			wb.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		File downFile = new File(fileName);
		if (request().getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) {
			fileNameChine = "=?UTF-8?B?" + (new String(Base64.encodeBase64(fileNameChine.getBytes("UTF-8")))) + "?=";    
        } else{
        	fileNameChine = java.net.URLEncoder.encode(fileNameChine, "UTF-8");
        }
		response().setHeader("Content-disposition","attachment;filename=" + fileNameChine);
		response().setContentType("application/msexcel");
		return downFile;
}
}