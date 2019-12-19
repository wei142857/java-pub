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
 * @date 2019年09月04日 09:04:49
 * @discription
 */ 
@Entity
@Table(name = "sub_install_orders")


public class SubInstallOrders implements Serializable{
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

	@Column(name = "cid")
	private Integer cid;	//使用兑换码ID

	@Column(name = "status")
	private Integer status;	//状态

	@Column(name = "doorthickness")
	private Integer doorthickness;	//门厚度

	@Column(name = "slicewidth")
	private Integer slicewidth;	//导向片宽度

	@Column(name = "sliceheight")
	private Integer sliceheight;	//导向片高度

	@Column(name = "lockimg1")
	private String lockimg1;	//门锁图1

	@Column(name = "lockimg2")
	private String lockimg2;	//门锁图2

	@Column(name = "hook")
	private Integer hook;	//是否有天地勾  1有  2没有

	@Column(name = "lockdirection")
	private Integer lockdirection;	//门锁方向

	@Column(name = "consigneename")
	private String consigneename;	//收获人姓名

	@Column(name = "consigneearea")
	private String consigneearea;	//收获人地区

	@Column(name = "consigneeaddress")
	private String consigneeaddress;	//收获人地址

	@Column(name = "consigneephone")
	private String consigneephone;	//收获人手机号

	@Column(name = "installname")
	private String installname;	//安装联系人姓名

	@Column(name = "installarea")
	private String installarea;	//安装地址

	@Column(name = "installaddress")
	private String installaddress;	//安装详细地址

	@Column(name = "installphone")
	private String installphone;	//安装联系人手机号

	@Column(name = "addtime")
	private Date addtime;	//添加时间

	@Column(name = "expressname")
	private String expressname;	//物流名称

	@Column(name = "expressorderid")
	private String expressorderid;	//物流订单

	@Column(name = "money")
	private Integer money;	//安装费用

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
	public void setCid(Integer cid){
		this.cid=cid;
	}
	public Integer getCid(){
		return cid;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public Integer getStatus(){
		return status;
	}
	public void setDoorthickness(Integer doorthickness){
		this.doorthickness=doorthickness;
	}
	public Integer getDoorthickness(){
		return doorthickness;
	}
	public void setSlicewidth(Integer slicewidth){
		this.slicewidth=slicewidth;
	}
	public Integer getSlicewidth(){
		return slicewidth;
	}
	public void setSliceheight(Integer sliceheight){
		this.sliceheight=sliceheight;
	}
	public Integer getSliceheight(){
		return sliceheight;
	}
	public void setLockimg1(String lockimg1){
		this.lockimg1=lockimg1;
	}
	public String getLockimg1(){
		return lockimg1;
	}
	public void setLockimg2(String lockimg2){
		this.lockimg2=lockimg2;
	}
	public String getLockimg2(){
		return lockimg2;
	}
	public void setHook(Integer hook){
		this.hook=hook;
	}
	public Integer getHook(){
		return hook;
	}
	public void setLockdirection(Integer lockdirection){
		this.lockdirection=lockdirection;
	}
	public Integer getLockdirection(){
		return lockdirection;
	}
	public void setConsigneename(String consigneename){
		this.consigneename=consigneename;
	}
	public String getConsigneename(){
		return consigneename;
	}
	public void setConsigneearea(String consigneearea){
		this.consigneearea=consigneearea;
	}
	public String getConsigneearea(){
		return consigneearea;
	}
	public void setConsigneeaddress(String consigneeaddress){
		this.consigneeaddress=consigneeaddress;
	}
	public String getConsigneeaddress(){
		return consigneeaddress;
	}
	public void setConsigneephone(String consigneephone){
		this.consigneephone=consigneephone;
	}
	public String getConsigneephone(){
		return consigneephone;
	}
	public void setInstallname(String installname){
		this.installname=installname;
	}
	public String getInstallname(){
		return installname;
	}
	public void setInstallarea(String installarea){
		this.installarea=installarea;
	}
	public String getInstallarea(){
		return installarea;
	}
	public void setInstalladdress(String installaddress){
		this.installaddress=installaddress;
	}
	public String getInstalladdress(){
		return installaddress;
	}
	public void setInstallphone(String installphone){
		this.installphone=installphone;
	}
	public String getInstallphone(){
		return installphone;
	}
	public void setAddtime(Date addtime){
		this.addtime=addtime;
	}
	public Date getAddtime(){
		return addtime;
	}
	public void setExpressname(String expressname){
		this.expressname=expressname;
	}
	public String getExpressname(){
		return expressname;
	}
	public void setExpressorderid(String expressorderid){
		this.expressorderid=expressorderid;
	}
	public String getExpressorderid(){
		return expressorderid;
	}
	public void setMoney(Integer money){
		this.money=money;
	}
	public Integer getMoney(){
		return money;
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

