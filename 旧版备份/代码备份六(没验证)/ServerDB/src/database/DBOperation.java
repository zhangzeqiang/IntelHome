/*
 * 数据库接口操作
 */
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
	
	/*
	 * 数据库操作描述符
	 */
	static Statement statement;
	static Connection conn;

	/*
	 * 通过用户，密码，和sql数据库地址和数据库名，获取描述符
	 */
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
			System.out.println("Sorry,can't find the Driver!"); 
			e.printStackTrace();
		} catch(SQLException e) {
			System.out.println("Sorry,have SQL ERROR!");
			e.printStackTrace();
		} catch(Exception e) {
			System.out.println("Sorry,have exception!");
			e.printStackTrace();
		} 
	}
	
	/*
	 * 查找类sql语句(select)的执行入口函数 --- 特点，返回一个描述符，以方便用户获取信息串
	 * 
	 */
	public ResultSet find(String sql) throws SQLException{
		ResultSet rs;
		rs = statement.executeQuery(sql);
		//rs.close();	//不能关闭，因为返回值需要用到rs
		System.out.println("Go to find...");
		return rs;
	}
	
	/*
	 * 插入类sql语句(insert)的执行入口函数 --- 特点，返回成功执行与否
	 */
	public boolean insert(String sql) throws SQLException{
		System.out.println("Go to insert...");
		return statement.execute(sql);
	}
	
	/*
	 * 更新,删除类sql语句()的执行入口函数 --- 特点，返回成功删除，更新执行条数
	 */
	public int update(String sql) throws SQLException{
		System.out.println("Go to update...");
		return statement.executeUpdate(sql);
	}
	
	/*
	 * 更新,删除类sql语句()的执行入口函数 --- 特点，返回成功删除，更新执行条数
	 */
	public int delete(String sql) throws SQLException{
		System.out.println("Go to delete...");
		return statement.executeUpdate(sql);
	}
	
	/*
	 * 查找类sql语句(select)的执行入口函数 --- 特点，返回一个描述符，以方便用户获取信息串
	 */
	public ResultSet select(String sql) throws SQLException{
		ResultSet rs;
		rs = statement.executeQuery(sql);
		//rs.close();	//不能关闭，因为返回值需要用到rs
		System.out.println("Go to select...");
		return rs;
	}
	
	/*
	 * 每次使用数据库都要记得显示关闭描述符
	 */
	public void close() throws SQLException{
		if (!conn.isClosed()){
			conn.close();
		}
		System.out.println("Success closeing to the Database!");
	}
}
