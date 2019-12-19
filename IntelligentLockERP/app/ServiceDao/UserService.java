package ServiceDao;

import java.util.List;

import models.SysUser;
import play.Logger;
import util.GlobalSetting;

import com.avaje.ebean.Ebean;

/*
 * 系统用户相关的方法；
 */
public class UserService {
	static UserService instance ;
	
	public static UserService getInstance()
	{
		if( instance == null )
			instance = new UserService();
		
		return instance;
	}
	
	//检查用户密码
	public SysUser checkUser(String name,String pass)
	{
		List<SysUser> usr = Ebean.getServer(GlobalSetting.defaultDB)
				.find(SysUser.class)
				.where().eq("login", name).findList();
		if( usr.size()==0 )
			return null;
		SysUser uu = usr.get(0);
		
		if( uu.getPwd()!=null && uu.getPwd().equals(pass) ){
			//修改原来的非Md5密码存储
			uu.setPwd("");
			uu.setPwdmd5(util.MD5.mkMd5(pass));
			Ebean.getServer(GlobalSetting.defaultDB).save(uu);
			return uu;
		}
		
		if( uu.getPwdmd5()!=null && (uu.getPwdmd5().equalsIgnoreCase(util.MD5.mkMd5(pass))
				||uu.getPwdmd5().equalsIgnoreCase(pass) )
				 )
		{
			return uu;
		}
		Logger.info(uu.getPwdmd5()+" -- "+util.MD5.mkMd5(pass));
			
		return null;
	}
	
}
