package com.cloopen.rest.demo;

import java.util.HashMap;
import java.util.Set;

import com.cloopen.rest.sdk.CCPRestSDK;
import com.cloopen.rest.sdk.CCPRestSDK.BodyType;

public class SDKTestLandingCall {

	public static boolean sendAlarm(String phone)
	{
		String key  = "sendAlarmVoice:"+phone;
		if(util.jedis.RedisUtil.getInstance().getEntity(key, String.class)!=null)
			return true;
		
		util.jedis.RedisUtil.getInstance().setEntity(key, "1",60*2 );
		
		HashMap<String, Object> result = null;
		CCPRestSDK restAPI = new CCPRestSDK();
		restAPI.init("app.cloopen.com", "8883");// 初始化服务器地址和端口，格式如下，服务器地址不需要写https://
		restAPI.setAccount("aaf98f894c7d3aca014c7e4d3d890167", 
				"76b3b276d8c44e6f96896fbf20b136cb");// 初始化主帐号名称和主帐号令牌
		restAPI.setAppId("8aaf07086dae7e1f016dafb9dfa4022f");// 初始化应用ID
		//type=1，则播放默认语音文件,0是自定义语音文件 
		result = restAPI.landingCall(phone, 
				"aa1.wav", 
				"", //"文本内容"
				"", //"来电显示的号码"
				"2", //"循环播放次数"
				"", //"外呼通知状态通知回调地址",
				"", //"第三方私有数据"
				"", //"文本转语音后的发音速度"
				"", //"文本转语音后的音量大小"
				"", //"文本转语音后的音调"
				"", //"文本转语音后的背景音编号"
				""  //"是否同时播放文本和语音文件"
				);
		
		//result = restAPI.landingCall("被叫号码", "语音文件名称", 
		//"文本内容", "来电显示的号码", "循环播放次数", "外呼通知状态通知回调地址", "第三方私有数据", 
		//"文本转语音后的发音速度", "文本转语音后的音量大小", "文本转语音后的音调", "文本转语音后的背景音编号", "是否同时播放文本和语音文件");

		System.out.println("SDKTestLandingCall result=" + result);
		util.LogUtil.writeLog( phone  + "\t" + result , "voice");
		if("000000".equals(result.get("statusCode"))){
			//正常返回输出data包体信息（map）
			return true;
		}else{
			//异常返回输出错误码和错误信息
			return false;
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		HashMap<String, Object> result = null;

		CCPRestSDK restAPI = new CCPRestSDK();
		restAPI.init("app.cloopen.com", "8883");// 初始化服务器地址和端口，格式如下，服务器地址不需要写https://
		restAPI.setAccount("aaf98f894c7d3aca014c7e4d3d890167", 
				"76b3b276d8c44e6f96896fbf20b136cb");// 初始化主帐号名称和主帐号令牌
		restAPI.setAppId("8aaf07086dae7e1f016dafb9dfa4022f");// 初始化应用ID
		//type=1，则播放默认语音文件,0是自定义语音文件 
		result = restAPI.landingCall("13520430223", 
				"aa1.wav", 
				"", //"文本内容"
				"", //"来电显示的号码"
				"2", //"循环播放次数"
				"", //"外呼通知状态通知回调地址",
				"", //"第三方私有数据"
				"", //"文本转语音后的发音速度"
				"", //"文本转语音后的音量大小"
				"", //"文本转语音后的音调"
				"", //"文本转语音后的背景音编号"
				""  //"是否同时播放文本和语音文件"
				);
		
		//result = restAPI.landingCall("被叫号码", "语音文件名称", 
		//"文本内容", "来电显示的号码", "循环播放次数", "外呼通知状态通知回调地址", "第三方私有数据", 
		//"文本转语音后的发音速度", "文本转语音后的音量大小", "文本转语音后的音调", "文本转语音后的背景音编号", "是否同时播放文本和语音文件");

		System.out.println("SDKTestLandingCall result=" + result);
		
		if("000000".equals(result.get("statusCode"))){
			//正常返回输出data包体信息（map）
			HashMap<String,Object> data = (HashMap<String, Object>) result.get("data");
			Set<String> keySet = data.keySet();
			for(String key:keySet){
				Object object = data.get(key);
				System.out.println(key +" = "+object);
			}
		}else{
			//异常返回输出错误码和错误信息
			System.out.println("错误码=" + result.get("statusCode") +" 错误信息= "+result.get("statusMsg"));
		}
	}

}
