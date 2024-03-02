package es.codeurjc.yourHOmeTEL.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import es.codeurjc.yourHOmeTEL.service.ReviewService;

@Controller
public class ReviewController {

    @Autowired
	private ReviewService reviewService;
    
}
