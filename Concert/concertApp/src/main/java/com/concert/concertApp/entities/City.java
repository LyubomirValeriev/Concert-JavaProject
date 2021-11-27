package com.concert.concertApp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.engine.query.ParameterRecognitionException;

import javax.persistence.*;
import java.util.Set;
import java.util.regex.Pattern;

@Entity
@Table(name = "city")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private  Long id ;

    @Column(name = "city_name")
    private  String name ;

@OneToMany(mappedBy =  "city")
private Set<ConcertHall> hall ;

    public City() {
    }

    public City(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

     public String getName() {
        return name;
    }

    public void setName(String name)
    {
        if(isCityValid(name) == false )
            throw  new ParameterRecognitionException("Invalid City name format! Try again <3") ;
        this.name = name;
    }


    public static  boolean isCityValid(String checkCity ){
        String cityRegex =  "^[a-zA-Z]*$" ;
        Pattern pat = Pattern.compile(cityRegex);
        if(checkCity == null)
            return  false ;
        return  pat.matcher(checkCity).matches();
    }
}
