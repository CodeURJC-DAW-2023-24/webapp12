package es.codeurjc.yourHOmeTEL.controller;

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

import es.codeurjc.yourHOmeTEL.model.Hotel;
import es.codeurjc.yourHOmeTEL.model.Review;
import es.codeurjc.yourHOmeTEL.model.UserE;
import es.codeurjc.yourHOmeTEL.repository.HotelRepository;
import es.codeurjc.yourHOmeTEL.repository.UserRepository;
import es.codeurjc.yourHOmeTEL.service.HotelService;
import es.codeurjc.yourHOmeTEL.service.ReviewService;
import es.codeurjc.yourHOmeTEL.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
	UserRepository userRepository;

    @Autowired
	UserService userService;


	@Autowired
	HotelService hotelService;

	@Autowired
	HotelRepository hotelRepository;

    /**
	 * This method adds to the DB the review posted by the client so it is
	 * displayed in the hotel's reviews page
	 * 
	 * @param model
	 * @param score
	 * @param comment
	 * @param date
	 * @param hotel
	 * @param user
	 * @return
	 */
	@PostMapping("/posthotelReviews/{id}")
	public String postReview(
			Model model, HttpServletRequest request,
			@RequestParam(required = false) Integer rating,
			@RequestParam String comment,
			@PathVariable Long id) {

		UserE hotelManager = hotelRepository.findById(id).orElseThrow().getManager();

		if (hotelManager.getvalidated()) {
			int score = (rating != null) ? rating : 0;
			if(score != 0){
				UserE user = userRepository.findByNick(request.getUserPrincipal().getName()).orElseThrow();
				Hotel targetHotel = hotelRepository.findById(id).orElseThrow();
				targetHotel.getReviews().add(new Review(score, comment, LocalDate.now(), targetHotel, user));
				hotelRepository.save(targetHotel);

				return "redirect:/hotelReviews/" + id;
			}
			else
				return "redirect:/hotelReviews/"+id;

		} else
			return "/error";
	}

	@GetMapping("/hotelReviews/{id}")
	public String hotelReviews(
			Model model,
			@PathVariable Long id) {

		UserE hotelManager = hotelRepository.findById(id).orElseThrow().getManager();

		if (hotelManager.getvalidated()) {
			Hotel selectedHotel = hotelRepository.findById(id).orElseThrow();
			model.addAttribute("hotel", selectedHotel);

			List<Review> reviews = new ArrayList<>();
			for (int i = 0; i < 6 && i < selectedHotel.getReviews().size(); i++) {
				reviews.add(selectedHotel.getReviews().get(i));
			}

			model.addAttribute("hotelreviews", reviews);
			model.addAttribute("totalreviews", selectedHotel.getReviews().size());

			for (int i = 1; i <= 5; i++) {
				reviews = reviewService.findByScoreAndHotel(i, selectedHotel);
				int numReviews = reviews.size();

				model.addAttribute("numreviews" + i, numReviews);
			}

			for (int i = 5; i >= 1; i--) {
				int percentageOfIScoreReview = selectedHotel.getPercentageOfNScore(i);

				model.addAttribute("percentageReview" + i, percentageOfIScoreReview);
			}
			return "hotelReviews";

		} else {
			return "/error";
		}

	}

    @GetMapping("/loadMoreReviews/{id}/{start}/{end}")
	public String loadMoreReviews(Model model,
			@PathVariable int id,
			@PathVariable int start,
			@PathVariable int end) {

		List<Review> reviews = hotelRepository.findById((long) id).get().getReviews();
		int reviewsQuantity = reviews.size();

		List<Review> newReviews = new ArrayList<>();

		if (start <= reviewsQuantity) {

			for (int i = start; i < end && i <= reviewsQuantity; i++) {
				newReviews.add(reviews.get(i - 1));
			}

			model.addAttribute("hotelreviews", newReviews);
		}

		return "hotelReviewTemplate";
	}

}
