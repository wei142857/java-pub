package util.classEntity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.util.Date;
import java.util.Hashtable;

public class FileOP {

	public static boolean WriteToDisk(String sData, String sFile) {
		return
			WriteToDisk(sData.getBytes(), sData.getBytes().length, sFile);
		
	}

	public static boolean WriteToDisktotail(String sData,String sFile){
		try {
			//Integer ofilelenth=getTotalLines(sFile);
			WriteToDisktotail(sData.getBytes(), 0,sData.substring(0, sData.getBytes().length).length(),
					sFile);
		} catch (Exception e) {
			System.out.println(e.toString());
			return false;
		}
		return true;
	}
	public static boolean WriteToDisk(String sData, int nLength, String sFile) {
		try {
			WriteToDisk(sData.getBytes(), sData.substring(0, nLength).length(),
					sFile);
		} catch (Exception e) {
			System.out.println(e.toString());
			return false;
		}
		return true;
	}

	public static boolean WriteToDisk(byte[] sData, String sFile) {
		return WriteToDisk(sData, sData.length, sFile);
	}

	public static boolean WriteToDisk(byte[] sData, int nLength, String sFile) {
		return WriteToDisk(sData, 0, sData.length, sFile);
	}

	public static String ReadFromDisk(String FileName) {

		if (FileName == null || FileName.length() == 0)
			return null;

		File tempFile = new File(FileName);
		if (tempFile.exists() == false || tempFile.canRead() == false)
			return null;
		int nDataLength = (int) tempFile.length();
		byte[] sReturn;
		try {
			FileInputStream tempFS = new FileInputStream(FileName);
			sReturn = new byte[nDataLength];
			tempFS.read(sReturn);
			tempFS.close();
		} catch (Exception e) {
			return null;
		}
		return new String(sReturn);
	}
	
	public static String ReadFromDisk(String FileName,String charset) {

		if (FileName == null || FileName.length() == 0)
			return null;

		File tempFile = new File(FileName);
		if (tempFile.exists() == false || tempFile.canRead() == false){
			System.out.println( FileName +" Not Found!");
			return null;
		}
		int nDataLength = (int) tempFile.length();
		byte[] sReturn;
		try {
			FileInputStream tempFS = new FileInputStream(FileName);
			sReturn = new byte[nDataLength];
			tempFS.read(sReturn);
			tempFS.close();
			
			return new String(sReturn,charset);
		} catch (Exception e) {
			System.out.println ( e.toString () );
			return null;
		}
		
	}


	public static byte[] ReadFromDiskByByte(String FileName) {

		if (FileName == null || FileName.length() == 0)
			return null;

		File tempFile = new File(FileName);
		if (tempFile.exists() == false || tempFile.canRead() == false) {
			System.out.println("file can't acess:" + FileName);
			return null;
		}
		int nDataLength = (int) tempFile.length();
		byte[] sReturn;
		try {
			FileInputStream tempFS = new FileInputStream(FileName);
			sReturn = new byte[nDataLength];
			tempFS.read(sReturn);
			tempFS.close();
		} catch (Exception e) {
			System.out.println("read file fail:" + e.toString());
			return null;
		}
		return sReturn;
	}

	public static String makeDir(String sPath, String sDir1) {
		if (sPath == null)
			return null;
		if (sPath.charAt(sPath.length() - 1) != File.separatorChar)
			sPath += File.separatorChar;
		String sd = sDir1;
		String sDir = sPath + sd + File.separatorChar;
		boolean bTag = false;
		try {
			File ok = new File(sDir);
			if (ok.exists())
				return sDir;
			bTag = ok.mkdir();
			if (!bTag)
				return null;
		} catch (Exception e) {
			System.out.println("make dir-" + e.toString());
			return null;
		}

		return sDir;
	}

	/**
	 */
	public static boolean move(String srcFile, String destPath) {
		// File (or directory) to be moved
        File file = new File(srcFile);
      
      
        // Move file to new directory
        boolean success = file.renameTo(new File(destPath));
      
        return success;
	}
	
	public static boolean copyTo(String srcFile, String destPath) {
		// File (or directory) to be moved
        String c = FileOP.ReadFromDisk(srcFile);
        if(c==null)
        	return false;
        FileOP.delete(srcFile);
        FileOP.WriteToDisk(c, destPath);
      
        return true;
	}

	/**
	 */
	public static boolean copy(String sFile, String sFileTo) {
		byte[] content = FileOP.ReadFromDiskByByte(sFile);
		if (content == null)
			return false;
		return FileOP.WriteToDisk(content, sFileTo);
	}

	/**
	 * �ж��Ƿ�Ŀ¼
	 */
	public static boolean isDir(String sFile) {

		File f = new File(sFile);
		if (f.isDirectory()) {
			return true;
		}
		return false;
	}

	// ɾ��
	public static boolean delete(String sFile) {
		try {
			File f = new File(sFile);

			if (f.delete()) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	// �������
	public static boolean exist(String sFile) {

		File f = new File(sFile);
		if (f.exists()) {
			return true;
		}
		return false;
	}

	// �г�Ŀ¼���ļ�
	public static String[] getFiles(String sDir) {
		File file = new File(sDir);
		if (file.exists())
			return file.list();
		return null;
	}

	public static void writeFileAtOffset(String sOut, String sFileName, int nOff) {
		try {
			RandomAccessFile f = new RandomAccessFile(sFileName, "rw");
			f.seek(nOff);
			f.write(sOut.getBytes());
			f.close();
		} catch (Exception ex) {
			System.err.println(ex);
		}
		System.out.println(sOut);
	}

	
	
	static int TOO_MUCH_DATA = 12000000;
	public static void writeFileAtTail(String sOut, String sFileName) {
		try {
			if (FileOP.length(sFileName) > TOO_MUCH_DATA) {
			     FileOP.move(sFileName, sFileName+"_"+util.StringUtil.getDateTimeString(new Date()
			     	, "yyyyMMddHHmmss") );
			}
			
			RandomAccessFile f = new RandomAccessFile(sFileName, "rw");
			f.seek(f.length());
			f.write(sOut.getBytes());
			f.close();
		} catch (Exception ex) {
			System.err.println(ex);
		}
		//System.out.println(sOut);
	}
	
	static Hashtable allF = new Hashtable();
	static long turn = 0;
	public static void writeFileAtTailBuffer(String sOut, String sFileName) {
		turn ++;
		RandomAccessFile f = null;
		try {
			f = (RandomAccessFile)allF.get(sFileName);
			if( f == null ){
				f = new RandomAccessFile(sFileName, "rw");
				f.seek( f.length() );
				allF.put(sFileName, f);
			}
			
			//检查size
			if( turn % 20 == 0 ){
				if (FileOP.length(sFileName) > TOO_MUCH_DATA) {
					 f.close();
				     FileOP.move(sFileName, sFileName+"_"+util.StringUtil.getDateTimeString(new Date()
				     	, "yyyyMMddHHmmss") );
				     f = new RandomAccessFile(sFileName, "rw");
					 allF.put(sFileName, f);
				}
			}
			
			f.write( sOut.getBytes() );
		} catch (Exception ex) {
			System.err.println(ex);
			try{
				if(f!=null) f.close();
			}
			catch(Exception ee)
			{
			}
			allF.remove( sFileName );
		}
	}

	public static boolean WriteToDisk(byte[] sData, int nBeg, int nLength,
			String sFile) {
		if (sFile == null)
			return false;
		File file = new File(sFile);
		try {
			FileOutputStream tempFS = new FileOutputStream(sFile);
			if (nLength > 0)
				tempFS.write(sData, nBeg, nLength);
			tempFS.close();
		} catch (Exception e) {
			File file1 = new File(file.getParent());
			if (file1.mkdirs()) {
				return FileOP.WriteToDisk(sData, nLength, sFile);
			}
			System.out.println(e);
			return false;
		}
		return true;
	}
	
	public static boolean WriteToDisktotail(byte[] sData, int nBeg, int nLength,
			String sFile) {
		if (sFile == null)
			return false;
		File file = new File(sFile);
		try {
			FileOutputStream tempFS = new FileOutputStream(sFile,true);
			if (nLength > 0)
				tempFS.write(sData, nBeg, nLength);
			tempFS.close();
		} catch (Exception e) {
			File file1 = new File(file.getParent());
			if (file1.mkdirs()) {
				return FileOP.WriteToDisk(sData, nLength, sFile);
			}
			System.out.println(e);
			return false;
		}
		return true;
	}

	// �ļ�����
	public static long length(String sFile) {

		File f = new File(sFile);
		if (f.exists()) {
			return f.length();
		}
		return 0;
	}

	// �ļ��޸�ʱ�䣻
	public static long getFileDate(String sFile) {

		File f = new File(sFile);
		if (f.exists()) {
			return f.lastModified();
		}
		return 0;
	}

	public static String makeDistinctFileName(String dir, String fileN) {
		String file;
		if (FileOP.exist(dir + fileN))
			return makeDistinctFileName(dir, "N" + fileN);
		else
			return fileN;
	}

	public static void deleteDir(String dir) {
		if (dir == null || dir.length() == 0)
			return;
		char c = dir.charAt(dir.length() - 1);
		if (c != File.separatorChar)
			dir += File.separatorChar;

		String[] fs = FileOP.getFiles(dir);
		if (fs != null) {

			for (int i = 0; i < fs.length; i++) {
				if (FileOP.isDir(dir + fs[i]))
					deleteDir(dir + fs[i]);
				else
					FileOP.delete(dir + fs[i]);
			}
		}
		FileOP.delete(dir);
	}
	
	//read file with charactor coding;
	public static String readFile(String sFilePath,String coding )
	{
		StringBuffer sb = new StringBuffer();
		try{
			BufferedReader output_file_one = new BufferedReader(new InputStreamReader(new FileInputStream(sFilePath),coding));
		
			
			String stri = output_file_one.readLine () ;
			
			while( stri!=null )
			{
				sb.append ( stri);
				sb.append ( "\r\n" );
				stri = output_file_one.readLine () ;
			}
			output_file_one.close();
		}
		catch(Exception e)
		{
			System.out.println ( e.toString () );
			return null;
		}
		return sb.toString ();
	}
	
	//write file with charactor coding;
	public static boolean writeFile(String sFilePath,String content ,String coding )
	{
		if( StringTool.isNull (content) )
			return false;
		try{
			if( !FileOP.exist (sFilePath))
				FileOP.WriteToDisk (" ",sFilePath);
			BufferedWriter output_file_one = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(sFilePath),coding));
		
			output_file_one.write ( content );
			
			output_file_one.close();
		}
		catch(Exception e)
		{
			System.out.println ( e.toString () );
			return false;
		}
		return true;
	}

	
	public static String getFileExt(String f){
		String[] ff = StringTool.splitString(f, ".");
		if( ff.length>0 )
			return ff[ff.length-1];
		return "";
	}
	
	public static int getTotalLines(String fileName) throws IOException {
		if(exist(fileName)){
	        FileReader in = new FileReader(fileName);
	        LineNumberReader reader = new LineNumberReader(in);
	        String strLine = reader.readLine();
	        int totalLines = 0;
	        while (strLine != null) {
	            totalLines++;
	            strLine = reader.readLine();
	        }
	        reader.close();
	        in.close();
	        return totalLines;
		}else
			return 0;
    }
}