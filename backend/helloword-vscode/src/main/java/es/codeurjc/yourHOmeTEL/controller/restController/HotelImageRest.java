package es.codeurjc.yourHOmeTEL.controller.restController;

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

import es.codeurjc.yourHOmeTEL.model.Hotel;
import es.codeurjc.yourHOmeTEL.model.UserE;
import es.codeurjc.yourHOmeTEL.service.HotelService;
import es.codeurjc.yourHOmeTEL.service.ImageService;
import es.codeurjc.yourHOmeTEL.service.UserService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class HotelImageRest {

    @Autowired
    private ImageService imgService;

    @Autowired
    private UserService userService;

    @Autowired
    private HotelService hotelService;
    

    //USER IMAGE CONTROLLERS

    @GetMapping("/hotels/{id}/image")
    public ResponseEntity<Object> downloadProfileImage(HttpServletRequest request, @PathVariable long id)
            throws MalformedURLException {
        return imgService.createResponseFromImage(imgService.getFilesFolder(), id);
    }

    @PostMapping("hotels/{id}/image/creation")
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

    @DeleteMapping("/hotels/{id}/image/removal")
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
