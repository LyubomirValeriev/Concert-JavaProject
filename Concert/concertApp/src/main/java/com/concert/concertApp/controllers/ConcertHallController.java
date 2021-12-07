package com.concert.concertApp.controllers;

import com.concert.concertApp.entities.City;
import com.concert.concertApp.entities.ConcertHall;
import com.concert.concertApp.entities.User;
import com.concert.concertApp.repositories.CityRepository;
import com.concert.concertApp.repositories.ConcertHallRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/ConHall")

public class ConcertHallController {

    private final ConcertHallRepository concertHallRepo;
    private final CityRepository cityRepo ;
    
    ConcertHallController(ConcertHallRepository concertHallRepo, CityRepository cityRepo) {
        this.concertHallRepo = concertHallRepo;
        this.cityRepo = cityRepo;
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
                                      String city,
                                      Long capacity,
                                      String adress,
                                      @RequestParam(required = false) Long id ){

        ConcertHall hall = null ;
        City city1 = null ;

        try {
            city1 = cityRepo.findByName(city).
                    orElse(new City(city));

            if(city1.getId() == null ){
                cityRepo.save(city1);
            }
            hall = concertHallRepo.findConcertHallByConHallId(id)
                    .orElse(new ConcertHall(name, adress,  capacity , city1));

            if (hall.getConHallId() != null) {
                hall.setConHallName(name);
                hall.setConHallAdress(adress);
                // hall.setConHallCity(city);
                hall.setConHallCapacity(capacity);
//
//                Optional<City> city2 = cityRepo.findByName(city);
//            if(city2.isEmpty()) {
//                saveCity(city, id);
//                city1 = cityRepo.findName(city);
//            }
//                city1 = cityRepo.findName(city);
//                    hall.setCity(city1);
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

        Optional<City> city1 = cityRepo.findByName(city);
        if(!city1.isEmpty()) {

            Optional<ConcertHall> hall = concertHallRepo.findConcertHallByConHallName(name);
            if (hall.isEmpty()) {
                return ResponseEntity.ok("Hall not found");

            }
            concertHallRepo.delete(hall.get());
            return ResponseEntity.ok("Concert hall with name : " + name + "in City" + city +
                    "was deleted");
            //return("Hall with name : " + name + "in " + city + " was deleted!");

        }else
            return ResponseEntity.ok("City not found");
    }

    @PostMapping("/city/save")
    public  ResponseEntity<?> saveCity(String name ,
                                       @RequestParam(required = false) Long id ){
        City city  ;
        try{
            city = cityRepo.findById(id)
                    .orElse(new City(name));

            if(city != null)
            {
                city.setName(name);
            }

        }catch (Exception e){
            return  new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }

        cityRepo.save(city);
        return ResponseEntity.ok("City was save" );
    }

    @GetMapping("/city/fetch")
    public List<City> getAllCitites() {
        return cityRepo.findAll();
    }

//    @GetMapping("/pages")
//    public ResponseEntity<?> filterHalls( @RequestParam(defaultValue = "") String conHallName,
//                                          @RequestParam(defaultValue = "") String adress,
//                                          @RequestParam(defaultValue = "") int currentPage,
//                                          @RequestParam(defaultValue = "") int perPage){
//
//        Pageable pageable = PageRequest.of(currentPage - 1, perPage);
//    //    Page<ConcertHall> results = concertHallRepo.filterHallPages(pageable, conHallName.toLowerCase(), adress.toLowerCase());
//
//
//
//        Map<String, Object> response = new HashMap();
//        response.put("totalElements", results.getTotalElements());
//        response.put("totalPages", results.getTotalPages());
//        response.put("users", results.getContent());
//
//return  ResponseEntity.ok(response);
//    }

}
