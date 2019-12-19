package app.dto;

import java.io.Serializable;

public class AppointInitOrderDto implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String idd;		//预约订单id

	private String title;	//商品名称
	
	private String status;	//订单状态
	
	private String smallimgurl; //商品小图
	
	public String getSmallimgurl() {
		return smallimgurl;
	}

	public void setSmallimgurl(String smallimgurl) {
		this.smallimgurl = smallimgurl;
	}

	public String getIdd() {
		return idd;
	}

	public void setIdd(String idd) {
		this.idd = idd;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
