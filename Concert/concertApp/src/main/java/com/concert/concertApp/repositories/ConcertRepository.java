package com.concert.concertApp.repositories;

import com.concert.concertApp.entities.Concert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ConcertRepository extends JpaRepository<Concert, Long> {
    Concert findConcertByTitle(String title);

    @Query("SELECT c " +
            "FROM Concert c JOIN c.performers p " +
            "WHERE lower(p.name) " +
            "LIKE :#{#performerName==null || #performerName.isEmptry()? '%' : '%'+#performerName+'%'} " +
            "AND lower(c.title) " +
            "LIKE :#{#concertTitle==null || #concertTitle.isEmptry()? '%' : '%'+#concertTitle+'%'}")
    Page<Concert> fetchConcertByFilter(Pageable pageable, String performerName, String concertTitle);

}