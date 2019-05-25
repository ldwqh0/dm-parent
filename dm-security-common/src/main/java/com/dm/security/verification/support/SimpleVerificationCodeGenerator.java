package com.dm.security.verification.support;

import java.util.Random;
import java.util.UUID;

import com.dm.security.verification.VerificationCode;
import com.dm.security.verification.VerificationCodeGenerator;

public class SimpleVerificationCodeGenerator implements VerificationCodeGenerator {

	private static final char[] characters = new char[] { 'a', 'b', 'c', 'c', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'm',
			'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8',
			'9' };

	@Override
	public VerificationCode generate(int length) {
		String id = UUID.randomUUID().toString();
		Random random = new Random();
		int count = characters.length;
		char[] result = new char[length];
		for (int i = 0; i < length; i++) {
			result[i] = characters[random.nextInt(count)];
		}
		return new VerificationCode(id, new String(result));
	}

}
