package es.codeurjc.yourHOmeTEL.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;

import es.codeurjc.yourHOmeTEL.model.User;
import es.codeurjc.yourHOmeTEL.repository.UserRepository;

@Controller
public class initDataBaseController implements CommandLineRunner {
    @Autowired
    private UserRepository repository;

    @Override
    public void run(String... args) throws Exception {

        repository.save(new User("Jack", "Bio", "loc", "lan", "phone",
        "mail", "pfp", null, null, null, null, null, null));

        List<User> bauers = repository.findByName("Jack");
        for (User Jack : jacks) {
            System.out.println(Jack);
        }

        repository.delete(jacks.get(0));
    }
}
