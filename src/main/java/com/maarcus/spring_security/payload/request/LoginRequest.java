package com.maarcus.spring_security.payload.request;

import lombok.Data;

@Data
public class LoginRequest {
  private String username;
  private String password;
}
