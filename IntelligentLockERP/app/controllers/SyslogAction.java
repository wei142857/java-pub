
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
public class SyslogAction extends Controller {
  
	//进入页面列表；
	public static Result view() {
		return ok( SyslogList.render() );
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
		sql.append("find Syslog where 1=1 ");
		
		String idd = StringUtil.getHttpParam(request(), "idd");
		if(idd==null)
			idd = "";	
		if ( !( idd .equals("") ) && !( idd .equals("undefined") ) ) 
			sql.append(" and ( idd= '" + idd + "'  )");
		
		String module = StringUtil.getHttpParam(request(), "module");
		if(module==null)
			module = "";	
		if ( !( module .equals("") ) && !( module .equals("undefined") ) ) 
			sql.append(" and ( module= '" + module + "'  )");
		
		String fcode = StringUtil.getHttpParam(request(), "fcode");
		if(fcode==null)
			fcode = "";	
		if ( !( fcode .equals("") ) && !( fcode .equals("undefined") ) ) 
			sql.append(" and ( fcode= '" + fcode + "'  )");
		
		String uri = StringUtil.getHttpParam(request(), "uri");
		if(uri==null)
			uri = "";	
		if ( !( uri .equals("") ) && !( uri .equals("undefined") ) ) 
			sql.append(" and ( uri= '" + uri + "'  )");
		
		String user = StringUtil.getHttpParam(request(), "user");
		if(user==null)
			user = "";	
		if ( !( user .equals("") ) && !( user .equals("undefined") ) ) 
			sql.append(" and ( user= '" + user + "'  )");
		
		String addtime = StringUtil.getHttpParam(request(), "addtime");
		if(addtime==null)
			addtime = "";	
		if ( !( addtime .equals("") ) && !( addtime .equals("undefined") ) ) 
			sql.append(" and ( addtime= '" + addtime + "'  )");
		
		String msg = StringUtil.getHttpParam(request(), "msg");
		if(msg==null)
			msg = "";	
		if ( !( msg .equals("") ) && !( msg .equals("undefined") ) ) 
			sql.append(" and ( msg like '%" + msg + "%'  )");
		
		String ip = StringUtil.getHttpParam(request(), "ip");
		if(ip==null)
			ip = "";	
		if ( !( ip .equals("") ) && !( ip .equals("undefined") ) ) 
			sql.append(" and ( ip= '" + ip + "'  )");
		
		String export = StringUtil.getHttpParam(request(), "export");// 是否导出
		if(export!=null && export.equals("1")){
			nStart = 0;
			nLimit = 65534;
		}
		
		Logger.info(sql.toString());
		//下面简单的分页，需要重构，应该由 Dao-Sevice去实现。
		int rowCount = Ebean.getServer(GlobalDBControl.getDB()).createQuery(Syslog .class, sql.toString()).findRowCount();
		List<Syslog> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.createQuery(Syslog .class, sql.toString())
				.setFirstRow(nStart).setMaxRows(nLimit)
				.orderBy("idd desc")
				.findList();
				
		if(export!=null && export.equals("1")){
			String fileName = "Syslog"+simp.format( new Date())+".xls";
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
		PagedObject<Syslog> po= new PagedObject( ulist,nPages,rowCount );
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(po) );
	}

	//列表
	public static Result listAll(){
		List<Syslog> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.find(Syslog .class)
				.orderBy("idd")
				.findList();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(ulist) );
	}
	
	//删除
	public static Result delete(int idd){
		String sql = "delete from Syslog where idd =:idd";
		Ebean.getServer(GlobalDBControl.getDB())
				.createUpdate(Syslog .class, sql).setParameter("idd", idd).execute();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson("操作成功") );
	}
	
	//获取 单个
	public static Result get(int idd){
		Syslog syslog = Ebean.getServer(GlobalDBControl.getDB())
				.find(Syslog .class).where().eq("idd", idd).findUnique();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(syslog) );
	}

	//新增 / 修改
	public static Result modify(){
		String idd = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "idd");
		int nidd = StringTool.GetInt(idd, 0);
		
		
		
		
		
		String module = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "module");
		
		
		
		String fcode = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "fcode");
		
		
		
		String uri = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "uri");
		
		
		
		String user = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "user");
		
		
		
		String addtime = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "addtime");
		
		
		
		String msg = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "msg");
		
		
		
		String ip = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "ip");
		
		
		
		String operation = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "operation");
		
		Syslog syslog ;
		if(  operation.equals("add") )	// 新增
			syslog = new Syslog ();
		else			//修改
			syslog = Ebean.getServer(GlobalDBControl.getDB())
					.find(Syslog .class).where().eq("idd", idd).findUnique();
		
		if( syslog!=null ){
			//赋值,字符串类型可以直接赋值，其他类型需要转换 ！！！！
			
				 
				
			
				
					syslog. setModule ( module );
				 
				
			
				
					syslog. setFcode ( fcode );
				 
				
			
				
					syslog. setUri ( uri );
				 
				
			
				
					syslog. setUser ( user );
				 
				
			
				 
				
					syslog. setAddtime ( new Date() );
				
			
				
					syslog. setMsg ( msg );
				 
				
			
				
					syslog. setIp ( ip );
				 
				
			
			Ebean.getServer(GlobalDBControl.getDB()).save( syslog );
			response().setHeader("Cache-Control", "no-cache");
			return ok( Json.toJson(syslog) );
		}

		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(null) );
	}
	
	static int num=0;
	public static File exportList(List<Syslog> infoList,String fileNameChine) throws UnsupportedEncodingException {
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
		
		ExcelGenerateHelper helper = ExcelGenerateHelper.getInstance(wb);

		// 获取行索引
		int rowIndex = 0;
		int colIndex = 0;

		// 设置表头
		String[] titles = { 
				
					"idd"
					
				
					
					,"module"
				
					
					,"fcode"
				
					
					,"uri"
				
					
					,"user"
				
					
					,"addtime"
				
					
					,"msg"
				
					
					,"ip"
				
			};

		// 生成表头
		helper.setRowIndex(rowIndex++);
		helper.generateHeader(sheet, titles, 0, StringUtils.EMPTY,
				StringUtils.EMPTY);

		// 循环生成数据行
		for (int i = 0; i < infoList.size(); i++) {
			colIndex = 0;
			Syslog info = (Syslog) infoList.get(i);
			Row row = sheet.createRow(rowIndex++);
			
			
				helper.createStringCell( row , colIndex++, ""+info. getIdd () );
			
				helper.createStringCell( row , colIndex++, ""+info. getModule () );
			
				helper.createStringCell( row , colIndex++, ""+info. getFcode () );
			
				helper.createStringCell( row , colIndex++, ""+info. getUri () );
			
				helper.createStringCell( row , colIndex++, ""+info. getUser () );
			
				helper.createStringCell( row , colIndex++, ""+info. getAddtime () );
			
				helper.createStringCell( row , colIndex++, ""+info. getMsg () );
			
				helper.createStringCell( row , colIndex++, ""+info. getIp () );
			
		}
		//在application.conf中配置的路径
		String path = Configuration.root().getString("export.path");
		int numStra = num++;//静态值，区分文件名
		File file = new File(path);
		file.mkdir();//判断文件夹是否存在，不存在就创建
		
		FileOutputStream out = null;
		String fileName = path + "Syslog" + System.currentTimeMillis() + numStra + ".xls";
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