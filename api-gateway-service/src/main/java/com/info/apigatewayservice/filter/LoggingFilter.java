package com.info.apigatewayservice.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

//centralize logging for micro-services
@Component
public class LoggingFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        logger.info("Centralize Logging Request: Method: {}, URL: {}, Headers: {}, Body: {}", exchange.getRequest().getMethod(), exchange.getRequest().getURI(),  exchange.getRequest().getHeaders(), exchange.getRequest().getBody());
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            try {
                logger.info("Centralize Logging Response: Response: Method: {},  URI: {}, Response: {}, Headers: {}, Status: {}", exchange.getRequest().getMethod(), exchange.getRequest().getURI(), new ObjectMapper().writeValueAsString(exchange.getResponse()), exchange.getResponse().getHeaders(), exchange.getResponse().getStatusCode());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }));
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
