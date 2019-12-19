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
 * 出库明细 和 锁编码 的对应关系的实体类
 * @author 陈宏亮
 * @date   2019/6/12 13:41
 *
 */
@Entity
@Table(name="intelligentlock_out_detail_code")
public class IntelligentLockOutDetailCode implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Basic(optional=false)
	@Column(name="idd")		//ID
	private Integer idd;
	
	@Column(name = "outdetailid")	//出库明细ID
	private Integer outdetailid;
	
	@Column(name = "codeid")	//锁编码表ID
	private Integer codeid;
	
	@Column(name="addtime")
	private Date addtime;	//添加时间
	
	@Column(name="operatornumber")
	private String operatornumber;	//操作号

	public Integer getIdd() {
		return idd;
	}

	public void setIdd(Integer idd) {
		this.idd = idd;
	}

	public Integer getOutdetailid() {
		return outdetailid;
	}

	public void setOutdetailid(Integer outdetailid) {
		this.outdetailid = outdetailid;
	}

	public Integer getCodeid() {
		return codeid;
	}

	public void setCodeid(Integer codeid) {
		this.codeid = codeid;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "IntelligentLockOutDetailCode [idd=" + idd + ", outdetailid="
				+ outdetailid + ", codeid=" + codeid + ", addtime=" + addtime
				+ ", operatornumber=" + operatornumber + "]";
	}
}
