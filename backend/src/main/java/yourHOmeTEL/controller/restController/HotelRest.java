package yourHOmeTEL.controller.restController;

import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import yourHOmeTEL.model.Hotel;
import yourHOmeTEL.model.Reservation;
import yourHOmeTEL.model.Review;
import yourHOmeTEL.model.Room;
import yourHOmeTEL.model.UserE;
import yourHOmeTEL.service.HotelService;
import yourHOmeTEL.service.ReviewService;
import yourHOmeTEL.service.RoomService;
import yourHOmeTEL.service.UserService;

@RestController
@RequestMapping("/api")
public class HotelRest {

	interface HotelDetails
			extends UserE.Basic, Hotel.Complete, Review.Basic, Room.Basic, Reservation.Basic {
	}

	@Autowired
	private ObjectMapper objectMapper;

	@PostConstruct
	public void setup() {
		objectMapper.setDefaultMergeable(true);
	}

	@Autowired
	UserService userService;

	@Autowired
	HotelService hotelService;

	@Autowired
	ReviewService reviewService;

	@Autowired
	RoomService roomService;

	/**
	 * Goes to the edit page of a hotel so you can edit the values. It loads the
	 * current hotel data in the form
	 * 
	 */
	@JsonView(HotelDetails.class)
	@GetMapping("/hotels/{id}")
	public ResponseEntity<Hotel> hotelDataLoadingForEdition(
			HttpServletRequest request,
			Model model,
			@PathVariable Long id) {

		try {
			UserE currentUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
			UserE foundUser = hotelService.findById(id).orElseThrow().getManager();

			if (currentUser.equals(foundUser) && foundUser.getvalidated()) {
				Hotel hotel = hotelService.findById(id).orElseThrow();
				return ResponseEntity.ok(hotel);

			} else if (!foundUser.getvalidated()) {
				return ResponseEntity.notFound().build();
			} else {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}

		} catch (NoSuchElementException e) {
			return ResponseEntity.notFound().build();
		}

	}

	// PENDIENTE -> Pasar las habitaciones y los costes como un array
	@PostMapping("/hotels")
	public ResponseEntity<Hotel> createHotel(
			HttpServletRequest request,
			String name, String description, String location,
			Integer room1, Integer cost1,
			Integer room2, Integer cost2,
			Integer room3, Integer cost3,
			Integer room4, Integer cost4) throws NoSuchElementException {

		try {
			Hotel newHotel = new Hotel(name, location, description);
			UserE manager = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
			newHotel.setManager(manager);
			hotelService.save(newHotel);

			// PENDIENTE -> Refactorizar si se puede
			{
				if (room1 != null)
					for (int i = 0; i < room1; i++) {
						Room room = new Room(1, cost1, new ArrayList<>(), newHotel);
						newHotel.getRooms().add(room);
						roomService.save(room);
					}

				if (room2 != null)
					for (int i = 0; i < room2; i++) {
						Room room = new Room(2, cost2, new ArrayList<>(), newHotel);
						newHotel.getRooms().add(room);
						roomService.save(room);
					}

				if (room3 != null)
					for (int i = 0; i < room3; i++) {
						Room room = new Room(3, cost3, new ArrayList<>(), newHotel);
						newHotel.getRooms().add(room);
						roomService.save(room);
					}

				if (room4 != null)
					for (int i = 0; i < room4; i++) {
						Room room = new Room(4, cost4, new ArrayList<>(), newHotel);
						newHotel.getRooms().add(room);
						roomService.save(room);
					}

			}

			hotelService.save(newHotel);

		} catch (NoSuchElementException e) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok().build();

	}

	@PutMapping("/hotels/{id}")
	public ResponseEntity<Hotel> editHotel(Model model, HttpServletRequest request, Hotel newHotel, Integer room1,
			Integer cost1,
			Integer room2,
			Integer cost2, Integer room3, Integer cost3, Integer room4, Integer cost4, @PathVariable Long id)
			throws IOException {

		Hotel hotel;

		try {
			UserE currentUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
			UserE foundUser = hotelService.findById(id).orElseThrow().getManager();

			if (currentUser.equals(foundUser)) {
				hotel = hotelService.findById(id).orElseThrow();

				hotel.setName(newHotel.getName());
				hotel.setLocation(newHotel.getLocation());
				hotel.setDescription(newHotel.getDescription());

			} else {
				return ResponseEntity.badRequest().build();
			}

		} catch (NoSuchElementException e) {
			return ResponseEntity.notFound().build();
		}

		// PENDIENTE -> Refactorizar si se puede
		{
			if (room1 != null)
				for (int i = 0; i < room1; i++) {
					Room room = new Room(1, cost1, new ArrayList<>(), newHotel);
					hotel.getRooms().add(room);
					roomService.save(room);
				}

			if (room2 != null)
				for (int i = 0; i < room2; i++) {
					Room room = new Room(2, cost2, new ArrayList<>(), newHotel);
					hotel.getRooms().add(room);
					roomService.save(room);
				}

			if (room3 != null)
				for (int i = 0; i < room3; i++) {
					Room room = new Room(3, cost3, new ArrayList<>(), newHotel);
					hotel.getRooms().add(room);
					roomService.save(room);
				}

			if (room4 != null)
				for (int i = 0; i < room4; i++) {
					Room room = new Room(4, cost4, new ArrayList<>(), newHotel);
					hotel.getRooms().add(room);
					roomService.save(room);
				}

		}

		hotelService.save(hotel);

		model.addAttribute("hotel", hotel);

		return ResponseEntity.ok().build();

	}

	@DeleteMapping("/hotels/{id}")
	public ResponseEntity<Hotel> deleteHotel(HttpServletRequest request, @PathVariable Long id) {
		try {
			UserE currentUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
			UserE foundUser = hotelService.findById(id).orElseThrow().getManager();

			if (currentUser.equals(foundUser)) {
				Optional<Hotel> hotel = hotelService.findById(id);
				if (hotel.isPresent()) {
					hotelService.deleteById(id);
					return ResponseEntity.noContent().build();
				} else {
					return ResponseEntity.notFound().build();
				}

			} else {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}
		} catch (NoSuchElementException e) {
			return ResponseEntity.notFound().build();
		}

	}

	@GetMapping("/hotels/clients/{id}")
	public ResponseEntity<List<UserE>> clientlist(HttpServletRequest request, @PathVariable Long id) {
		try {
			UserE currentUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
			UserE foundUser = hotelService.findById(id).orElseThrow().getManager();

			if (currentUser.equals(foundUser)) {
				Hotel hotel = hotelService.findById(id).orElseThrow();
				List<UserE> validClients = hotelService.getValidClients(hotel);
				return ResponseEntity.ok(validClients);
			} else {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/hotelsInfo/{id}")
	public ResponseEntity<Map<String, Object>> hotelInformation(@PathVariable Long id) {
		try {
			UserE hotelManager = hotelService.findById(id).orElseThrow().getManager();

			if (hotelManager.getvalidated()) {
				Hotel hotel = hotelService.findById(id).orElseThrow();
				if (hotel.getManager().getvalidated() == false)
					return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

				Map<String, Object> hotelInformation = new HashMap<>();
				hotelInformation.put("hotel", hotel);
				hotelInformation.put("numRooms", hotel.getRooms().size());

				return ResponseEntity.ok(hotelInformation);
			} else {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/moreHotelsManagerView/{start}/{end}")
	public ResponseEntity<List<Hotel>> loadMoreHotelsManagerView(
			HttpServletRequest request,
			@PathVariable Long start,
			@PathVariable Long end) {

		var hotelsQuantity = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow().getHotels()
				.size();
		var hotels = new ArrayList<Hotel>();

		if (start <= hotelsQuantity) {
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
		}

		return ResponseEntity.ok(hotels);
	}

	// PUBLIC CONTROLLERS

	// ADVANCED RECOMMENDATION ALGORITHM
	@JsonView(HotelDetails.class)
	@GetMapping("/hotels/index/recomended")
	public ResponseEntity<List<Hotel>> index(HttpServletRequest request, Pageable pageable) {
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
				int i = 1;
				int sizeAllHotels = hotelService.findAll().size();
				while (recomendedHotels.size() < 7 && i <= sizeAllHotels) {
					// if there's a gap in the id sequence, it will throw an exception and continue
					// the loop
					try {
						Hotel hotel = hotelService.findById((long) i).orElseThrow();
						if (hotel != null && hotel.getManager().getvalidated())
							recomendedHotels.add(hotel);
					} catch (NoSuchElementException e) {
					}
					i++;
				}
			}
		}
		return ResponseEntity.ok(recomendedHotels);
	}

	@JsonView(HotelDetails.class) // check if this is done correctly. I just added pageable
	@GetMapping("/hotels/index/search")
	public ResponseEntity<PageResponse<Hotel>> indexSearch(@RequestParam String searchValue, Pageable pageable) {
		try {
			Page<Hotel> hotels = hotelService.findTop6ByManager_ValidatedAndNameContainingIgnoreCaseOrderByNameDesc(
					true,
					searchValue, pageable);
			if (hotels.hasContent()) {
				PageResponse<Hotel> response = new PageResponse<>();
				response.setContent(hotels.getContent());
				response.setPageNumber(hotels.getNumber());
				response.setPageSize(hotels.getSize());
				response.setTotalElements(hotels.getTotalElements());
				response.setTotalPages(hotels.getTotalPages());

				return ResponseEntity.ok(response);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (NoSuchElementException e) {
			return ResponseEntity.notFound().build();
		}
	}

	// MANAGER CONTROLLERS

	// this one is done already, dont touch it

	@JsonView(HotelDetails.class)
	@GetMapping("/hotels/manager/{id}")
	public ResponseEntity<PageResponse<Hotel>> viewHotelsManager(HttpServletRequest request, @PathVariable Long id,
			Pageable pageable) {
		try {
			UserE requestManager = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
			UserE targetManager = userService.findById(id).orElseThrow();

			if ((requestManager.equals(targetManager) && requestManager.getvalidated())
					|| requestManager.getRols().contains("ADMIN")) {
				Page<Hotel> hotels = hotelService.findByManager_Id(id, pageable);
				if (hotels.hasContent()) {
					PageResponse<Hotel> response = new PageResponse<>();
					response.setContent(hotels.getContent());
					response.setPageNumber(hotels.getNumber());
					response.setPageSize(hotels.getSize());
					response.setTotalElements(hotels.getTotalElements());
					response.setTotalPages(hotels.getTotalPages());

					return ResponseEntity.ok(response);
				} else {
					return ResponseEntity.notFound().build();
				}
			} else {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}
		} catch (NoSuchElementException e) {
			return ResponseEntity.notFound().build();
		}
	}
}
