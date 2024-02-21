package es.codeurjc.yourHOmeTEL.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.codeurjc.yourHOmeTEL.model.UserE;

public interface UserRepository extends JpaRepository<UserE, Long> {

    List<UserE> findByName(String name);
    List<UserE> findByPhone(String phone);
    List<UserE> findLocationByName(String name);

}
