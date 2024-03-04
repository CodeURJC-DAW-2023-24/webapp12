package es.codeurjc.yourhometel.backend.model;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;

@Entity
public class UserE {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String lastname;

    private String bio;

    private String location;

    private String language;

    private String phone;

    private String email;

    @Lob
    private Blob imageFile;

    private boolean image;

    private String organization;

    private Boolean validated;

    private Boolean rejected;

    private List<String> rols;

    private String nick;

    private String pass;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Reservation> reservations;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Review> reviews;

    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Hotel> hotels;

    public UserE() {

    }

    public UserE(String nick, String name, String lastname, String email, String pass) {
        this.nick = nick;
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.pass = pass;
        this.hotels = new ArrayList<>();
        this.reviews = new ArrayList<>();
        this.reservations = new ArrayList<>();
    }

    public UserE(String name, String lastname, String bio, String location, String language, String phone, String email,
            String organization,
            Blob imageFile, String nick, String pass, Boolean validated, Boolean rejected, List<String> rols,
            List<Reservation> reservations, List<Review> reviews,
            List<Hotel> hotels) {

        this.name = name;
        this.lastname = lastname;
        this.bio = bio;
        this.location = location;
        this.language = language;
        this.phone = phone;
        this.email = email;
        this.organization = organization;
        this.imageFile = imageFile;
        this.rols = rols;
        this.nick = nick;
        this.pass = pass;
        this.validated = validated;
        this.rejected = rejected;
        this.reservations = reservations;
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

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
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

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public Blob getImageFile() {
        return imageFile;
    }

    public void setImageFile(Blob imageFile) {
        this.imageFile = imageFile;
    }

    public boolean isImage() {
        return image;
    }

    public void setImage(boolean image) {
        this.image = image;
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

    public Boolean getvalidated() {
        return validated;
    }

    public void setvalidated(Boolean validated) {
        this.validated = validated;
    }

    public Boolean getRejected() {
        return rejected;
    }

    public void setRejected(Boolean rejected) {
        this.rejected = rejected;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
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

    public List<String> getRols() {
        return this.rols;
    }

    public void setRols(List<String> rols) {
        this.rols = rols;
    }

}
