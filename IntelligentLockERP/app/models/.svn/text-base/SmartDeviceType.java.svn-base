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
import javax.persistence.Lob;
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
@Table(name = "smart_device_type")

public class SmartDeviceType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idd")
    private Integer idd; //ID
    @Column(name = "name")
    private String name;  //名称
    @Column(name = "catagary")
    private String catagary;  //设备分类
    @Column(name = "logo")
    private String logo;  //缩略图
    @Column(name = "image")
    private String image;  //图片
    @Lob
    @Column(name = "descinfo")
    private String descinfo;  //描述信息
    @Column(name = "image_link")
    private String imageLink;  //说明信息的图片
    @Column(name = "image_op1")
    private String imageOp1;  //操作图片的链接1
    @Column(name = "image_op2")
    private String imageOp2;  //操作图片的链接2
    @Column(name = "desc2")
    private String desc2;   //扩展信息2
    @Column(name = "desc3")
    private String desc3;   //扩展信息3
    @Column(name = "status")
    private Integer status;  //状态
    @Column(name = "addtime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date addtime;  //添加时间
    @Column(name = "factory")
    private String factory; //生成厂家
    @Column(name = "warranty_validity")
    private Integer warrantyValidity; //期限

    public Integer getWarrantyValidity() {
		return warrantyValidity;
	}

	public void setWarrantyValidity(Integer warrantyValidity) {
		this.warrantyValidity = warrantyValidity;
	}

	public SmartDeviceType() {
    	addtime = new Date();
    	status  = 0;
    }

    public SmartDeviceType(Integer idd) {
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

    public String getCatagary() {
        return catagary;
    }

    public void setCatagary(String catagary) {
        this.catagary = catagary;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescinfo() {
        return descinfo;
    }

    public void setDescinfo(String descinfo) {
        this.descinfo = descinfo;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getImageOp1() {
        return imageOp1;
    }

    public void setImageOp1(String imageOp1) {
        this.imageOp1 = imageOp1;
    }

    public String getImageOp2() {
        return imageOp2;
    }

    public void setImageOp2(String imageOp2) {
        this.imageOp2 = imageOp2;
    }

    public String getDesc2() {
        return desc2;
    }

    public void setDesc2(String desc2) {
        this.desc2 = desc2;
    }

    public String getDesc3() {
        return desc3;
    }

    public void setDesc3(String desc3) {
        this.desc3 = desc3;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
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
        if (!(object instanceof SmartDeviceType)) {
            return false;
        }
        SmartDeviceType other = (SmartDeviceType) object;
        if ((this.idd == null && other.idd != null) || (this.idd != null && !this.idd.equals(other.idd))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "models.SmartDeviceType[ idd=" + idd + " ]";
    }

}
