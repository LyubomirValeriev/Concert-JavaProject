package com.concert.concertApp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.concert.concertApp.entities.Discount;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
}
