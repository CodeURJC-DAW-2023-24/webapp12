package yourHOmeTEL.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import yourHOmeTEL.model.Hotel;
import yourHOmeTEL.model.UserE;

@Service
public class ImageService {

	@Autowired HotelService hotelService;

	@Autowired UserService userService;

	private static final Path FILES_FOLDER = Paths.get(System.getProperty("user.dir"), "uploaded-images");
	
	public String getFilesFolder() {
		return FILES_FOLDER.toString();
	}

	private Path createFilePath(long imageId, Path folder, String type) {
		return folder.resolve("image-" + type  + imageId + ".jpg");
	}

	public Path saveUserImage(long imageId, MultipartFile image) throws IOException {
		Files.createDirectories(FILES_FOLDER);
		Path newFile = createFilePath(imageId, FILES_FOLDER, "user");
		image.transferTo(newFile);
		return newFile;
	}

	public Path saveHotelImage(long imageId, MultipartFile image) throws IOException {

		Files.createDirectories(FILES_FOLDER);
		Path newFile = createFilePath(imageId, FILES_FOLDER, "hotel");
		image.transferTo(newFile);
		return newFile;
	}

	public ResponseEntity<Object> createDownlaodResponseFromUserImage(long userId)
			throws MalformedURLException, SQLException {

				Optional<UserE> user = userService.findById(userId);
				if (user.isPresent() && user.get().getImageFile() != null) {
		
					Resource file = new InputStreamResource(user.get().getImageFile().getBinaryStream());
					return ResponseEntity.ok().
					header(HttpHeaders.CONTENT_TYPE, "image/jpg")
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=image-user" + userId + ".jpg")		
					.contentLength(user.get().getImageFile().length())
					.body(file);
		
				} else {
					return ResponseEntity.notFound().build();
				}
		}

	public ResponseEntity<Object> createResponseFromUserImage(long userId)
			throws MalformedURLException, SQLException {

				Optional<UserE> user = userService.findById(userId);
				if (user.isPresent() && user.get().getImageFile() != null) {
		
					Resource file = new InputStreamResource(user.get().getImageFile().getBinaryStream());
					return ResponseEntity.ok().
					header(HttpHeaders.CONTENT_TYPE, "image/jpg")
					.contentLength(user.get().getImageFile().length())
					.body(file);
		
				} else {
					return ResponseEntity.notFound().build();
				}
		}

		public ResponseEntity<Object> createDownlaodResponseFromHotelImage(long hotelId)
		throws MalformedURLException, SQLException {

			Optional<Hotel> hotel = hotelService.findById(hotelId);
			if (hotel.isPresent() && hotel.get().getImageFile() != null) {
	
				Resource file = new InputStreamResource(hotel.get().getImageFile().getBinaryStream());
				return ResponseEntity.ok().
				header(HttpHeaders.CONTENT_TYPE, "image/jpg")
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=image-hotel" + hotelId + ".jpg")		
				.contentLength(hotel.get().getImageFile().length())
				.body(file);
	
			} else {
				return ResponseEntity.notFound().build();
			}
}

	public ResponseEntity<Object> createResponseFromHotelImage(long hotelId)
			throws MalformedURLException, SQLException {

				Optional<Hotel> hotel = hotelService.findById(hotelId);
				if (hotel.isPresent() && hotel.get().getImageFile() != null) {
		
					Resource file = new InputStreamResource(hotel.get().getImageFile().getBinaryStream());
					return ResponseEntity.ok().
					header(HttpHeaders.CONTENT_TYPE, "image/jpg")
					.contentLength(hotel.get().getImageFile().length())
					.body(file);
		
				} else {
					return ResponseEntity.notFound().build();
				}
	}

	public Path deleteUserImage(String folderName, UserE user) throws IOException {
		String imagePath = user.getImagePath();
		String defaultImagePath = FILES_FOLDER.resolve("default-user.jpg").toString();
		if (defaultImagePath.startsWith("/")) {
			defaultImagePath = defaultImagePath.substring(1);
		}
		if (imagePath.startsWith(FILES_FOLDER.toString()) && !imagePath.equals(defaultImagePath)) {
			Path imageFile = Paths.get(imagePath);
			Files.deleteIfExists(imageFile);
		}	
		return Paths.get(defaultImagePath);
	}

	public Path deleteHotelImage(String folderName, Hotel hotel) throws IOException {
		String imagePath = hotel.getImagePath();
		String defaultImagePath = FILES_FOLDER.resolve("default-hotel.jpg").toString();
		if (defaultImagePath.startsWith("/")) {
			defaultImagePath = defaultImagePath.substring(1);
		}
		if (imagePath.startsWith(FILES_FOLDER.toString()) && !imagePath.equals(defaultImagePath)) {
			Path imageFile = Paths.get(imagePath);
			Files.deleteIfExists(imageFile);
		}	
		return Paths.get(defaultImagePath);
	}
		
	}

