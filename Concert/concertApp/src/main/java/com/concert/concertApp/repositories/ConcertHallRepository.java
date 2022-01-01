package com.concert.concertApp.repositories;

import com.concert.concertApp.entities.ConcertHall;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;
import java.util.Optional;

public interface ConcertHallRepository extends JpaRepository<ConcertHall , Long>
{
   // Optional<ConcertHall> findConcertHallByConHallId(Long id);

    Optional<ConcertHall> findConcertHallByConHallName( String name );


    @Query("SELECT u.conHallAdress, u.conHallName, max(u.conHallCapacity) , max(i) " +
            "FROM ConcertHall u JOIN u.city i  " +
            "WHERE lower(u.conHallName) " +
            "LIKE :#{#name.isEmpty()? '%' : #name+'%'} " +
            "AND lower(u.conHallAdress) " +
            "LIKE :#{#adress.isEmpty()? '%' : #adress+'%'} " +
            "GROUP BY u.conHallAdress, u.conHallName, u.conHallCapacity " +
            "ORDER BY u.conHallCapacity ASC")
    Page<ConcertHall> filterHallPages(Pageable pageable, String name , String adress);

    @Query("SELECT u FROM ConcertHall u WHERE u.conHallAdress = :adress" )
    Optional<ConcertHall> findConcertHallByConHallAdress(String adress);

    @Query("SELECT u FROM ConcertHall u WHERE u.conHallAdress = :adress" )
    ConcertHall findConcertHallByAdress(String adress);

    @Query("SELECT u FROM ConcertHall u WHERE u.conHallName = :name" )
    ConcertHall findConcertHallByName(String name);
}
