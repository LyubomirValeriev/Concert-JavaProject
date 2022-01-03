package com.concert.concertApp.controllers;

import com.concert.concertApp.entities.Discount;
import com.concert.concertApp.repositories.DiscountRepository;
import org.hibernate.engine.query.ParameterRecognitionException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/discount")
public class DiscountController {
    private final DiscountRepository discountRepo;

    DiscountController(DiscountRepository discountRepo){
        this.discountRepo = discountRepo ;
    }

    @GetMapping("/fetch")
    public List<Discount> getAllDiscounts(){
        return discountRepo.findAll() ;
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveDiscount(String name,
                                          String percent) {
        if (name == null ||
                name.isEmpty())
            return ResponseEntity.ok("Мoля въведете име!");
        if (percent == null ||
                percent.isEmpty())
            return ResponseEntity.ok("Мoля въведете процент!");

        Discount discountInDb = discountRepo.filterDiscoun(name, percent);
        if (discountInDb != null)
            return ResponseEntity.ok("Вече съществува отстъпка с това име и проценти!");

        discountInDb = discountRepo.findDiscountByName(name.toLowerCase(Locale.ROOT));
        if (discountInDb != null)
            return ResponseEntity.ok("Вече съществува отстъпка с това име!");

        try {
            discountInDb = new Discount(
                    name,
                    percent
            );
            discountRepo.save(discountInDb);
            return  ResponseEntity.ok("Отстъпката беше запазена успешно <3") ;
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.ok("Не сте въвели всички елементи, опитайте отново!");
        }  catch (ParameterRecognitionException e) {
            return  ResponseEntity.ok( e.getMessage());
        }
        catch (NullPointerException e ){
            return  ResponseEntity.ok(e.getMessage());
        }
        catch (Exception e ){
            return ResponseEntity.ok(e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteDiscount(String name) {
        Discount discountInDb = discountRepo.findDiscountByName(name);
        if (discountInDb == null)
            return ResponseEntity.ok("Отстъпка с това име не може да бъде открита :_(");

        try {
            discountRepo.delete(discountInDb);
            return ResponseEntity.ok("Отстъпка с име :" + discountInDb.getDiscountName() + " беше изтрита");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.ok("Не можете да изтриете отстъпката, защото тя е част от някоя резервация");
        } catch (Exception e) {
            return ResponseEntity.ok("Нещо се обърка, опитай пак <3");
        }
}
}
