/*///////////////////////////////////////////////
 * @author	:john
 * @time	:17/5/2014
 * @function:网络传输必须的一些操作函数
 *///////////////////////////////////////////////
package net;
import protocol.MyPack;

public interface Transmit {
	public int send();					//send mypack in net.
	public int receive();				//receive mypack in net.
	public int setBuffer(MyPack pack);		//set pack to buffer.
	public int getBuffer(MyPack pack);		//get pack from buffer.
}
