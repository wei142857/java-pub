package app.service;

import java.util.List;

import models.SubOrders;
import util.GlobalDBControl;

import com.avaje.ebean.Ebean;

public class SubOrdersService {
	//查询待预约安装的订单
	public static SubOrders findToInstallSubOrder(String phone){
		List<SubOrders> list = Ebean.getServer(GlobalDBControl.getReadDB()).find(SubOrders.class)
				.where().eq("phone", phone).eq("isinstall", 1).findList();
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	//查询已预约安装的订单数量
	public static int findAlreadyInstallSubOrder(String phone){
		return Ebean.getServer(GlobalDBControl.getReadDB()).find(SubOrders.class)
				.where().eq("phone", phone).eq("isinstall", 0).findRowCount();
		
	}
	
	//查询待预约测量的订单
	public static SubOrders findToMeasureSubOrder(String phone){
		List<SubOrders> list = Ebean.getServer(GlobalDBControl.getReadDB()).find(SubOrders.class)
				.where().eq("phone", phone).eq("ismeasure", 1).findList();
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	//查询已预约安装的订单数量
	public static int findAlreadyMeasureSubOrder(String phone){
		return Ebean.getServer(GlobalDBControl.getReadDB()).find(SubOrders.class)
				.where().eq("phone", phone).eq("ismeasure", 0).findRowCount();
		
	}
	
}
