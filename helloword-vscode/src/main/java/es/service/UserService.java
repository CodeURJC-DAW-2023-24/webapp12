package es.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Sort;
import org.springframework.stereotype.Service;

import es.codeurjc.yourHOmeTEL.model.User;
import es.codeurjc.yourHOmeTEL.repository.UserRepository;

@Service
public class UserService implements GeneralService<User> {
    @Autowired
    private UserRepository repository;


    public List <User> findById(Long id){
        return null;

    }

    public void save(User go){
        
    }

    public void delete(User go){

    }
    public List <User> findAll(){
        return null;
    }
    public List <User> findAll(Sort sort){
        return null;
    }
    public Boolean exist (Long id){
        return null;
    }
    
}
