package yourHOmeTEL.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import yourHOmeTEL.model.Hotel;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

        List<Hotel> findByName(String name);

        List<Hotel> findByLocation(String location);

        Page<Hotel> findByManager_Id(Long managerId, Pageable pageable);

        Page<Hotel> findTop6ByManager_Validated(Boolean validated, Pageable pageable);

        Page<Hotel> findAllByManager_ValidatedAndNameContainingIgnoreCaseOrderByNameDesc(Boolean validated,
        String searchValue, Pageable pageable);

        List<Hotel> findAllByManager_Validated(Boolean validated);
        
        List<Hotel> findAllByManager_ValidatedAndNameContainingIgnoreCaseOrderByNameDesc(Boolean validated,
        String searchValue);

        List<Hotel> findTop6ByManager_Validated(Boolean validated);

        List<Hotel> findTop6ByManager_ValidatedAndNameContainingIgnoreCaseOrderByNameDesc(Boolean validated,
                        String searchValue);

        @Query("SELECT h FROM Hotel h WHERE h.manager.validated = true AND h NOT IN :hotels")
        List<Hotel> findValidHotelsNotInList(@Param("hotels") List<Hotel> hotels);
}
