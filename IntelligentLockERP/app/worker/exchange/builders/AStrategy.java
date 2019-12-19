package worker.exchange.builders;

import java.util.Date;
import java.util.Map;

import models.SmartExchanges;
import models.SmartInterface;
import util.GlobalSetting;
import util.LogUtil;
import util.classEntity.StringTool;
import worker.exchange.action.Monitor;
import worker.exchange.action.Notifier;
import worker.exchange.action.Order;
import Service.unicom.dto.IFResponse;
import app.service.ProductService;

import com.avaje.ebean.Ebean;

public abstract class AStrategy implements Order,Monitor,Notifier{
	
	public void handleMonitor(SmartExchanges smartExchange, SmartInterface si){
		handle(doQuery(smartExchange, si), smartExchange);
	}
	
	public void handleOrder(SmartExchanges smartExchange, SmartInterface si){
		handle(doOrder(smartExchange, si), smartExchange);
	}
	
	private void handle(IFResponse ifsp,SmartExchanges smartExchange){
		LogUtil.writeLog("handel"+"\t"+smartExchange.getIdd()+"\t"+ifsp.toString(), "astrategy");
		if(StringTool.isNull(ifsp.returninfo)){
			return;
		}
		if ( ifsp.success && ifsp.state==1 ) {
			setFlowOrderResult(smartExchange, true, "","",	
					ifsp.orderId,true);
		}else if ( ( ifsp.success && ifsp.state == 2 )) {
			if(System.currentTimeMillis()-smartExchange.getAddtime().getTime()>1000*60*60*24){
				smartExchange.setReturninfo(ifsp.returninfo);
				setFlowOrderResult(smartExchange, false, 
						"充值失败","充值失败",
						ifsp.orderId,true);
			}else{
				setFlowOrderResult(smartExchange, true, "","",ifsp.orderId,false);
			}
			
		}else {//得到结果失败
			smartExchange.setReturninfo(ifsp.returninfo);
			setFlowOrderResult(smartExchange, false, 
					ifsp.means,ifsp.rmeans,ifsp.orderId,true);
		}	
	}
	
	private void setFlowOrderResult( SmartExchanges smartExchange, boolean ok, String reason,
			String real,String orderid,boolean finished ) {
		if(ok){
			smartExchange.setRetid(orderid);
			smartExchange.setActtime( new Date() );
			if(finished){
				smartExchange.setStatus(1);
			}else{
				smartExchange.setStatus(3);
			}
			Ebean.getServer(GlobalSetting.defaultDB).save(smartExchange);
			
		}else{
			if( orderid != null )
				smartExchange.setRetid(orderid);
			smartExchange.setStatus(2);
			smartExchange.setMeans(reason);
			smartExchange.setRealmeans(real);
			Ebean.getServer(GlobalSetting.defaultDB).save(smartExchange);
			//处理退款
			ProductService.refundMoneyByPayFail(smartExchange);
		}
		if( finished )
			sendMsg(smartExchange, ok, reason);
	}
	
	public void sendMsg(SmartExchanges smartExchange,boolean ok,String reason){
		
	}

	@Override
	public IFResponse callback(SmartExchanges record,
			SmartInterface si, Map<String, Object> ext) {
		return IFResponse.mkSuccessResp();
	}
	
	@Override
	public IFResponse doOrder(SmartExchanges smartExchange, SmartInterface si) {
		return this.doOrder(smartExchange, si, null);
	}
	
	@Override
	public IFResponse doQuery(SmartExchanges smartExchange, SmartInterface si) {
		return this.doQuery(smartExchange, si, null);
	}
}
