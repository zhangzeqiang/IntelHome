package TypeTools;

public class Tools {
	public Tools(){}
	public static byte[] ipv4Address2BinaryArray(String ipAdd){
		  byte[] binIP = new byte[4];
		  String[] strs = ipAdd.split("\\.");
		  for(int i=0;i<strs.length;i++){
			  binIP[i] = (byte) Integer.parseInt(strs[i]);
		  }
		  return binIP;
	 }
	 public static String binaryArray2Ipv4Address(byte[]addr){
		  String ip="";
		  for(int i=0;i<addr.length;i++){
			  ip+=(addr[i] & 0xFF)+".";
		  }
		  return ip.substring(0, ip.length()-1);
	 }
}
