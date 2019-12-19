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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
/**
 * 商城商品信息表
 * @author 陈宏亮
 * @date   2019/7/1 11:00
 *
 */
@Entity
@Table(name = "eshop_product")
public class EShopProduct implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional=false)
	@Column(name="idd")
	private Integer idd;		//IDD
	
	@Column(name="title")			//标题
	private String title;
	
	@Column(name="subtitle")		//副标题
	private String subtitle;
	
	@Column(name="description")		//商品描述
	private String description;
	
	@Column(name="remark")
	private String remark;			//备注
	
	@Column(name="addtime")			//添加时间
	@Temporal(TemporalType.TIMESTAMP)
	private Date addtime;
	
	@Column(name="bigimgurl")		//大图
	private String bigimgurl;
	
	@Column(name="smallimgurl")		//小图
	private String smallimgurl;
	
	@Column(name="status")			//状态 0上线 -1下线
	private Integer status;
	
	@Column(name="sharetitle")		//分享标题
	private String sharetitle;
	
	@Column(name="sharedesc")		//分享描述
	private String sharedesc;
	
	@Column(name="shareicon")		//分享图标
	private String shareicon;
	
	@Column(name="sharelink")		//分享链接
	private String sharelink;
	
	@Column(name="saleprice")		//售价
	private Double saleprice;
	
	@Column(name="vipprice")		//会员价
	private Double vipprice;
	
	@Column(name="pcsaleprice")		//成本价
	private Double pcsaleprice;
	
	@Column(name="stock")			//库存			
	private Integer stock;
	
	@Column(name="orders")			//排序值
	private Integer orders;

	public Integer getIdd() {
		return idd;
	}

	public void setIdd(Integer idd) {
		this.idd = idd;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}

	public String getBigimgurl() {
		return bigimgurl;
	}

	public void setBigimgurl(String bigimgurl) {
		this.bigimgurl = bigimgurl;
	}

	public String getSmallimgurl() {
		return smallimgurl;
	}

	public void setSmallimgurl(String smallimgurl) {
		this.smallimgurl = smallimgurl;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getSharetitle() {
		return sharetitle;
	}

	public void setSharetitle(String sharetitle) {
		this.sharetitle = sharetitle;
	}

	public String getSharedesc() {
		return sharedesc;
	}

	public void setSharedesc(String sharedesc) {
		this.sharedesc = sharedesc;
	}

	public String getShareicon() {
		return shareicon;
	}

	public void setShareicon(String shareicon) {
		this.shareicon = shareicon;
	}

	public String getSharelink() {
		return sharelink;
	}

	public void setSharelink(String sharelink) {
		this.sharelink = sharelink;
	}

	public Double getSaleprice() {
		return saleprice;
	}

	public void setSaleprice(Double saleprice) {
		this.saleprice = saleprice;
	}

	public Double getVipprice() {
		return vipprice;
	}

	public void setVipprice(Double vipprice) {
		this.vipprice = vipprice;
	}

	public Double getPcsaleprice() {
		return pcsaleprice;
	}

	public void setPcsaleprice(Double pcsaleprice) {
		this.pcsaleprice = pcsaleprice;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public Integer getOrders() {
		return orders;
	}

	public void setOrders(Integer orders) {
		this.orders = orders;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return "EshopProduct [idd=" + idd + ", title="
				+ title + ", subtitle=" + subtitle + ", description="
				+ description + ", remark=" + remark + ", addtime=" + addtime
				+ ", bigimgurl=" + bigimgurl + ", smallimgurl=" + smallimgurl
				+ ", status=" + status + ", sharetitle=" + sharetitle
				+ ", sharedesc=" + sharedesc + ", shareicon=" + shareicon
				+ ", sharelink=" + sharelink + ", saleprice=" + saleprice
				+ ", vipprice=" + vipprice + ", pcsaleprice=" + pcsaleprice
				+ ", stock=" + stock + ", orders=" + orders + "]";
	}
}
