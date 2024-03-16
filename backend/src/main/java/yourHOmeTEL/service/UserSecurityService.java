package yourHOmeTEL.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import yourHOmeTEL.model.UserE;
import yourHOmeTEL.repository.UserRepository;

@Service
public class UserSecurityService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserE user = userRepository.findByNick(username).orElseThrow();
    List<GrantedAuthority> roles = new ArrayList<>();
    for (String role : user.getRols()) {
      roles.add(new SimpleGrantedAuthority("ROLE_" + role));
    }
    return new org.springframework.security.core.userdetails.User(user.getNick(), user.getPass(), roles);
  }
}
