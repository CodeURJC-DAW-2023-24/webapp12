package es.codeurjc.yourHOmeTEL.model;

import java.sql.Blob;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.List;

import io.micrometer.common.lang.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;


@Entity
public class Reservation {

   

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idReservation;


    private LocalDate date;

    private int numPeople;

 
    @ManyToOne
    private Hotel hotel;

    
    @ManyToMany
    private ArrayList<Room> rooms;

 
    @ManyToOne
    private UserE user;


    public Reservation(){
        
    }
    
    public Reservation(LocalDate date, int numPeople, Hotel hotel, ArrayList<Room> rooms, UserE user) {
        this.date = date;
        this.numPeople = numPeople;
        this.hotel = hotel;
        this.rooms = rooms;
        this.user = user;
    }


    public Long getIdReservation() {
        return idReservation;
    }


    public void setIdReservation(Long idReservation) {
        this.idReservation = idReservation;
    }


    public LocalDate getDate() {
        return date;
    }


    public void setDate(LocalDate date) {
        this.date = date;
    }


    public int getNumPeople() {
        return numPeople;
    }


    public void setNumPeople(int numPeople) {
        this.numPeople = numPeople;
    }


    public Hotel getHotel() {
        return hotel;
    }


    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }


    public List<Room> getRooms() {
        return rooms;
    }


    public void setRooms(ArrayList<Room> rooms) {
        this.rooms = rooms;
    }


    public UserE getUser() {
        return user;
    }


    public void setUser(UserE user) {
        this.user = user;
    }



}
