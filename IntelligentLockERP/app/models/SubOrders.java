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
 * @author xuml 实体类
 * @date 2019年08月28日 05:28:13
 * @discription
 */ 
@Entity
@Table(name = "sub_orders")


public class SubOrders implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "idd")
	private Integer idd;

	@Column(name = "title")
	private String title;	//商品标题

	@Column(name = "smallimgurl")
	private String smallimgurl;	//商品小图

	@Column(name = "name")
	private String name;	//收获人姓名

	@Column(name = "area")
	private String area;	//地区

	@Column(name = "address")
	private String address;	//详细地址

	@Column(name = "phone")
	private String phone;

	@Column(name = "addtime")
	private Date addtime;

	@Column(name = "orderid")
	private String orderid;

	@Column(name = "ch")
	private String ch;
	
	@Column(name = "isinstall")
	private Integer isinstall;
	
	@Column(name = "ismeasure")
	private Integer ismeasure;

	public void setIdd(Integer idd){
		this.idd=idd;
	}
	public Integer getIdd(){
		return idd;
	}
	public void setTitle(String title){
		this.title=title;
	}
	public String getTitle(){
		return title;
	}
	public void setSmallimgurl(String smallimgurl){
		this.smallimgurl=smallimgurl;
	}
	public String getSmallimgurl(){
		return smallimgurl;
	}
	public void setName(String name){
		this.name=name;
	}
	public String getName(){
		return name;
	}
	public void setArea(String area){
		this.area=area;
	}
	public String getArea(){
		return area;
	}
	public void setAddress(String address){
		this.address=address;
	}
	public String getAddress(){
		return address;
	}
	public void setPhone(String phone){
		this.phone=phone;
	}
	public String getPhone(){
		return phone;
	}
	public void setAddtime(Date addtime){
		this.addtime=addtime;
	}
	public Date getAddtime(){
		return addtime;
	}
	public void setOrderid(String orderid){
		this.orderid=orderid;
	}
	public String getOrderid(){
		return orderid;
	}
	public void setCh(String ch){
		this.ch=ch;
	}
	public String getCh(){
		return ch;
	}
	public Integer getIsinstall() {
		return isinstall;
	}
	public void setIsinstall(Integer isinstall) {
		this.isinstall = isinstall;
	}
	public Integer getIsmeasure() {
		return ismeasure;
	}
	public void setIsmeasure(Integer ismeasure) {
		this.ismeasure = ismeasure;
	}
	
}

