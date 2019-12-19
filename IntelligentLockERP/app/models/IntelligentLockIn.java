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
 *  - 入库单
 *2019年2月1日上午10:44:40
 */
@Entity
@Table(name="intelligentlock_in")
public class IntelligentLockIn implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Basic(optional=false)
	@Column(name="idd")
	private Integer idd;	//idd
	
	@Column(name="serialnumber")
	private String serialnumber;	//流水号
	
	@Column(name="operator")
	private String operator;	//操作人
	
	@Column(name="addtime")
	private Date addtime;	//添加时间
	
	@Column(name="operatnumber")
	private String operatnumber;	//操作号
	
	@Column(name="totalprice")
	private Double totalprice;	//总价
	
	@Column(name="type")
	private Integer type;	//状态
	
	@Column(name="remark")
	private String remark;	//备注
	
	@Column(name="productcount")
	private String productcount; //总数量

	public String getProductcount() {
		return productcount;
	}

	public void setProductcount(String productcount) {
		this.productcount = productcount;
	}

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

	public String getOperatnumber() {
		return operatnumber;
	}

	public void setOperatnumber(String operatnumber) {
		this.operatnumber = operatnumber;
	}

	public Double getTotalprice() {
		return totalprice;
	}

	public void setTotalprice(Double totalprice) {
		this.totalprice = totalprice;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "IntelligentLockIn [idd=" + idd + ", serialnumber=" + serialnumber + ", operator=" + operator
				+ ", addtime=" + addtime + ", operatnumber=" + operatnumber + ", totalprice=" + totalprice + ", type="
				+ type + ", remark=" + remark + ", productcount=" + productcount + "]";
	}
	
}
