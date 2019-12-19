/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author peng
 */
@Entity
@Table(name = "sys_dict")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SysDict.findAll", query = "SELECT s FROM SysDict s"),
    @NamedQuery(name = "SysDict.findByIdd", query = "SELECT s FROM SysDict s WHERE s.idd = :idd"),
    @NamedQuery(name = "SysDict.findByDictType", query = "SELECT s FROM SysDict s WHERE s.dictType = :dictType"),
    @NamedQuery(name = "SysDict.findByDictTypeName", query = "SELECT s FROM SysDict s WHERE s.dictTypeName = :dictTypeName"),
    @NamedQuery(name = "SysDict.findByDictId", query = "SELECT s FROM SysDict s WHERE s.dictId = :dictId"),
    @NamedQuery(name = "SysDict.findByDictName", query = "SELECT s FROM SysDict s WHERE s.dictName = :dictName"),
    @NamedQuery(name = "SysDict.findByStatus", query = "SELECT s FROM SysDict s WHERE s.status = :status"),
    @NamedQuery(name = "SysDict.findBySortno", query = "SELECT s FROM SysDict s WHERE s.sortno = :sortno"),
    @NamedQuery(name = "SysDict.findByRemark", query = "SELECT s FROM SysDict s WHERE s.remark = :remark")})
public class SysDict implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDD")
    private Integer idd;
    @Basic(optional = false)
    @Column(name = "DICT_TYPE")
    private String dictType;
    @Basic(optional = false)
    @Column(name = "DICT_TYPE_NAME")
    private String dictTypeName;
    @Basic(optional = false)
    @Column(name = "DICT_ID")
    private String dictId;
    @Basic(optional = false)
    @Column(name = "DICT_NAME")
    private String dictName;
    @Column(name = "STATUS")
    private Integer status;
    @Column(name = "SORTNO")
    private Integer sortno;
    @Column(name = "REMARK")
    private String remark;

    public SysDict() {
    }

    public SysDict(Integer idd) {
        this.idd = idd;
    }

    public SysDict(Integer idd, String dictType, String dictTypeName, String dictId, String dictName) {
        this.idd = idd;
        this.dictType = dictType;
        this.dictTypeName = dictTypeName;
        this.dictId = dictId;
        this.dictName = dictName;
    }

    public Integer getIdd() {
        return idd;
    }

    public void setIdd(Integer idd) {
        this.idd = idd;
    }

    public String getDictType() {
        return dictType;
    }

    public void setDictType(String dictType) {
        this.dictType = dictType;
    }

    public String getDictTypeName() {
        return dictTypeName;
    }

    public void setDictTypeName(String dictTypeName) {
        this.dictTypeName = dictTypeName;
    }

    public String getDictId() {
        return dictId;
    }

    public void setDictId(String dictId) {
        this.dictId = dictId;
    }

    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSortno() {
        return sortno;
    }

    public void setSortno(Integer sortno) {
        this.sortno = sortno;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
        if (!(object instanceof SysDict)) {
            return false;
        }
        SysDict other = (SysDict) object;
        if ((this.idd == null && other.idd != null) || (this.idd != null && !this.idd.equals(other.idd))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sys.SysDict[ idd=" + idd + " ]";
    }
    
}
