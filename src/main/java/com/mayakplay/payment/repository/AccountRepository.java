package com.mayakplay.payment.repository;

import com.mayakplay.payment.entity.Account;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.LockModeType;

public interface AccountRepository extends CrudRepository<Account, Long> {

    @Lock(LockModeType.PESSIMISTIC_READ)
    Account findReadLockedById(Long accountId);

}
