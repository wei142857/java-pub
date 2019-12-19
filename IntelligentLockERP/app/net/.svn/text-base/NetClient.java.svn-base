package net;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import util.classEntity.StringTool;

public class NetClient {

	public static void main(String[] args) {
	    // 建立连接后获得输出流
	    OutputStream outputStream;
		try {
			Socket socket = new Socket("127.0.0.1", 9001);
			
			outputStream = socket.getOutputStream();
			String message="A91E071070010101460043555309993022312E302E3139313132312230303030"
					/*+ "0110"
					+ "70"
					+ "010101"
					+ "0102030405060708"*/;
			socket.getOutputStream().write(StringTool.getFromHexString(message));
			socket.getOutputStream().flush();
			
			byte[] b =new byte[1000];
			int r = socket.getInputStream().read(b);
			String ret = "got:"+r+" , "+StringTool.makeHexString(b, r);
			System.out.println(ret);
			
			Thread.currentThread().sleep(1*1000);
			
			b =new byte[1000];
			r = socket.getInputStream().read(b);
			ret = "got:"+r+" , "+StringTool.makeHexString(b, r);
			System.out.println(ret);
			
			Thread.currentThread().sleep(1*1000);
			
			b =new byte[1000];
			r = socket.getInputStream().read(b);
			ret = "got:"+r+" , "+StringTool.makeHexString(b, r);
			System.out.println(ret);
			
			
			
			/*Thread.currentThread().sleep(10*1000);
			
			
			message="A90E"
					+ "0110"
					+ "70"
					+ "010101"
					+ "0102030405060708";
			socket.getOutputStream().write(StringTool.getFromHexString(message));
			socket.getOutputStream().flush();
			
			b =new byte[100];
			r = socket.getInputStream().read(b);
			ret = "got:"+r+" , "+StringTool.makeHexString(b, r);
			System.out.println(ret);*/
			
			
			outputStream.close();
			socket.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
