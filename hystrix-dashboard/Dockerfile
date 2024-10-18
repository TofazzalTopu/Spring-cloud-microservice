FROM openjdk:11
ADD target/hystrix-dashboard.jar hystrix-dashboard.jar
ENTRYPOINT ["java", "-jar", "hystrix-dashboard.jar"]
EXPOSE 9095