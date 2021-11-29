package com.concert.concertApp.controllers;

import com.concert.concertApp.entities.Concert;
import com.concert.concertApp.entities.Reservation;
import com.concert.concertApp.repositories.ConcertHallRepository;
import com.concert.concertApp.repositories.ConcertRepository;
import com.concert.concertApp.repositories.ReservationRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
