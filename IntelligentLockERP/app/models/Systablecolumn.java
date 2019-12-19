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
 * @author lzs
 */
@Entity
@Table(name = "systablecolumn")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Systablecolumn.findAll", query = "SELECT s FROM Systablecolumn s"),
    @NamedQuery(name = "Systablecolumn.findByIdd", query = "SELECT s FROM Systablecolumn s WHERE s.idd = :idd"),
    @NamedQuery(name = "Systablecolumn.findByTablename", query = "SELECT s FROM Systablecolumn s WHERE s.tablename = :tablename"),
    @NamedQuery(name = "Systablecolumn.findByColumnname", query = "SELECT s FROM Systablecolumn s WHERE s.columnname = :columnname"),
    @NamedQuery(name = "Systablecolumn.findByTypeString", query = "SELECT s FROM Systablecolumn s WHERE s.typeString = :typeString"),
    @NamedQuery(name = "Systablecolumn.findByExt", query = "SELECT s FROM Systablecolumn s WHERE s.ext = :ext"),
    @NamedQuery(name = "Systablecolumn.findByParamIdx", query = "SELECT s FROM Systablecolumn s WHERE s.paramIdx = :paramIdx"),
    @NamedQuery(name = "Systablecolumn.findByIsEnum", query = "SELECT s FROM Systablecolumn s WHERE s.isEnum = :isEnum")})
public class Systablecolumn implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idd")
    private Long idd;
    @Column(name = "tablename")
    private String tablename;
    @Column(name = "columnname")
    private String columnname;
    @Column(name = "typeString")
    private String typeString;
    @Column(name = "ext")
    private String ext;
    @Column(name = "paramIdx")
    private Integer paramIdx;
    @Column(name = "isEnum")
    private Integer isEnum;
    @Column(name = "enumType")
    private String enumType;
    

    public String getEnumType() {
		return enumType;
	}

	public void setEnumType(String enumType) {
		this.enumType = enumType;
	}

	public Systablecolumn() {
    }

    public Systablecolumn(Long idd) {
        this.idd = idd;
    }

    public Long getIdd() {
        return idd;
    }

    public void setIdd(Long idd) {
        this.idd = idd;
    }

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    public String getColumnname() {
        return columnname;
    }

    public void setColumnname(String columnname) {
        this.columnname = columnname;
    }

    public String getTypeString() {
        return typeString;
    }

    public void setTypeString(String typeString) {
        this.typeString = typeString;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public Integer getParamIdx() {
        return paramIdx;
    }

    public void setParamIdx(Integer paramIdx) {
        this.paramIdx = paramIdx;
    }

    public Integer getIsEnum() {
        return isEnum;
    }

    public void setIsEnum(Integer isEnum) {
        this.isEnum = isEnum;
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
        if (!(object instanceof Systablecolumn)) {
            return false;
        }
        Systablecolumn other = (Systablecolumn) object;
        if ((this.idd == null && other.idd != null) || (this.idd != null && !this.idd.equals(other.idd))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "models.Systablecolumn[ idd=" + idd + " ]";
    }
    
}
