package es.codeurjc.yourHOmeTEL.service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cglib.core.Local;
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
        UserE client1 =new UserE("Jack1", "Wells1", "Bio", "loc", "lan", "phone",
        "mail", "org", null, "user", passwordEncoder.encode("pass"), null, null, rolesUser, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        
        UserE client2 =new UserE("Jack4", "Wells4", "Bio", "loc", "lan", "phone",
        "mail", "org", null, "user2", passwordEncoder.encode("pass2"), null, null, rolesUser, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());            
        
        UserE manager1 = new UserE("Jack2", "Wells2", "Bio", "loc", "lan", "phone",
        "mail", "org", null, "manager",  passwordEncoder.encode("manager"), true, false, rolesManager, new ArrayList<>(), new ArrayList<>(),  new ArrayList<>()); 
        
        UserE manager2 = new UserE("Jack2", "Wells2", "Bio", "loc", "lan", "phone",
        "mail", "org", null, "manager2",  passwordEncoder.encode("manager2"), false, false, rolesManager, new ArrayList<>(), new ArrayList<>(),  new ArrayList<>());         

        UserE admin = new UserE("Jack3", "Wells3", "Bio", "loc", "lan", "phone",
        "mail", "org", null, "admin",  passwordEncoder.encode("admin"), null, null, rolesAdmin, new ArrayList<>(), new ArrayList<>(), new ArrayList<>()); 
        
        userRepository.save(manager1);
        userRepository.save(manager2);
        userRepository.save(client1);
        userRepository.save(client2);
        userRepository.save(admin); 

        //init rooms
        Room room1 = new Room(2, 200F, new ArrayList<>(), null);
        Room room2 = new Room(2, 200F, new ArrayList<>(), null);

        roomRepository.save(room1);
        roomRepository.save(room2);


        //init hoteles
        Hotel hotel1 = new Hotel("Hotel1", "hotel de plata", "loc1",0F, null, manager1, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        Hotel hotel2 = new Hotel("Hotel2", "hotel ", "loc2",0F, null, manager2, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        
        hotelRepository.save(hotel1); 
        hotelRepository.save(hotel2); 

        //add hotel to rooms
        room1.setHotel(hotel1);
        room2.setHotel(hotel2);//mirar el tema de las habitaciones del hotel ya que se supone que el hotel no tiene habitaciones
         
        //save rooms
        roomRepository.save(room1);
        roomRepository.save(room2);
        
        //add hotel to manager
        manager1.getHotels().add(hotel1);
        manager2.getHotels().add(hotel2);

        userRepository.save(manager1);
        userRepository.save(manager2);
        

       
        
        //date creation
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minus(Period.ofDays(1));
        LocalDate tomorrow = today.plus(Period.ofDays(1));
        LocalDate dayAfterTomorrow = today.plus(Period.ofDays(2));
        
        //init review
        Review review1 = new Review(4, "Hola", null, hotel1, client1);

        reviewRepository.save(review1);

        //add review to hotel
        hotel1.getReviews().add(review1);

        hotelRepository.save(hotel1);

        //add review to client
        client1.getReviews().add(review1);

        userRepository.save(client1);

        //init reservation
        

        Reservation reservation1 = new Reservation(LocalDate.of(2024, 2, 27), LocalDate.of(2024, 3, 1), 2, hotel1, room1, client1);
        Reservation reservation2 = new Reservation(LocalDate.of(2024, 3, 4), LocalDate.of(2024, 5, 6), 2, hotel2, room2, client1);
        Reservation reservation3 = new Reservation(LocalDate.of(2024, 6, 4), LocalDate.of(2024, 7, 6), 2, hotel2, room2, client2);

        reservationRepository.save(reservation1);
        reservationRepository.save(reservation2);
        reservationRepository.save(reservation3);

        //add reservation to hotel
        hotel1.getReservations().add(reservation1);
        hotel2.getReservations().add(reservation2);
        hotel2.getReservations().add(reservation3);

        hotelRepository.save(hotel1);
        hotelRepository.save(hotel2);
        
        //add reservation to room
        room1.getReservations().add(reservation1);
        room2.getReservations().add(reservation2);
        room2.getReservations().add(reservation3);

        roomRepository.save(room1);

        //add reservation to client
        client1.getReservations().add(reservation1);
        client1.getReservations().add(reservation2);
        client2.getReservations().add(reservation3);

        userRepository.save(client1);
        userRepository.save(client2);

    }
}

