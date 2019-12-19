package app.dto;

import java.io.Serializable;

/**
 * @ClassName: ReturnJson
 * @CreateTime 2014-9-28 下午5:23:32
 * @author : liqinchao
 * @Description: 封装通用的返回给App的Json对象
 *
 */
public class ReturnJson implements Serializable{
	/**
	 * 通用应答码，全局：0-成功，非0-失败 0 所有接口 成功 202 token过期，要重新登陆
	 */
	public int code = -100;
	public String message = "查询失败：服务器错误。";
	public Object data = new Object();

	public ReturnJson() {
		super();
	}

	public ReturnJson(int code, String message, Object data) {
		super();
		this.code = code;
		this.message = message;
		this.data = data;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	public void setParamsError(){
		this.code = PARAMS_ERROR;
		this.message = "参数错误";
	}
	
	public void setTokenTimeOut(){
		this.code=TOKEN_TIMEOUT;
		this.message = "token过期，要重新登陆";
	}
	
	public void setSuccess(){
		this.code = SUCCESS;
		this.message = "成功";
	}
	public static int TOKEN_TIMEOUT = 202;
	public static int PARAMS_ERROR = 101;
	public static int SUCCESS = 0;
	/*
	 * 如果code的值为909则表示系统要求客户必须进行语音验证后才能登陆。
	 */
	public static int INT_RET_VOICECheck = 909;
}
