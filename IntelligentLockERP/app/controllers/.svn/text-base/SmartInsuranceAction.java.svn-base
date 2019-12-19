
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
public class SmartInsuranceAction extends Controller {
  
	//进入页面列表；
	public static Result view() {
		return ok( SmartInsuranceList.render() );
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
		sql.append("find SmartInsurance where 1=1 ");
		
		String idd = StringUtil.getHttpParam(request(), "idd");
		if(idd==null)
			idd = "";	
		if ( !( idd .equals("") ) && !( idd .equals("undefined") ) ) 
			sql.append(" and ( idd= '" + idd + "'  )");
		
		String deviceid = StringUtil.getHttpParam(request(), "deviceid");
		if(deviceid==null)
			deviceid = "";	
		if ( !( deviceid .equals("") ) && !( deviceid .equals("undefined") ) ) 
			sql.append(" and ( deviceid= '" + deviceid + "'  )");
		
		String policyno = StringUtil.getHttpParam(request(), "policyno");
		if(policyno==null)
			policyno = "";	
		if ( !( policyno .equals("") ) && !( policyno .equals("undefined") ) ) 
			sql.append(" and ( policyno= '" + policyno + "'  )");
		
		String addtime = StringUtil.getHttpParam(request(), "addtime");
		if(addtime==null)
			addtime = "";	
		if ( !( addtime .equals("") ) && !( addtime .equals("undefined") ) ) 
			sql.append(" and ( addtime= '" + addtime + "'  )");
		
		String begintime = StringUtil.getHttpParam(request(), "begintime");
		if(begintime==null)
			begintime = "";	
		if ( !( begintime .equals("") ) && !( begintime .equals("undefined") ) ) 
			sql.append(" and ( begintime= '" + begintime + "'  )");
		
		String overtime = StringUtil.getHttpParam(request(), "overtime");
		if(overtime==null)
			overtime = "";	
		if ( !( overtime .equals("") ) && !( overtime .equals("undefined") ) ) 
			sql.append(" and ( overtime= '" + overtime + "'  )");
		
		String validity = StringUtil.getHttpParam(request(), "validity");
		if(validity==null)
			validity = "";	
		if ( !( validity .equals("") ) && !( validity .equals("undefined") ) ) 
			sql.append(" and ( validity= '" + validity + "'  )");
		
		String name = StringUtil.getHttpParam(request(), "name");
		if(name==null)
			name = "";	
		if ( !( name .equals("") ) && !( name .equals("undefined") ) ) 
			sql.append(" and ( name= '" + name + "'  )");
		
		String cardtype = StringUtil.getHttpParam(request(), "cardtype");
		if(cardtype==null)
			cardtype = "";	
		if ( !( cardtype .equals("") ) && !( cardtype .equals("undefined") ) ) 
			sql.append(" and ( cardtype= '" + cardtype + "'  )");
		
		String cardnum = StringUtil.getHttpParam(request(), "cardnum");
		if(cardnum==null)
			cardnum = "";	
		if ( !( cardnum .equals("") ) && !( cardnum .equals("undefined") ) ) 
			sql.append(" and ( cardnum= '" + cardnum + "'  )");
		
		String phone = StringUtil.getHttpParam(request(), "phone");
		if(phone==null)
			phone = "";	
		if ( !( phone .equals("") ) && !( phone .equals("undefined") ) ) 
			sql.append(" and ( phone= '" + phone + "'  )");
		
		String email = StringUtil.getHttpParam(request(), "email");
		if(email==null)
			email = "";	
		if ( !( email .equals("") ) && !( email .equals("undefined") ) ) 
			sql.append(" and ( email= '" + email + "'  )");
		
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
		
		String district = StringUtil.getHttpParam(request(), "district");
		if(district==null)
			district = "";	
		if ( !( district .equals("") ) && !( district .equals("undefined") ) ) 
			sql.append(" and ( district= '" + district + "'  )");
		
		String address = StringUtil.getHttpParam(request(), "address");
		if(address==null)
			address = "";	
		if ( !( address .equals("") ) && !( address .equals("undefined") ) ) 
			sql.append(" and ( address= '" + address + "'  )");
		
		String status = StringUtil.getHttpParam(request(), "status");
		if(status==null)
			status = "";	
		if ( !( status .equals("") ) && !( status .equals("undefined") ) ) 
			sql.append(" and ( status= '" + status + "'  )");
		
		String coverage = StringUtil.getHttpParam(request(), "coverage");
		if(coverage==null)
			coverage = "";	
		if ( !( coverage .equals("") ) && !( coverage .equals("undefined") ) ) 
			sql.append(" and ( coverage= '" + coverage + "'  )");
		
		String remark = StringUtil.getHttpParam(request(), "remark");
		if(remark==null)
			remark = "";	
		if ( !( remark .equals("") ) && !( remark .equals("undefined") ) ) 
			sql.append(" and ( remark= '" + remark + "'  )");
		
		String billno = StringUtil.getHttpParam(request(), "billno");
		if(billno==null)
			billno = "";	
		if ( !( billno .equals("") ) && !( billno .equals("undefined") ) ) 
			sql.append(" and ( billno= '" + billno + "'  )");
		
		String warrantyurl = StringUtil.getHttpParam(request(), "warrantyurl");
		if(warrantyurl==null)
			warrantyurl = "";	
		if ( !( warrantyurl .equals("") ) && !( warrantyurl .equals("undefined") ) ) 
			sql.append(" and ( warrantyurl= '" + warrantyurl + "'  )");
		
		String export = StringUtil.getHttpParam(request(), "export");// 是否导出
		if(export!=null && export.equals("1")){
			nStart = 0;
			nLimit = 65534;
		}
		
		Logger.info(sql.toString());
		//下面简单的分页，需要重构，应该由 Dao-Sevice去实现。
		int rowCount = Ebean.getServer(GlobalDBControl.getDB()).createQuery(SmartInsurance .class, sql.toString()).findRowCount();
		List<SmartInsurance> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.createQuery(SmartInsurance .class, sql.toString())
				.setFirstRow(nStart).setMaxRows(nLimit)
				.orderBy("idd desc")
				.findList();
				
		if(export!=null && export.equals("1")){
			String fileName = "SmartInsurance"+simp.format( new Date())+".xls";
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
		PagedObject<SmartInsurance> po= new PagedObject( ulist,nPages,rowCount );
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(po) );
	}

	//列表
	public static Result listAll(){
		List<SmartInsurance> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.find(SmartInsurance .class)
				.orderBy("idd")
				.findList();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(ulist) );
	}
	
	//删除
	public static Result delete(int idd){
		String sql = "delete from SmartInsurance where idd =:idd";
		Ebean.getServer(GlobalDBControl.getDB())
				.createUpdate(SmartInsurance .class, sql).setParameter("idd", idd).execute();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson("操作成功") );
	}
	
	//获取 单个
	public static Result get(int idd){
		SmartInsurance smartinsurance = Ebean.getServer(GlobalDBControl.getDB())
				.find(SmartInsurance .class).where().eq("idd", idd).findUnique();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(smartinsurance) );
	}

	//新增 / 修改
	public static Result modify(){
		String idd = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "idd");
		int nidd = StringTool.GetInt(idd, 0);
		
		
		
		
		
		String deviceid = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "deviceid");
		
		
		
		String policyno = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "policyno");
		
		
		
		String addtime = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "addtime");
		
		
		
		String begintime = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "begintime");
		
		
		
		String overtime = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "overtime");
		
		
		
		String validity = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "validity");
		
		
		
		String name = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "name");
		
		
		
		String cardtype = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "cardtype");
		
		
		
		String cardnum = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "cardnum");
		
		
		
		String phone = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "phone");
		
		
		
		String email = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "email");
		
		
		
		String province = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "province");
		
		
		
		String city = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "city");
		
		
		
		String district = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "district");
		
		
		
		String address = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "address");
		
		
		
		String status = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "status");
		
		
		
		String coverage = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "coverage");
		
		
		
		String remark = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "remark");
		
		
		
		String billno = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "billno");
		
		
		
		String warrantyurl = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "warrantyurl");
		
		
		
		String operation = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "operation");
		
		SmartInsurance smartinsurance ;
		if(  operation.equals("add") )	// 新增
			smartinsurance = new SmartInsurance ();
		else			//修改
			smartinsurance = Ebean.getServer(GlobalDBControl.getDB())
					.find(SmartInsurance .class).where().eq("idd", idd).findUnique();
		
		if( smartinsurance!=null ){
			//赋值,字符串类型可以直接赋值，其他类型需要转换 ！！！！
			
				 
				
				
			
				
					smartinsurance. setDeviceid ( deviceid );
				 
				
				
			
				
					smartinsurance. setPolicyno ( policyno );
				 
				
				
			
				 
				
					smartinsurance. setAddtime ( StringUtil.getDate(addtime) );
				
				
			
				 
				
					smartinsurance. setBegintime ( StringUtil.getDate(begintime) );
				
				
			
				 
				
					smartinsurance. setOvertime ( StringUtil.getDate(overtime) );
				
				
			
				 
				
				
					smartinsurance. setValidity ( StringTool.GetInt(validity,0) );
				
			
				
					smartinsurance. setName ( name );
				 
				
				
			
				 
				
				
					smartinsurance. setCardtype ( StringTool.GetInt(cardtype,0) );
				
			
				
					smartinsurance. setCardnum ( cardnum );
				 
				
				
			
				
					smartinsurance. setPhone ( phone );
				 
				
				
			
				
					smartinsurance. setEmail ( email );
				 
				
				
			
				
					smartinsurance. setProvince ( province );
				 
				
				
			
				
					smartinsurance. setCity ( city );
				 
				
				
			
				
					smartinsurance. setDistrict ( district );
				 
				
				
			
				
					smartinsurance. setAddress ( address );
				 
				
				
			
				 
				
				
					smartinsurance. setStatus ( StringTool.GetInt(status,0) );
				
			
				 
				
				
					smartinsurance. setCoverage ( StringTool.GetInt(coverage,0) );
				
			
				
					smartinsurance. setRemark ( remark );
				 
				
				
			
				
					smartinsurance. setBillno ( billno );
				 
				
				
			
				
					smartinsurance. setWarrantyurl ( warrantyurl );
				 
				
				
			
			Ebean.getServer(GlobalDBControl.getDB()).save( smartinsurance );
			response().setHeader("Cache-Control", "no-cache");
			return ok( Json.toJson(smartinsurance) );
		}

		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(null) );
	}
	
	static int num=0;
	public static File exportList(List<SmartInsurance> infoList,String fileNameChine) throws UnsupportedEncodingException {
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
		
		ExcelGenerateHelper helper = ExcelGenerateHelper.getInstance(wb);

		// 获取行索引
		int rowIndex = 0;
		int colIndex = 0;

		// 设置表头
		String[] titles = { 
				
					"idd"
					
				
					
					,"设备ID"
				
					
					,"保单号"
				
					
					,"添加时间"
				
					
					,"起报时间"
				
					
					,"终保日期"
				
					
					,"有效期"
				
					
					,"被保人姓名"
				
					
					,"1身份证"
				
					
					,"证件号"
				
					
					,"手机号"
				
					
					,"邮箱"
				
					
					,"省"
				
					
					,"市"
				
					
					,"区"
				
					
					,"详细地址"
				
					
					,"状态"
				
					
					,"保额"
				
					
					,"备注"
				
					
					,"支付订单号"
				
					
					,"电子保单URL"
				
			};

		// 生成表头
		helper.setRowIndex(rowIndex++);
		helper.generateHeader(sheet, titles, 0, StringUtils.EMPTY,
				StringUtils.EMPTY);

		// 循环生成数据行
		for (int i = 0; i < infoList.size(); i++) {
			colIndex = 0;
			SmartInsurance info = (SmartInsurance) infoList.get(i);
			Row row = sheet.createRow(rowIndex++);
			
			
				helper.createStringCell( row , colIndex++, ""+info. getIdd () );
			
				helper.createStringCell( row , colIndex++, ""+info. getDeviceid () );
			
				helper.createStringCell( row , colIndex++, ""+info. getPolicyno () );
			
				helper.createStringCell( row , colIndex++, ""+info. getAddtime () );
			
				helper.createStringCell( row , colIndex++, ""+info. getBegintime () );
			
				helper.createStringCell( row , colIndex++, ""+info. getOvertime () );
			
				helper.createStringCell( row , colIndex++, ""+info. getValidity () );
			
				helper.createStringCell( row , colIndex++, ""+info. getName () );
			
				helper.createStringCell( row , colIndex++, ""+info. getCardtype () );
			
				helper.createStringCell( row , colIndex++, ""+info. getCardnum () );
			
				helper.createStringCell( row , colIndex++, ""+info. getPhone () );
			
				helper.createStringCell( row , colIndex++, ""+info. getEmail () );
			
				helper.createStringCell( row , colIndex++, ""+info. getProvince () );
			
				helper.createStringCell( row , colIndex++, ""+info. getCity () );
			
				helper.createStringCell( row , colIndex++, ""+info. getDistrict () );
			
				helper.createStringCell( row , colIndex++, ""+info. getAddress () );
			
				helper.createStringCell( row , colIndex++, ""+info. getStatus () );
			
				helper.createStringCell( row , colIndex++, ""+info. getCoverage () );
			
				helper.createStringCell( row , colIndex++, ""+info. getRemark () );
			
				helper.createStringCell( row , colIndex++, ""+info. getBillno () );
			
				helper.createStringCell( row , colIndex++, ""+info. getWarrantyurl () );
			
		}
		//在application.conf中配置的路径
		String path = Configuration.root().getString("export.path");
		int numStra = num++;//静态值，区分文件名
		File file = new File(path);
		file.mkdir();//判断文件夹是否存在，不存在就创建
		
		FileOutputStream out = null;
		String fileName = path + "SmartInsurance" + System.currentTimeMillis() + numStra + ".xls";
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
				
					
					
				
					"设备ID"
					
				
					
					,"保单号"
				
					
					,"添加时间"
				
					
					,"起报时间"
				
					
					,"终保日期"
				
					
					,"有效期"
				
					
					,"被保人姓名"
				
					
					,"1身份证"
				
					
					,"证件号"
				
					
					,"手机号"
				
					
					,"邮箱"
				
					
					,"省"
				
					
					,"市"
				
					
					,"区"
				
					
					,"详细地址"
				
					
					,"状态"
				
					
					,"保额"
				
					
					,"备注"
				
					
					,"支付订单号"
				
					
					,"电子保单URL"
				
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
			
			List<SmartInsurance> list = new ArrayList<SmartInsurance>();
			 for (int i = 1; i <= lastRowNumber; i++) {
				 //批量添加
                 Row row = sheet.getRow(i);
                 if (row == null) {
                     break;
                 }
                 
                 SmartInsurance fc = new SmartInsurance();
                 int j = 0 ;
                 
					 
					
					
					
				
					
						fc. setDeviceid ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					
						fc. setPolicyno ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					 
					
						//fc. setAddtime ( StringUtil.getDate( StringUtil.cellValueToString(row.getCell(j) ) );
					
					
					
						j=j+1;
					
				
					 
					
						//fc. setBegintime ( StringUtil.getDate( StringUtil.cellValueToString(row.getCell(j) ) );
					
					
					
						j=j+1;
					
				
					 
					
						//fc. setOvertime ( StringUtil.getDate( StringUtil.cellValueToString(row.getCell(j) ) );
					
					
					
						j=j+1;
					
				
					 
					
					
						fc. setValidity ( StringTool.GetInt(StringUtil.cellValueToString(row.getCell(j)),0) );
					
					
						j=j+1;
					
				
					
						fc. setName ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					 
					
					
						fc. setCardtype ( StringTool.GetInt(StringUtil.cellValueToString(row.getCell(j)),0) );
					
					
						j=j+1;
					
				
					
						fc. setCardnum ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					
						fc. setPhone ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					
						fc. setEmail ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					
						fc. setProvince ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					
						fc. setCity ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					
						fc. setDistrict ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					
						fc. setAddress ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					 
					
					
						fc. setStatus ( StringTool.GetInt(StringUtil.cellValueToString(row.getCell(j)),0) );
					
					
						j=j+1;
					
				
					 
					
					
						fc. setCoverage ( StringTool.GetInt(StringUtil.cellValueToString(row.getCell(j)),0) );
					
					
						j=j+1;
					
				
					
						fc. setRemark ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					
						fc. setBillno ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
						j=j+1;
					
				
					
						fc. setWarrantyurl ( StringUtil.cellValueToString(row.getCell(j)) );
					 
					
					
					
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