package yourHOmeTEL.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import yourHOmeTEL.model.Hotel;
import yourHOmeTEL.model.Reservation;
import yourHOmeTEL.model.Room;
import yourHOmeTEL.model.UserE;
import yourHOmeTEL.service.HotelService;
import yourHOmeTEL.service.ReservationService;
import yourHOmeTEL.service.RoomService;
import yourHOmeTEL.service.UserService;

@Controller
public class ReservationController {

    @Autowired
    private ReservationService reservationService;


    @Autowired
	private UserService userService;


	@Autowired
	private HotelService hotelService;


	@Autowired
	private RoomService roomService;


    @PostMapping("/addReservation/{id}")
	public String addReservation(Model model, @PathVariable Long id, HttpServletRequest request, @RequestParam String checkIn,
	@RequestParam String checkOut, @RequestParam Integer numPeople) {

		if (checkIn.isEmpty() || checkOut.isEmpty() || numPeople == null)
			return "redirect:/hotelInformation/{id}";
		else{

			LocalDate checkInDate = reservationService.toLocalDate(checkIn);
			LocalDate checkOutDate = reservationService.toLocalDate(checkOut);
			Room room = hotelService.checkRooms(id, checkInDate, checkOutDate, numPeople);
			if (room != null) {
				UserE user = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
				Hotel hotel = hotelService.findById(id).orElseThrow();
				Reservation newRe = new Reservation(checkInDate, checkOutDate, numPeople, hotel, room, user);
				reservationService.save(newRe);
				return "redirect:/clientReservations";
			} else
				return "redirect:/notRooms/{id}";
		}
	}

	@GetMapping("/notRooms/{id}")
	public String notRooms(Model model, @PathVariable Long id) {
		return "notRooms";
	}

	/**
	 * Redirects the users to the page of reservations, where they can check their
	 * reservations
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	@GetMapping("/clientReservations")
	public String clientReservation(Model model, HttpServletRequest request) {
		UserE currentClient = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();

		List<Reservation> bookings = currentClient.getReservations();

		if (bookings.size() < 6) {
			model.addAttribute("reservations", bookings);

		} else {

			List<Reservation> auxBookings = new ArrayList<>();
			for (int i = 0; i < 6; i++) {
				auxBookings.add(bookings.get(i));
			}

			model.addAttribute("reservations", auxBookings);

		}

		model.addAttribute("user", currentClient);

		return "clientReservation";

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
	public String clientReservation(Model model, HttpServletRequest request, @PathVariable Long id) {
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
			return "redirect:/clientReservations";
		} else
			return "/error";
	}

}
