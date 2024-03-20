package yourHOmeTEL.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.io.IOException;
import java.sql.SQLException;

import org.springframework.core.io.Resource;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
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

import jakarta.servlet.http.HttpServletRequest;
import yourHOmeTEL.model.Hotel;
import yourHOmeTEL.model.Reservation;
import yourHOmeTEL.model.Review;
import yourHOmeTEL.model.UserE;
import yourHOmeTEL.service.HotelService;
import yourHOmeTEL.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private HotelService hotelService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	// PUBLIC CONTROLLERS

	// ADVANCED RECOMMENDATION ALGORITHM
	@GetMapping("/index")
	public String index(Model model, HttpServletRequest request) {
		List<Hotel> recomendedHotels = new ArrayList<>();
		try {
			String nick = request.getUserPrincipal().getName();
			UserE user = userService.findByNick(nick).orElseThrow();
			List<Reservation> userReservations = user.getReservations();
			recomendedHotels = userService.findRecomendedHotels(6, userReservations, user);

		} catch (NullPointerException e) {

		} finally {
			if (recomendedHotels.size() < 6) {
				// size +1 to avoid looking for id = 0 if size = 0
				for (int i = recomendedHotels.size() + 1; i < 7; i++) {
					Hotel hotel = hotelService.findById((long) i).orElseThrow();
					if (hotel != null && hotel.getManager().getvalidated())
						recomendedHotels.add(hotel);
				}
			}
		}
		model.addAttribute("hotels", recomendedHotels);
		return "index";
	}

	@GetMapping("/indexSearch")
	public String indexSearch(Model model, @RequestParam String searchValue) {
		List<Hotel> hotels = hotelService.findTop6ByManager_ValidatedAndNameContainingIgnoreCaseOrderByNameDesc(true,
				searchValue);
		model.addAttribute("hotels", hotels);
		return "index";

	}

	@GetMapping("/error")
	public String error(Model model, HttpServletRequest request) {
		return "/error";

	}

	@GetMapping("/returnMainPage")
	public String returnMainPage(Model model, HttpServletRequest request) {
		return "redirect:/index";

	}

	// CLIENT CONTROLLERS

	// MANAGER CONTROLLERS

	/**
	 * Loads the first 6 hotels of a manager
	 */
	@GetMapping("/viewHotelsManager")
	public String viewHotelsManager(Model model, HttpServletRequest request) {

		String managernick = request.getUserPrincipal().getName();
		UserE currentManager = userService.findByNick(managernick).orElseThrow();

		List<Hotel> hotels = currentManager.getHotels();

		if (hotels.size() > 6) {
			hotels = hotels.subList(0, 6);
		}

		model.addAttribute("hotels", hotels);

		return "viewHotelsManager";

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
		UserE currentManager = userService.findByNick(managernick).orElseThrow();

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

	@PostMapping("/application/{id}")
	public String managerApplication(Model model, HttpServletRequest request, @PathVariable Long id) {

		UserE currentUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
		UserE foundManager = userService.findById(id).orElseThrow();

		if (currentUser.equals(foundManager)) {
			foundManager.setRejected(false);
			userService.save(foundManager);
			model.addAttribute("user", foundManager);
			return "redirect:/profile";

		} else
			return "/error";
	}

	// ADMIN CONTROLLERS
	@GetMapping("/chartsAdmin")
	public String chartsAdmin(Model model, HttpServletRequest request) {

		return "chartsAdmin";

	}

	@GetMapping("/managerValidation")
	public String managerValidation(Model model) {
		List<UserE> unvalidatedManagersList = new ArrayList<>();
		unvalidatedManagersList = userService.findByValidatedAndRejected(false, false);

		if (unvalidatedManagersList != null) {
			model.addAttribute("unvalidatedManagers", unvalidatedManagersList);
		}

		return "managerValidation";
	}

	@PostMapping("/rejection/{id}")
	public String rejectManager(Model model, @PathVariable Long id) {
		UserE manager = userService.findById(id).orElseThrow();

		if (manager != null) {
			manager.setRejected(true);
			manager.setvalidated(false);
			userService.save(manager);
		}
		return "redirect:/managerValidation";

	}

	@PostMapping("/acceptance/{id}")
	public String acceptManager(Model model, @PathVariable Long id) {
		UserE manager = userService.findById(id).orElseThrow();

		if (manager != null) {
			manager.setRejected(false);
			manager.setvalidated(true);
			userService.save(manager);
		}
		return "redirect:/managerValidation";

	}

	@GetMapping("/managerList")
	public String managerList(Model model, HttpServletRequest request) {

		return "managerList";

	}

	@GetMapping("/editProfile/{id}")
	public String editProfile(Model model, HttpServletRequest request, @PathVariable Long id) {

		UserE currentUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
		UserE foundUser = userService.findById(id).orElseThrow(); // need to transform the throw into 404 error. Page
																	// 25 // database

		if (currentUser.equals(foundUser)) {
			model.addAttribute("user", foundUser);
			return "editProfile";
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

		UserE currentUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
		UserE foundUser = userService.findById(id).orElseThrow();

		if (currentUser.equals(foundUser)) {
			foundUser.setName(name);
			foundUser.setLocation(location);
			foundUser.setOrganization(org);
			foundUser.setLanguage(language);
			foundUser.setPhone(phone);
			foundUser.setEmail(mail);
			foundUser.setBio(bio);

			userService.save(foundUser);

			model.addAttribute("user", foundUser);

			return "redirect:/profile";
		} else
			return "/error";
	}

	@GetMapping("/profile/{id}/images")
	public ResponseEntity<Object> downloadImage(HttpServletRequest request, @PathVariable Long id) throws SQLException {

		Optional<UserE> user = userService.findById(id);
		if (user.isPresent() && user.get().getImageFile() != null) {

			Resource file = new InputStreamResource(user.get().getImageFile().getBinaryStream());

			return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpg")
					.contentLength(user.get().getImageFile().length()).body(file);
		} else {
			return ResponseEntity.notFound().build();
			// return "/error";
		}
	}

	@PostMapping("/editProfileimage/{id}")
	public String editImage(HttpServletRequest request, @RequestParam MultipartFile imageFile,
			@PathVariable Long id,
			Model model) throws IOException {

		UserE currentUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
		UserE foundUser = userService.findById(id).orElseThrow();

		if (currentUser.equals(foundUser)) {
			if (!imageFile.getOriginalFilename().isBlank()) {
				currentUser.setImageFile(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));
				userService.save(currentUser);
			}
			return "redirect:/editProfile/" + id;

		} else
			return "/error";

	}

	@GetMapping("/profile")
	public String profile(Model model, HttpServletRequest request) {

		String usernick = request.getUserPrincipal().getName();
		UserE currentUser = userService.findByNick(usernick).orElseThrow();
		if (currentUser.getBio() == null) {
			model.addAttribute("hasBio", false);
			currentUser.setBio("");
		} else
			model.addAttribute("hasBio", true);

		if (currentUser.getLocation() == null) {
			model.addAttribute("hasLocation", false);
			currentUser.setLocation("");
		} else
			model.addAttribute("hasLocation", true);

		if (currentUser.getPhone() == null) {
			model.addAttribute("hasPhone", false);
			currentUser.setPhone(" ");
		} else
			model.addAttribute("hasPhone", true);

		if (currentUser.getOrganization() == null) {
			model.addAttribute("hasOrg", false);
			currentUser.setOrganization(" ");
		} else
			model.addAttribute("hasOrg", true);

		if (currentUser.getLanguage() == null) {
			model.addAttribute("hasLang", false);
			currentUser.setLanguage(" ");
		} else
			model.addAttribute("hasLang", true);

		model.addAttribute("user", currentUser);
		model.addAttribute("imageFile", currentUser.getImageFile());

		return "profile";

	}

	@GetMapping("/login")
	public String login(Model model) {

		return "login";
	}

	@GetMapping("/loginError")
	public String loginError(Model model) {
		return "loginError";
	}

	@GetMapping("/register")
	public String registerClient(Model model) {
		return "register";
	}

	@PostMapping("/register")
	public String registerClient(Model model, UserE user, Integer type) throws IOException {
		if (!userService.existNick(user.getNick())) {
			user.setPass(passwordEncoder.encode(user.getPass()));
			List<String> rols = new ArrayList<>();
			rols.add("USER");
			if (type == 0) {
				rols.add("CLIENT");
				user.setvalidated(null);
				user.setRejected(null);
			} else {
				rols.add("MANAGER");
				user.setvalidated(false);
				user.setRejected(false);
			}
			user.setRols(rols);
			user.setCollectionRols(rols);
			List<Reservation> reservations = new ArrayList<>();
			user.setReservations(reservations);
			List<Hotel> hotels = new ArrayList<>();
			user.setHotels(hotels);
			List<Review> reviews = new ArrayList<>();
			user.setReviews(reviews);

			user.setLanguage("");
			user.setLocation("");
			user.setBio("");
			user.setPhone("");
			user.setOrganization("");

			userService.save(user);

			Resource image = new ClassPathResource("/static/images/default-hotel.jpg");
			user.setImageFile(BlobProxy.generateProxy(image.getInputStream(), image.contentLength()));
			user.setImage(true);

			userService.save(user);

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