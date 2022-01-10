package com.concert.concertApp.repositories;

import com.concert.concertApp.entities.ConcertHall;
import com.concert.concertApp.entities.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DiscountRepository extends JpaRepository<Discount, Long> {

    @Query("SELECT d " +
            "FROM Discount d " +
            "WHERE LOWER(d.discountName)" +
            " LIKE :#{#name == null || #name.isEmpty()? '%' : #name+'$'} " +
            "AND d.discountPercentage =:percent ")
    Discount filterDiscoun(String name, String percent);

    @Query("SELECT u FROM Discount u WHERE lower(u.discountName) = :name" )
    Discount findDiscountByName(String name);

    @Query("SELECT u FROM Discount u WHERE u.Id = :id" )
    Discount findDiscountById(Long id);
}
