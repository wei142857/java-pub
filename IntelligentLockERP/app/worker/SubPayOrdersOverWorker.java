package worker;

import java.util.Calendar;

import com.avaje.ebean.Ebean;

import play.Logger;
import util.GlobalDBControl;
import util.StringUtil;

public class SubPayOrdersOverWorker extends BaseWorker {
	public SubPayOrdersOverWorker() {
		Logger.info("SubPayOrdersOverWorker start...");
		this.start();
	}

	static int TIME = 3;

	public void run() {
		while (true) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MINUTE, -TIME);
			Ebean.getServer(GlobalDBControl.getReadDB())
					.createSqlUpdate(
							"delete from sub_measure_orders where status=1 and addtime<:overtime")
					.setParameter("overtime", cal.getTime()).execute();
			
			Ebean.getServer(GlobalDBControl.getReadDB())
			.createSqlUpdate(
					"delete from sub_install_orders where status=1 and addtime<:overtime")
			.setParameter("overtime", cal.getTime()).execute();
			
			StringUtil.sleep(1000*60);
		}
	}
}
