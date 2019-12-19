package controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import models.*;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Page;

import controllers.Secured;

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

import play.cache.Cache;
import play.libs.Json;
import play.mvc.*;
import play.*;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import util.AjaxHellper;
import util.GetUrl;
import util.GlobalDBControl;
import util.GlobalSetting;
import util.ImageGenerator;
import util.PagedObject;
import util.StringUtil;
import play.Logger;
import views.html.page.*;
import util.classEntity.*;

@Security.Authenticated(Secured.class)
public class SystableAction extends Controller {

	// 进入页面列表；
	public static Result view() {
		return ok(SystableList.render());
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
		sql.append("find Systable where 1=1 ");

		String idd = StringUtil.getHttpParam(request(), "idd");
		if (idd == null)
			idd = "";
		if (!(idd.equals("")) && !(idd.equals("undefined")))
			sql.append(" and ( idd= '" + idd + "'  )");

		String name = StringUtil.getHttpParam(request(), "name");
		if (name == null)
			name = "";
		if (!(name.equals("")) && !(name.equals("undefined")))
			sql.append(" and ( name= '" + name + "'  )");

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
				.createQuery(Systable.class, sql.toString()).findRowCount();
		List<Systable> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.createQuery(Systable.class, sql.toString())
				.setFirstRow(nStart).setMaxRows(nLimit).orderBy("idd desc")
				.findList();

		if (export != null && export.equals("1")) {
			String fileName = "Systable" + simp.format(new Date()) + ".xls";
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
		PagedObject<Systable> po = new PagedObject(ulist, nPages, rowCount);
		response().setHeader("Cache-Control", "no-cache");
		return ok(Json.toJson(po));
	}

	// 列表
	public static Result listAll() {
		List<Systable> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.find(Systable.class).orderBy("idd").findList();
		response().setHeader("Cache-Control", "no-cache");
		return ok(Json.toJson(ulist));
	}

	// 删除
	public static Result delete(int idd) {
		Systable systable = Ebean.getServer(GlobalDBControl.getDB())
				.find(Systable.class).where().eq("idd", idd).findUnique();
		
		String sql = "delete from Systable where idd =:idd";
		Ebean.getServer(GlobalDBControl.getDB())
				.createUpdate(Systable.class, sql).setParameter("idd", idd)
				.execute();
		sql = "delete from Systablecolumn where tablename ='"+systable.getName()+"'";
		Ebean.getServer(GlobalDBControl.getDB())
				.createUpdate(Systable.class, sql).setParameter("idd", idd)
				.execute();
		response().setHeader("Cache-Control", "no-cache");
		return ok(Json.toJson("操作成功"));
	}

	// 获取 单个
	public static Result get(int idd) {
		Systable systable = Ebean.getServer(GlobalDBControl.getDB())
				.find(Systable.class).where().eq("idd", idd).findUnique();
		response().setHeader("Cache-Control", "no-cache");
		return ok(Json.toJson(systable));
	}
	
	// 获取 单个
	public static Result viewColumn(int idd) {
		Systable systable = Ebean.getServer(GlobalDBControl.getDB())
				.find(Systable.class).where().eq("idd", idd).findUnique();
		session( "vtable",systable.getName() );
		response().setHeader("Cache-Control", "no-cache");
		return redirect("/Systablecolumn/View");
	}

	// 新增 / 修改
	public static Result modify() {
		String idd = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "idd");
		int nidd = StringTool.GetInt(idd, 0);

		String name = AjaxHellper.getHttpParamOfFormUrlEncoded(request(),
				"name");

		String ext = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "ext");

		String operation = AjaxHellper.getHttpParamOfFormUrlEncoded(request(),
				"operation");

		Systable systable;
		if (operation.equals("add")) // 新增
			systable = new Systable();
		else
			// 修改
			systable = Ebean.getServer(GlobalDBControl.getDB())
					.find(Systable.class).where().eq("idd", idd).findUnique();

		if (systable != null) {
			// 赋值,字符串类型可以直接赋值，其他类型需要转换 ！！！！

			systable.setName(name);

			systable.setExt(ext);

			Ebean.getServer(GlobalDBControl.getDB()).save(systable);
			response().setHeader("Cache-Control", "no-cache");
			return ok(Json.toJson(systable));
		}

		response().setHeader("Cache-Control", "no-cache");
		return ok(Json.toJson(null));
	}

	static int num = 0;

	public static File exportList(List<Systable> infoList, String fileNameChine)
			throws UnsupportedEncodingException {
		Workbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet("sheet1");

		sheet.setColumnWidth(0, 6000);

		sheet.setColumnWidth(1, 6000);

		sheet.setColumnWidth(2, 6000);

		ExcelGenerateHelper helper = ExcelGenerateHelper.getInstance(wb);

		// 获取行索引
		int rowIndex = 0;
		int colIndex = 0;

		// 设置表头
		String[] titles = {

		"idd"

		, "name"

		, "ext"

		};

		// 生成表头
		helper.setRowIndex(rowIndex++);
		helper.generateHeader(sheet, titles, 0, StringUtils.EMPTY,
				StringUtils.EMPTY);

		// 循环生成数据行
		for (int i = 0; i < infoList.size(); i++) {
			colIndex = 0;
			Systable info = (Systable) infoList.get(i);
			Row row = sheet.createRow(rowIndex++);

			helper.createStringCell(row, colIndex++, "" + info.getIdd());

			helper.createStringCell(row, colIndex++, "" + info.getName());

			helper.createStringCell(row, colIndex++, "" + info.getExt());

		}
		// 在application.conf中配置的路径
		String path = Configuration.root().getString("export.path");
		int numStra = num++;// 静态值，区分文件名
		File file = new File(path);
		file.mkdir();// 判断文件夹是否存在，不存在就创建

		FileOutputStream out = null;
		String fileName = path + "Systable" + System.currentTimeMillis()
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

	static String[] headers = {

	"name"

	, "ext"

	};
	static int MAX_LINES = 10000;

	public static Result upload() {
		String nameext = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "nameext");
		if(nameext==null)
			nameext = AjaxHellper.getHttpParam(request(), "nameext");
		Integer ncid;
		int successNum = 0; // 导入成功的数量
		MultipartFormData body = request().body().asMultipartFormData();
		FilePart file_excel = body.getFile("file_excel");

		if (file_excel != null) {
			String fileName = file_excel.getFilename();
			int rowNum = 0;// 导入Excel表里的有效记录数
			File file = file_excel.getFile();

			File fileTemp = new File(ExcelUtils.getUploadPath());
			fileTemp.mkdir();

			String uploadFileName = ExcelUtils.getUploadPath() + fileName;
			File destFile = ExcelUtils.createFile(uploadFileName);

			ExcelUtils.copy(file, destFile);
			file.delete();// 将临时文件删除掉

			Entity entity= Entity.anylisFile( uploadFileName );
			Systable systable = new Systable();
			systable.setExt(nameext);
			systable.setName( entity.name );
			
			for( EntityParam param: entity.allParams)
			{
				Systablecolumn sc =new Systablecolumn();
				sc.setTablename(entity.name);
				sc.setExt(param.ext);
				sc.setIsEnum(0);
				sc.setTypeString(param.typeString );
				sc.setParamIdx(param.paramIdx);
				sc.setColumnname(param.name);
				
				if( Ebean.getServer(GlobalDBControl.getDB()).find(Systablecolumn.class)
						.where().eq("tablename", entity.name)
						.eq("columnname", param.name)
						.findRowCount() <=0 )
				Ebean.getServer(GlobalDBControl.getDB()).save(sc);
			}
			if(Ebean.getServer(GlobalDBControl.getDB()).find(Systable.class)
					.where().eq("name", systable.getName()).findRowCount() <=0 )
				Ebean.getServer(GlobalDBControl.getDB()).save(systable);

			return ok(Json.toJson(entity.name));
		}

		return ok(Json.toJson(successNum));
	}

	// 检查是否是一个完全的空行
	public static boolean checkNullOrNot(Row row) {
		if (row == null)
			return true;
		int index = 0;
		for (int i = 0; i < headers.length; i++) {
			String cellValue = StringUtil.cellValueToString(row.getCell(i));
			if (cellValue == null || cellValue.trim().equals("")) {
				index++;
			}
		}
		return index == headers.length;
	}
	
	public static Result makeFile(int idd)
	{
		Systable systable = Ebean
				.getServer(GlobalDBControl.getDB()).find(Systable.class)
				.where().eq("idd", idd).findUnique();
		Entity entity = mkEntity( systable );
		if( entity.getIdx().length()==0 )
			return ok(PageMakerResult.render("",entity.name+"没有主键"));
		Cache.set("DATA_CLASS", entity);
		
		//get URL and write file ;
		String dir = Configuration.root().getString("app.dir");
		
		//HTML
		String file1 = dir+"/app/views/page/"+entity.name+"List.scala.html";
		String javaTxt1 = GetUrl.getURL("http://127.0.0.1:9000/PageMaker/ViewHtml");
		if(javaTxt1!=null){
			Logger.info(javaTxt1);
		}
		
		 //JAVA 
		String file2 = dir+"/app/controllers/"+entity.name+"Action.java";
		String javaTxt2 = GetUrl.getURL("http://127.0.0.1:9000/PageMaker/ViewJava");
		if(javaTxt2!=null){
			Logger.info(javaTxt2);
		}
		
		//ROUTS
		String file3 = dir+"/conf/routes";
		String oldRouts = FileOP.readFile(file3, "utf-8");
		String javaTxt3 = GetUrl.getURL("http://127.0.0.1:9000/PageMaker/ViewRouts");
		if( javaTxt3!=null && oldRouts.indexOf("/"+entity.name+"/") == -1 ) {
			oldRouts += "\r\n" +javaTxt3;
			Logger.info(oldRouts);
		}
		
		//ADD AUTH
		addFunctions( entity ); 
				
		MakeFile mf =new MakeFile(file1,javaTxt1,file2,javaTxt2,file3,oldRouts);
		//return ok("/"+entity.name+"/View");
		response().setHeader("Cache-Control", "no-cache");
		return ok(PageMakerResult.render("/"+entity.name+"/View",entity.name));
	}

	private static void addFunctions(Entity entity) {
		String name = entity.name;
		String chn = entity.chnname;
		if( chn==null )
			chn = entity.name;
		//不要重复；
		if( Ebean.getServer(GlobalDBControl.getDB()).find(SysFunction.class).where()
			.eq("fcode", name).findRowCount()>0 )
			return;
		SysFunction sysfunction = new SysFunction ();
		int pid = 1;
		sysfunction.setParent( pid );
		sysfunction.setName( chn );
		sysfunction.setLevel( 2 );
		sysfunction.setAddtime( new Date() );
		sysfunction.setFcode( name );
		sysfunction.setUrl("/"+name+"/View");
		sysfunction.setState(1);
		sysfunction.setIsMenu(1);
		Ebean.getServer(GlobalDBControl.getDB()).save(sysfunction);
		addFuncRole( sysfunction );
		
		//add list
		SysFunction subfunction = new SysFunction ();
		subfunction.setName(chn+"搜索");
		subfunction.setParent( sysfunction.getIdx() );
		subfunction.setLevel( 3 );
		subfunction.setAddtime( new Date() );
		subfunction.setFcode( name+".list" );
		subfunction.setUrl("/"+name+"/List");
		subfunction.setState(1);
		subfunction.setIsMenu(0);
		
		Ebean.getServer(GlobalDBControl.getDB()).save(subfunction);
		addFuncRole( subfunction );
		
		//add modify
		subfunction = new SysFunction ();
		subfunction.setName(chn+"修改");
		subfunction.setParent( sysfunction.getIdx() );
		subfunction.setLevel( 3 );
		subfunction.setAddtime( new Date() );
		subfunction.setFcode( name+".modify" );
		subfunction.setUrl("/"+name+"/Modify");
		subfunction.setState(1);
		subfunction.setIsMenu(0);
		
		Ebean.getServer(GlobalDBControl.getDB()).save(subfunction);
		addFuncRole( subfunction );
		
		//add delete
		subfunction = new SysFunction ();
		subfunction.setName(chn+"删除");
		subfunction.setParent( sysfunction.getIdx() );
		subfunction.setLevel( 3 );
		subfunction.setAddtime( new Date() );
		subfunction.setFcode( name+".delete" );
		subfunction.setUrl("/"+name+"/Delete");
		subfunction.setState(1);
		subfunction.setIsMenu(0);
		
		Ebean.getServer(GlobalDBControl.getDB()).save(subfunction);
		addFuncRole( subfunction );
		
		//add get 
		subfunction = new SysFunction ();
		subfunction.setName(chn+"查看");
		subfunction.setParent( sysfunction.getIdx() );
		subfunction.setLevel( 3 );
		subfunction.setAddtime( new Date() );
		subfunction.setFcode( name +".get");
		subfunction.setUrl("/"+name+"/Get");
		subfunction.setState(1);
		subfunction.setIsMenu(0);
		
		Ebean.getServer(GlobalDBControl.getDB()).save(subfunction);
		addFuncRole( subfunction );
		
		//add enum
		subfunction = new SysFunction ();
		subfunction.setParent( sysfunction.getIdx() );
		subfunction.setName(chn+"枚举");
		subfunction.setLevel( 3 );
		subfunction.setAddtime( new Date() );
		subfunction.setFcode( name+".all" );
		subfunction.setUrl("/"+name+"/All");
		subfunction.setState(1);
		subfunction.setIsMenu(0);
		
		Ebean.getServer(GlobalDBControl.getDB()).save(subfunction);
		addFuncRole( subfunction );
		
	}
	
	static void addFuncRole(SysFunction func)
	{
		if( Ebean.getServer(GlobalDBControl.getDB())
				.find(SysRoleFunc.class).where()
				.eq("fid", func.getIdx()).eq("rid", 1)
				.findRowCount()>0
				)
			return;
		SysRoleFunc sf = new SysRoleFunc();
		sf.setFid(func.getIdx());
		sf.setRid(1);
		sf.setState(0);
		sf.setAddtime(new Date());
		Ebean.getServer(GlobalDBControl.getDB()).save(sf);
	}

	private static Entity mkEntity(Systable systable) {
		Entity ent = new Entity();
		ent.name = systable.getName();
		ent.chnname = systable.getExt();
		ent.pkg = "models";
		
		List<Systablecolumn> ulist = Ebean.getServer(GlobalDBControl.getDB())
				.find(Systablecolumn.class).where()
				.eq("tablename", systable.getName())
				.findList();
		for( Systablecolumn co:ulist  ){
			EntityParam ep =new EntityParam();
			ep.name = co.getColumnname();
			ep.ext = co.getExt();
			ep.paramIdx = co.getParamIdx();
			ep.enumType = co.getEnumType();
			ep.typeString = co.getTypeString();
			ent.allParams.add(ep);
		}
		ent.idx = ent.getIdx();
		return ent;
	}

}