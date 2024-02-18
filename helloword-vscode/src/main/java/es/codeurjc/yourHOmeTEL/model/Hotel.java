package es.codeurjc.yourHOmeTEL.model;

import java.sql.Blob;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import io.micrometer.common.lang.Nullable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idHotel;

    @Nullable
    @ManyToOne
    private User manager;

    @Nullable
    @OneToMany
    private List<Room> rooms = new ArrayList<>();

    @Nullable
    @OneToMany
    private List<Reservation> reservation = new ArrayList<>();

    @Nullable
    @OneToMany
    private List<Review> reviews = new ArrayList<>();

    private String name;
    private String description;
    private String location;
    private int numRooms;
    private Blob imageHotel;
    private Date updateDate;
    
    public Hotel(Long idHotel, User manager, List<Room> rooms, List<Reservation> reservation, List<Review> reviews,
            String name, String description, String location, int numRooms, Blob imageHotel, Date updateDate) {
        this.idHotel = idHotel;
        this.manager = manager;
        this.rooms = rooms;
        this.reservation = reservation;
        this.reviews = reviews;
        this.name = name;
        this.description = description;
        this.location = location;
        this.numRooms = numRooms;
        this.imageHotel = imageHotel;
        this.updateDate = updateDate;
    }

    


    




}
