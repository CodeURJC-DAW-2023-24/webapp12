package es.codeurjc.yourHOmeTEL.controller;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import es.codeurjc.yourHOmeTEL.model.UserE;
import es.codeurjc.yourHOmeTEL.repository.UserRepository;
import es.codeurjc.yourHOmeTEL.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Optional;


@Controller
public class UserController{

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;
	


	@GetMapping("/profile")
	public String profile(Model model,  HttpServletRequest request) {
		String name = request.getUserPrincipal().getName();
		UserE foundUser =  userRepository.findByNick(name); //need to transform the throw into 404 error. Page 25 database
		model.addAttribute("name", foundUser.getName());
		return "profile";
		

		/*if (request.isUserInRole("ADMIN"))
		return "Admin";
		else if (request.isUserInRole("CLIENT"))
			return "Client";
		else
			return "Manager";}*/
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