package com.concert.concertApp.repositories;

import com.concert.concertApp.entities.ConcertHall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ConcertHallRepository extends JpaRepository<ConcertHall , Long>
{
    Optional<ConcertHall> findConcertHallByConHallId(Long id);

    Optional<ConcertHall> findConcertHallByConHallCityAndConHallName(String city , String name );


    @Query("SELECT u FROM ConcertHall u WHERE u.conHallAdress = :adress" )
    Optional<ConcertHall> findConcertHallByConHallAdress(String adress);


}
