package yourHOmeTEL.controller.restController;

import org.springframework.web.bind.annotation.RestController;

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
import yourHOmeTEL.service.ReviewService;
import yourHOmeTEL.service.UserService;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api")
public class ReviewRest {

	public interface ReviewDetails
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

	// REVIEW CRUD CONTROLLERS

	@JsonView(HotelDetails.class)
	@GetMapping("/reviews")
	@Operation(summary = "Load all reviews", description = "Load all reviews with pagination.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Reviews retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageResponse.class))),
			@ApiResponse(responseCode = "404", description = "User not found")
	})
	public ResponseEntity<PageResponse<Review>> loadAllReviews(
			HttpServletRequest request,
			Pageable pageable) {

		try {
			Page<Review> reviews = reviewService.findAll(pageable);
			PageResponse<Review> response = new PageResponse<>();
			response.setContent(reviews.getContent());
			response.setPageNumber(reviews.getNumber());
			response.setPageSize(reviews.getSize());
			response.setTotalElements(reviews.getTotalElements());
			response.setTotalPages(reviews.getTotalPages());

			return ResponseEntity.ok(response);
		} catch (NoSuchElementException e) {
			return ResponseEntity.notFound().build();

		}
	}

	@JsonView(ReviewDetails.class)
	@GetMapping("/reviews/{id}")
	@Operation(summary = "Get a review", description = "Get a specific review by its ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Review retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Review.class))),
			@ApiResponse(responseCode = "404", description = "Review  not found")
	})
	public ResponseEntity<Review> getReview(@PathVariable Long id) {

		try {
			Review targetReview = reviewService.findById(id).orElseThrow();
			return ResponseEntity.ok(targetReview);

		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}

	}

	@JsonView(ReviewDetails.class)
	@GetMapping("/reviews/users/{id}")
	@Operation(summary = "Get user reviews", description = "Get all reviews of a specific user by user ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Reviews retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageResponse.class))),
			@ApiResponse(responseCode = "404", description = "User not found")
	})
	public ResponseEntity<PageResponse<Review>> userReviews(@PathVariable Long id, Pageable pageable) {

		try {
			Page<Review> targetReviews = reviewService.findByUser_Id(id, pageable);
			if (targetReviews.hasContent()) {
				PageResponse<Review> response = new PageResponse<>();
				response.setContent(targetReviews.getContent());
				response.setPageNumber(targetReviews.getNumber());
				response.setPageSize(targetReviews.getSize());
				response.setTotalElements(targetReviews.getTotalElements());
				response.setTotalPages(targetReviews.getTotalPages());

				return ResponseEntity.ok(response);
			} else {
				return ResponseEntity.notFound().build();
			}

		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}

	}

	@JsonView(ReviewDetails.class)
	@GetMapping("/reviews/hotels/{id}")
	@Operation(summary = "Get hotel reviews", description = "Get all reviews of a specific hotel by hotel ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Reviews retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageResponse.class))),
			@ApiResponse(responseCode = "404", description = "Hotel not found")
	})
	public ResponseEntity<PageResponse<Review>> hotelReviews(HttpServletRequest request, @PathVariable Long id,
			Pageable pageable) {

		try {
			UserE requestUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
			UserE hotelManager = hotelService.findById(id).orElseThrow().getManager();

			if (hotelManager.getvalidated() || requestUser.getRols().contains("ADMIN")) {
				Page<Review> targetReviews = reviewService.findByHotel_Id(id, pageable);
				if (targetReviews.hasContent()) {
					PageResponse<Review> response = new PageResponse<>();
					response.setContent(targetReviews.getContent());
					response.setPageNumber(targetReviews.getNumber());
					response.setPageSize(targetReviews.getSize());
					response.setTotalElements(targetReviews.getTotalElements());
					response.setTotalPages(targetReviews.getTotalPages());

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

	@GetMapping("reviews/size/hotels/{id}")
	@Operation(summary = "Get the number of hotel reviews", description = "Get the number of reviews of a specific hotel by hotel ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Number of reviews retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Integer.class))),
			@ApiResponse(responseCode = "403", description = "Forbidden operation", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "404", description = "Hotel not found")
	})
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

	@GetMapping("/reviews/percentage/hotels/{id}")
	@Operation(summary = "Get the percentage of hotel reviews", description = "Get the percentage of reviews of a specific hotel by hotel ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Percentage of reviews retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))),
			@ApiResponse(responseCode = "403", description = "Forbidden operation", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "404", description = "Hotel not found")
	})
	public ResponseEntity<List<Integer>> hotelReviews(@PathVariable Long id) {
		try {
			Hotel selectedHotel = hotelService.findById(id).orElseThrow();
			UserE hotelManager = selectedHotel.getManager();

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

	@PostMapping("/reviews/hotels/{hotelId}")
	@Operation(summary = "Post a hotel review", description = "Post a review for a specific hotel by hotel ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Review created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Review.class))),
			@ApiResponse(responseCode = "400", description = "Invalid input", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "404", description = "Hotel not found")
	})
	public ResponseEntity<Review> postReview(HttpServletRequest request, @RequestBody Review review,
			@PathVariable Long hotelId) {

		if (review.getScore() == 0 || review.getScore() > 5) {
			return ResponseEntity.badRequest().build();

		} else {
			try {
				UserE hotelManager = hotelService.findById(hotelId).orElseThrow().getManager();

				if (hotelManager.getvalidated()) {
					Hotel targetHotel = hotelService.findById(hotelId).orElseThrow();
					UserE authorUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();

					Review newReview = new Review(review.getScore(), review.getComment(), LocalDate.now(), targetHotel,
							authorUser);
					reviewService.save(newReview);

					targetHotel.getReviews().add(newReview);
					hotelService.save(targetHotel);

					authorUser.getReviews().add(newReview);
					userService.save(authorUser);

					Review savedReview = reviewService.save(newReview);
					String loc = "https://localhost:8443/api/reviews/"+ savedReview.getId();
					URI uriLocation = URI.create(loc);
					return ResponseEntity.created(uriLocation).build();

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
	@Operation(summary = "Edit a review", description = "Edit a review by review ID and user ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Review edited successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Review.class))),
			@ApiResponse(responseCode = "403", description = "Forbidden operation", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "404", description = "User or review not found")
	})
	public ResponseEntity<Review> editReview(HttpServletRequest request, @PathVariable Long reviewId,
			@PathVariable Long userId,
			@RequestParam Map<String, Object> updates) throws JsonMappingException, JsonProcessingException {

		try {
			UserE requestUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
			UserE foundUser = userService.findById(userId).orElseThrow();

			if (requestUser.getRols().contains("ADMIN") || requestUser.equals(foundUser)) {
				Review targetReview = reviewService.findById(reviewId).orElseThrow();
				// merges the current review with the updates on the request body
				targetReview = objectMapper.readerForUpdating(targetReview)
						.readValue(objectMapper.writeValueAsString(updates)); // exists
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
	@Operation(summary = "Delete a review", description = "Delete a review by review ID.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Review deleted successfully", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "403", description = "Forbidden operation", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "404", description = "Review not found")
	})
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
