package com.maarcus.spring_security.controller;

import com.maarcus.spring_security.model.AppRole;
import com.maarcus.spring_security.model.Role;
import com.maarcus.spring_security.model.User;
import com.maarcus.spring_security.payload.request.LoginRequest;
import com.maarcus.spring_security.payload.request.SignUpRequest;
import com.maarcus.spring_security.payload.response.LoginResponse;
import com.maarcus.spring_security.payload.response.MessageResponse;
import com.maarcus.spring_security.repository.RoleRepository;
import com.maarcus.spring_security.repository.UserRepository;
import com.maarcus.spring_security.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired private JwtUtils jwtUtils;
  @Autowired AuthenticationManager authenticationManager;
  @Autowired private UserRepository userRepository;
  @Autowired private RoleRepository roleRepository;
  @Autowired private PasswordEncoder passwordEncoder;

  @GetMapping("csrf-token")
  public CsrfToken getCsrfToken(HttpServletRequest request) {
    return (CsrfToken) request.getAttribute(CsrfToken.class.getName());
  }

  @PostMapping("/sign-up")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {

    if (userRepository.findByUserName(signUpRequest.getUsername()).isPresent()) {
      return ResponseEntity.badRequest()
          .body(new MessageResponse("Error: Username is already taken!"));
    }

    if (userRepository.findByEmail(signUpRequest.getEmail()).isPresent()) {
      return ResponseEntity.badRequest()
          .body(new MessageResponse("Error: Email is already in use!"));
    }

    User user =
        new User(
            signUpRequest.getUsername(),
            passwordEncoder.encode(signUpRequest.getPassword()),
            signUpRequest.getEmail());
    Set<String> strRoles = signUpRequest.getRole();
    Role role;

    if (strRoles == null || strRoles.isEmpty()) {
      role =
          roleRepository
              .findByRoleName(AppRole.ROLE_USER)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
    } else {
      String roleStr = strRoles.iterator().next();

      if (roleStr.equals("admin")) {
        role =
            roleRepository
                .findByRoleName(AppRole.ROLE_ADMIN)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
      } else {
        role =
            roleRepository
                .findByRoleName(AppRole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
      }

      user.setAccountNonLocked(true);
      user.setAccountNonExpired(true);
      user.setCredentialsNonExpired(true);
      user.setEnabled(true);
      user.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
      user.setAccountExpiryDate(LocalDate.now().plusYears(1));
      user.setTwoFactorEnabled(false);
      user.setSignUpMethod("email");
    }

    user.setRole(role);
    userRepository.save(user);

    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }

  @PostMapping("/sign-in")
  public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
    Authentication authentication;
    try {
      authentication =
          authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                  loginRequest.getUsername(), loginRequest.getPassword()));
    } catch (AuthenticationException exception) {

      Map<String, Object> map = new HashMap<>();

      map.put("Message", "Bad credentials");
      map.put("Status", false);

      return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
    }

    SecurityContextHolder.getContext().setAuthentication(authentication);

    UserDetails userDetails = (UserDetails) authentication.getPrincipal();

    String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);
    List<String> roles =
        userDetails.getAuthorities().stream()
            .map(item -> item.getAuthority())
            .collect(Collectors.toList());

    LoginResponse response = new LoginResponse(jwtToken, userDetails.getUsername(), roles);
    return ResponseEntity.ok(response);
  }
}
