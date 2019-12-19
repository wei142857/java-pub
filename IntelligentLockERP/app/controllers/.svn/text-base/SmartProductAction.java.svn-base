
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

import models.IntelligentLockArea;
import models.ReturnMessage;
import models.SmartProduct;

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
import util.ImageUploadUtil;
import util.PagedObject;
import util.StringUtil;
import util.classEntity.StringTool;
import views.html.page.SmartProductList;

import com.avaje.ebean.Ebean;

@Security.Authenticated(Secured.class)
public class SmartProductAction extends Controller {
  
	//进入页面列表；
	public static Result view() {
		return ok( SmartProductList.render() );
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
		sql.append("find SmartProduct where 1=1 ");
		
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
		
		String yys = StringUtil.getHttpParam(request(), "yys");
		if(yys==null)
			yys = "";	
		if ( !( yys .equals("") ) && !( yys .equals("undefined") ) ) 
			sql.append(" and ( yys= '" + yys + "'  )");
		
		String area = StringUtil.getHttpParam(request(), "area");
		if(area==null)
			area = "";	
		if ( !( area .equals("") ) && !( area .equals("undefined") ) ) 
			sql.append(" and ( area= '" + area + "'  )");
		
		String price = StringUtil.getHttpParam(request(), "price");
		if(price==null)
			price = "";	
		if ( !( price .equals("") ) && !( price .equals("undefined") ) ) 
			sql.append(" and ( price= '" + price + "'  )");
		
		String cost = StringUtil.getHttpParam(request(), "cost");
		if(cost==null)
			cost = "";	
		if ( !( cost .equals("") ) && !( cost .equals("undefined") ) ) 
			sql.append(" and ( cost= '" + cost + "'  )");
		
		String bvalue = StringUtil.getHttpParam(request(), "bvalue");
		if(bvalue==null)
			bvalue = "";	
		if ( !( bvalue .equals("") ) && !( bvalue .equals("undefined") ) ) 
			sql.append(" and ( bvalue= '" + bvalue + "'  )");
		
		String status = StringUtil.getHttpParam(request(), "status");
		if(status==null)
			status = "";	
		if ( !( status .equals("") ) && !( status .equals("1") ) && !( status .equals("undefined") ) ) 
			sql.append(" and ( status= '" + status + "'  )");
		
		String remark = StringUtil.getHttpParam(request(), "remark");
		if(remark==null)
			remark = "";	
		if ( !( remark .equals("") ) && !( remark .equals("undefined") ) ) 
			sql.append(" and ( remark= '" + remark + "'  )");
		
		String icon = StringUtil.getHttpParam(request(), "icon");
		if(icon==null)
			icon = "";	
		if ( !( icon .equals("") ) && !( icon .equals("undefined") ) ) 
			sql.append(" and ( icon= '" + icon + "'  )");
		
		/*String addtime = StringUtil.getHttpParam(request(), "addtime");
		if(addtime==null)
			addtime = "";	
		if ( !( addtime .equals("") ) && !( addtime .equals("undefined") ) ) 
			sql.append(" and ( addtime= '" + addtime + "'  )");*/
		
		String orders = StringUtil.getHttpParam(request(), "orders");
		if(orders==null)
			orders = "";	
		if ( !( orders .equals("") ) && !( orders .equals("undefined") ) ) 
			sql.append(" and ( orders= '" + orders + "'  )");
		
		String export = StringUtil.getHttpParam(request(), "export");// 是否导出
		if(export!=null && export.equals("1")){
			nStart = 0;
			nLimit = 65534;
		}
		
		Logger.info(sql.toString());
		//下面简单的分页，需要重构，应该由 Dao-Sevice去实现。
		int rowCount = Ebean.getServer(GlobalDBControl.getDB()).createQuery(SmartProduct .class, sql.toString()).findRowCount();
		List<SmartProduct> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.createQuery(SmartProduct .class, sql.toString())
				.setFirstRow(nStart).setMaxRows(nLimit)
				.orderBy("idd desc")
				.findList();
				
		if(export!=null && export.equals("1")){
			String fileName = "SmartProduct"+simp.format( new Date())+".xls";
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
		PagedObject<SmartProduct> po= new PagedObject( ulist,nPages,rowCount );
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(po) );
	}

	//列表
	public static Result listAll(){
		List<SmartProduct> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.find(SmartProduct .class)
				.orderBy("idd")
				.findList();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(ulist) );
	}
	
	//删除
	public static Result delete(int idd){
		String sql = "delete from SmartProduct where idd =:idd";
		Ebean.getServer(GlobalDBControl.getDB())
				.createUpdate(SmartProduct .class, sql).setParameter("idd", idd).execute();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson("操作成功") );
	}
	
	//获取 单个
	public static Result get(int idd){
		SmartProduct smartproduct = Ebean.getServer(GlobalDBControl.getDB())
				.find(SmartProduct .class).where().eq("idd", idd).findUnique();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(smartproduct) );
	}
	
	//获取 地区
	public static Result getArea(){
		List<IntelligentLockArea> ul = Ebean.getServer(GlobalDBControl.getDB())
				.find(IntelligentLockArea .class)
				.orderBy("idd")
				.findList();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(ul) );
	}
	
	//上传图标uploadIcon
	public static Result uploadIcon(){
		ReturnMessage returnMessage = new ReturnMessage();
		MultipartFormData uploadImage = request().body().asMultipartFormData();
		if(uploadImage!=null){
			List<FilePart> fileList = uploadImage.getFiles();
			for(FilePart file:fileList){
				if(file!=null){
					String imagePath = ImageUploadUtil.putImg(file, "IPI");
					returnMessage.setCode("1");
					returnMessage.setMessage("上传成功");
					return ok(imagePath);
				}
			}
		}
			returnMessage.setCode("0");
			returnMessage.setMessage("上传的图标无效(null)");
			return ok(Json.toJson(returnMessage));
	}

	//新增 / 修改
	public static Result modify(){
		String idd = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "idd");
		int nidd = StringTool.GetInt(idd, 0);
		
		String title = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "title");
		
		String yys = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "yys");
		
		String area = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "area");
		
		String price = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "price");
		
		String cost = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "cost");
		
		String bvalue = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "bvalue");
		
		String status = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "status");
		
		String remark = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "remark");
		
		String icon = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "icon");
		
		String orders = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "orders");
		
		String operation = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "operation");
		
		SmartProduct smartproduct ;
		
		if(operation.equals("add")){	// 新增
			smartproduct = new SmartProduct ();
		}else	//修改
			smartproduct = Ebean.getServer(GlobalDBControl.getDB())
					.find(SmartProduct .class).where().eq("idd", idd).findUnique();
		
		if( smartproduct!=null ){
			//赋值,字符串类型可以直接赋值，其他类型需要转换 ！！！！
				
					smartproduct. setTitle ( title );
				
					smartproduct. setYys ( yys );
				
					smartproduct. setArea ( area );
				 
					smartproduct. setPrice ( StringTool.GetInt(price,0) );
					
					smartproduct. setCost ( StringTool.GetDouble(cost,0.0) );
				
					smartproduct. setBvalue ( StringTool.GetInt(bvalue,0) );
				
					smartproduct. setStatus ( StringTool.GetInt(status,-1) );
				
					smartproduct. setRemark ( remark );
				
					smartproduct. setIcon ( icon );
					
					if(operation.equals("add")){
						smartproduct. setAddtime(new Date());
					}
					
					smartproduct. setOrders ( StringTool.GetInt(orders,0) );
			
			Ebean.getServer(GlobalDBControl.getDB()).save( smartproduct );
			response().setHeader("Cache-Control", "no-cache");
			return ok( Json.toJson(smartproduct) );
		}

		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(null) );
	}
	
	static int num=0;
	public static File exportList(List<SmartProduct> infoList,String fileNameChine) throws UnsupportedEncodingException {
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
		
		ExcelGenerateHelper helper = ExcelGenerateHelper.getInstance(wb);

		// 获取行索引
		int rowIndex = 0;
		int colIndex = 0;

		// 设置表头
		String[] titles = { 
				
				"ID"
				
				,"标题"
				
				,"运营商"
				
				,"使用区域"
				
				,"话费价值"
			
				,"成本价"
				
				,"积分"
				
				,"状态"
				
				,"备注"
				
				,"图标"
				
				,"添加时间"
				
				,"排序值"
		};

		// 生成表头
		helper.setRowIndex(rowIndex++);
		helper.generateHeader(sheet, titles, 0, StringUtils.EMPTY,
				StringUtils.EMPTY);

		// 循环生成数据行
		for (int i = 0; i < infoList.size(); i++) {
			colIndex = 0;
			SmartProduct info = (SmartProduct) infoList.get(i);
			Row row = sheet.createRow(rowIndex++);
			
				helper.createStringCell( row , colIndex++, ""+info. getIdd () );
			
				helper.createStringCell( row , colIndex++, ""+info. getTitle () );
			
				helper.createStringCell( row , colIndex++, ""+info. getYys () );
			
				helper.createStringCell( row , colIndex++, ""+info. getArea () );
			
				helper.createStringCell( row , colIndex++, ""+info. getPrice () );
				
				helper.createStringCell( row , colIndex++, ""+info. getCost() );
			
				helper.createStringCell( row , colIndex++, ""+info. getBvalue () );
			
				helper.createStringCell( row , colIndex++, ""+info. getStatus () );
			
				helper.createStringCell( row , colIndex++, ""+info. getRemark () );
			
				helper.createStringCell( row , colIndex++, ""+info. getIcon () );
			
				helper.createStringCell( row , colIndex++, ""+info. getAddtime () );
			
				helper.createStringCell( row , colIndex++, ""+info. getOrders () );
			
		}
		//在application.conf中配置的路径
		String path = Configuration.root().getString("export.path");
		int numStra = num++;//静态值，区分文件名
		File file = new File(path);
		file.mkdir();//判断文件夹是否存在，不存在就创建
		
		FileOutputStream out = null;
		String fileName = path + "SmartProduct" + System.currentTimeMillis() + numStra + ".xls";
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
				
		"标题"
		
		,"运营商"
	
		,"使用区域"
		
		,"话费价值"
	
		,"成本价"
		
		,"积分"
	
		,"状态"
		
		,"备注"
		
		,"图标"
	
		,"添加时间"
		
		,"排序值"
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
			
			List<SmartProduct> list = new ArrayList<SmartProduct>();
			 for (int i = 1; i <= lastRowNumber; i++) {
				 
				 //批量添加
                 Row row = sheet.getRow(i);
                 if (row == null) {
                     break;
                 }
                 
                 SmartProduct fc = new SmartProduct();
                 
                 int j = 0 ;
				 fc. setTitle ( StringUtil.cellValueToString(row.getCell(j)) );
			
				 j=j+1;
				 fc. setYys ( StringUtil.cellValueToString(row.getCell(j)) );
			
				 j=j+1;
				 fc. setArea ( StringUtil.cellValueToString(row.getCell(j)) );
			 
				 j=j+1;
				 fc. setPrice ( StringTool.GetInt(StringUtil.cellValueToString(row.getCell(j)),0) );
				 
				 j=j+1;
				 fc. setCost ( StringTool.GetDouble(StringUtil.cellValueToString(row.getCell(j)),0.0) );
			
				 j=j+1;
				 fc. setBvalue ( StringTool.GetInt(StringUtil.cellValueToString(row.getCell(j)),0) );
			
				 j=j+1;
				 fc. setStatus ( StringTool.GetInt(StringUtil.cellValueToString(row.getCell(j)),0) );
			
				 j=j+1;
				 fc. setRemark ( StringUtil.cellValueToString(row.getCell(j)) );
			 
				 j=j+1;
				 fc. setIcon ( StringUtil.cellValueToString(row.getCell(j)) );
			
				 j=j+1;
				 //fc. setAddtime ( StringUtil.getDate( StringUtil.cellValueToString(row.getCell(j) ) );
			
				 j=j+1;
				 fc. setOrders ( StringTool.GetInt(StringUtil.cellValueToString(row.getCell(j)),0) );
				 
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