package es.codeurjc.yourhometel.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.codeurjc.yourhometel.backend.model.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByUser_Name(String name);

    List<Reservation> findByHotel_Name(String name);

    Optional<Reservation> findById(Long id);
}
