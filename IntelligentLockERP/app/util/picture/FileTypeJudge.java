package util.picture;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class FileTypeJudge {
	
	private static String bytes2hex(byte[] bytes){
		StringBuffer hex = new StringBuffer();
		for(int i = 0; i<bytes.length ; i++){
			String temp = Integer.toHexString(bytes[i]&0xFF);
			if(temp.length() == 1){
				hex.append("0");
			}
			hex.append(temp.toLowerCase());
		}
		return hex.toString();
	}
	
	
	private static String getFileHeader(String filePath) throws IOException{
		
	byte[] b = new byte[128];
	InputStream inputStream = null;
	inputStream = new FileInputStream(filePath);
	inputStream.read(b, 0, 28);
	inputStream.close();
	return bytes2hex(b);
	}
	
	public static FileType getType(String filePath) throws IOException{
		String fileHead = getFileHeader(filePath);
		if(fileHead == null || fileHead.length() == 0){
			return null;
		}
		fileHead = fileHead.toUpperCase();
		FileType[] fileTypes = FileType.values();
		for(FileType type : fileTypes){
			if(fileHead.startsWith(type.getValue())){
				return type;
			}
		}
		
		return null;
	}
	
	public static void main(String[] args) throws IOException {
		System.out.println(FileTypeJudge.getType("C:\\Users\\LingHai\\Desktop\\1.PNG"));
	}

}
