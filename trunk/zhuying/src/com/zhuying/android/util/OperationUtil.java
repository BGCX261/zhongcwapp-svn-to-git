package com.zhuying.android.util;


public class OperationUtil {
	private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6',
		'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	/**
	 * 将二进制转换成16进制
	 * @param data
	 * @return
	 */
	public static String parseByte2Hex(byte[] data) {
		int l = data.length;

		char[] out = new char[l << 1];

		// two characters form the hex value.
		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
			out[j++] = DIGITS[0x0F & data[i]];
		}

		return new String(out);
	}

/**
 * 16进制转二进制
 * @param hexStr
 * @return
 */
	public static byte[] parseHex2Byte(String hexStr) {

		if (hexStr.length() < 1)

		return null;

		byte[] result = new byte[hexStr.length()/2];

		for (int i = 0;i< hexStr.length()/2; i++) {

		int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);

		int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);

		result[i] = (byte) (high * 16 + low);

		}

		return result;

		}
	public static byte[] parseHex2Byte(String[] hexStr) {
   if(hexStr!=null &&hexStr.length>0){
	   byte[] result = new byte[hexStr.length];
	   for(int i=0;i<hexStr.length;i++){
		  // byte[] tmp=OperationUtil.int16ToByte(Integer.parseInt(hexStr[i].substring(2), 16));
		   //System.arraycopy(tmp,0,result,0,tmp.length); //字符数组1 拷贝到 字符数组3中
		   result[i]=(byte)(Integer.parseInt(hexStr[i].substring(2), 16));
		  
	   }
	   return result;
   }
   return null;
		}
	/**
	 * 将整型转为8位byte
	 * 
	 * @param int
	 * @return byte[]
	 */
	public byte[] int8ToByte(int i) {
		byte[] b = new byte[1];
		b[0] = (byte) i;
		return b;
	}

	/**
	 * 将整型转为16位byte
	 * 
	 * @param int
	 * @return byte[]
	 */
	public static byte[] int16ToByte(int i) {
		byte[] b = new byte[2];
		b[0] = (byte) ((i >> 8) & 0xff);
		b[1] = (byte) (i & 0xff);
		return b;
	}

	/**
	 * 将整型转为24位byte
	 * 
	 * @param int
	 * @return byte[]
	 */
	public byte[] int24ToByte(int i) {
		byte[] b = new byte[3];
		b[0] = (byte) ((i >> 16) & 0xff);
		b[1] = (byte) ((i >> 8) & 0xff);
		b[2] = (byte) (i & 0xff);

		return b;
	}

	/**
	 * 将整型转为32位byte
	 * 
	 * @param int
	 * @return byte[]
	 */
	public byte[] int32ToByte(int i) {
		byte[] b = new byte[4];
		b[0] = (byte) ((i >>> 24) & 0xff);
		b[1] = (byte) ((i >>> 16) & 0xff);
		b[2] = (byte) ((i >>> 8) & 0xff);
		b[3] = (byte) (i & 0xff);
		return b;
	}
	/**
	 * 将8位byte转为整型
	 * 
	 * @param byte[]，int
	 * @return int
	 */
	public int byteToInt8(byte[] b, int begin) {
		int i = b[begin];
		return i;
	}

	/**
	 * 将16位byte转为整型
	 * 
	 * @param byte[]，int
	 * @return int
	 */
	public int byteToInt16(byte[] b, int begin) {
		int i = ((b[begin] & 0xff) << 8) + (b[begin + 1] & 0xff);
		return i;
	}

	/**
	 * 将24位byte转为整型
	 * 
	 * @param byte[]，int
	 * @return int
	 */
	public int byteToInt24(byte[] b, int begin) {
		int i = (((b[begin] & 0xff) << 16)) + ((b[begin + 1] & 0xff) << 8)
				+ (b[begin + 2] & 0xff);
		return i;
	}

	/**
	 * 将32位byte转为整型
	 * 
	 * @param byte[]，int
	 * @return int
	 */
	public int byteToInt32(byte[] b, int begin) {
		int i = (((b[begin] & 0xff) << 24)) + (((b[begin + 1] & 0xff) << 16))
				+ ((b[begin + 2] & 0xff) << 8) + (b[begin + 3] & 0xff);
		return i;
	}

}
