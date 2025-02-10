package com.maarcus.spring_security.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AuditLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String logMethod;

  private String action;

  private Long userId;

  private String email;

  private String username;

  public AuditLog(String logMethod, String action, Long userId, String email, String username) {
    this.logMethod = logMethod;
    this.action = action;
    this.userId = userId;
    this.email = email;
    this.username = username;
  }
}
