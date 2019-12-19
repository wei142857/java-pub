package app.action;

import java.util.ArrayList;
import java.util.List;

import controllers.SysConfigAction;
import models.EShopConsignee;
import models.EShopProduct;
import models.SmartAppUser;
import play.mvc.Result;
import util.SmsUtil;
import util.StringUtil;
import util.classEntity.StringTool;
import util.json.JsonUtil;
import app.dto.EShopAddressDto;
import app.dto.EShopDto;
import app.dto.ReturnJson;
import app.service.EShopService;
import app.service.UserService;
import app.util.AppUtil;

public class EShopAction extends BaseAction{
	/**
	 * 功能说明:商城首页
	 * 请求方式:GET请求
	 * 接口地址:getEShopMainInfo
	 */
	public static Result getEShopMainInfo(){
		//检查签名
		if(!checkSign()){
			//检查签名失败
			return makSignFailor();
		}
		//通用的返回给APP的JSON对象
		ReturnJson reJson = new ReturnJson();
		EShopDto data = new EShopDto();
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
			List<EShopDto.products> returnEProList = new ArrayList<EShopDto.products>();
			List<EShopProduct> productsList;
			String ManagerPhone =  SysConfigAction.findSysconfig("智能锁", "苗苗姐测试商城商品手机号");
			if(appUser.getPhone()!=null&&ManagerPhone!=null&&ManagerPhone.indexOf(appUser.getPhone())!=-1){
				//去商品表中获取上线(status=0)和测试(status=1)的商品相关信息
				productsList = EShopService.selectEShopProductByStatus_1();
			}else{
				//去商品表中获取上线(status=0)的商品相关信息
				productsList = EShopService.selectEShopProductByStatus_0();
			}
			if(productsList!=null){
				for(EShopProduct product:productsList){
					EShopDto.products products = new EShopDto.products();
					products.setProductid(product.getIdd().toString());
					products.setTitle(product.getTitle());
					products.setSubtitle(product.getSubtitle());
					products.setPrice(product.getVipprice().toString());
					products.setBuyurl("/public/app/eshop/EShopProductDetail.html?idd="+product.getIdd());		//购买链接
					products.setImgurl(product.getBigimgurl());		//图片地址
					returnEProList.add(products);
				}
			}
			data.setProducts(returnEProList);	//设置商城商品列表信息
			data.setOrdersurl(SysConfigAction.findSysconfig("智能锁", "商城订单地址"));	//订单链接
			data.setTopleftimg(SysConfigAction.findSysconfig("智能锁", "商城首页上方左边图片"));
			data.setTopleftlink(SysConfigAction.findSysconfig("智能锁", "商城首页上方左边图片链接"));
			data.setToprightimg(SysConfigAction.findSysconfig("智能锁", "商城首页上方右边图片"));
			data.setToprightlink(SysConfigAction.findSysconfig("智能锁", "商城首页上方右边图片链接"));
			reJson.setSuccess();//返回状态码
			reJson.setData(data);//返回数据
		}
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}
	
	/**
	 * 功能说明:获取收货地址列表
	 * 请求方式:GET请求
	 * 接口地址:getEShopAddresses
	 */
	public static Result getEShopAddresses(){
		if(!checkSign()){
			return makSignFailor();
		}
		ReturnJson reJson = new ReturnJson();
		EShopAddressDto data = new EShopAddressDto();
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
		if(!checkSign()){
			return makSignFailor();
		}
		ReturnJson reJson = new ReturnJson();
		String token = request().getQueryString("token");
		String appid = request().getQueryString("appid");
		String addressid = request().getQueryString("addressid");
		String name = request().getQueryString("name");
		String area = request().getQueryString("area");
		String address = request().getQueryString("address");
		//对参数进行验证
		if(StringUtil.isNull(token)||StringUtil.isNull(addressid)||StringUtil.isNull(name)||StringUtil.isNull(area)||StringUtil.isNull(address)){
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
		SmartAppUser appUser = UserService.findUserByToken(token);
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
		if(!checkSign()){
			return makSignFailor();
		}
		ReturnJson reJson = new ReturnJson();
		String token = request().getQueryString("token");
		String appid = request().getQueryString("appid");
		String addressid = request().getQueryString("addressid");
		if(StringUtil.isNull(token)||StringUtil.isNull(addressid)){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		Integer naddressid = StringTool.GetInt(addressid, 0);
		SmartAppUser appUser = UserService.findUserByToken(token);
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
	
	public static Result getEShopPayCode(){
		String token = session("token");
		
		ReturnJson reJson = new ReturnJson();
		
		SmartAppUser user = UserService.findUserByToken(token);
		if (user == null) {
			reJson.setTokenTimeOut();
		} else {
			String phone = user.getPhone();
			String rdCode = StringUtil.mkNumRanmString(6);
			AppUtil.setRandomCode(phone, rdCode, AppUtil.TYPE_SMS_ESHOPPAY);
			SmsUtil.sendRandCode(phone, rdCode, AppUtil.TIMEOUT_CACHESMSCODE_MINUTE+"");
			
			appLogger.debug("eshopPay:"+rdCode+"\t"+user.getPhone());
			reJson.setSuccess();
		}
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}
	
}
