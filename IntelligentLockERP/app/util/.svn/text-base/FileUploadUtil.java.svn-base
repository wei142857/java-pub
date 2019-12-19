package util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import org.springframework.util.Assert;

import play.Configuration;
import play.Logger;
import play.mvc.Http.MultipartFormData.FilePart;

public class FileUploadUtil {
	private static final int BUFFER_SIZE = 16 * 1024;
	
	public static String putImg(FilePart filePart,String catalog){
		return putImg(filePart.getFile(),filePart.getFilename(), catalog);
	}
	
	public static String putImg(File file,String tempFileName,String catalog){
		try{
			String relativePath = makePath(catalog);
			new File(relativePath).mkdirs();
			String copyPath = relativePath+tempFileName;
			File dest = new File(copyPath);
			copy(file, dest);
			file.delete();
			return "/"+copyPath;
		}catch(Exception e){
			Logger.error("FileUploadUtil",e.toString());
		}
		return "";
	}
	
	private static String makePath(String catalog){
		return "public/excelFile/"+ catalog+"/"+StringUtil.getDateTimeString(new Date(), "yyyyMMdd")+"/";
	}
	
	private static String getFileName(String tempFileName) {
		
//		int index = tempFileName.lastIndexOf(".");
//		return new Date().getTime()+tempFileName.substring(index);
		return tempFileName;
	}
	private static File crateDestFile(String catalog,String relativePath,String fileName){
		String prefix = "/share/flow_web/";
		if(Configuration.root().getString("debugServer")==null){
			 prefix = Configuration.root().getString("app.dir");
		}
		String path = prefix + relativePath;
		new File(path).mkdirs();
		return new File(path+fileName);
	}
	private static void copy(File src, File dest) {
		Assert.notNull(src, "src file cannot be null");
		Assert.notNull(dest, "dest file cannot be null");
		// 当原文件为空文件的处理方式.

		try {

			InputStream in = null;
			OutputStream out = null;
			try {
				FileInputStream fileInputStream = new FileInputStream(src);
				in = new BufferedInputStream(fileInputStream,
						BUFFER_SIZE);
				out = new BufferedOutputStream(new FileOutputStream(dest),
						BUFFER_SIZE);
				byte[] buffer = new byte[BUFFER_SIZE];
				while (in.available() >= BUFFER_SIZE) {

					in.read(buffer);
					out.write(buffer);
				}
				if (in.available() < BUFFER_SIZE) {
					buffer = new byte[in.available()];
					in.read(buffer);
					out.write(buffer);
				}

			} finally {
				if (null != in) {
					in.close();
				}
				if (null != out) {
					out.close();
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
}
