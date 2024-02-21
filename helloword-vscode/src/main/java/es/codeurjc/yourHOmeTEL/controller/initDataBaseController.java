package es.codeurjc.yourHOmeTEL.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

import es.codeurjc.yourHOmeTEL.model.UserE;
import es.codeurjc.yourHOmeTEL.repository.UserRepository;
import jakarta.annotation.PostConstruct;


@Controller
public class initDataBaseController{


     @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @PostConstruct
    private void initDatabase() {

        List<String> roles = new ArrayList();
        roles.add("USER");

        userRepository.save(new UserE("Jack", "Bio", "loc", "lan", "phone",
        "mail", "org", null, "User",  passwordEncoder.encode("pass"), roles, null, null, null));
        userRepository.save(new UserE("Jack", "Bio", "loc", "lan", "phone",
        "mail", "org", null, "admin",  passwordEncoder.encode("admin"), roles, null, null, null));
    }
}
