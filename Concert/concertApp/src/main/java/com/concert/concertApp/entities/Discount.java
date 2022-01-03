package com.concert.concertApp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.engine.query.ParameterRecognitionException;
import org.hibernate.procedure.ParameterMisuseException;

import javax.persistence.*;

import static com.concert.concertApp.entities.ConcertHall.isValidNumber;

@Entity
@Table(name = "discounts")
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long Id ;

    @Column(name = "discount_name", length =  40,nullable = false)
    private  String discountName ;

    @Column(name = " discount_ercentage", length =  3, nullable = false)
    private  String discountPercentage ;

    public Discount() {
    }

    public Discount(String discountName, String discountPercentage) {
      this.setDiscountName(discountName);
      this.setDiscountPercentage(discountPercentage);
    }

    public Long getId() {
        return Id;
    }

    public String getDiscountName() {
        return discountName;
    }

    public void setDiscountName(String discountName) {
        if(discountName == null ||
        discountName.isEmpty())
           throw  new  NullPointerException("Моля въвете име на отсктъпката");

        boolean ok = discountName.chars().allMatch(Character::isLetter);
        if(!ok)
            throw new ParameterRecognitionException("Моля провелете името на отстъпката отново :)") ;
        this.discountName = discountName;
    }

    public String getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(String discountPercentage) {
        Integer percent = 0 ;
        if(isValidNumber(discountPercentage) == false ){
            throw  new ParameterMisuseException("Моля въведете само числа за отстъпката!") ;
        }
        else {
            percent = Integer.parseInt(discountPercentage);
        }
        if(percent <= 0){
            throw  new ParameterMisuseException("Отстъпката не може да бъде отрицателно число!");

        }else if(percent != percent.intValue() ){
            //  conHallCapacity != (int)conHallCapacity
//            conHallCapacity != conHallCapacity.intValue()
            throw  new ParameterMisuseException("Отстъпката трябва да е цяло число!");
        }else if(percent >100)
            throw  new ParameterMisuseException("Отстъпката не може да е число по-голямо от 100, иначе ние трябва да ви платим :_( ");
        this.discountPercentage = discountPercentage;
    }
}
