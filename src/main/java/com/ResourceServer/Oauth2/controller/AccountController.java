package com.ResourceServer.Oauth2.controller;



import com.ResourceServer.Oauth2.model.Customer;
import com.ResourceServer.Oauth2.repository.AccountsRepository;
import com.ResourceServer.Oauth2.model.Accounts;
import com.ResourceServer.Oauth2.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@RestController
public class AccountController {
    @Autowired
    private AccountsRepository accountsRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping("/myAccount")
    public Accounts getAccountDetails(@RequestParam String email ) {
        Optional<Customer> optionalCustomer =customerRepository.findByEmail(email);
        if(optionalCustomer.isPresent()){
            Accounts accounts = accountsRepository.findByCustomerId(optionalCustomer.get().getId());
            if (accounts != null ) {
                return accounts;
            }else {
                return null;
            }
        }else{
            return null;
        }


    }
}
