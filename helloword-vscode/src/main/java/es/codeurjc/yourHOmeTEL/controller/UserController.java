package es.codeurjc.yourHOmeTEL.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import es.codeurjc.yourHOmeTEL.model.UserE;
import es.codeurjc.yourHOmeTEL.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;


@Controller
public class UserController{

	@Autowired
	private UserService userService;


	@GetMapping("/profile")
	
	public String profile(Model model,  HttpServletRequest request) {
		String name = request.getUserPrincipal().getName();
		Optional <UserE> optionalUser = userService.findByName(name);

		if (optionalUser.isPresent()){
			model.addAttribute("user", optionalUser.get().getName());
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