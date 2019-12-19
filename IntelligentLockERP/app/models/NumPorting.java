package models;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "numporting")
public class NumPorting implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idd")
    private Integer idd;
	@Column(name="phone")
	private String phone;//手机号
	@Column(name="province")
	private String province;//省份
	@Column(name="city")
	private String city;//城市
	@Column(name="province_no")
	private Integer provinceNo;//省份编码
	@Column(name="city_no")
	private Integer cityNo;//城市编码
	public Integer getIdd() {
		return idd;
	}
	public void setIdd(Integer idd) {
		this.idd = idd;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
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
	public Integer getProvinceNo() {
		return provinceNo;
	}
	public void setProvinceNo(Integer provinceNo) {
		this.provinceNo = provinceNo;
	}
	public Integer getCityNo() {
		return cityNo;
	}
	public void setCityNo(Integer cityNo) {
		this.cityNo = cityNo;
	}
	
}
