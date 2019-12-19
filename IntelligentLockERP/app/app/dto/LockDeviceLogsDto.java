package app.dto;

import java.io.Serializable;
import java.util.List;

public class LockDeviceLogsDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Item> items;
	
	public List<Item> getItems() {
		return items;
	}
	public void setItems(List<Item> items) {
		this.items = items;
	}
	public static class Item implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String operationdate;//设备操作时间 2018-01-08
		private List<Record> records;
		
		public String getOperationdate() {
			return operationdate;
		}

		public void setOperationdate(String operationdate) {
			this.operationdate = operationdate;
		}

		public List<Record> getRecords() {
			return records;
		}

		public void setRecords(List<Record> records) {
			this.records = records;
		}


		public static class Record implements Serializable{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			private String operationname;//设备操作行为
			private Integer operationstatus;//设备操作状态 0：OK ，1：非法
			private String operationtime;//设备操作时间  10:18
			public String getOperationname() {
				return operationname;
			}
			public void setOperationname(String operationname) {
				this.operationname = operationname;
			}
			public Integer getOperationstatus() {
				return operationstatus;
			}
			public void setOperationstatus(Integer operationstatus) {
				this.operationstatus = operationstatus;
			}
			public String getOperationtime() {
				return operationtime;
			}
			public void setOperationtime(String operationtime) {
				this.operationtime = operationtime;
			}
			
		}
	}
	
}
