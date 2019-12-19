package app.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import play.mvc.Result;
import controllers.SysConfigAction;
import models.SmartAppUser;
import models.SmartBounty;
import models.SmartVip;
import util.StringUtil;
import util.classEntity.StringTool;
import util.json.JsonUtil;
import app.dto.BountyMessageDto;
import app.dto.GetBountyDto;
import app.dto.GetBuyVipResultDto;
import app.dto.ReturnJson;
import app.dto.VipDto;
import app.service.UserService;
import app.service.VipService;
import app.util.AppUtil;

public class VipAction extends BaseAction{
	/**
	 * 功能说明:会员首页
	 * 请求方式:GET请求
	 * 接口地址:getVipMainInfo
	 * @return	
	 */
	public static Result getVipMainInfo(){
		//检查签名
		if(!checkSign()){
			//检查签名失败
			return makSignFailor();
		}
		//通用的返回给APP的JSON对象
		ReturnJson reJson = new ReturnJson();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		VipDto data = new VipDto();
		/*
		 * request().getQueryString()方法
		 * 只适用于get请求方式,获取url后面的请求参数
		 */
		String token = request().getQueryString("token");
		String appid = request().getQueryString("appid");
		//判断token是否为null
		if(StringUtil.isNull(token)){
			//如果为null,则返回参数错误(token不可以为null)
			reJson.setParamsError();
			//将对象转换为JSON字符串
			String reContent = JsonUtil.parseObj(reJson);
			//以debug方式打印日志
			appLogger.debug(reContent);
			return ok(reContent);
		}
		//根据token获取VIP页面 用户相关的数据
		SmartAppUser appUser = UserService.findUserByToken(token);
		if(appUser==null){
			//若appUser为null(说明token为null),则将设置为token过期,要重新登录
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
		if(!checkSign()){
			return makSignFailor();
		}
		ReturnJson reJson = new ReturnJson();
		String token = request().getQueryString("token");
		String appid = request().getQueryString("appid");
		GetBountyDto data = new GetBountyDto();
		if(StringUtil.isNull(token)){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		SmartAppUser appUser = UserService.findUserByToken(token);
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
		if(!checkSign()){
			return makSignFailor();
		}
		ReturnJson reJson = new ReturnJson();
		String token = request().getQueryString("token");
		String appid = request().getQueryString("appid");
		
		String pageno = request().getQueryString("pageno");
		String pagesize = request().getQueryString("pagesize");
		Integer npageno = StringTool.GetInt(pageno, 0);
		Integer npagesize = StringTool.GetInt(pagesize, 0);
		if(StringUtil.isNull(token) || npageno<=0 || npagesize<=0){
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
			SmartAppUser appUser = UserService.findUserByToken(token);
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
	
	/**
	 * 功能说明:购买会员结果页
	 * 请求方式:GET
	 * 接口地址:getBuyVipResult
	 */
	public static Result getBuyVipResult(){
		if(!checkSign()){
			return makSignFailor();
		}
		ReturnJson reJson = new ReturnJson();
		GetBuyVipResultDto data = new GetBuyVipResultDto();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
		String token = request().getQueryString("token");
		String appid = request().getQueryString("appid");
		if(StringUtil.isNull(token)){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		SmartAppUser appUser = UserService.findUserByToken(token);
		if(appUser==null){
			reJson.setTokenTimeOut();
		}else{
			//去smart_vip表中查看该用户overtime
			SmartVip vip = VipService.selectSmartVipByUserid(appUser.getIdd());
			if(vip!=null){
				data.setOvertime(sdf.format(vip.getOvertime()));
			}else{
				data.setOvertime(sdf.format(VipService.addOneYear(new Date(), 1)));
			}
			reJson.setSuccess();//返回状态码和信息
			reJson.setData(data);
		}
		play.Logger.info("Overtime:"+data.getOvertime());
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}
	
}