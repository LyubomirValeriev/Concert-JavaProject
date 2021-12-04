package com.concert.concertApp.repositories;

import com.concert.concertApp.entities.Performer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PerformerRepository extends JpaRepository<Performer, Long> {
    Performer findPerformerByName(String performerName);

    @Query("SELECT p FROM Performer p " +
           "WHERE lower(p.name) LIKE :#{#name + '%'} ")
    Optional<Performer> fetchPerformerLikeName(String name);
}
