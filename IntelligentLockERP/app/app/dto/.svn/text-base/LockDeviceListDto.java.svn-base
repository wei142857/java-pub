package app.dto;

import java.io.Serializable;
import java.util.List;

public class LockDeviceListDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<Img> imgs;
	
	private List<Item> items;
	
	private String informgetbounty;	//今天是否领取过奖励金
	
	public String getInformgetbounty() {
		return informgetbounty;
	}
	public void setInformgetbounty(String informgetbounty) {
		this.informgetbounty = informgetbounty;
	}

	public List<Img> getImgs() {
		return imgs;
	}

	public void setImgs(List<Img> imgs) {
		this.imgs = imgs;
	}

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public static class Img implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String imgurl;//宣传图
		private String linkurl;//链接
		public String getImgurl() {
			return imgurl;
		}
		public void setImgurl(String imgurl) {
			this.imgurl = imgurl;
		}
		public String getLinkurl() {
			return linkurl;
		}
		public void setLinkurl(String linkurl) {
			this.linkurl = linkurl;
		}
	}
	
	public static class Item implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String lockname;//设备名称（用户定义）
		private String lockimgurl;//设备logo
		private String locktype;//设备型号
		private Integer lockid;//设备ID
		private Integer lockstatus;//设备状态 0- 不在线，1-在线
		private String lockelectricity;//设备电量
		private String operaterecord;//最后一次设备操作记录
		private Integer operationstatus;//操作记录状态 0 ：OK 1：非法
		public String getLockname() {
			return lockname;
		}
		public void setLockname(String lockname) {
			this.lockname = lockname;
		}
		public String getLocktype() {
			return locktype;
		}
		public void setLocktype(String locktype) {
			this.locktype = locktype;
		}
		public Integer getLockid() {
			return lockid;
		}
		public void setLockid(Integer lockid) {
			this.lockid = lockid;
		}
		public Integer getLockstatus() {
			return lockstatus;
		}
		public void setLockstatus(Integer lockstatus) {
			this.lockstatus = lockstatus;
		}
		public String getLockelectricity() {
			return lockelectricity;
		}
		public void setLockelectricity(String lockelectricity) {
			this.lockelectricity = lockelectricity;
		}
		public String getOperaterecord() {
			return operaterecord;
		}
		public void setOperaterecord(String operaterecord) {
			this.operaterecord = operaterecord;
		}
		public String getLockimgurl() {
			return lockimgurl;
		}
		public void setLockimgurl(String lockimgurl) {
			this.lockimgurl = lockimgurl;
		}
		public Integer getOperationstatus() {
			return operationstatus;
		}
		public void setOperationstatus(Integer operationstatus) {
			this.operationstatus = operationstatus;
		}
		
	}
}
