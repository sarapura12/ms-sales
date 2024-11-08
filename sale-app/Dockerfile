FROM openjdk:17-jdk-slim

ARG JAR_FILE=target/ms-sales-0.0.1.jar
COPY ${JAR_FILE} ms-sales.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "ms-sales.jar"]