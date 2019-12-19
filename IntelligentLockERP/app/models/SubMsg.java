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
 * @date 2019年08月28日 05:28:13
 * @discription
 */ 
@Entity
@Table(name = "sub_msg")


public class SubMsg implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "idd")
	private Integer idd;

	@Column(name = "phone")
	private String phone;

	@Column(name = "content")
	private String content;	//消息内容

	@Column(name = "addtime")
	private Date addtime;

	@Column(name = "act")
	private Integer act;	//1安装  2测量

	@Column(name = "oid")
	private Integer oid;	//安装或者测量的订单表ID

	public void setIdd(Integer idd){
		this.idd=idd;
	}
	public Integer getIdd(){
		return idd;
	}
	public void setPhone(String phone){
		this.phone=phone;
	}
	public String getPhone(){
		return phone;
	}
	public void setContent(String content){
		this.content=content;
	}
	public String getContent(){
		return content;
	}
	public void setAddtime(Date addtime){
		this.addtime=addtime;
	}
	public Date getAddtime(){
		return addtime;
	}
	public void setAct(Integer act){
		this.act=act;
	}
	public Integer getAct(){
		return act;
	}
	public void setOid(Integer oid){
		this.oid=oid;
	}
	public Integer getOid(){
		return oid;
	}
}

