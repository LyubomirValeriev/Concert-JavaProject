package com.concert.concertApp.entities;

import org.springframework.http.ResponseEntity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "discount")
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "discount_user_age")
    private String discountName;

    @Column(name = "discount_percent")
    private Integer discountPercentage;

    @OneToMany(mappedBy = "discount")
    private Set<Reservation> reservation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDiscountName() {
        return discountName;
    }

    public void setDiscountName(String discountName) {
        this.discountName = discountName;
    }

    public Integer getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(Integer discountPercentage) {

        if(discountPercentage < 0 && discountPercentage > 100) {
            throw new IllegalArgumentException("Невалидна отстъпка!");
        }

        this.discountPercentage = discountPercentage;
    }

    public Set<Reservation> getReservation() {
        return reservation;
    }

    public void setReservation(Set<Reservation> reservation) {
        this.reservation = reservation;
    }

    public Discount(String discountName, Integer discountPercentage) {
        this.discountName = discountName;
        this.discountPercentage = discountPercentage;
    }

    public Discount() {
    }

}
