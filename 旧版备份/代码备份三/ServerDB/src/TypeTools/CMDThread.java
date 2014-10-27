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
			input.next(cmd);
			if (cmd.equals("QUIT")){
				break;
			}
		}
		try {
			if (!socket.isClosed())
				socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
