package models;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
/**
 * EShop订单的实体类
 * @author 陈宏亮
 * @time 2019年7月10号 13:15
 */
@Entity
@Table(name = "eshop_orders")
public class EShopOrders implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "idd")
	private Integer idd;			//ID
	
	@Column(name = "userid")
	private Integer userid;			//用户编码
	
	@Column(name = "province")
	private String 	province;		//省
	
	@Column(name = "city")
	private String city;			//市
	
	@Column(name = "pidd")
	private Integer pidd;			//产品ID
	
	@Column(name = "title")
	private String title;			//标题
	
	@Column(name = "subtitle")
	private String subtitle;		//商品副标题
	
	@Column(name = "smallimgurl")
	private String smallimgurl;		//小图
	
	@Column(name = "amount")
	private Integer amount;			//购买的数量
	
	@Column(name = "addtime")
	@Temporal(TemporalType.DATE)
	private Date addtime;			//添加时间
	
	@Column(name = "price")
	private Double price;			//商品原价(分)
	
	@Column(name = "money")
	private Double money;			//人民币支付价格(分)
	
	@Column(name = "bvalue")
	private Integer bvalue;			//奖励金支付数量
	
	@Column(name = "pcsaleprice")
	private Double pcsaleprice;	//成本价
	
	@Column(name = "vipstatus")
	private Integer vipstatus;		//会员状态(0:会员 1:过期 2:未开通)
	
	@Column(name = "updatetime")
	@Temporal(TemporalType.DATE)
	private Date updatetime;		//支付成功时间
	
	@Column(name = "status")
	private Integer status;			//状态(0:成功 1:待支付 -1:失败/超时)
	
	@Column(name = "prepayid")
	private String prepayid;		//支付订单
	
	@Column(name = "consigneename")
	private String consigneename;	//收件人姓名
	
	@Column(name = "consigneearea")
	private String consigneearea;	//收件人地区
	
	@Column(name = "consigneeaddress")
	private String consigneeaddress;	//收件人详细地址
	
	@Column(name = "consigneephone")
	private String consigneephone;		//收件人手机号
	
	@Column(name = "expressname")
	private String expressname;			//物流名称
	
	@Column(name = "expressorderid")
	private String expressorderid;		//物流号
	
	@Column(name = "expressstatus")
	private Integer expressstatus;		//物流状态(0:已发货 1:未发货)
	
	@Column(name = "source")
	private String source;
	
	@Column(name = "award")
	private Integer award;				//是否发放奖励金

	public Integer getIdd() {
		return idd;
	}
	public void setIdd(Integer idd) {
		this.idd = idd;
	}
	public Integer getUserid() {
		return userid;
	}
	public void setUserid(Integer userid) {
		this.userid = userid;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public Integer getPidd() {
		return pidd;
	}
	public void setPidd(Integer pidd) {
		this.pidd = pidd;
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
	public String getSmallimgurl() {
		return smallimgurl;
	}
	public void setSmallimgurl(String smallimgurl) {
		this.smallimgurl = smallimgurl;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public Date getAddtime() {
		return addtime;
	}
	public void setAddtime(Date addtime) {
		this.addtime = addtime;
	}
	public Double getMoney() {
		return money;
	}
	public void setMoney(Double money) {
		this.money = money;
	}
	public Integer getBvalue() {
		return bvalue;
	}
	public void setBvalue(Integer bvalue) {
		this.bvalue = bvalue;
	}
	public Double getPcsaleprice() {
		return pcsaleprice;
	}
	public void setPcsaleprice(Double pcsaleprice) {
		this.pcsaleprice = pcsaleprice;
	}
	public Integer getVipstatus() {
		return vipstatus;
	}
	public void setVipstatus(Integer vipstatus) {
		this.vipstatus = vipstatus;
	}
	public Date getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getPrepayid() {
		return prepayid;
	}
	public void setPrepayid(String prepayid) {
		this.prepayid = prepayid;
	}
	public String getConsigneename() {
		return consigneename;
	}
	public void setConsigneename(String consigneename) {
		this.consigneename = consigneename;
	}
	public String getConsigneearea() {
		return consigneearea;
	}
	public void setConsigneearea(String consigneearea) {
		this.consigneearea = consigneearea;
	}
	public String getConsigneeaddress() {
		return consigneeaddress;
	}
	public void setConsigneeaddress(String consigneeaddress) {
		this.consigneeaddress = consigneeaddress;
	}
	public String getConsigneephone() {
		return consigneephone;
	}
	public void setConsigneephone(String consigneephone) {
		this.consigneephone = consigneephone;
	}
	public String getExpressname() {
		return expressname;
	}
	public void setExpressname(String expressname) {
		this.expressname = expressname;
	}
	public String getExpressorderid() {
		return expressorderid;
	}
	public void setExpressorderid(String expressorderid) {
		this.expressorderid = expressorderid;
	}
	public Integer getExpressstatus() {
		return expressstatus;
	}
	public void setExpressstatus(Integer expressstatus) {
		this.expressstatus = expressstatus;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public Integer getAward() {
		return award;
	}
	public void setAward(Integer award) {
		this.award = award;
	}
	
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return "EShopOrders [idd=" + idd + ", userid=" + userid + ", province="
				+ province + ", city=" + city + ", pidd=" + pidd + ", title="
				+ title + ", subtitle=" + subtitle + ", smallimgurl="
				+ smallimgurl + ", amount=" + amount + ", addtime=" + addtime
				+ ", money=" + money + ", bvalue=" + bvalue + ", pcsaleprice="
				+ pcsaleprice + ", vipstatus=" + vipstatus + ", updatetime="
				+ updatetime + ", status=" + status + ", prepayid=" + prepayid
				+ ", consigneename=" + consigneename + ", consigneearea="
				+ consigneearea + ", consigneeaddress=" + consigneeaddress
				+ ", consigneephone=" + consigneephone + ", expressname="
				+ expressname + ", expressorderid=" + expressorderid
				+ ", expressstatus=" + expressstatus + ", source=" + source
				+ ", award=" + award + "]";
	}
}
