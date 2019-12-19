package models;

import java.io.Serializable;

public class InsurancePay implements Serializable{
	private static final long serialVersionUID = 5116527747952746867L;
	
	private String total_fee;//支付金额	String	单位:元（精确到小数点后两位）
	private String out_trade_no;//支付流水号	String(<50)	必传
	private String productname;//产品名称	String	必传
	private String notifyurl;//后台回调地址	String	需要提前提供核心配置
	private String callbackurl;//成功页面跳转地址	String	必传
	private String failurl;//失败页面跳转地址	String	必传
	private String channel_id;//渠道编号	String	必传
	private String sign;//	String	用于校验数据，对如：total_fee=1600&key=md5_key(参见环境参数)的内容MD5运算获得（运算方式参见开发指南->api使用指南中的java代码样例）
	private String payType;//支付方式	String(2)	01 : 微信WAP支付 02 : 支付宝WAP支付 03：微信扫码支付 04：微信公众号oath支付 05:微信公众号支付 06:微信小程序支付 07:支付宝扫码支付
	private String open_id;//微信小程序	String	微信open_id
	private String app_id;//小程序ID	String	微信app_id
	
	public String getTotal_fee() {
		return total_fee;
	}
	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}
	public String getOut_trade_no() {
		return out_trade_no;
	}
	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}
	public String getProductname() {
		return productname;
	}
	public void setProductname(String productname) {
		this.productname = productname;
	}
	public String getNotifyurl() {
		return notifyurl;
	}
	public void setNotifyurl(String notifyurl) {
		this.notifyurl = notifyurl;
	}
	public String getCallbackurl() {
		return callbackurl;
	}
	public void setCallbackurl(String callbackurl) {
		this.callbackurl = callbackurl;
	}
	public String getFailurl() {
		return failurl;
	}
	public void setFailurl(String failurl) {
		this.failurl = failurl;
	}
	public String getChannel_id() {
		return channel_id;
	}
	public void setChannel_id(String channel_id) {
		this.channel_id = channel_id;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getOpen_id() {
		return open_id;
	}
	public void setOpen_id(String open_id) {
		this.open_id = open_id;
	}
	public String getApp_id() {
		return app_id;
	}
	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}
	@Override
	public String toString() {
		return "InsurancePay [total_fee=" + total_fee + ", out_trade_no=" + out_trade_no + ", productname="
				+ productname + ", notifyurl=" + notifyurl + ", callbackurl=" + callbackurl + ", failurl=" + failurl
				+ ", channel_id=" + channel_id + ", sign=" + sign + ", payType=" + payType + ", open_id=" + open_id
				+ ", app_id=" + app_id + "]";
	}
	
}
