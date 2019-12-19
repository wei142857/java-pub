package models;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 生产厂家的实体类
 * @author	陈宏亮
 * @date	2019年1月31号
 *
 */
@Entity
@Table(name="intelligentlock_manufacturer")
public class IntelligentLockManufacturer implements Serializable{
	
	private static final long serialVersionUID = 1433906419132457660L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Basic(optional=false)
	@Column(name="idd")
	private Integer idd;
	
	@Column(name="name")
	private String name;	//名称
	
	@Column(name="telphone")
	private String telphone;	//联系电话
	
	@Column(name="address")
	private String address;	//地址
	
	@Column(name="addtime")
	private Date addtime;	//添加时间

	public Integer getIdd() {
		return idd;
	}

	public void setIdd(Integer idd) {
		this.idd = idd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTelphone() {
		return telphone;
	}

	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}
}