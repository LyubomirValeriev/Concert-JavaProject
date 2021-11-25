package com.concert.concertApp.controllers;

import com.concert.concertApp.entities.ConcertHall;
import com.concert.concertApp.repositories.ConcertHallRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/ConHall")

public class ConcertHallController {

    private final ConcertHallRepository concertHallRepo;
    ConcertHallController(ConcertHallRepository concertHallRepo) {
        this.concertHallRepo = concertHallRepo;
    }

    @GetMapping("/fetch")
    public List<ConcertHall> getAllConHalls() {
        return concertHallRepo.findAll();
    }

    @GetMapping("/find/adress")
    public ResponseEntity<?> findHall(String adress) {
        Optional<ConcertHall> result = concertHallRepo.findConcertHallByConHallAdress(adress);

        return result.isPresent()
                ?ResponseEntity.ok(result.get())
                : ResponseEntity.ok("not found Concert Hall with adress:" + adress);
    }
    @PostMapping("/save")
    public ResponseEntity<?> saveHall(String name ,
                                      String city ,
                                      Long capacity,
                                      String adress,
                                      @RequestParam(required = false) Long id ){

        ConcertHall hall = null ;

        try {

            hall = concertHallRepo.findConcertHallByConHallId(id)
                    .orElse(new ConcertHall(name, adress, city, capacity));

            if (hall != null) {
                hall.setConHallName(name);
                hall.setConHallAdress(adress);
                hall.setConHallCity(city);
                hall.setConHallCapacity(capacity);
            }

        }catch (Exception e ){
            return  new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
       concertHallRepo.save(hall);
        return  ResponseEntity.ok("HAll was save");
      // return  ResponseEntity<ConcertHall>(concertHallRepo.save(hall), HttpStatus.OK);

    }
    @DeleteMapping("/deleteHall")
    public ResponseEntity<?> deleteHall (String name , String city ){
        Optional<ConcertHall> hall = concertHallRepo.findConcertHallByConHallCityAndConHallName(city , name);
        if(hall.isEmpty()){
            return ResponseEntity.ok("Hall not found");

        }
concertHallRepo.delete(hall.get()) ;
        return ResponseEntity.ok( "Concert hall with name : " + name +
                " in city :" + city + "was deleted" );
        //return("Hall with name : " + name + "in " + city + " was deleted!");


    }
}
