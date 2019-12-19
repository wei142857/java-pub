package app.dto;

import java.io.Serializable;

public class ShareEShopProductDto implements Serializable{

	private static final long serialVersionUID = 1L;

	private String title;	//分享标题
	
	private String desc;	//分享描述
	
	private String imgurl;	//分享图标
	
	private String linkurl;	//分享链接

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "ShareEShopProductDto [title=" + title + ", desc=" + desc
				+ ", imgurl=" + imgurl + ", linkurl=" + linkurl + "]";
	}
}
