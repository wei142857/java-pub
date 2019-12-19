package controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import models.SmartAppUserExtend;
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
import views.html.page.*;
import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlRow;

@Security.Authenticated(Secured.class)
public class SmartAppUserExtendAction extends Controller {
  
	//进入页面列表；
	public static Result view() {
		return ok( SmartAppUserExtendList.render() );
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
		sql.append("SELECT * FROM (SELECT u.idd AS userid,u.phone,u.nickname,v.overtime,SUM(bv.currentbvalue) AS bvalue,o.shareBvalue FROM smart_app_user u LEFT JOIN (SELECT b.userid,b.currentbvalue FROM smart_bounty b WHERE b.act = 1 and b.currentbvalue>0) bv ON u.idd = bv.userid LEFT JOIN smart_vip v ON u.idd = v.userid LEFT JOIN (SELECT source,shareBvalue FROM eshop_orders WHERE userid != source AND expressstatus = 0 AND status = 0) o ON u.idd = o.source GROUP BY u.idd) s WHERE 1 = 1");
		sql1.append("SELECT count(1) AS num FROM (SELECT u.idd AS userid,u.phone,u.nickname,v.overtime,SUM(bv.currentbvalue) AS bvalue,o.shareBvalue FROM smart_app_user u LEFT JOIN (SELECT b.userid,b.currentbvalue FROM smart_bounty b WHERE b.act = 1 and b.currentbvalue>0) bv ON u.idd = bv.userid LEFT JOIN smart_vip v ON u.idd = v.userid LEFT JOIN (SELECT source,shareBvalue FROM eshop_orders WHERE userid != source AND expressstatus = 0 AND status = 0) o ON u.idd = o.source GROUP BY u.idd) s WHERE 1 = 1");
		
		//构造联合查询条件
		String userid = StringUtil.getHttpParam(request(),"userid");
		if(userid==null)
			userid = "";	
		if (!( userid .equals("")) && !(userid .equals("undefined"))) 
			sql.append(" and(userid='" + userid + "')");
			sql1.append(" and(userid='" + userid + "')");
		
		String phone = StringUtil.getHttpParam(request(),"phone");
		if(phone==null)
			phone = "";
		if (!( phone .equals("")) && !( phone .equals("undefined") ) ) 
			sql.append(" and(phone='"+phone+"')");
			sql1.append(" and(phone='"+phone+"')");
		
		String nickname = StringUtil.getHttpParam(request(), "nickname");
		if(nickname == null)
			nickname = "";
		if(!(nickname.equals(""))&&!(nickname.equals("undefined")))
			sql.append(" and(nickname='"+nickname+"')");
			sql1.append(" and(nickname='"+nickname+"')");
			
		String overtime = StringUtil.getHttpParam(request(), "overtime");
		if(overtime ==null)
			overtime = "";
		if(!(overtime.equals(""))&&!(overtime.equals("undefined")))
			sql.append(" and(overtime='"+overtime+"')");
			sql1.append(" and(overtime='"+overtime+"')");
				
		String bvalue = StringUtil.getHttpParam(request(), "bvalue");
		if(bvalue ==null)
			bvalue = "";
		if(!(bvalue.equals(""))&&!(bvalue.equals("undefined")))
			sql.append(" and(bvalue='"+bvalue+"')");
			sql1.append(" and(bvalue='"+bvalue+"')");
			
		String shareBvalue = StringUtil.getHttpParam(request(), "shareBvalue");
		if(shareBvalue ==null)
			shareBvalue = "";
		if(!(shareBvalue.equals(""))&&!(shareBvalue.equals("undefined")))
			sql.append(" and(shareBvalue='"+shareBvalue+"')");
			sql1.append(" and(shareBvalue='"+shareBvalue+"')");
			
		String export = StringUtil.getHttpParam(request(), "export");// 是否导出
		if(export!=null && export.equals("1")){
			nStart = 0;
			nLimit = 65534;
		}
		
		Logger.info(sql.toString());
		//执行查询
		List<SqlRow> sqlRows = Ebean.getServer(GlobalDBControl.getDB()).createSqlQuery(sql.toString()).findList();
		//4.将sqlrow转为 自定义实体类
		List<SmartAppUserExtend> ulist = EntityConvert.convert(sqlRows, SmartAppUserExtend.class);
		
		SqlRow sqlRow = Ebean.getServer(GlobalDBControl.getDB()).createSqlQuery(sql1.toString()).findUnique(); 
		int rowCount = sqlRow.getInteger("num");
				
		if(export!=null && export.equals("1")){
			String fileName = "用户扩展信息"+simp.format( new Date())+".xls";
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
		PagedObject<SmartAppUserExtend> po= new PagedObject( ulist,nPages,rowCount );
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(po) );
	}

	//列表
	public static Result listAll(){
		String sql = "SELECT u.idd,u.phone,u.nickname,v.overtime,SUM(bv.currentbvalue),o.shareBvalue FROM smart_app_user u LEFT JOIN (SELECT b.userid,b.currentbvalue FROM smart_bounty b WHERE b.act = 1 and b.currentbvalue>0) bv ON u.idd = bv.userid LEFT JOIN smart_vip v ON u.idd = v.userid LEFT JOIN (SELECT source,shareBvalue FROM eshop_orders WHERE userid != source AND expressstatus = 0 AND status = 0) o ON u.idd = o.source WHERE 1=1 GROUP BY u.idd";
		List<SqlRow> sqlRows = Ebean.getServer(GlobalDBControl.getDB()).createSqlQuery(sql.toString()).findList();
		List<SmartAppUserExtend> ulist = EntityConvert.convert(sqlRows, SmartAppUserExtend.class);
		return ok( Json.toJson(ulist) );
	}
	
	static int num=0;
	public static File exportList(List<SmartAppUserExtend> infoList,String fileNameChine) throws UnsupportedEncodingException {
		Workbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet("sheet1");
		
			sheet.setColumnWidth( 0 , 6000 );
		
			sheet.setColumnWidth( 1 , 6000 );
		
			sheet.setColumnWidth( 2 , 6000 );
		
			sheet.setColumnWidth( 3 , 6000 );
			
			sheet.setColumnWidth( 4 , 6000 );
			
			sheet.setColumnWidth( 5 , 6000 );
			
			sheet.setColumnWidth( 6 , 6000 );
		
		ExcelGenerateHelper helper = ExcelGenerateHelper.getInstance(wb);

		// 获取行索引
		int rowIndex = 0;
		int colIndex = 0;

		// 设置表头
		String[] titles = { 
			"用户ID"
		
			,"手机号"
			
			,"用户昵称"
			
			,"会员状态"
			
			,"会员到期时间"
			
			,"剩余奖励金"
			
			,"分享得到的奖励金"
		};

		// 生成表头
		helper.setRowIndex(rowIndex++);
		helper.generateHeader(sheet, titles, 0, StringUtils.EMPTY,
				StringUtils.EMPTY);

		// 循环生成数据行
		for (int i = 0; i < infoList.size(); i++) {
			colIndex = 0;
			SmartAppUserExtend info = (SmartAppUserExtend) infoList.get(i);
			Row row = sheet.createRow(rowIndex++);
			helper.createStringCell( row , colIndex++, ""+info.getUserid() );
		
			helper.createStringCell( row , colIndex++, ""+info.getPhone() );
		
			helper.createStringCell( row , colIndex++, ""+info.getNickname() );
		
			helper.createStringCell( row , colIndex++, ""+info. getOvertime());
			
			helper.createStringCell( row , colIndex++, ""+info.getVipStatus() );
			
			helper.createStringCell( row , colIndex++, ""+info.getBvalue() );
			
			helper.createStringCell( row , colIndex++, ""+info.getShareBvalue() );
		}
		//在application.conf中配置的路径
		String path = Configuration.root().getString("export.path");
		int numStra = num++;//静态值，区分文件名
		File file = new File(path);
		file.mkdir();//判断文件夹是否存在，不存在就创建
		
		FileOutputStream out = null;
		String fileName = path + "SmartAppUserExtend" + System.currentTimeMillis() + numStra + ".xls";
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