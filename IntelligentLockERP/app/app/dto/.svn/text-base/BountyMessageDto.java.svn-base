package app.dto;

import java.io.Serializable;
import java.util.List;

public class BountyMessageDto implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Integer totalpages;	//总页数
	private Integer pageno;		//页号
	private String totalbvalue;//本月获取的奖励金数
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
	public String getTotalbvalue() {
		return totalbvalue;
	}
	public void setTotalbvalue(String totalbvalue) {
		this.totalbvalue = totalbvalue;
	}


	public static class Item implements Serializable{

		private static final long serialVersionUID = 1L;
		
		private String title;		//标题("参与奖励计划_领取")
        private String gaintime; 	//获取时间("2019-01-01")
        private String bvalue; 		//奖励金数量("+10")
        private String overtime;	//过期时间("2019-12-01")
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getGaintime() {
			return gaintime;
		}
		public void setGaintime(String gaintime) {
			this.gaintime = gaintime;
		}
		public String getBvalue() {
			return bvalue;
		}
		public void setBvalue(String bvalue) {
			this.bvalue = bvalue;
		}
		public String getOvertime() {
			return overtime;
		}
		public void setOvertime(String overtime) {
			this.overtime = overtime;
		}
		public static long getSerialversionuid() {
			return serialVersionUID;
		}
	}
}
