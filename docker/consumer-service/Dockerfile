# BUILD STAGE
FROM maven:3.8.6-amazoncorretto-17 AS build
COPY consumer-service/src /home/consumer-service/src
COPY consumer-service/pom.xml /home/consumer-service
COPY hive-shared/src /home/hive-shared/src
COPY hive-shared/pom.xml /home/hive-shared
RUN mvn -f /home/hive-shared/pom.xml clean install
RUN mvn -f /home/consumer-service/pom.xml clean package -Dmaven.test.skip=true

# PACKAGE STAGE
FROM openjdk:17-jdk-slim
COPY --from=build /home/consumer-service/target/consumer-service-0.0.1-SNAPSHOT.jar /usr/local/lib/consumer-service.jar
ENTRYPOINT ["java","-jar","/usr/local/lib/consumer-service.jar"]
