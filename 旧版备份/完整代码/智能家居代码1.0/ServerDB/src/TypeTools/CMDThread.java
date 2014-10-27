package TypeTools;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

public class CMDThread implements Runnable{
	ServerSocket socket;
	public CMDThread(ServerSocket s){
		this.socket = s;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		String cmd = "GO";
		while (true){
			Scanner input = new Scanner(System.in);
			System.out.println("请输入控制指令 ---- QUIT退出当前进程");
			cmd = input.nextLine();
			if (cmd.equals("QUIT")){
				break;
			}else{
			}
		}
		this.close();
	}
	public void close(){
		System.out.println("服务器正在关闭...");
		if (!this.socket.isClosed()){
			try {
				this.socket.close();
				System.out.println("服务器成功关闭");
			} catch (IOException e) {
				System.out.println("服务器关闭异常,进程终止");
				System.exit(0);
			}
		}
	}
}
