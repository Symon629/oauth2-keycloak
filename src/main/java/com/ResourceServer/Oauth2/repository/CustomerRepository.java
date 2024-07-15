package com.ResourceServer.Oauth2.repository;


import com.ResourceServer.Oauth2.model.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends CrudRepository<Customer,Long> {
    Optional<Customer> findByEmail(String email);
}
