/*
 * 用户机个人信息块
 */
package Pack;

public class pack {
	String IP;						//用户机IP
	int id;							//用户机服务器上注册的唯一编码码值(比如QQ号码)
	int passwd;						//用户编码密码(比如QQ密码)
	int port;						//用户机希望服务器跟其建立通信的端口号
	int state;
	
	/*
	 * state的状态
	 */
	public static final int NO_ONLINE = 0x0000;
	public static final int ONLINE = 0x0001;
	
	public pack(int id, String ip, int passwd, int port, int state){
		this.IP = ip;
		this.passwd = passwd;
		this.id = id;
		this.port = port;
		this.state = state;
	}
	public pack(){
		this.IP = "127.0.0.1";
		this.passwd = 0;
		this.id = 0;
		this.port = 0;
		state = NO_ONLINE;
	}
	
	/*
	 * set
	 */
	public boolean setIP(String ip)	{	this.IP = ip;	return true;}
	public boolean setid(int id)	{	this.id = id; 	return true;}
	public boolean setPasswd(int passwd) {this.passwd = passwd;return true;}
	public boolean setPort(int port){this.port = port; return true;	}
	public boolean setState(int state){	this.state = state; return true;	}
	/*
	 * get
	 */
	public String getIP()	{	return this.IP;		}
	public int getid()		{	return this.id;		}
	public int getPasswd()	{	return this.passwd;	}
	public int getPort()	{	return this.port;	}
	public int getState()	{	return this.state;	}
}
