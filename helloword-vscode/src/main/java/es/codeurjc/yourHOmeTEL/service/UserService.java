package es.codeurjc.yourHOmeTEL.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import es.codeurjc.yourHOmeTEL.model.UserE;
import es.codeurjc.yourHOmeTEL.repository.UserRepository;


@Service
public class UserService implements GeneralService<UserE> {
    
    @Autowired
    private UserRepository repository;


    @Override
    public Optional <UserE> findById(Long id){
        return repository.findById(id);

    }

    @Override
    public void save(UserE go){
        
    }

    @Override
    public void delete(UserE go){

    }

    @Override
    public List <UserE> findAll() {
        return repository.findAll();
    }

    @Override
    public List <UserE> findAll(Sort sort){
        if (!sort.equals(null)){
            return repository.findAll(sort);
        }
        else {
            return repository.findAll();
        }      
    }


    @Override
    public Boolean exist (Long id){
        Optional <UserE> user = repository.findById(id);
        if (user.isPresent())
            return true;
        else
            return false;
    }

    public Optional <UserE> findFirstByName(String name) {
        return repository.findFirstByName(name);
    }
    
    public Optional <UserE> findByName(String name){
        return repository.findByName(name);
    }

    public List<UserE> findByPhone(String phone){
        return repository.findByPhone(phone);
    }

    public List<UserE> findLocationByName (String name){
        return repository.findLocationByName(name);
    }

    public Optional <UserE> findByNick(String nick){
        return repository.findByNick(nick);
    }

    public boolean existNick(String nick) {
		Optional<UserE> user = findByNick(nick);
		return user.isPresent();
	}
    
}
