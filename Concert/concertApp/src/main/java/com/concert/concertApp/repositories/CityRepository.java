package com.concert.concertApp.repositories;

import com.concert.concertApp.entities.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface CityRepository extends JpaRepository<City , Long> {


    Optional<City> findById(Long id) ;

    @Query("SELECT u FROM City u WHERE u.id = :id" )
    Optional<City> findId(Long id);

    @Query("SELECT u FROM City u WHERE lower( u.name ) = :name" )
    Optional<City> findByName(String name);

    @Query("SELECT u FROM City u WHERE lower( u.name )= :name" )
    City findName(String name);
}
