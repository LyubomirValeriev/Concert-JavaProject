package com.concert.concertApp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.procedure.ParameterMisuseException;
import org.springframework.http.ResponseEntity;
import javax.persistence.*;
import java.security.NoSuchAlgorithmException;

@Entity
@Table(name = "concertHall")
public class ConcertHall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long conHallId ;



    @Column(name = "con_hall_name")
    private  String conHallName ;


    @Column(name = "con_hall_adress")
    private  String conHallAdress ;





    @Column(name = "con_hall_capacity")
    private  Long conHallCapacity ;


    @ManyToOne
    @JoinColumn(name = "city_id" )
    private City city ;

    public  ConcertHall(){

    }

    public ConcertHall( String conHallName, String conHallAdress,  Long conHallCapacity)throws NoSuchAlgorithmException {

        //this.con_hall_id = con_hall_id;
        this.conHallName = conHallName;
        this.conHallAdress = conHallAdress;
       // this.conHallCity = conHallCity;
        this.conHallCapacity = conHallCapacity;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Long getConHallId() {
        return conHallId;
    }



    public String getConHallName() {
        return conHallName;
    }

    public void setConHallName(String conHallName) {
        this.conHallName = conHallName;
    }

    public String getConHallAdress() {
        return conHallAdress;
    }

    public void setConHallAdress(String conHallAdress) {
        this.conHallAdress = conHallAdress;
    }



    public Long getConHallCapacity() {
        return conHallCapacity;
    }

    public void setConHallCapacity(Long conHallCapacity) {
        if(conHallCapacity <= 0){
            throw  new ParameterMisuseException("Capacity cannot be 0 or negative number");

        }else if(conHallCapacity != conHallCapacity.intValue() ){
            //  conHallCapacity != (int)conHallCapacity
//            conHallCapacity != conHallCapacity.intValue()
            throw  new ParameterMisuseException("The number must be an integer!");
        }
        this.conHallCapacity = conHallCapacity;
    }




//  //  public static boolean city( String c ) {
//        return c.matches( "([a - zA - Z] + |[a - zA - Z] + \\s[a - zA - Z] + )" );
//    }
}
