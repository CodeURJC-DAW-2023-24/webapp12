package es.codeurjc.yourHOmeTEL.model;





import java.time.LocalDate;
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

    private int maxClients;

    private float cost;

    @OneToMany(mappedBy = "room")
    private List<Reservation> reservations;

    @ManyToOne
    private Hotel hotel;


    public Room(){
        
    }

    public Room(int maxClients, float cost, List<Reservation> reservations, Hotel hotel) {
        this.maxClients = maxClients;
        this.cost = cost;
        this.reservations = reservations;
        this.hotel = hotel;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public int getMaxClients() {
        return maxClients;
    }


    public void setMaxClients(int maxClients) {
        this.maxClients = maxClients;
    }

    public float getcost() {
        return cost;
    }

    public void setcost(float cost) {
        this.cost = cost;
    }


    public List<Reservation> getReservations() {
        return reservations;
    }


    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }


    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public boolean available(LocalDate checkIn, LocalDate checkOut) {
        Boolean available = true;
        int i = 0;
        while(i < this.reservations.size() && available){
            if (checkIn.isAfter(this.reservations.get(i).getCheckIn()) && checkIn.isBefore(this.reservations.get(i).getCheckOut()))
                available = false;
            else if (checkOut.isAfter(this.reservations.get(i).getCheckIn()) && checkOut.isBefore(this.reservations.get(i).getCheckOut()))
                available = false;
            else
                i++;
        }
        return available;
    }
}
