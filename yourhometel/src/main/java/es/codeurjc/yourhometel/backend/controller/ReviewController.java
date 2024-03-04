package es.codeurjc.yourhometel.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import es.codeurjc.yourhometel.backend.service.ReviewService;

@Controller
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

}
