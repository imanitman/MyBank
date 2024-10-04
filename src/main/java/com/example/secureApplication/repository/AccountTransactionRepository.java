package com.example.secureApplication.repository;

import com.example.secureApplication.model.AccountTransactions;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountTransactionRepository extends CrudRepository<AccountTransactions, Long> {
    List<AccountTransactions> findByCustomerIdOrderByTransactionDtDesc(long customerId);
}
