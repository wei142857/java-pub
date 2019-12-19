package app.service;

import java.util.Date;

import util.GlobalDBControl;

import com.avaje.ebean.Ebean;

import models.SubMsg;

public class SubMsgService {
	public static int ACT_INSTALL = 1;
	public static int ACT_MEASURE = 2;
	public static void addMsg(String phone,String content,int act,int oid){
		SubMsg subMsg = new SubMsg();
		subMsg.setAct(act);
		subMsg.setAddtime(new Date());
		subMsg.setContent(content);
		subMsg.setOid(oid);
		subMsg.setPhone(phone);
		Ebean.getServer(GlobalDBControl.getDB()).save(subMsg);
	}
}
