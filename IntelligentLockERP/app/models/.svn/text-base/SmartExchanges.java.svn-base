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
 * @date 2019年05月31日 12:31:24
 * @discription
 */ 
@Entity
@Table(name = "smart_exchanges")


public class SmartExchanges implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "idd")
	private Integer idd;

	@Column(name = "userid")
	private Integer userid;

	@Column(name = "account")
	private String account;

	@Column(name = "pid")
	private Integer pid;

	@Column(name = "bvalue")
	private Integer bvalue;

	@Column(name = "addtime")
	private Date addtime;
	
	@Column(name = "acttime")
	private Date acttime;

	@Column(name = "status")
	private Integer status;

	@Column(name = "returninfo")
	private String returninfo;

	@Column(name = "means")
	private String means;
	
	@Column(name = "realmeans")
	private String realmeans;

	@Column(name = "retid")
	private String retid;

	@Column(name = "ip")
	private String ip;

	@Column(name = "area")
	private String area;
	
	@Column(name = "channel")
	private String channel;

	public void setIdd(Integer idd){
		this.idd=idd;
	}
	public Integer getIdd(){
		return idd;
	}
	public void setUserid(Integer userid){
		this.userid=userid;
	}
	public Integer getUserid(){
		return userid;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public void setPid(Integer pid){
		this.pid=pid;
	}
	public Integer getPid(){
		return pid;
	}
	public void setBvalue(Integer bvalue){
		this.bvalue=bvalue;
	}
	public Integer getBvalue(){
		return bvalue;
	}
	public void setAddtime(Date addtime){
		this.addtime=addtime;
	}
	public Date getAddtime(){
		return addtime;
	}
	public void setStatus(Integer status){
		this.status=status;
	}
	public Integer getStatus(){
		return status;
	}
	public void setReturninfo(String returninfo){
		this.returninfo=returninfo;
	}
	public String getReturninfo(){
		return returninfo;
	}
	public void setMeans(String means){
		this.means=means;
	}
	public String getMeans(){
		return means;
	}
	public void setRetid(String retid){
		this.retid=retid;
	}
	public String getRetid(){
		return retid;
	}
	public void setIp(String ip){
		this.ip=ip;
	}
	public String getIp(){
		return ip;
	}
	public void setArea(String area){
		this.area=area;
	}
	public String getArea(){
		return area;
	}
	public Date getActtime() {
		return acttime;
	}
	public void setActtime(Date acttime) {
		this.acttime = acttime;
	}
	public String getRealmeans() {
		return realmeans;
	}
	public void setRealmeans(String realmeans) {
		this.realmeans = realmeans;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	
}

