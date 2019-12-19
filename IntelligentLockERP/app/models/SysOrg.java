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
@Table(name = "sysorg")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SysOrg.findAll", query = "SELECT s FROM SysOrg s"),
    @NamedQuery(name = "SysOrg.findByIdx", query = "SELECT s FROM SysOrg s WHERE s.idx = :idx"),
    @NamedQuery(name = "SysOrg.findByName", query = "SELECT s FROM SysOrg s WHERE s.name = :name"),
    @NamedQuery(name = "SysOrg.findByAddtime", query = "SELECT s FROM SysOrg s WHERE s.addtime = :addtime"),
    @NamedQuery(name = "SysOrg.findByDomain", query = "SELECT s FROM SysOrg s WHERE s.domain = :domain"),
    @NamedQuery(name = "SysOrg.findByState", query = "SELECT s FROM SysOrg s WHERE s.state = :state")})
public class SysOrg implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idx")
    private Integer idx;
    @Column(name = "name")
    private String name;
    @Column(name = "addtime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date addtime;
    @Column(name = "domain")
    private String domain;
    @Column(name = "state")
    private Integer state;
    @Lob
    @Column(name = "ext")
    private String ext;

    public SysOrg() {
    }

    public SysOrg(Integer idx) {
        this.idx = idx;
    }

    public Integer getIdx() {
        return idx;
    }

    public void setIdx(Integer idx) {
        this.idx = idx;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
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
        if (!(object instanceof SysOrg)) {
            return false;
        }
        SysOrg other = (SysOrg) object;
        if ((this.idx == null && other.idx != null) || (this.idx != null && !this.idx.equals(other.idx))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "javaapplication19.SysOrg[ idx=" + idx + " ]";
    }
    
}
