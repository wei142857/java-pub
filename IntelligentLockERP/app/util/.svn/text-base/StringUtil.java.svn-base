package util;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;

import play.mvc.Http.Request;
import util.classEntity.StringTool;
import Service.cache.ICacheService;

public class StringUtil {

	/**
	 * 提供精确的乘法运算。
	 * 
	 * @param v1
	 *            被乘数
	 * @param v2
	 *            乘数
	 * @return 两个参数的积
	 */
	public static int mul(String v1, String v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.multiply(b2).intValue();
	}

	// 字符串 => 数字
	public static boolean isNull(String s) {
		if (s != null && s != "" && !s.equals("") && !s.equals(null)
				&& !s.equals("null")) {
			return false;
		} else {
			return true;
		}
	}

	// 字符串 => 数字
	public static int GetInt(String s, int defaultV) {
		int ret = defaultV;
		try {
			ret = Integer.parseInt(s);
		} catch (Exception e) {
			return defaultV;
		}
		return ret;
	}

	public static float GetFloat(String s, float defaultV) {
		float ret = defaultV;
		try {
			ret = Float.parseFloat(s);
		} catch (Exception e) {
			return defaultV;
		}
		return ret;
	}

	public static String getHttpParam(Request req, String key) {
		Map<String, String[]> all = req.queryString();
		if (all == null)
			return null;
		String[] values = all.get(key);
		if (values != null && values.length > 0) {
			return values[0];
		}
		return null;
	}

	// 获取IP地址
	public static String getIpAddr(Request request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.remoteAddress();
		}
		return ip;
	}

	/**
	 * 传入一个Cell将其转换为String 
	 * 
	 * @param cellValue
	 * @return
	 */
	public static String cellValueToString(Cell cellValue) {
		if (cellValue == null || cellValue.toString().equals("")) {
			return "";
		} else {
			
			String account = cellValue.toString();
			String regex = "^((\\d+.?\\d+)[Ee]{1}(\\d+))$";
			if (account.matches(regex)) {
				DecimalFormat df = new DecimalFormat("#.##");
				return df.format(Double.parseDouble(account)).replaceAll(
						"\\.0", "");
			} else {
				return cellValue.toString().replaceAll("\\.0", "");
			}
		}
	}

	/**
	 * 将半角转全角
	 * 
	 * @param input
	 * @return
	 */
	public static String toDBC(String input) {
		if (null != input) {
			char c[] = input.toCharArray();
			for (int i = 0; i < c.length; i++) {
				if ('\u3000' == c[i]) {
					c[i] = ' ';
				} else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
					c[i] = (char) (c[i] - 65248);
				}
			}
			String dbc = new String(c);
			/* return dbc.replaceAll("[\\pP|~|$|^|<|>|\\||\\+|=]*", ""); */
			return dbc;
		} else {
			return null;
		}
	}

	/**
	 * 判断是否为数字
	 * 
	 * @param src
	 * @return
	 */
	public static boolean isNumber(String src) {
		if (src == null || src.trim().equals("")) {
			return false;
		} else {
			return src.matches("^\\d+$");
		}
	}

	/**
	 * 验证邮箱是否合法
	 * 
	 * @param mail
	 * @return
	 */
	public static boolean checkEmail(String mail) {
		if (mail == null || mail.trim().equals("")) {
			return false;
		} else
			return mail
					.matches("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
	}

	/**
	 * 验证是否包含非法字符
	 * 
	 * @param str
	 * @return
	 */
	public static boolean checkString(String str) {
		if (str == null || str.trim().equals("")) {
			return false;
		} else {
			// 不包含%，&,$的任何字符串
			String regex = "[^%$&~\\-\\+=\\.\\*<>\\pP]{1,}";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(str);
			return m.matches();
		}
	}

	/* 获取一个标准的时间 */
	public static Date getDate(String s) {
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			//将指定时间格式解析成时间戳
			date = sdf.parse(s);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return date;
	}
	
	//获取几分钟之前时间戳
	public static String getDateStringMinutesAgo(int min) {
		java.util.Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -min);
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			return sdf.format(cal.getTime());

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	public static String getDateStringDaysAgo(int day) {
		java.util.Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -day);
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			return sdf.format(cal.getTime());

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	

	public static Date getDate(String s,String fmt) {
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(fmt);
			date = sdf.parse(s);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return date;
	}

	/* 获取一个标准的时间 */
	public static Date getDateTime(String s) {
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			date = sdf.parse(s);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return date;
	}

	public static String getDateStr(Date s) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			return sdf.format(s);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return "";
	}

	public static String getDateHourStr(Date s) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
			return sdf.format(s);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return "";
	}

	public static String getMinuteStr(Date s) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
			return sdf.format(s);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return "";
	}

	public static String getDateTimeStr(Date s) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return sdf.format(s);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return "";
	}

	public static String getDateTimeString(Date s,String fmt) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(fmt);
			return sdf.format(s);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return "";
	}
	
	public static String getDateString() {
		java.text.DateFormat format2 = new java.text.SimpleDateFormat(
				"yyyyMMddHHmmss");
		return format2.format(new Date());

	}

	public static String getDateStringforMillisecond(){
		java.text.DateFormat format2 = new java.text.SimpleDateFormat(
				"yyyyMMddhhmmssSSS");
		return format2.format(new Date());
	}

	public static String getDateString1() {
		java.text.DateFormat format2 = new java.text.SimpleDateFormat(
				"yyyyMMddHHmm");
		return format2.format(new Date());

	}

	// 过滤sql
	public static String getSafesql(String str) {
		// 过滤非法字符
		String inj_str = "'|and|exec|insert|select|delete|update|count|*|%|chr|mid|master|truncate|char|declare|;|or|-|+|#|<|>|limit|,";
		String mark = str;
		if (mark != null && !mark.equals("")) {
			String[] inj_stra = inj_str.split("\\|");
			for (int i = 0; i < inj_stra.length; i++) {
				if (str.indexOf(inj_stra[i]) >= 0) {
					mark = mark.replace(inj_stra[i], "");
				}
			}
		}
		return mark;
	}

	/**
	 * 获取随机字母数字组合
	 * 
	 * @param length
	 *            字符串长度
	 * @return
	 */
	static Random random;
	public static String getRandomCharAndNumr(Integer length) {
		String str = "";
		if( random==null ){
			random = new Random();
			random.setSeed(System.currentTimeMillis());
		}
		for (int i = 0; i < length; i++) {
			boolean b = random.nextBoolean();
			if (b) { // 字符串
				// int choice = random.nextBoolean() ? 65 : 97; 取得65大写字母还是97小写字母
				str += (char) (65 + random.nextInt(26));// 取得大写字母
			} else { // 数字
				str += String.valueOf(random.nextInt(10));
			}
		}
		return str;
	}

	/**
	 * 获取随机6位纯数字
	 * 
	 * @return
	 */
	public static String getRandom6Num() {
		int x = 0;
		Random r = new Random();
		while (true) {
			x = r.nextInt(999999);
			if (x > 99999) {
				break;
			}
		}
		return StringTool.getIDString(x, 6);
	}

	public static String getRandom4Num() {
		int x = 0;
		Random r = new Random();
		r.setSeed( System.currentTimeMillis() );
		x = r.nextInt(9999);
		return StringTool.getIDString(x, 4);
	}

	static Random rrr = new Random( System.currentTimeMillis() );
	public static int getRandomInt(int max) {
		return rrr.nextInt(max);
	}

	static String KEY_CON_char = "ABCDEF";
	static String KEY_CON_num = "23456789";
	//字母数字混合的随机6位密码
	public static String getRandom6NumChar() {
		int x = 0;
		Random r = new Random();
		String s1="";
		for(int i=0;i<3;i++)
			s1 += KEY_CON_char.charAt(r.nextInt(KEY_CON_char.length()));
		for(int i=0;i<3;i++)
			s1 += KEY_CON_num.charAt(r.nextInt(KEY_CON_num.length()));	

		return s1;
	}
	
	//随机生成定长数字字母混和随机数
	 public static String getCharAndNumr(int length) {
		  String val = "";
		  Random random = new Random();
		  for (int i = 0; i < length; i++) {
		   // 输出字母还是数字
		   String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
		   // 字符串
		   if ("char".equalsIgnoreCase(charOrNum)) {
		    // 取得大写字母还是小写字母
		    int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;
		    val += (char) (choice + random.nextInt(26));
		   } else if ("num".equalsIgnoreCase(charOrNum)) { // 数字
		    val += String.valueOf(random.nextInt(10));
		   }
		  }
		  return val;
	}

	static String KEY_CON = "01234567890abcdef";


	public static String mkMdString(int len) {
		Random rand = new Random(System.currentTimeMillis());
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < len; i++)
			sb.append(KEY_CON.charAt(rand.nextInt(KEY_CON.length())));
		return sb.toString();
	}

	static String KEY_CON1 = "01234567890";

	public static String mkNumRanmString(int len) {
		Random rand = new Random(System.currentTimeMillis());
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < len; i++)
			sb.append(KEY_CON1.charAt(rand.nextInt(KEY_CON1.length())));
		return sb.toString();
	}

	// 中文处理乱码
	public static String DealWithGBCodeOfRequest(String sGB) {
		if (sGB == null) {
			return null;
		}
		try {
			byte[] sReguest = sGB.getBytes("8859_1");
			return new String(sReguest);
		} catch (Exception e) {
			return sGB;
		}
	}

	public static String DealWithGBCodeOfRequest(String sGB , String coding ) {
		if (sGB == null)
			return null;
		try {
			//byte[] sReguest = sGB.getBytes("8859_1");
			byte[] sReguest = sGB.getBytes(coding);
			return new String( sReguest , "utf-8" );
		}
		catch (Exception e) {
			return sGB;
		}
	}

	//简单获取xml的key
	public static String findXMLByKey(String xml,String key)
	{
		if(StringTool.isNull(xml)||StringTool.isNull(key))
			return null;
		String bb = "<"+key+">",ee = "</"+key+">";
		int beg = xml.indexOf(bb);
		int end = xml.indexOf(ee);
		if( beg==-1||end==-1)
			return null;
		return xml.substring(beg+bb.length(), end).trim();

	}

	public static void sleep(long time)
	{
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String fmtMoney(float scale)
	{
		DecimalFormat   fnum  =   new  DecimalFormat("##0.00");    
		return fnum.format(scale); 
	}
	
	public static String fmtNum1(float scale)
	{
		DecimalFormat   fnum  =   new  DecimalFormat("##0");    
		return fnum.format(scale); 
	}
	
	public static String toMoney(String coin)
	{
		return coin.substring(0, coin.length()-1)+"."
				+coin.subSequence(coin.length()-1, coin.length());
	}

	//判断文件名是资源文件
	public static boolean isResourceFile(String file)
	{
		if( StringTool.isNull(file) )
			return false;
		int idx = file.lastIndexOf(".");
		if( idx == -1 )
			return false;
		String ext = file.substring(idx, file.length()).toLowerCase() ;
		if( ".jpg;.gif;.png;.html;.htm;.apk;.ipa".indexOf(ext)==-1 )
			return false;
		return true;
	}
	
	public static boolean isMsgLinkUrl(String url){
		if(!isNull(url))
			if(url.indexOf("app://")==0 || url.indexOf("http://")==0 || url.indexOf("https://")==0)
				return true;
		return false;
	}

	public static InetAddress getInetAddress(){  

		try{  
			return InetAddress.getLocalHost();  
		}catch(UnknownHostException e){  
			System.out.println("unknown host!");  
		}  
		return null;  

	}

	public static String getHostIp(){  
		InetAddress add= getInetAddress();
		if(null == add){  
			return null;  
		}  
		String ip = add.getHostAddress(); //get the ip address  
		return ip;  
	}

	//计算是否是月份最后的几天
	public static boolean isMonthlastDay( int day )
	{
		java.util.Calendar tim = Calendar.getInstance();
		int month = tim.get( Calendar.MONTH );
		tim.add( Calendar.DAY_OF_MONTH, day );
		return month != tim.get( Calendar.MONTH );
	}

	public static String hidePhone(String ph){
		if(ph!=null&&ph.length()==11){
			return ph.substring(0,3)+"****"+ph.substring(7);
		}
		return ph;
	}


	public static boolean isMobileNO(String mobiles){  
		Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(15[0-9])|(16[0-9])|(17[0-9])|(18[0-9])|(19[0-9]))\\d{8}$");  
		Matcher m = p.matcher(mobiles);  
		return m.matches();  
	}  
	
	/*
	 * 获取配置数据，如果没有找到，从数据库获取数据，放入Cache，缓存12小时
	 */
	static Hashtable<String,String> allKeyInMem = new Hashtable<String, String>();
	static Hashtable<String,String> allKeyTimeInMem = new Hashtable<String, String>();
	public static String findInMem( String group , String item )
	{
		try{
			String key       = ICacheService.Cache_String_Config + group + item;
			String value     = allKeyInMem.get( key );
			if( value == null )
				return null;
			if(  value.equals("NGValue"))
				value = "";
			
			//查找插入key的時間
			String keyTime   = ICacheService.Cache_String_Config + group + item + "-time";
			String valueTime = allKeyTimeInMem.get( keyTime );
			if( valueTime == null )
				return null;
			
			long now = System.currentTimeMillis();
			long llt = StringTool.GetLong(valueTime, 0);
			
			if( now-llt > 1000*120 ) //2分鐘
			{
				return null;
			}
			
			return value ;
		}catch(Exception e){
			return null;
		}
	}
	
	public static void setInMem( String group , String item , String value )
	{
		if( StringTool.isNull(value) )
			value = "NGValue";
			
		try{
			String key       = ICacheService.Cache_String_Config + group + item;
			String keyTime   = ICacheService.Cache_String_Config + group + item + "-time";
			
			long now = System.currentTimeMillis();
			allKeyInMem.put( key, value );
			allKeyTimeInMem.put(keyTime, ""+now );
		}catch(Exception e){
			
		}
	}
	
	//验证简单密码  ABABAB|ABCABC|AAAAAA|123456|654321
	public static boolean checkSimplePsw(String psw){
		Pattern patt = Pattern.compile("(\\d{2})(\\1{2})|(\\d{3})(\\1{1})|(\\d{1})(\\1{5})|(123456)|(654321)");
    	Matcher matcher = patt.matcher(psw);
    	return matcher.find();
	}
	
	public static String makeOrder(String prefix,String phone){
		return  prefix + phone + OrderId.get().toString();
	}
	
}
