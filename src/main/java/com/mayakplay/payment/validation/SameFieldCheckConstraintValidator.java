package com.mayakplay.payment.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.util.Objects;

public class SameFieldCheckConstraintValidator implements ConstraintValidator<SameFieldCheck, Object> {

    private String firstFieldName;
    private String secondFieldName;

    private boolean mustBeSame;

    @Override
    public void initialize(final SameFieldCheck constraintAnnotation) {
        firstFieldName = constraintAnnotation.firstFieldName();
        secondFieldName = constraintAnnotation.secondFieldName();
        mustBeSame = constraintAnnotation.mustBeSame();
    }

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        try {
            final Object firstObj = getProperty(value, firstFieldName);
            final Object secondObj = getProperty(value, secondFieldName);

            return Objects.equals(firstObj, secondObj) == mustBeSame;
        } catch (final Exception ignored) {
            return true;
        }
    }

    private Object getProperty(Object value, String firstFieldName) throws ReflectiveOperationException {
        Field declaredField = value.getClass().getDeclaredField(firstFieldName);
        declaredField.setAccessible(true);
        return declaredField.get(value);
    }

}