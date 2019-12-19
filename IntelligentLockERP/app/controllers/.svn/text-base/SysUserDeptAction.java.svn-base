
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
public class SysUserDeptAction extends Controller {
  
	//进入页面列表；
	public static Result view() {
		return ok( SysUserDeptList.render() );
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
		sql.append("find SysUserDept where 1=1 ");
		
		String idx = StringUtil.getHttpParam(request(), "idx");
		if(idx==null)
			idx = "";	
		if ( !( idx .equals("") ) && !( idx .equals("undefined") ) ) 
			sql.append(" and ( idx= '" + idx + "'  )");
		
		String uid = StringUtil.getHttpParam(request(), "uid");
		if(uid==null)
			uid = "";	
		if ( !( uid .equals("") ) && !( uid .equals("undefined") ) ) 
			sql.append(" and ( uid= '" + uid + "'  )");
		
		String did = StringUtil.getHttpParam(request(), "did");
		if(did==null)
			did = "";	
		if ( !( did .equals("") ) && !( did .equals("undefined") ) ) 
			sql.append(" and ( did= '" + did + "'  )");
		
		String state = StringUtil.getHttpParam(request(), "state");
		if(state==null)
			state = "";	
		if ( !( state .equals("") ) && !( state .equals("undefined") ) ) 
			sql.append(" and ( state= '" + state + "'  )");
		
		String deptcode = StringUtil.getHttpParam(request(), "deptcode");
		if(deptcode==null)
			deptcode = "";	
		if ( !( deptcode .equals("") ) && !( deptcode .equals("undefined") ) ) 
			sql.append(" and ( deptcode= '" + deptcode + "'  )");
		
		String addtime = StringUtil.getHttpParam(request(), "addtime");
		if(addtime==null)
			addtime = "";	
		if ( !( addtime .equals("") ) && !( addtime .equals("undefined") ) ) 
			sql.append(" and ( addtime= '" + addtime + "'  )");
		
		String export = StringUtil.getHttpParam(request(), "export");// 是否导出
		if(export!=null && export.equals("1")){
			nStart = 0;
			nLimit = 999999999;
		}
		
		Logger.info(sql.toString());
		//下面简单的分页，需要重构，应该由 Dao-Sevice去实现。
		int rowCount = Ebean.getServer(GlobalDBControl.getDB()).createQuery(SysUserDept .class, sql.toString()).findRowCount();
		List<SysUserDept> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.createQuery(SysUserDept .class, sql.toString())
				.setFirstRow(nStart).setMaxRows(nLimit)
				.orderBy("idx desc")
				.findList();
				
		if(export!=null && export.equals("1")){
			String fileName = "SysUserDept"+simp.format( new Date())+".xls";
			Logger.info(ulist.size()+"size++++++++++++++++++++++++"+fileName);
			File file=null;
			try{
				file= exportList(ulist,fileName);
			}catch(Exception e)
			{
				Logger.info("export file:"+e.toString());
			}

			return ok(file);
		}		
				
		int nPages = rowCount/nLimit;
		if( rowCount%nLimit>0 )
			nPages ++;
		PagedObject<SysUserDept> po= new PagedObject( ulist,nPages,rowCount );
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(po) );
	}

	//列表
	public static Result listAll(){
		List<SysUserDept> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.find(SysUserDept .class)
				.orderBy("idx")
				.findList();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(ulist) );
	}
	
	//删除
	public static Result delete(int idx){
		String sql = "delete from SysUserDept where idx =:idx";
		Ebean.getServer(GlobalDBControl.getDB())
				.createUpdate(SysUserDept .class, sql).setParameter("idx", idx).execute();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson("操作成功") );
	}
	
	//获取 单个
	public static Result get(int idx){
		SysUserDept sysuserdept = Ebean.getServer(GlobalDBControl.getDB())
				.find(SysUserDept .class).where().eq("idx", idx).findUnique();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(sysuserdept) );
	}

	//新增 / 修改
	public static Result modify(){
		String idx = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "idx");
		int nidx = StringTool.GetInt(idx, 0);
		
		
		
		
		
		String uid = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "uid");
		
		
		
		String did = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "did");
		
		
		
		String state = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "state");
		
		
		
		String deptcode = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "deptcode");
		
		
		
		String addtime = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "addtime");
		
		
		
		String operation = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "operation");
		
		SysUserDept sysuserdept ;
		if(  operation.equals("add") )	// 新增
			sysuserdept = new SysUserDept ();
		else			//修改
			sysuserdept = Ebean.getServer(GlobalDBControl.getDB())
					.find(SysUserDept .class).where().eq("idx", idx).findUnique();
		
		if( sysuserdept!=null ){
			//赋值,字符串类型可以直接赋值，其他类型需要转换 ！！！！
			
				 
				
			
				 
				
			
				 
				
			
				 
				
			
				
					sysuserdept. setDeptcode ( deptcode );
				 
				
			
				 
				
					sysuserdept. setAddtime ( new Date() );
				
			
			Ebean.getServer(GlobalDBControl.getDB()).save( sysuserdept );
			response().setHeader("Cache-Control", "no-cache");
			return ok( Json.toJson(sysuserdept) );
		}

		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(null) );
	}
	
	static int num=0;
	public static File exportList(List<SysUserDept> infoList,String fileNameChine) throws UnsupportedEncodingException {
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
					
				
					
					,"uid"
				
					
					,"did"
				
					
					,"state"
				
					
					,"deptcode"
				
					
					,"addtime"
				
			};

		// 生成表头
		helper.setRowIndex(rowIndex++);
		helper.generateHeader(sheet, titles, 0, StringUtils.EMPTY,
				StringUtils.EMPTY);

		// 循环生成数据行
		for (int i = 0; i < infoList.size(); i++) {
			colIndex = 0;
			SysUserDept info = (SysUserDept) infoList.get(i);
			Row row = sheet.createRow(rowIndex++);
			
			
				helper.createStringCell( row , colIndex++, ""+info. getIdx () );
			
				helper.createStringCell( row , colIndex++, ""+info. getUid () );
			
				helper.createStringCell( row , colIndex++, ""+info. getDid () );
			
				helper.createStringCell( row , colIndex++, ""+info. getState () );
			
				helper.createStringCell( row , colIndex++, ""+info. getDeptcode () );
			
				helper.createStringCell( row , colIndex++, ""+info. getAddtime () );
			
		}
		//在application.conf中配置的路径
		String path = Configuration.root().getString("export.path");
		int numStra = num++;//静态值，区分文件名
		File file = new File(path);
		file.mkdir();//判断文件夹是否存在，不存在就创建
		
		FileOutputStream out = null;
		String fileName = path + "SysUserDept" + System.currentTimeMillis() + numStra + ".xls";
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