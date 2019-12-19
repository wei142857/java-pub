
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

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.avaje.ebean.Ebean;

import app.service.VipService;
import models.IntelligentLockIn;
import models.IntelligentLockInDetail;
import models.IntelligentLockManufacturer;
import models.IntelligentLockProduct;
import models.IntelligentLockProductCode;
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
import util.StringUtil;
import util.classEntity.StringTool;
import views.html.page.IntelligentLockInList;

@Security.Authenticated(Secured.class)
public class IntelligentLockInAction extends Controller {
  
	//进入页面列表；
	public static Result view() {
		return ok( IntelligentLockInList.render() );
	}
	public static Result updateStatus() {
		String inid = StringUtil.getHttpParam(request(), "inid");
		//找到明细表中入库id为idd的所有商品
		Ebean.beginTransaction();
		//VipService.reduceVipValidityByCancelOrder(deviceId);
		try {
			/**
			 * 找到入库明细表中多有的关于这个inid的信息
			 */
			List<IntelligentLockInDetail> intelligentLockInDetailList = Ebean.getServer(GlobalDBControl.getDB()).createQuery(IntelligentLockInDetail.class,"find IntelligentLockInDetail where inid='"+inid+"'").findList();
			//如果存在明细
			if(intelligentLockInDetailList!=null) {
				//更新入库表中对应信息的状态
				Ebean.getServer(GlobalDBControl.getDB()).createUpdate(IntelligentLockIn.class,"update IntelligentLockIn set type='0' where idd='"+inid+"'").execute();//入库单页修改成入库成功
				//遍历明细
				for (IntelligentLockInDetail intelligentLockInDetail : intelligentLockInDetailList) {
					//判断数据中是否有deviceid 如果没有说明数据有问题
					if(intelligentLockInDetail.getDeviceid()==null) {
						LogUtil.writeLog("没有查找到deviceid/method-updateStatus", "InDB-ErrorLog");
						ResponseError error = new ResponseError();
						error.setStatus(1);
						error.setMassage("退货失败，deviceid不存在");
						return ok(Json.toJson(error));
					}
					//更新code表中对应锁的状态
					Ebean.getServer(GlobalDBControl.getDB()).createUpdate(IntelligentLockProductCode.class, "update IntelligentLockProductCode set status='0' where deviceid='"+intelligentLockInDetail.getDeviceid()+"'").execute();
					Ebean.getServer(GlobalDBControl.getDB()).createUpdate(SmartDevice.class,"update SmartDevice set buytime=null where deviceid='"+intelligentLockInDetail.getDeviceid()+"'").execute();
					//删除缓存 解除冻结
					VipService.unfreezeDevice(intelligentLockInDetail.getDeviceid());
				}
				Ebean.commitTransaction();
			}else {
				LogUtil.writeLog("ajax异步更新入库状态失败了,根据入库单idd未找到结果（"+inid+"）", "InDB-ErrorLog");
				ResponseError error = new ResponseError();
				error.setStatus(1);
				error.setMassage("这个锁出现数据问题");
				return ok(Json.toJson(error));
			}
		} catch (Exception e) {
			Ebean.rollbackTransaction();
			LogUtil.writeLog("ajax异步更新入库状态失败了,出现异常了"+e.getMessage(), "InDB-ErrorLog");
			ResponseError error = new ResponseError();
			error.setStatus(1);
			error.setMassage(e.getMessage()+"数据库访问失败了");
			return ok(Json.toJson(error));
		}finally {
			Ebean.endTransaction();
		}
		return ok(Json.toJson("更新成功了"));
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
		sql.append("find IntelligentLockIn where 1=1 ");
		
		String idd = StringUtil.getHttpParam(request(), "idd");
		if(idd==null)
			idd = "";	
		if ( !( idd .equals("") ) && !( idd .equals("undefined") ) ) 
			sql.append(" and ( idd= '" + idd + "'  )");
		
		String serialnumber = StringUtil.getHttpParam(request(), "serialnumber");
		if(serialnumber==null)
			serialnumber = "";	
		if ( !( serialnumber .equals("") ) && !( serialnumber .equals("undefined") ) ) 
			sql.append(" and ( serialnumber= '" + serialnumber + "'  )");
		
		String operator = StringUtil.getHttpParam(request(), "operator");
		if(operator==null)
			operator = "";	
		if ( !( operator .equals("") ) && !( operator .equals("undefined") ) ) 
			sql.append(" and ( operator= '" + operator + "'  )");
		
		String addtime = StringUtil.getHttpParam(request(), "addtime");
		if(addtime==null)
			addtime = "";	
		if ( !( addtime .equals("") ) && !( addtime .equals("undefined") ) ) 
			sql.append(" and ( addtime like '%" + addtime + "%'  )");
		
		String operatnumber = StringUtil.getHttpParam(request(), "operatnumber");
		if(operatnumber==null)
			operatnumber = "";	
		if ( !( operatnumber .equals("") ) && !( operatnumber .equals("undefined") ) ) 
			sql.append(" and ( operatnumber= '" + operatnumber + "'  )");
		
		String totalprice = StringUtil.getHttpParam(request(), "totalprice");
		if(totalprice==null)
			totalprice = "";	
		if ( !( totalprice .equals("") ) && !( totalprice .equals("undefined") ) ) 
			sql.append(" and ( totalprice= '" + totalprice + "'  )");
		
		String type = StringUtil.getHttpParam(request(), "type");
		if(type==null)
			type = "";	
		if ( !( type .equals("") ) && !( type .equals("undefined") ) ) 
			sql.append(" and ( type= '" + type + "'  )");
		
		String remark = StringUtil.getHttpParam(request(), "remark");
		if(remark==null)
			remark = "";	
		if ( !( remark .equals("") ) && !( remark .equals("undefined") ) ) 
			sql.append(" and ( remark= '" + remark + "'  )");
		
		String export = StringUtil.getHttpParam(request(), "export");// 是否导出
		if(export!=null && export.equals("1")){
			nStart = 0;
			nLimit = 65534;
		}
		
		Logger.info(sql.toString());
		//下面简单的分页，需要重构，应该由 Dao-Sevice去实现。
		int rowCount = Ebean.getServer(GlobalDBControl.getDB()).createQuery(IntelligentLockIn .class, sql.toString()).findRowCount();
		List<IntelligentLockIn> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.createQuery(IntelligentLockIn .class, sql.toString())
				.setFirstRow(nStart).setMaxRows(nLimit)
				.orderBy("idd desc")
				.findList();
				
		if(export!=null && export.equals("1")){
			String fileName = "IntelligentLockIn"+simp.format( new Date())+".xls";
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
			String fileName = "IntelligentLockIn"+simp.format( new Date())+".xls";
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
		PagedObject<IntelligentLockIn> po= new PagedObject( ulist,nPages,rowCount );
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(po) );
	}

	//列表
	public static Result listAll(){
		List<IntelligentLockIn> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.find(IntelligentLockIn .class)
				.orderBy("idd")
				.findList();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(ulist) );
	}
	
	//删除
	public static Result delete(int idd){
		String sql = "delete from IntelligentLockIn where idd =:idd";
		Ebean.beginTransaction();
		try {
			Ebean.getServer(GlobalDBControl.getDB()).createUpdate(IntelligentLockIn .class, sql).setParameter("idd", idd).execute();
			Ebean.commitTransaction();
		}catch (Exception e) {
			LogUtil.writeLog("入库表 前端发起删除操作失败", "In-ErrorLog");
			Ebean.rollbackTransaction();
		} finally {
			Ebean.endTransaction();
		}
		
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson("操作成功") );
	}
	
	//获取 单个
	public static Result get(int idd){
		IntelligentLockIn intelligentlockin = Ebean.getServer(GlobalDBControl.getDB())
				.find(IntelligentLockIn .class).where().eq("idd", idd).findUnique();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(intelligentlockin) );
	}

	//新增 / 修改
	public static Result modify(){
		String idd = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "idd");
		int nidd = StringTool.GetInt(idd, 0);
		String serialnumber = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "serialnumber");
		String operator = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "operator");
		String addtime = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "addtime");
		String operatnumber = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "operatnumber");
		String totalprice = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "totalprice");
		String type = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "type");
		String remark = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "remark");
		String operation = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "operation");
		IntelligentLockIn intelligentlockin ;
		if(  operation.equals("add") )	// 新增
			intelligentlockin = new IntelligentLockIn ();
		else			//修改
			intelligentlockin = Ebean.getServer(GlobalDBControl.getDB())
					.find(IntelligentLockIn .class).where().eq("idd", idd).findUnique();
		
		if( intelligentlockin!=null ){
			//赋值,字符串类型可以直接赋值，其他类型需要转换 ！！！！
					intelligentlockin. setSerialnumber ( serialnumber );
					intelligentlockin. setOperator ( operator );
					intelligentlockin. setAddtime ( StringUtil.getDate(addtime) );
					intelligentlockin. setOperatnumber ( operatnumber );
					intelligentlockin. setTotalprice ( StringTool.GetDouble(totalprice,0.0) );
					intelligentlockin. setType ( Integer.parseInt(type));
					intelligentlockin. setRemark ( remark );
			Ebean.beginTransaction();
			try {
				Ebean.getServer(GlobalDBControl.getDB()).save( intelligentlockin );
				Ebean.commitTransaction();
			}catch (Exception e) {
				LogUtil.writeLog("入库表 前端发起修改/更新操作失败", "In-ErrorLog");
				Ebean.rollbackTransaction();
			} finally {
				Ebean.endTransaction();
			}
			response().setHeader("Cache-Control", "no-cache");
			return ok( Json.toJson(intelligentlockin) );
		}

		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(null) );
	}
	
	static int num=0;
	public static File exportList(List<IntelligentLockIn> infoList,String fileNameChine) throws UnsupportedEncodingException {
		Workbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet("sheet1");
		
			sheet.setColumnWidth( 0 , 6000 );
		
			sheet.setColumnWidth( 1 , 6000 );
		
			sheet.setColumnWidth( 2 , 6000 );
		
			sheet.setColumnWidth( 3 , 6000 );
		
			sheet.setColumnWidth( 4 , 6000 );
		
			sheet.setColumnWidth( 5 , 6000 );
		
			sheet.setColumnWidth( 6 , 6000 );
		
		/* sheet.setColumnWidth( 7 , 6000 ); */
		
		ExcelGenerateHelper helper = ExcelGenerateHelper.getInstance(wb);

		// 获取行索引
		int rowIndex = 0;
		int colIndex = 0;

		// 设置表头
		String[] titles = { 
				
					"id"
					
				
					
				/* ,"流水号" */
				
					
					,"操作人"
				
					
					,"添加时间"
				
					
					,"操作号"
				
					
					,"总价"
				
					
					,"入库类型"
				
					
					,"备注"
				
			};

		// 生成表头
		helper.setRowIndex(rowIndex++);
		helper.generateHeader(sheet, titles, 0, StringUtils.EMPTY,
				StringUtils.EMPTY);

		// 循环生成数据行
		for (int i = 0; i < infoList.size(); i++) {
			colIndex = 0;
			IntelligentLockIn info = (IntelligentLockIn) infoList.get(i);
			Row row = sheet.createRow(rowIndex++);
			
			
				helper.createStringCell( row , colIndex++, ""+info. getIdd () );
			
				/*helper.createStringCell( row , colIndex++, ""+info. getSerialnumber () );*/
				
				helper.createStringCell( row , colIndex++, ""+info. getTotalprice () );
			
				helper.createStringCell( row , colIndex++, ""+info. getOperatnumber () );
				
				helper.createStringCell( row , colIndex++, ""+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(info.getAddtime ()));
				
				helper.createStringCell( row , colIndex++, ""+info. getOperator () );
			
				helper.createStringCell( row , colIndex++, ""+info. getType () );
			
				helper.createStringCell( row , colIndex++, ""+info. getRemark () );
			
		}
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
	// 设置表头
	static String[] titles = { 
				"产品名称"
			    ,"数量" 
				,"单价"
				,"生产批次"
				,"产品唯一编码"
				,"设备ID"
				,"生产厂家"
				,"入库类型"
				,"备注"
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
        
        //将下拉框数据放到新的sheet里，然后excle通过新的sheet数据加载下拉框数据
        Sheet hidden = wb.createSheet("hidden");
        Cell cell = null;
        for (int i = 0, length = dlData.length; i < length; i++)
        {
            String name = dlData[i];
            Row row = hidden.createRow(i);
            cell = row.createCell(0);
            cell.setCellValue(name);
        }
        
        Name namedCell = wb.createName();
        namedCell.setNameName("hidden");
        namedCell.setRefersToFormula("hidden!$A$1:$A$" + dlData.length);
        //加载数据,将名称为hidden的
        DVConstraint constraint = DVConstraint.createFormulaListConstraint("hidden");
        // 设置第一列的1-65534行为下拉列表
		CellRangeAddressList regions = new CellRangeAddressList(1, 65534, 0, 0);//创建列对象 参数:起始行号，终止行号， 起始列号，终止列号
        // 绑定
        DataValidation dataValidation = new HSSFDataValidation(regions, constraint);
        //将第二个sheet设置为隐藏
        wb.setSheetHidden(1, true);
        sheet1.addValidationData(dataValidation);//添加到Excel中
        
        ExcelGenerateHelper helper = ExcelGenerateHelper.getInstance(wb);
        
        
        //生产厂家列表
        List<IntelligentLockManufacturer> mList = Ebean.getServer(GlobalDBControl.getDB()).createQuery(IntelligentLockManufacturer.class).findList();
        String[] mData = new String[mList.size()];
        for (int i = 0; i < mList.size(); i++) {
        	mData[i] = mList.get(i).getName();
		}
        // 设置第6列的1-65534行为下拉列表
        CellRangeAddressList regions6 = new CellRangeAddressList(1, 65534, 6, 6);//创建列对象 参数:起始行号，终止行号， 起始列号，终止列号
        // 创建下拉列表数据
        DVConstraint constraint6 = DVConstraint.createExplicitListConstraint(mData);//创建一个下拉列表对象 包含数据
        // 绑定
        HSSFDataValidation dataValidation6 = new HSSFDataValidation(regions6, constraint6);//将列表对象和列对象绑定
        sheet1.addValidationData(dataValidation6);//添加到Excel中
        
        //入库类型
        String[] tData = {
        		"正常入库",
        		"样品入库",
        		"退货入库"
        		};
        // 设置第7列的1-65534行为下拉列表
        CellRangeAddressList regions7 = new CellRangeAddressList(1, 65534, 7, 7);//创建列对象 参数:起始行号，终止行号， 起始列号，终止列号
        // 创建下拉列表数据
        DVConstraint constraint7 = DVConstraint.createExplicitListConstraint(tData);//创建一个下拉列表对象 包含数据
        // 绑定
        HSSFDataValidation dataValidation7 = new HSSFDataValidation(regions7, constraint7);//将列表对象和列对象绑定
        sheet1.addValidationData(dataValidation7);//添加到Excel中
        
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
	
	static String[]  headers = {
				
					
					
				
					"流水号"
					
				
					
					,"操作人"
				
					
					,"添加时间"
				
					
					,"操作号"
				
					
					,"总价"
				
					
					,"入库类型"
				
					
					,"备注"
				
		};
	static int MAX_LINES = 10000;
	public static Result upload(){
		/* String cid = AjaxHellper.getHttpParam(request(), "uploadidd"); */
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
			
			List<IntelligentLockIn> list = new ArrayList<IntelligentLockIn>();
			 for (int i = 1; i <= lastRowNumber; i++) {
				 //批量添加
                 Row row = sheet.getRow(i);
                 if (row == null) {
                     break;
                 }
                 
                 IntelligentLockIn fc = new IntelligentLockIn();
                 int j = 0 ;
                 
				
					
						fc. setSerialnumber ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					
						fc. setOperator ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					
						fc. setAddtime ( StringUtil.getDate( StringUtil.cellValueToString(row.getCell(j))));
					 
					
					
					
						j=j+1;
					
				
					
						fc. setOperatnumber ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					
						fc. setTotalprice ( StringTool.GetDouble(StringUtil.cellValueToString(row.getCell(j)), 0.0) );
					 
					
					
					
						j=j+1;
						
						int typeStatus = 0;//默认正常入库
						String type = StringUtil.cellValueToString(row.getCell(j));
						if(type.equalsIgnoreCase("样品入库")) {
							typeStatus = 1;
						}else if(type.equalsIgnoreCase("退货入库")) {
							typeStatus = 2;
						}
						fc. setType (typeStatus);
					 
					
					
					
						j=j+1;
					
				
					
						fc. setRemark ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
                 list.add(fc);
                 if( list.size()>90 ){
                	Ebean.beginTransaction();
             		try {
             			Ebean.getServer(GlobalDBControl.getDB()).save(list);
             			Ebean.commitTransaction();
             		}catch (Exception e) {
             			LogUtil.writeLog("In Table excel file upload failed", "In-ErrorLog");
             			Ebean.rollbackTransaction();
             		} finally {
             			Ebean.endTransaction();
             		}
                	list.clear();
                 }
			 }
			 
			 if( list.size()>0 ){
				Ebean.beginTransaction();
				try {
					successNum = Ebean.getServer(GlobalDBControl.getDB()).save(list);
					Ebean.commitTransaction();
				}catch (Exception e) {
					LogUtil.writeLog("入库表 文件上传失败，事务已回滚", "In-ErrorLog");
					Ebean.rollbackTransaction();
				} finally {
					Ebean.endTransaction();
				}
				 list.clear();
				 return ok(Json.toJson(successNum));
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
	
}