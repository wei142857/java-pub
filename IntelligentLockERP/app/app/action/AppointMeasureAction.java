package app.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.SubMeasureOrders;
import models.SubOrders;
import models.SubWxUser;
import play.Logger;
import play.mvc.Result;
import util.GlobalDBControl;
import util.GlobalSetting;
import util.StringUtil;
import util.classEntity.StringTool;
import util.json.JsonUtil;
import app.dto.AppointInitOrderDto;
import app.dto.ReturnJson;
import app.service.SubWxUserService;

import com.avaje.ebean.Ebean;
/**
 * createMeasureOrd()
 * 返回的状态码:
 * 0:成功
 * 1:非法登录:openid为null
 * 2:Wx_User为null
 * 3:提交的信息有误
 * @author 陈宏亮
 *
 */
public class AppointMeasureAction extends BaseAction{
	
	//生成测量订单的方法
	public static Result createMeasureOrd(){
		
		ReturnJson reJson = new ReturnJson();
		try{
			String idd = request().getQueryString("idd");
			Integer nidd = StringUtil.GetInt(idd, 0);
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
				reJson.setMessage("登录异常,请重新登录");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			//oid单独验证 如果有存入如果没有则 不进行存储
			String oid = request().getQueryString("oid");
			Integer noid = StringTool.GetInt(oid, 0);
			Map<String,String> map = new HashMap<String,String>();
			//联系人信息
			String measurename = request().getQueryString("measurename");
			map.put("联系人姓名", measurename);
			String measurephone = request().getQueryString("measurephone");
			map.put("联系人手机号", measurephone);
			String measurearea = request().getQueryString("measurearea");
			map.put("安装地区", measurearea);
			String measureaddress = request().getQueryString("measureaddress");
			map.put("详细地址", measureaddress);
			String measuretime = request().getQueryString("measuretime");
			map.put("预约测量时间", measuretime);
			
			//遍历并验证
			for(Map.Entry<String, String> entry:map.entrySet()){
				if(StringUtil.isNull(entry.getValue())){
					reJson.setCode(3);
					reJson.setMessage(entry.getKey()+"数据上传失败,请重新填写并上传");
					String reContent = JsonUtil.parseObj(reJson);
					appLogger.debug(reContent);
					return ok(reContent);
				}
				if(entry.getKey().equals("联系人手机号")){
					if(!StringUtil.isMobileNO(entry.getValue())){
						reJson.setCode(3);
						reJson.setMessage(entry.getKey()+"数据错误,请重新填写并上传");
						String reContent = JsonUtil.parseObj(reJson);
						appLogger.debug(reContent);
						return ok(reContent);
					}
				}
			}
			//将传过来的 预约安装 订单信息保存到数据库
			SubMeasureOrders meaOrd;
			if(nidd==0){
				meaOrd = new SubMeasureOrders();
			}else{	//根据nidd查找到该预约测量订单
				meaOrd = selAppMeaOrdByIdd(nidd);
				if(meaOrd==null){
					meaOrd = new SubMeasureOrders();
				}
			}
			meaOrd.setPhone(subWxUser.getPhone());
			if(!StringUtil.isNull(oid)&&noid!=0){
				meaOrd.setOid(noid);
			}
			if(nidd==0){
				meaOrd.setStatus(1);
				meaOrd.setAddtime(new Date());
			}
			meaOrd.setMeasurename(measurename);
			meaOrd.setMeasurearea(measurearea);
			meaOrd.setMeasureaddress(measureaddress);
			meaOrd.setMeasurephone(measurephone);
			meaOrd.setMeasuretime(measuretime);
			//money prepayid 没设置
			
			Ebean.getServer(GlobalDBControl.getDB()).save(meaOrd);
			reJson.setCode(0);
			reJson.setMessage("成功");
			reJson.setData(meaOrd.getIdd());  //提交成功后将 预约测量订单id 返回
		}catch(Exception e){
			Logger.error("appointMeasureAction", e.getMessage());
		}
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}
	
	//根据openid 查找并返回 用户购买的 锁名称以及预约测量订单id 状态 和 商品小图
	public static Result selectMeasureOrd(){
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
			//查找该phone下的预约测量订单(非待支付状态)
			List<SubMeasureOrders> list = selAppMeaOrdByPho(subWxUser.getPhone());
			if(list!=null&&list.size()>0){
				List<AppointInitOrderDto> dtoList = new ArrayList<AppointInitOrderDto>();
				//遍历集合取出
				for(SubMeasureOrders order:list){
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
					SubOrders subOrder = AppointInstallAction.selSubOrdByIdd(order.getOid());
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
			reJson.setMessage("未查到预约测量订单信息");
		}catch(Exception e){
			Logger.error("appointMeasureAction", e.getMessage());
		}
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}
	
	//根据oid 查找 某笔 预约测量详情 并返回
	public static Result selectMeasureOrdDetail(){
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
			//查找该oid 下的预约测量订单
			SubMeasureOrders order = selAppMeaOrdByIdd(noid);
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
			reJson.setMessage("未查到预约测量订单信息");
		}catch(Exception e){
			Logger.error("appointMeasureAction", e.getMessage());
		}
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}
	
	//查找该phone下的预约测量订单(非待支付状态)
	public static List<SubMeasureOrders> selAppMeaOrdByPho(String phone){
		return Ebean.getServer(GlobalDBControl.getDB()).createQuery(SubMeasureOrders.class)
			.where().eq("phone", phone).ne("status", "1").findList();
	}
		
	//查找该idd 下的预约测量订单
	public static SubMeasureOrders selAppMeaOrdByIdd(Integer idd){
		return Ebean.getServer(GlobalDBControl.getDB()).find(SubMeasureOrders.class)
				.where().eq("idd", idd).findUnique();
	}
}
