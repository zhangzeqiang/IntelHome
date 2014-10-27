package main;

import protocol.MyPack;
import database.MysqlDB;
import database.NetBridgeDatabase;
import net.NetTransmit;
import net.SimpleSecurityFunc;
import net.SocketDatabaseAdaptTransmit;
import net.Transmit;

public class DoMain {

	public static void main(String args[]){
		
		MysqlDB mysql = new MysqlDB();													//跟数据库无关
		mysql.open();
			
		SocketDatabaseAdaptTransmit socketAdapt = 
				new SocketDatabaseAdaptTransmit(new NetBridgeDatabase(mysql), new SimpleSecurityFunc());//数据库实现通信
		
		//使用socket协议通信
		NetTransmit netTransmit = new NetTransmit(socketAdapt);			
		
		/*///////////////////////////////////////////////////////////
		 *                 以下全部为跟协议和数据库源类型无关的代码
		 *///////////////////////////////////////////////////////////
		Transmit transmit = (Transmit)netTransmit;										//使用
		NetThread netThread = new NetThread(transmit);
		Thread thread = new Thread(netThread);
		thread.start();
		MyPack pack = new MyPack();
		netThread.receive(pack);
		
		try {
			if (thread != null){
				thread.join();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mysql.close();
	}
}
