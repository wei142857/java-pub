package util.classEntity;

import java.io.Serializable;

public class GeneralTree implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Integer id;
	public Integer pid;
	public String text;
	public String url;
	public Object obj;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getPid() {
		return pid;
	}
	public void setPid(Integer pid) {
		this.pid = pid;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Object getObj() {
		return obj;
	}
	public void setObj(Object obj) {
		this.obj = obj;
	}
	
}
