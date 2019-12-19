package controllers;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import models.*;

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
public class SysOrgAction extends Controller {
	//进入页面列表；
	public static Result view() {
		return ok( SysOrgList.render() );
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
		sql.append("find SysOrg where 1=1 ");
		String idx = StringUtil.getHttpParam(request(), "idx");
		if(idx==null)
			idx = "";	
		if ( !( idx .equals("") ) && !( idx .equals("undefined") ) ) 
			sql.append(" and ( idx= '" + idx + "'  )");
		String name = StringUtil.getHttpParam(request(), "name");
		if(name==null)
			name = "";	
		if ( !( name .equals("") ) && !( name .equals("undefined") ) ) 
			sql.append(" and ( name= '" + name + "'  )");
		String addtime = StringUtil.getHttpParam(request(), "addtime");
		if(addtime==null)
			addtime = "";	
		if ( !( addtime .equals("") ) && !( addtime .equals("undefined") ) ) 
			sql.append(" and ( addtime= '" + addtime + "'  )");
		String domain = StringUtil.getHttpParam(request(), "domain");
		if(domain==null)
			domain = "";	
		if ( !( domain .equals("") ) && !( domain .equals("undefined") ) ) 
			sql.append(" and ( domain= '" + domain + "'  )");
		String state = StringUtil.getHttpParam(request(), "state");
		if(state==null)
			state = "";	
		if ( !( state .equals("") ) && !( state .equals("undefined") ) ) 
			sql.append(" and ( state= '" + state + "'  )");
		String ext = StringUtil.getHttpParam(request(), "ext");
		if(ext==null)
			ext = "";	
		if ( !( ext .equals("") ) && !( ext .equals("undefined") ) ) 
			sql.append(" and ( ext= '" + ext + "'  )");
		String export = StringUtil.getHttpParam(request(), "export");// 是否导出
		if(export!=null && export.equals("1")){
			pageno = 0;
			nLimit = 999999999;
		}
		Logger.info(sql.toString());
		//下面简单的分页，需要重构，应该由 Dao-Sevice去实现。
		Page<SysOrg> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.createQuery(SysOrg .class, sql.toString())
				//.where().ilike("name", key+"%") 
				.orderBy("idx desc")
				.findPagingList(nLimit).getPage(pageno);
		if(export!=null && export.equals("1")){
			String fileName = "SysOrg"+simp.format( new Date())+".xls";
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
		PagedObject<SysOrg> po= new PagedObject( ulist );
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(po) );
	}
	//列表
	public static Result listAll(){
		List<SysOrg> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.find(SysOrg .class)
				.orderBy("idx")
				.findList();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(ulist) );
	}
	//删除
	public static Result delete(int idx){
		String sql = "delete from SysOrg where idx =:idx";
		Ebean.getServer(GlobalDBControl.getDB())
				.createUpdate(SysOrg .class, sql).setParameter("idx", idx).execute();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson("操作成功") );
	}
	//获取 单个
	public static Result get(int idx){
		SysOrg sysorg = Ebean.getServer(GlobalDBControl.getDB())
				.find(SysOrg .class).where().eq("idx", idx).findUnique();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(sysorg) );
	}
	//新增 / 修改
	public static Result modify(){
		String idx = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "idx");
		int nidx = StringTool.GetInt(idx, 0);
		String name = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "name");
		String addtime = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "addtime");
		String domain = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "domain");
		String state = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "state");
		String ext = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "ext");
		String operation = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "operation");
		SysOrg sysorg ;
		if(  operation.equals("add") )	// 新增
			sysorg = new SysOrg ();
		else			//修改
			sysorg = Ebean.getServer(GlobalDBControl.getDB())
					.find(SysOrg .class).where().eq("idx", idx).findUnique();
		if( sysorg!=null ){
			//赋值,字符串类型可以直接赋值，其他类型需要转换 ！！！！
					sysorg. setName ( name );
					sysorg. setAddtime ( new Date() );
					sysorg. setDomain ( domain );
					sysorg. setExt ( ext );
			Ebean.getServer(GlobalDBControl.getDB()).save( sysorg );
			response().setHeader("Cache-Control", "no-cache");
			return ok( Json.toJson(sysorg) );
		}
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(null) );
	}
	static int num=0;
	public static File exportList(List<SysOrg> infoList,String fileNameChine) throws UnsupportedEncodingException {
		Workbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet("sheet1");
			sheet.setColumnWidth( 0 , 6000 );
			sheet.setColumnWidth( 1 , 6000 );
			sheet.setColumnWidth( 2 , 6000 );
			sheet.setColumnWidth( 3 , 6000 );
			sheet.setColumnWidth( 4 , 6000 );
			sheet.setColumnWidth( 5 , 6000 );
		ExcelGenerateHelper helper = ExcelGenerateHelper.getInstance(wb);
		// 获取行索引
		int rowIndex = 0;
		int colIndex = 0;
		// 设置表头
		String[] titles = { 
					"idx"
					,"name"
					,"addtime"
					,"domain"
					,"state"
					,"ext"
			};
		// 生成表头
		helper.setRowIndex(rowIndex++);
		helper.generateHeader(sheet, titles, 0, StringUtils.EMPTY,
				StringUtils.EMPTY);
		// 循环生成数据行
		for (int i = 0; i < infoList.size(); i++) {
			colIndex = 0;
			SysOrg info = (SysOrg) infoList.get(i);
			Row row = sheet.createRow(rowIndex++);
				helper.createStringCell( row , colIndex++, ""+info. getIdx () );
				helper.createStringCell( row , colIndex++, ""+info. getName () );
				helper.createStringCell( row , colIndex++, ""+info. getAddtime () );
				helper.createStringCell( row , colIndex++, ""+info. getDomain () );
				helper.createStringCell( row , colIndex++, ""+info. getState () );
				helper.createStringCell( row , colIndex++, ""+info. getExt () );
		}
		//在application.conf中配置的路径
		String path = Configuration.root().getString("export.path");
		int numStra = num++;//静态值，区分文件名
		File file = new File(path);
		file.mkdir();//判断文件夹是否存在，不存在就创建
		FileOutputStream out = null;
		String fileName = path + "SysOrg" + System.currentTimeMillis() + numStra + ".xls";
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