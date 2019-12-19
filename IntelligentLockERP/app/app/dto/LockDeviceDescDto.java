package app.dto;

import java.io.Serializable;

public class LockDeviceDescDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String link;
	private String descinfo;
	

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getDescinfo() {
		return descinfo;
	}

	public void setDescinfo(String descinfo) {
		this.descinfo = descinfo;
	}
	
	
	
}
