package com.concert.concertApp.repositories;

import com.concert.concertApp.entities.Concert;
import com.concert.concertApp.entities.Performer;
import com.concert.concertApp.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ConcertRepository extends JpaRepository<Concert, Long> {
    @Query("SELECT c.title, max(c.description), max(c.price), max(c.date), max(p.name) " +
            "FROM Concert c JOIN c.performers p " +
            "WHERE lower(c.title) " +
            "LIKE :#{#concertTitle.isEmpty()? '%' : #concertTitle + '%'} " +
            "AND lower(p.name) " +
            "LIKE :#{#performerName.isEmpty()? '%' : #performerName + '%'} " +
            "GROUP BY c.title, c.price " +
            "ORDER BY c.price ASC")
     Page<Concert> filterConcert(Pageable pageable, String concertTitle, String performerName);


    @Query("SELECT c FROM Concert  c WHERE lower(c.title) LIKE :#{#title + '%'}")
    Concert fetchConcertLikeTitle(String title);

    Concert findConcertByTitle(String title);

}