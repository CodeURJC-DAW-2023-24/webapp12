package yourHOmeTEL.controller.restController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

import yourHOmeTEL.model.*;
import yourHOmeTEL.service.*;

@RestController
@RequestMapping("/api")
public class HotelRest {

	public interface HotelDetails
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

	@Autowired
	ReservationService reservationService;

	// CRUD OPERATIONS

	@JsonView(HotelDetails.class)
	@GetMapping("/hotels")
	@Operation(summary = "Load all hotels", description = "Returns a page of all hotels.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Hotels retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageResponse.class))),
			@ApiResponse(responseCode = "403", description = "Forbidden, the user does not have permission to view these hotels", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = "application/json"))
	})
	public ResponseEntity<PageResponse<Hotel>> loadAllHotels(
			HttpServletRequest request,
			Pageable pageable) {

		try {
			UserE user = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();

			if (user.getRols().contains("ADMIN")) {

				Page<Hotel> hotels = hotelService.findAll(pageable);
				PageResponse<Hotel> response = new PageResponse<>();
				response.setContent(hotels.getContent());
				response.setPageNumber(hotels.getNumber());
				response.setPageSize(hotels.getSize());
				response.setTotalElements(hotels.getTotalElements());
				response.setTotalPages(hotels.getTotalPages());

				return ResponseEntity.ok(response);

			} else {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}

		} catch (NoSuchElementException e) {
			return ResponseEntity.notFound().build();

		}
	}

	@JsonView(HotelDetails.class)
	@GetMapping("/hotels/{id}")
	public ResponseEntity<Map<String, Object>> getHotelData(
			HttpServletRequest request,
			@PathVariable Long id) {

		try {
			UserE currentUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
			UserE hotelManager = hotelService.findById(id).orElseThrow().getManager();

			if (hotelManager.getvalidated() || currentUser.getRols().contains("ADMIN")) {
				Hotel hotel = hotelService.findById(id).orElseThrow();
				Map<String, Object> hotelInformation = new HashMap<>();
				hotelInformation.put("hotel", hotel);
				hotelInformation.put("numRooms", hotel.getRooms().size());
				return ResponseEntity.ok(hotelInformation);
			} else {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}

		} catch (NoSuchElementException e) {
			return ResponseEntity.notFound().build();
		}

	}

	@JsonView(HotelDetails.class)
	@GetMapping("/hotels/manager/{id}")
	public ResponseEntity<PageResponse<Hotel>> viewHotelsManager(HttpServletRequest request, @PathVariable Long id,
			Pageable pageable) {
		try {
			UserE requestManager = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
			UserE targetManager = userService.findById(id).orElseThrow();

			if (requestManager.equals(targetManager) || requestManager.getRols().contains("ADMIN")) {
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

	@JsonView(HotelDetails.class)
	@GetMapping("/hotels/reservations/{id}")
	public ResponseEntity<Hotel> getReservationsFromHotel(
			HttpServletRequest request,
			@PathVariable Long id) {

		try {
			UserE currentUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
			Reservation reservation = reservationService.findById(id).orElseThrow();
			UserE reservationClient = reservation.getUser();
			Hotel targetHotel = reservation.getHotel();
			UserE hotelManager = targetHotel.getManager();

			if ((currentUser.equals(reservationClient) && hotelManager.getvalidated()
					|| currentUser.getRols().contains("ADMIN"))) {

				return ResponseEntity.ok(targetHotel);

			} else {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}

		} catch (NoSuchElementException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@JsonView(HotelDetails.class)
	@GetMapping("/hotels/reviews/{id}")
	public ResponseEntity<Hotel> getReviewsFromHotel(
			HttpServletRequest request,
			@PathVariable Long id) {

		try {
			UserE currentUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
			Review requestReview = reviewService.findById(id).orElseThrow();
			Hotel targetHotel = requestReview.getHotel();
			UserE hotelManager = targetHotel.getManager();

			if (hotelManager.getvalidated() || currentUser.getRols().contains("ADMIN")) {

				return ResponseEntity.ok(targetHotel);

			} else {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}

		} catch (NoSuchElementException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@JsonView(HotelDetails.class)
	@GetMapping("/hotels/rooms/{id}")
	public ResponseEntity<Hotel> getHotelFromRoom(
			HttpServletRequest request,
			@PathVariable Long id) {

		try {
			UserE currentUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
			Room requestRoom = roomService.findById(id).orElseThrow();
			Hotel targetHotel = requestRoom.getHotel();
			UserE hotelManager = targetHotel.getManager();

			if (hotelManager.getvalidated() || currentUser.getRols().contains("ADMIN")) {

				return ResponseEntity.ok(targetHotel);

			} else {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}

		} catch (NoSuchElementException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@JsonView(HotelDetails.class)
	@GetMapping("/hotels/{id}/clients")
	public ResponseEntity<List<UserE>> clientsList(HttpServletRequest request, @PathVariable Long id) {
		try {
			UserE currentUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
			UserE foundUser = hotelService.findById(id).orElseThrow().getManager();

			if (currentUser.equals(foundUser) || currentUser.getRols().contains("ADMIN")) {
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

			Integer[] roomsCapacities = { room1, room2, room3, room4 };
			Integer[] roomCosts = { cost1, cost2, cost3, cost4 };

			List<Room> hotelRooms = newHotel.getRooms();

			for (int i = 0; i < roomsCapacities.length; i++) {
				if (roomsCapacities[i] != null) {
					for (int j = 0; j < roomsCapacities[i]; j++) {
						Room roomForJPeople = new Room(roomsCapacities[i], roomCosts[i], new ArrayList<>(), newHotel);
						hotelRooms.add(roomForJPeople);
						roomService.save(roomForJPeople);
					}
				}
			}

			hotelService.save(newHotel);

		} catch (NoSuchElementException e) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok().build();

	}

	@JsonView(HotelDetails.class)
	@PutMapping("/hotels/{id}")
	public ResponseEntity<Hotel> editHotel(
			HttpServletRequest request,
			@PathVariable Long id,
			@RequestParam Map<String, Object> updates) throws JsonMappingException, JsonProcessingException {

		try {
			UserE currentUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
			UserE foundUser = hotelService.findById(id).orElseThrow().getManager();

			Hotel originalHotel = hotelService.findById(id).orElseThrow();

			if (currentUser.equals(foundUser)) {

				originalHotel = objectMapper.readerForUpdating(originalHotel)
						.readValue(objectMapper.writeValueAsString(updates)); // exists
				hotelService.save(originalHotel);
				return ResponseEntity.ok(originalHotel);

			} else {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}

		} catch (NoSuchElementException e) {
			return ResponseEntity.notFound().build();
		}

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
	/*
	 * Old controller (AJAX version)
	 * // PENDIENTE ELIMINAR PAGEABLE Y VER SI SE PUEDEN COMBINAR EL LOADMOREHOTELS
	 * DE
	 * // USER Y MANAGER EN UN MISMO CONTROLADOR
	 * 
	 * @JsonView(HotelDetails.class)
	 * 
	 * @GetMapping("/manager/hotels/{start}/{end}")
	 * public ResponseEntity<PageResponse<Hotel>> loadMoreHotelsManagerView(
	 * HttpServletRequest request,
	 * 
	 * @PathVariable Long start,
	 * 
	 * @PathVariable Long end,
	 * Pageable pageable) {
	 * 
	 * try {
	 * UserE manager =
	 * userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
	 * Integer numHotelsManager = manager.getHotels().size();
	 * 
	 * Integer startInt = start.intValue();
	 * Integer endInt = end.intValue();
	 * 
	 * if (startInt < 0 || endInt < 0 || startInt > numHotelsManager || startInt >
	 * endInt) {
	 * return ResponseEntity.badRequest().build();
	 * } else if (endInt > numHotelsManager) {
	 * endInt = numHotelsManager;
	 * }
	 * 
	 * // we get the next 6 hotels from the manager in a sublist, or less if there
	 * are
	 * // less than 6
	 * Page<Hotel> hotels = hotelService.findByManager_Id(manager.getId(),
	 * pageable);
	 * 
	 * List<Hotel> hotelsList = hotels.getContent().subList(startInt, endInt);
	 * hotels = new PageImpl<>(hotelsList, pageable, hotels.getTotalElements());
	 * 
	 * PageResponse<Hotel> response = new PageResponse<>();
	 * response.setContent(hotels.getContent());
	 * response.setPageNumber(hotels.getNumber());
	 * response.setPageSize(hotels.getSize());
	 * response.setTotalElements(hotels.getTotalElements());
	 * response.setTotalPages(hotels.getTotalPages());
	 * 
	 * return ResponseEntity.ok(response);
	 * 
	 * } catch (NoSuchElementException e) {
	 * return ResponseEntity.notFound().build();
	 * }
	 * }
	 */

	@JsonView(HotelDetails.class)
	@GetMapping("/manager/{id}/hotels/{pageNumber}")
	public ResponseEntity<List<Hotel>> loadMoreHotelsManagerView(
			HttpServletRequest request, @PathVariable Long id, @PathVariable Long pageNumber) {

		try {
			UserE targetManager = userService.findById(id).orElseThrow();
			UserE requestManager = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
			if (targetManager.equals(requestManager) || requestManager.getRols().contains("ADMIN")) {
				Integer numHotelsManager = requestManager.getHotels().size();

				// It's multiplied and added by 6 because that's the number of hotels that are
				// loaded each time
				Integer hotelStart = Math.max(0, (pageNumber.intValue() - 1) * 6);
				Integer hotelEnd = hotelStart + 6;

				// if we go out of bounds in the hotel List
				if (pageNumber < 0 || hotelStart > numHotelsManager) {
					return ResponseEntity.badRequest().build();
				} else if (hotelEnd > numHotelsManager) {
					hotelEnd = numHotelsManager;
				}

				List<Hotel> hotels = requestManager.getHotels();
				List<Hotel> hotelsList = hotels.subList(hotelStart, hotelEnd);

				return ResponseEntity.ok(hotelsList);
			} else {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}

		} catch (NoSuchElementException e) {
			return ResponseEntity.notFound().build();
		}
	}

	// PUBLIC CONTROLLERS

	// ADVANCED RECOMMENDATION ALGORITHM

	@JsonView(HotelDetails.class)
	@GetMapping("/hotels/index/recommended")
	public ResponseEntity<List<Hotel>> index(HttpServletRequest request) {
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
				while (recomendedHotels.size() + 1 < 7 && i <= sizeAllHotels) {
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

	@JsonView(HotelDetails.class)
	@GetMapping("/hotels/index/search")
	public ResponseEntity<PageResponse<Hotel>> indexSearch(@RequestParam String searchValue, Pageable pageable) {
		try {
			Page<Hotel> hotels = hotelService.findAllByManager_ValidatedAndNameContainingIgnoreCaseOrderByNameDesc(
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
}
