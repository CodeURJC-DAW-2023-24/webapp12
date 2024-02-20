package es.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Sort;
import org.springframework.stereotype.Service;

import es.codeurjc.yourHOmeTEL.model.User;
import es.codeurjc.yourHOmeTEL.repository.UserRepository;

@Service
public class UserService implements GeneralService<User> {
    @Autowired
    private UserRepository repository;


    public Optional <User> findById(Long id){
        return repository.findById(id);

    }

    public void save(User go){
        
    }

    public void delete(User go){

    }
    public Optional <User> findAll(){
        return null;
    }
    public Optional <User> findAll(Sort sort){
        return null;
    }
    public Boolean exist (Long id){
        return null;
    }
    
}
