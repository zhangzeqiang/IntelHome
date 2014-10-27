package com.example.wifi10;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import android.os.Bundle;
import android.os.Message;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import Func.StreamControl;
import Func.function;
import TypeP.Type;
import Func.socket_func;

public class Login_page1 extends Activity {
	final static int PORT = 30030;//socket端口
	final static String SER_IP = "192.168.1.107";
	MyHandler myHandler = null;
	private TextView id_text;
	private TextView pwd_text;
	private Button cancelButton = null;
	private Button loginButton = null;
	private byte id_b;
	private byte pwd_b;
	public static String id_str;
	public static String pwd_str;
	private Bundle data;
	public int i;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_page1);
		
		loginButton = (Button)findViewById(R.id.login);
		cancelButton = (Button)findViewById(R.id.cancel);
		loginButton.setOnClickListener(new loginListener());
		cancelButton.setOnClickListener(new cancelListener());
		id_text = (TextView)findViewById(R.id.id_text);
		pwd_text = (TextView)findViewById(R.id.pwd_text);
		myHandler = new MyHandler();	
		System.out.println("start1");	
		new Thread(){//建立socket连接（必须要新建线程），服务器的IP地址和port端口
			@Override
			public void run(){
						i = socket_func.socket_init(SER_IP,PORT);
			}
		}.start();
	}
	class MyHandler extends Handler{
		@Override
		public void handleMessage(Message msg){
			super.handleMessage(msg);
			id_text.setText(msg.obj.toString());
			System.out.println(msg.obj.toString());
		//	if(msg.what==Type.LOGIN_ERROR){//msg 为 004则表示登陆失败，给出提示
		//	}
		}
	}
	class loginListener implements OnClickListener{
		@Override
		public void onClick(View v){
			if(id_text.getText().length()!=0){//检测是否有输入，有则取出id和pwd
				if(pwd_text.getText().length()!=0){
					new Thread(){
						@Override
						public void run(){
					                if( i == 0x01){	//连接成功返回0x01
					                	i = 0x00;
					                   socket_constructer();
					                }
					                else{
					        			String socket_error_str = "连接失败，请关闭重试";
					        			msg_sender(Type.LOGIN_ERROR,socket_error_str);			       
					                }
						}
					}.start();
				}
			}
		}		
	}
	class cancelListener implements OnClickListener{
		@Override
		public void onClick(View v){
			id_text.setText("");
			pwd_text.setText("");
		}
	}
    public void msg_sender(int num,String str){//错误情况反馈,num用于区分错误状态，调试时显示原因，str显示在手机上
    	Message msg = new Message();
    	msg.what = num;
    	msg.obj= str;
    	myHandler.sendMessage(msg);
    }
    
	public void socket_constructer(){//socket连接
		try{
			socket_output(getinfo());
			System.out.println("已经发送");
			byte[] line_get = new byte[100];
			line_get = socket_input();
			if(line_get[2]==Type.L_SUCCESS){//跳转到状态页面Activity______Wifi10 
				String socket_error_str = "登录成功";
				msg_sender(Type.LOGIN_ERROR,socket_error_str);
				
				data = new Bundle();//把id和pwd打包通过intent发送到另一个Activity即Wifi10	
				byte[] id_pwd = new byte[2];
				id_pwd[0]=id_b;
				id_pwd[1]=pwd_b;
				data.putByteArray("id_pwd",id_pwd);
	
				Intent intent = new Intent(Login_page1.this,Wifi10.class);//页面跳转
				intent.putExtras(data);
				startActivity(intent);
				//socket_func.login_socket.close();
			}
			else{//登录失败，断开socket，返回错误提示
				String login_error_str = "登录失败:账号密码错误";
				msg_sender(Type.LOGIN_ERROR,login_error_str);
				socket_func.login_socket.close();
			}					
		}
		catch(IOException e){
			e.printStackTrace();
			String socket_error_str = "无法连接服务器，请稍后再试";
			this.msg_sender(Type.LOGIN_ERROR,socket_error_str);
		}
	}
	void socket_output(byte[] output){
		try{
			socket_func.login_os.write(output);
		}
		catch(IOException e){
			e.printStackTrace();
			String socket_error_str = "无法向服务器发送信息，请稍后再试";
			this.msg_sender(Type.R_ERROR,socket_error_str);
		}
	}
	
	byte[] socket_input(){
		byte[] state_data = new byte[100];
		state_data[0] = 0x00;//接收数据先初始化
		try{
		socket_func.login_is.read(state_data);
		}
		catch(IOException e){
			e.printStackTrace();
			String socket_error_str = "无法接受信息，请重新登陆";
			this.msg_sender(Type.R_ERROR,socket_error_str);
		}
		return state_data;
	}
	
	public byte[] getinfo(){//打包要发送的消息
		id_b = function.stringTochar(id_text.getText().toString());
		pwd_b = function.stringTochar(pwd_text.getText().toString());
		byte D_length = 0x04;
		byte[] login_data = new byte[4]; 
		
				login_data[0]=D_length;//数据打包
				login_data[1]=Type.T_LOAD;
				login_data[2]=id_b;
				login_data[3]=pwd_b;

		System.out.println("login_data is ok!!!");
		return login_data;
	}
	/*	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login_page1, menu);
		return true;
	}*/
}
