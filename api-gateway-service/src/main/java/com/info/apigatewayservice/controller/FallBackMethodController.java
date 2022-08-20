package com.info.apigatewayservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class FallBackMethodController {

   @GetMapping(value = "/userServiceFallBack")
   public String userServiceFallBackMethod(){
      return "User service is taking longer time then expected. Please try again later";
   }

   @GetMapping(value = "/departmentServiceFallBack")
   public String departmentServiceFallBackMethod(){
      return "Department service is taking longer time then expected. Please try again later";
   }
}
