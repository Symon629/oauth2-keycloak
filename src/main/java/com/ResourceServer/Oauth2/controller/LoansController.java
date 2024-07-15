package com.ResourceServer.Oauth2.controller;


import com.ResourceServer.Oauth2.model.Customer;
import com.ResourceServer.Oauth2.repository.CustomerRepository;
import com.ResourceServer.Oauth2.repository.LoanRepository;
import com.ResourceServer.Oauth2.model.Loans;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class LoansController {
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping("/myLoans")
    public List<Loans> getLoanDetails(@RequestParam String email) {

        Optional<Customer> optionalCustomer = customerRepository.findByEmail(email);
        if(optionalCustomer.isPresent()){
            System.out.println("User name"+optionalCustomer.get().getName());
            List<Loans> loans = loanRepository.findByCustomerIdOrderByStartDtDesc(optionalCustomer.get().getId());
            if (loans != null ) {
                return loans;
            }else {
                return null;
            }
        }else{

            return null;
        }


    }
}
