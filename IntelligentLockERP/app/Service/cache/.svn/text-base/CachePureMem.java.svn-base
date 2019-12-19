package Service.cache;


import java.util.Hashtable;

/*
 * 以redius 为Cache的方式，速度快
 */

public class CachePureMem {

	public static CachePureMem instance = new CachePureMem();
	
	public boolean set(String key, String o, int timeout) {
		util.jedis.RedisUtil.getInstance().setEntity(key, o,timeout);
		return true;
	}
	
	
	public String get(String key) {
		return util.jedis.RedisUtil.getInstance().getEntity(key,String.class);
	}

	public Object clear(String key) {
		util.jedis.RedisUtil.getInstance().deleteEntity(key);
		return null;
	}
	
	/*
	 * 在内存中保存一套数据快速读取，超时后废弃。用于在获取 redis数据前，从内存中获取对象。
	 * 这个适用于 频繁获取，但不能是 大量key键的 数据存储, 防止 内存溢出
	 */
	static Hashtable<String,Object> allKeyInMem = new Hashtable<String, Object>();
	static Hashtable<String,Long> allKeyTimeInMem = new Hashtable<String, Long>();
	
	public static Object findInMemObj( String keyMem , int timeout )
	{
		try{
			String key       = keyMem;
			Object value     = allKeyInMem.get( key );
			if( value == null )
				return null;
			
			//查找插入key的時間
			String keyTime   = keyMem+ "-time";
			Long valueTime = allKeyTimeInMem.get( keyTime );
			if( valueTime == null )
				return null;
			
			long now = System.currentTimeMillis();
			
			if( now-valueTime.longValue() > 1000*timeout ) //是否超时了
			{
				return null;
			}
			
			return value ;
		}catch(Exception e){
			return null;
		}
	}
	
	public static void setInMemObj( String keyMem  , Object value )
	{
		try{
			String key       = keyMem;
			String keyTime   = keyMem + "-time";
			
			long now = System.currentTimeMillis();
			allKeyInMem.put( key, value );
			allKeyTimeInMem.put(keyTime, Long.valueOf(now) );
		}catch(Exception e){
			
		}
	}

}
