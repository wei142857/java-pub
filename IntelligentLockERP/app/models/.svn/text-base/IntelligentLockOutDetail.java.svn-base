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
 * 出库单明细表的实体类
 * @author 陈宏亮
 * @date	2019年1月31号
 */
@Entity
@Table(name="intelligentlock_out_detail")
public class IntelligentLockOutDetail implements Serializable{
	
	private static final long serialVersionUID = 2188356568128547305L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Basic(optional=false)
	@Column(name="idd")
	private Integer idd;
	
	@Column(name="operatornumber")
	private String operatornumber;	//操作号
	
	@Column(name="productid")
	private Integer productid;	//产品ID
	
	@Column(name="title")
	private String title;	//产品名称
	
	@Column(name="model")
	private String model;	//型号
	
	@Column(name="spec")
	private String spec;	//规格
	
	@Column(name="amount")
	private Integer amount;	//总数
	
	@Column(name="price")
	private Double price;	//单价
	
	@Column(name="totalprice")
	private Double totalprice;	//总价
	
	@Column(name = "channel")
	private String channel;	//出货渠道
	
	@Column(name = "type")
	private String type;	//类型
	
	@Column(name = "salenumber")
	private String salenumber;	//外部销售平台订单号
	
	@Column(name="remark")
	private String remark;	//备注
	
	@Column(name="addtime")
	private Date addtime;	//添加时间
	
	@Column(name="operator")
	private String operator;	//操作人
	
	@Column(name="outid")
	private Integer outid;	//出库单ID

	public Integer getIdd() {
		return idd;
	}

	public void setIdd(Integer idd) {
		this.idd = idd;
	}

	public String getOperatornumber() {
		return operatornumber;
	}

	public void setOperatornumber(String operatornumber) {
		this.operatornumber = operatornumber;
	}

	public Integer getProductid() {
		return productid;
	}

	public void setProductid(Integer productid) {
		this.productid = productid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getTotalprice() {
		return totalprice;
	}

	public void setTotalprice(Double totalprice) {
		this.totalprice = totalprice;
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

	public String getSalenumber() {
		return salenumber;
	}

	public void setSalenumber(String salenumber) {
		this.salenumber = salenumber;
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

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Integer getOutid() {
		return outid;
	}

	public void setOutid(Integer outid) {
		this.outid = outid;
	}

	@Override
	public String toString() {
		return "IntelligentLockOutDetail [idd=" + idd + ", operatornumber="
				+ operatornumber + ", productid=" + productid + ", title="
				+ title + ", model=" + model + ", spec=" + spec + ", amount="
				+ amount + ", price=" + price + ", totalprice=" + totalprice
				+ ", channel=" + channel + ", type=" + type + ", salenumber="
				+ salenumber + ", remark=" + remark + ", addtime=" + addtime
				+ ", operator=" + operator + ", outid=" + outid + "]";
	}
}
