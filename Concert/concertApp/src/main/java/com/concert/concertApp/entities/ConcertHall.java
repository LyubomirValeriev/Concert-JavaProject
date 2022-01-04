package com.concert.concertApp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.engine.query.ParameterRecognitionException;
import org.hibernate.procedure.ParameterMisuseException;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

@Entity
@Table(name = "concertHall")
public class ConcertHall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private  Long conHallId ;



    @Column(name = "con_hall_name",nullable = false, length =  40)
    private  String conHallName ;


    @Column(name = "con_hall_address",nullable = false , length =  30)
    private  String conHallAddress ;

    @Column(name = "con_hall_capacity",nullable = false)
    @JsonIgnore
    private  String conHallCapacity ;


    @ManyToOne
    @JoinColumn(name = "city_id" )
    @NonNull
    private City city ;

    public  ConcertHall(){

    }

    public ConcertHall( String conHallName,
                        String conHallAddress,
                        String conHallCapacity
            , City city) {

        this.setConHallName(conHallName);
        this.conHallAddress = conHallAddress.trim();
        this.setCity(city);
        this.setConHallCapacity(conHallCapacity);
        //this.conHallCapacity = conHallCapacity;
        // this.city = city;
        // this.conHallName = conHallName;
    }

    public ConcertHall(String conHallName, String conHallAddress ,String conHallCapacity ) {
        this.conHallName = conHallName;
        this.conHallAddress = conHallAddress;
        this.conHallCapacity = conHallCapacity ;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        if(city == null)
            throw  new NullPointerException("Please enter name of city") ;
        this.city = city;
    }

    public Long getConHallId() {
        return conHallId;
    }



    public String getConHallName() {
        return conHallName;
    }

    public void setConHallName(String conHallName) {

        if(conHallName== null
                || conHallName.isEmpty())
        throw new NullPointerException("Please enter name");

        if(isNameValid(conHallName) == false )
            throw  new ParameterRecognitionException("Еnter a valid name <3") ;

        this.conHallName = conHallName.trim();
    }

    public String getConHallAddress() {
        return conHallAddress;
    }

    public void setConHallAddress(String conHallAddress) {
        this.conHallAddress = conHallAddress;
    }



    public String getConHallCapacity() {
        return conHallCapacity;
    }

    public void setConHallCapacity(String conHallCapacity) {
      Integer conCap = 0 ;
        if(isValidNumber(conHallCapacity) == false ){
            throw  new ParameterMisuseException("Please enter only integers for the capacity of the hall!") ;
        }
        else {
            conCap = Integer.parseInt(conHallCapacity);
        }
        if(conCap <= 0){
            throw  new ParameterMisuseException("Capacity cannot be 0 or negative number!");

        }else if(conCap != conCap.intValue() ){
            //  conHallCapacity != (int)conHallCapacity
//            conHallCapacity != conHallCapacity.intValue()
            throw  new ParameterMisuseException("The number must be an integer!");
        }
        this.conHallCapacity = conHallCapacity;
    }
    public static  boolean isNameValid(String checkCity ){
        String nameRegex =  "^[a-zA-Z]*$" ;
        Pattern pat = Pattern.compile(nameRegex);
        if(checkCity == null)
            return  false ;
        return  pat.matcher(checkCity).matches();
    }

 public static  boolean isValidNumber(String capacity){
        try {
            Integer.parseInt(capacity);
            return true;
        }catch (Exception e){
            return  false;
 }
    }

//  //  public static boolean city( String c ) {
//        return c.matches( "([a - zA - Z] + |[a - zA - Z] + \\s[a - zA - Z] + )" );
//    }
}
