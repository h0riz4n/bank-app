FROM amazoncorretto:21

ENV TZ=Europe/Moscow

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} account-service.jar

ENTRYPOINT [ "java", "-jar", "-Dspring.profiles.active=${SPRING_PROFILE}", "/account-service.jar"]
