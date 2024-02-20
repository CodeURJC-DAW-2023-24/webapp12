package es.codeurjc.yourHOmeTEL.model;



import java.sql.Date;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;



@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idReview;
    

    private int score;

    private String comment;

    private Date date;

    @ManyToOne
    private Hotel hotel;

    
    @ManyToOne
    private User user;

    public Review(){
        
    }


    public Review(Long idReview, int score, String comment, Date date, Hotel hotel, User user) {
        this.idReview = idReview;
        this.score = score;
        this.comment = comment;
        this.date = date;
        this.hotel = hotel;
        this.user = user;
    }


    public Long getIdReview() {
        return idReview;
    }


    public void setIdReview(Long idReview) {
        this.idReview = idReview;
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


    public Date getDate() {
        return date;
    }


    public void setDate(Date date) {
        this.date = date;
    }


    public Hotel getHotel() {
        return hotel;
    }


    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }


    public User getUser() {
        return user;
    }


    public void setUser(User user) {
        this.user = user;
    }

    


    
    


    
}
