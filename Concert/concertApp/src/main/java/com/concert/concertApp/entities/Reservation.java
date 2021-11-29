package com.concert.concertApp.entities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = "reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long reservationId ;


 //   @Column(name  = " user_id " )
  // @Column(name = "concert_id")



    @Column(name = "reservation_date")
    private Timestamp reservationDate ;


    @Column(name = "reservation_ispaid")
    private boolean reservationPaid ;


    @Column(name = "reservation_discount")
    private boolean reservationDiscount ;

    @ManyToOne
    @JoinColumn(name = "concertId")
    private Concert concert ;

    public Long getReservationId() {
        return reservationId;
    }

    public Timestamp getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(Timestamp reservationDate) {
        this.reservationDate = reservationDate;
    }

    public boolean isReservationPaid() {
        return reservationPaid;
    }

    public void setReservationPaid(boolean reservationPaid) {
        this.reservationPaid = reservationPaid;
    }

    public boolean isReservationDiscount() {
        return reservationDiscount;
    }

    public void setReservationDiscount(boolean reservationDiscount) {
        this.reservationDiscount = reservationDiscount;
    }

    public Reservation() {
    }
    public Concert getConcert() {
        return concert;
    }

    public void setConcert(Concert concert) {
        this.concert = concert;
    }


}
