# Dockerfile
FROM openjdk:17-jdk

WORKDIR /app

COPY ./build/libs/*.jar /app/owner-microservice.jar

EXPOSE 8082

CMD ["java", "-jar", "/app/owner-microservice.jar"]