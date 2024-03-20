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
import yourHOmeTEL.model.Review;
import yourHOmeTEL.model.UserE;
import yourHOmeTEL.service.HotelService;
import yourHOmeTEL.service.ReviewService;
import yourHOmeTEL.service.UserService;

@Controller
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
	UserService userService;


	@Autowired
	HotelService hotelService;


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
	@PostMapping("/postHotelReviews/{id}")
	public String postReview(
			Model model, HttpServletRequest request,
			@RequestParam(required = false) Integer rating,
			@RequestParam String comment,
			@PathVariable Long id) {

		UserE hotelManager = hotelService.findById(id).orElseThrow().getManager();

		if (hotelManager.getvalidated()) {
			int score = (rating != null) ? rating : 0;
			if(score != 0){
				UserE user = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
				Hotel targetHotel = hotelService.findById(id).orElseThrow();
				targetHotel.getReviews().add(new Review(score, comment, LocalDate.now(), targetHotel, user));
				hotelService.save(targetHotel);

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

		UserE hotelManager = hotelService.findById(id).orElseThrow().getManager();

		if (hotelManager.getvalidated()) {
			Hotel selectedHotel = hotelService.findById(id).orElseThrow();
			model.addAttribute("hotel", selectedHotel);

			List<Review> reviews = new ArrayList<>();
			for (int i = 0; i < 6 && i < selectedHotel.getReviews().size(); i++) {
				reviews.add(selectedHotel.getReviews().get(i));
			}

			model.addAttribute("hotelReviews", reviews);
			model.addAttribute("totalReviews", selectedHotel.getReviews().size());

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

		List<Review> reviews = hotelService.findById((long) id).get().getReviews();
		int reviewsQuantity = reviews.size();

		List<Review> newReviews = new ArrayList<>();

		if (start <= reviewsQuantity) {

			for (int i = start; i < end && i <= reviewsQuantity; i++) {
				newReviews.add(reviews.get(i - 1));
			}

			model.addAttribute("hotelReviews", newReviews);
		}

		return "hotelReviewTemplate";
	}

}
