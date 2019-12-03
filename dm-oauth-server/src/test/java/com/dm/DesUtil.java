package com.dm;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class DesUtil {

	private static String algorithm = "DES";

	public static String encrypt(String value, String key)
			throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, InvalidKeySpecException,
			NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);
		DESKeySpec dks = new DESKeySpec(key.getBytes("UTF-8"));
		SecretKey sk = keyFactory.generateSecret(dks);
		Cipher c = Cipher.getInstance(algorithm);
		c.init(Cipher.ENCRYPT_MODE, sk);
		byte[] b = c.doFinal(value.getBytes("UTF-8"));
		return Hex.encodeHexString(b);
	}

	public static String decrypt(String value, String key) throws InvalidKeyException, UnsupportedEncodingException,
			NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException,
			BadPaddingException, DecoderException {
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);
		DESKeySpec dks = new DESKeySpec(key.getBytes("UTF-8"));
		SecretKey sk = keyFactory.generateSecret(dks);
		Cipher c = Cipher.getInstance(algorithm);
		c.init(Cipher.DECRYPT_MODE, sk);
		byte[] b = c.doFinal(Hex.decodeHex(value));
		return new String(b, "UTF-8");
	}

}
