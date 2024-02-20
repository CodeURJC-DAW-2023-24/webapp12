package es.codeurjc.yourHOmeTEL.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import es.codeurjc.yourHOmeTEL.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByName(String name);
    List<User> findByPhone(String phone);
    List<User> findLocationByName(String name);


    
}
