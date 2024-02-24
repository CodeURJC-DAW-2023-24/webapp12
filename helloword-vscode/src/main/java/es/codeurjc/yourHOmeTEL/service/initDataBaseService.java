package es.codeurjc.yourHOmeTEL.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import es.codeurjc.yourHOmeTEL.security.SecurityConfiguration;
import es.codeurjc.yourHOmeTEL.model.Hotel;
import es.codeurjc.yourHOmeTEL.model.Reservation;
import es.codeurjc.yourHOmeTEL.model.Review;
import es.codeurjc.yourHOmeTEL.model.Room;
import es.codeurjc.yourHOmeTEL.model.UserE;
import es.codeurjc.yourHOmeTEL.repository.HotelRepository;
import es.codeurjc.yourHOmeTEL.repository.ReservationRepository;
import es.codeurjc.yourHOmeTEL.repository.ReviewRepository;
import es.codeurjc.yourHOmeTEL.repository.RoomRepository;
import es.codeurjc.yourHOmeTEL.repository.UserRepository;
import jakarta.annotation.PostConstruct;


@Service
public class initDataBaseService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
	private PasswordEncoder passwordEncoder;

    @PostConstruct
    private void initDatabase() {

        List<String> rolesUser = new ArrayList<>();
        rolesUser.add("USER");
        rolesUser.add("CLIENT");
        List<String> rolesManager = new ArrayList<>();
        rolesManager.add("USER");
        rolesManager.add("MANAGER");
        List<String> rolesAdmin = new ArrayList<>();
        rolesAdmin.add("USER");
        rolesAdmin.add("ADMIN");

        //default entities
        ArrayList<Reservation> lReservations = new ArrayList<>();
        ArrayList<Review> lReviews = new ArrayList<>();
        ArrayList<Hotel> lHotels = new ArrayList<>();
        
        UserE cliente =new UserE("Jack1", "Wells1", "Bio", "loc", "lan", "phone",
        "mail", "org", null, "user", passwordEncoder.encode("pass"), rolesUser, lReservations, lReviews, lHotels);            
        userRepository.save(cliente);
        
        UserE manager = new UserE("Jack2", "Wells2", "Bio", "loc", "lan", "phone",
        "mail", "org", null, "manager",  passwordEncoder.encode("manager"), rolesManager, lReservations, lReviews, lHotels);     
        userRepository.save(manager);

        UserE admin = new UserE("Jack3", "Wells3", "Bio", "loc", "lan", "phone",
        "mail", "org", null, "admin",  passwordEncoder.encode("admin"), rolesAdmin, lReservations, lReviews, lHotels); 
        userRepository.save(admin);
            
        
        //init hotels       
        //create 1 room and add it to room list
        Room room = new Room();
        roomRepository.save(room);
        /*ArrayList <Room> rooms = new ArrayList<>();
        rooms.add(room);
        
        //create hotel with room list
        Hotel hotel = new Hotel("Hotel1","desc","location",80, null, manager, rooms, lReservations,lReviews);
   
        //add hotel to room
        hotel.getRooms().get(0).setHotel(hotel);

        //save
        /*hotelRepository.save(hotel);
        roomRepository.save(room);*/

    }

}

