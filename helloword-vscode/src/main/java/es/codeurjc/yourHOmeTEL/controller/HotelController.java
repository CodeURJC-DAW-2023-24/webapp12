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
import es.codeurjc.yourHOmeTEL.repository.HotelRepository;
import es.codeurjc.yourHOmeTEL.repository.UserRepository;
import es.codeurjc.yourHOmeTEL.service.UserService;
import jakarta.persistence.ManyToOne;
import jakarta.servlet.http.HttpServletRequest;



@Controller
public class HotelController {

	@Autowired
	HotelRepository hotelRepository;
    
    @GetMapping("/edithotel")
	public String edithotel(Model model,  HttpServletRequest request) {

		return "edithotel";

	}

    
    @GetMapping("/hotelinformation/{id}")
	public String hotelinformation(Model model,  HttpServletRequest request, @PathVariable Long id) {
		Hotel hotel = hotelRepository.findById(id).orElseThrow();
		model.addAttribute("hotel", hotel);

		return "hotelinformation";

	}

    @GetMapping("/hotelreview/{id}")
	public String hotelreview(Model model,  HttpServletRequest request, @PathVariable Long id) {

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
}
