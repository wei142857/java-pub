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
 * 该Model为VIP的实体类
 * 2019/5/28
 * @author 陈宏亮
 *
 */
@Entity
@Table(name = "smart_vip")
public class SmartVip implements Serializable{

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
	
	@Column(name = "buytime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date buytime;   //购买时间
	
	@Column(name = "overtime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date overtime;   //过期时间
	
	@Column(name = "lastbuytime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastbuytime;   //上次购买时间
	
	@Column(name = "lastovertime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastovertime;   //上次过期时间
	
	@Column(name = "firstbuytime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date firstbuytime;   //第一次过期时间
	
	@Column(name = "deviceid")
    private String deviceid;   //最后一次绑定的设备ID
	
	@Column(name = "channel")
    private String channel;   //获取会员渠道

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

	public Date getBuytime() {
		return buytime;
	}

	public void setBuytime(Date buytime) {
		this.buytime = buytime;
	}

	public Date getOvertime() {
		return overtime;
	}

	public void setOvertime(Date overtime) {
		this.overtime = overtime;
	}

	public Date getLastbuytime() {
		return lastbuytime;
	}

	public void setLastbuytime(Date lastbuytime) {
		this.lastbuytime = lastbuytime;
	}

	public Date getLastovertime() {
		return lastovertime;
	}

	public void setLastovertime(Date lastovertime) {
		this.lastovertime = lastovertime;
	}

	public Date getFirstbuytime() {
		return firstbuytime;
	}

	public void setFirstbuytime(Date firstbuytime) {
		this.firstbuytime = firstbuytime;
	}

	public String getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "SmartVip [idd=" + idd + ", userid=" + userid + ", addtime="
				+ addtime + ", buytime=" + buytime + ", overtime=" + overtime
				+ ", lastbuytime=" + lastbuytime + ", lastovertime="
				+ lastovertime + ", firstbuytime=" + firstbuytime
				+ ", deviceid=" + deviceid + ", channel=" + channel + "]";
	}
}
