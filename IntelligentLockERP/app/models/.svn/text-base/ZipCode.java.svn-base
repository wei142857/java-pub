package models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "zipcode")
public class ZipCode implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "zip_id")	
	private Integer zip_id;
	
	@Column(name = "region_id")	
	private String region_id;
	
	@Column(name = "zip_number")	
	private String zip_number;
	
	@Column(name = "code")	
	private String code;
	public Integer getZip_id() {
		return zip_id;
	}
	public void setZip_id(Integer zip_id) {
		this.zip_id = zip_id;
	}
	public String getRegion_id() {
		return region_id;
	}
	public void setRegion_id(String region_id) {
		this.region_id = region_id;
	}
	public String getZip_number() {
		return zip_number;
	}
	public void setZip_number(String zip_number) {
		this.zip_number = zip_number;
	}
	@Override
	public String toString() {
		return "ZipCode [zip_id=" + zip_id + ", region_id=" + region_id + ", zip_number=" + zip_number + ", code="
				+ code + "]";
	}
}
