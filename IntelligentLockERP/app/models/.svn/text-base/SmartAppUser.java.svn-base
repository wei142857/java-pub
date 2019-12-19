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
@Table(name = "smart_app_user")

public class SmartAppUser implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idd")
    private Integer idd;  //ID
    @Column(name = "login")
    private String login;  //登录名
    @Column(name = "usertype")
    private Integer usertype;  //用户来源
    @Column(name = "phone")
    private String phone;   //手机
    @Column(name = "addtime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date addtime;   //注册时间
    @Column(name = "appid")
    private String appid;  //APPID
    @Column(name = "ip")
    private String ip;  //IP
    public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}

	@Column(name = "password")
    private String password;  //密码
	@Column(name = "channel")
	private String channel;//渠道
    @Column(name = "appversion")
    private String appversion;  //APP版本
    @Column(name = "appos")
    private String appos;   //手机系统
    @Column(name = "phonetype")
    private String phonetype;  //机型
    @Column(name = "deviceid")
    private String deviceid;  //设备ID
    @Column(name = "apptoken")
    private String apptoken;  //苹果ID
    @Column(name = "idfa")
    private String idfa;  //广告ID
    @Column(name = "logo")
    private String logo;  //头像
    @Column(name = "nickname")
    private String nickname;  //昵称
    @Column(name = "lastlogintime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastlogintime;  //上次登录时间
    @Column(name = "status")
    private Integer status;  //状态
    @Column(name = "prov")
    private String prov;  //省
    @Column(name = "city")
    private String city;  //市
    @Column(name = "yys")
    private String yys; //运营商
    @Column(name = "pushstate")
    private Integer pushstate;  //推送开关
    @Column(name = "illegalpushstate")
    private String illegalpushstate;  //非法通知推送开关

    public SmartAppUser() {
    }

    public SmartAppUser(Integer idd) {
        this.idd = idd;
    }

    public Integer getIdd() {
        return idd;
    }

    public void setIdd(Integer idd) {
        this.idd = idd;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Integer getUsertype() {
        return usertype;
    }

    public void setUsertype(Integer usertype) {
        this.usertype = usertype;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAppversion() {
        return appversion;
    }

    public void setAppversion(String appversion) {
        this.appversion = appversion;
    }

    public String getAppos() {
        return appos;
    }

    public void setAppos(String appos) {
        this.appos = appos;
    }

    public String getPhonetype() {
        return phonetype;
    }

    public void setPhonetype(String phonetype) {
        this.phonetype = phonetype;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getApptoken() {
        return apptoken;
    }

    public void setApptoken(String apptoken) {
        this.apptoken = apptoken;
    }

    public String getIdfa() {
        return idfa;
    }

    public void setIdfa(String idfa) {
        this.idfa = idfa;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Date getLastlogintime() {
        return lastlogintime;
    }

    public void setLastlogintime(Date lastlogintime) {
        this.lastlogintime = lastlogintime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getProv() {
        return prov;
    }

    public void setProv(String prov) {
        this.prov = prov;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getYys() {
        return yys;
    }

    public void setYys(String yys) {
        this.yys = yys;
    }

    public Integer getPushstate() {
        return pushstate;
    }

    public void setPushstate(Integer pushstate) {
        this.pushstate = pushstate;
    }

    public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	
	public String getIllegalpushstate() {
		return illegalpushstate;
	}
	public void setIllegalpushstate(String illegalpushstate) {
		this.illegalpushstate = illegalpushstate;
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
        if (!(object instanceof SmartAppUser)) {
            return false;
        }
        SmartAppUser other = (SmartAppUser) object;
        if ((this.idd == null && other.idd != null) || (this.idd != null && !this.idd.equals(other.idd))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "models.SmartAppUser[ idd=" + idd + " ]";
    }
    
}
