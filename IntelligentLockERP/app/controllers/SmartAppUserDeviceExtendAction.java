package controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import models.SmartAppUserDeviceExtend;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import play.Configuration;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import util.EntityConvert;
import util.ExcelGenerateHelper;
import util.GlobalDBControl;
import util.PagedObject;
import util.StringUtil;
import util.classEntity.StringTool;
import views.html.page.SmartAppUserDeviceExtendList;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlRow;

@Security.Authenticated(Secured.class)
public class SmartAppUserDeviceExtendAction extends Controller {
  
	//进入页面列表；
	public static Result view() {
		return ok( SmartAppUserDeviceExtendList.render() );
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
		StringBuffer sql1 = new StringBuffer(); 
		//执行联合查询：
		sql.append("SELECT ud.deviceid as deviceidd,ud.deviceAppUser as userid,u.phone,d.deviceid,ud.addtime FROM smart_app_user_device ud JOIN smart_app_user u on u.idd = ud.deviceAppUser JOIN smart_device d on ud.deviceid = d.idd WHERE 1 = 1");
		sql1.append("SELECT count(1) num FROM smart_app_user_device ud JOIN smart_app_user u on u.idd = ud.deviceAppUser JOIN smart_device d on ud.deviceid = d.idd WHERE 1 = 1");
		
		//构造联合查询条件
		String deviceidd = StringUtil.getHttpParam(request(),"deviceidd");
		if(deviceidd==null)
			deviceidd = "";	
		if (!( deviceidd .equals("")) && !(deviceidd .equals("undefined"))) 
			sql.append(" and(ud.deviceid='" + deviceidd +"')");
			sql1.append(" and(ud.deviceid='" + deviceidd +"')");
		
		String userid = StringUtil.getHttpParam(request(),"userid");
		if(userid==null)
			userid = "";
		if (!( userid .equals("")) && !( userid .equals("undefined") ) ) 
			sql.append(" and(ud.deviceAppUser='"+userid+"')");
			sql1.append(" and(ud.deviceAppUser='"+userid+"')");
			
		String phone = StringUtil.getHttpParam(request(),"phone");
		if(phone==null)
			phone = "";
		if (!( phone .equals("")) && !( phone .equals("undefined") ) ) 
			sql.append(" and(u.phone='"+phone+"')");
			sql1.append(" and(u.phone='"+phone+"')");
		
		String deviceid = StringUtil.getHttpParam(request(), "deviceid");
		if(deviceid == null)
			deviceid = "";
		if(!(deviceid.equals(""))&&!(deviceid.equals("undefined")))
			sql.append(" and(d.deviceid='"+deviceid+"')");
			sql1.append(" and(d.deviceid='"+deviceid+"')");
		
		String addtime = StringUtil.getHttpParam(request(), "addtime");
		if(addtime ==null)
			addtime = "";
		if(!(addtime.equals(""))&&!(addtime.equals("undefined")))
			sql.append(" and(ud.addtime='"+addtime+"')");
			sql1.append(" and(ud.addtime='"+addtime+"')");
			
		String export = StringUtil.getHttpParam(request(), "export");// 是否导出
		if(export!=null && export.equals("1")){
			nStart = 0;
			nLimit = 65534;
		}
		
		Logger.info(sql.toString());
		//执行查询
		List<SqlRow> sqlRows = Ebean.getServer(GlobalDBControl.getDB()).createSqlQuery(sql.toString()).findList();
		//4.将sqlrow转为 自定义实体类
		List<SmartAppUserDeviceExtend> ulist = EntityConvert.convert(sqlRows, SmartAppUserDeviceExtend.class);
		
		SqlRow sqlRow = Ebean.getServer(GlobalDBControl.getDB()).createSqlQuery(sql1.toString()).findUnique(); 
		int rowCount = sqlRow.getInteger("num");
				
		if(export!=null && export.equals("1")){
			String fileName = "SmartAppUserDeviceExtend"+simp.format( new Date())+".xls";
			Logger.info(ulist.size()+"size++++++++++++++++++++++++"+fileName);
			File file=null;
			try{
				file= exportList(ulist,fileName);
			}catch(Exception e){
				Logger.info("export file:"+e.toString());
			}
			return ok(file);
		}		
		int nPages = rowCount/nLimit;
		if( rowCount%nLimit>0 )
			nPages ++;
		PagedObject<SmartAppUserDeviceExtend> po= new PagedObject( ulist,nPages,rowCount );
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(po) );
	}

	//列表
	public static Result listAll(){
		String sql = "SELECT ud.deviceid as deviceidd,ud.deviceAppUser as userid,u.phone,d.deviceid,ud.addtime FROM smart_app_user_device ud JOIN smart_app_user u on u.idd = ud.deviceAppUser JOIN smart_device d on ud.deviceid = d.idd";
		List<SqlRow> sqlRows = Ebean.getServer(GlobalDBControl.getDB()).createSqlQuery(sql.toString()).findList();
		List<SmartAppUserDeviceExtend> ulist = EntityConvert.convert(sqlRows, SmartAppUserDeviceExtend.class);
		return ok( Json.toJson(ulist) );
	}
	
	static int num=0;
	public static File exportList(List<SmartAppUserDeviceExtend> infoList,String fileNameChine) throws UnsupportedEncodingException {
		Workbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet("sheet1");
		
			sheet.setColumnWidth( 0 , 6000 );
		
			sheet.setColumnWidth( 1 , 6000 );
		
			sheet.setColumnWidth( 2 , 6000 );
		
			sheet.setColumnWidth( 3 , 6000 );
			
			sheet.setColumnWidth( 4 , 6000 );
		
		ExcelGenerateHelper helper = ExcelGenerateHelper.getInstance(wb);

		// 获取行索引
		int rowIndex = 0;
		int colIndex = 0;

		// 设置表头
		String[] titles = { 
			"设备表ID"
		
			,"用户表ID"
			
			,"用户手机号"
			
			,"设备号"
			
			,"绑定设备时间"
		};

		// 生成表头
		helper.setRowIndex(rowIndex++);
		helper.generateHeader(sheet, titles, 0, StringUtils.EMPTY,
				StringUtils.EMPTY);

		// 循环生成数据行
		for (int i = 0; i < infoList.size(); i++) {
			colIndex = 0;
			SmartAppUserDeviceExtend info = (SmartAppUserDeviceExtend) infoList.get(i);
			Row row = sheet.createRow(rowIndex++);
			helper.createStringCell( row , colIndex++, ""+info. getDeviceidd() );
		
			helper.createStringCell( row , colIndex++, ""+info. getUserid() );
		
			helper.createStringCell( row , colIndex++, ""+info. getPhone() );
		
			helper.createStringCell( row , colIndex++, ""+info. getDeviceid() );
			
			helper.createStringCell( row , colIndex++, ""+simp.format(info. getAddtime()));
		}
		//在application.conf中配置的路径
		String path = Configuration.root().getString("export.path");
		int numStra = num++;//静态值，区分文件名
		File file = new File(path);
		file.mkdir();//判断文件夹是否存在，不存在就创建
		
		FileOutputStream out = null;
		String fileName = path + "SmartAppUserDeviceExtend" + System.currentTimeMillis() + numStra + ".xls";
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