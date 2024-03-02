package es.codeurjc.yourHOmeTEL.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.codeurjc.yourHOmeTEL.model.Reservation;
import es.codeurjc.yourHOmeTEL.model.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    
    List<Room> findByMaxClients(int maxClients);
    Optional<Room> findById(Long id);
}
