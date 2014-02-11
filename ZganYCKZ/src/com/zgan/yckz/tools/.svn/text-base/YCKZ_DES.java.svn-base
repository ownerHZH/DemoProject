package com.zgan.yckz.tools;

import java.security.InvalidParameterException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;

public class YCKZ_DES {
	private static byte[] iv = { 1, 2, 3, 4, 5, 6, 7, 8 };

	/**
	 * º”√‹
	 * 
	 * @param encryptString
	 * @param encryptKey
	 * @return 
	 * @throws Exception
	 */
	public static String encryptDES(String encryptString, String encryptKey)
			throws Exception {
		// IvParameterSpec zeroIv = new IvParameterSpec(new byte[8]);
		IvParameterSpec zeroIv = new IvParameterSpec(iv);
		SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes(), "DES");

		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);

		byte[] encrypteData = cipher.doFinal(encryptString.getBytes());

		return YCKZ_Base64.encode(encrypteData);

	}

	/**
	 * Ω‚√‹
	 * 
	 * @param decryptString
	 * @param decryptKey
	 * @return
	 * @throws Exception
	 */
	public static String decryptDES(String decryptString, String decryptKey)
			throws Exception {
		byte[] byteMi = new YCKZ_Base64().decode(decryptString);
		IvParameterSpec zeroIv = new IvParameterSpec(iv);
		// IvParameterSpec zeroIv = new IvParameterSpec(new byte[8]);

		SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes(), "DES");
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

		cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
		byte[] decryptedData = cipher.doFinal(byteMi);

		return new String(decryptedData);
	}
}
