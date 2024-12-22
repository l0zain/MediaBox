FROM openjdk:21-jdk
COPY target/MediaBox-0.0.1-SNAPSHOT.jar application.jar
LABEL authors="naono"

ENTRYPOINT ["java", "-jar", "application.jar"]