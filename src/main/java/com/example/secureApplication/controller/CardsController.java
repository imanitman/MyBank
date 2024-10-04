package com.example.secureApplication.controller;

import com.example.secureApplication.model.Cards;
import com.example.secureApplication.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CardsController {
    private final CardRepository cardRepository;
    @GetMapping("/cards")
    public List<Cards> cardPage(@RequestParam long customerId){
        List<Cards> cards = cardRepository.findByCustomerId(customerId);
        if (cards == null) return null;
        else return cards;
    }
}
