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
    Optional<ConcertHall> findConcertHallByConHallId(Long id);

    Optional<ConcertHall> findConcertHallByConHallName( String name );


//    @Query("SELECT u " +
//            "FROM ConcertHall u JOIN u.city i  " +
//            "WHERE " +
//            "lower(u.conHallName) " +
//            "LIKE :#{#conHallName == null || #conHallName.isEmpty()? '%' : #conHallName+'%'} " +
//            "AND lower(u.conHallAdress) " +
//            "LIKE :#{#conHallAdress == null || #conHallAdress.isEmpty()? '%' : #conHallAdress+'%'}"+
//            "AND "+
//            "AND lower(i.name)" +
//            "LIKE :#{# city == null || #city.isEmpty()? '%' : #city+'%'}"
//    )
//    Page<ConcertHall> filterHallPages(Pageable pageable, String name , String adress);

    @Query("SELECT u FROM ConcertHall u WHERE u.conHallAdress = :adress" )
    Optional<ConcertHall> findConcertHallByConHallAdress(String adress);


}
