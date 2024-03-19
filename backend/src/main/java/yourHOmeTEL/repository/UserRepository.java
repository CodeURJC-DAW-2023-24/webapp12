package yourHOmeTEL.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import yourHOmeTEL.model.Hotel;
import yourHOmeTEL.model.UserE;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface UserRepository extends JpaRepository<UserE, Long> {

    Optional<UserE> findByNick(String nick);

    Optional<UserE> findFirstByName(String name);

    Optional<UserE> findByName(String name);

    Page<UserE> findByValidatedAndRejected(Boolean validated, Boolean rejected, Pageable pageable);

    Page<UserE> findByRejected(Boolean validated, Pageable pageable);

    Page<UserE> findByPhone(String phone,Pageable pageable);

    Page<UserE> findLocationByName(String name, Pageable pageable);

    Page<UserE> findByCollectionRolsContains(String rol, Pageable pageable);

    List<UserE> findByValidatedAndRejected(Boolean validated, Boolean rejected);

    List<UserE> findByRejected(Boolean validated);

    List<UserE> findByPhone(String phone);

    List<UserE> findLocationByName(String name);

    List<UserE> findByCollectionRolsContains(String rol);

    @Query(value = "SELECT DISTINCT u FROM UserE u JOIN u.reservations r WHERE r.hotel = :hotel",
    countQuery = "SELECT COUNT(*) FROM UserE u JOIN u.reservations r WHERE r.hotel = :hotel")
    List<UserE> findByHotelInReservations(@Param("hotel") Hotel hotel);
}