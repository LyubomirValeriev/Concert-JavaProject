package com.concert.concertApp.entities;


import javax.persistence.*;

@Entity
@Table(name = "city")
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id ;

    @Column(name = "city_name")
    private  String name ;



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

    public void setName(String name) {
        this.name = name;
    }
}
