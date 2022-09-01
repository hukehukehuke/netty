package com.huke.validation;

import com.huke.annotation.ValidEmail;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;

/**
 * @author huke
 * @date 2022/08/26/下午3:47
 */
public class EmailValidator implements ConstraintValidator<ValidEmail,String> {

    @Override
    public void initialize(ValidEmail constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return false;
    }
    private boolean validEmail(final String email){

        return false;
    }
}
