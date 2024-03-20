package yourHOmeTEL.controller.restController;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.attribute.UserPrincipal;
import java.sql.SQLException;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import yourHOmeTEL.model.Hotel;
import yourHOmeTEL.model.Reservation;
import yourHOmeTEL.model.Review;
import yourHOmeTEL.model.Room;
import yourHOmeTEL.model.UserE;
import yourHOmeTEL.security.jwt.AuthResponse;
import yourHOmeTEL.security.jwt.JwtTokenProvider;
import yourHOmeTEL.security.jwt.LoginRequest;
import yourHOmeTEL.security.jwt.Token;
import yourHOmeTEL.security.jwt.UserLoginService;
import yourHOmeTEL.service.HotelService;
import yourHOmeTEL.service.ImageService;
import yourHOmeTEL.service.ReservationService;
import yourHOmeTEL.service.ReviewService;
import yourHOmeTEL.service.UserSecurityService;
import yourHOmeTEL.service.UserService;

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
    private ReservationService reservationService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    public void setup() {
        objectMapper.setDefaultMergeable(true);
    }
    

    // a manager applies to be validated by an admin
    @JsonView(UserDetails.class)
    @PutMapping("/manager/{id}/application")
    public ResponseEntity<UserE> managerApplication(HttpServletRequest request, @PathVariable Long id) {
        try {
            UserE currentUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
            UserE foundManager = userService.findById(id).orElseThrow();

            if (currentUser.equals(foundManager)) {
                foundManager.setRejected(false);
                userService.save(foundManager);
                return ResponseEntity.ok(foundManager);
            } else
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ADMIN CONTROLLERS
    @JsonView(UserDetails.class)
    @GetMapping("/managers/validation")
    public ResponseEntity<PageResponse<UserE>> managerValidation(@RequestParam("validated") Boolean validated, 
    Pageable pageable) {

        try {
            if (validated == true){
                Page<UserE> requestedManagersList = userService.findByValidatedAndRejected(true, false, pageable);
                if (requestedManagersList.hasContent()) {
                    PageResponse<UserE> response = new PageResponse<>();
                    response.setContent(requestedManagersList.getContent());
                    response.setPageNumber(requestedManagersList.getNumber());
                    response.setPageSize(requestedManagersList.getSize());
                    response.setTotalElements(requestedManagersList.getTotalElements());
                    response.setTotalPages(requestedManagersList.getTotalPages());

                    return ResponseEntity.ok(response);
                }else{
                    return ResponseEntity.notFound().build();
                }
            }else if (validated == false){
                Page<UserE> requestedManagersList = userService.findByValidatedAndRejected(false, false, pageable);
                if (requestedManagersList.hasContent()) {
                    PageResponse<UserE> response = new PageResponse<>();
                    response.setContent(requestedManagersList.getContent());
                    response.setPageNumber(requestedManagersList.getNumber());
                    response.setPageSize(requestedManagersList.getSize());
                    response.setTotalElements(requestedManagersList.getTotalElements());
                    response.setTotalPages(requestedManagersList.getTotalPages());

                    return ResponseEntity.ok(response);
                }else{
                    return ResponseEntity.notFound().build();
                }
            }else{
                return ResponseEntity.badRequest().build();
            }

        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
     
    
    @PutMapping("/users/{id}/managers/rejection")
    @JsonView(UserDetails.class)
    public ResponseEntity<UserE> rejectManager(HttpServletRequest request, @RequestParam("rejected") Boolean rejected,
            @PathVariable Long id) {
        try {
            UserE manager = userService.findById(id).orElseThrow();
            if (rejected == true && manager.getRols().contains("MANAGER")) {
                manager.setRejected(true);
                manager.setvalidated(false);
                userService.save(manager);
                return ResponseEntity.ok(manager);

            } else if (rejected == false) {
                return ResponseEntity.badRequest().build();
            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // sets the selected manager as accepted
    @JsonView(UserDetails.class)
    @PutMapping("/users/{id}/managers/validation")
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

    // returns list of all managers. ADMIN and MANAGER can access
    @JsonView(UserDetails.class)
    @GetMapping("/users/managers/list")
    public ResponseEntity<PageResponse<UserE>> managerList(HttpServletRequest request, Pageable pageable) {
        try {
            Page<UserE> managersList = userService.findByCollectionRolsContains("MANAGER", pageable);
            if (managersList.hasContent()) {
                PageResponse<UserE> response = new PageResponse<>();
                response.setContent(managersList.getContent());
                response.setPageNumber(managersList.getNumber());
                response.setPageSize(managersList.getSize());
                response.setTotalElements(managersList.getTotalElements());
                response.setTotalPages(managersList.getTotalPages());

                return ResponseEntity.ok(response);
            }else{
                return ResponseEntity.notFound().build();
            }

        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }


    // USERS CRUD CONTROLLERS

    @JsonView(UserDetails.class)
    @GetMapping("/users/{id}/info")
    public ResponseEntity<UserE> profile(HttpServletRequest request, @PathVariable Long id) {
        try {
            UserE targetUser = userService.findById(id).orElseThrow();
            return ResponseEntity.ok(targetUser);

        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();

        }
    }

    @JsonView(UserDetails.class)
    @GetMapping("/users/reservations/{id}")
    public ResponseEntity<UserE> reservationUser(HttpServletRequest request, @PathVariable Long id) {
        try {
            UserE requestUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
            Reservation targetResevation = reservationService.findById(id).orElseThrow();
            UserE targetUser = targetResevation.getUser();
            
            if(requestUser.equals(targetUser) || requestUser.getRols().contains("ADMIN")){
                return ResponseEntity.ok(targetUser);
            }else{
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();

        }
    }

    @JsonView(UserDetails.class)
    @GetMapping("/users/reviews/{id}")
    public ResponseEntity<UserE> reviewUser(HttpServletRequest request, @PathVariable Long id) {
        try {
            UserE requestUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
            Review targetReview = reviewService.findById(id).orElseThrow();
            UserE targetUser = targetReview.getUser();
            
            if(requestUser.equals(targetUser) || requestUser.getRols().contains("ADMIN")){
                return ResponseEntity.ok(targetUser);
            }else{
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();

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

            URI location = fromCurrentRequest().build().toUri();
            return ResponseEntity.created(location).build();
        }
    }

    // edit profile using raw json body or x-www-form-urlencoded
    @JsonView(UserDetails.class)
    @PutMapping("/users/{id}/info/update")
    public ResponseEntity<UserE> editProfile(HttpServletRequest request, @PathVariable Long id,
            @RequestParam Map<String, Object> updates) throws JsonMappingException, JsonProcessingException {

        try {
            UserE requestUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
            UserE foundUser = userService.findById(id).orElseThrow();

            if (requestUser.getRols().contains("ADMIN") || requestUser.equals(foundUser)) {
                // merges the current user with the updates on the request body
                foundUser = objectMapper.readerForUpdating(foundUser).readValue(objectMapper.writeValueAsString(updates)); // exists
                userService.save(foundUser);
                return ResponseEntity.ok(foundUser);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
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
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
