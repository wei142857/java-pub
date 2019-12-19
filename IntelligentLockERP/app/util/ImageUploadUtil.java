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

public class ImageUploadUtil {
	/**
	 * 此方法可以将上传文件绝对路径下的文件 通过流输出到指定项目相对路径下的指定文件夹下
	 * @param file File	对象 前端传过来的文件绝对路径的File对象
	 * @param tempFileName	自定义指定文件的名称 主要是获取名称的后缀
	 * @param catalog	生成指定项目图片上传的文件夹
	 * @return 文件的相对路径 出现异常返回空字符串
	 */
	public static String putImg(File file,String tempFileName,String catalog){
		try{
			//catalog下生成文件夹 用于存储当天上传的图片 返回相对路径
			String relativePath = makePath(catalog);
			//根据文件名 解析格式返回一个 时间戳＋相同后缀 的文件
			String fileName = getFileName(tempFileName);
			//目的地文件的File对象
			File dest = crateDestFile(catalog,relativePath,fileName);
			/**
			 * 此方法用于将上传的文件通过流 输出到指定目的地文件夹下
			 *  @param file 上传文件的绝对路径的File对象
			 *  @param dest 目的地的文件File对象 文件还不存在 输出时自动创建
			 */
			copy(file, dest);
			file.delete();
			return "/"+relativePath+fileName;
		}catch(Exception e){
			Logger.error("ImageUploadUtil",e.toString());
		}
		return "";
	}
	/**
	 * 文件会上传到项目中静态文件中默认default文件夹下
	 * @param filePart 上传文件对象
	 * @return 返回文件的相对路径
	 */
	public static String putImg(FilePart filePart){
		return putImg(filePart.getFile(),filePart.getFilename(), "default");
	}
	/**
	 * 自定义指定文件存放路径的同时 自动获取文件的名称 将上传文件存储到指定文件夹（ ‘catalog’）下
	 * @param filePart 上传文件对象
	 * @param catalog 文件上传到指定文件夹下的名称
	 * @return 返回相对路径
	 */
	public static String putImg(FilePart filePart,String catalog){
		return putImg(filePart.getFile(),filePart.getFilename(), catalog);
	}
	/**
	 * 用于生成目的地文件的File对象
	 * @param catalog 项目指定的文件夹
	 * @param relativePath 生成的相对路径
	 * @param fileName 时间戳+文件后缀 格式 的文件名称
	 * @return 返回指定目的地下文件的File对象
	 */
	private static File crateDestFile(String catalog,String relativePath,String fileName){
		String prefix = "/share/skn/";
		if(Configuration.root().getString("debugServer")==null){
			 prefix = Configuration.root().getString("app.dir");
		}
		//根据拼接生成了绝对路径 项目绝对+文件相对
		String path = prefix + relativePath;
		//创建绝对路径下文件夹
		new File(path).mkdirs();
		//将绝对路径下生成文件的File对象返回
		return new File(path+fileName);
	}
	/**
	 * 指定项目的图片上传路径
	 * @param catalog 项目图片上传文件夹名称
	 * @return 返回一个一天更新一次的文件夹路径
	 */
	private static String makePath(String catalog){
		return "public/uploadImg/"+ catalog+"/"+StringUtil.getDateTimeString(new Date(), "yyyyMMdd")+"/";
	}
	/**
	 * 传过来文件名称 将文件更改成 “时间戳+文件后缀” 格式
	 * @param tempFileName 传过来文件名称
	 * @return 返回改变格式后的文件名称
	 */
	private static String getFileName(String tempFileName) {
		
		int index = tempFileName.lastIndexOf(".");
		return new Date().getTime()+tempFileName.substring(index);
	}
	private static final int BUFFER_SIZE = 16 * 1024;
	/**
	 * 此方法用于将上传的文件通过流 输出到指定目的地文件夹下
	 * @param src 上传文件的文件绝对路径
	 * @param dest 目的地文件的文件对象
	 */
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
				// -----1.
				// 当可读字节数大于缓冲字节数，以缓冲字节数为单位读入写入.
				byte[] buffer = new byte[BUFFER_SIZE];
				while (in.available() >= BUFFER_SIZE) {

					in.read(buffer);
					out.write(buffer);
				}
				// 当可读字节数小于缓冲字节数,用可读字节数长度的数组读入写入. example: available=10500,
				// buffer=1000.
				if (in.available() < BUFFER_SIZE) {
					buffer = new byte[in.available()];
					in.read(buffer);
					out.write(buffer);
				}
			 
				/*-------2.单字节读入写入.
				 int i = -1;
				 while ((i = in.read()) != -1) {
				 out.write(i);
				 }
				 */

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
	
	public static void main(String args[]){
		System.out.println(getFileName("asdkfjl.jpg"));
	}
}
