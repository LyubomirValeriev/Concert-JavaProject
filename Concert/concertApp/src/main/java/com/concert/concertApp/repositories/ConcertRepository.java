package com.concert.concertApp.repositories;

import com.concert.concertApp.entities.Concert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ConcertRepository extends JpaRepository<Concert, Long> {

    @Query("SELECT c.title, c.description, max(c.price), c.date, max(p) " +
            "FROM Concert c JOIN c.performers p " +
            "WHERE lower(c.title) " +
            "LIKE :#{#concertTitle.isEmpty()? '%' : #concertTitle + '%'} " +
            "AND lower(p.name) " +
            "LIKE :#{#performerName.isEmpty()? '%' : #performerName+'%'} " +
            "GROUP BY c.title, c.price " +
            "ORDER BY c.price ASC")
    Page<Concert> fetchConcertByFilter(Pageable pageable, String performerName, String concertTitle);

    @Query("SELECT c FROM Concert  c " +
           "WHERE lower(c.title) LIKE : #{#title + '%'} ")
    Concert fetchConcertLikeTitle(String title);

}