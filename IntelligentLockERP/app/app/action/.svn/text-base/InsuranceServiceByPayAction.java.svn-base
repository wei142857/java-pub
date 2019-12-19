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
import app.service.UserService;
import models.InsuranceByPay;
import models.InsuranceResponseModel;
import models.SmartAppUser;
import models.SmartAppUserDevice;
import models.SmartDevice;
import models.SmartInsurance;
import models.ValidatePolicy;
import models.ZipCode;
import play.libs.Json;
import play.mvc.Result;
import util.AjaxHellper;
import util.GlobalDBControl;
import util.LogUtil;
import util.StringUtil;
import util.http.HttpClientUtil;
import util.http.common.HttpConfig;
import util.http.common.HttpHeader;
import util.http.exception.HttpProcessException;
import util.json.JsonUtil;

@SuppressWarnings("all")
public class InsuranceServiceByPayAction extends BaseAction{
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
	private static Calendar calender = Calendar.getInstance();
	
	private static String aes_key = "m04v15YOa96jvXWJ1i38ZPGxp46P17hv";//报文密钥（测试）
	private static String md5_key = "hD9U9V4021LP51WV7834V25041EvuJkb"; //签名密钥（测试）
	private static String url = "http://api.open.tk.cn/api/v1/validatePolicy"; //请求接口地址
	private static String payUrl = "http://api.open.tk.cn/api/v1/pay"; //支付接口
	private static String outUrl = "http://api.open.tk.cn/api/v1/policy"; //出单测试接口
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
//	public static Result receiveInsuranceByPayTest() {
//		return redirect("data:image/jpg;base64,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");
//	}
	/**
	 * 核保接口
	 * @return json形式的ValidatePolicy对象
	 */
	public static Result receiveInsuranceByPay() {
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
		
		if(deviceid==null) {
			LogUtil.writeLog("忘记在URl拼接Deviceid", "Insurance");
			ReturnJson reJson = new ReturnJson();
			reJson.setParamsError();
			reJson.setMessage("您没有设备");
			String reContent = JsonUtil.parseObj(reJson);
			return ok(reContent);
		}else {
			SmartDevice findUnique = Ebean.getServer(GlobalDBControl.getDB()).createQuery(SmartDevice.class,"find SmartDevice where deviceid='"+deviceid+"'").findUnique();
			if(findUnique==null) {
				LogUtil.writeLog("设备不存在", "Insurance");
				ReturnJson reJson = new ReturnJson();
				reJson.setParamsError();
				reJson.setMessage("您的设备无效");
				String reContent = JsonUtil.parseObj(reJson);
				return ok(reContent);
			}
			String token = session("user_token");
			SmartAppUser user = UserService.findUserByToken(token);
			if(user == null) {
				LogUtil.writeLog("未登录", "Insurance");
				ReturnJson reJson = new ReturnJson();
				reJson.setParamsError();
				reJson.setMessage("您需要重新登录");
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
				ReturnJson reJson = new ReturnJson();
				reJson.setParamsError();
				reJson.setMessage("访问非法,设备已被保护!");
				String reContent = JsonUtil.parseObj(reJson);
				return ok(reContent);
			}
			
		}
		SmartInsurance insuranceInfo = Ebean.getServer(GlobalDBControl.getDB()).createQuery(SmartInsurance.class, "find SmartInsurance where deviceid='"+deviceid+"' and status='0'").findUnique();
		if(insuranceInfo!=null){
			LogUtil.writeLog("保单重复", "Insurance");
			ReturnJson reJson = new ReturnJson();
			reJson.setParamsError();
			reJson.setMessage("保单已经存在，不得重复提交");
			String reContent = JsonUtil.parseObj(reJson);
		}
//		String token = StringUtil.getHttpParam(request(), "token");//被保人姓名
		
		
		
		
		//当前时间对象
		Date date = new Date();
		//当前时间对象日期格式
		String formatTime = dateFormat.format(date);
		
		ValidatePolicy vp = new ValidatePolicy();
		//产品id是指这个保险的产品id
		vp.setProduct_id("1702A02A01");
		//多保额累加 单位 元
		vp.setAmount("9000.00");
		
		vp.setTrade_no(date.getTime()+"");
//		vp.setSignpksubId("");
		
		vp.setPremium("35.00");
		
		//投保时间 目前是当前时间
		vp.setInsure_date(formatTime);
		//起保时间 目前是当前时间
		String start_date = getStart_date(date);
		if(start_date==null) {
			LogUtil.writeLog("时间格式不对", "Insurance");
			ReturnJson reJson = new ReturnJson();
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
			ReturnJson reJson = new ReturnJson();
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
//				insured.put("occupation_code", "投保人职业代码未知");//职业代码
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
				ReturnJson reJson = new ReturnJson();
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
				ReturnJson reJson = new ReturnJson();
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
		
		String content = JsonUtil.parseObj(vp);//未加密请求报文
		String aes_key = "D21Uq65MzDF28PaEx7zO0dSM2WuG03z0";//报文密钥（测试）
		String md5_key = "12344404K53N5571lz91673701u04321"; //签名密钥（测试）
		//流水号
		String serialnumber = StringUtil.getDateString();
		LogUtil.writeLog(content, "Record");
		LogUtil.writeLog(serialnumber, "lsh");
		
		Map message = new HashMap();
		message.put("channel_id", "100000000193");
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
		//如果返回的结果为null 说明这次的校验是失败的
		if(resReport==null) {
			LogUtil.writeLog("出错了，请求返空"+new Date().getTime(), "Insurance");
			ReturnJson reJson = new ReturnJson();
			reJson.setParamsError();
			reJson.setMessage("保险接口请求失败");
			String reContent = JsonUtil.parseObj(reJson);
			return ok(reContent);
		}
		//将返回的结果转换成json对象
		JSONObject jsonObject = JSON.parseObject(resReport);
		//将对象和密钥传入业务统一方法
		/**
		 * 请求核保接口返回的结果
		 * 返回的结果类型：
		 * null
		 * 正确解密的请求报文
		 */
		String response = getContent(jsonObject, md5_key, aes_key);
		Ebean.beginTransaction();
		if(response!=null) {
			InsuranceByPay payModel = new InsuranceByPay();
			payModel.setTotal_fee("0.01");
			payModel.setOut_trade_no(serialnumber);
//			payModel.setProductname("家财险尊享版");
			payModel.setProductname("泰康在线保险");
			payModel.setNotifyurl("http://www.sknhome.com/v1/saveInsuranceOrder");//后台回调函数
			payModel.setCallbackurl("http://www.sknhome.com/v1/UserInfoInsurance.html?deviceid="+deviceid);//成功页面跳转地址
			payModel.setFailurl("http://www.sknhome.com/v1/UserInfoInsurance.html?deviceid="+deviceid);//失败页面跳转地址
			payModel.setChannel_id("100000000193");
			payModel.setPayType("01");//支付类型
//			payModel.setOpen_id("");//微信小程序id
//			payModel.setApp_id("");//小程序id
			
			String sign_code = "total_fee="+payModel.getTotal_fee()+"&key="+md5_key ;
			payModel.setSign(md5(sign_code));//签名 
			//请求生成支付链接接口
			String payModelJson = JsonUtil.parseObj(payModel);
			
			String payInterfaceResp = initHttpClient(payUrl, payModel);  
			JSONObject initPayJson = null;
			try {
				initPayJson = JSON.parseObject(payInterfaceResp);
			}catch (Exception e) {
				Ebean.rollbackTransaction();
				LogUtil.writeLog("JSON解析失败，付费版的支付响应链接不是JSON格式", "Insurance");
				ReturnJson reJson = new ReturnJson();
				reJson.setParamsError();
				reJson.setMessage("JSON解析失败");//直接返回错误信息让前端alert
				String reContent = JsonUtil.parseObj(reJson);
				return ok(reContent);
			}
			if(initPayJson==null) {
				LogUtil.writeLog("付费版的支付链接出现错误", "Insurance");
				ReturnJson reJson = new ReturnJson();
				reJson.setParamsError();
				reJson.setMessage("付费版的支付链接出现错误");//直接返回错误信息让前端alert
				String reContent = JsonUtil.parseObj(reJson);
				return ok(reContent);
			}
			/**
			 * 解析返回回来的数据 如果失败返回null
			 */
			String payUrlResult = getPayUrl(initPayJson);
			JSONObject payResult = JSON.parseObject(payUrlResult);
			if(payUrlResult==null) {
				LogUtil.writeLog("生成支付链接出现错误 "+initPayJson.getString("msg"), "Insurance");
				ReturnJson reJson = new ReturnJson();
				reJson.setParamsError();
				reJson.setMessage(initPayJson.getString("msg"));//直接返回错误信息让前端alert
				String reContent = JsonUtil.parseObj(reJson);
				return ok(reContent);
			}else {//生成支付页面成功了
				SmartInsurance ins = new SmartInsurance();
				ins.setAddtime(new Date());//添加时间
				ins.setAddress(addr_info);//详细地址
				try {
					ins.setBegintime(dateFormat.parse(start_date));
					ins.setOvertime(dateFormat.parse(endDate));
				} catch (ParseException e) {
					Ebean.rollbackTransaction();
					Ebean.endTransaction();
					LogUtil.writeLog("日期格式解析错误", "Insurance");
					ReturnJson reJson = new ReturnJson();
					reJson.setParamsError();
					reJson.setMessage(jsonObject.getString("日期格式解析错误"));//直接返回错误信息让前端alert
					String reContent = JsonUtil.parseObj(reJson);
					return ok(reContent);
				}//起保日期
				ins.setCardnum(licensenumber);//证件号
				ins.setCardtype(1);//证件类型
				ins.setCity(area);//市
				ins.setCoverage(9000);//保额
				ins.setDistrict("");//区
				ins.setDeviceid(deviceid);//设备id
				ins.setEmail(email);//邮箱
				ins.setName(insured_name);//被保人姓名
				ins.setPhone(phone);//被保人手机号
				ins.setProvince("");//省
				ins.setRemark("赠送版保险");//备注
				ins.setStatus(1);//状态 处理中
				ins.setValidity(1);//有效期 单位:年
				ins.setBillno(payResult.getString("billno"));
				ins.setWarrantyurl((String)payResult.get("policy_url"));
				ins.setPolicyno((String)payResult.get("policy_no"));
				ins.setValidity(1);//有效期 单位:年
				Ebean.getServer(GlobalDBControl.getDB()).save(ins);
				Ebean.commitTransaction();
				Ebean.endTransaction();
				LogUtil.writeLog(payResult.getString("payUrl"), "payurl");
				return redirect(payResult.getString("payUrl"));
			}
			
		}else {
			Ebean.endTransaction();
			LogUtil.writeLog("校验数失败", "Insurance");
			ReturnJson reJson = new ReturnJson();
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
	 * 获取支付链接和流水号
	 * 失败则返回null
	 * @return json格式的支付链接+支付流水号
	 */
	public static String getPayUrl(JSONObject jsonObject) {
		
		String result = jsonObject.getString("code");
		//校验返回结果
		String response = null;
		if("200".equals(result)){
			return jsonObject.getString("result");
		}else{
			return response;
		}
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
	public static <O> String initHttpClient(String url,O o) {
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
	 * 支付成功回调方法
	 */
	public static Result saveInsuranceOrder() {
		InsuranceResponseModel irm = new InsuranceResponseModel();
		irm.setProcess_date(dateFormat.format(new Date()));
		
		String json = request().body().asJson().toString();
		
		String aes_key = "D21Uq65MzDF28PaEx7zO0dSM2WuG03z0";//报文密钥（测试）
		String md5_key = "12344404K53N5571lz91673701u04321"; //签名密钥（测试）
		
		Map response = JSONObject.parseObject(json, Map.class);
		
		String sign = (String)response.get("sign");  //获取报文中的验签
		String callbackURL = (String)response.get("callbackURL"); //回调地址
		//校验验签是否正确
		response.remove("sign") ; 
		response.remove("callbackURL") ; 
		//排序生成验签
		String sortedStr = JSONObject.toJSONString(response,SerializerFeature.MapSortField);
		String signGenerate = md5(sortedStr+md5_key);
		LogUtil.writeLog("3","stat");
		if(signGenerate.equals(sign)){
			//解密数据
			String desResponse = decryptAES((String)response.get("biz_content"),aes_key); 
			Map resParams = JSONObject.parseObject(desResponse, Map.class) ;  
			Ebean.getServer(GlobalDBControl.getDB()).createUpdate(SmartInsurance.class, "update smart_insurance set status='0' where billno='"+resParams.get("billno")+"'").execute();
			irm.setBillno((String)resParams.get("billno"));
			irm.setCode("200");
			irm.setMessage("回调执行成功");
			LogUtil.writeLog("success","stat");
			return ok(Json.toJson(irm));
		}else{
			irm.setBillno("");
			irm.setCode("500");
			irm.setMessage("身份校验失败");
			LogUtil.writeLog("fail","stat");
			return ok(Json.toJson(irm));
		}
	}
	
}
