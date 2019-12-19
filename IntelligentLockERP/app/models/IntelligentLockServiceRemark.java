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
//用于客服和运营单独为了备注记录用
@Entity
@Table(name="intelligentlock_service_remark")
public class IntelligentLockServiceRemark implements Serializable{
	private static final long serialVersionUID = 8792162401458589688L;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Basic(optional=false)
	@Column(name="idd")
	private Integer idd;//IDD
	@Column(name="promoter")
	private String promoter;//订单引流人
	@Column(name="ordernumber")
	private String ordernumber;//订单号
	@Column(name="status")
	private String status;//订单状态
	@Column(name="installprincipal")
	private String installprincipal;//订单安装负责人
	@Column(name="ordertime")
	private Date ordertime;//锁下单时间
	@Column(name="deliverytime")
	private Date deliverytime;//发货时间
	@Column(name="logistics")
	private String logistics;//物流名称
	@Column(name="arrivaltime")
	private Date arrivaltime;//锁到货时间
	@Column(name="guidecustommadetime")
	private Date guidecustommadetime;//导向片定制时间
	@Column(name="guidesigningtime")
	private Date guidesigningtime;//导向片签收时间
	@Column(name="reservationtime")
	private Date reservationtime;//预约安装时间
	@Column(name="ordersuctime")
	private Date ordersuctime;//订单完成时间
	@Column(name="contact")
	private String contact;//用户联系人
	@Column(name="phone")
	private String phone;//用户联系电话
	@Column(name="addr")
	private String addr;//安装地址
	@Column(name="channel")
	private String channel;//渠道
	@Column(name="type")
	private String type;//需安装产品类型
	@Column(name="model")
	private String model;//需安装产品型号
	@Column(name="spec")
	private String spec;//安装产品规格
	@Column(name="num")
	private Integer num;//安装数量
	@Column(name="material")
	private String material;//门材质
	@Column(name="thickness")
	private String thickness;//门厚度
	@Column(name="opendirection")
	private String opendirection;//开门方向
	@Column(name="hook")
	private String hook;//天地钩
	@Column(name="guidecustommade")
	private String guidecustommade;//是否定制导向片
	@Column(name="guidetype")
	private String guidetype;//导向片类型
	@Column(name="guidesize")
	private String guidesize;//导向片尺寸
	@Column(name="installationprice")
	private Double installationprice;//安装费
	@Column(name="guidecustommadeprice")
	private Double guidecustommadeprice;//定制导向片费用
	@Column(name="measureprice")
	private Double measureprice;//测量
	@Column(name="emptyrunprice")
	private Double emptyrunprice;//空跑费
	@Column(name="totalprice")
	private Double totalprice;//总价
	@Column(name="remark")
	private String remark;//备注
	@Column(name="repair")
	private Double repair;//维修费用
	@Column(name="bearer")
	private String bearer;//承担方
	public Integer getIdd() {
		return idd;
	}
	public void setIdd(Integer idd) {
		this.idd = idd;
	}
	public String getPromoter() {
		return promoter;
	}
	public void setPromoter(String promoter) {
		this.promoter = promoter;
	}
	public String getOrdernumber() {
		return ordernumber;
	}
	public void setOrdernumber(String ordernumber) {
		this.ordernumber = ordernumber;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getInstallprincipal() {
		return installprincipal;
	}
	public void setInstallprincipal(String installprincipal) {
		this.installprincipal = installprincipal;
	}
	public Date getOrdertime() {
		return ordertime;
	}
	public void setOrdertime(Date ordertime) {
		this.ordertime = ordertime;
	}
	public Date getDeliverytime() {
		return deliverytime;
	}
	public void setDeliverytime(Date deliverytime) {
		this.deliverytime = deliverytime;
	}
	public String getLogistics() {
		return logistics;
	}
	public void setLogistics(String logistics) {
		this.logistics = logistics;
	}
	public Date getArrivaltime() {
		return arrivaltime;
	}
	public void setArrivaltime(Date arrivaltime) {
		this.arrivaltime = arrivaltime;
	}
	public Date getGuidecustommadetime() {
		return guidecustommadetime;
	}
	public void setGuidecustommadetime(Date guidecustommadetime) {
		this.guidecustommadetime = guidecustommadetime;
	}
	public Date getGuidesigningtime() {
		return guidesigningtime;
	}
	public void setGuidesigningtime(Date guidesigningtime) {
		this.guidesigningtime = guidesigningtime;
	}
	public Date getReservationtime() {
		return reservationtime;
	}
	public void setReservationtime(Date reservationtime) {
		this.reservationtime = reservationtime;
	}
	public Date getOrdersuctime() {
		return ordersuctime;
	}
	public void setOrdersuctime(Date ordersuctime) {
		this.ordersuctime = ordersuctime;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public String getMaterial() {
		return material;
	}
	public void setMaterial(String material) {
		this.material = material;
	}
	public String getThickness() {
		return thickness;
	}
	public void setThickness(String thickness) {
		this.thickness = thickness;
	}
	public String getOpendirection() {
		return opendirection;
	}
	public void setOpendirection(String opendirection) {
		this.opendirection = opendirection;
	}
	public String getHook() {
		return hook;
	}
	public void setHook(String hook) {
		this.hook = hook;
	}
	public String getGuidecustommade() {
		return guidecustommade;
	}
	public void setGuidecustommade(String guidecustommade) {
		this.guidecustommade = guidecustommade;
	}
	public String getGuidetype() {
		return guidetype;
	}
	public void setGuidetype(String guidetype) {
		this.guidetype = guidetype;
	}
	public String getGuidesize() {
		return guidesize;
	}
	public void setGuidesize(String guidesize) {
		this.guidesize = guidesize;
	}
	public Double getInstallationprice() {
		return installationprice;
	}
	public void setInstallationprice(Double installationprice) {
		this.installationprice = installationprice;
	}
	public Double getGuidecustommadeprice() {
		return guidecustommadeprice;
	}
	public void setGuidecustommadeprice(Double guidecustommadeprice) {
		this.guidecustommadeprice = guidecustommadeprice;
	}
	public Double getMeasureprice() {
		return measureprice;
	}
	public void setMeasureprice(Double measureprice) {
		this.measureprice = measureprice;
	}
	public Double getEmptyrunprice() {
		return emptyrunprice;
	}
	public void setEmptyrunprice(Double emptyrunprice) {
		this.emptyrunprice = emptyrunprice;
	}
	public Double getTotalprice() {
		return totalprice;
	}
	public void setTotalprice(Double totalprice) {
		this.totalprice = totalprice;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Double getRepair() {
		return repair;
	}
	public void setRepair(Double repair) {
		this.repair = repair;
	}
	public String getBearer() {
		return bearer;
	}
	public void setBearer(String bearer) {
		this.bearer = bearer;
	}
	@Override
	public String toString() {
		return "IntelligentLockServiceRemark [idd=" + idd + ", promoter=" + promoter + ", ordernumber=" + ordernumber
				+ ", status=" + status + ", installprincipal=" + installprincipal + ", ordertime=" + ordertime
				+ ", deliverytime=" + deliverytime + ", logistics=" + logistics + ", arrivaltime=" + arrivaltime
				+ ", guidecustommadetime=" + guidecustommadetime + ", guidesigningtime=" + guidesigningtime
				+ ", reservationtime=" + reservationtime + ", ordersuctime=" + ordersuctime + ", contact=" + contact
				+ ", phone=" + phone + ", addr=" + addr + ", channel=" + channel + ", type=" + type + ", model=" + model
				+ ", spec=" + spec + ", num=" + num + ", material=" + material + ", thickness=" + thickness
				+ ", opendirection=" + opendirection + ", hook=" + hook + ", guidecustommade=" + guidecustommade
				+ ", guidetype=" + guidetype + ", guidesize=" + guidesize + ", installationprice=" + installationprice
				+ ", guidecustommadeprice=" + guidecustommadeprice + ", measureprice=" + measureprice
				+ ", emptyrunprice=" + emptyrunprice + ", totalprice=" + totalprice + ", remark=" + remark + ", repair="
				+ repair + ", bearer=" + bearer + "]";
	}
	
}