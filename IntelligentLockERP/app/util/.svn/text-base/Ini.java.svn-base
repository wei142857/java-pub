package util;

import util.classEntity.FileOP;
import util.classEntity.StringTool;

public class Ini {

	private static String getKeyString(String sTitle, String sKey,
			String sDefault, String sFile) {
		if (sFile == null || sTitle == null || sKey == null)
			return null;
		String sContent1 = FileOP.ReadFromDisk(sFile);
		if (sContent1 == null)
			return sDefault;

		String[] infos = StringTool.splitToLines(sContent1);

		int BegLine = -1, EndLine = infos.length;
		boolean bFindBeg = false;

		// find the segment;
		for (int i = 0; i < infos.length; i++) {
			if (!bFindBeg
					&& infos[i].trim().equalsIgnoreCase("[" + sTitle + "]")) {
				BegLine = i;
				bFindBeg = true;
				continue;
			}
			if (bFindBeg && infos[i].trim().indexOf("[") == 0) {
				EndLine = i;
				break;
			}
		}
		if (BegLine == -1)
			return sDefault;

		for (int i = BegLine + 1; i < EndLine; i++) {
			String[] ones = StringTool.splitString(infos[i].trim(), "=", 2);
			if (ones == null || ones.length < 1)
				continue;
			if (ones[0].equalsIgnoreCase(sKey)) {
				if (ones.length == 2) {
					String ret = ones[1];
					return getResult(ones[1]);
				}
				return "";
			}
		}
		return sDefault;
	}

	private static String getResult(String ret) {
		if (ret.charAt(ret.length() - 1) == ';')
			return ret.substring(0, ret.length() - 1);
		return ret;
	}

	static public String getStringByKey(String sTitle, String sKey,
			String sDefault, String sFile) {
		return getKeyString(sTitle, sKey, sDefault, sFile);
	}

	static public int getIntegerByKey(String sTitle, String sKey, int nDefault,
			String sFile) {
		String content = getKeyString(sTitle, sKey, Integer.toString(nDefault),
				sFile);

		int nValue;
		try {
			nValue = Integer.parseInt(content);
		} catch (Exception e) {
			nValue = nDefault;
		}
		return nValue;
	}

}
