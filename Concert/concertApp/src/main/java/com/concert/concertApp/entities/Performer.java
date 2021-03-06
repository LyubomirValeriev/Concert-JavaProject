package com.concert.concertApp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Performer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    private String name;

    public Performer() {

    }

    public Performer(String name) {
        this.setName(name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {return name;
    }

    public void setName(String name) {
        this.name = name.trim();
    }
}