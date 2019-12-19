package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "wconf.function_info")
public class FunctionItem {
	@Id
	@Basic(optional=false)
	@Column(name = "ID")
	public Integer ID;
	@Column(name="Funname")
	public String Funname;
	@Column(name="Notes")
	public String Notes;
	@Column(name="url")
	public String url;
	@Column(name="tim")
	public Date time;
	@Column(name="picurl")
	public String picurl;
	@Column(name="menuid")
	public String menuid;
	@Column(name="typ")
	public Integer Flag;//1左侧菜单，0功能,2功能+左侧菜单,-1功能不显示项，
	@Column(name="funccode")
	public String Funccode;//功能编码,以功能模块开头命名，即操作类名称，如：UserInfo_search,UserInfo_add....
	@Transient
	public String checked="";
	@Transient
	public List<FunctionItem> childlist;
	
	//@Transient
	public class PageFunction {
			public List<FunctionItem> rows;
			public int total;
			public int records;
}
}
