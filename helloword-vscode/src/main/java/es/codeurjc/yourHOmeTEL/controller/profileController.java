package es.codeurjc.yourHOmeTEL.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import es.codeurjc.yourHOmeTEL.repository.UserRepository;
import jakarta.annotation.PostConstruct;



@Controller
public class profileController {

    @Autowired
    private UserRepository users;


    @PostConstruct
    public void init() {
    users.save(new User("Juan","hola buenas","Madrid","español", "609105789","hola@gmail.com"));
    }

    

    
}
