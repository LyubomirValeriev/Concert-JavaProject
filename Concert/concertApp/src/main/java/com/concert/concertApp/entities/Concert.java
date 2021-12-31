package com.concert.concertApp.entities;

import org.hibernate.PropertyValueException;
import org.hibernate.engine.query.ParameterRecognitionException;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Set;
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

    @Column(nullable = false)
    private  String title;

    private  String description;

    @Column(nullable = false)
    private String price;

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
    }

    public Concert(String title, String description, String price, Timestamp date, Set<Performer> performers) {
            this.setTitle(title);
            this.description = description;
            this.setPrice(price);
            this.setDate(date);
            this.performers = performers;

    }


    public Concert(String title, String description, String price, Timestamp date) {
        this.title = title;
        this.description = description;
        this.setPrice(price);
        this.setDate(date);
    }

    public Long getId() {
        return concertId;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        if (price == null || price.isEmpty())
            throw new NullPointerException("Please enter the price of a concert");

        if (!isNumeric(price)) {
            throw new ParameterRecognitionException("Please check your price input");
        }
        this.price = price;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        if (date == null)
            throw new NullPointerException("Please enter a date for the concert");
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

    public static boolean isNumeric(String price) {
        try {
            Double.parseDouble(price);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}


