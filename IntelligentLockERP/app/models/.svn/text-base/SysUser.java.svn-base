/*
 * To change this template, choose Tools | Templates
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
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "sysuser")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SysUser.findAll", query = "SELECT s FROM SysUser s"),
    @NamedQuery(name = "SysUser.findByIdx", query = "SELECT s FROM SysUser s WHERE s.idx = :idx"),
    @NamedQuery(name = "SysUser.findByLogin", query = "SELECT s FROM SysUser s WHERE s.login = :login"),
    @NamedQuery(name = "SysUser.findByPwd", query = "SELECT s FROM SysUser s WHERE s.pwd = :pwd"),
    @NamedQuery(name = "SysUser.findByName", query = "SELECT s FROM SysUser s WHERE s.name = :name"),
    @NamedQuery(name = "SysUser.findByPhone", query = "SELECT s FROM SysUser s WHERE s.phone = :phone"),
    @NamedQuery(name = "SysUser.findByEmail", query = "SELECT s FROM SysUser s WHERE s.email = :email"),
    @NamedQuery(name = "SysUser.findByState", query = "SELECT s FROM SysUser s WHERE s.state = :state"),
    @NamedQuery(name = "SysUser.findByAddtime", query = "SELECT s FROM SysUser s WHERE s.addtime = :addtime"),
    @NamedQuery(name = "SysUser.findByDomian", query = "SELECT s FROM SysUser s WHERE s.domian = :domian"),
    @NamedQuery(name = "SysUser.findBySex", query = "SELECT s FROM SysUser s WHERE s.sex = :sex"),
    @NamedQuery(name = "SysUser.findByBirthday", query = "SELECT s FROM SysUser s WHERE s.birthday = :birthday")})
public class SysUser implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idx")
    private Integer idx;
    @Basic(optional = false)
    @Column(name = "login")
    private String login;
    @Basic(optional = false)
    @Column(name = "pwd")
    private String pwd;
    @Basic(optional = false)
    @Column(name = "pwdmd5")
    private String pwdmd5;
    public String getPwdmd5() {
		return pwdmd5;
	}

	public void setPwdmd5(String pwdmd5) {
		this.pwdmd5 = pwdmd5;
	}


	@Column(name = "name")
    private String name;
    @Column(name = "phone")
    private String phone;
    @Column(name = "email")
    private String email;
    @Column(name = "state")
    private Integer state;
    @Column(name = "usertype")
    private Integer usertype;    // 用户类型  ： 0 - 普通，1 - 超级管理员，2 - 400客服 ,3-10010客服；
    @Column(name = "addtime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date addtime;
    @Column(name = "domian")
    private String domian;
    @Lob
    @Column(name = "ext")
    private String ext;
    @Column(name = "sex")
    private Integer sex;
    @Column(name = "birthday")
    @Temporal(TemporalType.DATE)
    private Date birthday;
    
    @Column(name = "orgid")
    private Integer orgid;     // 管理员帐户：可以是其他合作公司的人员，此处id关联 flowcompany的id

    public Integer getOrgid() {
		return orgid;
	}

	public void setOrgid(Integer orgid) {
		this.orgid = orgid;
	}

	public SysUser() {
    }

    public SysUser(Integer idx) {
        this.idx = idx;
    }

    public SysUser(Integer idx, String login, String pwd) {
        this.idx = idx;
        this.login = login;
        this.pwd = pwd;
    }

    public Integer getIdx() {
        return idx;
    }

    public void setIdx(Integer idx) {
        this.idx = idx;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }

    public String getDomian() {
        return domian;
    }

    public void setDomian(String domian) {
        this.domian = domian;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idx != null ? idx.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SysUser)) {
            return false;
        }
        SysUser other = (SysUser) object;
        if ((this.idx == null && other.idx != null) || (this.idx != null && !this.idx.equals(other.idx))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "javaapplication19.SysUser[ idx=" + idx + " ]";
    }

	public Integer getUsertype() {
		return usertype;
	}

	public void setUsertype(Integer usertype) {
		this.usertype = usertype;
	}
	
	
	//登录用户类型
	public static int ADMIN_USER_COMMON=0;   //普通人员
	public static int ADMIN_USER_SUPER=1;    //超管
	
	
	 
    
}
