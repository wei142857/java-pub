package util.clientPush;

import java.util.HashMap;
import java.util.Map;

import play.Logger;
import play.libs.Json;
import util.LogUtil;

import com.xiaomi.xmpush.server.Message;
import com.xiaomi.xmpush.server.Result;
import com.xiaomi.xmpush.server.Sender;

/**
 * 小米推送
 * 
 * @author xuml
 *
 */
public class AndroidSmsPush {
	private static Logger.ALogger logger = Logger.of(AndroidSmsPush.class);
	private static String APP_SECRET_KEY = "dnlcUhcrhWtXpmZCEe67Yw==";
	private static String MY_PACKAGE_NAME = "com.jutao.sknhome";
	private static Sender sender;
	private static String logFile = "androidpush";
	private static String logFileerror = "androidpusherror";
	private static Integer retry = 3;

	public static void SmsPushWithUrl(String phone, String deviceToken,
			String title, String content, Integer mid, String url) {
		Map<String, String> params = new HashMap<String, String>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{
				put("url", url);
				put("mid", "" + mid);
			}
		};
		Message message = new Message.Builder().title(title)
				.description(content).payload(Json.toJson(params).toString())
				.restrictedPackageName(MY_PACKAGE_NAME).passThrough(0)
				.notifyType(1) // 使用默认提示音提示
				.notifyId(mid).build();
		Result result;
		try {
			result = getSender().send(message, deviceToken, retry);
			logger.info("phone: " + phone + "deviceToken: " + deviceToken
					+ "title: " + title + "content: " + content + "mid: " + mid
					+ "MessageId: " + result.getMessageId() + " ErrorCode: "
					+ result.getErrorCode().getValue() + " Reason: "
					+ result.getReason());
			LogUtil.writeLog("phone: " + phone + "deviceToken: " + deviceToken
					+ "title: " + title + "content: " + content + "mid: " + mid
					+ "url: " + url + "MessageId: " + result.getMessageId()
					+ " ErrorCode: " + result.getErrorCode().getValue()
					+ " Reason: " + result.getReason(), logFile);
		} catch (Exception e) {
			logger.error("phone: " + phone + "deviceToken: " + deviceToken
					+ "title: " + title + "content: " + content + "mid: " + mid
					+ "url: " + url, e);
			LogUtil.writeLog("phone: " + phone + "deviceToken: " + deviceToken
					+ "title: " + title + "content: " + content + "mid: " + mid
					+ "url: " + url + "error:" + e.toString(), logFileerror);
		}
	}

	public static void SmsPush(String phone, String deviceToken, String title,
			String content, Integer mid) {
		Map<String, String> params = new HashMap<String, String>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			{
				put("mid", "" + mid);
			}
		};
		Message message = new Message.Builder().title(title)
				.description(content).payload(Json.toJson(params).toString())
				.restrictedPackageName(MY_PACKAGE_NAME).passThrough(0)
				.notifyType(1) // 使用默认提示音提示
				.notifyId(mid).build();
		Result result;
		try {
			result = getSender().send(message, deviceToken, retry);
			logger.info("phone: " + phone + "deviceToken: " + deviceToken
					+ "title: " + title + "content: " + content + "mid: " + mid
					+ "MessageId: " + result.getMessageId() + " ErrorCode: "
					+ result.getErrorCode().getValue() + " Reason: "
					+ result.getReason());
			LogUtil.writeLog(
					"phone: " + phone + "deviceToken: " + deviceToken
							+ "title: " + title + "content: " + content
							+ "mid: " + mid + "MessageId: "
							+ result.getMessageId() + " ErrorCode: "
							+ result.getErrorCode().getValue() + " Reason: "
							+ result.getReason(), logFile);
		} catch (Exception e) {
			logger.error(
					"phone: " + phone + "deviceToken: " + deviceToken
							+ "title: " + title + "content: " + content
							+ "mid: " + mid, e);
			LogUtil.writeLog("phone: " + phone + "deviceToken: " + deviceToken
					+ "title: " + title + "content: " + content + "mid: " + mid
					+ "error:" + e.toString(), logFileerror);
		}
	}

	private static Sender getSender() {
		if (sender == null) {
			sender = new Sender(APP_SECRET_KEY);
		}
		return sender;
	}

	public static void main(String[] args) {
		String phone = "";
		String deviceToken = "VfN6a+Vclt6Sd0ZrVyBR30XQUzjkP2k4eexNO9zS7XeDBw6L4slV++KoFUu+MZu";
		String title = "测试";
		String content = "test push";
		Integer mid = 10;
		SmsPush(phone, deviceToken, title, content, mid);
	}
}
