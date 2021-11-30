package com.concert.concertApp.controllers;


import com.concert.concertApp.entities.City;
import com.concert.concertApp.entities.ConcertHall;
import com.concert.concertApp.repositories.CityRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/City")
public class CityController {
    private final CityRepository cityRepo ;

    CityController(CityRepository cityRepo){
        this.cityRepo = cityRepo ;
    }

    @GetMapping("/fetch")
    public List<City> getAllCities(){
        return  cityRepo.findAll() ;
    }

    @GetMapping("/find/name")
    public ResponseEntity<?> findHall(String name) {
        Optional<City> result = cityRepo
                .findByName(name);

        return result.isPresent()
                ?ResponseEntity.ok(result.get())
                : ResponseEntity.ok("Не е открит град с име:" + name);
    }
    @PostMapping("/save")
    public ResponseEntity<?> saveCity (@RequestParam(required = false) Long id ,
                                       String name  ){
        City city = null ;
        try {

            city = cityRepo.findById(id)
                    .orElse(new City(name));

            if (city.getId() != null) {
                city.setName(name);
            }
            }catch (Exception e ){
            return  new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
cityRepo.save(city);
        return  ResponseEntity.ok("Града беше запазен успешно");
        }
}

