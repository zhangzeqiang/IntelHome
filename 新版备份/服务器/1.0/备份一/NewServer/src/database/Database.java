/*///////////////////////////////////////////////
 * @author	:john
 * @time	:17/5/2014
 * @function:常用数据库操作必须函数
 *///////////////////////////////////////////////
package database;

import java.sql.ResultSet;

public interface Database {
	
	public int open();
	
	//excute sql command that will return nothing,like update,insert,delete
	public int runSql(String sql);
	
	//excute sql command that will return resource,like select.
	public ResultSet getResource(String sql);
	
	public int close();
	
}
