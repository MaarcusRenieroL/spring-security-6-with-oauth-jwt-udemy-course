package com.maarcus.spring_security.service.implementation;

import com.maarcus.spring_security.model.AppRole;
import com.maarcus.spring_security.model.AuditLog;
import com.maarcus.spring_security.model.Role;
import com.maarcus.spring_security.model.User;
import com.maarcus.spring_security.repository.RoleRepository;
import com.maarcus.spring_security.repository.UserRepository;
import com.maarcus.spring_security.service.AuditLogService;
import com.maarcus.spring_security.service.UserService;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImplementation implements UserService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final AuditLogService auditLogService;

  public UserServiceImplementation(
      UserRepository userRepository,
      RoleRepository roleRepository,
      AuditLogService auditLogService) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.auditLogService = auditLogService;
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
}
