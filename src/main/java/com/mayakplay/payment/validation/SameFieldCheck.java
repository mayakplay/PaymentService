package com.mayakplay.payment.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@SuppressWarnings("unused")
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SameFieldCheckConstraintValidator.class)
public @interface SameFieldCheck {

    String firstFieldName();

    String secondFieldName();

    boolean mustBeSame() default false;

    String message() default "Поля не должны совпадать";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
