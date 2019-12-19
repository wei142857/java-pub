
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
public class IntelligentLockStockOutWaterAction extends Controller {
  
	//进入页面列表；
	public static Result view() {
		return ok( IntelligentLockStockOutWaterList.render() );
	}
	
	/**************************************************************	
	 *                增删改查的功能   ： AJAX方式
	 */	
	 private static SimpleDateFormat simp=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//列表
	public static Result list(){
		//获取前端grid 传递过来的参数：分页，搜索
		String start = StringUtil.getHttpParam(request(), "start");
		String limit = StringUtil.getHttpParam(request(), "limit");
		int nStart = StringTool.GetInt(start, 0);
		int nLimit = StringTool.GetInt(limit, 10);

		StringBuffer sql = new StringBuffer();
		sql.append("find IntelligentLockStockOutWater where 1=1 ");
		
		String idd = StringUtil.getHttpParam(request(), "idd");
		if(idd==null)
			idd = "";	
		if ( !( idd .equals("") ) && !( idd .equals("undefined") ) ) 
			sql.append(" and ( idd= '" + idd + "'  )");
		
		String outid = StringUtil.getHttpParam(request(), "outid");
		if(outid==null)
			outid = "";	
		if ( !( outid .equals("") ) && !( outid .equals("undefined") ) ) 
			sql.append(" and ( outid= '" + outid + "'  )");
		
		String trascatnumber = StringUtil.getHttpParam(request(), "trascatnumber");
		if(trascatnumber==null)
			trascatnumber = "";	
		if ( !( trascatnumber .equals("") ) && !( trascatnumber .equals("undefined") ) ) 
			sql.append(" and ( trascatnumber= '" + trascatnumber + "'  )");
		
		String productid = StringUtil.getHttpParam(request(), "productid");
		if(productid==null)
			productid = "";	
		if ( !( productid .equals("") ) && !( productid .equals("undefined") ) ) 
			sql.append(" and ( productid= '" + productid + "'  )");
		
		String batchid = StringUtil.getHttpParam(request(), "batchid");
		if(batchid==null)
			batchid = "";	
		if ( !( batchid .equals("") ) && !( batchid .equals("undefined") ) ) 
			sql.append(" and ( batchid= '" + batchid + "'  )");
		
		String operator = StringUtil.getHttpParam(request(), "operator");
		if(operator==null)
			operator = "";	
		if ( !( operator .equals("") ) && !( operator .equals("undefined") ) ) 
			sql.append(" and ( operator= '" + operator + "'  )");
		
		/*String addtime = StringUtil.getHttpParam(request(), "addtime");
		if(addtime==null)
			addtime = "";	
		if ( !( addtime .equals("") ) && !( addtime .equals("undefined") ) ) 
			sql.append(" and ( addtime= '" + addtime + "'  )");*/
		
		String operatornumber = StringUtil.getHttpParam(request(), "operatornumber");
		if(operatornumber==null)
			operatornumber = "";	
		if ( !( operatornumber .equals("") ) && !( operatornumber .equals("undefined") ) ) 
			sql.append(" and ( operatornumber= '" + operatornumber + "'  )");
		
		String export = StringUtil.getHttpParam(request(), "export");// 是否导出
		if(export!=null && export.equals("1")){
			nStart = 0;
			nLimit = 65534;
		}
		
		Logger.info(sql.toString());
		//下面简单的分页，需要重构，应该由 Dao-Sevice去实现。
		int rowCount = Ebean.getServer(GlobalDBControl.getDB()).createQuery(IntelligentLockStockOutWater .class, sql.toString()).findRowCount();
		List<IntelligentLockStockOutWater> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.createQuery(IntelligentLockStockOutWater .class, sql.toString())
				.setFirstRow(nStart).setMaxRows(nLimit)
				.orderBy("idd desc")
				.findList();
				
		if(export!=null && export.equals("1")){
			String fileName = "出库流水"+simp.format( new Date())+".xls";
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
		PagedObject<IntelligentLockStockOutWater> po= new PagedObject( ulist,nPages,rowCount );
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(po) );
	}

	//列表
	public static Result listAll(){
		List<IntelligentLockStockOutWater> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.find(IntelligentLockStockOutWater .class)
				.orderBy("idd")
				.findList();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(ulist) );
	}
	
	//删除
	public static Result delete(int idd){
		String sql = "delete from IntelligentLockStockOutWater where idd =:idd";
		Ebean.getServer(GlobalDBControl.getDB())
				.createUpdate(IntelligentLockStockOutWater .class, sql).setParameter("idd", idd).execute();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson("操作成功") );
	}
	
	//获取 单个
	/*public static Result get(int idd){
		IntelligentLockStockOutWater intelligentlockstockoutwater = Ebean.getServer(GlobalDBControl.getDB())
				.find(IntelligentLockStockOutWater .class).where().eq("idd", idd).findUnique();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(intelligentlockstockoutwater) );
	}

	//新增 / 修改
	public static Result modify(){
		String idd = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "idd");
		int nidd = StringTool.GetInt(idd, 0);
		
		String outid = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "outid");
		
		String trascatnumber = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "trascatnumber");
		
		String productid = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "productid");
		
		String batchid = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "batchid");
		
		String operator = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "operator");
		
		String addtime = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "addtime");
		
		String operatornumber = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "operatornumber");
		
		String operation = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "operation");
		
		IntelligentLockStockOutWater intelligentlockstockoutwater ;
		if(  operation.equals("add") )	// 新增
			intelligentlockstockoutwater = new IntelligentLockStockOutWater ();
		else			//修改
			intelligentlockstockoutwater = Ebean.getServer(GlobalDBControl.getDB())
					.find(IntelligentLockStockOutWater .class).where().eq("idd", idd).findUnique();
		
		if( intelligentlockstockoutwater!=null ){
			//赋值,字符串类型可以直接赋值，其他类型需要转换 ！！！！
			
			intelligentlockstockoutwater. setOutid ( StringTool.GetInt(outid,0) );
		
			intelligentlockstockoutwater. setTrascatnumber ( StringTool.GetInt(trascatnumber,0) );
		
			intelligentlockstockoutwater. setProductid ( StringTool.GetInt(productid,0) );
		
			intelligentlockstockoutwater. setBatchid ( batchid );
		 
			intelligentlockstockoutwater. setOperator ( operator );
		
			intelligentlockstockoutwater. setAddtime ( StringUtil.getDate(addtime) );
		
			intelligentlockstockoutwater. setOperatornumber ( operatornumber );
		
			Ebean.getServer(GlobalDBControl.getDB()).save( intelligentlockstockoutwater );
			response().setHeader("Cache-Control", "no-cache");
			return ok( Json.toJson(intelligentlockstockoutwater) );
		}
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(null) );
	}*/
	
	static int num=0;
	public static File exportList(List<IntelligentLockStockOutWater> infoList,String fileNameChine) throws UnsupportedEncodingException {
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
			
			,"出库单ID"
			
			,"本次交易数"
			
			,"产品ID"
			
			,"生产批次"
			
			,"操作人"
			
			,"添加时间"
			
			,"操作号"
		};
		// 生成表头
		helper.setRowIndex(rowIndex++);
		helper.generateHeader(sheet, titles, 0, StringUtils.EMPTY,
				StringUtils.EMPTY);

		// 循环生成数据行
		for (int i = 0; i < infoList.size(); i++) {
			colIndex = 0;
			IntelligentLockStockOutWater info = (IntelligentLockStockOutWater) infoList.get(i);
			Row row = sheet.createRow(rowIndex++);
			
			helper.createStringCell( row , colIndex++, ""+info. getIdd () );
		
			helper.createStringCell( row , colIndex++, ""+info. getOutid () );
		
			helper.createStringCell( row , colIndex++, ""+info. getTrascatnumber () );
		
			helper.createStringCell( row , colIndex++, ""+info. getProductid () );
		
			helper.createStringCell( row , colIndex++, ""+info. getBatchid () );
		
			helper.createStringCell( row , colIndex++, ""+info. getOperator () );
		
			helper.createStringCell( row , colIndex++, ""+simp.format(info. getAddtime ()) );
		
			helper.createStringCell( row , colIndex++, ""+info. getOperatornumber () );
		}
		//在application.conf中配置的路径
		String path = Configuration.root().getString("export.path");
		int numStra = num++;//静态值，区分文件名
		File file = new File(path);
		file.mkdir();//判断文件夹是否存在，不存在就创建
		
		FileOutputStream out = null;
		String fileName = path + "出库流水" + System.currentTimeMillis() + numStra + ".xls";
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
	
	/*static String[]  headers = {
		"出库单ID"
		
		,"本次交易数"
		
		,"产品ID"
		
		,"生产批次"
		
		,"操作人"
		
		,"添加时间"
	
		,"操作号"
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
			
			List<IntelligentLockStockOutWater> list = new ArrayList<IntelligentLockStockOutWater>();
			 for (int i = 1; i <= lastRowNumber; i++) {
				 //批量添加
                 Row row = sheet.getRow(i);
                 if (row == null) {
                     break;
                 }
                 
                 IntelligentLockStockOutWater fc = new IntelligentLockStockOutWater();
                 int j = 0 ;
                 
						fc. setOutid ( StringTool.GetInt(StringUtil.cellValueToString(row.getCell(j)),0) );
					
						j=j+1;
					
						fc. setTrascatnumber ( StringTool.GetInt(StringUtil.cellValueToString(row.getCell(j)),0) );
					
						j=j+1;
					
						fc. setProductid ( StringTool.GetInt(StringUtil.cellValueToString(row.getCell(j)),0) );
					
						j=j+1;
					
						fc. setBatchid (StringUtil.cellValueToString(row.getCell(j)) );
					
						j=j+1;
					
						fc. setOperator ( StringUtil.cellValueToString(row.getCell(j)) );
					
						j=j+1;
					
						//fc. setAddtime ( StringUtil.getDate( StringUtil.cellValueToString(row.getCell(j) ) );
					
						j=j+1;
					
						fc. setOperatornumber ( StringUtil.cellValueToString(row.getCell(j)) );
					
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
	}*/
	
}