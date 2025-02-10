package com.maarcus.spring_security.service.implementation;

import com.maarcus.spring_security.model.*;
import com.maarcus.spring_security.repository.PasswordResetTokenRepository;
import com.maarcus.spring_security.repository.RoleRepository;
import com.maarcus.spring_security.repository.UserRepository;
import com.maarcus.spring_security.service.AuditLogService;
import com.maarcus.spring_security.service.UserService;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImplementation implements UserService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final AuditLogService auditLogService;
  private final PasswordResetTokenRepository passwordResetTokenRepository;

  public UserServiceImplementation(
      UserRepository userRepository,
      RoleRepository roleRepository,
      AuditLogService auditLogService,
      PasswordResetTokenRepository passwordResetTokenRepository) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.auditLogService = auditLogService;
    this.passwordResetTokenRepository = passwordResetTokenRepository;
  }

  @Override
  public void updateUserRole(Long userId, String roleName) {
    User user =
        userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

    AppRole appRole = AppRole.valueOf(roleName);

    Role role =
        roleRepository
            .findByRoleName(appRole)
            .orElseThrow(() -> new RuntimeException("Role not found"));

    user.setRole(role);

    userRepository.save(user);

    auditLogService.createAuditLog(
        new AuditLog("UPDATE", "Update User Role", userId, user.getEmail(), user.getUserName()));
  }

  @Override
  public List<User> getAllUsers() {
    auditLogService.createAuditLog(new AuditLog("GET", "Get All Users", null, null, null));
    return userRepository.findAll();
  }

  @Override
  public Optional<User> getUserById(Long userId) {
    auditLogService.createAuditLog(new AuditLog("GET", "Get User By Id", null, null, null));
    return userRepository.findById(userId);
  }

  @Override
  public void generatePasswordResetToken(String email) {
    PasswordResetToken token =
        passwordResetTokenRepository.save(
            new PasswordResetToken(
                UUID.randomUUID().toString(),
                Instant.now().plus(24, ChronoUnit.HOURS),
                userRepository
                    .findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"))));
    String resetUrl = "http://localhost:8080/reset-password?token=" + token;
    // Send email to user
  }
}
