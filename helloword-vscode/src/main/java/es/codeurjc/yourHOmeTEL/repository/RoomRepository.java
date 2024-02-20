package es.codeurjc.yourHOmeTEL.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.codeurjc.yourHOmeTEL.model.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findByHotel_Name(String name);
    List<Room> findByMaxClients(int maxClients);
    List<Room> findBynumBeds(int numBeds);
}
