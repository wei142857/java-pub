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
@Table(name = "systable")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Systable.findAll", query = "SELECT s FROM Systable s"),
    @NamedQuery(name = "Systable.findByIdd", query = "SELECT s FROM Systable s WHERE s.idd = :idd"),
    @NamedQuery(name = "Systable.findByName", query = "SELECT s FROM Systable s WHERE s.name = :name"),
    @NamedQuery(name = "Systable.findByExt", query = "SELECT s FROM Systable s WHERE s.ext = :ext")})
public class Systable implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idd")
    private Integer idd;
    @Column(name = "name")
    private String name;
    @Column(name = "ext")
    private String ext;

    public Systable() {
    }

    public Systable(Integer idd) {
        this.idd = idd;
    }

    public Integer getIdd() {
        return idd;
    }

    public void setIdd(Integer idd) {
        this.idd = idd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        hash += (idd != null ? idd.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Systable)) {
            return false;
        }
        Systable other = (Systable) object;
        if ((this.idd == null && other.idd != null) || (this.idd != null && !this.idd.equals(other.idd))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "models.Systable[ idd=" + idd + " ]";
    }
    
}
