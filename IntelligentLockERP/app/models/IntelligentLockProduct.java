package models;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 作者:魏全振
 *  - 产品表
 *2019年2月1日
 */
@Entity
@Table(name="intelligentlock_product")
public class IntelligentLockProduct implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Basic(optional=false)
	@Column(name="idd")
	private Integer idd;	//idd
	
	@Column(name="image")
	private String image;	//产品图
	
	@Column(name="title")
	private String title;	//产品名称
	
	@Column(name="producttype")
	private String producttype;	//产品类型
	
	@Column(name="model")
	private String model;	//型号
	
	@Column(name="spec")
	private String spec;	//规格

	@Column(name="operator")
	private String operator;	//操作人
	
	@Column(name="threshold")
	private Integer threshold;	//库存警告阈值
	
	@Column(name="status")
	private Integer status;	//状态
	
	@Column(name="remark")
	private String remark;	//备注
	
	@Column(name="filepart")
	private String filepart;	//附件链接地址
	
	@Column(name="leaf")
	private Integer leaf;	//是否叶子节点
	
	@Column(name="level")
	private Integer level;	//树结构级别
	
	@Column(name="parentid")
	private Integer parentid;	//父级ID
	
	@Column(name="displayorder")
	private Integer displayorder;	//平级节点排序
	
	@Column(name="subq")
	private String subq;	//序列号
	
	@Column(name="addtime")
	private Date addtime;	//添加时间

	public Integer getIdd() {
		return idd;
	}

	public void setIdd(Integer idd) {
		this.idd = idd;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getProducttype() {
		return producttype;
	}

	public void setProducttype(String producttype) {
		this.producttype = producttype;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Integer getThreshold() {
		return threshold;
	}

	public void setThreshold(Integer threshold) {
		this.threshold = threshold;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getFilepart() {
		return filepart;
	}

	public void setFilepart(String filepart) {
		this.filepart = filepart;
	}

	public Integer getLeaf() {
		return leaf;
	}

	public void setLeaf(Integer leaf) {
		this.leaf = leaf;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getParentid() {
		return parentid;
	}

	public void setParentid(Integer parentid) {
		this.parentid = parentid;
	}

	public Integer getDisplayorder() {
		return displayorder;
	}

	public void setDisplayorder(Integer displayorder) {
		this.displayorder = displayorder;
	}

	public String getSubq() {
		return subq;
	}

	public void setSubq(String subq) {
		this.subq = subq;
	}

	public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}

	@Override
	public String toString() {
		return "IntelligentLockProduct [idd=" + idd + ", image=" + image + ", title=" + title + ", producttype="
				+ producttype + ", model=" + model + ", spec=" + spec + ", operator=" + operator + ", threshold="
				+ threshold + ", status=" + status + ", remark=" + remark + ", filepart=" + filepart + ", leaf=" + leaf
				+ ", level=" + level + ", parentid=" + parentid + ", displayorder=" + displayorder + ", subq=" + subq
				+ ", addtime=" + addtime + "]";
	}
	
}
