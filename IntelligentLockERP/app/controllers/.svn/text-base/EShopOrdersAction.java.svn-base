
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

import models.EShopOrders;
import models.ReturnMessage;

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
import views.html.page.EShopOrdersList;

import com.avaje.ebean.Ebean;

@Security.Authenticated(Secured.class)
public class EShopOrdersAction extends Controller {
  
	//进入页面列表；
	public static Result view() {
		return ok( EShopOrdersList.render() );
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
		sql.append("find EShopOrders where 1=1 ");
		
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
		
		String province = StringUtil.getHttpParam(request(), "province");
		if(province==null)
			province = "";	
		if ( !( province .equals("") ) && !( province .equals("undefined") ) ) 
			sql.append(" and ( province= '" + province + "'  )");
		
		String city = StringUtil.getHttpParam(request(), "city");
		if(city==null)
			city = "";	
		if ( !( city .equals("") ) && !( city .equals("undefined") ) ) 
			sql.append(" and ( city= '" + city + "'  )");
		
		String pidd = StringUtil.getHttpParam(request(), "pidd");
		if(pidd==null)
			pidd = "";	
		if ( !( pidd .equals("") ) && !( pidd .equals("undefined") ) ) 
			sql.append(" and ( pidd= '" + pidd + "'  )");
		
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
		
		String smallimgurl = StringUtil.getHttpParam(request(), "smallimgurl");
		if(smallimgurl==null)
			smallimgurl = "";	
		if ( !( smallimgurl .equals("") ) && !( smallimgurl .equals("undefined") ) ) 
			sql.append(" and ( smallimgurl= '" + smallimgurl + "'  )");
		
		String amount = StringUtil.getHttpParam(request(), "amount");
		if(amount==null)
			amount = "";	
		if ( !( amount .equals("") ) && !( amount .equals("undefined") ) ) 
			sql.append(" and ( amount= '" + amount + "'  )");
		
		String money = StringUtil.getHttpParam(request(), "money");
		if(money==null)
			money = "";	
		if ( !( money .equals("") ) && !( money .equals("undefined") ) ) 
			sql.append(" and ( money= '" + money + "'  )");
		
		String bvalue = StringUtil.getHttpParam(request(), "bvalue");
		if(bvalue==null)
			bvalue = "";	
		if ( !( bvalue .equals("") ) && !( bvalue .equals("undefined") ) ) 
			sql.append(" and ( bvalue= '" + bvalue + "'  )");
		
		String pcsaleprice = StringUtil.getHttpParam(request(), "pcsaleprice");
		if(pcsaleprice==null)
			pcsaleprice = "";	
		if ( !( pcsaleprice .equals("") ) && !( pcsaleprice .equals("undefined") ) ) 
			sql.append(" and ( pcsaleprice= '" + pcsaleprice + "'  )");
		
		String vipstatus = StringUtil.getHttpParam(request(), "vipstatus");
		if(vipstatus==null)
			vipstatus = "";	
		if ( !( vipstatus .equals("") ) && !( vipstatus .equals("undefined") ) && !( vipstatus .equals("-1") )) 
			sql.append(" and ( vipstatus= '" + vipstatus + "'  )");
		
		String status = StringUtil.getHttpParam(request(), "status");
		if(status==null)
			status = "";	
		if ( !( status .equals("") ) && !( status .equals("undefined") )&& !( status .equals("-2") ) ) 
			sql.append(" and ( status= '" + status + "'  )");
		
		String prepayid = StringUtil.getHttpParam(request(), "prepayid");
		if(prepayid==null)
			prepayid = "";	
		if ( !( prepayid .equals("") ) && !( prepayid .equals("undefined") ) ) 
			sql.append(" and ( prepayid= '" + prepayid + "'  )");
		
		String consigneename = StringUtil.getHttpParam(request(), "consigneename");
		if(consigneename==null)
			consigneename = "";	
		if ( !( consigneename .equals("") ) && !( consigneename .equals("undefined") ) ) 
			sql.append(" and ( consigneename= '" + consigneename + "'  )");
		
		String consigneearea = StringUtil.getHttpParam(request(), "consigneearea");
		if(consigneearea==null)
			consigneearea = "";	
		if ( !( consigneearea .equals("") ) && !( consigneearea .equals("undefined") ) ) 
			sql.append(" and ( consigneearea= '" + consigneearea + "'  )");
		
		String consigneeaddress = StringUtil.getHttpParam(request(), "consigneeaddress");
		if(consigneeaddress==null)
			consigneeaddress = "";	
		if ( !( consigneeaddress .equals("") ) && !( consigneeaddress .equals("undefined") ) ) 
			sql.append(" and ( consigneeaddress= '" + consigneeaddress + "'  )");
		
		String consigneephone = StringUtil.getHttpParam(request(), "consigneephone");
		if(consigneephone==null)
			consigneephone = "";	
		if ( !( consigneephone .equals("") ) && !( consigneephone .equals("undefined") ) ) 
			sql.append(" and ( consigneephone= '" + consigneephone + "'  )");
		
		String expressname = StringUtil.getHttpParam(request(), "expressname");
		if(expressname==null)
			expressname = "";	
		if ( !( expressname .equals("") ) && !( expressname .equals("undefined") ) ) 
			sql.append(" and ( expressname= '" + expressname + "'  )");
		
		String expressorderid = StringUtil.getHttpParam(request(), "expressorderid");
		if(expressorderid==null)
			expressorderid = "";	
		if ( !( expressorderid .equals("") ) && !( expressorderid .equals("undefined") ) ) 
			sql.append(" and ( expressorderid= '" + expressorderid + "'  )");
		
		String expressstatus = StringUtil.getHttpParam(request(), "expressstatus");
		if(expressstatus==null)
			expressstatus = "";	
		if ( !( expressstatus .equals("") ) && !( expressstatus .equals("undefined") ) && !( expressstatus .equals("-1") )) 
			sql.append(" and ( expressstatus= '" + expressstatus + "'  )");
		
		String export = StringUtil.getHttpParam(request(), "export");// 是否导出
		if(export!=null && export.equals("1")){
			nStart = 0;
			nLimit = 65534;
		}
		
		Logger.info(sql.toString());
		//下面简单的分页，需要重构，应该由 Dao-Sevice去实现。
		int rowCount = Ebean.getServer(GlobalDBControl.getDB()).createQuery(EShopOrders .class, sql.toString()).findRowCount();
		List<EShopOrders> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.createQuery(EShopOrders .class, sql.toString())
				.setFirstRow(nStart).setMaxRows(nLimit)
				.orderBy("idd desc")
				.findList();
				
		if(export!=null && export.equals("1")){
			String fileName = "EShopOrders"+simp.format( new Date())+".xls";
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
		PagedObject<EShopOrders> po= new PagedObject( ulist,nPages,rowCount );
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(po) );
	}

	//列表
	public static Result listAll(){
		List<EShopOrders> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.find(EShopOrders .class)
				.orderBy("idd")
				.findList();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(ulist) );
	}
	
	//修改物流发货状态
	public static Result salesReturn(int idd){
		//根据id查询该条订单信息
		EShopOrders eshoporders = Ebean.getServer(GlobalDBControl.getDB())
				.find(EShopOrders .class).where().eq("idd", idd).findUnique();
		if(eshoporders!=null){
			if(eshoporders.getExpressstatus()==0){
				String sql = "update eshop_orders set expressstatus=2 where idd =:idd";
				Ebean.getServer(GlobalDBControl.getDB())
				.createSqlUpdate(sql).setParameter("idd", idd).execute();
				response().setHeader("Cache-Control", "no-cache");
				return ok( Json.toJson("操作成功") );
			}else{
				return ok( Json.toJson("该订单没有发货,无需修改为退货状态") );
			}
		}
		return ok( Json.toJson("数据错误") );
	}
	
	//获取 单个
	public static Result get(int idd){
		EShopOrders eshoporders = Ebean.getServer(GlobalDBControl.getDB())
				.find(EShopOrders .class).where().eq("idd", idd).findUnique();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(eshoporders) );
	}

	//新增 / 修改
	public static Result modify(){
		ReturnMessage returnMessage = new ReturnMessage();
		String idd = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "idd");
		int nidd = StringTool.GetInt(idd, 0);
		String expressname = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "expressname");
		String expressorderid = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "expressorderid");
		
		EShopOrders	eshoporders = Ebean.getServer(GlobalDBControl.getDB())
				.find(EShopOrders .class).where().eq("idd", nidd).findUnique();
		
		if( eshoporders==null){
			response().setHeader("Cache-Control", "no-cache");
			returnMessage.setMessage("数据错误,该笔订单不存在");
			return ok( Json.toJson(returnMessage) );
		}
		if(eshoporders.getStatus()==1){		//待支付
			response().setHeader("Cache-Control", "no-cache");
			returnMessage.setMessage("该笔订单还未支付,请勿发货");
			return ok( Json.toJson(returnMessage) );
		}
		if(eshoporders.getStatus()==-1){	//超时
			response().setHeader("Cache-Control", "no-cache");
			returnMessage.setMessage("该笔订单已超时,请勿发货");
			return ok( Json.toJson(returnMessage) );
		}
		if(eshoporders.getExpressstatus()==2){	//退货
			response().setHeader("Cache-Control", "no-cache");
			returnMessage.setMessage("该笔订单已退货,请勿重新发货");
			return ok( Json.toJson(returnMessage) );
		}
		
		//赋值,字符串类型可以直接赋值，其他类型需要转换 ！！！！
		eshoporders. setExpressname ( expressname );
		eshoporders. setExpressorderid ( expressorderid );
		eshoporders. setExpressstatus(0);
			
		Ebean.getServer(GlobalDBControl.getDB()).save( eshoporders );
		response().setHeader("Cache-Control", "no-cache");
		returnMessage.setCode("0");
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
	
	static int num=0;
	public static File exportList(List<EShopOrders> infoList,String fileNameChine) throws UnsupportedEncodingException {
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
		
			sheet.setColumnWidth( 18 , 6000 );
		
			sheet.setColumnWidth( 19 , 6000 );
		
			sheet.setColumnWidth( 20 , 6000 );
		
			sheet.setColumnWidth( 21 , 6000 );
		
			sheet.setColumnWidth( 22 , 6000 );
		
			sheet.setColumnWidth( 23 , 6000 );
		
		ExcelGenerateHelper helper = ExcelGenerateHelper.getInstance(wb);

		// 获取行索引
		int rowIndex = 0;
		int colIndex = 0;

		// 设置表头
		String[] titles = { 
				
			"ID"
			
			,"用户ID"
			
			,"省"
			
			,"市"
			
			,"产品ID"
			
			,"标题"
			
			,"商品描述"
			
			,"小图"
			
			,"购买的数量"
			
			,"添加时间"
			
			,"人民币支付价格(分)"
			
			,"奖励金支付数量"
			
			,"成本价"
			
			,"会员状态"
			
			,"支付成功时间"
		
			,"状态"
		
			,"支付订单"
			
			,"收件人姓名"
			
			,"收件人地区"
			
			,"收件人详细地址"
			
			,"收件人手机号"
			
			,"物流名称"
			
			,"物流号"
			
			,"物流状态"
		};

		// 生成表头
		helper.setRowIndex(rowIndex++);
		helper.generateHeader(sheet, titles, 0, StringUtils.EMPTY,
				StringUtils.EMPTY);

		// 循环生成数据行
		for (int i = 0; i < infoList.size(); i++) {
			colIndex = 0;
			EShopOrders info = (EShopOrders) infoList.get(i);
			Row row = sheet.createRow(rowIndex++);
			
			helper.createStringCell( row , colIndex++, ""+info. getIdd () );
		
			helper.createStringCell( row , colIndex++, ""+info. getUserid () );
		
			helper.createStringCell( row , colIndex++, ""+info. getProvince () );
		
			helper.createStringCell( row , colIndex++, ""+info. getCity () );
		
			helper.createStringCell( row , colIndex++, ""+info. getPidd () );
		
			helper.createStringCell( row , colIndex++, ""+info. getTitle () );
		
			helper.createStringCell( row , colIndex++, ""+info. getSubtitle() );
		
			helper.createStringCell( row , colIndex++, ""+info. getSmallimgurl () );
		
			helper.createStringCell( row , colIndex++, ""+info. getAmount () );
		
			helper.createStringCell( row , colIndex++, ""+info. getAddtime () );
		
			helper.createStringCell( row , colIndex++, ""+info. getMoney () );
		
			helper.createStringCell( row , colIndex++, ""+info. getBvalue () );
		
			helper.createStringCell( row , colIndex++, ""+info. getPcsaleprice () );
		
			helper.createStringCell( row , colIndex++, ""+info. getVipstatus () );
		
			helper.createStringCell( row , colIndex++, ""+info. getUpdatetime () );
		
			helper.createStringCell( row , colIndex++, ""+info. getStatus () );
		
			helper.createStringCell( row , colIndex++, ""+info. getPrepayid () );
		
			helper.createStringCell( row , colIndex++, ""+info. getConsigneename () );
		
			helper.createStringCell( row , colIndex++, ""+info. getConsigneearea () );
		
			helper.createStringCell( row , colIndex++, ""+info. getConsigneeaddress () );
		
			helper.createStringCell( row , colIndex++, ""+info. getConsigneephone () );
		
			helper.createStringCell( row , colIndex++, ""+info. getExpressname () );
		
			helper.createStringCell( row , colIndex++, ""+info. getExpressorderid () );
		
			helper.createStringCell( row , colIndex++, ""+info. getExpressstatus () );
			
		}
		//在application.conf中配置的路径
		String path = Configuration.root().getString("export.path");
		int numStra = num++;//静态值，区分文件名
		File file = new File(path);
		file.mkdir();//判断文件夹是否存在，不存在就创建
		
		FileOutputStream out = null;
		String fileName = path + "EShopOrders" + System.currentTimeMillis() + numStra + ".xls";
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
	
		"用户编码"
		
		,"省"
		
		,"市"
		
		,"产品ID"
		
		,"标题"
		
		,"商品描述"
	
		,"小图"
		
		,"购买的数量"
		
		,"人民币支付价格(分)"
		
		,"奖励金支付数量"
	
		,"成本价"
		
		,"会员状态"
		
		,"状态"
		
		,"支付订单"
		
		,"收件人姓名"
	
		,"收件人地区"
		
		,"收件人详细地址"
		
		,"收件人手机号"
		
		,"物流名称"
	
		,"物流号"
		
		,"物流状态"
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
			
			List<EShopOrders> list = new ArrayList<EShopOrders>();
			for (int i = 1; i <= lastRowNumber; i++) {
				//批量添加
                Row row = sheet.getRow(i);
                if (row == null) {
                    break;
                }
                 
                EShopOrders fc = new EShopOrders();
                int j = 0 ;
				fc. setUserid ( StringTool.GetInt(StringUtil.cellValueToString(row.getCell(j)),0) );
			
				j=j+1;
				fc. setProvince ( StringUtil.cellValueToString(row.getCell(j)) );
				
				j=j+1;
				fc. setCity ( StringUtil.cellValueToString(row.getCell(j)) );
			
				j=j+1;
				fc. setPidd ( StringTool.GetInt(StringUtil.cellValueToString(row.getCell(j)),0) );
			
				j=j+1;
				fc. setTitle ( StringUtil.cellValueToString(row.getCell(j)) );
			
				j=j+1;
				fc. setSubtitle ( StringUtil.cellValueToString(row.getCell(j)) );
			
				j=j+1;
				fc. setSmallimgurl ( StringUtil.cellValueToString(row.getCell(j)) );
			
				j=j+1;
				fc. setAmount ( StringTool.GetInt(StringUtil.cellValueToString(row.getCell(j)),0) );
			
				j=j+1;
				fc. setMoney ( StringTool.GetDouble(StringUtil.cellValueToString(row.getCell(j)),0d) );
			
				j=j+1;
				fc. setBvalue ( StringTool.GetInt(StringUtil.cellValueToString(row.getCell(j)),0) );
			
			
				j=j+1;
				fc. setPcsaleprice ( StringTool.GetDouble(StringUtil.cellValueToString(row.getCell(j)),0d) );
			
				j=j+1;
				fc. setVipstatus ( StringTool.GetInt(StringUtil.cellValueToString(row.getCell(j)),0) );
			
			
				j=j+1;
				Integer status =  StringTool.GetInt(StringUtil.cellValueToString(row.getCell(j)),0);
				if(status==0){
					fc.setUpdatetime( new Date() );
				}
				fc. setStatus ( status );
			
				j=j+1;
				fc. setPrepayid ( StringUtil.cellValueToString(row.getCell(j)) );
				
				j=j+1;
				fc. setConsigneename ( StringUtil.cellValueToString(row.getCell(j)) );
			
				j=j+1;
				fc. setConsigneearea ( StringUtil.cellValueToString(row.getCell(j)) );
			
				j=j+1;
				fc. setConsigneeaddress ( StringUtil.cellValueToString(row.getCell(j)) );
			
				j=j+1;
				fc. setConsigneephone ( StringUtil.cellValueToString(row.getCell(j)) );
			
				j=j+1;
				fc. setExpressname ( StringUtil.cellValueToString(row.getCell(j)) );
			
				j=j+1;
				fc. setExpressorderid ( StringUtil.cellValueToString(row.getCell(j)) );
			
				j=j+1;
				fc. setExpressstatus ( StringTool.GetInt(StringUtil.cellValueToString(row.getCell(j)),0) );
				
                list.add(fc);
                if( list.size()>90 ){
                	Ebean.getServer(GlobalDBControl.getDB()).save(list);
                	successNum+=list.size();
                	list.clear();
                }
			 }
			 if( list.size()>0 ){
				 Ebean.getServer(GlobalDBControl.getDB()).save(list);
				 successNum+=list.size();
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