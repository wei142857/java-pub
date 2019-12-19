package pay.weixin.api.oauth;

import java.util.HashMap;
import java.util.Map;

public class CallBackUrl {
	@SuppressWarnings("serial")
    private static final Map<String, String> urls = new HashMap<String, String>(){{
    	put("101", "/public/app/install/appointInstall_login.html");//公众号登陆页面
    	put("102", "/public/app/install/appointInstall_inputInfo.html");//公众号预约安装页面
    	put("103", "/public/app/install/appointMeasure_submit.html");//公众号预约测量页面
    	put("104", "/public/app/install/appointInstall_scanDevice.html");//公众号预约测量页面
    	put("105", "/public/app/install/appointInstall_installOrder.html");//公众号预约安装订单页
    	put("106", "/public/app/install/appointMeasure_measureOrder.html");//公众号预约测量订单页
    	put("201", "/public/app/illegalnotify/bind.html");//非法通知绑定手机号页面
    	put("301", "/public/app/gzh/MyDevices.html");//公众号我的设备页面
    	put("302", "/public/app/gzh/user_homepage.html");//公众号会员中心页面
    	put("303", "/public/app/gzh/devicelogin.html");//公众号登陆页面
    }};
    
    /**
     * 通过返回码获取跳转链接
     * @param state 返回码
     * @return {String}
     */
    public static String get(String state) {
        return urls.get(state);
    }
}
