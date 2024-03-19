package yourHOmeTEL.service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import yourHOmeTEL.model.Hotel;
import yourHOmeTEL.model.Reservation;
import yourHOmeTEL.model.Review;
import yourHOmeTEL.repository.ReviewRepository;

@Service
public class ReviewService implements GeneralService<Review> {

    @Autowired
    private ReviewRepository reviewRepository;


    @Override
    public void save(Review review) {
        reviewRepository.save(review);

    }

    @Override
    public void delete(Review review) {
        reviewRepository.delete(review);
    }

    @Override
    public Optional<Review> findById(Long id) {
        return reviewRepository.findById(id);

    }

    public Page<Review> findByUser_Name(String name, Pageable pageable){
        return reviewRepository.findByUser_Name(name, pageable);
    }

    public Page<Review> findByHotel_Name(String name, Pageable pageable){
        return reviewRepository.findByHotel_Name(name, pageable);
    }

    public Page<Review> findByDate(Date date, Pageable pageable){
        return reviewRepository.findByDate(date, pageable);
    }

    public Page<Review> findByScore(int score, Pageable pageable){
        return reviewRepository.findByScore(score, pageable);
    }

    public Page<Review> findByHotel(Hotel hotel, Pageable pageable){
        return reviewRepository.findByHotel(hotel, pageable);
    }

    public Page<Review> findByScoreAndHotel(int score, Hotel hotel, Pageable pageable){
        return reviewRepository.findByScoreAndHotel(score, hotel, pageable);
    }

    public Page<Review> findByUser_Id(Long userId, Pageable pageable){
        return reviewRepository.findByUser_Id(userId, pageable);
    }

    public Page<Review> findByHotel_Id(Long hotelId, Pageable pageable){
        return reviewRepository.findByHotel_Id(hotelId, pageable);
    }

    public List<Review> findByUser_Name(String name){
        return reviewRepository.findByUser_Name(name);
    }

    public List<Review> findByHotel_Name(String name){
        return reviewRepository.findByHotel_Name(name);
    }

    public List<Review> findByDate(Date date){
        return reviewRepository.findByDate(date);
    }

    public List<Review> findByScore(int score){
        return reviewRepository.findByScore(score);
    }

    public List<Review> findByHotel(Hotel hotel){
        return reviewRepository.findByHotel(hotel);
    }

    public List<Review> findByScoreAndHotel(int score, Hotel hotel){
        return reviewRepository.findByScoreAndHotel(score, hotel);
    }

    @Override
    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    @Override
    public List<Review> findAll(Sort sort) {
        if (!sort.equals(null)) {
            return reviewRepository.findAll(sort);
        } else {
            return reviewRepository.findAll();
        }
    }


    @Override
    public Boolean exist(Long id) {
        Optional<Review> review = reviewRepository.findById(id);
        if (review.isPresent())
            return true;
        else
            return false;
    }
}
