package DataTransfer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

import database.DBTransfer;
import Pack.Message;
import Pack.pack;
import TypeTools.Tools;
import TypeTools.Type;

public class StreamControl {
	OutputStream out;
	InputStream in;
	Socket socket;
	public StreamControl(Socket s, OutputStream out, InputStream in){
		this.socket = s;
		this.out = out;
		this.in = in;
		if (true)	body();
	}

	private void body(){
		//这里可以新建一个分支去处理
		Thread thread = null;
		int boardOrPhoneType = Type.NODEFINE;
		int id = -1, passwd = -1;
		int i = 0;
		while (true){
			byte[] rec = new byte[100];
			try {
				i = this.in.read(rec);
				if (i == -1){		//socket断开连接
					if (boardOrPhoneType == Type.BEGIN_BOARD){		//板子断开连接
						if (id != -1 && passwd != -1){				//id有刷新
							//刷新state = 0
							DBTransfer db = new DBTransfer();
							if (DBTransfer.X_EXCEPTION == db.updateStateInUser(id, passwd, 0)){
								System.out.println("刷新板子state出现异常");
							}else {
								System.out.println("刷新板子state成功");
							}
						}
					}
					break;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				break;
			}
			//获取通信一些必要信息
			if (rec[1] >= Type.BEGIN_PHONE && rec[1] <= Type.END_PHONE){
				boardOrPhoneType = Type.BEGIN_PHONE;
			}else if(rec[1] >= Type.BEGIN_BOARD && rec[1] <= Type.END_BOARD){	//获取板子通信的信息
				boardOrPhoneType = Type.BEGIN_BOARD;
				//通过刷新这个通信方法，知道板子是否断线。
				if (Type.B_FLESH == rec[1]){
					if (rec[0] > 4){
						id = rec[2];
						passwd = rec[3];
					}
				}
			}else{
				boardOrPhoneType = Type.NODEFINE;
			}
			
			//开辟线程处理一个事件
			thread = new Thread(new EventHandleThread(rec,this.out));	//注意，这里不能出现读socket的情况
			thread.start();
			/*try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		}
		try {
			if (i != -1){
				thread.join();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}
	
/*	private void body(){
		//这里可以新建一个分支去处理
		Thread thread = null;
		int i = 0;
		while (true){
			byte[] rec = new byte[100];
			try {
				i = this.in.read(rec);
				if (i == -1){		//socket断开连接
					break;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				break;//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! 
			}
			//开辟线程处理一个事件

			thread = new Thread(new EventHandleThread(rec,this.out));	//注意，这里不能出现读socket的情况
			thread.start();
			try {
				//Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			if (i != -1){
				thread.join();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}*/
	public int writeByte(byte[] b){
		try {
			this.out.write(b);
			this.out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	/*public byte[] readByte() throws Exception{
		byte[] b = new byte[100];
		int i = this.in.read(b);
		if (i == -1){
			System.out.println("socket断开");
		}
		return b;
	}*/
	private class EventHandleThread implements Runnable{
		byte[] rec;
		OutputStream out;
		EventHandleThread(byte[] rec,OutputStream out){
			this.rec = rec;
			this.out = out;
		}
		public int writeByte(byte[] b){
			try {
				this.out.write(b);
				this.out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return 0;
		}
		/*
		 * 登陆模块
		 * 返回码:byte[],(length,code)
		 * 其中code有Type.L_SUCCESS(id,passwd匹配成功),Type.L_ERROR(匹配不成功)
		 * code为L_SUCCESS时还会返回此id号在数据库中的信息
		 */
		protected int onLoad(byte[] rec){
			System.out.println("客户机正在登陆...");
			//byte len	= rec[0];
			byte type	= rec[1];
			int id 		= rec[2] & 0xFF;
			int passwd 	= rec[3] & 0xFF;
			
			DBTransfer db = new DBTransfer();
			pack p = new pack();
			switch (db.getPack(id, passwd, p)){
		//	switch (DBTransfer.S_GET){
			case DBTransfer.S_GET:{				//登陆成功
				//成功登陆
				byte[] send = new byte[3];
				send[0] = 3;
				send[1] = type;
				send[2] = (byte)Type.L_SUCCESS;
				/*从数据库中查找信息*/
				this.writeByte(send);
				System.out.println("客户机登陆成功...");
			}break;
			case DBTransfer.E_PASSWD:			//密码错误
			default:{							//其他情况
				//登陆失败
				byte[] send = new byte[3];
				send[0] = 3;
				send[1] = type;
				send[2] = (byte)Type.L_ERROR;
				this.writeByte(send);
				System.out.println("客户机登陆失败...");
			}break;
			}
			
			db.close();
			return 0;
		}
		
		protected int onRegister(byte[] b){
			System.out.println("注册");
			return 0;
		}
		
		protected int onFlesh(byte[] b){
			System.out.println("客户机正在刷新...");
			/*
			 * if success
			 */
			byte type = b[1];			//type
			int id = b[2] & 0xff;		//id
			int passwd = b[3] & 0xff;	//passwd
			int i = DBTransfer.N_RECORD;
			
			DBTransfer db = new DBTransfer();
			pack p = new pack();
			switch (db.getPack(id, passwd, p)){
			case DBTransfer.S_GET:{				//登陆成功
				//成功登陆
				/*
				 * if success
				 */
				//len\type\id\passwd\state\dev_num\devno1\devstate1...
				byte[] send = new byte[100];
				send[1] = type;
				send[2] = (byte)id;
				send[3] = (byte)passwd;
				send[4] = (byte) p.getState();  //板子是否在线
				//获取所有Message记录
				Message[] m = new Message[100];
				i = db.getMessagesWithId(id, m);		//dev_num
				if (i > 0){
					//刷新成功
					send[5] = (byte)(i & 0xFF);		//dev_num
					int j = 0;
					for (j=0;j<i;j++){
						send[6+2*j] = (byte) m[j].getCmd();		//devno
						send[7+2*j] = (byte) m[j].getAction();	//devState
					}
					send[0] = (byte) (6+2*j);

					/*从数据库中查找信息*/
					this.writeByte(send);

					System.out.println("客户机刷新成功...");
				}	//获取全部记录数
			}break;
			case DBTransfer.E_PASSWD:			//密码错误
			default:{							//其他情况
				//刷新失败
				i = DBTransfer.N_RECORD;
			}break;
			}
			
			if (/*i == DBTransfer.N_RECORD || i == DBTransfer.X_EXCEPTION*/i <= 0){		//刷新失败
				/*
				 * if fail
				 */
				//len=3,type=b[1],failCode=0;
				byte[] send = new byte[3];
				send[0] = 3;
				send[1] = type;
				send[2] = (byte)Type.L_ERROR;
				this.writeByte(send);
				System.out.println("客户机刷新失败...");
			}
			
			db.close();
			
			return 0;
		}
		
		protected int onControl(byte[] b){
			System.out.println("客户机正在发送控制指令...");
			
			//指令包发送给ARM板
			if (Board_onControl(b) > 0){
				//数据返回给手机,成功控制
				byte[] send = new byte[10];
				send[0] = 3;
				send[1] = b[1];
				send[2] = Type.L_SUCCESS;
				writeByte(send);
			}else{
				//数据返回给手机，控制失败
				byte[] send = new byte[10];
				send[0] = 3;
				send[1] = b[1];
				send[2] = Type.L_ERROR;
				writeByte(send);
			}
			
			System.out.println("客户机完成控制...");
			return 0;
		}
		
		protected int Board_onLoad(byte[] b){
			System.out.println("ARM正在登陆...");
			//byte len	= rec[0];
			byte type	= rec[1];
			int id = rec[2] & 0xFF;
			int passwd = rec[3] & 0xFF;
			
			DBTransfer db = new DBTransfer();
			pack p = new pack();
			switch (db.getPack(id, passwd, p)){
			case DBTransfer.S_GET:{				//登陆成功
				//成功登陆
				byte[] send = new byte[3];
				send[0] = 3;
				send[1] = type;
				send[2] = (byte)Type.L_SUCCESS;
				/*从数据库中查找信息*/
				this.writeByte(send);
				System.out.println("ARM登陆成功...");
			}break;
			case DBTransfer.E_PASSWD:			//密码错误
			default:{							//其他情况
				//登陆失败
				byte[] send = new byte[3];
				send[0] = 3;
				send[1] = type;
				send[2] = (byte)Type.L_ERROR;
				this.writeByte(send);
				System.out.println("ARM登陆失败...");
			}break;
			}
			
			db.close();
			return 0;
		}
		
		protected int Board_onRegister(byte[] b){
			System.out.println("ARM正在注册...");
			
			System.out.println("ARM退出注册...");
			return 0;
		}
		
		protected int Board_onFlesh(byte[] b){
			System.out.println("ARM正在刷新...");
			//byte len 	= b[0];
			byte type 	= b[1];				//Type.B_Flesh
			byte id 	= b[2];
			byte passwd = b[3];					
			byte state	= b[4];				//Arm state(on_line or out_line)
			byte dev_num= b[5];
			
			class dev_desprition{
				public byte dev_no;
				public byte dev_state;
				public dev_desprition(){
					this.dev_no = 0;
					this.dev_state = 0;
				}
			}
			dev_desprition dev_des[] = new dev_desprition[10];
			int i = 0;
			for(i=0;i<dev_num;i++){
				dev_des[i] = new dev_desprition();		//初始化
				
				dev_des[i].dev_no = b[6+i*2];		//dev_no
				dev_des[i].dev_state = b[7+i*2];	//dev_state
			}
			
			//检查id, passwd对应的记录是否存在
			DBTransfer db = new DBTransfer();
			pack p = new pack();
			
			switch (db.getPack(id, passwd, p)){
			case DBTransfer.S_GET:{				//存在记录，刷新数据库
				//获取用户ip,port,并更新用户信息
				//String ip = socket.getLocalAddress().getHostAddress();	//ip
				String ip = socket.getInetAddress().getHostAddress();
				int port = socket.getLocalPort();						//服务器端口
				p.setIP(ip);
				p.setPort(port);
				p.setState(state);
				
				byte send[] = new byte[100];
				send[0] = 3;
				send[1] = type;						//Type.B_Flesh
				
				if (db.addPack(p) > 0){
					//删除原来message记录并更新message记录
					if (db.deleteMessagesOfUser(id) != DBTransfer.X_EXCEPTION){
					
						for(i=0;i<dev_num;i++){
							
							Message m = new Message();
							m.setid(id);
							m.setCmd(dev_des[i].dev_no);
							m.setAction(dev_des[i].dev_state);
							
							db.addMessage(m);
						}
						send[2] = Type.F_SUCCESS;			//刷新成功
					}else{
						send[2] = Type.F_ERROR;				//刷新失败
					}
				}else{	send[2] = Type.F_ERROR;		}		//刷新失败
				
				this.writeByte(send);
				System.out.println("ARM刷新成功...");
			}break;
			case DBTransfer.E_PASSWD:			//密码错误或用户不存在
			default:{							//其他情况
				byte send[] = new byte[100];
				send[0] = 3;
				send[1] = type;					//Type.B_Flesh
				send[2] = Type.F_ERROR;			//刷新失败
				this.writeByte(send);
				System.out.println("ARM刷新失败...");
			}break;
			}
			db.close();
			//记录存在，则将信息更新
			
			return 0;
		}
		
		/*
		 * 错误码:ARM不处于监听状态，主动连接socket失败 -1
		 * 
		 */
		protected int Board_onControl(byte[] b){
			System.out.println("服务器尝试主动连接ARM...");
			//获取指令包
			//byte len = b[0];
			//byte type = b[1];
			b[1] = Type.B_CONTROL;
			byte id = b[2];
			byte passwd = b[3];
			//byte dev_num= b[4];
			
			//获取ARM板信息
			pack p = new pack();
			DBTransfer db = new DBTransfer();
			if (db.getPack(id, passwd, p) == DBTransfer.S_GET){
				//存在记录
				String ip = p.getIP();
				int port = p.getPort() + 1;
				//新建socket,连接客户机
				try {
					Socket s = new Socket(ip, port);
					InputStream input = s.getInputStream();
					OutputStream output = s.getOutputStream();
					System.out.println("服务器成功与ARM建立连接，正在操作ARM...");
					
					output.write(b);
					
					byte[] rec = new byte[100];
					input.read(rec);		//收到ARM的回复
					//将数据更新数据库
					if (Board_onControl_update(rec) == 1){
						System.out.println("服务器退出与ARM的主动连接，完成操作...");
						return 1;
					}else{
						System.out.println("服务器更新ARM状态信息失败...");
						return -2;
					}
				} catch (ConnectException e){
					System.out.println("服务器无法与ARM建立连接，请检查ARM是否正处于监听状态");
					System.out.println("服务器退出连接操作...");
					db.close();
					return DBTransfer.X_EXCEPTION;
				}catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else{
				db.close();
				return DBTransfer.N_RECORD;
			}
			db.close();
			System.out.println("服务器退出连接操作...");
			return 0;
		}
		private int Board_onControl_update(byte[] rec) {
			// TODO Auto-generated method stub
			byte len = rec[0];
			byte type = rec[1];
			byte id = rec[2];
			byte passwd = rec[3];
			byte dev_num = rec[4];
			
			if (len == 3){
				//ARM fail to fresh
				return 0;
			}else{
				//ARM succeed to fresh
				DBTransfer db = new DBTransfer();
				pack p = new pack();
				int sign = 0;
				if ((sign = db.getPack(id, passwd, p)) == DBTransfer.S_GET){
					//删除原来的message记录，并更新message记录
					db.deleteMessagesOfUser(id);
					int i = 0;
					while (i<dev_num){
						Message m = new Message();
						m.setCmd(rec[5+i*2]);
						m.setAction(6+i*2);
						m.setid(id);
						db.addMessage(m);
						i++;
					}
					db.close();
					return 1;
				}else{
					db.close();
					return sign;
				}
			}
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			//开线程处理事件
			switch (rec[1] & 0xFF){
			case Type.T_LOAD:{		
				onLoad(rec);
			}break;
			case Type.T_REGISTER:{	
				onRegister(rec);
			}break;
			case Type.T_FLESH:{		
				onFlesh(rec);
			}break;
			case Type.T_CONTROL:{	
				onControl(rec);
			}break;
			case Type.B_LOAD:{
				Board_onLoad(rec);
			}break;
			case Type.B_REGISTER:{
				Board_onRegister(rec);
			}break;
			case Type.B_FLESH:{
				Board_onFlesh(rec);
			}break;
			default:{
				System.out.println("未知操作");
				byte[] send = new byte[100];
				send[0] = 0;
				this.writeByte(send);
			}break;
			}
		}
	}
}
