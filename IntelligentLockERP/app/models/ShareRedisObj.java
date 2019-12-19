package models;

import java.io.Serializable;

public class ShareRedisObj implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer userid;	//用户ID

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "ShareRedisObj [userid=" + userid + ", getUserid()="
				+ getUserid() + ", getClass()=" + getClass() + ", hashCode()="
				+ hashCode() + ", toString()=" + super.toString() + "]";
	}
}
