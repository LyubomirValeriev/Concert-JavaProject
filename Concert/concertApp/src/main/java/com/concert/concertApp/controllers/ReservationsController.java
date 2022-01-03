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
         return ResponseEntity.ok("Няма такава резервация ;");
     }
     Reservation reservation1  = reservationRepo.findReservationById(ReservationId);
     reservation1.freeUpSpace(Integer.parseInt(reservation1.getReservationTickets()));
     reservationRepo.delete(reservation.get());
     return  ResponseEntity.ok("Резервацията с id:" + ReservationId + " е изтрита ");
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
         discountInDb = discountRepo.findById(discount)
                 .orElseThrow(() -> new IllegalArgumentException("проверете id-то на потребителя отново <3"));
         concert = concertsRepo.findById(concertId)
                 .orElseThrow(() -> new IllegalArgumentException("Не е намерен концерт с такова id"));

         user = userRepo.findUserById(userId)
                 .orElseThrow(() -> new IllegalArgumentException("Не е намерен потребител с такова id"));

         Reservation reservation = null ;
         if (concert.getId() != null
                 && user.getId() != null) {

             Integer ticketsNumber = Integer.parseInt(numberTickets);
             double price = Double.parseDouble(concert.getPrice())  ;
             Integer discountPercent = Integer.parseInt(discountInDb.getDiscountPercentage());

             double finalPrice = (ticketsNumber) * (price) - (((ticketsNumber) * (price))* discountPercent/100) ; // смята отстъпка

             reservation = new Reservation(numberTickets ,
                     new Date(System.currentTimeMillis()),
                     (discountPercent != null)? true : false ,
                     finalPrice ,
                     concert,
                     user,
                     discountInDb
             );
         }
         reservation.checkedCapacity(Integer.parseInt(numberTickets));
         reservationRepo.save(reservation);
         MailSender.sendEmail(reservation);
         return  ResponseEntity.ok("Резервацията беше успешно запазена") ;
     }
     catch (DataIntegrityViolationException e) {
         return  new ResponseEntity<>("Не сте въвели всички елементи, опитайте отново!", HttpStatus.OK);
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
