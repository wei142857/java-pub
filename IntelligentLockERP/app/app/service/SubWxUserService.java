package app.service;

import java.util.List;

import util.GlobalDBControl;

import com.avaje.ebean.Ebean;

import models.SubWxUser;

public class SubWxUserService {
	public static int CH_SUB = 1;
	public static int CH_ILLEGAL_NOTIFY = 2;
	//预约安装绑定的微信用户
	public static SubWxUser findSubWxUser(String openid){
		 return Ebean.getServer(GlobalDBControl.getReadDB()).find(SubWxUser.class).where().eq("openid", openid).eq("ch", CH_SUB).findUnique();
	}
	//预约安装绑定的微信用户
	public static List<SubWxUser> findSubWxUsers(String  phone){
		return Ebean.getServer(GlobalDBControl.getReadDB()).find(SubWxUser.class).where().eq("phone", phone).eq("ch", CH_SUB).findList();
	}
	
	//非法通知绑定的微信用户+公众号登陆的用户
	public static SubWxUser findIllegalNotifyWxUser(String openid){
		return Ebean.getServer(GlobalDBControl.getReadDB()).find(SubWxUser.class).where().eq("openid", openid).eq("ch", CH_ILLEGAL_NOTIFY).findUnique();
	}
	
	//非法通知绑定的微信用户
	public static List<SubWxUser> findIllegalNotifyWxUsers(String phone){
		return Ebean.getServer(GlobalDBControl.getReadDB()).find(SubWxUser.class).where().eq("phone", phone).eq("ch", CH_ILLEGAL_NOTIFY).findList();
	}
}
