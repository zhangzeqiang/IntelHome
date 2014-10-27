package Func;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import TypeP.Type;
import android.content.Intent;
import android.os.Bundle;

import com.example.wifi10.Login_page1;
import com.example.wifi10.Wifi10;

public class socket_func {
	public static Socket login_socket;
	public static OutputStream login_os;
	public static InputStream login_is;
	
	public static int socket_init(String IP,int port){//socket¡¨Ω”
		try{
			login_socket = new Socket(IP,port);
			login_os = login_socket.getOutputStream();
			login_is = login_socket.getInputStream();
			return 0x01;
		}
		catch(IOException e){
			//e.printStackTrace();
			return 0x02;
		}
	}
}
