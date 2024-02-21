package es.codeurjc.yourHOmeTEL.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import org.springframework.stereotype.Controller;

import es.codeurjc.yourHOmeTEL.model.UserE;
import es.codeurjc.yourHOmeTEL.repository.UserRepository;


@Controller
public class initDataBaseController implements CommandLineRunner {
    @Autowired
    private UserRepository repository;

    @Override
    public void run(String... args) throws Exception {

        repository.save(new UserE("Jack", "Bio", "loc", "lan", "phone",
        "mail", "org", null, "hola", "adios", null, null, null, null));
    }
}
