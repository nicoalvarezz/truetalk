# BUILD STAGE
FROM maven:3.8.6-amazoncorretto-17 AS build
COPY post-service/src /home/post-service/src
COPY post-service/pom.xml /home/post-service
COPY hive-shared/src /home/hive-shared/src
COPY hive-shared/pom.xml /home/hive-shared
RUN mvn -f /home/hive-shared/pom.xml clean install
RUN mvn -f /home/post-service/pom.xml clean package -Dmaven.test.skip=true

# PACKAGE STAGE
FROM openjdk:17-jdk-slim
COPY --from=build /home/post-service/target/post-service-0.0.1-SNAPSHOT.jar /usr/local/lib/post-service.jar
ENTRYPOINT ["java","-jar","/usr/local/lib/post-service.jar"]
