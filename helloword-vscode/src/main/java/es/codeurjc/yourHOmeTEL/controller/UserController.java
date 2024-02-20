package es.codeurjc.yourHOmeTEL.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class UserController {

	@GetMapping("/profile")
	public String profile(Model model) {
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