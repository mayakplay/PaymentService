package com.mayakplay.payment.service;

public interface AccountOperationService {

    /**
     * Опрерация средств со счета
     *
     * @param accountId счет, с которого необходимо снять средства
     * @param amount сумма операции
     */
    void withdraw(Long accountId, double amount);

    /**
     * Опрерация внесения средств
     *
     * @param accountId счет, на который необходимо положить средства
     * @param amount сумма операции
     */
    void deposit(Long accountId, double amount);

    /**
     * Операция перевода средств со счета на счет
     *
     * @param accountIdFrom счет, С которого нужно перевести средства
     * @param accountIdTo счет, НА который нужно перевести средства
     * @param amount сумма перевода
     */
    void transfer(Long accountIdFrom, Long accountIdTo, double amount);

}
