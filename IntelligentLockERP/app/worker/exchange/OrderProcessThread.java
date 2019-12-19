package worker.exchange;

import play.Logger;
import models.SmartExchanges;
import models.SmartInterface;
import models.SmartProduct;
import util.GlobalDBControl;
import util.LogUtil;
import util.StringUtil;
import util.jedis.RedisUtil;
import worker.exchange.builders.AStrategy;

import com.avaje.ebean.Ebean;

public class OrderProcessThread extends Thread{
	static String QUEUE_EXCHANGE_PROCESS = "QUEUE:EXCHANGE:PROCESS";
	public OrderProcessThread(){
		Logger.info("!! OrderProcessThread start ..");
		this.start();
	}
	public static void add(SmartExchanges smartExchange){
		RedisUtil.getInstance().setRedisMQ(QUEUE_EXCHANGE_PROCESS, smartExchange);
	}
	public void run(){
		while(true){
			SmartExchanges smartExchange = RedisUtil.getInstance().getRedisOneMQ(QUEUE_EXCHANGE_PROCESS, SmartExchanges.class);
			if(smartExchange!=null){
				try{
					SmartProduct product = Ebean
							.getServer(GlobalDBControl.getReadDB())
							.find(SmartProduct.class).where()
							.eq("idd", smartExchange.getPid()).findUnique();

					SmartInterface si = Ebean
							.getServer(GlobalDBControl.getReadDB())
							.find(SmartInterface.class).where()
							.eq("idd", product.getIfid()).findUnique();

					AStrategy stratege = ScanStrategyFactory.getInstance()
							.getStrategy(si.getClassname());
					
					if(stratege!=null){
						try{
							stratege.handleOrder(smartExchange, si);
						}catch(Exception e){
							LogUtil.writeLog(smartExchange.getIdd()+"\t"+e.toString(), "orderprocessthread");
						}
					}else{
						LogUtil.writeLog(smartExchange.getIdd()+"\t"+"unfind", "orderprocessthread");
					}
				}catch(Exception e){
					LogUtil.writeLog(smartExchange.getIdd()+"\t"+"unfind", "orderprocessthread");
				}
				StringUtil.sleep(100);
			}
		}
	}
	
}
