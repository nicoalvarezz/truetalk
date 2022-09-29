#
# Build stage
#
FROM maven:3.8.6-amazoncorretto-17 AS build
COPY src /home/alethia/src
COPY pom.xml /home/alethia
RUN mvn -f /home/alethia/pom.xml clean package

#
# Package stage
#
FROM openjdk:17-jdk-slim
COPY --from=build /home/alethia/target/alethia-0.0.1-SNAPSHOT.jar /usr/local/lib/alethia.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/alethia.jar"]