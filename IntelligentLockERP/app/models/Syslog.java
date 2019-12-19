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
@Table(name = "syslog")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Syslog.findAll", query = "SELECT s FROM Syslog s"),
    @NamedQuery(name = "Syslog.findByIdd", query = "SELECT s FROM Syslog s WHERE s.idd = :idd"),
    @NamedQuery(name = "Syslog.findByModule", query = "SELECT s FROM Syslog s WHERE s.module = :module"),
    @NamedQuery(name = "Syslog.findByFcode", query = "SELECT s FROM Syslog s WHERE s.fcode = :fcode"),
    @NamedQuery(name = "Syslog.findByUri", query = "SELECT s FROM Syslog s WHERE s.uri = :uri"),
    @NamedQuery(name = "Syslog.findByUser", query = "SELECT s FROM Syslog s WHERE s.user = :user"),
    @NamedQuery(name = "Syslog.findByAddtime", query = "SELECT s FROM Syslog s WHERE s.addtime = :addtime"),
    @NamedQuery(name = "Syslog.findByMsg", query = "SELECT s FROM Syslog s WHERE s.msg = :msg"),
    @NamedQuery(name = "Syslog.findByIp", query = "SELECT s FROM Syslog s WHERE s.ip = :ip")})
public class Syslog implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idd")
    private Long idd;
    @Column(name = "module")
    private String module;
    @Column(name = "fcode")
    private String fcode;
    @Column(name = "uri")
    private String uri;
    @Column(name = "user")
    private String user;
    @Column(name = "addtime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date addtime;
    @Column(name = "msg")
    private String msg;
    @Column(name = "ip")
    private String ip;

    public Syslog() {
    }

    public Syslog(Long idd) {
        this.idd = idd;
    }

    public Long getIdd() {
        return idd;
    }

    public void setIdd(Long idd) {
        this.idd = idd;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getFcode() {
        return fcode;
    }

    public void setFcode(String fcode) {
        this.fcode = fcode;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
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
        if (!(object instanceof Syslog)) {
            return false;
        }
        Syslog other = (Syslog) object;
        if ((this.idd == null && other.idd != null) || (this.idd != null && !this.idd.equals(other.idd))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "models.Syslog[ idd=" + idd + " ]";
    }
    
}
