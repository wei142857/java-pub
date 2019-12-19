package app.gzh;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import app.dto.BountyMessageDto;
import app.dto.GetBountyDto;
import app.dto.ReturnJson;
import app.dto.VipDto;
import app.service.SubWxUserService;
import app.service.UserService;
import app.service.VipService;
import app.util.AppUtil;
import controllers.SysConfigAction;
import models.SmartAppUser;
import models.SmartBounty;
import models.SmartVip;
import models.SubWxUser;
import play.mvc.Result;
import util.GlobalSetting;
import util.StringUtil;
import util.classEntity.StringTool;
import util.json.JsonUtil;

public class VipAction extends BaseAction{
	/**
	 * 功能说明:会员首页
	 * 请求方式:GET请求
	 * 接口地址:getVipMainInfo
	 * @return	
	 */
	public static Result getVipMainInfo(){
		ReturnJson reJson = new ReturnJson();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		VipDto data = new VipDto();
		String openid = session(GlobalSetting.GZH_OPENID);
		if (StringUtil.isNull(openid)) {
			reJson.setCode(2);
			reJson.setMessage("非法登录,请关注微信：SKN智能生活" + "\t" + "公众号进行登录");
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		SubWxUser subWxUser = SubWxUserService.findIllegalNotifyWxUser(openid);
		
		SmartAppUser appUser = UserService.findUserByPhone(subWxUser.getPhone());
		if(appUser==null){
			reJson.setTokenTimeOut();
		}else{
			//设置页面需要的用户的相关信息
			data.setNickname(appUser.getNickname());//昵称
			data.setPhonenum(appUser.getPhone());//电话号码
			data.setAvatarurl(appUser.getLogo());//头像地址
			//根据userid获取对应的奖励金数据
			Integer amountBounty = VipService.findBountyByUserid(appUser.getIdd());
			data.setBvalue(""+amountBounty);
			//根据userid获取 即将过期的奖励金数据
			Integer disabledBouonty = VipService.findDisabledBountyByUserid(appUser.getIdd());
			data.setOverbvalue(""+disabledBouonty);
			//返回中间图片的地址
			data.setImgurl(SysConfigAction.findSysconfig("智能锁", "会员首页中间图"));
			//返回中间图片点击以后的地址
			data.setLinkurl(SysConfigAction.findSysconfig("智能锁", "会员首页中间图地址"));
			//返回会员价格
			data.setPrice(SysConfigAction.findSysconfig("智能锁", "会员价格"));
			//首先判断该用户	是否为会员用户/会员是否过期
			if(VipService.checkVip(appUser.getIdd())==1){
				//判断该用户今天是否领取过奖励金
				if(VipService.checkGetBounty(appUser.getIdd())){
					data.setInformgetbounty("1");
				}else{
					data.setInformgetbounty("0");
				}
			}else if(VipService.checkVip(appUser.getIdd())==2){
				data.setInformgetbounty("0");
			}else{
				data.setInformgetbounty("0");
			}
			//返回VIP对象
			SmartVip vip = VipService.selectSmartVipByUserid(appUser.getIdd());
			//返回过期时间
			if(vip!=null){
				//判断当前时间是否已过过期时间
				Date now = new Date();
				if(now.getTime()>vip.getOvertime().getTime()){
					data.setOvertime("已过期" + (now.getTime()-vip.getOvertime().getTime())/1000/60/60/24 + "天");
				}else{
					data.setOvertime(sdf.format(vip.getOvertime())+"到期");
				}
			}else{
				data.setOvertime("0");
			}
			//返回下面的items
			if(VipService.getVipMainImg(3)!=null){
				data.setItems(VipService.getVipMainImg(3));
			}
			play.Logger.info("剩余奖励金个数:"+data.getBvalue());
			//util.LogUtil.writeLog("剩余奖励金个数:"+data.getBvalue(), "addVipValidityTimeLog");
			reJson.setSuccess();//返回状态码
			reJson.setData(data);//返回数据
		}
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}
	
	/**
	 * 功能说明:领取奖励金
	 * 请求方式:GET请求
	 * 接口地址:getBounty
	 */
	public static Result getBounty(){
		ReturnJson reJson = new ReturnJson();
		GetBountyDto data = new GetBountyDto();
		String openid = session(GlobalSetting.GZH_OPENID);
		if (StringUtil.isNull(openid)) {
			reJson.setCode(2);
			reJson.setMessage("非法登录,请关注微信：SKN智能生活" + "\t" + "公众号进行登录");
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		SubWxUser subWxUser = SubWxUserService.findIllegalNotifyWxUser(openid);
		
		SmartAppUser appUser = UserService.findUserByPhone(subWxUser.getPhone());
		
		if(appUser==null){
			reJson.setTokenTimeOut();
		}else{
			//首先判断该用户	是否为会员用户/会员是否过期
			if(VipService.checkVip(appUser.getIdd())==1){
				//判断该用户今天是否领取过奖励金
				if(!VipService.checkGetBounty(appUser.getIdd())){
					Integer result = VipService.createGetBountyRecord(appUser.getIdd());
					if(result == 1){
						data.setRj("10");
						reJson.setSuccess();//返回状态码和信息
						reJson.setData(data);
					}else{
						reJson.setCode(100);
						reJson.setMessage("领取奖励金失败");
					}
				}else{
					reJson.setCode(101);
					reJson.setMessage("您今日已经领取过啦,请明日再来");
				}
			}else if(VipService.checkVip(appUser.getIdd())==2){
				reJson.setCode(102);
				reJson.setMessage("您尚未开通会员");
			}else{
				reJson.setCode(103);
				reJson.setMessage("您的会员已过期");
			}
		}
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}
	
	/**
	 * 功能说明:奖励金记录
	 * 请求方式:GET
	 * 接口地址:getBountyList
	 */
	public static Result getBountyList(){
		ReturnJson reJson = new ReturnJson();
		String openid = session(GlobalSetting.GZH_OPENID);
		if (StringUtil.isNull(openid)) {
			reJson.setCode(2);
			reJson.setMessage("非法登录,请关注微信：SKN智能生活" + "\t" + "公众号进行登录");
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		String pageno = request().getQueryString("pageno");
		String pagesize = request().getQueryString("pagesize");
		Integer npageno = StringTool.GetInt(pageno, 0);
		Integer npagesize = StringTool.GetInt(pagesize, 0);
		if(npageno<=0 || npagesize<=0){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		String querydate = request().getQueryString("querydate");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date nquerydate = sdf.parse(querydate);
			SubWxUser subWxUser = SubWxUserService.findIllegalNotifyWxUser(openid);
			
			SmartAppUser appUser = UserService.findUserByPhone(subWxUser.getPhone());
			if(appUser==null){
				reJson.setTokenTimeOut();
			}else{
				BountyMessageDto data = new BountyMessageDto();
				List<BountyMessageDto.Item> items = new ArrayList<BountyMessageDto.Item>(); 
				TreeMap<String,Object> params = new TreeMap<String,Object>();
				params.put("userid", appUser.getIdd());
				List<SmartBounty> bountyMessage = VipService.findBountyMessages(params, npageno-1, npagesize, querydate);
				Integer count = VipService.findBountyMessagesCountByCondition(params,querydate);
				for(SmartBounty message:bountyMessage){
					BountyMessageDto.Item item = new BountyMessageDto.Item();
					item.setTitle(message.getTitile());
					if(message.getAct()==1){
						item.setBvalue("+"+message.getBvalue());
					}else{
						item.setBvalue("-"+message.getBvalue());
					}
					item.setGaintime(""+sdf1.format(message.getAddtime()));
					if(message.getOvertime()!=null){
						item.setOvertime(""+sdf1.format(message.getOvertime()));
					}else{
						item.setOvertime("");
					}
					items.add(item);
				}
				//根据userid获取 本月获取的奖励金个数
				Integer monthBounty = VipService.findMonthBountyByUserid(appUser.getIdd(),nquerydate);
				data.setTotalbvalue(""+monthBounty);
				data.setTotalpages(AppUtil.getTotalPage(count, npagesize));
				data.setPageno(npageno);
				data.setItems(items);
				reJson.setSuccess();
				reJson.setData(data);
			}
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		} catch (ParseException e) {
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
	}
	
}
