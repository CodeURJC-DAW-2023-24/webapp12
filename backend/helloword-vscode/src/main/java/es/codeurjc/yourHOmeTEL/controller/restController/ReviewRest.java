package es.codeurjc.yourHOmeTEL.controller.restController;

import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.codeurjc.yourHOmeTEL.model.Hotel;
import es.codeurjc.yourHOmeTEL.model.Review;
import es.codeurjc.yourHOmeTEL.model.UserE;
import es.codeurjc.yourHOmeTEL.service.HotelService;
import es.codeurjc.yourHOmeTEL.service.ReviewService;
import es.codeurjc.yourHOmeTEL.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class ReviewRest {

    @Autowired
    private ReviewService reviewService;

    @Autowired
	UserService userService;


	@Autowired
	HotelService hotelService;
    
}
