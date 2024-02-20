package es.codeurjc.yourHOmeTEL.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.codeurjc.yourHOmeTEL.model.Reservation;


public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByUser_Name(String name);
    List<Reservation> findByDate(Date date);
    List<Reservation> findByHotel_Name(String name);
}
