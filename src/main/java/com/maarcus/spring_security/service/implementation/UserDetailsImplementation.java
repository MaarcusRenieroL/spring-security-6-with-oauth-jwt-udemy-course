package com.maarcus.spring_security.service.implementation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.maarcus.spring_security.model.User;
import java.util.Collection;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@NoArgsConstructor
@Data
public class UserDetailsImplementation implements UserDetails {
  private static final long serialVersionUID = 1L;

  private Long userId;
  private String userName;
  private String email;

  @JsonIgnore private String password;

  private boolean isTwoFactorEnabled;

  private Collection<? extends GrantedAuthority> authorities;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return userName;
  }

  public UserDetailsImplementation(
      Long userId,
      String userName,
      String email,
      String password,
      Collection<? extends GrantedAuthority> authorities,
      boolean isTwoFactorEnabled) {
    this.userId = userId;
    this.userName = userName;
    this.email = email;
    this.password = password;
    this.authorities = authorities;
    this.isTwoFactorEnabled = isTwoFactorEnabled;
  }

  public static UserDetailsImplementation build(User user) {
    GrantedAuthority authority =
        new SimpleGrantedAuthority(user.getRole().getRoleName().toString());

    return new UserDetailsImplementation(
        user.getUserId(),
        user.getUserName(),
        user.getEmail(),
        user.getPassword(),
        List.of(authority),
        user.isTwoFactorEnabled());
  }
}
