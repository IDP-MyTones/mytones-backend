FROM openjdk:17

COPY target/mytones-core.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]