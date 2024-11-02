package com.maarcus.spring_security.repository;

import com.maarcus.spring_security.model.AppRole;
import com.maarcus.spring_security.model.Role;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByRoleName(AppRole appRole);
}
