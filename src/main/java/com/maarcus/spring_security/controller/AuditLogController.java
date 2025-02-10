package com.maarcus.spring_security.controller;

import com.maarcus.spring_security.model.AuditLog;
import com.maarcus.spring_security.service.AuditLogService;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/audit-logs")
public class AuditLogController {

  private final AuditLogService auditLogService;

  public AuditLogController(AuditLogService auditLogService) {
    this.auditLogService = auditLogService;
  }

  @PostMapping("/")
  public AuditLog createAuditLog(AuditLog auditLog) {
    return auditLogService.createAuditLog(auditLog);
  }
  ;

  @GetMapping("/")
  public List<AuditLog> getAllAuditLogs() {
    return auditLogService.getAllAuditLogs();
  }
  ;

  @GetMapping("/{auditLogId}")
  public AuditLog getAuditLogById(@PathVariable Long auditLogId) {
    return auditLogService.getAuditLogById(auditLogId);
  }
  ;

  @GetMapping("/user/{userId}")
  public List<AuditLog> getAuditLogsByUserId(@PathVariable Long userId) {
    return auditLogService.getAuditLogsByUserId(userId);
  }
  ;

  @GetMapping("/user-name/{username}")
  public List<AuditLog> getAuditLogsByUsername(@PathVariable String username) {
    return auditLogService.getAuditLogsByUsername(username);
  }
  ;

  @GetMapping("/email/{email}")
  public List<AuditLog> getAuditLogsByEmail(@PathVariable String email) {
    return auditLogService.getAuditLogsByEmail(email);
  }
  ;

  @GetMapping("/action/{action}")
  public List<AuditLog> getAuditLogsByAction(String action) {
    return auditLogService.getAuditLogsByAction(action);
  }
  ;

  @GetMapping("/log-method/{logMethod}")
  public List<AuditLog> getAuditLogsByLogMethod(String logMethod) {
    return auditLogService.getAuditLogsByLogMethod(logMethod);
  }
  ;

  @DeleteMapping
  public AuditLog deleteAuditLog(Long id) {
    return auditLogService.deleteAuditLog(id);
  }
  ;
}
