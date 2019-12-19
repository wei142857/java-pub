
package controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import models.*;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Page;
import com.avaje.ebean.SqlRow;

import java.text.SimpleDateFormat;
import java.io.*;

import util.*;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddressList;

import play.libs.Json;
import play.mvc.*;
import play.*;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import util.AjaxHellper;
import util.GlobalDBControl;
import util.GlobalSetting;
import util.ImageGenerator;
import util.ImageUploadUtil;
import util.PagedObject;
import util.StringUtil;
import play.Logger;
import views.html.page.*;
import util.classEntity.*;

@Security.Authenticated(Secured.class)
public class EShopProductAction extends Controller {
  
	//进入页面列表；
	public static Result view() {
		return ok( EShopProductList.render() );
	}
	
	/**************************************************************	
	 *                增删改查的功能   ： AJAX方式
	 */	
	 private static SimpleDateFormat simp=new SimpleDateFormat("yyyy-MM-dd");
	 private static SimpleDateFormat simp1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	//列表
	public static Result list(){
		//获取前端grid 传递过来的参数：分页，搜索
		String start = StringUtil.getHttpParam(request(), "start");
		String limit = StringUtil.getHttpParam(request(), "limit");
		int nStart = StringTool.GetInt(start, 0);
		int nLimit = StringTool.GetInt(limit, 10);

		StringBuffer sql = new StringBuffer();
		sql.append("find EShopProduct where 1=1 ");
		
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
		
		String subtitle = StringUtil.getHttpParam(request(), "subtitle");
		if(subtitle==null)
			subtitle = "";	
		if ( !( subtitle .equals("") ) && !( subtitle .equals("undefined") ) ) 
			sql.append(" and ( subtitle= '" + subtitle + "'  )");
		
		String description = StringUtil.getHttpParam(request(), "description");
		if(description==null)
			description = "";	
		if ( !( description .equals("") ) && !( description .equals("undefined") ) ) 
			sql.append(" and ( description= '" + description + "'  )");
		
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
		
		String bigimgurl = StringUtil.getHttpParam(request(), "bigimgurl");
		if(bigimgurl==null)
			bigimgurl = "";	
		if ( !( bigimgurl .equals("") ) && !( bigimgurl .equals("undefined") ) ) 
			sql.append(" and ( bigimgurl= '" + bigimgurl + "'  )");
		
		String smallimgurl = StringUtil.getHttpParam(request(), "smallimgurl");
		if(smallimgurl==null)
			smallimgurl = "";	
		if ( !( smallimgurl .equals("") ) && !( smallimgurl .equals("undefined") ) ) 
			sql.append(" and ( smallimgurl= '" + smallimgurl + "'  )");
		
		String status = StringUtil.getHttpParam(request(), "status");
		if(status==null)
			status = "";	
		if ( !( status .equals("") ) && !( status .equals("undefined") )&& !( status .equals("-2") ) ) 
			sql.append(" and ( status= '" + status + "'  )");
		
		String sharetitle = StringUtil.getHttpParam(request(), "sharetitle");
		if(sharetitle==null)
			sharetitle = "";	
		if ( !( sharetitle .equals("") ) && !( sharetitle .equals("undefined") ) ) 
			sql.append(" and ( sharetitle= '" + sharetitle + "'  )");
		
		String sharedesc = StringUtil.getHttpParam(request(), "sharedesc");
		if(sharedesc==null)
			sharedesc = "";	
		if ( !( sharedesc .equals("") ) && !( sharedesc .equals("undefined") ) ) 
			sql.append(" and ( sharedesc= '" + sharedesc + "'  )");
		
		String shareicon = StringUtil.getHttpParam(request(), "shareicon");
		if(shareicon==null)
			shareicon = "";	
		if ( !( shareicon .equals("") ) && !( shareicon .equals("undefined") ) ) 
			sql.append(" and ( shareicon= '" + shareicon + "'  )");
		
		String sharelink = StringUtil.getHttpParam(request(), "sharelink");
		if(sharelink==null)
			sharelink = "";	
		if ( !( sharelink .equals("") ) && !( sharelink .equals("undefined") ) ) 
			sql.append(" and ( sharelink= '" + sharelink + "'  )");
		
		String saleprice = StringUtil.getHttpParam(request(), "saleprice");
		if(saleprice==null)
			saleprice = "";	
		if ( !( saleprice .equals("") ) && !( saleprice .equals("undefined") ) ) 
			sql.append(" and ( saleprice= '" + saleprice + "'  )");
		
		String vipprice = StringUtil.getHttpParam(request(), "vipprice");
		if(vipprice==null)
			vipprice = "";	
		if ( !( vipprice .equals("") ) && !( vipprice .equals("undefined") ) ) 
			sql.append(" and ( vipprice= '" + vipprice + "'  )");
		
		String pcsaleprice = StringUtil.getHttpParam(request(), "pcsaleprice");
		if(pcsaleprice==null)
			pcsaleprice = "";	
		if ( !( pcsaleprice .equals("") ) && !( pcsaleprice .equals("undefined") ) ) 
			sql.append(" and ( pcsaleprice= '" + pcsaleprice + "'  )");
		
		String stock = StringUtil.getHttpParam(request(), "stock");
		if(stock==null)
			stock = "";	
		if ( !( stock .equals("") ) && !( stock .equals("undefined") ) ) 
			sql.append(" and ( stock= '" + stock + "'  )");
		
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
		int rowCount = Ebean.getServer(GlobalDBControl.getDB()).createQuery(EShopProduct .class, sql.toString()).findRowCount();
		List<EShopProduct> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.createQuery(EShopProduct .class, sql.toString())
				.setFirstRow(nStart).setMaxRows(nLimit)
				.orderBy("orders desc")
				.findList();
				
		if(export!=null && export.equals("1")){
			String fileName = "EShopProduct"+simp.format( new Date())+".xls";
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
		PagedObject<EShopProduct> po= new PagedObject( ulist,nPages,rowCount );
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(po) );
	}

	//列表
	public static Result listAll(){
		List<EShopProduct> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.find(EShopProduct .class)
				.orderBy("idd")
				.findList();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(ulist) );
	}
	
	//删除
	public static Result delete(int idd){
		String sql = "delete from EShopProduct where idd =:idd";
		Ebean.getServer(GlobalDBControl.getDB())
				.createUpdate(EShopProduct .class, sql).setParameter("idd", idd).execute();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson("操作成功") );
	}
	
	//获取 单个
	public static Result get(int idd){
		EShopProduct eshopproduct = Ebean.getServer(GlobalDBControl.getDB())
				.find(EShopProduct .class).where().eq("idd", idd).findUnique();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(eshopproduct) );
	}

	//新增 / 修改
	public static Result modify(){
		ReturnMessage returnMessage = new ReturnMessage();
		String idd = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "idd");
		int nidd = StringTool.GetInt(idd, 0);
		
		String title = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "title");
		
		String subtitle = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "subtitle");
		
		String description = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "description");
		
		String remark = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "remark");
		
		String bigimgurl = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "bigimgurl");
		
		String smallimgurl = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "smallimgurl");
		
		String status = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "status");
		
		String sharetitle = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "sharetitle");
		
		String sharedesc = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "sharedesc");
		
		String shareicon = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "shareicon");
		
		String sharelink = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "sharelink");
		
		String saleprice = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "saleprice");
		
		String vipprice = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "vipprice");
		
		String pcsaleprice = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "pcsaleprice");
		
		String stock = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "stock");
		
		String orders = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "orders");
		
		String operation = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "operation");
		
		EShopProduct eshopproduct ;
		if(  operation.equals("add") )	// 新增
			eshopproduct = new EShopProduct ();
		else			//修改
			eshopproduct = Ebean.getServer(GlobalDBControl.getDB())
					.find(EShopProduct .class).where().eq("idd", idd).findUnique();
		if( eshopproduct!=null ){
			//赋值,字符串类型可以直接赋值，其他类型需要转换 ！！！！
			eshopproduct. setTitle ( title );
			eshopproduct. setSubtitle ( subtitle );
			eshopproduct. setDescription ( description );
			eshopproduct. setRemark ( remark );
			if(operation.equals("add")){
				eshopproduct.setAddtime(new Date());
			}
			eshopproduct. setBigimgurl ( bigimgurl );
			eshopproduct. setSmallimgurl ( smallimgurl );
			eshopproduct. setSaleprice( StringTool.GetDouble(saleprice, 0.0) );
			eshopproduct. setVipprice( StringTool.GetDouble(vipprice, 0.0));
			eshopproduct. setPcsaleprice( StringTool.GetDouble(pcsaleprice, 0.0) );
			eshopproduct. setStatus ( StringTool.GetInt(status,0) );
			eshopproduct. setSharetitle ( sharetitle );
			eshopproduct. setSharedesc ( sharedesc );
			eshopproduct. setShareicon ( shareicon );
			eshopproduct. setSharelink ( sharelink );
			eshopproduct. setStock ( StringTool.GetInt(stock,0) );
			eshopproduct. setOrders ( StringTool.GetInt(orders,0) );
			Ebean.getServer(GlobalDBControl.getDB()).save( eshopproduct );
			response().setHeader("Cache-Control", "no-cache");
			returnMessage.setCode("0");
			return ok( Json.toJson(returnMessage) );
		}
		response().setHeader("Cache-Control", "no-cache");
		returnMessage.setMessage("数据出错");
		return ok( Json.toJson(returnMessage) );
	}
	
	//上传图标
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
	
	//设置第一行的表头
	static String[] rowTitles = {
		"商品名称"
		,"副标题"
		,"商品描述"
		,"备注"
		,"大图"
		,"小图"
		,"状态"
		,"分享标题"
		,"分享描述"
		,"分享图标"
		,"分享链接"
		,"售价"
		,"会员价"
		,"成本价"
		,"库存"
		,"排序值"
	};
	//设置状态值的下拉选内容
	static String[] status = {
		"上线"
		,"下线"
	};
	//下载 商城产品信息Excel模板的方法
	public static Result downSample() throws UnsupportedEncodingException{
		//创建文件名称
		String fileNameChine = "商城产品信息Excel表"+simp.format(new Date())+".xls";
		Logger.info("fileNameChine:" + fileNameChine);
		/*
		 * 创建Excel工作薄对象:
		 * 由于HSSFWorkbook只能操作excel2003以下版本,
		 * XSSFWorkbook只能操作excel2007以上版本，
		 * 所以利用Workbook接口创建对应的对象操作excel
		 * 来处理兼容性.
		 */
		Workbook workbook = new HSSFWorkbook();
		//生成一个表格 设置：页签
		Sheet sheet = workbook.createSheet("sheet1");
		
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
        //遍历数组rowTitles 设置表头内容
        for(int i=0;i<rowTitles.length;i++){
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
            cell.setCellValue(rowTitles[i]);
            //给内容添加样式
            cell.setCellStyle(style);
        }
        // 设置第一列的3-65534行为下拉列表(状态值)
        // (1, 65534, 6, 6) ====> (起始行,结束行,起始列,结束列)
        CellRangeAddressList statusReg = new CellRangeAddressList(1, 65534, 6, 6);
        // 创建下拉列表数据
        DVConstraint statusConstraint = DVConstraint.createExplicitListConstraint(status);
        // 将设置下拉选的位置和数据的对应关系 绑定到一起
        DataValidation statusVal = new HSSFDataValidation(statusReg, statusConstraint);
        sheet.addValidationData(statusVal);
    
        //在application.conf中配置的路径
		String path = Configuration.root().getString("export.path");
		int numStra = num++;//静态值，区分文件名
		File file = new File(path);
		file.mkdir();//判断文件夹是否存在，不存在就创建		
        
		FileOutputStream out = null;
		//创建备份的Excel模板文件名
		String fileName = path + "商城产品信息Excel表" + System.currentTimeMillis() + numStra + ".xls";
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
	public static File exportList(List<EShopProduct> infoList,String fileNameChine) throws UnsupportedEncodingException {
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
		
			sheet.setColumnWidth( 14 , 6000 );
		
			sheet.setColumnWidth( 15 , 6000 );
		
			sheet.setColumnWidth( 16 , 6000 );
		
			sheet.setColumnWidth( 17 , 6000 );
		
		ExcelGenerateHelper helper = ExcelGenerateHelper.getInstance(wb);

		// 获取行索引
		int rowIndex = 0;
		int colIndex = 0;

		// 设置表头
		String[] titles = { 
			"IDD"
			,"商品名称"
			,"副标题"
			,"商品描述"
			,"备注"
			,"添加时间"
			,"大图"
			,"小图"
			,"状态"
			,"分享标题"
			,"分享描述"
			,"分享图标"
			,"分享链接"
			,"售价"
			,"会员价"
			,"成本价"
			,"库存"
			,"排序值"
		};

		// 生成表头
		helper.setRowIndex(rowIndex++);
		helper.generateHeader(sheet, titles, 0, StringUtils.EMPTY,
				StringUtils.EMPTY);

		// 循环生成数据行
		for (int i = 0; i < infoList.size(); i++) {
			colIndex = 0;
			EShopProduct info = (EShopProduct) infoList.get(i);
			Row row = sheet.createRow(rowIndex++);
			
			helper.createStringCell( row , colIndex++, ""+info. getIdd () );
		
			helper.createStringCell( row , colIndex++, ""+info. getTitle () );
		
			helper.createStringCell( row , colIndex++, ""+info. getSubtitle () );
		
			helper.createStringCell( row , colIndex++, ""+info. getDescription () );
		
			helper.createStringCell( row , colIndex++, ""+info. getRemark () );
		
			helper.createStringCell( row , colIndex++, ""+simp1.format(info. getAddtime ()) );
		
			helper.createStringCell( row , colIndex++, ""+info. getBigimgurl () );
		
			helper.createStringCell( row , colIndex++, ""+info. getSmallimgurl () );
		
			helper.createStringCell( row , colIndex++, ""+info. getStatus () );
		
			helper.createStringCell( row , colIndex++, ""+info. getSharetitle () );
		
			helper.createStringCell( row , colIndex++, ""+info. getSharedesc () );
		
			helper.createStringCell( row , colIndex++, ""+info. getShareicon () );
		
			helper.createStringCell( row , colIndex++, ""+info. getSharelink () );
		
			helper.createStringCell( row , colIndex++, ""+info. getSaleprice () );
		
			helper.createStringCell( row , colIndex++, ""+info. getVipprice () );
		
			helper.createStringCell( row , colIndex++, ""+info. getPcsaleprice () );
		
			helper.createStringCell( row , colIndex++, ""+info. getStock () );
		
			helper.createStringCell( row , colIndex++, ""+info. getOrders () );
		}
		//在application.conf中配置的路径
		String path = Configuration.root().getString("export.path");
		int numStra = num++;//静态值，区分文件名
		File file = new File(path);
		file.mkdir();//判断文件夹是否存在，不存在就创建
		
		FileOutputStream out = null;
		String fileName = path + "商城产品信息表" + System.currentTimeMillis() + numStra + ".xls";
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
		"商品名称"
		,"副标题"
		,"商品描述"
		,"备注"
		,"大图"
		,"小图"
		,"状态"
		,"分享标题"
		,"分享描述"
		,"分享图标"
		,"分享链接"
		,"售价"
		,"会员价"
		,"成本价"
		,"库存"
		,"排序值"
	};
	static int MAX_LINES = 10000;
	public static Result upload(){
		String cid = AjaxHellper.getHttpParam(request(), "uploadidd");
		Integer ncid;
		int successNum = 0; //导入成功的数量
		ReturnMessage returnMessage = new ReturnMessage();
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
				returnMessage.setCode("");
				returnMessage.setMessage("创建文件流时发生错误,请重试");
				util.LogUtil.writeLog("错误信息:"+returnMessage.getMessage(), "EShopLog");
				return ok(Json.toJson(returnMessage));
			}

			try {
				workBook = WorkbookFactory.create(fis);
			} catch (Exception e) {
				returnMessage.setMessage("创建WorkBook对象时发生错误,请重试");
				util.LogUtil.writeLog("错误信息:"+returnMessage.getMessage(), "EShopLog");
				Logger.info(e.toString());
				return ok(Json.toJson(returnMessage));
			} 
			
			Sheet sheet = workBook.getSheetAt(0);
			int lastRowNumber = sheet.getLastRowNum();
			Row rowTitle = sheet.getRow(0);
			if(checkNullOrNot(rowTitle)){
				returnMessage.setMessage("导入的第一行为表头,不能为空行");//返回true说明为空
				util.LogUtil.writeLog("错误信息:"+returnMessage.getMessage(), "EShopLog");
				return ok(Json.toJson(returnMessage));
			}
			// 表头
			for(int i=0;i < headers.length;i++){
				String cellValue = StringUtil.cellValueToString(rowTitle.getCell(i));
				if(!cellValue.equalsIgnoreCase(headers[i])){
					returnMessage.setMessage("导入的Excel表,表头不匹配,请下载标准格式后重新上传");
					util.LogUtil.writeLog("错误信息:"+returnMessage.getMessage(), "EShopLog");
					return ok(Json.toJson(returnMessage));
				}
			}
			for (int i = 1; i <= lastRowNumber; i++) {
				Row row = sheet.getRow(i);
				if (checkNullOrNot(row)) {
					break;
				}
				rowNum++;
			}
			
			if( rowNum > MAX_LINES ){
				returnMessage.setMessage("导入的数据过多,请分批上传");
				util.LogUtil.writeLog("错误信息:"+returnMessage.getMessage(), "EShopLog");
				return ok(Json.toJson(returnMessage));
			}
			List<EShopProduct> list = new ArrayList<EShopProduct>();
			for (int i = 1; i <= lastRowNumber; i++) {
				//批量添加
                Row row = sheet.getRow(i);
                if (row == null) {
                    break;
                }
                 
                EShopProduct fc = new EShopProduct();
                 
                int j = 0 ;
				fc. setTitle ( StringUtil.cellValueToString(row.getCell(j)) );
			
				j=j+1;
				fc. setSubtitle ( StringUtil.cellValueToString(row.getCell(j)) );
			
				j=j+1;
				fc. setDescription ( StringUtil.cellValueToString(row.getCell(j)) );
			
				j=j+1;
				fc. setRemark ( StringUtil.cellValueToString(row.getCell(j)) );
			
				j=j+1;
				fc. setBigimgurl ( StringUtil.cellValueToString(row.getCell(j)) );
			
				j=j+1;
				fc. setSmallimgurl ( StringUtil.cellValueToString(row.getCell(j)) );
			
				j=j+1;
				String status = StringUtil.cellValueToString(row.getCell(j));
				if(status.equals("上线")){
					fc. setStatus ( 0 );
				}else{
					fc. setStatus ( 1 );
				}
			
				j=j+1;
				fc. setSharetitle ( StringUtil.cellValueToString(row.getCell(j)) );
			
				j=j+1;
				fc. setSharedesc ( StringUtil.cellValueToString(row.getCell(j)) );
			
				j=j+1;
				fc. setShareicon ( StringUtil.cellValueToString(row.getCell(j)) );
			
				j=j+1;
				fc. setSharelink ( StringUtil.cellValueToString(row.getCell(j)) );
			
				j=j+1;
				fc. setSaleprice( StringTool.GetDouble(StringUtil.cellValueToString(row.getCell(j)), 0.0) );
			
				j=j+1;
				fc. setVipprice( StringTool.GetDouble(StringUtil.cellValueToString(row.getCell(j)), 0.0) );
				
				j=j+1;
				fc. setPcsaleprice( StringTool.GetDouble(StringUtil.cellValueToString(row.getCell(j)), 0.0) );
			
				j=j+1;
				fc. setStock ( StringTool.GetInt(StringUtil.cellValueToString(row.getCell(j)),0) );
			
				j=j+1;
				fc. setOrders ( StringTool.GetInt(StringUtil.cellValueToString(row.getCell(j)),0) );
				
				fc.setAddtime(new Date());
						
                 list.add(fc);
                 if( list.size()>90 ){
                	 Ebean.getServer(GlobalDBControl.getDB()).save(list);
                	 successNum += list.size();
                	 list.clear();
                 }
			 }
			 if( list.size()>0 ){
				 Ebean.getServer(GlobalDBControl.getDB()).save(list);
				 successNum += list.size();
				 list.clear();
			 }
			 returnMessage.setCode("0");
			 returnMessage.setMessage(""+successNum);
			 util.LogUtil.writeLog("成功导入商城商品信息:"+successNum+"条", "EShopLog");
			 return ok(Json.toJson(returnMessage));
		}
		returnMessage.setMessage("导入的商城商品信息表为空,请填写商城商品信息后再进行上传");
		util.LogUtil.writeLog("错误信息:"+returnMessage.getMessage(), "EShopLog");
		return ok(Json.toJson(returnMessage));
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
	
	public static Result uploadImg() {
		MultipartFormData body = request().body().asMultipartFormData();
		if(body!=null){
			List<FilePart> file_img = body.getFiles();
			if( file_img.size()>0 ){
				FilePart filePart = file_img.get(0);
				String path = ImageUploadUtil.putImg(filePart, "eshop");
				return ok(path);
			}
		}
		return ok("");
	}
}