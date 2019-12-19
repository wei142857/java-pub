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
 * 出库流水表对应的实体类
 * @author 陈宏亮
 * @date   2019年1月31号
 *
 */
@Entity
@Table(name="intelligentlock_stock_out_water")
public class IntelligentLockStockOutWater implements Serializable{
	
	private static final long serialVersionUID = -6442587919941579246L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "idd")
	private Integer idd;
	
	@Column(name = "outid")
	private Integer outid;	//出库单ID
	
	@Column(name = "trascatnumber")
	private Integer trascatnumber;	//本次交易数
	
	@Column(name = "productid")
	private Integer productid;	//产品ID
	
	@Column(name = "batchid")
	private String batchid;	//生产批次
	
	@Column(name = "operator")
	private String operator;	//操作人
	
	@Column(name = "addtime")
	private Date addtime;	//添加时间
	
	@Column(name = "operatornumber")
	private String operatornumber;	//操作号

	public Integer getIdd() {
		return idd;
	}

	public void setIdd(Integer idd) {
		this.idd = idd;
	}

	public Integer getOutid() {
		return outid;
	}

	public void setOutid(Integer outid) {
		this.outid = outid;
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

	public String getOperatornumber() {
		return operatornumber;
	}

	public void setOperatornumber(String operatornumber) {
		this.operatornumber = operatornumber;
	}

	@Override
	public String toString() {
		return "IntelligentLockStockOutWater [idd=" + idd + ", outid=" + outid
				+ ", trascatnumber=" + trascatnumber + ", productid="
				+ productid + ", batchid=" + batchid + ", operator=" + operator
				+ ", addtime=" + addtime + ", operatornumber=" + operatornumber
				+ "]";
	}
}
