package com.maarcus.spring_security.service.implementation;

import com.maarcus.spring_security.model.AuditLog;
import com.maarcus.spring_security.repository.AuditLogRepository;
import com.maarcus.spring_security.service.AuditLogService;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class AuditLogServiceImplementation implements AuditLogService {

  private final AuditLogRepository auditLogRepository;

  public AuditLogServiceImplementation(AuditLogRepository auditLogRepository) {
    this.auditLogRepository = auditLogRepository;
  }

  public AuditLog createAuditLog(AuditLog auditLog) {
    return auditLogRepository.save(auditLog);
  }

  public List<AuditLog> getAllAuditLogs() {
    return auditLogRepository.findAll();
  }

  public AuditLog getAuditLogById(Long auditLogId) {
    Optional<AuditLog> optionalAuditLog = auditLogRepository.findById(auditLogId);

    return optionalAuditLog.orElse(null);
  }

  public List<AuditLog> getAuditLogsByUserId(Long userId) {
    return auditLogRepository.findByUserId(userId);
  }

  public List<AuditLog> getAuditLogsByUsername(String username) {
    return auditLogRepository.findByUsername(username);
  }

  public List<AuditLog> getAuditLogsByEmail(String email) {
    return auditLogRepository.findByEmail(email);
  }

  public List<AuditLog> getAuditLogsByAction(String action) {
    return auditLogRepository.findByAction(action);
  }

  public List<AuditLog> getAuditLogsByLogMethod(String logMethod) {
    return auditLogRepository.findByLogMethod(logMethod);
  }

  public AuditLog deleteAuditLog(Long id) {
    Optional<AuditLog> optionalAuditLog = auditLogRepository.findById(id);

    if (optionalAuditLog.isEmpty()) {
      return null;
    }

    auditLogRepository.delete(optionalAuditLog.get());

    return optionalAuditLog.get();
  }
}
