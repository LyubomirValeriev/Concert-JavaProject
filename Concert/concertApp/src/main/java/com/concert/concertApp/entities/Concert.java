package com.concert.concertApp.entities;

<<<<<<< HEAD
import org.hibernate.PropertyValueException;
import org.springframework.http.ResponseEntity;
=======
import com.fasterxml.jackson.annotation.JsonIgnore;
>>>>>>> Luybo-Concert-to-Hall

import javax.persistence.*;
import java.sql.SQLException;
import java.sql.Timestamp;
<<<<<<< HEAD
import java.util.Set;

@Entity
@Table(name = "concerts")
public class Concert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private  Long id;
=======
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "concert")
public class Concert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private  Long concertId;
>>>>>>> Luybo-Concert-to-Hall

    @Column(nullable = false)
    private  String title;

    private  String description;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Timestamp date;



    @ManyToOne
    @JoinColumn(name  = "conHallId")
    private ConcertHall hall ;

    @ManyToMany
    @JoinTable(
            name = "concert_performers",
            joinColumns = @JoinColumn(name = "concert_id"),
            inverseJoinColumns = @JoinColumn(name = "performer_id")
    )
    private Set<Performer> performers;


    public Concert() {
<<<<<<< HEAD
    }

    public Concert(String title, String description, Double price, Timestamp date, Set<Performer> performers) {
            this.setTitle(title);
            this.description = description;
            this.price = price;
            this.date = date;
            this.performers = performers;
=======

    }


    public Concert(String title, String description, Double price, Timestamp date) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.date = date;
    }

    public Long getId() {
        return concertId;
>>>>>>> Luybo-Concert-to-Hall
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public Set<Performer> getPerformers() {
        return performers;
    }

    public void setPerformers(Set<Performer> performers) {
        this.performers = performers;
    }

    public ConcertHall getHall() {
        return hall;
    }

    public void setHall(ConcertHall hall) {
        this.hall = hall;
    }
}
