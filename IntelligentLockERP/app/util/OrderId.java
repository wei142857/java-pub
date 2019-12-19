package util;

import static util.Assertions.isTrueArgument;
import static util.Assertions.notNull;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class OrderId implements Comparable<OrderId>, Serializable{
	private static final long serialVersionUID = -3910521205013439736L;

	private static final AtomicInteger NEXT_COUNTER = new AtomicInteger(new SecureRandom().nextInt());

	private static final int LOW_ORDER_THREE_BYTES = 0x00ffffff;

	private static final char[] HEX_CHARS = new char[] {
	      '0', '1', '2', '3', '4', '5', '6', '7',
	      '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	
	private final int timestamp;

	private final int counter;

	public OrderId() {
		this(new Date());
	}


	public OrderId(final Date date) {
		this(dateToTimestampSeconds(date), NEXT_COUNTER.getAndIncrement(), false);
	}
	private static int dateToTimestampSeconds(final Date time) {
		return (int) (time.getTime() / 1000);
	}

	private OrderId(final int timestamp,  final int counter, final boolean checkCounter) {
		this.timestamp = timestamp;
		this.counter = counter & LOW_ORDER_THREE_BYTES;
	}

	public void putToByteBuffer(final ByteBuffer buffer) {
		notNull("buffer", buffer);
		isTrueArgument("buffer.remaining() >=6", buffer.remaining() >= 6);

		buffer.put(int3(timestamp));
		buffer.put(int2(timestamp));
		buffer.put(int1(timestamp));
		buffer.put(int0(timestamp));
		buffer.put(int1(counter));
		buffer.put(int0(counter));
	}


	public byte[] toByteArray() {
		ByteBuffer buffer = ByteBuffer.allocate(6);
		putToByteBuffer(buffer);
		return buffer.array();  // using .allocate ensures there is a backing array that can be returned
	}

	private static byte int3(final int x) {
		return (byte) (x >> 24);
	}

	private static byte int2(final int x) {
		return (byte) (x >> 16);
	}

	private static byte int1(final int x) {
		return (byte) (x >> 8);
	}

	private static byte int0(final int x) {
		return (byte) (x);
	}

	@Override
	public int compareTo(final OrderId other) {
		if (other == null) {
			throw new NullPointerException();
		}

		byte[] byteArray = toByteArray();
		byte[] otherByteArray = other.toByteArray();
		for (int i = 0; i < 6; i++) {
			if (byteArray[i] != otherByteArray[i]) {
				return ((byteArray[i] & 0xff) < (otherByteArray[i] & 0xff)) ? -1 : 1;
			}
		}
		return 0;
	}
	
	@Override
    public String toString() {
        return toHexString();
    }
	
	
	 public static OrderId get() {
	        return new OrderId();
	    }
	 
	 public String toHexString() {
	      char[] chars = new char[12];
	      int i = 0;
	      for (byte b : toByteArray()) {
	        chars[i++] = HEX_CHARS[b >> 4 & 0xF];
	        chars[i++] = HEX_CHARS[b & 0xF];
	      }
	      return new String(chars);
	    }
	 
	 public OrderId(final String hexString) {
	        this(parseHexString(hexString));
	    }
	 
	  private static byte[] parseHexString(final String s) {
	        if (!isValid(s)) {
	            throw new IllegalArgumentException("invalid hexadecimal representation of an ObjectId: [" + s + "]");
	        }

	        byte[] b = new byte[6];
	        for (int i = 0; i < b.length; i++) {
	            b[i] = (byte) Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16);
	        }
	        return b;
	    }
	  
	  public static boolean isValid(final String hexString) {
	        if (hexString == null) {
	            throw new IllegalArgumentException();
	        }

	        int len = hexString.length();
	        if (len != 12) {
	            return false;
	        }

	        for (int i = 0; i < len; i++) {
	            char c = hexString.charAt(i);
	            if (c >= '0' && c <= '9') {
	                continue;
	            }
	            if (c >= 'a' && c <= 'f') {
	                continue;
	            }
	            if (c >= 'A' && c <= 'F') {
	                continue;
	            }

	            return false;
	        }

	        return true;
	    }

	 
	 public OrderId(final byte[] bytes) {
	        this(ByteBuffer.wrap(notNull("bytes", bytes)));
	    }
	 
	  public OrderId(final ByteBuffer buffer) {
	        notNull("buffer", buffer);
	        isTrueArgument("buffer.remaining() >=6", buffer.remaining() >= 6);

	        // Note: Cannot use ByteBuffer.getInt because it depends on tbe buffer's byte order
	        // and ObjectId's are always in big-endian order.
	        timestamp = makeInt(buffer.get(), buffer.get(), buffer.get(), buffer.get());
	        counter = makeInt((byte) 0, (byte) 0, buffer.get(), buffer.get());
	    }
	  
	  private static int makeInt(final byte b3, final byte b2, final byte b1, final byte b0) {
	        // CHECKSTYLE:OFF
	        return (((b3) << 24) |
	                ((b2 & 0xff) << 16) |
	                ((b1 & 0xff) << 8) |
	                ((b0 & 0xff)));
	        // CHECKSTYLE:ON
	    }
	  
	  public int getCounter() {
	        return counter;
	    }
	  
	  public Date getDate() {
	        return new Date(timestamp * 1000L);
	    }
	
	public static void main(String[] args) {
		
		for (int i = 0; i < 10000; i++) {
			System.out.println(OrderId.get().toString());
		}	
//		5930d0468c0c
//		5930d0468c0d
		OrderId orderId = new OrderId("5930d0468c0c");
		System.out.println(StringUtil.getDateTimeStr(orderId.getDate()));
		System.out.println(orderId.getCounter());
		
		OrderId orderId2 = new OrderId("5930d0468c0d");
		System.out.println(StringUtil.getDateTimeStr(orderId2.getDate()));
		System.out.println(orderId2.getCounter());
	}

}
