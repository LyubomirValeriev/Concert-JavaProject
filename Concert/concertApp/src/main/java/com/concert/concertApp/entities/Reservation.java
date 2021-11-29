package com.concert.concertApp.entities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Entity
@Table(name = "reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long reservationId ;


   @OneToOne
   @JoinColumn(name = "user_Id")
   private User  user ;


    @Column(name = "reservation_date")
    private Timestamp reservationDate ;


    @Column(name = "reservation_ispaid")
    private boolean reservationPaid ;


    @Column(name = "reservation_discount")
    private boolean reservationDiscount ;

    @ManyToOne
    @JoinColumn(name = "concertId")
    private Concert concert ;

    @Column(name = "reservation_final_Price")
    private Double reservationFinalPrice ;

    public Double getReservationFinalPrice() {
        return reservationFinalPrice;
    }

    public void setReservationFinalPrice(Double reservationFinalPrice) {
        this.reservationFinalPrice = reservationFinalPrice;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public Timestamp getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(Timestamp reservationDate) {
        // ???
//        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm") ;
//        LocalDateTime date = LocalDateTime.parse((CharSequence) reservationDate, format);

        this.reservationDate = reservationDate ;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
