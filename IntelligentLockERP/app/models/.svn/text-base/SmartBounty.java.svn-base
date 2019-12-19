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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
/**
 * 该Model为奖励金的实体类
 * 2019/5/27
 * @author 陈宏亮
 *
 */
@Entity
@Table(name = "smart_bounty")
public class SmartBounty implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idd")
    private Integer idd;  //ID
 
 	@Column(name = "userid")
    private Integer userid;  //用户ID
 	
 	@Column(name = "addtime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date addtime;   //添加时间
 	
 	@Column(name = "bvalue")
 	private Integer bvalue;	//交易额
 	
 	@Column(name = "overtime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date overtime;	//过期时间
 	
 	@Column(name = "titile")
 	private String titile;	//标题
 	
 	@Column(name = "act")
 	private Integer act;	//收入1 支出2
 	
 	@Column(name = "remark")
 	private String reamark;	//备注
 	
 	@Column(name = "currentbvalue")
 	private Integer currentbvalue;	//当前结余奖励金

	public Integer getIdd() {
		return idd;
	}

	public void setIdd(Integer idd) {
		this.idd = idd;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}

	public Integer getBvalue() {
		return bvalue;
	}

	public void setBvalue(Integer bvalue) {
		this.bvalue = bvalue;
	}

	public Date getOvertime() {
		return overtime;
	}

	public void setOvertime(Date overtime) {
		this.overtime = overtime;
	}

	public String getTitile() {
		return titile;
	}

	public void setTitile(String titile) {
		this.titile = titile;
	}

	public Integer getAct() {
		return act;
	}

	public void setAct(Integer act) {
		this.act = act;
	}

	public String getReamark() {
		return reamark;
	}

	public void setReamark(String reamark) {
		this.reamark = reamark;
	}

	public Integer getCurrentbvalue() {
		return currentbvalue;
	}

	public void setCurrentbvalue(Integer currentbvalue) {
		this.currentbvalue = currentbvalue;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "SmartBounty [idd=" + idd + ", userid=" + userid + ", addtime="
				+ addtime + ", bvalue=" + bvalue + ", overtime=" + overtime
				+ ", title=" + titile + ", act=" + act + ", reamark=" + reamark
				+ ", currentbvalue=" + currentbvalue + "]";
	}
}
