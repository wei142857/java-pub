package app.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.ReturnMessage;
import models.SubCards;
import models.SubInstallOrders;
import models.SubMsg;
import models.SubOrders;
import models.SubWxUser;
import play.Logger;
import play.libs.Json;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import util.GlobalDBControl;
import util.GlobalSetting;
import util.ImageUploadUtil;
import util.StringUtil;
import util.classEntity.StringTool;
import util.json.JsonUtil;
import ServiceDao.WxGzhService;
import app.dto.AppointBackFillAddressDto;
import app.dto.AppointInitOrderDto;
import app.dto.ReturnJson;
import app.service.SubMsgService;
import app.service.SubOrdersService;
import app.service.SubWxUserService;

import com.avaje.ebean.Ebean;
/**
 * 返回的状态码:
 * findOrderByOpenid()
 * 0:成功
 * 1:未找到订单
 * 2:openid为null非法登录
 * 
 * createInstallOrd()
 * 0:成功
 * 1:openid为null
 * 2:Wx_User为null
 * 3:提交的信息有误.
 * 
 * selectInstallOrd()
 * 0:成功
 * 1:openid为null
 * 2:Wx_User为null
 * 3:未找到订单信息
 * 
 * selectInstallOrdDetail()
 * 0：成功
 * 1：参数错误
 * 2：数据错误,根据oid没找到该笔订单
 * 
 * selectInstallOrdPro()
 * 0:成功
 * 1:参数错误(oid state为null或者非法)
 * 2:数据错误,无进度信息
 * 
 * @author 陈宏亮
 *
 */
public class AppointInstallAction extends BaseAction{
	
	//用户上传图片
	public static Result uploadImg(){
		ReturnMessage returnMessage = new ReturnMessage();
		MultipartFormData uploadImage = request().body().asMultipartFormData();
		if(uploadImage!=null){
			FilePart file = uploadImage.getFile("imgFile");
			if(file!=null){
				String imagePath = ImageUploadUtil.putImg(file);
				returnMessage.setCode("0");
				returnMessage.setMessage(imagePath);
				return ok(Json.toJson(returnMessage));
			}
		}
		returnMessage.setCode("1");
		returnMessage.setMessage("上传的照片无效(null)");
		return ok(Json.toJson(returnMessage));
	}
	
	//根据openid 判断 并返回 收货地址 信息
	public static Result findOrderByOpenid(){
		ReturnJson reJson = new ReturnJson();
		AppointBackFillAddressDto data = new AppointBackFillAddressDto();
		try{
			String state = request().getQueryString("state");
			String openid = session(GlobalSetting.GZH_OPENID);
			if(StringUtil.isNull(openid)){
				reJson.setCode(2);
				reJson.setMessage("非法登录,请关注微信：SKN智能生活"+"\t"+"公众号进行登录");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			//根据openid获取到Wx_user
			SubWxUser subWxUser = SubWxUserService.findSubWxUser(openid);
			if(subWxUser!=null){
				if("102".equals(state)){
					//根据 WxUser的phone查找是否有相应的订单
					SubOrders subOrder = SubOrdersService.findToInstallSubOrder(subWxUser.getPhone());
					if(subOrder!=null){
						data.setOid(subOrder.getIdd());
						data.setName(subOrder.getName());
						data.setPhone(subOrder.getPhone());
						data.setArea(subOrder.getArea());
						data.setAddress(subOrder.getAddress());
					}
				}
				if("103".equals(state)){
					//根据 WxUser的phone查找是否有相应的订单
					SubOrders subOrder = SubOrdersService.findToMeasureSubOrder(subWxUser.getPhone());
					if(subOrder!=null){
						data.setOid(subOrder.getIdd());
						data.setName(subOrder.getName());
						data.setPhone(subOrder.getPhone());
						data.setArea(subOrder.getArea());
						data.setAddress(subOrder.getAddress());
					}
				}
				//有数据 返回成功
				reJson.setCode(0);
				reJson.setData(data);
				reJson.setMessage("成功");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			//没数据
			reJson.setCode(1);
			reJson.setMessage("无该用户订单信息");
		}catch(Exception e){
			Logger.error("appointInstallAction", e.getMessage());
		}
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}
	
	//将用户上传的数据保存 到预约安装 订单表中
	public static Result createInstallOrd(){
		ReturnJson reJson = new ReturnJson();
		try{
			//从session中获取openid
			String openid = session(GlobalSetting.GZH_OPENID);
			if(StringUtil.isNull(openid)){
				reJson.setCode(1);
				reJson.setMessage("非法登录,请关注微信：SKN智能生活"+"\t"+"公众号进行登录");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			//根据openid去Wx_User表中查找
			SubWxUser subWxUser = SubWxUserService.findSubWxUser(openid);
			if(subWxUser==null){
				reJson.setCode(2);
				reJson.setMessage("登录异常,请刷新后重新登录");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			Map<String,String> map = new HashMap<String,String>();
			//oid单独验证 如果有存入如果没有则 不进行存储
			String oid = request().getQueryString("oid");
			Integer noid = StringTool.GetInt(oid, 0);
			//门厚度
			String doorthickness = request().getQueryString("doorthickness");
			Integer ndoorthickness = StringTool.GetInt(doorthickness, 0);
			map.put("门厚度",doorthickness);
			//导向片宽度
			String slicewidth = request().getQueryString("slicewidth");
			Integer nslicewidth = StringTool.GetInt(slicewidth, 0);
			map.put("导向片宽度",slicewidth);
			//导向片高度
			String sliceheight = request().getQueryString("sliceheight");
			Integer nsliceheight = StringTool.GetInt(sliceheight, 0);
			map.put("导向片高度",sliceheight);
			//上传图片1 2
			String lockimg1 = request().getQueryString("lockimg1");
			map.put("左侧图片",lockimg1);
			String lockimg2 = request().getQueryString("lockimg2");
			map.put("右侧图片",lockimg2);
			//天地钩
			String hook = request().getQueryString("hook");
			Integer nhook = StringTool.GetInt(hook, 0);
			map.put("天地钩",hook);
			//开门方向
			String lockdirection = request().getQueryString("lockdirection");
			Integer nlockdirection = StringTool.GetInt(lockdirection, 0);
			map.put("开门方向",lockdirection);
			//收货人信息
			String consigneename = request().getQueryString("consigneename");
			map.put("收货人姓名",consigneename);
			String consigneearea = request().getQueryString("consigneearea");
			map.put("收货人地区",consigneearea);
			String consigneeaddress = request().getQueryString("consigneeaddress");
			map.put("收货人详细地址",consigneeaddress);
			String consigneephone = request().getQueryString("consigneephone");
			map.put("收货人手机号",consigneephone);
			//联系人信息
			String installname = request().getQueryString("installname");
			map.put("联系人姓名",installname);
			String installarea = request().getQueryString("installarea");
			map.put("联系人地区",installarea);
			String installaddress = request().getQueryString("installaddress");
			map.put("联系人详细地址",installaddress);
			String installphone = request().getQueryString("installphone");
			map.put("联系人手机号",installphone);
			//遍历并验证
			for(Map.Entry<String, String> entry:map.entrySet()){
				if(StringUtil.isNull(entry.getValue())){
					reJson.setCode(3);
					reJson.setMessage(entry.getKey()+"数据上传失败,请重新填写并上传");
					String reContent = JsonUtil.parseObj(reJson);
					appLogger.debug(reContent);
					return ok(reContent);
				}
				//验证是否能转换为Ingeter类型
				if(entry.getKey().equals("门厚度")||entry.getKey().equals("导向片宽度")||
						entry.getKey().equals("导向片高度")||entry.getKey().equals("天地钩")||
						entry.getKey().equals("开门方向")){
					if(StringTool.GetInt(entry.getValue(),0)==0){
						reJson.setCode(3);
						reJson.setMessage(entry.getKey()+"数据错误,请重新填写并上传");
						String reContent = JsonUtil.parseObj(reJson);
						appLogger.debug(reContent);
						return ok(reContent);
					}
				}
				if(entry.getKey().equals("联系人手机号")||entry.getKey().equals("收货人手机号")){
					if(!StringUtil.isMobileNO(entry.getValue())){
						reJson.setCode(3);
						reJson.setMessage(entry.getKey()+"数据格式错误,请重新填写并上传");
						String reContent = JsonUtil.parseObj(reJson);
						appLogger.debug(reContent);
						return ok(reContent);
					}
				}
			}
			//获取idd(预约安装订单的idd)
			String idd = request().getQueryString("idd");
			Integer nidd = StringUtil.GetInt(idd, 0);
			SubInstallOrders insOrd;
			//判断 如果有则找到该笔订单进行修改,如果没有则将页面提交的数据进行保存
			if(nidd!=0){
				//去数据库中根据idd查找该订单 并进行修改
				insOrd = selAppInsOrdByIdd(nidd);
				if(insOrd!=null){
					insOrd.setIdd(nidd);
				}else{
					insOrd = new SubInstallOrders();
				}
			}else{
				insOrd = new SubInstallOrders();
			}
			//保存到数据库
			//将传过来的 预约安装 订单信息保存到数据库
			insOrd.setPhone(subWxUser.getPhone());
			if(!StringUtil.isNull(oid)&&noid!=0){
				insOrd.setOid(noid);
			}
			if(nidd==0){
				insOrd.setStatus(1);
				insOrd.setAddtime(new Date());
			}
			insOrd.setDoorthickness(ndoorthickness);
			insOrd.setSlicewidth(nslicewidth);
			insOrd.setSliceheight(nsliceheight);
			insOrd.setLockimg1(lockimg1);
			insOrd.setLockimg2(lockimg2);
			insOrd.setHook(nhook);
			insOrd.setLockdirection(nlockdirection);
			insOrd.setConsigneename(consigneename);
			insOrd.setConsigneephone(consigneephone);
			insOrd.setConsigneearea(consigneearea);
			insOrd.setConsigneeaddress(consigneeaddress);
			insOrd.setInstallname(installname);
			insOrd.setInstallphone(installphone);
			insOrd.setInstallarea(installarea);
			insOrd.setInstalladdress(installaddress);
			//cid 物流信息 和 安装费用 设置
			
			SubOrders subOrder = selSubOrdByIdd(noid);
			if(!"offline".equals(subOrder.getCh())){//线上订单免安装费用
				
				insOrd.setStatus(2);
				Ebean.getServer(GlobalDBControl.getDB()).save(insOrd);
				
				Ebean.getServer(GlobalDBControl.getDB())
				.createSqlUpdate("update sub_orders set isinstall=0 where idd=:idd")
				.setParameter("idd", subOrder.getIdd())
				.execute();
				SubMsgService.addMsg(insOrd.getPhone(), "您已预约成功，我们会火速为您安排发货、安装，如有问题可以致电408881781", SubMsgService.ACT_INSTALL, insOrd.getIdd());
				WxGzhService.sendGzhMessage(insOrd.getPhone(), "尊敬的用户，您预约成功", "预约安装", "待发货", StringUtil.getDateTimeStr(new Date()), "我们会火速为您安排发货、安装，如有问题可以致电408881781");
				reJson.setCode(4);
				reJson.setMessage("免费安装订单");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			
			Ebean.getServer(GlobalDBControl.getDB()).save(insOrd);
			reJson.setCode(0);
			reJson.setMessage("成功");
			reJson.setData(insOrd.getIdd());  //提交成功后将 预约安装订单id 返回
		}catch(Exception e){
			Logger.error("appointInstallAction", e.getMessage());
		}
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}
	
	//根据openid 查找并返回 用户购买的 锁名称以及预约订单id 状态 和 商品小图
	public static Result selectInstallOrd(){
		ReturnJson reJson = new ReturnJson();
		try{
			String openid = session(GlobalSetting.GZH_OPENID);
			if(StringUtil.isNull(openid)){
				reJson.setCode(1);
				reJson.setMessage("非法登录,请关注微信：SKN智能生活"+"\t"+"公众号进行登录");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			//根据openid获取到Wx_user
			SubWxUser subWxUser = SubWxUserService.findSubWxUser(openid);
			if(subWxUser==null){
				reJson.setCode(2);
				reJson.setMessage("登录状态异常，请重新登录。");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			//查找该phone下的预约订单(非待支付状态)
			List<SubInstallOrders> list = selAppInsOrdByPho(subWxUser.getPhone());
			if(list!=null&&list.size()>0){
				List<AppointInitOrderDto> dtoList = new ArrayList<AppointInitOrderDto>();
				//遍历集合取出
				for(SubInstallOrders order:list){
					AppointInitOrderDto dto = new AppointInitOrderDto();
					if(order.getStatus()==2){
						dto.setStatus("待发货");
					}else if(order.getStatus()==3){
						dto.setStatus("待派单");
					}else{	//status==4
						dto.setStatus("已派单");
					}
					dto.setIdd(order.getIdd()+"");
					//根据oid(subOrder的id)找出购买锁的名称
					SubOrders subOrder = selSubOrdByIdd(order.getOid());
					dto.setTitle(subOrder.getTitle());
					dto.setSmallimgurl(subOrder.getSmallimgurl());
					dtoList.add(dto);
				}
				reJson.setData(dtoList);
				reJson.setCode(0);
				reJson.setMessage("成功");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			reJson.setCode(3);
			reJson.setMessage("未查到预约订单信息");
		}catch(Exception e){
			Logger.error("appointInstallAction", e.getMessage());
		}
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}
	
	//根据oid和state 查看某笔 预约订单的进度 并返回
	public static Result selectInstallOrdPro(){
		ReturnJson reJson = new ReturnJson();
		try{
			String oid = request().getQueryString("oid");
			String state = request().getQueryString("state");
			Integer noid =  StringUtil.GetInt(oid, 0);
			Integer nstate = StringUtil.GetInt(state, 0);
			if(StringUtil.isNull(oid)||StringUtil.isNull(state)||noid==0||nstate==0){
				reJson.setCode(1);
				reJson.setMessage("获取数据失败,请稍后再试");	//参数错误
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			Integer act = 0;
			if(nstate==102){
				act = 1;
			}else if(nstate==103){
				act = 2;
			}
			//根据oid和state找出响应的 进度信息
			List<SubMsg> list = selSubMsg(noid,act);
			if(list!=null && list.size()>0){
				reJson.setData(list);
				reJson.setCode(0);
				reJson.setMessage("成功");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			reJson.setCode(2);
			reJson.setMessage("数据错误,无进度信息");
		}catch(Exception e){
			Logger.error("appointInstallAction", e.getMessage());
		}
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}
	
	//根据oid 查找 某笔 预约订单详情 并返回
	public static Result selectInstallOrdDetail(){
		ReturnJson reJson = new ReturnJson();
		try{
			String oid = request().getQueryString("oid");
			Integer noid =  StringUtil.GetInt(oid, 0);
			if(StringUtil.isNull(oid)||noid==0){
				reJson.setCode(1);
				reJson.setMessage("获取数据失败,请稍后再试");	//参数错误
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			//查找该oid 下的预约订单
			SubInstallOrders order = selAppInsOrdByIdd(noid);
			if(order!=null){
				if(order.getMoney()!=null){
					order.setMoney(order.getMoney()/100);
				}
				reJson.setData(order);
				reJson.setCode(0);
				reJson.setMessage("成功");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			reJson.setCode(2);
			reJson.setMessage("未查到预约订单信息");
		}catch(Exception e){
			Logger.error("appointInstallAction", e.getMessage());
		}
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}
	
	public static Result buySubscribe_pass(){
		String pass = request().getQueryString("pass");
		String openid = session(GlobalSetting.GZH_OPENID);
		String oid = request().getQueryString("oid");
		ReturnJson reJson = new ReturnJson();
		if(openid!=null){
			SubWxUser user = SubWxUserService.findSubWxUser(openid);
			if(user!=null){
				SubInstallOrders subOrder = Ebean.getServer(GlobalDBControl.getReadDB())
						.find(SubInstallOrders.class).where().eq("phone", user.getPhone()).eq("idd", oid).findUnique();
				
				if(subOrder==null || subOrder.getStatus()!=1){
					reJson.setCode(3);
					reJson.setMessage("订单已结束");
					String reContent = JsonUtil.parseObj(reJson);
					appLogger.debug(reContent);
					return ok(reContent);
				}
				
				SubCards subCard = Ebean.getServer(GlobalDBControl.getReadDB()).find(SubCards.class).where().eq("pass", pass).findUnique();
				if(subCard==null || subCard.getStatus()!=1){
					reJson.setCode(4);
					reJson.setMessage("兑换码无效");
					String reContent = JsonUtil.parseObj(reJson);
					appLogger.debug(reContent);
					return ok(reContent);
				}
				
				Ebean.getServer(GlobalDBControl.getDB()).createSqlUpdate("update sub_cards set status=0 where idd=:idd").setParameter("idd", subCard.getIdd()).execute();
				
				Ebean.getServer(GlobalDBControl.getDB())
				.createSqlUpdate("update sub_install_orders set cid=:cid,status=2,updatetime=sysdate() where idd=:oid")
				.setParameter("cid", subCard.getIdd())
				.setParameter("oid", oid)
				.execute();
				
				Ebean.getServer(GlobalDBControl.getDB())
				.createSqlUpdate("update sub_orders set isinstall=0 where idd=:idd")
				.setParameter("idd", subOrder.getOid()).execute();
				
				SubMsgService.addMsg(subOrder.getPhone(), "您已预约成功，我们会火速为您安排发货、安装，如有问题可以致电408881781", SubMsgService.ACT_INSTALL, subOrder.getIdd());
				WxGzhService.sendGzhMessage(subOrder.getPhone(), "尊敬的用户，您预约成功", "预约安装", "待发货", StringUtil.getDateTimeStr(new Date()), "我们会火速为您安排发货、安装，如有问题可以致电408881781");
			
				
				reJson.setCode(0);
				reJson.setMessage("成功");
			}
		}
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}
	
	//查找该phone下的预约订单(非待支付状态)
	public static List<SubInstallOrders> selAppInsOrdByPho(String phone){
		return Ebean.getServer(GlobalDBControl.getDB()).createQuery(SubInstallOrders.class)
			.where().eq("phone", phone).ne("status", "1").findList();
	}
	
	//查找该idd 下的预约订单
	public static SubInstallOrders selAppInsOrdByIdd(Integer idd){
		return Ebean.getServer(GlobalDBControl.getDB()).find(SubInstallOrders.class)
				.where().eq("idd", idd).findUnique();
	}
	
	//根据oid(subOrder的id)找出subOrders
	public static SubOrders selSubOrdByIdd(Integer idd){
		return Ebean.getServer(GlobalDBControl.getDB()).find(SubOrders.class).where().eq("idd", idd).findUnique();
	}
	
	//根据oid和state 去 sub_msg中 查找数据
	public static List<SubMsg> selSubMsg(Integer oid,Integer act){
		return Ebean.getServer(GlobalDBControl.getDB()).createQuery(SubMsg.class)
				.where().eq("oid", oid).eq("act", act).order("addtime desc").findList();
	}
}
