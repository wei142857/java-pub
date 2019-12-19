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
import pay.alipay.AliPayApi;
import pay.alipay.AliPayApiConfigKit;
import pay.ext.kit.IpKit;
import pay.ext.kit.StrKit;
import pay.properties.AliPayProperties;
import pay.util.StringUtils;
import play.mvc.Result;
import play.mvc.With;
import util.GlobalDBControl;
import util.LogUtil;
import util.StringUtil;
import util.classEntity.StringTool;
import util.json.JsonUtil;
import app.dto.ReturnJson;
import app.service.PayService;
import app.service.UserService;
import app.service.VipService;
import app.util.AppUtil;

import com.alipay.api.AlipayApiException;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.avaje.ebean.Ebean;

import controllers.SysConfigAction;

@With(AliPayProperties.class)
public class AliPayAction extends BaseAction{
	public static Result buyVip_Ali(){
		if( !checkSign() )
			return makSignFailor();
		ReturnJson reJson = new ReturnJson();
		String token = request().getQueryString("token");
		String appid = request().getQueryString("appid");
		if(StringUtil.isNull(token)){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}	
		
		SmartAppUser user = UserService.findUserByToken(token);
		if(user==null){
			reJson.setTokenTimeOut();
		}else{
			String ip = IpKit.getRealIp(request());
			if (StrKit.isBlank(ip)) {
				ip = "127.0.0.1";
			}
			String vipPrice_Config = SysConfigAction.findSysconfig("智能锁", "会员价格");
			if(StringTool.isNull(vipPrice_Config))
				vipPrice_Config = "365.00";
			int vipPrice = new BigDecimal(vipPrice_Config).multiply(new BigDecimal(100)).intValue();
			String outTradeNo = StringUtils.getOutTradeNo();
			AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
			model.setBody("智能会员");
			model.setSubject("智能会员");
			model.setOutTradeNo(outTradeNo);
			model.setTotalAmount(vipPrice_Config);
			model.setProductCode("QUICK_MSECURITY_PAY");
			//代表会员年数
			model.setPassbackParams("1");
			String result="";
			try {
				result = AliPayApi.appPayToResponse(model, getDomain() + "/v1/notify_buyVip_Ali").getBody();
				//生成订单
				SmartPayOrder smartPayOrder = new SmartPayOrder();
				smartPayOrder.setAddtime(new Date());
				smartPayOrder.setChannel(appid);
				smartPayOrder.setOutTradeNo(outTradeNo);
				smartPayOrder.setRemark("一年智能会员");
				smartPayOrder.setStatus(SmartPayOrder.STATUS_TODO);
				smartPayOrder.setTotalFee(vipPrice);
				smartPayOrder.setType(SmartPayOrder.TYPE_ALI);
				smartPayOrder.setUserid(user.getIdd());
				smartPayOrder.setIp(ip);
				Ebean.getServer(GlobalDBControl.getDB()).save(smartPayOrder);
			} catch (AlipayApiException e) {
				LogUtil.writeLog("buyVip_Ali--"+"\t"+model.getOutTradeNo()+"\t"+model.getSubject()+"\t"+model.getTotalAmount()+"\t"+e.toString(), "aliPay");
			}
			LogUtil.writeLog("buyVip_Ali--"+"\t"+model.getOutTradeNo()+"\t"+model.getSubject()+"\t"+model.getTotalAmount()+"\t"+result, "aliPay");
			Map<String,String> data = new HashMap<String,String>();
			data.put("encryptorder", result);
			reJson.setSuccess();
			reJson.setData(data);
		}
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);	
	}
	
	public static Result notify_buyVip_Ali(){
		Map<String, String> params = AliPayApi.toMap(request());
		StringBuffer sb = new StringBuffer();
		int i = 1;
		for (Map.Entry<String, String> entry : params.entrySet()) {
			sb.append(entry.getKey() + " = " + entry.getValue());
			if(i!=params.size())
				sb.append(",");
			i++;
		}
		try{
			LogUtil.writeLog("notify_buyVip_Ali--"+"\t"+sb.toString(), "aliPay");
			if(AlipaySignature.rsaCheckV1(params, AliPayApiConfigKit.getAliPayApiConfig().getAlipayPublicKey(), AliPayApiConfigKit.getAliPayApiConfig().getCharset(),
					AliPayApiConfigKit.getAliPayApiConfig().getSignType())){
				String trade_status = params.get("trade_status");
				if(AliPayApi.tradeStatusIsOk(trade_status)){
					String out_trade_no  = params.get("out_trade_no");
					String trade_no    = params.get("trade_no");
					String total_amount    = params.get("total_amount");
					
					SmartPayOrder smartPayOrder = Ebean.getServer(GlobalDBControl.getDB()).find(SmartPayOrder.class)
							.where().eq("out_trade_no", out_trade_no).findUnique();
					
					if(smartPayOrder!=null){
						if(smartPayOrder.getStatus()==SmartPayOrder.STATUS_TODO){
							//验证金额
							if(new BigDecimal(total_amount).multiply(new BigDecimal(100))
									.intValue()==smartPayOrder.getTotalFee()){
								
								String sql = "update smart_payorder set transaction_id=:transaction_id,status=:status"
										+ ",notifytime=sysdate() where idd=:idd";
								Ebean.getServer(GlobalDBControl.getDB()).createSqlUpdate(sql)
								.setParameter("transaction_id", trade_no)
								.setParameter("status", SmartPayOrder.STATUS_SUCCESS)
								.setParameter("idd", smartPayOrder.getIdd()).execute();
								
								
								//支付成功充值会员
								VipService.addVipValidityTimeByRecharge(smartPayOrder.getUserid());
								
								return ok("success");
							}
						}
					}
				}
			}
		}catch(Exception e){
			LogUtil.writeLog("notify_buyVip_Ali--"+"\t"+sb.toString()+"\t"+e.toString(), "aliPay");
		}
		return ok("");
	}
	
	public static Result buyEShopProduct_Ali(){
		if( !checkSign() )
			return makSignFailor();
		ReturnJson reJson = new ReturnJson();
		String token = request().getQueryString("token");
		String appid = request().getQueryString("appid");
		String pid = request().getQueryString("pid");
		String amount = request().getQueryString("amount");
		//判断参数amount是否合法
		Integer namount = StringTool.GetInt(amount, 0);
		if(namount<=0){
			reJson.setMessage("购买数量填写不正确,请重新选择");
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		String consigneeId = request().getQueryString("consigneeId");
		String bvalue = request().getQueryString("bvalue");
		String smsCode = request().getQueryString("smsCode");
		String source = session("source");
		
		Integer nconsigneeId = StringTool.GetInt(consigneeId, 0);
		Integer npid = StringTool.GetInt(pid, 0);
		Integer nbvalue = StringTool.GetInt(bvalue, 0);
		if(StringUtil.isNull(token)){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}	
		
		SmartAppUser user = UserService.findUserByToken(token);
		if(user==null){
			reJson.setTokenTimeOut();
		}else{
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
			String totalfee = 	new BigDecimal(order.getMoney()+"").setScale(2).toString();
			
			if(totalfee.compareTo("0")>0){
				Calendar cal = Calendar.getInstance();
				cal.setTime(order.getAddtime());
				cal.add(Calendar.MINUTE, 2);
				AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
				model.setBody(product.getTitle());
				model.setSubject(product.getTitle());
				model.setOutTradeNo(outTradeNo);
				model.setTimeExpire(StringUtil.getDateTimeString(cal.getTime(), "yyyy-MM-dd HH:mm"));
				model.setTotalAmount(totalfee);
				model.setProductCode("QUICK_MSECURITY_PAY");
				model.setPassbackParams("");
				String result="";
				try {
					result = AliPayApi.appPayToResponse(model, getDomain() + "/v1/notify_buyEShopProduct_Ali").getBody();
					//生成订单
					SmartPayOrder smartPayOrder = new SmartPayOrder();
					smartPayOrder.setAddtime(new Date());
					smartPayOrder.setChannel(appid);
					smartPayOrder.setOutTradeNo(outTradeNo);
					smartPayOrder.setRemark(product.getTitle());
					smartPayOrder.setStatus(SmartPayOrder.STATUS_TODO);
					smartPayOrder.setTotalFee(new BigDecimal(order.getMoney()).multiply(new BigDecimal(100)).intValue());
					smartPayOrder.setType(SmartPayOrder.TYPE_ALI);
					smartPayOrder.setUserid(user.getIdd());
					smartPayOrder.setIp(ip);
					Ebean.getServer(GlobalDBControl.getDB()).save(smartPayOrder);
				} catch (AlipayApiException e) {
					LogUtil.writeLog("buyEShopProduct_Ali--"+"\t"+model.getOutTradeNo()+"\t"+model.getSubject()+"\t"+model.getTotalAmount()+"\t"+e.toString(), "aliPay");
				}
				LogUtil.writeLog("buyEShopProduct_Ali--"+"\t"+model.getOutTradeNo()+"\t"+model.getSubject()+"\t"+model.getTotalAmount()+"\t"+result, "aliPay");
				Map<String,String> data = new HashMap<String,String>();
				data.put("encryptorder", result);
				data.put("orderid", order.getIdd()+"");
				reJson.setSuccess();
				reJson.setData(data);
			}else{
				reJson.setSuccess();
				Map<String,String> data = new HashMap<String,String>();
				data.put("orderid", order.getIdd()+"");
				reJson.setData(data);
			}
			
		}
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);	
	}
	
	public static Result buyEShopProduct_Ali_Wap(){
		
		String token = session("token");
		String pid = request().getQueryString("pid");
		String amount = request().getQueryString("amount");
		String bvalue = request().getQueryString("bvalue");
		String consigneeId = request().getQueryString("consigneeId");
		String smsCode = request().getQueryString("smsCode");
		String source = session("source");
		ReturnJson reJson = new ReturnJson();
		
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
			String totalfee = 	new BigDecimal(order.getMoney()+"").setScale(2).toString();
			if(totalfee.compareTo("0")>0){
				String notifyUrl = getDomain() + "/v1/notify_buyEShopProduct_Ali";
				String returnUrl = getDomain() + "/public/app/eshop/EShopPayResult.html?orderid="+order.getIdd();
				Calendar cal = Calendar.getInstance();
				cal.setTime(order.getAddtime());
				cal.add(Calendar.MINUTE, 2);
				AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
				model.setBody(product.getTitle());
				model.setSubject(product.getTitle());
				model.setTotalAmount(totalfee);
				model.setPassbackParams("");
				model.setOutTradeNo(outTradeNo);
				model.setTimeExpire(StringUtil.getDateTimeString(cal.getTime(), "yyyy-MM-dd HH:mm"));
				model.setProductCode("QUICK_WAP_PAY");

				try {
					
					String form = AliPayApi.wapPayStr(model,returnUrl ,notifyUrl);
					
					SmartPayOrder smartPayOrder = new SmartPayOrder();
					smartPayOrder.setAddtime(new Date());
					smartPayOrder.setChannel("h5");
					smartPayOrder.setOutTradeNo(outTradeNo);
					smartPayOrder.setRemark(product.getTitle());
					smartPayOrder.setStatus(SmartPayOrder.STATUS_TODO);
					smartPayOrder.setTotalFee(new BigDecimal(order.getMoney()+"").multiply(new BigDecimal(100)).intValue());
					smartPayOrder.setType(SmartPayOrder.TYPE_ALI);
					smartPayOrder.setUserid(user.getIdd());
					smartPayOrder.setIp(ip);
					Ebean.getServer(GlobalDBControl.getDB()).save(smartPayOrder);
					
					Map<String,String> data = new HashMap<String,String>();
					data.put("orderid", order.getIdd()+"");
					data.put("form", form);
					reJson.setSuccess();
					reJson.setData(data);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				reJson.setSuccess();
				Map<String,String> data = new HashMap<String,String>();
				data.put("orderid", order.getIdd()+"");
				reJson.setData(data);
			}
		}
		
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);	
	}
	
	public static Result notify_buyEShopProduct_Ali(){
		Map<String, String> params = AliPayApi.toMap(request());
		StringBuffer sb = new StringBuffer();
		int i = 1;
		for (Map.Entry<String, String> entry : params.entrySet()) {
			sb.append(entry.getKey() + " = " + entry.getValue());
			if(i!=params.size())
				sb.append(",");
			i++;
		}
		try{
			LogUtil.writeLog("notify_buyEShopProduct_Ali--"+"\t"+sb.toString(), "aliPay");
			if(AlipaySignature.rsaCheckV1(params, AliPayApiConfigKit.getAliPayApiConfig().getAlipayPublicKey(), AliPayApiConfigKit.getAliPayApiConfig().getCharset(),
					AliPayApiConfigKit.getAliPayApiConfig().getSignType())){
				String trade_status = params.get("trade_status");
				if(AliPayApi.tradeStatusIsOk(trade_status)){
					String out_trade_no  = params.get("out_trade_no");
					String trade_no    = params.get("trade_no");
					String total_amount    = params.get("total_amount");
					
					SmartPayOrder smartPayOrder = Ebean.getServer(GlobalDBControl.getDB()).find(SmartPayOrder.class)
							.where().eq("out_trade_no", out_trade_no).findUnique();
					
					if(smartPayOrder!=null){
						if(smartPayOrder.getStatus()==SmartPayOrder.STATUS_TODO){
							//验证金额
							if(new BigDecimal(total_amount).multiply(new BigDecimal(100))
									.intValue()==smartPayOrder.getTotalFee()){
								
								String sql = "update smart_payorder set transaction_id=:transaction_id,status=:status"
										+ ",notifytime=sysdate() where idd=:idd";
								Ebean.getServer(GlobalDBControl.getDB()).createSqlUpdate(sql)
								.setParameter("transaction_id", trade_no)
								.setParameter("status", SmartPayOrder.STATUS_SUCCESS)
								.setParameter("idd", smartPayOrder.getIdd()).execute();
								
								
								//修改商城订单为已支付
								PayService.updateOrderStatus_0(out_trade_no);
								
								return ok("success");
							}
						}
					}
				}
			}
		}catch(Exception e){
			LogUtil.writeLog("notify_buyVip_Ali--"+"\t"+sb.toString()+"\t"+e.toString(), "aliPay");
		}
		return ok("");
	}
	
}
