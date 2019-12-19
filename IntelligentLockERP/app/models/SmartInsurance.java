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

@Entity
@Table(name = "smart_insurance")
public class SmartInsurance implements Serializable{
	private static final long serialVersionUID = -3297738310529942691L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
	@Column(name = "idd")
	private Integer idd;
	
	@Column(name = "deviceid")
	private String deviceid;//设备ID
	
	@Column(name = "policyno")
	private String policyno;//保单号

	@Column(name = "addtime")
	private Date addtime;//添加时间
	
	@Column(name = "begintime")
	private Date begintime;//起报时间
	
	@Column(name = "overtime")
	private Date overtime;//终保日期
	
	@Column(name = "validity")
	private Integer validity;//有效期
	
	@Column(name = "name")
	private String name;//被保人姓名
	
	@Column(name = "cardtype")
	private Integer cardtype;//证件类型 1身份证
	
	@Column(name = "cardnum")
	private String cardnum;//证件号
	
	@Column(name = "phone")
	private String phone;//手机号
	
	@Column(name = "email")
	private String email;//邮箱
	
	@Column(name = "province")
	private String province;//省
	
	@Column(name = "city")
	private String city;//市
	
	@Column(name = "district")
	private String district;//区
	
	@Column(name = "address")
	private String address;//详细地址
	
	@Column(name = "status")
	private Integer status;//状态
	
	@Column(name = "coverage")
	private Integer coverage;//保额
	
	@Column(name = "remark")
	private String remark;//备注
	
	@Column(name = "billno")
	private String billno;//支付订单号
	
	@Column(name = "warrantyurl")
	private String warrantyurl;//电子保单URL
	

	public String getWarrantyurl() {
		return warrantyurl;
	}

	public void setWarrantyurl(String warrantyurl) {
		this.warrantyurl = warrantyurl;
	}

	public String getBillno() {
		return billno;
	}

	public void setBillno(String billno) {
		this.billno = billno;
	}

	public Integer getIdd() {
		return idd;
	}

	public void setIdd(Integer idd) {
		this.idd = idd;
	}

	public String getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}

	public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}

	public Date getBegintime() {
		return begintime;
	}

	public void setBegintime(Date begintime) {
		this.begintime = begintime;
	}

	public Date getOvertime() {
		return overtime;
	}

	public String getPolicyno() {
		return policyno;
	}

	public void setPolicyno(String policyno) {
		this.policyno = policyno;
	}

	public void setOvertime(Date overtime) {
		this.overtime = overtime;
	}

	public Integer getValidity() {
		return validity;
	}

	public void setValidity(Integer validity) {
		this.validity = validity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getCardtype() {
		return cardtype;
	}

	public void setCardtype(Integer cardtype) {
		this.cardtype = cardtype;
	}

	public String getCardnum() {
		return cardnum;
	}

	public void setCardnum(String cardnum) {
		this.cardnum = cardnum;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getCoverage() {
		return coverage;
	}

	public void setCoverage(Integer coverage) {
		this.coverage = coverage;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "SmartInsurance [idd=" + idd + ", deviceid=" + deviceid + ", policyno=" + policyno + ", addtime="
				+ addtime + ", begintime=" + begintime + ", overtime=" + overtime + ", validity=" + validity + ", name="
				+ name + ", cardtype=" + cardtype + ", cardnum=" + cardnum + ", phone=" + phone + ", email=" + email
				+ ", province=" + province + ", city=" + city + ", district=" + district + ", address=" + address
				+ ", status=" + status + ", coverage=" + coverage + ", remark=" + remark + ", billno=" + billno
				+ ", warrantyurl=" + warrantyurl + "]";
	}
}
