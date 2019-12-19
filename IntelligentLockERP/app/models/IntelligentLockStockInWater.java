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
 * @author 魏全振
 *  - 入库流水
 *2019年2月11日上午10:58:28
 */
@Entity
@Table(name="intelligentlock_stock_in_water")
public class IntelligentLockStockInWater implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Basic(optional=false)
	@Column(name="idd")
	private Integer idd;	//ID
	
	@Column(name="inid")
	private Integer inid;	//入库单ID
	
	@Column(name="indetailid")
	private Integer indetailid;	//入库明细ID
	
	@Column(name="trascatnumber")
	private Integer trascatnumber;	//本次交易数
	
	@Column(name="productid")
	private Integer productid;	//商品ID
	
	@Column(name="batchid")
	private String batchid;	//批ID
	
	@Column(name="operator")
	private String operator;	//操作人
	
	@Column(name="addtime")
	private Date addtime;	//添加时间
	
	@Column(name="operatnumber")
	private String operatnumber;	//操作号

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

	public Integer getIndetailid() {
		return indetailid;
	}

	public void setIndetailid(Integer indetailid) {
		this.indetailid = indetailid;
	}

	public Integer getTrascatnumber() {
		return trascatnumber;
	}

	public void setTrascatnumber(Integer trascatnumber) {
		this.trascatnumber = trascatnumber;
	}

	public Integer getProductid() {
		return productid;
	}

	public void setProductid(Integer productid) {
		this.productid = productid;
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

	public String getOperatnumber() {
		return operatnumber;
	}

	public void setOperatnumber(String operatnumber) {
		this.operatnumber = operatnumber;
	}

	@Override
	public String toString() {
		return "IntelligentLockStockInWater [idd=" + idd + ", inid=" + inid + ", indetailid=" + indetailid
				+ ", trascatnumber=" + trascatnumber + ", productid=" + productid + ", batchid=" + batchid
				+ ", operator=" + operator + ", addtime=" + addtime + ", operatnumber=" + operatnumber + "]";
	}
	
}
