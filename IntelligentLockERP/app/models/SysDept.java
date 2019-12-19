/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.Serializable;

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
@Table(name = "sysdept")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SysDept.findAll", query = "SELECT s FROM SysDept s"),
    @NamedQuery(name = "SysDept.findByIdx", query = "SELECT s FROM SysDept s WHERE s.idx = :idx"),
    @NamedQuery(name = "SysDept.findByOid", query = "SELECT s FROM SysDept s WHERE s.oid = :oid"),
    @NamedQuery(name = "SysDept.findByName", query = "SELECT s FROM SysDept s WHERE s.name = :name"),
    @NamedQuery(name = "SysDept.findByParent", query = "SELECT s FROM SysDept s WHERE s.parent = :parent"),
    @NamedQuery(name = "SysDept.findByLevel", query = "SELECT s FROM SysDept s WHERE s.level = :level"),
    @NamedQuery(name = "SysDept.findByState", query = "SELECT s FROM SysDept s WHERE s.state = :state"),
    @NamedQuery(name = "SysDept.findByDeptcode", query = "SELECT s FROM SysDept s WHERE s.deptcode = :deptcode")})
public class SysDept implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idx")
    private Integer idx;
    @Column(name = "oid")
    private Integer oid;
    @Column(name = "name")
    private String name;
    @Column(name = "parent")
    private Integer parent;
    @Column(name = "level")
    private Integer level;
    @Column(name = "state")
    private Integer state;
    @Column(name = "deptcode")
    private String deptcode;

    public SysDept() {
    }

    public SysDept(Integer idx) {
        this.idx = idx;
    }

    public Integer getIdx() {
        return idx;
    }

    public void setIdx(Integer idx) {
        this.idx = idx;
    }

    public Integer getOid() {
        return oid;
    }

    public void setOid(Integer oid) {
        this.oid = oid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idx != null ? idx.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SysDept)) {
            return false;
        }
        SysDept other = (SysDept) object;
        if ((this.idx == null && other.idx != null) || (this.idx != null && !this.idx.equals(other.idx))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "javaapplication19.SysDept[ idx=" + idx + " ]";
    }
    
}
