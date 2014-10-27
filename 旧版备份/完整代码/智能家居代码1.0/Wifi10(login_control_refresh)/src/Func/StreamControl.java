package Func;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class StreamControl {
	OutputStream out;
	InputStream in;
	public StreamControl(OutputStream out, InputStream in){
		this.out = out;
		this.in = in;
	}
	protected int onLoad(){
		System.out.println("登陆");
		//func (从数据库查找ip，返回信息给你)
		return 0;
	}
	protected int onRegister(){
		System.out.println("注册");
		return 0;
	}
	protected int onFlesh(){
		System.out.println("刷新");
		return 0;
	}
	protected int onControl(){
		System.out.println("控制");
		return 0;
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
	public byte[] readByte(){
		byte[] b = new byte[100];
		try {
			this.in.read(b);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return b;
	}
}
