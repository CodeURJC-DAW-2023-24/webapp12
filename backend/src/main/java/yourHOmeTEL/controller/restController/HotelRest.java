package yourHOmeTEL.controller.restController;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

import yourHOmeTEL.model.*;
import yourHOmeTEL.service.*;

@RestController
@RequestMapping("/api")
public class HotelRest {

  public interface HotelDetails
      extends UserE.Basic, Hotel.Complete, Review.Basic, Room.Basic, Reservation.Basic {
  }

  @Autowired
  private ObjectMapper objectMapper;

  @PostConstruct
  public void setup() {
    objectMapper.setDefaultMergeable(true);
  }

  @Autowired
  UserService userService;

  @Autowired
  HotelService hotelService;

  @Autowired
  ReviewService reviewService;

  @Autowired
  RoomService roomService;

  @Autowired
  ReservationService reservationService;

  // CRUD OPERATIONS

  @JsonView(HotelDetails.class)
  @GetMapping("/hotels")
  @Operation(summary = "Load all hotels", description = "Returns a page of all hotels.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Hotels retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageResponse.class))),
      @ApiResponse(responseCode = "403", description = "Forbidden, the user does not have permission to view these hotels", content = @Content(mediaType = "application/json")),
      @ApiResponse(responseCode = "404", description = "User not found")
  })
  public ResponseEntity<PageResponse<Hotel>> loadAllHotels(
      HttpServletRequest request,
      Pageable pageable) {

    try {
      UserE user = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();

      if (user.getRols().contains("ADMIN")) {

        Page<Hotel> hotels = hotelService.findAll(pageable);
        PageResponse<Hotel> response = new PageResponse<>();
        response.setContent(hotels.getContent());
        response.setPageNumber(hotels.getNumber());
        response.setPageSize(hotels.getSize());
        response.setTotalElements(hotels.getTotalElements());
        response.setTotalPages(hotels.getTotalPages());

        return ResponseEntity.ok(response);

      } else {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
      }

    } catch (NoSuchElementException e) {
      return ResponseEntity.notFound().build();

    }
  }

  @JsonView(HotelDetails.class)
  @GetMapping("/hotels/{id}")
  @Operation(summary = "Get hotel data", description = "Fetches the hotel data for the given ID if the current user is the manager of the hotel and the manager is validated.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Found the hotel", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Hotel.class))),
      @ApiResponse(responseCode = "404", description = "Hotel not found"),
      @ApiResponse(responseCode = "403", description = "Forbidden, current user is not the manager of the hotel", content = @Content(mediaType = "application/json"))
  })
  public ResponseEntity<Hotel> getHotelData(
      HttpServletRequest request,
      @PathVariable Long id) {

    try {
      UserE hotelManager = hotelService.findById(id).orElseThrow().getManager();
      UserE currentUser;
      if (request.getUserPrincipal() == null) {
        currentUser = null;
      }
      else{
        currentUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
      }

      if (hotelManager.getvalidated() || currentUser.equals(hotelManager)) {
        Hotel hotel = hotelService.findById(id).orElseThrow();
        return ResponseEntity.ok(hotel);
      } else {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
      }

    } catch (NoSuchElementException e) {
      return ResponseEntity.notFound().build();
    }

  }

  @JsonView(HotelDetails.class)
  @GetMapping("/hotels/manager/{id}")
  @Operation(summary = "View hotels by manager", description = "Returns a page of hotels managed by the specified user.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Hotels found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageResponse.class))),
      @ApiResponse(responseCode = "403", description = "Forbidden, the user does not have permission to view these hotels", content = @Content(mediaType = "application/json")),
      @ApiResponse(responseCode = "404", description = "Manager not found")
  })
  public ResponseEntity<PageResponse<Hotel>> viewHotelsManager(HttpServletRequest request, @PathVariable Long id,
      Pageable pageable) {
    try {
      UserE requestManager = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
      UserE targetManager = userService.findById(id).orElseThrow();

      if (requestManager.equals(targetManager) || requestManager.getRols().contains("ADMIN")) {
        Page<Hotel> hotels = hotelService.findByManager_Id(id, pageable);
        if (hotels.hasContent()) {
          PageResponse<Hotel> response = new PageResponse<>();
          response.setContent(hotels.getContent());
          response.setPageNumber(hotels.getNumber());
          response.setPageSize(hotels.getSize());
          response.setTotalElements(hotels.getTotalElements());
          response.setTotalPages(hotels.getTotalPages());

          return ResponseEntity.ok(response);
        } else {
          return ResponseEntity.notFound().build();
        }
      } else {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
      }
    } catch (NoSuchElementException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @JsonView(HotelDetails.class)
  @GetMapping("/hotels/reservations/{id}")
  @Operation(summary = "Get reservations from a hotel", description = "Returns a list of reservations for a specific hotel.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Reservations retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Reservation.class))),
      @ApiResponse(responseCode = "403", description = "Forbidden, the user does not have permission to view these reservations", content = @Content(mediaType = "application/json")),
      @ApiResponse(responseCode = "404", description = "Reservation not found")
  })
  public ResponseEntity<Hotel> getReservationsFromHotel(
      HttpServletRequest request,
      @PathVariable Long id) {

    try {
      UserE currentUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
      Reservation reservation = reservationService.findById(id).orElseThrow();
      UserE reservationClient = reservation.getUser();
      Hotel targetHotel = reservation.getHotel();
      UserE hotelManager = targetHotel.getManager();

      if ((currentUser.equals(reservationClient) && hotelManager.getvalidated()
          || currentUser.getRols().contains("ADMIN"))) {

        return ResponseEntity.ok(targetHotel);

      } else {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
      }

    } catch (NoSuchElementException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @JsonView(HotelDetails.class)
  @GetMapping("/hotels/reviews/{id}")
  @Operation(summary = "Get reviews from a hotel", description = "Returns a list of reviews for a specific hotel.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Reviews retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Review.class))),
      @ApiResponse(responseCode = "403", description = "Forbidden, the user does not have permission to view these reviews", content = @Content(mediaType = "application/json")),
      @ApiResponse(responseCode = "404", description = "Review not found")
  })
  public ResponseEntity<Hotel> getReviewsFromHotel(
      HttpServletRequest request,
      @PathVariable Long id) {

    try {
      UserE currentUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
      Review requestReview = reviewService.findById(id).orElseThrow();
      Hotel targetHotel = requestReview.getHotel();
      UserE hotelManager = targetHotel.getManager();

      if (hotelManager.getvalidated() || currentUser.getRols().contains("ADMIN")) {

        return ResponseEntity.ok(targetHotel);

      } else {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
      }

    } catch (NoSuchElementException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @JsonView(HotelDetails.class)
  @GetMapping("/hotels/rooms/{id}")
  @Operation(summary = "Get rooms from a hotel", description = "Returns a list of rooms for a specific hotel.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Rooms retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Room.class))),
      @ApiResponse(responseCode = "403", description = "Forbidden, the user does not have permission to view these rooms", content = @Content(mediaType = "application/json")),
      @ApiResponse(responseCode = "404", description = "Room not found")
  })
  public ResponseEntity<Hotel> getHotelFromRoom(
      HttpServletRequest request,
      @PathVariable Long id) {

    try {
      UserE currentUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
      Room requestRoom = roomService.findById(id).orElseThrow();
      Hotel targetHotel = requestRoom.getHotel();
      UserE hotelManager = targetHotel.getManager();

      if (hotelManager.getvalidated() || currentUser.getRols().contains("ADMIN")) {

        return ResponseEntity.ok(targetHotel);

      } else {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
      }

    } catch (NoSuchElementException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @JsonView(HotelDetails.class)
  @GetMapping("/hotels/{id}/clients")
  @Operation(summary = "Get clients list", description = "Fetches the list of clients for the given hotel ID if the current user is the manager of the hotel.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Found the clients list", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserE.class))),
      @ApiResponse(responseCode = "403", description = "Forbidden, current user is not the manager of the hotel", content = @Content(mediaType = "application/json")),
      @ApiResponse(responseCode = "404", description = "Hotel not found")
  })
  public ResponseEntity<PageResponse<UserE>> clientsList(
      HttpServletRequest request,
      @PathVariable Long id,
      Pageable pageable) {
    try {
      UserE currentUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
      UserE foundUser = hotelService.findById(id).orElseThrow().getManager();

      if (currentUser.equals(foundUser) || currentUser.getRols().contains("ADMIN")) {
        Hotel hotel = hotelService.findById(id).orElseThrow();
        List<UserE> allClients = hotelService.getValidClients(hotel);

        PageResponse<UserE> response = new PageResponse<>();

        if (allClients.isEmpty() || pageable.getPageSize() == 0) {
          response.setContent(new ArrayList<UserE>());
        } else {
          int start = pageable.getPageNumber() * pageable.getPageSize();
          int end = Math.min(start + pageable.getPageSize(), allClients.size());
          List<UserE> validClients = allClients.subList(start, end);

          response.setContent(validClients);
        }

        response.setPageNumber(pageable.getPageNumber());
        response.setPageSize(pageable.getPageSize());
        response.setTotalElements(allClients.size());
        int totalPages = (int) Math.ceil((double) allClients.size() / pageable.getPageSize());
        response.setTotalPages(totalPages);

        return ResponseEntity.ok(response);

      } else {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
      }
    } catch (Exception e) {
      return ResponseEntity.notFound().build();
    }
  }

  // Pending -> Rooms and prices as array
  @JsonView(HotelDetails.class)
  @PostMapping("/hotels")
  @Operation(summary = "Create a hotel", description = "Creates a new hotel with the given details.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Hotel created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Hotel.class))),
      @ApiResponse(responseCode = "404", description = "Manager not found"),
  })
  public ResponseEntity<Hotel> createHotel(
      HttpServletRequest request,
      String name, String description, String location,
      Integer room1, Integer cost1,
      Integer room2, Integer cost2,
      Integer room3, Integer cost3,
      Integer room4, Integer cost4) throws NoSuchElementException {

    try {
      Hotel newHotel = new Hotel(name, location, description);
      UserE manager = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
      newHotel.setManager(manager);
      hotelService.save(newHotel);

      manager.getHotels().add(newHotel);
      userService.save(manager);

      Integer[] roomsCapacities = { room1, room2, room3, room4 };
      Integer[] roomCosts = { cost1, cost2, cost3, cost4 };

      List<Room> hotelRooms = newHotel.getRooms();

      for (int i = 0; i < roomsCapacities.length; i++) {
        if (roomsCapacities[i] != null) {
          for (int j = 0; j < roomsCapacities[i]; j++) {
            Room roomForJPeople = new Room(roomsCapacities[i], roomCosts[i], new ArrayList<>(), newHotel);
            hotelRooms.add(roomForJPeople);
            roomService.save(roomForJPeople);
          }
        }
      }

      Hotel savedHotel = hotelService.save(newHotel);
      String loc = "https://localhost:8443/api/hotels/" + savedHotel.getId();
      URI uriLocation = URI.create(loc);
      return ResponseEntity.created(uriLocation).body(savedHotel);

    } catch (NoSuchElementException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @JsonView(HotelDetails.class)
  @PutMapping("/hotels/{id}")
  @Operation(summary = "Edit a hotel", description = "Edits the details of the hotel with the given ID if the current user is the manager of the hotel.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Hotel edited successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Hotel.class))),
      @ApiResponse(responseCode = "403", description = "Forbidden, current user is not the manager of the hotel", content = @Content(mediaType = "application/json")),
      @ApiResponse(responseCode = "404", description = "Hotel not found")
  })
  public ResponseEntity<Hotel> editHotel(
      HttpServletRequest request,
      @PathVariable Long id,
      @RequestParam Map<String, Object> updates) throws JsonMappingException, JsonProcessingException {

    try {
      UserE currentUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
      UserE foundUser = hotelService.findById(id).orElseThrow().getManager();

      Hotel originalHotel = hotelService.findById(id).orElseThrow();

      if (currentUser.getRols().contains("ADMIN") || currentUser.equals(foundUser)) {

				originalHotel = objectMapper.readerForUpdating(originalHotel)
						.readValue(objectMapper.writeValueAsString(updates));
				hotelService.save(originalHotel);

				for (int i = 0; i < 4; i++){
					if(updates.containsKey("room"+(i+1))){
						String roomCapacityStr = (String) updates.get("room"+(i+1));
    					String roomCostStr = (String) updates.get("cost"+(i+1));
    					if (!roomCapacityStr.equals("") || !roomCostStr.equals("")){
      						Integer roomCapacity = Integer.parseInt(roomCapacityStr);
      						Integer roomCost = Integer.parseInt(roomCostStr);
							if(roomCapacity != null){
								List<Room> hotelRooms = originalHotel.getRooms();
								for (int j = 0; j < roomCapacity; j++) {
									Room roomForJPeople = new Room(roomCapacity, roomCost, new ArrayList<>(), originalHotel);
									hotelRooms.add(roomForJPeople);
									roomService.save(roomForJPeople);
								}
							}
						}
					}
				}
				return ResponseEntity.ok(originalHotel);

      } else {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
      }

    } catch (NoSuchElementException e) {
      return ResponseEntity.notFound().build();
    }

  }

  @DeleteMapping("/hotels/{id}")
  @Operation(summary = "Delete a hotel", description = "Deletes the hotel with the given ID if the current user is the manager of the hotel.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Hotel deleted successfully", content = @Content(mediaType = "application/json")),
      @ApiResponse(responseCode = "403", description = "Forbidden, current user is not the manager of the hotel", content = @Content(mediaType = "application/json")),
      @ApiResponse(responseCode = "404", description = "Hotel not found")
  })
  public ResponseEntity<Hotel> deleteHotel(HttpServletRequest request, @PathVariable Long id) {
    try {
      UserE currentUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
      UserE foundUser = hotelService.findById(id).orElseThrow().getManager();

      if (currentUser.equals(foundUser)) {
        Optional<Hotel> hotel = hotelService.findById(id);
        if (hotel.isPresent()) {
          hotelService.deleteById(id);
          return ResponseEntity.noContent().build();
        } else {
          return ResponseEntity.notFound().build();
        }

      } else {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
      }
    } catch (NoSuchElementException e) {
      return ResponseEntity.notFound().build();
    }

  }

  @JsonView(HotelDetails.class)
  @GetMapping("/hotels/{pageNumber}/manager/{id}")
  @Operation(summary = "Load more hotels for a manager", description = "Returns a list of hotels for a specific manager.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Hotels retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Hotel.class))),
      @ApiResponse(responseCode = "400", description = "Bad request, page number is out of bounds"),
      @ApiResponse(responseCode = "403", description = "Forbidden, the user does not have permission to view these hotels", content = @Content(mediaType = "application/json")),
      @ApiResponse(responseCode = "404", description = "Manager not found")
  })
  public ResponseEntity<List<Hotel>> loadMoreHotelsManagerView(
      HttpServletRequest request, @PathVariable Long id, @PathVariable Long pageNumber) {

    try {
      UserE targetManager = userService.findById(id).orElseThrow();
      UserE requestManager = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
      if (targetManager.equals(requestManager) || requestManager.getRols().contains("ADMIN")) {
        Integer numHotelsManager = requestManager.getHotels().size();

        // It's multiplied and added by 6 because that's the number of hotels that are
        // loaded each time
        Integer hotelStart = Math.max(0, (pageNumber.intValue() - 1) * 6);
        Integer hotelEnd = hotelStart + 6;

        // if we go out of bounds in the hotel List
        if (pageNumber < 0 || hotelStart > numHotelsManager) {
          return ResponseEntity.badRequest().build();
        } else if (hotelEnd > numHotelsManager) {
          hotelEnd = numHotelsManager;
        }

        List<Hotel> hotels = requestManager.getHotels();
        List<Hotel> hotelsList = hotels.subList(hotelStart, hotelEnd);

        return ResponseEntity.ok(hotelsList);
      } else {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
      }

    } catch (NoSuchElementException e) {
      return ResponseEntity.notFound().build();
    }
  }

  // PUBLIC CONTROLLERS

  // ADVANCED RECOMMENDATION ALGORITHM

  @Operation(summary = "Get recommended hotels", description = "Returns a list of recommended hotels for the current user.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Recommended hotels retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageResponse.class)))
  })
  @JsonView(HotelDetails.class)
  @GetMapping("/hotels/recommended")
  public ResponseEntity<PageResponse<Hotel>> indexSPA(HttpServletRequest request, Pageable pageable) {
    List<Hotel> recomendedHotels = new ArrayList<>();
    List<Hotel> allValidHotels = new ArrayList<>();
    try {
      UserE currentUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
      if (currentUser != null && currentUser.getRols().contains("CLIENT")) {
        List<Reservation> userReservations = currentUser.getReservations();
        recomendedHotels = hotelService.findRecomendedHotelsSPA(userReservations, currentUser);
      } else {
        recomendedHotels = hotelService.findAllByManager_Validated(true);
      }
    } catch (NullPointerException e) {
    }

    finally {
      allValidHotels = new ArrayList<>(recomendedHotels);
      if (recomendedHotels.size() < hotelService.count()) {
        List<Hotel> remainingHotels = hotelService.findValidHotelsNotInList(recomendedHotels);
        allValidHotels.addAll(remainingHotels);
      }
    }

    int start = pageable.getPageNumber() * pageable.getPageSize();
    int end = Math.min(start + pageable.getPageSize(), allValidHotels.size());
    List<Hotel> paginatedHotels = allValidHotels.subList(start, end);

    PageResponse<Hotel> response = new PageResponse<>();
    response.setContent(paginatedHotels);
    response.setPageNumber(pageable.getPageNumber());
    response.setPageSize(pageable.getPageSize());
    int totalElements = allValidHotels.size();
    response.setTotalElements(totalElements);
    int totalPages = (int) Math.ceil((double) totalElements / pageable.getPageSize());
    response.setTotalPages(totalPages);

    return ResponseEntity.ok(response);
  }

  @Operation(summary = "Search hotels", description = "Returns a page of hotels that match the search value.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Hotels found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageResponse.class))),
      @ApiResponse(responseCode = "404", description = "Hotels not found")
  })
  @JsonView(HotelDetails.class)
  @GetMapping("/hotels/specific")
  public ResponseEntity<PageResponse<Hotel>> indexSearch(@RequestParam String searchValue, Pageable pageable) {
    try {
      Page<Hotel> hotels = hotelService.findAllByManager_ValidatedAndNameContainingIgnoreCaseOrderByNameDesc(
          true,
          searchValue, pageable);
      if (hotels.hasContent()) {
        PageResponse<Hotel> response = new PageResponse<>();
        response.setContent(hotels.getContent());
        response.setPageNumber(hotels.getNumber());
        response.setPageSize(hotels.getSize());
        response.setTotalElements(hotels.getTotalElements());
        response.setTotalPages(hotels.getTotalPages());

        return ResponseEntity.ok(response);
      } else {
        return ResponseEntity.notFound().build();
      }
    } catch (NoSuchElementException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @Operation(summary = "Search hotels", description = "Returns a page of hotels that match the search value.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Hotels found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PageResponse.class))),
      @ApiResponse(responseCode = "404", description = "Hotels not found")
  })
  @JsonView(HotelDetails.class)
  @GetMapping("/hotels/specific/all")
  public ResponseEntity<List<Hotel>> indexSearchAll(@RequestParam String searchValue) {
    try {
      List<Hotel> hotels = hotelService.findAllByManager_ValidatedAndNameContainingIgnoreCaseOrderByNameDesc(
          true,
          searchValue);
      return ResponseEntity.ok(hotels);

    } catch (NoSuchElementException e) {
      return ResponseEntity.notFound().build();
    }
  }

  /*
   * @JsonView(HotelDetails.class)
   * 
   * @GetMapping("/hotels/size")
   * public ResponseEntity<Integer> hotelsSize() {
   * try {
   * Integer size = hotelService.findAll().size();
   * return ResponseEntity.ok(size);
   * 
   * } catch (NoSuchElementException e) {
   * return ResponseEntity.notFound().build();
   * }
   * }
   */
}
