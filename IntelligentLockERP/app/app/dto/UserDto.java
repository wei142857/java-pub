package app.dto;

import java.io.Serializable;

public class UserDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String token;//
	private String nickname;//昵称
	private String avatarurl;//头像地址
	private String phonenum;
	private Integer pushstatus;//推送状态
	
	public String getPhonenum() {
		return phonenum;
	}
	public void setPhonenum(String phonenum) {
		this.phonenum = phonenum;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getAvatarurl() {
		return avatarurl;
	}
	public void setAvatarurl(String avatarurl) {
		this.avatarurl = avatarurl;
	}
	public Integer getPushstatus() {
		return pushstatus;
	}
	public void setPushstatus(Integer pushstatus) {
		this.pushstatus = pushstatus;
	}
	
}
