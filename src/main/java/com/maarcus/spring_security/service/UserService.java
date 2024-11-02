package com.maarcus.spring_security.service;

import com.maarcus.spring_security.model.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
  void updateUserRole(Long userId, String roleName);

  List<User> getAllUsers();

  Optional<User> getUserById(Long userId);
}
