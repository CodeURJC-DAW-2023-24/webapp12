package es.codeurjc.yourhometel.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import es.codeurjc.yourhometel.backend.service.RoomService;

@Controller
public class RoomController {

    @Autowired
    private RoomService roomService;

}
