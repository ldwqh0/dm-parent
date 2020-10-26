package com.dm.common.validation;

import com.dm.common.validation.constraints.NotIn;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotInValidator implements ConstraintValidator<NotIn, Object> {
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        // TODO 待处理
        return true;
    }

    @Override
    public void initialize(NotIn constraintAnnotation) {
        constraintAnnotation.value();
    }
}
