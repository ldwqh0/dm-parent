package com.dm.security.verification.support;

import com.dm.security.verification.VerificationCode;
import com.dm.security.verification.VerificationCodeGenerator;
import com.google.code.kaptcha.Producer;

public class KaptchaCodeGenerator implements VerificationCodeGenerator {

    private final Producer produce;

    public KaptchaCodeGenerator(Producer produce) {
        this.produce = produce;
    }

    @Override
    public VerificationCode generate(int i) {

        produce.createText();
        // TODO Auto-generated method stub
        return null;
    }

}
