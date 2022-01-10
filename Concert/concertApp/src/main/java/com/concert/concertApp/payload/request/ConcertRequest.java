package com.concert.concertApp.payload.request;

import java.sql.Timestamp;
import java.util.List;

public class ConcertRequest {

    private  String title;
    private  String description;
    private String price;
    private Timestamp date;
    private Long  conHallId ;
    List<String> performers;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
        this.price = price;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public List<String> getPerformers() {
        return performers;
    }

    public void setPerformers(List<String> performers) {
        this.performers = performers;
    }

    public Long getConHallId() {
        return conHallId;
    }

    public void setConHallId(Long conHallId) {
        this.conHallId = conHallId;
    }
}
