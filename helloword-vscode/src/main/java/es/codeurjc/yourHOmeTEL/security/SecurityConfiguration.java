package es.codeurjc.yourHOmeTEL.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import es.codeurjc.yourHOmeTEL.service.UserSecurityService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private UserSecurityService userDetailService;
 
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
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authenticationProvider(authenticationProvider());
        http
            .authorizeHttpRequests(authorize -> authorize
                // PUBLIC PAGES
                .requestMatchers("/login").permitAll()
                .requestMatchers("/loginerror").permitAll()
                .requestMatchers("/css/**").permitAll()
                .requestMatchers("/js/**").permitAll()
                .requestMatchers("/fonts/**").permitAll()
                .requestMatchers("/images/**").permitAll()
                .requestMatchers("/img/**").permitAll()
                .requestMatchers("/index").permitAll()
                .requestMatchers("/register").permitAll()
                
                // PRIVATE PAGES
                .requestMatchers("/profile").hasAnyRole("USER")
                .requestMatchers("/editprofile").hasAnyRole("USER")
                .requestMatchers("/clientreservation").hasAnyRole("USER")
                .requestMatchers("/hotelinformation").hasAnyRole("USER")
                .requestMatchers("/hotelreview").hasAnyRole("USER")
                .requestMatchers("/edithotel").hasAnyRole("MANAGER")
                .requestMatchers("/viewhotelsmanager").hasAnyRole("MANAGER")
                .requestMatchers("/clientlist").hasAnyRole("MANAGER")
                .requestMatchers("/managerlist").hasAnyRole("ADMIN")
            )
            .formLogin(formLogin -> formLogin
                .loginPage("/login")
                .failureUrl("/loginerror")
                .defaultSuccessUrl("/profile")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .permitAll()
            );
        
        // Disable CSRF at the moment
        http.csrf(csrf -> csrf.disable());

        return  http.build();
    }

}
