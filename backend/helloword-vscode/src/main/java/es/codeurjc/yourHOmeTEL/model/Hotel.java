package es.codeurjc.yourHOmeTEL.model;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonMerge;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Hotel {

    public interface Basic {}
    public interface Complete {}   

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView({Complete.class, Basic.class})
    private Long id;

    @JsonMerge
    @JsonView({Complete.class, Basic.class})
    private String name;

    @JsonMerge
    @JsonView({Complete.class, Basic.class})
    private String description;

    @JsonMerge
    @JsonView({Complete.class, Basic.class})
    private String location;

    @JsonView({Complete.class, Basic.class})
    private float rating;

    @Lob
    @JsonView({Complete.class, Basic.class})
    @JsonIgnore
    private Blob imageFile;

    @JsonView({Complete.class, Basic.class})
    private boolean image;

    @JsonView({Complete.class, Basic.class})
    private String imagePath;

    @ManyToOne
    @JsonView(Complete.class)
    private UserE manager;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonView(Complete.class)
    private List<Room> rooms;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonView(Complete.class)
    private List<Reservation> reservations;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonView(Complete.class)
    private List<Review> reviews;

    public Hotel() {

    }

    public Hotel(String name, String location, String description) {
        this.name = name;
        this.location = location;
        this.description = description;
    }

    public Hotel(String name, String description, String location, float rating, Blob imageHotel,
            UserE manager, List<Room> rooms, List<Reservation> reservations, List<Review> reviews) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.rating = rating;
        // this.imageHotel = imageHotel;
        this.manager = manager;
        this.rooms = rooms;
        this.reservations = reservations;
        this.reviews = reviews;
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
        return this.rooms.size();
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

    public UserE getManager() {
        return manager;
    }

    public void setManager(UserE manager) {
        this.manager = manager;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
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

    public float getrating() {
        return rating;
    }

    public void setrating(float rating) {
        this.rating = rating;
    }

    public float getAverageRating() {

        int numReviews = this.reviews.size();

        if (numReviews < 1) {
            return 0f;
        } else {
            int total = 0;
            for (Review review : this.reviews) {
                total += review.getScore();
            }

            return total / numReviews;
        }
    }

    public int getPercentageOfNScore(int score) {

        int numReviews = this.reviews.size();

        if (numReviews > 0) {

            int numberOfNScoreReview = 0;

            for (Review review : this.reviews) {
                if (review.getScore() == score) {
                    numberOfNScoreReview++;
                }
            }

            return numberOfNScoreReview * 100 / numReviews;
        } else {
            return 0;
        }
    }

}
