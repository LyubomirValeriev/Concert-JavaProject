package com.concert.concertApp.controllers;

import com.concert.concertApp.entities.City;
import com.concert.concertApp.entities.ConcertHall;
import com.concert.concertApp.entities.User;
import com.concert.concertApp.payload.ConcertHallRequest;
import com.concert.concertApp.repositories.CityRepository;
import com.concert.concertApp.repositories.ConcertHallRepository;
import org.hibernate.engine.query.ParameterRecognitionException;
import org.springframework.dao.DataIntegrityViolationException;
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
    @PostMapping("/save/hall")
    public  ResponseEntity<?> saveConHall(
            @RequestBody ConcertHallRequest conHallRequest){



        if(conHallRequest.getAdress() == null
                || conHallRequest.getAdress().isEmpty())
            return ResponseEntity.ok( "Моля въведете адрес  ");


        ConcertHall concertHallInDb = concertHallRepo.findConcertHallByAdress(conHallRequest.getAdress());
//                .orElse(new ConcertHall(conHallRequest.getConHallName() ,
//                        conHallRequest.getAdress(),
//                        conHallRequest.getCapacity(),
//                        cityInDB));
        if(concertHallInDb != null)
            return  ResponseEntity.ok("Концертна зала на този адрес вече е запаметена");

        concertHallInDb = concertHallRepo.findConcertHallByName(conHallRequest.getConHallName()) ;
        if(concertHallInDb != null)
            return  ResponseEntity.ok("Концертна зала с това име вече съществува вече е запаметена");

        City cityInDB = cityRepo.findByName(conHallRequest.getCity())
                .orElse(new City(conHallRequest.getCity())) ;
        if(cityInDB.getId() == null)
        {
            cityRepo.save(cityInDB);
        }

        try {

            ConcertHall concertHall  = new ConcertHall(
              conHallRequest.getConHallName(),
               conHallRequest.getAdress(),
               String.valueOf(conHallRequest.getCapacity()),
               cityInDB
         );
         concertHallRepo.save( concertHall);
         return  ResponseEntity.ok("Концертната зала :" + concertHall.getConHallName() + " бе успешно запазена" );

        }
        catch (DataIntegrityViolationException e) {
            return ResponseEntity.ok("Не сте въвели всички елементи, опитайте отново!");
        }
        catch (ParameterRecognitionException e) {
            return  ResponseEntity.ok( e.getMessage()) ;
        }
        catch (Exception e ){
            return ResponseEntity.ok(e.getMessage());

        }
    }
//    @PostMapping("/save")
//    public ResponseEntity<?> saveHall(String name ,
//                                      String city,
//                                      Long capacity,
//                                      String adress,
//                                      @RequestParam(required = false) Long id ){
//
//        ConcertHall hall = null ;
//        City city1 = null ;
//
//        try {
//
//            city1 = cityRepo.findByName(city).
//                    orElse(new City(city));
//
//            if(city1.getId() == null ){
//                cityRepo.save(city1);
//            }
//            hall = concertHallRepo.findConcertHallByConHallId(id)
//                    .orElse(new ConcertHall(name, adress,  capacity , city1));
//
//            if (hall.getConHallId() != null) {
//                hall.setConHallName(name);
//                hall.setConHallAdress(adress);
//                // hall.setConHallCity(city);
//                hall.setConHallCapacity(capacity);
//
//           }
//
//        }catch (Exception e ){
//            return  new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
//        }
//       concertHallRepo.save(hall);
//        return  ResponseEntity.ok("HAll was save");
//      // return  ResponseEntity<ConcertHall>(concertHallRepo.save(hall), HttpStatus.OK);
//
//    }
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
                //return("Hall with name : " + name + "in " + city + " was deleted!");
            }
            catch ( DataIntegrityViolationException e ){
                return  ResponseEntity.ok ("Не можете да изтриете залата, защото тя е част от концерт");
            }


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

    @GetMapping("/pages")
    public ResponseEntity<?> filterHalls( @RequestParam(defaultValue = "") String conHallName,
                                          @RequestParam(defaultValue = "") String adress,
                                          @RequestParam(defaultValue = "1") int currentPage,
                                          @RequestParam(defaultValue = "5") int perPage){

        Pageable pageable = PageRequest.of(currentPage - 1, perPage);
       Page<ConcertHall> results = concertHallRepo.filterHallPages(
               pageable,
               conHallName.toLowerCase(),
               adress.toLowerCase());



        Map<String, Object> response = new HashMap();
        response.put("totalElements", results.getTotalElements());
        response.put("totalPages", results.getTotalPages());
        response.put("halls", results.getContent());

return  ResponseEntity.ok(response);
    }

}
