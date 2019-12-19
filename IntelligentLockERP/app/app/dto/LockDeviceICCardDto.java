package app.dto;

import java.io.Serializable;
import java.util.List;

public class LockDeviceICCardDto implements Serializable{
	
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
		private String iccardname;//设备指纹名称
		private String icardvalidity; //设备指纹有效期， -1：表示永久有效
		private Long iccardid;//设备指纹id
		private Integer iccardstatu;//设备状态  0正常 1添加中 2 删除中
		
		public String getIccardname() {
			return iccardname;
		}
		public void setIccardname(String iccardname) {
			this.iccardname = iccardname;
		}
		public String getIcardvalidity() {
			return icardvalidity;
		}
		public void setIcardvalidity(String icardvalidity) {
			this.icardvalidity = icardvalidity;
		}
		public Long getIccardid() {
			return iccardid;
		}
		public void setIccardid(Long iccardid) {
			this.iccardid = iccardid;
		}
		public Integer getIccardstatu() {
			return iccardstatu;
		}
		public void setIccardstatu(Integer iccardstatu) {
			this.iccardstatu = iccardstatu;
		}
		
	}
}
