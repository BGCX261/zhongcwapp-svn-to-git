package com.zhuying.android.util;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Vector;

import com.zhuying.android.service.LoginService;



/**
 * 密码加密
 * 
 * @author Administrator
 * 
 */
public class MobiEncrptionUtil {
	
	private static RSAEncryption encryption = RSAEncryption.getInstance(MobiEncrptionUtil.getPublicKey(LoginService.MOBISECRETPUBLIC_MODULUS,LoginService.MOBISECRETPUBLIC_EXPONENT),MobiEncrptionUtil.getPrivateKey(LoginService.MOBISECRETPUBLIC_MODULUS,LoginService.MOBISECRETPUBLIC_EXPONENT));

	/**
	 * 加密
	 * @param text
	 * @return
	 */
	public static String Encrypt(String text) {
		byte[] data;
		try {
			data = encryption.Encrypt(text.getBytes());
			return OperationUtil.parseByte2Hex(data);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 解密
	 * @param text
	 * @return
	 */
	public static String Decrypt(String text) {
		byte[] data;
		try {
			data = encryption.Decrypt(OperationUtil.parseHex2Byte(text));
			StringBuffer sbf=new StringBuffer();
			for(int i=0;i<data.length;i++){
				if(data[i]!=0){
					if(sbf.length()==0)
						sbf.append(data[i]);
					else
						sbf.append(","+data[i]);
				}
				else
					break;
			}
			String[] s=sbf.toString().split(",");
			byte[] b1= new byte[s.length];
			for(int j=0;j<s.length;j++){
				b1[j]=data[j];
			}
			return new String(b1,"UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static PublicKey getPublicKey(String modulus,String exponent){
		RSAPublicKeySpec keySpec=new RSAPublicKeySpec(new BigInteger(modulus),new BigInteger(exponent));
		KeyFactory keyFactory=null;
		try {
			keyFactory = KeyFactory.getInstance("RSA");
			PublicKey key=keyFactory.generatePublic(keySpec);
			return key;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return null;
	}
	private static PrivateKey getPrivateKey(String modulus,String exponent){
		RSAPrivateKeySpec keySpec=new RSAPrivateKeySpec(new BigInteger(modulus),new BigInteger(exponent));
		KeyFactory keyFactory=null;
		try {
			keyFactory = KeyFactory.getInstance("RSA");//"RSA/ECB/NoPadding"
			PrivateKey key=keyFactory.generatePrivate(keySpec);
			return key;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
