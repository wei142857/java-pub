package app.action;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.apache.commons.codec.binary.Base64;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.avaje.ebean.Ebean;

import app.dto.ReturnJson;
import app.service.SubWxUserService;
import app.service.UserService;
import app.service.VipService;
import jdk.nashorn.internal.objects.Global;
import models.InsuranceRegion;
import models.SmartAppUser;
import models.SmartAppUserDevice;
import models.SmartDevice;
import models.SmartDeviceType;
import models.SmartInsurance;
import models.SubWxUser;
import models.ValidatePolicy;
import models.ZipCode;
import play.Logger;
import play.mvc.Result;
import util.AesUtil;
import util.AjaxHellper;
import util.GlobalDBControl;
import util.GlobalSetting;
import util.LogUtil;
import util.StringUtil;
import util.http.HttpClientUtil;
import util.http.common.HttpConfig;
import util.http.common.HttpHeader;
import util.http.exception.HttpProcessException;
import util.json.JsonUtil;

@SuppressWarnings("all")
public class InsuranceServiceAction extends BaseAction{
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
	private static Calendar calender = Calendar.getInstance();
	
	private static String aes_key = "m04v15YOa96jvXWJ1i38ZPGxp46P17hv";//报文密钥（测试）
	private static String md5_key = "hD9U9V4021LP51WV7834V25041EvuJkb"; //签名密钥（测试）
	private static String url = "http://api.open.tk.cn/api/v1/validatePolicy"; //请求接口地址
	private static String url2 = "http://api.open.tk.cn/api/v1/policy"; //出单测试接口
	private static String channel_id = "100000000193"; //渠道编号
//	private static final String aes_key = "D21Uq65MzDF28PaEx7zO0dSM2WuG03z0";//报文密钥（测试）
//	private static final String md5_key = "12344404K53N5571lz91673701u04321"; //签名密钥（测试）
//	private static final String url = "http://ecuat.tk.cn/api/validatePolicy"; //请求接口地址
//	private static final String url2 = "http://ecuat.tk.cn/api/policy"; //出单测试接口
//	private static final String channel_id = "100000000193"; //渠道编号
	
	public static String getStart_date(Date formatTime) {
		try {
			String start_date = dateFormat2.format(formatTime);//转换成不精确到时分秒的时间格式
			long time = dateFormat2.parse(start_date).getTime();
			return addOneDay(new Date(time), 5);//根据投保日期加5天
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static String getEndDate(String start_date) {
		try {
			Date start_date_new = dateFormat.parse(start_date);
			String addOneYear = addOneYear(start_date_new);
//			String addOneDay = addOneDay(dateFormat.parse(addOneYear), 1);
			long time = dateFormat.parse(addOneYear).getTime();
			time -= 1000;
			return dateFormat.format(new Date(time));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 核保接口
	 * @return json形式的ValidatePolicy对象
	 */
	public static Result receiveInsurance() {
		util.AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "orderNo");
		String insured_name = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "insured_name");
		String licenseType = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "licenseType");//证件类型
		String licensenumber = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "licensenumber");
		
		String phone = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "phone");
		String email = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "email");
		
		String province = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "province");//省
		String provinceCode = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "provinceCode");//省编码
		//下面两个编码可能会是0
		String cityCode = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "cityCode");
		String city =  AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "city");
		
		String areaCode = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "areaCode");
		String area = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "area");
		
		String addr_info = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "addr_info");
		String deviceid = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "deviceid");
		ReturnJson reJson = new ReturnJson();
		String token = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "token");
		String openid = session(GlobalSetting.GZH_OPENID);
		SmartAppUser user = null;
		if(StringUtil.isNull(openid)) {//公众号登陆
			if (StringUtil.isNull(token)) {
				LogUtil.writeLog("未登录", "Insurance");
				reJson.setTokenTimeOut();
				reJson.setMessage("没有登录信息，请先登陆");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			user = UserService.findUserByToken(token);
			if(user==null){
				LogUtil.writeLog("未登录", "Insurance");
				reJson.setTokenTimeOut();
				reJson.setMessage("没有登录信息，请先登陆");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
		}else {//商城登陆
			LogUtil.writeLog("未登录", "Insurance");
			SubWxUser subWxUser = SubWxUserService.findIllegalNotifyWxUser(openid);
			if(subWxUser==null){
				reJson.setCode(2);
				reJson.setMessage("非法登录,请关注微信：SKN智能生活" + "\t" + "公众号进行登录");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			user = UserService.findUserByPhone(subWxUser.getPhone());
		}
		
		if(deviceid==null) {
			LogUtil.writeLog("忘记在URl拼接Deviceid", "Insurance");
			reJson.setParamsError();
			reJson.setMessage("您没有设备");
			String reContent = JsonUtil.parseObj(reJson);
			return ok(reContent);
		}else {
			SmartDevice findUnique = Ebean.getServer(GlobalDBControl.getDB()).createQuery(SmartDevice.class,"find SmartDevice where deviceid='"+deviceid+"'").findUnique();
			if(findUnique==null) {
				LogUtil.writeLog("设备不存在", "Insurance");
				reJson.setParamsError();
				reJson.setMessage("您的设备无效");
				String reContent = JsonUtil.parseObj(reJson);
				return ok(reContent);
			}
			//获取用户的所有设备id 这个设备ID需要到设备表关联id查询设备信息
			List<SmartAppUserDevice> userDeviceList = Ebean.getServer(GlobalDBControl.getDB()).createQuery(SmartAppUserDevice.class,"find SmartAppUserDevice where deviceAppUser='"+user.getIdd()+"'").findList();
			//创建一个集合 用于装用户的所有设备信息
			List<SmartDevice> deviceList = new ArrayList<SmartDevice>();
			//遍历用户的所有锁并将其信息存储
			for (SmartAppUserDevice userDevice : userDeviceList) {
				SmartDevice device = Ebean.getServer(GlobalDBControl.getDB()).createQuery(SmartDevice.class,"find SmartDevice where idd='"+userDevice.getDeviceid()+"'").findUnique();
				deviceList.add(device);
			}
			//标记
			boolean flag = true;
			//对比
			for (SmartDevice device : deviceList) {
				if(device.getDeviceid().equals(deviceid)) {
					flag = false;
				}
			}
			//如果用户与锁的id没有联系 说明用户非法操作
			if(flag) {
				LogUtil.writeLog("该设备不是用户的,deviceid:"+deviceid+" 用户id:"+user.getIdd(), "Insurance");
				reJson.setParamsError();
				reJson.setMessage("访问非法,设备已被保护!");
				String reContent = JsonUtil.parseObj(reJson);
				return ok(reContent);
			}
			
		}
		
		Integer vipStatus = VipService.checkVip(user.getIdd());
		if(vipStatus!=1) {
			LogUtil.writeLog("不是vip 不能领取保险", "Insurance");
			reJson.setParamsError();
			reJson.setMessage("会员已过期，无法领取保险");
			String reContent = JsonUtil.parseObj(reJson);
			return ok(reContent);
		}
		SmartInsurance insuranceInfo = Ebean.getServer(GlobalDBControl.getDB()).createQuery(SmartInsurance.class, "find SmartInsurance where deviceid='"+deviceid+"' and status='0'").findUnique();
		if(insuranceInfo!=null){
			LogUtil.writeLog("保单重复", "Insurance");
			reJson.setParamsError();
			reJson.setMessage("该设备的保险已领取过~");
			String reContent = JsonUtil.parseObj(reJson);
			return ok(reContent);
		}
		
//		String token = StringUtil.getHttpParam(request(), "token");//被保人姓名
		
		
		String serialnumber = StringUtil.getDateString();
		
		//当前时间对象
		Date date = new Date();
		//当前时间对象日期格式
		String formatTime = dateFormat.format(date);
		
		ValidatePolicy vp = new ValidatePolicy();
		//产品id是指这个保险的产品id
		vp.setProduct_id("1717A00301");
		//多保额累加 单位 元
		vp.setAmount("9000.00");
		
		vp.setTrade_no(serialnumber);
//		vp.setSignpksubId("");
		
		vp.setPremium("21.00");
		
		//投保时间 目前是当前时间
		vp.setInsure_date(formatTime);
		//起保时间 目前是当前时间
		String start_date = getStart_date(date);
		if(start_date==null) {
			LogUtil.writeLog("时间格式不对", "Insurance");
			reJson.setParamsError();
			reJson.setMessage("时间返回null");
			String reContent = JsonUtil.parseObj(reJson);
			return ok(reContent);
		}
		vp.setStart_date(start_date);
		//终保时间 目前是当前时间加一年
		String endDate = getEndDate(start_date);
		if(endDate==null){
			LogUtil.writeLog("时间格式不对", "Insurance");
			reJson.setParamsError();
			reJson.setMessage("时间返回null");
			String reContent = JsonUtil.parseObj(reJson);
			return ok(reContent);
		}
		vp.setEnd_date(endDate);
		
		//险别列表 目前不用设置
//		vp.setKind_list();
		
		//投保人信息
		Map<String,String> applicant = new HashMap<String,String>();
		applicant.put("type", "2");//	1-个人，2-机构
		applicant.put("name", "北京聚淘科技有限公司");
		applicant.put("certi_type", "4");
		applicant.put("certi_no", "91110108MA009JDE5R");
		applicant.put("unitProperties1", "900");
		applicant.put("unitSum1", "500");
		applicant.put("industry", "99");
//		applicant.put("sex", "2");
//		applicant.put("phone", "13711111111");
//		applicant.put("email", "13711111111@qq.com");
		applicant.put("postCode", "063500");
//		applicant.put("address", "东区国际");
		vp.setApplicant(applicant);
		
		
		//被保人信息
		Map<String,String> insured = new HashMap<String,String>();
		insured.put("type", "1");//	1-个人，2-机构
		insured.put("name", insured_name);
		insured.put("certi_type", licenseType);
		insured.put("certi_no", licensenumber);
		insured.put("birthday",findBirthdayByLicensenumber(licensenumber));
		insured.put("sex", findSexByLicensenumber(licensenumber));
		insured.put("phone", phone);
		insured.put("email", email);
		insured.put("relation", "99");//与投保人关系
//		insured.put("occupation_code", "投保人职业代码未知");//职业代码
		vp.setInsured(insured);
		
		if(!areaCode.equals("0")) {
			List<Object> insurance_subject_list = new ArrayList<Object>();
			ValidatePolicy.InsuranceSubjectList insuranceSubjectList = new ValidatePolicy.InsuranceSubjectList();
			insuranceSubjectList.setDistrict_code(areaCode);
			insuranceSubjectList.setDistrict_name(area);
			insuranceSubjectList.setProvince_code(provinceCode);
			insuranceSubjectList.setProvince_name(province);
			insuranceSubjectList.setCity_code(cityCode);
			insuranceSubjectList.setCity_name(city);
			ZipCode zipCode = null;
			String zipnum = null;
			try {
				zipCode = Ebean.getServer(GlobalDBControl.getDB()).createQuery(ZipCode.class,"find ZipCode where region_id='"+areaCode+"' or region_id='"+cityCode+"' or region_id='"+provinceCode+"'").findUnique();
				if(zipCode!=null) {
					zipnum = zipCode.getZip_number();
				}else {
					zipnum = "100000";
				}
			} catch (Exception e) {
				LogUtil.writeLog("获取邮编异常:"+e.getMessage(), "Insurance");
				reJson.setParamsError();
				reJson.setMessage("获取邮编异常");
				String reContent = JsonUtil.parseObj(reJson);
				return ok(reContent);
			}
			insuranceSubjectList.setPost_code(zipnum);
			insuranceSubjectList.setAddress(addr_info);
			insurance_subject_list.add(insuranceSubjectList);
			vp.setInsurance_subject_list(insurance_subject_list);
			
		}else {
			List<Object> insurance_subject_list = new ArrayList<Object>();
			ValidatePolicy.InsuranceSubjectList insuranceSubjectList = new ValidatePolicy.InsuranceSubjectList();
			insuranceSubjectList.setProvince_code(provinceCode);
			insuranceSubjectList.setProvince_name(province);
			insuranceSubjectList.setCity_code(provinceCode);
			insuranceSubjectList.setCity_name(province);
			insuranceSubjectList.setDistrict_code(cityCode);
			insuranceSubjectList.setDistrict_name(city);
			ZipCode zipCode = null;
			String zipnum = null;
			try {
				zipCode = Ebean.getServer(GlobalDBControl.getDB()).createQuery(ZipCode.class,"find ZipCode where region_id='"+areaCode+"' or region_id='"+cityCode+"' or region_id='"+provinceCode+"'").findUnique();
				if(zipCode!=null) {
					zipnum = zipCode.getZip_number();
				}else {
					zipnum = "100000";
				}
			} catch (Exception e) {
				LogUtil.writeLog("获取邮编异常:"+e.getMessage(), "Insurance");
				reJson.setParamsError();
				reJson.setMessage("获取邮编异常");
				String reContent = JsonUtil.parseObj(reJson);
				return ok(reContent);
			}
			insuranceSubjectList.setPost_code(zipnum);
			insuranceSubjectList.setAddress(addr_info);
			insurance_subject_list.add(insuranceSubjectList);
			vp.setInsurance_subject_list(insurance_subject_list);
		}
		
		
		Map<String,String> extend_params = new HashMap<String,String>();
		extend_params.put("ah_relation", "01");
		extend_params.put("house_structure", "02");
		extend_params.put("house_type", "01");
		extend_params.put("ah_relation_name", "房主");
		vp.setExtend_params(extend_params);
		
//		vp.setPaymentPlan("1");
//		vp.setAnnualPayment("1");
//		vp.setPayPlanByChannel("1");
		
//		List<ValidatePolicy.SPaymentPlanInfoList> sPaymentPlanInfoList = new ArrayList<ValidatePolicy.SPaymentPlanInfoList>();
//		ValidatePolicy.SPaymentPlanInfoList sPaymentPlanInfo = new ValidatePolicy.SPaymentPlanInfoList();
//
//		List<ValidatePolicy.SPaymentPlanInfoList.SPaymentPlanDetailList> sPaymentPlanDetailList = new ArrayList<ValidatePolicy.SPaymentPlanInfoList.SPaymentPlanDetailList>();
//		ValidatePolicy.SPaymentPlanInfoList.SPaymentPlanDetailList sPaymentPlanDetail = new ValidatePolicy.SPaymentPlanInfoList.SPaymentPlanDetailList();
//		
//		sPaymentPlanDetail.setPayTimes("1");
//		sPaymentPlanDetail.setShouldPremium("26.7");
//		sPaymentPlanDetailList.add(sPaymentPlanDetail);
//		
//		sPaymentPlanInfo.setTotalTimes("3");
//		sPaymentPlanInfo.setsPaymentPlanDetailList(sPaymentPlanDetailList);//分期详情计划
//		sPaymentPlanInfoList.add(sPaymentPlanInfo);
//		
//		vp.setsPaymentPlanInfoList(sPaymentPlanInfoList);
		
//		return ok(touchInterfaceByJsonObj(vp,url));//json形式将数据返回给页面
		String content = JsonUtil.parseObj(vp);//未加密请求报文
	
		//流水号
		LogUtil.writeLog(content, "Record");
		LogUtil.writeLog(serialnumber, "lsh");
		
		Map message = new HashMap();
		message.put("channel_id", channel_id);
		message.put("biz_content", encryptAES(content,aes_key));
		message.put("request_no", serialnumber);
		message.put("timestamp", new Date().getTime());
		String sign = JSONObject.toJSONString(message,SerializerFeature.MapSortField);
		sign+=md5_key;
		sign = md5(sign);
		message.put("sign",sign);
		//将封装好的请求报文发送到指定url进行校验
		//请求核保接口
		String resReport = initHttpClient(url, message);
		LogUtil.writeLog(resReport, "hbResult");
		JSONObject jsonObject = JSON.parseObject(resReport);
		if(jsonObject.getString("result_code").equalsIgnoreCase("FAIL")) {
			LogUtil.writeLog(jsonObject.getString("err_msg"), "Insurance");
			reJson.setParamsError();
			reJson.setMessage(jsonObject.getString("err_msg"));
			String reContent = JsonUtil.parseObj(reJson);
			return ok(reContent);
		}
		//如果返回的结果为null 说明这次的校验是失败的
		if(resReport==null) {
			LogUtil.writeLog("出错了，请求返空"+new Date().getTime(), "Insurance");
			reJson.setParamsError();
			reJson.setMessage("保险接口请求失败");
			String reContent = JsonUtil.parseObj(reJson);
			return ok(reContent);
		}
		//将返回的结果转换成json对象
		//将对象和密钥传入业务统一方法
		/**
		 * 返回的结果类型：
		 * null
		 * 正确解密的请求报文
		 */
		String response = getContent(jsonObject, md5_key, aes_key);
		if(response!=null) {
			Ebean.beginTransaction();
			//生成保险订单 状态为1 - 处理中
			SmartInsurance ins = new SmartInsurance();
			ins.setAddtime(new Date());//添加时间
			ins.setAddress(addr_info);//详细地址
			try {
				ins.setBegintime(dateFormat.parse(start_date));
				ins.setOvertime(dateFormat.parse(endDate));
			} catch (ParseException e) {
				Ebean.endTransaction();
				LogUtil.writeLog("日期格式解析错误", "Insurance");
				reJson.setParamsError();
				reJson.setMessage(jsonObject.getString("日期格式解析错误"));//直接返回错误信息让前端alert
				String reContent = JsonUtil.parseObj(reJson);
				return ok(reContent);
			}//起保日期
			ins.setCardnum(licensenumber);//证件号
			ins.setCardtype(1);//证件类型
			ins.setCity(city);//市
			ins.setCoverage(9000);//保额
			ins.setDistrict(area);//区
			ins.setDeviceid(deviceid);//设备id
			ins.setEmail(email);//邮箱
			ins.setName(insured_name);//被保人姓名
			ins.setPhone(phone);//被保人手机号
			ins.setProvince(province);//省
			ins.setRemark("赠送版保险");//备注
			ins.setStatus(1);//状态
//			ins.setBillno(billno);
			
			String billing = initHttpClient(url2, message);      
			LogUtil.writeLog(resReport, "cdResult");
			JSONObject billingJsonObj = null;
			try {
				billingJsonObj = JSON.parseObject(billing);
			}catch (Exception e) {
				Ebean.rollbackTransaction();
				Ebean.endTransaction();
				LogUtil.writeLog("JSON解析失败，billing不是JSON格式", "Insurance");
				reJson.setParamsError();
				reJson.setMessage("JSON解析失败");//直接返回错误信息让前端alert
				String reContent = JsonUtil.parseObj(reJson);
				return ok(reContent);
			}
			String result = getContent(billingJsonObj, md5_key, aes_key);
			//policy_url policy_no
			if(result!=null) {
				ins.setWarrantyurl((String)JSON.parseObject(result).get("policy_url"));
				ins.setPolicyno((String)JSON.parseObject(result).get("policy_no"));
				ins.setValidity(1);//有效期 单位:年
				Ebean.getServer(GlobalDBControl.getDB()).save(ins);
				Ebean.getServer(GlobalDBControl.getDB()).createUpdate(SmartInsurance.class, "update smart_insurance set status= '0'  where deviceid='"+deviceid+"'").execute();
			}else {
				Ebean.getServer(GlobalDBControl.getDB()).createUpdate(SmartInsurance.class, "update smart_insurance set status= '-1'  where deviceid='"+deviceid+"'").execute();
				Ebean.commitTransaction();
				Ebean.endTransaction();
				LogUtil.writeLog("赠送版保单出单失败了", "Insurance");
				reJson.setParamsError();
				reJson.setMessage(billingJsonObj.getString("err_msg"));//直接返回错误信息让前端alert
				String reContent = JsonUtil.parseObj(reJson);
				return ok(reContent);
			}
			Ebean.commitTransaction();
			Ebean.endTransaction();
			reJson.setSuccess();
			reJson.setMessage("成功提交保单~");//直接返回错误信息让前端alert
			String reContent = JsonUtil.parseObj(reJson);
			return ok(reContent);
		}else {
			LogUtil.writeLog("校验数失败", "Insurance");
			reJson.setParamsError();
			reJson.setMessage(jsonObject.getString("err_msg"));//直接返回错误信息让前端alert
			String reContent = JsonUtil.parseObj(reJson);
			return ok(reContent);
		}
	}
	public static String getContent(JSONObject jsonObject,String md5_key,String aes_key) {
		
		String result = jsonObject.getString("result_code");
		//校验返回结果
		String response = null;
		if("SUCCESS".equals(result)){
			String result_biz_content = jsonObject.getString("biz_content");
			//校验验签是否正确
			boolean check = checkSign(jsonObject,md5_key);
			if(check){
				//对返回业务参数解密
				response = decryptAES(result_biz_content,aes_key);
			}else{
				return response;
			}
		}else{
			return response;
		}
		return response;
	}
	
	/**
	 * 校验返回验签
	 * @param obj 请求返回的数据
	 * @param signKey 验签加密密钥
	 * @return
	 */
	public static boolean checkSign(JSONObject obj,String signKey){
		Map resultMap = new HashMap();
		resultMap.put("timestamp", obj.getLongValue("timestamp"));
		resultMap.put("biz_content", obj.getString("biz_content"));
		resultMap.put("result_code", obj.getString("result_code"));
		String resultJson = JSONObject.toJSONString(resultMap,SerializerFeature.MapSortField);
		String sign = obj.getString("sign");
		String signGen = md5(resultJson+signKey);
		System.out.println(signGen);
		if(sign.equals(signGen)){
			return true;
		}
		return false ; 
	}
	/**
	 * 传入日期 添加一年
	 * @param date 日期
	 * @return 增加一年的日期
	 */
	public static String addOneYear(Date date) {
		calender.setTime(date);
		calender.add(Calendar.YEAR, 1);
		return dateFormat.format(calender.getTime());
	}
	/**
	 * 传入日期 添加指定天数
	 * @param date 日期
	 * @return 增加指定天数的日期
	 */
	public static String addOneDay(Date date,int dayCount) {
		calender.setTime(date);
		calender.add(Calendar.DATE, dayCount);
		return dateFormat.format(calender.getTime());
	}
	
	/**
	 * 根据证件号解析出生日
	 * @param licensenumber
	 * @return 解析出的生日
	 */
	public static String findBirthdayByLicensenumber(String licensenumber) {
		String year = licensenumber.substring(6, 10)+"-";
		String month = licensenumber.substring(10, 12)+"-";
		String day = licensenumber.substring(12, 14);
		return year+month+day;
	}
	/**
	 * 根据证件号解析性别
	 * @param licensenumber
	 * @return 解析出的性别 1-男，2-女
	 */
	public final static String findSexByLicensenumber(String licensenumber) {
		int sex = Integer.parseInt(licensenumber.substring(16,17));
		return sex>>1<<1==sex?"2":"1";
	}
	/**
	 * 
	 * @param o
	 * @return
	 */
	public static String initHttpClient(String url,Map o) {
		String result = null;
		try {
			result = HttpClientUtil.post(HttpConfig.custom()
					.headers(HttpHeader.custom()
					.contentType("application/json;charset=utf-8")
					.build()).timeout(60*1000)
					.url(url).encoding("utf-8")
					.json(JsonUtil.parseObj(o)));
		} catch (HttpProcessException e) {
			e.printStackTrace();
		}
		return result;
		
	}
	/**
	 * AES 加密
	 * @param content
	 * @param key
	 * @return
	 */
	public static String encryptAES(String content, String key) {
		try {
			KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
			//需手动指定 SecureRandom 随机数生成规则，否则在Linux上可能生成随机key
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.setSeed(key.getBytes());
			keyGenerator.init(128, secureRandom);
			SecretKey secretKey = keyGenerator.generateKey();
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			byte[] encrypted = cipher.doFinal(content.getBytes("utf-8"));
			return Base64.encodeBase64String(encrypted);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * AES解密
	 * @param content
	 * @param key
	 * @return
	 */
	 public static String decryptAES(String content, String key) {
		 try {
				 KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
				 SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
				 secureRandom.setSeed(key.getBytes());
				 keyGenerator.init(128, secureRandom);
				 SecretKey secretKey = keyGenerator.generateKey();
				 Cipher cipher = Cipher.getInstance("AES");
				 cipher.init(Cipher.DECRYPT_MODE, secretKey);
				 byte[] encrypted = cipher.doFinal(Base64.decodeBase64(content));
				 return new String(encrypted, "utf-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	/**
	 * MD5运算
	 * @param content
	 * @return
	 */	
	public static String md5(String content) {
		try {
			return Base64.encodeBase64String(MessageDigest.getInstance("MD5").digest(content.getBytes("utf-8")));
		} catch (Exception e) {
			e.printStackTrace();
		}
			return null;
	}
	/**
	 * 查询地址数据库为前端传递三级树数据
	 * @return
	 */
	public static final Result findRegion() {
		try {
			List<InsuranceRegion> regionList = Ebean.getServer(GlobalDBControl.getDB()).createQuery(InsuranceRegion.class).findList();
			return ok(JsonUtil.parseObj(regionList));
		} catch (Exception e) {
			Logger.error("err",e);
		}
		return null;
	}
	/**
	 * 获取用户的保单
	 */
	public static final Result findInsInfo() {
		String deviceid = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "deviceid");
		SmartInsurance ins = Ebean.getServer(GlobalDBControl.getDB()).createQuery(SmartInsurance.class, "find SmartInsurance where deviceid='"+deviceid+"'").findUnique();
		return ok(JsonUtil.parseObj(ins));
	}
	/**
	 * 获取锁信息
	 */
	public static final Result findLockInfo() {
		String deviceid = AjaxHellper.getHttpParamOfFormUrlEncoded(request(), "deviceid");
		SmartDevice sd = Ebean.getServer(GlobalDBControl.getDB()).createQuery(SmartDevice.class, "find SmartDevice where deviceid='"+deviceid+"'").findUnique();
		SmartDeviceType lock = Ebean.getServer(GlobalDBControl.getDB()).createQuery(SmartDeviceType.class,"find SmartDeviceType where idd = '"+sd.getType()+"'").findUnique();
		return ok(JsonUtil.parseObj(lock));
	}
	
}
