package util;

import java.util.Hashtable;

import play.Configuration;
import util.classEntity.FileOP;
import util.classEntity.StringTool;

public class IniLoader {
	
	public static void main(String args[]) {
	}
	
	//获取一个 配置字串
	public static String getStringByKey(String sTitle, String sKey, String sDefault,
			String sFileName)
	{
		String dir= Configuration.root().getString("app.dir")+"conf/";
		String sFile = dir+sFileName+".ini";
		return getStringByKeyF( sTitle, sKey,sDefault,sFile);
	}
	
	//获取一个 配置数字
	public static int getIntegerByKey(String sTitle, String sKey, int sDefault,
			String sFileName)
	{
		String dir= Configuration.root().getString("app.dir")+"conf/";
		String sFile = dir+sFileName+".ini";
		return getIntegerByKeyF( sTitle, sKey,sDefault,sFile);
	}
	
	
	private static String null_String = "&&^^%%$$";

	private static String getStringByKeyF(String sTitle, String sKey, String sDefault,
			String sFile) {
		if (sDefault == null)
			sDefault = null_String;

		Hashtable IniF, IniProp, IniKey;
		// find ini file;
		IniF = (Hashtable) allInis.get(sFile);
		
		if ( IniF == null ) {
			IniF = new Hashtable();
			IniF.put("fdate", "" + FileOP.getFileDate(sFile));
			IniF.put("ggdate", "" + System.currentTimeMillis() );
			allInis.put(sFile, IniF);
		}
		
		long now = System.currentTimeMillis();
		if( now - StringTool.GetLong((String)IniF.get("ggdate"), 0) > 1000 * 60l  ){
			//检查文件，20分钟间隔
			if (  !((String) IniF.get("fdate")).equals(""+ FileOP.getFileDate(sFile)) ) {
				if (IniF != null)
					IniF.clear();
				IniF = new Hashtable();
				IniF.put("fdate", "" + FileOP.getFileDate(sFile));
				IniF.put("ggdate", "" + System.currentTimeMillis() );
				allInis.put(sFile, IniF);
			}
		}

		// find ini property;
		IniProp = (Hashtable) IniF.get(sTitle);
		if (IniProp == null) {
			IniProp = new Hashtable();
			IniF.put(sTitle, IniProp);
		}

		// find key;
		String sRet = (String) IniProp.get(sKey);
		if (sRet == null) {
			sRet = Ini.getStringByKey(sTitle, sKey, sDefault, sFile);
			IniProp.put(sKey, sRet);
		}
		if (sRet.equals(null_String))
			return null;

		return sRet;
	}

	private static int getIntegerByKeyF(String sTitle, String sKey, int nDefault,
			String sFile) {
		String sV = getStringByKeyF(sTitle, sKey, null, sFile);
		if (sV == null)
			return nDefault;
		return StringTool.GetInt(sV, nDefault);
	}

	// static things for Ini;
	private static Hashtable allInis = new Hashtable();
}
