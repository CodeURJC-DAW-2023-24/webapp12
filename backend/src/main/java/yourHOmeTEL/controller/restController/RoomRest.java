package yourHOmeTEL.controller.restController;

import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.net.URI;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import yourHOmeTEL.service.UserService;
import yourHOmeTEL.service.RoomService;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api")
public class RoomRest {

	public interface RoomDetails
			extends UserE.Basic, Hotel.Basic, Review.Basic, Room.Complete, Reservation.Basic {
	}

	@Autowired
	UserService userService;

	@Autowired
	HotelService hotelService;

	@Autowired
	ReservationService reservationService;

	@Autowired
	RoomService roomService;

	@Autowired
	private ObjectMapper objectMapper;

	@PostConstruct
	public void setup() {
		objectMapper.setDefaultMergeable(true);
	}

	// REVIEW CRUD CONTROLLERS

	@JsonView(HotelDetails.class)
	@GetMapping("/rooms")
	@Operation(summary = "Load all rooms", description = "Load all rooms with pagination.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Rooms retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageResponse.class))),
			@ApiResponse(responseCode = "403", description = "Forbidden operation", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "404", description = "User not found")
	})
	public ResponseEntity<PageResponse<Room>> loadAllRooms(
			HttpServletRequest request,
			Pageable pageable) {

		try {
			UserE user = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();

			if (user.getRols().contains("ADMIN")) {

				Page<Room> rooms = roomService.findAll(pageable);

				PageResponse<Room> response = new PageResponse<>();
				response.setContent(rooms.getContent());
				response.setPageNumber(rooms.getNumber());
				response.setPageSize(rooms.getSize());
				response.setTotalElements(rooms.getTotalElements());
				response.setTotalPages(rooms.getTotalPages());

				return ResponseEntity.ok(response);

			} else {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}

		} catch (NoSuchElementException e) {
			return ResponseEntity.notFound().build();

		}
	}

	@JsonView(RoomDetails.class)
	@GetMapping("/rooms/{id}")
	@Operation(summary = "Get a room", description = "Get a room by room ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Room retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Room.class))),
			@ApiResponse(responseCode = "403", description = "Forbidden operation", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "404", description = "Room not found")
	})
	public ResponseEntity<Room> getRooms(HttpServletRequest request, @PathVariable Long id) {
		try {
			UserE manager = roomService.findById(id).orElseThrow().getHotel().getManager();
			if (manager.getvalidated()) {
				Room targetRoom = roomService.findById(id).orElseThrow();
				return ResponseEntity.ok(targetRoom);
			} else {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}

	@JsonView(RoomDetails.class)
	@GetMapping("/rooms/hotels/{id}")
	@Operation(summary = "Get hotel rooms", description = "Get all rooms of a hotel by hotel ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Rooms retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageResponse.class))),
			@ApiResponse(responseCode = "403", description = "Forbidden operation", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "404", description = "Hotel not found")
	})
	public ResponseEntity<PageResponse<Room>> hotelRooms(HttpServletRequest request, @PathVariable Long id,
			Pageable pageable) {
		try {
			UserE requestUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
			Hotel targetHotel = hotelService.findById(id).orElseThrow();
			if (targetHotel.getManager().getvalidated() || requestUser.getRols().contains("ADMIN")) {
				Page<Room> targetRooms = roomService.findByHotel_Id(id, pageable);
				if (targetRooms.hasContent()) {
					PageResponse<Room> response = new PageResponse<>();
					response.setContent(targetRooms.getContent());
					response.setPageNumber(targetRooms.getNumber());
					response.setPageSize(targetRooms.getSize());
					response.setTotalElements(targetRooms.getTotalElements());
					response.setTotalPages(targetRooms.getTotalPages());

					return ResponseEntity.ok(response);
				} else {
					return ResponseEntity.notFound().build();
				}
			} else {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}

		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}

	@JsonView(RoomDetails.class)
	@GetMapping("/rooms/reservations/{id}")
	@Operation(summary = "Get reservation room", description = "Get a room by reservation ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Room retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Room.class))),
			@ApiResponse(responseCode = "403", description = "Forbidden operation", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "404", description = "Reservation not found")
	})
	public ResponseEntity<Room> reservationRoom(HttpServletRequest request, @PathVariable Long id, Pageable pageable) {
		try {
			UserE requestUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
			Reservation targetReservation = reservationService.findById(id).orElseThrow();

			UserE targetUser = targetReservation.getUser();
			UserE hotelManager = targetReservation.getRooms().getHotel().getManager();

			if ((hotelManager.getvalidated() && requestUser.equals(targetUser))
					|| requestUser.getRols().contains("ADMIN")) {
				Room targetRoom = targetReservation.getRooms();
				return ResponseEntity.ok(targetRoom);
			} else {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}

		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}

	}

	@PostMapping("/rooms/hotels/{hotelId}")
	@Operation(summary = "Create a room", description = "Create a new room for a specific hotel.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Room created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Room.class))),
			@ApiResponse(responseCode = "403", description = "Forbidden operation", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "404", description = "Hotel not found")
	})
	public ResponseEntity<Room> postRoom(HttpServletRequest request, @RequestBody Room room,
			@PathVariable Long hotelId) {
		try {
			Hotel currentHotel = hotelService.findById(hotelId).orElseThrow();
			UserE hotelManager = currentHotel.getManager();
			UserE currentUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
			if (currentUser.equals(hotelManager) || currentUser.getRols().contains("ADMIN")) {
				if (hotelManager.getvalidated()) {
					Hotel targetHotel = hotelService.findById(hotelId).orElseThrow();

					Room newRoom = new Room(room.getMaxClients(), room.getcost(), room.getReservations(), currentHotel);
					roomService.save(newRoom);

					targetHotel.getRooms().add(newRoom);
					hotelService.save(targetHotel);

					Room savedRoom = roomService.save(newRoom);
					String loc = "https://localhost:8443/api/rooms/"+ savedRoom.getId();
					URI uriLocation = URI.create(loc);
					return ResponseEntity.created(uriLocation).build();
				} else {
					return ResponseEntity.notFound().build();
				}
			} else {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}

	// edit profile using raw json body or x-www-form-urlencoded
	@JsonView(RoomDetails.class)
	@PutMapping("/rooms/{roomId}/hotels/{hotelId}")
	@Operation(summary = "Edit a room", description = "Edit a room by room ID and hotel ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Room edited successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Room.class))),
			@ApiResponse(responseCode = "403", description = "Forbidden operation", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "404", description = "Room or hotel not found")
	})
	public ResponseEntity<Room> editRoom(HttpServletRequest request, @PathVariable Long roomId,
			@PathVariable Long hotelId, @RequestParam Map<String, Object> updates)
			throws JsonMappingException, JsonProcessingException {

		try {
			UserE requestUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
			UserE hotelManager = hotelService.findById(hotelId).orElseThrow().getManager();
			if (requestUser.getRols().contains("ADMIN") || requestUser.equals(hotelManager)) {
				Room targetRoom = roomService.findById(roomId).orElseThrow();
				// merges the current review with the updates on the request body
				targetRoom = objectMapper.readerForUpdating(targetRoom)
						.readValue(objectMapper.writeValueAsString(updates)); // exists
				roomService.save(targetRoom);
				return ResponseEntity.ok(targetRoom);
			} else {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}

		} catch (NoSuchElementException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/rooms/{roomId}")
	@Operation(summary = "Delete a room", description = "Delete a room by room ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Room deleted successfully", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "403", description = "Forbidden operation", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "404", description = "Room not found")
	})
	public ResponseEntity<Room> deleteRoom(HttpServletRequest request, @PathVariable Long roomId) {
		try {
			Room targetRoom = roomService.findById(roomId).orElseThrow();
			UserE requestUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
			UserE hotelManager = targetRoom.getHotel().getManager();

			if (requestUser.getRols().contains("ADMIN") || (requestUser.equals(hotelManager))) {
				roomService.delete(targetRoom);
				return ResponseEntity.noContent().build();
			} else {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}
		} catch (NoSuchElementException e) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
	}
}
