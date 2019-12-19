package app.dto;

import java.io.Serializable;

public class GetBuyVipResultDto implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String overtime;	//过期时间

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
