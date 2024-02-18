package es.codeurjc.yourHOmeTEL.model;



import java.sql.Date;

import io.micrometer.common.lang.Nullable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idReview;


    @Nullable
    @ManyToOne
    private Hotel hotel;

    @Nullable
    @ManyToOne
    private User user;

    private int score;
    private String comment;
    private Date date;
    
    public Review(Long idReview, Hotel hotel, User user, int score, String comment, Date date) {
        this.idReview = idReview;
        this.hotel = hotel;
        this.user = user;
        this.score = score;
        this.comment = comment;
        this.date = date;
    }
    


    
}
