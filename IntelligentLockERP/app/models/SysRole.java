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
@Table(name = "sysrole")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SysRole.findAll", query = "SELECT s FROM SysRole s"),
    @NamedQuery(name = "SysRole.findByIdx", query = "SELECT s FROM SysRole s WHERE s.idx = :idx"),
    @NamedQuery(name = "SysRole.findByName", query = "SELECT s FROM SysRole s WHERE s.name = :name"),
    @NamedQuery(name = "SysRole.findByOid", query = "SELECT s FROM SysRole s WHERE s.oid = :oid"),
    @NamedQuery(name = "SysRole.findByState", query = "SELECT s FROM SysRole s WHERE s.state = :state"),
    @NamedQuery(name = "SysRole.findByAddtime", query = "SELECT s FROM SysRole s WHERE s.addtime = :addtime")})
public class SysRole implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idx")
    private Integer idx;
    @Column(name = "name")
    private String name;
    @Column(name = "oid")
    private Integer oid;
    @Column(name = "state")
    private Integer state;
    @Column(name = "addtime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date addtime;
    
    public SysRole() {
    }

    public SysRole(Integer idx) {
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

    public Integer getOid() {
        return oid;
    }

    public void setOid(Integer oid) {
        this.oid = oid;
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
        if (!(object instanceof SysRole)) {
            return false;
        }
        SysRole other = (SysRole) object;
        if ((this.idx == null && other.idx != null) || (this.idx != null && !this.idx.equals(other.idx))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "javaapplication19.SysRole[ idx=" + idx + " ]";
    }
    
}
