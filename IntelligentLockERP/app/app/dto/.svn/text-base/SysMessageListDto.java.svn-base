package app.dto;

import java.io.Serializable;
import java.util.List;

public class SysMessageListDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer totalpages;
	private Integer pageno;
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

	public static class Item implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String msgcontent;//消息内容
		private String msgdate;//消息时间 2019-02-20 11:04:10
		private Long msgid;//消息ID
		public String getMsgcontent() {
			return msgcontent;
		}
		public void setMsgcontent(String msgcontent) {
			this.msgcontent = msgcontent;
		}
		public String getMsgdate() {
			return msgdate;
		}
		public void setMsgdate(String msgdate) {
			this.msgdate = msgdate;
		}
		public Long getMsgid() {
			return msgid;
		}
		public void setMsgid(Long msgid) {
			this.msgid = msgid;
		} 
		
	}
	
}
