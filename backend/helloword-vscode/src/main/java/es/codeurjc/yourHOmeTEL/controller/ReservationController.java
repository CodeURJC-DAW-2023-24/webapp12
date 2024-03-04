package es.codeurjc.yourHOmeTEL.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import es.codeurjc.yourHOmeTEL.model.Hotel;
import es.codeurjc.yourHOmeTEL.model.Reservation;
import es.codeurjc.yourHOmeTEL.model.Room;
import es.codeurjc.yourHOmeTEL.model.UserE;
import es.codeurjc.yourHOmeTEL.repository.HotelRepository;
import es.codeurjc.yourHOmeTEL.repository.ReservationRepository;
import es.codeurjc.yourHOmeTEL.repository.RoomRepository;
import es.codeurjc.yourHOmeTEL.repository.UserRepository;
import es.codeurjc.yourHOmeTEL.service.HotelService;
import es.codeurjc.yourHOmeTEL.service.ReservationService;
import es.codeurjc.yourHOmeTEL.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @Autowired
	private ReservationRepository reservationRepository;

    @Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private HotelService hotelService;

	@Autowired
	private HotelRepository hotelRepository;

	@Autowired
	private RoomRepository roomRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

    @PostMapping("/addReservation/{id}")
	public String addReservation(Model model, @PathVariable Long id, HttpServletRequest request, String checkIn,
			String checkOut, Integer numPeople) {

		LocalDate checkInDate = reservationService.toLocalDate(checkIn);
		LocalDate checkOutDate = reservationService.toLocalDate(checkOut);
		Room room = hotelService.checkRooms(id, checkInDate, checkOutDate, numPeople);
		if (room != null) {
			UserE user = userRepository.findByNick(request.getUserPrincipal().getName()).orElseThrow();
			Hotel hotel = hotelRepository.findById(id).orElseThrow();
			Reservation newRe = new Reservation(checkInDate, checkOutDate, numPeople, hotel, room, user);
			reservationRepository.save(newRe);
			return "redirect:/clientreservations";
		} else
			return "redirect:/notRooms/{id}";
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
	@GetMapping("/clientreservations")
	public String clientreservation(Model model, HttpServletRequest request) {
		UserE currentClient = userRepository.findByNick(request.getUserPrincipal().getName()).orElseThrow();

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

		UserE currentClient = userRepository.findByNick(request.getUserPrincipal().getName()).orElseThrow();

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
		UserE currentUser = userRepository.findByNick(request.getUserPrincipal().getName()).orElseThrow();
		UserE foundUser = reservationRepository.findById(id).orElseThrow().getUser();

		if (currentUser.equals(foundUser)) {
			model.addAttribute("reservation", reservationRepository.findById(id).orElseThrow());
			return "reservationInfo";
		} else
			return "/error";

	}

	@GetMapping("/cancelReservation/{id}") // this should be a post
	public String deleteReservation(HttpServletRequest request, @PathVariable Long id) {

		UserE currentUser = userRepository.findByNick(request.getUserPrincipal().getName()).orElseThrow();
		UserE foundUser = reservationRepository.findById(id).orElseThrow().getUser();

		if (currentUser.equals(foundUser)) {
			Reservation reservation = reservationRepository.findById(id).orElseThrow();
			if (reservation != null) {
				UserE user = reservation.getUser();
				user.getReservations().remove(reservation);
				userRepository.save(user);

				Hotel hotel = reservation.getHotel();
				hotel.getReservations().remove(reservation);
				hotelRepository.save(hotel);

				Room room = reservation.getRooms();
				room.getReservations().remove(reservation);
				roomRepository.save(room);

				reservationRepository.deleteById(id);
			}
			return "redirect:/clientreservations";
		} else
			return "/error";
	}

}
