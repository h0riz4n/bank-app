FROM amazoncorretto:21-alpine-jdk

ENV TZ=Europe/Moscow

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} api-gateway.jar

ENTRYPOINT [ "java", "-jar", "-Dspring.profiles.active=${SPRING_PROFILE}", "api-gateway.jar"]
