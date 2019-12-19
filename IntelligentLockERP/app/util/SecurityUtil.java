package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.SysFunction;
import util.classEntity.TreeNode;

public class SecurityUtil implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private  Map<String,TreeNode> map = new HashMap<String,TreeNode>();
	private  List<TreeNode> allFuncs = new ArrayList<TreeNode>();
	/*
	 * 授权
	 */
	public void doAuthorization(List<SysFunction> list){
		for(SysFunction sf:list){
			TreeNode tn = new TreeNode(sf.getIdx(),sf.getName(),sf.getUrl(),sf.getParent(),
					sf.getIcon(),sf.getState(),sf.getFcode(),sf.getIsMenu(),false);
			map.put(sf.getFcode(), tn);
			if(sf.getIsMenu()>0 && sf.getParent()>0)
				allFuncs.add( tn );
		}
	}
	public TreeNode hasPermission(String url){
		if(StringUtil.isNull(url))
			return null;
		for(TreeNode tn:map.values()){
			if(!StringUtil.isNull(tn.url) && url.indexOf(tn.url)==0)
				return tn;
		}
		return null;
	}
	
	public boolean hasFunction(String fcode){
		return map.get( fcode ) != null ;
	}
	
	public List<TreeNode> showPermissions(){
		/*List<TreeNode> list = new ArrayList<TreeNode>();
		for(TreeNode tn:map.values()){
			if(tn.isMenu.intValue()==1)
				list.add(tn);
		}*/
		return allFuncs;
	}
	public void clear(){
		map.clear();
	}
}
