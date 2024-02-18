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
    private Long idHotel;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idUser;




    @Nullable
    @ManyToOne
    private User user;

    private int score;
    private String comment;
    private Date date;
    public Review(Long idHotel, Long idUser, int score, String comment, Date date) {
        this.idHotel = idHotel;
        this.idUser = idUser;
        this.score = score;
        this.comment = comment;
        this.date = date;
    }


    
}
