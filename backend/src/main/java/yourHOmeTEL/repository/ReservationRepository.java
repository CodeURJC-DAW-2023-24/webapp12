package yourHOmeTEL.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import yourHOmeTEL.model.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    
    Page<Reservation> findByUser_Name(String name, Pageable pageable);

    Page<Reservation> findByHotel_Name(String name, Pageable pageable);

    Page<Reservation> findByUser_Id(Long userId, Pageable pageable);

    Page<Reservation> findByHotel_Id(Long hotelId, Pageable pageable);

    Reservation findByHotel_Id(Long hotelId);

    Page<Reservation> findByRoom_Id(Long roomId, Pageable pageable);
    
    List<Reservation> findByUser_Name(String name);

    List<Reservation> findByHotel_Name(String name);  
}
