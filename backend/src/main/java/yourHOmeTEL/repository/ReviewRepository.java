package yourHOmeTEL.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import yourHOmeTEL.model.Hotel;
import yourHOmeTEL.model.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    
    Page<Review> findByUser_Name(String name, Pageable pageable);

    Page<Review> findByHotel_Name(String name, Pageable pageable);

    Page<Review> findByDate(Date date, Pageable pageable);

    Page<Review> findByScore(int score, Pageable pageable);

    Page<Review> findByHotel(Hotel hotel, Pageable pageable);

    Page<Review> findByScoreAndHotel(int score, Hotel hotel, Pageable pageable);
    
    List<Review> findByUser_Name(String name);

    List<Review> findByHotel_Name(String name);

    List<Review> findByDate(Date date);

    List<Review> findByScore(int score);

    List<Review> findByHotel(Hotel hotel);

    List<Review> findByScoreAndHotel(int score, Hotel hotel);

}
