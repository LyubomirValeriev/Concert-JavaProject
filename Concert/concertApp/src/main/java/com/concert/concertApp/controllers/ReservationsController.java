package com.concert.concertApp.controllers;

import com.concert.concertApp.entities.Concert;
import com.concert.concertApp.entities.ConcertHall;
import com.concert.concertApp.entities.Reservation;
import com.concert.concertApp.entities.User;
import com.concert.concertApp.repositories.ConcertHallRepository;
import com.concert.concertApp.repositories.ConcertRepository;
import com.concert.concertApp.repositories.ReservationRepository;
import com.concert.concertApp.repositories.UserRepository;
import org.hibernate.procedure.ParameterMisuseException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/Reservation")

public class ReservationsController {
    private final ReservationRepository reservationRepo ;
    private final ConcertRepository concertsRepo ;
    private final UserRepository userRepo ;

 ReservationsController(ReservationRepository reservationRepo , ConcertRepository concertRepo, UserRepository userRepo){
     this.reservationRepo = reservationRepo ;
     this.concertsRepo = concertRepo  ;
     this.userRepo = userRepo ;
 }

 @GetMapping("/fetch")
    public List<Reservation> getAllReservations(){
     return reservationRepo.findAll();
 }

 @DeleteMapping("/delete")
    public ResponseEntity<?> deleteReservation(Long id){
     Optional<Reservation> reservation = reservationRepo.findById(id);
     if(reservation.isEmpty()){
         return ResponseEntity.ok("Няма такава резервация ;(");
     }
     reservationRepo.delete(reservation.get());
     return  ResponseEntity.ok("Резервацията с id:" + id + " е изтрита ");
 }

 @PostMapping("/save")
    public ResponseEntity<?> saveReservation (Long userId,
                                              Long concertId,
                                              String numberTickets) {

     Concert concert = null;
     User user = null;
     try {
         concert = concertsRepo.findById(concertId)
                 .orElseThrow(() -> new IllegalArgumentException());

         user = userRepo.findUserById(userId)
                 .orElseThrow(() -> new IllegalArgumentException());
         Reservation reservation = null ;
         if (concert.getId() != null
                 && user.getId() != null) {
             double a = 0.1 ;
             reservation = new Reservation(numberTickets ,
                     new Date(System.currentTimeMillis()),
                     true,
                     true,
                     (double) 0,
                     concert,
                     user
             );
             reservation.setUser(user);
             reservation.setConcert(concert);
             reservation.setReservationDate( new Date(System.currentTimeMillis()));
             reservation.setReservationPaid(true);

         }
         reservationRepo.save(reservation);
         return  ResponseEntity.ok("Резервацията беше успешно запазена") ;

     }
     catch (IllegalArgumentException t) {
         return  new ResponseEntity<>("Няма такъв концерт/протребител с такова id", HttpStatus.OK);

     }catch (ParameterMisuseException p){
         return  new ResponseEntity<>(p.getMessage(), HttpStatus.OK);

     }
     catch (Exception e) {
         return  new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
     }


 }
// @GetMapping("/pages")
//    public ResponseEntity<?> filterReservations( @RequestParam(defaultValue = "0") Double reservationFinalPrice ,
//                                                 @RequestParam(defaultValue = "1") int currentPage,
//                                                 @RequestParam(defaultValue = "5") int perPage){
//
//        Pageable pageable = PageRequest.of(currentPage - 1, perPage);
//        Page<Reservation> results = reservationRepo.filterReservations(
//                pageable,
//                reservationFinalPrice
//        );
//
//
//
//        Map<String, Object> response = new HashMap();
//        response.put("totalElements", results.getTotalElements());
//        response.put("totalPages", results.getTotalPages());
//        response.put("halls", results.getContent());
//
//        return  ResponseEntity.ok(response);
//    }
}
