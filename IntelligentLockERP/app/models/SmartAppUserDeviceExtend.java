package models;

import java.io.Serializable;
import java.util.Date;

/**
 * 该实体类是用来保存用户和设备的绑定扩展信息的
 * @author 陈宏亮
 *
 */
public class SmartAppUserDeviceExtend implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Integer deviceidd;	//设备表ID
	
	private Integer userid;		//用户表ID
	
	private String	phone;		//用户手机号
	
	private String deviceid;	//设备号
	
	private Date addtime;		//绑定设备时间

	public Integer getDeviceidd() {
		return deviceidd;
	}

	public void setDeviceidd(Integer deviceidd) {
		this.deviceidd = deviceidd;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}

	@Override
	public String toString() {
		return "SmartAppUserDeviceExtend [deviceidd=" + deviceidd + ", userid="
				+ userid + ", phone=" + phone + ", deviceid=" + deviceid
				+ ", addtime=" + addtime + "]";
	}
}
