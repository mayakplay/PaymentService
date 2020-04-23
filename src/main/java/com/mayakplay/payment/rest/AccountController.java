package com.mayakplay.payment.rest;

import com.mayakplay.payment.filter.AccountBalanceChangeFilter;
import com.mayakplay.payment.filter.AccountBalanceTransferFilter;
import com.mayakplay.payment.model.AccountIdModel;
import com.mayakplay.payment.model.BalanceModel;
import com.mayakplay.payment.service.AccountOperationService;
import com.mayakplay.payment.service.AccountService;
import com.mayakplay.payment.validation.AccountExistsById;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("account")
public class AccountController {

    private final AccountService accountService;
    private final AccountOperationService accountOperationService;

    @GetMapping("balance")
    public BalanceModel balance(@RequestParam @NotNull @AccountExistsById Long accountId) {
        return new BalanceModel(accountService.getAccountBalance(accountId));
    }

    @PostMapping("create")
    public AccountIdModel create() {
        return new AccountIdModel(accountService.createAccount().getId());
    }

    @PostMapping("withdraw")
    public void withdraw(@Valid @RequestBody AccountBalanceChangeFilter filter) {
        accountOperationService.withdraw(filter.getAccountId(), filter.getAmount());
    }

    @PostMapping("deposit")
    public void deposit(@Valid @RequestBody AccountBalanceChangeFilter filter) {
        accountOperationService.deposit(filter.getAccountId(), filter.getAmount());
    }

    @PostMapping("transfer")
    public void transfer(@Valid @RequestBody AccountBalanceTransferFilter filter) {
        accountOperationService.transfer(filter.getAccountIdFrom(), filter.getAccountIdTo(), filter.getAmount());
    }

}
