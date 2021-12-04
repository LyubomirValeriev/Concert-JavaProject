package com.concert.concertApp.controllers;

import com.concert.concertApp.entities.Concert;
import com.concert.concertApp.entities.Performer;
import com.concert.concertApp.payload.request.ConcertRequest;
import com.concert.concertApp.repositories.ConcertRepository;
import com.concert.concertApp.repositories.PerformerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/concert")
@RestController

public class ConcertsController {
    private final ConcertRepository concertRepo;
    private final PerformerRepository performerRepo;

    ConcertsController(ConcertRepository concertRepo,
                       PerformerRepository performerRepo){
        this.concertRepo = concertRepo;
        this.performerRepo = performerRepo;
    }

    @PostMapping("/save/performer")
    public ResponseEntity<?> persistPerformer(String name){
        Performer performer = performerRepo.findPerformerByName(name);
        if(performer != null){
            return  ResponseEntity.ok("Performer already exists");
        }

        return ResponseEntity.ok("Performer " + performerRepo.save(new Performer(name)).getName() + " saved successfully");
    }

    private  Performer persistNewPerformerByName(String performerName){
        return  performerRepo.save(new Performer(performerName));
    }

    @PostMapping("/save/concert")
    public  ResponseEntity<?> persistConcert(@RequestBody ConcertRequest concertRequest){
        Set<Performer> performerSet = new HashSet<>();
        for(String performerName: concertRequest.getPerformers()){
            Performer performer = performerRepo.fetchPerformerLikeName(performerName.toLowerCase(Locale.ROOT))
                    .orElseGet(() -> persistNewPerformerByName(performerName));

            performerSet.add(performer);
        }

        Concert concertInDB = concertRepo.fetchConcertLikeTitle(concertRequest.getTitle().toLowerCase());
        if (concertInDB != null){
            return  ResponseEntity.ok("Concert already exist");
        }
        return ResponseEntity.ok("Concert " + concertRepo.save(new Concert(concertRequest.getTitle(),
                                                                           concertRequest.getDescription(),
                                                                           concertRequest.getPrice(),
                                                                           concertRequest.getDate(),
                                                                           performerSet)).getTitle()  + " saved successfully");
    }

    @GetMapping("filter/pages")
    public ResponseEntity<?> getConcertPages(@RequestParam(required = false) String performerName,
                                         @RequestParam(required = false) String concertTitle,
                                         @RequestParam(defaultValue = "1") int currentPage,
                                         @RequestParam(defaultValue = "5") int perPage){

        Pageable pageable = PageRequest.of(currentPage-1, perPage);
        Page<Concert> concertPages = concertRepo.fetchConcertByFilter(pageable,
                                                performerName == null? null : performerName.toLowerCase(),
                                                concertTitle == null? null :concertTitle.toLowerCase());

        Map<String, Object> response = new HashMap<>();
        response.put("results", concertPages.getContent());
        response.put("totalPages", concertPages.getTotalPages());
        response.put("totalElements", concertPages.getTotalElements());

        return  ResponseEntity.ok(response);
    }
}