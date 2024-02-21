package es.codeurjc.yourHOmeTEL.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import es.codeurjc.yourHOmeTEL.model.UserE;

public interface UserRepository extends JpaRepository<UserE, Long> {

    UserE findByNick(String nick);
    Optional <UserE> findFirstByName(String name);
    List <UserE> findByName(String name);
    List <UserE> findByPhone(String phone);
    List <UserE> findLocationByName(String name);

}
