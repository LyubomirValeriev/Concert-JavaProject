package com.concert.concertApp.controllers;

import com.concert.concertApp.entities.Concert;
import com.concert.concertApp.entities.ConcertHall;
import com.concert.concertApp.entities.Performer;
import com.concert.concertApp.repositories.ConcertHallRepository;
import com.concert.concertApp.repositories.ConcertRepository;
import com.concert.concertApp.repositories.PerformerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/concert")
@RestController

public class ConcertsController {

    private final ConcertRepository concertRepo;
    private final PerformerRepository performerRepo;
 private final ConcertHallRepository concertHallRepo ;

    ConcertsController(ConcertRepository concertRepo,
                       PerformerRepository performerRepo,
                       ConcertHallRepository concertHallRepo){

        this.concertRepo = concertRepo;
        this.performerRepo = performerRepo;
        this.concertHallRepo = concertHallRepo ;

    }

    @GetMapping("/fetch")
    public List<Concert> getAllConcerts(){
        return concertRepo.findAll() ;
    }
    @PostMapping("/save/performer")
    public ResponseEntity<?> savePerformer(String name){
        return ResponseEntity.ok("Performer " + performerRepo.save(new Performer(name)).getName() + " saved successfully");
    }

    @PostMapping("/save/concert")
    public  ResponseEntity<?> saveConcert(String title, String description, Double price, Timestamp date, String performerName){
        Performer performer = performerRepo.findPerformerByName(performerName);
        Concert concert = new Concert(title, description, price, date);

        Set<Performer> performers = new HashSet<>();
        performers.add(performer);
        concert.setPerformers(performers);

        return ResponseEntity.ok("Concert " + concertRepo.save(concert).getTitle() + " saved successfully");

    }
}