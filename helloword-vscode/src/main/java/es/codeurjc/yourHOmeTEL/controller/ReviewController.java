package es.codeurjc.yourhometel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import es.codeurjc.yourhometel.service.ReviewService;

@Controller
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

}
