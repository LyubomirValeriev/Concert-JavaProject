package com.concert.concertApp.entities;


import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "city")
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id ;

    @Column(name = "city_name")
    private  String name ;

@OneToMany(mappedBy = "city")
private Set<ConcertHall> halls ;



    public City() {
    }

    public City(String name) {
        this.name = name;
    }

    public Set<ConcertHall> getHalls() {
        return halls;
    }

    public void setHalls(Set<ConcertHall> halls) {
        this.halls = halls;
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
