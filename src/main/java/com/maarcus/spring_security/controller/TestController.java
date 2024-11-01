package com.maarcus.spring_security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

  @GetMapping("/hello")
  public String hello() {
    return "Hello";
  }

  @GetMapping("/hi")
  public String hi() {
    return "Hi";
  }

  @GetMapping("/contact")
  public String sayContact() {
    return "Contact";
  }
}
