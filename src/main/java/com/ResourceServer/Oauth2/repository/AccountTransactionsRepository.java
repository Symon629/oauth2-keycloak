package com.ResourceServer.Oauth2.repository;


import com.ResourceServer.Oauth2.model.AccountsTransactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountTransactionsRepository extends JpaRepository<AccountsTransactions,Long> {
    List<AccountsTransactions> findByCustomerIdOrderByTransactionDtDesc(int customerId);
}
