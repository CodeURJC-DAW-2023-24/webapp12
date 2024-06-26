package yourHOmeTEL.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;

import org.hibernate.engine.jdbc.BlobProxy;
import java.sql.Blob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import yourHOmeTEL.model.Hotel;
import yourHOmeTEL.model.Room;
import yourHOmeTEL.model.UserE;
import yourHOmeTEL.service.HotelService;
import yourHOmeTEL.service.ReviewService;
import yourHOmeTEL.service.RoomService;
import yourHOmeTEL.service.UserService;

@Controller
public class HotelController {

	@Autowired
	UserService userService;

	@Autowired
	HotelService hotelService;

	@Autowired
	ReviewService reviewService;

	@Autowired
	RoomService roomService;


	@GetMapping("/editHotel/{id}")
	public String editHotel(HttpServletRequest request, Model model, @PathVariable Long id) {

		UserE currentUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
		UserE foundUser = hotelService.findById(id).orElseThrow().getManager();

		if (currentUser.equals(foundUser)) {
			Hotel hotel = hotelService.findById(id).orElseThrow();
			model.addAttribute("hotel", hotel);
			return "editHotel";

		} else
			return "/error";
	}

	@PostMapping("/editHotel/{id}")
	public String editHotel(Model model, HttpServletRequest request, Hotel newHotel, Integer room1, Integer cost1,
			Integer room2,
			Integer cost2, Integer room3, Integer cost3, Integer room4, Integer cost4, @PathVariable Long id)
			throws IOException {

		UserE currentUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
		UserE foundUser = hotelService.findById(id).orElseThrow().getManager();

		if (currentUser.equals(foundUser)) {
			Hotel hotel = hotelService.findById(id).orElseThrow();

			hotel.setName(newHotel.getName());
			hotel.setLocation(newHotel.getLocation());
			hotel.setDescription(newHotel.getDescription());

			if (room1 != null && cost1 != null)
				for (int i = 0; i < room1; i++) {
					Room room = new Room(1, cost1, new ArrayList<>(), newHotel);
					hotel.getRooms().add(room);
					roomService.save(room);
				}

			if (room2 != null && cost2 != null)
				for (int i = 0; i < room2; i++) {
					Room room = new Room(2, cost2, new ArrayList<>(), newHotel);
					hotel.getRooms().add(room);
					roomService.save(room);
				}

			if (room3 != null && cost3 != null)
				for (int i = 0; i < room3; i++) {
					Room room = new Room(3, cost3, new ArrayList<>(), newHotel);
					hotel.getRooms().add(room);
					roomService.save(room);
				}

			if (room4 != null && cost4 != null)
				for (int i = 0; i < room4; i++) {
					Room room = new Room(4, cost4, new ArrayList<>(), newHotel);
					hotel.getRooms().add(room);
					roomService.save(room);
				}

			hotelService.save(hotel);

			model.addAttribute("hotel", hotel);

			return "redirect:/viewHotelsManager";

		} else
			return "/error";

	}

	@GetMapping("/deleteHotel/{id}")
	public String deleteHotel(HttpServletRequest request, Model model, @PathVariable Long id) {

		UserE currentUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
		UserE foundUser = hotelService.findById(id).orElseThrow().getManager();

		if (currentUser.equals(foundUser)) {
			Optional<Hotel> hotel = hotelService.findById(id);
			if (hotel.isPresent()) {
				hotelService.deleteById(id);
			}

			return "redirect:/viewHotelsManager";

		} else
			return "/error";
	}

	@GetMapping("/index/{id}/images")
	public ResponseEntity<Object> downloadImage(HttpServletRequest request, @PathVariable Long id) throws SQLException {

		Optional<Hotel> hotel = hotelService.findById(id);
		if (hotel.isPresent() && hotel.get().getImageFile() != null) {

			Resource file = new InputStreamResource(hotel.get().getImageFile().getBinaryStream());
			return ResponseEntity.ok().
			header(HttpHeaders.CONTENT_TYPE, "image/jpg")
			.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=image.jpg")		
			.contentLength(hotel.get().getImageFile().length())
			.body(file);

		} else {
			return ResponseEntity.notFound().build();
			// return "/error";
		}
	}

	@PostMapping("/editHotelImage/{id}")
	public String editImage(HttpServletRequest request, @RequestParam MultipartFile imageFile,
			@PathVariable Long id,
			Model model) throws IOException {

		UserE currentUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
		UserE foundUser = hotelService.findById(id).orElseThrow().getManager();

		if (currentUser.equals(foundUser)) {
			Hotel hotel = hotelService.findById(id).orElseThrow();

			if (!imageFile.getOriginalFilename().isBlank()) {
				hotel.setImageFile(BlobProxy.generateProxy(imageFile.getInputStream(), imageFile.getSize()));
				hotelService.save(hotel);
			}
			model.addAttribute("hotel", hotel);
			return "redirect:/editHotel/" + id;

		} else
			return "/error";
	}

	@PostMapping("/selectHotelImage/{imgName}")
	public String editImage(@RequestParam MultipartFile imageFile, @PathVariable String imgName,
			Model model, HttpServletRequest request) throws IOException {

		if (!imageFile.getOriginalFilename().isBlank()) {
			Path path = Paths.get("uploaded-images");
			if (!Files.exists(path)) {
				Files.createDirectories(path);
			}
			Path destination = path.resolve(imageFile.getOriginalFilename());
			Files.copy(imageFile.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
			return "redirect:/addHotel/" + imageFile.getOriginalFilename();
		} else
			return "redirect:/addHotelPhoto/" + imgName;
	}

	@GetMapping("/hotelInformation/{id}")
	public String hotelInformation(Model model, @PathVariable Long id) {

		UserE hotelManager = hotelService.findById(id).orElseThrow().getManager();

		if (hotelManager.getvalidated()) {
			Hotel hotel = hotelService.findById(id).orElseThrow();
			if (hotel.getManager().getvalidated() == false)
				return "redirect:/error";
			model.addAttribute("hotel", hotel);
			model.addAttribute("numRooms", hotel.getNumRooms());

			return "hotelInformation";

		} else
			return "/error";

	}

	@GetMapping("/addHotel/{imgName}")
	public String addHotelWithPhoto(Model model, HttpServletRequest request, @PathVariable String imgName) {

		Optional<UserE> user = userService.findByNick(request.getUserPrincipal().getName());
		if (user.isPresent()) {
			model.addAttribute("name", user.get().getName());
			model.addAttribute("imgName", imgName);
			return "addHotel";

		} else
			return "redirect:/login";

	}

	@GetMapping("/loadHotelImage/{imgName}")
	public ResponseEntity<Object> downloadHotelImage(HttpServletRequest request, @PathVariable String imgName)
			throws SQLException {
		try {
			Path imagePath = Paths.get("uploaded-images", imgName);
			Resource imageResource;
			if (!Files.exists(imagePath)) {
				// If the requested image is not found, load the default image from the classpath
				imageResource = new ClassPathResource("/static/images/default-hotel.jpg");
			} else {
				imageResource = new UrlResource(imagePath.toUri());
			}
			Blob image = hotelService.generateImage(imageResource);
			Resource file = new InputStreamResource(image.getBinaryStream());
			return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpg")
					.contentLength(image.length()).body(file);
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping("/createHotel/{imgName}")
	public String addHotelPost(HttpServletRequest request, Hotel newHotel, Integer room1, Integer cost1, Integer room2,
			Integer cost2, Integer room3, Integer cost3, Integer room4, Integer cost4, @PathVariable String imgName)
			throws IOException {
	
		UserE user = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
	
		newHotel.setManager(user);
		newHotel.setRooms(new ArrayList<>());
		newHotel.setReservations(new ArrayList<>());
		newHotel.setReviews(new ArrayList<>());
	
		Path imagePath = Paths.get("uploaded-images", imgName);
		byte[] imageBytes;
		if (!Files.exists(imagePath)) {
			// If the requested image is not found, load the default image from the classpath
			Resource imageResource = new ClassPathResource("/static/images/default-hotel.jpg");
			try (InputStream imageStream = imageResource.getInputStream()) {
				imageBytes = imageStream.readAllBytes();
			}
		} else {
			imageBytes = Files.readAllBytes(imagePath);
		}
	
		newHotel.setImageFile(BlobProxy.generateProxy(imageBytes));
		newHotel.setImage(true);
	
		// ... rest of the code ...
	
		hotelService.save(newHotel);
	
		// Only delete the image if it's not the default image
		if (Files.exists(imagePath)) {
			try {
				Files.delete(imagePath);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "redirect:/viewHotelsManager";
	}

	@GetMapping("/addHotelPhoto/{imgName}")
	public String addHotelPost(Model model, HttpServletRequest request, @PathVariable String imgName) {
		model.addAttribute("imgName", imgName);
		model.addAttribute("state", false);
		return "addHotelPhoto";
	}

	@GetMapping("/clientList/{id}")
	public String clientList(Model model, HttpServletRequest request, @PathVariable Long id) {
		UserE currentUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
		UserE foundUser = hotelService.findById(id).orElseThrow().getManager();

		if (currentUser.equals(foundUser)) {
			Hotel hotel = hotelService.findById(id).orElseThrow();
			List<UserE> validClients = new ArrayList<>();
			validClients = hotelService.getValidClients(hotel);
			model.addAttribute("clients", validClients);

			return "clientList";

		} else
			return "/error";
	}

	/**
	 * Using AJAX, loads the next 6 hotels in the page, or none if all are loaded
	 * 
	 * @param model
	 * @param start
	 * @param end
	 * @return
	 */
	@GetMapping("/loadMoreHotels/{start}/{end}")
	public String loadMoreHotels(Model model,
			@PathVariable Long start,
			@PathVariable Long end) {

		var hotelsQuantity = hotelService.count();

		if (start <= hotelsQuantity) {

			var hotels = new ArrayList<>();

			// We obtain the hotels IDs for the actual page
			List<Long> hotelIds = new ArrayList<>();
			for (long index = start; index < end && index <= hotelsQuantity; index++) {
				hotelIds.add(index);
			}

			// We look for the Hotel objects related to the IDs
			for (Long hotelId : hotelIds) {
				Hotel hotel = hotelService.findById(hotelId).orElse(null);
				if (hotel != null) {
					hotels.add(hotel);
				}
			}

			model.addAttribute("hotels", hotels);
		}

		return "hotelTemplate";
	}

	/**
	 * Using AJAX, loads the next 6 hotels in the page, or none if all are loaded
	 * 
	 * @param model
	 * @param start
	 * @param end
	 * @return
	 */
	@GetMapping("/loadMoreHotelsManagerView/{start}/{end}")
	public String loadMoreHotelsManagerView(
			Model model,
			HttpServletRequest request,
			@PathVariable Long start,
			@PathVariable Long end) {

		var hotelsQuantity = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow().getHotels()
				.size();

		if (start <= hotelsQuantity) {

			var hotels = new ArrayList<>();

			// We obtain the hotels IDs for the actual page
			List<Long> hotelIds = new ArrayList<>();
			for (long index = start; index < end && index <= hotelsQuantity; index++) {
				hotelIds.add(index);
			}

			// We look for the Hotel objects related to the IDs
			for (Long hotelId : hotelIds) {
				Hotel hotel = hotelService.findById(hotelId).orElse(null);
				if (hotel != null) {
					hotels.add(hotel);
				}
			}

			model.addAttribute("hotels", hotels);
		}

		return "hotelListViewTemplate";
	}

}