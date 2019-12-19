package app.action;

import java.util.Arrays;
import java.util.Map;

import play.Logger;
import play.Logger.ALogger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.Request;
import play.mvc.Result;
import util.AesUtil;
import util.AjaxHellper;
import util.IniLoader;
import util.MD5;
import util.classEntity.StringTool;
import util.json.JsonUtil;
import app.dto.ReturnJson;

public class BaseAction extends Controller{
	static String APPKEY 	= IniLoader.getStringByKey("app", "key", "m39dhaow8hautokeyh", "sys");
	static String APPSECRET = IniLoader.getStringByKey("app", "v10secret", "3820194637287940658", "sys");
	
	//aes加密
	static String SKEY 	= IniLoader.getStringByKey("aes", "skey", "6106c34e2786d852e79e6bf32ab8fa12", "sys");
	static String SIV = IniLoader.getStringByKey("aes", "siv", "00e3d201c5c2ac23f8154860272ba0a2", "sys");
		
	protected static ALogger appLogger = Logger.of(BaseAction.class);
	
	
	public static boolean checkSign()
	{
		appLogger.debug(AjaxHellper.getIpAddr(request())+"\t"+request().method() +"\t" +request().uri());
		String sign = request().getQueryString("sign");
		if( sign==null )
			return false;
		Map<String, String[]> params = request().queryString();
		StringBuilder sb = new StringBuilder();
		sb.append(APPKEY);
		
		Object[] keys = params.keySet().toArray();
		Arrays.sort(keys);
		String token = null,devid = null,type = "";
		for (Object key1 : keys) {
			String kk = (String)key1;
			if( kk.equals("sign") )
				continue;
			if(kk.equals("token"))
				token =request().getQueryString(kk);
			if(kk.equals("deviceid"))
				devid =request().getQueryString(kk);
			if(kk.equals("type"))
				type =request().getQueryString(kk);
			sb.append(key1).append(
					( request().getQueryString(kk) )
				);
		}
		sb.append( APPSECRET );
		
		String tcp         =    getHeader(request(), "tcp");
		if( tcp == null )
			tcp="";
		
		String headerkey   =	"for";
		String expiresTime =	getHeader(request(), "ExpiresTime");
		String checktcp    = 	MD5.mkMd5( expiresTime+sign+headerkey );
		
		String sign1 =  MD5.mkMd5(sb.toString());
		play.Logger.info("sign:\t"+sb.toString()+"\t"+sign1+"\t"+sign);
		play.Logger.info("tcp:\t"+(expiresTime+sign+headerkey)+"\t"+checktcp+"\t"+tcp);
		boolean tag= sign.trim().equalsIgnoreCase(sign1.trim())
				&& checktcp.trim().equalsIgnoreCase( tcp.trim() );
		
		if(!tag){
			appLogger.debug("sign============="+sign+","+sb.toString()+","+MD5.mkMd5(sb.toString()));
			appLogger.debug("tcp============="+tcp+","+checktcp+","+expiresTime+sign+headerkey);
		}
		
//		if( tag==true && IniLoader.getIntegerByKey("debug", "recact", 1, "sys") == 1 ){
//			BaseActionV4.recordAct( token,devid,type,request() );
//		}
		
		
		return tag;
	}
	
	public static boolean checkSign(Map<?,?> params){
		appLogger.debug(AjaxHellper.getIpAddr(request())+"\t"+request().method() +"\t" +request().path()+"\t"+params.toString());
		
		String sign = (String) params.get("sign");
		if( sign==null )
			return false;
		StringBuilder sb = new StringBuilder();
		sb.append(APPKEY);
		
		Object[] keys = params.keySet().toArray();
		Arrays.sort(keys);
		String token =null,devid=null,type="";
		for (Object key1 : keys) {
			String kk = (String)key1;
			if( kk.equals("sign") )
				continue;
			if(kk.equals("token"))
				token =(String) params.get(kk);
			if(kk.equals("deviceid"))
				devid =(String) params.get(kk);
			if(kk.equals("type"))
				type =(String) params.get(kk);
			sb.append(key1).append(
					( params.get(kk) )
				);
		}
		sb.append( APPSECRET );
		
		String tcp         =    getHeader(request(), "tcp");
		if( tcp == null )
			tcp="";
		
		String headerkey   =	"for";
		String expiresTime =	getHeader(request(), "ExpiresTime");
		String checktcp    = 	MD5.mkMd5( expiresTime+sign+headerkey );
		
		boolean tag= sign.equalsIgnoreCase(MD5.mkMd5(sb.toString()))
				&& checktcp .equalsIgnoreCase( tcp );
		
		if(!tag){
			appLogger.debug("sign============="+sign+","+sb.toString()+","+MD5.mkMd5(sb.toString()));
			appLogger.debug("tcp============="+tcp+","+checktcp+","+expiresTime+sign+headerkey);
		}
		
//		if( tag==true && IniLoader.getIntegerByKey("debug", "recact", 1, "sys") == 1 ){
//			BaseActionV4.recordAct( token,devid,type,request() );
//		}
		
		return tag;
	}
	
	public static String decryptAes2String(String name){
		String param = request().getQueryString(name);
		if(!util.StringUtil.isNull(param)){
			try{
				String decryptResult = AesUtil.Decrypt(param, SKEY, SIV);
				return decryptResult;
			}catch(Exception e){
				appLogger.error("getkAesToJsonArray error"+e);
			}
		}
		return null;
	}
	
	public static Map<?,?> decryptAesBody2Map(){
		String body = request().body().asText();
		appLogger.debug("body--"+body);
		if(!util.StringUtil.isNull(body)){
			try{
				String decryptResult = AesUtil.Decrypt(body, SKEY, SIV);
				appLogger.debug("decryptResult--"+decryptResult);
				if(!util.StringUtil.isNull(decryptResult)){
					Map<?, ?> params = JsonUtil.parseJson(decryptResult);
					if(params!=null)
						return params;
				}	
			}catch(Exception e){
				appLogger.error("parseAesBody2Json error"+e);
			}
		}
		return null;
	}
	
	public static String encryptString2Aes(String str){
		if(!util.StringUtil.isNull(str)){
			try{
				return AesUtil.Encrypt(str, SKEY, SIV);
			}catch(Exception e){
				appLogger.error("getStringtoAes error"+e);
			}
		}
		
		return null;
	}
	
	
	public static String getHeader(Request request,String key)
	{
		String ret = request.getHeader(key);
		if(!StringTool.isNull(ret))
			return ret;
		String[] keys = request.headers().get(key);
		if(keys!=null)
			return keys[0];
		return null;
	}
	
	public static Result makSignFailor()
	{
		ReturnJson reJson=new ReturnJson();
		reJson.setCode(1);
		reJson.setMessage("签名检验失败。");
		String reContent = JsonUtil.parseObj(reJson);
		appLogger.debug(reContent);
		return ok(reContent);
	}
	
	public static String getDomain(){
		return "http://www.sknhome.com";
	}
}
