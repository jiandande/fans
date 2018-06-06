package com.jiandande.review.util;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.security.Key;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import javax.crypto.Cipher;

import android.content.Context;

public class RsaDesHelper {
  
	private static RsaDesHelper helper;
	private static RSAPrivateKey privateKey;
	
	Context mContext;
	public static RsaDesHelper getInstance(Context context){
		if(helper==null){
			helper=new RsaDesHelper();
		}
		helper.mContext=context;
		return helper;
	}
	
	public static void main(String[] args) throws Exception {
		String str = "1397033839788";
		System.out.println("原文：" + str);

		RsaDesHelper rsa = new RsaDesHelper();
		RSAPrivateKey privateKey = (RSAPrivateKey) rsa.readFromFile("sk.dat");
		RSAPublicKey  publickKey = (RSAPublicKey) rsa.readFromFile("pk.dat");

		byte[] encbyte = rsa.encrypt(str, privateKey);
		System.out.println("私钥加密后：");
		String encStr = toHexString(encbyte);
		System.out.println(encStr);

		byte[] signBytes = rsa.sign(str, privateKey);
		System.out.println("签名值：");
		String signStr = toHexString(signBytes);
		System.out.println(signStr);

		byte[] decByte = rsa.decrypt(encStr, publickKey);
		System.out.println("公钥解密后：");
		String decStr = new String(decByte);
		System.out.println(decStr);
//
//		if (rsa.verifySign(str, signStr, publickKey)) {
//			System.out.println("rsa sign check success");
//		} else {
//			System.out.println("rsa sign check failure");
//		}
	}

	
	
	/**
	 * 加密,key可以是公钥，也可以是私钥
	 * 
	 * @param message
	 * @return
	 * @throws Exception
	 */
	public String encrypt(String message) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		if(privateKey==null){
			privateKey=(RSAPrivateKey)readFromAssert();
		}
		cipher.init(Cipher.ENCRYPT_MODE, privateKey);
		byte[]data= cipher.doFinal(message.getBytes());
		return toHexString(data);
	}
	/**
	 * 用私钥签名
	 * 
	 * @param message
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public String sign(String message) throws Exception {
		Signature signetcheck = Signature.getInstance("MD5withRSA");
		if(privateKey==null){
			privateKey=(RSAPrivateKey)readFromAssert();
		}
		signetcheck.initSign(privateKey);
		signetcheck.update(message.getBytes("utf-8"));
		byte[]data= signetcheck.sign();
		return toHexString(data);
	}

	/**
	 * 加密,key可以是公钥，也可以是私钥
	 * 
	 * @param message
	 * @return
	 * @throws Exception
	 */
	private byte[] encrypt(String message, Key key) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		return cipher.doFinal(message.getBytes());
	}

	/**
	 * 解密，key可以是公钥，也可以是私钥，如果是公钥加密就用私钥解密，反之亦然
	 * 
	 * @param message
	 * @return
	 * @throws Exception
	 */
	public byte[] decrypt(String message, Key key) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, key);
		return cipher.doFinal(toBytes(message));
	}

	/**
	 * 用私钥签名
	 * 
	 * @param message
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public byte[] sign(String message, PrivateKey key) throws Exception {
		Signature signetcheck = Signature.getInstance("MD5withRSA");
		signetcheck.initSign(key);
		signetcheck.update(message.getBytes("utf-8"));
		return signetcheck.sign();
	}

	private RSAPrivateKey readFromAssert() {
		Object obj=null;
		ObjectInputStream input=null;
		try {
			InputStream inputStream = mContext.getAssets().open("sk.dat");
			input = new ObjectInputStream(inputStream);
			obj = input.readObject();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(input!=null){
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		 
		
		return (RSAPrivateKey)obj;
	}

	/**
	 * 从文件读取object
	 * 
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	private Object readFromFile(String fileName)  {
		Object obj=null;
		ObjectInputStream input=null;
		try {
			input = new ObjectInputStream(new FileInputStream(
					fileName));
			obj = input.readObject();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(input!=null){
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		 
		
		return obj;
	}

	public static String toHexString(byte[] b) {
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(HEXCHAR[(b[i] & 0xf0) >>> 4]);
			sb.append(HEXCHAR[b[i] & 0x0f]);
		}
		return sb.toString();
	}

	public static final byte[] toBytes(String s) {
		byte[] bytes;
		bytes = new byte[s.length() / 2];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) Integer.parseInt(s.substring(2 * i, 2 * i + 2),
					16);
		}
		return bytes;
	}

	private static char[] HEXCHAR = { '0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
}
