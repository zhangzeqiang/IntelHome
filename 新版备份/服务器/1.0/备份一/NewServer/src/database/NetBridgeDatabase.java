/*///////////////////////////////////////////////
 * @author	:john
 * @time	:17/5/2014
 * @function:数据库作为网络通信第三方时,利用这个类作为中介跟数据库交互
 *///////////////////////////////////////////////
package database;

import protocol.MyPack;

public class NetBridgeDatabase {
	
	public NetBridgeDatabase(Database db){
		
		System.out.println("NetBridgeDatabase construct!");
		
	}
	public int setPackage(MyPack pack){
		
		System.out.println("NetBridgeDatabase setPackage!");
		
		return 0;
	}
	public int getPackage(MyPack pack){
		
		System.out.println("NetBridgeDatabase getPackage!");
		
		return 0;
	}
}
