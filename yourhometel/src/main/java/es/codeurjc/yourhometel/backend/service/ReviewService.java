package es.codeurjc.yourhometel.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import es.codeurjc.yourhometel.backend.model.Hotel;
import es.codeurjc.yourhometel.backend.model.Review;
import es.codeurjc.yourhometel.backend.repository.ReviewRepository;

@Service
public class ReviewService implements GeneralService<Review> {

    @Autowired
    private ReviewRepository repository;

    @Override
    public Optional<Review> findById(Long id) {
        return repository.findById(id);

    }

    @Override
    public void save(Review go) {

    }

    @Override
    public void delete(Review go) {

    }

    @Override
    public List<Review> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Review> findAll(Sort sort) {
        if (!sort.equals(null)) {
            return repository.findAll(sort);
        } else {
            return repository.findAll();
        }
    }

    public List<Review> findByScoreAndHotel(Hotel hotel, int score) {

        List<Review> reviews = repository.findByScore(score);
        List<Review> hotels = repository.findByHotel(hotel);
        reviews.retainAll(hotels);

        return reviews;
    }

    @Override
    public Boolean exist(Long id) {
        return null;
    }
}
