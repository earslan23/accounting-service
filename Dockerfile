FROM openjdk:17-alpine
EXPOSE 8283
ARG JAR_FILE=target/accounting-service-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
