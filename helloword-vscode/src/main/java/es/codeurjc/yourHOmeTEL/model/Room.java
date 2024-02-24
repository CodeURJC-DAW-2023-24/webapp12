package es.codeurjc.yourHOmeTEL.model;





import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int numBeds;

    private int maxClients;

    
    @OneToMany(mappedBy = "room")
    private ArrayList<Reservation> reservations;


    public Room(){
        
    }

    public Room(int numBeds, int maxClients, ArrayList<Reservation> reservations) {
        this.numBeds = numBeds;
        this.maxClients = maxClients;
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


    public List<Reservation> getReservations() {
        return reservations;
    }


    public void setReservations(ArrayList<Reservation> reservations) {
        this.reservations = reservations;
    }


    


    

    
}
