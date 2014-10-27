package net;

public class SimpleSecurityFunc implements Security{
	
	public SimpleSecurityFunc(){
	}
	
	@Override
	public int encrypt() {
		// TODO Auto-generated method stub
		
		System.out.println("SimpleSecurityFunc encrypt!");
		
		return 0;
	}

	@Override
	public int decrypt() {
		// TODO Auto-generated method stub
		
		System.out.println("SimpleSecurityFunc decrypt!");
		
		return 0;
	}
}
