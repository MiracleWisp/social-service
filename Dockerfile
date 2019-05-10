FROM openjdk:8-jdk-alpine

VOLUME /tmp

EXPOSE 8082

ARG JAR_FILE=target/social-service-1.0.jar
ADD ${JAR_FILE} social-service.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","social-service.jar"]