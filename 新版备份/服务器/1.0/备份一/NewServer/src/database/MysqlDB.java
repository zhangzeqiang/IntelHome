/*///////////////////////////////////////////////
 * @author	:john
 * @time	:17/5/2014
 * @function:mysql数据库源操作函数
 *///////////////////////////////////////////////
package database;

import java.sql.ResultSet;

public class MysqlDB implements Database{

	public MysqlDB(){
		
		System.out.println("MysqlDB construct!");
		
	}
	@Override
	public int open() {
		// TODO Auto-generated method stub
		
		System.out.println("MysqlDB open!");
		
		return 0;
	}

	@Override
	public int runSql(String sql) {
		// TODO Auto-generated method stub
		
		System.out.println("MysqlDB runSql!");
		
		return 0;
	}

	@Override
	public ResultSet getResource(String sql) {
		// TODO Auto-generated method stub
		
		System.out.println("MysqlDB getResource!");
		
		return null;
	}

	@Override
	public int close() {
		// TODO Auto-generated method stub
		
		System.out.println("MysqlDB close!");
		
		return 0;
	}
	
	
}
