package models;
import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author xuml 实体类
 * @date 2019年05月31日 12:31:26
 * @discription
 */ 
@Entity
@Table(name = "smart_interface")


public class SmartInterface implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "idd")
	private Integer idd;

	@Column(name = "name")
	private String name;

	@Column(name = "url")
	private String url;

	@Column(name = "appkey")
	private String appkey;

	@Column(name = "appsecret")
	private String appsecret;

	@Column(name = "appid")
	private String appid;

	@Column(name = "ext")
	private String ext;

	@Column(name = "remark")
	private String remark;

	@Column(name = "classname")
	private String classname;

	public void setIdd(Integer idd){
		this.idd=idd;
	}
	public Integer getIdd(){
		return idd;
	}
	public void setName(String name){
		this.name=name;
	}
	public String getName(){
		return name;
	}
	public void setUrl(String url){
		this.url=url;
	}
	public String getUrl(){
		return url;
	}
	public void setAppkey(String appkey){
		this.appkey=appkey;
	}
	public String getAppkey(){
		return appkey;
	}
	public void setAppsecret(String appsecret){
		this.appsecret=appsecret;
	}
	public String getAppsecret(){
		return appsecret;
	}
	public void setAppid(String appid){
		this.appid=appid;
	}
	public String getAppid(){
		return appid;
	}
	public void setExt(String ext){
		this.ext=ext;
	}
	public String getExt(){
		return ext;
	}
	public void setRemark(String remark){
		this.remark=remark;
	}
	public String getRemark(){
		return remark;
	}
	public void setClassname(String classname){
		this.classname=classname;
	}
	public String getClassname(){
		return classname;
	}
}

