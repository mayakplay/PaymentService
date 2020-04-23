package com.mayakplay.payment.service;

import com.mayakplay.payment.entity.Account;
import com.mayakplay.payment.exception.BalanceChangeException;
import com.mayakplay.payment.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public Account createAccount() {
        return accountRepository.save(new Account(0D));
    }

    @Override
    public boolean existsById(Long accountId) {
        return accountRepository.existsById(accountId);
    }

    @Override
    public double getAccountBalance(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new BalanceChangeException("Account not found"));

        return account.getBalance();
    }

}