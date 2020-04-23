package com.mayakplay.payment.validation;

import com.mayakplay.payment.service.AccountService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Log4j2
public class AccountExistsByIdConstraintValidator implements ConstraintValidator<AccountExistsById, Long> {

    @Autowired
    private AccountService accountService;

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return accountService.existsById(value);
    }

}
