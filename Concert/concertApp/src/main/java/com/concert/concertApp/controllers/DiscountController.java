package com.concert.concertApp.controllers;

import com.concert.concertApp.entities.Discount;
import com.concert.concertApp.repositories.DiscountRepository;
import org.hibernate.engine.query.ParameterRecognitionException;
import org.hibernate.procedure.ParameterMisuseException;
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
            return ResponseEntity.ok("Enter a title to create a discount!");
        if (percent == null ||
                percent.isEmpty())
            return ResponseEntity.ok("Enter a percent to create a discount!!");

        Discount discountInDb = discountRepo.filterDiscoun(name, percent);
        if (discountInDb != null)
            return ResponseEntity.ok("Discount with this name and percent already exist");

        discountInDb = discountRepo.findDiscountByName(name.toLowerCase(Locale.ROOT));
        if (discountInDb != null)
            return ResponseEntity.ok("Discount with this name already exist!");

        try {
            discountInDb = new Discount(
                    name,
                    percent
            );
            discountRepo.save(discountInDb);
            return  ResponseEntity.ok("Discount saved successfully  <3") ;
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.ok("It is mandatory to fill all discount fields correctly. Check your personal data!");
        }catch (ParameterMisuseException w) {        // проверка за невалидни данни
                return  ResponseEntity.ok( w.getMessage());}
        catch (ParameterRecognitionException e) {        // проверка за невалидни данни
            return  ResponseEntity.ok( e.getMessage());
        }
        catch (NullPointerException e ){                        // проверка за невалидни данни
            return  ResponseEntity.ok(e.getMessage());
        }
        catch (Exception e ){
            return ResponseEntity.ok("Something went wrong, try again <3");
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteDiscount(String name) {
        Discount discountInDb = discountRepo.findDiscountByName(name.toLowerCase(Locale.ROOT));
        if (discountInDb == null)
            return ResponseEntity.ok("Discount not found :_(");

        try {
            discountRepo.delete(discountInDb);
            return ResponseEntity.ok("Discount with name :" + discountInDb.getDiscountName() + " was deleted");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.ok("The Discount can not be deleted as it is part of a some reservation");
        } catch (Exception e) {
            return ResponseEntity.ok("Something went wrong, try again <3");
        }
}
}
