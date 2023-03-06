# BUILD STAGE
FROM maven:3.8.6-amazoncorretto-17 AS build
COPY user-service/src /home/user-service/src
COPY user-service/pom.xml /home/user-service
COPY hive-shared/src /home/hive-shared/src
COPY hive-shared/pom.xml /home/hive-shared
RUN mvn -f /home/hive-shared/pom.xml clean install
RUN mvn -f /home/user-service/pom.xml clean package -Dmaven.test.skip=true

# PACKAGE STAGE
FROM openjdk:17-jdk-slim
COPY --from=build /home/user-service/target/user-service-0.0.1-SNAPSHOT.jar /usr/local/lib/user-service.jar
ENTRYPOINT ["java","-jar","/usr/local/lib/user-service.jar"]
