package yourHOmeTEL.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import jakarta.annotation.PostConstruct;
import yourHOmeTEL.model.Hotel;
import yourHOmeTEL.model.Reservation;
import yourHOmeTEL.model.Review;
import yourHOmeTEL.model.Room;
import yourHOmeTEL.model.UserE;
import yourHOmeTEL.repository.HotelRepository;
import yourHOmeTEL.repository.ReservationRepository;
import yourHOmeTEL.repository.ReviewRepository;
import yourHOmeTEL.repository.RoomRepository;
import yourHOmeTEL.repository.UserRepository;

import java.util.Random;

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

                // init users
                UserE client1 = new UserE("Jack1", "Wells1", "Bio", "loc", "lan", "phone",
                                "mail", "org", null, "user", passwordEncoder.encode("pass"), null, null, rolesUser,
                                rolesUser, new ArrayList<>(),
                                new ArrayList<>(), new ArrayList<>());

                UserE client2 = new UserE("Jack4", "Wells4", "Bio", "loc", "lan", "phone",
                                "mail", "org", null, "user2", passwordEncoder.encode("pass2"), null, null, rolesUser,
                                rolesUser, new ArrayList<>(),
                                new ArrayList<>(), new ArrayList<>());

                UserE client3 = new UserE("Jack6", "Wells6", "Bio", "loc", "lan", "phone",
                                "mail", "org", null, "user3", passwordEncoder.encode("pass3"), null, null, rolesUser,
                                rolesUser, new ArrayList<>(),
                                new ArrayList<>(), new ArrayList<>());

                UserE manager1 = new UserE("Jack2", "Wells2", "Bio", "loc", "lan", "phone",
                                "mail", "org", null, "manager", passwordEncoder.encode("manager"), true, false,
                                rolesManager, rolesManager,
                                new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

                UserE manager2 = new UserE("Jack2", "Wells2", "Bio", "loc", "lan", "phone",
                                "mail", "org", null, "manager2", passwordEncoder.encode("manager2"), true, false,
                                rolesManager, rolesManager,
                                new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

                UserE manager3 = new UserE("Jack5", "Wells5", "Bio", "loc", "lan", "phone",
                                "mail", "org", null, "manager3", passwordEncoder.encode("manager3"), false, false,
                                rolesManager, rolesManager,
                                new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

                UserE admin = new UserE("Jack3", "Wells3", "Bio", "loc", "lan", "phone",
                                "mail", "org", null, "admin", passwordEncoder.encode("admin"), null, null, rolesAdmin,
                                rolesAdmin, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

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
                Hotel hotel1 = new Hotel("Hotel1", "hotel de playa", "loc1", 0F, null, manager1, new ArrayList<>(),
                                new ArrayList<>(), new ArrayList<>());

                Hotel hotel2 = new Hotel("Hotel2", "hotel ", "loc2", 0F, null, manager2, new ArrayList<>(),
                                new ArrayList<>(),
                                new ArrayList<>());

                Hotel hotel3 = new Hotel("Hotel3", "hotel ", "loc2", 0F, null, manager2, new ArrayList<>(),
                                new ArrayList<>(),
                                new ArrayList<>());

                Hotel hotel4 = new Hotel("Hotel4", "hotel ", "loc2", 0F, null, manager2, new ArrayList<>(),
                                new ArrayList<>(),
                                new ArrayList<>());

                Hotel hotel5 = new Hotel("Hotel5", "hotel ", "loc2", 0F, null, manager2, new ArrayList<>(),
                                new ArrayList<>(),
                                new ArrayList<>());

                Hotel hotel6 = new Hotel("Hotel6", "hotel ", "loc2", 0F, null, manager2, new ArrayList<>(),
                                new ArrayList<>(),
                                new ArrayList<>());

                Hotel hotel7 = new Hotel("Hotel7", "hotel ", "loc2", 0F, null, manager2, new ArrayList<>(),
                                new ArrayList<>(),
                                new ArrayList<>());

                Hotel hotel8 = new Hotel("Hotel8", "hotel ", "loc2", 0F, null, manager2, new ArrayList<>(),
                                new ArrayList<>(),
                                new ArrayList<>());

                Hotel hotel9 = new Hotel("Hotel9", "hotel ", "loc2", 0F, null, manager2, new ArrayList<>(),
                                new ArrayList<>(),
                                new ArrayList<>());

                Hotel hotel10 = new Hotel("Hotel10", "hotel ", "loc2", 0F, null, manager3, new ArrayList<>(),
                                new ArrayList<>(),
                                new ArrayList<>());

                Hotel hotel11 = new Hotel("Hotel11", "hotel ", "loc2", 0F, null, manager3, new ArrayList<>(),
                                new ArrayList<>(),
                                new ArrayList<>());

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
                room2.setHotel(hotel2);
                room3.setHotel(hotel3);

                // add room to hotel
                hotel1.getRooms().add(room1);
                hotel2.getRooms().add(room2);
                hotel3.getRooms().add(room3);

                // save rooms
                roomRepository.save(room1);
                roomRepository.save(room2);
                roomRepository.save(room3);

                // add hotel to manager
                manager1.getHotels().add(hotel1);
                manager2.getHotels().add(hotel2);
                manager2.getHotels().add(hotel3);

                userRepository.save(manager1);
                userRepository.save(manager2);

                // init reservation
                Reservation reservation1 = new Reservation(LocalDate.of(2024, 2, 27), LocalDate.of(2024, 2, 28), 2,
                                hotel1, room1, client1);
                Reservation reservation2 = new Reservation(LocalDate.of(2024, 3, 4), LocalDate.of(2024, 3, 6), 2,
                                hotel2, room2, client1);
                Reservation reservation3 = new Reservation(LocalDate.of(2024, 6, 4), LocalDate.of(2024, 6, 6), 2,
                                hotel2, room2, client2);
                Reservation reservation4 = new Reservation(LocalDate.of(2012, 6, 4), LocalDate.of(2013, 6, 6), 2,
                                hotel3, room3, client3);
                Reservation reservation5 = new Reservation(LocalDate.of(2012, 6, 5), LocalDate.of(2014, 6, 6), 2,
                                hotel3, room3, client3);
                Reservation reservation6 = new Reservation(LocalDate.of(2012, 6, 6), LocalDate.of(2015, 6, 6), 2,
                                hotel3, room3, client3);
                Reservation reservation7 = new Reservation(LocalDate.of(2012, 6, 7), LocalDate.of(2016, 6, 6), 2,
                                hotel3, room3, client3);
                Reservation reservation8 = new Reservation(LocalDate.of(2012, 6, 8), LocalDate.of(2017, 6, 6), 2,
                                hotel3, room3, client3);
                Reservation reservation9 = new Reservation(LocalDate.of(2012, 6, 9), LocalDate.of(2018, 6, 6), 2,
                                hotel3, room3, client3);

                reservationRepository.save(reservation1);
                reservationRepository.save(reservation2);
                reservationRepository.save(reservation3);

                reservationRepository.save(reservation4);
                reservationRepository.save(reservation5);
                reservationRepository.save(reservation6);
                reservationRepository.save(reservation7);
                reservationRepository.save(reservation8);
                reservationRepository.save(reservation9);

                // add reservation to hotel
                hotel1.getReservations().add(reservation1);
                hotel2.getReservations().add(reservation2);
                hotel2.getReservations().add(reservation3);
                hotel3.getReservations().add(reservation4);

                hotel2.getReservations().add(reservation5);
                hotel2.getReservations().add(reservation6);
                hotel2.getReservations().add(reservation7);
                hotel2.getReservations().add(reservation8);
                hotel2.getReservations().add(reservation9);

                hotelRepository.save(hotel1);
                hotelRepository.save(hotel2);
                hotelRepository.save(hotel3);

                // add reservation to room
                room1.getReservations().add(reservation1);
                room2.getReservations().add(reservation2);
                room2.getReservations().add(reservation3);
                room3.getReservations().add(reservation4);

                roomRepository.save(room1);
                roomRepository.save(room2);
                roomRepository.save(room3);

                // add reservation to client
                client1.getReservations().add(reservation1);
                client1.getReservations().add(reservation2);
                client2.getReservations().add(reservation3);
                client3.getReservations().add(reservation4);

                userRepository.save(client1);
                userRepository.save(client2);
                userRepository.save(client3);

                // init review
                Review review1 = new Review(4, "Hola", LocalDate.of(2024, 3, 2), hotel1, client1);
                Review review2 = new Review(4, "Hola", LocalDate.of(2024, 7, 7), hotel2, client2);

                reviewRepository.save(review1);
                reviewRepository.save(review2);

                // add review to hotel
                hotel1.getReviews().add(review1);
                hotel1.getReviews().add(review2);

                Random randomInt = new Random();

                for (int i = 0; i < 10; i++) {
                        Review review = new Review(randomInt.nextInt(5) + 1, "Hola" + i, LocalDate.of(2024, 7, 7),
                                        hotel3, client1);
                        reviewRepository.save(review);
                        hotel2.getReviews().add(review);
                }

                hotelRepository.save(hotel1);
                hotelRepository.save(hotel2);

                // add review to client
                client1.getReviews().add(review1);
                client2.getReviews().add(review2);

                userRepository.save(client1);
                userRepository.save(client2);

                // images for all type of users that we created

                String imagePath = "/static/images/userPhoto.jpg";
                Resource imageResource = new ClassPathResource(imagePath);
                if (!imageResource.exists()) {
                        imagePath = "/static/images/default-user.jpg";
                }
                client1.generateImage(imagePath);
                client1.setImagePath(imagePath);
                userRepository.save(client1);

                imagePath = "/static/images/client2.jpg";
                imageResource = new ClassPathResource(imagePath);
                if (!imageResource.exists()) {
                        imagePath = "/static/images/default-user.jpg";
                }
                client2.generateImage(imagePath);
                client2.setImagePath(imagePath);
                userRepository.save(client2);

                imagePath = "/static/images/client3.jpg";
                imageResource = new ClassPathResource(imagePath);
                if (!imageResource.exists()) {
                        imagePath = "/static/images/default-user.jpg";
                }
                client3.generateImage(imagePath);
                client3.setImagePath(imagePath);
                userRepository.save(client3);

                imagePath = "/static/images/manager.jpg";
                imageResource = new ClassPathResource(imagePath);
                if (!imageResource.exists()) {
                        imagePath = "/static/images/default-user.jpg";
                }
                manager1.generateImage(imagePath);
                manager1.setImagePath(imagePath);
                userRepository.save(manager1);

                imagePath = "/static/images/manager2.jpg";
                imageResource = new ClassPathResource(imagePath);
                if (!imageResource.exists()) {
                        imagePath = "/static/images/default-user.jpg";
                }
                manager2.generateImage(imagePath);
                manager2.setImagePath(imagePath);
                userRepository.save(manager2);

                imagePath = "/static/images/manager3.jpg";
                imageResource = new ClassPathResource(imagePath);
                if (!imageResource.exists()) {
                        imagePath = "/static/images/default-user.jpg";
                }
                manager3.generateImage(imagePath);
                manager3.setImagePath(imagePath);
                userRepository.save(manager3);

                imagePath = "/static/images/admin.jpg";
                imageResource = new ClassPathResource(imagePath);
                if (!imageResource.exists()) {
                        imagePath = "/static/images/default-user.jpg";
                }
                admin.generateImage(imagePath);
                admin.setImagePath(imagePath);
                userRepository.save(admin);

                // images for all type of hotels that we created

                String[] imagePaths = {
                                "/static/images/hotel.jpg",
                                "/static/images/hotel1.jpg",
                                "/static/images/hotel2.jpg",
                                "/static/images/hotel4.jpg",
                                "/static/images/hotel3.jpg",
                                "/static/images/hotel4.jpg",
                                "/static/images/hotel6.jpg",
                                "/static/images/hotel5.jpg",
                                "/static/images/hotel6.jpg",
                                "/static/images/hotel4.jpg",
                                "/static/images/hotel.jpg"
                };

                Hotel[] hotels = { hotel1, hotel2, hotel3, hotel4, hotel5, hotel6, hotel7, hotel8, hotel9, hotel10,
                                hotel11 };

                for (int i = 0; i < hotels.length; i++) {
                        String imagesPath = imagePaths[i];
                        Resource imagesResource = new ClassPathResource(imagesPath);
                        if (!imagesResource.exists()) {
                                imagesPath = "/static/images/default-hotel.jpg";
                        }
                        hotels[i].generateImage(imagesPath);
                        hotels[i].setImagePath(imagesPath);
                        hotelRepository.save(hotels[i]);
                }
        }
}
