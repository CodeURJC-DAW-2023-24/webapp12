package es.codeurjc.yourHOmeTEL.repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.codeurjc.yourHOmeTEL.model.Review;
import es.codeurjc.yourHOmeTEL.model.Room;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByUser_Name(String name);
    List<Review> findByHotel_Name(String name);
    List<Review> findByDate(Date date);
    List<Review> findByScore(int score);
    Optional<Review> findById(Long id);

}
