package com.maarcus.spring_security.service;

import com.maarcus.spring_security.model.AuditLog;
import java.util.List;

public interface AuditLogService {
  AuditLog createAuditLog(AuditLog auditLog);

  List<AuditLog> getAllAuditLogs();

  AuditLog getAuditLogById(Long auditLogId);

  List<AuditLog> getAuditLogsByUserId(Long userId);

  List<AuditLog> getAuditLogsByUsername(String username);

  List<AuditLog> getAuditLogsByEmail(String email);

  List<AuditLog> getAuditLogsByAction(String action);

  List<AuditLog> getAuditLogsByLogMethod(String logMethod);

  AuditLog deleteAuditLog(Long id);
}
