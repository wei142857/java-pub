package util;



/*
 * 这个类用于获取一些全局的配置信息
 1. 静态的字符串定义：
 1）cookie/session 里面的字段名称
 2）cache的key的字段名称

 2. 获取application.conf 文件中的值: Configuration.root().get(?)

 */
public class GlobalSetting {

	// Session 字段
	public final static String Session_Validate_Image = "imgcode"; // 图形验证码
	
	public final static String Session_User_Id = "user_id"; // 用户登录名
	
	public final static String Session_fuser_obj="fuser_data";//用户操作的数据
	
	public final static String WX_OPENID = "wx_openid"; // 支付openid
	
	public final static String GZH_OPENID = "gzh_openid"; // 公众号openid
	
	public final static String TOKEN = "token"; // token
	
	// Cookie 字段
	public final static String Cookie_Timestamp = "user_last_acttime"; // 用户活动时间,用于超时的判断
	public final static String Cache_User = "user_"; // 用户登录后，在cache里面缓存用户信息

	// DB name
	public final static String defaultDB = "default"; // 写数据连接池
	
	public final static String readDB = "reader"; // 读数据连接池
	

	public static String getrandnum() {
		int randnum = (int) (Math.random() * 1000000);
		String strreturn = Integer.toString(randnum);
		if (strreturn.length() < 6) {
			int tmp = 6 - strreturn.length();
			while (tmp > 0) {
				strreturn = strreturn + "0";
				tmp = tmp - 1;
			}
		}
		return strreturn;
	}
	
}
