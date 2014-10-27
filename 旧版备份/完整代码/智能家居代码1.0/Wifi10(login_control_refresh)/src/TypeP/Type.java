package TypeP;

public class Type {
	//信号类型（登录,注册，刷新，控制）
	public final static int T_LOAD = 0x10;
	public final static int T_REGISTER = 0x11;
	public final static int T_REFRESH = 0x12;
	public final static int T_CONTROL = 0x13;
	
	//服务器返回给客户端编码
	public final static int REC_OK = 0x01;//返回确认
	public final static int LOGIN_ERROR = 0x00;//登录失败
	
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
	public final static int R_SUCCESS = (byte)0x01;
	public final static int REFRESH_ACK_OK = R_SUCCESS;
	public final static int R_ERROR = (byte)0x00;
	public final static int REFRESH_ACK_ERROR = R_ERROR;
	//控制码
	public final static int C_OPEN = (byte)0x01;
	public final static int C_CLOSE = (byte)0x00;
}
