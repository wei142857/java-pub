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
@Table(name = "sysuserdept")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SysUserDept.findAll", query = "SELECT s FROM SysUserDept s"),
    @NamedQuery(name = "SysUserDept.findByIdx", query = "SELECT s FROM SysUserDept s WHERE s.idx = :idx"),
    @NamedQuery(name = "SysUserDept.findByUid", query = "SELECT s FROM SysUserDept s WHERE s.uid = :uid"),
    @NamedQuery(name = "SysUserDept.findByDid", query = "SELECT s FROM SysUserDept s WHERE s.did = :did"),
    @NamedQuery(name = "SysUserDept.findByState", query = "SELECT s FROM SysUserDept s WHERE s.state = :state"),
    @NamedQuery(name = "SysUserDept.findByDeptcode", query = "SELECT s FROM SysUserDept s WHERE s.deptcode = :deptcode"),
    @NamedQuery(name = "SysUserDept.findByAddtime", query = "SELECT s FROM SysUserDept s WHERE s.addtime = :addtime")})
public class SysUserDept implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idx")
    private Integer idx;
    @Column(name = "uid")
    private Integer uid;
    @Column(name = "did")
    private Integer did;
    @Column(name = "state")
    private Integer state;
    @Column(name = "deptcode")
    private String deptcode;
    @Column(name = "addtime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date addtime;

    public SysUserDept() {
    }

    public SysUserDept(Integer idx) {
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

    public Integer getDid() {
        return did;
    }

    public void setDid(Integer did) {
        this.did = did;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getDeptcode() {
        return deptcode;
    }

    public void setDeptcode(String deptcode) {
        this.deptcode = deptcode;
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
        if (!(object instanceof SysUserDept)) {
            return false;
        }
        SysUserDept other = (SysUserDept) object;
        if ((this.idx == null && other.idx != null) || (this.idx != null && !this.idx.equals(other.idx))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "javaapplication19.SysUserDept[ idx=" + idx + " ]";
    }
    
}
