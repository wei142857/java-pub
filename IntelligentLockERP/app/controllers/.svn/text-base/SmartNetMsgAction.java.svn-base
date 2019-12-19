
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
import util.*;
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
public class SmartNetMsgAction extends Controller {
  
	//进入页面列表；
	public static Result view() {
		return ok( SmartNetMsgList.render(null) );
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
		sql.append("find SmartNetMsg where 1=1 ");
		
		String idd = StringUtil.getHttpParam(request(), "idd");
		if(idd==null)
			idd = "";	
		if ( !( idd .equals("") ) && !( idd .equals("undefined") ) ) 
			sql.append(" and ( idd= '" + idd + "'  )");
		
		String tag = StringUtil.getHttpParam(request(), "tag");
		if(tag==null)
			tag = "";	
		if ( !( tag .equals("") ) && !( tag .equals("undefined") ) ) 
			sql.append(" and ( tag= '" + tag + "'  )");
		
		String type = StringUtil.getHttpParam(request(), "type");
		if(type==null)
			type = "";	
		if ( !( type .equals("") ) && !( type .equals("undefined") ) ) 
			sql.append(" and ( type= '" + type + "'  )");
		
		String dt = StringUtil.getHttpParam(request(), "dt");
		if(dt==null)
			dt = "";	
		if ( !( dt .equals("") ) && !( dt .equals("undefined") ) ) 
			sql.append(" and ( dt= '" + dt + "'  )");
		
		String dealed = StringUtil.getHttpParam(request(), "dealed");
		if(dealed==null)
			dealed = "";	
		if ( !( dealed .equals("") ) && !( dealed .equals("undefined") ) ) 
			sql.append(" and ( dealed= '" + dealed + "'  )");
		
		String dealtime = StringUtil.getHttpParam(request(), "dealtime");
		if(dealtime==null)
			dealtime = "";	
		if ( !( dealtime .equals("") ) && !( dealtime .equals("undefined") ) ) 
			sql.append(" and ( dealtime= '" + dealtime + "'  )");
		
		String lockid = StringUtil.getHttpParam(request(), "lockid");
		if(lockid==null)
			lockid = "";	
		if ( !( lockid .equals("") ) && !( lockid .equals("undefined") ) ) 
			sql.append(" and ( lockid= '" + lockid + "'  )");
		
		String len = StringUtil.getHttpParam(request(), "len");
		if(len==null)
			len = "";	
		if ( !( len .equals("") ) && !( len .equals("undefined") ) ) 
			sql.append(" and ( len= '" + len + "'  )");
		
		String mid = StringUtil.getHttpParam(request(), "mid");
		if(mid==null)
			mid = "";	
		if ( !( mid .equals("") ) && !( mid .equals("undefined") ) ) 
			sql.append(" and ( mid= '" + mid + "'  )");
		
		String msg = StringUtil.getHttpParam(request(), "msg");
		if(msg==null)
			msg = "";	
		if ( !( msg .equals("") ) && !( msg .equals("undefined") ) ) 
			sql.append(" and ( msg= '" + msg + "'  )");
		
		String ext = StringUtil.getHttpParam(request(), "ext");
		if(ext==null)
			ext = "";	
		if ( !( ext .equals("") ) && !( ext .equals("undefined") ) ) 
			sql.append(" and ( ext= '" + ext + "'  )");
		
		String replytime = StringUtil.getHttpParam(request(), "replytime");
		if(replytime==null)
			replytime = "";	
		if ( !( replytime .equals("") ) && !( replytime .equals("undefined") ) ) 
			sql.append(" and ( replytime= '" + replytime + "'  )");
		
		String reply = StringUtil.getHttpParam(request(), "reply");
		if(reply==null)
			reply = "";	
		if ( !( reply .equals("") ) && !( reply .equals("undefined") ) ) 
			sql.append(" and ( reply= '" + reply + "'  )");
		
		String replymsg = StringUtil.getHttpParam(request(), "replymsg");
		if(replymsg==null)
			replymsg = "";	
		if ( !( replymsg .equals("") ) && !( replymsg .equals("undefined") ) ) 
			sql.append(" and ( replymsg= '" + replymsg + "'  )");
		
		String export = StringUtil.getHttpParam(request(), "export");// 是否导出
		if(export!=null && export.equals("1")){
			nStart = 0;
			nLimit = 65534;
		}
		
		Logger.info(sql.toString());
		//下面简单的分页，需要重构，应该由 Dao-Sevice去实现。
		int rowCount = Ebean.getServer(GlobalDBControl.getDB()).createQuery(SmartNetMsg .class, sql.toString()).findRowCount();
		List<SmartNetMsg> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.createQuery(SmartNetMsg .class, sql.toString())
				.setFirstRow(nStart).setMaxRows(nLimit)
				.orderBy("idd desc")
				.findList();
				
		if(export!=null && export.equals("1")){
			String fileName = "SmartNetMsg"+simp.format( new Date())+".xls";
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
		PagedObject<SmartNetMsg> po= new PagedObject( ulist,nPages,rowCount );
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(po) );
	}

	//列表
	public static Result listAll(){
		List<SmartNetMsg> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.find(SmartNetMsg .class)
				.orderBy("idd")
				.findList();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(ulist) );
	}
	
	//删除
	public static Result delete(int idd){
		String sql = "delete from SmartNetMsg where idd =:idd";
		Ebean.getServer(GlobalDBControl.getDB())
				.createUpdate(SmartNetMsg .class, sql).setParameter("idd", idd).execute();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson("操作成功") );
	}
	
	//获取 单个
	public static Result get(int idd){
		SmartNetMsg smartnetmsg = Ebean.getServer(GlobalDBControl.getDB())
				.find(SmartNetMsg .class).where().eq("idd", idd).findUnique();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(smartnetmsg) );
	}

	//新增 / 修改
	public static Result modify(){
		String idd = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "idd");
		int nidd = StringTool.GetInt(idd, 0);
		
		
		
		
		
		String tag = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "tag");
		
		
		
		String type = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "type");
		
		
		
		String dt = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "dt");
		
		
		
		String dealed = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "dealed");
		
		
		
		String dealtime = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "dealtime");
		
		
		
		String lockid = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "lockid");
		
		
		
		String len = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "len");
		
		
		
		String mid = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "mid");
		
		
		
		String msg = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "msg");
		
		
		
		String ext = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "ext");
		
		
		
		String replytime = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "replytime");
		
		
		
		String reply = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "reply");
		
		
		
		String replymsg = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "replymsg");
		
		
		
		String operation = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "operation");
		
		SmartNetMsg smartnetmsg ;
		if(  operation.equals("add") )	// 新增
			smartnetmsg = new SmartNetMsg ();
		else			//修改
			smartnetmsg = Ebean.getServer(GlobalDBControl.getDB())
					.find(SmartNetMsg .class).where().eq("idd", idd).findUnique();
		
		if( smartnetmsg!=null ){
			//赋值,字符串类型可以直接赋值，其他类型需要转换 ！！！！
			
				 
				
				
			
				 
				
				
					smartnetmsg. setTag ( StringTool.GetInt(tag,0) );
				
			
				 
				
				
					smartnetmsg. setType ( StringTool.GetInt(type,0) );
				
			
				 
				
					smartnetmsg. setDt ( StringUtil.getDate(dt) );
				
				
			
				 
				
				
					smartnetmsg. setDealed ( StringTool.GetInt(dealed,0) );
				
			
				 
				
					smartnetmsg. setDealtime ( StringUtil.getDate(dealtime) );
				
				
			
				
					smartnetmsg. setLockid ( lockid );
				 
				
				
			
				 
				
				
					smartnetmsg. setLen ( StringTool.GetInt(len,0) );
				
			
				 
				
				
					smartnetmsg. setMid ( StringTool.GetInt(mid,0) );
				
			
				
					smartnetmsg. setMsg ( msg );
				 
				
				
			
				
					smartnetmsg. setExt ( ext );
				 
				
				
			
				 
				
					smartnetmsg. setReplytime ( StringUtil.getDate(replytime) );
				
				
			
				 
				
				
					smartnetmsg. setReply ( StringTool.GetInt(reply,0) );
				
			
				
					smartnetmsg. setReplymsg ( replymsg );
				 
				
				
			
			Ebean.getServer(GlobalDBControl.getDB()).save( smartnetmsg );
			response().setHeader("Cache-Control", "no-cache");
			return ok( Json.toJson(smartnetmsg) );
		}

		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(null) );
	}
	
	static int num=0;
	public static File exportList(List<SmartNetMsg> infoList,String fileNameChine) throws UnsupportedEncodingException {
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
		
			sheet.setColumnWidth( 12 , 6000 );
		
			sheet.setColumnWidth( 13 , 6000 );
		
		ExcelGenerateHelper helper = ExcelGenerateHelper.getInstance(wb);

		// 获取行索引
		int rowIndex = 0;
		int colIndex = 0;

		// 设置表头
		String[] titles = { 
				
					"ID"
					
				
					
					,"通信标识"
				
					
					,"类型"
				
					
					,"时间"
				
					
					,"是否处理"
				
					
					,"注册时间"
				
					
					,"锁"
				
					
					,"长度"
				
					
					,"传输ID"
				
					
					,"内容"
				
					
					,"说明"
				
					
					,"回复时间"
				
					
					,"回复的结果"
				
					
					,"回复的消息"
				
			};

		// 生成表头
		helper.setRowIndex(rowIndex++);
		helper.generateHeader(sheet, titles, 0, StringUtils.EMPTY,
				StringUtils.EMPTY);

		// 循环生成数据行
		for (int i = 0; i < infoList.size(); i++) {
			colIndex = 0;
			SmartNetMsg info = (SmartNetMsg) infoList.get(i);
			Row row = sheet.createRow(rowIndex++);
			
			
				helper.createStringCell( row , colIndex++, ""+info. getIdd () );
			
				helper.createStringCell( row , colIndex++, ""+info. getTag () );
			
				helper.createStringCell( row , colIndex++, ""+info. getType () );
			
				helper.createStringCell( row , colIndex++, ""+info. getDt () );
			
				helper.createStringCell( row , colIndex++, ""+info. getDealed () );
			
				helper.createStringCell( row , colIndex++, ""+info. getDealtime () );
			
				helper.createStringCell( row , colIndex++, ""+info. getLockid () );
			
				helper.createStringCell( row , colIndex++, ""+info. getLen () );
			
				helper.createStringCell( row , colIndex++, ""+info. getMid () );
			
				helper.createStringCell( row , colIndex++, ""+info. getMsg () );
			
				helper.createStringCell( row , colIndex++, ""+info. getExt () );
			
				helper.createStringCell( row , colIndex++, ""+info. getReplytime () );
			
				helper.createStringCell( row , colIndex++, ""+info. getReply () );
			
				helper.createStringCell( row , colIndex++, ""+info. getReplymsg () );
			
		}
		//在application.conf中配置的路径
		String path = Configuration.root().getString("export.path");
		int numStra = num++;//静态值，区分文件名
		File file = new File(path);
		file.mkdir();//判断文件夹是否存在，不存在就创建
		
		FileOutputStream out = null;
		String fileName = path + "SmartNetMsg" + System.currentTimeMillis() + numStra + ".xls";
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
	
	static String[]  headers = {
				
					
					
				
					"通信标识"
					
				
					
					,"类型"
				
					
					,"时间"
				
					
					,"是否处理"
				
					
					,"注册时间"
				
					
					,"锁"
				
					
					,"长度"
				
					
					,"传输ID"
				
					
					,"内容"
				
					
					,"说明"
				
					
					,"回复时间"
				
					
					,"回复的结果"
				
					
					,"回复的消息"
				
		};
	static int MAX_LINES = 10000;
	public static Result upload(){
		String cid = AjaxHellper.getHttpParam(request(), "uploadidd");
		Integer ncid;
		int successNum = 0; //导入成功的数量
		MultipartFormData body = request().body().asMultipartFormData();
		FilePart file_excel = body.getFile("file_excel");
		
		if (file_excel != null) {
			String fileName = file_excel.getFilename();
			int rowNum = 0;//导入Excel表里的有效记录数
			File file = file_excel.getFile();
			
			File fileTemp = new File(ExcelUtils.getUploadPath());
			fileTemp.mkdir();
			
			String uploadFileName = ExcelUtils.getUploadPath() + ExcelUtils.addTimeTagFileName(fileName);
			File destFile = ExcelUtils.createFile(uploadFileName);
			
			ExcelUtils.copy(file, destFile);
			file.delete();//将临时文件删除掉
			
			Workbook workBook = null;
			FileInputStream fis = null;
			
			try {
				fis = new FileInputStream(new File(uploadFileName));
			} catch (FileNotFoundException e) {
				return ok(Json.toJson(successNum));
			}

			try {
				workBook = WorkbookFactory.create(fis);
			} catch (Exception e) {
				Logger.info(e.toString());
				return ok(Json.toJson(-1));
			} 
			
			Sheet sheet = workBook.getSheetAt(0);
			int lastRowNumber = sheet.getLastRowNum();
			Row rowTitle = sheet.getRow(0);
			if(rowTitle==null){
				return ok(Json.toJson(successNum));
			}
			// 表头
			for(int i=0;i<headers.length;i++){
				String cellValue = StringUtil.cellValueToString(rowTitle.getCell(i));
				if(!cellValue.equalsIgnoreCase(headers[i])){
					
				}
			}
			for (int i = 1; i <= lastRowNumber; i++) {
				Row row = sheet.getRow(i);
				if (checkNullOrNot(row)) {
					break;
				}
				rowNum++;
			}
			
			if( rowNum > MAX_LINES )
				return ok( Json.toJson(successNum) );
			
			List<SmartNetMsg> list = new ArrayList<SmartNetMsg>();
			 for (int i = 1; i <= lastRowNumber; i++) {
				 //批量添加
                 Row row = sheet.getRow(i);
                 if (row == null) {
                     break;
                 }
                 
                 SmartNetMsg fc = new SmartNetMsg();
                 int j = 0 ;
                 
					 
					
					
					
				
					 
					
					
						fc. setTag ( StringTool.GetInt(StringUtil.cellValueToString(row.getCell(j)),0) );
					
					
						j=j+1;
					
				
					 
					
					
						fc. setType ( StringTool.GetInt(StringUtil.cellValueToString(row.getCell(j)),0) );
					
					
						j=j+1;
					
				
					 
					
						//fc. setDt ( StringUtil.getDate( StringUtil.cellValueToString(row.getCell(j) ) );
					
					
					
						j=j+1;
					
				
					 
					
					
						fc. setDealed ( StringTool.GetInt(StringUtil.cellValueToString(row.getCell(j)),0) );
					
					
						j=j+1;
					
				
					 
					
						//fc. setDealtime ( StringUtil.getDate( StringUtil.cellValueToString(row.getCell(j) ) );
					
					
					
						j=j+1;
					
				
					
						fc. setLockid ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					 
					
					
						fc. setLen ( StringTool.GetInt(StringUtil.cellValueToString(row.getCell(j)),0) );
					
					
						j=j+1;
					
				
					 
					
					
						fc. setMid ( StringTool.GetInt(StringUtil.cellValueToString(row.getCell(j)),0) );
					
					
						j=j+1;
					
				
					
						fc. setMsg ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					
						fc. setExt ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					 
					
						//fc. setReplytime ( StringUtil.getDate( StringUtil.cellValueToString(row.getCell(j) ) );
					
					
					
						j=j+1;
					
				
					 
					
					
						fc. setReply ( StringTool.GetInt(StringUtil.cellValueToString(row.getCell(j)),0) );
					
					
						j=j+1;
					
				
					
						fc. setReplymsg ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
                 list.add(fc);
                 if( list.size()>90 ){
                	 Ebean.getServer(GlobalDBControl.getDB()).save(list);
                	 list.clear();
                 }
			 }
			 
			 if( list.size()>0 ){
				 Ebean.getServer(GlobalDBControl.getDB()).save(list);
				 list.clear();
			 }
			 
			 successNum = Ebean.getServer(GlobalDBControl.getDB()).save(list);
			 return ok(Json.toJson(successNum));
		}
	
		return ok(Json.toJson(successNum));
	}
	
	//检查是否是一个完全的空行
	public static boolean checkNullOrNot(Row row){
		if(row==null)
			return true;
		int index=0;
		for(int i=0;i<headers.length;i++){
			String cellValue = StringUtil.cellValueToString(row.getCell(i));
			if(cellValue==null||cellValue.trim().equals("")){
				index++;
			}
		}
		return index == headers.length;
	}
	
}