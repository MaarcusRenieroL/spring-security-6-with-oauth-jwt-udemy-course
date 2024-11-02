package com.maarcus.spring_security.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Role {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "role_id")
  private Long roleId;

  @ToString.Exclude
  @Enumerated(EnumType.STRING)
  @Column(length = 20, name = "role_name")
  private AppRole roleName;

  @OneToMany(mappedBy = "role", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JsonBackReference
  @ToString.Exclude
  private Set<User> users = new HashSet<>();

  public Role(AppRole roleName) {
    this.roleName = roleName;
  }
}
