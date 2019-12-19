package controllers;


import java.util.Date;

import models.SysUser;
import models.Syslog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.Configuration;
import play.mvc.Http.Context;
import play.mvc.Http.Request;
import play.mvc.Result;
import play.mvc.Security;
import util.AjaxHellper;
import util.CookieHelper;
import util.GlobalDBControl;
import util.GlobalSetting;
import util.SecurityUtil;
import util.classEntity.TreeNode;
import util.jedis.RedisUtil;

import com.avaje.ebean.Ebean;
public class Secured extends Security.Authenticator {
	static Logger logger = LoggerFactory.getLogger(Secured.class);
    @Override
    public String getUsername(Context ctx) {
    	logger.info(ctx.request().uri());
    	if(ctx.request().body().asMultipartFormData()!=null)
    	{//upload 方式的请求 没有session
    		return "";
    	}
    	//查看用户是否超时: 从Cookies里面获取上次活动时间，看间隔是否超过了系统配置里面的时间
        String timelast =  CookieHelper.getCookie(ctx.request(), GlobalSetting.Cookie_Timestamp );
        long time_l = -1;
        try{
        	if( timelast != null ){
        		time_l = Long.parseLong( timelast );
        		time_l = System.currentTimeMillis() - time_l;
        	}
        }catch(Exception e)
        {
        	return null;
        }
        //读取在conf下的application下正定义的变量值
        int set = Configuration.root().getInt("web.timeout")*1000;
        if( time_l<0 || time_l >set  )
        	return null;
        
        //刷新活动时间;
        ctx.response().setCookie(GlobalSetting.Cookie_Timestamp, ""+System.currentTimeMillis() );               
        
        String ss = ctx.session().get(GlobalSetting.Session_User_Id);
       
        if(ss!=null){
        	SysUser suser = RedisUtil.getInstance().getEntity(ss, SysUser.class, 0);
        	if(suser!=null){
        		SecurityUtil su = RedisUtil.getInstance().getEntity(suser.getIdx()+"_security", SecurityUtil.class, 0);
            	if(su!=null ){
            		//枚舉類型不攔截
            		if( ctx.request().uri()!=null && ctx.request().uri().indexOf("/All")!=-1 )
            			return ss;
            		
            		TreeNode tn=su.hasPermission(ctx.request().uri() );
            		if( tn != null ){
            			if( tn.isMenu==0 ) //功能点
            				recordLog( ctx.request(),tn,suser );
            			return ss;
            		}
            	}
            	
            	if(suser!=null)
            		logger.info("!!! "+suser.getLogin()+" NO auth -- "+ctx.request().uri() );
        	}
        }
        
        //获取用户信息
        return null;
    }
    
    //不记录的模块编码
    static String[] filters = new String[]{".list",".get",".index",".all",".treedata"};
    //通用日志
    private void recordLog(Request request, TreeNode tn,SysUser suser) {
//    	for(int i=0;i<filters.length;i++){
//    		if(tn.fcode!=null&&tn.fcode.indexOf(filters[i])!=-1)
//    			return;
//    	}
    	
		Syslog log =new Syslog();
		log.setAddtime(new Date());
		log.setUser(suser.getLogin());
		log.setIp(AjaxHellper.getIpAddr(request));
		log.setFcode(tn.fcode);
		log.setModule(tn.text);
		log.setUri(request.uri());
		log.setMsg(AjaxHellper.getAllHttpParams(request));
		Ebean.getServer(GlobalDBControl.getDB()).save(log);
	}

	@Override
    public Result onUnauthorized(Context ctx) {
		logger.info("!!! no auth -- "+ctx.request().uri());
    	if( ctx.request().uri().equals("/mng/" )||ctx.request().uri().equals("/mng" ) ){
    		return redirect("/mng/logout");//routes.Login.logout());
    	}
    	return ok("不能访问："+ctx.request().uri()+" , 请重新登录 (或者找管理员开放权限）。");
    	
    	//return redirect(routes.Login.logout());
    }
    
    
}