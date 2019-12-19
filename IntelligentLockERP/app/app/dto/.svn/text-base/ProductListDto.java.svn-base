package app.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 为接口:getProductList返回数据
 * 查看话费列表(返回的是话费的记录)
 * @author 陈宏亮
 *
 */
public class ProductListDto implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private List<Item> items;
	
	public List<Item> getItems() {
		return items;
	}
	public void setItems(List<Item> items) {
		this.items = items;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public static class Item implements Serializable{

		private static final long serialVersionUID = 1L;
		
		private String price;	//价格
		private String bvalue;	//奖励金
		private String pid;		//商品ID
		public String getPrice() {
			return price;
		}
		public void setPrice(String price) {
			this.price = price;
		}
		public String getBvalue() {
			return bvalue;
		}
		public void setBvalue(String bvalue) {
			this.bvalue = bvalue;
		}
		public String getPid() {
			return pid;
		}
		public void setPid(String pid) {
			this.pid = pid;
		}
		public static long getSerialversionuid() {
			return serialVersionUID;
		}
	}
}
