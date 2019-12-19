package app.gzh;

import java.util.ArrayList;
import java.util.List;

import app.dto.EShopAddressDto;
import app.dto.ReturnJson;
import app.service.EShopService;
import app.service.SubWxUserService;
import app.service.UserService;
import models.EShopConsignee;
import models.SmartAppUser;
import models.SubWxUser;
import play.mvc.Result;
import util.GlobalSetting;
import util.StringUtil;
import util.classEntity.StringTool;
import util.json.JsonUtil;

public class EShopAction extends BaseAction{
	/**
	 * 功能说明:获取收货地址列表
	 * 请求方式:GET请求
	 * 接口地址:getEShopAddresses
	 */
	public static Result getEShopAddresses(){
		ReturnJson reJson = new ReturnJson();
		EShopAddressDto data = new EShopAddressDto();
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
			List<EShopConsignee> consigneeList = EShopService.selectAllAddress(appUser.getIdd());
			List<EShopAddressDto.Address> addressList = new ArrayList<EShopAddressDto.Address>();
			if(consigneeList!=null){
				for(EShopConsignee consignee:consigneeList){
					EShopAddressDto.Address address = new EShopAddressDto.Address();
					address.setAddressid(consignee.getIdd().toString());
					address.setArea(consignee.getArea());
					address.setAddress(consignee.getAddress());
					address.setName(consignee.getName());
					address.setPhone(consignee.getPhone());
					address.setIsdefault(consignee.getIsdefault().toString());
					addressList.add(address);
				}
			}
			data.setAddresses(addressList);
			reJson.setSuccess();//返回状态码
			reJson.setData(data);//返回数据
		}
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}
	
	/**
	 * 功能说明：修改收货地址
	 * 请求方式： GET
	 * 接口地址：editEShopAddress
	 */
	public static Result editEShopAddress(){
		ReturnJson reJson = new ReturnJson();
		String openid = session(GlobalSetting.GZH_OPENID);
		if (StringUtil.isNull(openid)) {
			reJson.setCode(2);
			reJson.setMessage("非法登录,请关注微信：SKN智能生活" + "\t" + "公众号进行登录");
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		String addressid = request().getQueryString("addressid");
		String name = request().getQueryString("name");
		String area = request().getQueryString("area");
		String address = request().getQueryString("address");
		//对参数进行验证
		if(StringUtil.isNull(addressid)||StringUtil.isNull(name)||StringUtil.isNull(area)||StringUtil.isNull(address)){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		//验证收件人的电话号码是否合法有效
		String phone = request().getQueryString("phone");
		if((!StringUtil.isMobileNO(phone))||StringUtil.isNull(phone)){
			reJson.setParamsError();
			reJson.setMessage("收件人的手机号码为无效,请输入有效的手机号码");
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		//验证设置默认地址的参数是否有效
		String isdefault = request().getQueryString("isdefault");
		if(StringUtil.isNull(isdefault)||!(isdefault.equals("0")||isdefault.equals("1"))){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		Integer naddressid = StringTool.GetInt(addressid, 0);
		Integer nisdefault = StringTool.GetInt(isdefault, 1);
		SubWxUser subWxUser = SubWxUserService.findIllegalNotifyWxUser(openid);
		
		SmartAppUser appUser = UserService.findUserByPhone(subWxUser.getPhone());
		if(appUser==null){
			reJson.setTokenTimeOut();
		}else{
			//判断naddressid是否为0(0:新增 其他的为修改)
			if(naddressid == 0){
				//新增一条收货地址
				EShopService.addAddress_check(appUser.getIdd(),name,phone,area,address,nisdefault);
			}else{
				//根据addressid查找到该userid下的该条地址信息
				EShopConsignee consignee = EShopService.selectAddressByAddressIdAndUserid(appUser.getIdd(),naddressid);
				if(consignee!=null&&consignee.getIdd()!=null){
					//将该条地址信息进行修改
					if(EShopService.updateAddress_check(appUser.getIdd(),naddressid,name,phone,area,address,nisdefault)){
						reJson.setSuccess();//返回状态码
					}
				}else{
					util.LogUtil.writeLog("传入的addressid:"+addressid+"为无效addressid或者该条地址的userid与当前登录的user不匹配", "EShopLog");
				}
			}
			reJson.setSuccess();//返回状态码
		}
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}
	
	/**
	 * 功能说明：删除收货地址
	 * 请求方式： GET
	 * 接口地址：deleteEShopAddress
	 */
	public static Result deleteEShopAddress(){
		ReturnJson reJson = new ReturnJson();
		String openid = session(GlobalSetting.GZH_OPENID);
		if (StringUtil.isNull(openid)) {
			reJson.setCode(2);
			reJson.setMessage("非法登录,请关注微信：SKN智能生活" + "\t" + "公众号进行登录");
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		String addressid = request().getQueryString("addressid");
		if(StringUtil.isNull(addressid)){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		Integer naddressid = StringTool.GetInt(addressid, 0);
		SubWxUser subWxUser = SubWxUserService.findIllegalNotifyWxUser(openid);
		
		SmartAppUser appUser = UserService.findUserByPhone(subWxUser.getPhone());
		if(appUser==null){
			reJson.setTokenTimeOut();
		}else{
			if(naddressid!=0){
				EShopConsignee consignee = EShopService.selectAddressByAddressIdAndUserid(appUser.getIdd(),naddressid);
				if(consignee!=null&&consignee.getIdd()!=null){
					//将该条信息进行删除
					//EShopService.deleteAddress_check(appUser.getIdd(),naddressid); //此处不做默认地址处理,用户可以无默认地址
					EShopService.deleteAddressByAddressidAndUserid(naddressid, appUser.getIdd());
					reJson.setSuccess();//返回状态码
				}
			}
			util.LogUtil.writeLog("传入的addressid:"+addressid+"为无效addressid或者该条地址的userid与当前登录的user不匹配", "EShopLog");
		}
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}
		
}
