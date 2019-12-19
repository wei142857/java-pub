/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
 * @author lzs
 */
@Entity
@Table(name = "smart_app_msg")

public class SmartAppMsg implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idd")
    private Long idd; //ID
    @Column(name = "userid")
    private Integer userid ; //用户ID
    @Column(name = "title")
    private String title;  //标题
    @Column(name = "msg")
    private String msg;   //内容
    @Column(name = "image")
    private String image;  //图片
    @Column(name = "link")
    private String link;  //链接
    @Column(name = "status")
    private Integer status; //状态
    
    @Column(name = "addtime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date addtime;  //添加时间

    public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}

	public SmartAppMsg() {
    	status = 0;
    	addtime = new Date();
    }

    public SmartAppMsg(Long idd) {
        this.idd = idd;
    }

    public Long getIdd() {
        return idd;
    }

    public void setIdd(Long idd) {
        this.idd = idd;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
        if (!(object instanceof SmartAppMsg)) {
            return false;
        }
        SmartAppMsg other = (SmartAppMsg) object;
        if ((this.idd == null && other.idd != null) || (this.idd != null && !this.idd.equals(other.idd))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "models.SmartAppMsg[ idd=" + idd + " ]";
    }
    
}
