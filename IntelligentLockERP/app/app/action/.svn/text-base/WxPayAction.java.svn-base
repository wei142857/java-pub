package app.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.EShopConsignee;
import models.EShopOrders;
import models.EShopProduct;
import models.SmartAppUser;
import models.SmartBountyEshop;
import models.SmartPayOrder;
import models.SubInstallOrders;
import models.SubMeasureOrders;
import models.SubWxUser;
import pay.ext.kit.HttpKit;
import pay.ext.kit.IpKit;
import pay.ext.kit.PaymentKit;
import pay.ext.kit.StrKit;
import pay.properties.WxGzhWapPayProperties;
import pay.properties.WxPayProperties;
import pay.properties.WxWapPayProperties;
import pay.util.StringUtils;
import pay.weixin.api.WxPayApi;
import pay.weixin.api.WxPayApi.TradeType;
import pay.weixin.api.WxPayApiConfigKit;
import pay.weixin.entity.H5ScencInfo;
import pay.weixin.entity.H5ScencInfo.H5;
import play.mvc.Result;
import play.mvc.With;
import util.GlobalDBControl;
import util.GlobalSetting;
import util.LogUtil;
import util.StringUtil;
import util.classEntity.StringTool;
import util.json.JsonUtil;
import ServiceDao.WxGzhService;
import app.dto.ReturnJson;
import app.service.PayService;
import app.service.SubMsgService;
import app.service.SubWxUserService;
import app.service.UserService;
import app.service.VipService;
import app.util.AppUtil;

import com.avaje.ebean.Ebean;

import controllers.SysConfigAction;


public class WxPayAction extends BaseAction {

	@With(WxPayProperties.class)
	public static Result buyVip_Wx() {
		if (!checkSign())
			return makSignFailor();
		ReturnJson reJson = new ReturnJson();
		String token = request().getQueryString("token");
		String appid = request().getQueryString("appid");
		if (StringUtil.isNull(token)) {
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}

		SmartAppUser user = UserService.findUserByToken(token);
		if (user == null) {
			reJson.setTokenTimeOut();
		} else {
			String ip = IpKit.getRealIp(request());
			if (StrKit.isBlank(ip)) {
				ip = "127.0.0.1";
			}
			String vipPrice_Config = SysConfigAction.findSysconfig("智能锁",
					"会员价格");
			if (StringTool.isNull(vipPrice_Config))
				vipPrice_Config = "365.00";
			int vipPrice = new BigDecimal(vipPrice_Config).multiply(
					new BigDecimal(100)).intValue();
			String outTradeNo = StringUtils.getOutTradeNo();
			Map<String, String> params = WxPayApiConfigKit.getWxPayApiConfig()

			.setBody("智能会员").setDetail("智能会员").setAttach("1")
					.setOutTradeNo(outTradeNo)

					.setSpbillCreateIp(ip).setTotalFee(vipPrice + "")
					.setTradeType(TradeType.APP)
					.setNotifyUrl(getDomain() + "/v1/notify_buyVip_Wx").build();

			String xmlResult = WxPayApi.pushOrder(false, params);

			StringBuffer sb = new StringBuffer();
			int i = 1;
			for (Map.Entry<String, String> entry : params.entrySet()) {
				if (entry.getValue() != null) {
					sb.append(entry.getKey() + " = " + entry.getValue());
					if (i != params.size())
						sb.append(",");
				}
				i++;
			}

			LogUtil.writeLog("buyVip_Wx--" + "\t" + sb.toString() + "\t"
					+ xmlResult, "weixinPay");

			Map<String, String> result = PaymentKit.xmlToMap(xmlResult);

			String return_code = result.get("return_code");
			String return_msg = result.get("return_msg");
			if (!PaymentKit.codeIsOK(return_code)) {
				reJson.setCode(1);
				reJson.setMessage(return_msg);
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			String result_code = result.get("result_code");
			if (!PaymentKit.codeIsOK(result_code)) {
				reJson.setCode(2);
				reJson.setMessage(return_msg);
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			String prepayId = result.get("prepay_id");
			// 生成订单
			SmartPayOrder smartPayOrder = new SmartPayOrder();
			smartPayOrder.setAddtime(new Date());
			smartPayOrder.setChannel(appid);
			smartPayOrder.setOutTradeNo(outTradeNo);
			smartPayOrder.setPrepayId(prepayId);
			smartPayOrder.setRemark("一年智能会员");
			smartPayOrder.setStatus(SmartPayOrder.STATUS_TODO);
			smartPayOrder.setTotalFee(vipPrice);
			smartPayOrder.setType(SmartPayOrder.TYPE_WX);
			smartPayOrder.setUserid(user.getIdd());
			smartPayOrder.setIp(ip);
			Ebean.getServer(GlobalDBControl.getDB()).save(smartPayOrder);
			Map<String, String> packageParams = new HashMap<String, String>();
			packageParams.put("appid", WxPayApiConfigKit.getWxPayApiConfig()
					.getAppId());
			packageParams.put("partnerid", WxPayApiConfigKit
					.getWxPayApiConfig().getMchId());
			packageParams.put("prepayid", prepayId);
			packageParams.put("package", "Sign=WXPay");
			packageParams.put("noncestr", PaymentKit.generateNonceStr());
			packageParams.put("timestamp", System.currentTimeMillis() / 1000
					+ "");
			String packageSign = PaymentKit.createSign(packageParams,
					WxPayApiConfigKit.getWxPayApiConfig().getPaternerKey());
			packageParams.put("sign", packageSign);

			reJson.setSuccess();
			reJson.setData(packageParams);

		}
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);

	}
	
	@With(WxGzhWapPayProperties.class)
	public static Result buyVip_Wx_Web(){
		String openid = session(GlobalSetting.GZH_OPENID);
		ReturnJson reJson = new ReturnJson();
		if(openid!=null){
			SubWxUser subWxUser = SubWxUserService.findIllegalNotifyWxUser(openid);
			SmartAppUser user = UserService.findUserByPhone(subWxUser.getPhone());
			if (user == null) {
				reJson.setTokenTimeOut();
			} else {

				String ip = IpKit.getRealIp(request());
				if (StrKit.isBlank(ip)) {
					ip = "127.0.0.1";
				}
				
				String vipPrice_Config = SysConfigAction.findSysconfig("公众号智能锁",
						"会员价格");
				if (StringTool.isNull(vipPrice_Config))
					vipPrice_Config = "365.00";
				int vipPrice = new BigDecimal(vipPrice_Config).multiply(
						new BigDecimal(100)).intValue();
				String outTradeNo = StringUtils.getOutTradeNo();
				
				Map<String, String> params = WxPayApiConfigKit.getWxPayApiConfig()
						.setAttach("1")
						.setBody("智能会员")
						.setDetail("智能会员")
						.setOpenId(openid)
						.setSpbillCreateIp(ip)
						.setTotalFee(vipPrice+"")
						.setTradeType(TradeType.JSAPI)
						.setNotifyUrl(getDomain()+"/v1/notify_buyVip_Wx")
						.setOutTradeNo(outTradeNo)
						.build();
				
				String xmlResult = WxPayApi.pushOrder(false,params);
				
				StringBuffer sb = new StringBuffer();
				int i = 1;
				for (Map.Entry<String, String> entry : params.entrySet()) {
					if (entry.getValue() != null) {
						sb.append(entry.getKey() + " = " + entry.getValue());
						if (i != params.size())
							sb.append(",");
					}
					i++;
				}

				LogUtil.writeLog("buyVip_Wx_Web--" + "\t" + sb.toString() + "\t"
						+ xmlResult, "weixinPay");
				
				Map<String, String> result = PaymentKit.xmlToMap(xmlResult);
				
				String return_code = result.get("return_code");
				String return_msg = result.get("return_msg");
				if (!PaymentKit.codeIsOK(return_code)) {
					reJson.setCode(1);
					reJson.setMessage(return_msg);
					String reContent = JsonUtil.parseObj(reJson);
					appLogger.debug(reContent);
					return ok(reContent);
				}
				String result_code = result.get("result_code");
				if (!PaymentKit.codeIsOK(result_code)) {
					reJson.setCode(2);
					reJson.setMessage(return_msg);
					String reContent = JsonUtil.parseObj(reJson);
					appLogger.debug(reContent);
					return ok(reContent);
				}
				// 以下字段在return_code 和result_code都为SUCCESS的时候有返回
				String prepayId = result.get("prepay_id");
				
				// 生成订单
				SmartPayOrder smartPayOrder = new SmartPayOrder();
				smartPayOrder.setAddtime(new Date());
				smartPayOrder.setChannel("gzh");
				smartPayOrder.setOutTradeNo(outTradeNo);
				smartPayOrder.setPrepayId(prepayId);
				smartPayOrder.setRemark("一年智能会员");
				smartPayOrder.setStatus(SmartPayOrder.STATUS_TODO);
				smartPayOrder.setTotalFee(vipPrice);
				smartPayOrder.setType(SmartPayOrder.TYPE_WX);
				smartPayOrder.setUserid(user.getIdd());
				smartPayOrder.setIp(ip);
				Ebean.getServer(GlobalDBControl.getDB()).save(smartPayOrder);
				
				Map<String, String> packageParams = PaymentKit.prepayIdCreateSign(prepayId);
				reJson.setSuccess();
				reJson.setData(packageParams);
			
			}
		}
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}
	
	@With(WxPayProperties.class)
	public static Result notify_buyVip_Wx() {
		String xmlMsg = HttpKit.readData(request());
		LogUtil.writeLog("notify_buyVip_Wx--" + "\t" + xmlMsg, "weixinPay");
		Map<String, String> params = PaymentKit.xmlToMap(xmlMsg);
		// 签名验证
		if (PaymentKit.verifyNotify(params, WxPayApiConfigKit
				.getWxPayApiConfig().getPaternerKey())) {
			String result_code = params.get("result_code");
			if (PaymentKit.codeIsOK(result_code)) {

				String transaction_id = params.get("transaction_id");
				String out_trade_no = params.get("out_trade_no");
				String total_fee = params.get("total_fee");
				SmartPayOrder smartPayOrder = Ebean
						.getServer(GlobalDBControl.getDB())
						.find(SmartPayOrder.class).where()
						.eq("out_trade_no", out_trade_no).findUnique();

				if (smartPayOrder != null) {
					if (smartPayOrder.getStatus() == SmartPayOrder.STATUS_TODO) {
						if (new BigDecimal(total_fee).intValue() == smartPayOrder
								.getTotalFee()) {
							String sql = "update smart_payorder set transaction_id=:transaction_id,status=:status"
									+ ",notifytime=sysdate() where idd=:idd";
							Ebean.getServer(GlobalDBControl.getDB())
									.createSqlUpdate(sql)
									.setParameter("transaction_id",
											transaction_id)
									.setParameter("status",
											SmartPayOrder.STATUS_SUCCESS)
									.setParameter("idd", smartPayOrder.getIdd())
									.execute();

							// 支付成功充值会员
							VipService.addVipValidityTimeByRecharge(smartPayOrder.getUserid());
							Map<String, String> xml = new HashMap<String, String>();
							xml.put("return_code", "SUCCESS");
							xml.put("return_msg", "OK");
							return ok(PaymentKit.toXml(xml));
						}
					}
				}
			}
		}
		return ok("");
	}
	//商品不需要支付人民币时候复用此方法
	@With(WxPayProperties.class)
	public static Result buyEShopProduct_Wx() {
		if (!checkSign())
			return makSignFailor();
		ReturnJson reJson = new ReturnJson();
		String token = request().getQueryString("token");
		String appid = request().getQueryString("appid");
		String pid = request().getQueryString("pid");
		String amount = request().getQueryString("amount");
		String consigneeId = request().getQueryString("consigneeId");
		String bvalue = request().getQueryString("bvalue");
		String smsCode = request().getQueryString("smsCode");
		String source = session("source");
		
		if (StringUtil.isNull(token)) {
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		//判断参数amount是否合法
		Integer namount = StringTool.GetInt(amount, 0);
		if(namount<=0){
			reJson.setMessage("购买数量填写不正确,请重新选择");
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		Integer nconsigneeId = StringTool.GetInt(consigneeId, 0);
		Integer npid = StringTool.GetInt(pid, 0);
		Integer nbvalue = StringTool.GetInt(bvalue, 0);
		SmartAppUser user = UserService.findUserByToken(token);
		if (user == null) {
			reJson.setTokenTimeOut();
		} else {
			String ip = IpKit.getRealIp(request());
			if (StrKit.isBlank(ip)) {
				ip = "127.0.0.1";
			}
			
			if(nbvalue>0){
				String code = AppUtil.getRandomCode(user.getPhone(), AppUtil.TYPE_SMS_ESHOPPAY);
				if(code==null || "".equals(code) || 
						smsCode==null || "".equals(smsCode) || !smsCode.equals(code)){
					reJson.setMessage("验证码输入错误");
					String reContent = JsonUtil.parseObj(reJson);
					appLogger.debug(reContent);
					return ok(reContent);
				}
			}
			String outTradeNo = StringUtils.getOutTradeNo();
			
			EShopProduct product = Ebean.getServer(GlobalDBControl.getReadDB())
					.find(EShopProduct.class).where().eq("idd", pid).findUnique();
			if(product==null){
				reJson.setMessage("商品不存在");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			//检查商品状态
			if(product.getStatus()==-1){
				reJson.setMessage("商品已下线,请选择其他商品购买");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			//检查地址是否存在
			EShopConsignee consignee = PayService.selectAddressByIDD(nconsigneeId,user.getIdd());
			if(consignee==null){
				reJson.setMessage("收货地址无效,请添加新的收货地址或选择其它的收货地址");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			//检查库存
			if(product.getStock()<namount){
				reJson.setMessage("商品库存不足,请稍后留意库存数量");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			//目前发现实际支付时奖励金数量不足的时候就直接返回失败结果
			Integer currentBounty = VipService.findBountyByUserid(user.getIdd());
			if(currentBounty>0&&nbvalue>0&&currentBounty<nbvalue){
				reJson.setMessage("奖励金数量不足");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			//扣除库存
			PayService.redeceEshopProductStock(npid,product.getStock(),namount);
			//检查是否会员 根据是否会员选择支付价格(checkVipResult:(0:会员 1:过期 2:非会员))
			Integer checkVipResult = PayService.checkIsVip(user.getIdd());
			//生成商城订单
			EShopOrders order = PayService.createOrder(user,product,consignee,checkVipResult,namount,outTradeNo,source);
			if(order==null){
				reJson.setMessage("数据错误,订单生成失败");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			//如果用户账户剩余的奖励金不足以全部支付购买商品所需奖励金时,那么就将剩余的奖励金全部抵扣,不足的用人民币支付)
			//判断用户账户剩余奖励金是否足够抵扣该笔订单所需支付奖励金
			List<SmartBountyEshop> bountyEshopList = new ArrayList<SmartBountyEshop>();
			if(currentBounty>0&&nbvalue>0&&currentBounty>=nbvalue){
				//奖励金足够抵扣
				bountyEshopList = PayService.payAPartOfBvalue(user.getIdd(),nbvalue,product.getTitle());
				//将商城订单补充完整,并保存到数据库中
				order.setBvalue(nbvalue);	//所抵扣的奖励金个数
				//奖励金足够抵付 无需再支付
				double money = new BigDecimal(order.getMoney()+"").subtract(new BigDecimal((nbvalue/10d)+"")).doubleValue();
				order.setMoney((money)<0?0:money);
				//此时 用户所需支付的金额为0 则直接将订单状态为支付成功:0
				if(money<=0){
					order.setStatus(0);
					order.setUpdatetime(new Date());
				}
			}else if(currentBounty>0&&nbvalue>0&&currentBounty<nbvalue){
				//奖励金不足,将剩余的奖励金扣除,不足的部分用人民币支付
				bountyEshopList = PayService.payAllBvalue(user.getIdd(),currentBounty,product.getTitle());
				//将商城订单补充完整,并保存到数据库中
				order.setBvalue(currentBounty);		//所抵扣的奖励金个数
				//设置所需支付的钱
				double money = new BigDecimal(order.getMoney()+"").subtract(new BigDecimal((nbvalue/10d)+"")).doubleValue();
				order.setMoney((money)<0?0:money);	//支付价格-奖励金抵扣的钱
			}else{	//用户没有选择使用奖励金,或是剩余的奖励金个数为0
				order.setBvalue(0);
				order.setMoney(order.getMoney());
			}
			//将订单信息保存到数据库中
			Ebean.getServer(GlobalDBControl.getDB()).save(order);
			if(bountyEshopList!=null&&bountyEshopList.size()>0){
				//将bountyEshopList(存档的奖励金消耗记录)保存
				for(SmartBountyEshop bountyEshop:bountyEshopList){
					bountyEshop.setOid(order.getIdd());
				}
				Ebean.getServer(GlobalDBControl.getDB()).save(bountyEshopList);
			}
			int totalfee = new BigDecimal(order.getMoney()+"").multiply(new BigDecimal(100)).intValue();
			//所需支付的钱>0
			if(totalfee>0){
				Calendar cal = Calendar.getInstance();
				cal.setTime(order.getAddtime());
				cal.add(Calendar.MINUTE, 2);
				Map<String, String> params = WxPayApiConfigKit.getWxPayApiConfig()
						.setBody(product.getTitle()).setDetail(product.getTitle())
//					.setAttach("")
						.setOutTradeNo(outTradeNo)
						.setTimeExpire(StringUtil.getDateTimeString(cal.getTime(), "yyyyMMddHHmmss"))
						.setSpbillCreateIp(ip).setTotalFee(totalfee+"")
						.setTradeType(TradeType.APP)
						.setNotifyUrl(getDomain() + "/v1/notify_buyEShopProduct_Wx").build();
				
				String xmlResult = WxPayApi.pushOrder(false, params);
				
				StringBuffer sb = new StringBuffer();
				int i = 1;
				for (Map.Entry<String, String> entry : params.entrySet()) {
					if (entry.getValue() != null) {
						sb.append(entry.getKey() + " = " + entry.getValue());
						if (i != params.size())
							sb.append(",");
					}
					i++;
				}
				
				LogUtil.writeLog("buyEShopProduct_Wx--" + "\t" + sb.toString() + "\t"
						+ xmlResult, "weixinPay");
				
				Map<String, String> result = PaymentKit.xmlToMap(xmlResult);
				
				String return_code = result.get("return_code");
				String return_msg = result.get("return_msg");
				if (!PaymentKit.codeIsOK(return_code)) {
					reJson.setCode(1);
					reJson.setMessage(return_msg);
					String reContent = JsonUtil.parseObj(reJson);
					appLogger.debug(reContent);
					return ok(reContent);
				}
				String result_code = result.get("result_code");
				if (!PaymentKit.codeIsOK(result_code)) {
					reJson.setCode(2);
					reJson.setMessage(return_msg);
					String reContent = JsonUtil.parseObj(reJson);
					appLogger.debug(reContent);
					return ok(reContent);
				}
				String prepayId = result.get("prepay_id");
				// 生成支付订单
				SmartPayOrder smartPayOrder = new SmartPayOrder();
				smartPayOrder.setAddtime(new Date());
				smartPayOrder.setChannel(appid);
				smartPayOrder.setOutTradeNo(outTradeNo);
				smartPayOrder.setPrepayId(prepayId);
				smartPayOrder.setRemark(product.getTitle());
				smartPayOrder.setStatus(SmartPayOrder.STATUS_TODO);
				smartPayOrder.setTotalFee(totalfee);
				smartPayOrder.setType(SmartPayOrder.TYPE_WX);
				smartPayOrder.setUserid(user.getIdd());
				smartPayOrder.setIp(ip);
				Ebean.getServer(GlobalDBControl.getDB()).save(smartPayOrder);
				
				
				Map<String, String> packageParams = new HashMap<String, String>();
				packageParams.put("appid", WxPayApiConfigKit.getWxPayApiConfig()
						.getAppId());
				packageParams.put("partnerid", WxPayApiConfigKit
						.getWxPayApiConfig().getMchId());
				packageParams.put("prepayid", prepayId);
				packageParams.put("package", "Sign=WXPay");
				packageParams.put("noncestr", PaymentKit.generateNonceStr());
				packageParams.put("timestamp", System.currentTimeMillis() / 1000
						+ "");
				String packageSign = PaymentKit.createSign(packageParams,
						WxPayApiConfigKit.getWxPayApiConfig().getPaternerKey());
				packageParams.put("sign", packageSign);
				packageParams.put("orderid", order.getIdd()+"");
				
				reJson.setSuccess();
				reJson.setData(packageParams);
			}else{
				reJson.setSuccess();
				Map<String, String> packageParams = new HashMap<String, String>();
				packageParams.put("orderid", order.getIdd()+"");
				reJson.setData(packageParams);
			}
		}
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);

	}
	
	@With(WxWapPayProperties.class)
	public static Result buyEShopProduct_Wx_Wap(){
		String token = session("token");
		String pid = request().getQueryString("pid");
		String amount = request().getQueryString("amount");
		String bvalue = request().getQueryString("bvalue");
		String consigneeId = request().getQueryString("consigneeId");
		String smsCode = request().getQueryString("smsCode");
		String source = session("source");

		ReturnJson reJson = new ReturnJson();
		//判断参数amount是否合法
		Integer namount = StringTool.GetInt(amount, 0);
		if(namount<=0){
			reJson.setMessage("购买数量填写不正确,请重新选择");
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		Integer nconsigneeId = StringTool.GetInt(consigneeId, 0);
		Integer npid = StringTool.GetInt(pid, 0);
		Integer nbvalue = StringTool.GetInt(bvalue, 0);
		SmartAppUser user = UserService.findUserByToken(token);
		if (user == null) {
			reJson.setTokenTimeOut();
		} else {
			String ip = IpKit.getRealIp(request());
			if (StrKit.isBlank(ip)) {
				ip = "127.0.0.1";
			}
			if(nbvalue>0){
				String code = AppUtil.getRandomCode(user.getPhone(), AppUtil.TYPE_SMS_ESHOPPAY);
				if(code==null || "".equals(code) || 
						smsCode==null || "".equals(smsCode) || !smsCode.equals(code)){
					reJson.setMessage("验证码输入错误");
					String reContent = JsonUtil.parseObj(reJson);
					appLogger.debug(reContent);
					return ok(reContent);
				}
			}
			
			String outTradeNo = StringUtils.getOutTradeNo();
			
			EShopProduct product = Ebean.getServer(GlobalDBControl.getReadDB())
					.find(EShopProduct.class).where().eq("idd", pid).findUnique();
			if(product==null){
				reJson.setMessage("商品不存在");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			//检查商品状态
			if(product.getStatus()==-1){
				reJson.setMessage("商品已下线,请选择其他商品购买");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			//检查地址是否存在
			EShopConsignee consignee = PayService.selectAddressByIDD(nconsigneeId,user.getIdd());
			if(consignee==null){
				reJson.setMessage("收货地址无效,请添加新的收货地址或选择其它的收货地址");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			//检查库存
			if(product.getStock()<namount){
				reJson.setMessage("商品库存不足,请稍后留意库存数量");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			Integer currentBounty = VipService.findBountyByUserid(user.getIdd());
			if(currentBounty>0&&nbvalue>0&&currentBounty<nbvalue){
				reJson.setMessage("奖励金数量不足");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			//扣除库存
			PayService.redeceEshopProductStock(npid,product.getStock(),namount);
			//检查是否会员 根据是否会员选择支付价格(checkVipResult:(0:会员 1:过期 2:非会员))
			Integer checkVipResult = PayService.checkIsVip(user.getIdd());
			//生成商城订单
			EShopOrders order = PayService.createOrder(user,product,consignee,checkVipResult,namount,outTradeNo,source);
			if(order==null){
				reJson.setMessage("数据错误,订单生成失败");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			//如果用户账户剩余的奖励金不足以全部支付购买商品所需奖励金时,那么就将剩余的奖励金全部抵扣,不足的用人民币支付)
			//判断用户账户剩余奖励金是否足够抵扣该笔订单所需支付奖励金
			List<SmartBountyEshop> bountyEshopList = new ArrayList<SmartBountyEshop>();
			if(currentBounty>0&&nbvalue>0&&currentBounty>=nbvalue){
				//奖励金足够抵扣
				bountyEshopList = PayService.payAPartOfBvalue(user.getIdd(),nbvalue,product.getTitle());
				//将商城订单补充完整,并保存到数据库中
				order.setBvalue(nbvalue);	//所抵扣的奖励金个数
				//奖励金足够抵付 无需再支付
				double money = new BigDecimal(order.getMoney()+"").subtract(new BigDecimal((nbvalue/10d)+"")).doubleValue();
				order.setMoney((money)<0?0:money);
				//此时 用户所需支付的金额为0 则直接将订单状态为支付成功:0
				if(money<=0){
					order.setStatus(0);
					order.setUpdatetime(new Date());
				}
			}else if(currentBounty>0&&nbvalue>0&&currentBounty<nbvalue){
				//奖励金不足,将剩余的奖励金扣除,不足的部分用人民币支付
				bountyEshopList = PayService.payAllBvalue(user.getIdd(),currentBounty,product.getTitle());
				//将商城订单补充完整,并保存到数据库中
				order.setBvalue(currentBounty);		//所抵扣的奖励金个数
				//设置所需支付的钱
				double money = new BigDecimal(order.getMoney()+"").subtract(new BigDecimal((nbvalue/10d)+"")).doubleValue();
				order.setMoney((money)<0?0:money);	//支付价格-奖励金抵扣的钱
			}else{	//用户没有选择使用奖励金,或是剩余的奖励金个数为0
				order.setBvalue(0);
				order.setMoney(order.getMoney());
			}
			//将订单信息保存到数据库中
			Ebean.getServer(GlobalDBControl.getDB()).save(order);
			if(bountyEshopList!=null&&bountyEshopList.size()>0){
				//将bountyEshopList(存档的奖励金消耗记录)保存
				for(SmartBountyEshop bountyEshop:bountyEshopList){
					bountyEshop.setOid(order.getIdd());
				}
				Ebean.getServer(GlobalDBControl.getDB()).save(bountyEshopList);
			}
			int totalfee = new BigDecimal(order.getMoney()+"").multiply(new BigDecimal(100)).intValue();
			if(totalfee>0){
				H5ScencInfo sceneInfo = new H5ScencInfo();
				H5 h5_info = new H5();
				h5_info.setType("Wap");
				//此域名必须在商户平台--"产品中心"--"开发配置"中添加
				h5_info.setWap_url("http://www.sknhome.com");
				h5_info.setWap_name("skn智能生活");
				sceneInfo.setH5_info(h5_info);
				Calendar cal = Calendar.getInstance();
				cal.setTime(order.getAddtime());
				cal.add(Calendar.MINUTE, 2);
				Map<String, String> params = WxPayApiConfigKit.getWxPayApiConfig()
//						.setAttach("")
						.setBody(product.getTitle())
						.setDetail(product.getTitle())
						.setSpbillCreateIp(ip)
						.setTotalFee(totalfee+"")
						.setTradeType(TradeType.MWEB)
						.setNotifyUrl(getDomain() + "/v1/notify_buyEShopProduct_Wx")
						.setOutTradeNo(outTradeNo)
						.setTimeExpire(StringUtil.getDateTimeString(cal.getTime(), "yyyyMMddHHmmss"))
						.setSceneInfo(sceneInfo.toString())
						.build();
				
				String xmlResult = WxPayApi.pushOrder(false,params);
				
				StringBuffer sb = new StringBuffer();
				int i = 1;
				for (Map.Entry<String, String> entry : params.entrySet()) {
					if (entry.getValue() != null) {
						sb.append(entry.getKey() + " = " + entry.getValue());
						if (i != params.size())
							sb.append(",");
					}
					i++;
				}

				LogUtil.writeLog("buyEShopProduct_Wx_Wap--" + "\t" + sb.toString() + "\t"
						+ xmlResult, "weixinPay");
				
				Map<String, String> result = PaymentKit.xmlToMap(xmlResult);
				
				String return_code = result.get("return_code");
				String return_msg = result.get("return_msg");
				if (!PaymentKit.codeIsOK(return_code)) {
					reJson.setCode(1);
					reJson.setMessage(return_msg);
					String reContent = JsonUtil.parseObj(reJson);
					appLogger.debug(reContent);
					return ok(reContent);
				}
				String result_code = result.get("result_code");
				if (!PaymentKit.codeIsOK(result_code)) {
					reJson.setCode(2);
					reJson.setMessage(return_msg);
					String reContent = JsonUtil.parseObj(reJson);
					appLogger.debug(reContent);
					return ok(reContent);
				}
				
				String prepayId = result.get("prepay_id");
				
				//生成支付订单
				SmartPayOrder smartPayOrder = new SmartPayOrder();
				smartPayOrder.setAddtime(new Date());
				smartPayOrder.setChannel("h5");
				smartPayOrder.setOutTradeNo(outTradeNo);
				smartPayOrder.setPrepayId(prepayId);
				smartPayOrder.setRemark(product.getTitle());
				smartPayOrder.setStatus(SmartPayOrder.STATUS_TODO);
				smartPayOrder.setTotalFee(totalfee);
				smartPayOrder.setType(SmartPayOrder.TYPE_WX);
				smartPayOrder.setUserid(user.getIdd());
				smartPayOrder.setIp(ip);
				Ebean.getServer(GlobalDBControl.getDB()).save(smartPayOrder);
				
				reJson.setSuccess();
				Map<String, String> data = new HashMap<String, String>();
				data.put("mweb_url", result.get("mweb_url"));
				data.put("orderid", order.getIdd()+"");
				reJson.setData(data);
			}else{
				reJson.setSuccess();
				Map<String, String> data = new HashMap<String, String>();
				data.put("orderid", order.getIdd()+"");
				reJson.setData(data);
			}
		}
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}
	
	@With(WxWapPayProperties.class)
	public static Result notify_buyEShopProduct_Wx() {
		String xmlMsg = HttpKit.readData(request());
		LogUtil.writeLog("notify_buyEShopProduct_Wx--" + "\t" + xmlMsg, "weixinPay");
		Map<String, String> params = PaymentKit.xmlToMap(xmlMsg);
		// 签名验证
		if (PaymentKit.verifyNotify(params, WxPayApiConfigKit
				.getWxPayApiConfig().getPaternerKey())) {
			String result_code = params.get("result_code");
			if (PaymentKit.codeIsOK(result_code)) {
				
				String transaction_id = params.get("transaction_id");
				String out_trade_no = params.get("out_trade_no");
				String total_fee = params.get("total_fee");
				SmartPayOrder smartPayOrder = Ebean
						.getServer(GlobalDBControl.getDB())
						.find(SmartPayOrder.class).where()
						.eq("out_trade_no", out_trade_no).findUnique();

				if (smartPayOrder != null) {
					if (smartPayOrder.getStatus() == SmartPayOrder.STATUS_TODO) {
						if (new BigDecimal(total_fee).intValue() == smartPayOrder
								.getTotalFee()) {
							String sql = "update smart_payorder set transaction_id=:transaction_id,status=:status"
									+ ",notifytime=sysdate() where idd=:idd";
							Ebean.getServer(GlobalDBControl.getDB())
									.createSqlUpdate(sql)
									.setParameter("transaction_id",
											transaction_id)
									.setParameter("status",
											SmartPayOrder.STATUS_SUCCESS)
									.setParameter("idd", smartPayOrder.getIdd())
									.execute();

							//修改商城订单为已支付
							PayService.updateOrderStatus_0(out_trade_no);
							
							Map<String, String> xml = new HashMap<String, String>();
							xml.put("return_code", "SUCCESS");
							xml.put("return_msg", "OK");
							return ok(PaymentKit.toXml(xml));
						}
					}
				}
				
				
			}
		}
		return ok("");
	}
	
	
	@With(WxWapPayProperties.class)
	public static Result buyEShopProduct_Wx_Web(){
		String token = session("token");
		String pid = request().getQueryString("pid");
		String amount = request().getQueryString("amount");
		String bvalue = request().getQueryString("bvalue");
		String consigneeId = request().getQueryString("consigneeId");
		String smsCode = request().getQueryString("smsCode");
		String source = session("source");
		ReturnJson reJson = new ReturnJson();
		//判断参数amount是否合法
		Integer namount = StringTool.GetInt(amount, 0);
		if(namount<=0){
			reJson.setMessage("购买数量填写不正确,请重新选择");
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		Integer nconsigneeId = StringTool.GetInt(consigneeId, 0);
		Integer npid = StringTool.GetInt(pid, 0);
		Integer nbvalue = StringTool.GetInt(bvalue, 0);
		SmartAppUser user = UserService.findUserByToken(token);
		if (user == null) {
			reJson.setTokenTimeOut();
		} else {
			String openid = session(GlobalSetting.WX_OPENID);
			if(openid==null){
				reJson.setCode(3);
				reJson.setMessage("授权未通过");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			String ip = IpKit.getRealIp(request());
			if (StrKit.isBlank(ip)) {
				ip = "127.0.0.1";
			}
			
			if(nbvalue>0){
				String code = AppUtil.getRandomCode(user.getPhone(), AppUtil.TYPE_SMS_ESHOPPAY);
				if(code==null || "".equals(code) || 
						smsCode==null || "".equals(smsCode) || !smsCode.equals(code)){
					reJson.setMessage("验证码输入错误");
					String reContent = JsonUtil.parseObj(reJson);
					appLogger.debug(reContent);
					return ok(reContent);
				}
			}
			
			String outTradeNo = StringUtils.getOutTradeNo();
			
			EShopProduct product = Ebean.getServer(GlobalDBControl.getReadDB())
					.find(EShopProduct.class).where().eq("idd", pid).findUnique();
			if(product==null){
				reJson.setMessage("商品不存在");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			//检查地址是否存在
			EShopConsignee consignee = PayService.selectAddressByIDD(nconsigneeId,user.getIdd());
			if(consignee==null){
				reJson.setMessage("收货地址无效,请添加新的收货地址或选择其它的收货地址");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			//检查库存
			if(product.getStock()<namount){
				reJson.setMessage("商品库存不足,请稍后留意库存数量");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			Integer currentBounty = VipService.findBountyByUserid(user.getIdd());
			if(currentBounty>0&&nbvalue>0&&currentBounty<nbvalue){
				reJson.setMessage("奖励金数量不足");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			//扣除库存
			PayService.redeceEshopProductStock(npid,product.getStock(),namount);
			//检查是否会员 根据是否会员选择支付价格(checkVipResult:(0:会员 1:过期 2:非会员))
			Integer checkVipResult = PayService.checkIsVip(user.getIdd());
			//生成商城订单
			EShopOrders order = PayService.createOrder(user,product,consignee,checkVipResult,namount,outTradeNo,source);
			if(order==null){
				reJson.setMessage("数据错误,订单生成失败");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			//如果用户账户剩余的奖励金不足以全部支付购买商品所需奖励金时,那么就将剩余的奖励金全部抵扣,不足的用人民币支付)
			//判断用户账户剩余奖励金是否足够抵扣该笔订单所需支付奖励金
			List<SmartBountyEshop> bountyEshopList = new ArrayList<SmartBountyEshop>();
			if(currentBounty>0&&nbvalue>0&&currentBounty>=nbvalue){
				//奖励金足够抵扣
				bountyEshopList = PayService.payAPartOfBvalue(user.getIdd(),nbvalue,product.getTitle());
				//将商城订单补充完整,并保存到数据库中
				order.setBvalue(nbvalue);	//所抵扣的奖励金个数
				//奖励金足够抵付 无需再支付
				double money = new BigDecimal(order.getMoney()+"").subtract(new BigDecimal((nbvalue/10d)+"")).doubleValue();
				order.setMoney((money)<0?0:money);
				//此时 用户所需支付的金额为0 则直接将订单状态为支付成功:0
				if(money<=0){
					order.setStatus(0);
					order.setUpdatetime(new Date());
				}
			}else if(currentBounty>0&&nbvalue>0&&currentBounty<nbvalue){
				//奖励金不足,将剩余的奖励金扣除,不足的部分用人民币支付
				bountyEshopList = PayService.payAllBvalue(user.getIdd(),currentBounty,product.getTitle());
				//将商城订单补充完整,并保存到数据库中
				order.setBvalue(currentBounty);		//所抵扣的奖励金个数
				//设置所需支付的钱
				double money = new BigDecimal(order.getMoney()+"").subtract(new BigDecimal((nbvalue/10d)+"")).doubleValue();
				order.setMoney((money)<0?0:money);	//支付价格-奖励金抵扣的钱
			}else{	//用户没有选择使用奖励金,或是剩余的奖励金个数为0
				order.setBvalue(0);
				order.setMoney(order.getMoney());
			}
			//将订单信息保存到数据库中
			Ebean.getServer(GlobalDBControl.getDB()).save(order);
			if(bountyEshopList!=null&&bountyEshopList.size()>0){
				//将bountyEshopList(存档的奖励金消耗记录)保存
				for(SmartBountyEshop bountyEshop:bountyEshopList){
					bountyEshop.setOid(order.getIdd());
				}
				Ebean.getServer(GlobalDBControl.getDB()).save(bountyEshopList);
			}
			//发起支付
			int totalfee = new BigDecimal(order.getMoney()+"").multiply(new BigDecimal(100)).intValue();
			if(totalfee>0){
				Calendar cal = Calendar.getInstance();
				cal.setTime(order.getAddtime());
				cal.add(Calendar.MINUTE, 2);
				Map<String, String> params = WxPayApiConfigKit.getWxPayApiConfig()
//						.setAttach("")
						.setBody(product.getTitle())
						.setDetail(product.getTitle())
						.setOpenId(openid)
						.setSpbillCreateIp(ip)
						.setTotalFee(totalfee+"")
						.setTradeType(TradeType.JSAPI)
						.setNotifyUrl(getDomain()+"/v1/notify_buyEShopProduct_Wx")
						.setOutTradeNo(outTradeNo)
						.setTimeExpire(StringUtil.getDateTimeString(cal.getTime(), "yyyyMMddHHmmss"))
						.build();
				
				String xmlResult = WxPayApi.pushOrder(false,params);
				
				StringBuffer sb = new StringBuffer();
				int i = 1;
				for (Map.Entry<String, String> entry : params.entrySet()) {
					if (entry.getValue() != null) {
						sb.append(entry.getKey() + " = " + entry.getValue());
						if (i != params.size())
							sb.append(",");
					}
					i++;
				}

				LogUtil.writeLog("buyEShopProduct_Wx_Web--" + "\t" + sb.toString() + "\t"
						+ xmlResult, "weixinPay");
				
				Map<String, String> result = PaymentKit.xmlToMap(xmlResult);
				
				String return_code = result.get("return_code");
				String return_msg = result.get("return_msg");
				if (!PaymentKit.codeIsOK(return_code)) {
					reJson.setCode(1);
					reJson.setMessage(return_msg);
					String reContent = JsonUtil.parseObj(reJson);
					appLogger.debug(reContent);
					return ok(reContent);
				}
				String result_code = result.get("result_code");
				if (!PaymentKit.codeIsOK(result_code)) {
					reJson.setCode(2);
					reJson.setMessage(return_msg);
					String reContent = JsonUtil.parseObj(reJson);
					appLogger.debug(reContent);
					return ok(reContent);
				}
				// 以下字段在return_code 和result_code都为SUCCESS的时候有返回
				String prepayId = result.get("prepay_id");
				
				//生成支付订单
				SmartPayOrder smartPayOrder = new SmartPayOrder();
				smartPayOrder.setAddtime(new Date());
				smartPayOrder.setChannel("h5");
				smartPayOrder.setOutTradeNo(outTradeNo);
				smartPayOrder.setPrepayId(prepayId);
				smartPayOrder.setRemark(product.getTitle());
				smartPayOrder.setStatus(SmartPayOrder.STATUS_TODO);
				smartPayOrder.setTotalFee(totalfee);
				smartPayOrder.setType(SmartPayOrder.TYPE_WX);
				smartPayOrder.setUserid(user.getIdd());
				smartPayOrder.setIp(ip);
				Ebean.getServer(GlobalDBControl.getDB()).save(smartPayOrder);
				
				Map<String, String> packageParams = PaymentKit.prepayIdCreateSign(prepayId);
				packageParams.put("orderid", order.getIdd()+"");
				reJson.setSuccess();
				reJson.setData(packageParams);
			}else{
				reJson.setSuccess();
				Map<String, String> data = new HashMap<String,String>();
				data.put("orderid", order.getIdd()+"");
				reJson.setData(data);
			}
		}
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}
	
	@With(WxGzhWapPayProperties.class)
	public static Result buySubInstall_Wx_Web(){
		String openid = session(GlobalSetting.GZH_OPENID);
		String oid = request().getQueryString("oid");
		ReturnJson reJson = new ReturnJson();
		if(openid!=null){
			SubWxUser user = SubWxUserService.findSubWxUser(openid);
			if(user!=null){
				String ip = IpKit.getRealIp(request());
				if (StrKit.isBlank(ip)) {
					ip = "127.0.0.1";
				}
				
				SubInstallOrders subOrder = Ebean.getServer(GlobalDBControl.getReadDB())
					.find(SubInstallOrders.class).where().eq("phone", user.getPhone()).eq("idd", oid).findUnique();
				
				if(subOrder==null || subOrder.getStatus()!=1){
					reJson.setCode(3);
					reJson.setMessage("订单已结束");
					String reContent = JsonUtil.parseObj(reJson);
					appLogger.debug(reContent);
					return ok(reContent);
				}
				
				String outTradeNo = StringUtils.getOutTradeNo();
				
				int totalfee = StringTool.GetInt(SysConfigAction.findSysconfig("预约安装测量", "安装价格"), 1);
				
				Ebean.getServer(GlobalDBControl.getDB())
				.createSqlUpdate("update sub_install_orders set prepayid=:prepayid,money=:money where idd=:idd")
				.setParameter("prepayid", outTradeNo)
				.setParameter("money", totalfee+"")
				.setParameter("idd", oid)
				.execute();
				
				
				Calendar cal = Calendar.getInstance();
				cal.setTime(subOrder.getAddtime());
				cal.add(Calendar.MINUTE, 2);
				Map<String, String> params = WxPayApiConfigKit.getWxPayApiConfig()
						.setAttach("102")
						.setBody("上门安装")
						.setDetail("上门安装")
						.setOpenId(openid)
						.setSpbillCreateIp(ip)
						.setTotalFee(totalfee+"")
						.setTradeType(TradeType.JSAPI)
						.setNotifyUrl(getDomain()+"/v1/notify_buySubscribe_Wx")
						.setOutTradeNo(outTradeNo)
						.setTimeExpire(StringUtil.getDateTimeString(cal.getTime(), "yyyyMMddHHmmss"))
						.build();
				
				String xmlResult = WxPayApi.pushOrder(false,params);
				
				StringBuffer sb = new StringBuffer();
				int i = 1;
				for (Map.Entry<String, String> entry : params.entrySet()) {
					if (entry.getValue() != null) {
						sb.append(entry.getKey() + " = " + entry.getValue());
						if (i != params.size())
							sb.append(",");
					}
					i++;
				}

				LogUtil.writeLog("buySubInstall_Wx_Web--" + "\t" + sb.toString() + "\t"
						+ xmlResult, "weixinPay");
				
				Map<String, String> result = PaymentKit.xmlToMap(xmlResult);
				
				String return_code = result.get("return_code");
				String return_msg = result.get("return_msg");
				if (!PaymentKit.codeIsOK(return_code)) {
					reJson.setCode(1);
					reJson.setMessage(return_msg);
					String reContent = JsonUtil.parseObj(reJson);
					appLogger.debug(reContent);
					return ok(reContent);
				}
				String result_code = result.get("result_code");
				if (!PaymentKit.codeIsOK(result_code)) {
					reJson.setCode(2);
					reJson.setMessage(return_msg);
					String reContent = JsonUtil.parseObj(reJson);
					appLogger.debug(reContent);
					return ok(reContent);
				}
				// 以下字段在return_code 和result_code都为SUCCESS的时候有返回
				String prepayId = result.get("prepay_id");
				
				//生成支付订单
				SmartPayOrder smartPayOrder = new SmartPayOrder();
				smartPayOrder.setAddtime(new Date());
				smartPayOrder.setChannel("gzh");
				smartPayOrder.setOutTradeNo(outTradeNo);
				smartPayOrder.setPrepayId(prepayId);
				smartPayOrder.setRemark("上门安装");
				smartPayOrder.setStatus(SmartPayOrder.STATUS_TODO);
				smartPayOrder.setTotalFee(totalfee);
				smartPayOrder.setType(SmartPayOrder.TYPE_WX);
				smartPayOrder.setUserid(user.getIdd());
				smartPayOrder.setIp(ip);
				Ebean.getServer(GlobalDBControl.getDB()).save(smartPayOrder);
				
				Map<String, String> packageParams = PaymentKit.prepayIdCreateSign(prepayId);
				reJson.setSuccess();
				reJson.setData(packageParams);
			}
		}
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}
	
	@With(WxGzhWapPayProperties.class)
	public static Result buySubMeasure_Wx_Web(){
		String openid = session(GlobalSetting.GZH_OPENID);
		String oid = request().getQueryString("oid");
		ReturnJson reJson = new ReturnJson();
		if(openid!=null){
			SubWxUser user = SubWxUserService.findSubWxUser(openid);
			if(user!=null){
				String ip = IpKit.getRealIp(request());
				if (StrKit.isBlank(ip)) {
					ip = "127.0.0.1";
				}
				
				SubMeasureOrders subOrder = Ebean.getServer(GlobalDBControl.getReadDB())
					.find(SubMeasureOrders.class).where().eq("phone", user.getPhone()).eq("idd", oid).findUnique();
				
				if(subOrder==null || subOrder.getStatus()!=1){
					reJson.setCode(3);
					reJson.setMessage("订单已结束");
					String reContent = JsonUtil.parseObj(reJson);
					appLogger.debug(reContent);
					return ok(reContent);
				}
				
				String outTradeNo = StringUtils.getOutTradeNo();
				
				int totalfee = StringTool.GetInt(SysConfigAction.findSysconfig("预约安装测量", "测量价格"), 1);
				
				Ebean.getServer(GlobalDBControl.getDB())
				.createSqlUpdate("update sub_measure_orders set prepayid=:prepayid,money=:money where idd=:idd")
				.setParameter("prepayid", outTradeNo)
				.setParameter("money", totalfee+"")
				.setParameter("idd", oid)
				.execute();
				
				
				Calendar cal = Calendar.getInstance();
				cal.setTime(subOrder.getAddtime());
				cal.add(Calendar.MINUTE, 2);
				Map<String, String> params = WxPayApiConfigKit.getWxPayApiConfig()
						.setAttach("103")
						.setBody("上门测量")
						.setDetail("上门测量")
						.setOpenId(openid)
						.setSpbillCreateIp(ip)
						.setTotalFee(totalfee+"")
						.setTradeType(TradeType.JSAPI)
						.setNotifyUrl(getDomain()+"/v1/notify_buySubscribe_Wx")
						.setOutTradeNo(outTradeNo)
						.setTimeExpire(StringUtil.getDateTimeString(cal.getTime(), "yyyyMMddHHmmss"))
						.build();
				
				String xmlResult = WxPayApi.pushOrder(false,params);
				
				StringBuffer sb = new StringBuffer();
				int i = 1;
				for (Map.Entry<String, String> entry : params.entrySet()) {
					if (entry.getValue() != null) {
						sb.append(entry.getKey() + " = " + entry.getValue());
						if (i != params.size())
							sb.append(",");
					}
					i++;
				}

				LogUtil.writeLog("buySubMeasure_Wx_Web--" + "\t" + sb.toString() + "\t"
						+ xmlResult, "weixinPay");
				
				Map<String, String> result = PaymentKit.xmlToMap(xmlResult);
				
				String return_code = result.get("return_code");
				String return_msg = result.get("return_msg");
				if (!PaymentKit.codeIsOK(return_code)) {
					reJson.setCode(1);
					reJson.setMessage(return_msg);
					String reContent = JsonUtil.parseObj(reJson);
					appLogger.debug(reContent);
					return ok(reContent);
				}
				String result_code = result.get("result_code");
				if (!PaymentKit.codeIsOK(result_code)) {
					reJson.setCode(2);
					reJson.setMessage(return_msg);
					String reContent = JsonUtil.parseObj(reJson);
					appLogger.debug(reContent);
					return ok(reContent);
				}
				// 以下字段在return_code 和result_code都为SUCCESS的时候有返回
				String prepayId = result.get("prepay_id");
				
				//生成支付订单
				SmartPayOrder smartPayOrder = new SmartPayOrder();
				smartPayOrder.setAddtime(new Date());
				smartPayOrder.setChannel("gzh");
				smartPayOrder.setOutTradeNo(outTradeNo);
				smartPayOrder.setPrepayId(prepayId);
				smartPayOrder.setRemark("上门测量");
				smartPayOrder.setStatus(SmartPayOrder.STATUS_TODO);
				smartPayOrder.setTotalFee(totalfee);
				smartPayOrder.setType(SmartPayOrder.TYPE_WX);
				smartPayOrder.setUserid(user.getIdd());
				smartPayOrder.setIp(ip);
				Ebean.getServer(GlobalDBControl.getDB()).save(smartPayOrder);
				
				Map<String, String> packageParams = PaymentKit.prepayIdCreateSign(prepayId);
				reJson.setSuccess();
				reJson.setData(packageParams);
			}
		}
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}
	
	@With(WxGzhWapPayProperties.class)
	public static Result notify_buySubscribe_Wx() {
		String xmlMsg = HttpKit.readData(request());
		LogUtil.writeLog("notify_buySubscribe_Wx--" + "\t" + xmlMsg, "weixinPay");
		Map<String, String> params = PaymentKit.xmlToMap(xmlMsg);
		// 签名验证
		if (PaymentKit.verifyNotify(params, WxPayApiConfigKit
				.getWxPayApiConfig().getPaternerKey())) {
			String result_code = params.get("result_code");
			if (PaymentKit.codeIsOK(result_code)) {
				
				String transaction_id = params.get("transaction_id");
				String out_trade_no = params.get("out_trade_no");
				String total_fee = params.get("total_fee");
				String attach = params.get("attach");
				SmartPayOrder smartPayOrder = Ebean
						.getServer(GlobalDBControl.getDB())
						.find(SmartPayOrder.class).where()
						.eq("out_trade_no", out_trade_no).findUnique();

				if (smartPayOrder != null) {
					if (smartPayOrder.getStatus() == SmartPayOrder.STATUS_TODO) {
						if (new BigDecimal(total_fee).intValue() == smartPayOrder
								.getTotalFee()) {
							String sql = "update smart_payorder set transaction_id=:transaction_id,status=:status"
									+ ",notifytime=sysdate() where idd=:idd";
							Ebean.getServer(GlobalDBControl.getDB())
									.createSqlUpdate(sql)
									.setParameter("transaction_id",
											transaction_id)
									.setParameter("status",
											SmartPayOrder.STATUS_SUCCESS)
									.setParameter("idd", smartPayOrder.getIdd())
									.execute();
							//修改预约订单状态
							if("102".equals(attach)){
								Ebean.getServer(GlobalDBControl.getDB())
									.createSqlUpdate("update sub_install_orders set status=2,updatetime=sysdate() where prepayid=:prepayid")
									.setParameter("prepayid", out_trade_no)
									.execute();
								SubInstallOrders subInstallOrder = Ebean.getServer(GlobalDBControl.getReadDB()).find(SubInstallOrders.class).where().eq("prepayid", out_trade_no).findUnique();
								Ebean.getServer(GlobalDBControl.getDB())
									.createSqlUpdate("update sub_orders set isinstall=0 where idd=:idd")
									.setParameter("idd", subInstallOrder.getOid())
									.execute();
								SubMsgService.addMsg(subInstallOrder.getPhone(), "您已预约成功，我们会火速为您安排发货、安装，如有问题可以致电408881781", SubMsgService.ACT_INSTALL, subInstallOrder.getIdd());
								WxGzhService.sendGzhMessage(subInstallOrder.getPhone(), "尊敬的用户，您预约成功", "预约安装", "待发货", StringUtil.getDateTimeStr(new Date()), "我们会火速为您安排发货、安装，如有问题可以致电408881781");
							}else if("103".equals(attach)){
								Ebean.getServer(GlobalDBControl.getDB())
								.createSqlUpdate("update sub_measure_orders set status=3,updatetime=sysdate() where prepayid=:prepayid")
								.setParameter("prepayid", out_trade_no)
								.execute();
								SubMeasureOrders subMeasureOrder = Ebean.getServer(GlobalDBControl.getReadDB()).find(SubMeasureOrders.class).where().eq("prepayid", out_trade_no).findUnique();
								Ebean.getServer(GlobalDBControl.getDB())
								.createSqlUpdate("update sub_orders set ismeasure=0 where idd=:idd")
								.setParameter("idd", subMeasureOrder.getOid())
								.execute();
								SubMsgService.addMsg(subMeasureOrder.getPhone(), "您已预约成功，我们会火速为您安排安装师傅上门测量，如有问题可以致电408881781", SubMsgService.ACT_MEASURE, subMeasureOrder.getIdd());
								WxGzhService.sendGzhMessage(subMeasureOrder.getPhone(), "尊敬的用户，您预约成功", "预约测量", "待派单", StringUtil.getDateTimeStr(new Date()), "我们会火速为您安排安装师傅上门测量，如有问题可以致电408881781");
							}
							
							Map<String, String> xml = new HashMap<String, String>();
							xml.put("return_code", "SUCCESS");
							xml.put("return_msg", "OK");
							return ok(PaymentKit.toXml(xml));
						}
					}
				}
				
				
			}
		}
		return ok("");
	}
}
