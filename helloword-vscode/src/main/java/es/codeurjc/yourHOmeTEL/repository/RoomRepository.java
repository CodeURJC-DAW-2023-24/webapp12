package es.codeurjc.yourHOmeTEL.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.codeurjc.yourHOmeTEL.model.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    
    List<Room> findByMaxClients(int maxClients);
}
