package Service.cache;

import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import play.Configuration;
import play.Logger;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlUpdate;

public class EmailService{
	static EmailService instance ;
	
	JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
	
	//获取配置文件，初始化sender；
	public static EmailService getInstance()
	{
		if( instance == null )
			instance = new EmailService();
		
		instance.mailSender.setDefaultEncoding( "GBK" );
		instance.mailSender.setHost( Configuration.root().getString("email.host"));
		instance.mailSender.setUsername( Configuration.root().getString("email.user"));
		instance.mailSender.setPassword( Configuration.root().getString("email.password"));
		
		Properties prop1 = new Properties() ;     
		prop1.put( "mail.smtp.auth", "true" ) ;// 将这个参数设为true，让服务器进行认证,认证用户名和密码是否正确  
		instance.mailSender.setJavaMailProperties( prop1 ) ; 
		
		return instance;
	}
	
	public boolean sendMsg(int uid , int aid ,String from, String to, String content, HashMap<String,Object> prop) {
		// 使用JavaMail的MimeMessage，支付更加复杂的邮件格式和内容
		MimeMessage msg = mailSender.createMimeMessage();
		// 创建MimeMessageHelper对象，处理MimeMessage的辅助类
		try {
			MimeMessageHelper helper = new MimeMessageHelper(msg, true);
			// 使用辅助类MimeMessage设定参数
			helper.setFrom( mailSender.getUsername() );
			helper.setTo(to);
			Logger.info( "recive:"+to);
			helper.setSubject( prop.get("subject").toString() );
			//helper.setText(  content, prop.get("html").toString() );
			helper.setText(prop.get("html").toString(),true );
			
			// 发送邮件
			mailSender.send(msg);
			String sql = "INSERT into wconf.cjms_user_email_log (userid,aid,recaddress,topic,body,sendtime) VALUES(:userid,:aid,:recaddress,:topic,:body,:sendtime)";
			SqlUpdate update = Ebean.createSqlUpdate(sql)
			.setParameter("userid", uid)
			.setParameter("aid", aid)
			.setParameter("recaddress", to)
			.setParameter("topic", prop.get("subject").toString())
			.setParameter("body", prop.get("html").toString())
			.setParameter("sendtime", new Date());
			update.execute();
	
		} catch (Exception e) {
			Logger.info( e.toString() );
			return false;
		}
		return true;
	}
	
	public boolean sendMsgSimple(HashMap<String,Object> prop,String to) {
		// 使用JavaMail的MimeMessage，支付更加复杂的邮件格式和内容
		MimeMessage msg = mailSender.createMimeMessage();
		// 创建MimeMessageHelper对象，处理MimeMessage的辅助类
		try {
			MimeMessageHelper helper = new MimeMessageHelper(msg, true);
			// 使用辅助类MimeMessage设定参数
			helper.setFrom( mailSender.getUsername() );
			if(to.equals("")){
				to = mailSender.getUsername();
			}
			helper.setTo(to);
			Logger.info( "recive:"+to);
			helper.setSubject( prop.get("subject").toString() );
			helper.setText(prop.get("html").toString(),true );
			
			// 发送邮件
			mailSender.send(msg);
			
		} catch (Exception e) {
			Logger.info( e.toString() );
			return false;
		}
		return true;
	}

    //简单下发
	public String sendEmail(int uid , int aid ,String to, String content, String subject, String htmlContent) {
		HashMap<String,Object> prop=new HashMap<String,Object>();
		prop.put("subject", subject);          
		prop.put("html"   , content);  
		sendMsg(  uid ,  aid ,"", to, content, prop);
		return "";
	}
	//试用申请发送邮件
	public String sendEmailTrial(String subject,String content,String to){
		HashMap<String,Object> prop=new HashMap<String,Object>();
		prop.put("subject", subject);          
		prop.put("html"   , content);  
		sendMsgSimple(prop,to);
		return "";
	}

}
