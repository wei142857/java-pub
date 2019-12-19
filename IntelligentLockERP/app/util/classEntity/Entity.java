package util.classEntity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Entity {
	public List<EntityParam> allParams = new ArrayList<EntityParam>();

	//Entity name;
	public String name = null;
	
	public String chnname = null;

	//Package name;
	public String pkg = null;

	public String getTxtParam() {
		String ret = "";
		for (int i = 0; i < allParams.size(); i++) {
			EntityParam pa = (EntityParam) allParams.get(i);
			if (i == 0 && pa.typeString.equalsIgnoreCase("String"))
				ret = pa.name;
			if (i > 0 && pa.typeString.equalsIgnoreCase("String"))
				ret = pa.name;
		}
		return ret;
	}

	public static Entity anylisFile(String sFilePath) {
		Entity entity = new Entity();
		try {
			BufferedReader output_file_one = new BufferedReader(
					new InputStreamReader(new FileInputStream(sFilePath),
							"UTF8"));
			EntityParam pam = null;

			String stri = output_file_one.readLine();

			while (stri != null) {
				stri = StringTool.replace(stri, "\t", "");
				String[] pams = StringTool.splitString(stri, " ");

				if (pams == null || pams.length < 1) {
					stri = output_file_one.readLine();
					continue;
				}

				// package
				if (pams[0].equalsIgnoreCase("package")) {
					if (pams.length > 1) {
						entity.pkg = pams[1];
						entity.pkg = StringTool.replace(entity.pkg, ";", "");
					}
				}

				// entity name;
				if (pams.length >= 3) {
					if (pams[0].equalsIgnoreCase("public")
							&& pams[1].equalsIgnoreCase("class")
							&& entity.name == null) {
						entity.name = dealName(pams[2]);
					}
				}
				// entity params;
				// param begin;
				if( pams[0].equalsIgnoreCase("@Id")||pams[0].indexOf("@Column") == 0 ){
					if( pam==null ){
						pam = new EntityParam();
						pam.paramIdx = entity.allParams.size();
					}
					if (pams[0].equalsIgnoreCase("@Id")) 
						pam.ext = "主键\t";
					if (pams[0].indexOf("@Column") == 0) 
						pam.ext += stri + "\t";
				}

				if ( (pams[0].equalsIgnoreCase("public")||pams[0].equalsIgnoreCase("private"))
						&& pams.length >= 3) {
					if (pam != null) {
						pam.name = dealName(pams[2]);
						pam.ext  = dealExt(pams[pams.length-1]);
						pam.typeString = pams[1];
						entity.allParams.add(pam);
						pam = null;
					}
				}

				stri = output_file_one.readLine();
			}
			output_file_one.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		return entity;
	}
	
	public String toString()
	{
		String ret =pkg+"."+name+"";
		ret += "param : " + allParams.size()+".";
		for( int i=0;i<allParams.size();i++ )
		{
			EntityParam eny=(EntityParam)allParams.get(i);
			ret += eny.toString()+"";
		}
		return ret;
				
	}
	
	//去掉名字里面乱码
	public static String dealName(String nn)
	{
		String[] ss = nn.split("//");
		ss = nn.split("=");
		ss = ss[0].split(";");
		return ss[0].replaceAll(";", "").replace("{", "").replace("}", "");
	}
	
	public static String dealExt(String nn)
	{
		String[] ss = nn.split("//");
		if(ss.length>1)
			return ss[1].replaceAll(";", "").replace("{", "").replace("}", "");
		return ss[0].replaceAll(";", "").replace("{", "").replace("}", "");
	}
	
	public String idx ="" ; 
	public String getIdx()
	{
		for (int i = 0; i < allParams.size(); i++) {
			EntityParam pa = (EntityParam) allParams.get(i);
			/*if (pa.ext.indexOf("主键") != -1)
				idx= pa.name;*/
			if( pa.paramIdx==0 )
				return pa.name;
		}
		return idx;
	}

}
