package worker;

public class LogOne implements java.io.Serializable{
	private static final long serialVersionUID = -249705059641074296L;
	public String content,log;
	
	public LogOne(String content,String log)
	{
		this.content = content;
		this.log = log;
	}
}
