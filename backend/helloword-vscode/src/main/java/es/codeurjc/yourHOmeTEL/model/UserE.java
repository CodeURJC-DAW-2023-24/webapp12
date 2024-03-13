package es.codeurjc.yourHOmeTEL.model;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;

@Entity
public class UserE {

    public interface Complete{}
    public interface Basic{}


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView({Complete.class, Basic.class})
    private Long id;

    @JsonView({Complete.class, Basic.class})
    private String name;

    @JsonView({Complete.class, Basic.class})
    private String lastname;

    @JsonView({Complete.class, Basic.class})
    private String bio;

    @JsonView({Complete.class, Basic.class})
    private String location;

    @JsonView({Complete.class, Basic.class})
    private String language;

    @JsonView({Complete.class, Basic.class})
    private String phone;

    @JsonView({Complete.class, Basic.class})
    private String email;

    @JsonIgnore
    @Lob
    private Blob imageFile;

    @JsonView({Complete.class, Basic.class})
    private String imagePath;

    @JsonView({Complete.class, Basic.class})
    private boolean image;

    @JsonView({Complete.class, Basic.class})
    private String organization;

    @JsonView({Complete.class, Basic.class})
    private Boolean validated;

    @JsonView({Complete.class, Basic.class})
    private Boolean rejected;

    @ElementCollection
    @JsonView({Complete.class, Basic.class})
    private List<String> collectionRols; //necessary to implement queries that requiere a collection of roles

    @JsonView({Complete.class, Basic.class})
    private List<String> rols; //must stay, even if duplicated, since authorization desnt work correctly with the roles collection

    @JsonView({Complete.class, Basic.class})
    private String nick;

    private String pass;

    @JsonView(Complete.class)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations;

    @JsonView(Complete.class)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;

    @JsonView(Complete.class)
    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL, orphanRemoval = true)
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
            String organization, Blob imageFile, String nick, String pass, Boolean validated, Boolean rejected, 
            List<String> rols, List<String> collectionRols, List<Reservation> reservations, List<Review> reviews, List<Hotel> hotels) {

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
        this.collectionRols = collectionRols;
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

    @JsonIgnore
    public Blob getImageFile() {
        return imageFile;
    }

    // Include the Base64 image data in JSON
    @JsonIgnore(false)
    public String getImageBase64() {
        if (this.imageFile != null) {
            try {
                byte[] bytes = this.imageFile.getBytes(1, (int) this.imageFile.length());
                return Base64.getEncoder().encodeToString(bytes);
            } catch (SQLException e) {
                // handle exception
            }
        }
        return null;
    }


    public void setImageFile(Blob imageFile) {
        this.imageFile = imageFile;
    }

    public void generateImage(String staticPath) throws IOException {
        Resource image = new ClassPathResource(staticPath);
        this.setImageFile(BlobProxy.generateProxy(image.getInputStream(), image.contentLength()));
        this.setImage(true);         
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
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

    public List<String> getCollectionRols() {
        return collectionRols;
    }

    public void setCollectionRols(List<String> collectionRols) {
        this.collectionRols = collectionRols;
    }

}
