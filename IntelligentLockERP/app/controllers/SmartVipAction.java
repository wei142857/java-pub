package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import models.SmartVip;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import play.Configuration;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import play.mvc.Security;
import util.AjaxHellper;
import util.ExcelGenerateHelper;
import util.ExcelUtils;
import util.GlobalDBControl;
import util.PagedObject;
import util.StringUtil;
import util.classEntity.StringTool;
import views.html.page.SmartVipList;
import com.avaje.ebean.Ebean;

@Security.Authenticated(Secured.class)
public class SmartVipAction extends Controller {
  
	//进入页面列表；
	public static Result view() {
		return ok( SmartVipList.render() );
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
		sql.append("find SmartVip where 1=1 ");
		
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
		
		String addtime = StringUtil.getHttpParam(request(), "addtime");
		if(addtime==null)
			addtime = "";	
		if ( !( addtime .equals("") ) && !( addtime .equals("undefined") ) ) 
			sql.append(" and ( addtime= '" + addtime + "'  )");
		
		String buytime = StringUtil.getHttpParam(request(), "buytime");
		if(buytime==null)
			buytime = "";	
		if ( !( buytime .equals("") ) && !( buytime .equals("undefined") ) ) 
			sql.append(" and ( buytime= '" + buytime + "'  )");
		
		String overtime = StringUtil.getHttpParam(request(), "overtime");
		if(overtime==null)
			overtime = "";	
		if ( !( overtime .equals("") ) && !( overtime .equals("undefined") ) ) 
			sql.append(" and ( overtime= '" + overtime + "'  )");
		
		String lastbuytime = StringUtil.getHttpParam(request(), "lastbuytime");
		if(lastbuytime==null)
			lastbuytime = "";	
		if ( !( lastbuytime .equals("") ) && !( lastbuytime .equals("undefined") ) ) 
			sql.append(" and ( lastbuytime= '" + lastbuytime + "'  )");
		
		String lastovertime = StringUtil.getHttpParam(request(), "lastovertime");
		if(lastovertime==null)
			lastovertime = "";	
		if ( !( lastovertime .equals("") ) && !( lastovertime .equals("undefined") ) ) 
			sql.append(" and ( lastovertime= '" + lastovertime + "'  )");
		
		String firstbuytime = StringUtil.getHttpParam(request(), "firstbuytime");
		if(firstbuytime==null)
			firstbuytime = "";	
		if ( !( firstbuytime .equals("") ) && !( firstbuytime .equals("undefined") ) ) 
			sql.append(" and ( firstbuytime= '" + firstbuytime + "'  )");
		
		String deviceid = StringUtil.getHttpParam(request(), "deviceid");
		if(deviceid==null)
			deviceid = "";	
		if ( !( deviceid .equals("") ) && !( deviceid .equals("undefined") ) ) 
			sql.append(" and ( deviceid= '" + deviceid + "'  )");
		
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
		int rowCount = Ebean.getServer(GlobalDBControl.getDB()).createQuery(SmartVip .class, sql.toString()).findRowCount();
		List<SmartVip> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.createQuery(SmartVip .class, sql.toString())
				.setFirstRow(nStart).setMaxRows(nLimit)
				.orderBy("idd desc")
				.findList();
				
		if(export!=null && export.equals("1")){
			String fileName = "SmartVip"+simp.format( new Date())+".xls";
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
		PagedObject<SmartVip> po= new PagedObject( ulist,nPages,rowCount );
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(po) );
	}

	//列表
	public static Result listAll(){
		List<SmartVip> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.find(SmartVip .class)
				.orderBy("idd")
				.findList();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(ulist) );
	}
	
	//删除
	public static Result delete(int idd){
		String sql = "delete from SmartVip where idd =:idd";
		Ebean.getServer(GlobalDBControl.getDB())
				.createUpdate(SmartVip .class, sql).setParameter("idd", idd).execute();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson("操作成功") );
	}
	
	//获取 单个
	public static Result get(int idd){
		SmartVip smartvip = Ebean.getServer(GlobalDBControl.getDB())
				.find(SmartVip .class).where().eq("idd", idd).findUnique();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(smartvip) );
	}

	//新增 / 修改
	public static Result modify(){
		String idd = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "idd");
		int nidd = StringTool.GetInt(idd, 0);
		
		String userid = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "userid");
		
		String addtime = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "addtime");
		
		String buytime = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "buytime");
		
		String overtime = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "overtime");
		
		String lastbuytime = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "lastbuytime");
		
		String lastovertime = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "lastovertime");
		
		String firstbuytime = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "firstbuytime");
		
		String deviceid = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "deviceid");
		
		String channel = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "channel");
		
		String operation = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "operation");
		
		SmartVip smartvip ;
		if(  operation.equals("add") )	// 新增
			smartvip = new SmartVip ();
		else			//修改
			smartvip = Ebean.getServer(GlobalDBControl.getDB())
					.find(SmartVip .class).where().eq("idd", idd).findUnique();
		
		if( smartvip!=null ){
			//赋值,字符串类型可以直接赋值，其他类型需要转换 ！！！！
			smartvip. setUserid ( StringTool.GetInt(userid,0) );
			smartvip. setAddtime ( StringUtil.getDate(addtime) );
			smartvip. setBuytime ( StringUtil.getDate(buytime) );
			smartvip. setOvertime ( StringUtil.getDate(overtime) );
			smartvip. setLastbuytime ( StringUtil.getDate(lastbuytime) );
			smartvip. setLastovertime ( StringUtil.getDate(lastovertime) );
			smartvip. setFirstbuytime ( StringUtil.getDate(firstbuytime) );
			smartvip. setDeviceid ( deviceid );
			smartvip. setChannel ( channel );
			
			Ebean.getServer(GlobalDBControl.getDB()).save( smartvip );
			response().setHeader("Cache-Control", "no-cache");
			return ok( Json.toJson(smartvip) );
		}

		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(null) );
	}
	
	static int num=0;
	public static File exportList(List<SmartVip> infoList,String fileNameChine) throws UnsupportedEncodingException {
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
		
		ExcelGenerateHelper helper = ExcelGenerateHelper.getInstance(wb);

		// 获取行索引
		int rowIndex = 0;
		int colIndex = 0;

		// 设置表头
		String[] titles = { 
				
			"ID"
			
			,"用户ID"
			
			,"添加时间"
			
			,"购买时间"
			
			,"过期时间"
			
			,"上次购买时间"
			
			,"上次过期时间"
			
			,"第一次过期时间"
			
			,"最后一次绑定的设备ID"
			
			,"获取会员渠道"
		};

		// 生成表头
		helper.setRowIndex(rowIndex++);
		helper.generateHeader(sheet, titles, 0, StringUtils.EMPTY,
				StringUtils.EMPTY);

		// 循环生成数据行
		for (int i = 0; i < infoList.size(); i++) {
			colIndex = 0;
			SmartVip info = (SmartVip) infoList.get(i);
			Row row = sheet.createRow(rowIndex++);
			
			helper.createStringCell( row , colIndex++, ""+info. getIdd () );
		
			helper.createStringCell( row , colIndex++, ""+info. getUserid () );
		
			helper.createStringCell( row , colIndex++, ""+info. getAddtime () );
		
			helper.createStringCell( row , colIndex++, ""+info. getBuytime () );
		
			helper.createStringCell( row , colIndex++, ""+info. getOvertime () );
		
			helper.createStringCell( row , colIndex++, ""+info. getLastbuytime () );
		
			helper.createStringCell( row , colIndex++, ""+info. getLastovertime () );
		
			helper.createStringCell( row , colIndex++, ""+info. getFirstbuytime () );
		
			helper.createStringCell( row , colIndex++, ""+info. getDeviceid () );
		
			helper.createStringCell( row , colIndex++, ""+info. getChannel () );
		}
		//在application.conf中配置的路径
		String path = Configuration.root().getString("export.path");
		int numStra = num++;//静态值，区分文件名
		File file = new File(path);
		file.mkdir();//判断文件夹是否存在，不存在就创建
		
		FileOutputStream out = null;
		String fileName = path + "SmartVip" + System.currentTimeMillis() + numStra + ".xls";
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
		
			"用户ID"
			
			,"添加时间"
			
			,"购买时间"
			
			,"过期时间"
		
			,"上次购买时间"
			
			,"上次过期时间"
			
			,"第一次过期时间"
			
			,"最后一次绑定的设备ID"
		
			,"获取会员渠道"
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
			
			List<SmartVip> list = new ArrayList<SmartVip>();
			 for (int i = 1; i <= lastRowNumber; i++) {
				 //批量添加
                 Row row = sheet.getRow(i);
                 if (row == null) {
                     break;
                 }
                SmartVip fc = new SmartVip();
                int j = 0 ;
				fc. setUserid ( StringTool.GetInt(StringUtil.cellValueToString(row.getCell(j)),0) );
			
				j=j+1;
				//fc. setAddtime ( StringUtil.getDate( StringUtil.cellValueToString(row.getCell(j) ) );
			
				j=j+1;
				//fc. setBuytime ( StringUtil.getDate( StringUtil.cellValueToString(row.getCell(j) ) );
			
				j=j+1;
				//fc. setOvertime ( StringUtil.getDate( StringUtil.cellValueToString(row.getCell(j) ) );
			
				j=j+1;
				//fc. setLastbuytime ( StringUtil.getDate( StringUtil.cellValueToString(row.getCell(j) ) );
			
				j=j+1;
				//fc. setLastovertime ( StringUtil.getDate( StringUtil.cellValueToString(row.getCell(j) ) );
			
				j=j+1;
				//fc. setFirstbuytime ( StringUtil.getDate( StringUtil.cellValueToString(row.getCell(j) ) );
			
				j=j+1;
				fc. setDeviceid ( StringUtil.cellValueToString(row.getCell(j)) );
			
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