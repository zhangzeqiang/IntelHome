/*
 * 常用数据库操作接口
 * 包括用户机个人信息，用户机控制消息表的常用函数接口
 */
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
	public static final int E_PASSWD = -11;			//密码错误
	public static final int E_REP_RECORD = -12;		//重复记录错误
	public static final int E_INSERT = -13;			//插入记录失败
	public static final int N_RECORD = -14;			//没有记录
	
	
	/*
	 * 成功定义	--- 若大于0也可以表示update成功的记录数
	 */
	public static final int S_INSERT = 5000;		//插入记录成功
	public static final int S_GET = 5001;			//成功获取记录(可能为复数)
	
	/*
	 * 异常定义
	 */
	public static final int X_EXCEPTION = -5000;	//异常问题
	
	public DBTransfer(){
		db = new DBOperation(url, user, passwd);
	}
	
	
	/*
	 * 关闭函数  --- 每次外部使用这个操作类都必须显示的关闭数据库接口
	 */
	public void close(){
		try {
			db.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * 添加用户机个人信息 
	 * 备注:包含较验用户机,若id存在且密码正确,则更新IP等信息
	 * 返回码有错误信息(E_PASSWD,X_EXCEPTION)
	 * 若原来不存在记录则插入，有E_INSERT,S_INSERT等负值
	 * 若原本有记录则更新记录，为正值，表示更新的记录条数,由于id唯一性，只能有1和0
	 */
	public int addPack(pack p){
		String ip = p.getIP();
		int passwd = p.getPasswd();
		int port = p.getPort();
		int id = p.getid();
		int state = p.getState();
		
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
					sql = "update "+T_user+" set state="+state+",ip='"+ip+"',port="+port+" where id="+id;		//注意这里string要加个单引号括起来，不然不能用
					rs.close();
					return db.update(sql);
				}
			}
			//System.out.println("passwd: "+passwd);
			//id不存在，则直接插入信息段
			sql = "insert into "+T_user+"(id,passwd,IP,port,state) values("
						 +id+","+passwd+",'"+ip+"',"+port+","+state+")";		//注意这里string要加个单引号括起来，不然不能用
			rs.close();
			return (db.insert(sql)?E_INSERT:S_INSERT);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			//System.out.println("pack数据插入失败");
			//e1.printStackTrace();
			return X_EXCEPTION;
		}
	}
	
	/*
	 * 添加用户机控制消息
	 * 作用，添加用户消息记录
	 * 返回码有E_INSERT，E_INSERT，X_EXCEPTION
	 */
	public int addMessage(Message m){
		int user_id = m.getid();
		int cmd = m.getCmd();
		int action = m.getAction();
		String sql = "";
		ResultSet rs;
		int newId = 0;
		try {
			//首先获取messageId,以便以有序的方式获取填充id
			sql = "select id from "+T_message;
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
	
	/*
	 * 获取用户机个人信息
	 * 作用:通过id和密码获取用户信息
	 * 返回码有E_PASSWD，S_GET，N_RECORD，X_EXCEPTION
	 */
	public int getPack(int id, int passwd, pack p){
		//pack p = new pack(0,"",0,0);
		ResultSet rs;
		try {
			//获取指定id下的密码记录
			String sql="select id,passwd,IP,port,state from "+T_user+" where id="+id;
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
					p.setState(rs.getInt("state"));
					rs.close();
					return S_GET;
				}
			}
			//System.out.println("不存在Id记录");
			return N_RECORD;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return X_EXCEPTION;
		}
	}
	
	/*
	 * 获取用户机控制消息
	 * 成功返回message表中第一条记录
	 * 返回码:N_RECORD(没有记录),X_EXCEPTION(异常)
	 * 若返回值大于0，表示成功获取的消息的id
	 */
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
				rs.close();
				return message_id;
			}
			rs.close();
			return N_RECORD;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			//rs.close();
			return X_EXCEPTION;
		}
	}
	
	/*
	 * 获取用户机控制消息
	 * 成功返回所有Message记录
	 * 返回码:N_RECORD(没有记录),X_EXCEPTION(异常)
	 * 若返回值大于0，表示成功获取的消息的个数
	 */
	public int getMessagesWithId(int user_id, Message[] m){
		ResultSet rs;
		try {
			String sql = "select id,cmd,action from "+T_message+" where user_id="+user_id;
			rs = db.find(sql);
			int i = 0;
			while (rs.next()){
				//有id记录,获取message信息
				m[i] = new Message();		//分配空间
				m[i].setid(user_id);
				m[i].setCmd(rs.getInt("cmd"));
				m[i].setAction(rs.getInt("action"));
				i++;
			}
			rs.close();
			if (i != 0){	return i;	}		//return the count of messages.
			return N_RECORD;			//没有记录，可能使密码错误或者用户名出错
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			//rs.close();
			return X_EXCEPTION;
		}
	}
	
	/*
	 * 删除用户机个人信息
	 * 返回码X_EXCEPTION
	 * 若返回码大于0，表示成功删除的用户信息记录条数，由于唯一性，只有0,1两种可能
	 */
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
	
	/*
	 * 删除一条用户机控制消息
	 * 返回码:X_EXCEPTION
	 * 若返回码大于0，表示成功删除的用户控制消息记录条数，由于这里是一条，故有0,1两种可能
	 */
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
	
	/*
	 * 删除对应用户机的控制消息
	 * 返回码:X_EXCEPTION
	 * 若返回码大于0，表示成功删除的用户控制消息记录条数,
	 */
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

