# BUILD STAGE
FROM maven:3.8.6-amazoncorretto-17 AS build
COPY alethia-service/src /home/alethia-service/src
COPY alethia-service/pom.xml /home/alethia-service
COPY hive-shared/src /home/hive-shared/src
COPY hive-shared/pom.xml /home/hive-shared
RUN mvn -f /home/hive-shared/pom.xml clean install
RUN mvn -f /home/alethia-service/pom.xml clean package -Dmaven.test.skip=true

# PACKAGE STAGE
FROM openjdk:17-jdk-slim
COPY --from=build /home/alethia-service/target/alethia-service-0.0.1-SNAPSHOT.jar /usr/local/lib/alethia-service.jar
ENTRYPOINT ["java","-jar","/usr/local/lib/alethia-service.jar"]
