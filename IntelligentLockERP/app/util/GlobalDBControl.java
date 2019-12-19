package util;

public class GlobalDBControl {
	
	//各个工作线程可以有自己的数据库连接池设置，这个可以在外部设置
	@SuppressWarnings("unchecked")
	private static ThreadLocal contextHolder = new ThreadLocal();
	
	public static String getDB()
	{
		String db =(String)contextHolder.get();
		if( db==null )	
			db = "default";
		return db;
	}
	
	public static String getReadDB()
	{
		return "reader";
	}

}
