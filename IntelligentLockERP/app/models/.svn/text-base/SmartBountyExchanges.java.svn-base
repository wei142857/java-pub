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
 * 该Model为奖励金扣除时 记录扣除奖励金和兑换记录的关系
 * @author 陈宏亮
 * @date 2019/6/13 15:57
 *
 */
@Entity
@Table(name = "smart_bounty_exchanges")
public class SmartBountyExchanges implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idd")
    private Integer idd;  //ID
	
	@Column(name = "bid")
	private Integer bid;	//bountyId
	
	@Column(name = "eid")
	private Integer eid;	//exchangeId
	
	@Column(name = "bvalue")
	private Integer bvalue;		//消耗该笔记录的奖励金个数
	
	@Column(name = "addtime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date addtime;   //添加时间

	public Integer getIdd() {
		return idd;
	}

	public void setIdd(Integer idd) {
		this.idd = idd;
	}

	public Integer getBid() {
		return bid;
	}

	public void setBid(Integer bid) {
		this.bid = bid;
	}

	public Integer getEid() {
		return eid;
	}

	public void setEid(Integer eid) {
		this.eid = eid;
	}

	public Integer getBvalue() {
		return bvalue;
	}

	public void setBvalue(Integer bvalue) {
		this.bvalue = bvalue;
	}

	public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "SmartBountyExchanges [idd=" + idd + ", bid=" + bid + ", eid="
				+ eid + ", bvalue=" + bvalue + ", addtime=" + addtime + "]";
	}
}
