package TypeTools;

public class Type {
	/*
	 * 手机信号类型(注册，登陆，刷新，控制)
	 */
	public final static int T_LOAD = 0x10;
	public final static int T_REGISTER = 0x11;
	public final static int T_FLESH = 0x12;
	public final static int T_CONTROL = 0x13;
	
	/*
	 * 开发板信号类型(注册，登陆，刷新)
	 */
	public final static int B_LOAD = 0x20;		//登陆
	public final static int B_REGISTER = 0x21;	//注册
	public final static int B_FLESH = 0x22;		//刷新
	public final static int B_CONTROL = 0x23;	//控制
	
	/*
	 * 服务器返回给客户机编码 
	 */
	public final static int REC_OK = 0x01;
	public final static int REC_ERROR = 0x00;
	
	/*
	 * 状态信息
	 */
	public final static int S_ONLINE = 0x01;
	public final static int S_OUTLINE = 0x00;
	
	/*
	 * 登陆返回码
	 */
	public final static int L_SUCCESS = (byte)0x01;
	public final static int LOAD_ACK_OK	= L_SUCCESS;
	
	public final static int L_ERROR = (byte)0x00;
	public final static int LOAD_ACK_ERROR = L_ERROR;
	
	/*
	 * 刷新返回码
	 */
	public final static int F_SUCCESS = (byte)0x01;
	public final static int FLESH_ACK_OK = F_SUCCESS;
	public final static int F_ERROR = (byte)0x00;
	public final static int FLESH_ACK_ERROR = F_ERROR;
	
	public Type(){}
}
