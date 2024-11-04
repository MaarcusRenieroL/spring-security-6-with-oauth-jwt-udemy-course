package com.maarcus.spring_security.payload.response;

import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserInfoResponse {

  private Long id;
  private String username;
  private String email;
  private boolean accountNonLocked;
  private boolean accountNonExpired;
  private boolean credentialsNonExpired;
  private boolean enabled;
  private LocalDate credentialsExpiryDate;
  private LocalDate accountExpiryDate;
  private boolean isTwoFactorEnabled;
  private List<String> roles;
}
