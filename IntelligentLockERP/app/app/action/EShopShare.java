package app.action;

import models.EShopProduct;
import models.ShareRedisObj;
import models.SmartAppUser;
import play.mvc.Result;
import util.GlobalDBControl;
import util.StringUtil;
import util.classEntity.StringTool;
import util.jedis.RedisUtil;
import util.json.JsonUtil;
import app.dto.ReturnJson;
import app.dto.ShareEShopMainInfoDto;
import app.dto.ShareEShopProductDto;
import app.service.UserService;

import com.avaje.ebean.Ebean;

import controllers.SysConfigAction;

public class EShopShare extends BaseAction{
	
	public static String shareEShopMainInfo_ASE= "eshopPay20190727";
	static Integer shareEShopMainInfo_ASE_Time = -1;
	static Integer shareEShopMainInfo_ASE_INDEX = 0;
	
	/**
	 * 功能说明:分享有礼(商城首页信息)
	 * 请求方式:GET
	 * 接口地址:shareEShopMainInfo
	 */
	public static Result shareEShopMainInfo(){
		ShareEShopMainInfoDto data = new ShareEShopMainInfoDto();
		ShareRedisObj obj = new ShareRedisObj();
		ReturnJson reJson = new ReturnJson();
		//从session中获取用户信息
		String token = request().getQueryString("token");
		if(token==null){
			token = session("token");
		}
		if(StringUtil.isNull(token)){	//没有token信息
			data.setTitle(SysConfigAction.findSysconfig("智能锁", "分享有礼分享标题"));
			data.setDesc(SysConfigAction.findSysconfig("智能锁", "分享有礼分享描述"));
			data.setImgurl(SysConfigAction.findSysconfig("智能锁", "分享有礼分享图标"));
			data.setLinkurl(SysConfigAction.findSysconfig("智能锁", "分享有礼分享链接"));
		}else{
			SmartAppUser appUser = UserService.findUserByToken(token);
			if(appUser==null){
				data.setTitle(SysConfigAction.findSysconfig("智能锁", "分享有礼分享标题"));
				data.setDesc(SysConfigAction.findSysconfig("智能锁", "分享有礼分享描述"));
				data.setImgurl(SysConfigAction.findSysconfig("智能锁", "分享有礼分享图标"));
				data.setLinkurl(SysConfigAction.findSysconfig("智能锁", "分享有礼分享链接"));
			}else{
				data.setTitle(SysConfigAction.findSysconfig("智能锁", "分享有礼分享标题"));
				data.setDesc(SysConfigAction.findSysconfig("智能锁", "分享有礼分享描述"));
				data.setImgurl(SysConfigAction.findSysconfig("智能锁", "分享有礼分享图标"));
				//从token中获取到手机号
				Integer userid = appUser.getIdd();
				obj.setUserid(userid);
				try {
					//得到加密后的电话号码
					String aseKey = Service.unicom.util.AESUtil.encrypt(obj.getUserid()+"", shareEShopMainInfo_ASE);
					RedisUtil.getInstance().setEntity(aseKey, obj, shareEShopMainInfo_ASE_Time, shareEShopMainInfo_ASE_INDEX);
					String linkUrl = SysConfigAction.findSysconfig("智能锁", "分享有礼分享链接");
					if(linkUrl.indexOf("?")==-1){
						data.setLinkurl(linkUrl+"?source="+aseKey);
					}else{
						data.setLinkurl(linkUrl+"&source="+aseKey);
					}
				} catch (Exception e) {
					reJson.setMessage("分享出错");
					String reContent = JsonUtil.parseObj(reJson);
					util.LogUtil.writeLog("AES加密过程出错:Exception:"+e.getMessage(), "EShopLog");
					appLogger.debug(reContent);
					return ok(reContent);
				}
			}
		}
		reJson.setSuccess();//返回状态码
		reJson.setData(data);//返回数据
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}
	
	
	/**
	 * 功能说明:分享商城商品(商品详情页)
	 * 请求方式:GET
	 * 接口地址:shareEShopProduct
	 */
	public static Result shareEShopProduct(){
		ShareEShopProductDto data = new ShareEShopProductDto();
		ShareRedisObj obj = new ShareRedisObj();
		ReturnJson reJson = new ReturnJson();
		String pid = request().getQueryString("pid");
		if(StringUtil.isNull(pid)){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		Integer npid = StringTool.GetInt(pid, 0);
		//查询商品信息
		EShopProduct product = Ebean.getServer(GlobalDBControl.getDB()).find(EShopProduct.class)
			.where().eq("idd", npid).findUnique();
		//检查该商品是否存在
		if(product==null){
			reJson.setParamsError();
			reJson.setMessage("分享的该商品不存在");
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		//判断商品状态
		if(product.getStatus()==-1){
			reJson.setParamsError();
			reJson.setMessage("分享的该商品已经下架");
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		data.setTitle(product.getSharetitle());
		data.setDesc(product.getSharedesc());
		data.setImgurl(product.getShareicon());
		//从session中获取用户信息
		String token = request().getQueryString("token");
		if(token==null){
			token = session("token");
		}
		if(StringUtil.isNull(token)){	//没有token信息
			data.setLinkurl(product.getSharelink());
		}else{
			SmartAppUser appUser = UserService.findUserByToken(token);
			if(appUser==null){
				data.setLinkurl(product.getSharelink());
			}else{	//有用户信息
				Integer userid = appUser.getIdd();
				obj.setUserid( userid );
				try {
					//得到加密后的电话号码
					String aseKey = Service.unicom.util.AESUtil.encrypt(""+obj.getUserid(), shareEShopMainInfo_ASE);
					RedisUtil.getInstance().setEntity(aseKey, obj, shareEShopMainInfo_ASE_Time, shareEShopMainInfo_ASE_INDEX);
					String linkUrl = product.getSharelink();
					if(linkUrl.indexOf("?")==-1){
						data.setLinkurl(linkUrl+"?source="+aseKey);
					}else{
						data.setLinkurl(linkUrl+"&source="+aseKey);
					}
				} catch (Exception e) {
					reJson.setMessage("分享出错");
					String reContent = JsonUtil.parseObj(reJson);
					util.LogUtil.writeLog("AES加密过程出错:Exception:"+e.getMessage(), "EShopLog");
					appLogger.debug(reContent);
					return ok(reContent);
				}
			}
		}
		reJson.setSuccess();//返回状态码
		reJson.setData(data);//返回数据
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}
	
	
}
