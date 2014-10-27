/*
 * 用户机控制消息块
 */
package Pack;

public class Message {
	int user_id;					//消息归属用户id
	int cmd;						//消息命令行代码集   --- 表示外设类型
	int action;						//可以用来表示关或者开等附加状态码
	
	/*
	 * cmd命令定义 
	 */
	public static final int LED = 0x0100;
	public static final int SOUND = 0x0101;
	public static final int PICTURE = 0x0102;
	
	/*
	 * action定义
	 */
	public static final int OFF	= 0x0000;
	public static final int ON= 0x0001;
	
	public Message(int user_id, int cmd, int action){
		this.user_id = user_id;
		this.cmd = cmd;
		this.action = action;
	}
	public Message(){
		this.user_id = 0;
		this.cmd = 0;
		this.action = OFF;
	}
	
	/*
	 * set
	 */
	public boolean setid(int id)		{	this.user_id = id;		return true;}
	public boolean setCmd(int cmd)		{	this.cmd = cmd;			return true;}
	public boolean setAction(int action){	this.action = action;	return true;}
	
	/*
	 * get
	 */
	public int getid()		{	return this.user_id;	}
	public int getCmd()		{	return this.cmd;		}
	public int getAction()	{	return this.action;		}
}
