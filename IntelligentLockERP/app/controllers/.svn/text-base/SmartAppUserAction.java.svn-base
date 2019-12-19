
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
public class SmartAppUserAction extends Controller {
  
	//进入页面列表；
	public static Result view() {
		return ok( SmartAppUserList.render() );
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
		sql.append("find SmartAppUser where 1=1 ");
		
		String idd = StringUtil.getHttpParam(request(), "idd");
		if(idd==null)
			idd = "";	
		if ( !( idd .equals("") ) && !( idd .equals("undefined") ) ) 
			sql.append(" and ( idd= '" + idd + "'  )");
		
		String login = StringUtil.getHttpParam(request(), "login");
		if(login==null)
			login = "";	
		if ( !( login .equals("") ) && !( login .equals("undefined") ) ) 
			sql.append(" and ( login= '" + login + "'  )");
		
		String usertype = StringUtil.getHttpParam(request(), "usertype");
		if(usertype==null)
			usertype = "";	
		if ( !( usertype .equals("") ) && !( usertype .equals("undefined") ) ) 
			sql.append(" and ( usertype= '" + usertype + "'  )");
		
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
		
		String appid = StringUtil.getHttpParam(request(), "appid");
		if(appid==null)
			appid = "";	
		if ( !( appid .equals("") ) && !( appid .equals("undefined") ) ) 
			sql.append(" and ( appid= '" + appid + "'  )");
		
		String ip = StringUtil.getHttpParam(request(), "ip");
		if(ip==null)
			ip = "";	
		if ( !( ip .equals("") ) && !( ip .equals("undefined") ) ) 
			sql.append(" and ( ip= '" + ip + "'  )");
		
		String password = StringUtil.getHttpParam(request(), "password");
		if(password==null)
			password = "";	
		if ( !( password .equals("") ) && !( password .equals("undefined") ) ) 
			sql.append(" and ( password= '" + password + "'  )");
		
		String appversion = StringUtil.getHttpParam(request(), "appversion");
		if(appversion==null)
			appversion = "";	
		if ( !( appversion .equals("") ) && !( appversion .equals("undefined") ) ) 
			sql.append(" and ( appversion= '" + appversion + "'  )");
		
		String appos = StringUtil.getHttpParam(request(), "appos");
		if(appos==null)
			appos = "";	
		if ( !( appos .equals("") ) && !( appos .equals("undefined") ) ) 
			sql.append(" and ( appos= '" + appos + "'  )");
		
		String phonetype = StringUtil.getHttpParam(request(), "phonetype");
		if(phonetype==null)
			phonetype = "";	
		if ( !( phonetype .equals("") ) && !( phonetype .equals("undefined") ) ) 
			sql.append(" and ( phonetype= '" + phonetype + "'  )");
		
		String deviceid = StringUtil.getHttpParam(request(), "deviceid");
		if(deviceid==null)
			deviceid = "";	
		if ( !( deviceid .equals("") ) && !( deviceid .equals("undefined") ) ) 
			sql.append(" and ( deviceid= '" + deviceid + "'  )");
		
		String apptoken = StringUtil.getHttpParam(request(), "apptoken");
		if(apptoken==null)
			apptoken = "";	
		if ( !( apptoken .equals("") ) && !( apptoken .equals("undefined") ) ) 
			sql.append(" and ( apptoken= '" + apptoken + "'  )");
		
		String idfa = StringUtil.getHttpParam(request(), "idfa");
		if(idfa==null)
			idfa = "";	
		if ( !( idfa .equals("") ) && !( idfa .equals("undefined") ) ) 
			sql.append(" and ( idfa= '" + idfa + "'  )");
		
		String logo = StringUtil.getHttpParam(request(), "logo");
		if(logo==null)
			logo = "";	
		if ( !( logo .equals("") ) && !( logo .equals("undefined") ) ) 
			sql.append(" and ( logo= '" + logo + "'  )");
		
		String nickname = StringUtil.getHttpParam(request(), "nickname");
		if(nickname==null)
			nickname = "";	
		if ( !( nickname .equals("") ) && !( nickname .equals("undefined") ) ) 
			sql.append(" and ( nickname= '" + nickname + "'  )");
		
		String lastlogintime = StringUtil.getHttpParam(request(), "lastlogintime");
		if(lastlogintime==null)
			lastlogintime = "";	
		if ( !( lastlogintime .equals("") ) && !( lastlogintime .equals("undefined") ) ) 
			sql.append(" and ( lastlogintime= '" + lastlogintime + "'  )");
		
		String status = StringUtil.getHttpParam(request(), "status");
		if(status==null)
			status = "";	
		if ( !( status .equals("") ) && !( status .equals("undefined") ) && !( status .equals("1") )) 
			sql.append(" and ( status= '" + status + "'  )");
		
		String prov = StringUtil.getHttpParam(request(), "prov");
		if(prov==null)
			prov = "";	
		if ( !( prov .equals("") ) && !( prov .equals("undefined") ) ) 
			sql.append(" and ( prov= '" + prov + "'  )");
		
		String city = StringUtil.getHttpParam(request(), "city");
		if(city==null)
			city = "";	
		if ( !( city .equals("") ) && !( city .equals("undefined") ) ) 
			sql.append(" and ( city= '" + city + "'  )");
		
		String yys = StringUtil.getHttpParam(request(), "yys");
		if(yys==null)
			yys = "";	
		if ( !( yys .equals("") ) && !( yys .equals("undefined") ) ) 
			sql.append(" and ( yys= '" + yys + "'  )");
		
		String pushstate = StringUtil.getHttpParam(request(), "pushstate");
		if(pushstate==null)
			pushstate = "";	
		if ( !( pushstate .equals("") ) && !( pushstate .equals("undefined") ) ) 
			sql.append(" and ( pushstate= '" + pushstate + "'  )");
		
		String export = StringUtil.getHttpParam(request(), "export");// 是否导出
		if(export!=null && export.equals("1")){
			nStart = 0;
			nLimit = 65534;
		}
		
		Logger.info(sql.toString());
		//下面简单的分页，需要重构，应该由 Dao-Sevice去实现。
		int rowCount = Ebean.getServer(GlobalDBControl.getDB()).createQuery(SmartAppUser .class, sql.toString()).findRowCount();
		List<SmartAppUser> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.createQuery(SmartAppUser .class, sql.toString())
				.setFirstRow(nStart).setMaxRows(nLimit)
				.orderBy("idd desc")
				.findList();
				
		if(export!=null && export.equals("1")){
			String fileName = "SmartAppUser"+simp.format( new Date())+".xls";
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
		PagedObject<SmartAppUser> po= new PagedObject( ulist,nPages,rowCount );
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(po) );
	}

	//列表
	public static Result listAll(){
		List<SmartAppUser> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.find(SmartAppUser .class)
				.orderBy("idd")
				.findList();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(ulist) );
	}
	
	//删除
	public static Result delete(int idd){
		String sql = "delete from SmartAppUser where idd =:idd";
		Ebean.getServer(GlobalDBControl.getDB())
				.createUpdate(SmartAppUser .class, sql).setParameter("idd", idd).execute();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson("操作成功") );
	}
	
	//获取 单个
	public static Result get(int idd){
		SmartAppUser smartappuser = Ebean.getServer(GlobalDBControl.getDB())
				.find(SmartAppUser .class).where().eq("idd", idd).findUnique();
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(smartappuser) );
	}

	//新增 / 修改
	public static Result modify(){
		String idd = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "idd");
		int nidd = StringTool.GetInt(idd, 0);
		String login = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "login");
		String usertype = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "usertype");
		String phone = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "phone");
		String addtime = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "addtime");
		String appid = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "appid");
		String ip = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "ip");
		String password = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "password");
		String appversion = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "appversion");
		String appos = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "appos");
		String phonetype = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "phonetype");
		String deviceid = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "deviceid");
		String apptoken = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "apptoken");
		String idfa = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "idfa");
		String logo = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "logo");
		String nickname = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "nickname");
		String lastlogintime = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "lastlogintime");
		String status = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "status");
		String prov = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "prov");
		String city = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "city");
		String yys = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "yys");
		String pushstate = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "pushstate");
		String operation = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "operation");
		
		SmartAppUser smartappuser ;
		if(  operation.equals("add") )	// 新增
			smartappuser = new SmartAppUser ();
		else			//修改
			smartappuser = Ebean.getServer(GlobalDBControl.getDB())
					.find(SmartAppUser .class).where().eq("idd", idd).findUnique();
		
		if( smartappuser!=null ){
			//赋值,字符串类型可以直接赋值，其他类型需要转换 ！！！！
			smartappuser. setUsertype ( StringTool.GetInt(usertype,0) );
		
			smartappuser. setPhone ( phone );
		
			smartappuser. setAddtime ( StringUtil.getDate(addtime) );
		
			smartappuser. setAppid ( appid );
		
			smartappuser. setIp ( ip );
		
			smartappuser. setPassword ( password );
		
			smartappuser. setAppversion ( appversion );
		
			smartappuser. setAppos ( appos );
		
			smartappuser. setPhonetype ( phonetype );
		
			smartappuser. setDeviceid ( deviceid );
		
			smartappuser. setApptoken ( apptoken );
		
			smartappuser. setIdfa ( idfa );
		
			smartappuser. setLogo ( logo );
		
			smartappuser. setNickname ( nickname );
		
			smartappuser. setLastlogintime ( StringUtil.getDate(lastlogintime) );
			
			smartappuser. setStatus ( StringTool.GetInt(status,0) );
		
			smartappuser. setProv ( prov );
		
			smartappuser. setCity ( city );
		
			smartappuser. setYys ( yys );
		
			smartappuser. setPushstate ( StringTool.GetInt(pushstate,0) );
				
			Ebean.getServer(GlobalDBControl.getDB()).save( smartappuser );
			response().setHeader("Cache-Control", "no-cache");
			return ok( Json.toJson(smartappuser) );
		}
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(null) );
	}
	
	static int num=0;
	public static File exportList(List<SmartAppUser> infoList,String fileNameChine) throws UnsupportedEncodingException {
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
		
		ExcelGenerateHelper helper = ExcelGenerateHelper.getInstance(wb);

		// 获取行索引
		int rowIndex = 0;
		int colIndex = 0;

		// 设置表头
		String[] titles = { 
				
			"ID"
			
			,"登录名"
			
			,"用户来源"
			
			,"手机"
			
			,"注册时间"
			
			,"APPID"
			
			,"IP"
			
			,"密码"
			
			,"APP版本"
			
			,"手机系统"
			
			,"机型"
			
			,"设备ID"
			
			,"苹果ID"
		
			,"广告ID"
			
			,"头像"
			
			,"昵称"
		
			,"上次登录时间"
			
			,"状态"
			
			,"省"
			
			,"市"
			
			,"运营商"
			
			,"推送开关"
		};
		// 生成表头
		helper.setRowIndex(rowIndex++);
		helper.generateHeader(sheet, titles, 0, StringUtils.EMPTY,
				StringUtils.EMPTY);

		// 循环生成数据行
		for (int i = 0; i < infoList.size(); i++) {
			colIndex = 0;
			SmartAppUser info = (SmartAppUser) infoList.get(i);
			Row row = sheet.createRow(rowIndex++);
			
			helper.createStringCell( row , colIndex++, ""+info. getIdd () );
		
			helper.createStringCell( row , colIndex++, ""+info. getLogin () );
		
			helper.createStringCell( row , colIndex++, ""+info. getUsertype () );
		
			helper.createStringCell( row , colIndex++, ""+info. getPhone () );
		
			helper.createStringCell( row , colIndex++, ""+simp1.format(info. getAddtime ()) );
		
			helper.createStringCell( row , colIndex++, ""+info. getAppid () );
		
			helper.createStringCell( row , colIndex++, ""+info. getIp () );
		
			helper.createStringCell( row , colIndex++, ""+info. getPassword () );
		
			helper.createStringCell( row , colIndex++, ""+info. getAppversion () );
		
			helper.createStringCell( row , colIndex++, ""+info. getAppos () );
		
			helper.createStringCell( row , colIndex++, ""+info. getPhonetype () );
		
			helper.createStringCell( row , colIndex++, ""+info. getDeviceid () );
		
			helper.createStringCell( row , colIndex++, ""+info. getApptoken () );
		
			helper.createStringCell( row , colIndex++, ""+info. getIdfa () );
		
			helper.createStringCell( row , colIndex++, ""+info. getLogo () );
		
			helper.createStringCell( row , colIndex++, ""+info. getNickname () );
		
			helper.createStringCell( row , colIndex++, ""+simp1.format(info. getLastlogintime ()) );
		
			helper.createStringCell( row , colIndex++, ""+info. getStatus () );
		
			helper.createStringCell( row , colIndex++, ""+info. getProv () );
		
			helper.createStringCell( row , colIndex++, ""+info. getCity () );
		
			helper.createStringCell( row , colIndex++, ""+info. getYys () );
		
			helper.createStringCell( row , colIndex++, ""+info. getPushstate () );
		}
		//在application.conf中配置的路径
		String path = Configuration.root().getString("export.path");
		int numStra = num++;//静态值，区分文件名
		File file = new File(path);
		file.mkdir();//判断文件夹是否存在，不存在就创建
		
		FileOutputStream out = null;
		String fileName = path + "SmartAppUser" + System.currentTimeMillis() + numStra + ".xls";
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
		"登录名"
		
		,"用户来源"
		
		,"手机"
	
		,"注册时间"
		
		,"APPID"
		
		,"IP"
		
		,"密码"
		
		,"APP版本"
	
		,"手机系统"
		
		,"机型"
		
		,"设备ID"
		
		,"苹果ID"
		
		,"广告ID"
	
		,"头像"
		
		,"昵称"
		
		,"上次登录时间"
		
		,"状态"
		
		,"省"
		
		,"市"
		
		,"运营商"
	
		,"推送开关"
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
			
			List<SmartAppUser> list = new ArrayList<SmartAppUser>();
			 for (int i = 1; i <= lastRowNumber; i++) {
				 //批量添加
                 Row row = sheet.getRow(i);
                 if (row == null) {
                     break;
                 }
                 
                 SmartAppUser fc = new SmartAppUser();
                 int j = 0 ;
						fc. setLogin ( StringUtil.cellValueToString(row.getCell(j)) );
					
						j=j+1;
						fc. setUsertype ( StringTool.GetInt(StringUtil.cellValueToString(row.getCell(j)),0) );
					
						j=j+1;
						fc. setPhone ( StringUtil.cellValueToString(row.getCell(j)) );
					
						j=j+1;
						//fc. setAddtime ( StringUtil.getDate( StringUtil.cellValueToString(row.getCell(j) ) );
					
						j=j+1;
						fc. setAppid ( StringUtil.cellValueToString(row.getCell(j)) );
					 
						j=j+1;
						fc. setIp ( StringUtil.cellValueToString(row.getCell(j)) );
					
						j=j+1;
						fc. setPassword ( StringUtil.cellValueToString(row.getCell(j)) );
					
						j=j+1;
						fc. setAppversion ( StringUtil.cellValueToString(row.getCell(j)) );
					
						j=j+1;
						fc. setAppos ( StringUtil.cellValueToString(row.getCell(j)) );
						
						j=j+1;
						fc. setPhonetype ( StringUtil.cellValueToString(row.getCell(j)) );
					
						j=j+1;
						fc. setDeviceid ( StringUtil.cellValueToString(row.getCell(j)) );
					 
						j=j+1;
						fc. setApptoken ( StringUtil.cellValueToString(row.getCell(j)) );
					
						j=j+1;
						fc. setIdfa ( StringUtil.cellValueToString(row.getCell(j)) );
						
						j=j+1;
						fc. setLogo ( StringUtil.cellValueToString(row.getCell(j)) );
					
						j=j+1;
						fc. setNickname ( StringUtil.cellValueToString(row.getCell(j)) );
					
						j=j+1;
						//fc. setLastlogintime ( StringUtil.getDate( StringUtil.cellValueToString(row.getCell(j) ) );
					
						j=j+1;
						fc. setStatus ( StringTool.GetInt(StringUtil.cellValueToString(row.getCell(j)),0) );
					
					
						j=j+1;
						fc. setProv ( StringUtil.cellValueToString(row.getCell(j)) );
					
						j=j+1;
						fc. setCity ( StringUtil.cellValueToString(row.getCell(j)) );
					
						j=j+1;
						fc. setYys ( StringUtil.cellValueToString(row.getCell(j)) );
					 
						j=j+1;
						fc. setPushstate ( StringTool.GetInt(StringUtil.cellValueToString(row.getCell(j)),0) );
				
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
	}
	
}