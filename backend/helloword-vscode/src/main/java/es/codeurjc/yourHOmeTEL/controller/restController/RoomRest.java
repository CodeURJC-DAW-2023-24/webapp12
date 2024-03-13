package es.codeurjc.yourHOmeTEL.controller.restController;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
//import es.codeurjc.yourHOmeTEL.service.RoomService;

import es.codeurjc.yourHOmeTEL.model.Hotel;
import es.codeurjc.yourHOmeTEL.model.Reservation;
import es.codeurjc.yourHOmeTEL.model.Review;
import es.codeurjc.yourHOmeTEL.model.Room;
import es.codeurjc.yourHOmeTEL.model.UserE;

@Controller
public class RoomRest {

    /*@Autowired
    private RoomService roomService;*/

    interface ReviewDetails
            extends UserE.Basic, Hotel.Basic, Review.Basic, Room.Complete, Reservation.Basic {
    }

    

}
