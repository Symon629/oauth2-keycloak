package com.ResourceServer.Oauth2.controller;

import com.ResourceServer.Oauth2.model.Customer;
import com.ResourceServer.Oauth2.repository.CardsRepository;
import com.ResourceServer.Oauth2.model.Cards;
import com.ResourceServer.Oauth2.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class CardsController {
    @Autowired
    private CardsRepository cardsRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping("/myCards")
    public List<Cards> getCardDetails(@RequestParam String email) {

        Optional<Customer> customerOptional = customerRepository.findByEmail(email);
        if(customerOptional.isPresent()){
            List<Cards> cards = cardsRepository.findByCustomerId(customerOptional.get().getId());
            if (cards != null ) {
                return cards;
            }else {
                return null;
            }
        }else{
            return null;
        }

    }
}
