# BUILD STAGE
FROM maven:3.8.6-amazoncorretto-17 AS build
COPY producer-service/src /home/producer-service/src
COPY producer-service/pom.xml /home/producer-service
COPY hive-shared/src /home/hive-shared/src
COPY hive-shared/pom.xml /home/hive-shared
RUN mvn -f /home/hive-shared/pom.xml clean install
RUN mvn -f /home/producer-service/pom.xml clean package -Dmaven.test.skip=true

# PACKAGE STAGE
FROM openjdk:17-jdk-slim
COPY --from=build /home/producer-service/target/producer-service-0.0.1-SNAPSHOT.jar /usr/local/lib/producer-service.jar
ENTRYPOINT ["java","-jar","/usr/local/lib/producer-service.jar"]
