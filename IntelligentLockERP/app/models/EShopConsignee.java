package models;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "eshop_consignee")
public class EShopConsignee implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "idd")	
	private Integer idd;		//ID
	
	@Column(name = "userid")
	private Integer userid;		//用户ID
	
	@Column(name = "area")
	private String area;		//收件人地区
	
	@Column(name = "address")
	private String address;		//收件人详细地址
	
	@Column(name = "name")
	private String name;		//收件人姓名		
	
	@Column(name = "phone")
	private String phone;		//收件人手机号
	
	@Column(name = "addtime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date addtime;		//添加时间
	
	@Column(name = "isdefault")
	private Integer isdefault;	//是否为默认地址 0:默认 1:非默认

	public Integer getIdd() {
		return idd;
	}

	public void setIdd(Integer idd) {
		this.idd = idd;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}

	public Integer getIsdefault() {
		return isdefault;
	}

	public void setIsdefault(Integer isdefault) {
		this.isdefault = isdefault;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return "EShopConsignee [idd=" + idd + ", userid=" + userid + ", area="
				+ area + ", address=" + address + ", name=" + name + ", phone="
				+ phone + ", addtime=" + addtime + ", isdefault=" + isdefault
				+ "]";
	}
}
