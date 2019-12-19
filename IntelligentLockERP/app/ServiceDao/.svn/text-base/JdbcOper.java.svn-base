package ServiceDao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

import javax.transaction.Transaction;


import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlRow;

import play.Logger;
import util.GlobalDBControl;
import util.classEntity.StringTool;


public class JdbcOper {
	public Connection  con      = null;
	public CallableStatement cst=null;
	public ResultSet rs         = null;
	Transaction tran ;
	

	//获取一个数据库连接的操作Object,并且包装了存储过程的调用
	public static JdbcOper getCalledbleDao(String sql)
	{
		JdbcOper op = new JdbcOper();
		try{
			op.con = play.db.DB.getConnection();
			op.cst = op.con.prepareCall(sql);
		}catch(Exception e)
		{
			op.close();
			Logger.info( "Ebean GetDao error"+e.toString()+"  ---!!! "+sql );
			
			return op;
		}
		return op;
	}
	
	//释放数据库连接；
	public void close()
	{
		
		try{
			if(tran!=null) tran.commit();
			if(tran!=null) tran=null;
			if(rs!=null) rs.close();
		}
		catch(Exception e){}
		try{
			if(cst!=null) cst.close();
		}
		catch(Exception e){}
		try{
			if(con!=null) con.close();
		}
		catch(Exception e){}
	}
	
	
	//通用查询数量
	public static int findSqlCount(String sql)
	{
		//Logger.info( sql );
		SqlRow sqlrow = Ebean.getServer(GlobalDBControl.getReadDB()).createSqlQuery(sql)
				.findUnique();
		int num = 0;
		try{
			if( sqlrow!=null ){
				Integer i =sqlrow.getInteger("num");
				if(i!=null)num = i;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return num;
	}
	
	//通用执行ｓｑｌ
	public static int extSql(String sql)
	{
		int ret =0; 
		try{
			ret = Ebean.getServer(GlobalDBControl.getDB()).createSqlUpdate(sql).execute();
		}catch(Exception e){
			Logger.error("extSql - " + sql ,e);
		}
		return ret;
	}
	
}
