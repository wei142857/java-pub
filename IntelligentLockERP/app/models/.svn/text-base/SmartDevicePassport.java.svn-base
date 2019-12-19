/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package models;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author lzs
 */
@Entity
@Table(name = "smart_device_passport")

public class SmartDevicePassport implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idd")
    private Long idd; //ID
    @Column(name = "deviceid")
    private String deviceid; //设备ID
    @Column(name = "password")
    private String password;  //密码
    @Column(name = "type")
    private Integer type;  //密码类型
    @Column(name = "idx")
    private String idx;  //密码编号
    @Column(name = "userinfo")
    private String userinfo;  //关联用户
    @Column(name = "userid")
    private Integer userid;  //关联用户ID
    @Column(name = "extinfo")
    private String extinfo;  //附属信息
    @Column(name = "addtime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date addtime;  //添加时间
    
    @Column(name = "validetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date validetime;  //到期时间
    
    @Column(name = "begintime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date begintime;   //开始发送的时间
    
    public Date getBegintime() {
		return begintime;
	}

	public void setBegintime(Date begintime) {
		this.begintime = begintime;
	}
    
    @Column(name = "stat")
    private Integer stat;  //密码状态
    

    public Integer getStat() {
		return stat;
	}

	public void setStat(Integer stat) {
		this.stat = stat;
	}

	public Date getValidetime() {
		return validetime;
	}

	public void setValidetime(Date validetime) {
		this.validetime = validetime;
	}

	public SmartDevicePassport() {
		stat = 0;
    	setAddtime(new Date());
    }

    public SmartDevicePassport(Long idd) {
        this.idd = idd;
    }

    public Long getIdd() {
        return idd;
    }

    public void setIdd(Long idd) {
        this.idd = idd;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getIdx() {
        return idx;
    }

    public void setIdx(String idx) {
        this.idx = idx;
    }

    public String getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(String userinfo) {
        this.userinfo = userinfo;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getExtinfo() {
        return extinfo;
    }

    public void setExtinfo(String extinfo) {
        this.extinfo = extinfo;
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idd != null ? idd.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SmartDevicePassport)) {
            return false;
        }
        SmartDevicePassport other = (SmartDevicePassport) object;
        if ((this.idd == null && other.idd != null) || (this.idd != null && !this.idd.equals(other.idd))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "models.SmartDevicePassport[ idd=" + idd + " ]";
    }
    
}
