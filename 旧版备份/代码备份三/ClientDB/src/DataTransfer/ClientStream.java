package DataTransfer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;

public class ClientStream implements Runnable{
	Socket socket;
	InputStream input;
	OutputStream output;
	/*
	 * 进行错误码
	 */
	static final int E_CONNECT = -10;
	static int ifExit = 1;
	
	public ClientStream(String ip, int port){
		System.out.println("客户机正在尝试建立连接...");
		try {
			socket = new Socket(ip, port);
			input = socket.getInputStream();
			output = socket.getOutputStream();
			System.out.println("客户机成功建立连接...");
		} catch (ConnectException e){
			ifExit = E_CONNECT;
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void run(){
		switch (ifExit){			//错误码
		case E_CONNECT:	{	
			System.out.println("客户机建立连接失败,请检查端口是否正确");
			return ;
		}
		default:break;
		}
		body();
		this.close();
	}
	protected int body(){
		Tools tools = new Tools();
		byte[] b_ip = tools.ipv4Address2BinaryArray("168.123.445.186");
		System.out.println(b_ip.length);
		try {
			/*byte b_type = LOAD;*/
			output.write(b_ip);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	protected void close(){
		System.out.println("客户机正在断开连接...");
		try {
			if (!socket.isClosed()){
				output.close();
			}
			if (!socket.isClosed()){
				socket.close();
				System.out.println("客户机成功断开连接");
			}
		} catch (IOException e) {
			System.out.println("客户机断开连接时发生异常");
		}
	}
}
