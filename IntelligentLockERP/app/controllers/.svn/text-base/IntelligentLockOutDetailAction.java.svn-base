
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
public class IntelligentLockOutDetailAction extends Controller {
  
	//进入页面列表；
	public static Result view() {
		
		String outid = StringUtil.getHttpParam(request(), "outid");
		
		if( !StringTool.isNull(outid) )
			return ok( IntelligentLockOutDetailListSpecial.render(outid) );
		
		return ok( IntelligentLockOutDetailList.render() );
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
		sql.append("find IntelligentLockOutDetail where 1=1 ");
		
		String idd = StringUtil.getHttpParam(request(), "idd");
		if(idd==null)
			idd = "";	
		if ( !( idd .equals("") ) && !( idd .equals("undefined") ) ) 
			sql.append(" and ( idd= '" + idd + "'  )");
		
		String operatornumber = StringUtil.getHttpParam(request(), "operatornumber");
		if(operatornumber==null)
			operatornumber = "";	
		if ( !( operatornumber .equals("") ) && !( operatornumber .equals("undefined") ) ) 
			sql.append(" and ( operatornumber= '" + operatornumber + "'  )");
		
		String productid = StringUtil.getHttpParam(request(), "productid");
		if(productid==null)
			productid = "";	
		if ( !( productid .equals("") ) && !( productid .equals("undefined") ) ) 
			sql.append(" and ( productid= '" + productid + "'  )");
		
		String title = StringUtil.getHttpParam(request(), "title");
		if(title==null)
			title = "";	
		if ( !( title .equals("") ) && !( title .equals("undefined") ) ) 
			sql.append(" and ( title= '" + title + "'  )");
		
		String model = StringUtil.getHttpParam(request(), "model");
		if(model==null)
			model = "";	
		if ( !( model .equals("") ) && !( model .equals("undefined") ) ) 
			sql.append(" and ( model= '" + model + "'  )");
		
		String spec = StringUtil.getHttpParam(request(), "spec");
		if(spec==null)
			spec = "";	
		if ( !( spec .equals("") ) && !( spec .equals("undefined") ) ) 
			sql.append(" and ( spec= '" + spec + "'  )");
		
		String amount = StringUtil.getHttpParam(request(), "amount");
		if(amount==null)
			amount = "";	
		if ( !( amount .equals("") ) && !( amount .equals("undefined") ) ) 
			sql.append(" and ( amount= '" + amount + "'  )");
		
		String price = StringUtil.getHttpParam(request(), "price");
		if(price==null)
			price = "";	
		if ( !( price .equals("") ) && !( price .equals("undefined") ) ) 
			sql.append(" and ( price= '" + price + "'  )");
		
		String totalprice = StringUtil.getHttpParam(request(), "totalprice");
		if(totalprice==null)
			totalprice = "";	
		if ( !( totalprice .equals("") ) && !( totalprice .equals("undefined") ) ) 
			sql.append(" and ( totalprice= '" + totalprice + "'  )");
		
		String channel = StringUtil.getHttpParam(request(), "channel");
		if(channel==null)
			channel = "";	
		if ( !( channel .equals("") ) && !( channel .equals("undefined") ) ) 
			sql.append(" and ( channel= '" + channel + "'  )");
		
		String type = StringUtil.getHttpParam(request(), "type");
		if(type==null)
			type = "";	
		if ( !( type .equals("") ) && !( type .equals("0") ) ) 
			sql.append(" and ( type= '" + type + "'  )");
		
		String salenumber = StringUtil.getHttpParam(request(), "salenumber");
		if(salenumber==null)
			salenumber = "";	
		if ( !( salenumber .equals("") ) && !( salenumber .equals("undefined") ) ) 
			sql.append(" and ( salenumber= '" + salenumber + "'  )");
		
		String remark = StringUtil.getHttpParam(request(), "remark");
		if(remark==null)
			remark = "";	
		if ( !( remark .equals("") ) && !( remark .equals("undefined") ) ) 
			sql.append(" and ( remark= '" + remark + "'  )");
		
		/*String addtime = StringUtil.getHttpParam(request(), "addtime");
		if(addtime==null)
			addtime = "";	
		if ( !( addtime .equals("") ) && !( addtime .equals("undefined") ) ) 
			sql.append(" and ( addtime= '" + addtime + "'  )");*/
		
		String operator = StringUtil.getHttpParam(request(), "operator");
		if(operator==null)
			operator = "";	
		if ( !( operator .equals("") ) && !( operator .equals("undefined") ) ) 
			sql.append(" and ( operator= '" + operator + "'  )");
		
		String outid = StringUtil.getHttpParam(request(), "outid");
		if(outid==null)
			outid = "";	
		if ( !( outid .equals("") ) && !( outid .equals("undefined") ) ) 
			sql.append(" and ( outid= '" + outid + "'  )");
		
		String export = StringUtil.getHttpParam(request(), "export");// 是否导出
		if(export!=null && export.equals("1")){
			nStart = 0;
			nLimit = 65534;
		}
		
		Logger.info(sql.toString());
		//下面简单的分页，需要重构，应该由 Dao-Sevice去实现。
		int rowCount = Ebean.getServer(GlobalDBControl.getDB()).createQuery(IntelligentLockOutDetail .class, sql.toString()).findRowCount();
		List<IntelligentLockOutDetail> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.createQuery(IntelligentLockOutDetail .class, sql.toString())
				.setFirstRow(nStart).setMaxRows(nLimit)
				.orderBy("idd desc")
				.findList();
				
		if(export!=null && export.equals("1")){
			String fileName = "IntelligentLockOutDetail"+simp.format( new Date())+".xls";
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
		PagedObject<IntelligentLockOutDetail> po= new PagedObject( ulist,nPages,rowCount );
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(po) );
	}

	//列表
	public static Result listAll(){
		List<IntelligentLockOutDetail> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.find(IntelligentLockOutDetail .class)
				.orderBy("idd")
				.findList();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(ulist) );
	}
	
	//修改 （为出库明细新增 修改备注的功能 其它信息不支持修改）
	public static Result modify(){
		String idd = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "idd");
		int nidd = StringTool.GetInt(idd, 0);
		
		String remark = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "remark");
		
		IntelligentLockOutDetail intelligentlockoutdetail = Ebean.getServer(GlobalDBControl.getDB())
				.find(IntelligentLockOutDetail .class).where().eq("idd", nidd).findUnique();
		
		if( intelligentlockoutdetail!=null ){
			
			intelligentlockoutdetail. setRemark ( remark );
			
			Ebean.getServer(GlobalDBControl.getDB()).save( intelligentlockoutdetail );
			response().setHeader("Cache-Control", "no-cache");
			return ok( Json.toJson(intelligentlockoutdetail) );
		}
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(null) );
	}
	
	//获取 单个
	public static Result get(int idd){
		IntelligentLockOutDetail intelligentlockoutdetail = Ebean.getServer(GlobalDBControl.getDB())
				.find(IntelligentLockOutDetail .class).where().eq("idd", idd).findUnique();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(intelligentlockoutdetail) );
	}
	
	//删除
	/*public static Result delete(int idd){
		String sql = "delete from IntelligentLockOutDetail where idd =:idd";
		Ebean.getServer(GlobalDBControl.getDB())
				.createUpdate(IntelligentLockOutDetail .class, sql).setParameter("idd", idd).execute();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson("操作成功") );
	}
	
	//新增 / 修改
	public static Result modify(){
		String idd = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "idd");
		int nidd = StringTool.GetInt(idd, 0);
		
		String operatornumber = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "operatornumber");
		
		String productid = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "productid");
		
		String title = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "title");
		
		String model = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "model");
		
		String spec = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "spec");
		
		String amount = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "amount");
		
		String price = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "price");
		
		String totalprice = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "totalprice");
		
		String channel = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "channel");
		
		String type = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "type");
		
		String salenumber = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "salenumber");
		
		String remark = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "remark");
		
		String addtime = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "addtime");
		
		String operator = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "operator");
		
		String outid = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "outid");
		
		String operation = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "operation");
		
		IntelligentLockOutDetail intelligentlockoutdetail ;
		if(  operation.equals("add") )	// 新增
			intelligentlockoutdetail = new IntelligentLockOutDetail ();
		else			//修改
			intelligentlockoutdetail = Ebean.getServer(GlobalDBControl.getDB())
					.find(IntelligentLockOutDetail .class).where().eq("idd", idd).findUnique();
		
		if( intelligentlockoutdetail!=null ){
			//赋值,字符串类型可以直接赋值，其他类型需要转换 ！！！！
				
			intelligentlockoutdetail. setOperatornumber ( operatornumber );
		
			intelligentlockoutdetail. setProductid ( StringTool.GetInt(productid,0) );
		
			intelligentlockoutdetail. setTitle ( title );
		 
			intelligentlockoutdetail. setModel ( model );
		
			intelligentlockoutdetail. setSpec ( spec );
		 
			intelligentlockoutdetail. setAmount ( StringTool.GetInt(amount,0) );
		
			intelligentlockoutdetail. setPrice ( StringTool.GetDouble(price,0.0 ));
		
			intelligentlockoutdetail. setTotalprice ( StringTool.GetDouble(totalprice,0.0) );
		
			intelligentlockoutdetail. setRemark ( remark );
		 
			intelligentlockoutdetail. setAddtime ( StringUtil.getDate(addtime) );
			
			intelligentlockoutdetail. setOperator ( operator );
		
			intelligentlockoutdetail. setOutid ( StringTool.GetInt(outid,0) );
			
			Ebean.getServer(GlobalDBControl.getDB()).save( intelligentlockoutdetail );
			response().setHeader("Cache-Control", "no-cache");
			return ok( Json.toJson(intelligentlockoutdetail) );
		}

		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(null) );
	}*/
	
	//以Excel的格式导出出库单明细数据
	static int num=0;
	public static File exportList(List<IntelligentLockOutDetail> infoList,String fileNameChine) throws UnsupportedEncodingException {
		Workbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet("出库单明细");
		
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
			
			sheet.setColumnWidth( 15 , 6000 );
		
		ExcelGenerateHelper helper = ExcelGenerateHelper.getInstance(wb);

		// 获取行索引
		int rowIndex = 0;
		int colIndex = 0;

		// 设置表头
		String[] titles = { 
				
			"idd"
			
			,"操作号"
		
			,"产品ID"
		
			,"产品名称"
			
			,"型号"
			
			,"规格"
			
			,"总数"
			
			,"单价"
			
			,"总价"
			
			,"出货渠道"
			
			,"类型"
			
			,"外部销售平台订单号"
			
			,"备注"
			
			,"添加时间"
			
			,"操作人"
			
			,"出库单ID"
		};
		// 生成表头
		helper.setRowIndex(rowIndex++);
		helper.generateHeader(sheet, titles, 0, StringUtils.EMPTY,
				StringUtils.EMPTY);

		// 循环生成数据行
		for (int i = 0; i < infoList.size(); i++) {
			colIndex = 0;
			IntelligentLockOutDetail info = (IntelligentLockOutDetail) infoList.get(i);
			Row row = sheet.createRow(rowIndex++);
			
			helper.createStringCell( row , colIndex++, ""+info. getIdd () );
		
			helper.createStringCell( row , colIndex++, ""+info. getOperatornumber () );
		
			helper.createStringCell( row , colIndex++, ""+info. getProductid () );
		
			helper.createStringCell( row , colIndex++, ""+info. getTitle () );
		
			helper.createStringCell( row , colIndex++, ""+info. getModel () );
		
			helper.createStringCell( row , colIndex++, ""+info. getSpec () );
		
			helper.createStringCell( row , colIndex++, ""+info. getAmount () );
		
			helper.createStringCell( row , colIndex++, ""+info. getPrice () );
		
			helper.createStringCell( row , colIndex++, ""+info. getTotalprice () );
			
			helper.createStringCell( row , colIndex++, ""+info. getChannel () );	//出货渠道
			
			helper.createStringCell( row , colIndex++, ""+info. getType () );	//类型
			
			helper.createStringCell( row , colIndex++, ""+info. getSalenumber () );	//外部销售平台订单号
		
			helper.createStringCell( row , colIndex++, ""+info. getRemark () );
		
			helper.createStringCell( row , colIndex++, ""+simp.format(info. getAddtime ()));
		
			helper.createStringCell( row , colIndex++, ""+info. getOperator () );
		
			helper.createStringCell( row , colIndex++, ""+info. getOutid () );
		}
		//在application.conf中配置的路径
		String path = Configuration.root().getString("export.path");
		int numStra = num++;//静态值，区分文件名
		File file = new File(path);
		file.mkdir();//判断文件夹是否存在，不存在就创建
		
		FileOutputStream out = null;
		String fileName = path + "出库明细" + System.currentTimeMillis() + numStra + ".xls";
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
					
		"操作号"
		
		,"产品ID"
		
		,"产品名称"
	
		,"型号"
	
		,"规格"
	
		,"总数"
	
		,"单价"
		
		,"总价"
		
		,"出货渠道"
		
		,"类型"
		
		,"外部销售平台订单号"
		
		,"备注"
		
		,"添加时间"
	
		,"操作人"
		
		,"出库单ID"
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
			
			List<IntelligentLockOutDetail> list = new ArrayList<IntelligentLockOutDetail>();
			 for (int i = 1; i <= lastRowNumber; i++) {
				 //批量添加
                 Row row = sheet.getRow(i);
                 if (row == null) {
                     break;
                 }
                 
                 IntelligentLockOutDetail fc = new IntelligentLockOutDetail();
                 int j = 0 ;
				
						fc. setOperatornumber ( StringUtil.cellValueToString(row.getCell(j)) );
					 
						j=j+1;
					
						fc. setProductid ( StringTool.GetInt(StringUtil.cellValueToString(row.getCell(j)),0) );
					
						j=j+1;
					
						fc. setTitle ( StringUtil.cellValueToString(row.getCell(j)) );
					
						j=j+1;
					
						fc. setModel ( StringUtil.cellValueToString(row.getCell(j)) );
					
						j=j+1;
					
						fc. setSpec ( StringUtil.cellValueToString(row.getCell(j)) );
					
						j=j+1;
					
						fc. setAmount ( StringTool.GetInt(StringUtil.cellValueToString(row.getCell(j)),0) );
					
						j=j+1;
					
						fc. setPrice ( StringTool.GetDouble(StringUtil.cellValueToString(row.getCell(j)),0.0) );
					
						j=j+1;
					
						fc. setTotalprice ( StringTool.GetDouble(StringUtil.cellValueToString(row.getCell(j)),0.0) );
					
						j=j+1;
					
						fc. setChannel ( StringUtil.cellValueToString(row.getCell(j)) );
					
						j=j+1;
						
						fc. setType ( StringUtil.cellValueToString(row.getCell(j)) );
					
						j=j+1;
					
						fc. setSalenumber( StringUtil.cellValueToString(row.getCell(j)) );
						
						j=j+1;
					
						fc. setRemark ( StringUtil.cellValueToString(row.getCell(j)) );
						
						j=j+1;
					
						//fc. setAddtime ( StringUtil.getDate( StringUtil.cellValueToString(row.getCell(j) ) );
					
						j=j+1;
					
						fc. setOperator ( StringUtil.cellValueToString(row.getCell(j)) );
					 
						j=j+1;
					
						fc. setOutid ( StringTool.GetInt(StringUtil.cellValueToString(row.getCell(j)),0) );
					
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