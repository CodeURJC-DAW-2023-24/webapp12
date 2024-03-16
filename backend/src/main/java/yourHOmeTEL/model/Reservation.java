package yourHOmeTEL.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonMerge;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Reservation {

    public interface Basic {}
    public interface Complete{}

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView({Complete.class, Basic.class})
    private Long id;

    @JsonMerge
    @JsonView({Complete.class, Basic.class})
    private LocalDate checkIn;

    @JsonMerge
    @JsonView({Complete.class, Basic.class})
    private LocalDate checkOut;

    @JsonView({Complete.class, Basic.class})
    private int numPeople;

    @ManyToOne
    @JsonView(Complete.class)
    private Hotel hotel;

    @ManyToOne
    @JsonView(Complete.class)
    private Room room;

    @ManyToOne
    @JsonView(Complete.class)
    private UserE user;

    public Reservation() {

    }

    public Reservation(LocalDate checkIn, LocalDate checkOut, int numPeople, Hotel hotel, Room room, UserE user) {
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.numPeople = numPeople;
        this.hotel = hotel;
        this.room = room;
        this.user = user;
    }

    public Long getIdReservation() {
        return id;
    }

    public void setIdReservation(Long id) {
        this.id = id;
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

    public Room getRooms() {
        return room;
    }

    public void setRooms(Room room) {
        this.room = room;
    }

    public UserE getUser() {
        return user;
    }

    public void setUser(UserE user) {
        this.user = user;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
    }

}
