package com.info.apigatewayservice.config;

import com.info.apigatewayservice.filter.AuthFilter;
import com.info.apigatewayservice.filter.PostGlobalFilter;
import com.info.apigatewayservice.filter.RequestFilter;
import com.info.apigatewayservice.model.Company;
import com.info.apigatewayservice.model.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.WebFilter;


@Configuration
public class GatewayConfig {

    public static final Logger logger = LoggerFactory.getLogger(GatewayConfig.class);
    @Autowired
    RequestFilter requestFilter;

    @Autowired
    AuthFilter authFilter;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        // adding 2 rotes to first microservice as we need to log request body if method is POST
        return builder.routes()
                .route("first-microservice", r -> r.path("/first")
                        .and().method("POST")
                        .and().readBody(Student.class, s -> true).filters(f -> f.filters(requestFilter, authFilter))
                        .uri("lb://first-microservice"))
                .route("first-microservice", r -> r.path("/first")
                        .and().method("GET").filters(f -> f.filters(authFilter))
                        .uri("http://localhost:8081"))
                .route("second-microservice", r -> r.path("/second")
                        .and().method("POST")
                        .and().readBody(Company.class, s -> true).filters(f -> f.filters(requestFilter, authFilter))
                        .uri("http://localhost:8084"))

                .route("second-microservice", r -> r.path("/second")
                        .and().method("GET").filters(f -> f.filters(authFilter))
                        .uri("http://localhost:8084"))
                .route("DEPARTMENT-SERVICE", r -> r.path("/departments/*")
                        .filters(f -> f.filters(authFilter)
                                .circuitBreaker(c-> c.setFallbackUri("forward:/departmentServiceFallBack")))
                        .uri("lb://DEPARTMENT-SERVICE"))
                .route("KUBERNETES-SERVICE", r -> r.path("/kubernetes/*")
                        .filters(f -> f.filters(authFilter))
                        .uri("lb://KUBERNETES-SERVICE"))
                .route("auth-server", r -> r.path("/login")
                        .uri("http://localhost:8087"))
                .build();
    }

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public WebFilter responseFilter() {
        return new PostGlobalFilter();
    }

}
