
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
public class SmartExchangesAction extends Controller {
  
	//进入页面列表；
	public static Result view() {
		return ok( SmartExchangesList.render() );
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
		sql.append("find SmartExchanges where 1=1 ");
		
		String idd = StringUtil.getHttpParam(request(), "idd");
		if(idd==null)
			idd = "";	
		if ( !( idd .equals("") ) && !( idd .equals("undefined") ) ) 
			sql.append(" and ( idd= '" + idd + "'  )");
		
		String userid = StringUtil.getHttpParam(request(), "userid");
		if(userid==null)
			userid = "";	
		if ( !( userid .equals("") ) && !( userid .equals("undefined") ) ) 
			sql.append(" and ( userid= '" + userid + "'  )");
		
		String account = StringUtil.getHttpParam(request(), "account");
		if(account==null)
			account = "";	
		if ( !( account .equals("") ) && !( account .equals("undefined") ) ) 
			sql.append(" and ( account= '" + account + "'  )");
		
		String pid = StringUtil.getHttpParam(request(), "pid");
		if(pid==null)
			pid = "";	
		if ( !( pid .equals("") ) && !( pid .equals("undefined") ) ) 
			sql.append(" and ( pid= '" + pid + "'  )");
		
		String bvalue = StringUtil.getHttpParam(request(), "bvalue");
		if(bvalue==null)
			bvalue = "";	
		if ( !( bvalue .equals("") ) && !( bvalue .equals("undefined") ) ) 
			sql.append(" and ( bvalue= '" + bvalue + "'  )");
		
		String addtime = StringUtil.getHttpParam(request(), "addtime");
		if(addtime==null)
			addtime = "";	
		if ( !( addtime .equals("") ) && !( addtime .equals("undefined") ) ) 
			sql.append(" and ( addtime= '" + addtime + "'  )");
		
		String acttime = StringUtil.getHttpParam(request(), "acttime");
		if(acttime==null)
			acttime = "";	
		if ( !( acttime .equals("") ) && !( acttime .equals("undefined") ) ) 
			sql.append(" and ( acttime= '" + acttime + "'  )");
		
		String status = StringUtil.getHttpParam(request(), "status");
		if(status==null)
			status = "";	
		if (!(status .equals(""))&&!(status.equals("undefined"))&&!(status.equals("0")))
			sql.append(" and ( status= '" + status + "'  )");
		
		String returninfo = StringUtil.getHttpParam(request(), "returninfo");
		if(returninfo==null)
			returninfo = "";	
		if ( !( returninfo .equals("") ) && !( returninfo .equals("undefined") ) ) 
			sql.append(" and ( returninfo= '" + returninfo + "'  )");
		
		String means = StringUtil.getHttpParam(request(), "means");
		if(means==null)
			means = "";	
		if ( !( means .equals("") ) && !( means .equals("undefined") ) ) 
			sql.append(" and ( means= '" + means + "'  )");
		
		String realmeans = StringUtil.getHttpParam(request(), "realmeans");
		if(realmeans==null)
			realmeans = "";	
		if ( !( realmeans .equals("") ) && !( realmeans .equals("undefined") ) ) 
			sql.append(" and ( realmeans= '" + realmeans + "'  )");
		
		String retid = StringUtil.getHttpParam(request(), "retid");
		if(retid==null)
			retid = "";	
		if ( !( retid .equals("") ) && !( retid .equals("undefined") ) ) 
			sql.append(" and ( retid= '" + retid + "'  )");
		
		String ip = StringUtil.getHttpParam(request(), "ip");
		if(ip==null)
			ip = "";	
		if ( !( ip .equals("") ) && !( ip .equals("undefined") ) ) 
			sql.append(" and ( ip= '" + ip + "'  )");
		
		String area = StringUtil.getHttpParam(request(), "area");
		if(area==null)
			area = "";	
		if ( !( area .equals("") ) && !( area .equals("undefined") ) ) 
			sql.append(" and ( area= '" + area + "'  )");
		
		String channel = StringUtil.getHttpParam(request(), "channel");
		if(channel==null)
			channel = "";	
		if ( !( channel .equals("") ) && !( channel .equals("undefined") ) ) 
			sql.append(" and ( channel= '" + channel + "'  )");
		
		String export = StringUtil.getHttpParam(request(), "export");// 是否导出
		if(export!=null && export.equals("1")){
			nStart = 0;
			nLimit = 65534;
		}
		
		Logger.info(sql.toString());
		//下面简单的分页，需要重构，应该由 Dao-Sevice去实现。
		int rowCount = Ebean.getServer(GlobalDBControl.getDB()).createQuery(SmartExchanges .class, sql.toString()).findRowCount();
		List<SmartExchanges> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.createQuery(SmartExchanges .class, sql.toString())
				.setFirstRow(nStart).setMaxRows(nLimit)
				.orderBy("idd desc")
				.findList();
				
		if(export!=null && export.equals("1")){
			String fileName = "SmartExchanges"+simp.format( new Date())+".xls";
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
		PagedObject<SmartExchanges> po= new PagedObject( ulist,nPages,rowCount );
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(po) );
	}

	//列表
	public static Result listAll(){
		List<SmartExchanges> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.find(SmartExchanges .class)
				.orderBy("idd")
				.findList();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(ulist) );
	}
	
	//删除
	public static Result delete(int idd){
		String sql = "delete from SmartExchanges where idd =:idd";
		Ebean.getServer(GlobalDBControl.getDB())
				.createUpdate(SmartExchanges .class, sql).setParameter("idd", idd).execute();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson("操作成功") );
	}
	
	//获取 单个
	public static Result get(int idd){
		SmartExchanges smartexchanges = Ebean.getServer(GlobalDBControl.getDB())
				.find(SmartExchanges .class).where().eq("idd", idd).findUnique();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(smartexchanges) );
	}

	//新增 / 修改
	public static Result modify(){
		String idd = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "idd");
		int nidd = StringTool.GetInt(idd, 0);
		
		String userid = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "userid");
		
		String account = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "account");
		
		String pid = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "pid");
		
		String bvalue = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "bvalue");
		
		String addtime = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "addtime");
		
		String acttime = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "acttime");
		
		String status = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "status");
		
		String returninfo = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "returninfo");
		
		String means = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "means");
		
		String realmeans = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "realmeans");
		
		String retid = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "retid");
		
		String ip = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "ip");
		
		String area = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "area");
		
		String channel = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "channel");
		
		String operation = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "operation");
		
		SmartExchanges smartexchanges ;
		if(  operation.equals("add") )	// 新增
			smartexchanges = new SmartExchanges ();
		else			//修改
			smartexchanges = Ebean.getServer(GlobalDBControl.getDB())
					.find(SmartExchanges .class).where().eq("idd", idd).findUnique();
		
		if( smartexchanges!=null ){
			//赋值,字符串类型可以直接赋值，其他类型需要转换 ！！！！
			smartexchanges. setUserid ( StringTool.GetInt(userid,0) );
			smartexchanges. setAccount ( account );
			smartexchanges. setPid ( StringTool.GetInt(pid,0) );
			smartexchanges. setBvalue ( StringTool.GetInt(bvalue,0) );
			smartexchanges. setAddtime ( StringUtil.getDate(addtime) );
			smartexchanges. setActtime ( StringUtil.getDate(acttime) );
			smartexchanges. setStatus ( StringTool.GetInt(status,0) );
			smartexchanges. setReturninfo ( returninfo );
			smartexchanges. setMeans ( means );
			smartexchanges. setRealmeans ( realmeans );
			smartexchanges. setRetid ( retid );
			smartexchanges. setIp ( ip );
			smartexchanges. setArea ( area );
			smartexchanges. setChannel ( channel );
			
			Ebean.getServer(GlobalDBControl.getDB()).save( smartexchanges );
			response().setHeader("Cache-Control", "no-cache");
			return ok( Json.toJson(smartexchanges) );
		}
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(null) );
	}
	
	static int num=0;
	public static File exportList(List<SmartExchanges> infoList,String fileNameChine) throws UnsupportedEncodingException {
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
		
			sheet.setColumnWidth( 14 , 6000 );
		
		ExcelGenerateHelper helper = ExcelGenerateHelper.getInstance(wb);

		// 获取行索引
		int rowIndex = 0;
		int colIndex = 0;

		// 设置表头
		String[] titles = { 
			"idd"
				
			,"userid"
			
			,"account"
			
			,"pid"
			
			,"bvalue"
			
			,"addtime"
			
			,"acttime"
			
			,"status"
			
			,"returninfo"
			
			,"means"
			
			,"realmeans"
		
			,"retid"
			
			,"ip"
		
			,"area"
			
			,"channel"
		};

		// 生成表头
		helper.setRowIndex(rowIndex++);
		helper.generateHeader(sheet, titles, 0, StringUtils.EMPTY,
				StringUtils.EMPTY);

		// 循环生成数据行
		for (int i = 0; i < infoList.size(); i++) {
			colIndex = 0;
			SmartExchanges info = (SmartExchanges) infoList.get(i);
			Row row = sheet.createRow(rowIndex++);
			
			helper.createStringCell( row , colIndex++, ""+info. getIdd () );
		
			helper.createStringCell( row , colIndex++, ""+info. getUserid () );
		
			helper.createStringCell( row , colIndex++, ""+info. getAccount () );
		
			helper.createStringCell( row , colIndex++, ""+info. getPid () );
		
			helper.createStringCell( row , colIndex++, ""+info. getBvalue () );
		
			helper.createStringCell( row , colIndex++, ""+info. getAddtime () );
		
			helper.createStringCell( row , colIndex++, ""+info. getActtime () );
		
			helper.createStringCell( row , colIndex++, ""+info. getStatus () );
		
			helper.createStringCell( row , colIndex++, ""+info. getReturninfo () );
		
			helper.createStringCell( row , colIndex++, ""+info. getMeans () );
		
			helper.createStringCell( row , colIndex++, ""+info. getRealmeans () );
		
			helper.createStringCell( row , colIndex++, ""+info. getRetid () );
		
			helper.createStringCell( row , colIndex++, ""+info. getIp () );
		
			helper.createStringCell( row , colIndex++, ""+info. getArea () );
		
			helper.createStringCell( row , colIndex++, ""+info. getChannel () );
			
		}
		//在application.conf中配置的路径
		String path = Configuration.root().getString("export.path");
		int numStra = num++;//静态值，区分文件名
		File file = new File(path);
		file.mkdir();//判断文件夹是否存在，不存在就创建
		
		FileOutputStream out = null;
		String fileName = path + "SmartExchanges" + System.currentTimeMillis() + numStra + ".xls";
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
			"userid"
			
			,"account"
			
			,"pid"
		
			,"bvalue"
			
			,"addtime"
			
			,"acttime"
			
			,"status"
		
			,"returninfo"
			
			,"means"
			
			,"realmeans"
			
			,"retid"
			
			,"ip"
		
			,"area"
		
			,"channel"
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
			for(int i=0;i < headers.length;i++){
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
			
			List<SmartExchanges> list = new ArrayList<SmartExchanges>();
			 for (int i = 1; i <= lastRowNumber; i++) {
				 //批量添加
                 Row row = sheet.getRow(i);
                 if (row == null) {
                     break;
                 }
                 
                 SmartExchanges fc = new SmartExchanges();
                 int j = 0 ;
					
				 fc. setUserid ( StringTool.GetInt(StringUtil.cellValueToString(row.getCell(j)),0) );
			
				 j=j+1;
				 fc. setAccount ( StringUtil.cellValueToString(row.getCell(j)) );
			 
				 j=j+1;
				 fc. setPid ( StringTool.GetInt(StringUtil.cellValueToString(row.getCell(j)),0) );
			
				 j=j+1;
				 fc. setBvalue ( StringTool.GetInt(StringUtil.cellValueToString(row.getCell(j)),0) );
			
				 j=j+1;
				 //fc. setAddtime ( StringUtil.getDate( StringUtil.cellValueToString(row.getCell(j) ) );
			
				 j=j+1;
				 //fc. setActtime ( StringUtil.getDate( StringUtil.cellValueToString(row.getCell(j) ) );
			
				 j=j+1;
				 fc. setStatus ( StringTool.GetInt(StringUtil.cellValueToString(row.getCell(j)),0) );
				
				 j=j+1;
				 fc. setReturninfo ( StringUtil.cellValueToString(row.getCell(j)) );
			
				 j=j+1;
				 fc. setMeans ( StringUtil.cellValueToString(row.getCell(j)) );
			
				 j=j+1;
				 fc. setRealmeans ( StringUtil.cellValueToString(row.getCell(j)) );
			
				 j=j+1;
				 fc. setRetid ( StringUtil.cellValueToString(row.getCell(j)) );
			
				 j=j+1;
				 fc. setIp ( StringUtil.cellValueToString(row.getCell(j)) );
			
				 j=j+1;
				 fc. setArea ( StringUtil.cellValueToString(row.getCell(j)) );
			
				 j=j+1;
				 fc. setChannel ( StringUtil.cellValueToString(row.getCell(j)) );
				
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
		for(int i=0;i < headers.length;i++){
			String cellValue = StringUtil.cellValueToString(row.getCell(i));
			if(cellValue==null||cellValue.trim().equals("")){
				index++;
			}
		}
		return index == headers.length;
	}
}