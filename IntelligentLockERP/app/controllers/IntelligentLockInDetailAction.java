
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.avaje.ebean.Ebean;

import app.service.VipService;
import jdk.nashorn.internal.objects.Global;
import models.IntelligentLockIn;
import models.IntelligentLockInDetail;
import models.IntelligentLockProduct;
import models.IntelligentLockProductCode;
import models.IntelligentLockStock;
import models.IntelligentLockStockInWater;
import models.ResponseError;
import models.SmartDevice;
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
import util.LogUtil;
import util.PagedObject;
import util.RuleNumberFormatUtil;
import util.StringUtil;
import util.classEntity.StringTool;
import views.html.page.IntelligentLockInDetailList;
import views.html.page.IntelligentLockInDetailListSpecial;

@Security.Authenticated(Secured.class)
public class IntelligentLockInDetailAction extends Controller {
	//进入页面列表；
	public static Result view() {
		String inid = StringUtil.getHttpParam(request(), "inid");
		if(!StringUtil.isNull(inid))
			return ok(IntelligentLockInDetailListSpecial.render(inid));
		return ok( IntelligentLockInDetailList.render() );
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
		sql.append("find IntelligentLockInDetail where 1=1 ");
		
		String idd = StringUtil.getHttpParam(request(), "idd");
		if(idd==null)
			idd = "";	
		if ( !( idd .equals("") ) && !( idd .equals("undefined") ) ) 
			sql.append(" and ( idd= '" + idd + "'  )");
		
		String inid = StringUtil.getHttpParam(request(), "inid");
		if(inid==null)
			inid = "";	
		if ( !( inid .equals("") ) && !( inid .equals("undefined") ) ) 
			sql.append(" and ( inid= '" + inid + "'  )");
		
		String productid = StringUtil.getHttpParam(request(), "productid");
		if(productid==null)
			productid = "";	
		if ( !( productid .equals("") ) && !( productid .equals("undefined") ) ) 
			sql.append(" and ( productid= '" + productid + "'  )");
		
		String deviceid = StringUtil.getHttpParam(request(), "deviceid");
		if(deviceid==null)
			deviceid = "";	
		if ( !( deviceid .equals("") ) && !( deviceid .equals("undefined") ) ) 
			sql.append(" and ( deviceid= '" + deviceid + "'  )");
		
		String producttype = StringUtil.getHttpParam(request(), "producttype");
		if(producttype==null)
			producttype = "";	
		if ( !( producttype .equals("") ) && !( producttype .equals("undefined") ) ) 
			sql.append(" and ( producttype= '" + producttype + "'  )");
		
		String title = StringUtil.getHttpParam(request(), "title");
		if(title==null)
			title = "";	
		if ( !( title .equals("") ) && !( title .equals("undefined") ) ) 
			sql.append(" and ( title like '%" + title + "%'  )");
		
		String model = StringUtil.getHttpParam(request(), "model");
		if(model==null)
			model = "";	
		if ( !( model .equals("") ) && !( model .equals("undefined") ) ) 
			sql.append(" and ( model like '%" + model + "%'  )");
		
		String spec = StringUtil.getHttpParam(request(), "spec");
		if(spec==null)
			spec = "";	
		if ( !( spec .equals("") ) && !( spec .equals("undefined") ) ) 
			sql.append(" and ( spec= '" + spec + "'  )");
		
		String manufacturer = StringUtil.getHttpParam(request(), "manufacturer");
		if(manufacturer==null)
			manufacturer = "";	
		if ( !( manufacturer .equals("") ) && !( manufacturer .equals("undefined") ) ) 
			sql.append(" and ( manufacturer= '" + manufacturer + "'  )");
		
		String batchid = StringUtil.getHttpParam(request(), "batchid");
		if(batchid==null)
			batchid = "";	
		if ( !( batchid .equals("") ) && !( batchid .equals("undefined") ) ) 
			sql.append(" and ( batchid= '" + batchid + "'  )");
		
		String productusage = StringUtil.getHttpParam(request(), "productusage");
		if(productusage==null)
			productusage = "";	
		if ( !( productusage .equals("") ) && !( productusage .equals("undefined") ) ) 
			sql.append(" and ( productusage= '" + productusage + "'  )");
		
		String inType = StringUtil.getHttpParam(request(), "inType");
		if(inType==null)
			inType = "";	
		if ( !( inType .equals("") ) && !( inType .equals("undefined") ) ) 
			sql.append(" and ( inType= '" + inType + "'  )");
		
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
		
		String remark = StringUtil.getHttpParam(request(), "remark");
		if(remark==null)
			remark = "";	
		if ( !( remark .equals("") ) && !( remark .equals("undefined") ) ) 
			sql.append(" and ( remark= '" + remark + "'  )");
		
		String addtime = StringUtil.getHttpParam(request(), "addtime");
		if(addtime==null)
			addtime = "";	
		if ( !( addtime .equals("") ) && !( addtime .equals("undefined") ) ) 
			sql.append(" and ( addtime like '%" + addtime + "%'  )");
		
		String operator = StringUtil.getHttpParam(request(), "operator");
		if(operator==null)
			operator = "";	
		if ( !( operator .equals("") ) && !( operator .equals("undefined") ) ) 
			sql.append(" and ( operator= '" + operator + "'  )");
		
		String operatenumber = StringUtil.getHttpParam(request(), "operatenumber");
		if(operatenumber==null)
			operatenumber = "";	
		if ( !( operatenumber .equals("") ) && !( operatenumber .equals("undefined") ) ) 
			sql.append(" and ( operatenumber= '" + operatenumber + "'  )");
		
		String export = StringUtil.getHttpParam(request(), "export");// 是否导出
		if(export!=null && export.equals("1")){
			nStart = 0;
			nLimit = 65534;
		}
		
		Logger.info(sql.toString());
		//下面简单的分页，需要重构，应该由 Dao-Sevice去实现。
		int rowCount = Ebean.getServer(GlobalDBControl.getDB()).createQuery(IntelligentLockInDetail .class, sql.toString()).findRowCount();
		List<IntelligentLockInDetail> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.createQuery(IntelligentLockInDetail.class, sql.toString())
				.setFirstRow(nStart).setMaxRows(nLimit)
				.orderBy("idd desc")
				.findList();
				
		if(export!=null && export.equals("1")){
			String fileName = "IntelligentLockInDetail"+simp.format( new Date())+".xls";
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
		PagedObject<IntelligentLockInDetail> po= new PagedObject( ulist,nPages,rowCount );
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(po) );
	}

	//列表
	public static Result listAll(){
		List<IntelligentLockInDetail> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.find(IntelligentLockInDetail .class)
				.orderBy("idd")
				.findList();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(ulist) );
	}
	
	//删除
	public static Result delete(int idd){
		String sql = "delete from IntelligentLockInDetail where idd =:idd";
		Ebean.beginTransaction();
		try {
			Ebean.getServer(GlobalDBControl.getDB()).createUpdate(IntelligentLockInDetail .class, sql).setParameter("idd", idd).execute();
			Ebean.commitTransaction();
		}catch (Exception e) {
			LogUtil.writeLog("入库详情 前端发起删除操作失败（事务已回滚）"+e.getMessage(), "InDetail-ErrorLog");
			Ebean.rollbackTransaction();
		} finally {
			Ebean.endTransaction();
		}
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson("操作成功") );
	}
	
	//获取 单个
	public static Result get(int idd){
		IntelligentLockInDetail intelligentlockindetail = Ebean.getServer(GlobalDBControl.getDB())
				.find(IntelligentLockInDetail .class).where().eq("idd", idd).findUnique();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(intelligentlockindetail) );
	}

	//新增 / 修改
	public static Result modify(){
		String idd = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "idd");
		int nidd = StringTool.GetInt(idd, 0);
		
		
		
		
		
		String inid = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "inid");
		
		
		
		String productid = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "productid");
		
		
		
		String producttype = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "producttype");
		
		
		
		String title = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "title");
		
		
		
		String model = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "model");
		
		
		
		String spec = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "spec");
		
		
		
		String manufacturer = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "manufacturer");
		
		
		
		String batchid = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "batchid");
		
		
		
		String productusage = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "productusage");
		
		
		
		String inType = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "inType");
		
		
		
		String amount = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "amount");
		
		
		
		String price = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "price");
		
		
		
		String totalprice = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "totalprice");
		
		
		
		String remark = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "remark");
		
		
		
		String addtime = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "addtime");
		
		
		
		String operator = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "operator");
		
		
		
		String operatenumber = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "operatenumber");
		
		
		
		String operation = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "operation");
		Ebean.beginTransaction();
		IntelligentLockInDetail intelligentlockindetail ;
		if(  operation.equals("add") )	// 新增
			intelligentlockindetail = new IntelligentLockInDetail ();
		else			//修改
			intelligentlockindetail = Ebean.getServer(GlobalDBControl.getDB())
					.find(IntelligentLockInDetail .class).where().eq("idd", idd).findUnique();
		
		if( intelligentlockindetail!=null ){
			//赋值,字符串类型可以直接赋值，其他类型需要转换 ！！！！
			
				 
				
				
			
				 
				
				
					intelligentlockindetail. setInid ( StringTool.GetInt(inid,0) );
				
			
				 
				
				
					intelligentlockindetail. setProductid ( StringTool.GetInt(productid,0) );
				
			
				
					intelligentlockindetail. setProducttype ( producttype );
				 
				
				
			
				
					intelligentlockindetail. setTitle ( title );
				 
				
				
			
				
					intelligentlockindetail. setModel ( model );
				 
				
				
			
				
					intelligentlockindetail. setSpec ( spec );
				 
				
				
			
				
					intelligentlockindetail. setManufacturer ( manufacturer );
				 
				
				
			
				
					intelligentlockindetail. setBatchid ( batchid );
				 
				
				
			
				
					intelligentlockindetail. setProductusage ( productusage );
				 
				
				
			
				
					intelligentlockindetail. setInType ( inType );
				 
				
				
			
				 
				
				
					intelligentlockindetail. setAmount ( StringTool.GetInt(amount,0) );
				
			
				 
				
				
					intelligentlockindetail. setPrice ( StringTool.GetDouble(price,0.0) );
				
			
				 
				
				
					intelligentlockindetail. setTotalprice ( StringTool.GetDouble(totalprice,0.0) );
				
			
				
					intelligentlockindetail. setRemark ( remark );
				 
				
				
			
				 
				
					intelligentlockindetail. setAddtime ( StringUtil.getDate(addtime) );
				
				
			
				
					intelligentlockindetail. setOperator ( operator );
				 
				
				
			
				
					intelligentlockindetail. setOperatenumber ( operatenumber );
				 
				
				
			try {
				Ebean.getServer(GlobalDBControl.getDB()).save( intelligentlockindetail );
				Ebean.commitTransaction();
			} catch (Exception e) {
				LogUtil.writeLog("前端 提交/更新 失败，事务已回滚（事务已回滚）"+e.getMessage(), "InDetail-ErrorLog");
				Ebean.rollbackTransaction();
			}finally {
				Ebean.endTransaction();
			}
			response().setHeader("Cache-Control", "no-cache");
			return ok( Json.toJson(intelligentlockindetail) );
		}

		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(null) );
	}
	
	static int num=0;
	public static File exportList(List<IntelligentLockInDetail> infoList,String fileNameChine) throws UnsupportedEncodingException {
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
		
//			sheet.setColumnWidth( 14 , 6000 );
//		
//			sheet.setColumnWidth( 15 , 6000 );
//		
//			sheet.setColumnWidth( 16 , 6000 );
//		
//			sheet.setColumnWidth( 17 , 6000 );
		
		ExcelGenerateHelper helper = ExcelGenerateHelper.getInstance(wb);

		// 获取行索引
		int rowIndex = 0;
		int colIndex = 0;

		// 设置表头
		String[] titles = { 
				
					"ID"
					
				
					
					,"入库单ID"
				
					
					,"产品ID"
				
					
//					,"产品类型"
				
					
					,"产品名称"
				
					
//					,"产品型号"
				
					
//					,"规格"
				
					
					,"生产厂家"
				
					
					,"生产批次"
				
					
//					,"产品用途"
				
					
					,"入库方式"
				
					
					,"数量"
				
					
					,"单价"
				
					
					,"总价"
				
					
					,"备注"
				
					
					,"添加时间"
				
					
					,"操作人"
				
					
					,"操作号"
				
			};

		// 生成表头
		helper.setRowIndex(rowIndex++);
		helper.generateHeader(sheet, titles, 0, StringUtils.EMPTY,
				StringUtils.EMPTY);

		// 循环生成数据行
		for (int i = 0; i < infoList.size(); i++) {
			colIndex = 0;
			IntelligentLockInDetail info = (IntelligentLockInDetail) infoList.get(i);
			Row row = sheet.createRow(rowIndex++);
			
			
				helper.createStringCell( row , colIndex++, ""+info. getIdd () );
			
				helper.createStringCell( row , colIndex++, ""+info. getInid () );
			
				helper.createStringCell( row , colIndex++, ""+info. getProductid () );
			
//				helper.createStringCell( row , colIndex++, ""+info. getProducttype () );
			
				helper.createStringCell( row , colIndex++, ""+info. getTitle () );
			
//				helper.createStringCell( row , colIndex++, ""+info. getModel () );
			
//				helper.createStringCell( row , colIndex++, ""+info. getSpec () );
			
				helper.createStringCell( row , colIndex++, ""+info. getManufacturer () );
			
				helper.createStringCell( row , colIndex++, ""+info. getBatchid () );
			
//				helper.createStringCell( row , colIndex++, ""+info. getProductusage () );
			
				helper.createStringCell( row , colIndex++, ""+info. getInType () );
			
				helper.createStringCell( row , colIndex++, ""+info. getAmount () );
			
				helper.createStringCell( row , colIndex++, ""+info. getPrice () );
			
				helper.createStringCell( row , colIndex++, ""+info. getTotalprice () );
			
				helper.createStringCell( row , colIndex++, ""+info. getRemark () );
			
				helper.createStringCell( row , colIndex++, ""+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(info. getAddtime ()) );
			
				helper.createStringCell( row , colIndex++, ""+info. getOperator () );
			
				helper.createStringCell( row , colIndex++, ""+info. getOperatenumber () );
			
		}
		//在application.conf中配置的路径
		String path = Configuration.root().getString("export.path");
		int numStra = num++;//静态值，区分文件名
		File file = new File(path);
		file.mkdir();//判断文件夹是否存在，不存在就创建
		
		FileOutputStream out = null;
		String fileName = path + "IntelligentLockInDetail" + System.currentTimeMillis() + numStra + ".xls";
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
				
					"产品名称"
					
					
					,"数量"
					
					
					,"单价"
					
					
					,"生产批次"
					
					
					,"唯一产品编码"
					
					
					,"设备ID"
					
					
					,"生产厂家"
					
					
					,"入库类型"

					
					,"备注"
					
				
		};
	
	static int MAX_LINES = 10000;
	public static Result upload(){
		String cid = AjaxHellper.getHttpParam(request(), "uploadidd");
		Integer ncid;
		int successNum = 0; //导入成功的数量
		MultipartFormData body = request().body().asMultipartFormData();
		FilePart file_excel = body.getFile("file_excel");
		String type = file_excel.getContentType();
		Logger.info("-------------------------上传文件类型为："+type);
		boolean hasFileType = false;//如果为false 则说明文件类型不对 反之则反
		List<String> fileTypeList = new ArrayList<String>();
		fileTypeList.add("application/vnd.ms-excel");
		fileTypeList.add("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		fileTypeList.add("application/vnd.openxmlformats-officedocument.spreadsheetml.template");
		fileTypeList.add("application/vnd.ms-excel.sheet.macroEnabled.12");
		fileTypeList.add("application/vnd.ms-excel.template.macroEnabled.12");
		fileTypeList.add("application/vnd.ms-excel.addin.macroEnabled.12");
		fileTypeList.add("application/vnd.ms-excel.sheet.binary.macroEnabled.12");
		for (String filetype : fileTypeList) {
			if(type.equalsIgnoreCase(filetype)) {
				hasFileType = true;
				break;
			}
		}
		
		if(!hasFileType) {
			ResponseError error = new ResponseError();
			error.setStatus(1);
			error.setMassage("上传文件必须是Excel文件");
			return ok(Json.toJson(error));
		}
		
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
			
			//开启事务
			Ebean.beginTransaction();
			try {
			List<IntelligentLockInDetail> list = new ArrayList<IntelligentLockInDetail>();
			
			//统一时间
			Date date = new Date();
			//总价记录 用于后面更改总价
			double totalprice4il = 0;
			//操作号
			String operatnumber = RuleNumberFormatUtil.createID();
			//流水号
			String serialnumber = StringUtil.getDateString();
			
			IntelligentLockIn il = new IntelligentLockIn();//创建一个入库单对象
			il.setAddtime(date);
			il.setOperator( Application.getSysUser().getLogin() );
			il.setOperatnumber( operatnumber );
			il.setSerialnumber(serialnumber);
			//先进行存储 后期会获取这条数据使用其id
			Ebean.getServer(GlobalDBControl.getDB()).save(il);//保存入库单对象
			//从入库表中根据操作号查询一条数据
			IntelligentLockIn ail = Ebean.getServer(GlobalDBControl.getDB())
				.find(IntelligentLockIn.class).where()
				.eq("operatnumber", operatnumber).findUnique();//保存完毕后 跟据操作号查询出这条入库单对象  用于后面获取它的ID
			 int inidd = 0;
			 int productCount = 0;//一次入库商品的总数量
			 Map<Integer,List<String>> productInNumberMap = new HashMap<Integer,List<String>>();//记录入库一种商品的数量以及价格 List.index0 数量 list.index1 价格 
			 Map<String,List<IntelligentLockInDetail>> inDetailMap = null;
			 for (int i = 1; i <= lastRowNumber; i++) {
				 //批量添加
                 Row row = sheet.getRow(i);
                 if (row == null) {
                     break;
                 }
                 
                 IntelligentLockInDetail fc = new IntelligentLockInDetail();//创建一个入库明细对象 此对象代表Excel中的一条数据
              
                 
             		/**获取入库单中对应的入库单ID*/
             		//入库单ID
                 	inidd = ail.getIdd();//将入库单ID保存
             		fc. setInid ( inidd );//给一条入库明细设置入库单ID
             		
             		int typeStatus = 0;//默认正常入库
					String inDBType = StringUtil.cellValueToString(row.getCell(7));//获取入库类型的
					if(inDBType.equalsIgnoreCase("样品入库")) {
						typeStatus = 1;
					}else if(inDBType.equalsIgnoreCase("退货入库")) {
						typeStatus = 2;
					}
             		
             		/**获取文件中商品名称*/
             		String productName = StringUtil.cellValueToString(row.getCell(0));//获取文件中的商品名称
             		if(productName.trim().equals("")) {
             			continue;
             		}
             		/**根据商品名称去查询产品表中有对应的商品 没有返回null*/
             		List<IntelligentLockProduct> productList = Ebean.getServer(GlobalDBControl.getDB())
         					.find(IntelligentLockProduct.class).where()
         					.eq("title", productName).findList();//跟据商品名称查询商品表返回一条商品对象
             		IntelligentLockProduct productId = null;
             		for (IntelligentLockProduct product : productList) {
						if(product.getStatus()==0) {
							productId = product;
							break;
						}
					}
					if(productId!=null) {//如果查询到了商品 则获取他的内容
						/**将查询到的商品的ID存入入库明细对象中*/
						//商品ID
						fc. setProductid ( productId.getIdd() );//给单条入库明细设置其商品ID
						fc. setTitle(productName);//设置商品名称
					}else {//如果商品表中没有查询到商品对象
						LogUtil.writeLog("\r\n产品表中未找到"+productName+"对应的商品名称（事务已回滚）", "InDetail-ErrorLog");
						ResponseError error = new ResponseError();
						error.setStatus(1);
						error.setMassage("产品表中未找到     '"+productName+"'  对应的商品名称,产品表被修改");
						return ok(Json.toJson(error));
					}
					
					//数量
					int count = StringTool.GetInt(StringUtil.cellValueToString(row.getCell(1)),0);
					productCount+=count;//记录总商品数
					fc. setAmount ( count );
					
					//单价
					double price = StringTool.GetDouble(StringUtil.cellValueToString(row.getCell(2)),0.0);
					fc. setPrice ( price );
					
					//总价
					fc. setTotalprice ( count*price );
					totalprice4il += count*price;
					
					//生产批次
					String batchId = StringUtil.cellValueToString(row.getCell(3)).trim();//获取文件中批次列数据
					//产品唯一编码
					String code = StringUtil.cellValueToString(row.getCell(4));//获取产品编码列字段
					//产品唯一编码可以为空
//					if(code.trim().equals("")) {
//						ResponseError error = new ResponseError();
//						error.setStatus(1);
//						error.setMassage("'"+productName+"'商品入库有误-----入库时请务必填写产品的唯一编码");
//						return ok(Json.toJson(error));
//					}else {
					if(!code.trim().equals("")) {//改变逻辑后加的判断 如果后期不在让产品唯一编码可以为空则删除这行并把上面的注释删掉
						IntelligentLockProductCode isDBHaveCode = Ebean.getServer(GlobalDBControl.getDB()).createQuery(IntelligentLockProductCode.class,"find intelligentlock_product_code where code = '"+code+"'").findUnique();
						if (isDBHaveCode!=null) {
							if(!inDBType.equals("退货入库")) {
								ResponseError error = new ResponseError();
								error.setStatus(1);
								error.setMassage("'"+productName+"'商品入库时，发现产品唯一编码重复，请您核对");
								return ok(Json.toJson(error));
							}
						}
					}
//					}
					
					String batchid2 = "";//产品编码表中对应商品ID的商品批次
					IntelligentLockProductCode ipc = null;//临时创建一个产品编码空对象 为了获取批次ID
					try {
						ipc = Ebean.getServer(GlobalDBControl.getDB()).find(IntelligentLockProductCode.class).where().eq("code", code).findUnique();//根据产品编码查询出唯一的商品
						if(ipc==null) {
							if(batchId=="") {//如果编码表中没有数据 同时 上传文件的批次为空 说明第一次入库 需要填写批次
											 //否则获取该商品的批次
								ResponseError error = new ResponseError();
								error.setStatus(1);
								error.setMassage("'"+productName+"' 是首次入库  生产批次不能为空 请您仔细检查您所有的的商品名称");
								return ok(Json.toJson(error));
							}
							IntelligentLockProductCode productCode = new IntelligentLockProductCode();//添加一个商品code数据
							productCode.setCode(code);
							productCode.setAddtime(date);
							productCode.setProductid(productId.getIdd());
							if(typeStatus==2) {
								productCode.setStatus(typeStatus);
							}else {
								productCode.setStatus(0);
							}
							productCode.setBatchid(batchId);
							productCode.setDeviceid(StringUtil.cellValueToString(row.getCell(5)));
							productCode.setOperatornumber(operatnumber);
							productCode.setRemark(StringUtil.cellValueToString(row.getCell(7)));
							Ebean.getServer(GlobalDBControl.getDB()).save(productCode);
							batchid2 = batchId;//将添加的Code中的批次ID保存
						}else if(typeStatus==2){
							try {
								Ebean.getServer(GlobalDBControl.getDB()).createUpdate(IntelligentLockProductCode.class, "update IntelligentLockProductCode set status='"+typeStatus+"' where code='"+code+"'").execute();
								batchId = ipc.getBatchid();
								batchid2 = ipc.getBatchid();
							}catch (Exception e) {
								LogUtil.writeLog("退货入库出现异常"+e.getMessage(), "InDB-ErrorLog");
								ResponseError error = new ResponseError();
								error.setStatus(1);
								error.setMassage("退货入库出现异常");
								return ok(Json.toJson(error));
							}
						}
					}catch(Exception e) {
						LogUtil.writeLog("唯一编码：'"+code+"'在编码表中不存在（事务已回滚）"+e.getMessage(), "InDB-ErrorLog");
						ResponseError error = new ResponseError();
						error.setStatus(1);
						error.setMassage("与产品编码表有误");
						return ok(Json.toJson(error));
					}
					LogUtil.writeLog("这里可以1", "testTrans");
					/**根据产品id查询库存表对应的数据是否存在
					 *  · 存在 - 已经有批次
					 *  · 不存在 - 库存中没有 且没有批次*/
					IntelligentLockStock ils = null;//库存表
					try {
						ils = Ebean.getServer(GlobalDBControl.getDB())//根据商品ID和批次ID查询出一条库存信息
								.createQuery(IntelligentLockStock.class,"find IntelligentLockStock where productid='"+productId.getIdd()+"' and batchid='"+batchid2+"'").findUnique();
					}catch(Exception e) {
						Ebean.rollbackTransaction();
						LogUtil.writeLog("库存表中查询不到 productid='"+productId.getIdd()+"' batchid='"+batchid2+"' 对应的数据（事务已回滚）"+e.getMessage(), "InDB-ErrorLog");
						ResponseError error = new ResponseError();
						error.setStatus(1);
						error.setMassage("库存表出现异常 ");
						return ok(Json.toJson(error));
					}
					if(code!=""&batchid2.equals("")&ils==null) {//如果跟据商品ID和批次ID查询库存表依然为null 则说明是第一次插入 需要设置批次ID
						ResponseError error = new ResponseError();
						error.setStatus(1);
						error.setMassage("'"+productName+"' 是首次入库  生产批次不能为空 请您仔细检查您所有的的商品名称");
						return ok(Json.toJson(error));
					}
					String updateBatchId = "";//库存表ID
					/** 数据中   null
					  *	数据库	 true
					  *	可以通过 - 更新数据*/
					if(batchId.equals("")&ils!=null) {
						/**
						 *	根据 商品ID 库存ID数据库通过商品ID查询到的集合中 最后一条数据 去更
						 *	新数据库指定的某一条数据
						 *///如果批次ID等于空而库存表不会空
						//继续跟据产品ID和批次ID查询库存表  目的是为了更新数据 上次结余 本次结余这种
						String SQL = "find intelligentlock_stock where productId="+productId.getIdd()+" and batchid= '"+batchid2+"'";
						IntelligentLockStock updateStock = Ebean.getServer(GlobalDBControl.getDB()).createQuery(IntelligentLockStock.class, SQL).findUnique();
						if(productInNumberMap.get(updateStock.getProductid())==null) {
							List<String> productInfo = new ArrayList<String>();
							productInfo.add(0, count+"");//记录数量
							productInfo.add(1, price+"");//记录价格
							productInfo.add(2, batchid2+"");//记录批次
							productInfo.add(3, productName);//记录商品名称
							productInfo.add(4, StringUtil.cellValueToString(row.getCell(5)));//记录生产厂家
							productInNumberMap.put(updateStock.getProductid(), productInfo);
						}else {
							List<String> productInfo2 = productInNumberMap.get(updateStock.getProductid());
							productInfo2.set(0, Integer.parseInt(productInfo2.get(0))+count+"");//将数量增加
						}
						updateBatchId = ils.getBatchid();//将库存表id保存
					}
					/** 数据中   true
					  *	数据库	 null
					  *	可以通过 - 向库存表插入数据 */
					if(!batchId.equals("")&ils==null) {//文件中有批次ID 单数数据库中没有 说明是新的数据 需要新增数据
						if(productInNumberMap.get(productId.getIdd())==null) {
							List<String> productInfo = new ArrayList<String>();
							productInfo.add(0, count+"");//记录数量
							productInfo.add(1, price+"");//记录价格
							productInfo.add(2, batchId+"");//记录批次
							productInfo.add(3, productName);//记录商品名称
							productInfo.add(4, StringUtil.cellValueToString(row.getCell(5)));//记录生产厂家
							productInNumberMap.put(productId.getIdd(), productInfo);
						}else {
							List<String> productInfo2 = productInNumberMap.get(productId.getIdd());
							productInfo2.set(0, Integer.parseInt(productInfo2.get(0))+count+"");//将数量增加
						}
					}
					/** 数据中   true
					 *	数据库	 true
					 *	可以通过 - 向库存表插入数据 */
					if(!batchId.equals("")&&!(ils==null)&&!batchId.equals(ils.getBatchid())) {//如果文件中存在批次id同时库存表中存在这个不同的批次ID 这说明是新的批次
						
						if(productInNumberMap.get(productId.getIdd())==null) {
							List<String> productInfo = new ArrayList<String>();
							productInfo.add(0, count+"");//记录数量
							productInfo.add(1, price+"");//记录价格
							productInfo.add(2, batchId+"");//记录批次
							productInfo.add(3, productName);//记录商品名称
							productInfo.add(4, StringUtil.cellValueToString(row.getCell(5)));//记录生产厂家
							productInNumberMap.put(productId.getIdd(), productInfo);
						}else {
							List<String> productInfo2 = productInNumberMap.get(productId.getIdd());
							productInfo2.set(0, Integer.parseInt(productInfo2.get(0))+count+"");//将数量增加
						}
						
					}else if(!batchId.equals("")&&!(ils==null)&&batchId.equals(ils.getBatchid())){//如果文件中存在批次ID同时数据库中也存在相同的批次ID说明只需要更改该库存的结余信息 上次结余 本次结余这种
						String SQL = "find intelligentlock_stock where productid="+productId.getIdd()+" and batchid='"+ils.getBatchid()+"'";
						IntelligentLockStock updateStock = Ebean.getServer(GlobalDBControl.getDB()).createQuery(IntelligentLockStock.class, SQL).findUnique();
						
						
						if(productInNumberMap.get(productId.getIdd())==null) {
							List<String> productInfo = new ArrayList<String>();
							productInfo.add(0, count+"");//记录数量
							productInfo.add(1, price+"");//记录价格
							productInfo.add(2, updateStock.getBatchid()+"");//记录批次
							productInfo.add(3, productName);//记录商品名称
							productInfo.add(4, StringUtil.cellValueToString(row.getCell(5)));//记录生产厂家
							productInNumberMap.put(productId.getIdd(), productInfo);
						}else {
							List<String> productInfo2 = productInNumberMap.get(productId.getIdd());
							productInfo2.set(0, Integer.parseInt(productInfo2.get(0))+count+"");//将数量增加
						}
						
					}
					if(updateBatchId!=""||!updateBatchId.equals("")) {//库存表ID不为空时
						fc. setBatchid ( updateBatchId );//将库存ID记录到单条入库明细表中
					}else {
						fc. setBatchid ( StringUtil.cellValueToString(row.getCell(3)) );//否则在文件中获取库存ID将其保存至入库明细中
					}
				
					
//					if(code.trim().equals("")) {
//						ResponseError error = new ResponseError();
//						error.setStatus(1);
//						error.setMassage("'"+productName+"'商品入库有误-----入库时请务必填写产品的唯一编码");
//						return ok(Json.toJson(error));
//					}
					LogUtil.writeLog("这里可以2", "testTrans");
					if(!code.trim().equals("")) {
						IntelligentLockProductCode findProductCode = null;
						try {
							/**查询锁编码库中是否存在这个锁编编码*/
							findProductCode = Ebean.getServer(GlobalDBControl.getDB())
									.find(IntelligentLockProductCode.class).where()
									.eq("code", code).findUnique();//跟据产品唯一编码获取编码表中唯一数据
						}catch (Exception e) {
							LogUtil.writeLog("查询IntelligentLockProductCode根据code出现异常A01-"+e.getMessage(), "InDB-ErrorLog");
							ResponseError error = new ResponseError();
							error.setStatus(1);
							error.setMassage("发生意外A01");
							return ok(Json.toJson(error));
						}
						if(findProductCode==null||findProductCode.equals(null)) {//如果没查询出 则新增一条产品编码数据
							IntelligentLockProductCode productCode = new IntelligentLockProductCode();
							productCode.setCode(code);
							productCode.setAddtime(date);
							productCode.setProductid(productId.getIdd());
							if(typeStatus==2) {
								productCode.setStatus(typeStatus);
							}else {
								productCode.setStatus(0);
							}
							productCode.setBatchid(batchId);
							productCode.setDeviceid(StringUtil.cellValueToString(row.getCell(5)));
							productCode.setRemark(StringUtil.cellValueToString(row.getCell(7)));//直接获取第八列
							productCode.setOperatornumber(operatnumber);
							Ebean.getServer(GlobalDBControl.getDB()).save(productCode);
						}else if(typeStatus==2) {
							Ebean.getServer(GlobalDBControl.getDB()).createUpdate(IntelligentLockProductCode.class, "update IntelligentLockProductCode set status='"+typeStatus+"' where code='"+code+"'").execute();
						}
					}
					
					//设备ID
					String deviceId = StringUtil.cellValueToString(row.getCell(5));
					if(deviceId.trim()!=""&&deviceId!=null) {
						List<IntelligentLockInDetail> intelligentLockInDetailList = Ebean.getServer(GlobalDBControl.getDB()).createQuery(IntelligentLockInDetail.class,"find IntelligentLockInDetail where deviceid = '"+deviceId+"'").findList();
						if(intelligentLockInDetailList.size()!=0) {
							if(!inDBType.equals("退货入库")) {
								ResponseError error = new ResponseError();
								error.setStatus(1);
								error.setMassage("'"+productName+"'入库时发现设备ID重复，请您核对");
								return ok(Json.toJson(error));
							}else {
								List<IntelligentLockIn> findList = Ebean.getServer(GlobalDBControl.getDB()).createQuery(IntelligentLockIn.class,"find IntelligentLockIn where type=2").findList();
								if(findList.size()!=0) {
									for (IntelligentLockIn intelligentLockIn : findList) {
										for (IntelligentLockInDetail intelligentLockInDetail : intelligentLockInDetailList) {
											Integer idd = intelligentLockIn.getIdd();
											Integer inid = intelligentLockInDetail.getInid();
											if(idd.equals(inid)) {
												ResponseError error = new ResponseError();
												error.setStatus(1);
												error.setMassage("该商品正在退货");
												return ok(Json.toJson(error));
											}
										}
									}
								}
							}
						}
						//fc. setDeviceid(deviceId);
					}
					fc. setDeviceid(deviceId);
					/*
					 * else { ResponseError error = new ResponseError(); error.setStatus(1);
					 * error.setMassage("'"+productName+"'商品必须指定设备ID"); return
					 * ok(Json.toJson(error)); }
					 */
					//生产厂家
					fc. setManufacturer ( StringUtil.cellValueToString(row.getCell(6)) );
					fc. setAddtime ( getDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date)) );
					fc. setOperator ( Application.getSysUser().getLogin() );
					fc. setOperatenumber ( operatnumber );
					//入库类型
					fc. setInType ( StringUtil.cellValueToString(row.getCell(7)) );
					
					LogUtil.writeLog("这里可以3", "testTrans");
					//更改入库单上入库类型的状态
					/**
					 *  如：这批锁入库类型为退货入库，这个excel表中所有的锁必须都是退货入库
					 */
					try {
						Ebean.getServer(GlobalDBControl.getDB()).createUpdate(IntelligentLockIn.class, "update IntelligentLockIn set type='"+typeStatus+"' where idd='"+inidd+"'").execute();
					}catch (Exception e) {
						Ebean.rollbackTransaction();
						LogUtil.writeLog("更新锁状态出现问题"+e.getMessage(), "InDB-ErrorLog");
						ResponseError error = new ResponseError();
						error.setStatus(1);
						error.setMassage("更新锁状态出现问题");
						return ok(Json.toJson(error));
					}
					LogUtil.writeLog("这里可以3.5", "testTrans");
					if(inDBType.equalsIgnoreCase("退货入库")) {
						/**
						 * 用户在入库时 删除用户与锁的信息删除用户vip 同时不让用户再次扫描二维码
						 */
						VipService.reduceVipValidityByCancelOrder(deviceId);
						
						LogUtil.writeLog("这里可以3.75", "testTrans");
						try {
							Ebean.getServer(GlobalDBControl.getDB()).createUpdate(SmartDevice.class, "delete from smart_device where deviceid='"+deviceId+"'");
						} catch (Exception e) {
							Ebean.rollbackTransaction();
							LogUtil.writeLog("删除数据出现问题"+e.getMessage(), "InDB-ErrorLog");
							ResponseError error = new ResponseError();
							error.setStatus(1);
							error.setMassage("删除数据出现问题");
							return ok(Json.toJson(error));
						}
					}
					LogUtil.writeLog("这里可以4", "testTrans");
					//备注
					String remark = StringUtil.cellValueToString(row.getCell(8)) ;
					fc. setRemark ( remark );
					//用于获取指定商品名称的商品的型号和规格
					IntelligentLockProduct productDetails = null;
					try {
						productDetails = Ebean.getServer(GlobalDBControl.getReadDB()).find(IntelligentLockProduct.class).where().eq("title", productName).eq("status", 0).findUnique();
						if(productDetails!=null) {
							fc. setModel ( productDetails.getModel() );
							fc. setSpec ( productDetails.getSpec() );
						}else {
							ResponseError error = new ResponseError();
							error.setStatus(1);
							error.setMassage("产品表中没有 '"+productName+"' 商品");
							return ok(Json.toJson(error));
						}
					} catch (Exception e) {
						Ebean.rollbackTransaction();
						LogUtil.writeLog("商品:"+productName+"商品获取型号/规格时出现问题"+e.getMessage(), "InDB-ErrorLog");
						ResponseError error = new ResponseError();
						error.setStatus(1);
						error.setMassage("'"+productName+"'获取商品型号/规格出现问题");
						return ok(Json.toJson(error));
					}
					successNum+=1;
				if(inDetailMap == null) {
					inDetailMap = new HashMap<String, List<IntelligentLockInDetail>>();
				}
				List<IntelligentLockInDetail> detailList = inDetailMap.get(productName);//对明细map添加/更改
				if(detailList!=null) {
					boolean flag = true;//记录是否有查到相同批次的 如果没有说明是新的数据 需要再次添加 默认新增
					for (IntelligentLockInDetail detail : detailList) {
						if(detail.getBatchid().equals(batchId)&&detail.getRemark().equals(remark)) {//如果批次相同进入
							detail.setAmount(detail.getAmount()+count);
							detail.setTotalprice(detail.getTotalprice()+(count*price));
							flag=false;//别进去
						}
					}
					if(flag) {
						List<IntelligentLockInDetail> addDetail = inDetailMap.get(productName);
						addDetail.add(fc);
					}
				}else {//如果根据名称没找到任何记录 说明该商品是第一次添加明细 创建
					List<IntelligentLockInDetail> detList = new ArrayList<IntelligentLockInDetail>();
					detList.add(fc);
					inDetailMap.put(productName, detList);
				}
					 
			 }
			 LogUtil.writeLog("这里可以5", "testTrans");
			 //遍历Map
			 if(inDetailMap!=null) {
					for (Map.Entry<String,List<IntelligentLockInDetail>> entry : inDetailMap.entrySet()) {
						List<IntelligentLockInDetail> detailList = entry.getValue();//找出同一种商品名称的list 它可能存在不同批次的商品
//						for (IntelligentLockInDetail detail : detailList) {//遍历每一个不同批次的商品 它可能只有一个
							try {
//								Ebean.getServer(GlobalDBControl.getDB()).save(detail);
								Ebean.getServer(GlobalDBControl.getDB()).save(detailList);
							} catch (Exception e) {
								 LogUtil.writeLog("入库明细记录失败 明细Map出现问题 "+e.getMessage(), "InDB-ErrorLog");
								 ResponseError error = new ResponseError();
								 error.setStatus(1);
								 error.setMassage("入库明细记录失败");
								 return ok(Json.toJson(error));
							}
//						}
					}
				 }
			 //遍历Map
			 if(productInNumberMap!=null) {//它暂时肯定不为空 但不保证后期任何人维护后都不为空
				 for (Map.Entry<Integer,List<String>> entry : productInNumberMap.entrySet()) {//遍历Map
					 List<String> info = entry.getValue();
					 String SQL = "find intelligentlock_stock where productId="+entry.getKey()+" and batchid= '"+info.get(2)+"'";//跟据商品ID和批次 获取库存中一条商品种类的库存
					 IntelligentLockStock updateStock = Ebean.getServer(GlobalDBControl.getDB()).createQuery(IntelligentLockStock.class, SQL).findUnique();//获取一条库存信息
					 if(updateStock==null) {//说明这是第一次入库 直接save操作
						 updateStock = new IntelligentLockStock();
						 updateStock.setBatchid(info.get(2));
						 updateStock.setAddtime(date);
						 updateStock.setLastsurplus(0);
						 updateStock.setTranscatnumber(Integer.parseInt(info.get(0)));
						 updateStock.setCurrentsurplus(Integer.parseInt(info.get(0)));
						 updateStock.setOperator(Application.getSysUser().getLogin());
						 updateStock.setProductid(entry.getKey());
						 updateStock.setPrice(Double.parseDouble(info.get(1)));
						 updateStock.setTitle(info.get(3));
						 updateStock.setFacturername(info.get(4));
						 Ebean.getServer(GlobalDBControl.getDB()).save(updateStock);
					 }else {
						 updateStock.setLastsurplus(updateStock.getCurrentsurplus());//上次结余
						 updateStock.setTranscatnumber(Integer.parseInt(info.get(0)));//本次交易
						 updateStock.setCurrentsurplus(updateStock.getCurrentsurplus()+Integer.parseInt(info.get(0)));//本次结余
						 updateStock.setOperator(Application.getSysUser().getLogin());
						 updateStock.setAddtime(date);
						 updateStock.setPrice(Double.parseDouble(info.get(1)));//商品的价格
						 try {
							 Ebean.getServer(GlobalDBControl.getDB()).save(updateStock);//将库存表保存 
						 } catch (Exception e) {
							 LogUtil.writeLog("更新库存表出现错误 位于IntelligentLockDetailAction "+e.getMessage(), "InDB-ErrorLog");
							 ResponseError error = new ResponseError();
							 error.setStatus(1);
							 error.setMassage("库存表更新时出现问题");
							 return ok(Json.toJson(error));
						 }
					 }
				 }
			 }
			 
			 if(ail!=null || !ail.equals(null)) {
				 //将总价更新进去
				 Ebean.getServer(GlobalDBControl.getDB()).createUpdate(IntelligentLockIn.class, "update intelligentlock_in set totalprice='"+totalprice4il+"' where operatnumber = '"+operatnumber+"'").execute();
				 Ebean.getServer(GlobalDBControl.getDB()).createUpdate(IntelligentLockIn.class, "update intelligentlock_in set productcount='"+productCount+"' where operatnumber = '"+operatnumber+"'").execute();
			 }
//			 if( list.size()>0 ){
			 if( inDetailMap!=null ){
//				 successNum =  Ebean.getServer(GlobalDBControl.getDB()).save(list);
//				 Ebean.getServer(GlobalDBControl.getDB()).save(list);
				 
				 List<IntelligentLockInDetail> detailList = Ebean.getServer(GlobalDBControl.getDB()).find(IntelligentLockInDetail.class).where().eq("inid", inidd).findList();//记录入库流水
				 for (IntelligentLockInDetail detail : detailList) {
					 IntelligentLockStockInWater iniw = new IntelligentLockStockInWater();//入库流水
					 iniw.setBatchid( detail.getBatchid());
					 iniw.setOperator(Application.getSysUser().getLogin());
					 iniw.setAddtime(date);
					 iniw.setOperatnumber(operatnumber);
					 iniw.setInid( ail.getIdd() );
					 iniw.setTrascatnumber(detail.getAmount());
					 iniw.setProductid( detail.getProductid() );
					 iniw.setIndetailid(detail.getIdd());
					 Ebean.getServer(GlobalDBControl.getDB()).save(iniw);
				 } 
				 
				 list.clear();
				 Ebean.commitTransaction();
				 return ok(Json.toJson(successNum));
			  }
			}catch (Exception e) {
				//回滚事务
				Ebean.rollbackTransaction();
				LogUtil.writeLog("事务无法提交，上传文件时出现问题（事务已回滚）"+e.getMessage(), "InDB-ErrorLog");
				ResponseError error = new ResponseError();
				error.setStatus(1);
				error.setMassage("更新错误，入库明细表异常"+e.getMessage());
				return ok(Json.toJson(error));
			}finally {
				Ebean.endTransaction();
			}

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
	public static Date getDate(String s) {
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			//将指定时间格式解析成时间戳
			date = sdf.parse(s);
	
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return date;
	}
}