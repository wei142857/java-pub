package controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import models.IntelligentLockOutDetailCode;
import models.IntelligentLockOutDetailCodeExtend;

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
import views.html.page.IntelligentLockOutDetailCodeExtendList;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlRow;

@Security.Authenticated(Secured.class)
public class IntelligentLockOutDetailCodeExtendAction extends Controller {
  
	//进入页面列表；
	public static Result view() {
		return ok( IntelligentLockOutDetailCodeExtendList.render() );
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
		sql.append("select dc.outdetailid,dc.codeid,pc.deviceid,pc.code from intelligentlock_out_detail_code dc join intelligentlock_product_code pc on dc.codeid = pc.idd where 1 = 1");
		sql1.append("select count(1) num from intelligentlock_out_detail_code dc join intelligentlock_product_code pc on dc.codeid = pc.idd where 1 = 1");
		
		//构造联合查询条件
		String outdetailid = StringUtil.getHttpParam(request(),"outdetailid");
		if(outdetailid==null)
			outdetailid = "";	
		if (!( outdetailid .equals("")) && !(outdetailid .equals("undefined"))) 
			sql.append(" and(dc.outdetailid='" + outdetailid + "')");
			sql1.append(" and(dc.outdetailid='" + outdetailid + "')");
		
		String codeid = StringUtil.getHttpParam(request(),"codeid");
		if(codeid==null)
			codeid = "";
		if (!( codeid .equals("")) && !( codeid .equals("undefined") ) ) 
			sql.append(" and(dc.codeid='"+codeid+"')");
			sql1.append(" and(dc.codeid='"+codeid+"')");
		
		String deviceid = StringUtil.getHttpParam(request(), "deviceid");
		if(deviceid == null)
			deviceid = "";
		if(!(deviceid.equals(""))&&!(deviceid.equals("undefined")))
			sql.append(" and(pc.deviceid='"+deviceid+"')");
			sql1.append(" and(pc.deviceid='"+deviceid+"')");
		
		String code = StringUtil.getHttpParam(request(), "code");
		if(code ==null)
			code = "";
		if(!(code.equals(""))&&!(code.equals("undefined")))
			sql.append(" and(pc.code='"+code+"')");
			sql1.append(" and(pc.code='"+code+"')");
			
		String export = StringUtil.getHttpParam(request(), "export");// 是否导出
		if(export!=null && export.equals("1")){
			nStart = 0;
			nLimit = 65534;
		}
		
		Logger.info(sql.toString());
		//执行查询
		List<SqlRow> sqlRows = Ebean.getServer(GlobalDBControl.getDB()).createSqlQuery(sql.toString()).findList();
		//4.将sqlrow转为 自定义实体类
		List<IntelligentLockOutDetailCodeExtend> ulist = EntityConvert.convert(sqlRows, IntelligentLockOutDetailCodeExtend.class);
		
		SqlRow sqlRow = Ebean.getServer(GlobalDBControl.getDB()).createSqlQuery(sql1.toString()).findUnique(); 
		int rowCount = sqlRow.getInteger("num");
				
		if(export!=null && export.equals("1")){
			String fileName = "IntelligentLockOutDetailCodeExtend"+simp.format( new Date())+".xls";
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
		PagedObject<IntelligentLockOutDetailCodeExtend> po= new PagedObject( ulist,nPages,rowCount );
		response().setHeader("Cache-Control", "no-cache");
		return ok( Json.toJson(po) );
	}

	//列表
	public static Result listAll(){
		String sql = "select dc.outdetailid,dc.codeid,pc.deviceid,pc.code from intelligentlock_out_detail_code dc join intelligentlock_product_code pc on dc.codeid = pc.idd";
		List<SqlRow> sqlRows = Ebean.getServer(GlobalDBControl.getDB()).createSqlQuery(sql.toString()).findList();
		List<IntelligentLockOutDetailCodeExtend> ulist = EntityConvert.convert(sqlRows, IntelligentLockOutDetailCodeExtend.class);
		return ok( Json.toJson(ulist) );
	}
	
	static int num=0;
	public static File exportList(List<IntelligentLockOutDetailCodeExtend> infoList,String fileNameChine) throws UnsupportedEncodingException {
		Workbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet("sheet1");
		
			sheet.setColumnWidth( 0 , 6000 );
		
			sheet.setColumnWidth( 1 , 6000 );
		
			sheet.setColumnWidth( 2 , 6000 );
		
			sheet.setColumnWidth( 3 , 6000 );
		
		ExcelGenerateHelper helper = ExcelGenerateHelper.getInstance(wb);

		// 获取行索引
		int rowIndex = 0;
		int colIndex = 0;

		// 设置表头
		String[] titles = { 
			"出库明细ID"
		
			,"产品编码ID"
			
			,"设备号"
			
			,"产品编码"
		};

		// 生成表头
		helper.setRowIndex(rowIndex++);
		helper.generateHeader(sheet, titles, 0, StringUtils.EMPTY,
				StringUtils.EMPTY);

		// 循环生成数据行
		for (int i = 0; i < infoList.size(); i++) {
			colIndex = 0;
			IntelligentLockOutDetailCodeExtend info = (IntelligentLockOutDetailCodeExtend) infoList.get(i);
			Row row = sheet.createRow(rowIndex++);
			helper.createStringCell( row , colIndex++, ""+info. getOutdetailid () );
		
			helper.createStringCell( row , colIndex++, ""+info. getCodeid () );
		
			helper.createStringCell( row , colIndex++, ""+info. getDeviceid() );
		
			helper.createStringCell( row , colIndex++, ""+info. getCode() );
		}
		//在application.conf中配置的路径
		String path = Configuration.root().getString("export.path");
		int numStra = num++;//静态值，区分文件名
		File file = new File(path);
		file.mkdir();//判断文件夹是否存在，不存在就创建
		
		FileOutputStream out = null;
		String fileName = path + "IntelligentLockOutDetailCodeExtend" + System.currentTimeMillis() + numStra + ".xls";
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