package worker.exchange.strategy;

import java.util.Map;

import models.SmartExchanges;
import models.SmartInterface;
import models.SmartProduct;
import util.GlobalDBControl;
import util.LogUtil;
import util.StringUtil;
import worker.exchange.builders.AStrategy;
import Service.unicom.FeeChargeQyService;
import Service.unicom.dto.IFResponse;

import com.avaje.ebean.Ebean;

public class FeeCharge extends AStrategy{

	@Override
	public IFResponse doOrder(SmartExchanges smartExchange,
			SmartInterface si, Map<String, Object> ext) {
		SmartProduct product = Ebean.getServer(GlobalDBControl.getReadDB())
				.find(SmartProduct.class).where().eq("idd", smartExchange.getPid()).findUnique();
		String orderid = StringUtil.makeOrder("lock"+StringUtil.getRandomCharAndNumr(5), smartExchange.getAccount());
		IFResponse ifsp = FeeChargeQyService.feeCharge(orderid, smartExchange.getAccount(), product.getPrice()*100+"", si.getUrl(), si.getAppkey(),si.getAppsecret());
		LogUtil.writeLog("doOrder"+"\t"+smartExchange.getIdd()+"\t"+ifsp.toString(), "feecharge");
		if(ifsp.code.equals("00000")){
			ifsp.success = true;
			ifsp.state = 2;
			ifsp.means = "充值受理！";
			return ifsp;
		}else{
			ifsp.success = false;
			ifsp.means = "系统繁忙";
			ifsp.rmeans = ifsp.msg;
		}
		return ifsp;
	}

	@Override
	public IFResponse doQuery(SmartExchanges smartExchange,
			SmartInterface si, Map<String, Object> ext) {
		IFResponse ifsp =FeeChargeQyService.queryCharge(smartExchange.getRetid(), si.getUrl(), si.getAppkey(), si.getAppsecret());
		LogUtil.writeLog("doQuery"+"\t"+smartExchange.getIdd()+"\t"+ifsp.toString(), "feecharge");
		if (ifsp.code.trim().equalsIgnoreCase("00000") && "2".equals(ifsp.status)) {
			ifsp.success = true;
			ifsp.state = 1;
			ifsp.means = "充值成功！";
		}else{
			ifsp.success = true;
			ifsp.state = 2;
			ifsp.means = "充值受理！";
			return ifsp;
		}
		return ifsp;
	}

}
