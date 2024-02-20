package es.codeurjc.yourHOmeTEL.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

	@GetMapping("/profile")
	public String Profile (Model model) {

		return "profile";
	}

}