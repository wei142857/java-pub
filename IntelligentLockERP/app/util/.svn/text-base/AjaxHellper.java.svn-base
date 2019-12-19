package util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import play.mvc.Http.Request;
import util.classEntity.StringTool;

public class AjaxHellper {
	
	//判断是否是IOS的方法
	public static boolean isIOS( Request req )
	{
		String[] keys = req.headers().get("User-Agent");
		if(keys!=null){
			String ua= keys[0];
			return ua.toUpperCase().indexOf("IPHONE") != -1;
		}
		return false;
	}
	/*
	 * JavaScript - kissy框架的ajax数据请求过来
	 * Request里面得到参数是FormUrlEncoded方式。
	 * 方法获取Request的参数
	 */
	public static String getHttpParamOfFormUrlEncoded(Request req,String key)
	{
		Map<String,String[]> all =req.body().asFormUrlEncoded();
		if( all==null )
			return null;
		String[] values = all.get(key);
		if(values!=null&&values.length>0){
			return values[0];
		}
		return null;
	}
	/*
	 * POST请求获取参数Map
	 * Request里面得到参数是FormUrlEncoded方式。
	 */
	public static Map<String,String> getQueryParam(Request req)
	{
		Map<String,String> params = new HashMap<String,String>();
		Map<String,String[]> all =req.body().asFormUrlEncoded();
		if( all==null )
			return null;
		for(Iterator it = all.entrySet().iterator();it.hasNext();){
			Map.Entry map = (Map.Entry) it.next();
			String key = (String) map.getKey();
			String[] value = (String[]) map.getValue();
			if (value != null && value.length > 0) {
				params.put(key, value[0]);
			}
		}
		return params;
	}
	
	/*
	 * JavaScript - kissy框架的ajax数据请求过来
	 * Request里面得到参数是 :url?param1=value1&param2=value2...
	 * 方法获取Request的参数
	 */
	public static String getHttpParam(Request req,String key)
	{
		if( req==null )
			return "";
		Map<String,String[]> all = req.queryString();
		if( all==null )
			return null;
		String[] values = all.get(key);
		if(values!=null&&values.length>0){
			return values[0];
		}
		return null;
	}
	
	public static String getHeader(Request request,String key)
	{
		if( request==null )
			return "";
		String ret = request.getHeader(key);
		if(!StringTool.isNull(ret))
			return ret;
		String[] keys = request.headers().get(key);
		if(keys!=null)
			return keys[0];
		return null;
	}
	
	/**
	 * 获取客户端IP
	 * 
	 * @param value
	 * @return
	 */
	public static String getIpAddr(Request request) {
		if( request==null )
			return "";
		
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");//nginx配置的获取真实IP
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.remoteAddress();
        }
        return ip;
    }
	
	public static String getUA( Request req )
	{
		if( req==null )
			return "";
		String[] keys = req.headers().get("User-Agent");
		if(keys!=null){
			String ua= keys[0];
			return ua;
		}
		return "";
	}
	
	//获取所有http参数
	public static String getAllHttpParams( Request req  )
	{
		if( req==null )
			return "";
		Map<String,String[]> all =req.body().asFormUrlEncoded();
		if( all==null )
			return "";
		StringBuffer sb =new StringBuffer();
		for(Iterator it = all.entrySet().iterator();it.hasNext();){
			Map.Entry map = (Map.Entry) it.next();
			String key = (String) map.getKey();
			String[] value = (String[]) map.getValue();
			if (value != null && value.length > 0) {
				sb.append(key + " : " + value[0] +"\r\n");
			}
		}
			
		return sb.toString();
	}
}
