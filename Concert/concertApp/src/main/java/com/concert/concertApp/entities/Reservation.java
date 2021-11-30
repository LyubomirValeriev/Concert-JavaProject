package com.concert.concertApp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Entity
@Table(name = "reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private  Long reservationId ;

    @Column(name = "reservation_date")
    private Timestamp reservationDate ;


    @Column(name = "reservation_ispaid")
    private boolean reservationPaid ;


    @Column(name = "reservation_discount")
    private boolean reservationDiscount ;


    @Column(name = "reservation_final_Price")
    private Double reservationFinalPrice ;

    @ManyToOne
    @JoinColumn(name = "concertId")
    private Concert concert ;
    
    @JsonIgnore
    @Column(name = "reservationUserId")
    private Integer  user ;

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
       LocalDateTime date =  reservationDate.toLocalDateTime();

        this.reservationDate = reservationDate.from(Instant.now()) ;
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

    public Integer getUser() {
        return user;
    }

    public void setUser(Integer user) {
        this.user = user;
    }

}
