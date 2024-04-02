package yourHOmeTEL.controller.restController;

import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;


import java.io.IOException;
import java.net.URI;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.media.*;

import org.springframework.security.crypto.password.PasswordEncoder;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import yourHOmeTEL.controller.restController.HotelRest.HotelDetails;
import yourHOmeTEL.model.Hotel;
import yourHOmeTEL.model.Reservation;
import yourHOmeTEL.model.Review;
import yourHOmeTEL.model.Room;
import yourHOmeTEL.model.UserE;
import yourHOmeTEL.service.PageResponse;
import yourHOmeTEL.service.ReservationService;
import yourHOmeTEL.service.ReviewService;
import yourHOmeTEL.service.UserService;

import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api")
public class UserRest {

    public interface UserDetails
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
    

    @Operation (summary = "Validates or rejects a manager") 
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Manager rejected or validated correctly",
            content = {@Content(
                mediaType = "application/json",
                schema = @Schema(implementation=UserE.class)
            )}
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Operation not allowed for the current user",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "404",
            description = "User not found"
        )
    }) 
    @JsonView(UserDetails.class)
    @PutMapping("/managers/{id}/applied")
	public ResponseEntity<UserE> managerApplication(HttpServletRequest request, @PathVariable Long id,
    @RequestParam Boolean state) {
        try{
            UserE currentUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
            UserE foundManager = userService.findById(id).orElseThrow();

            if (currentUser.equals(foundManager) || currentUser.getRols().contains("ADMIN")) {
                if (currentUser.getvalidated() == false){
                    foundManager.setRejected(!state);
                    userService.save(foundManager);
                }      
                return ResponseEntity.ok(foundManager);
            }else{
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
                    
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
	}
    

    // ADMIN CONTROLLERS
    @Operation (summary = "Returns a list of all managers, validated or pending of validation")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "List of managers returned correctly",
            content = {@Content(
                mediaType = "application/json",
                schema = @Schema(implementation=PageResponse.class)
            )}
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Bad request"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Managers not found"
        )
        
    })
    @JsonView(UserDetails.class)
    @GetMapping("/managers/validated")
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
    

    @Operation (summary = "Validates or rejects a manager")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Manager rejected or validated correctly",
            content = {@Content(
                mediaType = "application/json",
                schema = @Schema(implementation=UserE.class)
            )}
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Operation only allowed for admin users",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Manager not found"
        )
    })
    @PutMapping("/managers/{id}/rejected/state")
    @JsonView(UserDetails.class)
    public ResponseEntity<UserE> rejectManager(HttpServletRequest request, @RequestParam("rejected") Boolean rejected,
            @PathVariable Long id) {
        try {
            UserE currentUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
            UserE manager = userService.findById(id).orElseThrow();
            if (manager.getRols().contains("MANAGER") && currentUser.getRols().contains("ADMIN") && rejected){
                manager.setRejected(true);
                manager.setvalidated(false);

            } else if (!rejected) {
                manager.setRejected(false);
                manager.setvalidated(true);

            } else if (!currentUser.getRols().contains("ADMIN")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); 

            } else {
                return ResponseEntity.notFound().build();
            }

            userService.save(manager);
            return ResponseEntity.ok(manager);

        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @Operation (summary = "Returns a list of all managers")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "List of managers returned correctly",
            content = {@Content(
                mediaType = "application/json",
                schema = @Schema(implementation=PageResponse.class)
            )}
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Managers not found"
        )
    })
    @JsonView(UserDetails.class)
    @GetMapping("/managers")
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
    @Operation (summary = "Returns a list of all users")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "List of users returned correctly",
            content = {@Content(
                mediaType = "application/json",
                schema = @Schema(implementation=PageResponse.class)
            )}
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Operation only allowed for admin users",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Users not found"
        )
    })
    @JsonView(HotelDetails.class)
	@GetMapping("/users")
	public ResponseEntity<PageResponse<UserE>> loadAllUsers(
			HttpServletRequest request,
			Pageable pageable) {

		try {
			UserE user = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();

			if (user.getRols().contains("ADMIN")) {

				Page<UserE> users = userService.findAll(pageable);

				PageResponse<UserE> response = new PageResponse<>();
				response.setContent(users.getContent());
				response.setPageNumber(users.getNumber());
				response.setPageSize(users.getSize());
				response.setTotalElements(users.getTotalElements());
				response.setTotalPages(users.getTotalPages());

				return ResponseEntity.ok(response);

			} else {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}

		} catch (NoSuchElementException e) {
			return ResponseEntity.notFound().build();

		}
	}

    @Operation (summary = "Returns a specific user")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "User data returned correctly",
            content = {@Content(
                mediaType = "application/json",
                schema = @Schema(implementation=UserE.class)
            )}
        ),
        @ApiResponse(
            responseCode = "404",
            description = "User not found"
        )
    })
    @JsonView(UserDetails.class)
    @GetMapping("/users/{id}")
    public ResponseEntity<UserE> profile(HttpServletRequest request, @PathVariable Long id) {
        try {
            UserE targetUser = userService.findById(id).orElseThrow();
            return ResponseEntity.ok(targetUser);

        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();

        }
    }


    @Operation (summary = "Returns a specific user from a specific reservation")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "User data returned correctly",
            content = {@Content(
                mediaType = "application/json",
                schema = @Schema(implementation=UserE.class)
            )}
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Operation only allowed for admins or the user that made the reservation",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Reservation not found"
        )
    })
    @JsonView(UserDetails.class)
    @GetMapping("/users/reservations/{id}")
    public ResponseEntity<UserE> reservationUser(HttpServletRequest request, @PathVariable Long id) {
        try {
            UserE requestUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
            Reservation targetReservation = reservationService.findById(id).orElseThrow();
            UserE targetUser = targetReservation.getUser();
            
            if(requestUser.equals(targetUser) || requestUser.getRols().contains("ADMIN")){
                return ResponseEntity.ok(targetUser);
            }else{
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();

        }
    }

    @Operation (summary = "Returns a specific user from a specific review")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "User data returned correctly",
            content = {@Content(
                mediaType = "application/json",
                schema = @Schema(implementation=UserE.class)
            )}
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Operation only allowed for admins or the user that made the review",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Review not found"
        )
    })
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


    @Operation (summary = "Registers a new user")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "User registered correctly",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Bad request, maybe one of the user attributes is missing or the type is not valid"
        ),
        @ApiResponse(
            responseCode = "409",
            description = "User already exists"
        )
    })
    @PostMapping("/users")
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

            Resource image = new ClassPathResource("/static/images/default-hotel.jpg");
            newUser.setImageFile(BlobProxy.generateProxy(image.getInputStream(), image.contentLength()));
            newUser.setImage(true);

            UserE savedUser = userService.save(newUser);
			String loc = "https://localhost:8443/api/users/"+ savedUser.getId();
			URI uriLocation = URI.create(loc);
            return ResponseEntity.created(uriLocation).build();
        }
    }

    
    @Operation (summary = "Edits a user profile")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "User profile edited correctly",
            content = {@Content(
                mediaType = "application/json",
                schema = @Schema(implementation=UserE.class)
            )}
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Exception originated from JSON data processing or mapping"
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Operation not allowed for the current user",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "404",
            description = "User not found"
        )
    })
    @JsonView(UserDetails.class)
    @PutMapping("/users/{id}")
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
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Deletes a user profile")
    @ApiResponses(value = { 
        @ApiResponse(
            responseCode = "204",
            description = "Operation successful", 
            content = @Content
        ), 
        @ApiResponse(
            responseCode = "403",
            description = "Operation only allowed to admins or the user itself", 
            content = @Content(mediaType = "application/json")
        ), 
        @ApiResponse(
            responseCode = "404",
            description = "Profile not found"
        ) 
    })
    @DeleteMapping("/users/{id}")
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
 
    @Operation(summary = "Returns the type of user")
    @ApiResponse(
            responseCode = "200",
            description = "User role returned correctly",
            content = {@Content(
                mediaType = "application/json",
                schema = @Schema(implementation=String.class)
            )})
    @ApiResponse(
        responseCode = "404",
        description = "User not found")

    @GetMapping("/users/{id}/type")
    public ResponseEntity<String> userType(HttpServletRequest request, @PathVariable Long id) {
          
        try{
            UserE user = userService.findById(id).orElseThrow();
            if (user.getRols().contains("ADMIN"))
                return ResponseEntity.ok("ADMIN");
            else if (user.getRols().contains("CLIENT"))
                return ResponseEntity.ok("CLIENT");
            else
                return ResponseEntity.ok("MANAGER");

        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
       
        }
    }

     @Operation(summary = "Returns if the user is an admin or not")
     @ApiResponse(
            responseCode = "200",
            description = "Boolean matching user role returned correctly",
            content = {@Content(
                mediaType = "application/json",
                schema = @Schema(implementation=Boolean.class)
            )}
        )
    @ApiResponse(
        responseCode = "404",
        description = "User not found")
        
     @GetMapping("/users/{id}/type/admin")
     public ResponseEntity<Boolean> isAdmin(HttpServletRequest request, @PathVariable Long id) {
        try{
            UserE user = userService.findById(id).orElseThrow();
            return ResponseEntity.ok(user.getRols().contains("ADMIN"));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }  
     }

     @Operation(summary = "Returns if the user is a manager or not")
     @ApiResponse(
            responseCode = "200",
            description = "Boolean matching user role returned correctly",
            content = {@Content(
                mediaType = "application/json",
                schema = @Schema(implementation=Boolean.class)
            )}
        )
    @ApiResponse(
        responseCode = "404",
        description = "User not found")

     @GetMapping("/users/{id}/type/manager")
     public ResponseEntity<Boolean> isManager(HttpServletRequest request, @PathVariable Long id) {
        try{
            UserE user = userService.findById(id).orElseThrow();
            return ResponseEntity.ok(user.getRols().contains("MANAGER"));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
     }

     @Operation(summary = "Returns if the user is a client or not")
     @ApiResponse(
            responseCode = "200",
            description = "Boolean matching user role returned correctly",
            content = {@Content(
                mediaType = "application/json",
                schema = @Schema(implementation=Boolean.class)
            )}
        )
     @GetMapping("/users/{id}/type/client")
     public ResponseEntity<Boolean> isClient(HttpServletRequest request, @PathVariable Long id) {
        try{
            UserE user = userService.findById(id).orElseThrow();
            return ResponseEntity.ok(user.getRols().contains("CLIENT"));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
     }

     @Operation(summary = "Returns if the user is a user or not")
     @ApiResponse(
        responseCode = "200",
        description = "Boolean matching user role returned correctly",
        content = {@Content(
            mediaType = "application/json",
            schema = @Schema(implementation=Boolean.class)
        )}
    )
    @ApiResponse(
        responseCode = "404",
        description = "User not found")
        
     @GetMapping("/users/{id}/type/user")
     public ResponseEntity<Boolean> isUser(HttpServletRequest request, @PathVariable Long id) {
        try{
            UserE user = userService.findById(id).orElseThrow();
            return ResponseEntity.ok(user.getRols().contains("USER"));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
     }

     @Operation(summary = "Returns the path of the request")
     @ApiResponse(
            responseCode = "200",
            description = "User data returned correctly",
            content = {@Content(
                mediaType = "application/json",
                schema = @Schema(implementation=String.class)
            )}
        )
     @GetMapping("/request/path")
     public ResponseEntity<String> getPath(HttpServletRequest request) {
          return ResponseEntity.ok(request.getServletPath());
     } 

}
