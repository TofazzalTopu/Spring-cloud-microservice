package com.info.apigatewayservice.filter;

import com.info.apigatewayservice.model.Company;
import com.info.apigatewayservice.model.Department;
import com.info.apigatewayservice.model.Student;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class RequestFilter implements GatewayFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        Object body = exchange.getAttribute("cachedRequestBodyObject");
        System.out.println("in request filter");
        if(body instanceof Student) {
            System.out.println("Student:=> " + (Student) body);
        }
        else if(body instanceof Company) {
            System.out.println("Company:=> " + (Company) body);
        } else if(body instanceof Department) {
            System.out.println("Department:=> " + (Department) body);
        }
        return chain.filter(exchange);
    }
}
