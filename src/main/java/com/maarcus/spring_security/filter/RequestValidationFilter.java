package com.maarcus.spring_security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class RequestValidationFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String header = request.getHeader("X-Valid-Request");

    if (header == null || !header.equals("true")) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Request");
      return;
    }

    filterChain.doFilter(request, response);
  }
}
