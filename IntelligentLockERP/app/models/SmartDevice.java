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
@Table(name = "smart_device")

public class SmartDevice implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idd")
    private Integer idd;  //ID
    @Column(name = "type")
    private Integer type; //型号
    @Column(name = "deviceid")
    private String deviceid;  //设备ID
    @Column(name = "secret")
    private String secret;  //设备密码
    
    @Column(name = "power")
    private String power;  //电量
    
    public String getPower() {
		return power;
	}

	public void setPower(String power) {
		this.power = power;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	@Column(name = "imei")
    private String imei;   //IMEI
    @Column(name = "addtime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date addtime;  //添加时间
    @Column(name = "status")
    private Integer status; //状态
    @Column(name = "customerId")
    private Integer customerId; //客户ID
    @Column(name = "lastconnect")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastconnect; //上次连接时间
    @Column(name = "devicedesc")
    private String devicedesc; //命名
    @Column(name = "buytime")
    private Date buytime; //购买日期
    
   /* @Column(name = "version")
    private String version; //version
    
	public String getVersion() {
		return version;
	}*/

	/*public void setVersion(String version) {
		this.version = version;
	}*/

	public SmartDevice() {
    }

    public Date getBuytime() {
		return buytime;
	}

	public void setBuytime(Date buytime) {
		this.buytime = buytime;
	}

	public SmartDevice(Integer idd) {
        this.idd = idd;
    }

    public Integer getIdd() {
        return idd;
    }

    public void setIdd(Integer idd) {
        this.idd = idd;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Date getLastconnect() {
        return lastconnect;
    }

    public void setLastconnect(Date lastconnect) {
        this.lastconnect = lastconnect;
    }

    public String getDevicedesc() {
        return devicedesc;
    }

    public void setDevicedesc(String devicedesc) {
        this.devicedesc = devicedesc;
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
        if (!(object instanceof SmartDevice)) {
            return false;
        }
        SmartDevice other = (SmartDevice) object;
        if ((this.idd == null && other.idd != null) || (this.idd != null && !this.idd.equals(other.idd))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "models.SmartDevice[ idd=" + idd + " ]";
    }

    
}
