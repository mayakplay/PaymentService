package com.mayakplay.payment.filter;

import com.mayakplay.payment.validation.AccountExistsById;
import com.mayakplay.payment.validation.SameFieldCheck;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@SameFieldCheck(
        firstFieldName = "accountIdFrom",
        secondFieldName = "accountIdTo",
        message = "Невозможно совершить перевод, указан одинаковый счет отправителя и получателя!")
public class AccountBalanceTransferFilter {

    @NotNull
    @AccountExistsById
    private Long accountIdFrom;

    @NotNull
    @AccountExistsById
    private Long accountIdTo;

    @NotNull
    @Positive
    private Double amount;

}
