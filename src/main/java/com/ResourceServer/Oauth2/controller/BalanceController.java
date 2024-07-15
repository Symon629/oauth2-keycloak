package com.ResourceServer.Oauth2.controller;

import com.ResourceServer.Oauth2.model.Customer;
import com.ResourceServer.Oauth2.repository.AccountTransactionsRepository;
import com.ResourceServer.Oauth2.model.AccountsTransactions;
import com.ResourceServer.Oauth2.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class BalanceController {
    @Autowired
    private AccountTransactionsRepository accountTransactionsRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping("/myBalance")
    public List<AccountsTransactions> getBalanceDetails(@RequestParam String email) {

        Optional<Customer> optionalCustomer = customerRepository.findByEmail(email);

        if(optionalCustomer.isPresent()){
            List<AccountsTransactions> accountTransactions = accountTransactionsRepository.
                    findByCustomerIdOrderByTransactionDtDesc(optionalCustomer.get().getId());
            if (accountTransactions != null ) {
                return accountTransactions;
            }else {
                return null;
            }

        }else{
            return  null;
        }



    }
}
