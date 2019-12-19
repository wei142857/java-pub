package util;

import java.net.*;
import java.io.*;
import java.util.*;

import util.classEntity.FileOP;

public class GetUrl {

	public static String getURL(String url) {
		ByteBuffer buff = new ByteBuffer(ByteBuffer.INCREASE_SIZE,
				ByteBuffer.INCREASE_SIZE);
		try {
			URL myurl = new URL(url);

			HttpURLConnection myconn = (HttpURLConnection) myurl
					.openConnection();
			
			myconn.setConnectTimeout( 
            		IniLoader.getIntegerByKey( "timeout", "CommonHttpConnect" , 1000*15 , "sys" ) );
            
			myconn.setReadTimeout( 
            		IniLoader.getIntegerByKey( "timeout", "CommonHttpRead" , 1000*30 , "sys" ) );
			
			myconn.setRequestProperty("Accept-Language", "utf-8");
			DataInputStream myInput = new DataInputStream(
					myconn.getInputStream());

			int nSize;

			byte[] read = new byte[10240];
			while ((nSize = myInput.read(read)) > 0) {
				buff.append(read, 0, nSize);
			}
			myInput.close();
			myconn.disconnect();
		} catch (Exception e) {
			System.out.println("get url:" + e.toString());
			return null;
		}

		try {
			return new String(buff.getData(), 0, buff.getSize(), "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String postURL(String url) {
		ByteBuffer buff = new ByteBuffer(ByteBuffer.INCREASE_SIZE,
				ByteBuffer.INCREASE_SIZE);
		try {
			URL myurl = new URL(url);
			HttpURLConnection myconn = (HttpURLConnection) myurl
					.openConnection();

			myconn.setConnectTimeout( 
            		IniLoader.getIntegerByKey( "timeout", "CommonHttpConnect" , 1000*30 , "sys" ) );
            
			myconn.setReadTimeout( 
            		IniLoader.getIntegerByKey( "timeout", "CommonHttpRead" , 1000*60 , "sys" ) );
			
			myconn.setRequestMethod("POST");
			myconn.setDoOutput(true);
			myconn.setAllowUserInteraction(false);
			DataInputStream myInput = new DataInputStream(
					myconn.getInputStream());
			if (myInput.available() <= 0)
				return null;

			byte[] read = new byte[10240];
			int nSize;
			while ((nSize = myInput.read(read)) > 0) {

				buff.append(read, 0, nSize);
			}
			myInput.close();
			myconn.disconnect();
		} catch (Exception e) {
			return null;
		}
		return new String(buff.getData(), 0, buff.getSize());
	}

	public static String postURL(String url, java.util.Properties prop) {
		ByteBuffer buff = new ByteBuffer(ByteBuffer.INCREASE_SIZE,
				ByteBuffer.INCREASE_SIZE);
		try {
			URL myurl = new URL(url);
			HttpURLConnection myconn = (HttpURLConnection) myurl
					.openConnection();

			myconn.setConnectTimeout( 
            		IniLoader.getIntegerByKey( "timeout", "CommonHttpConnect" , 1000*30 , "sys" ) );
            
			myconn.setReadTimeout( 
            		IniLoader.getIntegerByKey( "timeout", "CommonHttpRead" , 1000*60 , "sys" ) );
			
			myconn.setRequestMethod("POST");
			myconn.setDoOutput(true);

			//myconn.setRequestProperty("Accept-Language", "zh-cn");
			myconn.setRequestProperty("Accept-Encoding", "gzip, deflate");
			DataOutputStream myOutput = new DataOutputStream(
					myconn.getOutputStream());
			String str = "";

			// parameter;
			Enumeration en = prop.propertyNames();
			for (int i = 0; i < prop.size(); i++) {
				String pName = (String) en.nextElement();
				String pValue = prop.getProperty(pName);
				if (pValue == null)
					continue;
				if (i == 0)
					str += pName + "=" + java.net.URLEncoder.encode(pValue,"utf-8");
				else
					str += "&" + pName + "="
							+ java.net.URLEncoder.encode(pValue,"utf-8");
			}

			myOutput.writeBytes(str);
			myOutput.close();

			DataInputStream myInput = new DataInputStream(
					myconn.getInputStream());

			byte[] read = new byte[10240];
			int nSize;
			while ((nSize = myInput.read(read)) > 0) {
				buff.append(read, 0, nSize);
			}
			myInput.close();
			myconn.disconnect();
		} catch (Exception e) {
			System.out.println(e.toString());
			util.LogUtil.writeLog(url+" -- "+e.toString(), "neterror");
			return null;
		}
		try {
			return new String(buff.getData(), 0, buff.getSize(),"utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}

	public static void postString(String hurl,String s) {

		try {
			// 创建连接
			URL url = new URL(hurl);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setConnectTimeout( 
            		IniLoader.getIntegerByKey( "timeout", "CommonHttpConnect" , 1000*30 , "sys" ) );
            
			connection.setReadTimeout( 
            		IniLoader.getIntegerByKey( "timeout", "CommonHttpRead" , 1000*60 , "sys" ) );
			
			
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setUseCaches(false);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestProperty("Content-Type", "application/json");

			connection.connect();

			// POST请求
			DataOutputStream out = new DataOutputStream(
					connection.getOutputStream());

			out.writeBytes( s );
			out.flush();
			out.close();

			// 读取响应
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String lines;
			StringBuffer sb = new StringBuffer("");
			while ((lines = reader.readLine()) != null) {
				lines = new String(lines.getBytes(), "utf-8");
				sb.append(lines);
			}
			System.out.println(sb);
			reader.close();
			// 断开连接
			connection.disconnect();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String args[])
	{
		/*Users uu =new Users("2.00dJkR7B0983sVd588269fb382IjxD");
		try{
			User u =uu.showUserById("1304546565");
			System.out.println(u);
		}catch(Exception e){
			System.out.println( e.toString() );
		}*/
		GetUrl.postString( "http://api.sinobyte.cn/v4/syncontact", 
				"{\"aa\":\"bb\"}" );
	}
	
	public static String getURL(String url,int connectTimeout,int readTimeout) {
		ByteBuffer buff = new ByteBuffer(ByteBuffer.INCREASE_SIZE,
				ByteBuffer.INCREASE_SIZE);
		try {
			URL myurl = new URL(url);

			HttpURLConnection myconn = (HttpURLConnection) myurl
					.openConnection();
			
			myconn.setConnectTimeout( 
					connectTimeout );
            
			myconn.setReadTimeout( 
					readTimeout );
			
			myconn.setRequestProperty("Accept-Language", "utf-8");
			DataInputStream myInput = new DataInputStream(
					myconn.getInputStream());

			int nSize;

			byte[] read = new byte[10240];
			while ((nSize = myInput.read(read)) > 0) {
				buff.append(read, 0, nSize);
			}
			myInput.close();
			myconn.disconnect();
		} catch (Exception e) {
			System.out.println("get url:" + e.toString());
			return null;
		}

		try {
			return new String(buff.getData(), 0, buff.getSize(), "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getURLQuick(String url) {
		return getURL(url,1000,2000);
	}
	
	public static String getURL(String url, java.util.Properties prop) {
		ByteBuffer buff = new ByteBuffer(ByteBuffer.INCREASE_SIZE,
				ByteBuffer.INCREASE_SIZE);
		try {
			// parameter;
			String str = "";
			Enumeration en = prop.propertyNames();
			for (int i = 0; i < prop.size(); i++) {
				String pName = (String) en.nextElement();
				String pValue = prop.getProperty(pName);
				if (pValue == null)
					continue;
				if (i == 0)
					str += pName + "=" + java.net.URLEncoder.encode(pValue,"utf-8");
				else
					str += "&" + pName + "="
							+ java.net.URLEncoder.encode(pValue,"utf-8");
			}
			
			if(url.indexOf("?")==-1)
				url += "?"+str;
			else
				url += "&"+str;
						
			URL myurl = new URL(url);
			play.Logger.info( url );
			HttpURLConnection myconn = (HttpURLConnection) myurl
					.openConnection();

			myconn.setConnectTimeout( 
            		IniLoader.getIntegerByKey( "timeout", "CommonHttpConnect" , 1000*30 , "sys" ) );
            
			myconn.setReadTimeout( 
            		IniLoader.getIntegerByKey( "timeout", "CommonHttpRead" , 1000*60 , "sys" ) );
			
			//myconn.setRequestProperty("Accept-Language", "zh-cn");

			DataInputStream myInput = new DataInputStream(
					myconn.getInputStream());

			byte[] read = new byte[10240];
			int nSize;
			while ((nSize = myInput.read(read)) > 0) {
				buff.append(read, 0, nSize);
			}
			myInput.close();
			myconn.disconnect();
		} catch (Exception e) {
			System.out.println(e.toString());
			return null;
		}
		try {
			return new String(buff.getData(), 0, buff.getSize(),"utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}

}