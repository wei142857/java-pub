package util.picture;
/**
 * @Description: 文件类型对应二进制文件头 ,文件头128位
 * @Author chenkangxian   
 * @Date 2013-8-25 下午6:57:52 
 * @Copyright: 2012 chenkangxian, All rights reserved.
 **/
public enum FileType {
	/**
	 * JEPG.
	 */
	JPEG("FFD8FF"),
	
	/**
	 * PNG.
	 */
	PNG("89504E47"),
	
	/**
	 * GIF.
	 */
	GIF("47494638"),
	
	/**
	 * TIFF.
	 */
	TIFF("49492A00"),
	
	/**
	 * Windows Bitmap.
	 */
	BMP("424D"),
	
	/**
	 * CAD.
	 */
	DWG("41433130"),
	
	/**
	 * Adobe Photoshop.
	 */
	PSD("38425053"),
	
	/**
	 * XML.
	 */
	XML("3C3F786D6C"),
	
	/**
	 * HTML.
	 */
	HTML("68746D6C3E"),
	
	/**
	 * Adobe Acrobat.
	 */
	PDF("255044462D312E"),
	
	/**
	 * ZIP Archive.
	 */
	ZIP("504B0304"),
	
	/**
	 * RAR Archive.
	 */
	RAR("52617221"),
	
	/**
	 * Wave.
	 */
	WAV("57415645"),
	
	/**
	 * AVI.
	 */
	AVI("41564920");
	
	private String value = "";
	
	private FileType(String value){
		this.value = value;
	}
	
	public String getValue(){
		return value;
	}
	
	public void setValue(String value){
		this.value = value;
	}
	
}
