package es.codeurjc.yourHOmeTEL.service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
        

        //init users
        UserE client =new UserE("Jack1", "Wells1", "Bio", "loc", "lan", "phone",
        "mail", "org", null, "user", passwordEncoder.encode("pass"), rolesUser, new ArrayList<>(), new ArrayList<>(), null);            
        
        UserE manager = new UserE("Jack2", "Wells2", "Bio", "loc", "lan", "phone",
        "mail", "org", null, "manager",  passwordEncoder.encode("manager"), rolesManager, null, null,  new ArrayList<>());     

        UserE admin = new UserE("Jack3", "Wells3", "Bio", "loc", "lan", "phone",
        "mail", "org", null, "admin",  passwordEncoder.encode("admin"), rolesAdmin, null, null, null); 
        
        //init rooms
        Room room1 = new Room(2, 2, 200F, new ArrayList<>(), null);
        Room room2 = new Room(2, 2, 200F, new ArrayList<>(), null);


        //init hoteles
        Hotel hotel1 = new Hotel("Hotel1", "hotel de plata", "loc1", 2, null, manager, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        hotel1.getRooms().add(room1);
        hotel1.getRooms().add(room2);

        //add hotel to room
        room1.setHotel(hotel1);
        room2.setHotel(hotel1);

        //init review
        Review review1 = new Review(4, "Hola", null, hotel1, client);
        
        //add review
        //hotel1.getReviews().add(review1);
        //client.getReviews().add(review1);

        //reservation
        Reservation reservation1 = new Reservation(null, 2, hotel1, room1, client);

        //client.getReservation().add(reservation1);
        //hotel1.getReservation().add(reservation1);
        //room1.getReservations().add(reservation1);

        //save
        userRepository.save(client);
        userRepository.save(manager);
        userRepository.save(admin);
        hotelRepository.save(hotel1);
        roomRepository.save(room1);
        roomRepository.save(room2);
        reviewRepository.save(review1);
        reservationRepository.save(reservation1);
    }
}

