package com.concert.concertApp.repositories;

import com.concert.concertApp.entities.Concert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConcertRepository extends JpaRepository<Concert, Long> {
}