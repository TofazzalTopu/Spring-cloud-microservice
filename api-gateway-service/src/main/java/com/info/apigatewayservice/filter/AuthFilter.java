package com.info.apigatewayservice.filter;

import com.info.apigatewayservice.util.AuthUtil;
import com.info.apigatewayservice.util.JWTUtil;
import com.info.apigatewayservice.validator.RouteValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import io.jsonwebtoken.Claims;

@Component
@RefreshScope
public class AuthFilter implements GatewayFilter {

    public static final Logger logger = LoggerFactory.getLogger(AuthFilter.class);

    @Autowired
    RouteValidator routeValidator;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private AuthUtil authUtil;

    @Value("${authentication.enabled}")
    private boolean authEnabled;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (!authEnabled) {
            System.out.println("Authentication is disabled. To enable it, make \"authentication.enabled\" property as true");
            return chain.filter(exchange);
        }
        String token = "";
        ServerHttpRequest request = exchange.getRequest();

        if (routeValidator.isSecured.test(request)) {
            System.out.println("validating authentication token");
            if (this.isCredsMissing(request)) {
                System.out.println("Credentials missing");
                logger.error(HttpStatus.UNAUTHORIZED + ", Credentials missing");
                return this.onError(exchange, "Credentials missing", HttpStatus.UNAUTHORIZED);
            }
            if (request.getHeaders().containsKey("userName") && request.getHeaders().containsKey("role")) {
                token = authUtil.getToken(request.getHeaders().get("userName").toString(), request.getHeaders().get("role").toString());
            } else {
                token = request.getHeaders().get("Authorization").toString().split(" ")[1];
            }

            if (jwtUtil.isInvalid(token)) {
                logger.error(HttpStatus.UNAUTHORIZED + " Auth header invalid");
                return this.onError(exchange, "Auth header invalid", HttpStatus.UNAUTHORIZED);
            } else {
                logger.error(HttpStatus.OK + ", Authentication is successful for the user: " + request.getHeaders().get("userName"));
                System.out.println("Authentication is successful");
            }

            this.populateRequestWithHeaders(exchange, token);
        }
        return chain.filter(exchange);
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    private String getAuthHeader(ServerHttpRequest request) {
        return request.getHeaders().getOrEmpty("Authorization").get(0);
    }


    private boolean isCredsMissing(ServerHttpRequest request) {
        return !(request.getHeaders().containsKey("userName") && request.getHeaders().containsKey("role")) && !request.getHeaders().containsKey("Authorization");
    }

    private void populateRequestWithHeaders(ServerWebExchange exchange, String token) {
        Claims claims = jwtUtil.getALlClaims(token);
        exchange.getRequest()
                .mutate()
                .header("id", String.valueOf(claims.get("id")))
                .header("role", String.valueOf(claims.get("role")))
                .build();
    }
}
