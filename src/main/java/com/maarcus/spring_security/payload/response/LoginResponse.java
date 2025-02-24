package com.maarcus.spring_security.payload.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
  private String token;
  private String username;
  private List<String> roles;
}
