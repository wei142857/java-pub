package util.classEntity;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;


public class StringTool {

	// Find and replace ;
	public static String replace(String sSrc, String sFind, String sTo) {

		if (sSrc == null)
			return null;
		if (sFind.length() == 0)
			return sSrc;
		int nFind = sSrc.indexOf(sFind), nOff = 0, keyLen = sFind.length();
		String sHead = "";
		if (nFind == -1)
			return sSrc;
		while (nFind != -1) {
			if (nFind > nOff) {
				sHead += sSrc.substring(nOff, nFind);

			}
			sHead += sTo;
			nOff = nFind + keyLen;
			nFind = sSrc.indexOf(sFind, nOff);
		}
		sHead += sSrc.substring(nOff, sSrc.length());
		return sHead;
	}

	public static String[] splitString(String sSrc, String sFilter, int nMax) {
		if (sSrc == null || sFilter == null)
			return null;
		if (sFilter.length() == 0)
			return new String[] { sSrc };
		int nCount = 0;
		int beg = 0;
		int nOff = sSrc.indexOf(sFilter);
		while (nOff != -1) {
			if (nOff - beg > 0) {
				nCount++;
			}
			beg = nOff + sFilter.length();
			nOff = sSrc.indexOf(sFilter, beg);

		}
		if (beg <= sSrc.length() - 1)
			nCount++;
		if (nCount > nMax)
			nCount = nMax;
		int nAlloc = nCount;
		String[] Ret = new String[nCount];
		nCount = 0;
		beg = 0;
		nOff = sSrc.indexOf(sFilter);
		while (nOff != -1) {
			if (nCount >= nMax - 1)
				break;
			if (nOff - beg > 0) {
				if (nCount == nMax - 1) {
					Ret[nCount] = sSrc.substring(beg, sSrc.length());
					return Ret;
				} else
					Ret[nCount] = sSrc.substring(beg, nOff);
				nCount++;
			}
			beg = nOff + sFilter.length();
			nOff = sSrc.indexOf(sFilter, beg);
		}
		if (nCount >= nAlloc)
			return Ret;
		if (beg <= sSrc.length() - 1) {
			Ret[nCount] = sSrc.substring(beg, sSrc.length());
			int nCount1 = Ret[nCount].length();
			if (nCount1 > sFilter.length()) {
				if (Ret[nCount].substring(nCount1 - sFilter.length(), nCount1)
						.compareTo(sFilter) == 0) {
					Ret[nCount] = Ret[nCount].substring(0,
							nCount1 - sFilter.length());
				}
			}
		}

		return Ret;
	}

	/**
   */
	public static String[] splitString(String sSrc, String sFilter) {
		if (sSrc == null || sFilter == null)
			return null;
		if (sFilter.length() == 0)
			return new String[] { sSrc };
		int nCount = 0;
		int beg = 0;
		int nOff = sSrc.indexOf(sFilter);
		while (nOff != -1) {
			if (nOff - beg > 0) {
				nCount++;
			}
			beg = nOff + sFilter.length();
			nOff = sSrc.indexOf(sFilter, beg);
		}

		if (beg <= sSrc.length() - 1)
			nCount++;
		String[] Ret = new String[nCount];
		nCount = 0;
		beg = 0;
		nOff = sSrc.indexOf(sFilter);
		while (nOff != -1) {
			if (nOff - beg > 0) {
				Ret[nCount] = sSrc.substring(beg, nOff);
				nCount++;
			}
			beg = nOff + sFilter.length();
			nOff = sSrc.indexOf(sFilter, beg);

		}
		if (beg <= sSrc.length() - 1)
			Ret[nCount] = sSrc.substring(beg, sSrc.length());

		return Ret;
	}

	public static String[] splitToLines(String srcStr) {
		if (srcStr == null)
			return null;
		srcStr = srcStr.trim();
		String[] outs = StringTool.splitString(srcStr, "\r\n");
		if (outs != null && outs.length == 1)
			outs = StringTool.splitString(srcStr, "\n");
		if (outs != null && outs.length == 1)
			outs = StringTool.splitString(srcStr, "\r");
		if (outs != null && outs.length == 1)
			outs = StringTool.splitString(srcStr, ",");
		if (outs != null && outs.length == 1)
			outs = StringTool.splitString(srcStr, "#");
		return outs;
	}

	public static String[] splitPhonum(String srcStr) {
		if (srcStr == null)
			return null;
		srcStr = srcStr.trim();
		String[] outs = StringTool.splitString(srcStr, "\r\n");
		if (outs != null && outs.length == 1)
			outs = StringTool.splitString(srcStr, "\n");
		if (outs != null && outs.length == 1)
			outs = StringTool.splitString(srcStr, "\r");
		if (outs != null && outs.length == 1)
			outs = StringTool.splitString(srcStr, ",");
		if (outs != null && outs.length == 1)
			outs = StringTool.splitString(srcStr, "#");
		return outs;
	}

	static public int GetInt(String sInt, int Default) {
		try {
			return Integer.valueOf(sInt).intValue();
		} catch (Exception e) {
			return Default;
		}
	}
	
	//16进制
	static public int GetIntEx(String sInt, int Default,int hex) {
		try {
			return Integer.valueOf(sInt,hex);
		} catch (Exception e) {
			return Default;
		}
	}

	static public float GetFloat(String sInt, float Default) {
		try {
			return Float.valueOf(sInt).floatValue();
		} catch (Exception e) {
			return Default;
		}
	}

	static public Double GetDouble(String sInt, Double Default) {
		try {
			return Double.valueOf(sInt).doubleValue();
		} catch (Exception e) {
			return Default;
		}
	}

	static public long GetLong(String sInt, int Default) {
		try {
			return Long.valueOf(sInt).longValue();
		} catch (Exception e) {
			return Default;
		}
	}

	public static String getIDString(long id, int nLength) {
		String sStr = Long.toString(id);
		String sPre = "";
		for (int i = 0; i < nLength - sStr.length(); i++)
			sPre += "0";
		return sPre + sStr;
	}

	public static String fillBinaryString(String binaryString, int nLength) {
		String sPre = "";
		for (int i = 0; i < nLength - binaryString.length(); i++)
			sPre += "0";
		return sPre + binaryString;
	}

	public static String DealWithGBCodeOfRequest(String sGB) {
		if (sGB == null)
			return null;
		try {
			byte[] sReguest = sGB.getBytes("8859_1");
			return new String(sReguest);
		} catch (Exception e) {
			return sGB;
		}
	}

	public static String makeHexString(String src, String codeSet) {
		String cmppContent = "";
		try {
			byte[] bSrc;
			if (codeSet != null && codeSet.length() != 0)
				bSrc = src.getBytes(codeSet);
			else
				bSrc = src.getBytes();

			cmppContent = makeHexString(bSrc);
		} catch (Exception e) {
			System.out
					.println("makeHexString " + codeSet + "--" + e.toString());
		}
		return cmppContent;
	}

	public static String makeHexString(byte[] bSrc) {
		String cmppContent = "";
		try {
			for (int i = 0; i < bSrc.length; i++) {
				String temp = Integer.toHexString(bSrc[i]);
				if (temp.length() < 2)
					temp = "0" + temp;
				else if (temp.length() > 2)
					temp = temp.substring(temp.length() - 2, temp.length());
				cmppContent += temp;
			}
			cmppContent = cmppContent.toUpperCase();
		} catch (Exception e) {
			System.out.println("makeHexString " + "--" + e.toString());
		}
		return cmppContent;
	}

	public static byte[] getFromHexString(String sInfo) {
		int nLen = sInfo.length() / 2;
		byte[] bC = new byte[nLen];
		try {
			for (int i = 0; i < nLen; i++) {
				bC[i] = (byte) Integer.parseInt(
						sInfo.substring(i * 2, i * 2 + 2), 16);
			}
		} catch (Exception e) {
			System.out.println("getFromHexString :" + sInfo + "-"
					+ e.toString());
		}
		return bC;
	}

	public static int getIndexOfArray(String[] arr, String s) {
		for (int i = 0; i < arr.length; i++)
			if (arr[i].equals(s))
				return i;
		return -1;
	}

	public static String formatHtmlSimple(String s) {
		s = StringTool.replace(s, " ", "&nbsp;");
		s = StringTool.replace(s, "^p", "<p>");
		return StringTool.replace(s, "\r\n", "<p>");
	}

	public static boolean isNull(String s) {
		return s == null || s.length() == 0;
	}

	/**
   */
	public static String[] splitEx(String sSrc, String sFilter) {
		if (isNull(sSrc) || isNull(sFilter))
			return null;

		int nCount = 0;
		int beg = 0;
		int nOff = sSrc.indexOf(sFilter);
		while (nOff != -1) {
			nCount++;
			beg = nOff + sFilter.length();
			nOff = sSrc.indexOf(sFilter, beg);
		}

		if (beg <= sSrc.length() - 1)
			nCount++;

		String[] Ret = new String[nCount];
		nCount = 0;
		beg = 0;
		nOff = sSrc.indexOf(sFilter);
		while (nOff != -1) {
			Ret[nCount] = sSrc.substring(beg, nOff);
			nCount++;
			beg = nOff + sFilter.length();
			nOff = sSrc.indexOf(sFilter, beg);
		}
		if (beg <= sSrc.length() - 1)
			Ret[nCount] = sSrc.substring(beg, sSrc.length());
		return Ret;
	}

	public static String properString(String input) {
		if (StringTool.isNull(input))
			return input;
		return input.substring(0, 1).toUpperCase()
				+ input.substring(1, input.length());
	}

	public static int getLinesCount(String file) {
		if (file == null)
			return 0;
		String[] lines = StringTool.splitToLines(FileOP.ReadFromDisk(file));
		if (lines == null)
			return 0;

		return lines.length;
	}
	
	public static String makeHexString(byte[] bSrc, int len) {
		String cmppContent = "";
		try {
			for (int i = 0; i < len; i++) {
				String temp = Integer.toHexString(bSrc[i]);
				if (temp.length() < 2)
					temp = "0" + temp;
				else if (temp.length() > 2)
					temp = temp.substring(temp.length() - 2, temp.length());
				cmppContent += temp;
			}
			cmppContent = cmppContent.toUpperCase();
		} catch (Exception e) {
			System.out.println("makeHexString " + "--" + e.toString());
		}
		return cmppContent;
	}
	
	public static String makeHexString(byte bSrc) {
		try {
			String temp = Integer.toHexString(bSrc);
			if (temp.length() < 2)
				return "0" + temp;
			if( temp.length()>2 )
				return temp.substring(temp.length()-2, temp.length());
			return temp;
		} catch (Exception e) {
			System.out.println("makeHexString " + "--" + e.toString());
		}
		return "";
	}
	
	public static byte[] intToBytes( int sec ) {
		try {
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			DataOutputStream dataOut = new DataOutputStream(byteOut);
			dataOut.writeInt(sec);
			return byteOut.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static byte[] longToBytes( long l ) {
		try {
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			DataOutputStream dataOut = new DataOutputStream(byteOut);
			dataOut.writeLong(l);
			return byteOut.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
}