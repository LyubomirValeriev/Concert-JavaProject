package com.concert.concertApp.controllers;


import com.concert.concertApp.entities.City;
import com.concert.concertApp.entities.ConcertHall;
import com.concert.concertApp.repositories.CityRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
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
                .findByName(name.toLowerCase(Locale.ROOT));

        return result.isPresent()
                ? ResponseEntity.ok(result.get())
                : ResponseEntity.ok("Not found city with name: " + name);
    }
    @PostMapping("/save")
    public ResponseEntity<?> saveCity (String name ,
                                       @RequestParam(required = false) Long id ){

        if (name.isEmpty() || name == null)
            return ResponseEntity.ok("Enter a name to save a city!");

        City cityInDB = null;
        try {
            cityInDB = cityRepo.findName(name.toLowerCase(Locale.ROOT));
            if (cityInDB != null)
                throw new IllegalArgumentException("City already exist !");

            cityInDB = cityRepo.findId(id)
                    .orElse(new City(name));

            if (cityInDB.getId() != null) {
                cityInDB.setName(name);
            }

            cityRepo.save(cityInDB);

            return ResponseEntity.ok("City saved successfully");

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }

    }

    @DeleteMapping("/delete")
    public  ResponseEntity<?> deleteCity(String name){
        if (name.isEmpty() || name == null)
            return ResponseEntity.ok("Enter a name to delete a city!");
        try{
            City city = cityRepo.findName(name);
            if(city == null)
                return  ResponseEntity.ok("City not found");
            cityRepo.delete(city);
            return ResponseEntity.ok("City was successfully deleted!");
        }catch (DataIntegrityViolationException e){
            return ResponseEntity.ok("The city can not be deleted as it is part of a concert hall!");
        }
    }
}

