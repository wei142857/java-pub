package app.dto;

import java.io.Serializable;
import java.util.List;

public class ExchangeListDto implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer totalpages;	//总页数
	private Integer pageno;	//页号
	private List<Item> items;
	
	public Integer getTotalpages() {
		return totalpages;
	}
	public void setTotalpages(Integer totalpages) {
		this.totalpages = totalpages;
	}
	public Integer getPageno() {
		return pageno;
	}
	public void setPageno(Integer pageno) {
		this.pageno = pageno;
	}
	public List<Item> getItems() {
		return items;
	}
	public void setItems(List<Item> items) {
		this.items = items;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static class Item implements Serializable{

		private static final long serialVersionUID = 1L;
		
		private String timestamp;	//兑换时间("2018-12-12 12:12:12")
		private String title;	//产品title("10元话费")
		private String bvalue;	//奖励金("100奖励金")
		private String state;	//状态("充值中"//充值中，充值失败，充值成功)
		public String getTimestamp() {
			return timestamp;
		}
		public void setTimestamp(String timestamp) {
			this.timestamp = timestamp;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getBvalue() {
			return bvalue;
		}
		public void setBvalue(String bvalue) {
			this.bvalue = bvalue;
		}
		public String getState() {
			return state;
		}
		public void setState(String state) {
			this.state = state;
		}
		public static long getSerialversionuid() {
			return serialVersionUID;
		}
	}
}
