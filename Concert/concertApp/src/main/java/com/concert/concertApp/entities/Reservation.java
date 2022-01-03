package com.concert.concertApp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.procedure.ParameterMisuseException;
import org.hibernate.procedure.ParameterStrategyException;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Set;

import static com.concert.concertApp.entities.ConcertHall.isValidNumber;

@Entity
@Table(name = "reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private  Long reservationId ;

    @Column(name = "reservation_date")
    private Date reservationDate ;



    @Column(name = "reservation_discount")
    private boolean reservationDiscount ;


    @Column(name = "reservation_final_Price")
    private Double reservationFinalPrice ;

    @Column(name = "reservation_number_of_tickets" , nullable = false )
    private  String  reservationTickets ;

    @ManyToOne
    @JoinColumn(name = "concertId")
    private Concert concert ;


    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "users_Id")
    private User  user ;


    @ManyToOne
    @JoinColumn(name = "discounts_Id")
    private  Discount discount ;

    public Reservation() {
    }

    public Reservation(String reservationTickets ,
                        Date reservationDate,

                       boolean reservationDiscount,
                       Double reservationFinalPrice,
                       Concert concert,
                       User user,
                       Discount discount
                       ) {
        this.setReservationTickets(reservationTickets);
        this.reservationDate = reservationDate;
        this.reservationDiscount = reservationDiscount;
        this.reservationFinalPrice = reservationFinalPrice;
        this.concert = concert;
        this.user = user ;
        this.discount = discount ;

    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    public Reservation(Double reservationFinalPrice   ) {
        this.reservationFinalPrice = reservationFinalPrice;

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

    public String getReservationTickets() {
        return reservationTickets;
    }

    public void setReservationTickets(String reservationTickets) {
        Integer tickets = 0 ;
        if(isValidNumber(reservationTickets) == false ){
            throw  new ParameterMisuseException("Моля въведете само числа за капацитета на залата  !") ;
        }
        else {
            tickets = Integer.parseInt(reservationTickets);
        }
        if(tickets <= 0){
            throw  new ParameterMisuseException("Capacity cannot be 0 or negative number");

        }else if(tickets != tickets.intValue() ){
            //  conHallCapacity != (int)conHallCapacity
//            conHallCapacity != conHallCapacity.intValue()
            throw  new ParameterMisuseException("The number must be an integer!");
        }
        this.reservationTickets = reservationTickets;
    }

    public  void checkedCapacity(Integer reservedTickets){
        if(
                concert.getReservedTickets() + reservedTickets >
                        Integer.parseInt(concert.getHall().getConHallCapacity())){

            throw  new ParameterStrategyException("Няма място вече във залата, моля опитайте по-късно :_( ") ;

        }else if(
                concert.getReservedTickets() + reservedTickets <=
                        Integer.parseInt(concert.getHall().getConHallCapacity()))
        {
            reservedTickets = reservedTickets + concert.getReservedTickets() ;
         concert.setReservedTickets(reservedTickets);
        }
    }

    public void freeUpSpace(Integer reservedTickets){

        Integer fus =   concert.getReservedTickets() - reservedTickets ;
        concert.setReservedTickets(
                fus
        );
    }
}
