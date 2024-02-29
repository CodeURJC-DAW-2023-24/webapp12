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
import es.codeurjc.yourHOmeTEL.model.Room;
import es.codeurjc.yourHOmeTEL.model.UserE;
import es.codeurjc.yourHOmeTEL.repository.HotelRepository;
import es.codeurjc.yourHOmeTEL.repository.ReservationRepository;
import es.codeurjc.yourHOmeTEL.repository.UserRepository;
import es.codeurjc.yourHOmeTEL.service.UserService;
import es.codeurjc.yourHOmeTEL.service.HotelService;
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
	private HotelService hotelService;

	@Autowired
	private HotelRepository hotelRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ReservationService reservationService;

	@Autowired
	private ReservationRepository reservationRepository;
	

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
	
	@PostMapping("/addReservation/{id}")
	public String addReservation(Model model, @PathVariable Long id, HttpServletRequest request, String checkIn, String checkOut, Integer numPeople) {
		LocalDate checkInDate = reservationService.toLocalDate(checkIn);
		LocalDate checkOutDate = reservationService.toLocalDate(checkOut);
		Room room = hotelService.checkRooms(id, checkInDate, checkOutDate);
		if (room != null){
			UserE user = userRepository.findByNick(request.getUserPrincipal().getName()).orElseThrow();
			Hotel hotel = hotelRepository.findById(id).orElseThrow();
			Reservation newRe = new Reservation(checkInDate,checkOutDate, numPeople, hotel, room, user);
			reservationRepository.save(newRe);
			return "redirect:/clientreservations";
		}
		else
		//Crear una pagina que muestre el error de que no hay habitaciones disponibles en esas fechas.
			return "redirect:/notRooms";
	}
	
	
	
	@GetMapping("/clientreservation")
	public String clientreservation(Model model,  HttpServletRequest request) {

		return "clientreservation";

	}

	//MANAGER CONTROLLERS
	@GetMapping("/viewhotelsmanager")
	public String viewHotelsManager(Model model,HttpServletRequest request) {

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
	public String chartsManager(Model model,  HttpServletRequest request) {

		return "chartsmanager";

	}

	@PostMapping("/application")
	public String managerApplication(Model model,  HttpServletRequest request) {

		String nick = request.getUserPrincipal().getName();
		UserE manager = userRepository.findByNick(nick).orElseThrow();
		manager.setRejected(false);
		userRepository.save(manager);
		model.addAttribute("user", manager);
		return "profile";

	}


	//ADMIN CONTROLLERS
	@GetMapping("/chartsadmin")
	public String chartsAdmin(Model model,  HttpServletRequest request) {

		return "chartsadmin";

	}

	@GetMapping("/managervalidation")
	public String managerValidation(Model model) {
		List <UserE> unvalidatedManagersList = new ArrayList<>();
		unvalidatedManagersList = userRepository.findByValidatedAndRejected(false, false);

		if (unvalidatedManagersList!=null){
			model.addAttribute("unvalidatedManagers", unvalidatedManagersList);		
		}	

		return "managervalidation";
	}

	@PostMapping("/rejection/{id}")
	public String rejectManager(Model model, @PathVariable Long id) {
		UserE manager = userRepository.findById(id).orElseThrow();
		
		if (manager!=null){
			manager.setRejected(true);
			manager.setvalidated(false);
			userRepository.save(manager);			
		}			
		return "redirect:/managervalidation";

	}

	@PostMapping("/acceptance/{id}")
	public String acceptManager(Model model,  @PathVariable Long id) {
		UserE manager = userRepository.findById(id).orElseThrow();
		
		if (manager!=null){
			manager.setRejected(false);
			manager.setvalidated(true);
			userRepository.save(manager);			
		}			
		return "redirect:/managervalidation";

	}



	@GetMapping("/managerlist")
	public String managerList(Model model,  HttpServletRequest request) {

		return "managerlist";

	}
	
	
	@GetMapping("/editprofile/{id}")
	public String editProfile(Model model, @PathVariable Long id ) {
		
		UserE foundUser = userRepository.findById(id).orElseThrow(); //need to transform the throw into 404 error. Page 25 database
		model.addAttribute("user", foundUser);
		return "editprofile";

	}

	
	@PostMapping("/editprofile/replace/{id}")
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
		foundUser.setOrganization(org);
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