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
 * 库存明细的实体类
 * @author 陈宏亮
 * @date	2019年1月31号
 */
@Entity
@Table(name="intelligentlock_stock")
public class IntelligentLockStock implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "idd")
	private Integer idd;	//序号
	
	@Column(name = "productid")
	private Integer productid;	//产品ID
	
	@Column(name = "title")
	private String title;	//商品名称
	
	@Column(name = "facturername")
	private String facturername;	//生产厂家
	
	@Column(name = "lastsurplus")
	private Integer lastsurplus;	//上次结余
	
	@Column(name = "transcatnumber")
	private Integer transcatnumber;	//本次交易数
	
	@Column(name = "currentsurplus")
	private Integer currentsurplus;	//本次结余
	
	@Column(name = "batchid")
	private String batchid;	//生产批次
	
	@Column(name = "operator")
	private String operator;	//操作人
	
	@Column(name = "addtime")
	private Date addtime;	//添加时间
	
	@Column(name = "price")
	private Double price;	//单价

	public Integer getIdd() {
		return idd;
	}

	public void setIdd(Integer idd) {
		this.idd = idd;
	}

	public Integer getProductid() {
		return productid;
	}

	public void setProductid(Integer productid) {
		this.productid = productid;
	}

	public Integer getLastsurplus() {
		return lastsurplus;
	}

	public void setLastsurplus(Integer lastsurplus) {
		this.lastsurplus = lastsurplus;
	}

	public Integer getTranscatnumber() {
		return transcatnumber;
	}

	public void setTranscatnumber(Integer transcatnumber) {
		this.transcatnumber = transcatnumber;
	}

	public Integer getCurrentsurplus() {
		return currentsurplus;
	}

	public void setCurrentsurplus(Integer currentsurplus) {
		this.currentsurplus = currentsurplus;
	}

	public String getBatchid() {
		return batchid;
	}

	public void setBatchid(String batchid) {
		this.batchid = batchid;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFacturername() {
		return facturername;
	}

	public void setFacturername(String facturername) {
		this.facturername = facturername;
	}

	@Override
	public String toString() {
		return "IntelligentLockStock [idd=" + idd + ", productid=" + productid
				+ ", title=" + title + ", facturername=" + facturername
				+ ", lastsurplus=" + lastsurplus + ", transcatnumber="
				+ transcatnumber + ", currentsurplus=" + currentsurplus
				+ ", batchid=" + batchid + ", operator=" + operator
				+ ", addtime=" + addtime + ", price=" + price + "]";
	}
}
