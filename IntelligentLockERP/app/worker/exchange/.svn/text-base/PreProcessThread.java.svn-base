package worker.exchange;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import play.Logger;

import com.avaje.ebean.Ebean;

import app.service.ProductService;
import app.service.UserService;
import app.service.VipService;
import util.GlobalDBControl;
import util.LogUtil;
import util.StringUtil;
import util.jedis.RedisUtil;
import models.SmartAppUser;
import models.SmartBountyExchanges;
import models.SmartExchanges;
import models.SmartProduct;

/**
 * 充值前预处理
 * 
 * @author xuml
 *
 */
public class PreProcessThread extends Thread {
	public static String QUEUE_EXCHANGE_PREPROCESS = "QUEUE:EXCHANGE:PREPROCESS";

	public PreProcessThread() {
		Logger.info("!! PreProcessThread start ..");
		this.start();
	}

	public static void add(Integer userid, String account, String channel,
			Integer bvalue, Integer pid, String ip) {
		Carrier ec = new Carrier();
		ec.setAccount(account);
		ec.setChannel(channel);
		ec.setUserid(userid);
		ec.setBvalue(bvalue);
		ec.setPid(pid);
		ec.setIp(ip);
		RedisUtil.getInstance().setRedisMQ(QUEUE_EXCHANGE_PREPROCESS, ec);
	}

	public void run() {
		while(true){
			Carrier carrier = RedisUtil.getInstance().getRedisOneMQ(
					QUEUE_EXCHANGE_PREPROCESS, Carrier.class);
			if (carrier != null) {
				try {
					List<SmartBountyExchanges> bountyExchangesList = new ArrayList<SmartBountyExchanges>();
					SmartAppUser user = UserService.findUserById(carrier
							.getUserid());
					Integer amountBounty = VipService.findBountyByUserid(carrier
							.getUserid()); // 用户当前剩余的奖励金
					SmartProduct product = ProductService
							.selectProductByPid(carrier.getPid()); // 兑换产品所需要的奖励金
					// 判断该用户当前的奖励金余额是否>该产品所需奖励金数量
					if (amountBounty > 0 && amountBounty >= product.getBvalue()) {
						// 找出 离过期时间最近的奖励金 使用它兑换产品
						bountyExchangesList = ProductService.payBvalueByOvertime(
								carrier.getUserid(), product.getBvalue(),
								product.getPrice());
						if (bountyExchangesList != null) {
							Date now = new Date();
							util.LogUtil.writeLog("用户:" + carrier.getUserid()
									+ "使用奖励金兑换pid:" + carrier.getPid() + "产品成功!",
									"addVipValidityTimeLog");
							// 生成充值记录
							SmartExchanges exchange = new SmartExchanges();
							exchange.setAddtime(new Date());
							exchange.setArea(user.getProv());
							exchange.setBvalue(carrier.getBvalue());
							exchange.setIp(carrier.getIp());
							exchange.setAccount(carrier.getAccount());
							exchange.setPid(carrier.getPid());
							exchange.setUserid(user.getIdd());
							exchange.setChannel(carrier.getChannel());
							Ebean.getServer(GlobalDBControl.getDB()).save(exchange);
							LogUtil.writeLog(exchange.getIdd()+"\t"+exchange.getUserid()+"\t"+exchange.getAccount()+"\t"+exchange.getBvalue(), "preprocessthread");
							for (SmartBountyExchanges bountyExchange : bountyExchangesList) {
								bountyExchange.setAddtime(now);
								bountyExchange.setEid(exchange.getIdd());
							}
							Ebean.getServer(GlobalDBControl.getDB()).save(
									bountyExchangesList);
							OrderProcessThread.add(exchange);
						} else {
							util.LogUtil.writeLog("用户:" + carrier.getUserid()
									+ "使用奖励金兑换pid:" + carrier.getPid()
									+ "产品失败(兑换时出错)!", "addVipValidityTimeLog");
						}
					}

				} catch (Exception e) {
					LogUtil.writeLog(carrier.getUserid()+"\t"+carrier.getAccount()+"\t"+carrier.getBvalue()+"\t"+carrier.getChannel()+"\t"+carrier.getIp()+"\t"+e.toString(), "preprocessthread");
				}
			}
			StringUtil.sleep(100);
		}
		
	}
}
