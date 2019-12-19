package nets;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ListenServerS extends Thread {
	int port;
	public ListenServerS(int p)
	{
		port = p;
		play.Logger.info("ListenServer-S start ... begin listen:"+port);
		start();
	}
	public void run()
	{
		ServerSocket svr = null;
		try{
			svr = new ServerSocket( port );
			
			while( true ){
				// 监听一端口，等待客户接入
				Socket socket = svr.accept();
				// 将会话交给线程处理
				if( socket != null ){
					play.Logger.info("LockSocketThread-S accept ... "+socket.getRemoteSocketAddress().toString() );
					util.LogUtil.writeLog("LockSocketThread-S accept ... "+socket.getRemoteSocketAddress().toString(),"LockSocketThreadS");
					new LockSocketThreadS( socket);
				}
				
				Thread.currentThread().sleep(1);
			}
		}catch(Exception e)
		{
			e.printStackTrace();
			util.LogUtil.writeLog("Run exception:"+e.toString(), "ListenServer-S");
		}finally{
			if(svr!=null)
				try {
					svr.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

}
