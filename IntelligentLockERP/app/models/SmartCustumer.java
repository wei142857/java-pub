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
@Table(name = "smart_custumer")

public class SmartCustumer implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idd")
    private Integer idd; //ID
    @Column(name = "name")
    private String name;  //名字
    @Column(name = "grage")
    private Integer grage; //级别
    @Column(name = "countory")
    private String countory;  //国家
    @Column(name = "prov")
    private String prov;   //省
    @Column(name = "city")
    private String city;  //市
    @Column(name = "birthday")
    @Temporal(TemporalType.DATE)
    private Date birthday;  //生日
    @Column(name = "sex")
    private String sex;  //性别
    @Column(name = "phone")
    private String phone;  //手机
    @Column(name = "contact")
    private String contact; //联系方式
    @Column(name = "address")
    private String address; //地址
    @Column(name = "email")
    private String email; //邮箱
    @Column(name = "addtime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date addtime;  //添加时间

    public SmartCustumer() {
    }

    public SmartCustumer(Integer idd) {
        this.idd = idd;
    }

    public Integer getIdd() {
        return idd;
    }

    public void setIdd(Integer idd) {
        this.idd = idd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getGrage() {
        return grage;
    }

    public void setGrage(Integer grage) {
        this.grage = grage;
    }

    public String getCountory() {
        return countory;
    }

    public void setCountory(String countory) {
        this.countory = countory;
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

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
        if (!(object instanceof SmartCustumer)) {
            return false;
        }
        SmartCustumer other = (SmartCustumer) object;
        if ((this.idd == null && other.idd != null) || (this.idd != null && !this.idd.equals(other.idd))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "models.SmartCustumer[ idd=" + idd + " ]";
    }
    
}
