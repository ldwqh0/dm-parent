package com.dm.auth.security;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;

public class AesUtil {

	private static String algorithm = "AES";

	public static String encrypt(String value, String key)
			throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, InvalidKeySpecException,
			NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("ASCII"), algorithm);
		Cipher cipher = Cipher.getInstance(algorithm);
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] cipherBytes = cipher.doFinal(value.getBytes("UTF-8"));
		return Base64.getEncoder().encodeToString(cipherBytes);
	}

	public static String decrypt(String value, String key) throws InvalidKeyException, UnsupportedEncodingException,
			NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException,
			BadPaddingException, DecoderException {
		SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("ASCII"), algorithm);
		byte[] cipherBytes = Base64.getDecoder().decode(value);
		Cipher cipher = Cipher.getInstance(algorithm);
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		return new String(cipher.doFinal(cipherBytes), "UTF-8");
	}

}
