package es.codeurjc.yourHOmeTEL.model;

import java.sql.Blob;
import java.sql.Date;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idHotel;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private User idManager;




    private String name;
    private String description;
    private String location;
    private int numRooms;
    private Blob imageHotel;
    private Date updateDate;

    


    public Hotel(Long idHotel, User idManager, String name, String description, String location, int numRooms,
            Blob imageHotel, Date updateDate) {
        this.idHotel = idHotel;
        this.idManager = idManager;
        this.name = name;
        this.description = description;
        this.location = location;
        this.numRooms = numRooms;
        this.imageHotel = imageHotel;
        this.updateDate = updateDate;
    }




}
