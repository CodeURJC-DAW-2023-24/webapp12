package es.codeurjc.yourHOmeTEL.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.codeurjc.yourHOmeTEL.model.Hotel;
import es.codeurjc.yourHOmeTEL.model.UserE;

@Repository
public interface UserRepository extends JpaRepository<UserE, Long> {

    Optional <UserE> findByNick(String nick);
    Optional <UserE> findFirstByName(String name);    
    Optional <UserE> findByName(String name);
    List <UserE> findByValidatedAndRejected(Boolean validated, Boolean rejected);
    List <UserE> findByRejected(Boolean validated);

    List <UserE> findByPhone(String phone);
    List <UserE> findLocationByName(String name);
    @Query("SELECT DISTINCT u FROM UserE u JOIN u.reservations r WHERE r.hotel = :hotel")
    List<UserE> findByHotelInReservations(@Param("hotel") Hotel hotel);

}