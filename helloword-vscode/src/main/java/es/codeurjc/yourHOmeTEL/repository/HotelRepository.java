package es.codeurjc.yourHOmeTEL.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.codeurjc.yourHOmeTEL.model.Hotel;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    List<Hotel> findByName(String name);
    List<Hotel> findByLocation(String location);
    List<Hotel> findBynumRooms(int numRooms);
}
