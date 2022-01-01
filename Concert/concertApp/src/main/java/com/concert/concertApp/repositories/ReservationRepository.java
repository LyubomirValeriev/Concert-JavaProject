package com.concert.concertApp.repositories;

import com.concert.concertApp.entities.ConcertHall;
import com.concert.concertApp.entities.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReservationRepository extends JpaRepository<Reservation , Long> {

//    @Query("SELECT max(u.reservationFinalPrice) ,max(c)" +
//            "FROM Reservation u JOIN  u.concert c " +
//       //     "WHERE lower(u.reservationFinalPrice)" +
//          //  "AND FROM Reservation JOIN u.user " +
//          //   "LIKE :#{#Price.isEmpty()? '%' : #Price+'%' }" +
//            " group by  u.reservationFinalPrice" +
//            " order by u.reservationFinalPrice ASC ")
//        Page<Reservation> filterReservations(Pageable pageable, Double Price  );

    @Query("SELECT u FROM Reservation u WHERE u.reservationId = :id" )
    Reservation findReservationById(Long id);
}
