package com.zhuying.android.util;

import java.io.ByteArrayOutputStream;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;

public class RSAEncryption {
	private PublicKey publicKey = null;
	private PrivateKey privateKey = null;
	private static RSAEncryption encryption;

	private RSAEncryption(PublicKey publicKey,PrivateKey privateKey) {
		this.publicKey = publicKey;
		this.privateKey = privateKey;
	}

	private RSAEncryption() {
	}

	public static RSAEncryption getInstance(PublicKey publicKey,PrivateKey privateKey) {
		if (encryption == null) {
			encryption = new RSAEncryption(publicKey,privateKey);
		}
		return encryption;
	}

	public byte[] Encrypt(byte[] src) throws Exception {
		// 获得一个RSA的Cipher类，使用公钥加密
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		return cipher.doFinal(src,0,src.length);
	}

	public byte[] Decrypt(byte[] src) throws Exception {
		// 用私钥解密
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return cipher.doFinal(src,0,src.length);
		
	}

	
}
