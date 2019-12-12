package com.dm;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.DecoderException;

public class TtTest {
	private static final String algorithm = "DES";

	private static final byte[] securityKey = "rereabewrewrewrwerc".getBytes();

	public static void main(String[] args)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException,
			BadPaddingException, InvalidKeySpecException, UnsupportedEncodingException, DecoderException {
		System.out.println(DesUtil.encrypt("20192124", "adgas5555555555555dg"));

		System.out.println(DesUtil.decrypt("14b672b2d334af2333cadf68a2d21b06", "adgas5555555555555dg"));
		;
	}

}
