package com.example.secureApplication.controller;

import com.example.secureApplication.model.Contact;
import com.example.secureApplication.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.Random;

@RestController
@RequiredArgsConstructor
public class ContactController {
    private final ContactRepository contactRepository;
    @PostMapping("/contact ")
    public Contact contactPage(@RequestBody Contact contact){
        contact.setContactId(getServiceReqNumber());
        contact.setCreateDt(new Date(System.currentTimeMillis()));
        return contactRepository.save(contact);
    }
    public String getServiceReqNumber(){
        Random random = new Random();
        int randomNum = random.nextInt(999999999 -9999)+9999;
        return "SR" + randomNum;

    }
}
