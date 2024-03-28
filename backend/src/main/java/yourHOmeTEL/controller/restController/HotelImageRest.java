package yourHOmeTEL.controller.restController;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import yourHOmeTEL.model.Hotel;
import yourHOmeTEL.model.UserE;
import yourHOmeTEL.service.HotelService;
import yourHOmeTEL.service.ImageService;
import yourHOmeTEL.service.UserService;

@RestController
@RequestMapping("/api")
public class HotelImageRest {

    @Autowired
    private ImageService imgService;

    @Autowired
    private UserService userService;

    @Autowired
    private HotelService hotelService;

    // USER IMAGE CONTROLLERS

    @GetMapping("/hotels/{id}/image")
    @Operation(summary = "Download hotel image", description = "Download the image of a specific hotel by hotel ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image retrieved successfully", content = @Content(mediaType = "image/*")),
            @ApiResponse(responseCode = "404", description = "Hotel not found")
    })
    public ResponseEntity<Object> downloadProfileImage(HttpServletRequest request, @PathVariable long id)
            throws MalformedURLException {
        return imgService.createResponseFromImage(imgService.getFilesFolder(), id);
    }

    @PostMapping("hotels/{id}/image")
    @Operation(summary = "Upload hotel image", description = "Upload an image for a specific hotel by hotel ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Image uploaded successfully", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Hotel not found")
    })
    public ResponseEntity<Object> uploadImage(HttpServletRequest request, @PathVariable long id,
            @RequestParam MultipartFile imageFile) throws IOException {
        try {
            UserE requestUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
            Hotel targetHotel = hotelService.findById(id).orElseThrow();
            UserE hotelManager = targetHotel.getManager();
            if (requestUser.getRols().contains("ADMIN") ||
                    (hotelManager.equals(requestUser) && hotelManager.getRols().contains("MANAGER"))) {
                URI location = fromCurrentRequest().build().toUri();
                targetHotel.setImagePath(location.toString());
                hotelService.save(targetHotel);
                imgService.saveImage(imgService.getFilesFolder(), targetHotel.getId(), imageFile);
                return ResponseEntity.created(location).build();

            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/hotels/{id}/image")
    @Operation(summary = "Delete hotel image", description = "Delete the image of a specific hotel by hotel ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Image deleted successfully", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Hotel not found")
    })
    public ResponseEntity<Object> deleteImage(HttpServletRequest request, @PathVariable long id)
            throws IOException {
        try {
            UserE requestUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
            Hotel targetHotel = hotelService.findById(id).orElseThrow();
            UserE hotelManager = targetHotel.getManager();
            if (requestUser.getRols().contains("ADMIN") ||
                    (hotelManager.equals(requestUser) && hotelManager.getRols().contains("MANAGER"))) {
                targetHotel.setImagePath(null);
                hotelService.save(targetHotel);
                this.imgService.deleteImage(imgService.getFilesFolder(), id);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
