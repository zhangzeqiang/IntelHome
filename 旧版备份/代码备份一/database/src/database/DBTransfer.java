package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import Pack.Message;
import Pack.pack;
import database.DBOperation;

public class DBTransfer {
	// URL指向要访问的数据库名test
	String url = "jdbc:mysql://127.0.0.1:3306/test";
	// MySQL配置时的用户名
	String user = "root";
	// MySQL配置时的密码
	String passwd = "13760273143";
	DBOperation db;
	String T_user = "user";
	String T_message = "message";
	
	/*
	 * 错误定义
	 */
	public static final int E_PASSWD = -11;
	public static final int E_REP_RECORD = -12;
	public static final int E_INSERT = -13;
	public static final int N_RECORD = -14;
	
	/*
	 * 成功定义	--- 若大于0也可以表示update成功的记录数
	 */
	public static final int S_INSERT = 5000;
	public static final int S_GET = 5001;
	/*
	 * 异常定义
	 */
	public static final int X_EXCEPTION = -5000;
	
	public DBTransfer(){
		db = new DBOperation(url, user, passwd);
	}
	public void close() throws SQLException{
		db.close();
	}
	// 添加用户机个人信息 
	// 备注:包含较验用户机,若id存在且密码正确,则更新IP等信息
	public int addPack(pack p){
		String ip = p.getIP();
		int passwd = p.getPasswd();
		int port = p.getPort();
		int id = p.getid();
		String sql = "";
		ResultSet rs;
		
		try {
			//检查id是否已经存在
			sql = "select passwd from "+T_user+" where id="+id;
			rs = db.find(sql);
			//id存在记录且密码正确，更新IP和port端口
			while (rs.next()){
				if (rs.getInt("passwd") != passwd){
					rs.close();
					return E_PASSWD;
				}else{
					//id不存在，则直接插入信息段
					sql = "update "+T_user+" set ip='"+ip+"',port="+port+" where id="+id;		//注意这里string要加个单引号括起来，不然不能用
					rs.close();
					return db.update(sql);
				}
			}
			//System.out.println("passwd: "+passwd);
			//id不存在，则直接插入信息段
			sql = "insert into "+T_user+"(id,passwd,IP,port) values("
						 +id+","+passwd+",'"+ip+"',"+port+")";		//注意这里string要加个单引号括起来，不然不能用
			rs.close();
			return (db.insert(sql)?E_INSERT:S_INSERT);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			//System.out.println("pack数据插入失败");
			//e1.printStackTrace();
			return X_EXCEPTION;
		}
	}
	//添加用户机控制消息
	public int addMessage(Message m){
		int user_id = m.getid();
		int cmd = m.getCmd();
		int action = m.getAction();
		String sql = "";
		ResultSet rs;
		int newId = 0;
		try {
			//首先获取messageId,以便以有序的方式获取填充id
			sql = "select id from "+T_message+" where user_id="+user_id;
			rs = db.find(sql);
			while (rs.next()){
				if( newId != rs.getInt("id")){
					break;
				}
				newId++;
			}//newId即为所获得的按序id
			
			//插入(newId,cmd,user_id,action)
			sql = "insert into "+T_message+"(id,cmd,user_id,action) values("+
			newId+","+cmd+","+user_id+","+action+")";
			rs.close();
			return (db.insert(sql)?E_INSERT:S_INSERT);
			
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			//System.out.println("message添加失败");
			//e1.printStackTrace();
			return X_EXCEPTION;
		}
	}
	//获取用户机个人信息
	//说明:state表示程序运行结果是否成功等信息 --- 是一个返回值
	public int getPack(int id, int passwd, pack p){
		//pack p = new pack(0,"",0,0);
		ResultSet rs;
		try {
			//获取指定id下的密码记录
			String sql="select id,passwd,IP,port from "+T_user+" where id="+id;
			rs = db.find(sql);
			//id存在记录且密码正确,获取IP,port
			while (rs.next()){
				//有id记录，判断密码是否正确
				if (rs.getInt("passwd") != passwd){
					rs.close();
					return E_PASSWD;
				}else{
					//密码正确
					p.setid(rs.getInt("id"));
					p.setIP(rs.getString("IP"));
					p.setPasswd(passwd);
					p.setPort(rs.getInt("port"));
					rs.close();
					return S_GET;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return X_EXCEPTION;
		}
		//System.out.println("不存在Id记录");
		return N_RECORD;
	}
	//获取用户机控制消息
	//成功返回message表中第一条记录
	public int getMessageWithId(int user_id, Message m){
		ResultSet rs;
		int message_id = 0;
		try {
			String sql = "select id,cmd,action from "+T_message+" where user_id="+user_id;
			rs = db.find(sql);
			//id存在记录且密码正确,获取IP,port
			while (rs.next()){
				//有id记录，判断密码是否正确
				m.setid(user_id);
				message_id = rs.getInt("id");
				m.setCmd(rs.getInt("cmd"));
				m.setAction(rs.getInt("action"));
				break;
			}
			rs.close();
			return message_id;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			//rs.close();
			return X_EXCEPTION;
		}
	}
	//删除用户机个人信息
	public int deletePack(int id, int passwd){
		try {
			String sql = "delete from "+T_user+" where id="+id+" and passwd="+passwd;
			return db.delete(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return X_EXCEPTION;
		}
	}
	//删除用户机控制消息
	public int deleteMessageWithId(int id){
		try {
			String sql = "delete from "+T_message+" where id="+id;
			return db.delete(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return X_EXCEPTION;
		}
	}
	public int deleteMessagesOfUser(int user_id){
		try {
			String sql = "delete from "+T_message+" where user_id="+user_id;
			return db.delete(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return X_EXCEPTION;
		}
	}
}

