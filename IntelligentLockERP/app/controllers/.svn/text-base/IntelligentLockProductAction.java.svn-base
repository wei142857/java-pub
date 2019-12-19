
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
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.avaje.ebean.Ebean;

import models.IntelligentLockProduct;
import models.ResponseError;
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
import util.FileUploadUtil;
import util.GlobalDBControl;
import util.ImageUploadUtil;
import util.LogUtil;
import util.PagedObject;
import util.StringUtil;
import util.classEntity.StringTool;
import views.html.page.IntelligentLockProductList;

@Security.Authenticated(Secured.class)
public class IntelligentLockProductAction extends Controller {
  
	//进入页面列表；
	public static Result view() {
		return ok( IntelligentLockProductList.render() );
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
		sql.append("find IntelligentLockProduct where 1=1 ");
		
		String idd = StringUtil.getHttpParam(request(), "idd");
		if(idd==null)
			idd = "";	
		if ( !( idd .equals("") ) && !( idd .equals("undefined") ) ) 
			sql.append(" and ( idd= '" + idd + "'  )");
		
		String image = StringUtil.getHttpParam(request(), "image");
		
//		if(image==null)
//			image = "";	
//		if ( !( image .equals("") ) && !( image .equals("undefined") ) ) 
//			sql.append(" and ( image= '" + image + "'  )");
		
		String title = StringUtil.getHttpParam(request(), "title");
		if(title==null)
			title = "";	
		if ( !( title .equals("") ) && !( title .equals("undefined") ) ) 
			sql.append(" and ( title like '%" + title + "%'  )");
		
		String producttype = StringUtil.getHttpParam(request(), "producttype");
		if(producttype==null)
			producttype = "";	
		if(producttype .equals("add")){
			sql.append(" and ( producttype =''  )");
		}else if ( !( producttype .equals("") ) && !( producttype .equals("undefined") ) ) {
			sql.append(" and ( producttype like '%" + producttype + "%'  )");
		}
		
		String model = StringUtil.getHttpParam(request(), "model");
		if(model==null)
			model = "";	
		if ( !( model .equals("") ) && !( model .equals("undefined") ) ) 
			sql.append(" and ( model like '%" + model + "%'  )");
		
		String spec = StringUtil.getHttpParam(request(), "spec");
		if(spec==null)
			spec = "";	
		if ( !( spec .equals("") ) && !( spec .equals("undefined") ) ) 
			sql.append(" and ( spec like '%" + spec + "%'  )");
		
		String operator = StringUtil.getHttpParam(request(), "operator");
		if(operator==null)
			operator = "";	
		if ( !( operator .equals("") ) && !( operator .equals("undefined") ) ) 
			sql.append(" and ( operator like '%" + operator + "%'  )");
		
		String threshold = StringUtil.getHttpParam(request(), "threshold");
		if(threshold==null)
			threshold = "";	
		if ( !( threshold .equals("") ) && !( threshold .equals("undefined") ) ) 
			sql.append(" and ( threshold like '%" + threshold + "%'  )");
		
		String status = StringUtil.getHttpParam(request(), "status");
		if(status==null)
			status = "";	
		if ( !( status .equals("") ) && !( status .equals("undefined") ) ) 
			sql.append(" and ( status like '%" + status + "%'  )");
		
		String remark = StringUtil.getHttpParam(request(), "remark");
		if(remark==null)
			remark = "";	
		if ( !( remark .equals("") ) && !( remark .equals("undefined") ) ) 
			sql.append(" and ( remark like '%" + remark + "%'  )");
		
		String filepart = StringUtil.getHttpParam(request(), "filepart");
		if(filepart==null)
			filepart = "";	
		if ( !( filepart .equals("") ) && !( filepart .equals("undefined") ) ) 
			sql.append(" and ( filepart like '%" + filepart + "%'  )");
		
		String leaf = StringUtil.getHttpParam(request(), "leaf");
		if(leaf==null)
			leaf = "";	
		if ( !( leaf .equals("") ) && !( leaf .equals("undefined") ) ) 
			sql.append(" and ( leaf= '" + leaf + "'  )");
		
		String level = StringUtil.getHttpParam(request(), "level");
		if(level==null)
			level = "";	
		if ( !( level .equals("") ) && !( level .equals("undefined") ) ) 
			sql.append(" and ( level= '" + level + "'  )");
		
		String parentid = StringUtil.getHttpParam(request(), "parentid");
		if(parentid==null)
			parentid = "";	
		if ( !( parentid .equals("") ) && !( parentid .equals("undefined") ) ) 
			sql.append(" and ( parentid= '" + parentid + "'  )");
		
		String displayorder = StringUtil.getHttpParam(request(), "displayorder");
		if(displayorder==null)
			displayorder = "";	
		if ( !( displayorder .equals("") ) && !( displayorder .equals("undefined") ) ) 
			sql.append(" and ( displayorder= '" + displayorder + "'  )");
		
		String subq = StringUtil.getHttpParam(request(), "subq");
		if(subq==null)
			subq = "";	
		if ( !( subq .equals("") ) && !( subq .equals("undefined") ) ) 
			sql.append(" and ( subq like '%" + subq + "%'  )");
		
		String addtime = StringUtil.getHttpParam(request(), "addtime");
		if(addtime==null)
			addtime = "";	
		if ( !( addtime .equals("") ) && !( addtime .equals("undefined") ) ) 
			sql.append(" and ( addtime= '" + addtime + "'  )");
		
		String export = StringUtil.getHttpParam(request(), "export");// 是否导出
		if(export!=null && export.equals("1")){
			nStart = 0;
			nLimit = 65534;
		}
		
		Logger.info(sql.toString());
		//下面简单的分页，需要重构，应该由 Dao-Sevice去实现。
		int rowCount = Ebean.getServer(GlobalDBControl.getDB()).createQuery(IntelligentLockProduct .class, sql.toString()).findRowCount();
		List<IntelligentLockProduct> productList = Ebean.getServer(GlobalDBControl.getDB())
				.createQuery(IntelligentLockProduct .class, sql.toString())
				.setFirstRow(nStart).setMaxRows(nLimit)
				.orderBy("idd desc")
				.findList();
		List<IntelligentLockProduct> ulist = new ArrayList<IntelligentLockProduct>();
		
		for (IntelligentLockProduct product : productList) {//去除level为0的 目的不展示分类
			if(product.getLevel()!=0) {
				ulist.add(product);
			}
		}
				
		if(export!=null && export.equals("1")){
			String fileName = "IntelligentLockProduct"+simp.format( new Date())+".xls";
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
		PagedObject<IntelligentLockProduct> po= new PagedObject( ulist,nPages,rowCount );
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(po) );
	}

	//列表
	public static Result listAll(){
		List<IntelligentLockProduct> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.find(IntelligentLockProduct .class)
				.orderBy("idd")
				.findList();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(ulist) );
	}
	
	//删除
	public static Result delete(int idd){
		String sql = "delete from IntelligentLockProduct where idd =:idd";
		Ebean.getServer(GlobalDBControl.getDB())
				.createUpdate(IntelligentLockProduct .class, sql).setParameter("idd", idd).execute();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson("操作成功") );
	}
	
	//获取 单个
	public static Result get(int idd){
		IntelligentLockProduct intelligentlockproduct = Ebean.getServer(GlobalDBControl.getDB())
				.find(IntelligentLockProduct .class).where().eq("idd", idd).findUnique();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(intelligentlockproduct) );
	}

	//新增 / 修改
	public static Result modify(){
		String idd = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "idd");
		int nidd = StringTool.GetInt(idd, 0);
		
		String operation = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "operation");
		String image = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "image");
		
		String title = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "title");
		if(operation.equals("add")) {
			IntelligentLockProduct aproduct = null;
			try {
				aproduct = Ebean.getServer(GlobalDBControl.getDB()).find(IntelligentLockProduct.class).where().eq("title", title).ne("status", 1).findUnique();
				if(aproduct!=null) {
					LogUtil.writeLog("添加的' "+title+"'产品已经存在", "Product-ErrorLog");
					ResponseError error = new ResponseError();
					error.setStatus(1);
					error.setMassage("添加的' "+title+"'产品已经存在");
					return ok(Json.toJson(error));
				}
			} catch (Exception e) {
				LogUtil.writeLog("添加的' "+title+"'产品已经存在", "Product-ErrorLog");
				ResponseError error = new ResponseError();
				error.setStatus(1);
				error.setMassage("添加的' "+title+"'产品已经存在");
				return ok(Json.toJson(error));
			}
		}
		//产品类型为空相当于定义了一个产品类型
		String producttype = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "producttype").trim();
		producttype = producttype.equals("add")?"":producttype;
//		if(producttype=="") {
//			LogUtil.writeLog("商品类型不能为空", "Product-ErrorLog");
//			ResponseError error = new ResponseError();
//			error.setStatus(1);
//			error.setMassage("商品类型不能为空");
//			return ok(Json.toJson(error));
//		}
		//查询出的父类商品
		IntelligentLockProduct prot = null;
		//父类商品的ID
		Integer priductid = null;
		try {
			//获取前端传过来的产品类型查询数据库中对应的产品类型的id
			prot = Ebean.getServer(GlobalDBControl.getDB()).find(IntelligentLockProduct.class).where().eq("title", producttype).findUnique();//查询商品类型的idd 商品类型的idd
			if(prot!=null){
				priductid = prot.getIdd();
			}
			if(prot==null&producttype!="") {
				LogUtil.writeLog("插入商品的商品类型 ‘"+producttype+"’ 不存在 ", "Product-ErrorLog");
				ResponseError error = new ResponseError();
				error.setStatus(1);
				error.setMassage("插入商品的商品类型 ‘"+producttype+"’ 不存在");
				return ok(Json.toJson(error));
			}
		} catch (Exception e) {
			LogUtil.writeLog("插入商品的商品类型 ‘"+producttype+"’ 有误 "+e.getMessage(), "Product-ErrorLog");
			ResponseError error = new ResponseError();
			error.setStatus(1);
			error.setMassage("插入商品的商品类型 ‘"+producttype+"’ 有误");
			return ok(Json.toJson(error));
		}
		String model = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "model");
		String spec = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "spec");
		String operator = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "operator");
		String threshold = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "threshold");
		String status = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "status");
		String remark = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "remark");
		String filepart = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "filepart");
		String leaf = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "leaf");
		String level = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "level");
		String parentid = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "parentid");
		String displayorder = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "displayorder");
		String subq = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "subq");
		String addtime = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "addtime");
		IntelligentLockProduct intelligentlockproduct ;
		if(  operation.equals("add") )	// 新增
			intelligentlockproduct = new IntelligentLockProduct ();
		else			//修改
			intelligentlockproduct = Ebean.getServer(GlobalDBControl.getDB())
					.find(IntelligentLockProduct .class).where().eq("idd", idd).findUnique();
		
		if( intelligentlockproduct!=null ){
			//赋值,字符串类型可以直接赋值，其他类型需要转换 ！！！！
				
					intelligentlockproduct. setImage ( image );
					intelligentlockproduct. setTitle ( title );
					intelligentlockproduct. setProducttype ( producttype );
					intelligentlockproduct. setModel ( model );
					intelligentlockproduct. setSpec ( spec );
					intelligentlockproduct. setOperator ( Application.getSysUser().getLogin() );
					intelligentlockproduct. setThreshold ( StringTool.GetInt(threshold,0) );
					intelligentlockproduct. setStatus ( StringTool.GetInt(status,0) );
					intelligentlockproduct. setRemark ( remark );
					intelligentlockproduct. setFilepart ( filepart );
					intelligentlockproduct. setLeaf ( StringTool.GetInt(leaf,0) );
					IntelligentLockProduct findUnique = null;//查询出商品分类 父级
					try {
						findUnique = Ebean.getServer(GlobalDBControl.getDB()).find(IntelligentLockProduct.class).where().eq("idd", priductid).findUnique();
						if(findUnique!=null) {
							int parentlevel = findUnique.getLevel();
							intelligentlockproduct. setLevel ( parentlevel+1 );
						}else {
							intelligentlockproduct. setLevel ( StringTool.GetInt(level,0) );
						}
					} catch (Exception e) {
						LogUtil.writeLog("插入商品的商品类型 ‘"+producttype+"’ 不存在 "+e.getMessage(), "Product-ErrorLog");
						ResponseError error = new ResponseError();
						error.setStatus(1);
						error.setMassage("插入商品的商品类型 ‘"+producttype+"’ 不存在");
						return ok(Json.toJson(error));
					}
				
					if(priductid!=null) {
						intelligentlockproduct. setParentid ( priductid );
					}
					intelligentlockproduct. setDisplayorder ( StringTool.GetInt(displayorder,0) );
					intelligentlockproduct. setSubq ( subq );
					if(operation.equals("add"))
						intelligentlockproduct. setAddtime ( getDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
			Ebean.beginTransaction();
			try {
				Ebean.getServer(GlobalDBControl.getDB()).save( intelligentlockproduct );
				Ebean.commitTransaction();
			} catch (Exception e) {
				Ebean.rollbackTransaction();
				LogUtil.writeLog("商品表新增/修改 保存时出错"+e.getMessage(), "库存错误日志");
				ResponseError error = new ResponseError();
				error.setStatus(1);
				error.setMassage("保存商品出现异常");
				return ok(Json.toJson(error));
			}finally {
				Ebean.endTransaction();
			}
			response().setHeader("Cache-Control", "no-cache");
			ResponseError success = new ResponseError();
			success.setStatus(0);
			success.setMassage("执行成功");
			return ok( Json.toJson(success) );
		}

		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(null) );
	}
	
	static int num=0;
	public static File exportList(List<IntelligentLockProduct> infoList,String fileNameChine) throws UnsupportedEncodingException {
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
		
			//sheet.setColumnWidth( 12 , 6000 );
		
			//sheet.setColumnWidth( 13 , 6000 );
		
			//sheet.setColumnWidth( 14 , 6000 );
		
			//sheet.setColumnWidth( 15 , 6000 );
		
			//sheet.setColumnWidth( 16 , 6000 );
		
		ExcelGenerateHelper helper = ExcelGenerateHelper.getInstance(wb);

		// 获取行索引
		int rowIndex = 0;
		int colIndex = 0;

		// 设置表头
		String[] titles = { 
				
					"idd"
					
				
					
					,"产品图"
				
					
					,"产品名称"
				
					
					,"产品类型"
				
					
					,"型号"
				
					
					,"规格"
				
					
					,"操作人"
				
					
					,"库存警告阈值"
				
					
					,"状态"
				
					
					,"备注"
				
					
					//,"附件链接地址"
				
					
					//,"是否叶子节点"
				
					
					//,"树结构级别"
				
					
					//,"父级ID"
				
					
					//,"平级节点排序"
				
					
					,"序列号"
				
					
					,"添加时间"
				
			};

		// 生成表头
		helper.setRowIndex(rowIndex++);
		helper.generateHeader(sheet, titles, 0, StringUtils.EMPTY,
				StringUtils.EMPTY);

		// 循环生成数据行
		for (int i = 0; i < infoList.size(); i++) {
			colIndex = 0;
			IntelligentLockProduct info = (IntelligentLockProduct) infoList.get(i);
			Row row = sheet.createRow(rowIndex++);
			
			
				helper.createStringCell( row , colIndex++, ""+info. getIdd () );
			
				helper.createStringCell( row , colIndex++, ""+info. getImage () );
			
				helper.createStringCell( row , colIndex++, ""+info. getTitle () );
			
				helper.createStringCell( row , colIndex++, ""+info. getProducttype () );
			
				helper.createStringCell( row , colIndex++, ""+info. getModel () );
			
				helper.createStringCell( row , colIndex++, ""+info. getSpec () );
			
				helper.createStringCell( row , colIndex++, ""+info. getOperator () );
			
				helper.createStringCell( row , colIndex++, ""+info. getThreshold () );
			
				helper.createStringCell( row , colIndex++, ""+info. getStatus () );
			
				helper.createStringCell( row , colIndex++, ""+info. getRemark () );
			
				//helper.createStringCell( row , colIndex++, ""+info. getFilepart () );
			
				//helper.createStringCell( row , colIndex++, ""+info. getLeaf () );
			
				//helper.createStringCell( row , colIndex++, ""+info. getLevel () );
			
				//helper.createStringCell( row , colIndex++, ""+info. getParentid () );
			
				//helper.createStringCell( row , colIndex++, ""+info. getDisplayorder () );
			
				helper.createStringCell( row , colIndex++, ""+info. getSubq () );
			
				helper.createStringCell( row , colIndex++, ""+info. getAddtime () );
			
		}
		//在application.conf中配置的路径
		String path = Configuration.root().getString("export.path");
		int numStra = num++;//静态值，区分文件名
		File file = new File(path);
		file.mkdir();//判断文件夹是否存在，不存在就创建
		
		FileOutputStream out = null;
		
		String fileName = path + "IntelligentLockProduct" + System.currentTimeMillis() + numStra + ".xls";
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
				
					
				
					//"产品图"
			
					
					//,
					"产品名称"
				
					
					,"产品类型"
				
					
					,"型号"
				
					
					,"规格"
				
					
					,"操作人"
				
					
					,"库存警告阈值"
				
					
					,"状态"
				
					
					,"备注"
				
					
					//,"附件链接地址"
				
					
					//,"是否叶子节点"
				
					
					//,"树结构级别"
				
					
					//,"父级ID"
				
					
					//,"平级节点排序"
				
					
					,"序列号"
				
					
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
			
			List<IntelligentLockProduct> list = new ArrayList<IntelligentLockProduct>();
			 for (int i = 1; i <= lastRowNumber; i++) {
				 //批量添加
                 Row row = sheet.getRow(i);
                 if (row == null) {
                     break;
                 }
                 
                 IntelligentLockProduct fc = new IntelligentLockProduct();
                 int j = 0 ;
                 
					 
					
					
					
				
					
						//fc. setImage ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						//j=j+1;
					
				
					
						fc. setTitle ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					
						fc. setProducttype ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					
						fc. setModel ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					
						fc. setSpec ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					
						fc. setOperator ( Application.getSysUser().getLogin() );
					 
					
					
					
						//j=j+1;
					
				
					 
					
					
						fc. setThreshold ( StringTool.GetInt(StringUtil.cellValueToString(row.getCell(j)),0) );
					
					
						j=j+1;
					
				
					 
						fc. setStatus ( StringTool.GetInt(StringUtil.cellValueToString(row.getCell(j)),0) );
					
					
						j=j+1;
					
				
					
						fc. setRemark ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					
						//fc. setFilepart ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						//j=j+1;
					
				
					 
					
					
						//fc. setLeaf ( StringTool.GetInt(StringUtil.cellValueToString(row.getCell(j)),0) );
					
					
						//j=j+1;
					
				
					 
					
					
						//fc. setLevel ( StringTool.GetInt(StringUtil.cellValueToString(row.getCell(j)),0) );
					
					
						//j=j+1;
					
				
					 
					
					
						//fc. setParentid ( StringTool.GetInt(StringUtil.cellValueToString(row.getCell(j)),0) );
					
					
						//j=j+1;
					
				
					 
					
					
						//fc. setDisplayorder ( StringTool.GetInt(StringUtil.cellValueToString(row.getCell(j)),0) );
					
					
						//j=j+1;
					
				
					
						fc. setSubq ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					 
					
						fc. setAddtime ( getDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));	
					
				
                 list.add(fc);
                 if( list.size()>90 ){
                	 Ebean.getServer(GlobalDBControl.getDB()).save(list);
                	 list.clear();
                 }
			 }
			 
			 if( list.size()>0 ){
				 successNum = Ebean.getServer(GlobalDBControl.getDB()).save(list);
				 list.clear();
				 return ok(Json.toJson(successNum));
			 }
			
		}
	
		return ok(Json.toJson(successNum));
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
	public static Result uploadImg() {
		MultipartFormData uploadImage = request().body().asMultipartFormData();
		List<FilePart> files = uploadImage.getFiles();
		for (FilePart file : files) {
			if(file!=null) {
				String imagePath = ImageUploadUtil.putImg(file, "IPI");
				return ok(imagePath);
			}
		}
		return ok("");
	}
	public static Result downloadStorageExcel() {
		String idd = AjaxHellper.getHttpParam(request(), "idd");
		IntelligentLockProduct product = Ebean.getServer(GlobalDBControl.getDB()).find(IntelligentLockProduct.class).where().eq("idd", idd).findUnique();
		if(product.getFilepart()==null) {
			return ok("");
		}
		File file = new File(product.getFilepart().substring(1, product.getFilepart().length()));
		if(!file.exists()) {
			product.setFilepart(null);
			Ebean.getServer(GlobalDBControl.getDB()).save(product);
			return ok(Json.toJson("文件不存在或已经丢失"));
		}
		String fileNameChine = file.getName();
		if (request().getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) {
			try {
				fileNameChine = "=?UTF-8?B?" + (new String(Base64.encodeBase64(fileNameChine.getBytes("UTF-8")))) + "?=";
			} catch (UnsupportedEncodingException e) {
				LogUtil.writeLog("下载文件名定义是出现问题"+e.getMessage(), "Product-ErrorLog");
			}    
        } else{
        	try {
				fileNameChine = java.net.URLEncoder.encode(fileNameChine, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				LogUtil.writeLog("下载文件名定义是出现问题"+e.getMessage(), "Product-ErrorLog");
			}
        }

		response().setHeader("Content-disposition","attachment;filename=" + fileNameChine);
		response().setContentType("application/msexcel");
//		String path = Configuration.root().getString("export.path");
//		String fileName = path +""+ product.getTitle()+".xls";
//		FileInputStream fis = null;
//		FileOutputStream fos = null;
//		try {
//			fis = new FileInputStream(file);
//			fos = new FileOutputStream(fileName);
//			byte[] b = new byte[10240];
//			int l = 0;
//			try {
//				while((l=fis.read(b))!=-1) {
//					fos.write(b,0,b.length);
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}finally {
//			try {
//				fis.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			try {
//				fos.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
		return ok(file);
	}
	
	public static Result hasFilePath(int idd) {
//		String idd = AjaxHellper.getHttpParam(request(), "idd");
		IntelligentLockProduct product = Ebean.getServer(GlobalDBControl.getDB()).find(IntelligentLockProduct.class).where().eq("idd", idd).findUnique();
		if(product.getFilepart()==null) {
			ResponseError error = new ResponseError();
			error.setStatus(1);
			error.setMassage("该商品未上传详细Excel");
			return ok(Json.toJson(error));
		}else {
			File file = new File(product.getFilepart().substring(1, product.getFilepart().length()));
			if(!file.exists()) {
				product.setFilepart(null);
				Ebean.getServer(GlobalDBControl.getDB()).save(product);
				LogUtil.writeLog("存储的Excel出现了丢失", "Product-ErrorLog");
				ResponseError error = new ResponseError();
				error.setStatus(1);
				error.setMassage("文件不存在或已经丢失");
				return ok(Json.toJson(error));
			}
			LogUtil.writeLog("上传文件时 数据库中不存在该idd（"+idd+"）对应的商品 IntelligentLockProduct", "Product-ErrorLog");
			return ok(Json.toJson(""));
		}
	}
	
	public static Result storageExcel() {
		String idd = AjaxHellper.getHttpParam(request(), "idd");
		String filePath = "";
		IntelligentLockProduct product = null;
		try {
			product = Ebean.getServer(GlobalDBControl.getDB()).find(IntelligentLockProduct.class).where().eq("idd", idd).findUnique();
			MultipartFormData uploadImage = request().body().asMultipartFormData();
			List<FilePart> files = uploadImage.getFiles();
			for (FilePart file : files) {
				if(file!=null) {
					if(product.getFilepart()!=null) {
						//获取原有文件名 并将其删除
						File fi = new File(product.getFilepart());
						if(fi.exists()) {
							fi.delete();
						}
					}
					//获取商品名称 用于文件命名
					String productName = product.getTitle();
					//获取文件名称 用于切割后缀
					String fileName = file.getFilename();
					int index = fileName.lastIndexOf(".");
					//获取后缀 用于拼接文件名 格式为 商品名称+后缀
					String suffix = fileName.substring(index);
					filePath = FileUploadUtil.putImg(file.getFile(),productName+suffix,"ExcelUpload");
				}
			}
			if(product == null) {
				LogUtil.writeLog("上传文件时 数据库中不存在该idd（"+idd+"）对应的商品 IntelligentLockProduct", "Product-ErrorLog");
				ResponseError error = new ResponseError();
				error.setStatus(1);
				error.setMassage("产品表Excel存储时出现问题");
				return ok(Json.toJson(error));
			}
			product.setFilepart(filePath);
			Ebean.getServer(GlobalDBControl.getDB()).save(product);
		} catch (Exception e) {
			LogUtil.writeLog("产品表Excel存储是出现问题 "+e.getMessage(), "Product-ErrorLog");
			ResponseError error = new ResponseError();
			error.setStatus(1);
			error.setMassage("产品表Excel存储时出现问题");
			return ok(Json.toJson(error));
		}
		return ok(Json.toJson("已上传‘"+product.getTitle()+"’商品的Excel"));
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
	
	public static Result productTypes() {
//		List<IntelligentLockProduct> productList = Ebean.getServer(GlobalDBControl.getDB()).createQuery(IntelligentLockProduct.class).findList();
		List<IntelligentLockProduct> productList = Ebean.getServer(GlobalDBControl.getDB()).createQuery(IntelligentLockProduct.class).where().eq("level", 0).findList();
		return ok(Json.toJson(productList));
	}
	
}