package models;

import java.io.Serializable;
import java.util.Date;

public class SmartAppUserExtend implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Integer userid;	//用户ID
	
	private String phone;  //手机号
	
	private String nickname; //用户昵称
	
	private Integer vipStatus;  //会员状态(0:非会员 1:会员 2:会员过期)
	
	private Date overtime;		//会员到期时间
	
	private Integer bvalue;    //剩余奖励金
	
	private Integer shareBvalue;	//分享得到的奖励金

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Integer getVipStatus() {
		if(vipStatus==null){
			if(overtime==null){
				return 0;
			}else if(overtime.getTime()>(new Date().getTime())){
				return 1;
			}else{
				return 2;
			}
		}
		return vipStatus;
	}

	public void setVipStatus(Integer vipStatus) {
		this.vipStatus = vipStatus;
	}

	public Date getOvertime() {
		return overtime;
	}

	public void setOvertime(Date overtime) {
		this.overtime = overtime;
	}

	public Integer getBvalue() {
		return bvalue;
	}

	public void setBvalue(Integer bvalue) {
		this.bvalue = bvalue;
	}

	public Integer getShareBvalue() {
		return shareBvalue;
	}

	public void setShareBvalue(Integer shareBvalue) {
		this.shareBvalue = shareBvalue;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "SmartAppUserExtend [userid=" + userid + ", phone=" + phone
				+ ", nickname=" + nickname + ", vipStatus=" + vipStatus
				+ ", overtime=" + overtime + ", bvalue=" + bvalue
				+ ", shareBvalue=" + shareBvalue + "]";
	}
}
