FROM openjdk:17-jdk-slim

ENV JDBC_DATABASE_URL='jdbc:mysql://mssalesDB/salesDB?createDatabaseIfNotExist=true&autoReconnect=true&serverTimeZone=GMT-3'
ENV JDBC_DATABASE_USERNAME=root
ENV JDBC_DATABASE_PASSWORD=admin

ARG JAR_FILE=target/ms-sales-0.0.1.jar
COPY ${JAR_FILE} ms-sales.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "ms-sales.jar"]