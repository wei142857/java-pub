package app.gzh;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import app.dto.ExchangeListDto;
import app.dto.ProductListDto;
import app.dto.ReturnJson;
import app.service.ProductService;
import app.service.SubWxUserService;
import app.service.UserService;
import app.util.AppUtil;
import models.SmartAppUser;
import models.SmartExchanges;
import models.SmartProduct;
import models.SubWxUser;
import play.mvc.Result;
import util.GlobalSetting;
import util.StringUtil;
import util.classEntity.StringTool;
import util.json.JsonUtil;

public class ProductAction extends BaseAction{
	/**
	 * 功能说明：查看话费列表
	 * 请求方式：GET
	 * 接口地址：getProductList
	 */
	public static Result getProductList(){
		ReturnJson reJson = new ReturnJson();
		ProductListDto data = new ProductListDto();
		String openid = session(GlobalSetting.GZH_OPENID);
		if (StringUtil.isNull(openid)) {
			reJson.setCode(2);
			reJson.setMessage("非法登录,请关注微信：SKN智能生活" + "\t" + "公众号进行登录");
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		String phone = request().getQueryString("phone");
		if(StringUtil.isNull(phone)){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		if(!StringUtil.isMobileNO(phone)){
			reJson.setParamsError();
			reJson.setMessage("您输入的号码无效，请重新输入。");
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		SubWxUser subWxUser = SubWxUserService.findIllegalNotifyWxUser(openid);
		
		SmartAppUser user = UserService.findUserByPhone(subWxUser.getPhone());
		
		if(user==null){
			reJson.setTokenTimeOut();
		}else{
			List<ProductListDto.Item> items = new ArrayList<ProductListDto.Item>();
			List<SmartProduct> productList = ProductService.selectProductList();
			if(productList!=null){
				List<SmartProduct> newProductList = new ArrayList<SmartProduct>();
				newProductList = ProductService.sortSmartProductByOrders(productList);
				for(SmartProduct product:newProductList){
					ProductListDto.Item item = new ProductListDto.Item();
					item.setPrice(product.getPrice()+"元");
					item.setBvalue(product.getBvalue()+"奖励金");
					item.setPid(""+product.getIdd());
					items.add(item);
				}
				data.setItems(items);
				reJson.setSuccess();
				reJson.setData(data);
			}
		}
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}
	
	/**
	 * 功能说明:话费充值(兑换产品/扣除奖励金)
	 * 请求方式:GET
	 * 接口地址:exchangeProduct
	 * @return
	 */
	public static Result exchangeProduct(){
		ReturnJson reJson = new ReturnJson();
		String openid = session(GlobalSetting.GZH_OPENID);
		if (StringUtil.isNull(openid)) {
			reJson.setCode(2);
			reJson.setMessage("非法登录,请关注微信：SKN智能生活" + "\t" + "公众号进行登录");
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		String phone = request().getQueryString("phone");
		String smscode = request().getQueryString("smscode");
		String pid = request().getQueryString("pid");
		Integer npid = StringTool.GetInt(pid, 0);
		if(StringUtil.isNull(phone)||StringUtil.isNull(smscode)||StringUtil.isNull(pid)){
			reJson.setParamsError();
			String reContent = JsonUtil.parseObj(reJson);
			appLogger.debug(reContent);
			return ok(reContent);
		}
		SubWxUser subWxUser = SubWxUserService.findIllegalNotifyWxUser(openid);
		
		SmartAppUser user = UserService.findUserByPhone(subWxUser.getPhone());
		if(user==null){
			reJson.setTokenTimeOut();
		}else{
			//验证手机号是否合法有效
			if(!StringUtil.isMobileNO(phone)){
				reJson.setParamsError();
				reJson.setMessage("您输入的号码无效，请重新输入。");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			//验证手机号码和手机验证码是否匹配/有效
			String rCode = AppUtil.getRandomCode(phone, AppUtil.TYPE_SMS_EXCHANGE);
			if(!smscode.equals(rCode)){
				reJson.setCode(104);
				reJson.setMessage("短信验证码错误");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			//验证pId是否有效
			SmartProduct product = ProductService.selectProductByPid(npid);
			if(product == null || product.getStatus()!=0){	//说明该pId无效/该产品已下线
				reJson.setParamsError();
				reJson.setMessage("您兑换的产品可能已下架，请选择其它商品兑换");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
			//判断用户奖励金余额>充值所需奖励金(满足条件则调用充值线程,扣除奖励金并生成兑换记录)
			Integer result = ProductService.payBountyExchangeProduct(user.getIdd(), phone, "APP", product.getPrice(), npid, user.getIp());
			if(result == 1){
				reJson.setSuccess();
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}else if(result == 2){
				reJson.setParamsError();
				reJson.setMessage("兑换失败,当前账户的奖励金余额不足");
				String reContent = JsonUtil.parseObj(reJson);
				appLogger.debug(reContent);
				return ok(reContent);
			}
		}
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}
	
	/**
	 * 功能说明:兑换记录
	 * 请求方式:GET
	 * 接口地址:getExchangeList
	 */
	public static Result getExchangeList(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
		SubWxUser subWxUser = SubWxUserService.findIllegalNotifyWxUser(openid);
		
		SmartAppUser appUser = UserService.findUserByPhone(subWxUser.getPhone());
		if(appUser==null){
			reJson.setTokenTimeOut();
		}else{
			ExchangeListDto data = new ExchangeListDto();
			List<ExchangeListDto.Item> items = new ArrayList<ExchangeListDto.Item>();
			List<SmartExchanges> exchangeList = ProductService.selectExchangesRecordByPage(appUser.getIdd(),npageno,npagesize);
			if(exchangeList != null && exchangeList.size()>0){
				for(SmartExchanges exchange:exchangeList){
					ExchangeListDto.Item item = new ExchangeListDto.Item();
					item.setTimestamp(sdf.format(exchange.getAddtime()));
					item.setBvalue(exchange.getBvalue()+"奖励金");
					if(exchange.getStatus()==1){
						item.setState("充值成功");
					}else if(exchange.getStatus()==2||exchange.getStatus()==null){
						item.setState("充值失败");
					}else{
						item.setState("充值中");
					}
					//根据pId去smart_product中查找该pId下对应的title值
					SmartProduct product = ProductService.selectProductByPid(exchange.getPid());
					if(product!=null){
						item.setTitle(product.getTitle());
					}else{
						item.setTitle("");
					}
					items.add(item);
				}
				//play.Logger.info("exchangeList"+exchangeList+","+data+","+npagesize);
				if(exchangeList.size()%npagesize==0){
					data.setTotalpages(exchangeList.size()/npagesize);
					data.setPageno(npageno);
				}else{
					data.setTotalpages((exchangeList.size()/npagesize)+1);
					data.setPageno(npageno);
				}
			}else{	//exchangeList==null
				data.setTotalpages(0);
				data.setPageno(1);
			}
			data.setItems(items);
			reJson.setSuccess();
			reJson.setData(data);
		}
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}
	
}
