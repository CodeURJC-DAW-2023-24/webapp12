package es.codeurjc.yourHOmeTEL.controller;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
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
import es.codeurjc.yourHOmeTEL.repository.HotelRepository;
import es.codeurjc.yourHOmeTEL.repository.UserRepository;
import es.codeurjc.yourHOmeTEL.service.UserService;
import es.codeurjc.yourHOmeTEL.service.ReservationService;
import jakarta.persistence.ManyToOne;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestBody;




@Controller
public class UserController{

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private HotelRepository hotelRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ReservationService reservationService;
	

	@GetMapping("/index")
	public String index(Model model,  HttpServletRequest request) {
		List <Hotel> hotels= new ArrayList<>();
		for (Long i = 1L; i<=1L; i++){	
			Hotel hotel = hotelRepository.findById(i).orElseThrow();
			hotels.add(hotel);
		}
		model.addAttribute("hotels", hotels);

		
		
		return "index";

	}

	@GetMapping("/clientlist")
	public String clientlist(Model model,  HttpServletRequest request) {

		return "clientlist";

	}

	
	@PostMapping("/addReservation/{id}")
	public String addReservation(@PathVariable Long id, HttpServletRequest request, String checkIn, String checkOut, Integer numPeople) {
		//Hay que mirar como hacer lo de las rooms
		UserE user = userRepository.findByNick(request.getUserPrincipal().getName()).orElseThrow();
		Hotel hotel = hotelRepository.findById(id).orElseThrow();
		LocalDate checkInDate = reservationService.toLocalDate(checkIn);
		LocalDate checkOutDate = reservationService.toLocalDate(checkOut);
		Reservation newRe = new Reservation(checkInDate,checkOutDate, numPeople, hotel, hotel.getRooms().getFirst(), user);
		return "redirect:/clientreservations";
	}
	
	
	
	@GetMapping("/clientreservation")
	public String clientreservation(Model model,  HttpServletRequest request) {

		return "clientreservation";

	}

	//MANAGER CONTROLLERS
	@GetMapping("/viewhotelsmanager")
	public String viewhotelsmanager(Model model,HttpServletRequest request) {

		String managernick = request.getUserPrincipal().getName();
		UserE currentManager =  userRepository.findByNick(managernick).orElseThrow();

		model.addAttribute("hotels", currentManager.getHotels());

		return "viewhotelsmanager";

	}

	@GetMapping("/testChart")
	public String testChart(Model model) {

		List<Integer> info = new ArrayList<>();
		model.addAttribute("info", info);
		
		return "testChart";

	}

	@GetMapping("/chartsmanager")
	public String chartsmanager(Model model,  HttpServletRequest request) {

		return "chartsmanager";

	}

	

	//ADMIN CONTROLLERS
	@GetMapping("/chartsadmin")
	public String chartsadmin(Model model,  HttpServletRequest request) {

		return "chartsadmin";

	}

	@GetMapping("/hotelvalidation")
	public String hotelvalidation(Model model,  HttpServletRequest request) {

		return "hotelvalidation";

	}

	@GetMapping("/managerlist")
	public String managerlist(Model model,  HttpServletRequest request) {

		return "managerlist";

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
	
	@GetMapping("/editprofile/{id}")
	public String editProfile(Model model, @PathVariable Long id ) {
		
		UserE foundUser = userRepository.findById(id).orElseThrow(); //need to transform the throw into 404 error. Page 25 database
		model.addAttribute("user", foundUser);
		return "editprofile";

	}

	
	@PostMapping("/editprofile/{id}")
	public String editProfile(Model model, @PathVariable Long id,		
	
	@RequestParam	String name,
	@RequestParam	String lastname,
	@RequestParam	String location,
	@RequestParam	String org,
	@RequestParam	String language,
	@RequestParam	String phone,
	@RequestParam	String mail,
	@RequestParam	String bio) {
		//TODO: process POST request
		
		UserE foundUser = userRepository.findById(id).orElseThrow();
		

		
		foundUser.setName(name);
		foundUser.setLocation(location);
		foundUser.setOrganizacion(org);
		foundUser.setLanguage(language);
		foundUser.setPhone(phone);
		foundUser.setEmail(mail);
		foundUser.setBio(bio);

		userRepository.save(foundUser);
		
		model.addAttribute("user", foundUser);

		return "redirect:/profile" ;

		//no se cambia el nick por el tema de la seguridad 

		
	}
	



	
	@GetMapping("/profile")
	public String profile(Model model,HttpServletRequest request) {
		

		String usernick = request.getUserPrincipal().getName();
		UserE currentUser =  userRepository.findByNick(usernick).orElseThrow();
		model.addAttribute("user", currentUser);

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

	@GetMapping("/register")
	public String registerClient(Model model) {
		return "register";
	}

	@PostMapping("/register")
	public String registerClient(Model model, UserE user, Integer type){
		if (!userService.existNick(user.getNick())) {
			user.setPass(passwordEncoder.encode(user.getPass()));
			List<String> rols = new ArrayList<>();
			rols.add("USER");
			if (type == 0)
				rols.add("CLIENT");
			else
				rols.add("MANAGER");
			user.setRols(rols);

			userRepository.save(user);
			return "redirect:/login";
		} else {
			return "redirect:/nickTaken";
		}
	}
	
	@GetMapping("/nickTaken")
	public String takenUserName(Model model, HttpServletRequest request) {
		return "nickTaken";
	}

}