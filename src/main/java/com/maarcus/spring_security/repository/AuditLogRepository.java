package com.maarcus.spring_security.repository;

import com.maarcus.spring_security.model.AuditLog;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
  List<AuditLog> findByUserId(Long id);

  List<AuditLog> findByUsername(String username);

  List<AuditLog> findByEmail(String email);

  List<AuditLog> findByAction(String action);

  List<AuditLog> findByLogMethod(String logMethod);
}
