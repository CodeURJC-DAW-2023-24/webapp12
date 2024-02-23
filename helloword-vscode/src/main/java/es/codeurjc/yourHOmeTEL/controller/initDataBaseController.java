package es.codeurjc.yourHOmeTEL.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;

import es.codeurjc.yourHOmeTEL.model.UserE;
import es.codeurjc.yourHOmeTEL.repository.UserRepository;
import jakarta.annotation.PostConstruct;


@Controller
public class initDataBaseController{


     @Autowired
    private UserRepository userRepository;

    @PostConstruct
    private void initDatabase() {

        List<String> rolesUser = new ArrayList();
        rolesUser.add("USER");
        rolesUser.add("CLIENT");
        List<String> rolesAdmin = new ArrayList();
        rolesAdmin.add("USER");
        rolesAdmin.add("ADMIN");

        userRepository.save(new UserE("Jack", "Wells", "Bio", "loc", "lan", "phone",
        "mail", "org", null, "user", "pass", rolesUser, null, null, null));
        userRepository.save(new UserE("Jack", "Wells", "Bio", "loc", "lan", "phone",
        "mail", "org", null, "admin",  "admin", rolesAdmin, null, null, null));
    }
}
