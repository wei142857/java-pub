package models;

import java.io.Serializable;

public class IntelligentLockOutDetailCodeExtend implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Integer outdetailid;	//出库明细ID
	
	private Integer codeid;			//产品编码表ID
	
	private String deviceid;		//设备ID
	
	private String code;			//产品编码

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getOutdetailid() {
		return outdetailid;
	}

	public void setOutdetailid(Integer outdetailid) {
		this.outdetailid = outdetailid;
	}

	public Integer getCodeid() {
		return codeid;
	}

	public void setCodeid(Integer codeid) {
		this.codeid = codeid;
	}

	public String getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return "IntelligentLockOutDetailCodeExtend [utdetailid=" + outdetailid + ", codeid=" + codeid
				+ ", deviceid=" + deviceid + ", code=" + code + "]";
	}
}
