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
import es.codeurjc.yourHOmeTEL.model.Room;
import es.codeurjc.yourHOmeTEL.model.UserE;
import es.codeurjc.yourHOmeTEL.repository.HotelRepository;
import es.codeurjc.yourHOmeTEL.repository.UserRepository;
import es.codeurjc.yourHOmeTEL.service.UserService;
import jakarta.persistence.ManyToOne;
import jakarta.servlet.http.HttpServletRequest;



@Controller
public class HotelController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	HotelRepository hotelRepository;
    
	@GetMapping("/edithotel/{id}")
	public String edithotel(Model model, @PathVariable Long id ) {
		
		Hotel hotel = hotelRepository.findById(id).orElseThrow(); 
		model.addAttribute("hotel", hotel);
		return "editHotel";

	}

	@PostMapping("/edithotel/{id}")
	public String edithotel(Model model, @PathVariable Long id,

	@RequestParam	String name,
	@RequestParam	int numRooms,
	@RequestParam	String location,
	@RequestParam	String description){

		Hotel hotel = hotelRepository.findById(id).orElseThrow();

		hotel.setName(name);
		hotel.setLocation(location);
		hotel.setDescription(description);

		hotelRepository.save(hotel);

		model.addAttribute("hotel", hotel);

		return "redirect:/viewhotelsmanager";
	
	}

	@GetMapping("/deleteHotel/{id}")
	public String deleteHotel(Model model, @PathVariable Long id) {

		Optional <Hotel> hotel = hotelRepository.findById(id); 
		if (hotel.isPresent()) {
			hotelRepository.deleteById(id);
		}

		return "redirect:/viewhotelsmanager";
	}



    
    @GetMapping("/hotelinformation/{id}")
	public String hotelinformation(Model model,  HttpServletRequest request, @PathVariable Long id) {
		Hotel hotel = hotelRepository.findById(id).orElseThrow();
		model.addAttribute("hotel", hotel);

		return "hotelinformation";

	}

    @GetMapping("/hotelreview")
	public String hotelreview(Model model,  HttpServletRequest request) {

		return "hotelreview";

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


	@GetMapping("/addHotel")
	public String addHotel(Model model,  HttpServletRequest request) {
		Optional <UserE> user = userRepository.findByNick(request.getUserPrincipal().getName());
		if (user.isPresent()){
			model.addAttribute("name", user.get().getName());
			return "addHotel";
		}
		else
			return "redirect:/login";
 
	}

	@PostMapping("/addHotel")
	public String addHotelPost(HttpServletRequest request, Hotel newHotel, Integer room1, Integer cost1, Integer room2, Integer cost2, Integer room3, Integer cost3, Integer room4, Integer cost4) {
		//Falta añadir lo de las fotos
		UserE user = userRepository.findByNick(request.getUserPrincipal().getName()).orElseThrow();

		newHotel.setManager(user);
		newHotel.setRooms(new ArrayList<>());
		newHotel.setReservation(new ArrayList<>());
		newHotel.setReviews(new ArrayList<>());

		for(int i = 0; i < room1; i++){
			newHotel.getRooms().add(new Room(1, cost1, new ArrayList<>(), newHotel));
		}
		for(int i = 0; i < room2; i++){
			newHotel.getRooms().add(new Room(2, cost2, new ArrayList<>(), newHotel));
		}
		for(int i = 0; i < room3; i++){
			newHotel.getRooms().add(new Room(3, cost3, new ArrayList<>(), newHotel));
		}
		for(int i = 0; i < room4; i++){
			newHotel.getRooms().add(new Room(4, cost4, new ArrayList<>(), newHotel));
		}
		hotelRepository.save(newHotel);
		return "redirect:/viewhotelsmanager";
	}
}
