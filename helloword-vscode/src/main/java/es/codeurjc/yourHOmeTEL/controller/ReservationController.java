package es.codeurjc.yourHOmeTEL.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import es.codeurjc.yourHOmeTEL.service.ReservationService;
import es.codeurjc.yourHOmeTEL.service.UserService;

@Controller
public class ReservationController {
    
    @Autowired
	private ReservationService reservationService;
}
