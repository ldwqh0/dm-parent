package com.dm.common.validation;

import com.dm.common.validation.constraints.Mobile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MobileValidator implements ConstraintValidator<Mobile, Object> {
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        // TODO 待实现
        return true;
    }
}
