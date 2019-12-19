package controllers;

import java.util.List;

import net.SmartMsgUtil;
import models.SmartNetMsg;
import models.SysUser;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import util.GlobalSetting;
import util.Rc4Util;
import util.SecurityUtil;
import util.StringUtil;
import util.classEntity.StringTool;
import util.classEntity.TreeNode;
import util.jedis.RedisUtil;
import views.html.page.*;

import com.avaje.ebean.Ebean;

@Security.Authenticated(Secured.class)
public class Application extends Controller {

	
	
	public static Result index() {
		String menustr = "";
		return ok(index.render(menustr));
	}
	
	//获取功能列表；
	public static Result functions()
	{
		response().setHeader("Cache-Control", "no-cache");
		SysUser usr = getSysUser();
		List<TreeNode> funcs =null;
		if(usr!=null)
			funcs = RedisUtil.getInstance().getEntity(usr.getIdx()+"_security", SecurityUtil.class, 0).showPermissions();
		return ok(Json.toJson( funcs ));		
	}
	
	public static SysUser getSysUser()
	{
		String ss = session(GlobalSetting.Session_User_Id);
		if(ss==null)
			return null;
		return RedisUtil.getInstance().getEntity(ss, SysUser.class, 0);
	}
	
	//修改用户密码
	public static Result modifyPass() {
		return ok( xiugaipass.render());
	}
	
	public static Result modifyPassAct(){
		String oldpass = StringUtil.getHttpParam(request(), "oldpass");
		String newpass = StringUtil.getHttpParam(request(), "newpass");
		SysUser usr = getSysUser();
		Logger.info(oldpass+" -- "+ newpass+" --"+usr);
		
		if(newpass==null||oldpass==null)
			return ok("密码参数没有输入。");
		if( newpass.length()<6||newpass.length()>16 )
			return ok("密码长度6-16。");
		
		if(!util.MD5.mkMd5(oldpass).equals(usr.getPwdmd5()))
			return ok("用户原密码输入不正确。");
		
		SysUser usr0 = Ebean.getServer(GlobalSetting.defaultDB)
				.find(SysUser.class)
				.where().eq("login", usr.getLogin()).findUnique();
		if( usr0==null )
			return ok("用户信息未找到。");
		
		//修改密码；
		usr0.setPwdmd5(util.MD5.mkMd5(newpass));
		Ebean.getServer(GlobalSetting.defaultDB).save(usr0);
		return ok("ok");
	}
	
	public static Result goDept() {
		return ok( deptPass.render());
	}
	
	public static Result dept(){
		String msg = StringUtil.getHttpParam(request(), "msg");
		String pass = StringUtil.getHttpParam(request(), "pass");
		
		int leng = msg.length()/2;
		byte[] data = StringTool.getFromHexString(msg) ;
		byte[] dataN = Rc4Util.RC4Base( data , pass );
		SmartNetMsg m = SmartMsgUtil.makeFromBytes( dataN , leng );
		
		if(m==null)
			return ok("解密消息失败");
		
		String ret = "解密后(HEX编码)："+StringTool.makeHexString(dataN) +"<br>";
		return ok( ret + "<br><hr><br>消息体解析：<br>" + m.toStringHtml(m,StringTool.makeHexString(dataN)) );
	}
	
}