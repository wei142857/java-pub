package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;


public class FunctionNode {
	public String id;
	public String pid;
	public String text;
	public String url;
	public boolean expanded = false;
	
	public FunctionNode( String id,String pid,String text,String url,boolean exp )
	{
		this.id= id;
		this.pid= pid;
		this.text= text;
		this.url= url;
		expanded = exp;
	}
}
