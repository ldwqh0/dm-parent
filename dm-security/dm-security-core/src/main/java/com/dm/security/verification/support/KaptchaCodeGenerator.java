
package com.dm.security.verification.support;

import org.springframework.beans.factory.annotation.Autowired;

import com.dm.security.verification.VerificationCode;
import com.dm.security.verification.VerificationCodeGenerator;
import com.google.code.kaptcha.Producer;

public class KaptchaCodeGenerator implements VerificationCodeGenerator {

    @Autowired
    private Producer produce;

    @Override
    public VerificationCode generate(int i) {

        produce.createText();
        // TODO Auto-generated method stub
        return null;
    }

}
