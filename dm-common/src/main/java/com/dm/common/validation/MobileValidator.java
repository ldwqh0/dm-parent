package com.dm.common.validation;

import com.dm.common.validation.constraints.Mobile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * 移动电话校验实现
 */
public class MobileValidator implements ConstraintValidator<Mobile, String> {

    private Pattern pattern = null;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || value.length() == 0 || pattern.matcher(value).matches();
    }

    @Override
    public void initialize(Mobile annotation) {
        this.pattern = Pattern.compile(annotation.regexp());
    }
}
