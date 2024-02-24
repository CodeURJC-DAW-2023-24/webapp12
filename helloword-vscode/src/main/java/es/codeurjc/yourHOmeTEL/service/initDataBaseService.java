package es.codeurjc.yourHOmeTEL.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import es.codeurjc.yourHOmeTEL.model.UserE;
import es.codeurjc.yourHOmeTEL.repository.UserRepository;
import jakarta.annotation.PostConstruct;


@Service
public class initDataBaseService{

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    private void initDatabase() {

        List<String> rolesUser = new ArrayList();
        rolesUser.add("USER");
        rolesUser.add("CLIENT");
        List<String> rolesManager = new ArrayList();
        rolesUser.add("USER");
        rolesUser.add("MANAGER");
        List<String> rolesAdmin = new ArrayList();
        rolesAdmin.add("USER");
        rolesAdmin.add("ADMIN");

        userRepository.save(new UserE("Jack1", "Wells1", "Bio", "loc", "lan", "phone",
        "mail", "org", null, "user", "pass", rolesUser, null, null, null));
        userRepository.save(new UserE("Jack2", "Wells2", "Bio", "loc", "lan", "phone",
        "mail", "org", null, "manager",  "admin", rolesManager, null, null, null));
        userRepository.save(new UserE("Jack3", "Wells3", "Bio", "loc", "lan", "phone",
        "mail", "org", null, "admin",  "admin", rolesAdmin, null, null, null));
    }
}
