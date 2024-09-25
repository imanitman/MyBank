package com.example.secureApplication.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BalanceController {
    @GetMapping("/balances")
    public String balancePage(){
        return "This is my balances";
    }
}
