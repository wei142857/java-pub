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

import util.ExcelGenerateHelper;

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
import util.ImageUploadUtil;
import util.PagedObject;
import util.StringUtil;
import play.Logger;
import views.html.page.*;
import util.classEntity.*;

@Security.Authenticated(Secured.class)
public class ResourceuploadAction extends Controller {

	// 进入页面列表；
	public static Result view() {
		return ok(ResourceuploadList.render(null));
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
		sql.append("find Resourceupload where 1=1 ");

		String idd = StringUtil.getHttpParam(request(), "idd");
		if (idd == null)
			idd = "";
		if (!(idd.equals("")) && !(idd.equals("undefined")))
			sql.append(" and ( idd= '" + idd + "'  )");

		String name = StringUtil.getHttpParam(request(), "name");
		if (name == null)
			name = "";
		if (!(name.equals("")) && !(name.equals("undefined")))
			sql.append(" and ( name like '" + name + "%'  )");

		String url = StringUtil.getHttpParam(request(), "url");
		if (url == null)
			url = "";
		if (!(url.equals("")) && !(url.equals("undefined")))
			sql.append(" and ( url= '" + url + "'  )");

		String ext = StringUtil.getHttpParam(request(), "ext");
		if (ext == null)
			ext = "";
		if (!(ext.equals("")) && !(ext.equals("undefined")))
			sql.append(" and ( ext= '" + ext + "'  )");

		String export = StringUtil.getHttpParam(request(), "export");// 是否导出
		if (export != null && export.equals("1")) {
			nStart = 0;
			nLimit = 65534;
		}

		Logger.info(sql.toString());
		// 下面简单的分页，需要重构，应该由 Dao-Sevice去实现。
		int rowCount = Ebean.getServer(GlobalDBControl.getDB())
				.createQuery(Resourceupload.class, sql.toString())
				.findRowCount();
		List<Resourceupload> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.createQuery(Resourceupload.class, sql.toString())
				.setFirstRow(nStart).setMaxRows(nLimit).orderBy("idd desc")
				.findList();

		if (export != null && export.equals("1")) {
			String fileName = "Resourceupload" + simp.format(new Date())
					+ ".xls";
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
		PagedObject<Resourceupload> po = new PagedObject(ulist, nPages,
				rowCount);
		response().setHeader("Cache-Control", "no-cache");
		return ok(Json.toJson(po));
	}

	// 列表
	public static Result listAll() {
		List<Resourceupload> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.find(Resourceupload.class).orderBy("idd").findList();
		response().setHeader("Cache-Control", "no-cache");
		return ok(Json.toJson(ulist));
	}

	// 删除
	public static Result delete(int idd) {
		String sql = "delete from Resourceupload where idd =:idd";
		Ebean.getServer(GlobalDBControl.getDB())
				.createUpdate(Resourceupload.class, sql)
				.setParameter("idd", idd).execute();
		response().setHeader("Cache-Control", "no-cache");
		return ok(Json.toJson("操作成功"));
	}

	// 获取 单个
	public static Result get(int idd) {
		Resourceupload resourceupload = Ebean
				.getServer(GlobalDBControl.getDB()).find(Resourceupload.class)
				.where().eq("idd", idd).findUnique();
		response().setHeader("Cache-Control", "no-cache");
		return ok(Json.toJson(resourceupload));
	}

	// 新增 / 修改
	public static Result modify() {
		String idd = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "idd");
		int nidd = StringTool.GetInt(idd, 0);

		String name = AjaxHellper.getHttpParamOfFormUrlEncoded(request(),
				"name");

		String url = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "url");

		String ext = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "ext");

		String operation = AjaxHellper.getHttpParamOfFormUrlEncoded(request(),
				"operation");

		Resourceupload resourceupload;
		if (operation.equals("add")) // 新增
			resourceupload = new Resourceupload();
		else
			// 修改
			resourceupload = Ebean.getServer(GlobalDBControl.getDB())
					.find(Resourceupload.class).where().eq("idd", idd)
					.findUnique();

		if (resourceupload != null) {
			// 赋值,字符串类型可以直接赋值，其他类型需要转换 ！！！！

			resourceupload.setName(name);

			resourceupload.setUrl(url);

			resourceupload.setExt(ext);

			Ebean.getServer(GlobalDBControl.getDB()).save(resourceupload);
			response().setHeader("Cache-Control", "no-cache");
			return ok(Json.toJson(resourceupload));
		}

		response().setHeader("Cache-Control", "no-cache");
		return ok(Json.toJson(null));
	}

	static int num = 0;

	public static File exportList(List<Resourceupload> infoList,
			String fileNameChine) throws UnsupportedEncodingException {
		Workbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet("sheet1");

		sheet.setColumnWidth(0, 6000);

		sheet.setColumnWidth(1, 6000);

		sheet.setColumnWidth(2, 6000);

		sheet.setColumnWidth(3, 6000);

		ExcelGenerateHelper helper = ExcelGenerateHelper.getInstance(wb);

		// 获取行索引
		int rowIndex = 0;
		int colIndex = 0;

		// 设置表头
		String[] titles = {

		"idd"

		, "name"

		, "url"

		, "ext"

		};

		// 生成表头
		helper.setRowIndex(rowIndex++);
		helper.generateHeader(sheet, titles, 0, StringUtils.EMPTY,
				StringUtils.EMPTY);

		// 循环生成数据行
		for (int i = 0; i < infoList.size(); i++) {
			colIndex = 0;
			Resourceupload info = (Resourceupload) infoList.get(i);
			Row row = sheet.createRow(rowIndex++);

			helper.createStringCell(row, colIndex++, "" + info.getIdd());

			helper.createStringCell(row, colIndex++, "" + info.getName());

			helper.createStringCell(row, colIndex++, "" + info.getUrl());

			helper.createStringCell(row, colIndex++, "" + info.getExt());

		}
		// 在application.conf中配置的路径
		String path = Configuration.root().getString("export.path");
		int numStra = num++;// 静态值，区分文件名
		File file = new File(path);
		file.mkdir();// 判断文件夹是否存在，不存在就创建

		FileOutputStream out = null;
		String fileName = path + "Resourceupload" + System.currentTimeMillis()
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
	
	static String mkFileName(FilePart f)
	{
		//只要后缀；
		String[] ff = StringTool.splitString(f.getFilename().toLowerCase(), ".");
		return "/public/upres/"+StringUtil.getDateString() + "." + ff[ ff.length - 1 ];
	}
	static String mkFilePath(String f)
	{
		String dir = Configuration.root().getString("app.dir");
		return dir + f;
	}
	
	public static Result upload()
	{
		Logger.info("do upload image ...");
		MultipartFormData body = request().body().asMultipartFormData();
		if(body!=null){
			List<FilePart> file_img = body.getFiles();
			if( file_img.size()>0 ){
				FilePart filePart = file_img.get(0);
				String path = ImageUploadUtil.putImg(filePart, "upres");
				return ok(path);
			}
		}
		return ok("");
	}

}