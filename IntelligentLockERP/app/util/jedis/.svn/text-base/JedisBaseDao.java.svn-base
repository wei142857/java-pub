package util.jedis;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import play.Logger;
import play.Logger.ALogger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import util.classEntity.StringTool;


public class JedisBaseDao {
	
	
	static ALogger logger = Logger.of(JedisBaseDao.class);

	private static String JEDIS_PASSWORD;

	private static String[] JEDIS_MASTER_IPS;
	private static String[] JEDIS_SLAVE_IPS;

	private static String JEDIS_MASTER;
	private static String JEDIS_SLAVE;

	public static JedisPool[] writers;
	//public static JedisPool OTHERWRITE;
	public static JedisPool[] readers;
	//public static JedisPool OTHERREAD;

	//用于队列的redius，防止和缓存使用的redius抢资源
	private static String JEDIS_QUEUE;
	public static JedisPool DEFALULTQueue;

	/**
	 * pools[0]=100.0.6.101 6379 Master1
	 * pools[1]=100.0.6.102 6380 Master2
	 * pools[2]=100.0.6.103:6379 Slave1
	 * pools[3]=100.0.6.103:6380 Slave2
	 */

	static {

		Config conf = Config.getInstance();

		JEDIS_MASTER = conf.getString("jedis.masterip","100.0.6.12:6379,100.0.6.12:6379");

		JEDIS_SLAVE =  conf.getString("jedis.slaveip", "100.0.6.12:6379,100.0.6.12:6379");

		//获取多个配置的host 和 port
		JEDIS_MASTER_IPS = JEDIS_MASTER.split(",");
		JEDIS_SLAVE_IPS = JEDIS_SLAVE.split(",");


		JEDIS_PASSWORD = conf.getString("jedis.password", null);

		//获取多个 initJedisPool
		writers = new JedisPool[JEDIS_MASTER_IPS.length];
		for(int i=0;i<JEDIS_MASTER_IPS.length;i++ ){
			String s = JEDIS_MASTER_IPS[i];
			String[] infos = StringTool.splitString(s, ":");
			writers[i] = initJedisPool(infos[0],Integer.valueOf(infos[1]));
		}
		
		//获取多个 initJedisPool
		readers = new JedisPool[JEDIS_SLAVE_IPS.length];
		for(int i=0;i<JEDIS_SLAVE_IPS.length;i++ ){
			String s = JEDIS_SLAVE_IPS[i];
			String[] infos = StringTool.splitString(s, ":");
			readers[i] = initJedisPool(infos[0],Integer.valueOf(infos[1]));
		}
		
		//DEFALULTWRITE = initJedisPool(JEDIS_MASTER_IPS[0].split(":")[0],Integer.valueOf(JEDIS_MASTER_IPS[0].split(":")[1]));	

		//OTHERWRITE = initJedisPool(JEDIS_MASTER_IPS[1].split(":")[0],Integer.valueOf(JEDIS_MASTER_IPS[1].split(":")[1]));

		//DEFALULTREAD = initJedisPool(JEDIS_SLAVE_IPS[0].split(":")[0],Integer.valueOf(JEDIS_SLAVE_IPS[0].split(":")[1]));

		//OTHERREAD = initJedisPool(JEDIS_SLAVE_IPS[1].split(":")[0],Integer.valueOf(JEDIS_SLAVE_IPS[1].split(":")[1]));

		JEDIS_QUEUE = conf.getString("jedis.queueip", null);

		if( JEDIS_QUEUE != null )
			DEFALULTQueue = initJedisPool(JEDIS_QUEUE.split(":")[0],Integer.valueOf(JEDIS_QUEUE.split(":")[1]));
	}

	

	private static JedisPool initJedisPool(String ip,int port) {
		JedisPool jedispool = null;
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxIdle(50);    // 最大空闲
		config.setMaxTotal(5000);  // 最大实例数
		config.setMaxWaitMillis(5000L);// 最大等待时间
		config.setTestOnBorrow(true);
		config.setTestOnReturn(true);
		config.setTestWhileIdle(true);
		config.setMinEvictableIdleTimeMillis(60000l);
		config.setTimeBetweenEvictionRunsMillis(3000l);
		config.setNumTestsPerEvictionRun(-1);
		
		try{
			jedispool = new JedisPool(config, ip, port, 60000,JEDIS_PASSWORD);
		}catch(Exception e){
			Logger.info( "ip -"+ip+"port - "+port +" - server down");
		}
		return jedispool;
	}
	
	public static int getDefaultDb(){
		return 0;
		/*Config conf = Config.getInstance();
		conf.reloadProperty();
		return conf.getInt("jedis.dbindex", 0);*/
	}
	/**
	 * 【获取数据】
	 * 
	 * @param key
	 * @return
	 */
	public static String get(String key, JedisPool jedisPool) {

		String value = null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			value = jedis.get(key);
		} catch (Exception e) {
			// 释放redis对象
			release(jedisPool, jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			close(jedis, jedisPool);
		}

		return value;
	}


	public static Set<String> keys(String key,int dbIndex, JedisPool jedisPool) {

		Set<String> values = null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(dbIndex);
			values = jedis.keys(key);
		} catch (Exception e) {
			release(jedisPool, jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			close(jedis, jedisPool);
		}

		return values;
	}
	/**
	 * 【关闭连接返还到连接池】
	 * @param jedis
	 * @param jedisPool
	 */
	public static void close(Jedis jedis, JedisPool jedisPool) {
		try {
			jedisPool.returnResource(jedis);

		} catch (Exception e) {
			if (jedis.isConnected()) {
				jedis.close();
				jedis.quit();
				jedis.disconnect();
			}
		}
	}
	
	public static void release(JedisPool jedisPool, Jedis jedis) {
		jedisPool.returnBrokenResource(jedis);
	}

	/**
	 * 【获取序列化数据】
	 * 
	 * @param key
	 * @return
	 */
	public static byte[] get(byte[] key,int dbIndex, JedisPool jedisPool) {

		byte[] value = null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(dbIndex);
			value = jedis.get(key);
		} catch (Exception e) {
			release(jedisPool, jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			close(jedis, jedisPool);
		}

		return value;
	}

	/**
	 * 【Set字符串值】
	 * @param strKey
	 * @param strValue
	 * @param jedisPool
	 * @return
	 *//*
	public long set(String strKey, String strValue, JedisPool jedisPool) {

		Jedis jedis = jedisPool.getResource();
		try {
			return jedis.setnx(strKey, strValue);

		} finally {
			// 返还到连接池
			close(jedis, jedisPool);
		}

	}*/

	/**
	 * 【Set序列化Key-Value值】
	 * @param key
	 * @param value
	 * @param jedisPool
	 */
	public static void set(byte[] key, byte[] value, JedisPool jedisPool) {

		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.set(key, value);
		} catch (Exception e) {
			release(jedisPool, jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			close(jedis, jedisPool);
		}
	}

	/**
	 * 【Set值并设置有效期】
	 * @param key
	 * @param value
	 * @param time 0永久
	 * @param jedisPool
	 */
	public static void set(byte[] key, byte[] value, int time,int dbIndex, JedisPool jedisPool) {

		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(dbIndex);
			jedis.set(key, value);
			if(time!=0){
				jedis.expire(key, time);
			}

		} catch (Exception e) {
			release(jedisPool, jedis);
		} finally {
			// 返还到连接池
			close(jedis, jedisPool);
		}
	}

	public static void hset(byte[] key, byte[] field, byte[] value, JedisPool jedisPool) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.hset(key, field, value);
		} catch (Exception e) {
			release(jedisPool, jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			close(jedis, jedisPool);
		}
	}

	public static void hset(String key, String field, String value, JedisPool jedisPool) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.hset(key, field, value);
		} catch (Exception e) {
			release(jedisPool, jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			close(jedis, jedisPool);
		}
	}

	/**
	 * 获取数据
	 * 
	 * @param key
	 * @return
	 */
	public static String hget(String key, String field, JedisPool jedisPool) {

		String value = null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			value = jedis.hget(key, field);
		} catch (Exception e) {
			release(jedisPool, jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			close(jedis, jedisPool);
		}

		return value;
	}

	/**
	 * 获取数据
	 * 
	 * @param key
	 * @return
	 */
	public static byte[] hget(byte[] key, byte[] field, JedisPool jedisPool) {

		byte[] value = null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			value = jedis.hget(key, field);
		} catch (Exception e) {
			release(jedisPool, jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			close(jedis, jedisPool);
		}

		return value;
	}

	public static void hdel(byte[] key, byte[] field, JedisPool jedisPool) {

		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.hdel(key, field);
		} catch (Exception e) {
			release(jedisPool, jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			close(jedis, jedisPool);
		}
	}

	/**
	 * 存储REDIS队列 顺序存储
	 * 
	 * @param byte[] key reids键名
	 * @param byte[] value 键值
	 */
	public static void lpush(byte[] key, byte[] value,int dbIndex, JedisPool jedisPool) {

		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(dbIndex);
			jedis.lpush(key, value);
		} catch (Exception e) {
			release(jedisPool, jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			close(jedis, jedisPool);
		}
	}

	/**
	 * 存储REDIS队列 反向存储
	 * 
	 * @param byte[] key reids键名
	 * @param byte[] value 键值
	 */
	public static void rpush(byte[] key, byte[] value, JedisPool jedisPool) {

		Jedis jedis = null;
		try {

			jedis = jedisPool.getResource();
			jedis.rpush(key, value);

		} catch (Exception e) {

			release(jedisPool, jedis);
			e.printStackTrace();

		} finally {

			// 返还到连接池
			close(jedis, jedisPool);

		}
	}

	/**
	 * 将列表 source 中的最后一个元素(尾元素)弹出，并返回给客户端
	 * 
	 * @param byte[] key reids键名
	 * @param byte[] value 键值
	 */
	public static void rpoplpush(byte[] key, byte[] destination, JedisPool jedisPool) {

		Jedis jedis = null;
		try {

			jedis = jedisPool.getResource();
			jedis.rpoplpush(key, destination);

		} catch (Exception e) {

			release(jedisPool, jedis);
			e.printStackTrace();

		} finally {

			// 返还到连接池
			close(jedis, jedisPool);

		}
	}

	/**
	 * 获取队列数据
	 * 
	 * @param byte[] key 键名
	 * @return
	 */
	public static List<byte[]> lpopList(byte[] key, int dbIndex,JedisPool jedisPool) {

		List<byte[]> list = null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(dbIndex);
			list = jedis.lrange(key, 0, -1);
			jedis.del(key);//读取该队列里所有数据后，删除该队列
		} catch (Exception e) {

			release(jedisPool, jedis);
			e.printStackTrace();

		} finally {

			// 返还到连接池
			close(jedis, jedisPool);

		}
		return list;
	}

	/**
	 * 获取队列数据
	 * 
	 * @param byte[] key 键名
	 * @return
	 */
	public static byte[] rpop(byte[] key, int dbIndex,JedisPool jedisPool) {

		byte[] bytes = null;
		Jedis jedis  = null;
		try {

			jedis = jedisPool.getResource();
			jedis.select(dbIndex);
			bytes = jedis.rpop(key);
			/*List<byte[]> retBytes = jedis.brpop(0,key);
			if( retBytes!=null&&retBytes.size() >1 )
				bytes = retBytes.get(1);*/

		} catch (Exception e) {
			release(jedisPool, jedis);
			e.printStackTrace();

		} finally {
			// 返还到连接池
			close(jedis, jedisPool);

		}
		return bytes;
	}
	

	/**
	 * 获取队列数据
	 * 
	 * @param byte[] key 键名
	 * @return
	 */
	public static String rpop(String key, int dbIndex,JedisPool jedisPool) {

		String bytes = null;
		Jedis jedis = null;
		try {

			jedis = jedisPool.getResource();
			jedis.select(dbIndex);
			bytes = jedis.rpop(key);

		} catch (Exception e) {
			release(jedisPool, jedis);
			e.printStackTrace();

		} finally {

			// 返还到连接池
			close(jedis, jedisPool);

		}
		return bytes;
	}
	
	
	//这个是不返还资源的使用 jedis对象
	public static byte[] rpop(byte[] key, int dbIndex,Jedis jedis) {

		byte[] bytes = null;
		try {

			jedis.select(dbIndex);
			bytes = jedis.rpop(key);
			/*List<byte[]> retBytes = jedis.brpop(0,key);
			if( retBytes!=null&&retBytes.size() >1 )
				bytes = retBytes.get(1);*/

		} catch (Exception e) {

		} finally {
		}
		return bytes;
	}

	public static void hmset(Object key, Map<String, String> hash, JedisPool jedisPool) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.hmset(key.toString(), hash);
		} catch (Exception e) {
			release(jedisPool, jedis);
			e.printStackTrace();

		} finally {
			// 返还到连接池
			close(jedis, jedisPool);

		}
	}

	public static void hmset(Object key, Map<String, String> hash, int time, JedisPool jedisPool) {
		Jedis jedis = null;
		try {

			jedis = jedisPool.getResource();
			jedis.hmset(key.toString(), hash);
			jedis.expire(key.toString(), time);
		} catch (Exception e) {
			release(jedisPool, jedis);
			e.printStackTrace();

		} finally {
			// 返还到连接池
			close(jedis, jedisPool);

		}
	}

	public static List<String> hmget(Object key, JedisPool jedisPool, String... fields) {
		List<String> result = null;
		Jedis jedis = null;
		try {

			jedis = jedisPool.getResource();
			result = jedis.hmget(key.toString(), fields);

		} catch (Exception e) {
			release(jedisPool, jedis);
			e.printStackTrace();

		} finally {
			// 返还到连接池
			close(jedis, jedisPool);

		}
		return result;
	}

	public static Set<String> hkeys(String key, JedisPool jedisPool) {
		Set<String> result = null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			result = jedis.hkeys(key);

		} catch (Exception e) {
			release(jedisPool, jedis);
			e.printStackTrace();

		} finally {
			// 返还到连接池
			close(jedis, jedisPool);

		}
		return result;
	}

	public static List<byte[]> lrange(byte[] key, int from, int to, JedisPool jedisPool) {
		List<byte[]> result = null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			result = jedis.lrange(key, from, to);

		} catch (Exception e) {
			release(jedisPool, jedis);
			e.printStackTrace();

		} finally {
			// 返还到连接池
			close(jedis, jedisPool);

		}
		return result;
	}

	public static Map<byte[], byte[]> hgetAll(byte[] key, JedisPool jedisPool) {
		Map<byte[], byte[]> result = null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			result = jedis.hgetAll(key);
		} catch (Exception e) {
			release(jedisPool, jedis);
			e.printStackTrace();

		} finally {
			// 返还到连接池
			close(jedis, jedisPool);
		}
		return result;
	}

	public static void del(byte[] key,int dbIndex, JedisPool jedisPool) {

		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(dbIndex);
			jedis.del(key);
		} catch (Exception e) {
			release(jedisPool, jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			close(jedis, jedisPool);
		}
	}
	
	public static void delStr(String key,int dbIndex, JedisPool jedisPool) {

		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(dbIndex);
			jedis.del(key);
		} catch (Exception e) {
			release(jedisPool, jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			close(jedis, jedisPool);
		}
	}

	/**
	 * 获取队列消息数
	 * @param key
	 * @param jedisPool
	 * @return
	 */
	public static long llen(byte[] key, JedisPool jedisPool) {

		long len = 0;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.llen(key);
		} catch (Exception e) {
			release(jedisPool, jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			close(jedis, jedisPool);
		}
		return len;
	}
	
	/**
	 * 是否存在某个key
	 * @param key
	 * @param jedisPool
	 * @return
	 */
	public static boolean exists(String key,JedisPool jedisPool){
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			boolean bool = jedis.exists(key);
			return bool;
		} catch (Exception e) {
			release(jedisPool, jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			close(jedis, jedisPool);
		}
		return false;
	}

	/**
	 * 是否存在某个key
	 * @param key
	 * @param jedisPool
	 * @return
	 */
	public static boolean exists(String key,int dbIndex,JedisPool jedisPool){
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(dbIndex);
			boolean bool = jedis.exists(key);
			return bool;
		} catch (Exception e) {
			release(jedisPool, jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			close(jedis, jedisPool);
		}
		return false;
	}
	//*********************数值操作* begin********************************
	/**
	 * 递减数字
	 * @param key
	 * @param dbIndex
	 * @param jedisPool
	 * @return
	 */
	public static Long DecrNum(String key,int dbIndex,JedisPool jedisPool){
		Long ret = null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(dbIndex);
			ret = jedis.decr(key);

		} catch (Exception e) {
			release(jedisPool, jedis);
		} finally {
			// 返还到连接池
			close(jedis, jedisPool);
		}
		return ret;
	}

	/**
	 * 减数字 number
	 * @param key
	 * @param dbIndex
	 * @param jedisPool
	 * @return
	 */
	public static Long DecrNum(String key,int number,int dbIndex,JedisPool jedisPool){
		Long ret = null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(dbIndex);
			ret = jedis.decrBy(key, number);

		} catch (Exception e) {
			release(jedisPool, jedis);
		} finally {
			// 返还到连接池
			close(jedis, jedisPool);
		}
		return ret;
	}
	/**
	 * 按时间数字递减数字
	 * @param key
	 * @param number
	 * @param time
	 * @param dbIndex
	 * @param jedisPool
	 * @return
	 */
	public static Long DecrNum(String key,int number,int time,int dbIndex,JedisPool jedisPool){
		Long ret = null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(dbIndex);
			ret = jedis.decrBy(key, number);
			if(time!=0)
				jedis.expire(key, time);

		} catch (Exception e) {
			release(jedisPool, jedis);
		} finally {
			// 返还到连接池
			close(jedis, jedisPool);
		}
		return ret;
	}

	/**
	 * 递增数字
	 * @param key
	 * @param dbIndex
	 * @param jedisPool
	 * @return
	 */
	public static Long IncrNum(String key,int dbIndex,JedisPool jedisPool){
		Long ret = null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(dbIndex);
			ret = jedis.incr(key);

		} catch (Exception e) {
			release(jedisPool, jedis);
		} finally {
			// 返还到连接池
			close(jedis, jedisPool);
		}
		return ret;
	}

	/**
	 * 按数字增加数值
	 * @param key
	 * @param dbIndex
	 * @param  number
	 * @param jedisPool
	 * @return
	 */
	public static Long IncrNum(String key,int number,int dbIndex,JedisPool jedisPool){
		Long ret = null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(dbIndex);
			ret = jedis.incrBy(key, number);

		} catch (Exception e) {
			release(jedisPool, jedis);
		} finally {
			// 返还到连接池
			close(jedis, jedisPool);
		}
		return ret;
	}
	/**
	 * 按时间数字增加数值
	 * @param key
	 * @param number
	 * @param time
	 * @param dbIndex
	 * @param  number
	 * @param jedisPool
	 * @return
	 */
	public static Long IncrNum(String key,int number,int time,int dbIndex,JedisPool jedisPool){
		Long ret = null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(dbIndex);
			ret = jedis.incrBy(key, number);
			if(time!=0)
				jedis.expire(key, time);

		} catch (Exception e) {
			release(jedisPool, jedis);
		} finally {
			// 返还到连接池
			close(jedis, jedisPool);
		}
		return ret;
	}

	//*********************数值操作* end********************************
	//*******************list  start*******************************
	
	/**
	 * 获取list的个数
	 * @param key
	 * @param jedisPool
	 * @return
	 */
	public static long llen(String key, int dbindex,JedisPool jedisPool) {

		long len = 0;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(dbindex);
			len = jedis.llen(key);
		} catch (Exception e) {
			release(jedisPool, jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			close(jedis, jedisPool);
		}
		return len;
	}
	/**
	 * 存储REDIS队列 顺序存储
	 * 
	 * @param byte[] key reids键名
	 * @param byte[] value 键值
	 */
	public static void lpush(String key, String value,int dbIndex, JedisPool jedisPool,int time) {

		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String  aadbIndex = jedis.select(dbIndex);
			Long lpushresust = jedis.lpush(key, value);
			if(time!=0){
				 Long timeresust =  jedis.expire(key, time);
			}
		} catch (Exception e) {
			release(jedisPool, jedis);
			logger.error("push--"+key,e);
		} finally {
			// 返还到连接池
			close(jedis, jedisPool);
		}
	}

	/**
	 * 获取List数据
	 * LRANGE languages 0 -1 
	 * 起始位置0 ，结束-1 获取list中所有值
	 * @param key  
	 * @return
	 */
	public static List<String> lrange(String key, Long start,Long end, int dbIndex,JedisPool jedisPool) {

		List<String> value = null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(dbIndex);
			value = jedis.lrange(key, start, end);
		} catch (Exception e) {
			release(jedisPool, jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			close(jedis, jedisPool);
		}

		return value;
	}
	
	

	/**
	 * 获取List数据
	 * LRANGE languages 0 -1 
	 * 起始位置0 ，结束-1 获取list中所有值
	 * @param key  
	 * @return
	 */
	public static long lrem(String key, Long count,String value, int dbIndex,JedisPool jedisPool) {

		long len = 0;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(dbIndex);
			len = jedis.lrem(key, count, value);
		} catch (Exception e) {
			release(jedisPool, jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			close(jedis, jedisPool);
		}

		return len;
	}
	//*******************list  end*******************************
	//******************存储用字符串  begin***************************************
	/**
	 * 【Set值并设置有效期】
	 * @param key
	 * @param value
	 * @param time 0永久
	 * @param jedisPool
	 */
	public static String setStr(String key, String value, int time,int dbIndex, JedisPool jedisPool) {
		String str = null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(dbIndex);
			str = jedis.set(key, value);
			if(time!=0){
				jedis.expire(key, time);
			}

		} catch (Exception e) {
			release(jedisPool, jedis);
		} finally {
			// 返还到连接池
			close(jedis, jedisPool);
		}
		return str;
	}

	/**
	 * 【获取序列化数据】
	 * 
	 * @param key
	 * @return
	 */
	public static String getStr(String key,int dbIndex, JedisPool jedisPool) {

		String value = null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(dbIndex);
			value = jedis.get(key);
		} catch (Exception e) {
			release(jedisPool, jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			close(jedis, jedisPool);
		}

		return value;
	}
	//******************存储用字符串 end***************************************
	
	
	//********************set begin *************************************
	/**
	 * 【Set中装值】
	 * @param key
	 * @param value
	 * @param jedisPool
	 * @return  long
	 */
	public static Long sadd(String key, String value,int dbIndex, JedisPool jedisPool) {
		Long str = null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(dbIndex);
			str = jedis.sadd(key, value);

		} catch (Exception e) {
			release(jedisPool, jedis);
		} finally {
			// 返还到连接池
			close(jedis, jedisPool);
		}
		return str;
	}
	
	/**
	 * 删除set中值
	 * @param skey
	 * @param members
	 * @param dbIndex
	 * @param jedisPool
	 * @return
	 */
	public static Long srem(String skey, String[] members, int dbIndex, JedisPool jedisPool) {
		Long str = null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(dbIndex);
			str = jedis.srem(skey, members);

		} catch (Exception e) {
			release(jedisPool, jedis);
		} finally {
			// 返还到连接池
			close(jedis, jedisPool);
		}
		return str;
	}
	
	/**
	 * 【Set判断是否包含value值】
	 * @param key
	 * @param value
	 * @param jedisPool
	 * @return  boolean
	 */
	public static boolean sismember(String key, String value,int dbIndex, JedisPool jedisPool) {
		boolean str = false;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(dbIndex);
			str = jedis.sismember(key, value);

		} catch (Exception e) {
			release(jedisPool, jedis);
		} finally {
			// 返还到连接池
			close(jedis, jedisPool);
		}
		return str;
	}
	
	/**
	 * 【Set判断是否包含value值】
	 * @param key
	 * @param jedisPool
	 * @return  Set<String>
	 */
	public static Set<String> smembers(String key,int dbIndex, JedisPool jedisPool) {
		Set<String> strs = null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(dbIndex);
			strs = jedis.smembers(key);

		} catch (Exception e) {
			release(jedisPool, jedis);
		} finally {
			// 返还到连接池
			close(jedis, jedisPool);
		}
		return strs;
	}
	
	
	/**
	 * 【set key值A 中的value 移动到key值B中】
	 * @param key
	 * @param value
	 * @param jedisPool
	 * @return  Long
	 */
	public static Long smove(String key, String tokey, String value,int dbIndex, JedisPool jedisPool) {
		Long str = null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(dbIndex);
			str = jedis.smove(key, tokey, value);

		} catch (Exception e) {
			release(jedisPool, jedis);
		} finally {
			// 返还到连接池
			close(jedis, jedisPool);
		}
		return str;
	}
	
	//********************set end *************************************
	


	/**
	 * 【测试】
	 * 
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		//		List<String> list = new ArrayList<String>();
		//		list.add("a");
		//		list.add("b");
		//		list.add("c");
		//		try {
		//			//			JedisBaseDao.set(ObjectUtil.objectToBytes("MESS"), ObjectUtil.objectToBytes(list), OTHERWRITE);
		//			System.out.println(((List<String>) ObjectUtil.bytesToObject(JedisBaseDao.get(ObjectUtil.objectToBytes("MESS"), OTHERREAD))).size());
		//		} catch (Exception e) {
		//			e.printStackTrace();
		//		}

		/*set();
		get();*/
	}

	/*public static byte[] redisKey = "key".getBytes();
	static JedisPool jedisPool = new JedisPool();*/
	/*public static void get() throws Exception{
		byte[] bytes = rpop(redisKey,jedisPool);
		 Message msg = (Message) ObjectUtil.bytesToObject(bytes);
//		Message msg = RedisUtil.getInstance().getRedisOneMQ("key", Message.class);
		if(msg != null){
			System.out.println(msg.getId()+"   "+msg.getContent());
		}
	}

	public static void set() throws Exception{

		Message msg1 = new Message(1, "内容1");
//        RedisUtil.getInstance().setRedisMQ("key", msg1);

        lpush(redisKey, ObjectUtil.objectToBytes(msg1),jedisPool);
        Message msg2 = new Message(2, "内容2");
//        RedisUtil.getInstance().setRedisMQ("key", msg2);
        lpush(redisKey, ObjectUtil.objectToBytes(msg2),jedisPool);
        Message msg3 = new Message(3, "内容3");
//        RedisUtil.getInstance().setRedisMQ("key", msg3);
        lpush(redisKey, ObjectUtil.objectToBytes(msg3),jedisPool);

	}*/
	
	/*
	 * 根据标识 , 获取 redis 配置的某个 pool
	 */
	static Hashtable<String, JedisPool> allPool = new Hashtable<String, JedisPool>();
	public static JedisPool findJedisPool(String poolTag)
	{
		if( StringTool.isNull( poolTag ) )
			return null;
		JedisPool pool = allPool.get( poolTag );
		if( pool==null ){
			Config conf = Config.getInstance();
			String queueInfo = conf.getString("jedis."+poolTag, null);
			if( queueInfo != null && queueInfo.split(":").length>1 ){
				pool = initJedisPool(queueInfo.split(":")[0],Integer.valueOf(queueInfo.split(":")[1]));
				if( pool!=null ){
					allPool.put( poolTag , pool );
				}
			}
			else{
				//util.LogUtil.writeLog( poolTag +" not find.", "jedis" );
				Logger.error( "Jedis Pool "+poolTag +" not find !!!");
			}
		}
		return pool;
	}
	
	
	/**
	 * -------------------------------------2017年活动相关--------------------------------------------
	 */
	
	/**
	 * 增加hash值
	 * @param jedisPool
	 * @param dbIndex
	 * @param key
	 * @param hash
	 */
	public static void hmset( JedisPool jedisPool,int dbIndex, String key, Map<String, String> hash) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(dbIndex);
			jedis.hmset(key, hash);
		} catch (Exception e) {
			release(jedisPool, jedis);
			e.printStackTrace();
			logger.error("hmset【 key:"+key+",value:"+hash.toString()+"】", e);

		} finally {
			close(jedis, jedisPool);
		}
	}
	
	/**
	 * 往list中增加数据
	 * @param jedisPool
	 * @param dbIndex
	 * @param key
	 * @param value
	 * @return
	 */
	public static Long lpush(JedisPool jedisPool,int dbIndex, String key, String value) {
		Long lpushresust = 0l;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			 jedis.select(dbIndex);
			 lpushresust = jedis.lpush(key, value);
		} catch (Exception e) {
			release(jedisPool, jedis);
			logger.error("lpush【key:"+key +",value:"+value +"】",e);
		} finally {
			close(jedis, jedisPool);
		}
		return lpushresust;
	}
	
	/**
	 * 是否存在某个key
	 * @param jedisPool
	 * @param dbIndex
	 * @param key
	 * @return
	 */
	public static boolean exists(JedisPool jedisPool, int dbIndex,String key){
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(dbIndex);
			boolean bool = jedis.exists(key);
			return bool;
		} catch (Exception e) {
			release(jedisPool, jedis);
			logger.error("exists【key:"+key +"】",e);
		} finally {
			close(jedis, jedisPool);
		}
		return false;
	}
	
	/**
	 * 获取HSET数据
	 * @param jedisPool
	 * @param dbIndex
	 * @param key
	 * @param field
	 * @return
	 */
	public static String hget(JedisPool jedisPool, int dbIndex, String key, String field ) {

		String value = null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(dbIndex);
			value = jedis.hget(key, field);
		} catch (Exception e) {
			release(jedisPool, jedis);
			logger.error("hget【key:"+key +",value:"+value +"】",e);
		} finally {
			close(jedis, jedisPool);
		}
		return value;
	}
	
	/**
	 * 【Set中put值】
	 * @param jedisPool
	 * @param dbIndex
	 * @param key
	 * @param value
	 * @return
	 */
	public static Long sadd(JedisPool jedisPool, int dbIndex, String key, String value ) {
		Long str = null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(dbIndex);
			str = jedis.sadd(key, value);
		} catch (Exception e) {
			release(jedisPool, jedis);
			logger.error("sadd【key:"+key +",value:"+value +"】",e);
		} finally {
			close(jedis, jedisPool);
		}
		return str;
	}
	
	
	/**
	 * 【Set中put值】
	 * @param jedisPool
	 * @param dbIndex
	 * @param key
	 * @param value
	 * @return
	 */
	public static Set<String> smembers(JedisPool jedisPool, int dbIndex, String key ) {
		Set<String> str = null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(dbIndex);
			str = jedis.smembers(key);
		} catch (Exception e) {
			release(jedisPool, jedis);
			logger.error("sadd【key:"+key +"】",e);
		} finally {
			close(jedis, jedisPool);
		}
		return str;
	}
	
	/**
	 * 【Set中put值】
	 * @param jedisPool
	 * @param dbIndex
	 * @param key
	 * @param value
	 * @return
	 */
	public static Long scard(JedisPool jedisPool, int dbIndex, String key ) {
		Long str = null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(dbIndex);
			str = jedis.scard(key);
		} catch (Exception e) {
			release(jedisPool, jedis);
			logger.error("scard【key:"+key +"】",e);
		} finally {
			close(jedis, jedisPool);
		}
		return str;
	}
	
	/**
	 * 取set 交集 
	 * @param jedisPool
	 * @param dbIndex
	 * @param dstkey
	 * @param keys
	 * @param seconds
	 * @return
	 */
	public static Long sinterstore(JedisPool jedisPool, int dbIndex,String dstkey, String[] keys ,int seconds) {
		Long str = null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(dbIndex);
			str = jedis.sinterstore(dstkey, keys);
			if(seconds > 0){
				jedis.expire(dstkey, seconds);
			}
		} catch (Exception e) {
			release(jedisPool, jedis);
			logger.error("sinterstore【key:"+dstkey + ",keys:"+keys +",seconds:"+seconds+ "】",e);
		} finally {
			close(jedis, jedisPool);
		}
		return str;
	}
	
	
	/**
	 * 【ZSet中zincrby 分值】
	 * @param jedisPool
	 * @param dbIndex
	 * @param key
	 * @param score
	 * @param member
	 * @return
	 */
	public static Double zincrby(JedisPool jedisPool, int dbIndex, String key, double score, String member ) {
		double str = 0l;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(dbIndex);
			str = jedis.zincrby(key, score, member);
		} catch (Exception e) {
			release(jedisPool, jedis);
			logger.error("zincrby【key: "+key+",score: " + score +",member: "+member +"】",e);
		} finally {
			close(jedis, jedisPool);
		}
		return str;
	}
	
	/**
	 * 增加hset field分值
	 * @param jedisPool
	 * @param dbIndex
	 * @param key
	 * @param field
	 * @param value
	 * @return
	 */
	public static Long hincrBy(JedisPool jedisPool, int dbIndex, String key, String field, Long value ) {
		Long str = 0l;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(dbIndex);
			str = jedis.hincrBy(key, field, value);
		} catch (Exception e) {
			release(jedisPool, jedis);
			logger.error("hincrBy【key: "+key+",field: " + field +",value: "+value +"】",e);
		} finally {
			close(jedis, jedisPool);
		}
		return str;
	}
	
	
	/**
	 * 增加hset zrevrange
	 * @param jedisPool
	 * @param dbIndex
	 * @param key
	 * @param field
	 * @param value
	 * @return
	 */
	public static Set<String> zrevrange(JedisPool jedisPool, int dbIndex, String key, Long start, Long end ) {
		Set<String> str = null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(dbIndex);
			str = jedis.zrevrange(key, start, end);
		} catch (Exception e) {
			release(jedisPool, jedis);
			logger.error("zrevrange【key: "+key+",start: " + start +",end: "+ end +"】",e);
		} finally {
			// 返还到连接池
			close(jedis, jedisPool);
		}
		return str;
	}
	
	
	/**
	 * 增加zsort zrevrank 排名
	 * @param jedisPool
	 * @param dbIndex
	 * @param key
	 * @param member
	 * @return
	 */
	public static Long zrevrank(JedisPool jedisPool, int dbIndex, String key, String member) {
		Long str = null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(dbIndex);
			str = jedis.zrevrank(key, member);
		} catch (Exception e) {
			release(jedisPool, jedis);
			logger.error("zrevrank【key: "+key+",member: " + member +"】",e);
		} finally {
			// 返还到连接池
			close(jedis, jedisPool);
		}
		return str;
	}
	
	/**
	 * 返回zsorce 分值
	 * @param jedisPool
	 * @param dbIndex
	 * @param key
	 * @param member
	 * @return
	 */
	public static Double zscore(JedisPool jedisPool, int dbIndex, String key, String member) {
		Double str = null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(dbIndex);
			str = jedis.zscore(key, member);
		} catch (Exception e) {
			release(jedisPool, jedis);
			logger.error("zscore【key: "+key+",member: " + member +"】",e);
		} finally {
			// 返还到连接池
			close(jedis, jedisPool);
		}
		return str;
	}
	
	
	/**
	 * 返回zcard 个数
	 * @param jedisPool
	 * @param dbIndex
	 * @param key
	 * @param member
	 * @return
	 */
	public static Long zcard(JedisPool jedisPool, int dbIndex, String key) {
		Long str = null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(dbIndex);
			str = jedis.zcard(key);
		} catch (Exception e) {
			release(jedisPool, jedis);
			logger.error("zcard【key: "+key+"】",e);
		} finally {
			// 返还到连接池
			close(jedis, jedisPool);
		}
		return str;
	}
	
	
	/**
	 * 获取hset中所有的值
	 * @param jedisPool
	 * @param dbIndex
	 * @param key
	 * @return
	 */
	public static Map<String, String> hgetAll(JedisPool jedisPool, int dbIndex, String key) {
		Map<String, String> result = null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(dbIndex);
			result = jedis.hgetAll(key);
		} catch (Exception e) {
			release(jedisPool, jedis);
			logger.error("hgetAll【key: "+ key+ "】",e);

		} finally {
			close(jedis, jedisPool);
		}
		return result;
	}
	
	
	/**
	 * 按数字增加数值
	 * @param key
	 * @param dbIndex
	 * @param  number
	 * @param jedisPool
	 * @return
	 */
	public static Long IncrByNum(JedisPool jedisPool, int dbIndex, String key, long number){
		Long ret = null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(dbIndex);
			ret = jedis.incrBy(key, number);

		} catch (Exception e) {
			release(jedisPool, jedis);
			logger.error("IncrByNum【key: "+ key+ ",number: "+number+ "】",e);
		} finally {
			// 返还到连接池
			close(jedis, jedisPool);
		}
		return ret;
	}
	
	/**
	 * 自增++
	 * @param key
	 * @param dbIndex
	 * @param  number
	 * @param jedisPool
	 * @return
	 */
	public static Long Incr(JedisPool jedisPool, int dbIndex, String key){
		Long ret = null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(dbIndex);
			ret = jedis.incr(key);

		} catch (Exception e) {
			release(jedisPool, jedis);
			logger.error("Incr【key: "+ key+ "】",e);
		} finally {
			// 返还到连接池
			close(jedis, jedisPool);
		}
		return ret;
	}
	
	/**
	 * 阻塞队列,取数
	 * @param jedisPool
	 * @param dbIndex
	 * @param time
	 * @param keys
	 * @return
	 */
	public static List<String> brpop(JedisPool jedisPool, int dbIndex, int time ,String[] keys ){
		List<String> ret = null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(dbIndex);
			ret = jedis.brpop(time, keys);

		} catch (Exception e) {
			release(jedisPool, jedis);
			logger.error("brpop【key: "+ keys+ ",time:"+ time +"】",e);
		} finally {
			// 返还到连接池
			close(jedis, jedisPool);
		}
		return ret;
	}
	
	
	/**
	 * 获取list的个数
	 * @param key
	 * @param jedisPool
	 * @return
	 */
	public static long llen(JedisPool jedisPool, int dbIndex, String key) {
		long len = 0;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(dbIndex);
			len = jedis.llen(key);
		} catch (Exception e) {
			release(jedisPool, jedis);
			e.printStackTrace();
		} finally {
			// 返还到连接池
			close(jedis, jedisPool);
		}
		return len;
	}
	
	/**
	 * 按数字增加数值
	 * @param key
	 * @param dbIndex
	 * @param  number
	 * @param jedisPool
	 * @return
	 */
	public static Long IncrNum( String key,int number,int dbIndex, JedisPool jedisPool ,int timeout){
		Long ret = null;
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.select(dbIndex);
			ret = jedis.incrBy(key, number);
			
			jedis.expire(key, timeout);

		} catch (Exception e) {
			release(jedisPool, jedis);
		} finally {
			// 返还到连接池
			close(jedis, jedisPool);
		}
		return ret;
	}
	
	public static void test (JedisPool jedisPool) throws Exception{
		Jedis	jedis = jedisPool.getResource();
		
		jedis.incr("hello");
		
	}
}
