package DataTransfer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ServerStream implements Runnable{
	Socket socket;
	static OutputStream out;
	static InputStream in;
	static int ifExit = 1;
	
	/*
	 * 错误码
	 */
	static final int E_IOEXCEPTION = -10;
	
	public ServerStream(Socket s){
		this.socket = s;
		System.out.println("服务器正在建立新连接...");
		try {
			out = s.getOutputStream();
			in = s.getInputStream();
			System.out.println("服务器成功建立新连接");
		} catch (IOException e) {
			ifExit = E_IOEXCEPTION;
		}
	}
	@Override
	public void run() {
		switch (ifExit){			//错误码
		case E_IOEXCEPTION:	{	
			System.out.println("服务器建立连接失败");
			return ;
		}
		default:break;
		}
		body();
		this.close();
	}
	protected int body(){
		byte[] b = readByte();
		System.out.println(b[0] & 0xFF);
		int i = 0;
		while(i < 100){
			i++;
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return 0;
	}
	public static int writeByte(byte[] b){
		try {
			out.write(b);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	public static byte[] readByte(){
		byte[] b = new byte[100];
		try {
			in.read(b);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return b;
	}
	protected int close(){
		System.out.println("服务器正在断开连接...");
		try {
			in.close();
			out.close();
			if (!this.socket.isClosed()){
				this.socket.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("服务器断开连接");
		return 0;
	}
}
