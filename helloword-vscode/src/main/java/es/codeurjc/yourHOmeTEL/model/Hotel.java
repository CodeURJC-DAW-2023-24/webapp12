package es.codeurjc.yourHOmeTEL.model;

import java.sql.Blob;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String description;

    private String location;

    private float rating;


    // private Blob imageHotel;

    @Lob
    private Blob imageFile;

    private boolean image;

    @ManyToOne
    private UserE manager;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Room> rooms;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Reservation> reservations;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Review> reviews;

    public Hotel() {

    }

    public Hotel(String name, String location, String description) {
        this.name = name;
        this.location = location;
        this.description = description;
    }

    public Hotel(String name, String description, String location, float rating, Blob imageHotel,
            UserE manager, List<Room> rooms, List<Reservation> reservations, List<Review> reviews) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.rating = rating;
        // this.imageHotel = imageHotel;
        this.manager = manager;
        this.rooms = rooms;
        this.reservations = reservations;
        this.reviews = reviews;
    }

    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getNumRooms() {
        return this.rooms.size();
    }

    public Blob getImageFile() {
        return imageFile;
    }

    public void setImageFile(Blob imageFile) {
        this.imageFile = imageFile;
    }

    public boolean isImage() {
        return image;
    }

    public void setImage(boolean image) {
        this.image = image;
    }

    public UserE getManager() {
        return manager;
    }

    public void setManager(UserE manager) {
        this.manager = manager;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public float getrating() {
        return rating;
    }

    public void setrating(float rating) {
        this.rating = rating;
    }

    public float getAverageRating() {

        int numReviews = this.reviews.size();

        if (numReviews < 1) {
            return 0f;
        } else {
            int total = 0;
            for (Review review : this.reviews) {
                total += review.getScore();
            }

            return total / numReviews;
        }
    }

    public int getPercentageOfNScore(int score) {

        int numReviews = this.reviews.size();

        if (numReviews > 0) {

            int numberOfNScoreReview = 0;

            for (Review review : this.reviews) {
                if (review.getScore() == score) {
                    numberOfNScoreReview++;
                }
            }

            return numberOfNScoreReview * 100 / numReviews;
        } else {
            return 0;
        }
    }

}
