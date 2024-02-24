package es.codeurjc.yourHOmeTEL.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import es.codeurjc.yourHOmeTEL.model.UserE;
import es.codeurjc.yourHOmeTEL.repository.UserRepository;
import es.codeurjc.yourHOmeTEL.service.UserService;
import jakarta.servlet.http.HttpServletRequest;



@Controller
public class UserController{

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;
	

	@GetMapping("/index")
	public String index(Model model,  HttpServletRequest request) {

		return "index";

	}

	@GetMapping("/editprofile")
	public String editprofile(Model model,  HttpServletRequest request) {
		String nick = request.getUserPrincipal().getName();
		UserE foundUser =  userRepository.findByNick(nick); //need to transform the throw into 404 error. Page 25 database
		model.addAttribute("user", foundUser);
		return "editprofile";

	}
	
	@GetMapping("/profile")
	public String profile(Model model,  HttpServletRequest request) {
		String nick = request.getUserPrincipal().getName();
		UserE foundUser =  userRepository.findByNick(nick); //need to transform the throw into 404 error. Page 25 database
		model.addAttribute("user", foundUser);
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