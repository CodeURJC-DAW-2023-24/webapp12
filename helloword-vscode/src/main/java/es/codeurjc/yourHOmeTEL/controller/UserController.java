package es.codeurjc.yourHOmeTEL.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import es.codeurjc.yourHOmeTEL.model.User;
import es.codeurjc.yourHOmeTEL.repository.UserRepository;
import es.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Controller
public class UserController{

	@Autowired
	private UserService userService;


	@GetMapping("/profile{id}")
	
	public String profile(Model model, @PathVariable Long id) {
		Optional <User> optionalUser = userService.findById(id);

		if (optionalUser.isPresent()){
			model.addAttribute("name", optionalUser.get().getName());

		}
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