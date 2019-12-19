package controllers;

import java.io.File;
import java.util.Date;
import java.util.List;

import models.SysFunction;

import com.avaje.ebean.Ebean;

import play.Configuration;
import play.Logger;
import play.libs.Json;
import play.mvc.*;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.data.*;
import static play.data.Form.*;
import views.html.page.*;
import util.GetUrl;
import util.GlobalDBControl;
import util.StringUtil;
import util.classEntity.*;
import play.cache.Cache;


public class PageMakerAction extends Controller {
  
	//进入页面列表；
	public static Result view() {
		Entity entity=null ;
		if( !StringTool.isNull(fnn ) ){
			entity=Entity.anylisFile( fnn );
			Cache.set("DATA_CLASS", entity);
		}
		return ok( PageMakerView.render(entity) );
	}
	
	/***********************************************************
	 *  				页面生成 方法
	 */
	static String mkFilePath(String f)
	{
		String dir = Configuration.root().getString("app.dir");
		return dir + f;
	}
	static String mkFileName(FilePart f)
	{
		//只要后缀；
		String[] ff = StringTool.splitString(f.getFilename().toLowerCase(), ".");
		return "/public/actimage/"+StringUtil.getDateString() + "." + ff[ ff.length - 1 ];
	}
	
	static String fnn="";
	public static Result dataUp()
	{
		MultipartFormData body = request().body().asMultipartFormData();
		
		if( body!=null ){
			List<FilePart> file_img = body.getFiles();
			if( file_img.size()>0 ){
				FilePart fp = file_img.get(0);
				String fname= mkFileName(fp);
				Logger.info ( mkFilePath(fname) +" -- " 
						+fp.getFile().getAbsolutePath());
				
				String dest = mkFilePath(fname);
				FileOP.copy(fp.getFile().getAbsolutePath(),	dest );
				FileOP.delete(fp.getFile().getAbsolutePath());
				
				fnn= dest ;
				//
				return ok( dest );
			}
		}
		return ok("");
	}
	
	public static Result htmlView()
	{
		Entity entity = (Entity)Cache.get("DATA_CLASS");
		if( entity==null )
			return ok( PageMakerView.render(null) );
		response().setContentType("text/plain");
		
		return ok( PageMakerViewHtml.render( entity ,entity.getIdx() ) );
	}
	
	public static Result htmlTreeView()
	{
		Entity entity = (Entity)Cache.get("DATA_CLASS");
		if( entity==null )
			return ok( PageMakerView.render(null) );
		response().setContentType("text/plain");
		
		//return ok( PageMakerViewHtmlTree.render( entity ,entity.getIdx() ) );
		return ok( PageMakerViewHtml.render( entity ,entity.getIdx() ) );
	}

	public static Result routesView()
	{
		Entity entity = (Entity)Cache.get("DATA_CLASS");
		if( entity==null )
			return ok( PageMakerView.render(null) );
		response().setContentType("text/plain");
		
		return ok( PageMakerViewRouts.render( entity ) );
	}
	
	public static Result javaView()
	{
		Entity entity = (Entity)Cache.get("DATA_CLASS");
		if( entity==null )
			return ok( PageMakerView.render(null) );
		response().setContentType("text/plain");
		
		return ok( PageMakerViewJava.render( entity,entity.getIdx(),entity.name.toLowerCase() ) );
	}
	
	public static Result makeFile()
	{
		Entity entity = (Entity)Cache.get("DATA_CLASS");
		if( entity==null )
			return ok( PageMakerView.render(null) );
		
		if( entity.getIdx().length()==0 )
			return ok(PageMakerResult.render("",entity.name+"没有主键"));
		
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
		addFunctions( entity.name ); 
				
		MakeFile mf =new MakeFile(file1,javaTxt1,file2,javaTxt2,file3,oldRouts);
		//return ok("/"+entity.name+"/View");
		response().setHeader("Cache-Control", "no-cache");
		return ok(PageMakerResult.render("/"+entity.name+"/View",entity.name));
	}

	
	private static void addFunctions(String name) {
		//不要重复；
		if( Ebean.getServer(GlobalDBControl.getDB()).find(SysFunction.class).where()
			.eq("fcode", name).findRowCount()>0 )
			return;
		SysFunction sysfunction = new SysFunction ();
		int pid = 1;
		sysfunction.setParent( pid );
		sysfunction.setName(name);
		sysfunction.setLevel( 2 );
		sysfunction.setAddtime( new Date() );
		sysfunction.setFcode( name );
		sysfunction.setUrl("/"+name+"/View");
		sysfunction.setState(1);
		sysfunction.setIsMenu(1);
		Ebean.getServer(GlobalDBControl.getDB()).save(sysfunction);
		
		//add list
		SysFunction subfunction = new SysFunction ();
		subfunction.setName("搜索");
		subfunction.setParent( sysfunction.getIdx() );
		subfunction.setLevel( 3 );
		subfunction.setAddtime( new Date() );
		subfunction.setFcode( name+".list" );
		subfunction.setUrl("/"+name+"/List");
		subfunction.setState(1);
		subfunction.setIsMenu(0);
		
		Ebean.getServer(GlobalDBControl.getDB()).save(subfunction);
		
		//add modify
		subfunction = new SysFunction ();
		subfunction.setName("修改");
		subfunction.setParent( sysfunction.getIdx() );
		subfunction.setLevel( 3 );
		subfunction.setAddtime( new Date() );
		subfunction.setFcode( name+".modify" );
		subfunction.setUrl("/"+name+"/Modify");
		subfunction.setState(1);
		subfunction.setIsMenu(0);
		
		Ebean.getServer(GlobalDBControl.getDB()).save(subfunction);
		
		//add delete
		subfunction = new SysFunction ();
		subfunction.setName("删除");
		subfunction.setParent( sysfunction.getIdx() );
		subfunction.setLevel( 3 );
		subfunction.setAddtime( new Date() );
		subfunction.setFcode( name+".delete" );
		subfunction.setUrl("/"+name+"/Delete");
		subfunction.setState(1);
		subfunction.setIsMenu(0);
		
		Ebean.getServer(GlobalDBControl.getDB()).save(subfunction);
		
		//add get 
		subfunction = new SysFunction ();
		subfunction.setName("查看");
		subfunction.setParent( sysfunction.getIdx() );
		subfunction.setLevel( 3 );
		subfunction.setAddtime( new Date() );
		subfunction.setFcode( name +".get");
		subfunction.setUrl("/"+name+"/Get");
		subfunction.setState(1);
		subfunction.setIsMenu(0);
		
		Ebean.getServer(GlobalDBControl.getDB()).save(subfunction);
		
		//add enum
		subfunction = new SysFunction ();
		subfunction.setParent( sysfunction.getIdx() );
		subfunction.setName("枚举");
		subfunction.setLevel( 3 );
		subfunction.setAddtime( new Date() );
		subfunction.setFcode( name+".all" );
		subfunction.setUrl("/"+name+"/All");
		subfunction.setState(1);
		subfunction.setIsMenu(0);
		
		Ebean.getServer(GlobalDBControl.getDB()).save(subfunction);
	}
}

class MakeFile extends Thread
{
	String f1,f2,f3;
	String c1,c2,c3;
	public MakeFile(String f1,String c1,String f2,String c2,String f3,String c3)
	{
		this.f1=f1;
		this.f2=f2;
		this.f3=f3;
		this.c1=c1;
		this.c2=c2;
		this.c3=c3;
		this.start();
	}
	public void run()
	{
		FileOP.writeFile(f1, c1, "utf-8");
		FileOP.writeFile(f2, c2, "utf-8");
		FileOP.writeFile(f3, c3, "utf-8");
	}
}