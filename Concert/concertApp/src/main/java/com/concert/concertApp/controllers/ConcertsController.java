package com.concert.concertApp.controllers;

import com.concert.concertApp.Additional.MailSender;
import com.concert.concertApp.entities.Concert;
import com.concert.concertApp.entities.ConcertHall;
import com.concert.concertApp.entities.Performer;

import com.concert.concertApp.payload.request.ConcertRequest;

import com.concert.concertApp.repositories.ConcertHallRepository;

import com.concert.concertApp.repositories.ConcertRepository;
import com.concert.concertApp.repositories.PerformerRepository;
import org.hibernate.engine.query.ParameterRecognitionException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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

    @GetMapping("/fetch/concerts")
    public List<Concert> getAllConcerts() {
        return concertRepo.findAll();
    }



    @PostMapping("/save/performer")
    public ResponseEntity<?> persistPerformer(String name){
        Performer performer = performerRepo.findPerformerByName(name.trim());
        if(performer != null){
            return  ResponseEntity.ok("Performer already exists");
        }

        return ResponseEntity.ok("Performer " + performerRepo.save(new Performer(name)).getName() + " saved successfully");
    }

    private  Performer persistNewPerformerByName(String performerName){
        return  performerRepo.save(new Performer(performerName));
    }

    @PostMapping("/save/concert")
    public  ResponseEntity<?> persistConcert(@RequestBody ConcertRequest concertRequest) {
        if ((concertRequest.getTitle() == null) || (concertRequest.getTitle()).trim().isEmpty()){
            return ResponseEntity.ok("Enter a title to create a concert!");
        }
        if ((concertRequest.getPerformers() == null) || (concertRequest.getPerformers()).isEmpty()){
            return ResponseEntity.ok("Enter performers to create a concert!");
        }
        if(concertRequest.getConHallId() == null || concertRequest.getConHallId() == 0) {
        return  ResponseEntity.ok("Enter Concert Hall id  to create a concert!");
        }


        Set<Performer> performerSet = new HashSet<>();
        for (String performerName : concertRequest.getPerformers()) {
            Performer performer = performerRepo.fetchPerformerLikeName(performerName.toLowerCase(Locale.ROOT))
                    .orElseGet(() -> persistNewPerformerByName(performerName));

            performerSet.add(performer);
        }


        Concert concertInDB = concertRepo.fetchConcertLikeTitle((concertRequest.getTitle().toLowerCase()).trim());
        if (concertInDB != null) {
            return ResponseEntity.ok("Concert already exist");
        }


        try {
            ConcertHall hall =  concertHallRepo.findById(concertRequest.getConHallId())
                .orElseThrow(() -> new IllegalArgumentException("Concert Hall with id:" +concertRequest.getConHallId() +" doesn't exist!"));
            Concert concert = new Concert((concertRequest.getTitle()),
                    concertRequest.getDescription(),
                    concertRequest.getPrice(),
                    concertRequest.getDate(),
                    performerSet,
                    hall);
            concertRepo.save(concert);

            return ResponseEntity.ok("Concert " + concert.getTitle()  + " saved successfully");
        }catch (NullPointerException e) {
        return ResponseEntity.ok(e.getMessage());

        }catch (IllegalArgumentException k){
            return  ResponseEntity.ok(k.getMessage());
        }catch (ParameterRecognitionException pre) {
            return ResponseEntity.ok(pre.getMessage());  // проверка за невалидни данни - price
        }

    }

    @GetMapping("/filter/pages")
    public ResponseEntity<?> getConcertPages(@RequestParam(defaultValue = "5") int perPage,
                                             @RequestParam(defaultValue = "1") int currentPage,
                                             @RequestParam(defaultValue = "") String concertTitle,
                                             @RequestParam(defaultValue = "") String performerName) {

        Pageable pageable = PageRequest.of(currentPage-1, perPage);
        Page<Concert> concertPages = concertRepo.filterConcert(pageable, (concertTitle.toLowerCase()).trim(), (performerName.toLowerCase()).trim());

        Map<String, Object> response = new HashMap<>();
        response.put("totalElements", concertPages.getTotalElements());
        response.put("totalPages", concertPages.getTotalPages());
        response.put("results", concertPages.getContent());

    return  ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/performer")
    public ResponseEntity<?> deletePerformer(String performerName){
        try {
            Performer performer = performerRepo.findPerformerByName(performerName.trim());
            if (performer == null){
                return ResponseEntity.ok("Performer not found!");
            }
            performerRepo.delete(performer);
        }catch (DataIntegrityViolationException e){
           return ResponseEntity.ok("The performer can not be deleted as it is part of a concert!");
        }

        return  ResponseEntity.ok("Performer " + performerName + " was successfully deleted!");
    }

    @DeleteMapping("/delete/concert")
    public ResponseEntity<?> deleteConcert(String title){
        Concert concert = concertRepo.findConcertByTitle(title.trim());
        if (concert == null){
            return ResponseEntity.ok("Concert not found!");
        }
        concertRepo.delete(concert);
        return  ResponseEntity.ok("Concert " + title + " was successfully deleted!");
    }

}