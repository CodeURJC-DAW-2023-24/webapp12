package es.codeurjc.yourHOmeTEL.controller.restController;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException.Forbidden;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import es.codeurjc.yourHOmeTEL.model.Hotel;
import es.codeurjc.yourHOmeTEL.model.Reservation;
import es.codeurjc.yourHOmeTEL.model.Review;
import es.codeurjc.yourHOmeTEL.model.Room;
import es.codeurjc.yourHOmeTEL.model.UserE;
import es.codeurjc.yourHOmeTEL.service.HotelService;
import es.codeurjc.yourHOmeTEL.service.ReservationService;
import es.codeurjc.yourHOmeTEL.service.RoomService;
import es.codeurjc.yourHOmeTEL.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

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

    @JsonView(ReservationDetails.class)
    @GetMapping("/reservations/{id}")
	public ResponseEntity<Reservation> getReservation(HttpServletRequest request, @PathVariable Long id) {

		try{
            UserE requestUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
            UserE targetUser = reservationService.findById(id).orElseThrow().getUser();

			if (requestUser.getRols().contains("ADMIN") && requestUser.equals(targetUser)) {
				Reservation targetReservation = reservationService.findById(id).orElseThrow();
                return ResponseEntity.ok(targetReservation);
			} else {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}

        }catch(Exception e){
            return ResponseEntity.notFound().build();
        }
	}

    @JsonView(ReservationDetails.class)
    @GetMapping("/reservations/users/{id}")
	public ResponseEntity<List<Reservation>> getUserReservations(@PathVariable Long id, HttpServletRequest request,
    Pageable pageable) {

		try{
			UserE requestUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
            UserE targetUser = reservationService.findById(id).orElseThrow().getUser();

			if (requestUser.getRols().contains("ADMIN") && requestUser.equals(targetUser)) {
                List <Reservation> targetReservations = targetUser.getReservations();
				return ResponseEntity.ok(targetReservations);
			} else {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}
		
		}catch(Exception e){
			return ResponseEntity.notFound().build();
		}

	}

    @JsonView(ReservationDetails.class)
    @GetMapping("/reservations/hotels/{id}")
	public ResponseEntity<List<Reservation>> getHotelReservations(@PathVariable Long id, HttpServletRequest request,
    Pageable pageable) {

		try{
            UserE targetManager = hotelService.findById(id).orElseThrow().getManager();

			if (targetManager.getvalidated()) {
                List<Reservation> targetReservations = hotelService.findById(id).orElseThrow().getReservations(); 
				return ResponseEntity.ok(targetReservations);
			} else {
				return ResponseEntity.notFound().build();
			}
		
		}catch(Exception e){
			return ResponseEntity.notFound().build();
		}

	}

    @JsonView(ReservationDetails.class)
    @GetMapping("/reservations/rooms/{id}")
	public ResponseEntity<List<Reservation>> getRoomReservations(@PathVariable Long id, HttpServletRequest request,
    Pageable pageable) {

		try{
            UserE targetManager = hotelService.findById(id).orElseThrow().getManager();

			if (targetManager.getvalidated()) {
                List<Reservation> targetReservations = roomService.findById(id).orElseThrow().getReservations(); 
				return ResponseEntity.ok(targetReservations);
			} else {
				return ResponseEntity.notFound().build();
			}
		
		}catch(Exception e){
			return ResponseEntity.notFound().build();
		}

	}

    @PostMapping("/addReservation/{id}")
	public String addReservation(Model model, @PathVariable Long id, HttpServletRequest request, String checkIn,
			String checkOut, Integer numPeople) {

		LocalDate checkInDate = reservationService.toLocalDate(checkIn);
		LocalDate checkOutDate = reservationService.toLocalDate(checkOut);
		Room room = hotelService.checkRooms(id, checkInDate, checkOutDate, numPeople);
		if (room != null) {
			UserE user = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
			Hotel hotel = hotelService.findById(id).orElseThrow();
			Reservation newRe = new Reservation(checkInDate, checkOutDate, numPeople, hotel, room, user);
			reservationService.save(newRe);
			return "redirect:/clientreservations";
		} else
			return "redirect:/notRooms/{id}";
	}


	

	/**
	 * Loads up to 6 more reservations
	 */
	@GetMapping("/loadMoreReservations/{start}/{end}")
	public String loadMoreReservations(
			Model model,
			HttpServletRequest request,
			@PathVariable int start,
			@PathVariable int end) {

		UserE currentClient = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();

		List<Reservation> bookings = currentClient.getReservations();
		List<Reservation> auxBookings = new ArrayList<>();

		if (start <= bookings.size()) {

			for (int i = start; i < end && i <= bookings.size(); i++) {
				auxBookings.add(bookings.get(i - 1));
			}

			model.addAttribute("reservations", auxBookings);
		}

		return "reservationTemplate";

	}

	@GetMapping("/reservationInfo/{id}")
	public String clientreservation(Model model, HttpServletRequest request, @PathVariable Long id) {
		UserE currentUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
		UserE foundUser = reservationService.findById(id).orElseThrow().getUser();

		if (currentUser.equals(foundUser)) {
			model.addAttribute("reservation", reservationService.findById(id).orElseThrow());
			return "reservationInfo";
		} else
			return "/error";

	}

	@GetMapping("/cancelReservation/{id}") // this should be a post
	public String deleteReservation(HttpServletRequest request, @PathVariable Long id) {

		UserE currentUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
		UserE foundUser = reservationService.findById(id).orElseThrow().getUser();

		if (currentUser.equals(foundUser)) {
			Reservation reservation = reservationService.findById(id).orElseThrow();
			if (reservation != null) {
				UserE user = reservation.getUser();
				user.getReservations().remove(reservation);
				userService.save(user);

				Hotel hotel = reservation.getHotel();
				hotel.getReservations().remove(reservation);
				hotelService.save(hotel);

				Room room = reservation.getRooms();
				room.getReservations().remove(reservation);
				roomService.save(room);

				reservationService.deleteById(id);
			}
			return "redirect:/clientreservations";
		} else
			return "/error";
	}
    
}