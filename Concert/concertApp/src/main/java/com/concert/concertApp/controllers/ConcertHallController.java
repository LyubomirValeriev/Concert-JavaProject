package com.concert.concertApp.controllers;

import com.concert.concertApp.entities.City;
import com.concert.concertApp.entities.ConcertHall;
import com.concert.concertApp.entities.User;
import com.concert.concertApp.payload.ConcertHallRequest;
import com.concert.concertApp.repositories.CityRepository;
import com.concert.concertApp.repositories.ConcertHallRepository;
import org.hibernate.engine.query.ParameterRecognitionException;
import org.hibernate.procedure.ParameterMisuseException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/ConHall")

public class ConcertHallController {

    private final ConcertHallRepository concertHallRepo;
    private final CityRepository cityRepo ;
    
    ConcertHallController(ConcertHallRepository concertHallRepo,
                          CityRepository cityRepo) {
        this.concertHallRepo = concertHallRepo;
        this.cityRepo = cityRepo;
    }

    @GetMapping("/fetch")
    public List<ConcertHall> getAllConHalls() {
        return concertHallRepo.findAll();
    }

    @GetMapping("/find/adress")
    public ResponseEntity<?> findHall(String address) {
        Optional<ConcertHall> result = concertHallRepo.findConcertHallByConHallAddress(address);

        return result.isPresent()
                ?ResponseEntity.ok(result.get())
                : ResponseEntity.ok("Not found Concert Hall with adress:" + address);
    }
    @PostMapping("/save/hall")
    public  ResponseEntity<?> saveConHall(
            @RequestBody ConcertHallRequest conHallRequest){

        if( conHallRequest.getCity() == null|| conHallRequest.getCity().isEmpty() )
            return ResponseEntity.ok("Enter a city name to create a concert hall!");

        if( conHallRequest.getAddress() == null|| conHallRequest.getAddress().isEmpty() )
            return ResponseEntity.ok("Enter a address to create a concert hall!");

        if( conHallRequest.getCapacity() == null|| conHallRequest.getCapacity().isEmpty() )
            return ResponseEntity.ok("Enter a capacity to create a concert hall!");

        ConcertHall concertHallInDb = concertHallRepo.findConcertHallByAddress(conHallRequest.getAddress());
        if(concertHallInDb != null)
            return  ResponseEntity.ok("This address is already in use");

        concertHallInDb = concertHallRepo.findConcertHallByName(conHallRequest.getConHallName()) ;
        if(concertHallInDb != null)
            return  ResponseEntity.ok("Concert Hall with name :"+conHallRequest.getConHallName()+" is already exist.");




        try {
            City cityInDB = cityRepo.findByName(conHallRequest.getCity().toLowerCase(Locale.ROOT))
                    .orElse(new City(conHallRequest.getCity())) ;
            if(cityInDB.getId() == null)
            {
                cityRepo.save(cityInDB);
            }
            ConcertHall concertHall  = new ConcertHall(
              conHallRequest.getConHallName(),
               conHallRequest.getAddress(),
               String.valueOf(conHallRequest.getCapacity()),
               cityInDB
         );
         concertHallRepo.save( concertHall);
         return  ResponseEntity.ok("Concert Hall: " + concertHall.getConHallName() + " saved successfully" );

        }
        catch (DataIntegrityViolationException e) {
            return ResponseEntity.ok("It is mandatory to fill all concert hall fields correctly. Check your personal data.");
        }
        catch (ParameterMisuseException u) {                // проверка за : капацитета
            return ResponseEntity.ok(u.getMessage());
        }
        catch (ParameterRecognitionException e) {           // проверка за :  името
            return  ResponseEntity.ok( e.getMessage()) ;
        }
        catch (NullPointerException e ){                    // проверка за :  името
            return  ResponseEntity.ok(e.getMessage());
        }
        catch (Exception e ){
            return ResponseEntity.ok("Something went wrong, try again <3");

        }
    }

    @DeleteMapping("/deleteHall")
    public ResponseEntity<?> deleteHall (
            String name ,
            String city ){

        Optional<City> city1 = cityRepo.findByName(city);
        if(!city1.isEmpty()) {

            Optional<ConcertHall> hall = concertHallRepo.findConcertHallByConHallName(name);
            if (hall.isEmpty()) {
                return ResponseEntity.ok("Hall not found");

            }
            try {
                concertHallRepo.delete(hall.get());
                return ResponseEntity.ok("Concert hall with name : " + name + " in City " + city +
                        " was deleted");
            }
            catch ( DataIntegrityViolationException e ){
                return  ResponseEntity.ok ("The Concert Hall can not be deleted as it is part of a concert!");
            } catch (Exception e){
        return  ResponseEntity.ok("Something went wrong, try again <3");}



    }else
            return ResponseEntity.ok("City not found");
    }


    @GetMapping("/city/fetch")
    public List<City> getAllCites() {
        return cityRepo.findAll();
    }

    @GetMapping("/pages")
    public ResponseEntity<?> filterHalls( @RequestParam(defaultValue = "") String conHallName,
                                          @RequestParam(defaultValue = "") String address,
                                          @RequestParam(defaultValue = "1") int currentPage,
                                          @RequestParam(defaultValue = "5") int perPage){

        Pageable pageable = PageRequest.of(currentPage - 1, perPage);
       Page<ConcertHall> results = concertHallRepo.filterHallPages(
               pageable,
               conHallName.toLowerCase(),
               address.toLowerCase());



        Map<String, Object> response = new HashMap();
        response.put("totalElements", results.getTotalElements());
        response.put("totalPages", results.getTotalPages());
        response.put("halls", results.getContent());

return  ResponseEntity.ok(response);
    }

}
