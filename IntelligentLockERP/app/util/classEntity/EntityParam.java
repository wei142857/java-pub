package util.classEntity;

public class EntityParam
{
	public String name="";
	public String typeString="";
	public String ext="";
	public int    paramIdx;
	
	public String enumType="";
	
	public String getChnName()
	{
		if(!StringTool.isNull(ext))
			return ext;
		return name;
	}
	
	public EntityParam cloneNew()
	{
		EntityParam ep=new EntityParam();
		ep.name = this.name;
		ep.typeString = this.typeString;
		ep.enumType = this.enumType;
		ep.ext = this.ext;
		
		return ep;
	}
	
	public String getEditFieldType()
	{
		if( typeString.equalsIgnoreCase ("Date") )
		   return "dateField";
		
		return "textField";
	}
	
	public boolean isTxtParam()
	{
		return typeString.equalsIgnoreCase ("String");
	}
	
	public String toString()
	{
		return "["+name +":" + typeString+":"+ext+"]";
	}
	
	public String upperHead()
	{
		return name.substring(0, 1).toUpperCase()+name.substring(1);
	}
}
