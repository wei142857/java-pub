package models;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;
/**
 * 该Model为smart_product的实体类
 * 2019/5/31
 * @author 陈宏亮
 *
 */
@Entity
@Table(name = "smart_product")
public class SmartProduct implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
	@Column(name = "idd")
    private Integer idd;  //ID
	
	@Column(name = "title")
    private String title;   //标题
	
	@Column(name = "yys")
    private String yys;   //运营商

	@Column(name = "area")
    private String area;   //使用区域
	
	@Column(name = "price")
	private Integer price;	//话费价值

	@Column(name = "bvalue")
    private Integer bvalue;   //积分
	
	@Column(name = "status")
    private Integer status;   //状态 0正常 -1下线
	
	@Column(name = "remark")
    private String remark;   //备注
	
	@Column(name = "icon")
    private String icon;   //图标

	@Column(name = "addtime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date addtime;   //添加时间
	
	@Column(name = "orders")
    private Integer orders;   //排序值
	
	@Column(name = "cost")
    private Double cost;   //成本价
	
	@Column(name = "ifid")
    private Integer ifid;   //充值接口

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

	public String getYys() {
		return yys;
	}

	public void setYys(String yys) {
		this.yys = yys;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getBvalue() {
		return bvalue;
	}

	public void setBvalue(Integer bvalue) {
		this.bvalue = bvalue;
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

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Date getAddtime() {
		return addtime;
	}

	public void setAddtime(Date addtime) {
		this.addtime = addtime;
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

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}
	
	public Integer getIfid() {
		return ifid;
	}

	public void setIfid(Integer ifid) {
		this.ifid = ifid;
	}

	@Override
	public String toString() {
		return "SmartProduct [idd=" + idd + ", title=" + title + ", yys=" + yys
				+ ", area=" + area + ", price=" + price + ", bvalue=" + bvalue
				+ ", status=" + status + ", remark=" + remark + ", icon="
				+ icon + ", addtime=" + addtime + ", orders=" + orders
				+ ", cost=" + cost + "]";
	}
}
