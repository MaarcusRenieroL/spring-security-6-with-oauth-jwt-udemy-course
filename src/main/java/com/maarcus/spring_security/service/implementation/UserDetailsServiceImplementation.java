package com.maarcus.spring_security.service.implementation;

import com.maarcus.spring_security.model.User;
import com.maarcus.spring_security.repository.UserRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImplementation implements UserDetailsService {

  @Autowired private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> optionalUser = userRepository.findByUserName(username);

    if (optionalUser.isPresent()) {
      return UserDetailsImplementation.build(optionalUser.get());
    }

    throw new UsernameNotFoundException("User not found with the username " + username);
  }
}
