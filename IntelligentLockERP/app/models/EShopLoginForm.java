package models;

import java.io.Serializable;
//EShop 表单
public class EShopLoginForm implements Serializable{
	private static final long serialVersionUID = 3692775313797635967L;
	public String phone;
	public String validate;
	public String sms;
	
	public String validMsg;//表单验证结果
	
}
