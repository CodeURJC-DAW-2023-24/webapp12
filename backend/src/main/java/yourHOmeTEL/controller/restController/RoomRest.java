package yourHOmeTEL.controller.restController;

import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import yourHOmeTEL.service.ReviewService;
import yourHOmeTEL.service.UserService;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api")
public class RoomRest {

    /*@Autowired
    private RoomService roomService;*/

    interface RoomDetails
            extends UserE.Basic, Hotel.Basic, Review.Basic, Room.Complete, Reservation.Basic {
    }

    private ReviewService reviewService;

    @Autowired
	UserService userService;

	@Autowired
	HotelService hotelService;

	@Autowired
	ReservationService reservationService;

	@Autowired
    private ObjectMapper objectMapper;

	@PostConstruct
    public void setup() {
        objectMapper.setDefaultMergeable(true);
    }


    //TIENES QUE CAMBIAR LOS NOMBRES PARA QUE TRABAJEN CON ROOM, Y HACER LAS COMPROBACIONES DE SEGURIDAD NECESARIAS
	//ej: que la room sea de un hotel con manager validado. hago cosas similares en reserva creo.
	//ej2: que la persona que accede a la room de una reserva, sea la misma persona de la reserva o un admin.
    //REVIEW CRUD CONTROLLERS

	@JsonView(RoomDetails.class)
	@GetMapping("/rooms/{id}")
	public ResponseEntity <Review> getRooms(@PathVariable Long id) {

		try{
			Review targetReview = reviewService.findById(id).orElseThrow();
			return ResponseEntity.ok(targetReview);
		
		}catch(Exception e){
			return ResponseEntity.notFound().build();
		}

	}

	
	@JsonView(RoomDetails.class)
	@GetMapping("/rooms/hotels/{id}")
	public ResponseEntity<List<Review>> hotelRooms(@PathVariable Long id, Pageable pageable) {

		try{
			UserE targetuser = userService.findById(id).orElseThrow();
			return ResponseEntity.ok(targetuser.getReviews());
		
		}catch(Exception e){
			return ResponseEntity.notFound().build();
		}

	}

	@JsonView(RoomDetails.class) //ESTE YA ESTA IMPLEMENTADO EN TEORIA. TIENES QUE PROBAR QUE FUNCIONA BIEN, Y QUIZA
	//AÑADIRLO AL SECURITY CONFIG DE USUARIOS + ADMIN (LOS MANAGER NO PORQUE NO RESERVAN)		
	@GetMapping("/rooms/reservations/{id}")
	public ResponseEntity<Room> reservationRoom(HttpServletRequest request, @PathVariable Long id) {
		
		try{
			UserE requestUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
			Reservation targetReservation = reservationService.findById(id).orElseThrow();

			UserE targetUser = targetReservation.getUser();	
			UserE hotelManager = targetReservation.getRooms().getHotel().getManager();

			if ((hotelManager.getvalidated() && requestUser.equals(targetUser)) || requestUser.getRols().contains("ADMIN")) {		
				Room targetRoom = targetReservation.getRooms();
				return ResponseEntity.ok(targetRoom);
			} else {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}
		
		}catch(Exception e){
			return ResponseEntity.notFound().build();
		}

	}

	@PostMapping("/rooms/hotels/{hotelId}/create")
	public ResponseEntity<Review> postRoom(HttpServletRequest request, @RequestBody Review review,
	 @PathVariable Long userId, @PathVariable Long hotelId) {
		
		if(review.getScore() == 0 || review.getScore() > 5) {
			return ResponseEntity.badRequest().build();

		} else{
			try {
				UserE hotelManager = hotelService.findById(hotelId).orElseThrow().getManager();

				if (hotelManager.getvalidated()) {
					Hotel targetHotel = hotelService.findById(hotelId).orElseThrow();
					UserE authorUser = userService.findById(userId).orElseThrow();

					Review newReview = new Review(review.getScore(), review.getComment(), LocalDate.now(), targetHotel, authorUser);
					reviewService.save(newReview);

					targetHotel.getReviews().add(newReview);
					hotelService.save(targetHotel);

					authorUser.getReviews().add(newReview);
					userService.save(authorUser);

					 URI location = fromCurrentRequest().build().toUri();
            		return ResponseEntity.created(location).build(); 

				} else {
					return ResponseEntity.notFound().build();
				} 

			} catch (Exception e) {
				return ResponseEntity.notFound().build();
			}
		}
	}

	// edit profile using raw json body or x-www-form-urlencoded
    @JsonView(RoomDetails.class)
	@PutMapping("/rooms/{roomId}/hotels/{hotelId}/update")
    public ResponseEntity<Review> editRoom(HttpServletRequest request, @PathVariable Long reviewId, @PathVariable Long userId,
            @RequestParam Map<String, Object> updates) throws JsonMappingException, JsonProcessingException {

        try {
            UserE requestUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
            UserE foundUser = userService.findById(userId).orElseThrow();

            if (requestUser.getRols().contains("ADMIN") || requestUser.equals(foundUser)) {
                Review targetReview = reviewService.findById(reviewId).orElseThrow();
				// merges the current review with the updates on the request body
                targetReview = objectMapper.readerForUpdating(targetReview).readValue(objectMapper.writeValueAsString(updates)); // exists
                reviewService.save(targetReview);
                return ResponseEntity.ok(targetReview);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/rooms/{roomId}/hotels/{hotelId}/removal")
    public ResponseEntity<Review> deleteRoom(HttpServletRequest request, @PathVariable Long reviewId,
	@PathVariable Long userId) {

        try {
            UserE requestUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
            UserE targetUser = userService.findById(userId).orElseThrow();

            if (requestUser.getRols().contains("ADMIN") || requestUser.equals(targetUser)) {
                Review targetReview = reviewService.findById(reviewId).orElseThrow();
				reviewService.delete(targetReview);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    

}
