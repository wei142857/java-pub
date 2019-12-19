package worker.exchange;

import java.util.Calendar;
import java.util.List;

import models.SmartExchanges;
import models.SmartInterface;
import models.SmartProduct;
import play.Logger;
import util.GlobalDBControl;
import util.GlobalSetting;
import util.StringUtil;
import worker.exchange.builders.AStrategy;

import com.avaje.ebean.Ebean;

public class MonitorProcessThread extends Thread {
	public MonitorProcessThread() {
		Logger.info("!! MonitorProcessThread start ..");
		this.start();
	}

	public void run() {
		while (true) {
			List<SmartExchanges> smartExchanges = findSmartExchanges();
			for (SmartExchanges smartExchange : smartExchanges) {

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

				if (stratege != null) {
					stratege.handleMonitor(smartExchange, si);
				}
				StringUtil.sleep(100);
			}
			StringUtil.sleep(1000 * 40);
		}
	}

	private List<SmartExchanges> findSmartExchanges() {
		// 查询： 时间 > 3天之前的 ，且20秒前完成的
		java.util.Calendar tm1 = Calendar.getInstance();
		tm1.add(Calendar.HOUR, -(24 * 3 + 3));
		java.util.Calendar tm2 = Calendar.getInstance();
		tm2.add(Calendar.SECOND, -20);

		return Ebean.getServer(GlobalSetting.defaultDB)
				.find(SmartExchanges.class).where().eq("status", 3)
				// 状态为0001的
				.gt("acttime", tm1.getTime()).lt("acttime", tm2.getTime())
				.setMaxRows(15000).findList();
	}
}
