package com.maarcus.spring_security.controller;

import com.maarcus.spring_security.model.User;
import com.maarcus.spring_security.service.UserService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
  private final UserService userService;

  public AdminController(UserService userService) {
    this.userService = userService;
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @GetMapping("/get-all-users")
  public ResponseEntity<List<User>> getAllUsers() {
    return ResponseEntity.ok().body(userService.getAllUsers());
  }

  @PutMapping("/update-role")
  public ResponseEntity<String> updateUserRole(
      @RequestParam Long userId, @RequestParam String role) {
    userService.updateUserRole(userId, role);
    return ResponseEntity.ok().body("User role updated");
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<User> getUserById(@PathVariable Long userId) {
    return ResponseEntity.ok()
        .body(
            userService
                .getUserById(userId)
                .orElseThrow(() -> new RuntimeException("User not found")));
  }
}
