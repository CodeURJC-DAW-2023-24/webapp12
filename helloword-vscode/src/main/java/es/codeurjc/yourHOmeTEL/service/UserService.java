package es.codeurjc.yourHOmeTEL.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Sort;
import org.springframework.stereotype.Service;

import es.codeurjc.yourHOmeTEL.model.UserE;
import es.codeurjc.yourHOmeTEL.repository.UserRepository;

@Service
public class UserService implements GeneralService<UserE> {
    @Autowired
    private UserRepository repository;


    public Optional <UserE> findById(Long id){
        return repository.findById(id);

    }

    public void save(UserE go){
        
    }

    public void delete(UserE go){

    }
    public Optional <UserE> findAll(){
        return null;
    }
    public Optional <UserE> findAll(Sort sort){
        return null;
    }
    public Boolean exist (Long id){
        return null;
    }
    
}
