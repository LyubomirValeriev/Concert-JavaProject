package com.concert.concertApp.repositories;

import com.concert.concertApp.entities.Performer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerformerRepository extends JpaRepository<Performer, Long> {
    Performer findPerformerByName(String performerName);
}
