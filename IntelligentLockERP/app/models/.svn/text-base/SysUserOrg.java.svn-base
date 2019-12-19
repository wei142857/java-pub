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
@Table(name = "sysuserorg")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SysUserOrg.findAll", query = "SELECT s FROM SysUserOrg s"),
    @NamedQuery(name = "SysUserOrg.findByIdx", query = "SELECT s FROM SysUserOrg s WHERE s.idx = :idx"),
    @NamedQuery(name = "SysUserOrg.findByUid", query = "SELECT s FROM SysUserOrg s WHERE s.uid = :uid"),
    @NamedQuery(name = "SysUserOrg.findByOid", query = "SELECT s FROM SysUserOrg s WHERE s.oid = :oid"),
    @NamedQuery(name = "SysUserOrg.findByAddtime", query = "SELECT s FROM SysUserOrg s WHERE s.addtime = :addtime"),
    @NamedQuery(name = "SysUserOrg.findByState", query = "SELECT s FROM SysUserOrg s WHERE s.state = :state"),
    @NamedQuery(name = "SysUserOrg.findByOdomin", query = "SELECT s FROM SysUserOrg s WHERE s.odomin = :odomin")})
public class SysUserOrg implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idx")
    private Integer idx;
    @Column(name = "uid")
    private Integer uid;
    @Column(name = "oid")
    private Integer oid;
    @Column(name = "addtime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date addtime;
    @Column(name = "state")
    private Integer state;
    @Column(name = "odomin")
    private String odomin;

    public SysUserOrg() {
    }

    public SysUserOrg(Integer idx) {
        this.idx = idx;
    }

    public Integer getIdx() {
        return idx;
    }

    public void setIdx(Integer idx) {
        this.idx = idx;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getOid() {
        return oid;
    }

    public void setOid(Integer oid) {
        this.oid = oid;
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getOdomin() {
        return odomin;
    }

    public void setOdomin(String odomin) {
        this.odomin = odomin;
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
        if (!(object instanceof SysUserOrg)) {
            return false;
        }
        SysUserOrg other = (SysUserOrg) object;
        if ((this.idx == null && other.idx != null) || (this.idx != null && !this.idx.equals(other.idx))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "javaapplication19.SysUserOrg[ idx=" + idx + " ]";
    }
    
}
