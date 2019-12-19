package models;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@SuppressWarnings("all")
public class ValidatePolicy implements Serializable{
	private static final long serialVersionUID = 795463991378821989L;

	private String product_id;//产品ID
	private String amount;//保额
	
	/**出单接口特有属性**/
	private String trade_no;//支付流水号
	/**出单接口特有属性**/
//	private String signpksubId;//签约号
	
	private String premium;//保费
	private String insure_date;//投保时间
	private String start_date;//起保时间
	private String end_date;//终保时间
	
	public static class KindList{
		private String kind_code;//险别代码
		private String amount;//险别保额
		private String premium;//险别保费
		public String getKind_code() {
			return kind_code;
		}
		public void setKind_code(String kind_code) {
			this.kind_code = kind_code;
		}
		public String getAmount() {
			return amount;
		}
		public void setAmount(String amount) {
			this.amount = amount;
		}
		public String getPremium() {
			return premium;
		}
		public void setPremium(String premium) {
			this.premium = premium;
		}
		@Override
		public String toString() {
			return "KindList [kind_code=" + kind_code + ", amount=" + amount + ", premium=" + premium + "]";
		}
		
	}
//	private List<KindList> kind_list;//险别列表
	
	private Map<String,String> applicant;//投保人
	private Map<String,String> insured;//被保人
	public static class InsuranceSubjectList{
		private String province_code;//省编码
		private String province_name;//省名称
		private String city_code;//市编码
		private String city_name;//市编码
		private String district_code;//区编码
		private String district_name;//区名称
		private String post_code;//邮编
		private String address;//详细地址
		public String getProvince_code() {
			return province_code;
		}
		public void setProvince_code(String province_code) {
			this.province_code = province_code;
		}
		public String getProvince_name() {
			return province_name;
		}
		public void setProvince_name(String province_name) {
			this.province_name = province_name;
		}
		public String getCity_code() {
			return city_code;
		}
		public void setCity_code(String city_code) {
			this.city_code = city_code;
		}
		public String getCity_name() {
			return city_name;
		}
		public void setCity_name(String city_name) {
			this.city_name = city_name;
		}
		public String getDistrict_code() {
			return district_code;
		}
		public void setDistrict_code(String district_code) {
			this.district_code = district_code;
		}
		public String getDistrict_name() {
			return district_name;
		}
		public void setDistrict_name(String district_name) {
			this.district_name = district_name;
		}
		public String getPost_code() {
			return post_code;
		}
		public void setPost_code(String post_code) {
			this.post_code = post_code;
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		@Override
		public String toString() {
			return "InsuranceSubjectList [province_code=" + province_code + ", province_name=" + province_name
					+ ", city_code=" + city_code + ", city_name=" + city_name + ", district_code=" + district_code
					+ ", district_name=" + district_name + ", post_code=" + post_code + ", address=" + address + "]";
		}
//		@Override
//		public String toString() {
//			return "InsuranceSubjectList [province_code=" + province_code + ", city_code=" + city_code
//					+ ", district_code=" + district_code + ", post_code=" + post_code + ", address=" + address + "]";
//		}
		
	}
	private List<Object> insurance_subject_list;//标的列表
	
	private Map<String,String> extend_params;//扩展参数信息
//	private String paymentPlan;//是否分期
//	private String annualPayment;//是否年缴
//	private String payPlanByChannel;//是否渠道传入分期计划	
	
//	public static class SPaymentPlanInfoList{
//		private String totalTimes;//分期数
//		
//		public static class SPaymentPlanDetailList{
//			private String payTimes;//期次
//			private String shouldPremium;//保费
//			public String getPayTimes() {
//				return payTimes;
//			}
//			public void setPayTimes(String payTimes) {
//				this.payTimes = payTimes;
//			}
//			public String getShouldPremium() {
//				return shouldPremium;
//			}
//			public void setShouldPremium(String shouldPremium) {
//				this.shouldPremium = shouldPremium;
//			}
//			@Override
//			public String toString() {
//				return "SPaymentPlanDetailList [payTimes=" + payTimes + ", shouldPremium=" + shouldPremium + "]";
//			}
//			
//		}
//		private List<SPaymentPlanDetailList> sPaymentPlanDetailList;//分期详情计划
//		public String getTotalTimes() {
//			return totalTimes;
//		}
//		public void setTotalTimes(String totalTimes) {
//			this.totalTimes = totalTimes;
//		}
//		public List<SPaymentPlanDetailList> getsPaymentPlanDetailList() {
//			return sPaymentPlanDetailList;
//		}
//		public void setsPaymentPlanDetailList(List<SPaymentPlanDetailList> sPaymentPlanDetailList) {
//			this.sPaymentPlanDetailList = sPaymentPlanDetailList;
//		}
//		@Override
//		public String toString() {
//			return "SPaymentPlanInfoList [totalTimes=" + totalTimes + ", sPaymentPlanDetailList="
//					+ sPaymentPlanDetailList + "]";
//		}
//		
//	}
//	private List<SPaymentPlanInfoList> sPaymentPlanInfoList;//分期计划
	public String getProduct_id() {
		return product_id;
	}
	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getPremium() {
		return premium;
	}
	public void setPremium(String premium) {
		this.premium = premium;
	}
	public String getInsure_date() {
		return insure_date;
	}
	public void setInsure_date(String insure_date) {
		this.insure_date = insure_date;
	}
	public String getStart_date() {
		return start_date;
	}
	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}
	public String getEnd_date() {
		return end_date;
	}
	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}
//	public List<KindList> getKind_list() {
//		return kind_list;
//	}
//	public void setKind_list(List<KindList> kind_list) {
//		this.kind_list = kind_list;
//	}
	public Map<String, String> getApplicant() {
		return applicant;
	}
	public void setApplicant(Map<String, String> applicant) {
		this.applicant = applicant;
	}
	public Map<String, String> getInsured() {
		return insured;
	}
	public void setInsured(Map<String, String> insured) {
		this.insured = insured;
	}
	public List<Object> getInsurance_subject_list() {
		return insurance_subject_list;
	}
	public void setInsurance_subject_list(List<Object> insurance_subject_list) {
		this.insurance_subject_list = insurance_subject_list;
	}
	public Map<String, String> getExtend_params() {
		return extend_params;
	}
	public void setExtend_params(Map<String, String> extend_params) {
		this.extend_params = extend_params;
	}
//	public String getPaymentPlan() {
//		return paymentPlan;
//	}
//	public void setPaymentPlan(String paymentPlan) {
//		this.paymentPlan = paymentPlan;
//	}
//	public String getAnnualPayment() {
//		return annualPayment;
//	}
//	public void setAnnualPayment(String annualPayment) {
//		this.annualPayment = annualPayment;
//	}
//	public String getPayPlanByChannel() {
//		return payPlanByChannel;
//	}
//	public void setPayPlanByChannel(String payPlanByChannel) {
//		this.payPlanByChannel = payPlanByChannel;
//	}
	public String getTrade_no() {
		return trade_no;
	}
	public void setTrade_no(String trade_no) {
		this.trade_no = trade_no;
	}
//	public String getSignpksubId() {
//		return signpksubId;
//	}
//	public void setSignpksubId(String signpksubId) {
//		this.signpksubId = signpksubId;
//	}
//	public List<SPaymentPlanInfoList> getsPaymentPlanInfoList() {
//		return sPaymentPlanInfoList;
//	}
//	public void setsPaymentPlanInfoList(List<SPaymentPlanInfoList> sPaymentPlanInfoList) {
//		this.sPaymentPlanInfoList = sPaymentPlanInfoList;
//	}
//	@Override
//	public String toString() {
//		return "ValidatePolicy [product_id=" + product_id + ", amount=" + amount + ", premium=" + premium
//				+ ", insure_date=" + insure_date + ", start_date=" + start_date + ", end_date=" + end_date
//				+ ", kind_list=" + kind_list + ", applicant=" + applicant + ", insured=" + insured
//				+ ", insurance_subject_list=" + insurance_subject_list + ", extend_params=" + extend_params
//				+ ", paymentPlan=" + paymentPlan + ", annualPayment=" + annualPayment + ", payPlanByChannel="
//				+ payPlanByChannel + ", sPaymentPlanInfoList=" + sPaymentPlanInfoList + "]";
//	}
	
}