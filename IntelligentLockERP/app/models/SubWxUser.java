package models;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author xuml 实体类
 * @date 2019年08月28日 05:28:06
 * @discription
 */ 
@Entity
@Table(name = "sub_wx_user")


public class SubWxUser implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "idd")
	private Integer idd;

	@Column(name = "openid")
	private String openid;

	@Column(name = "phone")
	private String phone;

	@Column(name = "addtime")
	private Date addtime;
	
	@Column(name = "ch")
	private Integer ch;

	public void setIdd(Integer idd){
		this.idd=idd;
	}
	public Integer getIdd(){
		return idd;
	}
	public void setOpenid(String openid){
		this.openid=openid;
	}
	public String getOpenid(){
		return openid;
	}
	public void setPhone(String phone){
		this.phone=phone;
	}
	public String getPhone(){
		return phone;
	}
	public void setAddtime(Date addtime){
		this.addtime=addtime;
	}
	public Date getAddtime(){
		return addtime;
	}
	public Integer getCh() {
		return ch;
	}
	public void setCh(Integer ch) {
		this.ch = ch;
	}
	
}

