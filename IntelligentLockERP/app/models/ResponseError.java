package models;

import java.io.Serializable;

/**
 * 该类用于封装状态以及相应信息
 * status是1表明错误
 */
public class ResponseError implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Integer status;
	private String massage;
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getMassage() {
		return massage;
	}
	public void setMassage(String massage) {
		this.massage = massage;
	}
	public void setError(Integer status,String massage) {
		this.status = status;
		this.massage = massage;
	}
	@Override
	public String toString() {
		return "ResponseError [status=" + status + ", massage=" + massage + "]";
	}
	
}
