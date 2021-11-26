package com.concert.concertApp.repositories;

import com.concert.concertApp.entities.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City , Long> {

}
