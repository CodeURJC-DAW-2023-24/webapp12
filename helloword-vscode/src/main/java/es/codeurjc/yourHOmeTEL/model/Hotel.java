package es.codeurjc.yourHOmeTEL.model;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    private int numRooms;

    private float rating;
    //añadir rating y cambiar la base de datos



    private Blob imageHotel;


    @ManyToOne
    private UserE manager;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Room> rooms;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Reservation> reservation;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Review> reviews;


    public Hotel(){
        
    }    

    public Hotel(String name, String location, String description){
        this.name = name;
        this.location = location;
        this.description = description;
    } 
    
    public Hotel(String name, String description, String location, int numRooms,float rating, Blob imageHotel,
            UserE manager, List<Room> rooms, List<Reservation> reservation, List<Review> reviews) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.numRooms = numRooms;
        this.rating = rating;
        this.imageHotel = imageHotel;
        this.manager = manager;
        this.rooms = rooms;
        this.reservation = reservation;
        this.reviews = reviews;
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


    public int getnumRooms() {
        return numRooms;
    }


    public void setnumRooms(int numRooms) {
        this.numRooms = numRooms;
    }


    public Blob getImageHotel() {
        return imageHotel;
    }


    public void setImageHotel(Blob imageHotel) {
        this.imageHotel = imageHotel;
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


    public List<Reservation> getReservation() {
        return reservation;
    }


    public void setReservation(List<Reservation> reservation) {
        this.reservation = reservation;
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
        rating = rating;
    }


    //PENDIENTE añadir métodos sobre las reseñas, como la media de reseñas, y ordenarlas

}
