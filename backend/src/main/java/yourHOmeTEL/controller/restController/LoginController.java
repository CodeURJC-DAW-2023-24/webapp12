package yourHOmeTEL.controller.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import yourHOmeTEL.security.jwt.AuthResponse;
import yourHOmeTEL.security.jwt.LoginRequest;
import yourHOmeTEL.security.jwt.UserLoginService;
import yourHOmeTEL.security.jwt.AuthResponse.Status;

@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private UserLoginService userService;

    @Operation(summary = "User login through form")
    @ApiResponses(value = {
            @ApiResponse(
                responseCode = "200",
                description = "User logged in successfully"
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Bad request"
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Unauthorized"
            ),
            @ApiResponse(
                responseCode = "403",
                description = "Forbidden"
            )
    })
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<AuthResponse> loginForm(
            @CookieValue(name = "accessToken", required = false) String accessToken,
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            @ModelAttribute LoginRequest loginRequest) {
        return userService.login(loginRequest, accessToken, refreshToken);
    }

    @Operation(summary = "User login through JSON")
    @ApiResponses(value = {
            @ApiResponse(
                responseCode = "200",
                description = "User logged in successfully"
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Bad request"
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Unauthorized"
            ),
            @ApiResponse(
                responseCode = "403",
                description = "Forbidden"
            )
    })
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthResponse> loginJson(
            @CookieValue(name = "accessToken", required = false) String accessToken,
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            @RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest, accessToken, refreshToken);
    }

    @Operation(summary = "Checks if the user logged in is the same as the user in the token")
    @ApiResponses(value = {
            @ApiResponse(
                responseCode = "200",
                description = "User is logged in"
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Bad request"
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Unauthorized"
            ),
            @ApiResponse(
                responseCode = "403",
                description = "Forbidden"
            )
    })
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(
            @CookieValue(name = "refreshToken", required = false) String refreshToken) {
        return userService.refresh(refreshToken);
    }

    @Operation(summary = "Logs out the user")
    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logOut(HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.ok(new AuthResponse(Status.SUCCESS, userService.logout(request, response)));
    }

}
