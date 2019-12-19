/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package models;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import net.SmartMsgUtil;

/**
 *
 * @author lzs
 */
@Entity
@Table(name = "smart_msg")

public class SmartNetMsg implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idd")
    private Long idd; //ID
    
    @Column(name = "tag")
    private Integer tag;  //通信标识
    
    @Column(name = "rtime")
    private Integer rtime;  //通信标识
    
    public Integer getRtime() {
		return rtime;
	}

	public void setRtime(Integer rtime) {
		this.rtime = rtime;
	}

	@Column(name = "type")
    private Integer type ; //类型
    
    @Column(name = "dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dt;   //时间
    
    @Column(name = "dealed")
    private Integer dealed;  //是否处理
    
    @Column(name = "dealtime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dealtime;   //注册时间
    
    @Column(name = "begintime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date begintime;   //开始发送的时间
    
    public Date getBegintime() {
		return begintime;
	}

	public void setBegintime(Date begintime) {
		this.begintime = begintime;
	}

	@Column(name = "lockid")
    private String lockid;  //锁
    
    @Column(name = "rc4")
    private String secretMsg;  //加解密
    
    public String getSecretMsg() {
		return secretMsg;
	}

	public void setSecretMsg(String secretMsg) {
		this.secretMsg = secretMsg;
	}

    @Column(name = "len")
    private Integer len;  //长度
    
    public Date getReplytime() {
		return replytime;
	}

	public void setReplytime(Date replytime) {
		this.replytime = replytime;
	}

	public Integer getReply() {
		return reply;
	}

	public void setReply(Integer reply) {
		this.reply = reply;
	}

	public String getReplymsg() {
		return replymsg;
	}

	public void setReplymsg(String replymsg) {
		this.replymsg = replymsg;
	}

	@Column(name = "mid")
    private Integer mid;  //传输ID
    
    public Integer getMid() {
		return mid;
	}

	public void setMid(Integer mid) {
		this.mid = mid;
	}

	public Integer getTag() {
		return tag;
	}

	public void setTag(Integer tag) {
		this.tag = tag;
	}

    @Column(name = "msg")
    private String msg;   //内容
    
    @Column(name = "ext")
    private String ext;  //说明
    
    @Column(name = "ext1")
    private String ext1;  //说明
    
    public String getExt1() {
		return ext1;
	}

	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}

	public String getExt2() {
		return ext2;
	}

	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}

	@Column(name = "ext2")
    private String ext2;  //说明
    
    @Column(name = "replytime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date replytime;   //回复时间
    
    @Column(name = "reply")
    private Integer reply;  //回复的结果
    
    @Column(name = "replymsg")
    private String replymsg;  //回复的消息
    

    public Long getIdd() {
		return idd;
	}

	public void setIdd(Long idd) {
		this.idd = idd;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Date getDt() {
		return dt;
	}

	public void setDt(Date dt) {
		this.dt = dt;
	}

	public Integer getDealed() {
		return dealed;
	}

	public void setDealed(Integer dealed) {
		this.dealed = dealed;
	}

	public Date getDealtime() {
		return dealtime;
	}

	public void setDealtime(Date dealtime) {
		this.dealtime = dealtime;
	}

	public String getLockid() {
		return lockid;
	}

	public void setLockid(String lockid) {
		this.lockid = lockid;
	}

	public Integer getLen() {
		return len;
	}

	public void setLen(Integer len) {
		this.len = len;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public SmartNetMsg() {
		reply=0;
		dealed=0;
		tag = 0x20;
		rtime=0;
    }

    public SmartNetMsg(Long idd) {
        this.idd = idd;
    }

    @Transient
    public byte[] data;
    
    //锁信息上报的 字段
    @Transient
    public String deviceId;
    @Transient
    public int    deviceType; //型号
    @Transient
    public String deviceTypeCode; //型号编码
    
    @Transient
    public String verison;
    
    //关联、非关联 信息上报
    @Transient
    public int    info;
    @Transient
    public String userCode;
    
    //reset
    @Transient
    public String timestamp;
    @Transient
    public String sign;
    
    @Transient
    public boolean rc4=false;
    
    //描述信息--显示后台用
    @Column(name = "partmsg")
    public String descMsg = "";
    
    public String getDescMsg() {
		return descMsg;
	}
	public void setDescMsg(String descMsg) {
		this.descMsg = descMsg;
	}

	public String toString(SmartNetMsg msg)
    {
    	String ret ="";
		
    	// 来自锁的消息
    	if ( msg.getTag() == 0x10) {
    		ret += "@@锁上传消息\r\n--------------------";
    		ret += "消息流水："+msg.getMid()+" -- 通信类型："+msg.getTag()+" -- 消息类型："+msg.getType()+"\r\n--------------------";
    		// 连接
			if (msg.getType() == SmartMsgUtil.CMD_LOCK_CONNECT) {
				// 上报的信息. 终端类型1byte , 终端型号2byte, 终端 ID(BCD编码)
				ret += "锁连接消息：\r\n--------------------";
				ret += "设备类型 ："+msg.deviceType;
				ret += ",设备类型编码 ："+msg.deviceTypeCode;
				ret += ",设备ID ："+msg.deviceId;
			}

			// 关联用户状态信息上报
			else if (msg.getType() >= SmartMsgUtil.CMD_LOCK_OPEN
					&& msg.getType() <= SmartMsgUtil.CMD_LOCK_DELUSER) {
				if (msg.getType() == SmartMsgUtil.CMD_LOCK_OPEN)
						ret += "开锁：\r\n--------------------";
				if (msg.getType() == SmartMsgUtil.CMD_LOCK_LOCK)
					ret += "关锁：\r\n--------------------";
				if (msg.getType() == SmartMsgUtil.CMD_LOCK_ADDUSER)
					ret += "添加用户：\r\n--------------------";
				if (msg.getType() == SmartMsgUtil.CMD_LOCK_DELUSER)
					ret += "删除用户：\r\n--------------------";
				
				// 上报的信息. 类型1字节，用户编号2字节， 终端 ID 8byte
				ret += "用户类型："+msg.info;
				ret += ",用户编码："+msg.userCode;
				ret += ",设备ID："+msg.deviceId;
			}
			// 非关联用户状态信息上报
			else if (msg.getType() >= SmartMsgUtil.CMD_LOCK_REMOTE_OPEN
					&& msg.getType() <= SmartMsgUtil.CMD_LOCK_POWER) {
				if (msg.getType() == SmartMsgUtil.CMD_LOCK_POWER){
					ret += "电量上报：\r\n--------------------";
					ret += "电量："+msg.info ;
				}
				if (msg.getType() == SmartMsgUtil.CMD_LOCK_ALARM){
					ret += "锁报警：\r\n--------------------";
					ret += "行为："+msg.info ;
				}
				// 上报的信息. 内容 1字节，终端 ID
				ret += ",设备ID："+msg.deviceId;
			}
			// reset
			else if (msg.getType() == SmartMsgUtil.CMD_LOCK_RESET) {
				ret += "锁重置清空数据：\r\n--------------------";
				// Reset. 时间戳8Byte，终端 ID 8byte，签名
				ret += "时间戳："+msg.timestamp ;
				ret += ",设备ID："+msg.deviceId;
				ret += ",签名："+msg.sign;
			} else if (msg.getType() == SmartMsgUtil.CMD_LOCK_TIME) {
				ret += "查询时间：\r\n--------------------";
				ret += "设备ID："+msg.deviceId;
			}
    	}
    	if (msg.getTag() == 0x21) {
    		ret += "锁回复消息\r\n--------------------";
    		ret += "消息流水："+msg.getMid()+" -- 通信类型："+msg.getTag()+" -- 回复状态："+msg.getType()+"\r\n--------------------";
    		if (msg.getLen() >= 3 + 2 + 8)// 对应创建用户的应答，回复用户编号
			{
    			ret += "用户编码"+msg.userCode ;
    			ret += "，锁编码"+msg.deviceId ;
			} else if (msg.getLen() >= 3 + 8) {
				ret += "锁编码"+msg.deviceId ;
			}
    	}
    	
    	if ( msg.getTag() == 0x11 ){
    		ret += "服务器回复消息\r\n--------------------";
    		ret += "消息流水："+msg.getMid()+" -- 通信类型："+msg.getTag()+" -- 回复状态："+msg.getType()+"\r\n--------------------";
    		ret += msg.descMsg;
    	}
    	
    	if ( msg.getTag() == 0x20 ){
    		ret += "!!服务器指令\r\n--------------------";
    		ret += "消息流水："+msg.getMid()+" -- 通信类型："+msg.getTag()+" -- 命令："+msg.getType()+"\r\n--------------------";
    		if (msg.getType() == SmartMsgUtil.CMD_SVR_ADDUSER){
    			ret += "添加用户";
    		}
    		if (msg.getType() == SmartMsgUtil.CMD_SVR_ADD_PASS){
    			ret += "添加密码";
    		}
    		if (msg.getType() == SmartMsgUtil.CMD_SVR_ADD_TEMPPASS){
    			ret += "添加临时密码";
    		}
    		if (msg.getType() == SmartMsgUtil.CMD_SVR_DELPASS){
    			ret += "删除用户";
    		}
    		
    		ret += ","+msg.descMsg;
    	}
    	
    	return ret +"\r\n\r\n";
    }
	
	public String toStringHtml(SmartNetMsg msg ,String s)
    {
    	String ret ="";
		
    	// 来自锁的消息
    	if ( msg.getTag() == 0x10) {
    		//ret += "锁上传消息<br>";
    		ret += "消息流水(第1个字节"+s.substring(0, 1*2)
    				+ ")："+msg.getMid()+" <br> 通信类型(第2个字节"+s.substring(1*2, 2*2)
    						+ ")："+msg.getTag()+" <br> 消息类型(第3个字节"+s.substring(2*2, 3*2)
    								+ ")："+msg.getType()+"<br>";
    		// 连接
			if (msg.getType() == SmartMsgUtil.CMD_LOCK_CONNECT) {
				// 上报的信息. 终端类型1byte , 终端型号2byte, 终端 ID(BCD编码)
				ret += "消息类型说明：锁连接消息<br>";
				ret += "设备类型 (第4个字节"+s.substring(3*2, 4*2)
    				+ ")："+msg.deviceType;
				ret += "<br>设备类型编码(第5-6个字节"+s.substring(4*2, 6*2)
    				+ ")： "+msg.deviceTypeCode;
				ret += "<br>IMSI(第7-14个字节"+s.substring(6*2, s.length())
    				+ ")："+msg.deviceId;
			}

			// 关联用户状态信息上报
			else if (msg.getType() >= SmartMsgUtil.CMD_LOCK_OPEN
					&& msg.getType() <= SmartMsgUtil.CMD_LOCK_DELUSER) {
				if (msg.getType() == SmartMsgUtil.CMD_LOCK_OPEN)
						ret += "消息类型-开锁<br>";
				if (msg.getType() == SmartMsgUtil.CMD_LOCK_LOCK)
					ret += "关锁：<br>";
				if (msg.getType() == SmartMsgUtil.CMD_LOCK_ADDUSER)
					ret += "消息类型-添加用户<br>";
				if (msg.getType() == SmartMsgUtil.CMD_LOCK_DELUSER)
					ret += "消息类型-删除用户<br>";
				
				// 上报的信息. 类型1字节，用户编号2字节， 终端 ID 8byte
				ret += "<br>用户类型 (第4个字节"+s.substring(3*2, 4*2)
    				+ ")："+msg.info;
				ret += "<br>用户编码 (第5-6个字节"+s.substring(4*2, 6*2)
    				+ ")："+msg.userCode;
				ret += "<br>IMSI(第7-14个字节"+s.substring(6*2, s.length())
    				+ ")："+msg.deviceId;
			}
			// 非关联用户状态信息上报
			else if (msg.getType() >= SmartMsgUtil.CMD_LOCK_REMOTE_OPEN
					&& msg.getType() <= SmartMsgUtil.CMD_LOCK_POWER) {
				if (msg.getType() == SmartMsgUtil.CMD_LOCK_POWER){
					ret += "消息类型-电量上报<br>";
					ret += "<br>电量(第4个字节"+s.substring(3*2, 4*2)
		    				+ ")："+msg.info ;
					}
				if (msg.getType() == SmartMsgUtil.CMD_LOCK_ALARM){
					ret += "消息类型说明-锁报警<br>";
					ret += "<br>报警类型(第4个字节"+s.substring(3*2, 4*2)
    				+ ")："+msg.info ;
				}
				// 上报的信息. 内容 1字节，终端 ID
				ret += "<br>IMSI(第5-12个字节"+s.substring(4*2, s.length())
	    				+ ")："+msg.deviceId;
			}
			// reset
			else if (msg.getType() == SmartMsgUtil.CMD_LOCK_RESET) {
				ret += "消息类型-锁重置清空数据<br>";
				// Reset. 时间戳8Byte，终端 ID 8byte，签名
				ret += "<br>时间戳(第4-11个字节"+s.substring(3*2, (3+8)*2)
	    				+ ")："+msg.timestamp;
				ret += "<br>IMSI(第12-19个字节"+s.substring((3+8)*2, (3+8+8)*2)
	    				+ ")："+msg.deviceId;
				ret += "<br>签名(第20-35个字节"+s.substring((3+8+8)*2, s.length())
	    				+ ")："+msg.sign;
			} else if (msg.getType() == SmartMsgUtil.CMD_LOCK_TIME) {
				ret += "消息类型-查询时间<br>";
				ret += "<br>IMSI(第4-11个字节"+s.substring(3*2, s.length())
	    				+ ")："+msg.deviceId;
			}
    	}
    	if (msg.getTag() == 0x21) {
    		ret += "锁回复消息<br>";
    		ret += "消息流水(第1个字节"+s.substring(0, 1*2)
    				+ ")："+msg.getMid()+" <br> 通信类型(第2个字节"+s.substring(1*2, 2*2)
					+ ")："+msg.getTag()+" <br> 状态(第3个字节"+s.substring(2*2, 3*2)
							+ ")："+msg.getType()+"<br>";
    		if (msg.getLen() >= 3 + 2 + 8)// 对应创建用户的应答，回复用户编号
			{
				ret += "<br>用户编码(第4-5个字节"+s.substring(3*2, 2*(3+2))
	    				+ ")："+msg.userCode;
				ret += "<br>IMSI(第6-13个字节"+s.substring((3+2)*2, s.length())
	    				+ ")："+msg.deviceId;
			} else if (msg.getLen() >= 3 + 8) {
				ret += "<br>IMSI(第4-11个字节"+s.substring((3)*2, s.length())
	    				+ ")："+msg.deviceId;
			}
    	}
    	
    	if ( msg.getTag() == 0x11 ){
    		ret += "服务器回复消息<br>";
    		ret += "消息流水(第1个字节"+s.substring(0, 1*2)
    				+ ")："+msg.getMid()+" <br> 通信类型(第2个字节"+s.substring(1*2, 2*2)
					+ ")："+msg.getTag()+" <br> 状态(第3个字节"+s.substring(2*2, 3*2)
							+ ")："+msg.getType()+"<br>";
    		ret += msg.descMsg;
    	}
    	
    	if ( msg.getTag() == 0x20 ){
    		ret += "服务器指令<br>";
    		ret += "消息流水(第1个字节"+s.substring(0, 1*2)
    				+ ")："+msg.getMid()+" <br> 通信类型(第2个字节"+s.substring(1*2, 2*2)
					+ ")："+msg.getTag()+" <br> 消息类型(第3个字节"+s.substring(2*2, 3*2)
							+ ")："+msg.getType()+"<br>";
    		if (msg.getType() == SmartMsgUtil.CMD_SVR_ADDUSER){
    			ret += "添加用户";
    		}
    		if (msg.getType() == SmartMsgUtil.CMD_SVR_ADD_PASS){
    			ret += "添加密码";
    		}
    		if (msg.getType() == SmartMsgUtil.CMD_SVR_ADD_TEMPPASS){
    			ret += "添加临时密码";
    		}
    		if (msg.getType() == SmartMsgUtil.CMD_SVR_DELPASS){
    			ret += "删除用户";
    		}
    		
    		ret += ","+msg.descMsg;
    	}
    	
    	return ret +"\r\n\r\n";
    }
}
