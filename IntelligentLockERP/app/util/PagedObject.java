package util;

import java.util.List;

import com.avaje.ebean.Page;

/*
 * 用于operamasksUI-grid
 * 获取后端分页数据的json对象
 */

public class PagedObject <t> {
	public static final long serialVersionUID = 1L;
	public List<t> rows;
	public int total;
	public int records;//页数

	public PagedObject(Page<t> p) {
		records = p.getTotalPageCount();
		total = p.getTotalRowCount();
		rows = p.getList();
	}
	
	public PagedObject(List<t> p) {
		records = 1;
		total = p.size();
		rows = p;
	}
	
	public PagedObject(List<t> p,int recod,int totals) {
		records = recod;
		total = totals;
		rows = p;
	}
}