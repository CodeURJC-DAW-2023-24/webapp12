package es.codeurjc.yourHOmeTEL.model;





import java.util.List;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int numBeds;

    private int maxClients;

    
    @ManyToOne
    private Hotel hotel;

    
    @ManyToMany(mappedBy = "rooms")
    private List<Reservation> reservations;


    public Room(){
        
    }

    public Room(Long id, int numBeds, int maxClients, Hotel hotel, List<Reservation> reservations) {
        this.id = id;
        this.numBeds = numBeds;
        this.maxClients = maxClients;
        this.hotel = hotel;
        this.reservations = reservations;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public int getNumBeds() {
        return numBeds;
    }


    public void setNumBeds(int numBeds) {
        this.numBeds = numBeds;
    }


    public int getMaxClients() {
        return maxClients;
    }


    public void setMaxClients(int maxClients) {
        this.maxClients = maxClients;
    }


    public Hotel getHotel() {
        return hotel;
    }


    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }


    public List<Reservation> getReservations() {
        return reservations;
    }


    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }


    


    

    
}
