package com.maarcus.spring_security.service.implementation;

import com.maarcus.spring_security.model.AppRole;
import com.maarcus.spring_security.model.Role;
import com.maarcus.spring_security.model.User;
import com.maarcus.spring_security.repository.RoleRepository;
import com.maarcus.spring_security.repository.UserRepository;
import com.maarcus.spring_security.service.UserService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImplementation implements UserService {

  @Autowired private UserRepository userRepository;
  @Autowired private RoleRepository roleRepository;

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
  }

  @Override
  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  @Override
  public Optional<User> getUserById(Long userId) {
    return userRepository.findById(userId);
  }
}
