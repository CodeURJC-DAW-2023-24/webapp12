package es.codeurjc.yourHOmeTEL.model;

import java.sql.Blob;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import io.micrometer.common.lang.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;


@Entity
public class Hotel {
   
   
    private String name;

    private String description;

    private String location;

    private int numRooms;

    private Blob imageHotel;

    private Date updateDate;

  
    @ManyToOne
    private User manager;


    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Room> rooms;


    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservation;


    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;


    public Hotel(String name, String description, String location, int numRooms, Blob imageHotel, Date updateDate,
            User manager, List<Room> rooms, List<Reservation> reservation, List<Review> reviews) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.numRooms = numRooms;
        this.imageHotel = imageHotel;
        this.updateDate = updateDate;
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


    public Date getUpdateDate() {
        return updateDate;
    }


    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }


    public User getManager() {
        return manager;
    }


    public void setManager(User manager) {
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

    

}