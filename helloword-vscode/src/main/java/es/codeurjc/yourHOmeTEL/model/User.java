package es.codeurjc.yourHOmeTEL.model;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

import io.micrometer.common.lang.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;


@Entity
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

    private List<String> rols;

    private String nick;

    private String pass;

    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservation;

    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;

    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Hotel> hotels;


    public User(Long id, String name, String bio, String location, String language, String phone, String email,
            Blob profileImg,  List<String> rols, String nick, String pass, List<Reservation> reservation, List<Review> reviews,
            List<Hotel> hotels) {
        this.id = id;
        this.name = name;
        this.bio = bio;
        this.location = location;
        this.language = language;
        this.phone = phone;
        this.email = email;
        this.profileImg = profileImg;
        this.rols = rols;
        this.nick = nick;
        this.pass = pass;
        this.reservation = reservation;
        this.reviews = reviews;
        this.hotels = hotels;
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


    public String getBio() {
        return bio;
    }


    public void setBio(String bio) {
        this.bio = bio;
    }


    public String getLocation() {
        return location;
    }


    public void setLocation(String location) {
        this.location = location;
    }


    public String getLanguage() {
        return language;
    }


    public void setLanguage(String language) {
        this.language = language;
    }


    public String getPhone() {
        return phone;
    }


    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getEmail() {
        return email;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    public Blob getProfileImg() {
        return profileImg;
    }


    public void setProfileImg(Blob profileImg) {
        this.profileImg = profileImg;
    }






    public String getNick() {
        return nick;
    }


    public void setNick(String nick) {
        this.nick = nick;
    }


    public String getPass() {
        return pass;
    }


    public void setPass(String pass) {
        this.pass = pass;
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


    public List<Hotel> getHotels() {
        return hotels;
    }


    public void setHotels(List<Hotel> hotels) {
        this.hotels = hotels;
    }

    public List<String> getRols(){
        return this.rols;
    }   
}
