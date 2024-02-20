package es.codeurjc.yourHOmeTEL.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import es.codeurjc.yourHOmeTEL.model.User;
import es.codeurjc.yourHOmeTEL.repository.UserRepository;

import java.util.List;


@Controller
public class UserController {

	@Autowired
	private UserRepository repository;


	@GetMapping("/profile")
	
	public String profile(Model model) {
		List <User> user = repository.findByName("Jack");
		model.addAttribute("name", );


		return "profile";
	}

	@GetMapping("/login")
	public String login(Model model) {
		return "login";
	}

	@GetMapping("/loginerror")
	public String loginError(Model model) {
		return "loginError";
	}
}