package com.concert.concertApp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private  Long reservationId ;

    @Column(name = "reservation_date")
    private Date reservationDate ;


    @Column(name = "reservation_ispaid")
    private boolean reservationPaid ;


    @Column(name = "reservation_discount")
    private boolean reservationDiscount ;


    @Column(name = "reservation_final_Price")
    private Double reservationFinalPrice ;

    @ManyToOne
    @JoinColumn(name = "concertId")
    private Concert concert ;

    // username * по това да търсим кой ги е запазил а не по id
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "users_Id")
    private User  user ;

    public Reservation() {
    }

    public Reservation(Date reservationDate,
                       boolean reservationPaid,
                       boolean reservationDiscount,
                       Double reservationFinalPrice,
                       Concert concert,
                       User user
                       ) {
        this.reservationDate = reservationDate;
        this.reservationPaid = reservationPaid;
        this.reservationDiscount = reservationDiscount;
        this.reservationFinalPrice = reservationFinalPrice;
        this.concert = concert;
        this.user = user ;

    }


    public Double getReservationFinalPrice() {
        return reservationFinalPrice;
    }

    public void setReservationFinalPrice(Double reservationFinalPrice) {
        this.reservationFinalPrice = reservationFinalPrice;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public Date getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(Date reservationDate) {
        // ???
//        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm") ;


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
