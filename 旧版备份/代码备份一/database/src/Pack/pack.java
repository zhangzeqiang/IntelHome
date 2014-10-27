/*
 * 用户机个人信息块
 */
package Pack;

public class pack {
	String IP;
	int id;
	int passwd;
	int port;
	
	public pack(int id, String ip, int passwd, int port){
		this.IP = ip;
		this.passwd = passwd;
		this.id = id;
		this.port = port;
	}
	public pack(){
		this.IP = "127.0.0.1";
		this.passwd = 0;
		this.id = 0;
		this.port = 0;
	}
	public boolean setIP(String ip)	{	this.IP = ip;	return true;}
	public boolean setid(int id)	{	this.id = id; 	return true;}
	public boolean setPasswd(int passwd) {this.passwd = passwd;return true;}
	public boolean setPort(int port){this.port = port; return true;	}
	
	public String getIP()	{	return this.IP;		}
	public int getid()		{	return this.id;		}
	public int getPasswd()	{	return this.passwd;	}
	public int getPort()	{	return this.port;	}
}
