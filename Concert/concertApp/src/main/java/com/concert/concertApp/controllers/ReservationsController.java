package com.concert.concertApp.controllers;

import com.concert.concertApp.entities.Concert;
import com.concert.concertApp.entities.Reservation;
import com.concert.concertApp.repositories.ConcertHallRepository;
import com.concert.concertApp.repositories.ConcertRepository;
import com.concert.concertApp.repositories.ReservationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/Reservation")

public class ReservationsController {
    private final ReservationRepository reservationRepo ;
    private final ConcertRepository concertsRepo ;

 ReservationsController(ReservationRepository reservationRepo , ConcertRepository concertRepo){
     this.reservationRepo = reservationRepo ;
     this.concertsRepo = concertRepo  ;
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

}
