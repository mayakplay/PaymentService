package com.mayakplay.payment.filter;

import com.mayakplay.payment.validation.AccountExistsById;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class AccountBalanceChangeFilter {

    @NotNull
    @AccountExistsById
    private Long accountId;

    @NotNull
    @Positive
    private Double amount;

}
