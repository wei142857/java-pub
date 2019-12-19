package util.classEntity;

public class TreeNode implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	public Integer id;
	public Integer pid;
	public String text;
	public String url;
	public String icon;
	public Integer state;
	public String fcode;
	public Integer isMenu;
	public boolean expanded=true;
	/*
	 * 授予角色时候 ， 角色树构造方法
	 */
	public TreeNode(Integer id,String txt,Integer pid,Integer state,boolean expanded){
		this.id  = id;
		this.text = txt;
		this.pid = pid;
		this.state = state;
		this.expanded = expanded;
	}
	/*
	 * 授权的时候，权限树构造方法
	 */
	public TreeNode(Integer id,String txt,String url,Integer pid,String icon,Integer state,String fcode,Integer isMenu,boolean expanded)
	{
		this.id=id;
		this.pid=pid;
		this.text=txt;
		this.url=url;
		this.icon = icon;
		this.state = state;
		this.fcode = fcode;
		this.isMenu = isMenu;
		this.expanded = expanded;
	}
	
	public static String findParentCode(String code)
	{
		if(code==null)
			return "";
		int idx = code.lastIndexOf(".");
		if(idx==-1)
			return "";
		return code.substring(0, idx);
	}
}
