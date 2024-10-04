package com.example.secureApplication.repository;

import com.example.secureApplication.model.Cards;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends CrudRepository<Cards,Long> {
    List<Cards> findByCustomerId(long customerId);
}
