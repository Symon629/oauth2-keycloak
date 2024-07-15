package com.ResourceServer.Oauth2.repository;


import com.ResourceServer.Oauth2.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface  ContactRepository extends JpaRepository<Contact,Long> {
}
