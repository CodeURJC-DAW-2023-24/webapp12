package yourHOmeTEL.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import yourHOmeTEL.security.jwt.JwtRequestFilter;
import yourHOmeTEL.security.jwt.UnauthorizedHandlerJwt;
import yourHOmeTEL.service.UserSecurityService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private UserSecurityService userDetailService;

    @Autowired
	private JwtRequestFilter jwtRequestFilter;
    
    @Autowired
  	private UnauthorizedHandlerJwt unauthorizedHandlerJwt;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

    @Bean
	@Order(1)
	public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
		
		http.authenticationProvider(authenticationProvider());
		
		http
			.securityMatcher("/api/**")
			.exceptionHandling(handling -> handling.authenticationEntryPoint(unauthorizedHandlerJwt));

        http
			.authorizeHttpRequests(authorize -> authorize
                    
                    //ADMIN AND MANAGER ENDPOINTS
                    .requestMatchers(HttpMethod.GET,"/api/users/managers/list").hasAnyRole("MANAGER","ADMIN")

                    .requestMatchers(HttpMethod.POST,"/api/hotels/{id}").hasAnyRole("MANAGER","ADMIN")

                    .requestMatchers(HttpMethod.POST,"/api/rooms/hotels/{hotelId}/create").hasAnyRole("MANAGER","ADMIN")
                    .requestMatchers(HttpMethod.PUT,"/api/rooms/{roomId}/hotels/{hotelId}/update").hasAnyRole("MANAGER","ADMIN")
                    .requestMatchers(HttpMethod.DELETE,"/api/rooms/{roomId}/hotels/{hotelId}/removal").hasAnyRole("MANAGER","ADMIN")


                    //ADMIN AND USER ENDPOINTS
                    .requestMatchers(HttpMethod.PUT,"/api/reviews/{reviewId}/users/{userId}/update").hasAnyRole("CLIENT","ADMIN")
                    .requestMatchers(HttpMethod.DELETE,"/api/reviews/{reviewId}/users/{userId}/removal").hasAnyRole("CLIENT","ADMIN")

                    .requestMatchers(HttpMethod.GET,"/api/reservations/{id}").hasAnyRole("CLIENT","ADMIN")
                    .requestMatchers(HttpMethod.PUT,"/api/reservations/{reservationId}/users/{userId}/hotels/{hotelId}/rooms/{roomId}/update").hasAnyRole("CLIENT","ADMIN")
                    .requestMatchers(HttpMethod.PUT,"/api/reservations/{reservationId}/users/{userId}/hotels/{hotelId}/rooms/{roomId}/removal").hasAnyRole("CLIENT","ADMIN")

                    //ADMIN ENDPOINTS
                    .requestMatchers(HttpMethod.GET,"/api/managers/validation").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.POST,"/api/users/{id}/managers/rejection").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.POST,"/api/users/{id}/managers/validation").hasRole("ADMIN")
                
                    //MANAGER ENDPOINTS

                    //MANAGER AND CLIENT ENDPOINTS
                    
                                
					//USER ENDPOINTS
                    .requestMatchers(HttpMethod.GET,"/api/users/{id}/info").hasRole("USER")
                    .requestMatchers(HttpMethod.PUT,"/api/users/{id}/info/update").hasRole("USER")
                    .requestMatchers(HttpMethod.DELETE,"/api/users/{id}/info/removal").hasRole("USER")

                    .requestMatchers(HttpMethod.GET,"/api/hotels/{id}").hasRole("USER")

                    .requestMatchers(HttpMethod.GET,"/api/reservations/users/{id}").hasRole("USER")
                    .requestMatchers(HttpMethod.GET,"/api/reservations/hotels/{id}").hasRole("USER")
                    .requestMatchers(HttpMethod.GET,"/api/reservations/rooms/{id}").hasRole("USER")

                    .requestMatchers(HttpMethod.GET,"/api/rooms/{id}").hasRole("USER")
                    .requestMatchers(HttpMethod.GET,"/api/rooms/hotels/{id}").hasRole("USER")
                    .requestMatchers(HttpMethod.GET,"/api/rooms/reservations/{id}").hasRole("USER")

                    //USER IMAGE ENDPOINTS
                    .requestMatchers(HttpMethod.GET,"/api/users/{id}/image").hasAnyRole("USER", "ADMIN")
                    .requestMatchers(HttpMethod.POST,"/api/users/{id}/image/creation").hasAnyRole("USER", "ADMIN")
                    .requestMatchers(HttpMethod.DELETE,"/api/users/{id}/image/removal").hasAnyRole("USER", "ADMIN")
                    
                    //HOTEL IMAGE ENDPOINTS
                    .requestMatchers(HttpMethod.GET,"/api/hotels/{id}/image").hasAnyRole("USER","ADMIN")
                    .requestMatchers(HttpMethod.POST,"/api/hotels/{id}/image/creation").hasAnyRole("USER","ADMIN")
                    .requestMatchers(HttpMethod.PUT,"/api/hotels/{id}/image/removal").hasAnyRole("USER","ADMIN")
                    
                    //CLIENT ENDPOINTS
                    .requestMatchers(HttpMethod.POST,"/api/reservations/users/{userId}/hotels/{hotelId}/rooms/{roomId}/creation").hasRole("CLIENT")

                    .requestMatchers(HttpMethod.POST,"/api/reviews/users/{userId}/hotels/{hotelId}/create").hasRole("CLIENT")
                    
                    // PUBLIC ENDPOINTS
                    .requestMatchers(HttpMethod.POST,"/api/users/register").permitAll()
                    .requestMatchers(HttpMethod.GET,"/api/reviews/{id}").permitAll()
                    .requestMatchers(HttpMethod.GET,"/api/reviews/users/{id}").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/reviews/hotels/{id}").permitAll()
                    .anyRequest().permitAll()

                    
			);
		
        // Disable Form login Authentication
        http.formLogin(formLogin -> formLogin.disable());

        // Disable CSRF protection (it is difficult to implement in REST APIs)
        http.csrf(csrf -> csrf.disable());

        // Disable Basic Authentication
        http.httpBasic(httpBasic -> httpBasic.disable());

        // Stateless session
        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		// Add JWT Token filter
		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

    @Bean
    @Order(2)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authenticationProvider(authenticationProvider());
        http
                .authorizeHttpRequests(authorize -> authorize
                        // public pages
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/loginerror").permitAll()
                        .requestMatchers("/css/**").permitAll()
                        .requestMatchers("/js/**").permitAll()
                        .requestMatchers("/fonts/**").permitAll()
                        .requestMatchers("/images/**").permitAll()
                        .requestMatchers("/img/**").permitAll()
                        .requestMatchers("/index").permitAll()
                        .requestMatchers("/register").permitAll()
                        .requestMatchers("/nickTaken").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/notfounderror/**").permitAll()
                        .requestMatchers("/hotelReviews/**").permitAll()
                        .requestMatchers("/hotelinformation/**").permitAll()
                        .requestMatchers("indexsearch").permitAll()
                        .requestMatchers("/notRooms/**").permitAll()
                        .requestMatchers("/indexsearch").permitAll()
                        .requestMatchers("/returnmainpage").permitAll()
                        .requestMatchers("/captcha").permitAll()
                        .requestMatchers("/loadMoreHotels/**").permitAll()
                        .requestMatchers("/loadMoreReviews/**").permitAll()
                        .requestMatchers("/profile/*/images/").permitAll()
                        .requestMatchers("/static/images/**").permitAll()
                        .requestMatchers("/index/*/images/**").permitAll()

                        // User pages
                        .requestMatchers("/profile/**").hasAnyRole("USER")
                        .requestMatchers("/editprofile/**").hasAnyRole("USER")
                        .requestMatchers("/editprofile/*/images").hasAnyRole("USER")
                        .requestMatchers("/editprofileimage/**").hasAnyRole("USER")
                        .requestMatchers("/posthotelReviews/**").hasAnyRole("USER")
                        .requestMatchers("/replace/**").hasAnyRole("USER")

                        // Client pages
                        .requestMatchers("/clientreservations/**").hasAnyRole("CLIENT")
                        .requestMatchers("/reservationInfo/**").hasAnyRole("CLIENT")
                        .requestMatchers("/addReservation/**").hasAnyRole("CLIENT")
                        .requestMatchers("/cancelReservation/**").hasAnyRole("CLIENT")
                        .requestMatchers("/posthotelReviews/**").hasAnyRole("CLIENT")
                        .requestMatchers("/loadMoreReservations/**").hasAnyRole("CLIENT")

                        // Manager pages
                        .requestMatchers("/edithotel/**").hasAnyRole("MANAGER")
                        .requestMatchers("/viewhotelsmanager").hasAnyRole("MANAGER")
                        .requestMatchers("/deleteHotel/**").hasAnyRole("MANAGER")
                        .requestMatchers("/clientlist/**").hasAnyRole("MANAGER")
                        .requestMatchers("/chartsmanager").hasAnyRole("MANAGER")
                        .requestMatchers("/addHotel/**").hasAnyRole("MANAGER")
                        .requestMatchers("/testChart/**").hasAnyRole("MANAGER")
                        .requestMatchers("/application/**").hasAnyRole("MANAGER")
                        .requestMatchers("/index/*/images/**").hasAnyRole("MANAGER")
                        .requestMatchers("/edithotelimage/**").hasAnyRole("MANAGER")
                        .requestMatchers("/addHotelPhoto/**").hasAnyRole("MANAGER")
                        .requestMatchers("/selecthotelimage/**").hasAnyRole("MANAGER")
                        .requestMatchers("/createHotel/**").hasAnyRole("MANAGER")
                        .requestMatchers("/chartsManager").hasAnyRole("MANAGER")
                        .requestMatchers("/addHotel").hasAnyRole("MANAGER")
                        .requestMatchers("/index/*/images/**").hasAnyRole("MANAGER")
                        .requestMatchers("/edithotelimage/**").hasAnyRole("MANAGER")
                        .requestMatchers("/loadMoreHotelsManagerView/**").hasAnyRole("MANAGER")

                        // Admin pages
                        .requestMatchers("/managerlist").hasAnyRole("ADMIN")
                        .requestMatchers("/chartsadmin").hasAnyRole("ADMIN")
                        .requestMatchers("/managervalidation").hasAnyRole("ADMIN")
                        .requestMatchers("/acceptance/**").hasAnyRole("ADMIN")
                        .requestMatchers("/rejection/**").hasAnyRole("ADMIN")

                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")
                        .failureUrl("/loginerror")
                        .defaultSuccessUrl("/profile")
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .permitAll());

        return http.build();
    }

}
