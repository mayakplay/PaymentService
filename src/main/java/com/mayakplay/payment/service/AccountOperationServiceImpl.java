package com.mayakplay.payment.service;


import com.mayakplay.payment.entity.Account;
import com.mayakplay.payment.exception.BalanceChangeException;
import com.mayakplay.payment.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountOperationServiceImpl implements AccountOperationService {

    private final AccountRepository accountRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = BalanceChangeException.class)
    public void withdraw(Long accountId, double amount) {
        increaseBalance(accountId, -amount);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = BalanceChangeException.class)
    public void deposit(Long accountId, double amount) {
        increaseBalance(accountId, amount);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = BalanceChangeException.class)
    public void transfer(Long accountIdFrom, Long accountIdTo, double amount) {
        increaseBalance(accountIdFrom, -amount);
        increaseBalance(accountIdTo, amount);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    protected void increaseBalance(Long accountId, double amount) {

        Account account = accountRepository.findReadLockedById(accountId);

        if (account == null) {
            throw new BalanceChangeException("Account not found");
        }

        double balanceAfter = account.getBalance() + amount;

        if (balanceAfter < 0) {
            throw new BalanceChangeException("Not enough money");
        }

        account.setBalance(balanceAfter);
        accountRepository.save(account);
    }

}
