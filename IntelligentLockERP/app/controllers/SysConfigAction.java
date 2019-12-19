package controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import models.SysConfig;

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
import util.AjaxHellper;
import util.ExcelGenerateHelper;
import util.GlobalDBControl;
import util.PagedObject;
import util.StringUtil;
import util.classEntity.StringTool;
import views.html.page.SysConfigList;
import views.html.page.SysConfigListSpecial;
import Service.cache.ICacheService;

import com.avaje.ebean.Ebean;

@Security.Authenticated(Secured.class)
public class SysConfigAction extends Controller {

	// 进入页面列表；
	public static Result view() {
		String dataGrp = StringUtil.getHttpParam(request(), "data");
		
		if( !StringTool.isNull(dataGrp) )
			return ok( SysConfigListSpecial.render(dataGrp) );
		
		return ok(SysConfigList.render());
	}
	

	/**************************************************************
	 * 增删改查的功能 ： AJAX方式
	 */
	private static SimpleDateFormat simp = new SimpleDateFormat("yyyy-MM-dd");

	// 列表
	public static Result list() {
		// 获取前端grid 传递过来的参数：分页，搜索
		String start = StringUtil.getHttpParam(request(), "start");
		String limit = StringUtil.getHttpParam(request(), "limit");
		int nStart = StringTool.GetInt(start, 0);
		int nLimit = StringTool.GetInt(limit, 10);

		StringBuffer sql = new StringBuffer();
		sql.append("find SysConfig where 1=1 ");

		String idd = StringUtil.getHttpParam(request(), "idd");
		if (idd == null)
			idd = "";
		if (!(idd.equals("")) && !(idd.equals("undefined")))
			sql.append(" and ( idd= '" + idd + "'  )");

		String datagroup = StringUtil.getHttpParam(request(), "datagroup");
		if (datagroup == null)
			datagroup = "";
		if (!(datagroup.equals("")) && !(datagroup.equals("undefined")))
			sql.append(" and ( datagroup like '%" + datagroup + "%'  )");

		String dataitem = StringUtil.getHttpParam(request(), "dataitem");
		if (dataitem == null)
			dataitem = "";
		if (!(dataitem.equals("")) && !(dataitem.equals("undefined")))
			sql.append(" and ( dataitem like '%" + dataitem + "%'  )");

		String datavalue = StringUtil.getHttpParam(request(), "datavalue");
		if (datavalue == null)
			datavalue = "";
		if (!(datavalue.equals("")) && !(datavalue.equals("undefined")))
			sql.append(" and ( datavalue= '" + datavalue + "'  )");

		String addtime = StringUtil.getHttpParam(request(), "addtime");
		if (addtime == null)
			addtime = "";
		if (!(addtime.equals("")) && !(addtime.equals("undefined")))
			sql.append(" and ( addtime= '" + addtime + "'  )");

		String export = StringUtil.getHttpParam(request(), "export");// 是否导出
		if (export != null && export.equals("1")) {
			nStart = 0;
			nLimit = 65534;
		}

		Logger.info(sql.toString());
		// 下面简单的分页，需要重构，应该由 Dao-Sevice去实现。
		int rowCount = Ebean.getServer(GlobalDBControl.getDB())
				.createQuery(SysConfig.class, sql.toString()).findRowCount();
		List<SysConfig> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.createQuery(SysConfig.class, sql.toString())
				.setFirstRow(nStart).setMaxRows(nLimit).orderBy("idd desc")
				.findList();

		if (export != null && export.equals("1")) {
			String fileName = "SysConfig" + simp.format(new Date()) + ".xls";
			Logger.info(ulist.size() + "size++++++++++++++++++++++++"
					+ fileName);
			File file = null;
			try {
				file = exportList(ulist, fileName);
			} catch (Exception e) {
				Logger.info("export file:" + e.toString());
			}

			return ok(file);
		}

		int nPages = rowCount / nLimit;
		if (rowCount % nLimit > 0)
			nPages++;
		PagedObject<SysConfig> po = new PagedObject<SysConfig>(ulist, nPages, rowCount);
		response().setHeader("Cache-Control", "no-cache");
		return ok(Json.toJson(po));
	}
	

	// 列表
	public static Result listAll() {
		List<SysConfig> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.find(SysConfig.class).orderBy("idd").findList();
		response().setHeader("Cache-Control", "no-cache");
		return ok(Json.toJson(ulist));
	}

	// 删除
	public static Result delete(int idd) {
		String sql = "delete from SysConfig where idd =:idd";
		Ebean.getServer(GlobalDBControl.getDB())
				.createUpdate(SysConfig.class, sql).setParameter("idd", idd)
				.execute();
		response().setHeader("Cache-Control", "no-cache");
		return ok(Json.toJson("操作成功"));
	}

	// 获取 单个
	public static Result get(int idd) {
		SysConfig sysconfig = Ebean.getServer(GlobalDBControl.getDB())
				.find(SysConfig.class).where().eq("idd", idd).findUnique();
		response().setHeader("Cache-Control", "no-cache");
		return ok(Json.toJson(sysconfig));
	}

	// 新增 / 修改
	public static Result modify() {
		String idd = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "idd");
		int nidd = StringTool.GetInt(idd, 0);

		String datagroup = AjaxHellper.getHttpParamOfFormUrlEncoded(request(),
				"datagroup");

		String dataitem = AjaxHellper.getHttpParamOfFormUrlEncoded(request(),
				"dataitem");

		String datavalue = AjaxHellper.getHttpParamOfFormUrlEncoded(request(),
				"datavalue");
		
		String remark = AjaxHellper.getHttpParamOfFormUrlEncoded(request(),
				"remark");
		
		if( datavalue==null )
			datavalue = "";
		
		datavalue = datavalue.trim();

		String addtime = AjaxHellper.getHttpParamOfFormUrlEncoded(request(),
				"addtime");

		String operation = AjaxHellper.getHttpParamOfFormUrlEncoded(request(),
				"operation");

		SysConfig sysconfig;
		if (operation.equals("add")) // 新增
			sysconfig = new SysConfig();
		else
			// 修改
			sysconfig = Ebean.getServer(GlobalDBControl.getDB())
					.find(SysConfig.class).where().eq("idd", idd).findUnique();

		if (sysconfig != null) {
			// 赋值,字符串类型可以直接赋值，其他类型需要转换 ！！！！

			sysconfig.setDatagroup(datagroup);

			if( datagroup.equals("微信关键字回复") )
				if( dataitem.charAt(dataitem.length()-1) !='、')
					dataitem = dataitem+"、";
			
			sysconfig.setDataitem(dataitem);

			sysconfig.setDatavalue(datavalue);
			sysconfig.setRemark(remark);

			sysconfig.setAddtime(new Date());

			Ebean.getServer(GlobalDBControl.getDB()).save(sysconfig);
			
			//Empty cache
			String key = ICacheService.Cache_String_Config 
					+ datagroup + dataitem ;
//			CacheMem.instance.clear(key);
			util.jedis.RedisUtil.getInstance().deleteEntity(key);
			
			response().setHeader("Cache-Control", "no-cache");
			return ok(Json.toJson(sysconfig));
		}

		response().setHeader("Cache-Control", "no-cache");
		return ok(Json.toJson(null));
	}
	
	/**
	 * 修改配置信息
	 * @param group
	 * @param item
	 * @param value
	 * @return
	 */
	public static boolean updateBygroupAnditem(String group,String item,String value){
		
		String updateSql = "update sys_config set datavalue='"+ value +"' where datagroup = '"+ group +"' and dataitem='"+ item +"'";

		int execute = Ebean.getServer(GlobalDBControl.getDB()).createSqlUpdate(updateSql).execute();
		if(execute > 0)
			CleanConfig(group,item);
		
		return execute>0 ? true:false;
	}
	
	/**
	 * 清除缓存
	 * @param group
	 * @param item
	 */
	public static void CleanConfig(String group,String item){
		//Empty cache
		String key = ICacheService.Cache_String_Config  + group + item ;
		util.jedis.RedisUtil.getInstance().deleteEntity(key);
//		CacheMem.instance.clear(key);
	}

	static int num = 0;

	public static File exportList(List<SysConfig> infoList, String fileNameChine)
			throws UnsupportedEncodingException {
		Workbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet("sheet1");

		sheet.setColumnWidth(0, 6000);

		sheet.setColumnWidth(1, 6000);

		sheet.setColumnWidth(2, 6000);

		sheet.setColumnWidth(3, 6000);

		sheet.setColumnWidth(4, 6000);

		ExcelGenerateHelper helper = ExcelGenerateHelper.getInstance(wb);

		// 获取行索引
		int rowIndex = 0;
		int colIndex = 0;

		// 设置表头
		String[] titles = {

		"idd"

		, "datagroup"

		, "dataitem"

		, "datavalue"

		, "addtime"

		};

		// 生成表头
		helper.setRowIndex(rowIndex++);
		helper.generateHeader(sheet, titles, 0, StringUtils.EMPTY,
				StringUtils.EMPTY);

		// 循环生成数据行
		for (int i = 0; i < infoList.size(); i++) {
			colIndex = 0;
			SysConfig info = (SysConfig) infoList.get(i);
			Row row = sheet.createRow(rowIndex++);

			helper.createStringCell(row, colIndex++, "" + info.getIdd());

			helper.createStringCell(row, colIndex++, "" + info.getDatagroup());

			helper.createStringCell(row, colIndex++, "" + info.getDataitem());

			helper.createStringCell(row, colIndex++, "" + info.getDatavalue());

			helper.createStringCell(row, colIndex++, "" + info.getAddtime());

		}
		// 在application.conf中配置的路径
		String path = Configuration.root().getString("export.path");
		int numStra = num++;// 静态值，区分文件名
		File file = new File(path);
		file.mkdir();// 判断文件夹是否存在，不存在就创建

		FileOutputStream out = null;
		String fileName = path + "SysConfig" + System.currentTimeMillis()
				+ numStra + ".xls";
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
			fileNameChine = "=?UTF-8?B?"
					+ (new String(Base64.encodeBase64(fileNameChine
							.getBytes("UTF-8")))) + "?=";
		} else {
			fileNameChine = java.net.URLEncoder.encode(fileNameChine, "UTF-8");
		}

		response().setHeader("Content-disposition",
				"attachment;filename=" + fileNameChine);
		response().setContentType("application/msexcel");
		return downFile;
	}
	
	
	/*
	 * 获取配置数据，如果没有找到，从数据库获取数据，放入Cache，缓存12小时
	 */
	public static String findSysconfig(String group,String item, String defaul )
	{
		//缓存里面查找
		String key       = ICacheService.Cache_String_Config + group + item;
		String value = util.jedis.RedisUtil.getInstance().getEntity(key, String.class);
		if( value != null ){
			return value;
		}
		
		//数据库里面查找
		List<SysConfig> ulist = Ebean.getServer(GlobalDBControl.getReadDB())
				.createQuery(SysConfig.class).where()
				.eq("datagroup", group)
				.eq("dataitem", item)
				.findList();
		if(ulist!=null && !ulist.isEmpty() && ulist.size()>0){
			SysConfig sc= ulist.get(0);
			util.jedis.RedisUtil.getInstance().setEntity(key, sc.getDatavalue(),60*2);
			return sc.getDatavalue();
		}
		return defaul;
	}
	
	public static String findSysconfig(String group,String item )
	{
		//缓存里面查找
		String key       = ICacheService.Cache_String_Config + group + item;
		String value = util.jedis.RedisUtil.getInstance().getEntity(key, String.class);
		if( value != null ){
			return value;
		}
		
		//数据库里面查找
		List<SysConfig> ulist = Ebean.getServer(GlobalDBControl.getReadDB())
				.createQuery(SysConfig.class).where()
				.eq("datagroup", group)
				.eq("dataitem", item)
				.findList();
		if(ulist!=null && !ulist.isEmpty() && ulist.size()>0){
			SysConfig sc= ulist.get(0);
			util.jedis.RedisUtil.getInstance().setEntity(key, sc.getDatavalue(),60*2);
			return sc.getDatavalue();
		}
		return null;
	}
	
	public static String findSysconfigLike(String group,String item)
	{
		//数据库里面查找
		List<SysConfig> ulist = Ebean.getServer( GlobalDBControl.getReadDB() )
				.createQuery(SysConfig.class).where()
				.eq("datagroup", group)
				.ilike("dataitem", "%"+item+"%").orderBy("idd")
				.findList();
		if(ulist.size()>0){
			SysConfig sc= ulist.get(0);
			return sc.getDatavalue();
		}
		return null;
	}

}