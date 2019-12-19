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
@Table(name = "resourceupload")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Resourceupload.findAll", query = "SELECT r FROM Resourceupload r"),
    @NamedQuery(name = "Resourceupload.findByIdd", query = "SELECT r FROM Resourceupload r WHERE r.idd = :idd"),
    @NamedQuery(name = "Resourceupload.findByName", query = "SELECT r FROM Resourceupload r WHERE r.name = :name"),
    @NamedQuery(name = "Resourceupload.findByUrl", query = "SELECT r FROM Resourceupload r WHERE r.url = :url"),
    @NamedQuery(name = "Resourceupload.findByExt", query = "SELECT r FROM Resourceupload r WHERE r.ext = :ext")})
public class Resourceupload implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idd")
    private Long idd;
    @Column(name = "name")
    private String name;
    @Column(name = "url")
    private String url;
    @Column(name = "ext")
    private String ext;

    public Resourceupload() {
    }

    public Resourceupload(Long idd) {
        this.idd = idd;
    }

    public Long getIdd() {
        return idd;
    }

    public void setIdd(Long idd) {
        this.idd = idd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
        if (!(object instanceof Resourceupload)) {
            return false;
        }
        Resourceupload other = (Resourceupload) object;
        if ((this.idd == null && other.idd != null) || (this.idd != null && !this.idd.equals(other.idd))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "models.Resourceupload[ idd=" + idd + " ]";
    }
    
}
