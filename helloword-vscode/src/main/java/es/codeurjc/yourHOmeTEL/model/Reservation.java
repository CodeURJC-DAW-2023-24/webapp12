package es.codeurjc.yourHOmeTEL.model;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.micrometer.common.lang.Nullable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

public class Reservation {

   

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private User idReservation;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Date date;

    @Nullable
    @ManyToOne
    private Hotel hotel;

    @Nullable
    @ManyToMany
    private List<Room> rooms = new ArrayList<>();

    @Nullable
    @ManyToOne
    private User user;


    private int numPeople;


    public Reservation(User idReservation, Date date, Hotel hotel, List<Room> rooms, User user, int numPeople) {
        this.idReservation = idReservation;
        this.date = date;
        this.hotel = hotel;
        this.rooms = rooms;
        this.user = user;
        this.numPeople = numPeople;
    }



    




}
