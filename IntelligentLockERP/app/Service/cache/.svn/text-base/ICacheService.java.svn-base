package Service.cache;

public interface ICacheService {
	/*
	 * cache operator;
	 */

	// 设置一个数据像 key为键值，o为对象，timeout为超时设置（秒）
	public boolean set(String key, Object o, int timeout);

	// 设置一个数据像 key为键值，o为对象，永不超时
	public boolean set(String key, Object o);

	// 根据键值 获取数据
	public Object get(String key);

	// 清除数据
	public Object clear(String key);
	
	//cache string 
	
	public static String Cache_String_Config="SYSCONF_";

}
