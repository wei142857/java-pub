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
 * @date 2019年06月04日 05:04:37
 * @discription
 */ 
@Entity
@Table(name = "smart_payorder")


public class SmartPayOrder implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "idd")
	private Integer idd;

	@Column(name = "userid")
	private Integer userid;

	@Column(name = "out_trade_no")
	private String outTradeNo;

	@Column(name = "transaction_id")
	private String transactionId;

	@Column(name = "addtime")
	private Date addtime;

	@Column(name = "total_fee")
	private Integer totalFee;

	@Column(name = "status")
	private Integer status;	//1待支付  2支付成功 3已退款

	@Column(name = "refund_desc")
	private String refundDesc;	//退款原因

	@Column(name = "notifytime")
	private Date notifytime;

	@Column(name = "channel")
	private String channel;

	@Column(name = "remark")
	private String remark;	//商品备注

	@Column(name = "prepay_id")
	private String prepayId;

	@Column(name = "type")
	private Integer type;	//1微信 2支付宝

	@Column(name = "attach")
	private String attach;	//订单信息
	
	@Column(name = "ip")
	private String ip;

	public void setIdd(Integer idd){
		this.idd=idd;
	}
	public Integer getIdd(){
		return idd;
	}
	public void setUserid(Integer userid){
		this.userid=userid;
	}
	public Integer getUserid(){
		return userid;
	}
	public void setOutTradeNo(String outTradeNo){
		this.outTradeNo=outTradeNo;
	}
	public String getOutTradeNo(){
		return outTradeNo;
	}
	public void setTransactionId(String transactionId){
		this.transactionId=transactionId;
	}
	public String getTransactionId(){
		return transactionId;
	}
	public void setAddtime(Date addtime){
		this.addtime=addtime;
	}
	public Date getAddtime(){
		return addtime;
	}
	public void setTotalFee(Integer totalFee){
		this.totalFee=totalFee;
	}
	public Integer getTotalFee(){
		return totalFee;
	}
	public static Integer STATUS_TODO=1;
	public static Integer STATUS_SUCCESS=2;
	public static Integer STATUS_REFUND=3;
	public void setStatus(Integer status){
		this.status=status;
	}
	public Integer getStatus(){
		return status;
	}
	public void setRefundDesc(String refundDesc){
		this.refundDesc=refundDesc;
	}
	public String getRefundDesc(){
		return refundDesc;
	}
	public void setNotifytime(Date notifytime){
		this.notifytime=notifytime;
	}
	public Date getNotifytime(){
		return notifytime;
	}
	public void setChannel(String channel){
		this.channel=channel;
	}
	public String getChannel(){
		return channel;
	}
	public void setRemark(String remark){
		this.remark=remark;
	}
	public String getRemark(){
		return remark;
	}
	public void setPrepayId(String prepayId){
		this.prepayId=prepayId;
	}
	public String getPrepayId(){
		return prepayId;
	}
	public static Integer TYPE_WX = 1;
	public static Integer TYPE_ALI = 2;
	public void setType(Integer type){
		this.type=type;
	}
	public Integer getType(){
		return type;
	}
	public void setAttach(String attach){
		this.attach=attach;
	}
	public String getAttach(){
		return attach;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	
}

