package com.maarcus.spring_security.config;

import static org.springframework.security.config.Customizer.withDefaults;

import com.maarcus.spring_security.filter.CustomLoggingFilter;
import com.maarcus.spring_security.filter.RequestValidationFilter;
import com.maarcus.spring_security.model.AppRole;
import com.maarcus.spring_security.model.Role;
import com.maarcus.spring_security.model.User;
import com.maarcus.spring_security.repository.RoleRepository;
import com.maarcus.spring_security.repository.UserRepository;
import java.time.LocalDate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

  @Bean
  AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(
        (requests) ->
            requests
                .requestMatchers("/api/admin/**")
                .hasRole("ADMIN")
                .requestMatchers("/contact", "/api/auth/**")
                .permitAll()
                .requestMatchers("/hi")
                .denyAll()
                .anyRequest()
                .authenticated());
    // http.formLogin(withDefaults());
    http.csrf(
        csrf ->
            csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .ignoringRequestMatchers("/api/auth"));
    http.addFilterBefore(new CustomLoggingFilter(), UsernamePasswordAuthenticationFilter.class);
    http.addFilterAfter(new RequestValidationFilter(), CustomLoggingFilter.class);
    http.sessionManagement(
        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    http.httpBasic(withDefaults());
    return http.build();
  }

  @Bean
  CommandLineRunner initData(
      RoleRepository roleRepository,
      UserRepository userRepository,
      PasswordEncoder passwordEncoder) {
    return args -> {
      Role userRole =
          roleRepository
              .findByRoleName(AppRole.ROLE_USER)
              .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_USER)));
      Role adminRole =
          roleRepository
              .findByRoleName(AppRole.ROLE_ADMIN)
              .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_ADMIN)));

      if (userRepository.findByUserName("user1").isEmpty()) {
        User user1 = new User("user1", passwordEncoder.encode("password1"), "user1@example.com");
        user1.setAccountNonLocked(false);
        user1.setAccountNonExpired(true);
        user1.setCredentialsNonExpired(true);
        user1.setEnabled(true);
        user1.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
        user1.setAccountExpiryDate(LocalDate.now().plusYears(1));
        user1.setTwoFactorEnabled(false);
        user1.setSignUpMethod("email");
        user1.setRole(userRole);
        userRepository.save(user1);
      }
      if (userRepository.findByUserName("admin").isEmpty()) {
        User admin = new User("admin", passwordEncoder.encode("adminPass"), "admin@example.com");
        admin.setAccountNonLocked(true);
        admin.setAccountNonExpired(true);
        admin.setCredentialsNonExpired(true);
        admin.setEnabled(true);
        admin.setCredentialsExpiryDate(LocalDate.now().plusYears(1));
        admin.setAccountExpiryDate(LocalDate.now().plusYears(1));
        admin.setTwoFactorEnabled(false);
        admin.setSignUpMethod("email");
        admin.setRole(adminRole);
        userRepository.save(admin);
      }
    };
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
