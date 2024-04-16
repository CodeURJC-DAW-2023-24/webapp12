package yourHOmeTEL.controller.restController;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.NoSuchElementException;

import javax.sql.rowset.serial.SerialException;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.hibernate.engine.jdbc.BlobProxy;
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

import java.sql.Blob;

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

    @GetMapping("/hotels/{id}/imagePath")
    @Operation(summary = "Get hotel image path", description = "Get the image path for a specific hotel by hotel ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image path retrieved successfully", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Hotel not found")
    })
    public ResponseEntity<String> getImagePath(HttpServletRequest request, @PathVariable long id) {
        try {
            UserE requestUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
            Hotel targetHotel = hotelService.findById(id).orElseThrow();
            UserE hotelManager = targetHotel.getManager();
            if (requestUser.getRols().contains("ADMIN") ||
                    (hotelManager.equals(requestUser) && hotelManager.getRols().contains("MANAGER"))) {

                String imagePath = "/api/hotels/" + id + "/image";
                return ResponseEntity.ok(imagePath);

            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Download hotel image", description = "Download the image of a specific hotel by hotel ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image retrieved successfully", content = @Content(mediaType = "image/*")),
            @ApiResponse(responseCode = "404", description = "Hotel not found")
    })

    @GetMapping("/hotels/{id}/images")
    public ResponseEntity<Object> downloadProfileImage(HttpServletRequest request, @PathVariable long id)
            throws MalformedURLException, SQLException {
        return imgService.createDownlaodResponseFromHotelImage(id);
    }

    @Operation(summary = "Returns hotel image", description = "Download the image of a specific hotel by hotel ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image retrieved successfully", content = @Content(mediaType = "image/*")),
            @ApiResponse(responseCode = "404", description = "Hotel not found")
    })

    @GetMapping("/hotels/{id}/image")
    public ResponseEntity<Object> getProfileImage(HttpServletRequest request, @PathVariable long id)
            throws MalformedURLException, SQLException {
        return imgService.createResponseFromHotelImage(id);
    }

    @PostMapping("hotels/{id}/image")
    @Operation(summary = "Upload hotel image", description = "Upload an image for a specific hotel by hotel ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Image uploaded successfully", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Forbidden operation", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Hotel not found")
    })
    public ResponseEntity<Object> uploadImage(HttpServletRequest request, @PathVariable long id,
            @RequestParam  MultipartFile imageFile)
            throws IOException, SQLException, SerialException {
        try {
            UserE requestUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
            Hotel targetHotel = hotelService.findById(id).orElseThrow();
            UserE hotelManager = targetHotel.getManager();
            if (requestUser.getRols().contains("ADMIN") ||
                    (hotelManager.equals(requestUser) && hotelManager.getRols().contains("MANAGER"))) {

                String loc = "/api/hotels/" + id + "/image";
                URI location = URI.create(loc);
                if(!imageFile.isEmpty()){
                    Blob blob = new javax.sql.rowset.serial.SerialBlob(imageFile.getBytes());
                    targetHotel.setImageFile(blob);
                    Path newFilePath = imgService.saveHotelImage(targetHotel.getId(), imageFile);
                    targetHotel.setImagePath(newFilePath.toString());
                }else{
                    Resource image = new ClassPathResource("/static/images/default-hotel.jpg");
                    targetHotel.setImageFile(BlobProxy.generateProxy(image.getInputStream(), image.contentLength()));
                    targetHotel.setImage(true);

                }
                hotelService.save(targetHotel);
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
            throws IOException, SQLException, SerialException {
        try {
            UserE requestUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
            Hotel targetHotel = hotelService.findById(id).orElseThrow();
            UserE hotelManager = targetHotel.getManager();
            if (requestUser.getRols().contains("ADMIN") ||
                    (hotelManager.equals(requestUser) && hotelManager.getRols().contains("MANAGER"))) {
                Path defaultImagePath = Paths.get(imgService.getFilesFolder(), "default-hotel.jpg");
                byte[] defaultImageBytes = Files.readAllBytes(defaultImagePath);
                Blob defaultImageBlob = new javax.sql.rowset.serial.SerialBlob(defaultImageBytes);
                targetHotel.setImageFile(defaultImageBlob);
                Path newFilePath = imgService.deleteHotelImage(imgService.getFilesFolder(), targetHotel);
                targetHotel.setImagePath(newFilePath.toString());
                hotelService.save(targetHotel);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
