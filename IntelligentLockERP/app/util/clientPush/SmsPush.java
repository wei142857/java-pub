package util.clientPush;

import play.Logger;
import util.StringUtil;
import util.clientPush.android.huawei.HuaweiSmsPush;
import models.SmartAppUser;

public class SmsPush {
	public static void push(SmartAppUser user, String title, String content,
			Integer mid, String url) {
		try {
			if (user.getPushstate() == 1) {// 开启推送 默认开启推送
				if (isIos(user.getChannel())) {
					if (!StringUtil.isMsgLinkUrl(url))
						IosSmsPush.SmsPush(user.getPhone(), user.getApptoken(),
								content, mid);
					else
						IosSmsPush.SmsPushWithUrl(user.getPhone(),
								user.getApptoken(), content, mid, url);
				}
				if (isAndroid(user.getChannel())) {
					if (user.getPhonetype().toUpperCase().indexOf("HUAWEI") != -1) {
						if (!StringUtil.isMsgLinkUrl(url))
							HuaweiSmsPush.SmsPush(user.getPhone(),
									user.getApptoken(), title, content, mid);
						else
							HuaweiSmsPush.SmsPushWithUrl(user.getPhone(),
									user.getApptoken(), title, content, mid,
									url);
					} else if (user.getPhonetype().toUpperCase()
							.indexOf("XIAOMI") != -1) {
						if (!StringUtil.isMsgLinkUrl(url))
							AndroidSmsPush.SmsPush(user.getPhone(),
									user.getApptoken(), title, content, mid);
						else
							AndroidSmsPush.SmsPushWithUrl(user.getPhone(),
									user.getApptoken(), title, content, mid,
									url);
					}
				}
			}
		} catch (Exception e) {
			Logger.error("push error,title:" + title + ",content:" + content
					+ ",phone:" + user.getPhone());
		}

	}

	public static boolean isAndroid(String channel) {
		return !isIos(channel);
	}

	public static boolean isIos(String channel) {
		return "APPSTORE".equals(channel.toUpperCase());
	}
}
