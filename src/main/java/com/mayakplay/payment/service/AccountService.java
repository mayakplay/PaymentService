package com.mayakplay.payment.service;

import com.mayakplay.payment.entity.Account;

public interface AccountService {

    /**
     * @return только-что созданный счет
     */
    Account createAccount();

    /**
     * Проверка существования счета по айди
     *
     * @param accountId айди
     * @return true, если существует
     *         false, если не существует
     */
    boolean existsById(Long accountId);

    /**
     * Получение баланса счета по его айди
     *
     * @param accountId айди
     * @return баланс счета
     */
    double getAccountBalance(Long accountId);

}
