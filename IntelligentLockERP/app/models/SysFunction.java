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
import javax.persistence.Lob;
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
@Table(name = "sysfunction")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SysFunction.findAll", query = "SELECT s FROM SysFunction s"),
    @NamedQuery(name = "SysFunction.findByIdx", query = "SELECT s FROM SysFunction s WHERE s.idx = :idx"),
    @NamedQuery(name = "SysFunction.findByName", query = "SELECT s FROM SysFunction s WHERE s.name = :name"),
    @NamedQuery(name = "SysFunction.findByIsMenu", query = "SELECT s FROM SysFunction s WHERE s.isMenu = :isMenu"),
    @NamedQuery(name = "SysFunction.findByFcode", query = "SELECT s FROM SysFunction s WHERE s.fcode = :fcode"),
    @NamedQuery(name = "SysFunction.findByParent", query = "SELECT s FROM SysFunction s WHERE s.parent = :parent"),
    @NamedQuery(name = "SysFunction.findByIcon", query = "SELECT s FROM SysFunction s WHERE s.icon = :icon"),
    @NamedQuery(name = "SysFunction.findByState", query = "SELECT s FROM SysFunction s WHERE s.state = :state"),
    @NamedQuery(name = "SysFunction.findByAddtime", query = "SELECT s FROM SysFunction s WHERE s.addtime = :addtime"),
    @NamedQuery(name = "SysFunction.findByUrl", query = "SELECT s FROM SysFunction s WHERE s.url = :url")})
public class SysFunction implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idx")
    private Integer idx;
    @Column(name = "name")
    private String name;
    @Lob
    @Column(name = "ext")
    private String ext;
    @Column(name = "isMenu")
    private Integer isMenu;
    @Column(name = "fcode")
    private String fcode;
    @Column(name = "parent")
    private Integer parent;
    @Column(name = "icon")
    private String icon;
    @Column(name = "state")
    private Integer state;
    @Column(name = "addtime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date addtime;
    @Column(name = "url")
    private String url;
    @Column(name = "level")
    private Integer level;

    public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public SysFunction() {
    }

    public SysFunction(Integer idx) {
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

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public Integer getIsMenu() {
        return isMenu;
    }

    public void setIsMenu(Integer isMenu) {
        this.isMenu = isMenu;
    }

    public String getFcode() {
        return fcode;
    }

    public void setFcode(String fcode) {
        this.fcode = fcode;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
        if (!(object instanceof SysFunction)) {
            return false;
        }
        SysFunction other = (SysFunction) object;
        if ((this.idx == null && other.idx != null) || (this.idx != null && !this.idx.equals(other.idx))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "javaapplication19.SysFunction[ idx=" + idx + " ]";
    }
    
}
