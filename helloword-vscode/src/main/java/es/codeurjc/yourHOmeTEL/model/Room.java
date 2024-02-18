package es.codeurjc.yourHOmeTEL.model;





import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long numBeds;
    private Long maxClients;
    public Room(Long id, Long numBeds, Long maxClients) {
        this.id = id;
        this.numBeds = numBeds;
        this.maxClients = maxClients;
    }

    
}
