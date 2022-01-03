package com.concert.concertApp.controllers;

import com.concert.concertApp.entities.Discount;
import com.concert.concertApp.repositories.DiscountRepository;
import org.hibernate.PropertyValueException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("discount")
public class DiscountController {
    @Autowired
    DiscountRepository discountRepository;

    @GetMapping("/fetch")
    public List<Discount> getAllDiscounts() {return discountRepository.findAll();}

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteDiscount(Long id) {
        Optional<Discount> discount = discountRepository.findById(id);
        if (discount.isEmpty()) {
            return ResponseEntity.ok("Няма такава отстъпка");
        }
        discountRepository.delete(discount.get());
        return ResponseEntity.ok("Отсъпката е изтрита");
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveDiscount( String discountName, Integer discountPercentage,@RequestParam(required = false) Long id) {
        Discount discount = null;
        try {
            discount = discountRepository.findById(id)
                    .orElse(new Discount(discountName,discountPercentage));
            if (discount.getId() == null) {
                discountRepository.save(discount);
            }
            return ResponseEntity.ok("Отстъпката беше запазена успещно!");
        } catch (IllegalArgumentException t) {
            return new ResponseEntity<>("Няма такава отстъпка с такова id", HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }











}


