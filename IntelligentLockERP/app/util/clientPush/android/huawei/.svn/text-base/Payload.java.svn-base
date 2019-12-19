package util.clientPush.android.huawei;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.json.JsonUtil;

public class Payload {
	Payload(){
		root = new HashMap<String,Object>();
		message = new HashMap<String,Object>();
		android = new HashMap<String,Object>();
		notification = new HashMap<String,Object>();
		click_action = new HashMap<String,Object>();
		click_action.put("type", 3);
		notification.put("style", 0);//默认样式
		notification.put("auto_clear", 86400000);
		notification.put("click_action", click_action);
		android.put("notification", notification);
		android.put("collapse_key", -1);//本应用的离线消息待用户上线后全部发送给用户
		android.put("ttl", "129600s");//系统对离线消息存储用户指定的时间,此处设置了最大15天
		message.put("token", tokens);
		message.put("android", android);
		root.put("message", message);
	}
	
	public static Payload custom(){
		return new Payload();
	}
	
	public Payload title(String title){
		notification.put("title", title);
		return this;
	}
	
	public Payload notifyId(int notifyId){
		notification.put("notify_id", notifyId);
		return this;
	}
	
	public Payload body(String body){
		notification.put("body", body);
		return this;
	}
	
	public Payload url(String url){
		click_action.put("intent", url);
		if(url.startsWith("http"))
			click_action.put("type", 2);
		else
			click_action.put("type", 1);
		return this;
	}
	
	private Map<String,Object> root;
	private Map<String,Object> message;
	private Map<String,Object> notification;
	private Map<String,Object> android;
	private Map<String,Object> click_action;
	private List<String> tokens = new ArrayList<String>();
	
	public Payload token(String token){
		tokens.add(token);
		return this;
	}
	
	public String build(){
		return JsonUtil.parseObj(root);
	}
	
}
