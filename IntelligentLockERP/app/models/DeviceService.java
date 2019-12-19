package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *	设备服务Model
 */
public class DeviceService implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public List<DeviceServiceItems> items;
	
	public DeviceService() {
		items = new ArrayList<DeviceServiceItems>();
	}
	
	public DeviceService(List<DeviceServiceItems> items) {
		if(items==null) return;
		this.items=items;
	}
	
	public List<DeviceServiceItems> getItems() {
		return items;
	}

	public void setItems(List<DeviceServiceItems> items) {
		if(items==null) return;
		this.items = items;
	}

	public void addItem(DeviceServiceItems items) {
		if(items==null) return;
		this.items.add(items);
	}
	
	@Override
	public String toString() {
		return "DeviceService [items=" + items + "]";
	}
	
}
