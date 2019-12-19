package Service.unicom.dto;

public class IFResponse {
	
	public String orderId;
	public String status="";
	public String code;
	public String msg;
	public String returninfo="";
	
	//解析的结果
	public boolean success;
	public int state = 0;  // 1-充值成功，2-充值受理
	public String means="";
	public String rmeans="";
	
	
	
	public static IFResponse mkSuccessResp()
	{
		IFResponse ifsp= new IFResponse();
		ifsp.success=true;
		ifsp.orderId = "0000";
		ifsp.state = 1;
		ifsp.returninfo = "充值成功测试";
		return ifsp;
	}
	public static IFResponse mkErrorResp()
	{
		IFResponse ifsp= new IFResponse();
		ifsp.success=false;
		ifsp.returninfo = "充值失败测试";
		ifsp.rmeans = "充值失败测试";
		ifsp.means="系统繁忙";
		return ifsp;
	}
	
	public static IFResponse unFindStrategyResp()
	{
		IFResponse ifsp= new IFResponse();
		ifsp.success=false;
		ifsp.orderId = "";
		ifsp.returninfo = "接口未找到";
		ifsp.rmeans = "接口未找到";
		ifsp.means="系统繁忙";
		return ifsp;
	}
	@Override
	public String toString() {
		return "IFResponse [orderId=" + orderId + ", status=" + status
				+ ", code=" + code + ", msg=" + msg + ", returninfo="
				+ returninfo + ", success=" + success + ", state=" + state
				+ ", means=" + means + ", rmeans=" + rmeans + "]";
	}
	
}
