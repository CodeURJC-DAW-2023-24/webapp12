package es.codeurjc.yourHOmeTEL.model;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

import io.micrometer.common.lang.Nullable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String bio;
    private String location;
    private String language;
    private String phone;
    private String email;
    private Blob profileImg;

    @Nullable
    @OneToMany
    private List<Reservation> reservation = new ArrayList<>();

    @Nullable
    @OneToMany
    private List<Review> reviews = new ArrayList<>();

    @Nullable
    @OneToMany
    private List<Hotel> hotels = new ArrayList<>();

    private List<String> rols;
    private String nick;
    private String pass;
   
    public User(Long id, String name, String bio, String location, String language, String phone, String email,
            Blob profileImg, List<Reservation> reservation, List<Review> reviews, List<Hotel> hotels, List<String> rols,
            String nick, String pass) {
        this.id = id;
        this.name = name;
        this.bio = bio;
        this.location = location;
        this.language = language;
        this.phone = phone;
        this.email = email;
        this.profileImg = profileImg;
        this.reservation = reservation;
        this.reviews = reviews;
        this.hotels = hotels;
        this.rols = rols;
        this.nick = nick;
        this.pass = pass;
    }

    public List<String> getRols(){
        return this.rols;
    }   
}
