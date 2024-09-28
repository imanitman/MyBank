package com.example.secureApplication.controller;

import com.example.secureApplication.model.Customer;
import com.example.secureApplication.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> reagisterPage (@RequestBody  Customer customer){
        try {
            if (customer != null){
                customer.setPassword(passwordEncoder.encode(customer.getPassword()));
                customerRepository.save(customer);
                return ResponseEntity.ok("User created successfully");
            }
            else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exists");
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error occurred here");
        }
    }
}
