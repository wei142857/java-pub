
package controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import models.IntelligentLockProduct;
import models.SubOrders;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
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
import views.html.page.SubOrdersList;

import com.avaje.ebean.Ebean;

@Security.Authenticated(Secured.class)
public class SubOrdersAction extends Controller {
	public static List<String> SKN_M6 = Arrays.asList("skn-M6", "/public/uploadImg/IPI/20190805/1564991619925.jpg");
	//下标 1 为香槟金图片  2为钢琴黑图片
	public static List<String> SKN_N1 = Arrays.asList("skn-N1", "/public/uploadImg/IPI/20190805/1564991154998.jpg","/public/uploadImg/IPI/20190805/1564990786244.jpg");
	public static List<String> SKN_A7 =  Arrays.asList("skn-A7","/public/uploadImg/IPI/20190805/1564986213921.jpg");
  
	//进入页面列表；
	public static Result view() {
		return ok( SubOrdersList.render() );
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
		sql.append("find SubOrders where 1=1 ");
		
		String idd = StringUtil.getHttpParam(request(), "idd");
		if(idd==null)
			idd = "";	
		if ( !( idd .equals("") ) && !( idd .equals("undefined") ) ) 
			sql.append(" and ( idd= '" + idd + "'  )");
		
		String title = StringUtil.getHttpParam(request(), "title");
		if(title==null)
			title = "";	
		if ( !( title .equals("") ) && !( title .equals("undefined") ) ) 
			sql.append(" and ( title= '" + title + "'  )");
		
		String smallimgurl = StringUtil.getHttpParam(request(), "smallimgurl");
		if(smallimgurl==null)
			smallimgurl = "";	
		if ( !( smallimgurl .equals("") ) && !( smallimgurl .equals("undefined") ) ) 
			sql.append(" and ( smallimgurl= '" + smallimgurl + "'  )");
		
		String name = StringUtil.getHttpParam(request(), "name");
		if(name==null)
			name = "";	
		if ( !( name .equals("") ) && !( name .equals("undefined") ) ) 
			sql.append(" and ( name= '" + name + "'  )");
		
		String area = StringUtil.getHttpParam(request(), "area");
		if(area==null)
			area = "";	
		if ( !( area .equals("") ) && !( area .equals("undefined") ) ) 
			sql.append(" and ( area= '" + area + "'  )");
		
		String address = StringUtil.getHttpParam(request(), "address");
		if(address==null)
			address = "";	
		if ( !( address .equals("") ) && !( address .equals("undefined") ) ) 
			sql.append(" and ( address= '" + address + "'  )");
		
		String phone = StringUtil.getHttpParam(request(), "phone");
		if(phone==null)
			phone = "";	
		if ( !( phone .equals("") ) && !( phone .equals("undefined") ) ) 
			sql.append(" and ( phone= '" + phone + "'  )");
		
		String addtime = StringUtil.getHttpParam(request(), "addtime");
		if(addtime==null)
			addtime = "";	
		if ( !( addtime .equals("") ) && !( addtime .equals("undefined") ) ) 
			sql.append(" and ( addtime= '" + addtime + "'  )");
		
		String orderid = StringUtil.getHttpParam(request(), "orderid");
		if(orderid==null)
			orderid = "";	
		if ( !( orderid .equals("") ) && !( orderid .equals("undefined") ) ) 
			sql.append(" and ( orderid= '" + orderid + "'  )");
		
		String ch = StringUtil.getHttpParam(request(), "ch");
		if(ch==null)
			ch = "";	
		if ( !( ch .equals("") ) && !( ch .equals("undefined") ) ) 
			sql.append(" and ( ch= '" + ch + "'  )");
		
		String export = StringUtil.getHttpParam(request(), "export");// 是否导出
		if(export!=null && export.equals("1")){
			nStart = 0;
			nLimit = 65534;
		}
		
		Logger.info(sql.toString());
		//下面简单的分页，需要重构，应该由 Dao-Sevice去实现。
		int rowCount = Ebean.getServer(GlobalDBControl.getDB()).createQuery(SubOrders .class, sql.toString()).findRowCount();
		List<SubOrders> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.createQuery(SubOrders .class, sql.toString())
				.setFirstRow(nStart).setMaxRows(nLimit)
				.orderBy("idd desc")
				.findList();
				
		if(export!=null && export.equals("1")){
			String fileName = "SubOrders"+simp.format( new Date())+".xls";
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
		PagedObject<SubOrders> po= new PagedObject( ulist,nPages,rowCount );
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(po) );
	}

	//列表
	public static Result listAll(){
		List<SubOrders> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.find(SubOrders .class)
				.orderBy("idd")
				.findList();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(ulist) );
	}
	
	//删除
	public static Result delete(int idd){
		String sql = "delete from SubOrders where idd =:idd";
		Ebean.getServer(GlobalDBControl.getDB())
				.createUpdate(SubOrders .class, sql).setParameter("idd", idd).execute();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson("操作成功") );
	}
	
	//获取 单个
	public static Result get(int idd){
		SubOrders suborders = Ebean.getServer(GlobalDBControl.getDB())
				.find(SubOrders .class).where().eq("idd", idd).findUnique();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(suborders) );
	}

	//新增 / 修改
	public static Result modify(){
		String idd = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "idd");
		int nidd = StringTool.GetInt(idd, 0);
		
		
		
		
		
		String title = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "title");
		
		
		
		String smallimgurl = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "smallimgurl");
		
		
		
		String name = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "name");
		
		
		
		String area = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "area");
		
		
		
		String address = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "address");
		
		
		
		String phone = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "phone");
		
		
		
		String addtime = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "addtime");
		
		
		
		String orderid = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "orderid");
		
		
		
		String ch = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "ch");
		
		
		
		String operation = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "operation");
		
		SubOrders suborders ;
		if(  operation.equals("add") )	// 新增
			suborders = new SubOrders ();
		else			//修改
			suborders = Ebean.getServer(GlobalDBControl.getDB())
					.find(SubOrders .class).where().eq("idd", idd).findUnique();
		
		if( suborders!=null ){
			//赋值,字符串类型可以直接赋值，其他类型需要转换 ！！！！
			
				 
				
				
			
				
					suborders. setTitle ( title );
				 
				
				
			
				
					suborders. setSmallimgurl ( smallimgurl );
				 
				
				
			
				
					suborders. setName ( name );
				 
				
				
			
				
					suborders. setArea ( area );
				 
				
				
			
				
					suborders. setAddress ( address );
				 
				
				
			
				
					suborders. setPhone ( phone );
				 
				
				
			
				 
				
					suborders. setAddtime ( StringUtil.getDate(addtime) );
				
				
			
				
					suborders. setOrderid ( orderid );
				 
				
				
			
				
					suborders. setCh ( ch );
				 
				
				
			
			Ebean.getServer(GlobalDBControl.getDB()).save( suborders );
			response().setHeader("Cache-Control", "no-cache");
			return ok( Json.toJson(suborders) );
		}

		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(null) );
	}
	
	static int num=0;
	public static File exportList(List<SubOrders> infoList,String fileNameChine) throws UnsupportedEncodingException {
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
				
					"idd"
					
				
					
					,"商品标题"
				
					
					,"商品小图"
				
					
					,"收获人姓名"
				
					
					,"地区"
				
					
					,"详细地址"
				
					
					,"phone"
				
					
					,"addtime"
				
					
					,"orderid"
				
					
					,"ch"
				
			};

		// 生成表头
		helper.setRowIndex(rowIndex++);
		helper.generateHeader(sheet, titles, 0, StringUtils.EMPTY,
				StringUtils.EMPTY);

		// 循环生成数据行
		for (int i = 0; i < infoList.size(); i++) {
			colIndex = 0;
			SubOrders info = (SubOrders) infoList.get(i);
			Row row = sheet.createRow(rowIndex++);
			
			
				helper.createStringCell( row , colIndex++, ""+info. getIdd () );
			
				helper.createStringCell( row , colIndex++, ""+info. getTitle () );
			
				helper.createStringCell( row , colIndex++, ""+info. getSmallimgurl () );
			
				helper.createStringCell( row , colIndex++, ""+info. getName () );
			
				helper.createStringCell( row , colIndex++, ""+info. getArea () );
			
				helper.createStringCell( row , colIndex++, ""+info. getAddress () );
			
				helper.createStringCell( row , colIndex++, ""+info. getPhone () );
			
				helper.createStringCell( row , colIndex++, ""+info. getAddtime () );
			
				helper.createStringCell( row , colIndex++, ""+info. getOrderid () );
			
				helper.createStringCell( row , colIndex++, ""+info. getCh () );
			
		}
		//在application.conf中配置的路径
		String path = Configuration.root().getString("export.path");
		int numStra = num++;//静态值，区分文件名
		File file = new File(path);
		file.mkdir();//判断文件夹是否存在，不存在就创建
		
		FileOutputStream out = null;
		String fileName = path + "SubOrders" + System.currentTimeMillis() + numStra + ".xls";
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
				
					
					
				
					"商品标题"
					
				
					
					,"商品小图"
				
					
					,"收获人姓名"
				
					
					,"地区"
				
					
					,"详细地址"
				
					
					,"phone"
				
					
					,"addtime"
				
					
					,"orderid"
				
					
					,"ch"
				
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
			
			List<SubOrders> list = new ArrayList<SubOrders>();
			 for (int i = 1; i <= lastRowNumber; i++) {
				 //批量添加
                 Row row = sheet.getRow(i);
                 if (row == null) {
                     break;
                 }
                 /**获取文件中商品名称*/
          		 String productName = StringUtil.cellValueToString(row.getCell(0));//获取文件中的商品名称
          		 if(productName.trim().equals("")) {
          			 continue;
          		 }
                 /**首先校验订单ID和渠道ID是否一致,如果一直则直接跳过**/
                 String orderId = StringUtil.cellValueToString(row.getCell(4));
                 String ch = StringUtil.cellValueToString(row.getCell(5));
                 if(orderId.equals(ch)) {
                	 continue;
                 }
         		 SubOrders fc = new SubOrders();
         		 String title = StringUtil.cellValueToString(row.getCell(0));
         		 String imgUrl = "";
				 
				 
				 //判断title包含某个锁名称
				 if(title.toUpperCase().indexOf("M6")!=-1) {
					 imgUrl = SKN_M6.get(1);
					 title = SKN_M6.get(0);
				 }else if(title.toUpperCase().indexOf("N1")!=-1){
					 if(title.indexOf("金")!=-1){
						 imgUrl = SKN_N1.get(1);
					 }else{
						 imgUrl = SKN_N1.get(2);
					 }
					 
					 title = SKN_N1.get(0);
				 }else if(title.toUpperCase().indexOf("A7")!=-1){
					 imgUrl = SKN_A7.get(1);
					 title = SKN_A7.get(0);
				 }else {
					 imgUrl = "#";//默认没有图
				 }
				 fc. setTitle (title);//标题
				 fc. setSmallimgurl (imgUrl);//商品小图
				 fc. setName ( StringUtil.cellValueToString(row.getCell(1)) );
				 
				 String areatemp = StringUtil.cellValueToString(row.getCell(2));//excel地址
				 String address = areatemp.substring(areatemp.lastIndexOf(" "), areatemp.length());
				 
				 String area = areatemp.substring(0, areatemp.lastIndexOf(" "));
				 
				 fc. setArea (area);
				 fc. setAddress ( address );
				 fc. setPhone ( StringUtil.cellValueToString(row.getCell(3)));
				 fc. setAddtime ( StringUtil.getDate( simp.format(new Date())));
				 fc. setOrderid ( orderId );
				 fc. setCh ( ch );
				 fc. setIsinstall(1);
				 fc. setIsmeasure(1);
					
				
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
	// 设置表头
	static String[] titles = { 
				"商品标题"
//			    ,"商品小图"
				,"收获人姓名"
				,"详细地址"
				,"手机号"
//				,"添加时间"
				,"订单ID"
				,"渠道ID"
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
        
        ExcelGenerateHelper helper = ExcelGenerateHelper.getInstance(wb);
        
//        // 设置第一列的1-65534行为下拉列表
//        CellRangeAddressList regions = new CellRangeAddressList(1, 65534, 0, 0);//创建列对象 参数:起始行号，终止行号， 起始列号，终止列号
//        // 创建下拉列表数据
//        DVConstraint constraint = DVConstraint.createExplicitListConstraint(dlData);//创建一个下拉列表对象 包含数据
//        // 绑定
//        HSSFDataValidation dataValidation = new HSSFDataValidation(regions, constraint);//将列表对象和列对象绑定
//        sheet1.addValidationData(dataValidation);//添加到Excel中
//        
//        
//        
//        //生产厂家列表
//        List<IntelligentLockManufacturer> mList = Ebean.getServer(GlobalDBControl.getDB()).createQuery(IntelligentLockManufacturer.class).findList();
//        String[] mData = new String[mList.size()];
//        for (int i = 0; i < mList.size(); i++) {
//        	mData[i] = mList.get(i).getName();
//		}
//        // 设置第6列的1-65534行为下拉列表
//        CellRangeAddressList regions6 = new CellRangeAddressList(1, 65534, 6, 6);//创建列对象 参数:起始行号，终止行号， 起始列号，终止列号
//        // 创建下拉列表数据
//        DVConstraint constraint6 = DVConstraint.createExplicitListConstraint(mData);//创建一个下拉列表对象 包含数据
//        // 绑定
//        HSSFDataValidation dataValidation6 = new HSSFDataValidation(regions6, constraint6);//将列表对象和列对象绑定
//        sheet1.addValidationData(dataValidation6);//添加到Excel中
//        
//        //入库类型
//        String[] tData = {
//        		"正常入库",
//        		"样品入库",
//        		"退货入库"
//        		};
//        // 设置第7列的1-65534行为下拉列表
//        CellRangeAddressList regions7 = new CellRangeAddressList(1, 65534, 7, 7);//创建列对象 参数:起始行号，终止行号， 起始列号，终止列号
//        // 创建下拉列表数据
//        DVConstraint constraint7 = DVConstraint.createExplicitListConstraint(tData);//创建一个下拉列表对象 包含数据
//        // 绑定
//        HSSFDataValidation dataValidation7 = new HSSFDataValidation(regions7, constraint7);//将列表对象和列对象绑定
//        sheet1.addValidationData(dataValidation7);//添加到Excel中
//        
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
	
}