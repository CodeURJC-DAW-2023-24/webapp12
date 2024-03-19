package yourHOmeTEL.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import yourHOmeTEL.model.Hotel;
import yourHOmeTEL.model.Review;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    List<Hotel> findByName(String name);

    List<Hotel> findByLocation(String location);

    Page<Hotel> findByManager_Id(Long managerId, Pageable pageable);

    Page<Hotel> findTop6ByManager_Validated(Boolean validated, Pageable pageable);

    Page<Hotel> findTop6ByManager_ValidatedAndNameContainingIgnoreCaseOrderByNameDesc(Boolean validated,
            String searchValue, Pageable pageable);

    List<Hotel> findTop6ByManager_Validated(Boolean validated);

    List<Hotel> findTop6ByManager_ValidatedAndNameContainingIgnoreCaseOrderByNameDesc(Boolean validated,
            String searchValue);
}
