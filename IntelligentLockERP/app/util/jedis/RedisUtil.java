package util.jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import play.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import util.json.JsonUtil;

public class RedisUtil {
	private static final Logger.ALogger logger = Logger.of(RedisUtil.class);

	private static RedisUtil instance = null;

	public static RedisUtil getInstance() {
		if (instance == null)
			instance = new RedisUtil();
		return instance;
	}

	public void setEntityStr(String key, Object obj) {
		setEntityStr(key, obj, 0, JedisBaseDao.getDefaultDb());
	}

	public void setEntityStr(String key, Object obj, int dbindex) {
		setEntityStr(key, obj, 0, dbindex);
	}

	public void setEntityStr(String key, Object obj, int time, int dbIndex) {
		try {
			JedisBaseDao.setStr(key, JsonUtil.parseObj(obj), time, dbIndex,
					getWritePool(key));
		} catch (Exception e) {
			logger.error(
					"setRedis time ERROR[key:" + key + ",obj:" + obj.toString()
							+ "]", e);
		}

	}

	public void setEntity(String key, Object obj, int time) {
		setEntity(key, obj, time, JedisBaseDao.getDefaultDb());
	}
	
	public void setEntity(String key, Object obj) {
		setEntity(key, obj, 0, JedisBaseDao.getDefaultDb());
	}

	public void setEntity(String key, Object obj, int time, int dbIndex) {
		try {
			JedisBaseDao.set(key.getBytes(), ObjectUtil.objectToBytes(obj),
					time, dbIndex, getWritePool(key));
		} catch (Exception e) {
			logger.error(
					"setRedis time ERROR[key:" + key + ",obj:" + obj.toString()
							+ "]", e);
		}
	}

	public String setNum(String key, Integer num, int time, int dbIndex) {
		try {
			return JedisBaseDao.setStr(key, num.toString(), time, dbIndex,
					getWritePool(key));
		} catch (Exception e) {
			logger.error(
					"setRedis time ERROR[key:" + key + ",obj:" + num.toString()
							+ "]", e);
		}
		return "";
	}

	/**
	 * 递减数字
	 * 
	 * @param key
	 * @param dbIndex
	 * @return
	 */
	public Long DecrNum(String key, int dbIndex) {
		try {
			return JedisBaseDao.DecrNum(key, dbIndex, getWritePool(key));
		} catch (Exception e) {
			logger.error(
					"setRedis time ERROR[key:" + key + ",obj:" + key + "]", e);
		}
		return null;
	}

	/**
	 * 减数字 number
	 * 
	 * @param key
	 * @param number
	 * @param dbIndex
	 * @return
	 */
	public Long DecrNum(String key, int number, int dbIndex) {
		try {
			return JedisBaseDao
					.DecrNum(key, number, dbIndex, getWritePool(key));
		} catch (Exception e) {
			logger.error(
					"setRedis time ERROR[key:" + key + ",obj:" + key + "]", e);
		}
		return null;
	}

	public Long DecrNum(String key, int number, int time, int dbIndex) {
		try {
			return JedisBaseDao.DecrNum(key, number, time, dbIndex,
					getWritePool(key));
		} catch (Exception e) {
			Logger.error(
					"setRedis time ERROR[key:" + key + ",obj:" + key + "]", e);
		}
		return null;
	}

	/**
	 * 递增数字
	 * 
	 * @param key
	 * @param dbIndex
	 * @return
	 */
	public Long IncrNum(String key, int dbIndex) {
		try {
			return JedisBaseDao.IncrNum(key, dbIndex, getWritePool(key));
		} catch (Exception e) {
			logger.error(
					"setRedis time ERROR[key:" + key + ",obj:" + key + "]", e);
		}
		return null;
	}

	/**
	 * 递增数字
	 * 
	 * @param key
	 * @param dbIndex
	 * @return
	 */
	public Long IncrNum(String key, int number, int dbIndex) {
		try {
			return JedisBaseDao
					.IncrNum(key, number, dbIndex, getWritePool(key));
		} catch (Exception e) {
			logger.error(
					"setRedis time ERROR[key:" + key + ",obj:" + key + "]", e);
		}
		return null;
	}

	/**
	 * 递增数字
	 * 
	 * @param key
	 * @param dbIndex
	 * @return
	 */
	public Long IncrNum(String key, int number, int time, int dbIndex) {
		try {
			return JedisBaseDao.IncrNum(key, number, time, dbIndex,
					getWritePool(key));
		} catch (Exception e) {
			Logger.error(
					"setRedis time ERROR[key:" + key + ",obj:" + key + "]", e);
		}
		return null;
	}

	public <T> T getEntityStr(String key, Class<T> clazz) {

		return getEntityStr(key, clazz, JedisBaseDao.getDefaultDb());
	}

	public <T> T getEntityStr(String key, Class<T> clazz, int dbIndex) {
		T instance = null;
		try {
			instance = JsonUtil.parseJson(
					JedisBaseDao.getStr(key, dbIndex, getReadPool(key)), clazz);

		} catch (Exception e) {
			logger.error(
					"getRedis ERROR[key:" + key + ",obj:" + clazz.getName()
							+ "]", e);
		}
		return instance;
	}

	public <T> T getEntity(String key, Class<T> clazz) {
		return getEntity(key, clazz, JedisBaseDao.getDefaultDb());
	}

	@SuppressWarnings("unchecked")
	public <T> T getEntity(String key, Class<T> clazz, int dbIndex) {
		T instance = null;
		try {
			instance = (T) ObjectUtil.bytesToObject(JedisBaseDao.get(
					key.getBytes(), dbIndex, getReadPool(key)));
		} catch (Exception e) {
			logger.error(
					"getRedis ERROR[key:" + key + ",obj:" + clazz.getName()
							+ "]", e);
		}
		return instance;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> getEntityToList(String key, Class<T> clazz, int dbIndex) {
		List<T> instance = null;
		try {
			instance = (List<T>) ObjectUtil.bytesToObject(JedisBaseDao.get(
					key.getBytes(), dbIndex, getReadPool(key)));
		} catch (Exception e) {
			logger.error(
					"getRedistolist ERROR[key:" + key + ",obj:"
							+ clazz.getName() + "]", e);
		}
		return instance;
	}

	public <T> List<T> getEntityToList(String key, Class<T> clazz) {
		return getEntityToList(key, clazz, JedisBaseDao.getDefaultDb());
	}

	public <T> List<T> getEntityStrToList(String key, Class<T> clazz,int dbIndex) {
		List<T> instance = null;
		try {
			
			instance = JsonUtil.parseJson2List(
					JedisBaseDao.getStr(key, dbIndex, getReadPool(key)), clazz);

		} catch (Exception e) {
			Logger.error(
					"getRedistolist ERROR[key:" + key + ",obj:"
							+ clazz.getName() + "]", e);
		}
		return instance;
	}


	public void deleteEntity(String key) {
		deleteEntity(key, JedisBaseDao.getDefaultDb());
	}

	public void deleteEntity(String key, int dbIndex) {
		try {
			JedisBaseDao.del(key.getBytes(), dbIndex, getWritePool(key));
		} catch (Exception e) {
			logger.error("deleteEntity[key:" + key + "]");
			e.printStackTrace();
		}
	}
	
	public void deleteEntityStr(String key) {
		deleteEntityStr(key, JedisBaseDao.getDefaultDb());
	}

	public void deleteEntityStr(String key, int dbIndex) {
		try {
			JedisBaseDao.delStr(key, dbIndex, getWritePool(key));
		} catch (Exception e) {
			logger.error("deleteEntity[key:" + key + "]");
			e.printStackTrace();
		}
	}

	public JedisPool getWritePool(String key) {
		JedisPool jedispool = null;
		int hashcode = 0;
		char[] c1 = key.toCharArray();
		for (int i = 0; i < c1.length; i++) {
			hashcode += (int) c1[i];
		}
		int idx = hashcode % JedisBaseDao.writers.length;
		// if (hashcode % 2 == 0)
		{
			jedispool = JedisBaseDao.writers[idx];
			if (jedispool == null) {
				util.LogUtil.writeLog("DEFALULTWRITE - server down - " + idx,
						"redis_server_statu");
			}
		}
		/*
		 * else{ jedispool = JedisBaseDao.OTHERWRITE; if(jedispool == null){
		 * util.LogUtil.writeLog( "OTHERWRITE - server down",
		 * "redis_server_statu"); } }
		 */
		return jedispool;

	}

	// 队列的queue pool
	public JedisPool getQueuePool() {
		JedisPool jedispool = null;

		jedispool = JedisBaseDao.writers[0];
		if (JedisBaseDao.DEFALULTQueue != null)
			jedispool = JedisBaseDao.DEFALULTQueue;

		return jedispool;

	}

	public JedisPool getReadPool(String key) {
		JedisPool jedispool = null;
		int hashcode = 0;
		char[] c1 = key.toCharArray();
		for (int i = 0; i < c1.length; i++) {
			hashcode += (int) c1[i];
		}

		int idx = hashcode % JedisBaseDao.readers.length;

		// if (hashcode % 2 == 0)
		{
			jedispool = JedisBaseDao.readers[idx];
			if (jedispool == null) {
				util.LogUtil.writeLog("readers - server down - " + idx,
						"redis_server_statu");
			}
		}
		/*
		 * else{ jedispool = JedisBaseDao.OTHERREAD; if(jedispool == null){
		 * util.LogUtil.writeLog( "OTHERREAD - server down",
		 * "redis_server_statu"); } }
		 */
		return jedispool;
	}

	/**
	 * 【生产者生产消息往redis队列里放入消息】 【将一个或多个值 value 插入到列表 key 的表头】
	 * 
	 * @param message_queue_key
	 * @param value
	 */
	public void setRedisMQ(String message_queue_key, Object value) {
		setRedisMQ(message_queue_key, value, JedisBaseDao.getDefaultDb());
	}

	/**
	 * 【生产者生产消息往redis队列里放入消息】 【将一个或多个值 value 插入到列表 key 的表头】
	 * 
	 * @param message_queue_key
	 * @param value
	 */
	private void setRedisMQ(String message_queue_key, Object value, int dbIndex) {
		if (value != null) {
			try {
				JedisBaseDao.lpush(message_queue_key.getBytes(),
						ObjectUtil.objectToBytes(value), dbIndex,
						getQueuePool());
			} catch (Exception e) {
				logger.error("setRedisMQ ERROR[key:" + message_queue_key
						+ ",obj:" + value.toString() + "]", e);
			}
		}
	}

	/**
	 * 【消费者去获取redis某Key队列里的一条尾元素】 【移除并返回列表 key 的尾元素】
	 * 
	 * @param message_queue_key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private <T> T getRedisOneMQ(String message_queue_key, Class<T> clazz,
			int dbIndex) {
		try {
			return (T) ObjectUtil.bytesToObject(JedisBaseDao.rpop(
					message_queue_key.getBytes(), dbIndex, getQueuePool()));
		} catch (Exception e) {
			logger.error("setRedisMQ ERROR[key:" + message_queue_key + ",obj:"
					+ clazz.getName() + "]", e);
		}
		return null;
	}

	/**
	 * 【消费者去获取redis某Key队列里的一条尾元素】 【移除并返回列表 key 的尾元素】
	 * 
	 * @param message_queue_key
	 * @return
	 */
	public <T> T getRedisOneMQ(String message_queue_key, Class<T> clazz) {
		return getRedisOneMQ(message_queue_key, clazz,
				JedisBaseDao.getDefaultDb());
	}

	/**
	 * 【消费者去获取redis某Key队列里所有元素】 【移除并返回列表 key 的所有元素】
	 * 
	 * @param message_queue_key
	 * @return
	 */
	public <T> List<T> getRedisListMQ(String message_queue_key, Class<T> clazz) {
		return getRedisListMQ(message_queue_key, clazz,
				JedisBaseDao.getDefaultDb());
	}

	/**
	 * 【消费者去获取redis某Key队列里所有元素】 【移除并返回列表 key 的所有元素】
	 * 
	 * @param message_queue_key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private <T> List<T> getRedisListMQ(String message_queue_key,
			Class<T> clazz, int dbIndex) {
		List<T> list = new ArrayList<T>();
		try {
			List<byte[]> temp = JedisBaseDao.lpopList(
					message_queue_key.getBytes(), dbIndex, getQueuePool());
			for (int i = 0; i < temp.size(); i++) {
				list.add((T) ObjectUtil.bytesToObject(temp.get(i)));
			}
			return list;
		} catch (Exception e) {
			logger.error("setRedisMQ ERROR[key:" + message_queue_key + ",obj:"
					+ clazz.getName() + "]", e);
		}
		return null;
	}

	/**
	 * 是否存在某个key
	 * 
	 * @param key
	 * @return
	 */
	public boolean exists(String key) {
		return JedisBaseDao.exists(key, RedisUtil.getInstance()
				.getReadPool(key));
	}

	/**
	 * 是否存在某个key
	 * 
	 * @param key
	 * @return
	 */
	public boolean exists(String key, int dbindex) {
		return JedisBaseDao.exists(key, dbindex, RedisUtil.getInstance()
				.getReadPool(key));
	}

	/**
	 * 根据模糊值，查询出所有的key
	 * 
	 * @param pattern
	 * @return
	 */
	/*
	 * public Set<String> getkeysBypattern(String pattern){ Set<String> strSet =
	 * null; Set<String> strSet1 = null;
	 * 
	 * strSet = JedisBaseDao.keys(pattern, 0,JedisBaseDao.DEFALULTWRITE);
	 * strSet1 = JedisBaseDao.keys(pattern, 0, JedisBaseDao.OTHERWRITE);
	 * strSet.addAll(strSet1); return strSet; }
	 */

	/**
	 * 向list中插入数据
	 * 
	 * @param key
	 * @param value
	 * @param dbIndex
	 */
	public void lpush(String key, Object value, int dbIndex, int time) {
		if (value != null) {
			try {
				JedisBaseDao.lpush(key, JsonUtil.parseObj(value), dbIndex,
						getWritePool(key), time);
			} catch (Exception e) {
				logger.error(
						"lpush ERROR[key:" + key + ",obj:" + value.toString()
								+ "]", e);
			}
		}
	}

	/**
	 * 根据key值，获取list中所有的值
	 * 
	 * @param key
	 * @param dbIndex
	 * @return
	 */
	public List<String> lrange(String key, int dbIndex, Long start, Long end) {
		List<String> list = null;
		try {
			list = JedisBaseDao.lrange(key, start, end, dbIndex,
					getReadPool(key));
		} catch (Exception e) {
			logger.error("lrange ERROR[key:" + key + "]", e);
		}
		return list;
	}

	/**
	 * 根据key值，获取list中所有的值
	 * 
	 * @param key
	 * @param dbIndex
	 * @return
	 */
	public long lrem(String key, int dbIndex, Long count, String value) {
		long len = 0;
		try {
			len = JedisBaseDao.lrem(key, count, value, dbIndex,
					getWritePool(key));
		} catch (Exception e) {
			logger.error("lrange ERROR[key:" + key + "]", e);
		}
		return len;
	}

	/**
	 * 根据key值，获取list中所有的值
	 * 
	 * @param key
	 * @param dbIndex
	 * @return
	 */
	public long llen(String key, int dbIndex) {
		long len = 0;
		try {
			len = JedisBaseDao.llen(key, dbIndex, getReadPool(key));
		} catch (Exception e) {
			logger.error("lrange ERROR[key:" + key + "]", e);
		}
		return len;
	}

	/**
	 * set插入值
	 * 
	 * @param key
	 * @param value
	 * @param dbIndex
	 * @return
	 */
	public Long sadd(String key, String value, int dbIndex) {
		return JedisBaseDao.sadd(key, value, dbIndex, getWritePool(key));
	}

	/**
	 * 删除set中的value
	 * 
	 * @param skey
	 * @param members
	 * @param dbIndex
	 * @return
	 */
	public Long srem(String skey, String[] members, int dbIndex) {
		return JedisBaseDao.srem(skey, members, dbIndex, getWritePool(skey));
	}

	/**
	 * 判断set中是否含有value值
	 * 
	 * @param key
	 * @param value
	 * @param dbIndex
	 * @return
	 */
	public boolean sismember(String key, String value, int dbIndex) {

		return JedisBaseDao.sismember(key, value, dbIndex, getWritePool(key));
	}

	/**
	 * 获取set中所有的值
	 * 
	 * @param key
	 * @param dbIndex
	 * @return
	 */
	public Set<String> smembers(String key, int dbIndex) {
		return JedisBaseDao.smembers(key, dbIndex, getWritePool(key));
	}

	/**
	 * set key中的value值 move到tokey中，这个针对本项目好像不好用 ,因为是多个redis，移过去可能会获取不到
	 * 
	 * @param key
	 * @param tokey
	 * @param value
	 * @param dbIndex
	 * @return
	 */
	public Long smove(String key, String tokey, String value, int dbIndex) {
		return JedisBaseDao
				.smove(key, tokey, value, dbIndex, getWritePool(key));
	}

	public static void main(String args[]) {

		// ###################redis队列测试################

		// 生产者
		/*
		 * long num1 = System.currentTimeMillis(); try { for (int i = 0; i <
		 * 100000; i++) {
		 * RedisUtil.getInstance().setRedisMQ("message_queue","Message"); } }
		 * catch (Exception e) { e.printStackTrace(); } long num2 =
		 * System.currentTimeMillis(); System.out.println("生产10万条：" + (num2 -
		 * num1) + "毫秒"); // 消费者取一条元素 String str =
		 * RedisUtil.getInstance().getRedisOneMQ("message_queue",String.class);
		 * System.out.println(str);
		 * 
		 * // 消费者取全部元素 long num3 = System.currentTimeMillis(); List<String> list
		 * =
		 * RedisUtil.getInstance().getRedisListMQ("message_queue",String.class);
		 * for (int i = 0; i < list.size(); i++) { System.out.println(i + ":" +
		 * list.get(i).toString()); } long num4 = System.currentTimeMillis();
		 * System.out.println("消费10万条：" + (num4 - num3) + "毫秒");
		 */

		// ###################redis队列测试################
	}

	/**************************************************************
	 * 新增的队列操作，指定使用的队列queue所在的redis： 目标，指定某些需要快速处理的队列，独享redis, 且指定DB到4
	 * 注意：如果进入队列的时候指定了redisTag，取的时候也要指定该redisTag
	 */
	static int Specail_QueueDB = 4;

	public void setRedisMQ(String message_queue_key, Object value,
			String redisTag) {
		setRedisMQ(message_queue_key, value, Specail_QueueDB, redisTag);
	}

	private void setRedisMQ(String message_queue_key, Object value,
			int dbIndex, String redisTag) {
		if (value != null) {
			try {
				JedisBaseDao.lpush(message_queue_key.getBytes(),
						ObjectUtil.objectToBytes(value), dbIndex,
						getQueuePool(redisTag));
			} catch (Exception e) {
				logger.error("setRedisMQ ERROR[key:" + message_queue_key
						+ ",obj:" + value.toString() + "]", e);
			}
		}
	}

	public void setRedisMQStr(String message_queue_key, Object value,
			String redisTag, int time) {
		if (value != null) {
			try {
				JedisBaseDao.lpush(message_queue_key, JsonUtil.parseObj(value),
						Specail_QueueDB, getQueuePool(redisTag), 0);
			} catch (Exception e) {
				Logger.error("setRedisMQ ERROR[key:" + message_queue_key
						+ ",obj:" + value.toString() + "]", e);
			}
		}
	}

	public <T> T getRedisOneMQ(String message_queue_key, Class<T> clazz,
			String jedisTag) {
		return getRedisOneMQ(message_queue_key, clazz, Specail_QueueDB,
				jedisTag);
	}

	@SuppressWarnings("unchecked")
	private <T> T getRedisOneMQ(String message_queue_key, Class<T> clazz,
			int dbIndex, String jedisTag) {
		try {
			return (T) ObjectUtil.bytesToObject(JedisBaseDao.rpop(
					message_queue_key.getBytes(), dbIndex,
					getQueuePool(jedisTag)));
		} catch (Exception e) {
			logger.error("setRedisMQ ERROR[key:" + message_queue_key + ",obj:"
					+ clazz.getName() + "]", e);
		}
		return null;
	}

	public <T> List<T> getRedisListMQ(String message_queue_key, Class<T> clazz,
			String jedisTag) {
		return getRedisListMQ(message_queue_key, clazz, Specail_QueueDB,
				jedisTag);
	}

	@SuppressWarnings("unchecked")
	private <T> List<T> getRedisListMQ(String message_queue_key,
			Class<T> clazz, int dbIndex, String jedisTag) {
		List<T> list = new ArrayList<T>();
		try {
			List<byte[]> temp = JedisBaseDao.lpopList(
					message_queue_key.getBytes(), dbIndex,
					getQueuePool(jedisTag));
			for (int i = 0; i < temp.size(); i++) {
				list.add((T) ObjectUtil.bytesToObject(temp.get(i)));
			}
			return list;
		} catch (Exception e) {
			logger.error("setRedisMQ ERROR[key:" + message_queue_key + ",obj:"
					+ clazz.getName() + "]", e);
		}
		return null;
	}

	// 指定获取 队列的 jedis pool
	public JedisPool getQueuePool(String pool) {
		JedisPool jedispool = null;
		jedispool = JedisBaseDao.writers[0];
		if (JedisBaseDao.DEFALULTQueue != null)
			jedispool = JedisBaseDao.DEFALULTQueue;
		if (JedisBaseDao.findJedisPool(pool) != null)
			jedispool = JedisBaseDao.findJedisPool(pool);

		return jedispool;
	}

	/***************************************************
	 * 获取一个 jedis对象，不做释放的使用,主要用于在 取队列的线程
	 */
	public Jedis getQueueRedis(String pool) {
		JedisPool jedisPool = getQueuePool(pool);
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
		} catch (Exception e) {
			Logger.error("!! getQueueRedis fail", e);
		}
		return jedis;
	}

	public <T> T getRedisOneMQ(String message_queue_key, Class<T> clazz,
			Jedis jedis) {
		return getRedisOneMQ(message_queue_key, clazz, Specail_QueueDB, jedis);
	}

	@SuppressWarnings("unchecked")
	private <T> T getRedisOneMQ(String message_queue_key, Class<T> clazz,
			int dbIndex, Jedis jedis) {
		try {
			return (T) ObjectUtil.bytesToObject(JedisBaseDao.rpop(
					message_queue_key.getBytes(), dbIndex, jedis));
		} catch (Exception e) {
			logger.error("getRedisOneMQ ERROR[key:" + message_queue_key
					+ ",obj:" + clazz.getName() + "]", e);
		}
		return null;
	}

	/**
	 * 【消费者去获取redis某Key队列里的一条尾元素】 【移除并返回列表 key 的尾元素】
	 * 
	 * @param key
	 * @return
	 */
	public <T> T rpop(String key, Class<T> clazz, int dbIndex) {
		T instance = null;
		try {

			instance = JsonUtil.parseJson(
					JedisBaseDao.rpop(key, dbIndex, getReadPool(key)), clazz);
		} catch (Exception e) {
			Logger.error(
					"getRedisOneJson ERROR[key:" + key + ",obj:"
							+ clazz.getName() + "]", e);
		}
		return instance;
	}

	/**
	 * 阻塞取数据
	 * 
	 * @param key
	 * @return
	 */
	public List<String> brpop(String[] keys, int dbIndex) {
		List<String> brpop = null;
		try {
			brpop = JedisBaseDao.brpop(getQueuePool(), dbIndex, 0, keys);
		} catch (Exception e) {
			Logger.error("brpop ERROR[keys:" + keys + "]", e);
		}
		return brpop;
	}
	
}
