import DataTransfer.ClientStream;


public class doMain {
	final static int PORT = 20001;
	final static String IP = "127.0.0.1";
	public static void main(String[] args){
		Thread thread = new Thread(new ClientStream(IP, PORT));
		thread.start();
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			System.out.println("退出客户机程序");
		}
	}
}
