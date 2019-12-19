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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "sysrolefunc")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SysRoleFunc.findAll", query = "SELECT s FROM SysRoleFunc s"),
    @NamedQuery(name = "SysRoleFunc.findByIdx", query = "SELECT s FROM SysRoleFunc s WHERE s.idx = :idx"),
    @NamedQuery(name = "SysRoleFunc.findByFid", query = "SELECT s FROM SysRoleFunc s WHERE s.fid = :fid"),
    @NamedQuery(name = "SysRoleFunc.findByRid", query = "SELECT s FROM SysRoleFunc s WHERE s.rid = :rid"),
    @NamedQuery(name = "SysRoleFunc.findByState", query = "SELECT s FROM SysRoleFunc s WHERE s.state = :state"),
    @NamedQuery(name = "SysRoleFunc.findByAddtime", query = "SELECT s FROM SysRoleFunc s WHERE s.addtime = :addtime")})
public class SysRoleFunc implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idx")
    private Integer idx;
    @Column(name = "fid")
    private Integer fid;
    @Column(name = "rid")
    private Integer rid;
    @Column(name = "state")
    private Integer state;
    @Column(name = "addtime")
    private Date addtime;

    public SysRoleFunc() {
    }

    public SysRoleFunc(Integer idx) {
        this.idx = idx;
    }

    public Integer getIdx() {
        return idx;
    }

    public void setIdx(Integer idx) {
        this.idx = idx;
    }

    public Integer getFid() {
        return fid;
    }

    public void setFid(Integer fid) {
        this.fid = fid;
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
        if (!(object instanceof SysRoleFunc)) {
            return false;
        }
        SysRoleFunc other = (SysRoleFunc) object;
        if ((this.idx == null && other.idx != null) || (this.idx != null && !this.idx.equals(other.idx))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "javaapplication19.SysRoleFunc[ idx=" + idx + " ]";
    }
    
}
