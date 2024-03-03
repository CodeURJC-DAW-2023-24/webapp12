package es.codeurjc.yourHOmeTEL.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;

import es.codeurjc.yourHOmeTEL.model.Hotel;
import es.codeurjc.yourHOmeTEL.model.Reservation;
import es.codeurjc.yourHOmeTEL.model.Review;
import es.codeurjc.yourHOmeTEL.model.Room;
import es.codeurjc.yourHOmeTEL.model.UserE;
import es.codeurjc.yourHOmeTEL.repository.HotelRepository;
import es.codeurjc.yourHOmeTEL.repository.ReviewRepository;
import es.codeurjc.yourHOmeTEL.repository.UserRepository;
import es.codeurjc.yourHOmeTEL.service.HotelService;
import es.codeurjc.yourHOmeTEL.service.UserService;
import jakarta.persistence.ManyToOne;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class HotelController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	HotelService hotelService;

	@Autowired
	HotelRepository hotelRepository;

	@Autowired
	ReviewRepository reviewRepository;

	@GetMapping("/edithotel/{id}")
	public String edithotel(Model model, @PathVariable Long id) {

		Hotel hotel = hotelRepository.findById(id).orElseThrow();
		model.addAttribute("hotel", hotel);
		return "editHotel";

	}

	@PostMapping("/edithotel/{id}")
	public String edithotel(Model model, HttpServletRequest request, Hotel newHotel, Integer room1, Integer cost1, Integer room2,
	Integer cost2, Integer room3, Integer cost3, Integer room4, Integer cost4, @PathVariable Long id) 
	throws IOException{

		Hotel hotel = hotelRepository.findById(id).orElseThrow();

		hotel.setName(newHotel.getName());
		hotel.setLocation(newHotel.getLocation());
		hotel.setDescription(newHotel.getDescription());

		List <Room> resetedRooms = new ArrayList<>();
		hotel.setRooms(resetedRooms);

		if(room1 != null)
			for (int i = 0; i < room1; i++) {
				hotel.getRooms().add(new Room(1, cost1, new ArrayList<>(), newHotel));
			}
		
		if(room2 != null)
			for (int i = 0; i < room2; i++) {
				hotel.getRooms().add(new Room(2, cost2, new ArrayList<>(), newHotel));
			}

		if(room3 != null)
			for (int i = 0; i < room3; i++) {
				hotel.getRooms().add(new Room(3, cost3, new ArrayList<>(), newHotel));
			}

		if(room4 != null)
			for (int i = 0; i < room4; i++) {
				hotel.getRooms().add(new Room(4, cost4, new ArrayList<>(), newHotel));
			}

		hotelRepository.save(hotel);

		model.addAttribute("hotel", hotel);

		return "redirect:/viewhotelsmanager";

	}

	@GetMapping("/deleteHotel/{id}")
	public String deleteHotel(Model model, @PathVariable Long id) {

		Optional<Hotel> hotel = hotelRepository.findById(id);
		if (hotel.isPresent()) {
			hotelRepository.deleteById(id);
		}

		return "redirect:/viewhotelsmanager";
	}

	@GetMapping("/index/{id}/images")
	public ResponseEntity<Object> downloadImage(@PathVariable Long id) throws SQLException {

		Optional<Hotel> hotel = hotelRepository.findById(id);
		if (hotel.isPresent() && hotel.get().getImageFile() != null) {

			Resource file = new InputStreamResource(hotel.get().getImageFile().getBinaryStream());

			return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpg")
					.contentLength(hotel.get().getImageFile().length()).body(file);

		} else {
			return ResponseEntity.notFound().build();
		}

	}



	@PostMapping("/edithotelimage/{id}")
    public String editImage(@RequestParam MultipartFile imageFile,
                            @PathVariable Long id,
                            Model model) throws IOException {
        Hotel hotel = hotelRepository.findById(id).orElseThrow();

        if (!imageFile.getOriginalFilename().isBlank()) {
            hotel.setImageFile(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));
            hotelRepository.save(hotel);           
        }
		model.addAttribute("hotel", hotel);
        return "redirect:/edithotel/"+id;
    }

	@PostMapping("/selecthotelimage/{imgName}")
    public String editImage(@RequestParam MultipartFile imageFile,
                            Model model, HttpServletRequest request, @PathVariable String imgName) throws IOException {
		
		if(!imageFile.getOriginalFilename().isBlank())
			return "redirect:/addHotel/"+imageFile.getOriginalFilename();
		else
			return "redirect:/addHotelPhoto/"+imgName;
    }

	@GetMapping("/hotelinformation/{id}")
	public String hotelinformation(Model model, HttpServletRequest request, @PathVariable Long id) {
		Hotel hotel = hotelRepository.findById(id).orElseThrow();
		if (hotel.getManager().getvalidated() == false)
			return "redirect:/error";
		model.addAttribute("hotel", hotel);
		model.addAttribute("numRooms", hotel.getNumRooms());

		return "/hotelinformation";

	}

	@GetMapping("/hotelReview/{id}")
	public String hotelreview(Model model, @PathVariable Long id) {
		Hotel hotel = hotelRepository.findById(id).orElseThrow();
		model.addAttribute("hotel", hotel);
		return "hotelReview";

	}

	/**
	 * This method adds to the DB the review posted by the client so it is
	 * displayed in the hotel's reviews page
	 * 
	 * @param model
	 * @param score
	 * @param comment
	 * @param date
	 * @param hotel
	 * @param user
	 * @return
	 */
	@PostMapping("/posthotelReviews/{id}")
	public String postReview(Model model, Review review, @PathVariable Long id) {
		Hotel targetHotel = hotelRepository.findById(id).orElseThrow();
		targetHotel.getReviews().add(review);
		hotelRepository.save(targetHotel);

		return "redirect:/hotelReviews" + id;
	}

	@GetMapping("/hotelReviews/{id}")
	public String hotelReviews(Model model, @PathVariable Long id) {
		Hotel selectedHotel = hotelRepository.findById(id).orElseThrow();
		model.addAttribute("hotel", selectedHotel);
		model.addAttribute("hotelreviews", selectedHotel.getReviews());
		model.addAttribute("numreviews", selectedHotel.getReviews().size());

		int totalReviews = 0;
		for (int i = 1; i <= 5; i++) {
			List<Review> reviews = reviewRepository.findByScore(i);
			int numReviews = reviews.size();
			totalReviews += numReviews;
			model.addAttribute("numreviews" + i, numReviews);
		}
		model.addAttribute("totalreviews", totalReviews);
		// PENDIENTE deberíamos obtener los datos de las reseñas para mostrar aquí
		// Mario: por que lo haces con hotel name y no id?

		for (int i = 5; i >= 1; i--) {
			int percentageOfIScoreReview = selectedHotel.getPercentageOfNScore(i);

			model.addAttribute("percentageReview" + i, percentageOfIScoreReview);
		}

		return "hotelReviews";
	}


	@GetMapping("/addHotel/{imgName}")
	public String addHotelWithPhoto(Model model, HttpServletRequest request, @PathVariable String imgName) {

		Optional<UserE> user = userRepository.findByNick(request.getUserPrincipal().getName());
		if (user.isPresent()) {
			model.addAttribute("name", user.get().getName());
			model.addAttribute("photo", imgName);
			return "addHotel";

			
		} else
			return "redirect:/login";

	}

	@PostMapping("/createHotel/{imgName}") 
	public String addHotelPost(HttpServletRequest request, Hotel newHotel, Integer room1, Integer cost1, Integer room2,
			Integer cost2, Integer room3, Integer cost3, Integer room4, Integer cost4, @PathVariable String imgName) 
			throws IOException{

		UserE user = userRepository.findByNick(request.getUserPrincipal().getName()).orElseThrow();

		newHotel.setManager(user);
		newHotel.setRooms(new ArrayList<>());
		newHotel.setReservations(new ArrayList<>());
		newHotel.setReviews(new ArrayList<>());

		Resource image = new ClassPathResource("/static/images/"+imgName);

        newHotel.setImageFile(BlobProxy.generateProxy(image.getInputStream(), image.contentLength()));
        newHotel.setImage(true);

		if(room1 != null)
			for (int i = 0; i < room1; i++) {
				newHotel.getRooms().add(new Room(1, cost1, new ArrayList<>(), newHotel));
			}

		if(room2 != null)
			for (int i = 0; i < room2; i++) {
				newHotel.getRooms().add(new Room(2, cost2, new ArrayList<>(), newHotel));
			}

		if(room3 != null)
			for (int i = 0; i < room3; i++) {
				newHotel.getRooms().add(new Room(3, cost3, new ArrayList<>(), newHotel));
			}

		if(room4 != null)
			for (int i = 0; i < room4; i++) {
				newHotel.getRooms().add(new Room(4, cost4, new ArrayList<>(), newHotel));
			}
		hotelRepository.save(newHotel);
		return "redirect:/viewhotelsmanager";
	}

	@GetMapping("/addHotelPhoto/{imgName}")
	public String addHotelPost(Model model, HttpServletRequest request, @PathVariable String imgName) {
		model.addAttribute("photo", imgName);
		return "addHotelPhoto";
	}

	@GetMapping("/clientlist/{id}")
	public String clientlist(Model model, HttpServletRequest request, @PathVariable Long id) {
		Hotel hotel = hotelRepository.findById(id).orElseThrow();
		List<UserE> validClients = new ArrayList<>();
		validClients = hotelService.getValidClients(hotel);

		model.addAttribute("clients", validClients);

		return "clientlist";

	}

	/**
	 * Loads the next 6 hotels
	 */
	/*
	 * @GetMapping("/loadMoreHotels/{start}/{end}")
	 * public String loadMoreHotels(
	 * Model model,
	 * 
	 * @PathVariable Long start,
	 * 
	 * @PathVariable Long end) {
	 * 
	 * var hotelsQuantity = hotelRepository.count();
	 * 
	 * if (start <= hotelsQuantity) {
	 * 
	 * var hotels = new ArrayList<>();
	 * 
	 * // we get the next 6 hotels or the remaining ones
	 * for (Long index = start; index < end && index < hotelsQuantity; index++) {
	 * hotels.add(hotelRepository.findById(index));
	 * }
	 * 
	 * model.addAttribute("hotels", hotels);
	 * }
	 * 
	 * return "/hotelTemplate";
	 * }
	 */


	 /**
		* Using AJAX, loads the next 6 hotels in the page, or none if all are loaded
		* @param model
		* @param start
		* @param end
		* @return
	  */
	@GetMapping("/loadMoreHotels/{start}/{end}")
	public String loadMoreHotels(Model model,
			@PathVariable Long start,
			@PathVariable Long end) {

		var hotelsQuantity = hotelRepository.count();

		if (start <= hotelsQuantity) {

			var hotels = new ArrayList<>();

			// Obtenemos los IDs de los hoteles para la página actual
			List<Long> hotelIds = new ArrayList<>();
			for (long index = start; index < end && index <= hotelsQuantity; index++) {
				hotelIds.add(index);
			}

			// Buscamos los objetos Hotel correspondientes a los IDs
			for (Long hotelId : hotelIds) {
				Hotel hotel = hotelRepository.findById(hotelId).orElse(null);
				if (hotel != null) {
					hotels.add(hotel);
				}
			}

			model.addAttribute("hotels", hotels);
		}

		return "hotelTemplate";
	}

}