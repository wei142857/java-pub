package util;

import java.util.concurrent.atomic.AtomicLong;

@SuppressWarnings("all")
public class RuleNumberFormatUtil {
	
	private static final byte[] lock = new byte[0];
	
	private static AtomicLong atomicnumber = new AtomicLong(100000000);
	// 位数，默认是8位
	private final static long w = 100000000;
 
	public static String createID() {
		long r = 0;
		synchronized (lock) {
			r = (long) ((Math.random() + 1) * w);
		}
		return System.currentTimeMillis() + String.valueOf(r).substring(1);
	}
	/**
	 * 	获取具有保证数据原子性且安全的long类型8位数递增数字
	 * @return 返回数字的同时 数字增1 
	 */
	public static long getAtomicnumber() {
		return atomicnumber.getAndIncrement();
	}
	/**
	 *	 通过CAS算法保证数据安全
	 * 	 非同步非阻塞 获取数字可以忽略ABA问题
	 * @return 将数字增1然后将其返回
	 */
	public static long getAtomicnumberByCAS() {
		for(;;) {
			if(atomicnumber.compareAndSet(atomicnumber.get(), atomicnumber.get()+1));
				return atomicnumber.get();
		}
	}
}
