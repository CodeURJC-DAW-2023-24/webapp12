package es.codeurjc.yourHOmeTEL.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.codeurjc.yourHOmeTEL.model.Hotel;
import es.codeurjc.yourHOmeTEL.model.UserE;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    List<Hotel> findByName(String name);
    List<Hotel> findByLocation(String location);
    List<Hotel> findTop6ByManager_Validated(Boolean validated);
    List<Hotel> findTop6ByManager_ValidatedAndNameContainingIgnoreCaseOrderByNameDesc(Boolean validated, String searchValue);
}

