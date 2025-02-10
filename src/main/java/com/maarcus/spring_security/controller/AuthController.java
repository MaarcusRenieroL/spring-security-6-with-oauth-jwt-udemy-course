package com.maarcus.spring_security.controller;

import com.maarcus.spring_security.model.AppRole;
import com.maarcus.spring_security.model.AuditLog;
import com.maarcus.spring_security.model.Role;
import com.maarcus.spring_security.model.User;
import com.maarcus.spring_security.payload.request.LoginRequest;
import com.maarcus.spring_security.payload.request.SignUpRequest;
import com.maarcus.spring_security.payload.response.LoginResponse;
import com.maarcus.spring_security.payload.response.MessageResponse;
import com.maarcus.spring_security.repository.RoleRepository;
import com.maarcus.spring_security.repository.UserRepository;
import com.maarcus.spring_security.service.AuditLogService;
import com.maarcus.spring_security.service.UserService;
import com.maarcus.spring_security.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final JwtUtils jwtUtils;
  private final AuthenticationManager authenticationManager;
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuditLogService auditLogService;
  private final UserService userService;

  public AuthController(
      JwtUtils jwtUtils,
      AuthenticationManager authenticationManager,
      UserRepository userRepository,
      RoleRepository roleRepository,
      PasswordEncoder passwordEncoder,
      AuditLogService auditLogService,
      UserService userService) {
    this.jwtUtils = jwtUtils;
    this.authenticationManager = authenticationManager;
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.passwordEncoder = passwordEncoder;
    this.auditLogService = auditLogService;
    this.userService = userService;
  }

  @GetMapping("csrf-token")
  public CsrfToken getCsrfToken(HttpServletRequest request) {
    auditLogService.createAuditLog(new AuditLog("GET", "Get CSRF Token", null, null, null));
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

    auditLogService.createAuditLog(
        new AuditLog("POST", "Create User", user.getUserId(), user.getEmail(), user.getUserName()));
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

    auditLogService.createAuditLog(
        new AuditLog("POST", "Login", null, null, userDetails.getUsername()));
    return ResponseEntity.ok(response);
  }

  @PostMapping("/public/forgot-password")
  public ResponseEntity<?> forgotPassword(@RequestParam String email) {
    try {
      userService.generatePasswordResetToken(email);
      return ResponseEntity.ok(new MessageResponse("Password reset email sent!"));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new MessageResponse("Error sending password reset email"));
    }
  }
}
