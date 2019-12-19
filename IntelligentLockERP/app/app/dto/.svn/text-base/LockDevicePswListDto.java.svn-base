package app.dto;

import java.io.Serializable;
import java.util.List;

public class LockDevicePswListDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Item> items;
	

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
		private String pswname;//设备密码名称
		private String pswcode;//设备密码
		private String pswvalidity;//设备密码有效期， -1：表示永久有效
		private Long pswid;
		private Integer pswstatu;//设备状态  0正常 1添加中 2 删除中
		private String pswbegintime;//设备密码的有效期开始时间
		public String getPswname() {
			return pswname;
		}
		public void setPswname(String pswname) {
			this.pswname = pswname;
		}
		public String getPswcode() {
			return pswcode;
		}
		public void setPswcode(String pswcode) {
			this.pswcode = pswcode;
		}
		public String getPswvalidity() {
			return pswvalidity;
		}
		public void setPswvalidity(String pswvalidity) {
			this.pswvalidity = pswvalidity;
		}
		public Long getPswid() {
			return pswid;
		}
		public void setPswid(Long pswid) {
			this.pswid = pswid;
		}
		public Integer getPswstatu() {
			return pswstatu;
		}
		public void setPswstatu(Integer pswstatu) {
			this.pswstatu = pswstatu;
		}
		public String getPswbegintime() {
			return pswbegintime;
		}
		public void setPswbegintime(String pswbegintime) {
			this.pswbegintime = pswbegintime;
		}
		
	}
}
