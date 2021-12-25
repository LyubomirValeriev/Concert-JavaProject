package com.concert.concertApp.payload;

import com.concert.concertApp.entities.City;
import org.hibernate.engine.query.ParameterRecognitionException;
import org.hibernate.procedure.ParameterMisuseException;
import org.springframework.lang.NonNull;

import java.util.regex.Pattern;

public class ConcertHallRequest {

    String conHallName ;

    String city ;

    Long capacity ;

    String adress ;

    public String getConHallName() {
        return conHallName;
    }

    public void setConHallName(String conHallName) {


        this.conHallName = conHallName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Long getCapacity() {
        return capacity;
    }

    public void setCapacity(Long capacity) {
        if(capacity <= 0){

        }
        this.capacity = capacity;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }




}
