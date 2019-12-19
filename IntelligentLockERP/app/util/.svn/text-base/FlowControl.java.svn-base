package util;

import java.util.Date;

//流量控制类，用redis检查
public class FlowControl {

	// base function
	public static synchronized boolean checkActInTimeRedius(String key,
			int count, int timeout) {
		Integer smD = util.jedis.RedisUtil.getInstance().getEntityStr(key,
				Integer.class, 3);
		if (smD == null) {
			smD = 0;
			util.jedis.RedisUtil.getInstance().setEntityStr(key, smD, timeout,
					3);
		}
		smD++;
		if (smD > count)
			return true;
		util.jedis.RedisUtil.getInstance().IncrNum(key, 3);
		return false;
	}

	public static boolean checkDayAct(String act, int count) {
		String key = "_DayChk_" + act + util.StringUtil.getDateStr(new Date());
		return checkActInTimeRedius(key, count, 3600 * 24);
	}

	public static boolean checkHourAct(String act, int count) {
		String key = "_HourChk_" + act
				+ util.StringUtil.getDateHourStr(new Date());
		return checkActInTimeRedius(key, count, 3600);
	}
}
