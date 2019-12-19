
package controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import models.IntelligentLockManufacturer;
import models.ReturnMessage;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import play.Configuration;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import util.AjaxHellper;
import util.ExcelGenerateHelper;
import util.GlobalDBControl;
import util.PagedObject;
import util.StringUtil;
import util.classEntity.StringTool;
import util.jedis.RedisUtil;
import views.html.page.IntelligentLockManufacturerList;

import com.avaje.ebean.Ebean;

@Security.Authenticated(Secured.class)
public class IntelligentLockManufacturerAction extends Controller {
  
	//进入页面列表；
	public static Result view() {
		return ok( IntelligentLockManufacturerList.render() );
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
		sql.append("find IntelligentLockManufacturer where 1=1 ");
		
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
		
		String telphone = StringUtil.getHttpParam(request(), "telphone");
		if(telphone==null)
			telphone = "";	
		if ( !( telphone .equals("") ) && !( telphone .equals("undefined") ) ) 
			sql.append(" and ( telphone= '" + telphone + "'  )");
		
		String address = StringUtil.getHttpParam(request(), "address");
		if(address==null)
			address = "";	
		if ( !( address .equals("") ) && !( address .equals("undefined") ) ) 
			sql.append(" and ( address= '" + address + "'  )");
		
		/*String addtime = StringUtil.getHttpParam(request(), "addtime");
		if(addtime==null)
			addtime = "";	
		if ( !( addtime .equals("") ) && !( addtime .equals("undefined") ) ) 
			sql.append(" and ( addtime= '" + addtime + "'  )");*/
		
		String export = StringUtil.getHttpParam(request(), "export");// 是否导出
		if(export!=null && export.equals("1")){
			nStart = 0;
			nLimit = 65534;
		}
		
		Logger.info(sql.toString());
		//下面简单的分页，需要重构，应该由 Dao-Sevice去实现。
		int rowCount = Ebean.getServer(GlobalDBControl.getDB()).createQuery(IntelligentLockManufacturer .class, sql.toString()).findRowCount();
		List<IntelligentLockManufacturer> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.createQuery(IntelligentLockManufacturer .class, sql.toString())
				.setFirstRow(nStart).setMaxRows(nLimit)
				.orderBy("idd asc")
				.findList();
				
		if(export!=null && export.equals("1")){
			String fileName = "IntelligentLockManufacturer"+simp.format( new Date())+".xls";
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
		PagedObject<IntelligentLockManufacturer> po= new PagedObject( ulist,nPages,rowCount );
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(po) );
	}

	//列表
	public static Result listAll(){
		List<IntelligentLockManufacturer> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.find(IntelligentLockManufacturer .class)
				.orderBy("idd")
				.findList();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(ulist) );
	}
	
	//删除
	public static Result delete(int idd){
		String sql = "delete from IntelligentLockManufacturer where idd =:idd";
		Ebean.getServer(GlobalDBControl.getDB())
				.createUpdate(IntelligentLockManufacturer .class, sql).setParameter("idd", idd).execute();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson("操作成功") );
	}
	
	//获取 单个
	public static Result get(int idd){
		IntelligentLockManufacturer IntelligentLockManufacturer = Ebean.getServer(GlobalDBControl.getDB())
				.find(IntelligentLockManufacturer .class).where().eq("idd", idd).findUnique();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(IntelligentLockManufacturer) );
	}

	//新增 / 修改
	public static Result modify(){
		String idd = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "idd");
		int nidd = StringTool.GetInt(idd, 0);
		String name = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "name");
		String telphone = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "telphone");
		String address = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "address");
		
		//根据厂家的名称 电话 和地址去数据库中查询
		StringBuffer sql = new StringBuffer();
		sql.append("find IntelligentLockManufacturer where 1 = 1");
		sql.append(" and name = :name and telphone = :telphone and address = :address");
		IntelligentLockManufacturer facture = Ebean.getServer(GlobalDBControl.getDB())
				.createQuery(IntelligentLockManufacturer.class,sql.toString())
				.setParameter("name", name).setParameter("telphone", telphone
						).setParameter("address", address).findUnique();
		//若数据库中存在该厂家信息 则提示 已经存在 无需重复添加
		if(facture!=null){
			ReturnMessage returnMessage = new ReturnMessage();
			returnMessage.setCode("0");
			returnMessage.setMessage("添加的该厂家已存在,请勿重新添加!");
			return ok(Json.toJson(returnMessage));
		}
		
		String operation = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "operation");
		
		IntelligentLockManufacturer IntelligentLockManufacturer ;
		if(  operation.equals("add") ){	// 新增
			RedisUtil.getInstance().deleteEntityStr("smart:deviceid:4600435553098130",10);
			RedisUtil.getInstance().deleteEntityStr("freeze:device:4600435553098130",10);
			IntelligentLockManufacturer = new IntelligentLockManufacturer ();
		}else			//修改	
			IntelligentLockManufacturer = Ebean.getServer(GlobalDBControl.getDB())
					.find(IntelligentLockManufacturer .class).where().eq("idd", idd).findUnique();
		
		if( IntelligentLockManufacturer!=null ){
			//赋值,字符串类型可以直接赋值，其他类型需要转换 ！！！！
			
			IntelligentLockManufacturer. setName ( name );
		
			
			IntelligentLockManufacturer. setTelphone ( telphone );
			
		
			IntelligentLockManufacturer. setAddress ( address );
		
			if(  operation.equals("add") ){
				IntelligentLockManufacturer. setAddtime ( new Date() );
			}
					
			Ebean.getServer(GlobalDBControl.getDB()).save( IntelligentLockManufacturer );
			response().setHeader("Cache-Control", "no-cache");
			return ok( Json.toJson(IntelligentLockManufacturer) );
		}

		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(null) );
	}
	
	static int num=0;
	public static File exportList(List<IntelligentLockManufacturer> infoList,String fileNameChine) throws UnsupportedEncodingException {
		Workbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet("sheet1");
		
			sheet.setColumnWidth( 0 , 6000 );
		
			sheet.setColumnWidth( 1 , 6000 );
		
			sheet.setColumnWidth( 2 , 6000 );
		
			sheet.setColumnWidth( 3 , 6000 );
		
			sheet.setColumnWidth( 4 , 6000 );
		
		ExcelGenerateHelper helper = ExcelGenerateHelper.getInstance(wb);

		// 获取行索引
		int rowIndex = 0;
		int colIndex = 0;

		// 设置表头
		String[] titles = { 
			"idd"
			,"名称"
			,"联系电话"
			,"地址"
			,"添加时间"
		};

		// 生成表头
		helper.setRowIndex(rowIndex++);
		helper.generateHeader(sheet, titles, 0, StringUtils.EMPTY,
				StringUtils.EMPTY);

		// 循环生成数据行
		for (int i = 0; i < infoList.size(); i++) {
			colIndex = 0;
			IntelligentLockManufacturer info = (IntelligentLockManufacturer) infoList.get(i);
			Row row = sheet.createRow(rowIndex++);
			helper.createStringCell( row , colIndex++, ""+info. getIdd () );
		
			helper.createStringCell( row , colIndex++, ""+info. getName () );
		
			helper.createStringCell( row , colIndex++, ""+info. getTelphone () );
		
			helper.createStringCell( row , colIndex++, ""+info. getAddress () );
		
			helper.createStringCell( row , colIndex++, ""+simp.format(info. getAddtime ()) );
		}
		//在application.conf中配置的路径
		String path = Configuration.root().getString("export.path");
		int numStra = num++;//静态值，区分文件名
		File file = new File(path);
		file.mkdir();//判断文件夹是否存在，不存在就创建
		
		FileOutputStream out = null;
		String fileName = path + "生产厂家信息表" + System.currentTimeMillis() + numStra + ".xls";
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
		"名称"
		,"联系电话"
		,"地址"
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
			
			List<IntelligentLockManufacturer> list = new ArrayList<IntelligentLockManufacturer>();
			 for (int i = 1; i <= lastRowNumber; i++) {
				 //批量添加
                 Row row = sheet.getRow(i);
                 if (row == null) {
                     break;
                 }
                 
                 IntelligentLockManufacturer fc = new IntelligentLockManufacturer();
                 int j = 0 ;
					
						fc. setName ( StringUtil.cellValueToString(row.getCell(j)) );
					 
						j=j+1;
					
						fc. setTelphone ( StringUtil.cellValueToString(row.getCell(j)) );
					
						j=j+1;
					
						fc. setAddress ( StringUtil.cellValueToString(row.getCell(j)) );
					
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
	}*/
	
}