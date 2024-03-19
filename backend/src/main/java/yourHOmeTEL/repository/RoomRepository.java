package yourHOmeTEL.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import yourHOmeTEL.model.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    Page<Room> findByMaxClients(int maxClients, Pageable pageable);

    Page<Room> findByHotel_Id(Long hotelIdLong, Pageable pageable);
    
    List<Room> findByMaxClients(int maxClients); 
}
