package main;

import protocol.MyPack;
import net.Transmit;

public class NetThread implements Runnable{
	private Transmit transmit;
	
	public NetThread(Transmit transmit){
		
		this.transmit = transmit;
		
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		int i = 0;
		while (i<10){
			MyPack pack = new MyPack();
			this.send(pack);
			i++;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public synchronized int send(MyPack pack){
		
		transmit.setBuffer(pack);		
		transmit.send();
		return 0;
	}
	
	public synchronized int receive(MyPack pack){
		
		transmit.receive();
		transmit.getBuffer(pack);
		return 0;
	}
}
