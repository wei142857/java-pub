package util;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import play.Logger;
import sun.misc.BASE64Encoder;
import util.classEntity.StringTool;

public class AesUtil {

	static byte[] getIV(String key) {
		String iv = key; // IV length: must be 16 bytes long
		return iv.getBytes();
	}

	static final String CIPHER_ALGORITHM_CBC = "AES/CBC/PKCS5Padding"; //需要密钥和向量，自动补齐
	static final String CIPHER_ALGORITHM_ECB = "AES/ECB/PKCS5Padding"; //只需要一个密钥，不需要向量，自动补齐
	//static final String CIPHER_ALGORITHM_CBC = "AES/CBC/NoPadding";  //需要补齐尾数才能做加密
	static final String KEY_ALGORITHM = "AES";
	

//	static Cipher cipher;
//	static SecretKey secretKey;

	public static void main(String args[]){
		String sKey = "6106c34e2786d852e79e6bf32ab8fa12";
		String sLv = "00e3d201c5c2ac23f8154860272ba0a2";
		/*
		// 需要加密的字串
		String cSrc = "18675670006";
		// 加密
		String enString;
		try {
			
			
			enString = AesUtil.Encrypt(cSrc, sKey,sLv);
			
			System.out.println("加密]后的字串是：" + enString);

			// 解密
			long time = new Date().getTime();
			String DeString = AesUtil.Decrypt(enString, sKey,sLv);
			System.out.println(new Date().getTime()-time);
			System.out.println("解密后的字串是：" + DeString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		
		try {
			System.out.println(URLEncoder.encode(AesUtil.Encrypt("18600212340", sKey,sLv),"utf-8"));
//			System.out.println(AesUtil.Decrypt(URLDecoder.decode("%2FtG%2Bd27qLp9vjKYsZSc4Hg%3D%3D", "utf-8"), sKey,sLv));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	// 加密 skey和slv为不同值  base64转换
	public static String Encrypt(String str,String sKey,String sLv) throws Exception{
		try {
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_CBC);

			SecretKey secretKey = new SecretKeySpec(StringTool.getFromHexString(sKey), "AES");// 生成密匙

			cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(
					StringTool.getFromHexString(sLv)));// 使用加密模式初始化 密钥
			byte[] encrypt = cipher.doFinal(str.getBytes("utf-8")); // 按单部分操作加密或解密数据，或者结束一个多部分操作。
			String sout = Base64.encodeBase64String(encrypt);
			Logger.info("Encrypt - 加密 后：" + sout);
			return sout;
		} catch (Exception e) {
			Logger.error("Aes Encrypt ", e);
			throw e;
			
		}
	}
	// 解密 skey和slv为不同值  base64转换
	public static String Decrypt(String sSrc, String sKey,String sLv) throws Exception {
		try {
			byte[] src = Base64.decodeBase64(sSrc);
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_CBC);

			SecretKey secretKey = new SecretKeySpec(StringTool.getFromHexString(sKey), "AES");// 生成密匙

			cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(
					StringTool.getFromHexString(sLv)));// 使用解密模式初始化 密钥
			byte[] decrypt = cipher.doFinal(src); // 按单部分操作加密或解密数据，或者结束一个多部分操作。
			return new String(decrypt, "utf-8");
		} catch (Exception e) {
			Logger.error("Aes Decrypt ", e);
			throw e;
		}
	}
	
	
	// 加密 skey和slv为同一值  16进制转换
	public static String Encrypt(String str, String sKey) {
		try {
			Cipher	cipher = Cipher.getInstance(CIPHER_ALGORITHM_CBC);

			SecretKey	secretKey = new SecretKeySpec(sKey.getBytes(), "AES");// 生成密匙

			cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(
					getIV(sKey)));// 使用加密模式初始化 密钥
			byte[] encrypt = cipher.doFinal(str.getBytes("utf-8")); // 按单部分操作加密或解密数据，或者结束一个多部分操作。

			String sout = StringTool.makeHexString(encrypt);
			Logger.info("Encrypt - 加密 后：" + sout);
			return sout;
		} catch (Exception e) {
			Logger.error("Aes Encrypt ", e);
		}
		return null;
	}
	
	

	// 解密 skey和slv为同一值 16进制转换
	public static String Decrypt(String sSrc, String sKey) {
		try {
			byte[] src = StringTool.getFromHexString(sSrc);

			Cipher	cipher = Cipher.getInstance(CIPHER_ALGORITHM_CBC);

			// KeyGenerator 生成aes算法密钥
			SecretKey secretKey = new SecretKeySpec(sKey.getBytes(), "AES");// 生成密匙

			cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(
					getIV(sKey)));// 使用解密模式初始化 密钥
			byte[] decrypt = cipher.doFinal(src); // 按单部分操作加密或解密数据，或者结束一个多部分操作。
			return new String(decrypt, "utf-8");
		} catch (Exception e) {
			Logger.error("Aes Decrypt ", e);
		}
		return null;
	}
	
	// 加密 skey为同一值  ,Base64进制转换
	public static String EncryptBase64ECB(String str, String sKey) {
		try {
			Cipher cipher = Cipher.getInstance( CIPHER_ALGORITHM_ECB );
			SecretKeySpec skeySpec = new SecretKeySpec(sKey.getBytes(), "AES");
            cipher.init( Cipher.ENCRYPT_MODE, skeySpec );
            
			byte[] encrypt = cipher.doFinal(str.getBytes("utf-8")); // 按单部分操作加密或解密数据，或者结束一个多部分操作。
			BASE64Encoder base64 = new BASE64Encoder();
	        return base64.encode(encrypt);
		} catch (Exception e) {
			Logger.error("Aes Encrypt ", e);
		}
		return null;
	}

}
