# Dockerfile
FROM openjdk:17-jdk

WORKDIR /app

COPY ./build/libs/*.jar /app/api-gateway.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/api-gateway.jar"]