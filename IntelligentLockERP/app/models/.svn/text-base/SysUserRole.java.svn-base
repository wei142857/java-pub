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
@Table(name = "sysuserrole")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SysUserRole.findAll", query = "SELECT s FROM SysUserRole s"),
    @NamedQuery(name = "SysUserRole.findByIdx", query = "SELECT s FROM SysUserRole s WHERE s.idx = :idx"),
    @NamedQuery(name = "SysUserRole.findByUid", query = "SELECT s FROM SysUserRole s WHERE s.uid = :uid"),
    @NamedQuery(name = "SysUserRole.findByRid", query = "SELECT s FROM SysUserRole s WHERE s.rid = :rid"),
    @NamedQuery(name = "SysUserRole.findByState", query = "SELECT s FROM SysUserRole s WHERE s.state = :state"),
    @NamedQuery(name = "SysUserRole.findByAddtime", query = "SELECT s FROM SysUserRole s WHERE s.addtime = :addtime")})
public class SysUserRole implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idx")
    private Integer idx;
    @Column(name = "uid")
    private Integer uid;
    @Column(name = "rid")
    private Integer rid;
    @Column(name = "state")
    private Integer state;
    @Column(name = "addtime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date addtime;

    public SysUserRole() {
    }

    public SysUserRole(Integer idx) {
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

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idx != null ? idx.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SysUserRole)) {
            return false;
        }
        SysUserRole other = (SysUserRole) object;
        if ((this.idx == null && other.idx != null) || (this.idx != null && !this.idx.equals(other.idx))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "javaapplication19.SysUserRole[ idx=" + idx + " ]";
    }
    
}
