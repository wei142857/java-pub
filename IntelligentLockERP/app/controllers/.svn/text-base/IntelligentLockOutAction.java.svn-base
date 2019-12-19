
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

import models.IntelligentLockInDetail;
import models.IntelligentLockOut;
import models.IntelligentLockOutDetail;
import models.IntelligentLockOutDetailCode;
import models.IntelligentLockProduct;
import models.IntelligentLockProductCode;
import models.IntelligentLockStock;
import models.IntelligentLockStockOutWater;
import models.IntelligentLockTemporaryOutDetailCode;
import models.ReturnMessage;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;

import play.Configuration;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import play.mvc.Security;
import util.ExcelGenerateHelper;
import util.ExcelUtils;
import util.GlobalDBControl;
import util.PagedObject;
import util.StringUtil;
import util.classEntity.StringTool;
import views.html.page.IntelligentLockOutList;

import com.avaje.ebean.Ebean;

@Security.Authenticated(Secured.class)
public class IntelligentLockOutAction extends Controller {
  
	//进入页面列表；
	public static Result view() {
		return ok( IntelligentLockOutList.render() );
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
		sql.append("find IntelligentLockOut where 1=1 ");
		
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
		
		String export = StringUtil.getHttpParam(request(), "export");// 是否导出
		if(export!=null && export.equals("1")){
			nStart = 0;
			nLimit = 65534;
		}
		
		Logger.info(sql.toString());
		//下面简单的分页，需要重构，应该由 Dao-Sevice去实现。
		int rowCount = Ebean.getServer(GlobalDBControl.getDB()).createQuery(IntelligentLockOut .class, sql.toString()).findRowCount();
		List<IntelligentLockOut> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.createQuery(IntelligentLockOut .class, sql.toString())
				.setFirstRow(nStart).setMaxRows(nLimit)
				.orderBy("idd desc")
				.findList();
		//以Excel表的格式导出出库单的数据
		if(export!=null && export.equals("1")){
			String fileName = "IntelligentLockOut"+simp.format( new Date())+".xls";
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
		PagedObject<IntelligentLockOut> po= new PagedObject( ulist,nPages,rowCount );
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(po) );
	}

	//列表
	public static Result listAll(){
		List<IntelligentLockOut> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.find(IntelligentLockOut .class)
				.orderBy("idd")
				.findList();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(ulist) );
	}
	
	//删除
	public static Result delete(int idd){
		String sql = "delete from IntelligentLockOut where idd =:idd";
		Ebean.getServer(GlobalDBControl.getDB())
				.createUpdate(IntelligentLockOut .class, sql).setParameter("idd", idd).execute();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson("操作成功") );
	}
	
	//获取 单个
	public static Result get(int idd){
		IntelligentLockOut intelligentlockout = Ebean.getServer(GlobalDBControl.getDB())
				.find(IntelligentLockOut .class).where().eq("idd", idd).findUnique();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(intelligentlockout) );
	}

	//新增 / 修改
	/*public static Result modify(){
		String idd = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "idd");
		int nidd = StringTool.GetInt(idd, 0);
		
		String serialnumber = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "serialnumber");
		
		String operator = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "operator");
		
		String addtime = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "addtime");
		
		String operatornumber = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "operatornumber");
		
		String totalprice = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "totalprice");
		
		String remark = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "remark");
		
		String operation = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "operation");
		
		IntelligentLockOut intelligentlockout ;
		if(  operation.equals("add") )	// 新增
			intelligentlockout = new IntelligentLockOut ();
		else			//修改
			intelligentlockout = Ebean.getServer(GlobalDBControl.getDB())
					.find(IntelligentLockOut .class).where().eq("idd", idd).findUnique();
		
		if( intelligentlockout!=null ){
			//赋值,字符串类型可以直接赋值，其他类型需要转换 ！！！！
			
					intelligentlockout. setSerialnumber (serialnumber);
				
					intelligentlockout. setOperator ( operator );
				 
					intelligentlockout. setAddtime ( StringUtil.getDate(addtime) );
				
					intelligentlockout. setOperatornumber ( operatornumber );
				
					intelligentlockout. setTotalprice ( StringTool.GetDouble(totalprice,0.0));
				
					intelligentlockout. setRemark ( remark );
				 
					intelligentlockout. setChannel ( channel );
				 
					intelligentlockout. setType ( type );
				 
					intelligentlockout. setSalenumber ( salenumber );
				 
			
			Ebean.getServer(GlobalDBControl.getDB()).save( intelligentlockout );
			response().setHeader("Cache-Control", "no-cache");
			return ok( Json.toJson(intelligentlockout) );
		}

		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(null) );
	}*/
	
	//设置出库单Excel模板第一行的表头
	static String[] oneTitles = {
		"出库单备注"
	};
	//设置出库单Excel模板第三行的表头
	static String[] threeTitles = {
		"产品名称",
		"唯一产品编码",
		"数量",
		"出库单价",
		"出货渠道",
		"类型",
		"外部销售平台订单号",
		"备注"
	};
	
	//设置出库单Excel模板中的出库类型下拉选内容
	static String[] types = {
		"正常发货",
		"换货发货",
		"山西代售",
		"代理工程用锁",
		"质检",
		"拍照",
		"测试",
		"培训"
	};
		
	//导出 出库单Excel模板的方法
	public static Result downSample() throws UnsupportedEncodingException{
		//创建文件名称
		String fileNameChine = "IntelligentLockOut"+simp.format(new Date())+".xls";
		Logger.info("fileNameChine:" + fileNameChine);
		//“0,0,3,4”  ===>  “起始行，截止行，起始列，截止列”
		String headnum0[] = {"0,0,0,3","1,1,0,3"};
		/*
		 * 创建Excel工作薄对象:
		 * 由于HSSFWorkbook只能操作excel2003以下版本,
		 * XSSFWorkbook只能操作excel2007以上版本，
		 * 所以利用Workbook接口创建对应的对象操作excel
		 * 来处理兼容性.
		 */
		Workbook workbook = new HSSFWorkbook();
		//生成一个表格 设置：页签
		Sheet sheet = workbook.createSheet("出库单模板1");
		
		//动态的合并单元格
        for (int i = 0; i < headnum0.length; i++) {
            sheet.autoSizeColumn(i, true);
            String[] temp = headnum0[i].split(",");
            Integer startrow = Integer.parseInt(temp[0]);
            Integer overrow = Integer.parseInt(temp[1]);
            Integer startcol = Integer.parseInt(temp[2]);
            Integer overcol = Integer.parseInt(temp[3]);
            sheet.addMergedRegion(new CellRangeAddress(startrow, overrow, startcol, overcol));
        }
		
		//创建样式对象
		CellStyle style = workbook.createCellStyle();
		//设置表头样式:
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);//是设置单元格填充样式
        style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);//背景颜色
        //设置边框
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setBorderTop(CellStyle.BORDER_THIN);
        //设置 垂直/水平居中
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		//设置字体样式
		Font font = workbook.createFont(); 
        font.setFontName("楷体");
        font.setFontHeightInPoints((short)16);    
        font.setBoldweight(Font.BOLDWEIGHT_BOLD); 
        style.setFont(font);
        //创建默认字体对象 并设置默认字体
        Font defaultFont = workbook.getFontAt((short)0);
        defaultFont.setCharSet(Font.DEFAULT_CHARSET);
        defaultFont.setFontHeightInPoints((short)12);
        defaultFont.setFontName("微软雅黑");
        //设置默认的 列宽和行高
        sheet.setDefaultColumnWidth(30*256);
        sheet.setDefaultRowHeightInPoints(20);
        //生成表格的 第一行
        Row rowFirst = sheet.createRow(0);
        //设置第一行的 行高
        rowFirst.setHeightInPoints(40);
        //遍历数组oneTitles 设置第一行的表头内容
        for(int i=0;i<oneTitles.length;i++){
        	// 根据字段长度自动调整列的宽度
        	//sheet.autoSizeColumn(i, true);
        	/*
        	 * 设置第一行的列宽:setColumnWidth
        	 * 这个方法宽度的单位是字符数的256分之一
        	 * 最后我们的表达式为：256*width+184
        	 */
            sheet.setColumnWidth(i, 6000);
        	//创建第一行中的每个单元格
        	Cell cell = rowFirst.createCell(i);
        	//往单元格中添加数据
            cell.setCellValue(oneTitles[i]);
            //给内容添加样式
            cell.setCellStyle(style);
        }
        
        //生成表格的 第三行
        Row rowThree = sheet.createRow(2);
        //设置第三行的 行高
        rowThree.setHeightInPoints(40);
        //遍历数组threeTitles 设置第三行的表头内容
        for(int i=0;i<threeTitles.length;i++){
        	//设置第三行的列宽
        	sheet.setColumnWidth(i, 6000);
        	//创建第三行中的每个单元格
        	Cell cell = rowThree.createCell(i);
        	cell.setCellValue(threeTitles[i]); 
        	cell.setCellStyle(style);
        }
        //去产品表中查询当前产品表中都有哪些产品名称 
        List<IntelligentLockProduct> productList = Ebean.getServer(GlobalDBControl.getDB()).createQuery(IntelligentLockProduct.class, "find intelligentlock_product where 1 = 1 and status = 0 and producttype is not null ").findList();
        //创建数组 用来存放 产品名称
        String[] productNameArray = new String[productList.size()];
        //遍历每个产品对象获取产品名称 用来设置Excel表中下拉选的内容
        for (int i = 0; i < productList.size(); i++) {
        	IntelligentLockProduct intelligentLockProduct = productList.get(i);
        	productNameArray[i] = intelligentLockProduct.getTitle();
		}
        //将下拉框数据放到新的sheet里，然后excle通过新的sheet数据加载下拉框数据
        Sheet hidden = workbook.createSheet("hidden");
        Cell cell = null;
        for (int i = 0, length = productNameArray.length; i < length; i++)
        {
            String name = productNameArray[i];
            Row row = hidden.createRow(i);
            cell = row.createCell(0);
            cell.setCellValue(name);
        }
 
        Name namedCell = workbook.createName();
        namedCell.setNameName("hidden");
        namedCell.setRefersToFormula("hidden!$A$1:$A$" + productNameArray.length);
        //加载数据,将名称为hidden的
        DVConstraint constraint = DVConstraint.createFormulaListConstraint("hidden");
        
        // 设置第一列的3-65534行为下拉列表
        // (3, 65534, 0, 0) ====> (起始行,结束行,起始列,结束列)
        CellRangeAddressList regions = new CellRangeAddressList(3, 65534, 0, 0);
        // 将设置下拉选的位置和数据的对应关系 绑定到一起
        DataValidation dataValidation = new HSSFDataValidation(regions, constraint);
        //将第二个sheet设置为隐藏
        workbook.setSheetHidden(1, true);
        
        sheet.addValidationData(dataValidation);
        
        //设置第三列的第二行为下拉选
        CellRangeAddressList typeRegions = new CellRangeAddressList(3, 65534, 5, 5);
        DVConstraint typeConstraint = DVConstraint.createExplicitListConstraint(types);
        DataValidation typeDataValidation = new HSSFDataValidation(typeRegions, typeConstraint);
        sheet.addValidationData(typeDataValidation);
        
        //在application.conf中配置的路径
		String path = Configuration.root().getString("export.path");
		int numStra = num++;//静态值，区分文件名
		File file = new File(path);
		file.mkdir();//判断文件夹是否存在，不存在就创建		
        
		FileOutputStream out = null;
		//创建备份的Excel模板文件名
		String fileName = path + "IntelligentLockOut" + System.currentTimeMillis() + numStra + ".xls";
		try {
			out = new FileOutputStream(fileName);
			workbook.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		File sampleFile = new File(fileName);
		if (request().getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) {
			fileNameChine = "=?UTF-8?B?" + (new String(Base64.encodeBase64(fileNameChine.getBytes("UTF-8")))) + "?=";    
        } else{
        	fileNameChine = java.net.URLEncoder.encode(fileNameChine, "UTF-8");
        }

		response().setHeader("Content-disposition","attachment;filename=" + fileNameChine);
		response().setContentType("application/msexcel");
		return ok(sampleFile);
	}
	
	static int num=0;
	public static File exportList(List<IntelligentLockOut> infoList,String fileNameChine) throws UnsupportedEncodingException {
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
				
			"出货单ID"
		
			,"流水号"
			
			,"产品名称"
			
			,"型号"
			
			,"规格"
		
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
			IntelligentLockOut info = (IntelligentLockOut) infoList.get(i);
			Row row = sheet.createRow(rowIndex++);
			
			helper.createStringCell( row , colIndex++, ""+info. getIdd () );	//产品ID
		
			helper.createStringCell( row , colIndex++, ""+info. getSerialnumber () );	//流水号
		
			helper.createStringCell( row , colIndex++, ""+info. getTotalprice () );	//总价
		
			helper.createStringCell( row , colIndex++, ""+info. getRemark () );	//备注
			
			helper.createStringCell( row , colIndex++, ""+simp.format(info. getAddtime ()) );	//添加时间
			
			helper.createStringCell( row , colIndex++, ""+info. getOperator () );	//操作人
		
			helper.createStringCell( row , colIndex++, ""+info. getOperatornumber () );	//操作号
		}
		//在application.conf中配置的路径
		String path = Configuration.root().getString("export.path");
		int numStra = num++;//静态值，区分文件名
		File file = new File(path);
		file.mkdir();//判断文件夹是否存在，不存在就创建
		
		FileOutputStream out = null;
		String fileName = path + "出库单" + System.currentTimeMillis() + numStra + ".xls";
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
	
	//出库单导入Excel表的方法
	static int MAX_LINES = 10000;
	//设置产品类型的白名单 即:当出库的产品名称对应的产品类型为集合中的元素时 可以上传产品名称为""
	static String [] productType={
		"锁体",
		"锁架",
		"导向片"
	};
	public static Result upload(){
		//开启事务
		Ebean.beginTransaction();
		int detailNum = 0;	//统计条数用
		int successNum = 0;	//统计条数用
		ReturnMessage returnMessage  = new ReturnMessage(); //创建对象用于返回页面处理结果
		MultipartFormData body = request().body().asMultipartFormData();
		//根据表单的name属性找到对应的数据
		FilePart file_excel = body.getFile("file_excel");
		//判断file_excel是否为空
		if (file_excel != null) {
			//获取文件的名称
			String fileName = file_excel.getFilename();
			int rowNum = 0;//导入Excel表里的有效记录数
			File file = file_excel.getFile();
			
			//ExcelUtils.getUploadPath() 获得文件的物理路径
			File fileTemp = new File(ExcelUtils.getUploadPath());
			//创建文件夹
			fileTemp.mkdir();
			
			//ExcelUtils.addTimeTagFileName(fileName) 在文件名上加当前时间标签
			String uploadFileName = ExcelUtils.getUploadPath() + ExcelUtils.addTimeTagFileName(fileName);
			
			//ExcelUtils.createFile(uploadFileName) 将一个文件名包装成一个文件对象
			File destFile = ExcelUtils.createFile(uploadFileName);
			
			//将临时文件file复制到 目标文件destFile中
			ExcelUtils.copy(file, destFile);
			file.delete();//将临时文件删除掉
			//Excel工作薄对象
			Workbook workBook = null;
			//创建文件输入流
			FileInputStream fis = null;
			
			try {
				//读写目标文件
				fis = new FileInputStream(new File(uploadFileName));
			} catch (FileNotFoundException e) {
				//returnMessage.setCode("");
				returnMessage.setMessage("创建本次上传的独立文件失败!");
				util.LogUtil.writeLog("导入失败!"+ "\t" + "错误信息:" + returnMessage.getMessage(), "outLog");
				return ok(Json.toJson(returnMessage));
			}
			
			try {
				workBook = WorkbookFactory.create(fis);
			} catch (Exception e) {
				//logger 记录器
				Logger.info(e.toString());
				//returnMessage.setCode("-2");
				returnMessage.setMessage("");
				return ok(Json.toJson(returnMessage));
			} 
			
			//写日志的类 和 方法
			//LogUtil.writeLog(con, logname);
			
			try{//进行事务提交
				//Excel工作表对象
				Sheet sheet = workBook.getSheetAt(0);
				
				//获取总行数
				int lastRowNumber = sheet.getLastRowNum();//0开始
				
				//判断第一行是否为一个空行
				Row row1 = sheet.getRow(0);
				if (checkNullOrNot(row1)) {
					//返回true说明为空
					//returnMessage.setCode("");
					returnMessage.setMessage("导入的Excel第一行为表头,不能为空行");
					util.LogUtil.writeLog("导入失败!"+ "\t" +"错误出现在导入Excel的第:1行" + "\t" + "错误信息:" + returnMessage.getMessage() ,"outLog");
					return ok(Json.toJson(returnMessage));
				}
				//判断第三行是否为一个空行
				Row row3 = sheet.getRow(2);
				if (checkNullOrNot(row3)) {
					//returnMessage.setCode("");
					returnMessage.setMessage("导入的Excel第三行为表头,不能为空行");
					util.LogUtil.writeLog("导入失败!"+ "\t" +"错误出现在导入Excel表的第:3行" + "\t" + "错误信息:" + returnMessage.getMessage() ,"outLog");
					return ok(Json.toJson(returnMessage));
				}
				//除表头以外的空行清除
				for (int i = 3; i <= lastRowNumber; i++) {
					Row row = sheet.getRow(i);
					if (checkNullOrNot(row)) {//返回true 为空行 则 rowNum不加1
						break;
					}
					rowNum++;
				}
				lastRowNumber=3+rowNum;
				
				//添加日志
				util.LogUtil.writeLog("导入的文件名称:" + fileName + "\t" + "本次上传的独立文件名称:" + uploadFileName + "\t" + "导入Excel表的数据行数:" + rowNum + "行" + "\t" + "操作人:" + Application.getSysUser().getLogin(),"outLog");
				
				//判断导入的Excel表第一行表头是否符合规范
				for(int i=0;i<oneTitles.length;i++){
					//将第一行表头的单元值转换为String类型
					String cellValue = StringUtil.cellValueToString(sheet.getRow(0).getCell(i));
					if(!cellValue.equalsIgnoreCase(oneTitles[i])){
						//returnMessage.setCode("");
						returnMessage.setMessage("导入的Excel第一行表头不符合规范");
						util.LogUtil.writeLog("导入失败!"+ "\t" +"错误出现Excel表的第1行的第:" + (i+1) + "个单元格" + "\t" + "错误信息:" + returnMessage.getMessage() ,"outLog");
						return ok(Json.toJson(returnMessage));
					}
				}
				//判断导入的Excel表第三行表头是否符合规范
				for(int i=0;i<threeTitles.length;i++){
					//将第三行表头的单元值转换为String类型
					String cellValue = StringUtil.cellValueToString(sheet.getRow(2).getCell(i));
					if(!cellValue.equalsIgnoreCase(threeTitles[i])){
						//returnMessage.setCode("");
						returnMessage.setMessage("导入的Excel第三行表头不符合规范");
						util.LogUtil.writeLog("导入失败!"+ "\t" +"错误出现Excel表的第3行的第:" + (i+1) + "个单元格" + "\t" + "错误信息:" + returnMessage.getMessage(),"outLog");
						return ok(Json.toJson(returnMessage));
					}
				}
				
				if( rowNum > MAX_LINES ){
					//returnMessage.setCode("");
					returnMessage.setMessage("导入的数据超过最大限制");
					util.LogUtil.writeLog("导入失败!"+ "\t" +"错误信息:" + returnMessage.getMessage(),"outLog");
					return ok(Json.toJson(returnMessage));
				}
				//创建一个集合用来保存 出库单明细对象(临时)
				List<IntelligentLockOutDetail> outDetailList = new ArrayList<IntelligentLockOutDetail>();
				//创建一个集合用来保存 出库单明细对象(最终数据) (出库明细的最终条数)
				List<IntelligentLockOutDetail> finalDetailList = new ArrayList<IntelligentLockOutDetail>();
				//创建一个集合用来保存 产品对象
				//List<IntelligentLockProduct> productList = new ArrayList<IntelligentLockProduct>();
				//创建一个集合用来保存 产品编码对象(判断导入的Excel表中的产品编码是否可用)
				//List<IntelligentLockProductCode> checkCodeList = new ArrayList<IntelligentLockProductCode>();
				//创建一个集合用来保存 产品编码表对象(临时)
				List<IntelligentLockProductCode> productCodeList = new ArrayList<IntelligentLockProductCode>();
				
				//创建一个集合用来保存 库存对象(数据库返回的数据) 用来获取 上次结余和本次结余等数据
				List<IntelligentLockStock> stockBatchList = new ArrayList<IntelligentLockStock>();
				//创建一个集合用来保存 库存对象(数据库返回的数据) 用来获取 上次结余和本次结余等数据
				List<IntelligentLockStock> haveCodeList = new ArrayList<IntelligentLockStock>();
				//创建一个集合用来保存 库存对象(数据库返回的数据) 用来保存库存对象
				List<IntelligentLockStock> saveStockList = new ArrayList<IntelligentLockStock>();
				
				//创建一个集合用来临时保存 出库明细 和 产品编码 的对应关系 用来辅助存储intelligentlock_out_detail_code数据
				List<IntelligentLockTemporaryOutDetailCode> temporaryList = new ArrayList<IntelligentLockTemporaryOutDetailCode>();
				
				//创建一个集合用来保存 出库单流水对象
				//List<IntelligentLockStockOutWater> outWaterList = new ArrayList<IntelligentLockStockOutWater>();
				
				//创建一个Map表用来(出库单明细中使用) 记录出库明细中的 产品ID+出货渠道+类型+订单号+价格 和 数量
				Map<String,Integer> outDetailMap = new HashMap<String,Integer>();
				//创建一个Map表用来 去除重複数据(最终出库明细表中使用) 记录 产品ID+出货渠道+类型+订单号+价格 和 数量 
				Map<String,Integer> finalDetailMap = new HashMap<String,Integer>();
				//遍历finalDetailList表中的产品ID 放入productNameMap中 去除重复的产品名称
				Map<Integer,Integer> productNameMap = new HashMap<Integer,Integer>();
				//创建一个Map表用来 记录 key:产品ID+生产批次 value:数量
				Map<String,Integer> amountMap = new HashMap<String,Integer>();
				//创建一个Map表用来 记录(产品编码不为null)key:产品ID+生产批次 value:数量
				Map<String,Integer> haveCodeAmountMap = new HashMap<String,Integer>();
				//创建一个Map表用来 记录无产品编码的产品的 key:商品名称 value:数量
				Map<String,Integer> logMap = new HashMap<String,Integer>();
				
				//创建一个Map表用来 记录 产品ID 和 数量 用于设置出库流水的 本次交易数
				//Map<Integer,Integer> outWaterBatchMap = new HashMap<Integer,Integer>();
				//创建一个Map表用来 记录 key:出库明细ID+产品ID+生产批次 value:数量(Map的size也就是出库流水的条数)
				//Map<String,Integer> outWaterMap = new HashMap<String,Integer>();
				//创建 产品对象
				//IntelligentLockProduct product = new IntelligentLockProduct();
				
				//创建 出库单对象
				IntelligentLockOut out = new IntelligentLockOut();
				
				//将添加时间设置为当前添加时间
				Date date = new Date();
				//String addtime=simp.format(date);
				
				//添加出库单的统一数据
				Row unifyrow = sheet.getRow(1);	//从第二行开始读取
				
				if(unifyrow!=null){
					//获取 出库单备注(总体)
					int j = 0;
					String globalRemark = StringUtil.cellValueToString(unifyrow.getCell(j));
					//设置出库单备注(总)
					out.setRemark(globalRemark);
				}else{
					out.setRemark("");
				}
				
				//设置出库单流水号
				out.setSerialnumber(StringUtil.getDateString());
				//设置出库单操作人
				out.setOperator(Application.getSysUser().getLogin());
				//设置出库单添加时间
				out.setAddtime(date);
				//设置出库单操作号
				String operatornumber = IDUtils.createID();
				out.setOperatornumber(operatornumber);
				
				//创建出库单总价
				double totalprice=0.0;
				
				//在for循环外 创建产品名称 方便后续获取
				String productName = null;
				
				//批量添加
				for (int i = 3; i < lastRowNumber; i++) {
					
					//创建 产品编码对象
					IntelligentLockProductCode productCode = new IntelligentLockProductCode();
					
					//创建 出库单明细对象
					IntelligentLockOutDetail outDetail = new IntelligentLockOutDetail();
					
					//创建 辅助保存 出库明细和产品编码 对应关系的对象
					IntelligentLockTemporaryOutDetailCode temporary = new IntelligentLockTemporaryOutDetailCode();
					
					//获取 单独的每条出库数据
					Row row = sheet.getRow(i);	//从第四行开始读取
					
					//获取 产品名称
					int column = 0;
					productName = StringUtil.cellValueToString(row.getCell(column));
					//根据产品名称 获取 对应的产品ID
					StringBuffer sql = new StringBuffer();
					sql.append("find IntelligentLockProduct where 1=1 and status = 0 ");
					sql.append(" and title = :productName");
					IntelligentLockProduct productObject = Ebean.getServer(GlobalDBControl.getDB())
							.createQuery(IntelligentLockProduct.class,sql.toString())
							.setParameter("productName", productName)
							.findUnique();
					//判断productObject对象是否为null (为null说明该产品名称无效)
					if(productObject==null){
						//returnMessage.setCode("");
						returnMessage.setMessage("无效的产品名称:"+productName);
						util.LogUtil.writeLog("导入失败!"+ "\t" +"错误出现Excel表的第:" + (i+1) + "行" + "\t" + "错误信息:" + returnMessage.getMessage(),"outLog");
						return ok(Json.toJson(returnMessage));
					}
					//获取产品ID
					Integer productid = productObject.getIdd();
					
					//获取 唯一产品编码
					column = column+1;
					String code = StringUtil.cellValueToString(row.getCell(column));
					//判断Excel上传的编码是否为空
					if(!code.equals("")){
						//判断导入的产品唯一编码是否为有效编码
						StringBuffer check = new StringBuffer();
						check.append("find IntelligentLockProductCode where 1=1");
						check.append(" and code = :code");
						IntelligentLockProductCode extraCheckCode = Ebean.getServer(GlobalDBControl.getDB())
								.createQuery(IntelligentLockProductCode.class,check.toString())
								.setParameter("code", code).findUnique();
						//找到与导入编码相匹配的产品
						//没找到相匹配的产品编码
						if(extraCheckCode==null){
							//returnMessage.setCode("");
							returnMessage.setMessage("无效的产品编码:"+code);
							util.LogUtil.writeLog("导入失败!"+ "\t" +"错误出现Excel表的第:" + (i+1) + "行" + "\t" + "错误信息:" + returnMessage.getMessage() ,"outLog");
							return ok(Json.toJson(returnMessage));
						}
						//判断 状态是否为 待售:0(integer) 退货待售：2（integer）
						if(extraCheckCode.getStatus()!=0&&extraCheckCode.getStatus()!=2){//状态为已售
							//returnMessage.setCode("");
							returnMessage.setMessage("产品编码为:"+code+"的产品已经被售出了");
							util.LogUtil.writeLog("导入失败!"+ "\t" +"错误出现Excel表的第:" + (i+1) + "行" + "\t" + "错误信息:" + returnMessage.getMessage() ,"outLog");
							return ok(Json.toJson(returnMessage));
						}
						//判断 该产品编码对应的产品名称 是否与上传的产品名称相匹配
						/*if(productid!=extraCheckCode.getProductid()){
							//returnMessage.setCode("");
							returnMessage.setMessage("产品编码:"+code+"与产品ID:"+productid+"不匹配");
							util.LogUtil.writeLog("导入失败!"+ "\t" +"错误出现Excel表的第:" + (i+1) + "行" + "\t" + "错误信息:" + returnMessage.getMessage() ,"outLog");
							return ok(Json.toJson(returnMessage));
						}*/
					}else{//产品编码为""
						//查看上传的产品名称的parent是否是 产品编码为""的产品
						StringBuffer check = new StringBuffer();
						check.append("find IntelligentLockProduct where 1=1 and status = 0 ");
						check.append("and title = :title");
						IntelligentLockProduct extraCheck = Ebean.getServer(GlobalDBControl.getDB())
								.createQuery(IntelligentLockProduct.class,check.toString())
								.setParameter("title", productName).findUnique();
						if(extraCheck!=null){
							for(int j = 0;j<productType.length;j++){
								if(productType[j].equals(extraCheck.getProducttype())){
									break;
								}
								//若该产品名称对应的产品类型 与 白名单中的所有元素都不匹配
								if(j==(productType.length-1)&&(productType[j].equals(extraCheck.getProducttype())==false)){
									//returnMessage.setCode("");
									returnMessage.setMessage("产品名称:"+productName+"的产品的唯一编码不能为空");
									util.LogUtil.writeLog("导入失败!"+ "\t" +"错误出现Excel表的第:" + (i+1) + "行" + "\t" + "错误信息:" + returnMessage.getMessage() ,"outLog");
									return ok(Json.toJson(returnMessage));
								}
							}
						}else {
							returnMessage.setMessage("产品名称:"+productName+"的产品不存在或已下架,请选择其它商品出库");
							util.LogUtil.writeLog("导入失败!"+ "\t" +"错误出现Excel表的第:" + (i+1) + "行" + "\t" + "错误信息:" + returnMessage.getMessage() ,"outLog");
							return ok(Json.toJson(returnMessage));
						}
						
					}
					
					//获取 数量
					column = column+1;
					Integer amount =  StringTool.GetInt(StringUtil.cellValueToString(row.getCell(column)), 0);
					//判断是否满足产品编码不为null且数量为1
					if(!code.equals("")){
						if(amount!=1){
							//returnMessage.setCode("");
							returnMessage.setMessage("产品编码为:"+code+"的产品数量填写有误");
							util.LogUtil.writeLog("导入失败!"+ "\t" +"错误出现Excel表的第:" + (i+1) + "行" + "\t" + "错误信息:" + returnMessage.getMessage() ,"outLog");
							return ok(Json.toJson(returnMessage));
						}
					}
					//判断添加的数量是否 符合规范
					if(amount<=0){
						//returnMessage.setCode("");
						returnMessage.setMessage("产品名称:"+productName+"的产品的数量填写有误");
						util.LogUtil.writeLog("导入失败!"+ "\t" +"错误出现Excel表的第:" + (i+1) + "行" + "\t" + "错误信息:" + returnMessage.getMessage() ,"outLog");
						return ok(Json.toJson(returnMessage));
					}
					
					//判断如果产品编码为"",则将出库数量和产品名称保存到对象中 用来记录日志
					if(code.equals("")){
						//判断logMap表中是否存在该productName
						if(!logMap.containsKey(productName)){
							logMap.put(productName, amount);//不存在
						}else{//存在
							logMap.put(productName, logMap.get(productName)+1);
						}
					}
					
					//获取 出库单价
					column = column+1;
					Double price =  StringTool.GetDouble(StringUtil.cellValueToString(row.getCell(column)), 0.0);
					if(price<0){
						//returnMessage.setCode("");
						returnMessage.setMessage("产品名称:"+productName+"的产品的单价填写有误");
						util.LogUtil.writeLog("导入失败!"+ "\t" +"错误出现Excel表的第:" + (i+1) + "行" + "\t" + "错误信息:" + returnMessage.getMessage() ,"outLog");
						return ok(Json.toJson(returnMessage));
					}
					
					//获取 出货渠道
					column = column+1;
					String channel = StringUtil.cellValueToString(row.getCell(column));
					
					//获取 类型
					column = column+1;
					String type = StringUtil.cellValueToString(row.getCell(column));
					
					//获取 外部销售平台订单号
					column = column+1;
					String salenumber = StringUtil.cellValueToString(row.getCell(column));
					
					//获取 备注
					column = column+1;
					String remark = StringUtil.cellValueToString(row.getCell(column));
					
					//把每种产品的(价格*数量)加起来
					totalprice+=amount*price;
					
					//根据产品ID查询到对应的产品名称 型号 规格
					StringBuffer buffer = new StringBuffer();
					buffer.append("find IntelligentLockProduct where 1 = 1");
					buffer.append(" and idd = :productid");
					IntelligentLockProduct pmo = Ebean.getServer(GlobalDBControl.getDB())
							.createQuery(IntelligentLockProduct.class,buffer.toString())
							.setParameter("productid", productid).findUnique();
					//设置出库单明细中的产品名称
					outDetail.setTitle(pmo.getTitle());
					//设置出库单明细中的型号
					outDetail.setModel(pmo.getModel());
					//设置出库单明细中的规格
					outDetail.setSpec(pmo.getSpec());
					
					//设置 出库单明细中的  总数
					outDetail.setAmount(amount);
					//设置 出库单明细中的 单价
					outDetail.setPrice(price);
					//设置 出库单明细中的 出货渠道
					outDetail.setChannel(channel);
					//设置 出库单明细中的 类型
					outDetail.setType(type);
					//设置 出库单明细中的 外部销售平台订单号
					outDetail.setSalenumber(salenumber);
					//设置 出库单明细中的 备注
					outDetail.setRemark(remark);
					//设置 出库单明细中的 产品ID
					outDetail.setProductid(productid);
					
					//用于 根据产品ID+渠道+类型+平台订单号 设置出库明细用
					String key = outDetail.getProductid()+outDetail.getChannel()+outDetail.getType()+outDetail.getSalenumber()+outDetail.getPrice();
					if(!code.equals("")){
						if(!outDetailMap.containsKey(key)){
							outDetailMap.put(key, 1);
						}else{
							outDetailMap.put(key, outDetailMap.get(key)+1);
						}
					}else{
						if(!outDetailMap.containsKey(key)){
							outDetailMap.put(key, amount);
						}else{
							outDetailMap.put(key, outDetailMap.get(key)+amount);
						}
					}
					
					//统计 产品ID 和 数量 将重复的产品ID去掉 用来记录产品编码为"" 的出库数量
					if(!code.equals("")){
						//产品编码不为null 则记录产品ID就可以 value值无意义
						if(!productNameMap.containsKey(productid)){
							productNameMap.put(productid, 1);
						}
					}else{//为null 将Excel表中的 不同批次的 产品数量加起来
						if(!productNameMap.containsKey(productid)){
							productNameMap.put(productid, amount);
						}else{
							productNameMap.put(productid, productNameMap.get(productid)+amount);
						}
						
					}
					
					//将出库明细对象保存到 出库明细集合(临时)中
					outDetailList.add(outDetail);
					
					//设置 产品编码表中的 锁唯一编码
					productCode.setCode(code);
					//设置 产品编码表中的 产品ID
					productCode.setProductid(productid);
					//设置 产品编码表中的 操作号
					productCode.setOperatornumber(operatornumber);
					//将 产品编码对象添加到list集合中
					productCodeList.add(productCode);
					
					if(!code.equals("")){
						//设置辅助 保存出库明细和产品编码 对象的属性
						temporary.setProductid(productid);
						temporary.setChannel(channel);
						temporary.setSalenumber(salenumber);
						temporary.setType(type);
						temporary.setCode(code);
						temporary.setOperatornumber(operatornumber);
						//保存到集合中
						temporaryList.add(temporary);
					}
				}
				//遍历 出库明细集合(临时)
				for(IntelligentLockOutDetail extraOutDetail:outDetailList){
					//遍历outDetailMap表 设置出库明细对象 的数量 和 出库单对象的总价
					for(String key:outDetailMap.keySet()){
						// 看集合中的 productid是否和 outDetailMap中的key(productid)相同
						if(key.equals(extraOutDetail.getProductid()+extraOutDetail.getChannel()+extraOutDetail.getType()+extraOutDetail.getSalenumber()+extraOutDetail.getPrice())){
							//创建 最终的出库明细对象 重新设定最终的出库明细对象的属性
							IntelligentLockOutDetail finalDetail = new IntelligentLockOutDetail();
							//产品ID
							finalDetail.setProductid(extraOutDetail.getProductid());
							//产品名称
							finalDetail.setTitle(extraOutDetail.getTitle());
							//型号
							finalDetail.setModel(extraOutDetail.getModel());
							//规格
							finalDetail.setSpec(extraOutDetail.getSpec());
							//添加时间
							finalDetail.setAddtime(date);
							//操作号
							finalDetail.setOperatornumber(operatornumber);
							//操作人
							finalDetail.setOperator(Application.getSysUser().getLogin());
							//单价
							finalDetail.setPrice(extraOutDetail.getPrice());
							
							//总数
							finalDetail.setAmount(outDetailMap.get(key));
							//总价
							finalDetail.setTotalprice(outDetailMap.get(key)*extraOutDetail.getPrice());
							
							//出货渠道
							finalDetail.setChannel(extraOutDetail.getChannel());
							//类型
							finalDetail.setType(extraOutDetail.getType());
							//外部销售平台订单号
							finalDetail.setSalenumber(extraOutDetail.getSalenumber());
							//备注
							finalDetail.setRemark(extraOutDetail.getRemark());
							
							//将 key:该产品ID+渠道+类型+平台订单号+价格 value:数量 保存到finalDetailMap表中
							String extraKey = extraOutDetail.getProductid()+extraOutDetail.getChannel()+extraOutDetail.getType()+extraOutDetail.getSalenumber()+extraOutDetail.getPrice();
							if(!finalDetailMap.containsKey(extraKey)){
								finalDetailMap.put(extraKey, 1);
								//将finalDetail对象添加到finalDetailList集合中
								finalDetailList.add(finalDetail);
							}
						}
					}
				}
				
				//设置出库单总价
				out.setTotalprice(totalprice);
				
				//将出库单对象存到数据库
				Ebean.getServer(GlobalDBControl.getDB()).save(out);
				util.LogUtil.writeLog("成功将出库单对象保存到数据库outid为:"+out.getIdd(),"outLog");
				
				//遍历finalDetailList 设置 对象的 出库单ID
				for(IntelligentLockOutDetail finalOut:finalDetailList){
					finalOut.setOutid(out.getIdd());
					
				}
				if(finalDetailList!=null&&finalDetailList.size()>0){
					//将出库单明细对象 存到数据库
					Ebean.getServer(GlobalDBControl.getDB()).save(finalDetailList);
					util.LogUtil.writeLog("成功将出库明细对象保存到数据库" ,"outLog");
				}
				
				//遍历temporaryList集合	存储intelligentlock_out_detail_code表的数据
				if(temporaryList!=null&&temporaryList.size()>0){
					for(IntelligentLockTemporaryOutDetailCode temporary:temporaryList){
						//创建 出库明细和产品编码的 关联对象
						IntelligentLockOutDetailCode outDetailCode = new IntelligentLockOutDetailCode();
						//获取 出库明细idd
						Integer outdetailid = 
								selectOutDetail(temporary.getProductid(),temporary.getChannel(),temporary.getType(),temporary.getSalenumber(),temporary.getOperatornumber());
						if(outdetailid!=null){
							outDetailCode.setOutdetailid(outdetailid);
						}
						//获取 编码表id
						Integer codeid = selectProductCode(temporary.getCode());
						if(codeid!=null){
							outDetailCode.setCodeid(codeid);
						}
						outDetailCode.setAddtime(date);
						outDetailCode.setOperatornumber(operatornumber);
						//将temporary对象保存到数据库中
						saveOutDetailCode(outDetailCode);
					}
				}
				//遍历 产品编码集合(临时) 用来获取产品编码
				for(IntelligentLockProductCode extraProductCode:productCodeList){
					//首先判断 产品编码是否为null
					if(!extraProductCode.getCode().equals("")){
						//根据产品编码 去产品编码表中 获取 生产批次
						StringBuffer sql = new StringBuffer();
						sql.append("find IntelligentLockProductCode where 1=1");
						sql.append(" and code = :code");
						//将获取到的 产品编码对象放到产品编码codeBatchidList集合中(接收数据库返回数据)
						List<IntelligentLockProductCode> codeBatchidList = Ebean.getServer(GlobalDBControl.getDB())
								.createQuery(IntelligentLockProductCode.class,sql.toString())
								.setParameter("code", extraProductCode.getCode())
								.findList();
						if(codeBatchidList!=null&&codeBatchidList.size()>0){
							//遍历batchidList集合获取 生产批次
							for(IntelligentLockProductCode extracatchid:codeBatchidList){
								//将查询的生产批次设置给 产品编码(临时)表 的对象中
								extraProductCode.setBatchid(extracatchid.getBatchid());
							}
						}
					}else{//产品编码为null
						/*//根据产品ID 去库存中获取 生产批次
						StringBuffer sql = new StringBuffer();
						sql.append("find IntelligentLockStock where 1=1");
						sql.append(" and productid = :productid");
						//将获取到的 产品编码对象放到产品编码catchidList集合中(接收数据库返回数据)
						List<IntelligentLockStock> batchList = Ebean.getServer(GlobalDBControl.getDB())
								.createQuery(IntelligentLockStock.class,sql.toString())
								.setParameter("productid", extraProductCode.getProductid())
								.findList();*/
						extraProductCode.setBatchid("");
					}
				}
				
				String amountMapKey = null;
				//遍历产品编码表(临时) 获取产品ID+生产批次 用于统计 库存数量 设置出库流水/库存中 产品编码不为""的本次交易数 
				for(IntelligentLockProductCode extraProductCode:productCodeList){
					amountMapKey = ""+extraProductCode.getProductid()+extraProductCode.getBatchid();
					//将 产品ID+生产批次 存入amountMap表中 用于统计数量
					if(!amountMap.containsKey(amountMapKey)){//不包含
						amountMap.put(amountMapKey, 1);
					}else{
						amountMap.put(amountMapKey, amountMap.get(amountMapKey)+1);
					}
				}
				
				//首先遍历 productNameMap表 用来获取产品ID 和 对应的数量
				for(Integer setWaterKey:productNameMap.keySet()){
					//创建 出库流水对象
					IntelligentLockStockOutWater outWater = new IntelligentLockStockOutWater();
					//设置出库单流水中的 出库单ID
					outWater.setOutid(out.getIdd());
					//设置出库单流水中的 产品ID
					outWater.setProductid(setWaterKey);
					//设置出库单流水中的 操作人
					outWater.setOperator(Application.getSysUser().getLogin());
					//设置出库单流水中的 添加时间
					outWater.setAddtime(date);
					//设置出库单流水中的 操作号
					outWater.setOperatornumber(operatornumber);
					//*********设置出库流水表数据*********
					//然后在遍历amountMap表 来设置出库流水的 生产批次和本次交易数
					for(String setOutWater:amountMap.keySet()){
						//遍历 产品编码表(临时) 获取(生产批次)唯一编码
						for(IntelligentLockProductCode extraProductCode:productCodeList){
							//判断 产品ID+生产批次 是否与 amountMap表中的Key值相同
							if(setOutWater.equals(setWaterKey+extraProductCode.getBatchid())){
								//首先判断 产品编码是否为null (若生产批次为""则产品编码一定为"")
								if(!(extraProductCode.getBatchid().equals(""))){//不为null
									//设置本次交易数
									outWater.setTrascatnumber(amountMap.get(setOutWater));
									//设置生产批次
									outWater.setBatchid(extraProductCode.getBatchid());
									//判断该出库流水对象是否保存过 
									if(!haveCodeAmountMap.containsKey(outWater.getProductid()+outWater.getBatchid())){
										//将其保存到haveCodeAmountMap中
										haveCodeAmountMap.put(outWater.getProductid()+outWater.getBatchid(), 1);
										//将设置好的 出库流水对象保存到数据库
										Ebean.getServer(GlobalDBControl.getDB()).save(outWater);
										util.LogUtil.writeLog("成功将出库流水对象保存到数据库outWaterId为:"+outWater.getIdd(),"outLog");
									}
								}else{
									//记录 出库流水的本次交易数(在库存<本次交易数时使用)
									Integer outWaterNum = productNameMap.get(setWaterKey);
									//若产品编码为null 则根据 产品编码表(临时)获取产品ID 去库存表中去查找对应的 生产批次
									StringBuffer sql = new StringBuffer();
									sql.append("find IntelligentLockStock where 1 = 1");
									sql.append(" and productid = :productid");
									//将查询结果放到 stockBatchList 集合中(结果可能为好多生产批次)
									stockBatchList = Ebean.getServer(GlobalDBControl.getDB())
											.createQuery(IntelligentLockStock.class,sql.toString())
											.setParameter("productid", setWaterKey)
											.findList();
									//遍历 stockBatchList 寻找出库的生产批次
									for(IntelligentLockStock extraStockBatch:stockBatchList){
										//判断该产品ID下 该生产批次下 本次结余是否>= 本次出库的数量
										if(extraStockBatch.getCurrentsurplus()>=outWaterNum){//大于
											//设置 出库流水对象的 生产批次
											outWater.setBatchid(extraStockBatch.getBatchid());
											//设置 出库流水对象的 本次交易数
											outWater.setTrascatnumber(outWaterNum);
											//将出库流水对象保存到数据库中
											Ebean.getServer(GlobalDBControl.getDB()).save(outWater);
											util.LogUtil.writeLog("成功将出库流水对象保存到数据库outWaterId为:"+outWater.getIdd(),"outLog");
										}else{//若该 产品ID下的 该生产批次下 数量<出库数量
											//将 出库的数量减掉
											outWaterNum-=extraStockBatch.getCurrentsurplus();
											//创建 出库流水对象
											IntelligentLockStockOutWater outWaterSpecial = new IntelligentLockStockOutWater();
											//设置出库单流水中的 出库单ID
											outWaterSpecial.setOutid(out.getIdd());
											//设置出库单流水中的 产品ID
											outWaterSpecial.setProductid(setWaterKey);
											//设置出库单流水中的 操作人
											outWaterSpecial.setOperator(Application.getSysUser().getLogin());
											//设置出库单流水中的 添加时间
											outWaterSpecial.setAddtime(date);
											//设置出库单流水中的 操作号
											outWaterSpecial.setOperatornumber(operatornumber);
											//设置 出库流水对象的 生产批次
											outWaterSpecial.setBatchid(extraStockBatch.getBatchid());
											//设置 出库流水对象的 本次交易数
											outWaterSpecial.setTrascatnumber(extraStockBatch.getCurrentsurplus());
											//将出库流水对象保存到数据库中
											Ebean.getServer(GlobalDBControl.getDB()).save(outWaterSpecial);
											util.LogUtil.writeLog("成功将出库流水对象保存到数据库outWaterId为:"+outWaterSpecial.getIdd(),"outLog");
										}
									}
								}
							}
						}
					}
				}
				//首先遍历 productNameMap表 用来获取产品ID 和 对应的数量
				for(Integer setStockKey:productNameMap.keySet()){
					//创建 库存对象
					IntelligentLockStock stock = new IntelligentLockStock();
					//设置 库存对象的 添加时间 
					stock.setAddtime(date);
					//设置 库存对象的 产品ID
					stock.setProductid(setStockKey);
					//设置 库存对象的 产品名称(将导入的Excel表中的产品名称提到方法外,提高作用域,直接获取)
					stock.setTitle(productName);
					//设置 库存对象的 生产厂家(根据产品ID去入库明细中查找)
					StringBuffer str = new StringBuffer();
					List<IntelligentLockInDetail> titleList = new ArrayList<IntelligentLockInDetail>();
					str.append("find IntelligentLockInDetail where 1 = 1");
					str.append(" and productid = :productid");
					titleList = Ebean.getServer(GlobalDBControl.getDB())
							.createQuery(IntelligentLockInDetail.class,str.toString())
							.setParameter("productid", setStockKey)
							.findList();
					//判断 根据产品ID 是否查询到数据
					if(titleList.size()>0){
						stock.setFacturername(titleList.get(0).getManufacturer());
					}else{
						//returnMessage.setCode("");
						returnMessage.setMessage("入库明细中找不到产品ID为:"+setStockKey+"的产品");
						util.LogUtil.writeLog("导入失败!"+ "\t" + "错误信息:" + returnMessage.getMessage() ,"outLog");
						return ok(Json.toJson(returnMessage));
					}
					//设置 库存对象的 操作人
					stock.setOperator(Application.getSysUser().getLogin());
					//然后在遍历amountMap表 来设置库存的 生产批次和结余数等数据
					for(String setStock:amountMap.keySet()){
						//遍历 产品编码表(临时) 获取(生产批次)唯一编码
						for(IntelligentLockProductCode extraProductCode:productCodeList){
							//判断 产品ID+生产批次 是否与 amountMap表中的Key值相同
							if(setStock.equals(setStockKey+extraProductCode.getBatchid())){
								//首先判断 产品编码是否为null (若生产批次不为"" 则产品编码一定不为"")
								if(!(extraProductCode.getBatchid().equals(""))){//不为null
									//设置 库存对象的 生产批次
									stock.setBatchid(extraProductCode.getBatchid());
									//设置 库存对象的 本次交易数
									stock.setTranscatnumber(amountMap.get(setStockKey+extraProductCode.getBatchid()));
									//根据产品ID和生产批次 去数据库查询 本次结余数的值
									StringBuffer sql = new StringBuffer();
									sql.append("find IntelligentLockStock where 1 = 1");
									sql.append(" and productid = :productid");
									sql.append(" and batchid = :batchid");
									//将查询结果放到 haveCodeList 集合中
									haveCodeList = Ebean.getServer(GlobalDBControl.getDB())
											.createQuery(IntelligentLockStock.class,sql.toString())
											.setParameter("productid", extraProductCode.getProductid())
											.setParameter("batchid", extraProductCode.getBatchid()).findList();
									if(haveCodeList!=null&&haveCodeList.size()>0){
										//遍历 haveCodeList 集合
										for(IntelligentLockStock extraHaveCode:haveCodeList){
											//判断本次结余的数量是否>=本次出库的数量
											if(extraHaveCode.getCurrentsurplus()>=amountMap.get(setStockKey+extraProductCode.getBatchid())){
												//设置 库存对象的 本次结余数设置为上次结余数
												stock.setLastsurplus(extraHaveCode.getCurrentsurplus());
												//设置 库存对象的 本次结余设置为(本次结余-本次交易数)
												stock.setCurrentsurplus(extraHaveCode.getCurrentsurplus()-amountMap.get(setStockKey+extraProductCode.getBatchid()));
												//将库存对象保存到saveStockList集合中
												saveStockList.add(stock);
											}
										}
									}
								}else{
									//记录 库存的本次交易数(在库存<本次交易数时使用)
									Integer stockNum = productNameMap.get(setStockKey);
									//若产品编码为null 则根据 产品编码表(临时)获取产品ID 去库存表中去查找对应的 生产批次
									StringBuffer sql = new StringBuffer();
									sql.append("find IntelligentLockStock where 1 = 1");
									sql.append(" and productid = :productid");
									//将查询结果放到 stockBatchList 集合中(结果可能为好多生产批次)
									stockBatchList = Ebean.getServer(GlobalDBControl.getDB())
											.createQuery(IntelligentLockStock.class,sql.toString())
											.setParameter("productid", setStockKey)
											.findList();
									//遍历 stockBatchList 寻找出库的生产批次
									for(IntelligentLockStock extraStockBatch:stockBatchList){
										//判断该产品ID下 该生产批次下 本次结余是否>= 本次出库的数量
										if(extraStockBatch.getCurrentsurplus()>=stockNum){//大于
											//设置 库存对象的 本次结余数设置为上次结余数
											stock.setLastsurplus(extraStockBatch.getCurrentsurplus());
											//设置 库存对象的 本次交易数设置为 本次出库的数量
											stock.setTranscatnumber(stockNum);
											//设置 库存对象的 本次结余设置为(本次结余-本次交易数)
											stock.setCurrentsurplus(extraStockBatch.getCurrentsurplus()-stockNum);
											//设置 库存对象的 生产批次
											stock.setBatchid(extraStockBatch.getBatchid());
											//将库存对象保存到saveStockList集合中
											saveStockList.add(stock);
										}else{//若该 产品ID下的 该生产批次下 数量<出库数量
											//将 出库的数量减掉
											stockNum-=extraStockBatch.getCurrentsurplus();
											//创建 库存对象
											IntelligentLockStock stockSpecial = new IntelligentLockStock();
											//设置 库存对象的 添加时间 
											stockSpecial.setAddtime(date);
											//设置 库存对象的 产品ID
											stockSpecial.setProductid(setStockKey);
											//设置 库存对象的 操作人
											stockSpecial.setOperator(Application.getSysUser().getLogin());
											//上次结余数
											stockSpecial.setLastsurplus(extraStockBatch.getCurrentsurplus());
											//本次交易数 (剩多少全都出库了)
											stockSpecial.setTranscatnumber(extraStockBatch.getCurrentsurplus());
											//本次结余0 
											stockSpecial.setCurrentsurplus(0);
											//生产批次
											stockSpecial.setBatchid(extraStockBatch.getBatchid());
											//将库存对象保存到saveStockList集合中
											saveStockList.add(stockSpecial);
										}
									}
									//***********庫存不足時抛出異常
									if(stockBatchList.size()<=0&&stockNum>0){
										//returnMessage.setCode("");
										returnMessage.setMessage("库存中产品ID为:"+setStockKey+"的产品,库存剩余量不足");
										util.LogUtil.writeLog("导入失败!"+ "\t" + "错误信息:" + returnMessage.getMessage() ,"outLog");
										return ok(Json.toJson(returnMessage));
									}
								}
							}
						}
					}
				}
				//修改库存 中的结余数
				if( saveStockList.size()>0 ){
					for(IntelligentLockStock updateStock:saveStockList){
						Ebean.getServer(GlobalDBControl.getDB())
								.createSqlUpdate("UPDATE intelligentlock_stock SET lastsurplus = :lastsurplus , transcatnumber = :transcatnumber , currentsurplus = :currentsurplus , addtime = :addtime , operator = :operator WHERE batchid = :batchid and productid = :productid")
								.setParameter("productid", updateStock.getProductid())
								.setParameter("batchid", updateStock.getBatchid())
								.setParameter("lastsurplus", updateStock.getLastsurplus())
								.setParameter("transcatnumber", updateStock.getTranscatnumber())
								.setParameter("currentsurplus", updateStock.getCurrentsurplus())
								.setParameter("addtime", updateStock.getAddtime())
								.setParameter("operator", updateStock.getOperator())
								.execute();
						util.LogUtil.writeLog("成功修改库存中的结余,产品id为:"+updateStock.getProductid(),"outLog");
					}
				}
				
				//遍历产品编码List(临时)获取到出库的每个产品的产品编码
				for(IntelligentLockProductCode extraProductCode:productCodeList){
					//将遍历出来的产品编码 中的状态"待售"改为"已售"
					Ebean.getServer(GlobalDBControl.getDB())
							.createSqlUpdate("UPDATE intelligentlock_product_code SET status = 1 WHERE code = :code")
							.setParameter("code", extraProductCode.getCode())
							.execute();
					util.LogUtil.writeLog("成功修改锁编码表的状态信息为1,修改的code为:"+extraProductCode.getCode(),"outLog");
				}
				//导入成功 生成 出库明细 的条数
				detailNum=finalDetailList.size();
				//导入成功 生成 库存明细/出库流水 的条数
				successNum=amountMap.size();
				returnMessage.setCode("0");
				returnMessage.setMessage("1");
				util.LogUtil.writeLog("导入完成" + "\t" + "成功导入出库单:1条" + "\t" + "对应生成出库明细:" + detailNum + "条" + "\t" + "生成出库流水:" + successNum + "条" + "\t" + "生成库存明细:" + successNum + "条" ,"outLog");
				//遍历productCodeList集合取出 本次出库的锁编码 并记录日志
				for(IntelligentLockProductCode lockCode:productCodeList){
					util.LogUtil.writeLog("出库单id为:" + finalDetailList.get(0).getOutid() + "\t" + "本次出库的锁对应的锁编码有:" + lockCode.getCode(), "outLog");
				}
				//遍历logMap表 取出 key:商品名称 value:数量
				Set<Entry<String,Integer>> entrySet = logMap.entrySet();
				for(Entry<String,Integer> e:entrySet){
					util.LogUtil.writeLog("本次出库的无所编码的产品:" + "\t" + "对应的产品名称为:" + e.getKey() + "\t" + "数量为:" + e.getValue(), "outLog");
				}
				Ebean.commitTransaction();
				return ok(Json.toJson(returnMessage));
			}catch(Exception e){//事务回滚
				Ebean.rollbackTransaction();
				returnMessage.setMessage("数据出错");
				util.LogUtil.writeLog("导入失败!" + "\t" + "失败原因:" + returnMessage.getMessage()+"出错原因:"+e.getMessage(),"outLog");
				return ok(Json.toJson(returnMessage));
			}finally{//结束事务
				Ebean.endTransaction();
			}
		}else{//文件为null
			//returnMessage.setCode("");
			returnMessage.setMessage("没有找到上传的文件");
			util.LogUtil.writeLog("导入失败!" + "\t" + "失败原因:" + returnMessage.getMessage(),"outLog");
			Ebean.endTransaction();
			return ok(Json.toJson(returnMessage));
		}
	}
	
	//检查是否是一个完全的空行 返回true是一个完全的空行 false:不是
	public static boolean checkNullOrNot(Row row){
		if(row==null)
			return true;
		int index=0;
		//判断第一行/二数据是否为一个空行 (目前第二行不判断)
		if(row.getRowNum()==0||row.getRowNum()==1){
			for(int i=0;i<oneTitles.length;i++){
				String cellValue = StringUtil.cellValueToString(row.getCell(i));
				if(cellValue==null||cellValue.trim().equals("")){
					index++;
				}
			}
			return index == oneTitles.length;
		}else{
			//判断第三行以后的数据是否为一个空行
			for(int i=0;i<threeTitles.length;i++){
				String cellValue = StringUtil.cellValueToString(row.getCell(i));
				if(cellValue==null||cellValue.trim().equals("")){
					index++;
				}
			}
			return index == threeTitles.length;
		}
	}
	
	//根据条件去 intelligentlock_out_detail表中 查询该条数据的idd
	public static Integer selectOutDetail(Integer productid,String channel,String type,String salenumber,String operatornumber){
		//获取出库明细idd
		IntelligentLockOutDetail outDetail= Ebean.getServer(GlobalDBControl.getDB())
			.find(IntelligentLockOutDetail.class)
			.where().eq("productid", productid)
			.eq("channel", channel)
			.eq("type", type)
			.eq("salenumber", salenumber)
			.eq("operatornumber", operatornumber)
			.findUnique();
		if(outDetail!=null){
			return outDetail.getIdd();
		}
		return null;
	}
	
	//根据code值去 intelligentlock_product_code表中 查询该条数据的idd
	public static Integer selectProductCode(String code){
		IntelligentLockProductCode productCode = Ebean.getServer(GlobalDBControl.getDB())
				.find(IntelligentLockProductCode.class)
				.where().eq("code", code).findUnique();
		if(productCode!=null){
			return productCode.getIdd();
		}
		return null;
	}
	
	//将 出库明细和产品编码的 关联对象 保存到intelligentlock_out_detail_code表中
	public static void saveOutDetailCode(IntelligentLockOutDetailCode outDetailCode){
		if(outDetailCode!=null){
			Ebean.getServer(GlobalDBControl.getDB()).save(outDetailCode);
			util.LogUtil.writeLog("成功将出库明细和产品编码的 关联数据保存id为:"+outDetailCode.getIdd(),"outLog");
		}
	}
	
	//根据时间戳随机生成20位的操作码
	public static class IDUtils {
		private static final byte[] LOCK = new byte[0];
		// 位数，默认是8位
		private final static long W = 10000000;
	 
		public static String createID() {
			long r = 0;
			synchronized (LOCK) {
				r = (long) ((Math.random() + 1) * W);
			}
			return System.currentTimeMillis() + String.valueOf(r).substring(1);
		}
	}
}