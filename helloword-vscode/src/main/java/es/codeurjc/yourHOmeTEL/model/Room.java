package es.codeurjc.yourHOmeTEL.model;





import java.util.ArrayList;
import java.util.List;

import io.micrometer.common.lang.Nullable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Nullable
    @ManyToOne
    private Hotel hotel;

    @Nullable
    @ManyToMany
    private List<Reservation> reservations = new ArrayList<>();

    private Long numBeds;
    private Long maxClients;
    
    public Room(Long id, Hotel hotel, List<Reservation> reservations, Long numBeds, Long maxClients) {
        this.id = id;
        this.hotel = hotel;
        this.reservations = reservations;
        this.numBeds = numBeds;
        this.maxClients = maxClients;
    }

    

    
}
