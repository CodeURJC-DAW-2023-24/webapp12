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

import jakarta.servlet.http.HttpServletRequest;
import yourHOmeTEL.model.UserE;
import yourHOmeTEL.service.ImageService;
import yourHOmeTEL.service.UserService;

@RestController
@RequestMapping("/api")
public class UserImageRest {

    @Autowired
    private ImageService imgService;

    @Autowired
    private UserService userService;

    //USER IMAGE CONTROLLERS

    @GetMapping("/users/{id}/image")
    public ResponseEntity<Object> downloadProfileImage(HttpServletRequest request, @PathVariable long id)
            throws MalformedURLException {
        return imgService.createResponseFromImage(imgService.getFilesFolder(), id);
    }

    @PostMapping("/users/{id}/image")
    public ResponseEntity<Object> uploadImage(HttpServletRequest request, @PathVariable long id,
            @RequestParam MultipartFile imageFile) throws IOException {
        try {
            UserE requestUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
            UserE foundUser = userService.findById(id).orElseThrow();
            if (requestUser.getRols().contains("ADMIN") || foundUser.equals(requestUser)) {          
                URI location = fromCurrentRequest().build().toUri();
                foundUser.setImagePath(location.toString());
                userService.save(foundUser);
                imgService.saveImage(imgService.getFilesFolder(), foundUser.getId(), imageFile);
                return ResponseEntity.created(location).build();

            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/users/{id}/image")
    public ResponseEntity<Object> deleteImage(HttpServletRequest request, @PathVariable long id)
            throws IOException {
        try {
            UserE requestUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
            UserE foundUser = userService.findById(id).orElseThrow();
            if(requestUser.getRols().contains("ADMIN") || requestUser.equals(foundUser)){
                foundUser.setImagePath(null);
                userService.save(foundUser);
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
