package com.dm;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.DecoderException;

import com.dm.auth.security.AesUtil;

public class TtTest {

	public static void main(String[] args)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException,
			BadPaddingException, InvalidKeySpecException, UnsupportedEncodingException, DecoderException {
		System.out.println(AesUtil.encrypt("2019-12-21T00:00:00.000+08:00", "我都不知道我要输入一段什么样的文本来加密这个内容"));
	}

}
