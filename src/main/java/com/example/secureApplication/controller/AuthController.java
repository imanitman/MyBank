package com.example.secureApplication.controller;

import com.example.secureApplication.model.Customer;
import com.example.secureApplication.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<String> reagisterPage (@RequestBody  Customer customer){
        try {
            if (customer != null){
                customer.setPwd(passwordEncoder.encode(customer.getPwd()));
                customer.setCreateDt(new Date(System.currentTimeMillis()));
                customerRepository.save(customer);
                return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
            }
            else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exists");
            }
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error occurred here");
        }
    }
    @GetMapping("/user")
    public Customer getUserDetailAfterLogin(Authentication authentication){
        String email = authentication.getName();
        Optional<Customer> optionalCustomer = customerRepository.findByEmail(email);
        return optionalCustomer.orElse(null);
    }
}
