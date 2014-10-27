/*///////////////////////////////////////////////
 * @author	:john
 * @time	:17/5/2014
 * @function:以数据库作为第三方,来实现两个Socket通道数据通信
 *///////////////////////////////////////////////
package net;

//import java.net.Socket;

import database.NetBridgeDatabase;
import protocol.MyPack;

public class SocketDatabaseAdaptTransmit implements Transmit{

	Security security;
	private NetBridgeDatabase bridgeDB;
	//private Socket acceptSocket;
	
	public SocketDatabaseAdaptTransmit(NetBridgeDatabase db/*, Socket socket*/, Security security){
		
		//this.acceptSocket = socket;
		this.bridgeDB = db;
		this.security = security;
		System.out.println("SocketDatabaseAdaptTransmit construct!");
	}
	public int setSecurity(Security security){
		
		this.security = security;
		
		return 0;
	}
	@Override
	public int send() {
		// TODO Auto-generated method stub
		
		this.security.encrypt();					//encrypt pack before send data!
		System.out.println("SocketDatabaseAdaptTransmit send data!");
		
		return 0;
	}

	@Override
	public int receive() {
		// TODO Auto-generated method stub
		
		System.out.println("SocketDatabaseAdaptTransmit receive data!");
		this.security.decrypt();					//decrypt pack after receive data!
		
		return 0;
	}

	@Override
	public int setBuffer(MyPack pack) {
		// TODO Auto-generated method stub
		
		System.out.println("SocketDatabaseAdaptTransmit prefer to setBuffer!");
		this.bridgeDB.getPackage(pack);				//set pack to database after prefer to setBuffer!
		System.out.println("SocketDatabaseAdaptTransmit succeed to setBuffer!");
		return 0;
	}

	@Override
	public int getBuffer(MyPack pack) {
		// TODO Auto-generated method stub
		System.out.println("SocketDatabaseAdaptTransmit prefer to getBuffer!");
		this.bridgeDB.setPackage(pack);				//get pack from database before getBuffer!
		System.out.println("SocketDatabaseAdaptTransmit succeed to getBuffer!");
		
		return 0;
	}
	
}
