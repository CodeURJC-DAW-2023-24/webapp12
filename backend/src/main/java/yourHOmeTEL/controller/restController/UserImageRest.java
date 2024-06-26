package yourHOmeTEL.controller.restController;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.sql.SQLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.NoSuchElementException;

import javax.sql.rowset.serial.SerialException;

import java.sql.Blob;
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
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.media.*;

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

    // USER IMAGE CONTROLLERS

    @Operation(summary = "Download user profile image")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User profile image downloaded successfully", content = @Content(mediaType = "image/*")),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/users/{id}/images")
    public ResponseEntity<Object> downloadProfileImage(HttpServletRequest request, @PathVariable long id)
            throws MalformedURLException, SQLException {
        return imgService.createDownlaodResponseFromUserImage(id);
    }

    @Operation(summary = "Returns user profile image")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User profile image downloaded successfully", content = @Content(mediaType = "image/*")),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/users/{id}/image")
    public ResponseEntity<Object> getProfileImage(HttpServletRequest request, @PathVariable long id)
            throws MalformedURLException, SQLException {
        return imgService.createResponseFromUserImage(id);
    }

    @Operation(summary = "Upload user profile image")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User profile image uploaded successfully", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Operation only allowed for the user or an admin", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/users/{id}/image")
    public ResponseEntity<Object> uploadImage(HttpServletRequest request, @PathVariable long id,
            @RequestParam MultipartFile imageFile) throws IOException, SQLException, SerialException {
        try {
            UserE requestUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
            UserE foundUser = userService.findById(id).orElseThrow();
            if (requestUser.getRols().contains("ADMIN") || foundUser.equals(requestUser)) {
                String loc = "/api/users/" + id + "/image";
                URI location = URI.create(loc);
                Blob blob = new javax.sql.rowset.serial.SerialBlob(imageFile.getBytes());
                foundUser.setImageFile(blob);
                Path newFilePath = imgService.saveUserImage(foundUser.getId(), imageFile);
                foundUser.setImagePath(newFilePath.toString());
                userService.save(foundUser);              
                return ResponseEntity.created(location).build();

            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete user profile image")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User profile image deleted successfully", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Operation only allowed for the user or an admin", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/users/{id}/image")
    public ResponseEntity<Object> deleteImage(HttpServletRequest request, @PathVariable long id)
            throws IOException, SQLException, SerialException {
        try {
            UserE requestUser = userService.findByNick(request.getUserPrincipal().getName()).orElseThrow();
            UserE foundUser = userService.findById(id).orElseThrow();
            if (requestUser.getRols().contains("ADMIN") || requestUser.equals(foundUser)) {
                Path defaultImagePath = Paths.get(imgService.getFilesFolder(), "default-user.jpg");
                byte[] defaultImageBytes = Files.readAllBytes(defaultImagePath);
                Blob defaultImageBlob = new javax.sql.rowset.serial.SerialBlob(defaultImageBytes);
                foundUser.setImageFile(defaultImageBlob);
                Path newFilePath = imgService.deleteUserImage(imgService.getFilesFolder(), foundUser);
                foundUser.setImagePath(newFilePath.toString());
                userService.save(foundUser);
                
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
