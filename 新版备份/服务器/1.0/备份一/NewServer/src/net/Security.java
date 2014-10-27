/*///////////////////////////////////////////////
 * @author	:john
 * @time	:17/5/2014
 * @function:数据加密解密
 *///////////////////////////////////////////////
package net;

public interface Security {
	public int encrypt();				//encrypt mypack for security transmit
	public int decrypt();				//decrypt mypack for use
}
