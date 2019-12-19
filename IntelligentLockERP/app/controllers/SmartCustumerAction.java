
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
public class SmartCustumerAction extends Controller {
  
	//进入页面列表；
	public static Result view() {
		return ok( SmartCustumerList.render(null) );
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
		sql.append("find SmartCustumer where 1=1 ");
		
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
		
		String grage = StringUtil.getHttpParam(request(), "grage");
		if(grage==null)
			grage = "";	
		if ( !( grage .equals("") ) && !( grage .equals("undefined") ) ) 
			sql.append(" and ( grage= '" + grage + "'  )");
		
		String countory = StringUtil.getHttpParam(request(), "countory");
		if(countory==null)
			countory = "";	
		if ( !( countory .equals("") ) && !( countory .equals("undefined") ) ) 
			sql.append(" and ( countory= '" + countory + "'  )");
		
		String prov = StringUtil.getHttpParam(request(), "prov");
		if(prov==null)
			prov = "";	
		if ( !( prov .equals("") ) && !( prov .equals("undefined") ) ) 
			sql.append(" and ( prov= '" + prov + "'  )");
		
		String city = StringUtil.getHttpParam(request(), "city");
		if(city==null)
			city = "";	
		if ( !( city .equals("") ) && !( city .equals("undefined") ) ) 
			sql.append(" and ( city= '" + city + "'  )");
		
		String birthday = StringUtil.getHttpParam(request(), "birthday");
		if(birthday==null)
			birthday = "";	
		if ( !( birthday .equals("") ) && !( birthday .equals("undefined") ) ) 
			sql.append(" and ( birthday= '" + birthday + "'  )");
		
		String sex = StringUtil.getHttpParam(request(), "sex");
		if(sex==null)
			sex = "";	
		if ( !( sex .equals("") ) && !( sex .equals("undefined") ) ) 
			sql.append(" and ( sex= '" + sex + "'  )");
		
		String phone = StringUtil.getHttpParam(request(), "phone");
		if(phone==null)
			phone = "";	
		if ( !( phone .equals("") ) && !( phone .equals("undefined") ) ) 
			sql.append(" and ( phone= '" + phone + "'  )");
		
		String contact = StringUtil.getHttpParam(request(), "contact");
		if(contact==null)
			contact = "";	
		if ( !( contact .equals("") ) && !( contact .equals("undefined") ) ) 
			sql.append(" and ( contact= '" + contact + "'  )");
		
		String address = StringUtil.getHttpParam(request(), "address");
		if(address==null)
			address = "";	
		if ( !( address .equals("") ) && !( address .equals("undefined") ) ) 
			sql.append(" and ( address= '" + address + "'  )");
		
		String email = StringUtil.getHttpParam(request(), "email");
		if(email==null)
			email = "";	
		if ( !( email .equals("") ) && !( email .equals("undefined") ) ) 
			sql.append(" and ( email= '" + email + "'  )");
		
		String addtime = StringUtil.getHttpParam(request(), "addtime");
		if(addtime==null)
			addtime = "";	
		if ( !( addtime .equals("") ) && !( addtime .equals("undefined") ) ) 
			sql.append(" and ( addtime= '" + addtime + "'  )");
		
		String export = StringUtil.getHttpParam(request(), "export");// 是否导出
		if(export!=null && export.equals("1")){
			nStart = 0;
			nLimit = 65534;
		}
		
		Logger.info(sql.toString());
		//下面简单的分页，需要重构，应该由 Dao-Sevice去实现。
		int rowCount = Ebean.getServer(GlobalDBControl.getDB()).createQuery(SmartCustumer .class, sql.toString()).findRowCount();
		List<SmartCustumer> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.createQuery(SmartCustumer .class, sql.toString())
				.setFirstRow(nStart).setMaxRows(nLimit)
				.orderBy("idd desc")
				.findList();
				
		if(export!=null && export.equals("1")){
			String fileName = "SmartCustumer"+simp.format( new Date())+".xls";
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
		PagedObject<SmartCustumer> po= new PagedObject( ulist,nPages,rowCount );
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(po) );
	}

	//列表
	public static Result listAll(){
		List<SmartCustumer> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.find(SmartCustumer .class)
				.orderBy("idd")
				.findList();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(ulist) );
	}
	
	//删除
	public static Result delete(int idd){
		String sql = "delete from SmartCustumer where idd =:idd";
		Ebean.getServer(GlobalDBControl.getDB())
				.createUpdate(SmartCustumer .class, sql).setParameter("idd", idd).execute();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson("操作成功") );
	}
	
	//获取 单个
	public static Result get(int idd){
		SmartCustumer smartcustumer = Ebean.getServer(GlobalDBControl.getDB())
				.find(SmartCustumer .class).where().eq("idd", idd).findUnique();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(smartcustumer) );
	}

	//新增 / 修改
	public static Result modify(){
		String idd = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "idd");
		int nidd = StringTool.GetInt(idd, 0);
		
		
		
		
		
		String name = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "name");
		
		
		
		String grage = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "grage");
		
		
		
		String countory = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "countory");
		
		
		
		String prov = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "prov");
		
		
		
		String city = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "city");
		
		
		
		String birthday = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "birthday");
		
		
		
		String sex = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "sex");
		
		
		
		String phone = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "phone");
		
		
		
		String contact = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "contact");
		
		
		
		String address = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "address");
		
		
		
		String email = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "email");
		
		
		
		String addtime = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "addtime");
		
		
		
		String operation = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "operation");
		
		SmartCustumer smartcustumer ;
		if(  operation.equals("add") )	// 新增
			smartcustumer = new SmartCustumer ();
		else			//修改
			smartcustumer = Ebean.getServer(GlobalDBControl.getDB())
					.find(SmartCustumer .class).where().eq("idd", idd).findUnique();
		
		if( smartcustumer!=null ){
			//赋值,字符串类型可以直接赋值，其他类型需要转换 ！！！！
			
				 
				
				
			
				
					smartcustumer. setName ( name );
				 
				
				
			
				 
				
				
					smartcustumer. setGrage ( StringTool.GetInt(grage,0) );
				
			
				
					smartcustumer. setCountory ( countory );
				 
				
				
			
				
					smartcustumer. setProv ( prov );
				 
				
				
			
				
					smartcustumer. setCity ( city );
				 
				
				
			
				 
				
					smartcustumer. setBirthday ( StringUtil.getDate(birthday) );
				
				
			
				
					smartcustumer. setSex ( sex );
				 
				
				
			
				
					smartcustumer. setPhone ( phone );
				 
				
				
			
				
					smartcustumer. setContact ( contact );
				 
				
				
			
				
					smartcustumer. setAddress ( address );
				 
				
				
			
				
					smartcustumer. setEmail ( email );
				 
				
				
			
				 
				
					smartcustumer. setAddtime ( StringUtil.getDate(addtime) );
				
				
			
			Ebean.getServer(GlobalDBControl.getDB()).save( smartcustumer );
			response().setHeader("Cache-Control", "no-cache");
			return ok( Json.toJson(smartcustumer) );
		}

		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(null) );
	}
	
	static int num=0;
	public static File exportList(List<SmartCustumer> infoList,String fileNameChine) throws UnsupportedEncodingException {
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
		
		ExcelGenerateHelper helper = ExcelGenerateHelper.getInstance(wb);

		// 获取行索引
		int rowIndex = 0;
		int colIndex = 0;

		// 设置表头
		String[] titles = { 
				
					"ID"
					
				
					
					,"名字"
				
					
					,"级别"
				
					
					,"国家"
				
					
					,"省"
				
					
					,"市"
				
					
					,"生日"
				
					
					,"性别"
				
					
					,"手机"
				
					
					,"联系方式"
				
					
					,"地址"
				
					
					,"邮箱"
				
					
					,"添加时间"
				
			};

		// 生成表头
		helper.setRowIndex(rowIndex++);
		helper.generateHeader(sheet, titles, 0, StringUtils.EMPTY,
				StringUtils.EMPTY);

		// 循环生成数据行
		for (int i = 0; i < infoList.size(); i++) {
			colIndex = 0;
			SmartCustumer info = (SmartCustumer) infoList.get(i);
			Row row = sheet.createRow(rowIndex++);
			
			
				helper.createStringCell( row , colIndex++, ""+info. getIdd () );
			
				helper.createStringCell( row , colIndex++, ""+info. getName () );
			
				helper.createStringCell( row , colIndex++, ""+info. getGrage () );
			
				helper.createStringCell( row , colIndex++, ""+info. getCountory () );
			
				helper.createStringCell( row , colIndex++, ""+info. getProv () );
			
				helper.createStringCell( row , colIndex++, ""+info. getCity () );
			
				helper.createStringCell( row , colIndex++, ""+info. getBirthday () );
			
				helper.createStringCell( row , colIndex++, ""+info. getSex () );
			
				helper.createStringCell( row , colIndex++, ""+info. getPhone () );
			
				helper.createStringCell( row , colIndex++, ""+info. getContact () );
			
				helper.createStringCell( row , colIndex++, ""+info. getAddress () );
			
				helper.createStringCell( row , colIndex++, ""+info. getEmail () );
			
				helper.createStringCell( row , colIndex++, ""+info. getAddtime () );
			
		}
		//在application.conf中配置的路径
		String path = Configuration.root().getString("export.path");
		int numStra = num++;//静态值，区分文件名
		File file = new File(path);
		file.mkdir();//判断文件夹是否存在，不存在就创建
		
		FileOutputStream out = null;
		String fileName = path + "SmartCustumer" + System.currentTimeMillis() + numStra + ".xls";
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
				
					
					
				
					"名字"
					
				
					
					,"级别"
				
					
					,"国家"
				
					
					,"省"
				
					
					,"市"
				
					
					,"生日"
				
					
					,"性别"
				
					
					,"手机"
				
					
					,"联系方式"
				
					
					,"地址"
				
					
					,"邮箱"
				
					
					,"添加时间"
				
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
			
			List<SmartCustumer> list = new ArrayList<SmartCustumer>();
			 for (int i = 1; i <= lastRowNumber; i++) {
				 //批量添加
                 Row row = sheet.getRow(i);
                 if (row == null) {
                     break;
                 }
                 
                 SmartCustumer fc = new SmartCustumer();
                 int j = 0 ;
                 
					 
					
					
					
				
					
						fc. setName ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					 
					
					
						fc. setGrage ( StringTool.GetInt(StringUtil.cellValueToString(row.getCell(j)),0) );
					
					
						j=j+1;
					
				
					
						fc. setCountory ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					
						fc. setProv ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					
						fc. setCity ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					 
					
						//fc. setBirthday ( StringUtil.getDate( StringUtil.cellValueToString(row.getCell(j) ) );
					
					
					
						j=j+1;
					
				
					
						fc. setSex ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					
						fc. setPhone ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					
						fc. setContact ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					
						fc. setAddress ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					
						fc. setEmail ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					 
					
						//fc. setAddtime ( StringUtil.getDate( StringUtil.cellValueToString(row.getCell(j) ) );
					
					
					
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