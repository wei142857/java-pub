
package controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import models.*;
import ServiceDao.WxGzhService;

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
public class SubMeasureOrdersAction extends Controller {
  
	//进入页面列表；
	public static Result view() {
		return ok( SubMeasureOrdersList.render() );
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
		sql.append("find SubMeasureOrders where 1=1 ");
		
		String idd = StringUtil.getHttpParam(request(), "idd");
		if(idd==null)
			idd = "";	
		if ( !( idd .equals("") ) && !( idd .equals("undefined") ) ) 
			sql.append(" and ( idd= '" + idd + "'  )");
		
		String phone = StringUtil.getHttpParam(request(), "phone");
		if(phone==null)
			phone = "";	
		if ( !( phone .equals("") ) && !( phone .equals("undefined") ) ) 
			sql.append(" and ( phone= '" + phone + "'  )");
		
		String oid = StringUtil.getHttpParam(request(), "oid");
		if(oid==null)
			oid = "";	
		if ( !( oid .equals("") ) && !( oid .equals("undefined") ) ) 
			sql.append(" and ( oid= '" + oid + "'  )");
		
		String measurename = StringUtil.getHttpParam(request(), "measurename");
		if(measurename==null)
			measurename = "";	
		if ( !( measurename .equals("") ) && !( measurename .equals("undefined") ) ) 
			sql.append(" and ( measurename= '" + measurename + "'  )");
		
		String measurearea = StringUtil.getHttpParam(request(), "measurearea");
		if(measurearea==null)
			measurearea = "";	
		if ( !( measurearea .equals("") ) && !( measurearea .equals("undefined") ) ) 
			sql.append(" and ( measurearea= '" + measurearea + "'  )");
		
		String measureaddress = StringUtil.getHttpParam(request(), "measureaddress");
		if(measureaddress==null)
			measureaddress = "";	
		if ( !( measureaddress .equals("") ) && !( measureaddress .equals("undefined") ) ) 
			sql.append(" and ( measureaddress= '" + measureaddress + "'  )");
		
		String measurephone = StringUtil.getHttpParam(request(), "measurephone");
		if(measurephone==null)
			measurephone = "";	
		if ( !( measurephone .equals("") ) && !( measurephone .equals("undefined") ) ) 
			sql.append(" and ( measurephone= '" + measurephone + "'  )");
		
		String status = StringUtil.getHttpParam(request(), "status");
		if(status==null)
			status = "";	
		if ( !( status .equals("") ) && !( status .equals("undefined") ) ) 
			sql.append(" and ( status= '" + status + "'  )");
		
		String money = StringUtil.getHttpParam(request(), "money");
		if(money==null)
			money = "";	
		if ( !( money .equals("") ) && !( money .equals("undefined") ) ) 
			sql.append(" and ( money= '" + money + "'  )");
		
		String addtime = StringUtil.getHttpParam(request(), "addtime");
		if(addtime==null)
			addtime = "";	
		if ( !( addtime .equals("") ) && !( addtime .equals("undefined") ) ) 
			sql.append(" and ( addtime= '" + addtime + "'  )");
		
		String measuretime = StringUtil.getHttpParam(request(), "measuretime");
		if(measuretime==null)
			measuretime = "";	
		if ( !( measuretime .equals("") ) && !( measuretime .equals("undefined") ) ) 
			sql.append(" and ( measuretime= '" + measuretime + "'  )");
		
		String prepayid = StringUtil.getHttpParam(request(), "prepayid");
		if(prepayid==null)
			prepayid = "";	
		if ( !( prepayid .equals("") ) && !( prepayid .equals("undefined") ) ) 
			sql.append(" and ( prepayid= '" + prepayid + "'  )");
		
		String updatetime = StringUtil.getHttpParam(request(), "updatetime");
		if(updatetime==null)
			updatetime = "";	
		if ( !( updatetime .equals("") ) && !( updatetime .equals("undefined") ) ) 
			sql.append(" and ( updatetime= '" + updatetime + "'  )");
		
		String export = StringUtil.getHttpParam(request(), "export");// 是否导出
		if(export!=null && export.equals("1")){
			nStart = 0;
			nLimit = 65534;
		}
		
		Logger.info(sql.toString());
		//下面简单的分页，需要重构，应该由 Dao-Sevice去实现。
		int rowCount = Ebean.getServer(GlobalDBControl.getDB()).createQuery(SubMeasureOrders .class, sql.toString()).findRowCount();
		List<SubMeasureOrders> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.createQuery(SubMeasureOrders .class, sql.toString())
				.setFirstRow(nStart).setMaxRows(nLimit)
				.orderBy("idd desc")
				.findList();
				
		if(export!=null && export.equals("1")){
			String fileName = "SubMeasureOrders"+simp.format( new Date())+".xls";
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
		PagedObject<SubMeasureOrders> po= new PagedObject( ulist,nPages,rowCount );
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(po) );
	}

	//列表
	public static Result listAll(){
		List<SubMeasureOrders> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.find(SubMeasureOrders .class)
				.orderBy("idd")
				.findList();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(ulist) );
	}
	
	//派单
	public static Result outOrder(){
		String idd = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "idd");
		int execute = Ebean.getServer(GlobalDBControl.getDB()).createUpdate(SubMeasureOrders.class, "update sub_measure_orders set status = 4 where idd= '"+idd+"'").execute();
		if(execute==0) {
			return ok(Json.toJson(0));
		}
		SubMeasureOrders smo = Ebean.getServer(GlobalDBControl.getDB()).createQuery(SubMeasureOrders.class, "find sub_measure_orders where idd='"+idd+"'").findUnique();
		SubMsg sm = new SubMsg();
		sm.setOid(smo.getIdd());
		sm.setPhone(smo.getPhone());
		sm.setContent("已向您分派安装师傅，请保持电话通畅，以方便安装师傅联系您");
		sm.setAct(2);
		sm.setAddtime(new Date());
		Ebean.getServer(GlobalDBControl.getDB()).save(sm);
		WxGzhService.sendGzhMessage(smo.getPhone(), "尊敬的用户，您预约成功", "预约测量", "已派单", StringUtil.getDateTimeStr(new Date()), "已为您分派安装师傅，请保持电话通畅，以方便安装师傅联系您");
		return ok(Json.toJson(execute));
	}
	
	//删除
	public static Result delete(int idd){
		String sql = "delete from SubMeasureOrders where idd =:idd";
		Ebean.getServer(GlobalDBControl.getDB())
				.createUpdate(SubMeasureOrders .class, sql).setParameter("idd", idd).execute();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson("操作成功") );
	}
	
	//获取 单个
	public static Result get(int idd){
		SubMeasureOrders submeasureorders = Ebean.getServer(GlobalDBControl.getDB())
				.find(SubMeasureOrders .class).where().eq("idd", idd).findUnique();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(submeasureorders) );
	}

	//新增 / 修改
	public static Result modify(){
		String idd = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "idd");
		int nidd = StringTool.GetInt(idd, 0);
		
		
		
		
		
		String phone = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "phone");
		
		
		
		String oid = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "oid");
		
		
		
		String measurename = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "measurename");
		
		
		
		String measurearea = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "measurearea");
		
		
		
		String measureaddress = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "measureaddress");
		
		
		
		String measurephone = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "measurephone");
		
		
		
		String status = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "status");
		
		
		
		String money = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "money");
		
		
		
		String addtime = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "addtime");
		
		
		
		String measuretime = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "measuretime");
		
		
		
		String prepayid = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "prepayid");
		
		
		
		String updatetime = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "updatetime");
		
		
		
		String operation = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "operation");
		
		SubMeasureOrders submeasureorders ;
		if(  operation.equals("add") )	// 新增
			submeasureorders = new SubMeasureOrders ();
		else			//修改
			submeasureorders = Ebean.getServer(GlobalDBControl.getDB())
					.find(SubMeasureOrders .class).where().eq("idd", idd).findUnique();
		
		if( submeasureorders!=null ){
			//赋值,字符串类型可以直接赋值，其他类型需要转换 ！！！！
			
				 
				
				
			
				
					submeasureorders. setPhone ( phone );
				 
				
				
			
				 
				
				
					submeasureorders. setOid ( StringTool.GetInt(oid,0) );
				
			
				
					submeasureorders. setMeasurename ( measurename );
				 
				
				
			
				
					submeasureorders. setMeasurearea ( measurearea );
				 
				
				
			
				
					submeasureorders. setMeasureaddress ( measureaddress );
				 
				
				
			
				
					submeasureorders. setMeasurephone ( measurephone );
				 
				
				
			
				 
				
				
					submeasureorders. setStatus ( StringTool.GetInt(status,0) );
				
			
				 
				
				
					submeasureorders. setMoney ( StringTool.GetInt(money,0) );
				
			
				 
				
					submeasureorders. setAddtime ( StringUtil.getDate(addtime) );
				
				
			
				
					submeasureorders. setMeasuretime ( measuretime );
				 
				
				
			
				
					submeasureorders. setPrepayid ( prepayid );
				 
				
				
			
				 
				
					submeasureorders. setUpdatetime ( StringUtil.getDate(updatetime) );
				
				
			
			Ebean.getServer(GlobalDBControl.getDB()).save( submeasureorders );
			response().setHeader("Cache-Control", "no-cache");
			return ok( Json.toJson(submeasureorders) );
		}

		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(null) );
	}
	
	static int num=0;
	public static File exportList(List<SubMeasureOrders> infoList,String fileNameChine) throws UnsupportedEncodingException {
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
				
					"idd"
					
				
					
					,"手机号"
				
					
					,"订单ID"
				
					
					,"测量联系人姓名"
				
					
					,"测量地址"
				
					
					,"测量详细地址"
				
					
					,"测量联系人手机号"
				
					
					,"状态"
				
					
					,"支付金额"
				
					
					,"添加时间"
				
					
					,"预约测量时间"
				
					
					,"预订单"
				
					
					,"支付时间"
				
			};

		// 生成表头
		helper.setRowIndex(rowIndex++);
		helper.generateHeader(sheet, titles, 0, StringUtils.EMPTY,
				StringUtils.EMPTY);

		// 循环生成数据行
		for (int i = 0; i < infoList.size(); i++) {
			colIndex = 0;
			SubMeasureOrders info = (SubMeasureOrders) infoList.get(i);
			Row row = sheet.createRow(rowIndex++);
			
			
				helper.createStringCell( row , colIndex++, ""+info. getIdd () );
			
				helper.createStringCell( row , colIndex++, ""+info. getPhone () );
			
				helper.createStringCell( row , colIndex++, ""+info. getOid () );
			
				helper.createStringCell( row , colIndex++, ""+info. getMeasurename () );
			
				helper.createStringCell( row , colIndex++, ""+info. getMeasurearea () );
			
				helper.createStringCell( row , colIndex++, ""+info. getMeasureaddress () );
			
				helper.createStringCell( row , colIndex++, ""+info. getMeasurephone () );
			
				helper.createStringCell( row , colIndex++, ""+info. getStatus () );
			
				helper.createStringCell( row , colIndex++, ""+info. getMoney () );
			
				helper.createStringCell( row , colIndex++, ""+info. getAddtime () );
			
				helper.createStringCell( row , colIndex++, ""+info. getMeasuretime () );
			
				helper.createStringCell( row , colIndex++, ""+info. getPrepayid () );
			
				helper.createStringCell( row , colIndex++, ""+info. getUpdatetime () );
			
		}
		//在application.conf中配置的路径
		String path = Configuration.root().getString("export.path");
		int numStra = num++;//静态值，区分文件名
		File file = new File(path);
		file.mkdir();//判断文件夹是否存在，不存在就创建
		
		FileOutputStream out = null;
		String fileName = path + "SubMeasureOrders" + System.currentTimeMillis() + numStra + ".xls";
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
				
					
					
				
					"手机号"
					
				
					
					,"订单ID"
				
					
					,"测量联系人姓名"
				
					
					,"测量地址"
				
					
					,"测量详细地址"
				
					
					,"测量联系人手机号"
				
					
					,"状态"
				
					
					,"支付金额"
				
					
					,"添加时间"
				
					
					,"预约测量时间"
				
					
					,"预订单"
				
					
					,"支付时间"
				
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
			
			List<SubMeasureOrders> list = new ArrayList<SubMeasureOrders>();
			 for (int i = 1; i <= lastRowNumber; i++) {
				 //批量添加
                 Row row = sheet.getRow(i);
                 if (row == null) {
                     break;
                 }
                 
                 SubMeasureOrders fc = new SubMeasureOrders();
                 int j = 0 ;
                 
					 
					
					
					
				
					
						fc. setPhone ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					 
					
					
						fc. setOid ( StringTool.GetInt(StringUtil.cellValueToString(row.getCell(j)),0) );
					
					
						j=j+1;
					
				
					
						fc. setMeasurename ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					
						fc. setMeasurearea ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					
						fc. setMeasureaddress ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					
						fc. setMeasurephone ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					 
					
					
						fc. setStatus ( StringTool.GetInt(StringUtil.cellValueToString(row.getCell(j)),0) );
					
					
						j=j+1;
					
				
					 
					
					
						fc. setMoney ( StringTool.GetInt(StringUtil.cellValueToString(row.getCell(j)),0) );
					
					
						j=j+1;
					
				
					 
					
						//fc. setAddtime ( StringUtil.getDate( StringUtil.cellValueToString(row.getCell(j) ) );
					
					
					
						j=j+1;
					
				
					
						fc. setMeasuretime ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					
						fc. setPrepayid ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					 
					
						//fc. setUpdatetime ( StringUtil.getDate( StringUtil.cellValueToString(row.getCell(j) ) );
					
					
					
						j=j+1;
					
				
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
	
}