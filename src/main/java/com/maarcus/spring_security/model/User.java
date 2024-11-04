package com.maarcus.spring_security.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
    name = "users",
    uniqueConstraints = {
      @UniqueConstraint(columnNames = "username"),
      @UniqueConstraint(columnNames = "email")
    })
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userId;

  @NotBlank
  @Size(max = 20)
  @Column(name = "username")
  private String userName;

  @NotBlank
  @Column(name = "email")
  private String email;

  @NotBlank
  @Size(max = 100)
  @Column(name = "password")
  @JsonIgnore
  private String password;

  private boolean accountNonLocked = false;
  private boolean accountNonExpired = false;
  private boolean credentialsNonExpired = false;
  private boolean enabled = true;

  private LocalDate credentialsExpiryDate;
  private LocalDate accountExpiryDate;

  private String twoFactorSecret;
  private boolean isTwoFactorEnabled = false;
  private String signUpMethod;

  @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
  @JoinColumn(name = "role_id", referencedColumnName = "role_id")
  @JsonBackReference
  @ToString.Exclude
  private Role role;

  @CreationTimestamp
  @Column(updatable = false)
  private LocalDateTime createdDate;

  @UpdateTimestamp private LocalDateTime updatedDate;

  public User(String userName, String password, String email) {
    this.userName = userName;
    this.password = password;
    this.email = email;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof User)) {
      return false;
    }

    return userId != null && userId.equals(((User) o).getUserId());
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
