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
 * @date 2019年08月28日 05:28:12
 * @discription
 */ 
@Entity
@Table(name = "sub_measure_orders")


public class SubMeasureOrders implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "idd")
	private Integer idd;

	@Column(name = "phone")
	private String phone;	//手机号

	@Column(name = "oid")
	private Integer oid;	//订单ID

	@Column(name = "measurename")
	private String measurename;	//测量联系人姓名

	@Column(name = "measurearea")
	private String measurearea;	//测量地址

	@Column(name = "measureaddress")
	private String measureaddress;	//测量详细地址

	@Column(name = "measurephone")
	private String measurephone;	//测量联系人手机号

	@Column(name = "status")
	private Integer status;	//状态

	@Column(name = "money")
	private Integer money;	//支付金额

	@Column(name = "addtime")
	private Date addtime;	//添加时间

	@Column(name = "measuretime")
	private String measuretime;	//预约测量时间

	@Column(name = "prepayid")
	private String prepayid;	//预订单

	@Column(name = "updatetime")
	private Date updatetime;	//支付时间

	public void setIdd(Integer idd){
		this.idd=idd;
	}
	public Integer getIdd(){
		return idd;
	}
	public void setPhone(String phone){
		this.phone=phone;
	}
	public String getPhone(){
		return phone;
	}
	public void setOid(Integer oid){
		this.oid=oid;
	}
	public Integer getOid(){
		return oid;
	}
	public void setMeasurename(String measurename){
		this.measurename=measurename;
	}
	public String getMeasurename(){
		return measurename;
	}
	public void setMeasurearea(String measurearea){
		this.measurearea=measurearea;
	}
	public String getMeasurearea(){
		return measurearea;
	}
	public void setMeasureaddress(String measureaddress){
		this.measureaddress=measureaddress;
	}
	public String getMeasureaddress(){
		return measureaddress;
	}
	public void setMeasurephone(String measurephone){
		this.measurephone=measurephone;
	}
	public String getMeasurephone(){
		return measurephone;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public Integer getStatus(){
		return status;
	}
	public void setMoney(Integer money){
		this.money=money;
	}
	public Integer getMoney(){
		return money;
	}
	public void setAddtime(Date addtime){
		this.addtime=addtime;
	}
	public Date getAddtime(){
		return addtime;
	}
	public void setMeasuretime(String measuretime){
		this.measuretime=measuretime;
	}
	public String getMeasuretime(){
		return measuretime;
	}
	public void setPrepayid(String prepayid){
		this.prepayid=prepayid;
	}
	public String getPrepayid(){
		return prepayid;
	}
	public void setUpdatetime(Date updatetime){
		this.updatetime=updatetime;
	}
	public Date getUpdatetime(){
		return updatetime;
	}
}

