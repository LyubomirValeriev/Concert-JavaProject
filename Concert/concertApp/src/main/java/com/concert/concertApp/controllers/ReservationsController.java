package com.concert.concertApp.controllers;

import com.concert.concertApp.Additional.MailSender;
import com.concert.concertApp.entities.*;
import com.concert.concertApp.repositories.*;
import org.hibernate.procedure.ParameterMisuseException;
import org.hibernate.procedure.ParameterStrategyException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.concert.concertApp.entities.ConcertHall.isValidNumber;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/Reservation")

public class ReservationsController {
    private final ReservationRepository reservationRepo ;
    private final ConcertRepository concertsRepo ;
    private final UserRepository userRepo ;
    private  final DiscountRepository discountRepo;

 ReservationsController(ReservationRepository reservationRepo,
                        ConcertRepository concertRepo,
                        UserRepository userRepo,
                        DiscountRepository discountRepo){
     this.reservationRepo = reservationRepo ;
     this.concertsRepo = concertRepo  ;
     this.userRepo = userRepo ;
     this.discountRepo = discountRepo ;
 }

 @GetMapping("/fetch")
    public List<Reservation> getAllReservations(){
     return reservationRepo.findAll();
 }

 @DeleteMapping("/delete")
    public ResponseEntity<?> deleteReservation(Long ReservationId){
    
     Optional<Reservation> reservation = reservationRepo.findById(ReservationId);
     if(reservation.isEmpty()){
         return ResponseEntity.ok("Reservation not found ! ;");
     }
     Reservation reservation1  = reservationRepo.findReservationById(ReservationId);
     reservation1.freeUpSpace(Integer.parseInt(reservation1.getReservationTickets()));
     reservationRepo.delete(reservation.get());
     return  ResponseEntity.ok("Reservation with id:" + ReservationId + " was deleted");
 }

 @PostMapping("/save")
    public ResponseEntity<?> saveReservation (Long userId,
                                              Long concertId,
                                              String numberTickets,
                                              Long discount) {

     Discount discountInDb = null ;
     Concert concert = null;
     User user = null;
     try {
         if(discount != null) {
             discountInDb = discountRepo.findById(discount)
                     .orElseThrow(() -> new IllegalArgumentException("Check  discount id again <3"));
         } else if (discount == null) {
             discount = Long.valueOf(0);
             discountInDb = discountRepo.findDiscountById(discount);
            // throw new IllegalArgumentException("Enter discount id to create reservation");
         }
         if(concertId != null ) {
             concert = concertsRepo.findById(concertId)
                     .orElseThrow(() -> new IllegalArgumentException("Check concert id again <3"));
         }else if(concertId == null)
         {
             throw  new IllegalArgumentException("Enter concert to create reservation");
         }

         if(userId != null) {
             user = userRepo.findUserById(userId)
                     .orElseThrow(() -> new IllegalArgumentException("Check  user id again <3"));
         }
         else if(userId == null )
         {
             throw  new IllegalArgumentException("Enter user id to create reservation");
         }

         if(numberTickets == null || numberTickets.isEmpty()) {
             throw new IllegalArgumentException("Enter number of tickets which you want to buy to create reservation");
         }else if(!isValidNumber(numberTickets)){
             throw  new IllegalArgumentException("Enter only numbers !");
         }

         Reservation reservation = null;
         if (concert.getId() != null
                 && user.getId() != null) {

             Integer ticketsNumber = Integer.parseInt(numberTickets);
             double price = Double.parseDouble(concert.getPrice());

             Integer discountPercent = Integer.parseInt(discountInDb.getDiscountPercentage());

             double finalPrice = (ticketsNumber) * (price) - (((ticketsNumber) * (price)) * discountPercent / 100); // смята отстъпка

             reservation = new Reservation(numberTickets,
                     new Date(System.currentTimeMillis()),
                     ((ticketsNumber) * (price) != (ticketsNumber) * (price))? true : false ,
                     finalPrice ,
                     concert,
                     user,
                     discountInDb
             );
         }
         reservation.checkedCapacity(Integer.parseInt(numberTickets));
         reservationRepo.save(reservation);
         MailSender.sendEmail(reservation);
         return  ResponseEntity.ok("The reservation was successfully saved, check your e-mail") ;
     }
     catch (DataIntegrityViolationException e) {
         return  new ResponseEntity<>("It is mandatory to fill all concert hall fields correctly. Check your personal data.!", HttpStatus.OK);
     }
     catch (IllegalArgumentException t) {
         return  new ResponseEntity<>(t.getMessage(), HttpStatus.OK);
     }
     catch (RuntimeException re){
         return ResponseEntity.ok(re.getMessage());
     }

     catch (Exception e) {
         return  new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
     }


 }

}
