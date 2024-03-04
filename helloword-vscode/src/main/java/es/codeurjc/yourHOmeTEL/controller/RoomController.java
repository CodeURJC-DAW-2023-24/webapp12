package es.codeurjc.yourhometel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import es.codeurjc.yourhometel.service.RoomService;

@Controller
public class RoomController {

    @Autowired
    private RoomService roomService;

}
