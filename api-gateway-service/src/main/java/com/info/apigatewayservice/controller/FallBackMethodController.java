package com.info.apigatewayservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class FallBackMethodController {
   private static final Logger logger = LoggerFactory.getLogger(FallBackMethodController.class);

   @GetMapping(value = "/userServiceFallBack")
   public String userServiceFallBackMethod(){
      logger.info("\n====User service is taking longer time then expected. Please try again later");
      return "User service is taking longer time then expected. Please try again later";
   }

   @GetMapping(value = "/departmentServiceFallBack")
   public String departmentServiceFallBackMethod(){
      logger.info("\n===Department service is taking longer time then expected. Please try again later");
      return "Department service is taking longer time then expected. Please try again later";
   }
}
