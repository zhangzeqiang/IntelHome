package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBOperation {
// 驱动程序名
static final String DRIVER = "com.mysql.jdbc.Driver";
// URL指向要访问的数据库名
public final static String URL = "jdbc:mysql://127.0.0.1:3306/test";
// MySQL配置时的用户名
public final static String USER = "root";
// MySQL配置时的密码
public final static String PASSWORD = "13760273143";

static Statement statement;
static Connection conn;

	public DBOperation(String url, String user, String passwd){
		try { 
			// 加载驱动程序
			Class.forName(DRIVER).newInstance();
			// 连续数据库
			conn = DriverManager.getConnection(url, user, passwd);
			if(!conn.isClosed()) 
				System.out.println("Succeeded connecting to the Database!");
			// statement用来执行SQL语句
			statement = conn.createStatement();
		} catch(ClassNotFoundException e) {
			System.out.println("Sorry,can`t find the Driver!"); 
			e.printStackTrace();
		} catch(SQLException e) {
			System.out.println("Sorry,have SQL ERROR!");
			e.printStackTrace();
		} catch(Exception e) {
			System.out.println("Sorry,have exception!");
			e.printStackTrace();
		} 
	}
	public ResultSet find(String sql) throws SQLException{
		ResultSet rs;
		rs = statement.executeQuery(sql);
		//rs.close();	//不能关闭，因为返回值需要用到rs
		System.out.println("Finish to find...");
		return rs;
	}
	public boolean insert(String sql) throws SQLException{
		System.out.println("Finish to insert...");
		return statement.execute(sql);
	}
	public int update(String sql) throws SQLException{
		System.out.println("Finish to update...");
		return statement.executeUpdate(sql);
	}
	public int delete(String sql) throws SQLException{
		System.out.println("Finish to delete...");
		return statement.executeUpdate(sql);
	}
	public ResultSet select(String sql) throws SQLException{
		ResultSet rs;
		rs = statement.executeQuery(sql);
		//rs.close();	//不能关闭，因为返回值需要用到rs
		System.out.println("Finish to select...");
		return rs;
	}
	public void close() throws SQLException{
		if (!conn.isClosed()){
			conn.close();
		}
		System.out.println("Success closeing to the Database!");
	}
}
