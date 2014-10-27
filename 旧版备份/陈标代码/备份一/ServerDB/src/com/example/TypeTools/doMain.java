package com.example.TypeTools;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import database.DBTransfer;

import TypeTools.CMDThread;
import DataTransfer.ServerStream;

public class doMain {
	final static int PORT = 30030;
	static String buff = "";
	static ServerSocket socket;
	public static void main(String[] args) throws IOException{
		init();
		//服务器控制指令(关闭，数据库导出，和其他服务器数据传输控制指令)
		new Thread(new CMDThread(socket)).start();
		//客户机线程
		while(true){
			try{
				Socket acceptSocket = socket.accept();
				//服务器线程类（load,flesh,control
				Thread thread1 = new Thread(new ServerStream(acceptSocket));
				thread1.start();
			} catch(SocketException e){
				//System.out.println("socket异常被关闭");
				break;
			}
		}
		
		/*try {
			thread1.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			if (!acceptSocket.isClosed()){
				acceptSocket.close();
			}
		}*/
	}
	public static void init(){
		System.out.println("服务器正在开启...");
		try{
			socket = new ServerSocket(PORT);
			System.out.println("服务器成功开启");
		}catch (IOException e){
			System.out.println("端口被占用,进程终止");
			System.exit(0);
		}
	}
	public static void close(){
		System.out.println("服务器正在关闭...");
		if (!socket.isClosed()){
			try {
				socket.close();
				System.out.println("服务器成功关闭");
			} catch (IOException e) {
				System.out.println("服务器关闭异常,进程终止");
				System.exit(0);
			}
		}
	}
}

