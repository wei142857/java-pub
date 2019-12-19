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
 * 作者:魏全振
 *  - 入库单明细
 *2019年2月11日上午10:34:49
 */
@Entity
@Table(name="intelligentlock_in_detail")
public class IntelligentLockInDetail implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Basic(optional=false)
	@Column(name="idd")
	private Integer idd;	//ID
	
	@Column(name="inid")
	private Integer inid;	//入库单ID
	
	@Column(name="productid")
	private Integer productid;	//产品ID	

	@Column(name="producttype")
	private String producttype;	//产品类型

	@Column(name="title")
	private String title;	//产品名称

	@Column(name="model")
	private String model;	//产品型号

	@Column(name="spec")
	private String spec;	//规格

	@Column(name="manufacturer")
	private String manufacturer;	//生产厂家
	
	@Column(name="deviceid")
	private String deviceid;	//设备ID

	@Column(name="batchid")
	private String batchid;	//生产批次

	@Column(name="productusage")
	private String productusage;	//产品用途
	
	@Column(name="inType")
	private String inType;	//入库方式

	@Column(name="amount")
	private Integer amount;	//数量

	@Column(name="price")
	private Double price;	//单价

	@Column(name="totalprice")
	private Double totalprice;	//总价

	@Column(name="remark")
	private String remark;	//备注

	@Column(name="addtime")
	private Date addtime;	//添加时间
	
	@Column(name="operator")
	private String operator;	//操作人

	@Column(name="operatenumber")
	private String operatenumber;	//操作号

	public Integer getIdd() {
		return idd;
	}

	public void setIdd(Integer idd) {
		this.idd = idd;
	}

	public Integer getInid() {
		return inid;
	}

	public void setInid(Integer inid) {
		this.inid = inid;
	}

	public Integer getProductid() {
		return productid;
	}

	public void setProductid(Integer productid) {
		this.productid = productid;
	}

	public String getProducttype() {
		return producttype;
	}

	public void setProducttype(String producttype) {
		this.producttype = producttype;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}

	public String getBatchid() {
		return batchid;
	}

	public void setBatchid(String batchid) {
		this.batchid = batchid;
	}

	public String getProductusage() {
		return productusage;
	}

	public void setProductusage(String productusage) {
		this.productusage = productusage;
	}

	public String getInType() {
		return inType;
	}

	public void setInType(String inType) {
		this.inType = inType;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getTotalprice() {
		return totalprice;
	}

	public void setTotalprice(Double totalprice) {
		this.totalprice = totalprice;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getOperatenumber() {
		return operatenumber;
	}

	public void setOperatenumber(String operatenumber) {
		this.operatenumber = operatenumber;
	}

	@Override
	public String toString() {
		return "IntelligentLockInDetail [idd=" + idd + ", inid=" + inid + ", productid=" + productid + ", producttype="
				+ producttype + ", title=" + title + ", model=" + model + ", spec=" + spec + ", manufacturer="
				+ manufacturer + ", deviceid=" + deviceid + ", batchid=" + batchid + ", productusage=" + productusage
				+ ", inType=" + inType + ", amount=" + amount + ", price=" + price + ", totalprice=" + totalprice
				+ ", remark=" + remark + ", addtime=" + addtime + ", operator=" + operator + ", operatenumber="
				+ operatenumber + "]";
	}
	
}
