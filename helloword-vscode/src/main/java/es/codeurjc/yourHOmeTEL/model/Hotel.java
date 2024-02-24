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
    private Long idReview; 
    
    private String name;

    private String description;

    private String location;

    private int numRooms;

    private Blob imageHotel;


    @ManyToOne
    private UserE manager;

    @OneToMany
    private ArrayList<Room> rooms;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private ArrayList<Reservation> reservation;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private ArrayList<Review> reviews;


    public Hotel(){
        
    }    
    
    public Hotel(String name, String description, String location, int numRooms, Blob imageHotel,
            UserE manager, ArrayList<Room> rooms, ArrayList<Reservation> reservation, ArrayList<Review> reviews) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.numRooms = numRooms;
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


    public int getNumRooms() {
        return numRooms;
    }


    public void setNumRooms(int numRooms) {
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


    public void setRooms(ArrayList<Room> rooms) {
        this.rooms = rooms;
    }


    public List<Reservation> getReservation() {
        return reservation;
    }


    public void setReservation(ArrayList<Reservation> reservation) {
        this.reservation = reservation;
    }


    public List<Review> getReviews() {
        return reviews;
    }


    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    //PENDIENTE añadir métodos sobre las reseñas, como la media de reseñas, y ordenarlas

}
