package yourHOmeTEL.controller.restController;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException.Forbidden;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import yourHOmeTEL.model.Hotel;
import yourHOmeTEL.model.Reservation;
import yourHOmeTEL.model.Review;
import yourHOmeTEL.model.Room;
import yourHOmeTEL.model.UserE;
import yourHOmeTEL.service.HotelService;
import yourHOmeTEL.service.ReservationService;
import yourHOmeTEL.service.RoomService;
import yourHOmeTEL.service.UserService;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;
import com.fasterxml.jackson.databind.ObjectMapper;


@RestController
@RequestMapping("/api")
public class ReservationRest {

	interface ReservationDetails
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

	@JsonView(ReservationDetails.class)
	@GetMapping("/reservations/{id}")
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
	public ResponseEntity<List<Reservation>> getUserReservations(@PathVariable Long id, HttpServletRequest request,
			Pageable pageable) {

		try {
			UserE requestUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
			UserE targetUser = userService.findById(id).orElseThrow();

			if (requestUser.getRols().contains("ADMIN") || requestUser.equals(targetUser)) {
				List<Reservation> targetReservations = targetUser.getReservations();
				return ResponseEntity.ok(targetReservations);
			} else {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}

		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}

	}

	@JsonView(ReservationDetails.class)
	@GetMapping("/reservations/hotels/{id}")
	public ResponseEntity<List<Reservation>> getHotelReservations(@PathVariable Long id, HttpServletRequest request,
			Pageable pageable) {

		try {
			UserE requestUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
			UserE targetManager = hotelService.findById(id).orElseThrow().getManager();

			if (targetManager.getvalidated() || requestUser.getRols().contains("ADMIN")) {
				List<Reservation> targetReservations = hotelService.findById(id).orElseThrow().getReservations();
				return ResponseEntity.ok(targetReservations);
			} else {
				return ResponseEntity.notFound().build();
			}

		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}

	}

	@JsonView(ReservationDetails.class)
	@GetMapping("/reservations/rooms/{id}")
	public ResponseEntity<List<Reservation>> getRoomReservations(@PathVariable Long id, HttpServletRequest request,
			Pageable pageable) {

		try {
			UserE requestUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
			UserE targetManager = hotelService.findById(id).orElseThrow().getManager();

			if (targetManager.getvalidated() || requestUser.getRols().contains("ADMIN")) {
				List<Reservation> targetReservations = roomService.findById(id).orElseThrow().getReservations();
				return ResponseEntity.ok(targetReservations);
			} else {
				return ResponseEntity.notFound().build();
			}

		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}

	}

	@PostMapping("/reservations/users/{userId}/hotels/{hotelId}/rooms/{roomId}/creation")
	public ResponseEntity<Reservation> addReservation(HttpServletRequest request, @RequestBody Reservation reservation,
	 @PathVariable Long userId, @PathVariable Long hotelId, @PathVariable Long roomId) {

		if (reservation.getCheckIn().isAfter(reservation.getCheckOut()) || reservation.getNumPeople() <= 0 ||
		(reservation.getCheckIn() == null || reservation.getCheckOut() == null)) {
			return ResponseEntity.badRequest().build();
		
		}else{
			UserE requestUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
			UserE targetUser = userService.findById(userId).orElseThrow();
			if (requestUser.equals(targetUser)){ //left to check if date is valid
				try {
					UserE hotelManager = hotelService.findById(hotelId).orElseThrow().getManager();
					Room targetRoom = roomService.findById(roomId).orElseThrow();
					Hotel targetHotel = hotelService.findById(hotelId).orElseThrow();

					if (hotelManager.getvalidated() && targetHotel.getRooms().contains(targetRoom)) {
						reservation.setCheckIn(reservation.getCheckIn().plusDays(1));
						reservation.setCheckOut(reservation.getCheckOut().plusDays(1));
						reservation.setHotel(targetHotel);
						reservation.setRooms(targetRoom);
						reservation.setUser(targetUser);
						reservationService.save(reservation);

						targetRoom.getReservations().add(reservation);
						roomService.save(targetRoom);

						targetHotel.getReservations().add(reservation);
						hotelService.save(targetHotel);

						targetUser.getReservations().add(reservation);
						userService.save(targetUser);

						URI location = fromCurrentRequest().build().toUri();
						return ResponseEntity.created(location).build(); 

					} else {
						return ResponseEntity.notFound().build();
					} 

				} catch (NoSuchElementException e) {
					return ResponseEntity.notFound().build();
				}
			}else{
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			} 
		}
	}


	// edit profile using raw json body or x-www-form-urlencoded
    @JsonView(ReservationDetails.class)
	@PutMapping("/reservations/{reservationId}/users/{userId}/hotels/{hotelId}/rooms/{roomId}/update")
    public ResponseEntity<Reservation> editReservation(HttpServletRequest request, @PathVariable Long reservationId, @PathVariable Long userId,
            @RequestParam Map<String, Object> updates) throws JsonMappingException, JsonProcessingException {

        try {
            UserE requestUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
            UserE foundUser = userService.findById(userId).orElseThrow();

            if (requestUser.getRols().contains("ADMIN") || requestUser.equals(foundUser)) {
                Reservation targetReservation = reservationService.findById(reservationId).orElseThrow();
				// merges the current reservation with the updates on the request body
				targetReservation = objectMapper.readerForUpdating(targetReservation).readValue(objectMapper.writeValueAsString(updates)); // exists
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

   @DeleteMapping("/reservations/{reservationId}/users/{userId}/hotels/{hotelId}/rooms/{roomId}/removal")
    public ResponseEntity<Reservation> deleteReservation(HttpServletRequest request, @PathVariable Long reservationId,
	@PathVariable Long userId, @PathVariable Long hotelId, @PathVariable Long roomId) {

        try {
            UserE requestUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
            UserE targetUser = userService.findById(userId).orElseThrow();

            if (requestUser.getRols().contains("ADMIN") || requestUser.equals(targetUser)) {
                Reservation targetReservation = reservationService.findById(reservationId).orElseThrow();
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