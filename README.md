# Spring-cloud-microservice

Design microservice architecture with Spring Cloud (Eureka Server, Eureka Client, API Gateway, FeignClient, Cloud Config Server, Histryx)

Eureka Server: Configure Eureka Server to register services.

API Gateway: Automatic Routing

Circuit Breaker: To handle cascading failure.

Load Balancer: Route distributes traffic to the available services. 

FeignClient: For service-to-service communication.

Rate Limiting: Limit the number of calls from a user to an endpoint.

Retry: Retry API calls if there is a failure.

Cloud Config Server: To fetch credentials from the external server. 
