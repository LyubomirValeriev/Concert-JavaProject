package com.concert.concertApp.entities;


import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long reservationId ;

// test
//    @Column(name  = " user_id " )
//
//    @Column(name = "concert_id")


    @Column(name = "reservation_date")
    private Timestamp reservationDate ;


    @Column(name = "reservation_ispaid")
    private boolean reservationPaid ;


    @Column(name = "reservation_discount")
    private boolean reservationDiscount ;

}
