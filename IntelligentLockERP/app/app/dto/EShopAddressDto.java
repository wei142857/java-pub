package app.dto;

import java.io.Serializable;
import java.util.List;

public class EShopAddressDto implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private List<Address> addresses;	//地址列表
	
	public List<Address> getAddresses() {
		return addresses;
	}
	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static class Address implements Serializable{

		private static final long serialVersionUID = 1L;
		
		private String addressid;	//地址ID
		
		private String area;		//4级地区组合的区域
		
		private String address;		//详细地址
		
		private String isdefault;	//是否默认,0:非默认 1:默认
		
		private String phone;		//收件人手机号码
		
		private String name;		//收件人姓名

		public String getAddressid() {
			return addressid;
		}

		public void setAddressid(String addressid) {
			this.addressid = addressid;
		}

		public String getArea() {
			return area;
		}

		public void setArea(String area) {
			this.area = area;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public String getIsdefault() {
			return isdefault;
		}

		public void setIsdefault(String isdefault) {
			this.isdefault = isdefault;
		}

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public static long getSerialversionuid() {
			return serialVersionUID;
		}

		@Override
		public String toString() {
			return "address [addressid=" + addressid + ", area=" + area
					+ ", address=" + address + ", isdefault=" + isdefault
					+ ", phone=" + phone + ", name=" + name + "]";
		}
	}
}
