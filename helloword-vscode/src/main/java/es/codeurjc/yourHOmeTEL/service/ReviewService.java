package es.codeurjc.yourHOmeTEL.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import es.codeurjc.yourHOmeTEL.model.Review;
import es.codeurjc.yourHOmeTEL.repository.ReviewRepository;

@Service
public class ReviewService implements GeneralService<Review> {

    @Autowired
    private ReviewRepository repository;


    @Override
    public Optional <Review> findById(Long id){
        return repository.findById(id);

    }

    @Override
    public void save(Review go){
        
    }

    @Override
    public void delete(Review go){

    }

    @Override
    public List <Review> findAll() {
        return repository.findAll();
    }

    @Override
    public List <Review> findAll(Sort sort){
        if (!sort.equals(null)){
            return repository.findAll(sort);
        }
        else {
            return repository.findAll();
        }      
    }


    @Override
    public Boolean exist (Long id){
        return null;
    }
}
