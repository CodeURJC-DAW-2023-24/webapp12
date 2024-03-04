package es.codeurjc.yourhometel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import es.codeurjc.yourhometel.service.ReservationService;

@Controller
public class ReservationController {

    @Autowired
    private ReservationService reservationService;
}
