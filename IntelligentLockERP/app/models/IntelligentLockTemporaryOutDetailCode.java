package models;

import java.io.Serializable;

/**
 * 用于临时存放 出库明细表 和 产品编码code 的对应关系
 * 辅助IntelligentLockOutDetailCode类
 * intelligentlock_out_detail_code表存储数据
 * @author 陈宏亮
 *
 */
public class IntelligentLockTemporaryOutDetailCode implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer productid;		//产品ID
	
	private String channel;		//出货渠道
	
	private String type;		//类型
	
	private String salenumber;	//销售平台订单号
	
	private String code;		//产品编码
	
	private String operatornumber; //操作号

	public Integer getProductid() {
		return productid;
	}

	public void setProductid(Integer productid) {
		this.productid = productid;
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getOperatornumber() {
		return operatornumber;
	}

	public void setOperatornumber(String operatornumber) {
		this.operatornumber = operatornumber;
	}

	@Override
	public String toString() {
		return "IntelligentLockTemporaryOutDetailCode [productid=" + productid + ", channel=" + channel + ", type="
				+ type + ", salenumber=" + salenumber + ", code=" + code + ", operatornumber=" + operatornumber + "]";
	}
}
