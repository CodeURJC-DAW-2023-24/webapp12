package es.codeurjc.yourHOmeTEL.controller.restController;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.naming.Binding;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.attribute.UserPrincipal;
import java.sql.SQLException;

import org.springframework.core.io.Resource;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.security.crypto.password.PasswordEncoder;

import es.codeurjc.yourHOmeTEL.model.Hotel;
import es.codeurjc.yourHOmeTEL.model.Room;
import es.codeurjc.yourHOmeTEL.model.Reservation;
import es.codeurjc.yourHOmeTEL.model.Review;
import es.codeurjc.yourHOmeTEL.model.UserE;
import es.codeurjc.yourHOmeTEL.security.jwt.AuthResponse;
import es.codeurjc.yourHOmeTEL.security.jwt.JwtTokenProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import es.codeurjc.yourHOmeTEL.security.jwt.LoginRequest;
import es.codeurjc.yourHOmeTEL.security.jwt.Token;
import es.codeurjc.yourHOmeTEL.security.jwt.UserLoginService;
import es.codeurjc.yourHOmeTEL.service.UserService;
import es.codeurjc.yourHOmeTEL.service.HotelService;
import es.codeurjc.yourHOmeTEL.service.ImageService;
import es.codeurjc.yourHOmeTEL.service.UserSecurityService;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Key;
import java.util.Date;

@RestController
@RequestMapping("/api")
public class UserRest {

    interface UserDetails
            extends UserE.Complete, Hotel.Basic, Review.Basic, Room.Basic, Reservation.Basic {
    }

    @Autowired
    private UserService userService;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private ImageService imgService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserLoginService userLoginService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostConstruct
    public void setup() {
        objectMapper.setDefaultMergeable(true);
    }

    // PUBLIC CONTROLLERS

    // ADVANCED RECOMMENDATION ALGORITHM
    @GetMapping("/index")
    public String index(Model model, HttpServletRequest request) {
        List<Hotel> recomendedHotels = new ArrayList<>();
        try {
            String nick = request.getUserPrincipal().getName();
            UserE user = userService.findByNick(nick).orElseThrow();
            List<Reservation> userReservations = user.getReservations();
            recomendedHotels = userService.findRecomendedHotels(6, userReservations, user);

        } catch (NullPointerException e) {

        } finally {
            if (recomendedHotels.size() < 6) {
                // size +1 to avoid looking for id = 0 if size = 0
                for (int i = recomendedHotels.size() + 1; i < 7; i++) {
                    Hotel hotel = hotelService.findById((long) i).orElseThrow();
                    if (hotel != null && hotel.getManager().getvalidated())
                        recomendedHotels.add(hotel);
                }
            }
        }
        model.addAttribute("hotels", recomendedHotels);
        return "index";
    }

    @GetMapping("/indexsearch")
    public String indexSearch(Model model, @RequestParam String searchValue) {
        List<Hotel> hotels = hotelService.findTop6ByManager_ValidatedAndNameContainingIgnoreCaseOrderByNameDesc(true,
                searchValue);
        model.addAttribute("hotels", hotels);
        return "index";

    }

    @GetMapping("/error")
    public String Error(Model model, HttpServletRequest request) {
        return "/error";

    }

    @GetMapping("/returnmainpage")
    public String returnmainpage(Model model, HttpServletRequest request) {
        return "redirect:/index";

    }

    // CLIENT CONTROLLERS

    // MANAGER CONTROLLERS

    // Loads the first 6 hotels of a manager
    @GetMapping("/viewhotelsmanager")
    public String viewHotelsManager(Model model, HttpServletRequest request) {

        String managernick = request.getUserPrincipal().getName();
        UserE currentManager = userService.findByNick(managernick).orElseThrow();

        List<Hotel> hotels = currentManager.getHotels();

        if (hotels.size() > 6) {
            hotels = hotels.subList(0, 6);
        }

        model.addAttribute("hotels", hotels);

        return "viewhotelsmanager";

    }

    @GetMapping("/testChart")
    public String testChart(Model model) {

        List<Integer> info = new ArrayList<>();
        model.addAttribute("info", info);

        return "testChart";

    }

    /**
     * Loads the data of all hotels owned by a manager to be viewed in a graph
     * 
     * @param model
     * @param request
     * @return The chart template
     */
    @GetMapping("/chartsManager")
    public String chartsManager(Model model, HttpServletRequest request) {

        String managernick = request.getUserPrincipal().getName();
        UserE currentManager = userService.findByNick(managernick).orElseThrow();

        var reviewsAverage = new ArrayList<String>();
        var hotelNames = new ArrayList<String>();

        for (Hotel hotel : currentManager.getHotels()) {
            hotelNames.add(hotel.getName());
            reviewsAverage.add("%.1f".formatted((hotel.getAverageRating())));
        }

        model.addAttribute("reviewsAverage", reviewsAverage);
        model.addAttribute("hotelNames", hotelNames);

        return "chartsManager";

    }

    // a manager applies to be validated by an admin
    @PostMapping("/application/{id}")
    public String managerApplication(Model model, HttpServletRequest request, @PathVariable Long id) {

        UserE currentUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
        UserE foundManager = userService.findById(id).orElseThrow();

        if (currentUser.equals(foundManager)) {
            foundManager.setRejected(false);
            userService.save(foundManager);
            model.addAttribute("user", foundManager);
            return "redirect:/profile";

        } else
            return "/error";
    }

    // ADMIN CONTROLLERS
    @GetMapping("/chartsadmin")
    public String chartsAdmin(Model model, HttpServletRequest request) {

        return "chartsadmin";

    }

    @JsonView(UserDetails.class)
    @GetMapping("/managers/validation")
    public ResponseEntity<List<UserE>> managerValidation() {
        List<UserE> unvalidatedManagersList = new ArrayList<>();

        try {
            unvalidatedManagersList = userService.findByValidatedAndRejected(false, false);
            return ResponseEntity.ok(unvalidatedManagersList);

        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // AQUI EMPIEZAN MIS CONTROLADORES

    @PutMapping("/users/{id}/managers/rejection")
    public ResponseEntity<UserE> rejectManager(HttpServletRequest request, @RequestParam("rejected") Boolean rejected,
            @PathVariable Long id) {
        try {
            UserE manager = userService.findById(id).orElseThrow();
            if (manager.getRols().contains("MANAGER") && rejected == true) {
                manager.setRejected(true);
                manager.setvalidated(false);
                userService.save(manager);
                return ResponseEntity.ok(manager);

            } else if (rejected == false) {
                return ResponseEntity.badRequest().build();
            }

            else {
                return ResponseEntity.notFound().build();
            }

        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // sets the selected manager as accepted
    @PutMapping("/users/{id}/managers/verification")
    public ResponseEntity<UserE> acceptManager(HttpServletRequest request, @RequestParam("accepted") Boolean accepted,
            @PathVariable Long id) {
        try {
            UserE manager = userService.findById(id).orElseThrow();
            if (manager.getRols().contains("MANAGER") && accepted == true) {
                manager.setRejected(false);
                manager.setvalidated(true);
                userService.save(manager);
                return ResponseEntity.ok(manager);

            } else if (accepted == false) {
                return ResponseEntity.badRequest().build();

            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // returns list of all managers
    @JsonView(UserDetails.class)
    @GetMapping("/users/managers/list")
    public ResponseEntity<List<UserE>> managerList(HttpServletRequest request) {

        try {
            List<UserE> managersList = userService.findByCollectionRolsContains("MANAGER");
            return ResponseEntity.ok(managersList);

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/users/{id}/image")
    public ResponseEntity<Object> downloadProfileImage(HttpServletRequest request, @PathVariable long id)
            throws MalformedURLException {
        return imgService.createResponseFromImage(imgService.getFilesFolder(), id);
    }

    @PostMapping("users/{id}/image/creation")
    public ResponseEntity<Object> uploadImage(HttpServletRequest request, @PathVariable long id,
            @RequestParam MultipartFile imageFile) throws IOException {
        try {
            UserE foundUser = userService.findById(id).orElseThrow();
            URI location = fromCurrentRequest().build().toUri();
            foundUser.setImagePath(location.toString());
            userService.save(foundUser);
            imgService.saveImage(imgService.getFilesFolder(), foundUser.getId(), imageFile);
            return ResponseEntity.created(location).build();

        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/users/{id}/image/removal")
    public ResponseEntity<Object> deleteImage(HttpServletRequest request, @PathVariable long id)
            throws IOException {
        try {
            UserE foundUser = userService.findById(id).orElseThrow();
            foundUser.setImagePath(null);
            userService.save(foundUser);
            this.imgService.deleteImage(imgService.getFilesFolder(), id);
            return ResponseEntity.noContent().build();

        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // USERS CRUD CONTROLLERS

    @JsonView(UserDetails.class)
    @GetMapping("/users/{id}/info")
    public ResponseEntity<UserE> profile(HttpServletRequest request, @PathVariable Long id) {

        if (request.getUserPrincipal() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else {

            try {
                UserE foundUser = userService.findById(id).orElseThrow();
                return ResponseEntity.ok(foundUser);
            } catch (NoSuchElementException e) {
                return ResponseEntity.notFound().build();

            }
        }
    }

    // returns 400 if not all needed attributes are included on the body request
    @PostMapping("/users/register")
    public ResponseEntity<UserE> registerClient(HttpServletRequest request, @RequestBody UserE newUser,
            @RequestParam("type") Integer type) throws IOException {

        if (type == null || (type != 0 && type != 1) || newUser.getNick() == null || newUser.getPass() == null
                || newUser.getEmail() == null || newUser.getName() == null || newUser.getLastname() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (userService.existNick(newUser.getNick())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } else {
            newUser.setPass(passwordEncoder.encode(newUser.getPass()));
            List<String> rols = new ArrayList<>();
            rols.add("USER");
            if (type == 0) {
                rols.add("CLIENT");
                newUser.setvalidated(null);
                newUser.setRejected(null);
            } else if (type == 1) {
                rols.add("MANAGER");
                newUser.setvalidated(false);
                newUser.setRejected(false);
            }

            newUser.setRols(rols);
            List<Reservation> reservations = new ArrayList<>();
            newUser.setReservations(reservations);
            List<Hotel> hotels = new ArrayList<>();
            newUser.setHotels(hotels);
            List<Review> reviews = new ArrayList<>();
            newUser.setReviews(reviews);

            newUser.setLanguage("");
            newUser.setLocation("");
            newUser.setBio("");
            newUser.setPhone("");
            newUser.setOrganization("");

            userService.save(newUser);

            Resource image = new ClassPathResource("/static/images/default-hotel.jpg");
            newUser.setImageFile(BlobProxy.generateProxy(image.getInputStream(), image.contentLength()));
            newUser.setImage(true);

            userService.save(newUser);
            return ResponseEntity.ok(newUser);
        }
    }

    // edit profile using raw json body or x-www-form-urlencoded
    @PutMapping("/users/{id}/info/update")
    public ResponseEntity<UserE> editProfile(HttpServletRequest request, @PathVariable Long id,
            @RequestParam Map<String, Object> updates) throws JsonMappingException, JsonProcessingException {

        if (request.getUserPrincipal() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } else {

            try {
                UserE requestUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
                UserE foundUser = userService.findById(id).orElseThrow();

                if (requestUser.getRols().contains("ADMIN") || requestUser.equals(foundUser)) {
                    // merges the current user with the updates on the request body
                    objectMapper.readerForUpdating(foundUser).readValue(objectMapper.writeValueAsString(updates)); // exists
                    userService.save(foundUser);
                    return ResponseEntity.ok(foundUser);
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                }

            } catch (NoSuchElementException e) {
                return ResponseEntity.notFound().build();
            }
        }
    }

    @DeleteMapping("/users/{id}/info/removal")
    public ResponseEntity<UserE> deleteProfile(HttpServletRequest request, @PathVariable Long id) {
        try {
            UserE requestUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
            UserE foundUser = userService.findById(id).orElseThrow();

            if (requestUser.getRols().contains("ADMIN") || requestUser.equals(foundUser)) {
                userService.delete(foundUser);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
