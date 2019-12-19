
package controllers;

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
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
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
import views.html.page.*;
import util.classEntity.*;

@Security.Authenticated(Secured.class)
public class SubCardsAction extends Controller {
  
	//进入页面列表；
	public static Result view() {
		return ok( SubCardsList.render() );
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
		sql.append("find SubCards where 1=1 ");
		
		String idd = StringUtil.getHttpParam(request(), "idd");
		if(idd==null)
			idd = "";	
		if ( !( idd .equals("") ) && !( idd .equals("undefined") ) ) 
			sql.append(" and ( idd= '" + idd + "'  )");
		
		String pass = StringUtil.getHttpParam(request(), "pass");
		if(pass==null)
			pass = "";	
		if ( !( pass .equals("") ) && !( pass .equals("undefined") ) ) 
			sql.append(" and ( pass= '" + pass + "'  )");
		
		String addtime = StringUtil.getHttpParam(request(), "addtime");
		if(addtime==null)
			addtime = "";	
		if ( !( addtime .equals("") ) && !( addtime .equals("undefined") ) ) 
			sql.append(" and ( addtime= '" + addtime + "'  )");
		
		String status = StringUtil.getHttpParam(request(), "status");
		if(status==null)
			status = "";	
		if ( !( status .equals("") ) && !( status .equals("undefined") ) ) 
			sql.append(" and ( status= '" + status + "'  )");
		
		String export = StringUtil.getHttpParam(request(), "export");// 是否导出
		if(export!=null && export.equals("1")){
			nStart = 0;
			nLimit = 65534;
		}
		
		Logger.info(sql.toString());
		//下面简单的分页，需要重构，应该由 Dao-Sevice去实现。
		int rowCount = Ebean.getServer(GlobalDBControl.getDB()).createQuery(SubCards .class, sql.toString()).findRowCount();
		List<SubCards> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.createQuery(SubCards .class, sql.toString())
				.setFirstRow(nStart).setMaxRows(nLimit)
				.orderBy("idd desc")
				.findList();
				
		if(export!=null && export.equals("1")){
			String fileName = "SubCards"+simp.format( new Date())+".xls";
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
		if(export!=null && export.equals("2")){
			String fileName = "SubCards"+simp.format( new Date())+".xls";
			File file2=null;
			try{
				file2=  downLoadWorkFormatFile(fileName);
			}catch(Exception e)
			{
				Logger.info("export file:"+e.toString());
			}

			return ok(file2);
		}
		
		int nPages = rowCount/nLimit;
		if( rowCount%nLimit>0 )
			nPages ++;
		PagedObject<SubCards> po= new PagedObject( ulist,nPages,rowCount );
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(po) );
	}

	//列表
	public static Result listAll(){
		List<SubCards> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.find(SubCards .class)
				.orderBy("idd")
				.findList();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(ulist) );
	}
	
	//删除
	public static Result delete(int idd){
		String sql = "delete from SubCards where idd =:idd";
		Ebean.getServer(GlobalDBControl.getDB())
				.createUpdate(SubCards .class, sql).setParameter("idd", idd).execute();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson("操作成功") );
	}
	
	//获取 单个
	public static Result get(int idd){
		SubCards subcards = Ebean.getServer(GlobalDBControl.getDB())
				.find(SubCards .class).where().eq("idd", idd).findUnique();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(subcards) );
	}

	//新增 / 修改
	public static Result modify(){
		String idd = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "idd");
		int nidd = StringTool.GetInt(idd, 0);
		
		
		
		
		
		String pass = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "pass");
		
		
		
		String addtime = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "addtime");
		
		
		
		String status = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "status");
		
		
		
		String operation = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "operation");
		
		SubCards subcards ;
		if(  operation.equals("add") )	// 新增
			subcards = new SubCards ();
		else			//修改
			subcards = Ebean.getServer(GlobalDBControl.getDB())
					.find(SubCards .class).where().eq("idd", idd).findUnique();
		
		if( subcards!=null ){
			//赋值,字符串类型可以直接赋值，其他类型需要转换 ！！！！
			
				 
				
				
			
				
					subcards. setPass ( pass );
				 
				
				
			
				 
				
					subcards. setAddtime ( StringUtil.getDate(addtime) );
				
				
			
				 
				
				
					subcards. setStatus ( StringTool.GetInt(status,0) );
				
			
			Ebean.getServer(GlobalDBControl.getDB()).save( subcards );
			response().setHeader("Cache-Control", "no-cache");
			return ok( Json.toJson(subcards) );
		}

		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(null) );
	}
	
	static int num=0;
	public static File exportList(List<SubCards> infoList,String fileNameChine) throws UnsupportedEncodingException {
		Workbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet("sheet1");
		
			sheet.setColumnWidth( 0 , 6000 );
		
			sheet.setColumnWidth( 1 , 6000 );
		
			sheet.setColumnWidth( 2 , 6000 );
		
			sheet.setColumnWidth( 3 , 6000 );
		
		ExcelGenerateHelper helper = ExcelGenerateHelper.getInstance(wb);

		// 获取行索引
		int rowIndex = 0;
		int colIndex = 0;

		// 设置表头
		String[] titles = { 
				
					"idd"
					
				
					
					,"兑换码"
				
					
					,"添加时间"
				
					
					,"状态"
				
			};

		// 生成表头
		helper.setRowIndex(rowIndex++);
		helper.generateHeader(sheet, titles, 0, StringUtils.EMPTY,
				StringUtils.EMPTY);

		// 循环生成数据行
		for (int i = 0; i < infoList.size(); i++) {
			colIndex = 0;
			SubCards info = (SubCards) infoList.get(i);
			Row row = sheet.createRow(rowIndex++);
			
			
				helper.createStringCell( row , colIndex++, ""+info. getIdd () );
			
				helper.createStringCell( row , colIndex++, ""+info. getPass () );
			
				helper.createStringCell( row , colIndex++, ""+info. getAddtime () );
			
				helper.createStringCell( row , colIndex++, ""+info. getStatus () );
			
		}
		//在application.conf中配置的路径
		String path = Configuration.root().getString("export.path");
		int numStra = num++;//静态值，区分文件名
		File file = new File(path);
		file.mkdir();//判断文件夹是否存在，不存在就创建
		
		FileOutputStream out = null;
		String fileName = path + "SubCards" + System.currentTimeMillis() + numStra + ".xls";
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
				
					
					
				
					"兑换码"
					
				
					
					,"添加时间"
				
					
					,"状态"
				
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
			
			List<SubCards> list = new ArrayList<SubCards>();
			 for (int i = 1; i <= lastRowNumber; i++) {
				 //批量添加
                 Row row = sheet.getRow(i);
                 if (row == null) {
                     break;
                 }
                 String pass = StringUtil.cellValueToString(row.getCell(0));
                 int rcount = Ebean.getServer(GlobalDBControl.getDB()).createQuery(SubCards.class, "find SubCards where pass='"+pass+"'").findRowCount();
                 if(pass.trim().equals("")||rcount!=0) {
          			 continue;
          		 }
                 SubCards fc = new SubCards();
                 
                 
                 
                 
                 
                 
                 
                 
                 
                 
                 
                 
                 
                 
                 
                 
                 
                 
                 
                 
				 fc. setPass ( pass	 );
				 fc. setAddtime (new Date());
				 fc. setStatus (1);
				 
				 
				 
				 
				 
				 
				 
				 
				 
				 
				 
				 
				 
				 
				 
				 
				 
				 
				 
				 
				
                 list.add(fc);
                 if( list.size()>90 ){
                	 Ebean.getServer(GlobalDBControl.getDB()).save(list);
                	 list.clear();
                 }
			 }
			 
			 if( list.size()>0 ){
				 successNum = Ebean.getServer(GlobalDBControl.getDB()).save(list);
				 list.clear();
			 }
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
	// 设置表头
		static String[] titles = { 
					"兑换码"
		};
		public static File downLoadWorkFormatFile(String fileNameChine)throws UnsupportedEncodingException {
			//创建工作簿
			HSSFWorkbook wb = new HSSFWorkbook();
			//获取表头样式对象
			HSSFCellStyle style = wb.createCellStyle();
			//居中格式
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			//获取字体样式对象
			HSSFFont fontStyle = wb.createFont(); 
	        fontStyle.setFontName("微软雅黑");    
	        fontStyle.setFontHeightInPoints((short)12);    
	        fontStyle.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); 
	        style.setFont(fontStyle);
	        
	        //新建sheet
	        HSSFSheet sheet1 = wb.createSheet("Sheet1");
	        
	        //生成sheet1内容
	        HSSFRow rowFirst = sheet1.createRow(0);//第一个sheet的第一行为标题
	        //写标题
	        for(int i=0;i<titles.length;i++){
	            HSSFCell cell = rowFirst.createCell(i); //获取第一行的每个单元格
	            sheet1.setColumnWidth(i, 6000); //设置每列的列宽
	            cell.setCellStyle(style); //加样式
	            cell.setCellValue(titles[i]); //往单元格里写数据
	        }
	        
	        //遍历所有商品供用户在Excel中选择
	        List<IntelligentLockProduct> findList = Ebean.getServer(GlobalDBControl.getDB()).createQuery(IntelligentLockProduct.class, "find intelligentlock_product where level!=0 and status!=1").findList();
	        String[] dlData = new String[findList.size()];//将商品名称添加到数组中
	        for (int i = 0; i < findList.size(); i++) {
	        	IntelligentLockProduct intelligentLockProduct = findList.get(i);
	        	String title = intelligentLockProduct.getTitle();
	        	dlData[i] = title;
			}
	        
	        ExcelGenerateHelper helper = ExcelGenerateHelper.getInstance(wb);
	        
	        // 获取行索引
	 		int rowIndex = 0;
	 		int colIndex = 0;
	 		// 生成表头
			helper.setRowIndex(rowIndex++);
			helper.generateHeader(sheet1, titles, 0, StringUtils.EMPTY,
					StringUtils.EMPTY);
			//在application.conf中配置的路径
			String path = Configuration.root().getString("export.path");
			int numStra = num++;//静态值，区分文件名
			File file = new File(path);
			file.mkdir();//判断文件夹是否存在，不存在就创建
			
			FileOutputStream out = null;
			String fileName = path + "IntelligentLockIn" + System.currentTimeMillis() + numStra + ".xls";
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