package es.codeurjc.yourHOmeTEL.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonView;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Review {

    public interface Basic{}

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(Basic.class)
    private Long id;

    @JsonView(Basic.class)
    private int score;

    @JsonView(Basic.class)
    private String comment;

    @JsonView(Basic.class)
    private LocalDate date;

    @ManyToOne
    private Hotel hotel;

    @ManyToOne
    private UserE user;

    public Review() {

    }

    public Review(int score, String comment, LocalDate date, Hotel hotel, UserE user) {
        this.score = score;
        this.comment = comment;
        this.date = date;
        this.hotel = hotel;
        this.user = user;
    }

    public Long getIdReview() {
        return id;
    }

    public void setIdReview(Long id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public UserE getUser() {
        return user;
    }

    public void setUser(UserE user) {
        this.user = user;
    }

}
