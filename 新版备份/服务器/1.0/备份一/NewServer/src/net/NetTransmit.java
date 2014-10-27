/*///////////////////////////////////////////////
 * @author	:john
 * @time	:17/5/2014
 * @function:负责数据的发送和接收，以及缓冲的写入和读取
 *///////////////////////////////////////////////
package net;

import protocol.MyPack;


public class NetTransmit implements Transmit{
	Transmit transmit;
	
	public NetTransmit(Transmit transmit){
		
		this.transmit = transmit;
		
	}
	public int setTransmit(Transmit transmit){
		
		this.transmit = transmit;
		return 0;
	}

	@Override
	public int send() {
		// TODO Auto-generated method stub
		this.transmit.send();
		return 0;
	}

	@Override
	public int receive() {
		// TODO Auto-generated method stub
		this.transmit.receive();
		return 0;
	}

	@Override
	public int setBuffer(MyPack pack) {
		// TODO Auto-generated method stub
		this.transmit.setBuffer(pack);
		return 0;
	}

	@Override
	public int getBuffer(MyPack pack) {
		// TODO Auto-generated method stub
		this.transmit.getBuffer(pack);
		return 0;
	}
	
}
