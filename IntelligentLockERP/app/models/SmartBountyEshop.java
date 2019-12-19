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
 * 该model类为用户使用奖励金去商城购买商品,
 * 生成未支付订单后,暂时将奖励金扣除,将扣除
 * 奖励金的记录保存在smart_bounty_eshop中
 * @author 陈宏亮
 * @date 2019年7月10日 15:50
 */
@Entity
@Table(name= "smart_bounty_eshop")
public class SmartBountyEshop implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idd")
    private Integer idd;  //ID
	
	@Column(name = "bid")
	private Integer bid;	//bountyId
	
	@Column(name = "oid")
	private Integer oid;	//orderId
	
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

	public Integer getOid() {
		return oid;
	}

	public void setOid(Integer oid) {
		this.oid = oid;
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
		return "SmartBountyEshop [idd=" + idd + ", bid=" + bid + ", oid=" + oid
				+ ", bvalue=" + bvalue + ", addtime=" + addtime + "]";
	}
}
