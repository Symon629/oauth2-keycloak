package com.ResourceServer.Oauth2.controller;

import com.ResourceServer.Oauth2.repository.ContactRepository;
import com.ResourceServer.Oauth2.model.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.List;
import java.util.Random;

@RestController
public class ContactController {
    @Autowired
    private ContactRepository contactRepository;

    @PostMapping("/contact")
    // The pre filter runs before actual method
    // in th case below it will run for everything except when then contact name is Test
    // Now rememeber everything has to be of type collection when using the preFilter()
    @PreFilter("filterObject.contactName !='Test'")
    public Contact saveContactInquiryDetails(@RequestBody List<Contact> contacts) {
        Contact contact = contacts.get(0);
        contact.setContactId(getServiceReqNumber());
        contact.setCreateDt(new Date(System.currentTimeMillis()));
        return contactRepository.save(contact);
    }

    // Ther eis also a postFilter() that only applies to return object
    // that is in the code below if the contactName is test it will not return it to the user/


//    @PostFilter("filterObject.contactName !='Test'")
//    public List<contact> saveContactInquiryDetails(@RequestBody Contact contacts) {
//        contact.setContactId(getServiceReqNumber());
//        contact.setCreateDt(new Date(System.currentTimeMillis()));
//        Contact contactFromdb = contactRepository.save(contact);
//        List<Contact> contactsreponse = new ArrayList<>();
//        contactsreponse.add(contactFromdb );
//        reutrn contactsreponse;
//
//    }

    public String getServiceReqNumber() {
        Random random = new Random();
        int ranNum = random.nextInt(999999999 - 9999) + 9999;
        return "SR"+ranNum;
    }
}
