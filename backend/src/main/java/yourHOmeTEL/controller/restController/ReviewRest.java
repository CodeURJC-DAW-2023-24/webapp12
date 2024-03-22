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
import yourHOmeTEL.service.ReviewService;
import yourHOmeTEL.service.UserService;

import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api")
public class ReviewRest {

    interface ReviewDetails
            extends UserE.Basic, Hotel.Basic, Review.Complete, Room.Basic, Reservation.Basic {
    }

    @Autowired
    private ReviewService reviewService;

    @Autowired
	UserService userService;

	@Autowired
	HotelService hotelService;

	@Autowired
    private ObjectMapper objectMapper;

	@PostConstruct
    public void setup() {
        objectMapper.setDefaultMergeable(true);
    }

	//REVIEW CRUD CONTROLLERS

	@JsonView(ReviewDetails.class)
	@GetMapping("/reviews/{id}")
	public ResponseEntity <Review> getReview(@PathVariable Long id) {

		try{
			Review targetReview = reviewService.findById(id).orElseThrow();
			return ResponseEntity.ok(targetReview);
		
		}catch(Exception e){
			return ResponseEntity.notFound().build();
		}

	}

	
	@JsonView(ReviewDetails.class)
	@GetMapping("/reviews/users/{id}")
	public ResponseEntity<PageResponse<Review>> userReviews(@PathVariable Long id, Pageable pageable) {

		try{
			Page<Review> targetReviews = reviewService.findByUser_Id(id, pageable);
			if (targetReviews.hasContent()) {
					PageResponse<Review> response = new PageResponse<>();
					response.setContent(targetReviews.getContent());
					response.setPageNumber(targetReviews.getNumber());
					response.setPageSize(targetReviews.getSize());
					response.setTotalElements(targetReviews.getTotalElements());
					response.setTotalPages(targetReviews.getTotalPages());

					return ResponseEntity.ok(response);
				}else{
					return ResponseEntity.notFound().build();
				}
		
		}catch(Exception e){
			return ResponseEntity.notFound().build();
		}

	}

	@JsonView(ReviewDetails.class)
	@GetMapping("/reviews/hotels/{id}")
	public ResponseEntity<PageResponse<Review>> hotelReviews(HttpServletRequest request, @PathVariable Long id, 
	Pageable pageable) {
		
		try{
			UserE requestUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
			UserE hotelManager = hotelService.findById(id).orElseThrow().getManager();

			if (hotelManager.getvalidated() || requestUser.getRols().contains("ADMIN")) {
				Page<Review> targetReviews = reviewService.findByHotel_Id(hotelManager.getId(), pageable);
				if (targetReviews.hasContent()) {
					PageResponse<Review> response = new PageResponse<>();
					response.setContent(targetReviews.getContent());
					response.setPageNumber(targetReviews.getNumber());
					response.setPageSize(targetReviews.getSize());
					response.setTotalElements(targetReviews.getTotalElements());
					response.setTotalPages(targetReviews.getTotalPages());

					return ResponseEntity.ok(response);
				}else{
					return ResponseEntity.notFound().build();
				}
			} else {
				return ResponseEntity.notFound().build();
			}
		
		}catch(Exception e){
			return ResponseEntity.notFound().build();
		}

	}

	@GetMapping("/hotels/{id}/reviews/size")
	public ResponseEntity<Integer> hotelReviews(HttpServletRequest request, @PathVariable Long id) {
		try {
			Hotel selectedHotel = hotelService.findById(id).orElseThrow();
			UserE hotelManager = selectedHotel.getManager();
			UserE currentUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
			if (hotelManager.getvalidated() || currentUser.getRols().contains("ADMIN")) {
				int size = selectedHotel.getReviews().size();
				return ResponseEntity.ok(size);
			} else {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}

	} 

	@GetMapping("/hotels/{id}/reviews/percentage")
	public ResponseEntity<List<Integer>> hotelReviews(@PathVariable Long id) {
		try {
			Hotel selectedHotel = hotelService.findById(id).orElseThrow();
			UserE hotelManager = hotelService.findById(id).orElseThrow().getManager();

			if (hotelManager.getvalidated()) {
				List<Integer> percentageReviews = new ArrayList<>();
				for (int i = 5; i >= 1; i--) {
					int percentageOfIScoreReview = selectedHotel.getPercentageOfNScore(i);
					percentageReviews.add(percentageOfIScoreReview);
				}
				return ResponseEntity.ok(percentageReviews);

			} else {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}

	}

	@PostMapping("/reviews/users/hotels/{hotelId}")
	public ResponseEntity<Review> postReview(HttpServletRequest request, @RequestBody Review review,
	 @PathVariable Long userId, @PathVariable Long hotelId) {
		
		if(review.getScore() == 0 || review.getScore() > 5) {
			return ResponseEntity.badRequest().build();

		} else{
			try {
				UserE hotelManager = hotelService.findById(hotelId).orElseThrow().getManager();

				if (hotelManager.getvalidated()) {
					Hotel targetHotel = hotelService.findById(hotelId).orElseThrow();
					UserE authorUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();

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
    @JsonView(ReviewDetails.class)
	@PutMapping("/reviews/{reviewId}/users/{userId}")
    public ResponseEntity<Review> editReview(HttpServletRequest request, @PathVariable Long reviewId, @PathVariable Long userId,
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

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<Review> deleteReview(HttpServletRequest request, @PathVariable Long reviewId) {

        try {
			Review targetReview = reviewService.findById(reviewId).orElseThrow();
            UserE currentUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
			UserE reviewUser = targetReview.getUser();

            if (currentUser.getRols().contains("ADMIN") || currentUser.equals(reviewUser)) {
                
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
