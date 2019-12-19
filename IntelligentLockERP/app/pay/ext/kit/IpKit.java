/**
 * Copyright (c) 2011-2014, James Zhan 詹波 (jfinal@126.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package pay.ext.kit;


public class IpKit {
	
	public static String getRealIp(play.mvc.Http.Request request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.remoteAddress();
		}
		return ip;
	}
	
	public static String getRealIpV2(play.mvc.Http.Request request) {
		String accessIP = request.getHeader("x-forwarded-for");
        if (null == accessIP)
            return request.remoteAddress();
        return accessIP;
	}
}
