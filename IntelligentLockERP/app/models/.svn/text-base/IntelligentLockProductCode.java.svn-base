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
 *  - 锁编码表
 * 2019年2月19日下午3:08:33
 */
@Entity
@Table(name="intelligentlock_product_code")
public class IntelligentLockProductCode implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Basic(optional=false)
	@Column(name="idd")
	private Integer idd;	//ID
	
	@Column(name="code")
	private String code;	//锁唯一编码
	
	@Column(name="deviceid")
	private String deviceid;	//设备ID
	
	@Column(name="addtime")
	private Date addtime;	//添加时间
	
	@Column(name="productid")
	private Integer productid;	//产品ID
	
	@Column(name="status")
	private Integer status;	//状态
	
	@Column(name="batchid")
	private String batchid;	//生产批次
	
	@Column(name="operatornumber")
	private String operatornumber;	//操作号
	
	@Column(name="remark")
	private String remark;	//备注

	public Integer getIdd() {
		return idd;
	}

	public void setIdd(Integer idd) {
		this.idd = idd;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}

	public Integer getProductid() {
		return productid;
	}

	public void setProductid(Integer productid) {
		this.productid = productid;
	}

	public String getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getBatchid() {
		return batchid;
	}

	public void setBatchid(String batchid) {
		this.batchid = batchid;
	}

	public String getOperatornumber() {
		return operatornumber;
	}

	public void setOperatornumber(String operatornumber) {
		this.operatornumber = operatornumber;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "IntelligentLockProductCode [idd=" + idd + ", code=" + code + ", addtime=" + addtime + ", productid="
				+ productid + ", deviceid=" + deviceid + ", status=" + status + ", batchid=" + batchid
				+ ", operatornumber=" + operatornumber + ", remark=" + remark + "]";
	}

}
