package com.mayakplay.payment.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@SuppressWarnings("unused")
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AccountExistsByIdConstraintValidator.class)
public @interface AccountExistsById {

    String message() default "Аккаунт не существует";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}
