package es.codeurjc.yourHOmeTEL.service;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.serial.SerialBlob;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cglib.core.Local;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Io;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import com.mysql.cj.jdbc.Blob;

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
import org.springframework.core.io.Resource;

@Service
public class initDataBaseService {

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
    private void initDatabase() throws IOException {

        List<String> rolesUser = new ArrayList<>();
        rolesUser.add("USER");
        rolesUser.add("CLIENT");
        List<String> rolesManager = new ArrayList<>();
        rolesManager.add("USER");
        rolesManager.add("MANAGER");
        List<String> rolesAdmin = new ArrayList<>();
        rolesAdmin.add("USER");
        rolesAdmin.add("ADMIN");

        // default entities

        // init users
;
        

        //init users
        UserE client1 =new UserE("Jack1", "Wells1", "Bio", "loc", "lan", "phone",
        "mail", "org", null, "user", passwordEncoder.encode("pass"), null, null, rolesUser, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
       
        UserE client2 =new UserE("Jack4", "Wells4", "Bio", "loc", "lan", "phone",
        "mail", "org", null, "user2", passwordEncoder.encode("pass2"), null, null, rolesUser, new ArrayList<>(), new ArrayList<>(), new ArrayList<>()); 
        
        UserE client3 =new UserE("Jack6", "Wells6", "Bio", "loc", "lan", "phone",
        "mail", "org", null, "user3", passwordEncoder.encode("pass3"), null, null, rolesUser, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());            
        
        UserE manager1 = new UserE("Jack2", "Wells2", "Bio", "loc", "lan", "phone",
        "mail", "org", null, "manager",  passwordEncoder.encode("manager"), true, false, rolesManager, new ArrayList<>(), new ArrayList<>(),  new ArrayList<>()); 
        
        UserE manager2 = new UserE("Jack2", "Wells2", "Bio", "loc", "lan", "phone",
        "mail", "org", null, "manager2",  passwordEncoder.encode("manager2"), true, false, rolesManager, new ArrayList<>(), new ArrayList<>(),  new ArrayList<>());
        
        UserE manager3 = new UserE("Jack5", "Wells5", "Bio", "loc", "lan", "phone",
        "mail", "org", null, "manager3",  passwordEncoder.encode("manager3"), false, false, rolesManager, new ArrayList<>(), new ArrayList<>(),  new ArrayList<>());         

        UserE admin = new UserE("Jack3", "Wells3", "Bio", "loc", "lan", "phone",
        "mail", "org", null, "admin",  passwordEncoder.encode("admin"), null, null, rolesAdmin, new ArrayList<>(), new ArrayList<>(), new ArrayList<>()); 
        
        userRepository.save(manager1);
        userRepository.save(manager2);
        userRepository.save(manager3);
        userRepository.save(client1);
        userRepository.save(client2);
        userRepository.save(client3);
        userRepository.save(admin); 

        // init rooms
        Room room1 = new Room(2, 200F, new ArrayList<>(), null);
        Room room2 = new Room(2, 200F, new ArrayList<>(), null);
        Room room3 = new Room(3, 300F, new ArrayList<>(), null);

        roomRepository.save(room1);
        roomRepository.save(room2);
        roomRepository.save(room3);

        // init hoteles


        //init hoteles
        Hotel hotel1 = new Hotel("Hotel1", "hotel de plata", "loc1",0F, null, manager1, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        Hotel hotel2 = new Hotel("Hotel2", "hotel ", "loc2",0F, null, manager2, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        
        Hotel hotel3 = new Hotel("Hotel3", "hotel ", "loc2",0F, null, manager3, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        Hotel hotel4 = new Hotel("Hotel4", "hotel ", "loc2",0F, null, manager3, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        Hotel hotel5 = new Hotel("Hotel5", "hotel ", "loc2",0F, null, manager3, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());


        Hotel hotel6 = new Hotel("Hotel6", "hotel ", "loc2",0F, null, manager3, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());


        Hotel hotel7 = new Hotel("Hotel7", "hotel ", "loc2",0F, null, manager3, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());


        Hotel hotel8 = new Hotel("Hotel8", "hotel ", "loc2",0F, null, manager3, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());


        Hotel hotel9 = new Hotel("Hotel9", "hotel ", "loc2",0F, null, manager3, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());


        Hotel hotel10 = new Hotel("Hotel3", "hotel ", "loc2",0F, null, manager3, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        Hotel hotel11 = new Hotel("Hotel10", "hotel ", "loc2",0F, null, manager3, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        
        hotelRepository.save(hotel1); 
        hotelRepository.save(hotel2);
        hotelRepository.save(hotel3); 
        hotelRepository.save(hotel4); 
        hotelRepository.save(hotel5); 
        hotelRepository.save(hotel6); 
        hotelRepository.save(hotel7); 
        hotelRepository.save(hotel8); 
        hotelRepository.save(hotel9); 
        hotelRepository.save(hotel10); 
        hotelRepository.save(hotel11); 



        // add hotel to rooms
        room1.setHotel(hotel1);
        room2.setHotel(hotel2);//mirar el tema de las habitaciones del hotel ya que se supone que el hotel no tiene habitaciones
        room3.setHotel(hotel3); 
        
        //save rooms
        roomRepository.save(room1);
        roomRepository.save(room2);
        roomRepository.save(room3);
        
        //add hotel to manager
        manager1.getHotels().add(hotel1);
        manager2.getHotels().add(hotel2);
        manager2.getHotels().add(hotel3);

        userRepository.save(manager1);
        userRepository.save(manager2);
          
        
        //date creation
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minus(Period.ofDays(1));
        LocalDate tomorrow = today.plus(Period.ofDays(1));
        LocalDate dayAfterTomorrow = today.plus(Period.ofDays(2));
        

        //init reservation
        Reservation reservation1 = new Reservation(LocalDate.of(2024, 2, 27), LocalDate.of(2024, 3, 1), 2, hotel1, room1, client1);
        Reservation reservation2 = new Reservation(LocalDate.of(2024, 3, 4), LocalDate.of(2024, 5, 6), 2, hotel2, room2, client1);
        Reservation reservation3 = new Reservation(LocalDate.of(2024, 6, 4), LocalDate.of(2024, 7, 6), 2, hotel2, room2, client2);
        Reservation reservation4 = new Reservation(LocalDate.of(2024, 6, 4), LocalDate.of(2024, 7, 6), 2, hotel3, room3, client3);
        
        reservationRepository.save(reservation1);
        reservationRepository.save(reservation2);
        reservationRepository.save(reservation3);
        reservationRepository.save(reservation4);

        //add reservation to hotel
        hotel1.getReservations().add(reservation1);
        hotel2.getReservations().add(reservation2);
        hotel2.getReservations().add(reservation3);
        hotel3.getReservations().add(reservation4);





        hotelRepository.save(hotel1);
        hotelRepository.save(hotel2);
        hotelRepository.save(hotel3);
        
        //add reservation to room
        room1.getReservations().add(reservation1);
        room2.getReservations().add(reservation2);
        room2.getReservations().add(reservation3);
        room3.getReservations().add(reservation4);

        roomRepository.save(room1);
        roomRepository.save(room2);
        roomRepository.save(room3);

        //add reservation to client
        client1.getReservations().add(reservation1);
        client1.getReservations().add(reservation2);
        client2.getReservations().add(reservation3);
        client3.getReservations().add(reservation4);

        userRepository.save(client1);
        userRepository.save(client2);
        userRepository.save(client3);

        //init review
        Review review1 = new Review(4, "Hola", LocalDate.of(2024, 3, 2), hotel1, client1);
        Review review2 = new Review(4, "Hola", LocalDate.of(2024, 7, 7), hotel2, client2);

        reviewRepository.save(review1);
        reviewRepository.save(review2);

        // add review to hotel
        hotel1.getReviews().add(review1);
        hotel1.getReviews().add(review2);

        hotelRepository.save(hotel1);
        hotelRepository.save(hotel2);

        //add review to client
        client1.getReviews().add(review1);
        client2.getReviews().add(review2);
        
        userRepository.save(client1);
        userRepository.save(client2);

        //images for all type of users that we created
        setImage(client1, "/static/images/userPhoto.jpg");
        setImage(client2, "/static/images/client2.jpg");
        setImage(client3, "/static/images/client3.jpg");
        setImage(manager1, "/static/images/manager.jpg");
        setImage(manager1, "/static/images/manager.jpg");
        setImage(manager2, "/static/images/manager2.jpg");
        setImage(manager3, "/static/images/manager3.jpg");
        setImage(admin, "/static/images/admin.jpg");
        userRepository.save(client1);
        userRepository.save(client2);
        userRepository.save(client3);
        userRepository.save(manager1);
        userRepository.save(manager2);
        userRepository.save(manager3);
        userRepository.save(admin);


        //images for hotels

        setImage(hotel1, "/static/images/hotel.jpg");
        setImage(hotel2, "/static/images/hotel1.jpg");
        setImage(hotel3, "/static/images/hotel2.jpg");
        setImage(hotel4, "/static/images/hotel4.jpg");
        setImage(hotel5, "/static/images/hotel3.jpg");
        setImage(hotel6, "/static/images/hotel4.jpg");
        setImage(hotel7, "/static/images/hotel6.jpg");
        setImage(hotel8, "/static/images/hortel5.jpg");
        setImage(hotel9, "/static/images/hotel6.jpg");
        setImage(hotel10, "/static/images/hotel4.jpg");
        setImage(hotel11, "/static/images/hotel.jpg");
        hotelRepository.save(hotel1);
        hotelRepository.save(hotel2);
        hotelRepository.save(hotel3);
        hotelRepository.save(hotel4);
        hotelRepository.save(hotel5);
        hotelRepository.save(hotel6);
        hotelRepository.save(hotel7);
        hotelRepository.save(hotel8);
        hotelRepository.save(hotel9);
        hotelRepository.save(hotel10);
        hotelRepository.save(hotel11);
 
        

    }

    // para asignarle la iimagen al usuario
    public void setImage(UserE user, String classpathResource) throws IOException {

        Resource image = new ClassPathResource(classpathResource);

        user.setImageFile(BlobProxy.generateProxy(image.getInputStream(), image.contentLength()));
        user.setImage(true);
    } 

    //for hotels images
    public void setImage(Hotel hotel, String classpathResource) throws IOException {

        Resource image = new ClassPathResource(classpathResource);

        hotel.setImageFile(BlobProxy.generateProxy(image.getInputStream(), image.contentLength()));
        hotel.setImage(true);
    }
}
