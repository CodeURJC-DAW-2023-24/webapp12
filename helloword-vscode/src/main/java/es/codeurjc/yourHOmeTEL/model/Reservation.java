package es.codeurjc.yourHOmeTEL.model;

import java.sql.Blob;
import java.util.Date;

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
    private User idUser;


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private User idHotel;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private User idRoom;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Date date;


    @Nullable
    @ManyToOne
    private User user;


    private int numPeople;



    public Reservation(User idUser, User idHotel, User idRoom, Date date, int numPeople) {
        this.idUser = idUser;
        this.idHotel = idHotel;
        this.idRoom = idRoom;
        this.date = date;
        this.numPeople = numPeople;
    }




}
