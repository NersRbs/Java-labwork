# Dockerfile
FROM openjdk:17-jdk

WORKDIR /app

COPY ./build/libs/*.jar /app/cat-microservice.jar

EXPOSE 8081

CMD ["java", "-jar", "/app/cat-microservice.jar"]