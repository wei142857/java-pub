package net;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import util.classEntity.StringTool;
import models.SmartDevice;
import models.SmartNetMsg;

public class SmartMsgUtil {
	/*********** 字典表 关于锁端信息的设置 ****************/
	public static String DICT_USR_TYPE = "lock_user_type"; // 锁端用户类型
	public static String DICT_ALARM_TYPE = "lock_alarm_type"; // 锁报警类型
	public static String DICT_TIP_TYPE = "lock_tip_type"; // 锁提醒类型

	/********** 锁端 发送的信息 ************************/
	public static int CMD_LOCK_CONNECT = 0x70; // 首次上报

	public static int CMD_LOCK_OPEN = 1; // 开锁通知
	public static int CMD_LOCK_LOCK = 2; // 关锁通知
	public static int CMD_LOCK_ADDUSER = 3; // 加用户通知
	public static int CMD_LOCK_DELUSER = 4; // 删用户通知

	public static int CMD_LOCK_REMOTE_OPEN = 6; // 远程开锁
	public static int CMD_LOCK_ALARM = 7; // 报警
	public static int CMD_LOCK_TIP = 8; // 提醒
	public static int CMD_LOCK_POWER = 9; // 电量通知

	public static int CMD_LOCK_RESET = 0x0A; // 重置
	public static int CMD_LOCK_TIME = 0x0B; // 问时间

	static HashMap<Integer, String> LockCmds = new HashMap<Integer, String>() {
		{
			put(1, "开锁通知");
			put(2, "关锁通知");
			put(3, "锁端添加用户");
			put(4, "锁端删除用户");
			put(6, "远程请求开锁");
			put(7, "报警");
			put(8, "提醒");
			put(9, "电量通知");
			put(0x0A, "锁重置清空数据");
			put(0x0B, "查时间");
			put(0x70, "锁连接APP");
			// put(0x10,"问时间");
		}
	};

	/********** 服务端 发送的信息 ************************/
	public static int CMD_SVR_ADDUSER = 1; // 添加用户
	public static int CMD_SVR_ADD_PASS = 2; // 添加密码
	public static int CMD_SVR_ADD_TEMPPASS = 3; // 添加密码带有效期
	public static int CMD_SVR_DELPASS = 4; // 删除密码
	public static int CMD_SVR_CONNECT_REP = 0x71; // 返回密钥

	static HashMap<Integer, String> ServerCmds = new HashMap<Integer, String>() {
		{
			put(1, "APP端加用户");
			put(2, "APP端添加密码");
			put(3, "APP端添加临时密码");
			put(4, "APP端删除用户");
			put(0x71, "回复密钥");
		}
	};

	/*
	 * 数据说明 固定包头(0xA9) 包体长度 传输 ID 通信标识 命令 数据内容 签名 数据长度 1Byte 1Byte 1Byte 1Byte
	 * 1Byte 可变字节 16Byte
	 */
	// 从消息体构建一个 SmartNetMsg
	public static SmartNetMsg makeFromBytes(byte[] data, int len) {
		if (len < 3) {
			util.LogUtil.writeLog("read msg error: length:" + len,
					"LockSocketThread");
			return null;
		}

		SmartNetMsg msg = new SmartNetMsg();
		msg.setDealed(0);
		msg.setDt(new Date());
		msg.setLen(len);

		msg.setMid((int) data[0]); // 传输 ID
		msg.setTag((int) data[1]); // 通信标识
		msg.setType((int) data[2]); // 命令

		util.LogUtil.writeLog("Got msg : length - " + len + " ,传输 ID- "
				+ data[0] + " ,类型- " + data[1] + ",命令-" + data[2] + " , "
				+ StringTool.makeHexString(data, len), "LockSocketThread");

		msg.setMsg(StringTool.makeHexString(data, len)); // 内容体
		if (msg.getTag() == 0x10)
			msg.setExt(LockCmds.get(msg.getType())); // 消息 文字说明
		else
			msg.setExt("下发回复");

		msg.data = data;

		/************** 扩展字段 *****************************/
		// 来自锁的消息
		if (msg.getTag() == 0x10) {
			// 连接
			if (msg.getType() == CMD_LOCK_CONNECT) {
				if (len < 3 + 1 + 2 + 8) {
					util.LogUtil.writeLog("read connect msg error: length:"
							+ len, "LockSocketThread");
					return null;
				}

				// 上报的信息. 终端类型1byte , 终端型号2byte, 终端 ID(BCD编码)
				msg.deviceType = (int) data[3];
				msg.deviceTypeCode = msg.getMsg().substring((3 + 1) * 2,
						(3 + 1 + 2) * 2);
				msg.deviceId = msg.getMsg().substring((3 + 1+2) * 2, (3 + 1+2 + 8) * 2 );
				
				try{
					msg.verison =  msg.getMsg().substring((3 + 1+2 + 8) * 2 ,  len * 2 );
					msg.verison = new String(StringTool.getFromHexString(msg.verison));
					play.Logger.info( msg.deviceId+"\t"+msg.verison );	
					util.LogUtil.writeLog( msg.deviceId+"\t"+msg.verison , "lockVersion");
				}catch(Exception ee){
					
				}
			}

			// 关联用户状态信息上报
			else if (msg.getType() >= CMD_LOCK_OPEN
					&& msg.getType() <= CMD_LOCK_DELUSER) {
				if (len < 3 + 1 + 2 + 8) {
					util.LogUtil.writeLog("read connect msg error: length:"
							+ len, "LockSocketThread");
					return null;
				}
				// 上报的信息. 类型1字节，用户编号2字节， 终端 ID 8byte
				msg.info = (int) data[3];
				msg.userCode = msg.getMsg().substring((3 + 1) * 2,
						(3 + 1 + 2) * 2);
				;
				msg.deviceId = msg.getMsg().substring((3 + 3) * 2, len * 2);
			}

			// 非关联用户状态信息上报
			else if (msg.getType() >= CMD_LOCK_REMOTE_OPEN
					&& msg.getType() <= CMD_LOCK_POWER) {
				// 上报的信息. 内容 1字节，终端 ID
				if (len < 3 + 1 + 8) {
					util.LogUtil.writeLog("read remote open msg error: length:"
							+ len, "LockSocketThread");
					return null;
				}
				msg.info = (int) data[3];
				msg.deviceId = msg.getMsg().substring((3 + 1) * 2, (3 + 1 + 8) * 2 );
			}

			// reset
			else if (msg.getType() == CMD_LOCK_RESET) {
				if (len < 3 + 8 + 8 + 16) {
					util.LogUtil.writeLog(
							"read reset msg error: length:" + len,
							"LockSocketThread");
					return null;
				}

				// Reset. 时间戳8Byte，终端 ID 8byte，签名
				msg.timestamp = msg.getMsg().substring(3 * 2, (3 + 8) * 2);
				msg.deviceId = msg.getMsg().substring((3 + 8) * 2,
						(3 + 8 + 8) * 2);
				msg.sign = msg.getMsg().substring((3 + 8 + 8) * 2, len * 2);
			} else if (msg.getType() == CMD_LOCK_TIME) {
				if (len < 3 + 8) {
					util.LogUtil.writeLog(
							"read reset msg error: length:" + len,
							"LockSocketThread");
					return null;
				}

				// Reset. 时间戳8Byte，终端 ID 8byte，签名
				msg.deviceId = msg.getMsg().substring(3 * 2, len * 2);
			}
		}

		// 锁端的回复
		if (msg.getTag() == 0x21) {
			if (len >= 3 + 2 + 8)// 对应创建用户的应答，回复用户编号
			{
				msg.userCode = msg.getMsg().substring(3 * 2, (3 + 2) * 2);
				msg.deviceId = msg.getMsg().substring((3 + 2) * 2,
						msg.getMsg().length());
			} else if (len >= 3 + 8) {
				msg.deviceId = msg.getMsg().substring(3 * 2,
						msg.getMsg().length());
			}

			play.Logger.info("!!! -- " + msg.userCode);
		}
		/************** 扩展信息 End. *****************************/
		if (!StringTool.isNull(msg.deviceId))
			msg.setLockid(msg.deviceId);

		return msg;
	}

	/************************ 对上行的回复 ************************/
	// 服务器端接到普通指令的回应
	public static SmartNetMsg makeCommonReply(SmartNetMsg m, boolean ok) {
		SmartNetMsg msg = new SmartNetMsg();
		msg.setDealed(0);
		msg.setDt(new Date());

		msg.setMid(m.getMid()); // 传输 ID
		msg.setTag(0x11); // 通信标识,上报回复

		if (ok)
			msg.setType(0); // 成功
		else
			msg.setType(1); // 失败

		// 消息内容的字节
		byte[] ret = new byte[2 + 3];
		ret[0] = (byte) 0xA9;
		ret[1] = (byte) 3; // 去掉包头后的 长度

		ret[2] = (byte) (m.getMid().intValue()); // 传输ID
		ret[3] = (byte) 0x11; // 上报应答
		ret[4] = (byte) msg.getType().intValue();

		msg.data = ret;
		msg.setLen(3);
		msg.setMsg(StringTool.makeHexString(ret, ret.length));
		msg.setExt("普通回复消息");

		// 消息 文字说明
		if (ok)
			msg.setExt("消息" + m.getMid().intValue() + "回复成功");
		else
			msg.setExt("消息" + m.getMid().intValue() + "回复失败");
		return msg;
	}

	public static SmartNetMsg makeConnectReply(SmartNetMsg m, SmartDevice device) {
		SmartNetMsg msg = new SmartNetMsg();
		msg.setDealed(0);
		msg.setDt(new Date());

		msg.setMid(m.getMid()); // 传输 ID
		msg.setTag(0x11); // 通信标识,上报回复
		msg.setType(0x71); // 回复
		msg.setLen(8);
		msg.setLockid(device.getDeviceid());
		msg.setExt("回复密钥");

		// 消息内容的字节
		msg.setMsg("A9" + "08"
				+ StringTool.makeHexString((byte) (m.getMid().intValue())) // MID
				+ "11" // 传输标识
				+ "71" // 命令
				+ device.getSecret());
		
		msg.descMsg = "设备密钥：" + device.getSecret();

		return msg;
	}

	public static SmartNetMsg makeTimeReply(SmartNetMsg m, boolean b) {
		SmartNetMsg msg = new SmartNetMsg();
		msg.setDealed(0);
		msg.setDt(new Date());

		msg.setMid(m.getMid()); // 传输 ID
		msg.setTag(0x11); // 通信标识,上报回复
		msg.setType(0x11); // 回复
		msg.setLen(2 + 8);
		msg.setLockid(m.deviceId);
		msg.setExt("回复时间");

		String timeSt = longToHexs(System.currentTimeMillis() / 1000);
		// 消息内容的字节
		msg.setMsg("A9" + "0A"
				+ StringTool.makeHexString((byte) (m.getMid().intValue())) // MID
				+ "11" // tag
				+ timeSt);
		
		msg.descMsg = "时间戳：" + timeSt;

		return msg;
	}

	public static SmartNetMsg makeRemoteOpenReply(SmartNetMsg m,
			SmartDevice device, boolean ok) {
		SmartNetMsg msg = new SmartNetMsg();
		msg.setDealed(0);
		msg.setDt(new Date());

		msg.setMid(m.getMid()); // 传输 ID
		msg.setTag(0x11); // 通信标识,上报回复
		if (ok)
			msg.setType(0); // 远程开锁回复
		else
			msg.setType(1); // 远程开锁回复

		msg.setLen(3 + 8 + 16); // 时间戳+签名
		msg.setLockid(device.getDeviceid());
		if (ok)
			msg.setExt("同意远程开锁");
		else
			msg.setExt("拒绝远程开锁");

		// 消息内容的字节
		String data = "A9"
				+ StringTool.makeHexString((byte) msg.getLen().intValue())
				+ StringTool.makeHexString((byte) (m.getMid().intValue())) // MID
				+ "11" // 传输标识
				+ "0" + msg.getType() // 命令
				+ longToHexs(System.currentTimeMillis());

		data = data.toUpperCase();

		util.LogUtil.writeLog(data + "\t" + device.getSecret() + "\t"
				+ util.MD5.mkMd5((data + device.getSecret())), "md5");
		msg.setMsg(data + util.MD5.mkMd5(data + device.getSecret())); // 签名

		return msg;
	}

	/************************ 做下发消息 ************************/
	// 服务器端发起加用户 ： 0x00:默认用户,0x01:指纹用户,0x03:IC卡用户 , 0x04:钥匙用户 ,0x05:手机用户
	// ,0x06:人脸用户 ,0x07:掌纹用户 ,0x08:虹膜用户
	public static SmartNetMsg makeAddUserMsg(SmartDevice device, int utype) {
		SmartNetMsg msg = new SmartNetMsg();
		msg.setDealed(0);
		msg.setDt(new Date());

		msg.setMid(mkMid(device.getDeviceid())); // 传输 ID
		msg.setTag(0x20); // 通信标识,下发
		msg.setType(CMD_SVR_ADDUSER); // 添加用户
		msg.setLen(3 + 1 + 16); // TID 通信标识 命令 ，类型1byte， 签名16byte
		msg.setLockid(device.getDeviceid());
		msg.setExt2("" + utype);
		msg.setExt("服务器-添加用户");

		// 消息内容的字节
		String data = "A9"
				+ StringTool.makeHexString((byte) msg.getLen().intValue())
				+ StringTool.makeHexString((byte) (msg.getMid().intValue())) // MID
				+ "20" // 传输标识
				+ "0" + CMD_SVR_ADDUSER // 命令
				+ "0" + utype;
		data = data.toUpperCase();
		msg.setMsg(data + util.MD5.mkMd5(data + device.getSecret())); // 签名
		
		msg.descMsg = "用户类型" + utype ;
		return msg;
	}

	// 服务器端发起加密码
	public static SmartNetMsg makeAddPassMsg(SmartDevice device, String pass) {
		if (device == null)
			return null;
		SmartNetMsg msg = new SmartNetMsg();
		msg.setDealed(0);
		msg.setDt(new Date());

		msg.setMid(mkMid(device.getDeviceid())); // 传输 ID
		msg.setTag(0x20); // 通信标识,下发
		msg.setType(CMD_SVR_ADD_PASS); // 添加用户
		msg.setLen(3 + 3 + 16); // TID 通信标识 命令， 内容（密码3Byte） 签名16
		msg.setLockid(device.getDeviceid());
		msg.setExt("服务器-设置密码");
		msg.setExt1(pass);

		// 消息内容的字节
		String data = "A9"
				+ StringTool.makeHexString((byte) msg.getLen().intValue())
				+ StringTool.makeHexString((byte) (msg.getMid().intValue())) // MID
				+ "20" // 传输标识
				+ "0" + CMD_SVR_ADD_PASS // 命令
				+ pass;
		data = data.toUpperCase();
		msg.setMsg(data + util.MD5.mkMd5(data + device.getSecret())); // 签名
		
		msg.descMsg = "密码：" + pass ;
		return msg;
	}

	// 服务器端发起加有效期密码
	public static SmartNetMsg makeAddPassMsg2(SmartDevice device, String pass,
			long time,Date beginTime) {
		SmartNetMsg msg = new SmartNetMsg();
		msg.setDealed(0);
		msg.setDt(new Date());

		msg.setMid(mkMid(device.getDeviceid())); // 传输 ID
		msg.setTag(0x20); // 通信标识,下发
		msg.setType(CMD_SVR_ADD_TEMPPASS); // 添加用户
		msg.setLen(3 + 3 + 8 + 16); // TID 通信标识 命令， 密码3Byte ，开始生效的秒数4bytes，
									// 签名16bytes
		msg.setLockid(device.getDeviceid());
		msg.setExt("服务器-设置临时密码");
		msg.setBegintime(beginTime);

		msg.setExt1(pass);
		msg.setExt2("" + time);

		// 消息内容的字节
		String timeSt = longToHexs(time);
		String data = "A9"
				+ StringTool.makeHexString((byte) msg.getLen().intValue())
				+ StringTool.makeHexString((byte) (msg.getMid().intValue())) // MID
				+ "20" // 传输标识
				+ "0" + CMD_SVR_ADD_TEMPPASS // 命令
				+ pass + timeSt;
		data = data.toUpperCase();
		msg.setMsg(data + util.MD5.mkMd5(data + device.getSecret())); // 签名
		
		msg.descMsg = "密码：" + pass +",时间有效期："+timeSt ;
		return msg;
	}

	// 服务器端发起删除用户
	public static SmartNetMsg makeDelPass(SmartDevice device, String ucode,
			int type) {
		SmartNetMsg msg = new SmartNetMsg();
		msg.setDealed(0);
		msg.setDt(new Date());

		msg.setMid(mkMid(device.getDeviceid())); // 传输 ID
		msg.setTag(0x20); // 通信标识,下发
		msg.setType(CMD_SVR_DELPASS); // 添加用户
		msg.setLen(3 + 1 + 2 + 16); // TID 通信标识 命令，密码类型1，用户编号2 ，签名16
		msg.setLockid(device.getDeviceid());
		msg.setExt("服务器-删除用户");
		msg.setExt1(ucode);
		msg.setExt2("" + type);

		// 消息内容的字节
		String data = "A9"
				+ StringTool.makeHexString((byte) msg.getLen().intValue())
				+ StringTool.makeHexString((byte) (msg.getMid().intValue())) // MID
				+ "20" // 传输标识
				+ "0" + CMD_SVR_DELPASS // 命令
				+ StringTool.makeHexString((byte) type) + ucode;
		data = data.toUpperCase();
		msg.setMsg(data + util.MD5.mkMd5(data + device.getSecret())); // 签名
		
		msg.descMsg = "用户类型：" + type +",用户编码："+ucode ;
		return msg;
	}

	// int =》 hex string
	public static String intToHexs(int sec) {
		byte[] byteArray = StringTool.intToBytes(sec);
		return StringTool.makeHexString(byteArray);
	}

	public static String longToHexs(long sec) {
		byte[] byteArray = StringTool.longToBytes(sec);
		return StringTool.makeHexString(byteArray);
	}

	static int mkMid(String devid) {
		Integer msgId = util.jedis.RedisUtil.getInstance().getEntity(
				"mid:" + devid, Integer.class);
		if (msgId == null)
			msgId = 0;
		msgId = msgId + 1;
		if (msgId >= 127)
			msgId = 0;
		util.jedis.RedisUtil.getInstance().setEntity("mid:" + devid, msgId);
		return msgId;
	}

	/*************** RC4加密消息处理 *****************/
	

}
