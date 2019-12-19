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

/**
 *
 * @author lzs
 */
@Entity
@Table(name = "sys_config")
@NamedQueries({
    @NamedQuery(name = "SysConfig.findAll", query = "SELECT s FROM SysConfig s"),
    @NamedQuery(name = "SysConfig.findByIdd", query = "SELECT s FROM SysConfig s WHERE s.idd = :idd"),
    @NamedQuery(name = "SysConfig.findByDatagroup", query = "SELECT s FROM SysConfig s WHERE s.datagroup = :datagroup"),
    @NamedQuery(name = "SysConfig.findByDataitem", query = "SELECT s FROM SysConfig s WHERE s.dataitem = :dataitem"),
    @NamedQuery(name = "SysConfig.findByDatavalue", query = "SELECT s FROM SysConfig s WHERE s.datavalue = :datavalue"),
    @NamedQuery(name = "SysConfig.findByAddtime", query = "SELECT s FROM SysConfig s WHERE s.addtime = :addtime")})
public class SysConfig implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idd")
    private Integer idd;
    @Column(name = "datagroup")
    private String datagroup;
    @Column(name = "dataitem")
    private String dataitem;
    @Column(name = "datavalue")
    private String datavalue;
    @Column(name = "addtime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date addtime;
    
    @Column(name = "remark")
    private String remark;

    public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

    public SysConfig() {
    }

    public SysConfig(Integer idd) {
        this.idd = idd;
    }

    public Integer getIdd() {
        return idd;
    }

    public void setIdd(Integer idd) {
        this.idd = idd;
    }

    public String getDatagroup() {
        return datagroup;
    }

    public void setDatagroup(String datagroup) {
        this.datagroup = datagroup;
    }

    public String getDataitem() {
        return dataitem;
    }

    public void setDataitem(String dataitem) {
        this.dataitem = dataitem;
    }

    public String getDatavalue() {
        return datavalue;
    }

    public void setDatavalue(String datavalue) {
        this.datavalue = datavalue;
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
        hash += (idd != null ? idd.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SysConfig)) {
            return false;
        }
        SysConfig other = (SysConfig) object;
        if ((this.idd == null && other.idd != null) || (this.idd != null && !this.idd.equals(other.idd))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "models.SysConfig[idd=" + idd + "]";
    }

}
