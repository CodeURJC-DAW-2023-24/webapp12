package es.codeurjc.yourhometel.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

import org.springframework.core.io.Resource;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.security.crypto.password.PasswordEncoder;

import es.codeurjc.yourhometel.model.Hotel;
import es.codeurjc.yourhometel.model.Reservation;
import es.codeurjc.yourhometel.model.Room;
import es.codeurjc.yourhometel.model.UserE;
import es.codeurjc.yourhometel.repository.HotelRepository;
import es.codeurjc.yourhometel.repository.ReservationRepository;
import es.codeurjc.yourhometel.repository.RoomRepository;
import es.codeurjc.yourhometel.repository.UserRepository;
import es.codeurjc.yourhometel.service.UserService;
import es.codeurjc.yourhometel.service.HotelService;
import es.codeurjc.yourhometel.service.ReservationService;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private HotelService hotelService;

	@Autowired
	private HotelRepository hotelRepository;

	@Autowired
	private ReservationService reservationService;

	@Autowired
	private ReservationRepository reservationRepository;

	@Autowired
	private RoomRepository roomRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	// PUBLIC CONTROLLERS

	// ADVANCED RECOMMENDATION ALGORITHM
	@GetMapping("/index")
	public String index(Model model, HttpServletRequest request) {
		List<Hotel> recomendedHotels = new ArrayList<>();
		try {
			String nick = request.getUserPrincipal().getName();
			UserE user = userRepository.findByNick(nick).orElseThrow();
			List<Reservation> userReservations = user.getReservations();
			recomendedHotels = userService.findRecomendedHotels(6, userReservations, user);

		} catch (NullPointerException e) {

		} finally {
			if (recomendedHotels.size() < 6) {
				// size +1 to avoid looking for id = 0 if size = 0
				for (int i = recomendedHotels.size() + 1; i < 7; i++) {
					Hotel hotel = hotelRepository.findById((long) i).orElseThrow();
					if (hotel != null && hotel.getManager().getvalidated())
						recomendedHotels.add(hotel);
				}
			}
		}
		model.addAttribute("hotels", recomendedHotels);
		return "index";
	}

	@GetMapping("/indexsearch")
	public String indexSearch(Model model, @RequestParam String searchValue) {
		List<Hotel> hotels = hotelRepository.findTop6ByManager_ValidatedAndNameContainingIgnoreCaseOrderByNameDesc(true,
				searchValue);
		model.addAttribute("hotels", hotels);
		return "index";

	}

	@GetMapping("/error")
	public String Error(Model model, HttpServletRequest request) {
		return "/error";

	}

	@GetMapping("/returnmainpage")
	public String returnmainpage(Model model, HttpServletRequest request) {
		return "redirect:/index";

	}

	// CLIENT CONTROLLERS
	@PostMapping("/addReservation/{id}")
	public String addReservation(Model model, @PathVariable Long id, HttpServletRequest request, String checkIn,
			String checkOut, Integer numPeople) {

		LocalDate checkInDate = reservationService.toLocalDate(checkIn);
		LocalDate checkOutDate = reservationService.toLocalDate(checkOut);
		Room room = hotelService.checkRooms(id, checkInDate, checkOutDate, numPeople);
		if (room != null) {
			UserE user = userRepository.findByNick(request.getUserPrincipal().getName()).orElseThrow();
			Hotel hotel = hotelRepository.findById(id).orElseThrow();
			Reservation newRe = new Reservation(checkInDate, checkOutDate, numPeople, hotel, room, user);
			reservationRepository.save(newRe);
			return "redirect:/clientreservations";
		} else
			return "redirect:/notRooms/{id}";
	}

	@GetMapping("/notRooms/{id}")
	public String notRooms(Model model, @PathVariable Long id) {
		return "notRooms";
	}

	/**
	 * Redirects the users to the page of reservations, where they can check their
	 * reservations
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	@GetMapping("/clientreservations")
	public String clientreservation(Model model, HttpServletRequest request) {
		UserE currentClient = userRepository.findByNick(request.getUserPrincipal().getName()).orElseThrow();

		List<Reservation> bookings = currentClient.getReservations();

		if (bookings.size() < 6) {
			model.addAttribute("reservations", bookings);

		} else {

			List<Reservation> auxBookings = new ArrayList<>();
			for (int i = 0; i < 6; i++) {
				auxBookings.add(bookings.get(i));
			}

			model.addAttribute("reservations", auxBookings);

		}

		model.addAttribute("user", currentClient);

		return "clientReservation";

	}

	/**
	 * Loads up to 6 more reservations
	 */
	@GetMapping("/loadMoreReservations/{start}/{end}")
	public String loadMoreReservations(
			Model model,
			HttpServletRequest request,
			@PathVariable int start,
			@PathVariable int end) {

		UserE currentClient = userRepository.findByNick(request.getUserPrincipal().getName()).orElseThrow();

		List<Reservation> bookings = currentClient.getReservations();
		List<Reservation> auxBookings = new ArrayList<>();

		if (start <= bookings.size()) {

			for (int i = start; i < end && i <= bookings.size(); i++) {
				auxBookings.add(bookings.get(i - 1));
			}

			model.addAttribute("reservations", auxBookings);
		}

		return "reservationTemplate";

	}

	@GetMapping("/reservationInfo/{id}")
	public String clientreservation(Model model, HttpServletRequest request, @PathVariable Long id) {
		UserE currentUser = userRepository.findByNick(request.getUserPrincipal().getName()).orElseThrow();
		UserE foundUser = reservationRepository.findById(id).orElseThrow().getUser();

		if (currentUser.equals(foundUser)) {
			model.addAttribute("reservation", reservationRepository.findById(id).orElseThrow());
			return "reservationInfo";
		} else
			return "/error";

	}

	@GetMapping("/cancelReservation/{id}") // this should be a post
	public String deleteReservation(HttpServletRequest request, @PathVariable Long id) {

		UserE currentUser = userRepository.findByNick(request.getUserPrincipal().getName()).orElseThrow();
		UserE foundUser = reservationRepository.findById(id).orElseThrow().getUser();

		if (currentUser.equals(foundUser)) {
			Reservation reservation = reservationRepository.findById(id).orElseThrow();
			if (reservation != null) {
				UserE user = reservation.getUser();
				user.getReservations().remove(reservation);
				userRepository.save(user);

				Hotel hotel = reservation.getHotel();
				hotel.getReservations().remove(reservation);
				hotelRepository.save(hotel);

				Room room = reservation.getRooms();
				room.getReservations().remove(reservation);
				roomRepository.save(room);

				reservationRepository.deleteById(id);
			}
			return "redirect:/clientreservations";
		} else
			return "/error";
	}

	// MANAGER CONTROLLERS

	/**
	 * Loads the first 6 hotels of a manager
	 */
	@GetMapping("/viewhotelsmanager")
	public String viewHotelsManager(Model model, HttpServletRequest request) {

		String managernick = request.getUserPrincipal().getName();
		UserE currentManager = userRepository.findByNick(managernick).orElseThrow();

		List<Hotel> hotels = currentManager.getHotels();

		if (hotels.size() > 6) {
			hotels = hotels.subList(0, 6);
		}

		model.addAttribute("hotels", hotels);

		return "viewhotelsmanager";

	}

	@GetMapping("/testChart")
	public String testChart(Model model) {

		List<Integer> info = new ArrayList<>();
		model.addAttribute("info", info);

		return "testChart";

	}

	/**
	 * Loads the data of all hotels owned by a manager to be viewed in a graph
	 * 
	 * @param model
	 * @param request
	 * @return The chart template
	 */
	@GetMapping("/chartsManager")
	public String chartsManager(Model model, HttpServletRequest request) {

		String managernick = request.getUserPrincipal().getName();
		UserE currentManager = userRepository.findByNick(managernick).orElseThrow();

		var reviewsAverage = new ArrayList<String>();
		var hotelNames = new ArrayList<String>();

		for (Hotel hotel : currentManager.getHotels()) {
			hotelNames.add(hotel.getName());
			reviewsAverage.add("%.1f".formatted((hotel.getAverageRating())));
		}

		model.addAttribute("reviewsAverage", reviewsAverage);
		model.addAttribute("hotelNames", hotelNames);

		return "chartsManager";

	}

	@PostMapping("/application")
	public String managerApplication(Model model, HttpServletRequest request) {

		String nick = request.getUserPrincipal().getName();
		UserE manager = userRepository.findByNick(nick).orElseThrow();
		manager.setRejected(false);
		userRepository.save(manager);
		model.addAttribute("user", manager);
		return "profile";

	}

	// ADMIN CONTROLLERS
	@GetMapping("/chartsadmin")
	public String chartsAdmin(Model model, HttpServletRequest request) {

		return "chartsadmin";

	}

	@GetMapping("/managervalidation")
	public String managerValidation(Model model) {
		List<UserE> unvalidatedManagersList = new ArrayList<>();
		unvalidatedManagersList = userRepository.findByValidatedAndRejected(false, false);

		if (unvalidatedManagersList != null) {
			model.addAttribute("unvalidatedManagers", unvalidatedManagersList);
		}

		return "managervalidation";
	}

	@PostMapping("/rejection/{id}")
	public String rejectManager(Model model, @PathVariable Long id) {
		UserE manager = userRepository.findById(id).orElseThrow();

		if (manager != null) {
			manager.setRejected(true);
			manager.setvalidated(false);
			userRepository.save(manager);
		}
		return "redirect:/managervalidation";

	}

	@PostMapping("/acceptance/{id}")
	public String acceptManager(Model model, @PathVariable Long id) {
		UserE manager = userRepository.findById(id).orElseThrow();

		if (manager != null) {
			manager.setRejected(false);
			manager.setvalidated(true);
			userRepository.save(manager);
		}
		return "redirect:/managervalidation";

	}

	@GetMapping("/managerlist")
	public String managerList(Model model, HttpServletRequest request) {

		return "managerlist";

	}

	@GetMapping("/editprofile/{id}")
	public String editProfile(Model model, HttpServletRequest request, @PathVariable Long id) {

		UserE currentUser = userRepository.findByNick(request.getUserPrincipal().getName()).orElseThrow();
		UserE foundUser = userRepository.findById(id).orElseThrow(); // need to transform the throw into 404 error. Page
																		// 25 // database

		if (currentUser.equals(foundUser)) {
			model.addAttribute("user", foundUser);
			return "editprofile";
		} else
			return "/error";

	}

	@PostMapping("/replace/{id}")
	public String editProfile(HttpServletRequest request, Model model, @PathVariable Long id,

			@RequestParam String name,
			@RequestParam String lastname,
			@RequestParam String location,
			@RequestParam String org,
			@RequestParam String language,
			@RequestParam String phone,
			@RequestParam String mail,
			@RequestParam String bio) { // could be changed to construct user automatically

		UserE currentUser = userRepository.findByNick(request.getUserPrincipal().getName()).orElseThrow();
		UserE foundUser = userRepository.findById(id).orElseThrow();

		if (currentUser.equals(foundUser)) {
			foundUser.setName(name);
			foundUser.setLocation(location);
			foundUser.setOrganization(org);
			foundUser.setLanguage(language);
			foundUser.setPhone(phone);
			foundUser.setEmail(mail);
			foundUser.setBio(bio);

			userRepository.save(foundUser);

			model.addAttribute("user", foundUser);

			return "redirect:/profile";
		} else
			return "/error";
	}

	@GetMapping("/profile/{id}/images")
	public ResponseEntity<Object> downloadImage(HttpServletRequest request, @PathVariable Long id) throws SQLException {

		Optional<UserE> user = userRepository.findById(id);
		if (user.isPresent() && user.get().getImageFile() != null) {

			Resource file = new InputStreamResource(user.get().getImageFile().getBinaryStream());

			return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpg")
					.contentLength(user.get().getImageFile().length()).body(file);
		} else {
			return ResponseEntity.notFound().build();
			// return "/error";
		}
	}

	@PostMapping("/editprofileimage/{id}")
	public String editImage(HttpServletRequest request, @RequestParam MultipartFile imageFile,
			@PathVariable Long id,
			Model model) throws IOException {

		UserE currentUser = userRepository.findByNick(request.getUserPrincipal().getName()).orElseThrow();
		UserE foundUser = userRepository.findById(id).orElseThrow();

		if (currentUser.equals(foundUser)) {
			if (!imageFile.getOriginalFilename().isBlank()) {
				currentUser.setImageFile(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));
				userRepository.save(currentUser);
			}
			return "redirect:/editprofile/" + id;

		} else
			return "/error";

	}

	@GetMapping("/profile")
	public String profile(Model model, HttpServletRequest request) {

		String usernick = request.getUserPrincipal().getName();
		UserE currentUser = userRepository.findByNick(usernick).orElseThrow();
		if (currentUser.getBio() == null) {
			model.addAttribute("hasbio", false);
			currentUser.setBio("");
		} else
			model.addAttribute("hasbio", true);

		if (currentUser.getLocation() == null) {
			model.addAttribute("haslocation", false);
			currentUser.setLocation("");
		} else
			model.addAttribute("haslocation", true);

		if (currentUser.getPhone() == null) {
			model.addAttribute("hasphone", false);
			currentUser.setPhone(" ");
		} else
			model.addAttribute("hasphone", true);

		if (currentUser.getOrganization() == null) {
			model.addAttribute("hasorg", false);
			currentUser.setOrganization(" ");
		} else
			model.addAttribute("hasorg", true);

		if (currentUser.getLanguage() == null) {
			model.addAttribute("haslang", false);
			currentUser.setLanguage(" ");
		} else
			model.addAttribute("haslang", true);

		model.addAttribute("user", currentUser);
		model.addAttribute("imageFile", currentUser.getImageFile());

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
	public String registerClient(Model model, UserE user, Integer type) {
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