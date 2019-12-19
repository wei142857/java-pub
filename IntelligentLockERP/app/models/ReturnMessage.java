package models;

import java.io.Serializable;

/**
 * 创建ReturnMessage实体类
 * 用于return给页面处理结果
 * @author chl
 *
 */
public class ReturnMessage implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String code;	//自定义状态码
	
	private String message;	//提示信息
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
