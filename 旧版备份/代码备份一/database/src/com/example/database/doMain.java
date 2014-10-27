package com.example.database;
import java.sql.SQLException;

import database.DBTransfer;
import Pack.Message;
//import Pack.Message;
//import Pack.pack;
import Pack.pack;

public class doMain {
	@SuppressWarnings("unused")
	public static void main(String[] args){
		DBTransfer db = new DBTransfer();
		//更新
		/*pack p = new pack(15,"see",30,20);
		System.out.println(db.addPack(p));*/
		
		//添加消息
		/*Message m = new Message(15,10,10);
		System.out.println(db.addMessage(m));*/
		
		//添加用户信息
		/*pack p = new pack(0,"",0,0);
		int state = db.getPack(12, 20, p);
		System.out.println("state:"+state);
		System.out.println("id="+p.getid());
		System.out.println("IP="+p.getIP());
		System.out.println("passwd="+p.getPasswd());
		System.out.println("port="+p.getPort());*/
		
		//删除用户消息
		/*System.out.println("num:"+db.deleteMessagesOfUser(15));*/
		
		//删除用户
		/*System.out.println("num:"+db.deletePack(10, 20));*/
		
		//获取一条指定用户消息
		/*Message m = new Message();
		int state = db.getMessageWithId(15, m);
		System.out.println("id="+state);
		System.out.println("user_id="+m.getid());
		System.out.println("cmd="+m.getCmd());
		System.out.println("action="+m.getAction());*/
		
		//删除一条用户消息
		/*if (DBTransfer.X_EXCEPTION != state){
			db.deleteMessageWithId(state);
		}*/
		try {
			db.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 
}