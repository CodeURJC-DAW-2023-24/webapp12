package yourHOmeTEL.controller.restController;

import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.net.URI;
import java.util.ArrayList;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import yourHOmeTEL.controller.restController.HotelRest.HotelDetails;
import yourHOmeTEL.model.Hotel;
import yourHOmeTEL.model.Reservation;
import yourHOmeTEL.model.Review;
import yourHOmeTEL.model.Room;
import yourHOmeTEL.model.UserE;
import yourHOmeTEL.service.HotelService;
import yourHOmeTEL.service.PageResponse;
import yourHOmeTEL.service.ReservationService;
import yourHOmeTEL.service.RoomService;
import yourHOmeTEL.service.UserService;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api")
public class ReservationRest {

	public interface ReservationDetails
			extends UserE.Basic, Hotel.Basic, Review.Basic, Room.Basic, Reservation.Complete {
	}

	@Autowired
	private ReservationService reservationService;

	@Autowired
	private UserService userService;

	@Autowired
	private HotelService hotelService;

	@Autowired
	private RoomService roomService;

	@Autowired
	private ObjectMapper objectMapper;

	@PostConstruct
	public void setup() {
		objectMapper.setDefaultMergeable(true);
	}

	@JsonView(HotelDetails.class)
	@GetMapping("/reservations")
	@Operation(summary = "Load all reservations", description = "Returns a page of all reservations.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Reservations retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageResponse.class))),
			@ApiResponse(responseCode = "403", description = "Forbidden, the user does not have permission to view these reservations", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "404", description = "User not found")
	})
	public ResponseEntity<PageResponse<Reservation>> loadAllReservations(
			HttpServletRequest request,
			Pageable pageable) {

		try {
			UserE user = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();

			if (user.getRols().contains("ADMIN")) {

				Page<Reservation> reservations = reservationService.findAll(pageable);

				PageResponse<Reservation> response = new PageResponse<>();
				if (reservations.hasContent()) {
					response.setContent(reservations.getContent());
								   
				} else {
					response.setContent(new ArrayList<Reservation>());   
				}
				response.setPageNumber(reservations.getNumber());
				response.setPageSize(reservations.getSize());
				response.setTotalElements(reservations.getTotalElements());
				response.setTotalPages(reservations.getTotalPages());

				return ResponseEntity.ok(response);

			} else {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}

		} catch (NoSuchElementException e) {
			return ResponseEntity.notFound().build();

		}
	}

	@JsonView(ReservationDetails.class)
	@GetMapping("/reservations/{id}")
	@Operation(summary = "Get a reservation", description = "Returns a specific reservation.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Reservation retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Reservation.class))),
			@ApiResponse(responseCode = "403", description = "Forbidden, the user does not have permission to view this reservation", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "404", description = "Reservation not found")
	})
	public ResponseEntity<Reservation> getReservation(HttpServletRequest request, @PathVariable Long id) {

		try {
			UserE requestUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
			UserE targetUser = reservationService.findById(id).orElseThrow().getUser();

			if (requestUser.getRols().contains("ADMIN") || requestUser.equals(targetUser)) {
				Reservation targetReservation = reservationService.findById(id).orElseThrow();
				targetReservation.setCheckIn(targetReservation.getCheckIn().plusDays(1));
				targetReservation.setCheckOut(targetReservation.getCheckOut().plusDays(1));
				return ResponseEntity.ok(targetReservation);
			} else {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}

		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}

	@JsonView(ReservationDetails.class)
	@GetMapping("/reservations/users/{id}")
	@Operation(summary = "Get user reservations", description = "Returns a page of reservations for a specific user.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Reservations retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageResponse.class))),
			@ApiResponse(responseCode = "403", description = "Forbidden, the user does not have permission to view these reservations", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "404", description = "User not found")
	})
	public ResponseEntity<PageResponse<Reservation>> getUserReservations(@PathVariable Long id,
			HttpServletRequest request,
			Pageable pageable) {

		try {
			UserE requestUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
			UserE targetUser = userService.findById(id).orElseThrow();

			if (requestUser.getRols().contains("ADMIN") || requestUser.equals(targetUser)) {
				Page<Reservation> targetReservations = reservationService.findByUser_Id(id, pageable);
				PageResponse<Reservation> response = new PageResponse<>();
				if (targetReservations.hasContent()) {
					response.setContent(targetReservations.getContent());
				}else{
					response.setContent(new ArrayList<Reservation>());
				}
						
					response.setPageNumber(targetReservations.getNumber());
					response.setPageSize(targetReservations.getSize());
					response.setTotalElements(targetReservations.getTotalElements());
					response.setTotalPages(targetReservations.getTotalPages());

					return ResponseEntity.ok(response);

			} else {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}

		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}

	}

	@JsonView(ReservationDetails.class)
	@GetMapping("/reservations/hotels/{id}")
	@Operation(summary = "Get hotel reservations", description = "Returns a page of reservations for a specific hotel.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Reservations retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageResponse.class))),
			@ApiResponse(responseCode = "404", description = "Hotel not found")
	})
	public ResponseEntity<PageResponse<Reservation>> getHotelReservations(@PathVariable Long id,
			HttpServletRequest request,
			Pageable pageable) {

		try {
			UserE requestUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
			UserE targetManager = hotelService.findById(id).orElseThrow().getManager();

			if (targetManager.getvalidated() || requestUser.getRols().contains("ADMIN")) {
				Page<Reservation> targetReservations = reservationService.findByHotel_Id(id, pageable);
				if (targetReservations.hasContent()) {
					PageResponse<Reservation> response = new PageResponse<>();
					response.setContent(targetReservations.getContent());
					response.setPageNumber(targetReservations.getNumber());
					response.setPageSize(targetReservations.getSize());
					response.setTotalElements(targetReservations.getTotalElements());
					response.setTotalPages(targetReservations.getTotalPages());

					return ResponseEntity.ok(response);
				} else {
					return ResponseEntity.notFound().build();
				}
			} else {
				return ResponseEntity.notFound().build();
			}

		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}

	}

	@JsonView(ReservationDetails.class)
	@GetMapping("/reservations/rooms/{id}")
	@Operation(summary = "Get room reservations", description = "Returns a page of reservations for a specific room.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Reservations retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageResponse.class))),
			@ApiResponse(responseCode = "404", description = "Room not found")
	})
	public ResponseEntity<PageResponse<Reservation>> getRoomReservations(@PathVariable Long id,
			HttpServletRequest request,
			Pageable pageable) {

		try {
			UserE requestUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
			UserE targetManager = hotelService.findById(id).orElseThrow().getManager();

			if (targetManager.getvalidated() || requestUser.getRols().contains("ADMIN")) {
				Page<Reservation> targetReservations = reservationService.findByRoom_Id(id, pageable);
				if (targetReservations.hasContent()) {
					PageResponse<Reservation> response = new PageResponse<>();
					response.setContent(targetReservations.getContent());
					response.setPageNumber(targetReservations.getNumber());
					response.setPageSize(targetReservations.getSize());
					response.setTotalElements(targetReservations.getTotalElements());
					response.setTotalPages(targetReservations.getTotalPages());

					return ResponseEntity.ok(response);
				} else {
					return ResponseEntity.notFound().build();
				}
			} else {
				return ResponseEntity.notFound().build();
			}

		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}

	}
	@JsonView(ReservationDetails.class)
	@PostMapping("/reservations/users/{userId}/hotels/{hotelId}")
	@Operation(summary = "Add a reservation", description = "Add a new reservation for a specific user and hotel.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Reservation created successfully", content = @Content(mediaType = "application/json",schema = @Schema(implementation = Reservation.class))),
			@ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "403", description = "Forbidden operation", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "404", description = "Resource not found"),
			@ApiResponse(responseCode = "409", description = "Conflict with existing data")
	})
	public ResponseEntity<Object> addReservation(HttpServletRequest request, @RequestBody Reservation reservation,
			@PathVariable Long userId, @PathVariable Long hotelId) {
	
		if (reservation.getCheckIn().isAfter(reservation.getCheckOut()) || reservation.getNumPeople() <= 0 ||
				(reservation.getCheckIn() == null || reservation.getCheckOut() == null)) {
			return ResponseEntity.badRequest().build();
		} else {
			Room room = hotelService.checkRooms(hotelId, reservation.getCheckIn(), reservation.getCheckOut(),
					reservation.getNumPeople());
			if (room != null) {
				UserE requestUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
				UserE targetUser = userService.findById(userId).orElseThrow();
	
				if (requestUser.equals(targetUser)) {
					try {
						UserE hotelManager = hotelService.findById(hotelId).orElseThrow().getManager();
						Hotel targetHotel = hotelService.findById(hotelId).orElseThrow();
	
						if (hotelManager.getvalidated() && targetHotel.getRooms().contains(room)) {
							reservation.setCheckIn(reservation.getCheckIn().plusDays(1));
							reservation.setCheckOut(reservation.getCheckOut().plusDays(1));
							reservation.setHotel(targetHotel);
							reservation.setRooms(room);
							reservation.setUser(targetUser);
							reservationService.save(reservation);
	
							room.getReservations().add(reservation);
							roomService.save(room);
	
							targetHotel.getReservations().add(reservation);
							hotelService.save(targetHotel);
	
							targetUser.getReservations().add(reservation);
							userService.save(targetUser);
	
							Reservation savedReservation = reservationService.save(reservation);
							String loc = "https://localhost:8443/api/reservations/"+ savedReservation.getId();
							URI uriLocation = URI.create(loc);
							return ResponseEntity.created(uriLocation).body(savedReservation);
	
						} else {
							return ResponseEntity.notFound().build();
						}
					} catch (NoSuchElementException e) {
						return ResponseEntity.notFound().build();
					}
				} else {
					return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
				}
			} else {
				return ResponseEntity.status(HttpStatus.CONFLICT)
						.body("No rooms available for the specified number of people or date.");
			}
		}
	}

	// edit profile using raw json body or x-www-form-urlencoded
	@JsonView(ReservationDetails.class)
	@PutMapping("/reservations/{reservationId}")
	@Operation(summary = "Edit a reservation", description = "Edit an existing reservation by its ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Reservation edited successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Reservation.class))),
			@ApiResponse(responseCode = "403", description = "Forbidden operation", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "404", description = "Reservation not found")
	})
	public ResponseEntity<Reservation> editReservation(HttpServletRequest request, @PathVariable Long reservationId,
			@RequestParam Map<String, Object> updates) throws JsonMappingException, JsonProcessingException {

		try {
			Reservation targetReservation = reservationService.findById(reservationId).orElseThrow();
			UserE currentUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
			UserE reservationUser = targetReservation.getUser();

			if (currentUser.getRols().contains("ADMIN") || currentUser.equals(reservationUser)) {
				// merges the current reservation with the updates on the request body
				targetReservation = objectMapper.readerForUpdating(targetReservation)
						.readValue(objectMapper.writeValueAsString(updates)); // exists
				targetReservation.setCheckIn(targetReservation.getCheckIn().plusDays(1));
				targetReservation.setCheckOut(targetReservation.getCheckOut().plusDays(1));
				reservationService.save(targetReservation);
				targetReservation.setCheckIn(targetReservation.getCheckIn().minusDays(1));
				targetReservation.setCheckOut(targetReservation.getCheckOut().minusDays(1));
				return ResponseEntity.ok(targetReservation);
			} else {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}

		} catch (NoSuchElementException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/reservations/{reservationId}")
	@Operation(summary = "Delete a reservation", description = "Delete an existing reservation by its ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Reservation deleted successfully", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "403", description = "Forbidden operation", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "404", description = "Reservation not found")
	})
	public ResponseEntity<Reservation> deleteReservation(HttpServletRequest request, @PathVariable Long reservationId) {

		try {
			Reservation targetReservation = reservationService.findById(reservationId).orElseThrow();
			UserE requestUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
			UserE targetUser = targetReservation.getUser();

			if (requestUser.getRols().contains("ADMIN") || requestUser.equals(targetUser)) {
				reservationService.delete(targetReservation);
				return ResponseEntity.noContent().build();
			} else {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}
		} catch (NoSuchElementException e) {
			return ResponseEntity.notFound().build();
		}
	}

}