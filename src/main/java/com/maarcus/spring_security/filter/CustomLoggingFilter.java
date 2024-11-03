package com.maarcus.spring_security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class CustomLoggingFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws IOException, ServletException {
    System.out.println("CustomLoggingFilter -> Request URI: " + request.getRequestURI());
    filterChain.doFilter(request, response);
    System.out.println("CustomLoggingFilter -> Response Status: " + response.getStatus());
  }
}
