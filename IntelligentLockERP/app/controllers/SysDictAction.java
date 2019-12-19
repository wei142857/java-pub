package controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.SysDict;

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
import util.jedis.RedisUtil;
import views.html.page.*;

import com.avaje.ebean.Ebean;

//@Security.Authenticated(Secured.class)
public class SysDictAction extends Controller {

	// 进入页面列表；
	public static Result view() {
		session( "vtableColumn","");
		return ok(SysDictList.render());
	}
	
	public static Result viewOne() {
		return ok(SysDictListOne.render(session( "vtableColumn") ));
	}

	/**************************************************************
	 * 增删改查的功能 ： AJAX方式
	 */
	private static SimpleDateFormat simp = new SimpleDateFormat("yyyy-MM-dd");

	// 列表
	public static Result list() {
		session( "vtableColumn","");
		
		// 获取前端grid 传递过来的参数：分页，搜索
		String start = StringUtil.getHttpParam(request(), "start");
		String limit = StringUtil.getHttpParam(request(), "limit");
		int nStart = StringTool.GetInt(start, 0);
		int nLimit = StringTool.GetInt(limit, 10);

		StringBuffer sql = new StringBuffer();
		sql.append("find SysDict where 1=1 ");

		String idd = StringUtil.getHttpParam(request(), "idd");
		if (idd == null)
			idd = "";
		if (!(idd.equals("")) && !(idd.equals("undefined")))
			sql.append(" and ( idd= '" + idd + "'  )");

		String dictType = StringUtil.getHttpParam(request(), "dictType");
		if (dictType == null)
			dictType = "";
		
		if (!(dictType.equals("")) && !(dictType.equals("undefined")))
			sql.append(" and ( dictType= '" + dictType + "'  )");

		String dictTypeName = StringUtil
				.getHttpParam(request(), "dictTypeName");
		if (dictTypeName == null)
			dictTypeName = "";
		if (!(dictTypeName.equals("")) && !(dictTypeName.equals("undefined")))
			sql.append(" and ( dictTypeName= '" + dictTypeName + "'  )");

		String dictId = StringUtil.getHttpParam(request(), "dictId");
		if (dictId == null)
			dictId = "";
		if (!(dictId.equals("")) && !(dictId.equals("undefined")))
			sql.append(" and ( dictId= '" + dictId + "'  )");

		String dictName = StringUtil.getHttpParam(request(), "dictName");
		if (dictName == null)
			dictName = "";
		if (!(dictName.equals("")) && !(dictName.equals("undefined")))
			sql.append(" and ( dictName= '" + dictName + "'  )");

		String status = StringUtil.getHttpParam(request(), "status");
		if (status == null)
			status = "";
		if (!(status.equals("")) && !(status.equals("undefined")))
			sql.append(" and ( status= '" + status + "'  )");

		String sortno = StringUtil.getHttpParam(request(), "sortno");
		if (sortno == null)
			sortno = "";
		if (!(sortno.equals("")) && !(sortno.equals("undefined")))
			sql.append(" and ( sortno= '" + sortno + "'  )");

		String remark = StringUtil.getHttpParam(request(), "remark");
		if (remark == null)
			remark = "";
		if (!(remark.equals("")) && !(remark.equals("undefined")))
			sql.append(" and ( remark= '" + remark + "'  )");

		String export = StringUtil.getHttpParam(request(), "export");// 是否导出
		if (export != null && export.equals("1")) {
			nStart = 0;
			nLimit = 65534;
		}

		Logger.info(sql.toString());
		// 下面简单的分页，需要重构，应该由 Dao-Sevice去实现。
		int rowCount = Ebean.getServer(GlobalDBControl.getDB())
				.createQuery(SysDict.class, sql.toString()).findRowCount();
		List<SysDict> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.createQuery(SysDict.class, sql.toString()).setFirstRow(nStart)
				.setMaxRows(nLimit).orderBy("idd desc").findList();

		if (export != null && export.equals("1")) {
			String fileName = "SysDict" + simp.format(new Date()) + ".xls";
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
		PagedObject<SysDict> po = new PagedObject(ulist, nPages, rowCount);
		response().setHeader("Cache-Control", "no-cache");
		return ok(Json.toJson(po));
	}
	
	//查看某个
	public static Result listOne() {
		// 获取前端grid 传递过来的参数：分页，搜索
		String start = StringUtil.getHttpParam(request(), "start");
		String limit = StringUtil.getHttpParam(request(), "limit");
		int nStart = StringTool.GetInt(start, 0);
		int nLimit = StringTool.GetInt(limit, 10);

		StringBuffer sql = new StringBuffer();
		sql.append("find SysDict where 1=1 ");

		String idd = StringUtil.getHttpParam(request(), "idd");
		if (idd == null)
			idd = "";
		if (!(idd.equals("")) && !(idd.equals("undefined")))
			sql.append(" and ( idd= '" + idd + "'  )");

		String dictType = StringUtil.getHttpParam(request(), "dictType");
		if (dictType == null)
			dictType = "";
		
		if( !StringTool.isNull(session( "vtableColumn" ) ) ){
			dictType = session( "vtableColumn");
		}
		
		if (!(dictType.equals("")) && !(dictType.equals("undefined")))
			sql.append(" and ( dictType= '" + dictType + "'  )");

		String dictTypeName = StringUtil
				.getHttpParam(request(), "dictTypeName");
		if (dictTypeName == null)
			dictTypeName = "";
		if (!(dictTypeName.equals("")) && !(dictTypeName.equals("undefined")))
			sql.append(" and ( dictTypeName= '" + dictTypeName + "'  )");

		String dictId = StringUtil.getHttpParam(request(), "dictId");
		if (dictId == null)
			dictId = "";
		if (!(dictId.equals("")) && !(dictId.equals("undefined")))
			sql.append(" and ( dictId= '" + dictId + "'  )");

		String dictName = StringUtil.getHttpParam(request(), "dictName");
		if (dictName == null)
			dictName = "";
		if (!(dictName.equals("")) && !(dictName.equals("undefined")))
			sql.append(" and ( dictName= '" + dictName + "'  )");

		String status = StringUtil.getHttpParam(request(), "status");
		if (status == null)
			status = "";
		if (!(status.equals("")) && !(status.equals("undefined")))
			sql.append(" and ( status= '" + status + "'  )");

		String sortno = StringUtil.getHttpParam(request(), "sortno");
		if (sortno == null)
			sortno = "";
		if (!(sortno.equals("")) && !(sortno.equals("undefined")))
			sql.append(" and ( sortno= '" + sortno + "'  )");

		String remark = StringUtil.getHttpParam(request(), "remark");
		if (remark == null)
			remark = "";
		if (!(remark.equals("")) && !(remark.equals("undefined")))
			sql.append(" and ( remark= '" + remark + "'  )");

		String export = StringUtil.getHttpParam(request(), "export");// 是否导出
		if (export != null && export.equals("1")) {
			nStart = 0;
			nLimit = 65534;
		}

		Logger.info(sql.toString());
		// 下面简单的分页，需要重构，应该由 Dao-Sevice去实现。
		int rowCount = Ebean.getServer(GlobalDBControl.getDB())
				.createQuery(SysDict.class, sql.toString()).findRowCount();
		List<SysDict> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.createQuery(SysDict.class, sql.toString()).setFirstRow(nStart)
				.setMaxRows(nLimit).orderBy("idd desc").findList();

		if (export != null && export.equals("1")) {
			String fileName = "SysDict" + simp.format(new Date()) + ".xls";
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
		PagedObject<SysDict> po = new PagedObject(ulist, nPages, rowCount);
		response().setHeader("Cache-Control", "no-cache");
		return ok(Json.toJson(po));
	}

	// 列表
	public static Result listAll() {
		List<SysDict> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.find(SysDict.class).orderBy("idd").findList();
		response().setHeader("Cache-Control", "no-cache");
		return ok(Json.toJson(ulist));
	}

	// 删除
	public static Result delete(int idd) {
		String sql = "delete from SysDict where idd =:idd";
		Ebean.getServer(GlobalDBControl.getDB())
				.createUpdate(SysDict.class, sql).setParameter("idd", idd)
				.execute();
		response().setHeader("Cache-Control", "no-cache");
		return ok(Json.toJson("操作成功"));
	}

	// 获取 单个
	public static Result get(int idd) {
		SysDict sysdict = Ebean.getServer(GlobalDBControl.getDB())
				.find(SysDict.class).where().eq("idd", idd).findUnique();
		response().setHeader("Cache-Control", "no-cache");
		return ok(Json.toJson(sysdict));
	}

	// 新增 / 修改
	public static Result modify() {
		String idd = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "idd");
		int nidd = StringTool.GetInt(idd, 0);

		String dictType = AjaxHellper.getHttpParamOfFormUrlEncoded(request(),
				"dictType");

		cleanMemTypeList(dictType);

		String dictTypeName = AjaxHellper.getHttpParamOfFormUrlEncoded(
				request(), "dictTypeName");

		String dictId = AjaxHellper.getHttpParamOfFormUrlEncoded(request(),
				"dictId");

		String dictName = AjaxHellper.getHttpParamOfFormUrlEncoded(request(),
				"dictName");

		String status = AjaxHellper.getHttpParamOfFormUrlEncoded(request(),
				"status");

		String sortno = AjaxHellper.getHttpParamOfFormUrlEncoded(request(),
				"sortno");

		String remark = AjaxHellper.getHttpParamOfFormUrlEncoded(request(),
				"remark");

		String operation = AjaxHellper.getHttpParamOfFormUrlEncoded(request(),
				"operation");

		SysDict sysdict;
		if (operation.equals("add")) // 新增
		{
			sysdict = new SysDict();
			if( !StringTool.isNull(session( "vtableColumn" ) ) ){
				sysdict.setDictType( session( "vtableColumn" ) );
			}
		}
		else
			// 修改
			sysdict = Ebean.getServer(GlobalDBControl.getDB())
					.find(SysDict.class).where().eq("idd", idd).findUnique();

		if (sysdict != null) {
			// 赋值,字符串类型可以直接赋值，其他类型需要转换 ！！！！

			sysdict.setDictType(dictType);
			if( !StringTool.isNull( session( "vtableColumn" ) ) )
				sysdict.setDictType( session( "vtableColumn" ) );

			sysdict.setDictTypeName(dictTypeName);

			sysdict.setDictId(dictId);

			sysdict.setDictName(dictName);
			sysdict.setStatus(Integer.parseInt(status));
			sysdict.setSortno(Integer.parseInt(sortno));

			sysdict.setRemark(remark);

			Ebean.getServer(GlobalDBControl.getDB()).save(sysdict);
			response().setHeader("Cache-Control", "no-cache");
			return ok(Json.toJson(sysdict));
		}

		response().setHeader("Cache-Control", "no-cache");
		return ok(Json.toJson(null));
	}

	static int num = 0;

	public static File exportList(List<SysDict> infoList, String fileNameChine)
			throws UnsupportedEncodingException {
		Workbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet("sheet1");

		sheet.setColumnWidth(0, 6000);

		sheet.setColumnWidth(1, 6000);

		sheet.setColumnWidth(2, 6000);

		sheet.setColumnWidth(3, 6000);

		sheet.setColumnWidth(4, 6000);

		sheet.setColumnWidth(5, 6000);

		sheet.setColumnWidth(6, 6000);

		sheet.setColumnWidth(7, 6000);

		ExcelGenerateHelper helper = ExcelGenerateHelper.getInstance(wb);

		// 获取行索引
		int rowIndex = 0;
		int colIndex = 0;

		// 设置表头
		String[] titles = {

		"idd"

		, "dictType"

		, "dictTypeName"

		, "dictId"

		, "dictName"

		, "status"

		, "sortno"

		, "remark"

		};

		// 生成表头
		helper.setRowIndex(rowIndex++);
		helper.generateHeader(sheet, titles, 0, StringUtils.EMPTY,
				StringUtils.EMPTY);

		// 循环生成数据行
		for (int i = 0; i < infoList.size(); i++) {
			colIndex = 0;
			SysDict info = (SysDict) infoList.get(i);
			Row row = sheet.createRow(rowIndex++);

			helper.createStringCell(row, colIndex++, "" + info.getIdd());

			helper.createStringCell(row, colIndex++, "" + info.getDictType());

			helper.createStringCell(row, colIndex++,
					"" + info.getDictTypeName());

			helper.createStringCell(row, colIndex++, "" + info.getDictId());

			helper.createStringCell(row, colIndex++, "" + info.getDictName());

			helper.createStringCell(row, colIndex++, "" + info.getStatus());

			helper.createStringCell(row, colIndex++, "" + info.getSortno());

			helper.createStringCell(row, colIndex++, "" + info.getRemark());

		}
		// 在application.conf中配置的路径
		String path = Configuration.root().getString("export.path");
		int numStra = num++;// 静态值，区分文件名
		File file = new File(path);
		file.mkdir();// 判断文件夹是否存在，不存在就创建

		FileOutputStream out = null;
		String fileName = path + "SysDict" + System.currentTimeMillis()
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

	public static Result getDictEntrys(String dictType) {
		Object ch = RedisUtil.getInstance().getEntity("dict_" + dictType, List.class, 0);
		if (ch != null) {
			List<SysDict> ulist = (List<SysDict>) ch;
			return ok(Json.toJson(ulist));
		} else {
			List<SysDict> ulist = memTypeList(dictType);
			return ok(Json.toJson(ulist));
		}
	}

	public static void cleanMemTypeList(String dictType) {
		RedisUtil.getInstance().deleteEntity("dict_" + dictType, 0);
		RedisUtil.getInstance().deleteEntity("Web_Map_Dict" + dictType, 0);
	}

	/**
	 * @param dictType
	 * @return
	 */
	private static List<SysDict> memTypeList(String dictType) {
		StringBuffer sql = new StringBuffer();
		sql.append("find SysDict where 1=1 ");
		sql.append(" and ( dictType= '" + dictType + "'  )");
		List<SysDict> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.createQuery(SysDict.class, sql.toString()).orderBy("sortno")
				.findList();
		
		RedisUtil.getInstance().setEntity("dict_" + dictType, ulist, 60 * 60, 0);
		return ulist;
	}

	public static Result getDictEntrybyId(String dictType, String id) {
		SysDict ch = (SysDict) RedisUtil.getInstance().getEntity("dict_" + dictType + "_"
				+ id,SysDict.class,0);
		if (ch != null) {
			return ok(Json.toJson(ch));
		} else {
			StringBuffer sql = new StringBuffer();
			sql.append("find SysDict where 1=1 ");
			sql.append(" and ( dict_Type= '" + dictType + "'  )");
			sql.append(" and ( dict_id= '" + id + "'  )");
			SysDict sysdict = Ebean.getServer(GlobalDBControl.getDB())
					.createQuery(SysDict.class, sql.toString()).findUnique();
			
			RedisUtil.getInstance().setEntity("dict_" + dictType + "_" + id, sysdict, 60 * 60, 0);
			return ok(Json.toJson(sysdict));
		}
	}
	
	public static SysDict getDictEntrybyDictType(String dictType, String id) {
		SysDict ch = (SysDict) RedisUtil.getInstance().getEntity("dict_" + dictType + "_"
				+ id,SysDict.class,0);
		if (ch != null) {
			return ch;
		} else {
			StringBuffer sql = new StringBuffer();
			sql.append("find SysDict where 1=1 ");
			sql.append(" and ( dict_Type= '" + dictType + "'  )");
			sql.append(" and ( dict_id= '" + id + "'  )");
			SysDict sysdict = Ebean.getServer(GlobalDBControl.getDB())
					.createQuery(SysDict.class, sql.toString()).findUnique();
			
			RedisUtil.getInstance().setEntity("dict_" + dictType + "_" + id, sysdict, 60 * 60, 0);
			
			return sysdict;
		}
	}

	/*
	 * 前端展示使用的map 
	 * route ： GET /SysDict/Map/:type
	 * controllers.sysadmin.SysDictAction.getDictEntrysMap(type:String) 
	   js: var typeMap={}; 
	   $.get("/SysDict/Map/11",function(responseText){ 
	   typeMap =  responseText; });
	 */
	public static Result getDictEntrysMap(String dictType) {
		String key = "Web_Map_Dict" + dictType;
		response().setHeader("Cache-Control", "no-cache");

		@SuppressWarnings("unchecked")
		Map<String, String> out = (Map<String, String>) RedisUtil.getInstance().getEntity(key, Map.class);
		if (out != null)
			return ok(Json.toJson(out));

		out = new HashMap<String, String>();
		StringBuffer sql = new StringBuffer();
		sql.append("find SysDict where 1=1 ");
		sql.append(" and ( dictType= '" + dictType + "'  )");
		List<SysDict> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.createQuery(SysDict.class, sql.toString()).orderBy("sortno")
				.findList();

		for (SysDict dict : ulist)
			out.put(dict.getDictId(), dict.getDictName());
		RedisUtil.getInstance().setEntity(key, out, 60 * 60);
		return ok(Json.toJson(out));
	}
	
	//查找 字典 设定值
	public static String getDictString( String dictType , String id ) {
		String key ="Sys_DictStr:" + dictType + "_"		+ id;
		String ch =  util.jedis.RedisUtil.getInstance().getEntity(key,String.class);
		if (ch != null) {
			return ch;
		} else {
			StringBuffer sql = new StringBuffer();
			sql.append("find SysDict where 1=1 ");
			sql.append(" and ( dict_Type= '" + dictType + "'  )");
			sql.append(" and ( dict_id= '" + id + "'  )");
			SysDict sysdict = Ebean.getServer(GlobalDBControl.getDB())
					.createQuery(SysDict.class, sql.toString()).findUnique();
			if( sysdict != null ){
				util.jedis.RedisUtil.getInstance().setEntity( key , sysdict.getDictName(),
					60 * 5);
				return sysdict.getDictName();
			}
			return null;
		}
	}
}