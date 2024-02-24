package es.codeurjc.yourHOmeTEL.controller;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.crypto.password.PasswordEncoder;

import es.codeurjc.yourHOmeTEL.model.Hotel;
import es.codeurjc.yourHOmeTEL.model.Reservation;
import es.codeurjc.yourHOmeTEL.model.Review;
import es.codeurjc.yourHOmeTEL.model.UserE;
import es.codeurjc.yourHOmeTEL.repository.UserRepository;
import es.codeurjc.yourHOmeTEL.service.UserService;
import jakarta.persistence.ManyToOne;
import jakarta.servlet.http.HttpServletRequest;



@Controller
public class UserController{

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;
	

	@GetMapping("/index")
	public String index(Model model,  HttpServletRequest request) {

		return "index";

	}

	@GetMapping("/register")
	public String register(Model model,  HttpServletRequest request) {

		return "register";

	}

	@PostMapping("/replaceprofile")
	public String replaceprofile (Model model,  HttpServletRequest request, @RequestParam  String nick,
	@RequestParam  String name, @RequestParam  String lastname, @RequestParam  String location, @RequestParam  String org, 
	@RequestParam  String language, @RequestParam  String phone , @RequestParam  String mail) {

		String usernick = request.getUserPrincipal().getName();
		UserE currentUser =  userRepository.findByNick(usernick).orElseThrow(); //need to transform the throw into 404 error. Page 25 database
				
		if (currentUser!= null){
			List<Reservation> lReservations = new ArrayList<>();
        	List<Review> lReviews = new ArrayList<>();
        	List<Hotel> lHotels = new ArrayList<>();
			UserE newUser = new UserE(name, lastname, "Bio", location, language, phone,
        	mail, org, null, nick, passwordEncoder.encode("admin"), currentUser.getRols(), lReservations, lReviews, lHotels);

			newUser.setId(currentUser.getId());
			userRepository.save(newUser);
		}
			return "editprofile";
			
			/*return ResponseEntity.ok(foundUser);
		
		}else {
			return ResponseEntity.notFound().build();
		}*/

	}
	
	@GetMapping("/editprofile")
	public String editprofile(Model model,  HttpServletRequest request) {
		String nick = request.getUserPrincipal().getName();
		UserE foundUser = userRepository.findByNick(nick).orElseThrow(); //need to transform the throw into 404 error. Page 25 database
		model.addAttribute("user", foundUser);
		return "editprofile";

	}
	
	@GetMapping("/profile")
	public String profile(Model model,  HttpServletRequest request) {
		String nick = request.getUserPrincipal().getName();
		UserE foundUser =  userRepository.findByNick(nick).orElseThrow(); //need to transform the throw into 404 error. Page 25 database
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

	/**
	 * This method adds to the DB the review posted by the client so it is 
	 * displayed in the hotel's reviews page
	 * @param model
	 * @param score
	 * @param comment
	 * @param date
	 * @param hotel
	 * @param user
	 * @return
	 */
	@GetMapping("/postReview")
	public String postReview(
		Model model, 
		@RequestParam int score,
		@RequestParam	String comment,
		@RequestParam Date date,
		@RequestParam Hotel hotel,
		@RequestParam UserE user){

			model.addAttribute("score", score);
			model.addAttribute("comment", comment);
			model.addAttribute("date", date);
			model.addAttribute("hotel", hotel);
			model.addAttribute("user", user);

			return "hotelReview";
	}


	/**
	 * This controller is responsible of getting all the reviews from certain hotel to 
	 * display on its reviews page
	 * @param model
	 * @param hotelName
	 * @return
	 */
	@GetMapping("posthotelReview/{hotelName}")
	public String hotelReview(Model model, @PathVariable String hotelName){

		//PENDIENTE deberíamos obtener los datos de las reseñas para mostrar aquí
		return "hotelReview";
	}


}