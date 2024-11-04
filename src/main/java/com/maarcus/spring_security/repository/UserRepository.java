package com.maarcus.spring_security.repository;

import com.maarcus.spring_security.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUserName(String userName);

  Optional<User> findByEmail(String email);
}
