
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
public class SmartDeviceTypeAction extends Controller {
  
	//进入页面列表；
	public static Result view() {
		return ok( SmartDeviceTypeList.render(null) );
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
		sql.append("find SmartDeviceType where 1=1 ");
		
		String idd = StringUtil.getHttpParam(request(), "idd");
		if(idd==null)
			idd = "";	
		if ( !( idd .equals("") ) && !( idd .equals("undefined") ) ) 
			sql.append(" and ( idd= '" + idd + "'  )");
		
		String name = StringUtil.getHttpParam(request(), "name");
		if(name==null)
			name = "";	
		if ( !( name .equals("") ) && !( name .equals("undefined") ) ) 
			sql.append(" and ( name= '" + name + "'  )");
		
		String catagary = StringUtil.getHttpParam(request(), "catagary");
		if(catagary==null)
			catagary = "";	
		if ( !( catagary .equals("") ) && !( catagary .equals("undefined") ) ) 
			sql.append(" and ( catagary= '" + catagary + "'  )");
		
		String logo = StringUtil.getHttpParam(request(), "logo");
		if(logo==null)
			logo = "";	
		if ( !( logo .equals("") ) && !( logo .equals("undefined") ) ) 
			sql.append(" and ( logo= '" + logo + "'  )");
		
		String image = StringUtil.getHttpParam(request(), "image");
		if(image==null)
			image = "";	
		if ( !( image .equals("") ) && !( image .equals("undefined") ) ) 
			sql.append(" and ( image= '" + image + "'  )");
		
		String descinfo = StringUtil.getHttpParam(request(), "descinfo");
		if(descinfo==null)
			descinfo = "";	
		if ( !( descinfo .equals("") ) && !( descinfo .equals("undefined") ) ) 
			sql.append(" and ( descinfo= '" + descinfo + "'  )");
		
		String imageLink = StringUtil.getHttpParam(request(), "imageLink");
		if(imageLink==null)
			imageLink = "";	
		if ( !( imageLink .equals("") ) && !( imageLink .equals("undefined") ) ) 
			sql.append(" and ( imageLink= '" + imageLink + "'  )");
		
		String imageOp1 = StringUtil.getHttpParam(request(), "imageOp1");
		if(imageOp1==null)
			imageOp1 = "";	
		if ( !( imageOp1 .equals("") ) && !( imageOp1 .equals("undefined") ) ) 
			sql.append(" and ( imageOp1= '" + imageOp1 + "'  )");
		
		String imageOp2 = StringUtil.getHttpParam(request(), "imageOp2");
		if(imageOp2==null)
			imageOp2 = "";	
		if ( !( imageOp2 .equals("") ) && !( imageOp2 .equals("undefined") ) ) 
			sql.append(" and ( imageOp2= '" + imageOp2 + "'  )");
		
		String desc2 = StringUtil.getHttpParam(request(), "desc2");
		if(desc2==null)
			desc2 = "";	
		if ( !( desc2 .equals("") ) && !( desc2 .equals("undefined") ) ) 
			sql.append(" and ( desc2= '" + desc2 + "'  )");
		
		String desc3 = StringUtil.getHttpParam(request(), "desc3");
		if(desc3==null)
			desc3 = "";	
		if ( !( desc3 .equals("") ) && !( desc3 .equals("undefined") ) ) 
			sql.append(" and ( desc3= '" + desc3 + "'  )");
		
		String status = StringUtil.getHttpParam(request(), "status");
		if(status==null)
			status = "";	
		if ( !( status .equals("") ) && !( status .equals("undefined") ) ) 
			sql.append(" and ( status= '" + status + "'  )");
		
		String addtime = StringUtil.getHttpParam(request(), "addtime");
		if(addtime==null)
			addtime = "";	
		if ( !( addtime .equals("") ) && !( addtime .equals("undefined") ) ) 
			sql.append(" and ( addtime= '" + addtime + "'  )");
		
		String factory = StringUtil.getHttpParam(request(), "factory");
		if(factory==null)
			factory = "";	
		if ( !( factory .equals("") ) && !( factory .equals("undefined") ) ) 
			sql.append(" and ( factory= '" + factory + "'  )");
		
		String export = StringUtil.getHttpParam(request(), "export");// 是否导出
		if(export!=null && export.equals("1")){
			nStart = 0;
			nLimit = 65534;
		}
		
		Logger.info(sql.toString());
		//下面简单的分页，需要重构，应该由 Dao-Sevice去实现。
		int rowCount = Ebean.getServer(GlobalDBControl.getDB()).createQuery(SmartDeviceType .class, sql.toString()).findRowCount();
		List<SmartDeviceType> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.createQuery(SmartDeviceType .class, sql.toString())
				.setFirstRow(nStart).setMaxRows(nLimit)
				.orderBy("idd desc")
				.findList();
				
		if(export!=null && export.equals("1")){
			String fileName = "SmartDeviceType"+simp.format( new Date())+".xls";
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
		PagedObject<SmartDeviceType> po= new PagedObject( ulist,nPages,rowCount );
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(po) );
	}

	//列表
	public static Result listAll(){
		List<SmartDeviceType> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.find(SmartDeviceType .class)
				.orderBy("idd")
				.findList();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(ulist) );
	}
	
	//删除
	public static Result delete(int idd){
		String sql = "delete from SmartDeviceType where idd =:idd";
		Ebean.getServer(GlobalDBControl.getDB())
				.createUpdate(SmartDeviceType .class, sql).setParameter("idd", idd).execute();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson("操作成功") );
	}
	
	//获取 单个
	public static Result get(int idd){
		SmartDeviceType smartdevicetype = Ebean.getServer(GlobalDBControl.getDB())
				.find(SmartDeviceType .class).where().eq("idd", idd).findUnique();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(smartdevicetype) );
	}

	//新增 / 修改
	public static Result modify(){
		String idd = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "idd");
		int nidd = StringTool.GetInt(idd, 0);
		
		
		
		
		
		String name = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "name");
		
		
		
		String catagary = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "catagary");
		
		
		
		String logo = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "logo");
		
		
		
		String image = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "image");
		
		
		
		String descinfo = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "descinfo");
		
		
		
		String imageLink = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "imageLink");
		
		
		
		String imageOp1 = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "imageOp1");
		
		
		
		String imageOp2 = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "imageOp2");
		
		
		
		String desc2 = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "desc2");
		
		
		
		String desc3 = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "desc3");
		
		
		
		String status = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "status");
		
		
		
		String addtime = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "addtime");
		
		
		
		String factory = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "factory");
		
		
		
		String operation = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "operation");
		
		SmartDeviceType smartdevicetype ;
		if(  operation.equals("add") )	// 新增
			smartdevicetype = new SmartDeviceType ();
		else			//修改
			smartdevicetype = Ebean.getServer(GlobalDBControl.getDB())
					.find(SmartDeviceType .class).where().eq("idd", idd).findUnique();
		
		if( smartdevicetype!=null ){
			//赋值,字符串类型可以直接赋值，其他类型需要转换 ！！！！
			
				 
				
				
			
				
					smartdevicetype. setName ( name );
				 
				
				
			
				
					smartdevicetype. setCatagary ( catagary );
				 
				
				
			
				
					smartdevicetype. setLogo ( logo );
				 
				
				
			
				
					smartdevicetype. setImage ( image );
				 
				
				
			
				
					smartdevicetype. setDescinfo ( descinfo );
				 
				
				
			
				
					smartdevicetype. setImageLink ( imageLink );
				 
				
				
			
				
					smartdevicetype. setImageOp1 ( imageOp1 );
				 
				
				
			
				
					smartdevicetype. setImageOp2 ( imageOp2 );
				 
				
				
			
				
					smartdevicetype. setDesc2 ( desc2 );
				 
				
				
			
				
					smartdevicetype. setDesc3 ( desc3 );
				 
				
				
			
				 
				
				
					smartdevicetype. setStatus ( StringTool.GetInt(status,0) );
				
			
				 
				
					smartdevicetype. setAddtime ( StringUtil.getDate(addtime) );
				
				
			
				
					smartdevicetype. setFactory ( factory );
				 
				
				
			
			Ebean.getServer(GlobalDBControl.getDB()).save( smartdevicetype );
			response().setHeader("Cache-Control", "no-cache");
			return ok( Json.toJson(smartdevicetype) );
		}

		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(null) );
	}
	
	static int num=0;
	public static File exportList(List<SmartDeviceType> infoList,String fileNameChine) throws UnsupportedEncodingException {
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
					
				
					
					,"名称"
				
					
					,"设备分类"
				
					
					,"缩略图"
				
					
					,"图片"
				
					
					,"描述信息"
				
					
					,"说明信息的图片"
				
					
					,"操作图片的链接1"
				
					
					,"操作图片的链接2"
				
					
					,"扩展信息2"
				
					
					,"扩展信息3"
				
					
					,"状态"
				
					
					,"添加时间"
				
					
					,"生成厂家"
				
			};

		// 生成表头
		helper.setRowIndex(rowIndex++);
		helper.generateHeader(sheet, titles, 0, StringUtils.EMPTY,
				StringUtils.EMPTY);

		// 循环生成数据行
		for (int i = 0; i < infoList.size(); i++) {
			colIndex = 0;
			SmartDeviceType info = (SmartDeviceType) infoList.get(i);
			Row row = sheet.createRow(rowIndex++);
			
			
				helper.createStringCell( row , colIndex++, ""+info. getIdd () );
			
				helper.createStringCell( row , colIndex++, ""+info. getName () );
			
				helper.createStringCell( row , colIndex++, ""+info. getCatagary () );
			
				helper.createStringCell( row , colIndex++, ""+info. getLogo () );
			
				helper.createStringCell( row , colIndex++, ""+info. getImage () );
			
				helper.createStringCell( row , colIndex++, ""+info. getDescinfo () );
			
				helper.createStringCell( row , colIndex++, ""+info. getImageLink () );
			
				helper.createStringCell( row , colIndex++, ""+info. getImageOp1 () );
			
				helper.createStringCell( row , colIndex++, ""+info. getImageOp2 () );
			
				helper.createStringCell( row , colIndex++, ""+info. getDesc2 () );
			
				helper.createStringCell( row , colIndex++, ""+info. getDesc3 () );
			
				helper.createStringCell( row , colIndex++, ""+info. getStatus () );
			
				helper.createStringCell( row , colIndex++, ""+info. getAddtime () );
			
				helper.createStringCell( row , colIndex++, ""+info. getFactory () );
			
		}
		//在application.conf中配置的路径
		String path = Configuration.root().getString("export.path");
		int numStra = num++;//静态值，区分文件名
		File file = new File(path);
		file.mkdir();//判断文件夹是否存在，不存在就创建
		
		FileOutputStream out = null;
		String fileName = path + "SmartDeviceType" + System.currentTimeMillis() + numStra + ".xls";
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
				
					
					
				
					"名称"
					
				
					
					,"设备分类"
				
					
					,"缩略图"
				
					
					,"图片"
				
					
					,"描述信息"
				
					
					,"说明信息的图片"
				
					
					,"操作图片的链接1"
				
					
					,"操作图片的链接2"
				
					
					,"扩展信息2"
				
					
					,"扩展信息3"
				
					
					,"状态"
				
					
					,"添加时间"
				
					
					,"生成厂家"
				
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
			
			List<SmartDeviceType> list = new ArrayList<SmartDeviceType>();
			 for (int i = 1; i <= lastRowNumber; i++) {
				 //批量添加
                 Row row = sheet.getRow(i);
                 if (row == null) {
                     break;
                 }
                 
                 SmartDeviceType fc = new SmartDeviceType();
                 int j = 0 ;
                 
					 
					
					
					
				
					
						fc. setName ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					
						fc. setCatagary ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					
						fc. setLogo ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					
						fc. setImage ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					
						fc. setDescinfo ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					
						fc. setImageLink ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					
						fc. setImageOp1 ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					
						fc. setImageOp2 ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					
						fc. setDesc2 ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					
						fc. setDesc3 ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					 
					
					
						fc. setStatus ( StringTool.GetInt(StringUtil.cellValueToString(row.getCell(j)),0) );
					
					
						j=j+1;
					
				
					 
					
						//fc. setAddtime ( StringUtil.getDate( StringUtil.cellValueToString(row.getCell(j) ) );
					
					
					
						j=j+1;
					
				
					
						fc. setFactory ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
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