package com.example.secureApplication.controller;

import com.example.secureApplication.model.Loans;
import com.example.secureApplication.repository.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LoansController {
    private final LoanRepository loanRepository;
    @GetMapping("/loans")
    public List<Loans> loanPage(@RequestParam long customerId){
        List<Loans> myLoans = loanRepository.findByCustomerIdOrderByStartDtDesc(customerId);
        if (myLoans == null) return null;
        else return myLoans;
    }
}
