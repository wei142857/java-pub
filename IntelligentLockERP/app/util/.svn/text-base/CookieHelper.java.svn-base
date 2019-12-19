package util;

public class CookieHelper {
	
	//获取Request里面的一个cookie值
	public static String getCookie(play.mvc.Http.Request request,String name)
	{
		if( request.cookies().get(name)!=null )
		{
			return request.cookies().get(name).value();
		}
		return null;
	}
	
	//设置一个cookie(带时间，以秒计算)
	//response().setCookie(name, value, 1000);
}
