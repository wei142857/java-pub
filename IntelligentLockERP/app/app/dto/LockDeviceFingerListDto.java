package app.dto;

import java.io.Serializable;
import java.util.List;

public class LockDeviceFingerListDto implements Serializable{
	
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
		private String fingerprintname;//设备指纹名称
		private String fingerprintvalidity; //设备指纹有效期， -1：表示永久有效
		private Long fingerprintid;//设备指纹id
		private Integer pswstatu;//设备状态  0正常 1添加中 2 删除中
		public String getFingerprintname() {
			return fingerprintname;
		}
		public void setFingerprintname(String fingerprintname) {
			this.fingerprintname = fingerprintname;
		}
		public String getFingerprintvalidity() {
			return fingerprintvalidity;
		}
		public void setFingerprintvalidity(String fingerprintvalidity) {
			this.fingerprintvalidity = fingerprintvalidity;
		}
		public Long getFingerprintid() {
			return fingerprintid;
		}
		public void setFingerprintid(Long fingerprintid) {
			this.fingerprintid = fingerprintid;
		}
		public Integer getPswstatu() {
			return pswstatu;
		}
		public void setPswstatu(Integer pswstatu) {
			this.pswstatu = pswstatu;
		}
		
	}
}
