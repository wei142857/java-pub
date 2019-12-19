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
 * 出库单的实体类
 * @author 陈宏亮
 * @date	2019年1月31号
 */

@Entity
@Table(name="intelligentlock_out")
public class IntelligentLockOut implements Serializable{
	
	private static final long serialVersionUID = -8047076741484381535L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "idd")
	private Integer idd;	//出货单ID
	
	@Column(name = "serialnumber")
	private String serialnumber;	//流水号
	
	@Column(name = "operator")
	private String operator;	//操作人
	
	@Column(name = "addtime")
	private Date addtime;	//添加时间
	
	@Column(name = "operatornumber")
	private String operatornumber;	//操作号
	
	@Column(name = "totalprice")
	private Double totalprice;	//总价
	
	@Column(name = "remark")
	private String remark;	//备注
	
	public Integer getIdd() {
		return idd;
	}

	public void setIdd(Integer idd) {
		this.idd = idd;
	}

	public String getSerialnumber() {
		return serialnumber;
	}

	public void setSerialnumber(String serialnumber) {
		this.serialnumber = serialnumber;
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

	@Override
	public String toString() {
		return "IntelligentLockOut [idd=" + idd + ", serialnumber="
				+ serialnumber + ", operator=" + operator + ", addtime="
				+ addtime + ", operatornumber=" + operatornumber
				+ ", totalprice=" + totalprice + ", remark=" + remark + "]";
	}
}
